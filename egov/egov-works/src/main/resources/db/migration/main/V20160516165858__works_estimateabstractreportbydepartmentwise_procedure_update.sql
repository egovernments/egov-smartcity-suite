--------------Procedure updated to show Total bill amount----------------
create or replace FUNCTION egwupdateworkprogressmatview()
RETURNS void as $$
declare
  v_id bigint;
  v_estimatenumber character varying(256);
  v_agreementnumber character varying(50);
  v_agreementdate timestamp without time zone;
  v_contractor bigint;
  v_agreementamount double precision;
  v_latestmbnumber character varying(50);
  v_latestmbdate timestamp without time zone;
  v_latestbillnumber character varying(50);
  v_latestbilldate timestamp without time zone;
  v_billtype character varying(50);
  v_billamount double precision;
  v_totalbillamount double precision;
  v_totalbillpaidsofar double precision;
  v_balancevalueofworktobill double precision;
  v_grossamountbilled double precision;
  v_spilloverbillscreatedflag boolean;

  v_wostatuscode character varying(50);
  v_checkworkordernumber character varying(256);
  v_workcompleted boolean;

  lineestimatedetails record;
  billdetailsdetails record;
BEGIN
    delete from egw_mv_estimate_abstract_by_department_billdetail;
    delete from egw_mv_work_progress_register;
    for lineestimatedetails in (select distinct led.id, led.estimatenumber,le.ward, le.location, le.workcategory, le.typeofslum, le.beneficiary, led.nameofwork, pc.code, le.fund, le.function, le.budgethead, le.typeofwork, le.subtypeofwork, le.adminsanctionby, le.adminsanctiondate, led.estimateamount, le.modeofallotment, le.technicalsanctionby, le.technicalsanctiondate, led.actualestimateamount, le.spilloverflag,le.billscreated,le.executingdepartment,led.grossamountbilled ,le.scheme ,le.subscheme,le.natureofwork,le.id as leid,led.id ledid,status.code as lestatus,dept.name as departmentName,le.workordercreated from egw_lineestimate_details as led, egw_lineestimate as le,egw_status as status,eg_department dept,egw_projectcode pc where pc.id = led.projectcode and le.status = status.id and le.id = led.lineestimate and le.executingdepartment = dept.id and le.status in (select id from egw_status where code in ('ADMINISTRATIVE_SANCTIONED', 'TECHNICAL_SANCTIONED')))
    loop
        v_id := lineestimatedetails.id;
        v_estimatenumber := lineestimatedetails.estimatenumber;
        v_totalbillpaidsofar := 0;
        v_grossamountbilled := lineestimatedetails.grossamountbilled;
        v_agreementamount := 0;
        v_balancevalueofworktobill := 0;
	v_spilloverbillscreatedflag := lineestimatedetails.billscreated;
        select wo.workorder_number, wo.workorder_date, wo.contractor_id, COALESCE(wo.workorder_amount,0),wostatus.code into v_agreementnumber, v_agreementdate, v_contractor, v_agreementamount,v_wostatuscode from egw_workorder as wo,egw_status wostatus where wo.status_id = wostatus.id and wo.estimatenumber = v_estimatenumber;
        
        select br.billnumber, br.billdate, br.billtype, br.billamount, mb.mb_refno, mb.mb_date into v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount, v_latestmbnumber, v_latestmbdate from eg_billregister as br
            join egw_contractorbill as con on br.id = con.id join egw_mb_header as mb on br.id = mb.billregister_id where con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED')) and con.billsequencenumber = (
                    select max(conn.billsequencenumber) from egw_contractorbill as conn
                        join eg_billregister as brr on brr.id = conn.id where
                            conn.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber) and brr.billstatus = 'APPROVED');

 select COALESCE(sum(br.billamount),0) into v_totalbillpaidsofar from eg_billregister as br
            join egw_contractorbill as con on br.id = con.id
                left outer join eg_billregistermis bill_reg_mis on bill_reg_mis.billid=br.id
                    left outer join voucherheader bill_voucher_hdr on bill_reg_mis.voucherheaderid=bill_voucher_hdr.id
                        left outer join miscbilldetail misc_bill_dtl on misc_bill_dtl.billvhid=bill_voucher_hdr.id
                            left outer join voucherheader paymentvoucher on paymentvoucher.id = misc_bill_dtl.billvhid
                            where paymentvoucher.status = 0 and bill_voucher_hdr.status = 0 and con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED';

select COALESCE(sum(br.billamount),0) into v_totalbillamount from eg_billregister as br
            join egw_contractorbill as con on br.id = con.id where
                con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED';


	for billdetailsdetails in(select con.workorder,br.id ,br.billnumber,br.billdate,br.billamount,br.billtype from eg_billregister as br join egw_contractorbill as con on br.id = con.id where
                con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED') 
	loop
 INSERT INTO egw_mv_estimate_abstract_by_department_billdetail(
            ledid, workorder,billid, billnumber, billdate, billamount,billtype)
    VALUES (lineestimatedetails.id,billdetailsdetails.workorder,billdetailsdetails.id,billdetailsdetails.billnumber,billdetailsdetails.billdate,billdetailsdetails.billamount,billdetailsdetails.billtype);
	end loop;

       SELECT DISTINCT wo.workorder_number into v_checkworkordernumber FROM egw_workorder wo ,egw_status status WHERE EXISTS (SELECT 1 FROM egw_contractorbill cb , eg_billregister br WHERE cb.id=br.id and wo.id = cb.workOrder AND br.billstatus  = 'APPROVED' AND br.billtype = 'Final Bill' ) and wo.status_id=status.ID AND wo.workorder_number = v_agreementnumber AND status.code= 'APPROVED' ;
                      
	IF v_checkworkordernumber != '' THEN 
	    v_workcompleted = true;
	ELSE
	    v_workcompleted = false;
	END IF;
              
        IF v_spilloverbillscreatedflag THEN
            v_totalbillpaidsofar := v_totalbillpaidsofar + v_grossamountbilled;
            v_totalbillamount := v_totalbillamount + v_grossamountbilled;
        END IF;

        IF v_totalbillamount > 0 THEN
            v_balancevalueofworktobill := v_agreementamount - v_totalbillamount;
        ELSE
            v_balancevalueofworktobill = v_agreementamount;
        END IF;       

        INSERT INTO egw_mv_work_progress_register(
            id, ward, location, workcategory, typeofslum, beneficiary, nameofwork, wincode,
            fund, function, budgethead, typeofwork, subtypeofwork, adminsanctionby,
            adminsanctiondate, adminsanctionamount, technicalsanctionby,
            technicalsanctiondate, estimateamount, modeofallotment, agreementnumber,
            agreementdate, contractor, agreementamount, latestmbnumber, latestmbdate,
            latestbillnumber, latestbilldate, billtype, billamount, totalbillamount,
            totalbillpaidsofar, balancevalueofworktobill, spilloverflag, department,
	    scheme,subscheme,natureofwork,leid,ledid,lestatus,departmentName,wostatuscode,workordercreated,workcompleted,createdby, createddate, lastmodifiedby,
            lastmodifieddate)
    VALUES (nextval('seq_egw_mv_work_progress_register'), lineestimatedetails.ward, lineestimatedetails.location,                		    lineestimatedetails.workcategory, lineestimatedetails.typeofslum, lineestimatedetails.beneficiary,     		    lineestimatedetails.nameofwork, lineestimatedetails.code,lineestimatedetails.fund, lineestimatedetails.function, lineestimatedetails.budgethead, lineestimatedetails.typeofwork,lineestimatedetails.subtypeofwork, lineestimatedetails.adminsanctionby,
            lineestimatedetails.adminsanctiondate, lineestimatedetails.estimateamount,lineestimatedetails.technicalsanctionby,
            lineestimatedetails.technicalsanctiondate, lineestimatedetails.actualestimateamount, lineestimatedetails.modeofallotment, v_agreementnumber,v_agreementdate, v_contractor, v_agreementamount, v_latestmbnumber, v_latestmbdate,
            v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount, v_totalbillamount,
            v_totalbillpaidsofar, v_balancevalueofworktobill, lineestimatedetails.spilloverflag, lineestimatedetails.executingdepartment,  lineestimatedetails.scheme,lineestimatedetails.subscheme,lineestimatedetails.natureofwork,lineestimatedetails.leid,lineestimatedetails.ledid,lineestimatedetails.lestatus, lineestimatedetails.departmentName,v_wostatuscode,lineestimatedetails.workordercreated,v_workcompleted,1, now(), 1, now());

    end loop;
end;
$$ LANGUAGE plpgsql;

