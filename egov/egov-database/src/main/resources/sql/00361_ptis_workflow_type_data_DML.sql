alter table egpt_property_mutation rename state_id to state;
insert into eg_wf_types values(nextval('seq_eg_wf_types'),359,'PropertyMutation','/ptis/property/transfer/view.action?model.id=:ID',1,now(),1,now(),'Y','N','org.egov.ptis.domain.entity.property.PropertyMutation','Property Owner Transfer',0);
