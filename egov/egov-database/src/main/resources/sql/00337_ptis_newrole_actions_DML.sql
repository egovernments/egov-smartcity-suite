INSERT INTO eg_role (id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version) values (nextval('seq_eg_role'),'ULB Operator','ULB Operator',current_timestamp,1,1,current_timestamp,0);

UPDATE eg_roleaction SET roleid = (SELECT id FROM eg_role WHERE name='ULB Operator') WHERE roleid = (select id from eg_role where name='CSC Operator');

UPDATE eg_userrole SET roleid = (select id from eg_role where name='ULB Operator') WHERE userid in (SELECT id FROM eg_user WHERE name in ('manasa','subhash','nayeemalla','malathi','parvati'));

INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Forward Property' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Reject Property' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='ULB Operator'));

INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Create Property' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='Property Verifier'));

INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action WHERE name='Load Admin Boundaries' and contextroot='ptis'), (SELECT id FROM eg_role WHERE name='Property Verifier'));

--rollback UPDATE eg_roleaction SET roleid = (SELECT id FROM eg_role WHERE name='ULB Operator') WHERE roleid = (select id from eg_role where name='CSC Operator');
--rollback DELETE FROM eg_role WHERE name='ULB Operator';
--rollback DELETE FROM eg_roleaction  WHERE actionid in(SELECT id FROM eg_action WHERE name in('Forward Property','Reject Property','Create Property','Load Admin Boundaries')) and roleid in (SELECT id from eg_role where name='ULB Operator','Property Verifier');
