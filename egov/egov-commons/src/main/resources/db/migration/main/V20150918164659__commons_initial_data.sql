-----------------START--------------------
INSERT INTO accountdetailtype (id, name, description, tablename, columnname, attributename, nbroflevels, isactive, created, lastmodified, modifiedby, full_qualified_name) VALUES (1, 'Employee', 'Employee', 'eg_employee', 'id', 'Employee_id', 1, 1, '0001-05-15 00:00:00 BC', '0001-05-15 00:00:00 BC', 1, 'org.egov.pims.model.PersonalInformation');
INSERT INTO accountdetailtype (id, name, description, tablename, columnname, attributename, nbroflevels, isactive, created, lastmodified, modifiedby, full_qualified_name) VALUES (2, 'Telephone', 'Telephone', 'accountEntityMaster', 'id', 'accountentitymaster_id', 1, 1, '0001-09-09 00:00:00 BC', NULL, NULL, 'org.egov.masters.model.AccountEntity');
INSERT INTO accountdetailtype (id, name, description, tablename, columnname, attributename, nbroflevels, isactive, created, lastmodified, modifiedby, full_qualified_name) VALUES (3, 'DrawingOfficer', 'Drawing Officer', 'EG_DRAWINGOFFICER', 'id', 'DrawingOfficer_id', 1, 1, '0001-08-13 00:00:00 BC', NULL, NULL, 'org.egov.pims.commons.DrawingOfficer');
update accountdetailtype set full_qualified_name = 'org.egov.eis.entity.Employee' where name = 'Employee';
------------------END---------------------

-----------------START--------------------

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (157, 'Workflow', true, null, 1, 'Workflow', 1);


------------------END---------------------


-----------------START--------------------
INSERT INTO eg_address (housenobldgapt, subdistrict, postoffice, landmark, country, userid, type, streetroadline, citytownvillage, arealocalitysector, district, state, pincode, id, version) VALUES ('001', NULL, NULL, 'Bank Road', NULL, 1, 'PROPERTYADDRESS', NULL, NULL, NULL, NULL, NULL, '532001', 1, 0);
------------------END---------------------

-----------------START-------------------
INSERT INTO eg_location (id, name, description, locationid, createddate, lastmodifieddate, isactive, islocation) VALUES (2, 'Z01C1', 'Zone 1 Counter 1', 86, '2015-08-28 10:45:26.83161', '2015-08-28 10:45:26.83161', 1, 0);
------------------END---------------------





-----------------START-------------------

INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (5, 'CSC Operator', 'Collection Operator mans the Citizen Service Centers.', '2010-01-01 00:00:00', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (7, 'Citizen', 'Citizen who can raise complaint', '2010-01-01 00:00:00', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (15, 'Employee', 'Default role for all employees', '2015-08-28 00:00:00', 1, 1, '2015-08-28 00:00:00', 0);
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (16, 'ULB Operator', 'ULB Operator', '2015-08-28 10:45:17.567676', 1, 1, '2015-08-28 10:45:17.567676', 0);
------------------END---------------------
-----------------START-------------------

Insert into EG_ROLEACTION (roleid, actionid) values (15,37);

------------------END---------------------
-----------------START-------------------
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (1, 'AREA', 'area', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (2, 'Length', 'Length', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (3, 'Quantity', 'Quantity', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (4, 'Numbers', 'Numbers', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (5, 'Volume', 'Volume', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uomcategory (id, category, narration, lastmodified, createddate, createdby, lastmodifiedby) VALUES (6, 'Weight', 'Weight', '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
------------------END---------------------
-----------------START-------------------
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (1, 2, 'MTR', 'MTR', 1, 1, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (2, 2, 'CENTIMETER', 'centimeter', 100, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, NULL);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (3, 2, '30 MTR', '30 METER', 30, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (4, 6, 'SQM', 'square meter', 1, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (5, 6, '10 SQM', '10 SQM', 10, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (6, 5, 'CUM', 'cubic meter', 1, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (7, 3, 'GRM', 'GRM', 1, 1, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (8, 3, 'KGS', 'KGS', 1000, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (9, 3, 'TON', 'TON', 1000000, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (10, 3, 'BAG', 'BAG', 12, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (11, 3, 'BOX', 'BOX1', 12, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (12, 3, 'DOZ', 'DOZ', 12, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (13, 4, 'Each', 'each', 1, 0, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
INSERT INTO eg_uom (id, uomcategoryid, uom, narration, conv_factor, baseuom, lastmodified, createddate, createdby, lastmodifiedby) VALUES (14, 4, 'No', 'number', 1, 1, '2015-08-28 10:39:50.396309', '2015-08-28 10:39:50.396309', 1, 1);
------------------END---------------------

-----------------START-------------------
INSERT INTO eg_chairperson (id, name, fromdate, todate, active, createdby, createddate, lastmodifieddate, lastmodifiedby, version) VALUES (1, 'Mr. XXX Chiar Person', '2015-08-28', '2016-08-27', true, 1, '2015-08-28 00:00:00', '2015-08-28 00:00:00', 1, 0);
------------------END---------------------
