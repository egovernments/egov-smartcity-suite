------ START : Sewerage application status ---
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','Inspection Fee Pending',now(),'INSPECTIONFEEPENDING',12);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'SEWERAGETAXAPPLICATION','Deputy Executive Engineer Approved',now(),'DEEAPPROVED',13);
------ END : Sewerage application status ---


--------------------------Sewerage new application workflow ---------------------------------
Delete from eg_wf_matrix where objecttype='SewerageApplicationDetails' and additionalrule='NEWSEWERAGECONNECTION';

------------ START : Without inspection charges ----------------------
Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','NEW','','','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Clerk Approved','Assistant Engineer Approval Pending','Assistant Engineer','CREATED','Forward',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Clerk Approved','','Assistant Engineer Approval Pending','Assistant Engineer','NEWSEWERAGECONNECTION','Assistant Engineer Approved','Estimation Notice Generation Pending','Senior Assistant,Junior Assistant','INITIALAPPROVED','Submit,Reject',null,null,'2016-05-02','2016-05-02','0');


Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Rejected','','','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Assistant Engineer Approved','Deputy Executive Engineer Approval Pending','Deputy Executive Engineer','INITIALAPPROVED','Forward,Cancel',null,null,'2016-05-02','2016-05-02','0');
---------------------------------- END --------------------------------

------------------- START : With inspection charges -------------------
Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','NEW','','Inspection Fee Collection','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Inspection Fee Pending','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPENDING','Forward',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Inspection Fee Pending','','Assistant Engineer Approval Pending','Assistant Engieer','NEWSEWERAGECONNECTION','Inspection Fee Collected','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPAID','Forward,Reject',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Inspection Fee Collected','','Assistant Engineer Approval Pending','Assistant Engieer','NEWSEWERAGECONNECTION','Assistant Engineer Approved','Deputy Executive Engineer Approval Pending','Deputy Executive Engineer','INITIALAPPROVED','Forward,Reject',null,null,'2016-05-02','2016-05-02','0');


Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Rejected','','Inspection Fee Collection','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Inspection Fee Collected','Assistant Engineer Approval Pending','Assistant Engineer','INSPECTIONFEEPAID','Forward,Cancel',null,null,'2016-05-02','2016-05-02','0');
-------------------------------------- End -------------------------------

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Assistant Engineer Approved','','Deputy Executive Engineer Approval Pending','Deputy Executive Engineer','NEWSEWERAGECONNECTION','Deputy Executive Engineer Approved','Estimation Notice Generation Pending','Senior Assistant,Junior Assistant','DEEAPPROVED','Forward,Reject',null,null,'2016-05-02','2016-05-02','0');


Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Deputy Executive Engineer Approved','','Estimation Notice Generation Pending','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Estimation Notice Generated','Donation Charges Payment Pending','Senior Assistant,Junior Assistant','ESTIMATIONAMOUNTPAID ','Generate Estimation Notice',null,null,'2016-05-02','2016-05-02','0');


Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Estimation Notice Generated','','Donation Charges Payment Pending','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Payment Done Against Estimation','Executive Engineer Approval Pending','Executive Engineer','ESTIMATIONAMOUNTPAID','Forward',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Payment Done Against Estimation','','Executive Engineer Approval Pending','Executive Engineer','NEWSEWERAGECONNECTION','Executive Engineer Approved','Work Order Generation Pending','Senior Assistant,Junior Assistant','FINALAPPROVED','Approve',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Executive Engineer Approved','','Work Order Generation Pending','Senior Assistant,Junior Assistant','NEWSEWERAGECONNECTION','Work Order Generated','Connection Execution Pending','Assistant Engineer','WORKORDERGENERATED','Generate Work Order',null,null,'2016-05-02','2016-05-02','0');

Insert into eg_wf_matrix(id,department,objecttype,currentstate,currentstatus,pendingactions,currentdesignation,additionalrule,nextstate,
nextaction,nextdesignation,nextstatus,validactions,fromqty,toqty,fromdate,todate,version)values(
nextval('seq_eg_wf_matrix'),'ANY','SewerageApplicationDetails','Work Order Generated','','Connection Execution Pending','Assistant Engineer','NEWSEWERAGECONNECTION','END','END','','','Execute Connection',null,null,'2016-05-02','2016-05-02','0');
