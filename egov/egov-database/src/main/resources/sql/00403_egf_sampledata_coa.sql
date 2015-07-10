insert into chartofaccounts (id,glcode,name,isactiveforposting,parentid,created,type,classification,majorcode,createdby,modifiedby) 
values(nextval('seq_chartofaccounts'),4311004,'Receivables for Property Taxes- Arrears',true,(select id from chartofaccounts where glcode='43110'),current_date,'A',4,431,1,1);


