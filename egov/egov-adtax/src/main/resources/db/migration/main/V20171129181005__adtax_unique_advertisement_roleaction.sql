INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'checkUnqAdvertisementNumber','/hoarding/checkUnique-advertisementNo',(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Unique Advertisement number check',false,'adtax',(select id from eg_module where name='Advertisement Tax'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='checkUnqAdvertisementNumber'));

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='checkUnqAdvertisementNumber'));
