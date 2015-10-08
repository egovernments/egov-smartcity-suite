
-----------------START--------------------

INSERT INTO egadtax_agency (id, code, name, ssid, emailid, mobilenumber, address, createddate, lastmodifieddate, createdby, lastmodifiedby, version, depositamount, status) VALUES (nextval('SEQ_egadtax_AGENCY'), 'SAIENTER', 'Sri Sai Enterprise', NULL, NULL, '9999999999', '333,1st main road, m.g.road, visakhapatnam', '2015-09-14 00:00:00', NULL, 1, NULL, 0, 0, 'ACTIVE');
INSERT INTO egadtax_agency (id, code, name, ssid, emailid, mobilenumber, address, createddate, lastmodifieddate, createdby, lastmodifiedby, version, depositamount, status) VALUES (nextval('SEQ_egadtax_AGENCY'), 'TIRUMALAENTER', 'TIRUMALA AGENCY', NULL, NULL, '9999999999', '233,13TH main road, TEMPLE ROAD, TIRUPATHI', '2015-09-14 00:00:00', NULL, 1, NULL, 0, 0, 'ACTIVE');
INSERT INTO egadtax_agency (id, code, name, ssid, emailid, mobilenumber, address, createddate, lastmodifieddate, createdby, lastmodifiedby, version, depositamount, status) VALUES (nextval('SEQ_egadtax_AGENCY'), 'TEST', 'Test', 'TEST123', 'abc@egovernments.org', '4564565465', 'Testing', '2015-09-18 11:10:32.02', '2015-09-18 11:10:32.02', 2, 2, 0, 500, 'ACTIVE');
INSERT INTO egadtax_agency (id, code, name, ssid, emailid, mobilenumber, address, createddate, lastmodifieddate, createdby, lastmodifiedby, version, depositamount, status) VALUES (nextval('SEQ_egadtax_AGENCY'), '101', 'Ramesh', 'X001', 'bimalendu.lenka@gmail.com', '8123831853', 'EGOV', '2015-09-29 13:54:04.985', '2015-09-29 13:56:24.104', 2, 2, 1, 1000, 'ACTIVE');

------------------END---------------------
-----------------START--------------------


INSERT INTO egadtax_category (id, code, name, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_CATEGORY'), 'NON-ILLUMINATED', 'NON-ILLUMINATED', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_category (id, code, name, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_CATEGORY'), 'ILLUMINATED', 'ILLUMINATED', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_category (id, code, name, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_CATEGORY'), 'CINEMAHALL', 'Advertisement in cinema House', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_category (id, code, name, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_CATEGORY'), 'PRINTEDADVERTISEMENT', 'Printed Advertisement', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);

------------------END---------------------
-----------------START--------------------

INSERT INTO egadtax_hoardingdocument_type (id, name, mandatory, version) VALUES (nextval('SEQ_egadtax_document_TYPE'), 'Permission letter', false, NULL);
INSERT INTO egadtax_hoardingdocument_type (id, name, mandatory, version) VALUES (nextval('SEQ_egadtax_document_TYPE'), 'Application form', false, NULL);

------------------END---------------------
-----------------START--------------------

INSERT INTO egadtax_rates_class (id, description, active, version) VALUES (nextval('SEQ_egadtax_ratesClass'), 'Best Rate', true, NULL);
INSERT INTO egadtax_rates_class (id, description, active, version) VALUES (nextval('SEQ_egadtax_ratesClass'), 'Better Rate', true, NULL);
INSERT INTO egadtax_rates_class (id, description, active, version) VALUES (nextval('SEQ_egadtax_ratesClass'), 'Average Rate', true, NULL);
INSERT INTO egadtax_rates_class (id, description, active, version) VALUES (nextval('SEQ_egadtax_ratesClass'), 'Poor Rate', true, NULL);


------------------END---------------------
-----------------START--------------------

INSERT INTO egadtax_subcategory (id, category, code, description, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_SUBCATEGORY'), (SELECT ID FROM  egadtax_CATEGORY WHERE CODE='NON-ILLUMINATED'), 'ONLANDBUILDING', 'On Land, Building', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_subcategory (id, category, code, description, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_SUBCATEGORY'), (SELECT ID FROM  egadtax_CATEGORY WHERE CODE='ILLUMINATED'), 'illuminated-landbuilding', 'On Land, Building,wall', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_subcategory (id, category, code, description, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_SUBCATEGORY'),(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='CINEMAHALL'), 'CINEMAHALL', 'In cinema hall', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);
INSERT INTO egadtax_subcategory (id, category, code, description, active, createddate, createdby, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_SUBCATEGORY'),(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='PRINTEDADVERTISEMENT'), 'PRINTEDADVERTISEMENT', 'Printed advertisement for display', true, '2015-09-14 00:00:00', 1, NULL, NULL, NULL);

------------------END---------------------
-----------------START--------------------


INSERT INTO egadtax_revenueinspectors (id, name, active, version) VALUES (nextval('SEQ_egadtax_revenueinspectors'), 'Julian', true, NULL);
INSERT INTO egadtax_revenueinspectors (id, name, active, version) VALUES (nextval('SEQ_egadtax_revenueinspectors'), 'Ramesh', true, NULL);
------------------END---------------------

-----------------START--------------------
INSERT INTO egadtax_unitofmeasure (id, code, description, active, createdby, createddate, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_UnitOfMeasure'), 'SQMT', 'SQ.MT', true, 1, '2015-09-14 00:00:00', NULL, NULL, NULL);
INSERT INTO egadtax_unitofmeasure (id, code, description, active, createdby, createddate, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_UnitOfMeasure'), 'SQFT', 'SQ.FT', true, 1, '2015-09-14 00:00:00', NULL, NULL, NULL);
INSERT INTO egadtax_unitofmeasure (id, code, description, active, createdby, createddate, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_UnitOfMeasure'), 'NUMBER', 'NUMBER', true, 1, '2015-09-14 00:00:00', NULL, NULL, NULL);
INSERT INTO egadtax_unitofmeasure (id, code, description, active, createdby, createddate, version, lastmodifieddate, lastmodifiedby) VALUES (nextval('SEQ_egadtax_UnitOfMeasure'), 'EACH', 'EACH', true, 1, '2015-09-14 00:00:00', NULL, NULL, NULL);
------------------END---------------------