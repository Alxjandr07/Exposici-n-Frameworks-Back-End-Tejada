import { db } from '~/db';
import { conductores } from '~/db/schema';
import { eq, and } from 'drizzle-orm';

function calcularLicenciaPorVencer(fechaVencimiento) {
  const hoy = new Date();
  const vence = new Date(fechaVencimiento);
  const diff = vence.getTime() - hoy.getTime();
  const dias = Math.ceil(diff / (1000 * 60 * 60 * 24));
  return dias >= 0 && dias <= 30;
}

function mapearResponse(c) {
  return {
    id: c.id,
    nombres: c.nombres,
    apellidos: c.apellidos,
    cedula: c.cedula,
    numeroLicencia: c.numeroLicencia,
    tipoLicencia: c.tipoLicencia,
    fechaVencimientoLicencia: c.fechaVencimientoLicencia,
    telefono: c.telefono,
    email: c.email,
    estado: c.estado,
    activo: c.activo,
    licenciaPorVencer: calcularLicenciaPorVencer(c.fechaVencimientoLicencia),
    creadoEn: c.creadoEn,
    actualizadoEn: c.actualizadoEn,
  };
}

function errorResponse(mensaje, status = 400) {
  return new Response(JSON.stringify({ error: mensaje }), {
    status,
    headers: { 'Content-Type': 'application/json' },
  });
}

async function obtenerActivo(id) {
  const resultado = await db
    .select()
    .from(conductores)
    .where(and(eq(conductores.id, id), eq(conductores.activo, true)))
    .limit(1);

  if (resultado.length === 0) return null;
  return resultado[0];
}

export async function GET({ params }) {
  const id = params.id;
  const conductor = await obtenerActivo(id);

  if (!conductor)
    return errorResponse('Conductor no encontrado', 404);

  return new Response(JSON.stringify(mapearResponse(conductor)), {
    headers: { 'Content-Type': 'application/json' },
  });
}

export async function PUT({ request, params }) {
  try {
    const id = params.id;
    const conductor = await obtenerActivo(id);

    if (!conductor)
      return errorResponse('Conductor no encontrado', 404);

    const body = await request.json();

    if (!body.nombres?.trim() || body.nombres.length > 100)
      return errorResponse('Los nombres son obligatorios (max 100 caracteres)');
    if (!body.apellidos?.trim() || body.apellidos.length > 100)
      return errorResponse('Los apellidos son obligatorios (max 100 caracteres)');
    if (!/^\d{10}$/.test(body.cedula))
      return errorResponse('La cedula debe tener 10 digitos');
    if (!body.numeroLicencia?.trim() || body.numeroLicencia.length > 30)
      return errorResponse('El numero de licencia es obligatorio (max 30 caracteres)');
    if (!body.tipoLicencia?.trim() || body.tipoLicencia.length > 10)
      return errorResponse('El tipo de licencia es obligatorio (max 10 caracteres)');
    if (!body.fechaVencimientoLicencia)
      return errorResponse('La fecha de vencimiento de la licencia es obligatoria');
    if (!body.estado?.trim())
      return errorResponse('El estado es obligatorio');

    const estadosValidos = ['ACTIVO', 'INACTIVO', 'SUSPENDIDO'];
    if (!estadosValidos.includes(body.estado.toUpperCase()))
      return errorResponse('Estado no valido');

    if (body.cedula !== conductor.cedula) {
      const dup = await db
        .select()
        .from(conductores)
        .where(and(eq(conductores.cedula, body.cedula), eq(conductores.activo, true)))
        .limit(1);
      if (dup.length > 0)
        return errorResponse('Ya existe otro conductor con esa cedula');
    }

    if (body.numeroLicencia !== conductor.numeroLicencia) {
      const dup = await db
        .select()
        .from(conductores)
        .where(and(eq(conductores.numeroLicencia, body.numeroLicencia), eq(conductores.activo, true)))
        .limit(1);
      if (dup.length > 0)
        return errorResponse('Ya existe otro conductor con ese numero de licencia');
    }

    if (body.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(body.email))
      return errorResponse('El email debe tener un formato valido');
    if (body.telefono && body.telefono.length > 20)
      return errorResponse('El telefono no puede superar los 20 caracteres');

    const actualizado = await db
      .update(conductores)
      .set({
        nombres: body.nombres.trim(),
        apellidos: body.apellidos.trim(),
        cedula: body.cedula,
        numeroLicencia: body.numeroLicencia.trim(),
        tipoLicencia: body.tipoLicencia.trim().toUpperCase(),
        fechaVencimientoLicencia: body.fechaVencimientoLicencia,
        telefono: body.telefono?.trim() || null,
        email: body.email?.trim() || null,
        estado: body.estado.toUpperCase(),
        actualizadoEn: new Date(),
      })
      .where(eq(conductores.id, id))
      .returning();

    return new Response(JSON.stringify(mapearResponse(actualizado[0])), {
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (err) {
    return errorResponse('Error interno del servidor', 500);
  }
}

export async function DELETE({ params }) {
  const id = params.id;
  const conductor = await obtenerActivo(id);

  if (!conductor)
    return errorResponse('Conductor no encontrado', 404);

  await db
    .update(conductores)
    .set({
      activo: false,
      estado: 'INACTIVO',
      actualizadoEn: new Date(),
    })
    .where(eq(conductores.id, id));

  return new Response(null, { status: 204 });
}
