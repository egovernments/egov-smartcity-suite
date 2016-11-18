-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'date wise Report','date wise Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'month wise Report','month wise Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'religion wise Report','religion wise Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'act wise Report','act wise Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'marital status Report','marital status Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Marriage Certificate Report','Marriage Certificate Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Age Wise Report','Age Wise Report',(select id from eg_module  where name = 'Marriage Registration'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'status Wise Report','status Wise Report',(select id from eg_module  where name = 'Marriage Registration'));


-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------

-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationdatewiseReport') ,(select id FROM eg_feature WHERE name = 'date wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationmonthwiseReport') ,(select id FROM eg_feature WHERE name = 'month wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationreligionwiseReport') ,(select id FROM eg_feature WHERE name = 'religion wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationactwiseReport') ,(select id FROM eg_feature WHERE name = 'act wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Act Wise Report View Details') ,(select id FROM eg_feature WHERE name = 'act wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Status at the time Marriage Report') ,(select id FROM eg_feature WHERE name = 'marital status Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Status at the time Marriage Report View Details') ,(select id FROM eg_feature WHERE name = 'marital status Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Marriage Registration Certificate Details Report') ,(select id FROM eg_feature WHERE name = 'Marriage Certificate Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View Marriage Registration') ,(select id FROM eg_feature WHERE name = 'Marriage Certificate Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Marriage Registration Age Wise Report') ,(select id FROM eg_feature WHERE name = 'Age Wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Age Wise Report View Details') ,(select id FROM eg_feature WHERE name = 'Age Wise Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RegistrationStatusReport') ,(select id FROM eg_feature WHERE name = 'status Wise Report'));


-----------------------------------------------------------ADDING FEATURE ACTION ENDS----------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ROLE BEGINS----------------------------------------------------


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'date wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'date wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'month wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'month wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'religion wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'religion wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'act wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'act wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'marital status Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'marital status Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Marriage Certificate Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'Marriage Certificate Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Age Wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'Age Wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'status Wise Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Marriage Registration RprtViewr') ,(select id FROM eg_feature WHERE name = 'status Wise Report'));



-----------------------------------------------------------ADDING ROLE ACTION STARTS----------------------------------------------------



INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Status at the time Marriage Report View Details'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Marriage Registration Certificate Details Report'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'Marriage Registration Age Wise Report'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration RprtViewr'), (SELECT id FROM eg_action WHERE name = 'RegistrationStatusReport'));


-----------------------------------------------------------ADDING ROLE ACTION ENDS----------------------------------------------------