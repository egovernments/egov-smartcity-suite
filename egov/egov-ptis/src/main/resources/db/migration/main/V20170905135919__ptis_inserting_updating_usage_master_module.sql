insert into eg_module (id,name,enabled,contextroot, parentmodule,displayname,ordernumber) values (nextval('SEQ_EG_MODULE'),'UsageMaster',true,null,
(select id from eg_module where name='PTIS-Masters'),'Nature Of Usage',null);

-----context root changed for usage master

update eg_action set parentmodule=(select id from eg_module where name='UsageMaster') where name in ('Usage Master List','Modify Usage Master','propertyusageMasterEditAction','Usage Master');

-------renaming usage master screens 

update eg_action set displayname='Modify Usage' where name='Modify Usage Master';

update eg_action set displayname='Create Usage' where name='Usage Master';

-----context root changed for arrear demand register

update eg_action set parentmodule=(select id from eg_module where name='PTIS-Reports') where name in ('ArrearDemandRegisterVLT','ArrearDemandRegisterPT','ArrearDemandRegisterResult');

---adding action for view usage master

INSERT INTO EG_ACTION (ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER, DISPLAYNAME, ENABLED, CONTEXTROOT, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, APPLICATION) VALUES (nextval('SEQ_EG_ACTION'), 'View Usage Master', '/usage/view', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'UsageMaster'), 3, 'View Usage', true, 'ptis', 0, 1, now(), 1, now(), (SELECT id FROM eg_module WHERE name='Property Tax' AND parentmodule IS NULL));
