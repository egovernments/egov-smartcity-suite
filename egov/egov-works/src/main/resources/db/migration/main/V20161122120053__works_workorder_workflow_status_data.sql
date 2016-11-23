------ START : Insert Application types for workflow ---
insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,enabled,grouped,typefqn,displayname,version)
values(nextval('seq_eg_wf_types'),(select id from eg_module where name ='Works Management'),'WorkOrder','/egworks/letterofacceptance/update/:ID',1,now(),1,now(),TRUE,FALSE,'org.egov.works.workorder.entity.WorkOrder','Letter Of Acceptance',0);

------ END : Insert Application types for workflow ---

--rollback delete from EG_WF_TYPES where type = 'WorkOrder' and module = (select id from eg_module where name ='Works Management');
--rollback delete from EG_WF_MATRIX where objecttype = 'WorkOrder';
