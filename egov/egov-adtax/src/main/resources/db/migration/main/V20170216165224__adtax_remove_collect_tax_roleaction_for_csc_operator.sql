
delete from EG_ROLEACTION where ROLEID =(select id from eg_role where (name) LIKE 'CSC Operator') and ACTIONID=(select id FROM eg_action  WHERE name = 'CollectAdvertisementTax'); 
