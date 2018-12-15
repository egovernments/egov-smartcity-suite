INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name IN ('ESCALATION_HIERARCHY_BOUNDARY_DROPDOWN',
                                   'ROUTER_BOUNDARY_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='BOUNDARY_BY_NAME_AND_BOUNDARY_TYPE') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_BOUNDARY_DROPDOWN',
                                   'ROUTER_BOUNDARY_DROPDOWN'));
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='BOUNDARY_BY_NAME_AND_BOUNDARY_TYPE')
WHERE action IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_BOUNDARY_DROPDOWN',
                                   'ROUTER_BOUNDARY_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_BOUNDARY_DROPDOWN',
                                   'ROUTER_BOUNDARY_DROPDOWN');

                 
INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name IN ('ESCALATION_HIERARCHY_POSITION_DROPDOWN',
                                   'ROUTER_POSITION_DROPDOWN'))) roles,
(SELECT id FROM eg_action WHERE name='EIS_POSITIONS_BY_CODE_NAME_POSITION_NAME') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_POSITION_DROPDOWN',
                                   'ROUTER_POSITION_DROPDOWN'));
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='EIS_POSITIONS_BY_CODE_NAME_POSITION_NAME')
WHERE action IN (SELECT id FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_POSITION_DROPDOWN',
                                   'ROUTER_POSITION_DROPDOWN'));
                                        
DELETE FROM eg_action WHERE name IN ('ESCALATION_HIERARCHY_POSITION_DROPDOWN',
                                   'ROUTER_POSITION_DROPDOWN');
                                   
INSERT INTO eg_roleaction (roleid, actionid) SELECT roles.roleid,actions.id FROM 
(SELECT roleid FROM eg_roleaction 
 WHERE actionid IN (SELECT id FROM eg_action 
                    WHERE name ='GRIEVANCE_UPDATE_LOCATION_DROPDOWN')) roles,
(SELECT id FROM eg_action WHERE name='BOUNDARY_CHILD_BY_PARENT') actions 
ON CONFLICT (roleid, actionid) DO NOTHING;  

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name ='GRIEVANCE_UPDATE_LOCATION_DROPDOWN');
                                        
UPDATE eg_feature_action 
SET action = (SELECT id FROM eg_action WHERE name='BOUNDARY_CHILD_BY_PARENT')
WHERE action IN (SELECT id FROM eg_action WHERE name ='GRIEVANCE_UPDATE_LOCATION_DROPDOWN');
                                        
DELETE FROM eg_action WHERE name ='GRIEVANCE_UPDATE_LOCATION_DROPDOWN';


UPDATE eg_action SET url='/grievance/pending' WHERE name='GRIEVANCE_MY_PENDING';

DELETE FROM eg_roleaction 
WHERE actionid IN (SELECT id FROM eg_action WHERE name ='GRIEVANCE_MY_PENDING_SEARCH');

DELETE FROM eg_feature_action WHERE action = (SELECT id FROM eg_action WHERE name ='GRIEVANCE_MY_PENDING_SEARCH');

DELETE FROM eg_action WHERE name ='GRIEVANCE_MY_PENDING_SEARCH';


UPDATE eg_action SET url='/grievance/register/by-anonymous' WHERE name='ANONYMOUS_GRIEVANCE_REGISTER';
UPDATE eg_action SET url='/grievance/register/by-citizen' WHERE name='GRIEVANCE_REGISTER_CITIZEN';
UPDATE eg_action SET url='/grievance/register/by-officials' WHERE name='GRIEVANCE_REGISTER_OFFICIAL';
UPDATE eg_action SET url='/grievance/register/by-thirdparty' WHERE name='THIRDPARTY_GRIEVANCE_REGISTER';

DELETE FROM eg_roleaction WHERE actionid IN (SELECT id FROM eg_action WHERE name IN(
    													 'THIRDPARTY_GRIEVANCE_SAVE',
                                                         'GRIEVANCE_REGISTER_OFFICIAL_SAVE',
                                                         'GRIEVANCE_REGISTER_SAVE'));

DELETE FROM eg_feature_action WHERE action in (SELECT id FROM eg_action WHERE name IN(
     													 'THIRDPARTY_GRIEVANCE_SAVE',
                                                         'GRIEVANCE_REGISTER_OFFICIAL_SAVE',
                                                         'GRIEVANCE_REGISTER_SAVE'));

DELETE FROM eg_action WHERE name IN('THIRDPARTY_GRIEVANCE_SAVE',
                                                         'GRIEVANCE_REGISTER_OFFICIAL_SAVE',
                                                         'GRIEVANCE_REGISTER_SAVE');

UPDATE eg_action SET url='/grievance/search' WHERE name='GRIEVANCE_SEARCH';
UPDATE eg_action SET url='/grievance/view/' WHERE name='GRIEVANCE_VIEW';
UPDATE eg_action SET url='/grievance/attachment/' WHERE name='GRIEVANCE_VIEW_IMAGE_DOWNLOAD';
UPDATE eg_wf_types SET link ='/pgr/grievance/update/:ID' WHERE type='Complaint';

INSERT INTO EG_ROLEACTION (roleid,actionid) SELECT roles.id, actions.id
FROM (SELECT id FROM eg_role WHERE name='PUBLIC') roles,
     (SELECT id FROM eg_action WHERE name in ('GRIEVANCE_VIEW_IMAGE_DOWNLOAD',
                                              'GRIEVANCE_VIEW')) actions
ON CONFLICT (roleid, actionid) DO NOTHING;

UPDATE EG_ACTION SET url='/grievance/update/', name='GRIEVANCE_UPDATE' WHERE name='GRIEVANCE_UPDATE_CITIZEN';

INSERT INTO EG_ROLEACTION (roleid,actionid) SELECT roles.id, actions.id
FROM (SELECT id FROM eg_role WHERE name='CITIZEN') roles,
     (SELECT id FROM eg_action WHERE name  = 'GRIEVANCE_SEARCH') actions
ON CONFLICT (roleid, actionid) DO NOTHING;