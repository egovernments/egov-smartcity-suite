
-------------- view : EGPT_MV_C1_COLL_RECEIPTS ----------------
CREATE VIEW  EGPT_MV_C1_COLL_RECEIPTS  AS select
  cr.id_demand_detail,
  cr.receipt_number,
  cr.receipt_date,
  cr.reason_amount,
  reas.id_demand_reason_master as reasmid,
  i.start_date as instlsdate,
  i.end_date as instedate
from
  egdm_collected_receipts cr,
  eg_demand_details dd,
  eg_installment_master i,
  eg_demand_reason reas
where
    cr.id_demand_detail = dd.id
and reas.id_installment = i.id
and dd.id_demand_reason = reas.id;


-------------- view : egpt_mv_c2_rcpt_tax_coll ----------------
create view egpt_mv_c2_rcpt_tax_coll as
select 
  receipt_number, 
  sum(reason_amount) as tax_coll
from 
  egpt_mv_c1_coll_receipts
where 
  reasmid = (select id from eg_demand_reason_master where reasonmaster='General Tax' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))         
group by receipt_number;

-------------- view : egpt_mv_c3_rcpt_penalty_coll ----------------
create view egpt_mv_c3_rcpt_penalty_coll as
select 
  receipt_number, 
  sum(reason_amount) as penalty_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='PENALTY_FINES' 
		and module=(select id from eg_module where name='Property Tax'))
and (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))  
group by receipt_number;

-------------- view : egpt_mv_c4_rcpt_librarycess_coll ----------------
create view egpt_mv_c4_rcpt_librarycess_coll as
select 
  receipt_number, 
  sum(reason_amount) as librarycess_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='Library Cess' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))   
group by receipt_number;

-------------- view : egpt_mv_c5_rcpt_arreartax_coll ----------------
create view egpt_mv_c5_rcpt_arreartax_coll as
select 
  receipt_number, 
  sum(reason_amount) as arreartax_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='General Tax' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))      
group by receipt_number;

-------------- view : egpt_mv_c6_rcpt_arrearpenalty_coll ----------------
create view egpt_mv_c6_rcpt_arrearpenalty_coll as
select 
  receipt_number, 
  sum(reason_amount) as arrearpenalty_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='PENALTY_FINES' 
		and module=(select id from eg_module where name='Property Tax'))
and (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))  
group by receipt_number;

-------------- view : egpt_mv_c7_rcpt_arrearlibrarycess_coll ----------------
create view egpt_mv_c7_rcpt_arrearlibrarycess_coll as
select 
  receipt_number, 
  sum(reason_amount) as arrearlibrarycess_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='Library Cess' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))   
group by receipt_number;



-------------- view : egpt_mv_c8_unique_rcpt_dd ----------------
create view egpt_mv_c8_unique_rcpt_dd as
select
  receipt_number,
  receipt_date,
  min(id_demand_detail) as id_dem_detail
from
  egpt_mv_c1_coll_receipts
group by receipt_number, receipt_date;

-------------- view : egpt_mv_c9_prop_coll_data ----------------
create view egpt_mv_c9_prop_coll_data as
select
  c8.receipt_number,
  c8.receipt_date,
  bp.propertyid,
  pid.zone_num as zoneid, pid.ward_adm_id as wardid, pid.adm1 as areaid, pid.adm2 as localityid, pid.adm3 as streetid, 
  ch.payeename, 
  ch.collectiontype,
  COALESCE(fit.type, 'cash', 'cash', 'bankchallan', 'bankchallan', 'card', 'card', 'online', 'online', 'atm', 'atm','banktobank','banktobank','cheque/dd') as payment_mode,
  u.username
from 
  egpt_mv_c8_unique_rcpt_dd c8,
  eg_demand_details dd,
  egpt_ptdemand ptd,
  egpt_property prop,
  egpt_basic_property bp,
  egpt_propertyid pid,
  egcl_collectionheader ch,
  egcl_collectioninstrument ci,
  egf_instrumentheader fih,
  egf_instrumenttype fit,
  eg_user u
where 
  c8.id_dem_detail = dd.id 
and dd.id_demand = ptd.id_demand
and ptd.id_property = prop.id
and prop.id_basic_property = bp.id
and bp.id_propertyid = pid.id
and (c8.receipt_number = ch.receiptnumber or c8.receipt_number = ch.manualreceiptnumber)
and ch.id = ci.collectionheader
and ci.instrumentheader = fih.id
and cast(fih.instrumenttype as bigint) = fit.id
and ch.createdby = u.id;

-------------- view : egpt_mv_c10_report ----------------
create view egpt_mv_c10_report as
select 
  c9.*, 
  c2.tax_coll, 
  c3.penalty_coll,
  c4.librarycess_coll,
  c5.arreartax_coll,
  c6.arrearpenalty_coll,
  c7.arrearlibrarycess_coll
from
  egpt_mv_c9_prop_coll_data c9 left outer join egpt_mv_c3_rcpt_penalty_coll c3 on c9.receipt_number = c3.receipt_number
  left outer join egpt_mv_c5_rcpt_arreartax_coll c5 on c9.receipt_number = c5.receipt_number
  left outer join egpt_mv_c6_rcpt_arrearpenalty_coll c6 on c9.receipt_number = c6.receipt_number
  left outer join egpt_mv_c7_rcpt_arrearlibrarycess_coll c7 on c9.receipt_number = c7.receipt_number,
  egpt_mv_c2_rcpt_tax_coll c2,
  egpt_mv_c4_rcpt_librarycess_coll c4
where
  c9.receipt_number = c2.receipt_number
  or c9.receipt_number = c4.receipt_number;
  

/*
drop view egpt_mv_c10_report;
drop view egpt_mv_c9_prop_coll_data;
drop view egpt_mv_c8_unique_rcpt_dd;
drop view egpt_mv_c7_rcpt_arrearlibrarycess_coll;
drop view egpt_mv_c6_rcpt_arrearpenalty_coll;
drop view egpt_mv_c5_rcpt_arreartax_coll;
drop view egpt_mv_c4_rcpt_librarycess_coll;
drop view egpt_mv_c3_rcpt_penalty_coll;
drop view egpt_mv_c2_rcpt_tax_coll;
drop view EGPT_MV_C1_COLL_RECEIPTS;
*/
