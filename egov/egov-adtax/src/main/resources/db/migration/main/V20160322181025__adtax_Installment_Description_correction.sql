
update eg_installment_master set description='2015-16'  where ID_MODULE=(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null) and description='ADTAX/15-16';

update eg_installment_master set description='2016-17'  where ID_MODULE=(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null) and description='ADTAX/16-17';