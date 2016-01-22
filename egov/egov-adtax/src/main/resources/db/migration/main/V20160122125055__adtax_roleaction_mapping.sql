--update contextroot for adtax module
update eg_module set contextroot ='adtax' where name='Advertisement Tax' and parentmodule is null;

--add new sub modules for adtax masters
INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax Agency','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Agency', null);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax RateClass','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Rate Class', null);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax UnitOfMeasure','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Unit Of Measure', null);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax Category','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Category', null);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax Subcategory','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Subcategory', null);

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Advertisement Tax ScheduleofRate','t','adtax',(select id from eg_module where name = 'AdvertisementTaxMasters'),'Schedule of Rate', null);

--Agency module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Agency') where contextroot='adtax' and name='CreateAgency';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Agency') where contextroot='adtax' and name='AgencySuccess';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Agency') where contextroot='adtax' and name='AgencyUpdate';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Agency') where contextroot='adtax' and name='SearchAgency';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Agency') where contextroot='adtax' and name='AgencyView';

--RateClass module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax RateClass') where contextroot='adtax' and name='createRateClass';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax RateClass') where contextroot='adtax' and name='savedRateClass';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax RateClass') where contextroot='adtax' and name='SearchRateClass';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax RateClass') where contextroot='adtax' and name='RateClassUpdate';


--UnitOfMeasure module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax UnitOfMeasure') where contextroot='adtax' and name='CreateUnitOfMeasure';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax UnitOfMeasure') where contextroot='adtax' and name='UnitOfMeasureSuccess';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax UnitOfMeasure') where contextroot='adtax' and name='UnitOfMeasureUpdate';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax UnitOfMeasure') where contextroot='adtax' and name='SearchUnitOfMeasure';


--Category module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Category') where contextroot='adtax' and name='Createcategory';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Category') where contextroot='adtax' and name='categorySuccess';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Category') where contextroot='adtax' and name='categoryUpdate';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Category') where contextroot='adtax' and name='Searchcategory';

--Subcategory module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Subcategory') where contextroot='adtax' and name='CreateSubcategory';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Subcategory') where contextroot='adtax' and name='subcategorySuccess';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Subcategory') where contextroot='adtax' and name='subcategoryUpdate';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax Subcategory') where contextroot='adtax' and name='Searchsubcategory';


--ScheduleofRate module update
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax ScheduleofRate') where contextroot='adtax' and name='scheduleOfRateOnLoad';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax ScheduleofRate') where contextroot='adtax' and name='createScheduleOfRate';
update eg_action set parentmodule=(select id from eg_module where name='Advertisement Tax ScheduleofRate') where contextroot='adtax' and name='savedScheduleOfRate';


--Add Admin role
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Advertisement Tax Admin', 'Advertisement Tax Admin', now(), 1, 1, now(), 0);


--Roleaction mapping for adtax master
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) (SELECT r.id, action.id FROM EG_ROLE r CROSS JOIN eg_action action WHERE r.name = 'Advertisement Tax Admin' AND action.parentmodule in (select id from eg_module where parentmodule= (select id from eg_module where name = 'AdvertisementTaxMasters')));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='CreateLegacyHoarding'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='HoardingSearchUpdate'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='HoardingLegacyview'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='HoardingLegacyUpdate'));

--report
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='dcbReportSearch'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='HoardingSearchUpdate'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='dcbReportSearch'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='HoardingSearchUpdate'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='dcbReportSearch'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='HoardingSearchUpdate'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='dcbReportSearch'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='AjaxSubCategories'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Load Block By Locality'));

--collection
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='CollectAdvertisementTax'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='searchHoardingResult'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='generateBillForCollection'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='AjaxSubCategories'));


