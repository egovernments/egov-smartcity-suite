Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'ExpenseBillCreate','/bill/contingentBill-create',
(select id from eg_module where name='Bill Registers'),1,'ExpenseBillCreate',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseBillCreate'));

ALTER TABLE egf_budgetgroup ALTER COLUMN isactive TYPE boolean USING CASE WHEN isactive = 0 THEN FALSE WHEN isactive = 1 THEN TRUE ELSE NULL END;




