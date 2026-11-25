ALTER TABLE public.inventory
    DROP COLUMN inventory_minimum;

ALTER TABLE public.products
    ADD COLUMN IF NOT EXISTS inventory_minimum REAL;