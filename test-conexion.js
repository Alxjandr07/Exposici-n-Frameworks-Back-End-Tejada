import 'dotenv/config';
import { Pool } from 'pg';

const pool = new Pool({
  connectionString: process.env.DATABASE_URL,
});

async function test() {
  try {
    const res = await pool.query('SELECT NOW()');
    console.log('✅ Conexión exitosa:', res.rows[0]);
  } catch (err) {
    console.error('❌ Error de conexión:', err.message);
  } finally {
    await pool.end();
  }
}

test();