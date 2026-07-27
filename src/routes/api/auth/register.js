import { db } from '~/db';
import { usuarios } from '~/db/schema';
import { eq } from 'drizzle-orm';
import bcrypt from 'bcryptjs';

function errorResponse(mensaje, status = 400) {
  return new Response(JSON.stringify({ error: mensaje }), {
    status,
    headers: { 'Content-Type': 'application/json' },
  });
}

export async function POST({ request }) {
  try {
    const body = await request.json();

    if (!body.nombre?.trim() || body.nombre.length > 100)
      return errorResponse('El nombre es obligatorio (max 100 caracteres)');
    if (!body.email?.trim() || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(body.email))
      return errorResponse('Email invalido');
    if (!body.password || body.password.length < 6)
      return errorResponse('La contrasena debe tener al menos 6 caracteres');

    const existente = await db
      .select()
      .from(usuarios)
      .where(eq(usuarios.email, body.email))
      .limit(1);

    if (existente.length > 0)
      return errorResponse('Ya existe un usuario con ese email');

    const rolesValidos = ['ROLE_ADMIN', 'ROLE_COORDINADOR', 'ROLE_SEGURIDAD'];
    const rol = body.rol?.toUpperCase() || 'ROLE_COORDINADOR';
    if (!rolesValidos.includes(rol))
      return errorResponse('Rol no valido');

    const ahora = new Date();
    const passwordHash = await bcrypt.hash(body.password, 10);

    const nuevo = await db.insert(usuarios).values({
      nombre: body.nombre.trim(),
      email: body.email.trim().toLowerCase(),
      passwordHash,
      rol,
      activo: true,
      creadoEn: ahora,
      actualizadoEn: ahora,
    }).returning();

    return new Response(JSON.stringify({
      id: nuevo[0].id,
      nombre: nuevo[0].nombre,
      email: nuevo[0].email,
      rol: nuevo[0].rol,
      activo: nuevo[0].activo,
      creadoEn: nuevo[0].creadoEn,
    }), {
      status: 201,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (err) {
    return errorResponse('Error interno del servidor', 500);
  }
}
