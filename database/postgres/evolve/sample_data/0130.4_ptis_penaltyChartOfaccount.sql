
#UP
insert into CHARTOFACCOUNTS(ID, glcode, name,
isactiveforposting, parentid, lastmodified, modifiedby, created, 
purposeid,  type, classification, functionreqd)
values(SEQ_CHARTOFACCOUNTS.nextval,'180800100','MISCELLANEOUS INCOME-DISHONOURE CHEQUE COLLECTION CHARGES',0,
(select id from CHARTOFACCOUNTS  where glcode like '18080' and upper(name) like 'MISCELLANEOUS INCOME'),sysdate,1,sysdate,
(select ID from EGF_ACCOUNTCODE_PURPOSE where upper(name) like 'PTPENALTYCODE'),'I',2,0);


update eg_demand_reason set purposeid =(select ID from EGF_ACCOUNTCODE_PURPOSE where upper(name) like 'PTPENALTYCODE')
where  id_demand_reason_master =(select id from eg_demand_reason_master  where upper(code) like 'PENALTY'); 

#DOWN

delete CHARTOFACCOUNTS where glcode='180800100' and purposeid=(select ID from EGF_ACCOUNTCODE_PURPOSE where upper(name) like 'PTPENALTYCODE');

update eg_demand_reason set purposeid =null where  id_demand_reason_master =(select id from eg_demand_reason_master  where upper(code) like 'PENALTY');

