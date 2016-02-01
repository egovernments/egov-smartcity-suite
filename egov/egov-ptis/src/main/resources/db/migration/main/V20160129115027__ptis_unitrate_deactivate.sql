ALTER TABLE egpt_mstr_category ADD COLUMN isactive boolean default true;
DROP INDEX idx_unq_egpt_cat_catname;

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Deactivate Unit rate','/admin/unitRate-deactivate.action',null,
(select id from eg_module  where name='PTIS-Administration'),1,' Deactivate Unit rate',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Deactivate Unit rate'),id from eg_role where name in ('Property Administrator');