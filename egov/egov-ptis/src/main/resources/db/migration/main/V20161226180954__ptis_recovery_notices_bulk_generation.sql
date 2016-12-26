INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'RecoveryNotices-Form','/recoveryNotices/form',null,(select id from eg_module  where name='Existing property'),6,'Generate Recovery Notices',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'RecoveryNotices-Form'),id from eg_role where name in ('Property Approver');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'RecoveryNotices-GenerateBulk','/recoveryNotices/generatenotice',null,(select id from eg_module  where name='Existing property'),null,'Genearte ESD Notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'RecoveryNotices-GenerateBulk'),id from eg_role where name in ('Property Approver');


create table egpt_recovery_notices_info(
  id bigint not null,
  propertyid character varying(64),
  noticetype character varying(64),
  isgenerated boolean,
  jobNumber bigint not null,
  error character varying(256),
  createdby bigint not null,
  lastmodifiedby bigint not null,
  createddate timestamp without time zone not null,
  lastmodifieddate timestamp without time zone not null,
  version bigint
);

alter table egpt_recovery_notices_info add constraint pk_egpt_recovery_notices_info primary key(id);
alter table egpt_recovery_notices_info add constraint fk_egpt_rec_notices_info_createdby foreign key(createdby) references eg_user(id);
alter table egpt_recovery_notices_info add constraint fk_egpt_rec_notices_info_lastmodifiedby foreign key(lastmodifiedby) references eg_user(id);

create sequence seq_egpt_recovery_notices_info;
