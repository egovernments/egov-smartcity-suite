update eg_wf_matrix set currentDesignation='Senior Assistant,Junior Assistant' where id in(select id from 
eg_wf_matrix where objecttype='WaterConnectionDetails' and currentDesignation='Revenue Clerk');


update eg_wf_matrix set nextDesignation='Senior Assistant,Junior Assistant' where id in(select id from 
eg_wf_matrix where objecttype='WaterConnectionDetails' and nextDesignation='Revenue Clerk');


update eg_appconfig_values set value='Senior Assistant,Junior Assistant' where key_id 
in(select id from eg_appconfig where key_name='CLERKDESIGNATIONFORCSCOPERATOR');


delete from egeis_assignment where designation in(SELECT id FROM eg_designation WHERE name='Assistant engineer') and
department in(SELECT id FROM eg_department WHERE name='Engineering') ;

delete from eg_userrole where roleid in(select id from eg_role where name='ULB Operator') and userid in(select id from eg_user where username='rishi');

delete from eg_position where name='R-Assistant-engineer-1' and  deptdesig in(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Assistant engineer') 
and department = 
(select id from eg_department where name = 'Engineering'));

delete from egeis_deptdesig where id in(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Assistant engineer') 
and department = 
(select id from eg_department where name = 'Engineering'));

delete from eg_department where name='Engineering';


INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, 
version) VALUES (nextval('SEQ_EG_DEPARTMENT'), 'Engineering', '2010-01-01 00:00:00', 'ENG', 1, 1, '2015-01-01 00:00:00', 0);


INSERT INTO egeis_deptdesig (id, designation, department, outsourcedposts, sanctionedposts,
 version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), 
 (select id from eg_designation where name = 'Assistant engineer'), (select id from eg_department where name = 'Engineering'),
 0, 2, 0, now(), now(), 1, 1);

INSERT INTO eg_position (id, name, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby,
 ispostoutsourced, version) VALUES (nextval('seq_eg_position'),'R-Assistant-engineer-1',
(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Assistant engineer') 
and department = 
(select id from eg_department where name = 'Engineering')), now(), now(), 1, 1, false, 0);


insert into eg_userrole (roleid,userid)values((select id from eg_role where name='ULB Operator'),(select id from eg_user where username='rishi'));


INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade
,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,version,employee,isprimary) 
VALUES (nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Assistant engineer'),null,
(SELECT id FROM eg_department WHERE name='Engineering'),(SELECT id FROM eg_position 
WHERE name='R-Assistant-engineer-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',
1,(SELECT id FROM egeis_employee WHERE code='E025'),true);





delete from egeis_assignment where designation in(SELECT id FROM eg_designation WHERE name='Commissioner') and
department in(SELECT id FROM eg_department WHERE name='Administration') ;

delete from eg_userrole where roleid in(select id from eg_role where name='ULB Operator') and userid in(select id from eg_user where username='rishi');

delete from eg_position where name='ADM-Commissioner-1' and  deptdesig in(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Commissioner') 
and department = 
(select id from eg_department where name = 'Administration'));

delete from egeis_deptdesig where id in(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Commissioner') 
and department = 
(select id from eg_department where name = 'Administration'));

delete from eg_department where name='Administration';


INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, 
version) VALUES (nextval('SEQ_EG_DEPARTMENT'), 'Administration', '2010-01-01 00:00:00', 'ADM', 1, 1, '2015-01-01 00:00:00', 0);



INSERT INTO egeis_deptdesig (id, designation, department, outsourcedposts, sanctionedposts,
 version, createddate, lastmodifieddate, createdby, lastmodifiedby) VALUES (nextval('seq_egeis_deptdesig'), 
 (select id from eg_designation where name = 'Commissioner'), (select id from eg_department where name = 'Administration'),
 0, 2, 0, now(), now(), 1, 1);

INSERT INTO eg_position (id, name, deptdesig, createddate, lastmodifieddate, createdby, lastmodifiedby,
 ispostoutsourced, version) VALUES (nextval('seq_eg_position'),'ADM-Commissioner-1',
(select id from egeis_deptdesig where designation = (select id from eg_designation where name = 'Commissioner') 
and department = 
(select id from eg_department where name = 'Administration')), now(), now(), 1, 1, false, 0);


insert into eg_userrole (roleid,userid)values((select id from eg_role where name='Property Administrator')
,(select id from eg_user where username='vaibhav'));




INSERT INTO egeis_assignment (id,fund,function,designation,functionary,department,position,grade
,lastmodifiedby,lastmodifieddate,createddate,createdby,fromdate,todate,version,employee,isprimary) 
VALUES (nextval('seq_egeis_assignment'),(SELECT id FROM fund WHERE name='Municipal Fund'),null,
(SELECT id FROM eg_designation WHERE name='Commissioner'),null,
(SELECT id FROM eg_department WHERE name='Administration'),(SELECT id FROM eg_position 
WHERE name='ADM-Commissioner-1'),1,1,now(),now(),1,'01-Apr-2015','31-Mar-2020',
1,(SELECT id FROM egeis_employee WHERE code='E036'),true);