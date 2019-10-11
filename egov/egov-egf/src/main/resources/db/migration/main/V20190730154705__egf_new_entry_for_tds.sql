insert into tds(id,type,glcodeid,recoveryname,isactive,createddate,createdby,version) 
values(nextval('seq_tds'),'3502064',(select id from chartofaccounts where glcode = '3502064'),'SGST on TDS',true,now(),1,0);

insert into tds(id,type,glcodeid,recoveryname,isactive,createddate,createdby,version) 
values(nextval('seq_tds'),'3502065',(select id from chartofaccounts where glcode = '3502065'),'CGST on TDS',true,now(),1,0);
