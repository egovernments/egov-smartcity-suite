

update EG_ACTION set url='/voucher/common-ajaxGetAllCoaCodes.action',queryparams=null where name='ajax-process-coacodes';

update EG_ACTION set url='/voucher/common-ajaxGetAllFunctionName.action',queryparams=null where name='ajax-process-function';

update EG_ACTION set url='/voucher/common-ajaxGetAllLiabCodes.action',queryparams=null where name='loadAllLiablityCodes';

update EG_ACTION set url='/voucher/common-ajaxGetAllAssetCodes.action',queryparams=null where name='loadAllAssetCodes';

update EG_ACTION set url='/voucher/common-ajaxGetAllCoaCodesExceptCashBank.action',queryparams=null where name='ajax-process-getAllCoaCodesExceptCashBank';


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'CoaDetailCode','/voucher/common-ajaxCoaDetailCode.action',null,
(select id from eg_module where name='EGF-COMMON'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'GetAllBankName','/voucher/common-ajaxGetAllBankName.action',null,
(select id from eg_module where name='EGF-COMMON'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'GetAllCoaNames','/voucher/common-ajaxGetAllCoaNames.action',null,
(select id from eg_module where name='EGF-COMMON'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid)  select (select id from eg_role where name='Super User'), id from eg_action where name 
in('CoaDetailCode','GetAllBankName','GetAllCoaNames');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Voucher Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-coacodes'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Payment Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-coacodes'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Bill Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-coacodes'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Voucher Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-function'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Payment Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-function'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Bill Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-function'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Voucher Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-getAllCoaCodesExceptCashBank'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Payment Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-getAllCoaCodesExceptCashBank'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Bill Creator') ,(select id FROM eg_action  WHERE name = 'ajax-process-getAllCoaCodesExceptCashBank'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ajax-process-function'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ajax-process-function'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='CSC Operator'),(select id from eg_action where name='GetAllCoaNames'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='ULB Operator'),(select id from eg_action where name='GetAllCoaNames'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='CSC Operator'),(select id from eg_action where name='GetAllBankName'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES 
( (select id from eg_role where name='ULB Operator'),(select id from eg_action where name='GetAllBankName'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Financial Administrator') ,(select id FROM eg_action  WHERE name = 'loadAllLiablityCodes'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Financial Administrator') ,(select id FROM eg_action  WHERE name = 'CoaDetailCode'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Financial Administrator') ,(select id FROM eg_action  WHERE name = 'ajax-process-function'));


