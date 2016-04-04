UPDATE EG_MODULE SET ORDERNUMBER = 1 WHERE  NAME = 'Trade License Transactions';
UPDATE EG_MODULE SET ORDERNUMBER = 2 WHERE  NAME = 'Search Trade';
UPDATE EG_MODULE SET ORDERNUMBER = 3 WHERE  NAME = 'Trade License Masters';
UPDATE EG_MODULE SET ORDERNUMBER = 4 WHERE  NAME = 'Trade License Reports';
UPDATE EG_MODULE SET ORDERNUMBER = 1 WHERE  NAME = 'Trade License Category';
UPDATE EG_MODULE SET ORDERNUMBER = 2 WHERE  NAME = 'Trade License SubCategory';
UPDATE EG_MODULE SET ORDERNUMBER = 3 WHERE  NAME = 'Trade License UOM';

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License Fee Matrix',true,'tl',(select id from eg_module where name = 'Trade License Masters'),'Fee Matrix', 4);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License Validity',true,'tl',(select id from eg_module where name = 'Trade License Masters'),'License Validity', 5);

UPDATE EG_ACTION SET parentmodule=(SELECT id from eg_module where name='Trade License Fee Matrix'), displayname='Create License Fee Matrix' WHERE name='Create-License FeeMatrix';

UPDATE EG_ACTION SET parentmodule=(SELECT id from eg_module where name='Trade License Validity'), displayname='Create License Validity' WHERE name='Create-Validity';

UPDATE EG_ACTION SET parentmodule=(SELECT id from eg_module where name='Trade License Validity'), displayname='Update License Validity',ordernumber=2 WHERE name='Update-Validity';

UPDATE EG_ACTION SET parentmodule=(SELECT id from eg_module where name='Trade License Validity'), displayname='View License Validity',ordernumber=3 WHERE name='View-Validity';

UPDATE EG_ACTION SET enabled=false WHERE name = 'Create-FeeMatrix';

UPDATE EG_ACTION SET enabled=false WHERE name ='TLAuditTrailReport';

UPDATE EG_ACTION SET enabled=false WHERE name ='Search Trade Portal Licenses';

DELETE FROM EG_ROLEACTION WHERE actionid in (select id from eg_action where name in ('Create-Motor FeeMatrix','Create-Workforce FeeMatrix'));

DELETE FROM EG_ACTION where name in ('Create-Motor FeeMatrix','Create-Workforce FeeMatrix');


