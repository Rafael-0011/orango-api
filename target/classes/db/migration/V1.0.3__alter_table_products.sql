ALTER TABLE public.inventory
    ADD COLUMN IF NOT EXISTS status varchar(50),
    ADD COLUMN IF NOT EXISTS inventory_minimum REAL;