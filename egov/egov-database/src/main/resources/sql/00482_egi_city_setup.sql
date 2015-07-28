CREATE SEQUENCE SEQ_EG_CITYPREFERENCES;

CREATE TABLE EG_CITYPREFERENCES (
id numeric PRIMARY KEY,
gisKML bigint,
gisShape bigint,
logo bigint,
createdby numeric,
createddate timestamp,
lastmodifiedby numeric,
lastmodifieddate timestamp,
version numeric,
FOREIGN KEY (gisKML) REFERENCES eg_filestoremap (id),
FOREIGN KEY (gisShape) REFERENCES eg_filestoremap (id),
FOREIGN KEY (logo) REFERENCES eg_filestoremap (id)
);

ALTER TABLE eg_city ADD COLUMN preferences numeric;

ALTER TABLE eg_city ADD CONSTRAINT fk_preference FOREIGN KEY (preferences) REFERENCES EG_CITYPREFERENCES (id) MATCH FULL;

ALTER TABLE eg_city ADD COLUMN recaptchapub varchar(64);

update eg_city SET recaptchapub ='6LfidggTAAAAADwfl4uOq1CSLhCkH8OE7QFinbVs';

create sequence hibernate_sequence;

create table EG_CITY_AUD (
        id integer not null,
        REV integer not null,
        name varchar(256),
        localName varchar(256),
        active boolean,
        domainURL varchar(128),
        recaptchaPK varchar(64),
        recaptchaPub varchar(64),
        code varchar(4),
        districtCode varchar(10),
        districtName varchar(50),
        longitude double precision,
        latitude double precision,
        REVTYPE numeric,
        primary key (id, REV)
 );

 create table REVINFO (
        REV SERIAL NOT NULL,
        REVTSTMP bigint,
        primary key (REV)
    );

INSERT INTO eg_module values (nextval('seq_eg_module'), 'EGI-SETUP', true, null, (select id from eg_module where name='Administration'),'Setups',5);

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'CitySetup','/city/setup/view',
(select id from eg_module where name='EGI-SETUP'),1,'City Setup',true,'egi',(select id from eg_module where name='Administration' and parentmodule is null));

 Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'CitySetupUpdate','/city/setup/update',
(select id from eg_module where name='EGI-SETUP'),1,'CitySetupUpdate',false,'egi',(select id from eg_module where name='Administration' and parentmodule is null));
 
Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='CitySetup'));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='CitySetupUpdate'));
