------------Start-------------
INSERT INTO EG_APPCONFIG ( id, key_name, description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values 
(nextval('SEQ_EG_APPCONFIG'), 'OVERHEAD_PURPOSE', 'Overhead purpose',0,1,1,now(),now(), (select id from eg_module where name='Works Management')); 
--------------END------

---rollback DELETE FROM EG_APPCONFIG WHERE ID IN (SELECT id FROM EG_APPCONFIG WHERE key_name='OVERHEAD_PURPOSE');