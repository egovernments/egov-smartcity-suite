ALTER TABLE eg_demand_reason_master RENAME COLUMN module_id TO module;
ALTER TABLE eg_demand_reason_master RENAME COLUMN id_category TO "category";
ALTER TABLE eg_demand_reason_master RENAME COLUMN reason_master TO reasonmaster;
ALTER TABLE eg_demand_reason_master RENAME COLUMN is_debit TO isdebit;
ALTER TABLE eg_demand_reason_master RENAME COLUMN order_id TO "order";
ALTER TABLE eg_demand_reason_master RENAME COLUMN created_date TO create_date;
