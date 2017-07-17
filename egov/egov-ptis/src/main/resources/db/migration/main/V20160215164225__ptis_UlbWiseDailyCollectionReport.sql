
create table EGPT_ULBWISEDAILYCOLLECTION (
ID bigint NOT NULL,
DISTRICT character varying(50) NOT NULL,
ULBNAME character varying(50) NOT NULL,
ULBCODE character varying(50) NOT NULL,
schemaname character varying(50) NOT NULL,
Last_calculated_date Date,
Commissioner_Name character varying(125) ,
Commissioner_MobileNumber character varying(12) ,
Commissioner_employeecode character varying(25) ,
TARGET_ARREARS_DEMAND double precision DEFAULT 0, 
TARGET_CURRENT_DEMAND double precision DEFAULT 0,
TODAY_ARREARS_COLLECTION double precision DEFAULT 0,
TODAY_CURRENTYEAR_COLLECTION double precision DEFAULT 0,
CUMMULATIVE_ARREARS_COLLECTION double precision DEFAULT 0,
CUMMULATIVE_CURRENTYEAR_COLLECTION double precision DEFAULT 0,
LASTYEAR_COLLECTION double precision DEFAULT 0,
LASTYEAR_CUMMULATIVE_COLLECTION double precision DEFAULT 0,
isactive boolean NOT NULL DEFAULT true );


alter table EGPT_ULBWISEDAILYCOLLECTION add constraint pk_EGPT_ULBWISEDAILYCOLLECTION primary key (id);
ALTER TABLE ONLY EGPT_ULBWISEDAILYCOLLECTION ADD CONSTRAINT unq_userWiseColl_ulb  UNIQUE (DISTRICT, ULBNAME);

create sequence  SEQ_ULBWISEDAILYCOLLECTION;

------------------------------------

CREATE OR REPLACE FUNCTION  LoadPtisUlbDailycollection()
  RETURNS void AS
$BODY$
declare
  v_cityname character varying(128);
  v_schemaName   character varying(50);
  v_count bigint;
  v_currentdemand double precision;
  v_arreardemand double precision;
  v_eg_currentcoll double precision;
  v_eg_arrearcoll double precision;
  v_es_currentcoll double precision;
  v_es_arrearcoll double precision;
  v_curr_currentcoll double precision;
  v_curr_arrearcoll double precision;
  v_prev_currentcoll double precision;
  v_prev_arrearcoll double precision;
  v_curr_yes_arrearcoll double precision;
  v_curr_yes_currentcoll double precision;
  v_prev_yes_currentcoll double precision;
  v_prev_yes_arrearcoll double precision;
  cities EGPT_ULBWISEDAILYCOLLECTION%ROWTYPE;
begin
for cities in (select * from EGPT_ULBWISEDAILYCOLLECTION  where  last_calculated_date is null or last_calculated_date ::timestamp::date < (current_date) and isactive=true) 
  loop
    v_cityname := cities.ulbname;
    v_schemaName:= cities.schemaname;
    v_count := 0;
    v_currentdemand := 0;
    v_arreardemand := 0;
    v_eg_currentcoll := 0;
    v_eg_arrearcoll := 0;
    v_es_currentcoll := 0;
    v_es_arrearcoll := 0;
    v_curr_currentcoll := 0;
    v_curr_arrearcoll := 0;
    v_prev_currentcoll := 0;
    v_prev_arrearcoll := 0;
    v_curr_yes_arrearcoll := 0;
    v_curr_yes_currentcoll := 0;
    v_prev_yes_currentcoll := 0;
    v_prev_yes_arrearcoll := 0;
    raise notice 'Getting details for City : %', v_cityname;
  --  EXECUTE 'set search_path to '|| v_cityname; -- security
select COALESCE (sum(aggregate_current_demand),0), COALESCE (sum(aggregate_arrear_demand),0) into v_currentdemand, v_arreardemand  from egpt_mv_propertyinfo ;
-- cummulative tax 
     select COALESCE(sum(cd.cramount),0) into v_eg_currentcoll from egcl_collectionheader ch, egcl_collectiondetails cd, egpt_mv_propertyinfo mv
      where mv.upicno=ch.consumercode and ch.id=cd.collectionheader and ch.receiptdate::date between currFinYearDate(current_date)::date 
      and (select DATE 'yesterday')::date and ch.servicedetails = (select id from EGCL_SERVICEDETAILS where name = 'Property Tax')
      and ch.status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED'))and cd.description like concat('%',currentFinancialYear(),'%');
-- cummulative arrears
select  COALESCE (sum(cd.cramount),0) into v_eg_arrearcoll from egcl_collectionheader ch, egcl_collectiondetails cd, egpt_mv_propertyinfo mv
      where mv.upicno=ch.consumercode and ch.id=cd.collectionheader and ch.receiptdate::date between currFinYearDate(current_date)::date 
      and (select DATE 'yesterday')::date and ch.servicedetails = (select id from EGCL_SERVICEDETAILS where name = 'Property Tax')
      and ch.status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED'))and cd.description not like concat('%',currentFinancialYear(),'%');
      -- cummulative tax/arrears in Eseva 
    select  COALESCE(sum(d_crnpt+d_crned+d_crnlcs+d_crnuauthcnstplty+d_pltyoncrn),0), COALESCE( sum(d_arrpt+d_arred+d_arrlcs+d_arruauthcnstplty+d_pltyonarr),0) into v_es_currentcoll, v_es_arrearcoll 
      from pt_asmtrcpt_tbl rcpt, egpt_mv_propertyinfo mv 
      where rcpt.i_asmtno::text = mv.upicno and rcpt.dt_etrydt between currFinYearDate(current_date)::date and (select DATE 'yesterday')::date ;
      v_curr_arrearcoll := v_eg_arrearcoll + v_es_arrearcoll;
      v_curr_currentcoll := v_eg_currentcoll + v_es_currentcoll; 
 ---  today tax collection
select   COALESCE (sum(cd.cramount),0) into v_curr_yes_currentcoll from egcl_collectionheader ch, egcl_collectiondetails cd, egpt_mv_propertyinfo mv
      where mv.upicno=ch.consumercode and ch.id=cd.collectionheader and ch.receiptdate::date= (select DATE 'yesterday')::date 
       and ch.servicedetails = (select id from EGCL_SERVICEDETAILS where name = 'Property Tax')
      and ch.status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED'))and cd.description like concat('%',currentFinancialYear(),'%');
--  today arrears collection
select  COALESCE (sum(cd.cramount),0)  into v_curr_yes_arrearcoll from egcl_collectionheader ch, egcl_collectiondetails cd, egpt_mv_propertyinfo mv
      where mv.upicno=ch.consumercode and ch.id=cd.collectionheader and ch.receiptdate::date = (select DATE 'yesterday')::date 
      and ch.servicedetails = (select id from EGCL_SERVICEDETAILS where name = 'Property Tax')
      and ch.status in (select id from egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED'))and cd.description not like concat('%',currentFinancialYear(),'%') ;
  -- Last year tax cummulative
  select COALESCE(sum(d_crnpt+d_crned+d_crnlcs+d_crnuauthcnstplty+d_pltyoncrn),0), COALESCE(sum(d_arrpt+d_arred+d_arrlcs+d_arruauthcnstplty+d_pltyonarr) ,0)    into v_prev_currentcoll, v_prev_arrearcoll
       from pt_asmtrcpt_tbl rcpt, egpt_mv_propertyinfo mv
      where rcpt.i_asmtno::text = mv.upicno and  rcpt.dt_etrydt between lastFinYearDate(current_date)::date 
      and concat(extract(year from current_date) - 1, '-', (extract(month from current_date)), '-' ,extract(day from current_date) - 1)::date   ;
--Last year as on day
  select COALESCE(sum(d_crnpt+d_crned+d_crnlcs+d_crnuauthcnstplty+d_pltyoncrn),0), COALESCE(sum(d_arrpt+d_arred+d_arrlcs+d_arruauthcnstplty+d_pltyonarr) ,0)  into v_prev_yes_currentcoll, v_prev_yes_arrearcoll 
       from pt_asmtrcpt_tbl rcpt, egpt_mv_propertyinfo mv
      where rcpt.i_asmtno::text = mv.upicno and  rcpt.dt_etrydt=concat(extract(year from current_date) - 1, '-', (extract(month from current_date)), '-' ,extract(day from current_date) - 1)::date    ;  
update EGPT_ULBWISEDAILYCOLLECTION set last_calculated_date=now(), target_arrears_demand= v_arreardemand ,target_current_demand=v_currentdemand,today_arrears_collection=(v_curr_yes_arrearcoll),today_currentyear_collection=(v_curr_yes_currentcoll),cummulative_arrears_collection=(cummulative_arrears_collection+v_curr_arrearcoll),cummulative_currentyear_collection=(cummulative_currentyear_collection+v_curr_currentcoll),lastyear_collection=(v_prev_yes_currentcoll+ v_prev_yes_arrearcoll),lastyear_cummulative_collection=(lastyear_cummulative_collection + v_prev_currentcoll+v_prev_arrearcoll) where id=cities.id;
raise notice 'updated for city : %', v_cityname;
end loop;
end;
$BODY$
  LANGUAGE plpgsql;
