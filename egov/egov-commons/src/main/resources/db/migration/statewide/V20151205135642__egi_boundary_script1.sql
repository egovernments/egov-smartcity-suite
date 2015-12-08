INSERT INTO eg_boundary_type (id, hierarchy, parent, name, hierarchytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, localname) VALUES (7, 2, 6, 'Locality', 2, '2015-08-28 10:44:03.831086', '2015-08-28 10:44:03.831086', 1, 1, 0, NULL);
INSERT INTO eg_boundary_type (id, hierarchy, parent, name, hierarchytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, localname) VALUES (8, 3	, 7, 'Street', 2, '2015-08-28 10:44:03.831086', '2015-08-28 10:44:03.831086', 1, 1, 0, NULL);



INSERT INTO eg_boundary (id, boundarynum, parent, name, boundarytype, localname, bndry_name_old_local, fromdate, todate, bndryid, longitude, latitude, materializedpath, ishistory, createddate, lastmodifieddate, createdby, lastmodifiedby) values (nextval('seq_eg_boundary'),1, null, 'Chennai' , (select id from eg_boundary_type where name='City' and hierarchytype=(select id from eg_hierarchy_type where name='LOCATION')), 'Chennai', null, to_date('01/04/2004 00:00:00', 'dd/mm/yyyy hh24:mi:ss'), to_date('01/04/2099 23:59:59', 'dd/mm/yyyy hh24:mi:ss'), null, null, null, null, 'N',  now(), now(), 1, 1);






