
delete from EG_WF_TYPES where type = 'SewerageApplicationDetails' and link = '/stms/transactions/update/:ID';

------ START : Application types for workflow ---

INSERT INTO EG_WF_TYPES (id, module, type, link, createdby, createddate, lastmodifiedby, lastmodifieddate, enabled, grouped, typefqn, displayname, version) VALUES (nextval('seq_eg_wf_types'), (select id from eg_module where name = 'Sewerage Tax Management'), 'SewerageApplicationDetails', '/stms/transactions/update/:ID', 1, now(), 1,now(), 'Y', 'N', 'org.egov.stms.transactions.entity.SewerageApplicationDetails', 'Sewerage Connection', 0);
------ END : Application types for workflow ---


--rollback delete from EG_WF_TYPES where type = 'SewerageApplicationDetails' and link = '/stms/transactions/update/:ID';