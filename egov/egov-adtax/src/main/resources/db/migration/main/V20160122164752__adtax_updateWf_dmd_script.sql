
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='HoardingUpdate'));

update eg_wf_matrix set objecttype ='AdvertisementPermitDetail' where currentstate='Rejected' and additionalrule='CREATEADVERTISEMENT';


update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101')  where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Advertisement Tax' and module=(select id from eg_module where name='Advertisement Tax'));

update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1101101') where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Enchroachment Fee' and module=(select id from eg_module where name='Advertisement Tax'));


update EG_DEMAND_REASON set GLCODEID =(select ID from CHARTOFACCOUNTS where GLCODE = '1402002') where id_demand_reason_master in ( select id from eg_demand_reason_master 
where reasonmaster='Penalty' and module=(select id from eg_module where name='Advertisement Tax'));