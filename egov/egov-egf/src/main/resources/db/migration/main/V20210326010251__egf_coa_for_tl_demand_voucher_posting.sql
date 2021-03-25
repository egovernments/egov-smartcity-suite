INSERT into chartofaccounts VALUES (nextval('seq_chartofaccounts'),'4313006','Arrear Receivables for Trade License','Arrear Receivables for Trade License',true,(select id from chartofaccounts where glcode = '43130'), null, 'A', 'A', null, 4, false, false, null,null, null,null, null, '431', null,null, now(), 1, now(), 1, 0);

INSERT into chartofaccounts VALUES (nextval('seq_chartofaccounts'),'4313007','Current Receivables for Trade License','Current Receivables for Trade License',true,(select id from chartofaccounts where glcode = '43130'), null, 'A', 'A', null, 4, false, false, null,null, null,null, null, '431', null,null, now(), 1, now(), 1, 0);


