ALTER TABLE egtl_license ALTER COLUMN direct_worker_male SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN direct_worker_female SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN contract_worker_male SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN contract_worker_female SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN daily_wages_male SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN daily_wages_female SET DEFAULT 0;
ALTER TABLE egtl_license ALTER COLUMN total_workers SET DEFAULT 0;

ALTER TABLE egtl_license ALTER COLUMN mandal_name SET DEFAULT 'ANY'; 
ALTER TABLE egtl_license ALTER COLUMN door_number SET DEFAULT 'ANY';
