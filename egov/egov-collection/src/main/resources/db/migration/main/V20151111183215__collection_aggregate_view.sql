create or replace view public.receipt_aggr_view
as 
select date(receiptdate) as receipt_date, text 'public' as ulb, sum(Totalamount) as total,count(*) as recordCount  from	public.egcl_collectionheader	where  status in (select id from	public.egw_status where moduletype='ReceiptHeader' and code in ('TO_BE_SUBMITTED','SUBMITTED','APPROVED','REMITTED')) group by receipt_date 
