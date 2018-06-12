insert into chartofaccounts  VALUES (nextval('seq_chartofaccounts'),'1803001','Profit on Asset Sale/Disposal',null,true,(select id from chartofaccounts where glcode = '18030'), (select id from egf_accountcode_purpose where name ='ASSET_PROFIT'), 'A', 'I', null, 4, false, false, (select id from schedulemapping where schedule = 'I-09'),null, null,null, null, '180', null,null, now(), 1, now(), 1, 0);

insert into chartofaccounts  VALUES (nextval('seq_chartofaccounts'),'2711001','Loss on  Asset Sale/Disposal',null,true,(select id from chartofaccounts where glcode = '27110'), ((select id from egf_accountcode_purpose where name ='ASSET_LOSS')), 'A', 'E', null, 4, false, false, (select id from schedulemapping where schedule = 'I-17'),null, null,null, null, '271', null,null, now(), 1, now(), 1, 0);

update chartofaccounts  set purposeid = (select id from egf_accountcode_purpose  where  name ='Revaluation Reserve Account') where glcode='3126001';

update chartofaccounts  set purposeid = (select id from egf_accountcode_purpose where name ='Depreciation Expense Account') where glcode='2722000';

update chartofaccounts  set purposeid = (select id from egf_accountcode_purpose where name ='Accumulated Depreciation') where glcode in ('4112001','4112008','4112003','4112002','4112014','4112004','4112009','4112010','4112006','4112005','4112099','4112001','4112013',
'4112099','4112015','4112020');

update chartofaccounts set purposeid = (select id from egf_accountcode_purpose where name ='Fixed Assets') where glcode in ('4101001','4101002','4101003','4101004','4101005','4101099','4103207','4108003','4103211','4102001','4102008','4102003','4102002',
'4102014','4102004','4102009','4102010','4102006','4102005','4102099','4102011','4102013','4102015','4102020');

update chartofaccounts set purposeid = (select id from egf_accountcode_purpose where name ='Fixed Assets Written off') where glcode='4108099';

update chartofaccounts set purposeid = (select id from egf_accountcode_purpose where name ='ASSET_DEDUCTION') where glcode='2704003';


