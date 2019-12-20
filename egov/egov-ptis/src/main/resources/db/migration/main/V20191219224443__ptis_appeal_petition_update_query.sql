----------update appeal details
update eg_wf_types set type='Petition',displayname='Petition',typefqn='org.egov.ptis.domain.entity.objection.Petition' where link ='/ptis/revPetition/revPetition-view.action?objectionId=:ID';

update egw_status set moduletype='PTPetition' where moduletype='PTObejction'; 

INSERT INTO egpt_status (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'APPEAL PETITION',CURRENT_TIMESTAMP,'Y','APPEAL_PETETION');

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Appeal Order',true,null,'APPEALPETITION',(select id from egpt_application_type where code='APPEAL_PETETION'));

