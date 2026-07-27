import { pgTable, bigserial, varchar, timestamp, date, boolean } from 'drizzle-orm/pg-core';

export const conductores = pgTable('conductores', {
  id: bigserial('id', { mode: 'number' }).primaryKey(),
  nombres: varchar('nombres', { length: 100 }).notNull(),
  apellidos: varchar('apellidos', { length: 100 }).notNull(),
  cedula: varchar('cedula', { length: 10 }).notNull().unique(),
  numeroLicencia: varchar('numero_licencia', { length: 30 }).notNull().unique(),
  tipoLicencia: varchar('tipo_licencia', { length: 10 }).notNull(),
  fechaVencimientoLicencia: date('fecha_vencimiento_licencia').notNull(),
  telefono: varchar('telefono', { length: 20 }),
  email: varchar('email', { length: 255 }),
  estado: varchar('estado', { length: 20 }).notNull().default('ACTIVO'),
  activo: boolean('activo').notNull().default(true),
  creadoEn: timestamp('creado_en', { withTimezone: true }).notNull().defaultNow(),
  actualizadoEn: timestamp('actualizado_en', { withTimezone: true }).notNull().defaultNow(),
});

export const usuarios = pgTable('usuarios', {
  id: bigserial('id', { mode: 'number' }).primaryKey(),
  nombre: varchar('nombre', { length: 100 }).notNull(),
  email: varchar('email', { length: 255 }).notNull().unique(),
  passwordHash: varchar('password_hash', { length: 255 }).notNull(),
  rol: varchar('rol', { length: 30 }).notNull().default('ROLE_COORDINADOR'),
  activo: boolean('activo').notNull().default(true),
  creadoEn: timestamp('creado_en', { withTimezone: true }).notNull().defaultNow(),
  actualizadoEn: timestamp('actualizado_en', { withTimezone: true }).notNull().defaultNow(),
});