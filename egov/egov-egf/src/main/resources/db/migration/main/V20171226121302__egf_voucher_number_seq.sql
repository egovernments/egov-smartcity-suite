
--sq_1_brv_201516
drop sequence if exists sq_1_brv_201516;
CREATE SEQUENCE  sq_1_brv_201516;
select setval('sq_1_brv_201516',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%brv%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%brv%2015-16') end as integer)+1));


--sq_1_cjv_201516
drop sequence if exists sq_1_cjv_201516;
CREATE SEQUENCE  sq_1_cjv_201516;
select setval('sq_1_cjv_201516',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%cjv%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%cjv%2015-16') end as integer)+1));


--sq_1_csl_201516
drop sequence if exists sq_1_csl_201516;
CREATE SEQUENCE  sq_1_csl_201516;
select setval('sq_1_csl_201516',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%csl%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%csl%2015-16') end as integer)+1));


--sq_1_csl_201617
drop sequence if exists sq_1_csl_201617;
CREATE SEQUENCE  sq_1_csl_201617;
select setval('sq_1_csl_201617',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%csl%2016-17') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%csl%2016-17') end as integer)+1));



--sq_1_gjv_201516
drop sequence if exists sq_1_gjv_201516;
CREATE SEQUENCE  sq_1_gjv_201516;
select setval('sq_1_gjv_201516',(cast(case when (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%gjv%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(vouchernumber,7,8)))) from voucherheader   where lower(vouchernumber) like '1%gjv%2015-16') end as integer)+1));

--sq_1_csl_cgvn_201516
drop sequence if exists sq_1_csl_cgvn_201516;
CREATE SEQUENCE  sq_1_csl_cgvn_201516;
select setval('sq_1_csl_cgvn_201516',(cast((case when (select (TRIM (LEADING '0' from max(substring(cgvn,11,10))))  from voucherheader   where lower(vouchernumber) like '1%csl%2015-16') is null then '0' else (select (TRIM (LEADING '0' from max(substring(cgvn,11,10))))  from voucherheader   where lower(vouchernumber) like '1%csl%2015-16') end) as integer)+1));




