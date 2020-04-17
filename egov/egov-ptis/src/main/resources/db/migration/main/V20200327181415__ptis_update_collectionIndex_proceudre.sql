CREATE OR REPLACE FUNCTION loadcollectionindex(in_rcptheadid bigint, in_ulbname character varying, in_districtname character varying, in_regionname character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
declare
        header record;
        v_rcptheadid bigint;
        v_receiptnum varchar(25);
        v_receiptdate date;
        v_arrearamount double precision;
        v_arrearlibces double precision;
        v_currentamount double precision;
        v_currentlibcess double precision;
        v_penalty double precision;
        v_advance double precision;
        v_frominst varchar(25);
        v_toinst varchar(25);
        v_paymentgateway varchar(100);
        v_rebateamount double precision;
        v_status varchar(30);
        v_ptmf_id bigint;
        v_billingservice varchar(30);
	v_arrearedutax double precision;
	v_currentedutax double precision;
	v_wardname varchar(25);
	
begin
   for header in (select ch.*, status.description from egcl_collectionheader ch, egw_status status where ch.status=status.id and ch.servicedetails in (select id from egcl_servicedetails where upper(code) in ('PT', 'VLT', 'PTMF')) and ch.id=in_rcptheadid)
        loop
        v_status := header.description;
        v_rcptheadid := header.id;
        v_paymentgateway := '';
        --raise notice 'loadcollectionindex : header id (%)', v_rcptheadid;
        delete from egpt_collectionindex where receiptnumber=header.receiptnumber;
        if(header.collectiontype = 'O') then
         select sd.name into v_paymentgateway from egcl_onlinepayments op,egcl_servicedetails sd where op.servicedetails = sd.id and op.collectionheader=v_rcptheadid;

       end if;
        --raise notice 'loadcollectionindex : payment gateway (%)', v_paymentgateway;

        -- arrear tax without library cess
      select sum(cd.cramount) into v_arrearamount from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='ARREAR_AMOUNT' and upper(cd.description) not like upper('Library Cess')||'%' and upper(cd.description) not like upper('Education Tax')||'%';
     --raise notice 'arrear amount (%)', v_arrearamount;

        --arrear library cess
      select sum(cd.cramount) into v_arrearlibces from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='ARREAR_AMOUNT' and upper(cd.description) like upper('Library Cess')||'%';
      --raise notice 'arrear lib amount (%)', v_arrearlibces;

        -- current tax without library cess
      select sum(cd.cramount) into v_currentamount from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='CURRENT_AMOUNT' and upper(cd.description) not like upper('Library Cess')||'%' and upper(cd.description) not like upper('Education Tax')||'%';
      --raise notice 'current amount (%)', v_currentamount;

        --current library cess
      select sum(cd.cramount) into v_currentlibcess from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='CURRENT_AMOUNT' and upper(cd.description) like upper('Library Cess')||'%';
     --raise notice 'curr lib cess (%)', v_currentlibcess;

  --arrear education tax
      select sum(cd.cramount) into v_arrearedutax from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='ARREAR_AMOUNT' and upper(cd.description) like upper('Education Tax')||'%';
      --raise notice 'arrear lib amount (%)', v_arrearedutax;

  --current education tax
      select sum(cd.cramount) into v_currentedutax from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose='CURRENT_AMOUNT' and upper(cd.description) like upper('Education Tax')||'%';
      --raise notice 'arrear lib amount (%)', v_currentedutax;

        --total penalty
     select sum(cd.cramount) into v_penalty from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.purpose in ('CURRENT_LATEPAYMENT_CHARGES','ARREAR_LATEPAYMENT_CHARGES');
     --raise notice 'penalty (%)', v_penalty;

        --ealry payment rebate
     select sum(cd.dramount) into v_rebateamount from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.dramount>0 and cd.purpose = 'REBATE';
      --raise notice 'rebate (%)', v_rebateamount;

        --advance collection
     select sum(cd.cramount) into v_advance from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.cramount>0 and cd.purpose = 'ADVANCE_AMOUNT';
      --raise notice 'advance (%)', v_advance;

    select id into v_ptmf_id from egcl_servicedetails where upper(code) = 'PTMF';

    if (header.servicedetails != v_ptmf_id) then

    --paid from installment
    select right(cd.description,11) into v_frominst from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.ordernumber = 
 (select min(ordernumber) from egcl_collectiondetails where collectionheader = v_rcptheadid);
    --raise notice 'from inst (%)', v_frominst;

    --paid TO installment
    select right(cd.description,11) into v_toinst from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.ordernumber = (select max(ordernumber) from egcl_collectiondetails where cramount> 0 and collectionheader = v_rcptheadid);
    --raise notice 'to installmetn (%) %', v_toinst ,v_rcptheadid;

    end if;

    select name into v_billingservice from egcl_servicedetails where id = header.servicedetails;

	select name into v_wardname from egpt_mv_propertyinfo  prop,eg_boundary bd where  prop.wardid=bd.id and prop.upicno = header.consumercode;

    insert into egpt_collectionindex(receiptnumber, receiptdate, createdDate, modifieddate, billingservice, paymentmode, arrearamount, arrearLibCess, currentamount, currentlibces, latepaymentcharges, totalamount, rebateamount, advanceamount, channel, billnumber, consumercode, status, frominstallment, toinstallment, payeename, ulbname, districtname, regionname, paymentgateway, createdBy, modifiedBy, revenueward, arrearedutax, currentedutax)
    select distinct(header.receiptnumber), header.receiptdate, header.createddate, header.lastmodifieddate, v_billingservice, it.type, v_arrearamount, v_arrearlibces, v_currentamount, v_currentlibcess, v_penalty, header.totalamount, v_rebateamount, v_advance, header.source, header.referencenumber, header.consumercode, v_status, v_frominst, v_toinst, header.paidby, in_ulbname, in_districtname, in_regionname, v_paymentgateway, header.createdby, header.lastmodifiedby, v_wardname, v_arrearedutax, v_currentedutax
    from egcl_collectioninstrument ci, egf_instrumentheader ih, egf_instrumenttype it
    where ci.collectionheader=v_rcptheadid and ci.instrumentheader=ih.id and ih.instrumenttype=it.id;
   
        end loop;
        --raise notice 'total count (%)', count(*) from egpt_collectionindex;
        return 1;
        exception
                WHEN OTHERS THEN
                        raise exception 'error in inserting data : % % % %',v_rcptheadid, header.receiptdate, SQLERRM, SQLSTATE;
end;
$function$;
