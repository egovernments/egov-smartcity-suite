INSERT INTO eg_wf_types 
(id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,renderyn,groupyn,typefqn,displayname) VALUES 
(nextval('seq_eg_wf_types'),(SELECT id FROM eg_module WHERE name='Property Tax'),'Objection','/ptis/objection/objection-view.action?objectionId=:ID',1,now(),1,now(), 'Y', 'N', 
'org.egov.ptis.domain.entity.objection.Objection', 'Revision Petition' ); 

alter table EGPT_HEARING  DROP COLUMN inspectionRequired CASCADE;
alter table EGPT_HEARING add column inspectionRequired  BOOLEAN;
