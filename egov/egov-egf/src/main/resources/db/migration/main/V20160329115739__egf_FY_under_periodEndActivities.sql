

update eg_module set parentmodule =(select id from eg_module where name='Period End Activities') where name='Financial Masters Financial year';

delete from eg_roleaction where actionid =(select id from eg_action where name='Financial Year-SetUp');
delete from eg_action where name='Financial Year-SetUp';
