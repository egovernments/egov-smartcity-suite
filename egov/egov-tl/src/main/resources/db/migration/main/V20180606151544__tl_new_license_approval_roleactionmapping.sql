 --Create new license role action mapping
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Create New License Application',
'/license/create',null,(select id from EG_MODULE WHERE name = 'Trade License Transactions'),1,
'Create New License Application','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Create New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Create New License');

--License Approval role action mapping
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Show License Application for Approver',
'/license/update/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Show License Application for Approver','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Show License Application for Approver'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='View Trade License for Approval');

--forward License role action mapping

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Forward New License Application',
'/license/update/forward/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Forward New License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Forward New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--license approve role action mapping

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Approve New License Application',
'/license/update/approve/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Approve New License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Approve New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--license reject role action mapping

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Reject New License Application',
'/license/update/reject/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Reject New License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Reject New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--license cancel role action mapping

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Cancel New License Application',
'/license/update/cancel/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Cancel New License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Cancel New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--license certificate preview before digisign

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Application Certificate Preview',
'/license/update/preview/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Application Certificate Preview','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='License Application Certificate Preview'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--digital sign license application

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Digitaly Sign New License Application',
'/license/update/sign',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Digitaly Sign New License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='Digitaly Sign New License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

--license application acknowledgment

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Application Success Acknowledgment',
'/license/success/acknowledgement/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Application Success Acknowledgment','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='License Application Success Acknowledgment'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Create New License');

--license application provisional certificate from application

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Application Provisional Certificate',
'/license/update/generateCertificate/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Application Provisional Certificate','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT 
(SELECT id FROM eg_action WHERE name='License Application Provisional Certificate'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='NewTradeLicense-approve');

-- create new license application portal service

INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),
(select id from eg_module where name='Trade License'),'Apply for New License',null,0,'/tl/license/create','true',
'Apply for New License','true','true',null,(select id from eg_user where username='system'),now(),now(),
(select id from eg_user where username='system'));

--license Fee collection

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Fee Verification',
'/license/fee/verification/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Fee Verification','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT (SELECT id FROM eg_action WHERE name='License Fee Verification'), 
id from eg_role where name in('Collection Operator','SYSTEM');

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Fee Collection',
'/license/fee/collection/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Fee Collection','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT (SELECT id FROM eg_action WHERE name='License Fee Collection'),
id from eg_role where name in('Collection Operator','SYSTEM');

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License Fee Collection'),
(select feature FROM EG_FEATURE_ACTION WHERE action in(select id from eg_action where name='Trade License Bill Collect')));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License Fee Verification'),
(select feature FROM EG_FEATURE_ACTION WHERE action in(select id from eg_action where name='Trade License Bill Collect')));

--license final certificate from search

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Final Certificate',
'/license/certificate/original/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Final Certificate','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='License Final Certificate'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='View Trade License Generate Certificate');

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License Final Certificate'),
(select feature FROM EG_FEATURE_ACTION WHERE action in(select id from eg_action where name='View Trade License Generate Certificate')));

--license provisional certificate from search

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'License Provisional Certificate',
'/license/certificate/provisional/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'License Provisional Certificate','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='License Provisional Certificate'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='Generate Provisional Certificate');

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License Provisional Certificate'),
(select feature FROM EG_FEATURE_ACTION WHERE action in(select id from eg_action where name='Generate Provisional Certificate')));


--print acknowledgement
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Print License Acknowledgement',
'/license/print/ack',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'Print License Acknowledgement','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Print License Acknowledgement'),
(select id FROM eg_role  where name='CSC Operator'));

--feature for new license
INSERT INTO EG_FEATURE (id,name,description,module,version,enabled) VALUES
(nextval('SEQ_EG_FEATURE'),'Create License Application','Create License Application',
(select id from eg_module where name='Trade License'),0,true);
 
INSERT INTO EG_FEATURE_ACTION (feature,action) select(select id FROM EG_FEATURE WHERE name  ='Create License Application'),
id FROM eg_action WHERE name in('Create New License Application','License Application Success Acknowledgment','Load Block By Locality',
'getWardsByLocality','LicenseSubcategoryByCategory','LicenseSubcategoryDetailByFeeType');

--feature for license approval

INSERT INTO EG_FEATURE (id,name,description,module,version,enabled) VALUES
(nextval('SEQ_EG_FEATURE'),'New License Application Approval','New License Application Approval',
(select id from eg_module where name='Trade License'),0,true);
 
INSERT INTO EG_FEATURE_ACTION (feature,action) select(select id FROM EG_FEATURE WHERE name  ='New License Application Approval'),
id FROM eg_action WHERE name in('Show License Application for Approver','Forward New License Application',
'Approve New License Application','Reject New License Application','Cancel New License Application','License Application Certificate Preview',
'License Application Provisional Certificate','Digitaly Sign New License Application','View License Success',
'Load Block By Locality','getWardsByLocality','LicenseSubcategoryByCategory','LicenseSubcategoryDetailByFeeType',
'View Support Documents','License DCB View','Reassignment','AjaxDesignationsByDepartmentWithDesignation',
'AjaxApproverByDesignationAndDepartment');


update eg_wf_matrix set currentstate='Start' where additionalrule ='CSCOPERATORNEWLICENSE' and department  ='PUBLIC HEALTH AND SANITATION' ;
update eg_wf_matrix set currentstate='Start' where additionalrule ='CSCOPERATORRENEWLICENSE' and department  ='PUBLIC HEALTH AND SANITATION' ;

update egp_portalservice set isactive =false where code ='Create New License' and module in(select id from eg_module where name='Trade License');

update eg_action set enabled =false where name='Create New License' and contextroot='tl';

update eg_feature  set enabled =false where name in('Create New License','License Approval') and
module=(select id from eg_module where name='Trade License');