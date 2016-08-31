DROP INDEX IF EXISTS idx_bill_demand;
create index "idx_bill_demand" on eg_bill(id_demand);
DROP INDEX IF EXISTS idx_bill_cancelled;
create index "idx_bill_cancelled" on eg_bill(is_cancelled);
DROP INDEX IF EXISTS idx_bill_billtype;
create index "idx_bill_billtype" on eg_bill(id_bill_type);