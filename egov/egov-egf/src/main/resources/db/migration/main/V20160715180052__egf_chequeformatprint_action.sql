Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'chequeFormatPrint','/payment/chequeAssignmentPrint-generateChequeFormat.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='chequeFormatPrint'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),(select id from eg_action where name='chequeFormatPrint'));


insert into chequeformat values(nextval('seq_chequeformat'),'SBI-general-format1','general',205,95,'8,17','DDMMYYYY',
'153,10',null,'12,25','indian','28,35',165,125,'8,42',null,'153,44',true,0);

insert into chequeformat values(nextval('seq_chequeformat'),'SBI-PD','pd',202,94,null,'DD-Mon-YYYY',
'168,21',null,'15,35','indian','21,41',175,170,'10,46',null,'15,57',true,0);



