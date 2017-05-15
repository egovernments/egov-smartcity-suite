--- Base Register Feature and Action Update

DELETE FROM EG_FEATURE_ACTION where action=(select id FROM eg_action WHERE name ='BaseReportTotal') and feature=(select id FROM EG_FEATURE
WHERE name  ='Base Register Report');

update eg_action set url='/report/base-register/search',name='License Base Register Report' where name='baseregistersearch';

delete from eg_roleaction where actionid = (select id from eg_action where name='baseregistersearchresult');

delete from eg_feature_action  where action = (select id from eg_action where name='baseregistersearchresult');

delete from eg_action where name='baseregistersearchresult';

update eg_action set url='/report/base-register/grand-total',name='Base Register Report Grand Total',parentmodule=(select id from eg_module where name='Trade License Reports'),application=(select id from eg_module  where name = 'Trade License') where name='BaseReportTotal';

update eg_action set url='/report/base-register/download',name='Base Register Report Download',parentmodule=(select id from eg_module where name='Trade License Reports'),application=(select id from eg_module  where name = 'Trade License') where name='BaseRegisterReport';

insert into eg_feature_action(feature,action) values ((select id from eg_feature  where name = 'Base Register Report'), (select id from eg_action where name='Base Register Report Grand Total'));

insert into eg_feature_action(feature,action) values ((select id from eg_feature  where name = 'Base Register Report'), (select id from eg_action where name='LicenseSubcategoryByCategory'));

insert into eg_feature_action (feature,action) values ((select id from eg_feature where name  ='Base Register Report'), (select id FROM eg_action where name ='viewTradeLicense-view'));


-- DCB Report Action and Feature Updates

update eg_action set url='/report/dcb/search',name='License DCB Report', displayname='DCB Report' where name='TradeLicenseDCBReportLocalityWise';

delete from eg_roleaction where actionid = (select id from eg_action where name='TLDCBReportList');

delete from eg_feature_action  where action = (select id from eg_action where name='TLDCBReportList');

delete from eg_action where name='TLDCBReportList';

update eg_action set url='/report/dcb/grand-total',name='DCB Report Grand Total',parentmodule=(select id from eg_module where name='Trade License Reports'),application=(select id from eg_module  where name = 'Trade License') where name='DCBReportSum';

update eg_action set url='/report/dcb/download',name='DCB Report Download',parentmodule=(select id from eg_module where name='Trade License Reports'),application=(select id from eg_module  where name = 'Trade License') where name='DCBReportgeneration';

update eg_feature set name='License DCB Report' where name='DCB Report By Trade';

INSERT INTO EG_FEATURE_ACTION (feature,action) VALUES ((select id FROM EG_FEATURE WHERE name  ='License DCB Report'), (select id FROM eg_action WHERE name ='viewTradeLicense-view'));
 

-- Yearwise DCB Report Action and Feature Updates

update eg_action set url='/report/dcb/yearwise/search',name='License Yearwise DCB Report', displayname='Yearwise DCB Report' where name='installmentwise-dcbreport';

delete from eg_roleaction where actionid = (select id from eg_action where name='installmentwise-dcbresult');

delete from eg_feature_action  where action = (select id from eg_action where name='installmentwise-dcbresult');

delete from eg_action where name='installmentwise-dcbresult';

update eg_feature set name='License Yearwise DCB Report' where name='YearWise DCB Report';

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='viewTradeLicense-view'),(select id FROM EG_FEATURE
 WHERE name  ='License Yearwise DCB Report'));
 
-- Search Trade Action Update

update eg_action set url='/search/license', name='Search Trade License' where name='SearchTradeLicense';

delete from eg_roleaction where actionid = (select id from eg_action where name='searchTrade-search');

delete from eg_feature_action  where action = (select id from eg_action where name='searchTrade-search');

delete from eg_action where name='searchTrade-search';

update eg_action set url='/search/autocomplete', name='Search License Autocomplete' where name='SearchAjax-PopulateData';

--- Online Payment Action and Feature Entries

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'License Online Payment','/pay/online',null,(select id from eg_module  where name = 'Trade License'),1,'License Online Payment',FALSE, 'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'License DCB View','/dcb/view',null,(select id from eg_module  where name = 'Trade License'),1,'License DCB View',FALSE, 'tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('SEQ_EG_FEATURE'),'License Online Payment','Search License for Online Payment',(select id from eg_module where name='Trade License'),0);
 
INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License Online Payment'),(select id FROM EG_FEATURE
 WHERE name  ='License Online Payment'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Search License Autocomplete'),(select id FROM EG_FEATURE
 WHERE name  ='License Online Payment'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='License DCB View'),(select id FROM EG_FEATURE
 WHERE name  ='License Online Payment'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='viewTradeLicense-view'),(select id FROM EG_FEATURE
 WHERE name  ='License Online Payment'));
  
insert into eg_feature_role(feature,role) values ((select id from eg_feature  where name = 'License Online Payment'), (select id from eg_role where name='Super User'));

insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name='License Online Payment'));

insert into eg_roleaction(roleid,actionid) values ((select id from eg_role where name='Super User'), (select id from eg_action where name='License DCB View'));

--Demand Notice Action and Feature Changes

update eg_action set name ='License Demand Notice Search', url='/demand-notice/search' where name='Search TL For Bulk Demand Notice';

update eg_action set name ='Generate License Demand Notice', url='/demand-notice/generate' where name='generate tl demand notice pdf';

delete from eg_roleaction where actionid = (select id from eg_action where name='Search TL For Bulk Demand Notice generation');

delete from eg_feature_action  where action = (select id from eg_action where name='Search TL For Bulk Demand Notice generation');

delete from eg_action where name='Search TL For Bulk Demand Notice generation';

delete from eg_roleaction where actionid = (select id from eg_action where name='tldemandnoticereport');

delete from eg_feature_action  where action = (select id from eg_action where name='tldemandnoticereport');

delete from eg_action where name='tldemandnoticereport';
