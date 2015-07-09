insert into eg_modules (id,name,description)
values(nextval('seq_eg_modules'),'PGR','Public Grievance Module');

--rollback delete from eg_modules where name='PGR';

insert into eg_wf_types 
(id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname)
values 
(nextval('seq_eg_wf_types'),(select id from eg_modules where name='PGR'),'Complaint','/pgr/complaint-update?id=:ID',1,now(),1,now(), 'Y', 'N', 'org.egov.pgr.entity.Complaint', 'Complaint' );

--rollback delete from eg_wf_types where link='/pgr/complaint-update?id=:ID';

update eg_action set queryparams='id=' where url='/complaint-update' and name='Update Complaint';

-- rollback update eg_action set queryparams=null where url='/complaint-update' and name='Update Complaint';
