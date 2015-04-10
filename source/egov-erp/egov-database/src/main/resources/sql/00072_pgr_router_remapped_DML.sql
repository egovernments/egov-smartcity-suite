
update eg_employee set id_user=72 where name='HO';

update eg_emp_assignment set position_id=7,id_employee=(select id from eg_employee where name='HO') where id=4;

delete from pgr_router ;

insert into pgr_router (id,complainttypeid,position,bndryid,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values (nextval('seq_pgr_router'),null,7,1,0,1,now(),1,now());

delete from eg_action where name ='DEFAULT';

insert into eg_roleaction_map (select id,(select id from eg_action where url='/complaint-update') from eg_role );




UPDATE eg_address SET  version =0;
UPDATE eg_event_processor_spec SET  version =0;
UPDATE eg_event_result SET  version =0;
UPDATE eg_filestoremap SET  version =0;
UPDATE eg_hierarchy_type SET  version =0;
UPDATE eg_role SET  version =0;
UPDATE EG_WF_STATES SET  version =0;
UPDATE EG_WF_TYPES SET  version =0;
UPDATE eg_user SET  version =0;

UPDATE pgr_complainant SET  version =0;
UPDATE pgr_complaintstatus SET  version =0;
UPDATE pgr_complaintstatus_mapping SET  version =0;
UPDATE pgr_complainttype SET  version =0;
UPDATE pgr_receiving_center SET  version =0;
UPDATE pgr_escalation SET  version =0;
UPDATE pgr_complaint SET  version =0;

UPDATE egeis_position_hierarchy SET  version =0;
UPDATE VOUCHERHEADER SET  version =0;
UPDATE EGEIS_POST_CREATION SET  version =0;

UPDATE EG_POSITION SET  version =0;

update eg_user set locale='en_IN';

UPDATE EG_USER SET NAME=USERNAME;


insert into EGEIS_DEPTDESIG (id,desig_id,dept_id,outsourced_posts,sanctioned_posts)
values(nextval('SEQ_EIS_DEPTDESIG'),3,10,null,null);

update eg_position set deptdesig=(select id from  EGEIS_DEPTDESIG where desig_id=3 and dept_id=10) where id=7



