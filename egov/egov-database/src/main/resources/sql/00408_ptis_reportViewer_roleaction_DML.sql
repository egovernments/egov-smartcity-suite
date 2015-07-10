INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'PropTax report viewer','/reportViewer', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PropTax report viewer'),1, 'PropTax report viewer',false,'ptis',0,1,now(),1,now());

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name LIKE 'ULB Operator'),(select id FROM eg_action WHERE NAME = 'PropTax report viewer' and CONTEXTROOT='ptis'));

