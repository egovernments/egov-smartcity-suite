DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Create New License') 
AND action= (select id from eg_action where name ='TL_NEW_LICENSE_APPLICATION_ACKNOWLEDGEMENT');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Create New License'),
(select id from eg_action where name ='TL_NEW_LICENSE_APPLICATION_ACKNOWLEDGEMENT'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Create New License') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Create New License'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Create Legacy License') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Create Legacy License'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'View Trade') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'View Trade'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Modify Legacy License') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Modify Legacy License'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Renew License') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Renew License'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Create Closure Application') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Create Closure Application'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'Closure Application Approval') 
AND action= (select id from eg_action where name ='File Download');

INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'Closure Application Approval'),
(select id from eg_action where name ='File Download'));

DELETE FROM eg_feature_action WHERE feature=(select id from eg_feature where name = 'License Approval') 
AND action= (select id from eg_action where name ='File Download');
INSERT INTO eg_feature_action(feature,action) VALUES
((select id from eg_feature where name = 'License Approval'),
(select id from eg_action where name ='File Download'));

UPDATE eg_action set url='/report/dailycollection' WHERE name='TL_REPORT_DAILY_COLLECTION';