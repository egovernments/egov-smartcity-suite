
update eg_action set url='/license/success/' where name='View License Success';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) VALUES (NEXTVAL('SEQ_EG_ACTION'),'View License Application',
'/license/view/',null,(select id from EG_MODULE WHERE name = 'Trade License'),1,
'View License Application','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (actionid,roleid) SELECT DISTINCT (SELECT id FROM eg_action WHERE name='View License Application'),
roleid FROM EG_ROLEACTION  WHERE actionid  in(SELECT id FROM eg_action WHERE name='viewTradeLicense-view');

update egp_inbox  set link  ='/tl/license/view/'||entityrefid where moduleid =(select id from eg_module  where name='Trade License');