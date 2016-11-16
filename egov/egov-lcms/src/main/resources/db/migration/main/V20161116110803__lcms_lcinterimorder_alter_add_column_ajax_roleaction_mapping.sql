----alter add columns
ALTER TABLE eglc_lcinterimorder  ADD column actionitem character varying(500); 
ALTER TABLE eglc_lcinterimorder_aud ADD column actionitem character varying(500); 

ALTER TABLE eglc_lcinterimorder ADD column duedate date ; 
ALTER TABLE eglc_lcinterimorder_aud ADD column duedate date ; 


ALTER TABLE eglc_lcinterimorder  ADD column actiontaken character varying(500); 
ALTER TABLE eglc_lcinterimorder_aud ADD column actiontaken character varying(500); 


ALTER TABLE eglc_lcinterimorder  ADD column employee bigint ;
alter table eglc_lcinterimorder add CONSTRAINT fk_lcinterimorder_employee FOREIGN KEY (employee) REFERENCES egeis_employee (id);

ALTER TABLE eglc_lcinterimorder_aud ADD column employee bigint ; 

---eg_action
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'getAllEmployeeNames','/ajax/getemployeeNames',(select id from eg_module 
 where name='LCMS Transactions'),1,'getAllEmployeeNames',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='getAllEmployeeNames'));
-----eg_feature_action
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'getAllEmployeeNames')
 ,(select id FROM eg_feature WHERE name = 'Interim Order'));