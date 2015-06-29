ALTER TABLE eg_reason_category RENAME COLUMN order_id TO "order";

--ROLLBACK ALTER TABLE eg_reason_category RENAME COLUMN "order" TO order_id;
