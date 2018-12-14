UPDATE eg_action SET name='GRIEVANCE_TYPE_BY_NAME', url='/complainttype/by-name', queryparamregex='^complaintTypeName=[A-Za-z ]{1,150}+$'
WHERE name='GRIEVANCE_REGISTER_CITIZEN_TYPE_DROPDOWN';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action WHERE name IN ('REPORT_GRIEVANCE_TYPE_DROPDOWN',
                                         'GRIEVANCE_REGISTER_OFFICIAL_TYPE_DROPDOWN',
                                         'ESCALATION_TIME_GRIEVANCE_TYPE_DROPDOWN',
                                        'REPORT_GRIEVANCE_TYPEWISE_TYPE_DROPDOWN',
                                        'ROUTER_GRIEVANCETYPE_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='GRIEVANCE_TYPE_BY_NAME') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name IN ('REPORT_GRIEVANCE_TYPE_DROPDOWN',
                                         'GRIEVANCE_REGISTER_OFFICIAL_TYPE_DROPDOWN',
                                         'ESCALATION_TIME_GRIEVANCE_TYPE_DROPDOWN',
                                        'REPORT_GRIEVANCE_TYPEWISE_TYPE_DROPDOWN',
                                        'ROUTER_GRIEVANCETYPE_DROPDOWN'));

UPDATE eg_feature_action SET action = (SELECT id FROM eg_action WHERE name='GRIEVANCE_TYPE_BY_NAME')
WHERE action IN (SELECT id FROM eg_action WHERE name IN ('REPORT_GRIEVANCE_TYPE_DROPDOWN',
                                         'GRIEVANCE_REGISTER_OFFICIAL_TYPE_DROPDOWN',
                                         'ESCALATION_TIME_GRIEVANCE_TYPE_DROPDOWN',
                                        'REPORT_GRIEVANCE_TYPEWISE_TYPE_DROPDOWN',
                                        'ROUTER_GRIEVANCETYPE_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('REPORT_GRIEVANCE_TYPE_DROPDOWN',
                                         'GRIEVANCE_REGISTER_OFFICIAL_TYPE_DROPDOWN',
                                         'ESCALATION_TIME_GRIEVANCE_TYPE_DROPDOWN',
                                        'REPORT_GRIEVANCE_TYPEWISE_TYPE_DROPDOWN',
                                        'ROUTER_GRIEVANCETYPE_DROPDOWN');


UPDATE eg_action SET name='GRIEVANCE_TYPE_BY_CATEGORY', url='/complainttype/by-category'
WHERE name='GRIEVANCE_REGISTER_CITIZEN_TYPE_BY_CATEGORY_DROPDOWN';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN',
                                        'GRIEVANCE_REGISTER_THIRDPARTY_TYPE_BY_CATEGORY_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='GRIEVANCE_TYPE_BY_CATEGORY') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action 
                   WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN',
                                        'GRIEVANCE_REGISTER_THIRDPARTY_TYPE_BY_CATEGORY_DROPDOWN'));
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='GRIEVANCE_TYPE_BY_CATEGORY')
WHERE action IN (SELECT id FROM eg_action 
                 WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN',
                                        'GRIEVANCE_REGISTER_THIRDPARTY_TYPE_BY_CATEGORY_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_TYPE_BY_CATEGORY_DROPDOWN',
                                        'GRIEVANCE_REGISTER_THIRDPARTY_TYPE_BY_CATEGORY_DROPDOWN');
                                        
UPDATE eg_action SET name='GRIEVANCE_REGISTER_CHECK_CRN_REQUIRED', url='/complaint/crn-required'
WHERE name='GRIEVANCE_REGISTER_CRN_REQUIRED_CHECK';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action WHERE name = 'THIRDPARTY_GRIEVANCE_CRN_REQUIRED_CHECK')) roles,
(SELECT id FROM eg_action WHERE name='GRIEVANCE_REGISTER_CHECK_CRN_REQUIRED') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction
WHERE actionid IN (SELECT id FROM eg_action
                   WHERE name = 'THIRDPARTY_GRIEVANCE_CRN_REQUIRED_CHECK');
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='GRIEVANCE_REGISTER_CHECK_CRN_REQUIRED')
WHERE action IN (SELECT id FROM eg_action WHERE name = 'THIRDPARTY_GRIEVANCE_CRN_REQUIRED_CHECK');
                                        
DELETE FROM eg_action WHERE name = 'THIRDPARTY_GRIEVANCE_CRN_REQUIRED_CHECK';


UPDATE eg_action SET name='GRIEVANCE_REGISTER_LOCATIONS', url='/complaint/locations', queryparamregex='^locationName=[\w\s()/-]{1,512}+$'
WHERE name='GRIEVANCE_REGISTER_CITIZEN_LOCATION_DROPDOWN';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_LOCATION_DROPDOWN',
                                   'GRIEVANCE_THIRDPARTY_LOCATION_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='GRIEVANCE_REGISTER_LOCATIONS') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action 
                   WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_LOCATION_DROPDOWN',
                                   'GRIEVANCE_THIRDPARTY_LOCATION_DROPDOWN'));
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='GRIEVANCE_REGISTER_LOCATIONS')
WHERE action IN (SELECT id FROM eg_action 
                 WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_LOCATION_DROPDOWN',
                                   'GRIEVANCE_THIRDPARTY_LOCATION_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('GRIEVANCE_REGISTER_OFFICIAL_LOCATION_DROPDOWN',
                                   'GRIEVANCE_THIRDPARTY_LOCATION_DROPDOWN');
                                   
DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action 
                   WHERE name IN ('GRIEVANCE_REGISTER_LOCATION_REQUIRED_CHECK',
                                  'GRIEVANCE_REGISTER_OFFICIAL_LOCATION_REQUIRED_CHECK'));
DELETE FROM eg_feature_action
WHERE action IN (SELECT id FROM eg_action 
                   WHERE name IN ('GRIEVANCE_REGISTER_LOCATION_REQUIRED_CHECK',
                                  'GRIEVANCE_REGISTER_OFFICIAL_LOCATION_REQUIRED_CHECK'));
                                  
DELETE FROM eg_action 
                   WHERE name IN ('GRIEVANCE_REGISTER_LOCATION_REQUIRED_CHECK',
                                  'GRIEVANCE_REGISTER_OFFICIAL_LOCATION_REQUIRED_CHECK');


UPDATE eg_action SET name='GRIEVANCE_SEARCH' WHERE name='GRIEVANCE_SEARCH_OFFICIAL';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name ='GRIEVANCE_SEARCH_ANONYMOUS')) roles,
(SELECT id FROM eg_action WHERE name='GRIEVANCE_SEARCH') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name ='GRIEVANCE_SEARCH_ANONYMOUS');
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='GRIEVANCE_SEARCH')
WHERE action IN (SELECT id FROM eg_action WHERE name ='GRIEVANCE_SEARCH_ANONYMOUS');
                                        
DELETE FROM eg_action WHERE name ='GRIEVANCE_SEARCH_ANONYMOUS';

UPDATE eg_action SET name='GRIEVANCE_UPDATE_PROCESS_OWNERS', url='/grievance/process-owners', 
queryparams=null WHERE name='GRIEVANCE_UPDATE_WF_POSITION_DROPDOWN';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action WHERE name ='ESCALATION_HIERARCHY_POSITION_DROPDOWN_BY_DEPT_DESIG')) roles,
(SELECT id FROM eg_action WHERE name='EIS_POSITIONS_BY_DEPT_DESIG') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name ='ESCALATION_HIERARCHY_POSITION_DROPDOWN_BY_DEPT_DESIG');
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='EIS_POSITIONS_BY_DEPT_DESIG')
WHERE action IN (SELECT id FROM eg_action WHERE name ='ESCALATION_HIERARCHY_POSITION_DROPDOWN_BY_DEPT_DESIG');
                                        
DELETE FROM eg_action WHERE name ='ESCALATION_HIERARCHY_POSITION_DROPDOWN_BY_DEPT_DESIG';

INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name IN ('ESCALATION_HIERARCHY_DESIGNATION_BY_DEPT_DROPDOWN',
                                  'GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='EIS_DESIGNATION_BY_DEPARTMENT') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_DESIGNATION_BY_DEPT_DROPDOWN',
                                  'GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN'));
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='EIS_DESIGNATION_BY_DEPARTMENT')
WHERE action IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_DESIGNATION_BY_DEPT_DROPDOWN',
                                  'GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_DESIGNATION_BY_DEPT_DROPDOWN',
                                     'GRIEVANCE_UPDATE_WF_DESIGNATION_DROPDOWN');
 
INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name ='ESCALATION_TIME_DESIGNATION_DROPDOWN')) roles,
(SELECT id FROM eg_action WHERE name='EIS_DESIGNATION_BY_NAME') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name ='ESCALATION_TIME_DESIGNATION_DROPDOWN');
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='EIS_DESIGNATION_BY_NAME')
WHERE action IN (SELECT id FROM eg_action WHERE name ='ESCALATION_TIME_DESIGNATION_DROPDOWN');
                                        
DELETE FROM eg_action WHERE name ='ESCALATION_TIME_DESIGNATION_DROPDOWN';

INSERT INTO EG_ROLEACTION (roleid,actionid) SELECT roles.id, actions.id
  FROM (SELECT id FROM eg_role WHERE name='PUBLIC') roles,
       (SELECT id FROM eg_action WHERE name in ('GRIEVANCE_TYPE_BY_NAME',
                                                'GRIEVANCE_REGISTER_LOCATIONS',
                                               'GRIEVANCE_SEARCH')) actions
ON CONFLICT (roleid, actionid) DO NOTHING;