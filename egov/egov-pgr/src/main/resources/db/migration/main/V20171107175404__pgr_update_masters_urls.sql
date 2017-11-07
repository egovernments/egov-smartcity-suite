

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Update ComplaintType',
'/complainttype/update',null,(select id from EG_MODULE where name = 'Complaint Type'),1,
'Update ComplaintType','f','pgr',0,1,now(),1,
now(),(select id from eg_module  where name = 'PGR'));

insert into eg_feature_action (action,feature)  values ((Select id from eg_action where name='Update ComplaintType'),
(select id from eg_feature  where name='Modify Grievance Type'));

insert into eg_roleaction (actionid,roleid) select distinct (select id from eg_action where name='Update ComplaintType'),
roleid from eg_roleaction  where actionid  in(select id from eg_action where name='UpdateComplaintType');


insert into eg_feature_action (action,feature)  values ((Select id from eg_action where name='ViewComplaintType'),
(select id from eg_feature  where name='Create Grievance Type'));

insert into eg_feature_action (action,feature)  values ((Select id from eg_action where name='ViewComplaintType'),
(select id from eg_feature  where name='Modify Grievance Type'));

delete from eg_roleaction  where actionid =(select id from eg_action where name ='ViewComplaintType') and
roleid =(select id from eg_role  where name ='Grievance Officer');

delete from eg_roleaction  where actionid =(select id from eg_action where name ='ViewComplaintType') and
roleid =(select id from eg_role  where name ='Redressal Officer');

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name='ViewComplaintType'),
(select id from eg_role  where name ='Grievance Officer'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name='ViewComplaintType'),
(select id from eg_role  where name ='Redressal Officer'));


update eg_action set url='/complaint/category/create' where name='Create Grievance Category';

update eg_action set url='/complaint/category/update' where name='Update Grievance Category';

update eg_action set url='/complaint/category/search' where name='Search For Edit Grievance Category';


update eg_action set  name='Create ComplaintType' where name='Add Complaint Type';

update eg_action set url='/complainttype/search' , name='Search ComplaintType' where name='UpdateComplaintType';

--Grievance Router
update eg_action set url='/complaint/router/create' , name='Create Complaint Router' where name='Create Router';

update eg_action set url='/complaint/router/search' where name='Search viewRouter Result';

update eg_action set url='/complaint/router/search/update' , name='Search Complaint Router' where name='Edit Router';

update eg_action set url='/complaint/router/search/view' , name='View Complaint Router' where name='View Router';

update eg_action set url='/complaint/router/search/download' where name='ComplaintRouter Download';

update eg_action set url='/complaint/router/update' , name='Update Complaint Router' where name='Update Router';

update eg_action set url='/complaint/router/view' , name='View Complaint RouterViaSearch' where name='RouterView';

update eg_action set url='/complaint/router/delete' , name='Delete Complaint Router' where name='Delete Router';

--Grievance Bulk Router

update eg_action set url='/complaint/bulkrouter' where name='Search Bulk Router Generation';

update eg_action set url='/complaint/bulkrouter/create' where name='Save Bulk Router';


--EscalationTime
update eg_action set url='/complaint/escalationtime/view' , name='View Escalation Time' ,displayname='View Escalation Time'
where name='Search Escalation Time';

update eg_action set url='/complaint/escalationtime' where name='Define Escalation Time';

update eg_action set url='/complaint/escalationtime/update' where name='Save Escalation Time';

--Escalation
update eg_action set url='/complaint/escalation/view' where name='View Escalation';

update eg_action set url='/complaint/bulkescalation' where name='Search Bulk Escalation';

update eg_action set url='/complaint/bulkescalation/update' where name='Save Bulk Escalation';

update eg_action set url='/complaint/escalation/search' where name='Define Escalation';

update eg_action set url='/complaint/escalation/search/update' where name='Update Escalation';

--delete
delete from eg_feature_action  where action  in (select id from eg_action where name='viewComplaintTypeResult');

delete from eg_roleaction where actionid  in(select id from eg_action where name='viewComplaintTypeResult');

delete from eg_action where name='viewComplaintTypeResult';

delete from eg_feature_action  where action  in (select id from eg_action where name='Search updateRouter Result');

delete from eg_roleaction where actionid  in(select id from eg_action where name='Search updateRouter Result');

delete from eg_action where name='Search updateRouter Result';

delete from eg_feature_action  where action  in (select id from eg_action where name='Search Escalation Time result');

delete from eg_roleaction where actionid  in(select id from eg_action where name='Search Escalation Time result');

delete from eg_action where name='Search Escalation Time result';

delete from eg_feature_action  where action  in (select id from eg_action where name='Search Escalation result');

delete from eg_roleaction where actionid  in(select id from eg_action where name='Search Escalation result');

delete from eg_action where name='Search Escalation result';

delete from eg_feature_action  where action  in (select id from eg_action where name='update RouterViaSearch');

delete from eg_roleaction where actionid  in(select id from eg_action where name='update RouterViaSearch');

delete from eg_action where name='update RouterViaSearch';

delete from eg_feature_action  where action  in (select id from eg_action where name='complaintTypeSuccess');

delete from eg_roleaction where actionid  in(select id from eg_action where name='complaintTypeSuccess');

delete from eg_action where name='complaintTypeSuccess';
