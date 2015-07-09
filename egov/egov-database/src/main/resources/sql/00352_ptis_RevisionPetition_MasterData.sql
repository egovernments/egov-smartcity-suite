
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'PTObejction','GENERATE HEARING NOTICE',now(),'GENERATE HEARING NOTICE',7);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'PTObejction','GENERATE ENDORSEMENT NOTICE',now(),'GENERATE ENDORSEMENT NOTICE',8);

update eg_wf_types set link='/ptis/revPetition/revPetition-view.action?objectionId=:ID'   where type='Objection';


 
 