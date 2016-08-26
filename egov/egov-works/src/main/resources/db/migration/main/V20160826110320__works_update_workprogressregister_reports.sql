CREATE OR REPLACE FUNCTION egwupdateworkprogressmatview()
  RETURNS void AS $$

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
  v_checkworkordernumber1 character varying(256);
  v_checkworkordernumber2 character varying(256);
  v_checkboqpresent double precision;
  v_workcompleted boolean;
  v_boqpresent boolean;
  v_loanotcreated boolean;
  v_workcommenced boolean;
  v_workstatus character varying(256);
  v_check boolean;
  v_milestonepercentagecompleted numeric(13,2);
  v_estimatestatuscode character varying(50);
  v_estimatevalue double precision;
  v_workvalue double precision;
  v_technicalsanctionby double precision;
  v_technicalsanctiondate timestamp without time zone;
  v_contractorname character varying(100);
  v_contractorcode character varying(50);
  v_boundarynum bigint;
  v_woofflinestatuscode character varying(50);
  v_workorderid bigint;
  
 
  lineestimatedetails record;
  billdetailsdetails record;
BEGIN
    delete from egw_mv_billdetail;
    delete from egw_mv_work_progress_register;
    for lineestimatedetails in (select distinct led.id, led.estimatenumber,le.ward, le.location, le.workcategory, le.beneficiary, led.nameofwork, pc.code, le.fund, le.function, le.budgethead, le.typeofwork, le.subtypeofwork, le.adminsanctionby, le.adminsanctiondate, led.estimateamount, le.modeofallotment, le.spilloverflag,le.billscreated,le.executingdepartment,led.grossamountbilled ,le.scheme ,le.subscheme,le.natureofwork,le.id as leid,led.id ledid,status.code as lineestimatestatus,dept.name as departmentName,le.workordercreated,tow.description as typeofworkname,stow.description as subtypeofworkname from egw_lineestimate_details as led, egw_lineestimate as le left outer join egw_typeofwork stow on stow.id = le.subtypeofwork,egw_status as status,eg_department dept,egw_projectcode pc,egw_typeofwork tow where tow.id = le.typeofwork and  pc.id = led.projectcode and le.status = status.id and le.id = led.lineestimate and le.executingdepartment = dept.id and status.code in ('ADMINISTRATIVE_SANCTIONED', 'TECHNICAL_SANCTIONED') )
    loop
        v_id := lineestimatedetails.id;
        v_estimatenumber := lineestimatedetails.estimatenumber;
        v_totalbillpaidsofar := 0;
        v_grossamountbilled := lineestimatedetails.grossamountbilled;
        v_agreementamount := 0;
        v_balancevalueofworktobill := 0;
	v_spilloverbillscreatedflag := lineestimatedetails.billscreated;

	select estimatestatus.code,estimate.estimatevalue,estimate.workvalue, techsan.technicalsanctionby,
		techsan.technicalsanctiondate into v_estimatestatuscode ,v_estimatevalue,v_workvalue, v_technicalsanctionby,
			v_technicalsanctiondate from egw_abstractestimate as estimate join egw_estimate_technicalsanction techsan
				on estimate.id = techsan.abstractestimate,egw_status estimatestatus, egw_lineestimate_details as led, egw_lineestimate
					as le where estimate.status = estimatestatus.id and estimate.estimatenumber = v_estimatenumber
						and estimatestatus.code in ('ADMIN_SANCTIONED','TECH_SANCTIONED') and estimate.estimatenumber = led.estimatenumber
							and le.id = led.lineestimate and techsan.createddate = (select max(createddate)
								from egw_estimate_technicalsanction tech where tech.abstractestimate = estimate.id);

        select wo.workorder_number, wo.workorder_date, wo.contractor_id, COALESCE(wo.workorder_amount,0), wostatus.code, contractor.name, contractor.code,wo.id into v_agreementnumber, v_agreementdate, v_contractor, v_agreementamount,v_wostatuscode,v_contractorname,v_contractorcode, v_workorderid from egw_workorder as wo,egw_status wostatus,egw_contractor contractor where wo.status_id = wostatus.id and wostatus.code= 'APPROVED' and wo.estimatenumber = v_estimatenumber and contractor.id = wo.contractor_id;

select wo.workorder_number, wo.workorder_date, wo.contractor_id, COALESCE(wo.workorder_amount,0),wostatus.code into v_agreementnumber, v_agreementdate, v_contractor, v_agreementamount,v_wostatuscode from egw_workorder as wo,egw_status wostatus where wo.status_id = wostatus.id and wostatus.code= 'APPROVED' and wo.estimatenumber = v_estimatenumber;

select boundary.boundarynum into v_boundarynum from eg_boundary boundary where boundary.id = lineestimatedetails.ward;

select status.code into v_woofflinestatuscode from egw_offline_status offlinestatus,egw_status status where 
offlinestatus.status_id = (select max(status_id) from egw_offline_status  status where status.object_type = 'WorkOrder' and 
status.object_id = v_workorderid) and offlinestatus.status_id = status.id and
offlinestatus.object_id = v_workorderid ;

SELECT DISTINCT wo.workorder_number into v_checkworkordernumber FROM egw_workorder wo ,egw_status status WHERE EXISTS (SELECT 1 FROM egw_contractorbill cb,egw_workorder_estimate woe, eg_billregister br WHERE cb.id=br.id and cb.workorderestimate = woe.id and wo.id = woe.workorder_id AND br.billstatus  = 'APPROVED' AND br.billtype = 'Final Bill' ) and wo.status_id=status.ID AND wo.workorder_number = v_agreementnumber AND status.code= 'APPROVED' ;
                      
	IF v_checkworkordernumber != '' THEN 
	    v_workcompleted = true;
	ELSE
	    v_workcompleted = false;
	END IF;

select count(ea.id) into v_checkboqpresent from egw_estimate_activity ea,egw_abstractestimate as ae,egw_status aestatus where ea.abstractestimate = ae.id and ae.status = aestatus.id and  aestatus.code in ('ADMIN_SANCTIONED','TECH_SANCTIONED') and ae.estimatenumber = v_estimatenumber;
                      
	IF v_checkboqpresent > 0 THEN 
	    v_boqpresent = true;
	ELSE
	    v_boqpresent = false; 
	END IF;
	v_workstatus = '';
	IF (v_boqpresent) THEN 

	     	select wo.workorder_number into v_checkworkordernumber1 from egw_workorder as wo,egw_status wostatus where wo.status_id = wostatus.id and wo.estimatenumber = v_estimatenumber and wostatus.code = 'APPROVED' and lineestimatedetails.lineestimatestatus = 'ADMINISTRATIVE_SANCTIONED' ;
                      
		IF v_checkworkordernumber1 != '' THEN 
		    v_loanotcreated = false;
		ELSE
		    v_workstatus = 'LOA Not Created'; 
		    v_loanotcreated = true;
		END IF;

		select wo.workorder_number into v_checkworkordernumber2 from egw_workorder_estimate  as woe,egw_workorder wo,egw_abstractestimate ae  where woe.abstractestimate_id = ae.id and ae.status in (select id from egw_status where moduletype = 'AbstractEstimate' and code in ('ADMIN_SANCTIONED')) and  woe.workorder_id = wo.id and status_id = (select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED') and wo.workorder_number = v_agreementnumber and woe.workorder_id = (select distinct(os.object_id) from egw_offline_status  as os,egw_status egwstatus where os.status_id = egwstatus.id and os.id = (select max(status.id) from egw_offline_status  status where status.object_type = 'WorkOrder' and status.object_id = woe.workorder_id) and os.object_id = woe.workorder_id and lower(egwstatus.code) = 'work_commenced' and os.object_type = 'WorkOrder' );

                IF v_checkworkordernumber2 != '' THEN 
		    v_workcommenced = true;
		ELSE
		     IF v_workstatus = '' THEN 
		    	v_workstatus = 'Not Commenced'; 
		     END IF;
		    v_workcommenced = false;
		END IF;

		IF (lineestimatedetails.spilloverflag) THEN 
		    IF (v_workcommenced=true or lineestimatedetails.workordercreated=true) and (v_workcompleted!=true) THEN 
			    v_workstatus = 'In Progress'; 
		    END IF;
		ELSE
		   IF (v_workcommenced) and (v_workcompleted!=true) THEN 
			IF v_workstatus = '' THEN 
			    v_workstatus = 'In Progress'; 
			END IF;
		    END IF;
		END IF;

		IF (v_workcompleted) THEN 
		    v_workstatus = 'Completed'; 
		END IF;

	ELSE
	
		select wo.workorder_number into v_checkworkordernumber1 from egw_workorder as wo,egw_status wostatus where wo.status_id = wostatus.id and wo.estimatenumber = v_estimatenumber and wostatus.code = 'APPROVED' and lineestimatedetails.lineestimatestatus in ('ADMINISTRATIVE_SANCTIONED', 'TECHNICAL_SANCTIONED') ;
                      
		IF v_checkworkordernumber1 != '' THEN 
		    v_loanotcreated = false;
		ELSE
		    v_workstatus = 'LOA Not Created'; 
		    v_loanotcreated = true;
		END IF;

		select wo.workorder_number into v_checkworkordernumber2 from egw_workorder_estimate  as woe,egw_workorder wo,egw_abstractestimate ae  where woe.abstractestimate_id = ae.id and ae.status in (select id from egw_status where moduletype = 'AbstractEstimate' and code in ('ADMIN_SANCTIONED')) and  woe.workorder_id = wo.id and status_id in (select id from egw_status where moduletype = 'WorkOrder' and code not in ('APPROVED','CREATED')) and wo.workorder_number = v_agreementnumber;

                IF v_checkworkordernumber2 != '' THEN 
		    v_workcommenced = true;
		ELSE
		    IF v_workstatus = '' THEN 
		    	v_workstatus = 'Not Commenced'; 
		     END IF;
		    v_workcommenced = false;
		END IF;

		IF (lineestimatedetails.spilloverflag) THEN 
		    IF (v_wostatuscode = 'APPROVED' or lineestimatedetails.workordercreated=true) and (v_workcompleted!=true) THEN 
			    v_workstatus = 'In Progress'; 
		    END IF;
		ELSE
		   IF v_wostatuscode = 'APPROVED' and (v_workcompleted!=true) THEN 
			IF v_workstatus = '' THEN 
			    v_workstatus = 'In Progress'; 
			END IF;
		    END IF;
		END IF;

		IF (v_workcompleted) THEN 
		    v_workstatus = 'Completed'; 
		END IF;
	    
	END IF;
	
	select br.billnumber, br.billdate, br.billtype, br.billamount, mb.mb_refno, mb.mb_date into v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount, v_latestmbnumber, v_latestmbdate from egw_workorder_estimate woe,eg_billregister as br
            join egw_contractorbill as con on br.id = con.id join egw_mb_header as mb on br.id = mb.billregister_id where con.workorderestimate = woe.id and woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED')) and con.billsequencenumber = (select max(conn.billsequencenumber) from egw_workorder_estimate woe,egw_contractorbill as conn join eg_billregister as brr on brr.id = conn.id where
conn.workorderestimate = woe.id and woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED')) and brr.billstatus = 'APPROVED');

	IF (v_boqpresent) THEN
        select mb.mb_refno, mb.mb_date into v_latestmbnumber, v_latestmbdate from egw_mb_header as mb where
        	mb.createddate = (select max(mbheader.createddate) from egw_mb_header as mbheader where 
        		mbheader.workorder_estimate_id in (select id from egw_workorder_estimate as woe where 
        			woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and 
        				status_id = (select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))) 
        					and status_id = (select id from egw_status where moduletype = 'MBHeader' and code = 'APPROVED'));
	END IF;

 select COALESCE(sum(br.billamount),0) into v_totalbillpaidsofar from egw_workorder_estimate woe, eg_billregister as br
            join egw_contractorbill as con on br.id = con.id
                left outer join eg_billregistermis bill_reg_mis on bill_reg_mis.billid=br.id
                    left outer join voucherheader bill_voucher_hdr on bill_reg_mis.voucherheaderid=bill_voucher_hdr.id
                        left outer join miscbilldetail misc_bill_dtl on misc_bill_dtl.billvhid=bill_voucher_hdr.id
                            left outer join voucherheader paymentvoucher on paymentvoucher.id = misc_bill_dtl.billvhid
                            where paymentvoucher.status = 0 and bill_voucher_hdr.status = 0 and con.workorderestimate = woe.id and woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED';

select COALESCE(sum(br.billamount),0) into v_totalbillamount from egw_workorder_estimate woe,eg_billregister as br
            join egw_contractorbill as con on br.id = con.id where
                con.workorderestimate = woe.id and woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED';


	for billdetailsdetails in(select woe.workorder_id,br.id ,br.billnumber,br.billdate,br.billamount,br.billtype from egw_workorder_estimate woe,eg_billregister as br join egw_contractorbill as con on br.id = con.id where
                con.workorderestimate = woe.id and woe.workorder_id = (select id from egw_workorder where workorder_number = v_agreementnumber and status_id = (
                                select id from egw_status where moduletype = 'WorkOrder' and code = 'APPROVED'))
                                    and br.billstatus = 'APPROVED') 
	loop
 INSERT INTO egw_mv_billdetail(
            ledid, workorder,billid, billnumber, billdate, billamount,billtype)
    VALUES (lineestimatedetails.id,billdetailsdetails.workorder_id,billdetailsdetails.id,billdetailsdetails.billnumber,billdetailsdetails.billdate,billdetailsdetails.billamount,billdetailsdetails.billtype);
	end loop;

       
        v_check := false;
	SELECT true into v_check FROM egw_workorder wo ,egw_status wostatus,egw_milestone ms left join egw_track_milestone tms on ms.id = tms.milestone,egw_workorder_estimate woest WHERE tms.id is null and ms.workorderestimate = woest.id and woest.workorder_id = wo.id and wo.status_id=wostatus.id and wo.workorder_number = v_agreementnumber and wostatus.code= 'APPROVED' ;
     

	IF (v_check) THEN 
	v_milestonepercentagecompleted := 0;
	ELSE
		SELECT DISTINCT tms.totalpercentage into v_milestonepercentagecompleted FROM egw_workorder wo ,egw_status wostatus,egw_milestone ms ,egw_track_milestone tms,egw_workorder_estimate woest WHERE  ms.id = tms.milestone and ms.workorderestimate = woest.id and woest.workorder_id = wo.id and ms.status = (select id from egw_status where moduletype = 'Milestone' and code = 'APPROVED') and wo.status_id=wostatus.id and wo.workorder_number = v_agreementnumber and wostatus.code = 'APPROVED' ;    
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

        INSERT INTO egw_mv_work_progress_register(id, ward, location, workcategory,  beneficiary, nameofwork, wincode,fund, function, budgethead, typeofwork, subtypeofwork, adminsanctionby,adminsanctiondate, adminsanctionamount, technicalsanctionby, technicalsanctiondate, modeofallotment, agreementnumber,agreementdate, contractor, agreementamount, latestmbnumber, latestmbdate,latestbillnumber, latestbilldate, billtype, billamount, totalbillamount,totalbillpaidsofar, balancevalueofworktobill, spilloverflag, department,scheme,subscheme,natureofwork,leid,ledid,lineestimatestatus,departmentName,wostatuscode, workordercreated,workcompleted,typeofworkname,subtypeofworkname,milestonepercentagecompleted,createdby, createddate, lastmodifiedby,lastmodifieddate,estimatestatuscode,estimatevalue,workvalue,workstatus, estimatenumber,contractorname,contractorcode,boundarynum, woofflinestatuscode, boqexists) VALUES (nextval('seq_egw_mv_work_progress_register'), lineestimatedetails.ward, lineestimatedetails.location,lineestimatedetails.workcategory,  lineestimatedetails.beneficiary,lineestimatedetails.nameofwork, lineestimatedetails.code,lineestimatedetails.fund, lineestimatedetails.function, lineestimatedetails.budgethead, lineestimatedetails.typeofwork,lineestimatedetails.subtypeofwork, lineestimatedetails.adminsanctionby,            lineestimatedetails.adminsanctiondate, lineestimatedetails.estimateamount,v_technicalsanctionby, v_technicalsanctiondate,lineestimatedetails.modeofallotment,v_agreementnumber,v_agreementdate, v_contractor, v_agreementamount, v_latestmbnumber, v_latestmbdate,v_latestbillnumber, v_latestbilldate, v_billtype, v_billamount, v_totalbillamount,v_totalbillpaidsofar, v_balancevalueofworktobill, lineestimatedetails.spilloverflag, lineestimatedetails.executingdepartment,lineestimatedetails.scheme,lineestimatedetails.subscheme,lineestimatedetails.natureofwork,lineestimatedetails.leid,lineestimatedetails.ledid,lineestimatedetails.lineestimatestatus, lineestimatedetails.departmentName,v_wostatuscode,lineestimatedetails.workordercreated,v_workcompleted,lineestimatedetails.typeofworkname,lineestimatedetails.subtypeofworkname,
v_milestonepercentagecompleted,1, now(), 1, now(),v_estimatestatuscode,v_estimatevalue,v_workvalue,v_workstatus, v_estimatenumber,v_contractorname,v_contractorcode,v_boundarynum,v_woofflinestatuscode, v_boqpresent);

    end loop;
end;
$$ LANGUAGE plpgsql;