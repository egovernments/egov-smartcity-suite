CREATE OR REPLACE FUNCTION update_tl_workflow()
  RETURNS void AS
$BODY$
declare
stateid numeric default 0;
createby numeric default 0;
license_num  numeric default 0;
initiatorpos numeric default 0;
sender varchar(200);
cur_lic egtl_license%ROWTYPE;

begin
RAISE NOTICE 'updating workflow';
for cur_lic in (select * from egtl_license where egwstatusid IN (SELECT id FROM egw_status WHERE moduletype='TRADELICENSE' AND code IN ('FIRSTLVLCOLLECTIONDONE','CREATED','INSPECTIONDONE')))
loop
begin
stateid:=cur_lic.state_id;
createby:=(select createdby from eg_wf_states where id=stateid);
license_num:=cur_lic.id;
initiatorpos:= (SELECT "position" FROM view_egeis_employee WHERE employee=
(SELECT createdby FROM eg_wF_states WHERE id=(SELECT state_id FROM egtl_license WHERE id=license_num))
 limit 1);
sender:= (SELECT username||'::'|| name FROM eg_user WHERE id IN (SELECT createdby FROM eg_wf_states WHERE id 
=(SELECT state_id FROM egtl_license WHERE id=license_num)));
delete from eg_wf_state_history where state_id=stateid;
UPDATE eg_wf_states SET value='License Created',nextaction='First level Collection Pending',
lastmodifieddate =CURRENT_DATE,
  lastmodifiedby  = createby,dateinfo =CURRENT_DATE,
  comments ='Manually moved to initiator draft to support NEW workflow',
  owner_pos=initiatorpos,sendername=sender,status  =0,initiator_pos=initiatorpos WHERE id = stateid;
update egtl_license set egwstatusid=(select id from egw_status
 where moduletype ='TRADELICENSE' and code='CREATED'),id_status=(select id from egtl_mstr_status where code='ACK')
 where id = license_num;
end;
end loop;
end;
$BODY$
  LANGUAGE plpgsql;

select update_tl_workflow();
