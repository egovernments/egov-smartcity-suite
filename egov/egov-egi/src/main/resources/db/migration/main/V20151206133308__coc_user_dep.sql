INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (1, 'Accounts', '2010-01-01 00:00:00', 'A', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (2, 'Buildings', '2010-01-01 00:00:00', 'B', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (3, 'Roads', '2010-01-01 00:00:00', 'BR', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (4, 'Storm Water Drains', '2010-01-01 00:00:00', 'D', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (16, 'General', '2010-01-01 00:00:00', 'G', 1, 1, '2015-01-01 00:00:00', 0);
INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (17, 'Family Welfare', '2010-01-01 00:00:00', 'HD', 1, 1, '2015-01-01 00:00:00', 0);

update eg_hierarchy_type set name='ADMINISTRATION',code='ADMIN' where code='REVENUE';


INSERT INTO eg_city (domainurl, name, localname, id, active, version, createdby, lastmodifiedby, createddate, lastmodifieddate, code, recaptchapk, districtcode, districtname, longitude, latitude, preferences, recaptchapub) VALUES ('localhost', 'Chennai', 'Chennai', nextval('seq_eg_city'), true, 0, 1, 1, '2010-01-01 00:00:00', '2015-01-01 00:00:00', '0001', '6LfidggTAAAAANDSoCgfkNdvYm3Ugnl9HC8_68o0', NULL, NULL, NULL, NULL, NULL, '6LfidggTAAAAADwfl4uOq1CSLhCkH8OE7QFinbVs');


INSERT INTO eg_boundary (id, boundarynum, parent, name, boundarytype, localname, bndry_name_old, bndry_name_old_local, fromdate, todate, bndryid, longitude, latitude, materializedpath, ishistory, createddate, lastmodifieddate, createdby, lastmodifiedby, version) VALUES (1, 1, NULL, 'Chennai', 1, 'Chennai', NULL, NULL, '0001-04-01 00:00:00 BC', '2099-03-31 00:00:00', NULL, NULL, NULL, '1', false, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 0);

