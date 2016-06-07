---------START-----------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksAjaxGetWard','/lineestimate/ajax-getward',null,(SELECT id FROM EG_MODULE WHERE name = 'WorksLineEstimate'),1,'Create Line Estimate','false','egworks',0,1,now(),1,now(),(SELECT id FROM eg_module  WHERE name = 'Works Management'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_action WHERE name ='WorksAjaxGetWard' and contextroot = 'egworks'));

ALTER TABLE egw_lineestimate_details ALTER COLUMN quantity TYPE double precision USING quantity::double precision;

ALTER TABLE egw_lineestimate ADD COLUMN workcategory character varying(100) not null;

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksAjaxGetWard' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksAjaxGetWard' and contextroot = 'egworks';

--rollback ALTER TABLE egw_lineestimate_details ALTER COLUMN quantity TYPE character varying(100);
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN workCategory;
------------END---------------