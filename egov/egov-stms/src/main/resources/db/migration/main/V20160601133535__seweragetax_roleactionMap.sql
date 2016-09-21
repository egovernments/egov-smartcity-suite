
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSewerageClosetsCheck','/ajaxconnection/check-closets-exists',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Ajax Sewerage Closets Check','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));


-----------------Users Role action Mapping--------------------

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Approver'),(select id from eg_action where name ='AjaxSewerageClosetsCheck' and contextroot = 'stms'));