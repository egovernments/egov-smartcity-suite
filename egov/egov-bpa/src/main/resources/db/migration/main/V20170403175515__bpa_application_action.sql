Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'BpaNewApplication','/application/newApplication-newform',null,(select id from eg_module where name='BPA Transanctions'),1,'New Bpa Application','true','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='BpaNewApplication'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),
(select id from eg_action where name='BpaNewApplication'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'BpaNewApplicationsubmit','/application/newApplication-create',null,(select id from eg_module where name='BPA Transanctions'),1,'New Bpa Application','false','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));



Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='BpaNewApplicationsubmit'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),
(select id from eg_action where name='BpaNewApplicationsubmit'));

alter table EGBPA_APPLICATION  drop column MODIFIEDBY;
   alter table EGBPA_APPLICATION  drop column  MODIFIEDDATE;

	alter table EGBPA_APPLICATION  add column  createdDate timestamp without time zone NOT NULL;

	alter table EGBPA_APPLICATION  add column  lastModifiedDate timestamp without time zone NOT NULL;

	alter table EGBPA_APPLICATION  add column  lastModifiedBy bigint NOT NULL;
	alter table EGBPA_Applicant  add column  lastModifiedDate timestamp without time zone NOT NULL;
