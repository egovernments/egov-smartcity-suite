
 CREATE OR REPLACE FUNCTION dmdincr_incrementalload(in_wardid bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
declare
        v_previoustax   double precision;
        v_presenttax    double precision;
        v_basicproperty bigint;
        v_propertyid    bigint;
        v_reason        character varying(16);
        v_temp          bigint;

        basicproprec    record;
        proprec         record;
begin
  --raise notice 'dmdincr_incrementalload: in_wardid (%)', in_wardid;
  for basicproprec in (select bp.id bpid from egpt_basic_property bp, egpt_propertyid pid where bp.isactive=TRUE and bp.id_propertyid=pid.id and pid.ward_adm_id=in_wardid order by bp.id)
  loop
    begin
        v_basicproperty := basicproprec.bpid;
                v_previoustax   := 0;
                v_presenttax    := 0;
      --raise notice 'dmdincr_incrementalload: v_basicproperty (%)', v_basicproperty;
          for proprec in (select prop.id propid, prop.modify_reason, prop.status, prop.modified_date, prop.isexemptedfromtax from egpt_property prop where prop.status not in ('C', 'W') and prop.id_basic_property=v_basicproperty and prop.id not in (select property from egpt_taxfor_propertychange) order by prop.id_basic_property, prop.id, prop.modified_date)
          loop
                begin
                        v_propertyid := proprec.propid;
                        v_reason := proprec.modify_reason;
                        --raise notice 'lppcalc_incrementalload: v_propertyid, v_reason (% %)', v_propertyid, v_reason;
                        select coalesce(present_tax, 0) into v_previoustax from egpt_taxfor_propertychange where basicproperty=v_basicproperty and property=(select max(property) from egpt_taxfor_propertychange where basicproperty=v_basicproperty);
                        v_presenttax := dmdincr_gettaxforproperty(v_propertyid);
                        --raise notice 'lppcalc_incrementalload: v_previoustax, v_presenttax (% %)', v_previoustax, v_presenttax;
                        insert into egpt_taxfor_propertychange (basicproperty, property, reason, status, trans_date, previous_tax, present_tax, isexempted) values (v_basicproperty, v_propertyid, v_reason, proprec.status, proprec.modified_date, v_previoustax, v_presenttax, proprec.isexemptedfromtax);
                        --raise notice 'lppcalc_incrementalload: v_basicproperty, v_propertyid (% %)', v_basicproperty, v_propertyid;
                END;
          end loop;
    END;
  end loop;
exception
  when others then
    raise notice 'dmdincr_incrementalload % %', SQLERRM, SQLSTATE;
end;
$function$;


 CREATE OR REPLACE FUNCTION dmdincr_gettaxforproperty(in_propid bigint)
 RETURNS double precision
 LANGUAGE plpgsql
AS $function$
declare
  v_proptax double precision;

  proprec record;
begin
        --raise notice 'dmdincr_gettaxforproperty: in_propid (%)', in_propid;
        select sum(dd.amount) into v_proptax from egpt_ptdemand ptd, eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm where ptd.id_property=in_propid and ptd.id_demand=d.id and d.id=dd.id_demand and dd.id_demand_reason=dr.id and dr.id_demand_reason_master=drm.id and drm.module=359 and drm.code not in ('PENALTY_FINES','CHQ_BUNC_PENALTY','ADVANCE');
        --raise notice 'dmdincr_gettaxforproperty: in_proptax (%)', in_proptax;
        return v_proptax;
end;
$function$;
      
 
 

