insert into EG_APPCONFIG(ID,KEY_NAME,DESCRIPTION,MODULE)
values(nextval('SEQ_EG_APPCONFIG'),'statusexcludeReport', 'voucher report status to exclude',(select id from eg_module where name ='EGF'));


insert into EG_APPCONFIG_VALUES(ID,KEY_ID,EFFECTIVE_FROM,VALUE)
values(nextval('SEQ_EG_APPCONFIG_VALUES'),(select id from EG_APPCONFIG where KEY_NAME='statusexcludeReport' ),current_date,'4,5');



