
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot)
values(nextval('SEQ_EG_ACTION'),'Ajax Dishonored Cheque','/brs/ajaxDishonored-populateAccountCodes.action',
(select id from eg_module where name='BRS'),1,'Ajax Dishonored Cheque','f','EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_Action where name='Ajax Dishonored Cheque'));
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot)
values(nextval('SEQ_EG_ACTION'),'Dishonored Cheque Search','/brs/dishonoredCheque-search.action',
(select id from eg_module where name='BRS'),4,'Dishonored Cheque','t','EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_Action where name='Dishonored Cheque Search'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot)
values(nextval('SEQ_EG_ACTION'),'Dishonored Cheque List','/brs/dishonoredCheque-list.action',
(select id from eg_module where name='BRS'),4,'Dishonored Cheque List','f','EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_Action where name='Dishonored Cheque List'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot)
values(nextval('SEQ_EG_ACTION'),'Dishonoring Cheque','/brs/dishonoredCheque-dishonorCheque.action',
(select id from eg_module where name='BRS'),4,'Dishonoring Cheque','f','EGF');

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_Action where name='Dishonoring Cheque'));




