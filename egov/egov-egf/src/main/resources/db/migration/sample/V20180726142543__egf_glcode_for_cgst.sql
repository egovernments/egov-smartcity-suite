insert into chartofaccounts  VALUES (nextval('seq_chartofaccounts'),'3502035','CGST on Rents',null,true,(select id from chartofaccounts where glcode = '35020'), null, 'A', 'L',
null, 4, false, false, (select id from schedulemapping where schedule = 'B-09'),null, null,null, null, '350', null,null, now(), 1, now(), 1, 0);

update chartofaccounts set name='SGST on Rents' where glcode='3502034' and name='GST on Rents';