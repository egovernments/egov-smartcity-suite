
----------------------------------------Attendance Report role action---------------------------

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Council Management Reports', true, 'council', (select id from eg_module where name = 'Council Management'), 'Reports', 3);

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Attendance Report','/councilmeeting/attendance/report/search',(select id from eg_module where name='Council Management Reports'),1,'Attendance Report',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Attendance Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Attendance Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Attendance Report'));

CREATE TABLE egcncl_preamble_wards
(
  preamble bigint NOT NULL,
  ward bigint NOT NULL,
   CONSTRAINT fk_egcncl_preamble_wards FOREIGN KEY (preamble)
      REFERENCES egcncl_preamble (id),
  CONSTRAINT fk_egcncl_preamble_boundry FOREIGN KEY (ward)
      REFERENCES eg_boundary (id)
);


----------------Preamble ward wise report role action--------------------

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Ward wise Preamble Report','/councilreports/preamblewardwise/search',(select id from eg_module where name='Council Management Reports'),2,'Ward wise Preamble Report',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ward wise Preamble Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Ward wise Preamble Report'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Ward wise Preamble Report'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Preamble Wardwise Search Result','/councilreports/preamblewardwise/search-result',(select id from eg_module where name='Council Management Reports'),3,'Preamble Wardwise Search Result',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Preamble Wardwise Search Result'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='Preamble Wardwise Search Result'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Preamble Wardwise Search Result'));

----------------Preamble ward wise report feature mapping--------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Ward wise Preamble Report','Ward wise Preamble Report',(select id from eg_module  where name = 'Council Management'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Ward wise Preamble Report') ,(select id FROM eg_feature WHERE name = 'Ward wise Preamble Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Preamble Wardwise Search Result') ,(select id FROM eg_feature WHERE name = 'Ward wise Preamble Report'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Ward wise Preamble Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Ward wise Preamble Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Ward wise Preamble Report'));


-------------------------generate agenda pdf role action mapping---------------------

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'GenerateAgendaPdf','/councilmeeting/generateagenda/',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),9,'GenerateAgendaPdf',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='GenerateAgendaPdf'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='GenerateAgendaPdf'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='GenerateAgendaPdf'));



-------------------------generate agenda pdf feature mapping---------------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateAgendaPdf') ,(select id FROM eg_feature WHERE name = 'View Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateAgendaPdf') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'GenerateAgendaPdf') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));
