
--License Closure Approval
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'License Closure Approval','License Closure Approval',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='AjaxDesignationDropdown'),(select id FROM EG_FEATURE WHERE name  ='License Closure Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='AjaxApproverDropdown'),(select id FROM EG_FEATURE WHERE name  ='License Closure Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='viewclosurelicense'),(select id FROM EG_FEATURE  WHERE name  ='License Closure Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='updateclosurelicense'),(select id FROM EG_FEATURE  WHERE name  ='License Closure Approval'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('SYSTEM')),(select id FROM EG_FEATURE
WHERE name  ='License Closure Approval'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='License Closure Approval'));

--License Approval
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('seq_eg_feature'),'License Approval','License Approval',(select id from eg_module where name='Trade License'),0);
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='View Trade License for Approval'),(select id FROM EG_FEATURE WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='LicenseSubcategoryDetailByFeeType'),(select id FROM EG_FEATURE WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Load Block By Locality'),(select id FROM EG_FEATURE  WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='AjaxDesignationDropdown'),(select id FROM EG_FEATURE WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='AjaxApproverDropdown'),(select id FROM EG_FEATURE WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='NewTradeLicense-approve'),(select id FROM EG_FEATURE  WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('SYSTEM')),(select id FROM EG_FEATURE
WHERE name  ='License Approval'));
INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
WHERE name  ='License Approval'));

