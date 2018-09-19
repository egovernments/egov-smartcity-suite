insert into chartofaccounts  VALUES (nextval('seq_chartofaccounts'),'1301026','Rent from Parks',null,true,(select id from chartofaccounts where glcode = '13010'), null, 'A', 'I',
null, 4, false, false, (select id from schedulemapping where schedule = 'I-03'),null, null,null, null, '130', null,null, now(), 1, now(), 1, 0);

insert into chartofaccounts  VALUES (nextval('seq_chartofaccounts'),'1301027','Rent and Lease of Pay & Use Toilets',null,true,(select id from chartofaccounts where glcode = '13010'), null, 'A', 'I',
null, 4, false, false, (select id from schedulemapping where schedule = 'I-03'),null, null,null, null, '130', null,null, now(), 1, now(), 1, 0);
