
--sq_1_ejv_201516
drop sequence if exists sq_1_ejv_201516;
CREATE SEQUENCE  sq_1_ejv_201516;
select setval('sq_1_ejv_201516',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '%ejv%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '%ejv%2015-16') end as integer)+1));



--sq_1_bpv_201516
drop sequence if exists sq_1_bpv_201516;
CREATE SEQUENCE  sq_1_bpv_201516;
select setval('sq_1_bpv_201516',(cast((case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '%bpv%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '%bpv%2015-16') end) as integer)+1));



--sq_1_jvg_cgvn_201516
drop sequence if exists sq_1_jvg_cgvn_201516;
CREATE SEQUENCE  sq_1_jvg_cgvn_201516;
select setval('sq_1_jvg_cgvn_201516',(cast((case when (select (TRIM (LEADING '0' from max(substring(cgvn,11,10))))  from voucherheader   where vouchernumber like '%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(cgvn,11,10))))  from voucherheader   where vouchernumber like '%2015-16') end) as integer)+1));




