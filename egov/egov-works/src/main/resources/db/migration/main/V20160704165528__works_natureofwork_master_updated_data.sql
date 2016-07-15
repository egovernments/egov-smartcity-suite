----------START---------------
UPDATE EGW_NATUREOFWORK SET NAME = 'Operations and Maintenance', CODE = 'Operations and Maintenance' WHERE NAME = 'Repairs and maintenance Works';

--rollback UPDATE EGW_NATUREOFWORK SET NAME = 'Repairs and maintenance Works', CODE = 'Repairs and maintenance' WHERE NAME = 'Operations and Maintenance';

---------------END---------------