------------------------------------ADDING FEATURE STARTS------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Common Search Application','Common Search Application',(select id from eg_module where name='Common'));
------------------------------------ADDING FEATURE ENDS------------------------


------------------------------------ADDING FEATURE ACTION STARTS------------------------
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ElasiticapplicationSearch') ,(select id FROM eg_feature WHERE name = 'Common Search Application'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'autopopulateApplicationType') ,(select id FROM eg_feature WHERE name = 'Common Search Application'));
------------------------------------ADDING FEATURE ACTION ENDS------------------------

------------------------------------ADDING FEATURE ROLE STARTS------------------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'WC_VIEW_ACCESS_ROLE') ,(select id FROM eg_feature WHERE name = 'Common Search Application'));
------------------------------------ADDING FEATURE ROLE ENDS------------------------
