#UP

/**** values for app  config screen 23/12/2008 ****************/

/*** bug Junit inserting value? ***/


select SEQ_EG_APPCONFIG.nextval from dual; 

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'BankPension','Pension Bank','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'COMMUTED_PERIOD','Commuted Period','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'BASIC_COMP_PERCENT','Basic_Comp_Percent','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'DA_PAYCOMPONENT','DA Salary Code','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'GRATUITY_LIMIT','Gratuity Limit','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'PENSION_COMMUTED_PERCENT','Max Pension Commute Percentage','Pension');

insert into eg_appconfig VALUES (SEQ_EG_APPCONFIG.nextval,'GRATUITY_FACTOR','Gratuity_Factor','Pension');


/*** bug Junit inserting value? ***/

select SEQ_EG_APPCONFIG_VALUES.nextval from dual; 
insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='BankPension' ),'22-DEC-2008',86);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='COMMUTED_PERIOD' ),'22-DEC-2008',10);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='BASIC_COMP_PERCENT' ),'22-DEC-2008',20);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='DA_PAYCOMPONENT' ),'22-DEC-2008',30);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='GRATUITY_LIMIT' ),'22-DEC-2008',20000);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='PENSION_COMMUTED_PERCENT' ),'22-DEC-2008',40);

insert into EG_APPCONFIG_VALUES VALUES (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where KEY_NAME ='GRATUITY_FACTOR' ),'22-DEC-2008',45);

/**********************************  Menu tree for Nominee Transfer **********************/

INSERT INTO EG_MODULE
  (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
VALUES
 (SEQ_MODULEMASTER.NEXTVAL, 'Nominee Transfer', SYSDATE, 1, 'Nominee Transfer', (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Pay-Pension'), 'Nominee Transfer');

INSERT INTO EG_ACTION
  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 VALUES
  (SEQ_EG_ACTION.NEXTVAL, 'NomineeTransfer', SYSDATE, '/nomineeTransfer/employeeNomineeDetail.jsp', '', 1, (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Nominee Transfer'), 1, 'Transfer', 1);

INSERT INTO EG_ROLEACTION_MAP
(ROLEID, ACTIONID)
 VALUES
((SELECT e.id_ROLE FROM EG_ROLES e WHERE e.ROLE_NAME='SUPER USER'), (SELECT ID FROM EG_ACTION WHERE name LIKE 'NomineeTransfer'));

#DOWN