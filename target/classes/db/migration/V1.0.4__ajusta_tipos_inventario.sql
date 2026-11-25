-- 1. Alterar colunas da tabela PRODUCTS
ALTER TABLE products
    ALTER COLUMN id SET DATA TYPE bigint;

-- 2. Alterar colunas da tabela INVENTORY
-- Nota: Como mudamos tipos e convertemos texto/real para formatos específicos, usamos USING
ALTER TABLE inventory
    ALTER COLUMN id SET DATA TYPE bigint,
ALTER COLUMN product_id SET DATA TYPE bigint,
    ALTER COLUMN current_average_cost SET DATA TYPE numeric(10,2),
    ALTER COLUMN inventory_minimum SET DATA TYPE numeric(38,2),
    ALTER COLUMN status SET DATA TYPE smallint USING (status::smallint);

-- 3. Alterar colunas da tabela INVENTORY_TRANSACTIONS
ALTER TABLE inventory_transactions
    ALTER COLUMN id SET DATA TYPE bigint,
ALTER COLUMN product_id SET DATA TYPE bigint,
    ALTER COLUMN unit_value SET DATA TYPE numeric(10,2),
    ALTER COLUMN cmv SET DATA TYPE numeric(12,2);
