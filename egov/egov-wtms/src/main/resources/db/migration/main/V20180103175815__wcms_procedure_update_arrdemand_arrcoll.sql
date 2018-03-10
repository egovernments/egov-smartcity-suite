CREATE OR REPLACE FUNCTION wtms_updatemv_arrearsdemand()
RETURNS void AS
$BODY$
DECLARE
props record;

v_demand_amount double precision;
v_demand_collection double precision;
v_arrearcoll double precision;
v_moduleid bigint;
v_curr1stinst     bigint;
v_curr2ndinst     bigint;
v_1stinststartdate date;
v_arreardemand double precision;
v_finyearstartdate  date;
v_finyearenddate date;

BEGIN
  
    select id into v_moduleid from eg_module where name='Property Tax';
    select date(startingdate), date(endingdate) into v_finyearstartdate, v_finyearenddate from financialyear where now() between startingdate and endingdate;
    select id, start_date into v_curr1stinst, v_1stinststartdate from eg_installment_master where id_module=v_moduleid and date(start_date)=v_finyearstartdate;
    select id into v_curr2ndinst from eg_installment_master where id_module=v_moduleid and date(end_date)=v_finyearenddate;

    for props in (SELECT distinct conn.consumercode,conndet.id FROM egwtr_connection conn,egwtr_connectiondetails conndet, egw_status status WHERE conn.id=conndet.connection and conndet.connectionstatus='ACTIVE' and conndet.connectiontype='NON_METERED' AND conndet.statusid=status.id and status.moduletype='WATERTAXAPPLICATION' and status.code='SANCTIONED' and EXISTS (select demConn.* from egwtr_demand_connection demConn where conndet.id=demConn.connectiondetails and exists (select demand.* from eg_demand demand where demand.is_history='N' and demConn.demand=demand.id)))
    loop
        begin
            select coalesce(sum(dd.amount),0) ,coalesce(sum(dd.amt_collected),0) into v_demand_amount,v_demand_collection from egwtr_connection con, egwtr_connectiondetails condet, egwtr_demand_connection demcom, eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst where con.id=condet.connection and condet.connectionstatus ='ACTIVE' and condet.id=demcom.connectiondetails and demcom.demand=d.id and d.id=dd.id_demand and dd.id_demand_reason=dr.id and drm.id=dr.id_demand_reason_master and dr.id_installment=inst.id and inst.id not in (v_curr1stinst, v_curr2ndinst) and d.is_history='N' and con.consumercode=props.consumercode;

            select coalesce(sum(coalesce(cold.cramount,0)),0) into v_arrearcoll from egcl_collectionheader collhead , egcl_collectiondetails cold where collhead.id=cold.collectionheader and collhead.receiptdate>=v_1stinststartdate and collhead.consumercode=props.consumercode and collhead.servicedetails= (select id from egcl_servicedetails where code ='WT' ) and collhead.status in (select id from egw_status where code in ('TO_BE_SUBMITTED' , 'SUBMITTED' ,'APPROVED') and moduletype ='ReceiptHeader') and cold.purpose ='ARREAR_AMOUNT';
            v_arreardemand:= v_demand_amount-v_demand_collection+v_arrearcoll;
            update egwtr_mv_dcb_view set arr_demand=v_arreardemand,arr_coll=v_arrearcoll,arr_balance=v_arreardemand-v_arrearcoll where hscno=props.consumercode;
        END;
    END LOOP;
       raise notice 'updated arrear demand amount & arrear amount collected';
END;
$BODY$ LANGUAGE plpgsql;
