-----------Start-----------
delete from EGW_TYPEOFWORK;

Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Roads',null,'Roads',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Buildings',null,'Buildings',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Bridges',null,'Bridges',1,to_date('26-05-10','DD-MM-RR'),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Water Works',null,'Water Works',1,to_date('26-05-10','DD-MM-RR'),null,null,(select id from EG_PARTYTYPE where code='Contractor'));

Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'BT Roads',(select id from EGW_TYPEOFWORK where code='Roads'),'BT Roads',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'CC Roads',(select id from EGW_TYPEOFWORK where code='Roads'),'CC Roads',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'HQ Buildings',(select id from EGW_TYPEOFWORK where code='Buildings'),'HQ Buildings',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Zonal office build',(select id from EGW_TYPEOFWORK where code='Buildings'),'Zonal office buildings',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Bridge',(select id from EGW_TYPEOFWORK where code='Bridges'),'Bridge',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));
Insert into EGW_TYPEOFWORK (ID,CODE,PARENTID,DESCRIPTION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,PARTYTYPEID) values (NEXTVAL('SEQ_EGW_TYPEOFWORK'),'Water Works',(select id from EGW_TYPEOFWORK where code='Water Works'),'Water Works',1,now(),null,null,(select id from EG_PARTYTYPE where code='Contractor'));

--rollback delete from EGW_TYPEOFWORK

-----------END----------------