--------------------egswtax_application_type--------------------
INSERT INTO egswtax_application_type(id, code, name, description, processingtime, active, createddate, lastmodifieddate, createdby, lastmodifiedby, version) VALUES (nextval('seq_egswtax_application_type'), 'CLOSESEWERAGECONNECTION', 'Close Sewerage Connection', null, 1, true, now(), now(), 4, 4, 0);

------ START : Sewerage application status ---
Insert into egw_status (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','Close Connection Sanctioned',now(),'CLOSERSANCTIONED',14);

-------------------- egswtax_document_type_master--------------------
INSERT INTO egswtax_document_type_master(id,description,isactive,applicationtype,ismandatory,version) values (nextval('seq_egswtax_document_type_master'),'Others','t',(select id from egswtax_application_type where code='CLOSESEWERAGECONNECTION' and active='t'),'f',0);


ALTER TABLE egswtax_applicationdetails ADD COLUMN closeconnectionreason character varying(1024);
