Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'AjaxLoadRTGSNumberByAccountId','/voucher/common-ajaxLoadRTGSNumberByAccountId.action',(select id from eg_module where name='MIS Reports' ),1,'AjaxLoadRTGSNumberByAccountId',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxLoadRTGSNumberByAccountId'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'RtgsIssueRegisterReport-exportHtml','/report/rtgsIssueRegisterReport-exportHtml.action',(select id from eg_module where name='MIS Reports' ),1,'RtgsIssueRegisterReport-exportHtml',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='RtgsIssueRegisterReport-exportHtml'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'RtgsIssueRegisterReport-exportPdf','/report/rtgsIssueRegisterReport-exportPdf.action',(select id from eg_module where name='MIS Reports' ),1,'RtgsIssueRegisterReport-exportPdf',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='RtgsIssueRegisterReport-exportPdf'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'RtgsIssueRegisterReport-exportXls','/report/rtgsIssueRegisterReport-exportXls.action',(select id from eg_module where name='MIS Reports' ),1,'RtgsIssueRegisterReport-exportXls',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='RtgsIssueRegisterReport-exportXls'));




Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='RTGSIssueRegisterReport'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadAllBanksByFund'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='AjaxLoadRTGSNumberByAccountId'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='RtgsIssueRegisterReport-exportHtml'));


Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='RtgsIssueRegisterReport-exportPdf'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='RtgsIssueRegisterReport-exportXls'));



Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RTGSIssueRegisterReport'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajaxLoadAllBanksByFund'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='AjaxLoadRTGSNumberByAccountId'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RtgsIssueRegisterReport-exportHtml'));


Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RtgsIssueRegisterReport-exportPdf'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='RtgsIssueRegisterReport-exportXls'));



