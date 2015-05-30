create sequence seq_eg_identityrecovery;

create table eg_identityrecovery (
id numeric primary key,
token varchar(36) unique,
userid bigint,
expiry timestamp,
"version" bigint
);

create sequence seq_eg_messagetemplate;

create table eg_messagetemplate (
id numeric primary key,
templatename varchar(100) unique,
"template" varchar,
locale varchar(10),
"version" bigint
);

insert into eg_messagetemplate values(nextval('seq_eg_messagetemplate'),'user.pwd.recovery','Click the link below to reset your password {2} {0}{1} {2} If clicking the link above doesn''''t work, please copy and paste the URL in a new browser window instead.{2}If you''''ve received this in error, it''''s likely that another user entered your email address or mobile number by mistake while trying to reset a password. If you didn''''t initiate the request, you don''''t need to take any further action and can safely disregard this email.','en_IN');
insert into eg_messagetemplate values(nextval('seq_eg_messagetemplate'),'user.pwd.reset','Hi {0}, {2} Your password has been reset, your new password is {2} {1} {2} Please change this password as soon as you login for more security.','en_IN');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'RecoverPassword', NULL, NULL, now(), '/login/password/recover', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),
0, 'RecoverPassword', 0, NULL, 'egi');

INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'ResetPassword', NULL, NULL, now(), '/login/password/reset', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'User Management'),
0, 'RestPassword', 0, NULL, 'egi');

--drop sequence seq_eg_identityrecovery;
--drop table eg_identityrecovery;
--drop sequence seq_eg_messagetemplate;
--drop table eg_messagetemplate ;