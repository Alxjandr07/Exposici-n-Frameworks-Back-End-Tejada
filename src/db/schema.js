import { pgTable, serial, varchar, integer, timestamp } from 'drizzle-orm/pg-core';

export const buses = pgTable('buses', {
  id: serial('id').primaryKey(),
  placa: varchar('placa', { length: 10 }).notNull(),
  modelo: varchar('modelo', { length: 100 }).notNull(),
  capacidad: integer('capacidad').notNull(),
  estado: varchar('estado', { length: 20 }).default('activo'),
  creadoEn: timestamp('creado_en').defaultNow(),
});