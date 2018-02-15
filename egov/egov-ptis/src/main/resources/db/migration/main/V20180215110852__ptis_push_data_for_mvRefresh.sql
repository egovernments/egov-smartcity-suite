CREATE OR REPLACE FUNCTION egptpushdatatomvrefresh(noofdays integer)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
begin
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'NEW_ASSESSMENT' from egpt_basic_property bp where bp.propertyid is not null and not exists (select upicno from egpt_mv_propertyinfo where upicno = bp.propertyid);
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'NEW_ASSESSMENT' from egpt_basic_property where propertyid is not null and propertyid in (select propertyid from egpt_basic_property where propertyid is not null except select upicno from egpt_mv_propertyinfo);
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'MISSING' from egpt_basic_property where propertyid is not null and propertyid in (select upicno from egpt_mv_propertyinfo where source='D' and (annualdemand is null or annualdemand=0));
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) SELECT nextval('seq_egpt_mv_assessments'), propertyid, lastmodifieddate, 'ASSESSMENT' FROM egpt_basic_property WHERE propertyid is not null and lastmodifieddate::date  >= NOW()::date - noofdays;
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), consumercode, lastmodifieddate, 'COLLECTION' from egcl_collectionheader where servicedetails in (select id from egcl_servicedetails where upper(name) in ('PROPERTY TAX','PROPERTY TAX (ON LAND)')) and status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED')) and lastmodifieddate::date  >= NOW()::date - noofdays;
  insert into egpt_mv_assessments (id, assessmentno, date_modified, type) select nextval('seq_egpt_mv_assessments'), assessmentno, lastmodifieddate, 'EDITDEMAND' from EGPT_DEMANDAUDIT where lastmodifieddate::date  >= NOW()::date - noofdays;
END;
$function$;

