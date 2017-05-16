CREATE TABLE egswtax_sewerageratedetail_master (
    id bigint NOT NULL,
    sewerageratemaster bigint not null,
    noofclosets bigint NOT NULL,
    amount double precision NOT NULL,
    version bigint
);

ALTER TABLE ONLY egswtax_sewerageratedetail_master
    ADD CONSTRAINT pk_egswtax_sewerageratedetail_master PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_sewerageratedetail_master
    ADD CONSTRAINT fk_egswtax_swtaxdtl_sewearge FOREIGN KEY (sewerageratemaster) REFERENCES egswtax_sewerage_rates_master(id);

CREATE SEQUENCE SEQ_EGSWTAX_SEWERAGERATEDETAIL_MASTER; 

ALTER table egswtax_sewerage_rates_master alter monthlyrate drop not null;

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'EDIT_DONATION_CHARGE', 'EDIT Donation Charge',0, (select id from eg_module where name='Sewerage Tax Management')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='EDIT_DONATION_CHARGE' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, 'NO',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SEWERAGE MONTHLY RATES BY MULTIPLE CLOSETS', 'SEWERAGE MONTHLY RATES BY MULTIPLE CLOSETS',0, (select id from eg_module where name='Sewerage Tax Management')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SEWERAGE MONTHLY RATES BY MULTIPLE CLOSETS' and module= (select id from eg_module where name='Sewerage Tax Management')), current_date, 'NO',0);


