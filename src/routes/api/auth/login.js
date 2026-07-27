import { db } from '~/db';
import { usuarios } from '~/db/schema';
import { eq, and } from 'drizzle-orm';
import bcrypt from 'bcryptjs';
import crypto from 'crypto';

function errorResponse(mensaje, status = 401) {
  return new Response(JSON.stringify({ error: mensaje }), {
    status,
    headers: { 'Content-Type': 'application/json' },
  });
}

export async function POST({ request }) {
  try {
    const body = await request.json();

    if (!body.email || !body.password)
      return errorResponse('Email y contrasena son obligatorios');

    const usuario = await db
      .select()
      .from(usuarios)
      .where(and(eq(usuarios.email, body.email.toLowerCase().trim()), eq(usuarios.activo, true)))
      .limit(1);

    if (usuario.length === 0)
      return errorResponse('Credenciales invalidas');

    const valida = await bcrypt.compare(body.password, usuario[0].passwordHash);
    if (!valida)
      return errorResponse('Credenciales invalidas');

    const token = crypto.randomBytes(32).toString('hex');

    return new Response(JSON.stringify({
      token,
      tipo: 'Bearer',
      usuario: {
        id: usuario[0].id,
        nombre: usuario[0].nombre,
        email: usuario[0].email,
        rol: usuario[0].rol,
      },
    }), {
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (err) {
    return errorResponse('Error interno del servidor', 500);
  }
}
