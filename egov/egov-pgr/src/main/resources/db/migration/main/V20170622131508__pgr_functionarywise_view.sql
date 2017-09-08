
CREATE OR REPLACE VIEW egpgr_mv_functionarywise_report_view AS 
SELECT 
cast(usr.employee as bigint) as  employeeId,
usr.name as employeeName,
ctype.id as  complainttypeid,
ctype.name as complainttypename,
usr.todate as employeeStartDate,
complainant.id as complainantId,
crn,
complainant.name as complainantName,
cd.createddate as createdDate,
cd.details as complaintDetail,
cs.name as status ,
bndry.name || ' - ' || childlocation.name as boundaryName,
cd.citizenfeedback as feedback,
COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered ,
COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','NOTCOMPLETED') THEN 1 END) inprocess,
COUNT(CASE WHEN cs.name IN ('COMPLETED','WITHDRAWN','CLOSED') THEN 1 END) Completed,
COUNT(CASE WHEN cs.name IN ('REOPENED') THEN 1 END) reopened,
COUNT(CASE WHEN cs.name IN ('REJECTED') THEN 1 END) Rejected ,
SUM(
	CASE 
		WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND 
			(state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours) THEN 1 
		WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND 
			(now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 1 ELSE 0 END) withinSLA,
SUM(
	CASE 
		WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND 
			(state.lastmodifieddate - cd.createddate) > (interval '1h' * ctype.slahours) THEN 1 
		WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND 
			(now() - cd.createddate ) > (interval '1h' * ctype.slahours)) THEN 1 ELSE 0 END) beyondSLA ,
CASE
   WHEN bool_or(state.value IN ('COMPLETED','REJECTED','WITHDRAWN') AND 
      (state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 'Yes'
   WHEN bool_or(state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') AND 
  	  (now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 'Yes' ELSE 'No' END as isSLA 
FROM egpgr_complaintstatus cs ,
egpgr_complainttype ctype,
view_egeis_employee usr ,
eg_wf_states state ,
egpgr_complaint cd
left JOIN eg_boundary bndry on cd.location =bndry.id 
left JOIN eg_boundary childlocation on cd.childlocation=childlocation.id 
left JOIN eg_department dept on cd.department =dept.id  
left join eg_position pos on cd.assignee=pos.id ,egpgr_complainant complainant 
WHERE cd.status = cs.id and 
cd.complainttype= ctype.id  and 
cd.state_id = state.id and
 cd.assignee= usr.position and 
usr.todate >= (now()::date + 1 - interval '1 sec') and 
complainant.id=cd.complainant 
group by usr.employee,usr.name,complainant.id ,
crn,cd.createddate ,complainant.name ,cd.createddate ,cd.details,cs.name  ,
bndry.name || ' - ' || childlocation.name ,
cd.citizenfeedback,ctype.id,ctype.name,usr.todate;