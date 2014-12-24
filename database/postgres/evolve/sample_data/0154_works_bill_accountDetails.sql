#UP
UPDATE chartofaccounts SET purposeid=61 where glcode in(select glcode from chartofaccounts
WHERE majorcode='341' AND classification=4);

update chartofaccounts set purposeid=62 where glcode in ('2305901',
'2305902',
'2305906',
'2305301'
);
#DOWN
UPDATE chartofaccounts SET purposeid=null where glcode in(select glcode from chartofaccounts
WHERE majorcode='341' AND classification=4);
update chartofaccounts set purposeid=null where glcode in ('2305901',
'2305902',
'2305906',
'2305301'
);