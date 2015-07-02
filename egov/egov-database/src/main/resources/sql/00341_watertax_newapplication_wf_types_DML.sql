
insert into eg_wf_types 
(id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname,version)
values 
(nextval('seq_eg_wf_types'),(select id from eg_modules where name='Water Tax'),'WaterConnectionDetails','/wtms//application/view/:ID',1,now(),1,now(), 'Y', 'N', 'org.egov.wtms.application.entity.WaterConnectionDetails', 'New Water Tap Connection Application', 0 );

--rollback delete from eg_wf_types where type='WaterConnectionDetails';
