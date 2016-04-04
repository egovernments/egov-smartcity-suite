update eg_wf_types set module=(select id from eg_module where name='Advertisement Tax');
update egw_status set DESCRIPTION='Advertisement Tax Amount Paid' where code='ADTAXAMOUNTPAID';
update egw_status set DESCRIPTION='Advertisement Tax Permit Generated' where code='ADTAXPERMITGENERATED';
