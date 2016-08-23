-----------------------------------update eg_roleaction----------------------------------------------------------

delete from eg_feature_action where action in (select id from eg_action where name in('Create License Category','Modify License Category','View License Category','Save License Category','saveEditedCategory') and contextroot='tl');

delete from eg_roleaction where actionid in (select id from eg_action where name='Save License Category'); 
delete from eg_roleaction where actionid in (select id from eg_action where name='saveEditedCategory'); 

delete from eg_action where name='Save License Category';
delete from eg_action where name='saveEditedCategory';

update eg_action set url='/licensecategory/create',QUERYPARAMS=null where name='Create License Category';
update eg_action set url='/licensecategory/update',QUERYPARAMS=null where name='Modify License Category';
update eg_action set url='/licensecategory/view',QUERYPARAMS=null  where name='View License Category';

---------------------------update eg_feature_action-------------------------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Create License Category') ,
(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify License Category') ,
(select id FROM eg_feature WHERE name = 'Create License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License Category') ,
(select id FROM eg_feature WHERE name = 'Create License Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify License Category') ,
(select id FROM eg_feature WHERE name = 'Modify License Category'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License Category') ,
(select id FROM eg_feature WHERE name = 'Modify License Category'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License Category') ,
(select id FROM eg_feature WHERE name = 'View License Category'));




