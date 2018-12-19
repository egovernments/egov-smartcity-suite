UPDATE EGTL_LICENSE SET ownership_type='0' WHERE ownership_type='Own';
UPDATE EGTL_LICENSE SET ownership_type='1' WHERE ownership_type='Rented';
UPDATE EGTL_LICENSE SET ownership_type='2' WHERE ownership_type='State Government';
UPDATE EGTL_LICENSE SET ownership_type='3' WHERE ownership_type='Central Government';
UPDATE EGTL_LICENSE SET ownership_type='4' WHERE ownership_type='ULB';
ALTER TABLE EGTL_LICENSE ALTER COLUMN ownership_type TYPE numeric(1,0) using ownership_type::numeric;