
CREATE OR REPLACE  VIEW egpgr_mv_ageing_report_view AS
SELECT row_number() over() AS id,
mv.name AS department,
mv.status AS status,
mv.bndry AS boundary,
mv.createddate AS createddate,
mv.greater30 AS greater30,
mv.btw10to30 AS btw10to30,
mv.btw5to10 AS btw5to10,
mv.btw2to5 AS btw2to5,
mv.lsthn2 AS lsthn2
FROM(
SELECT
dept.name as name,
bndry.name as bndry,
cs.name as status,
cd.createddate as createddate,
COUNT(CASE WHEN EXTRACT(EPOCH FROM state.lastmodifieddate - cd.createddate)/86400 > 30 THEN 1 END ) AS greater30,
COUNT(CASE WHEN EXTRACT(EPOCH FROM state.lastmodifieddate - cd.createddate)/86400 BETWEEN 10 and 30 THEN 1 END ) AS btw10to30,
COUNT(CASE WHEN EXTRACT(EPOCH FROM state.lastmodifieddate - cd.createddate)/86400 BETWEEN 5 and 10 THEN 1 END) AS btw5to10,
COUNT(CASE WHEN EXTRACT(EPOCH FROM state.lastmodifieddate - cd.createddate)/86400 BETWEEN 2 and 5 THEN 1 END) AS btw2to5,
COUNT(CASE WHEN EXTRACT(EPOCH FROM state.lastmodifieddate - cd.createddate)/86400 BETWEEN 0 and 2 THEN 1 END) AS lsthn2
from egpgr_complaintstatus cs,egpgr_complainttype ctype,eg_wf_states state,egpgr_complaint cd
left JOIN eg_department dept ON cd.department =dept.id 
left JOIN eg_boundary bndry on cd.location =bndry.id
where  cd.state_id=state.id and  cd.status  = cs.id and
cd.complainttype= ctype.id   AND cs.name IN ('COMPLETED', 'WITHDRAWN','CLOSED','REJECTED')
group by dept.name,cs.name ,bndry.name,cd.createddate
UNION ALL
SELECT dept.name as name,
bndry.name as bndry,
cs.name as status,
cd.createddate as createddate,
COUNT(CASE WHEN cd.createddate < (now()::date + 1 - interval '1 sec')-interval '30 day 1' THEN 1 END ) AS greater30,
COUNT(CASE WHEN cd.createddate BETWEEN (now()::date + 1 - interval '1 sec')-interval '29 day 23:59:58' and 
(now()::date + 1 - interval '1 sec')-interval '10 day 1'THEN 1 END) AS btw10to30,
COUNT(CASE WHEN cd.createddate BETWEEN (now()::date + 1 - interval '1 sec')-interval '9 day 23:59:58' and 
(now()::date + 1 - interval '1 sec')-interval '5 day 1' THEN 1 END) AS btw5to10, 
COUNT(CASE WHEN cd.createddate BETWEEN (now()::date + 1 - interval '1 sec')-interval '4 day 23:59:50' and 
(now()::date + 1 - interval '1 sec')-interval '2 day 1' THEN 1 END) AS btw2to5, 
COUNT(CASE WHEN cd.createddate BETWEEN (now()::date + 1 - interval '1 sec')-interval '1 day 23:59:50' and 
(now()::date + 1 - interval '1 sec') THEN 1 END) AS lsthn2  
FROM egpgr_complaintstatus cs  ,egpgr_complainttype ctype ,egpgr_complaint cd   
left JOIN eg_department dept on cd.department =dept.id 
left JOIN eg_boundary bndry on cd.location =bndry.id 
WHERE cd.status  = cs.id and cd.complainttype= ctype.id AND 
cs.name IN ('REGISTERED','FORWARDED', 'PROCESSING','REOPENED')
group by dept.name,cs.name ,bndry.name ,cd.createddate)mv;

