DROP VIEW egpgr_mv_functionarywise_report_view;

CREATE OR REPLACE VIEW egpgr_mv_drilldown_report_view AS 
 SELECT usr.employee::bigint AS employeeid,
    usr.name AS employeename,
    usr.todate AS employeestartdate,
    complainant.id AS complainantid,
    cd.crn,
    complainant.name AS complainantname,
    ctype.id AS complainttypeid,
    ctype.name AS complainttypename,
    cd.createddate,
    cd.details AS complaintdetail,
    cs.name AS status,
    COALESCE(childlocation.name, 'GIS_LOCATION'::character varying) AS locality,
    bndry.name AS parentboundary,
    dept.name AS department,
    (usr.name::text || '~'::text) || pos.name::text AS employeeposition,
    (bndry.name::text || ' - '::text) || childlocation.name::text AS boundaryname,
    cd.citizenfeedback AS feedback,
    count(
        CASE
            WHEN cs.name::text = 'REGISTERED'::text THEN 1
            ELSE NULL::integer
        END) AS registered,
    count(
        CASE
            WHEN cs.name::text = ANY (ARRAY['FORWARDED'::character varying::text, 'PROCESSING'::character varying::text, 'NOTCOMPLETED'::character varying::text]) THEN 1
            ELSE NULL::integer
        END) AS inprocess,
    count(
        CASE
            WHEN cs.name::text = ANY (ARRAY['COMPLETED'::character varying::text, 'WITHDRAWN'::character varying::text, 'CLOSED'::character varying::text]) THEN 1
            ELSE NULL::integer
        END) AS completed,
    count(
        CASE
            WHEN cs.name::text = 'REOPENED'::text THEN 1
            ELSE NULL::integer
        END) AS reopened,
    count(
        CASE
            WHEN cs.name::text = 'REJECTED'::text THEN 1
            ELSE NULL::integer
        END) AS rejected,
    sum(
        CASE
            WHEN (state.value::text = ANY (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (state.lastmodifieddate - cd.createddate) < ('01:00:00'::interval * ctype.slahours::double precision) THEN 1
            WHEN (state.value::text <> ALL (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (now() - cd.createddate::timestamp with time zone) < ('01:00:00'::interval * ctype.slahours::double precision) THEN 1
            ELSE 0
        END) AS withinsla,
    sum(
        CASE
            WHEN (state.value::text = ANY (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (state.lastmodifieddate - cd.createddate) > ('01:00:00'::interval * ctype.slahours::double precision) THEN 1
            WHEN (state.value::text <> ALL (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (now() - cd.createddate::timestamp with time zone) > ('01:00:00'::interval * ctype.slahours::double precision) THEN 1
            ELSE 0
        END) AS beyondsla,
        CASE
            WHEN bool_or((state.value::text = ANY (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (state.lastmodifieddate - cd.createddate) < ('01:00:00'::interval * ctype.slahours::double precision)) THEN 'Yes'::text
            WHEN bool_or((state.value::text <> ALL (ARRAY['COMPLETED'::character varying::text, 'REJECTED'::character varying::text, 'WITHDRAWN'::character varying::text])) AND (now() - cd.createddate::timestamp with time zone) < ('01:00:00'::interval * ctype.slahours::double precision)) THEN 'Yes'::text
            ELSE 'No'::text
        END AS issla
   FROM egpgr_complaintstatus cs,
    egpgr_complainttype ctype,
    view_egeis_employee usr,
    eg_wf_states state,
    egpgr_complaint cd
     LEFT JOIN eg_boundary bndry ON cd.location = bndry.id
     LEFT JOIN eg_boundary childlocation ON cd.childlocation = childlocation.id
     LEFT JOIN eg_department dept ON cd.department::numeric = dept.id
     LEFT JOIN eg_position pos ON cd.assignee = pos.id,
    egpgr_complainant complainant
  WHERE cd.status = cs.id AND cd.complainttype::numeric = ctype.id AND cd.state_id = state.id AND cd.assignee = usr."position" AND usr.todate >= now() AND complainant.id = cd.complainant
  GROUP BY usr.employee, usr.name, complainant.id, cd.crn, cd.createddate, complainant.name, cd.details, cs.name, ((bndry.name::text || ' - '::text) || childlocation.name::text), cd.citizenfeedback, usr.todate, ctype.id, ctype.name, bndry.name, childlocation.name, dept.name, ((usr.name::text || '~'::text) || pos.name::text);