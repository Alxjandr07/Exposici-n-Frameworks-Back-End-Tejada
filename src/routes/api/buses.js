import { db } from '~/db';
import { buses } from '~/db/schema';

// GET /api/buses - lista todos los buses
export async function GET() {
  const listaBuses = await db.select().from(buses);
  return new Response(JSON.stringify(listaBuses), {
    headers: { 'Content-Type': 'application/json' },
  });
}

// POST /api/buses - crea un bus nuevo
export async function POST({ request }) {
  const body = await request.json();

  const nuevoBus = await db.insert(buses).values({
    placa: body.placa,
    modelo: body.modelo,
    capacidad: body.capacidad,
    estado: body.estado || 'activo',
  }).returning();

  return new Response(JSON.stringify(nuevoBus[0]), {
    status: 201,
    headers: { 'Content-Type': 'application/json' },
  });
}