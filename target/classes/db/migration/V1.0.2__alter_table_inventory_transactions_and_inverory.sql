ALTER TABLE inventory ALTER COLUMN current_average_cost TYPE decimal(14,4);
ALTER TABLE inventory_transactions ALTER COLUMN unit_value TYPE decimal(14,4);
ALTER TABLE inventory_transactions ALTER COLUMN cmv TYPE decimal(14,4);