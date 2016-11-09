INSERT into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILPREAMBLE','ADJOURNED',now(),'ADJOURNED',7);

INSERT into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'COUNCILMEETING','MOM_FINALISED',now(),'MOM FINALISED',4);

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)  values(nextval('SEQ_EG_ACTION'),'generate-resolution','/councilmom/generateresolution', (select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')) ,4,'view-resolution',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='generate-resolution'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'generate-resolution') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

create sequence SEQ_EGCNCL_MOM_NUMBER;
create sequence SEQ_EGCNCL_SUMOTO_NUMBER;







