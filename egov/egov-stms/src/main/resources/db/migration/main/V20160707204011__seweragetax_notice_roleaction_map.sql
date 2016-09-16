CREATE TABLE egswtax_notice
(
  id bigint NOT NULL, -- Primary Key
  applicationdetails bigint,
  module bigint, -- FK to EG_MODULE
  noticetype character varying(32),
  noticeno character varying(64), 
  noticedate timestamp without time zone,
  filestore bigint,
  applicationnumber character varying(50),
  CONSTRAINT pk_egswtax_notice PRIMARY KEY (id),
  CONSTRAINT fk_notice_applicationdetails FOREIGN KEY (applicationDetails)
      REFERENCES egswtax_applicationdetails (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      CONSTRAINT fk_module FOREIGN KEY (module)
      REFERENCES eg_module (id),
      CONSTRAINT uniq_noticeno UNIQUE (noticeno)
      );


CREATE SEQUENCE SEQ_EGSWTAX_NOTICE;



INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'SewerageReports', true, null, (select id from eg_module where name = 'Sewerage Tax Management'), 'Reports', 3);

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'SearchNotice', 
'/reports/search-notice', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Search Notice', true, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'SearchNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'SearchNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'SearchNotice'));




INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'ShowNotice', 
'reports/searchNotices-showNotices', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Show Notice', false, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ShowNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'ShowNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'ShowNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'ShowNotice'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'STZipAndDownload', 
'/reports/searchNotices-seweragezipAndDownload', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Sewerage Notice Zip & Dowmload', false, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'STZipAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'STZipAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'STZipAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'STZipAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'STZipAndDownload'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'STMergeAndDownload', 
'/reports/searchNotices-mergeAndDownload', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Sewerage Notice Merge & Dowmload', false, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'STMergeAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'STMergeAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'STMergeAndDownload'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'STMergeAndDownload'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'STNoticeSearchResult', 
'/reports/searchResult', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Sewerage Notice Search Result', false, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'STNoticeSearchResult'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'STNoticeSearchResult'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'STNoticeSearchResult'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'STNoticeSearchResult'));


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES 
(nextval('seq_eg_action'), 'STNoticeSearchResultCount', 
'/reports/search-NoticeResultSize', null, (select id from eg_module where name = 'SewerageReports'), 1,
 'Sewerage Notice Search Result Count', false, 'stms', 0, 1, now(), 1, now(), 
 (select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'STNoticeSearchResultCount'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'STNoticeSearchResultCount'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'STNoticeSearchResultCount'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'STNoticeSearchResultCount'));


INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Report Viewer'), (select id from eg_action where name = 'AjaxSewerageClosetsCheck'));

UPDATE EG_ACTION SET parentmodule=(select id from eg_module where name='SewerageReports') where name='viewSewerageConnectionDCBReport' and url='/reports/sewerageRateReportView' and contextroot = 'stms';


 
 

