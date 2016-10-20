-----------------------------------update eg_roleaction----------------------------------------------------------

delete from eg_feature_action where action in (select id from eg_action where name in('Create Unit Of Measurement','Modify Unit Of Measurement','View Unit Of Measurement','Save UOM','editUnitOfMeasurement') and contextroot='tl');

delete from eg_roleaction where actionid in (select id from eg_action where name='Save UOM'); 
delete from eg_roleaction where actionid in (select id from eg_action where name='editUnitOfMeasurement'); 

delete from eg_action where name='Save UOM';
delete from eg_action where name='editUnitOfMeasurement';

update eg_action set url='/licenseunitofmeasurement/create' where name='Create Unit Of Measurement';
update eg_action set url='/licenseunitofmeasurement/update',QUERYPARAMS=null where name='Modify Unit Of Measurement';
update eg_action set url='/licenseunitofmeasurement/view',QUERYPARAMS=null  where name='View Unit Of Measurement';

---------------------------update eg_feature_action-------------------------------------------------------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Create Unit Of Measurement') ,
(select id FROM eg_feature WHERE name = 'Create Unit Of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify Unit Of Measurement') ,
(select id FROM eg_feature WHERE name = 'Create Unit Of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Create Unit Of Measurement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'Modify Unit Of Measurement') ,
(select id FROM eg_feature WHERE name = 'Modify Unit Of Measurement'));
INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View License SubCategory') ,
(select id FROM eg_feature WHERE name = 'Modify Unit Of Measurement'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES ((select id FROM eg_action  WHERE name = 'View Unit Of Measurement') ,
(select id FROM eg_feature WHERE name = 'View Unit Of Measurement'));




