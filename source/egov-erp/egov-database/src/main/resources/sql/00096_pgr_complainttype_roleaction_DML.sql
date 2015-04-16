update eg_action set url='/complainttype/create' where name='Add Complaint Type';
update eg_action set url='/complainttype/update' where name='UpdateComplaintType';
update eg_action set url='/complainttype/view' where name='ViewComplaintType';
update eg_action set url='/complainttype/ajax/result' where name='viewComplaintTypeResult';
update eg_action set url='/complaint/view' where name='View Complaint';

--rollback update eg_action set url='/complaint-type' where name='Add Complaint Type';
--rollback update eg_action set url='/complaint-type/update' where name='UpdateComplaintType';
--rollback update eg_action set url='/view-complaintType/form' where name='ViewComplaintType';
--rollback update eg_action set url='/view-complaintType/result' where name='viewComplaintTypeResult';
--rollback update eg_action set url='/view-complaint' where name='View Complaint';
