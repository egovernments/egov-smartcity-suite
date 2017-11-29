
---feature for escalation router report
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES 
(NEXTVAL('seq_eg_feature'),'Escalation Router Report','Escalation Router Report',
(select id from eg_module  where name = 'PGR'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Download EscalationRouter Report'),
(select id from eg_feature  where name='Escalation Router Report'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='EscalationRouter Report Search'),
(select id from eg_feature  where name='Escalation Router Report'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='ListComplaintTypeByCategoryOfficials'),
(select id from eg_feature  where name='Escalation Router Report'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='AjaxRouterPosition'),
(select id from eg_feature  where name='Escalation Router Report'));

update eg_feature  SET name ='View Escalation Time' , description='View Grievance Escalation Time' 
where name='Search Escalation Time';