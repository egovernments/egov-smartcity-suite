------------------START------------------
update egcl_servicedetails set fund=(select id from fund where code='01') where name='Advertisement Tax';

-------------------END----