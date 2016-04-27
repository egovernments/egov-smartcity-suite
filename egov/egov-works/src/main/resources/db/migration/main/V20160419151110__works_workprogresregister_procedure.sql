create or replace FUNCTION egwUpdateWorkProgressMatView()
RETURNS void as $$
declare
  v_id bigint;
  v_estimatenumber character varying(256);
  v_ward bigint;
  v_location bigint;
  v_workcategory character varying(100);
  v_typeofslum character varying(100);
  v_beneficiary character varying(100);
  v_nameofwork character varying(1024);
  v_wincode character varying(256);
  v_fund bigint;
  v_function bigint;
  v_budgethead bigint;
  v_typeofwork bigint;
  v_subtypeofwork bigint;
  v_adminsanctionby bigint;
  v_adminsanctiondate timestamp without time zone;
  v_adminsanctionamount double precision;
  v_technicalsanctionby bigint;
  v_technicalsanctiondate timestamp without time zone;
  v_estimateamount double precision;
  v_modeofallotment character varying(64);
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
  v_totalbillpaidsofar double precision;
  v_balancevalueofworktobill double precision;
  v_spilloverflag boolean;
  v_department bigint;
  v_grossamountbilled double precision;

  lineestimatedetails record;
BEGIN
	for lineestimatedetails in (select distinct led.id, led.estimatenumber from egw_lineestimate_details as led, egw_lineestimate as le where led.lineestimate = le.id and le.status in (select id from egw_status where code in ('ADMINISTRATIVE_SANCTIONED', 'TECHNICAL_SANCTIONED')))
	loop
		v_id := lineestimatedetails.id;
		v_estimatenumber := lineestimatedetails.estimatenumber;
		select le.ward, le.location, le.workcategory, le.typeofslum, le.beneficiary, led.nameofwork, led.projectcode, le.fund, le.function, le.budgethead, le.typeofwork, le.subtypeofwork, le.adminsanctionby, le.adminsanctiondate, led.estimateamount, le.modeofallotment, le.technicalsanctionby, le.technicalsanctiondate, led.actualestimateamount, le.spilloverflag, le.executingdepartment, led.grossamountbilled into v_ward, v_location, v_workcategory, v_typeofslum, v_beneficiary, v_nameofwork, v_wincode, v_fund, v_function, v_budgethead, v_typeofwork, v_subtypeofwork, v_adminsanctionby, v_adminsanctiondate, v_adminsanctionamount, v_modeofallotment, v_technicalsanctionby, v_technicalsanctiondate, v_estimateamount, v_spilloverflag, v_department, v_grossamountbilled from egw_lineestimate_details as led, egw_lineestimate as le where led.id = v_id and le.id = led.lineestimate;
		select wo.workorder_number, wo.workorder_date, wo.contractor_id, wo.workorder_amount into v_agreementnumber, v_agreementdate, v_contractor, v_agreementamount from egw_workorder as wo where wo.estimatenumber = v_estimatenumber;
		select code into v_wincode from egw_projectcode where id = (select projectcode from egw_lineestimate_details where id = v_id);
		select br.billnumber, br.billdate, br.billtype, br.billamount into v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount from eg_billregister as br
			join egw_contractorbill as con on br.id = con.id where con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
				select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED')) and con.billsequencenumber = (
					select max(conn.billsequencenumber) from egw_contractorbill as conn
						join eg_billregister as brr on brr.id = conn.id where
							conn.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber) and brr.billstatus = 'APPROVED');
		select sum(br.billamount) into v_totalbillpaidsofar from eg_billregister as br
			join egw_contractorbill as con on br.id = con.id
				LEFT OUTER JOIN EG_BILLREGISTERMIS BILL_REG_MIS ON BILL_REG_MIS.BILLID=br.ID
					LEFT OUTER JOIN VOUCHERHEADER BILL_VOUCHER_HDR ON BILL_REG_MIS.VOUCHERHEADERID=BILL_VOUCHER_HDR.ID
						LEFT OUTER JOIN MISCBILLDETAIL MISC_BILL_DTL ON MISC_BILL_DTL.BILLVHID=BILL_VOUCHER_HDR.ID
							where MISC_BILL_DTL.billvhid = 0 and con.workorder = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
								select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
									and br.billstatus = 'APPROVED';
		IF v_spilloverflag THEN
			v_totalbillpaidsofar := v_totalbillpaidsofar - v_grossamountbilled;
		END IF;

		v_balancevalueofworktobill := v_agreementamount - v_totalbillpaidsofar;


		INSERT INTO egw_mv_work_progress_register(
            id, ward, location, workcategory, typeofslum, beneficiary, nameofwork, wincode, 
            fund, function, budgethead, typeofwork, subtypeofwork, adminsanctionby, 
            adminsanctiondate, adminsanctionamount, technicalsanctionby, 
            technicalsanctiondate, estimateamount, modeofallotment, agreementnumber, 
            agreementdate, contractor, agreementamount, latestmbnumber, latestmbdate, 
            latestbillnumber, latestbilldate, billtype, billamount, totalbillpaidsofar, 
            balancevalueofworktobill, spilloverflag, department,
            createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('seq_egw_mv_work_progress_register'), v_ward, v_location, v_workcategory, v_typeofslum, v_beneficiary, v_nameofwork, v_wincode, 
            v_fund, v_function, v_budgethead, v_typeofwork, v_subtypeofwork, v_adminsanctionby, 
            v_adminsanctiondate, v_adminsanctionamount, v_technicalsanctionby, 
            v_technicalsanctiondate, v_estimateamount, v_modeofallotment, v_agreementnumber, 
            v_agreementdate, v_contractor, v_agreementamount, v_latestmbnumber, v_latestmbdate, 
            v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount, v_totalbillpaidsofar, 
            v_balancevalueofworktobill, v_spilloverflag, v_department,
            1, now(), 1, now());

	end loop;
end;
$$ LANGUAGE plpgsql;