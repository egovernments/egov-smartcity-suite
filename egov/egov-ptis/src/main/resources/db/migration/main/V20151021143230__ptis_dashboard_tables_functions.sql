--functions
CREATE OR REPLACE FUNCTION lastFinYearDate(v_currDate timestamp without time zone)
  RETURNS timestamp without time zone AS
$BODY$
declare
	v_date timestamp without time zone;
	dateStr	character varying(10);
BEGIN  
	dateStr := '01/04/';
	if(extract(MONTH from v_currDate)>=1 and extract(MONTH from v_currDate)<=3)then
		dateStr := dateStr || extract(YEAR from v_currDate)-2;
	else
		dateStr := dateStr || extract(YEAR from v_currDate)-1;
	end if;
	v_date := to_date(dateStr,'dd/MM/yyyy');
	return v_date;   
END; 
$BODY$
  LANGUAGE plpgsql;

create or replace function previousFinancialYear()
returns character varying as
$BODY$
declare
	prevFinYear character varying (10);
begin
	select concat(extract(year from inst.start_date) - 1, '-', extract(year from inst.end_date) - 1) into prevFinYear
	from eg_installment_master inst, eg_module m 
	where now() between inst.start_date and inst.end_date
	and inst.id_module = m.id and m.name = 'Property Tax';
	return prevFinYear;
end;
$BODY$
LANGUAGE plpgsql;

create or replace function currentFinancialYear()
returns character varying as
$BODY$
declare
	currFinYear character varying (10);
begin
	select concat(extract(year from inst.start_date), '-', extract(year from inst.end_date)) into currFinYear
	from eg_installment_master inst, eg_module m 
	where now() between inst.start_date and inst.end_date
	and inst.id_module = m.id and m.name = 'Property Tax';
	return currFinYear;
end;
$BODY$
LANGUAGE plpgsql;
  
--tables
CREATE TABLE egpt_mv_collection
AS
select ch.id rcptheadid, ch.consumercode, ch.receiptnumber, ch.totalamount, ch.createddate, 
case when ch.receiptnumber like '%BW%' then 'B' else case when ch.manualreceiptnumber is null then ch.collectiontype else 'M' end end collectiontype
FROM egcl_collectionheader ch, egpt_mv_propertyinfo m1
WHERE m1.upicno=ch.consumercode
and ch.servicedetails=(select id from egcl_servicedetails where name = 'Property Tax')
and ch.status in (select id from egw_status where moduletype = 'ReceiptHeader' and description in ('To Be Submitted','Submitted','Approved'))
and ch.createddate>=lastFinYearDate(current_date)
order by ch.createddate;

CREATE TABLE egcl_mv_monthly_coll
AS
select m1.zoneid, m1.wardid,(lpad(extract(MONTH FROM mc.createddate)::varchar,2,'0')||'-'||extract(YEAR FROM mc.createddate)) as period, sum(mc.totalamount) collection
FROM egpt_mv_propertyinfo m1, egpt_mv_collection mc, eg_boundary zone, eg_boundary ward
WHERE m1.upicno=mc.consumercode
and m1.wardid=ward.id
and m1.zoneid=zone.id
GROUP BY m1.zoneid, m1.wardid,(lpad(extract(MONTH FROM mc.createddate)::varchar,2,'0')||'-'||extract(YEAR FROM mc.createddate));

CREATE TABLE egpt_mv_finyear_tot_coll
AS
select * from egpt_mv_collection coll, financialyear fy
where fy.startingdate<=now()
and fy.endingdate>=now()
and coll.createddate>=fy.startingdate 
and coll.createddate<=fy.endingdate;

CREATE table egpt_mv_finyear_coll
AS
select mc.rcptheadid, sum(cd.cramount) collection from egpt_mv_finyear_tot_coll mc, egcl_collectiondetails cd
where mc.rcptheadid=cd.id
and cd.cramount>0
and cd.description like concat('%',currentFinancialYear(),'%')
group by mc.rcptheadid;

CREATE table egpt_mv_lfinyear_coll
AS
select mc.rcptheadid, sum(cd.cramount) collection from egpt_mv_finyear_tot_coll mc, egcl_collectiondetails cd
where mc.rcptheadid=cd.id
and cd.cramount>0
and cd.description like concat('%',previousFinancialYear(),'%')
group by mc.rcptheadid;

create table egpt_collection_report (
	id bigint not null,
	zoneid bigint not null,
	wardid bigint not null,
	target_amount_month bigint not null,
	target_amount_year bigint not null,
	report_type character varying (26)  not null,
	period character varying (12),
	createdby bigint  not null,
	modifiedby bigint  not null,
	createddate timestamp without time zone  not null,
	modifieddate timestamp without time zone  not null
);

alter table egpt_collection_report  add constraint pk_egpt_collection_report primary key(id);
alter table egpt_collection_report  add constraint fk_zoneid_egpt_collection_report foreign key (zoneid) 
references eg_boundary(id);
alter table egpt_collection_report  add constraint fk_wardid_egpt_collection_report foreign key (wardid) 
references eg_boundary(id);
