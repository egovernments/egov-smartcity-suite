
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'PTObejction','Rejected',now(),'Rejected',1);

alter table egpt_objection add column GENERATE_SPECIALNOTICE bool default false;

UPDATE eg_wf_matrix SET NEXTACTION='FORWARD TO COMMISSIONER APPROVAL' ,nextdesignation='Assistant'  where objecttype='RevisionPetition' AND NEXTACTION='ADD HEARING DATE';
 

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'RevisionPetition', 'Revision Petition:CREATED', NULL, 'FORWARD TO COMMISSIONER APPROVAL',NULL, NULL, 'Revision Petition:Registration', 'ADD HEARING DATE', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

UPDATE eg_wf_matrix SET currentstate='Revision Petition:Registration' where objecttype='RevisionPetition' AND NEXTACTION='Generate Hearing Notice';  

UPDATE eg_wf_matrix SET validactions='Forward,Reject Inspection' where objecttype='RevisionPetition' and nextstate='Revision Petition:Inspection verified' AND NEXTACTION='Final approval';  

UPDATE eg_wf_matrix SET validactions='Approve,Reject Inspection' where objecttype='RevisionPetition' and nextstate='Revision Petition:Approved'AND NEXTACTION='Print Endoresement'; 

UPDATE eg_wf_matrix SET nextstatus='INSPECTION COMPLETED' where objecttype='RevisionPetition' and currentstate='Revision Petition:Rejected';

UPDATE eg_wf_matrix SET nextstate='Revision Petition:Hearing completed' where objecttype='RevisionPetition' and currentstate='Revision Petition:Hearing date fixed' and pendingactions='Generate Hearing Notice'; 

UPDATE eg_wf_matrix SET  nextaction='Record inspection details' where objecttype='RevisionPetition' and currentstate='Revision Petition:Hearing date fixed' and pendingactions='Generate Hearing Notice'; 

delete from eg_wf_matrix where currentstate='Revision Petition:Record Hearing details' and pendingactions='Record Hearing details';

UPDATE eg_wf_matrix SET  nextstatus='HEARING COMPLETED' where objecttype='RevisionPetition' and currentstate='Revision Petition:Hearing date fixed' and pendingactions='Generate Hearing Notice'; 

UPDATE eg_wf_matrix SET nextstate='Revision Petition:Print Special Notice',nextAction='Print Special Notice' where objecttype='RevisionPetition' and currentstate='Revision Petition:Approved' and pendingactions='Print Endoresement'; 

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'RevisionPetition', 'Revision Petition:Print Special Notice', NULL, 'Print Special Notice',NULL, NULL, 'Revision Petition:END', 'END', 'ASSISTANT', NULL, 'Print Special Notice', NULL, NULL, '2015-04-01', '2099-04-01');
