------ START : Insert New status for workflow ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','New',now(),'NEW',0);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Created',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Approved',now(),'APPROVED',2);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Rejected',now(),'REJECTED',3);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Cancelled',now(),'CANCELLED',4);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval
('SEQ_EGW_STATUS'),'RevisionAbstractEstimate','Re-Submitted',now(),'RESUBMITTED',5);
------ END : Insert New status for workflow ---

insert into eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,enabled,grouped,typefqn,displayname,version)
values(nextval('seq_eg_wf_types'),(select id from eg_module where name ='Works Management'),'RevisionAbstractEstimate','/egworks/revisionestimate/update/:ID',1,now(),1,now(),TRUE,FALSE,'org.egov.works.revisionestimate.entity.RevisionAbstractEstimate','Revision Estimate',0);

