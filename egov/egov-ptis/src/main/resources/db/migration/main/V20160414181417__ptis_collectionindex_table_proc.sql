drop table if exists egpt_collectionindex;

CREATE TABLE egpt_collectionindex (
receiptnumber character varying(25),
receiptdate timestamp without time zone,
createdDate timestamp without time zone,
modifieddate timestamp without time zone,
billingservice character varying(25),
paymentmode character varying(25),
arrearamount double precision,
arrearLibCess double precision,
currentamount double precision,
currentlibces double precision,
penalty double precision,
latepaymentcharges double precision,
totalamount double precision,
advanceamount double precision,
rebateamount double precision,
channel character varying(25),
paymentgateway character varying(25),
billnumber character varying(25),
consumercode character varying(25),
frominstallment character varying(25),
toinstallment character varying(25),
status character varying(25),
payeename character varying(150),
ulbname character varying(25),
districtname character varying(25),
regionname character varying(25),
createdBy bigint,
modifiedBy bigint
);


create or replace function dataForCollectionIndex()
	returns void as $$
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
	v_frominst varchar(25);
	v_toinst varchar(25);
	v_ulbname varchar(25);
	v_districtname varchar(25);
	v_regionname varchar(25);
	v_paymentgateway varchar(30);
    v_rebateamount double precision;
	v_status varchar(30);
begin
   select name into v_ulbname from eg_city;
   select districtname into v_districtname from eg_city;
   select regionname into v_regionname from eg_city;
   delete from egpt_collectionindex;
   for header in (select ch.*, status.description from egcl_collectionheader ch, egw_status status where ch.status=status.id and ch.servicedetails = (select id from egcl_servicedetails where upper(name) = 'PROPERTY TAX'))
	loop
	--raise notice 'header id (%)', v_rcptheadid;
	v_status := header.description;
	v_rcptheadid := header.id;
    v_paymentgateway := '';
	if(header.collectiontype = 'O') then
         select sd.name into v_paymentgateway from egcl_onlinepayments op,egcl_servicedetails sd where op.servicedetails = sd.id and op.collectionheader=v_rcptheadid;
	end if; 	
	--raise notice 'payment gateway (%)', v_paymentgateway;

	-- arrear tax without library cess
      select sum(cd.cramount) into v_arrearamount from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.chartofaccount = (select id from chartofaccounts where glcode = '4311004') and upper(cd.description) not like upper('Library Cess')||'%';
     --raise notice 'arrear amount (%)', v_arrearamount;
   
	--arrear library cess
      select sum(cd.cramount) into v_arrearlibces from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.chartofaccount = (select id from chartofaccounts where glcode = '4311004') and upper(cd.description) like upper('Library Cess')||'%';
      --raise notice 'arrear lib amount (%)', v_arrearlibces;

	-- current tax without library cess
      select sum(cd.cramount) into v_currentamount from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.chartofaccount in (select id from chartofaccounts where glcode in ('1100101','3503002','1402001'));
      --raise notice 'current amount (%)', v_currentamount;

	--current library cess
      select sum(cd.cramount) into v_currentlibcess from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.chartofaccount = (select id from chartofaccounts where glcode = '3503001') and upper(cd.description) like upper('Library Cess')||'%';
     --raise notice 'curr lib cess (%)', v_currentlibcess;

	--total penalty
     select sum(cd.cramount) into v_penalty from egcl_collectiondetails cd where cd.collectionheader=v_rcptheadid and cd.cramount>0 and cd.chartofaccount = (select id from chartofaccounts where glcode = '1402002');
     --raise notice 'penalty (%)', v_penalty;
   
	--ealry payment rebate
     select sum(cd.dramount) into v_rebateamount from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.dramount>0 and cd.chartofaccount = (select id from chartofaccounts where glcode ='2202103');
      --raise notice 'rebate (%)', v_rebateamount;

	--paid from installment
    select right(cd.description,11) into v_frominst from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.ordernumber =  (select min(ordernumber) from egcl_collectiondetails where collectionheader = v_rcptheadid);
    --raise notice 'from inst (%)', v_frominst;

	--paid TO installment
    select right(cd.description,11) into v_toinst from egcl_collectiondetails cd where v_rcptheadid=cd.collectionheader and cd.ordernumber = (select max(ordernumber) from egcl_collectiondetails where cramount> 0 and collectionheader = v_rcptheadid);
    --raise notice 'to installmetn (%) %', v_toinst ,v_rcptheadid;

    insert into egpt_collectionindex(receiptnumber, receiptdate, createdDate, modifieddate, billingservice, paymentmode, arrearamount, arrearLibCess, currentamount, currentlibces, latepaymentcharges, totalamount, rebateamount, channel, billnumber, consumercode, status, frominstallment, toinstallment, payeename, ulbname, districtname, regionname, paymentgateway, createdBy, modifiedBy) 
    select distinct(header.receiptnumber), header.receiptdate, header.createddate, header.lastmodifieddate, 'Property Tax', (CASE WHEN it.type ='cash' THEN 'cash' WHEN it.type='bank' THEN 'bank' WHEN it.type='card' THEN 'card' ELSE 'cheque/dd' END), v_arrearamount, v_arrearlibces, v_currentamount, v_currentlibcess, v_penalty, header.totalamount, v_rebateamount, header.source, header.referencenumber, header.consumercode, v_status, v_frominst, v_toinst, header.paidby, v_ulbname, v_districtname, v_regionname, v_paymentgateway, header.createdby, header.lastmodifiedby 
    from egcl_collectioninstrument ci, egf_instrumentheader ih, egf_instrumenttype it
    where ci.collectionheader=v_rcptheadid and ci.instrumentheader=ih.id and ih.instrumenttype=it.id;
    
	end loop;
	raise notice 'total count (%)', count(*) from egpt_collectionindex;
	exception 
		WHEN OTHERS THEN 
			raise exception 'error in inserting data : % % % %',v_rcptheadid,header.receiptdate,SQLERRM, SQLSTATE;

end;
$$ language plpgsql;

