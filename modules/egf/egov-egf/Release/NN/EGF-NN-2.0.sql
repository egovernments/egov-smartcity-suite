show user;
set define off;
set define off
 -- INFRASTRUTURE2.0.7.SQL  Starts
CREATE TABLE EG_LOCATION
(
  ID NUMBER NOT NULL,
  NAME VARCHAR2(50) NOT NULL,
  DESCRIPTION VARCHAR2(100),
  IPADDRESS VARCHAR2(150),
  LOCATIONID NUMBER,
  CREATEDDATE DATE,
  LASTMODIFIEDDATE DATE,
  CONSTRAINT PK_EG_LOCATION PRIMARY KEY (ID )
);

CREATE TABLE EG_USERCOUNTER_MAP
(
  ID NUMBER NOT NULL,
  USERID NUMBER NOT NULL,
  COUNTERID NUMBER NOT NULL,
  CONSTRAINT PK_EG_USERCOUNTER_MAP PRIMARY KEY (ID )
);


CREATE TABLE EG_LOGIN_LOG
(
  ID NUMBER NOT NULL,
  USERID NUMBER NOT NULL,
  LOGINTIME DATE,
  LOGOUTTIME DATE,
  CONSTRAINT PK_EG_LOGIN_LOG PRIMARY KEY (ID )
);

  CREATE SEQUENCE SEQ_EG_LOCATION
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

  
  CREATE SEQUENCE SEQ_EG_LOGIN_LOG
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

  CREATE SEQUENCE SEQ_EG_USERCOUNTER_MAP
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;




ALTER TABLE EG_LOCATION ADD (
  CONSTRAINT FK_PARENTID FOREIGN KEY (LOCATIONID) 
    REFERENCES EG_LOCATION (ID));

ALTER TABLE EG_USERCOUNTER_MAP ADD (
  CONSTRAINT FK_MAPUSERID FOREIGN KEY (USERID) 
    REFERENCES EG_USER (ID_USER));

ALTER TABLE EG_USERCOUNTER_MAP ADD (
  CONSTRAINT FK_MAPCOUNTERID FOREIGN KEY (COUNTERID) 
    REFERENCES EG_LOCATION (ID));




ALTER TABLE EG_LOGIN_LOG ADD (
  CONSTRAINT FK_LOGINUSERID FOREIGN KEY (USERID) 
    REFERENCES EG_USER (ID_USER));

ALTER TABLE EG_LOGIN_LOG ADD LOCATIONID NUMBER; 

ALTER TABLE EG_LOGIN_LOG ADD (
  CONSTRAINT FK_LOGINLOCID FOREIGN KEY (LOCATIONID) 
    REFERENCES EG_LOCATION (ID));
    
ALTER TABLE EG_DEDUCTION_DETAILS  MODIFY (DOCTYPEID   NULL);

<!-- JBPM Schema (Execute the schema if you are using jbpm workflow) -->

/*  not executing as JBPM is not used
create table JBPM_ACTION (ID_ number(19,0) not null, class char(1 char) not null, NAME_ varchar2(255 char), ISPROPAGATIONALLOWED_ number(1,0), ACTIONEXPRESSION_ varchar2(255 char), ISASYNC_ number(1,0), REFERENCEDACTION_ number(19,0), ACTIONDELEGATION_ number(19,0), EVENT_ number(19,0), PROCESSDEFINITION_ number(19,0), TIMERNAME_ varchar2(255 char), DUEDATE_ varchar2(255 char), REPEAT_ varchar2(255 char), TRANSITIONNAME_ varchar2(255 char), TIMERACTION_ number(19,0), EXPRESSION_ varchar2(4000 char), EVENTINDEX_ number(10,0), EXCEPTIONHANDLER_ number(19,0), EXCEPTIONHANDLERINDEX_ number(10,0), primary key (ID_));
create table JBPM_BYTEARRAY (ID_ number(19,0) not null, NAME_ varchar2(255 char), FILEDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_BYTEBLOCK (PROCESSFILE_ number(19,0) not null, BYTES_ raw(1024), INDEX_ number(10,0) not null, primary key (PROCESSFILE_, INDEX_));
create table JBPM_COMMENT (ID_ number(19,0) not null, VERSION_ number(10,0) not null, ACTORID_ varchar2(255 char), TIME_ timestamp, MESSAGE_ varchar2(4000 char), TOKEN_ number(19,0), TASKINSTANCE_ number(19,0), TOKENINDEX_ number(10,0), TASKINSTANCEINDEX_ number(10,0), primary key (ID_));
create table JBPM_DECISIONCONDITIONS (DECISION_ number(19,0) not null, TRANSITIONNAME_ varchar2(255 char), EXPRESSION_ varchar2(255 char), INDEX_ number(10,0) not null, primary key (DECISION_, INDEX_));
create table JBPM_DELEGATION (ID_ number(19,0) not null, CLASSNAME_ varchar2(4000 char), CONFIGURATION_ varchar2(4000 char), CONFIGTYPE_ varchar2(255 char), PROCESSDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_EVENT (ID_ number(19,0) not null, EVENTTYPE_ varchar2(255 char), TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSDEFINITION_ number(19,0), NODE_ number(19,0), TRANSITION_ number(19,0), TASK_ number(19,0), primary key (ID_));
create table JBPM_EXCEPTIONHANDLER (ID_ number(19,0) not null, EXCEPTIONCLASSNAME_ varchar2(4000 char), TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSDEFINITION_ number(19,0), GRAPHELEMENTINDEX_ number(10,0), NODE_ number(19,0), TRANSITION_ number(19,0), TASK_ number(19,0), primary key (ID_));
create table JBPM_JOB (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, DUEDATE_ timestamp, PROCESSINSTANCE_ number(19,0), TOKEN_ number(19,0), TASKINSTANCE_ number(19,0), ISSUSPENDED_ number(1,0), ISEXCLUSIVE_ number(1,0), LOCKOWNER_ varchar2(255 char), LOCKTIME_ timestamp, EXCEPTION_ varchar2(4000 char), RETRIES_ number(10,0), NAME_ varchar2(255 char), REPEAT_ varchar2(255 char), TRANSITIONNAME_ varchar2(255 char), ACTION_ number(19,0), GRAPHELEMENTTYPE_ varchar2(255 char), GRAPHELEMENT_ number(19,0), NODE_ number(19,0), primary key (ID_));
create table JBPM_LOG (ID_ number(19,0) not null, CLASS_ char(1 char) not null, INDEX_ number(10,0), DATE_ timestamp, TOKEN_ number(19,0), PARENT_ number(19,0), MESSAGE_ varchar2(4000 char), EXCEPTION_ varchar2(4000 char), ACTION_ number(19,0), NODE_ number(19,0), ENTER_ timestamp, LEAVE_ timestamp, DURATION_ number(19,0), NEWLONGVALUE_ number(19,0), TRANSITION_ number(19,0), CHILD_ number(19,0), SOURCENODE_ number(19,0), DESTINATIONNODE_ number(19,0), VARIABLEINSTANCE_ number(19,0), OLDBYTEARRAY_ number(19,0), NEWBYTEARRAY_ number(19,0), OLDDATEVALUE_ timestamp, NEWDATEVALUE_ timestamp, OLDDOUBLEVALUE_ double precision, NEWDOUBLEVALUE_ double precision, OLDLONGIDCLASS_ varchar2(255 char), OLDLONGIDVALUE_ number(19,0), NEWLONGIDCLASS_ varchar2(255 char), NEWLONGIDVALUE_ number(19,0), OLDSTRINGIDCLASS_ varchar2(255 char), OLDSTRINGIDVALUE_ varchar2(255 char), NEWSTRINGIDCLASS_ varchar2(255 char), NEWSTRINGIDVALUE_ varchar2(255 char), OLDLONGVALUE_ number(19,0), OLDSTRINGVALUE_ varchar2(4000 char), NEWSTRINGVALUE_ varchar2(4000 char), TASKINSTANCE_ number(19,0), TASKACTORID_ varchar2(255 char), TASKOLDACTORID_ varchar2(255 char), SWIMLANEINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_MODULEDEFINITION (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(4000 char), PROCESSDEFINITION_ number(19,0), STARTTASK_ number(19,0), primary key (ID_));
create table JBPM_MODULEINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, PROCESSINSTANCE_ number(19,0), TASKMGMTDEFINITION_ number(19,0), NAME_ varchar2(255 char), primary key (ID_));
create table JBPM_NODE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), DESCRIPTION_ varchar2(4000 char), PROCESSDEFINITION_ number(19,0), ISASYNC_ number(1,0), ISASYNCEXCL_ number(1,0), ACTION_ number(19,0), SUPERSTATE_ number(19,0), SUBPROCNAME_ varchar2(255 char), SUBPROCESSDEFINITION_ number(19,0), DECISIONEXPRESSION_ varchar2(255 char), DECISIONDELEGATION number(19,0), SCRIPT_ number(19,0), SIGNAL_ number(10,0), CREATETASKS_ number(1,0), ENDTASKS_ number(1,0), NODECOLLECTIONINDEX_ number(10,0), primary key (ID_));
create table JBPM_POOLEDACTOR (ID_ number(19,0) not null, VERSION_ number(10,0) not null, ACTORID_ varchar2(255 char), SWIMLANEINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_PROCESSDEFINITION (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), DESCRIPTION_ varchar2(4000 char), VERSION_ number(10,0), ISTERMINATIONIMPLICIT_ number(1,0), STARTSTATE_ number(19,0), primary key (ID_));
create table JBPM_PROCESSINSTANCE (ID_ number(19,0) not null, VERSION_ number(10,0) not null, KEY_ varchar2(255 char), START_ timestamp, END_ timestamp, ISSUSPENDED_ number(1,0), PROCESSDEFINITION_ number(19,0), ROOTTOKEN_ number(19,0), SUPERPROCESSTOKEN_ number(19,0), primary key (ID_), unique (KEY_, PROCESSDEFINITION_));
create table JBPM_RUNTIMEACTION (ID_ number(19,0) not null, VERSION_ number(10,0) not null, EVENTTYPE_ varchar2(255 char), TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSINSTANCE_ number(19,0), ACTION_ number(19,0), PROCESSINSTANCEINDEX_ number(10,0), primary key (ID_));
create table JBPM_SWIMLANE (ID_ number(19,0) not null, NAME_ varchar2(255 char), ACTORIDEXPRESSION_ varchar2(255 char), POOLEDACTORSEXPRESSION_ varchar2(255 char), ASSIGNMENTDELEGATION_ number(19,0), TASKMGMTDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_SWIMLANEINSTANCE (ID_ number(19,0) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), ACTORID_ varchar2(255 char), SWIMLANE_ number(19,0), TASKMGMTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TASK (ID_ number(19,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ varchar2(4000 char), PROCESSDEFINITION_ number(19,0), ISBLOCKING_ number(1,0), ISSIGNALLING_ number(1,0), CONDITION_ varchar2(255 char), DUEDATE_ varchar2(255 char), PRIORITY_ number(10,0), ACTORIDEXPRESSION_ varchar2(255 char), POOLEDACTORSEXPRESSION_ varchar2(255 char), TASKMGMTDEFINITION_ number(19,0), TASKNODE_ number(19,0), STARTSTATE_ number(19,0), ASSIGNMENTDELEGATION_ number(19,0), SWIMLANE_ number(19,0), TASKCONTROLLER_ number(19,0), primary key (ID_));
create table JBPM_TASKACTORPOOL (TASKINSTANCE_ number(19,0) not null, POOLEDACTOR_ number(19,0) not null, primary key (TASKINSTANCE_, POOLEDACTOR_));
create table JBPM_TASKCONTROLLER (ID_ number(19,0) not null, TASKCONTROLLERDELEGATION_ number(19,0), primary key (ID_));
create table JBPM_TASKINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ varchar2(4000 char), ACTORID_ varchar2(255 char), CREATE_ timestamp, START_ timestamp, END_ timestamp, DUEDATE_ timestamp, PRIORITY_ number(10,0), ISCANCELLED_ number(1,0), ISSUSPENDED_ number(1,0), ISOPEN_ number(1,0), ISSIGNALLING_ number(1,0), ISBLOCKING_ number(1,0), TASK_ number(19,0), TOKEN_ number(19,0), PROCINST_ number(19,0), SWIMLANINSTANCE_ number(19,0), TASKMGMTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TOKEN (ID_ number(19,0) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), START_ timestamp, END_ timestamp, NODEENTER_ timestamp, NEXTLOGINDEX_ number(10,0), ISABLETOREACTIVATEPARENT_ number(1,0), ISTERMINATIONIMPLICIT_ number(1,0), ISSUSPENDED_ number(1,0), LOCK_ varchar2(255 char), NODE_ number(19,0), PROCESSINSTANCE_ number(19,0), PARENT_ number(19,0), SUBPROCESSINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TOKENVARIABLEMAP (ID_ number(19,0) not null, VERSION_ number(10,0) not null, TOKEN_ number(19,0), CONTEXTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TRANSITION (ID_ number(19,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ varchar2(4000 char), PROCESSDEFINITION_ number(19,0), FROM_ number(19,0), TO_ number(19,0), CONDITION_ varchar2(255 char), FROMINDEX_ number(10,0), primary key (ID_));
create table JBPM_VARIABLEACCESS (ID_ number(19,0) not null, VARIABLENAME_ varchar2(255 char), ACCESS_ varchar2(255 char), MAPPEDNAME_ varchar2(255 char), PROCESSSTATE_ number(19,0), TASKCONTROLLER_ number(19,0), INDEX_ number(10,0), SCRIPT_ number(19,0), primary key (ID_));
create table JBPM_VARIABLEINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), CONVERTER_ char(1 char), TOKEN_ number(19,0), TOKENVARIABLEMAP_ number(19,0), PROCESSINSTANCE_ number(19,0), BYTEARRAYVALUE_ number(19,0), DATEVALUE_ timestamp, DOUBLEVALUE_ double precision, LONGIDCLASS_ varchar2(255 char), LONGVALUE_ number(19,0), STRINGIDCLASS_ varchar2(255 char), STRINGVALUE_ varchar2(255 char), TASKINSTANCE_ number(19,0), primary key (ID_));
create index IDX_ACTION_EVENT on JBPM_ACTION (EVENT_);
create index IDX_ACTION_ACTNDL on JBPM_ACTION (ACTIONDELEGATION_);
create index IDX_ACTION_PROCDF on JBPM_ACTION (PROCESSDEFINITION_);
alter table JBPM_ACTION add constraint FK_ACTION_EVENT foreign key (EVENT_) references JBPM_EVENT;
alter table JBPM_ACTION add constraint FK_ACTION_EXPTHDL foreign key (EXCEPTIONHANDLER_) references JBPM_EXCEPTIONHANDLER;
alter table JBPM_ACTION add constraint FK_ACTION_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_ACTION add constraint FK_CRTETIMERACT_TA foreign key (TIMERACTION_) references JBPM_ACTION;
alter table JBPM_ACTION add constraint FK_ACTION_ACTNDEL foreign key (ACTIONDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_ACTION add constraint FK_ACTION_REFACT foreign key (REFERENCEDACTION_) references JBPM_ACTION;
alter table JBPM_BYTEARRAY add constraint FK_BYTEARR_FILDEF foreign key (FILEDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_BYTEBLOCK add constraint FK_BYTEBLOCK_FILE foreign key (PROCESSFILE_) references JBPM_BYTEARRAY;
create index IDX_COMMENT_TOKEN on JBPM_COMMENT (TOKEN_);
create index IDX_COMMENT_TSK on JBPM_COMMENT (TASKINSTANCE_);
alter table JBPM_COMMENT add constraint FK_COMMENT_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_COMMENT add constraint FK_COMMENT_TSK foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_DECISIONCONDITIONS add constraint FK_DECCOND_DEC foreign key (DECISION_) references JBPM_NODE;
create index IDX_DELEG_PRCD on JBPM_DELEGATION (PROCESSDEFINITION_);
alter table JBPM_DELEGATION add constraint FK_DELEGATION_PRCD foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_EVENT add constraint FK_EVENT_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_EVENT add constraint FK_EVENT_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_EVENT add constraint FK_EVENT_TRANS foreign key (TRANSITION_) references JBPM_TRANSITION;
alter table JBPM_EVENT add constraint FK_EVENT_TASK foreign key (TASK_) references JBPM_TASK;
create index IDX_JOB_TSKINST on JBPM_JOB (TASKINSTANCE_);
create index IDX_JOB_PRINST on JBPM_JOB (PROCESSINSTANCE_);
create index IDX_JOB_TOKEN on JBPM_JOB (TOKEN_);
alter table JBPM_JOB add constraint FK_JOB_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_JOB add constraint FK_JOB_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_JOB add constraint FK_JOB_PRINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_JOB add constraint FK_JOB_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_JOB add constraint FK_JOB_TSKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_SOURCENODE foreign key (SOURCENODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_LOG add constraint FK_LOG_OLDBYTES foreign key (OLDBYTEARRAY_) references JBPM_BYTEARRAY;
alter table JBPM_LOG add constraint FK_LOG_NEWBYTES foreign key (NEWBYTEARRAY_) references JBPM_BYTEARRAY;
alter table JBPM_LOG add constraint FK_LOG_CHILDTOKEN foreign key (CHILD_) references JBPM_TOKEN;
alter table JBPM_LOG add constraint FK_LOG_DESTNODE foreign key (DESTINATIONNODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_TASKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_SWIMINST foreign key (SWIMLANEINSTANCE_) references JBPM_SWIMLANEINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_PARENT foreign key (PARENT_) references JBPM_LOG;
alter table JBPM_LOG add constraint FK_LOG_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_LOG add constraint FK_LOG_VARINST foreign key (VARIABLEINSTANCE_) references JBPM_VARIABLEINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_TRANSITION foreign key (TRANSITION_) references JBPM_TRANSITION;
create index IDX_MODDEF_PROCDF on JBPM_MODULEDEFINITION (PROCESSDEFINITION_);
alter table JBPM_MODULEDEFINITION add constraint FK_TSKDEF_START foreign key (STARTTASK_) references JBPM_TASK;
alter table JBPM_MODULEDEFINITION add constraint FK_MODDEF_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
create index IDX_MODINST_PRINST on JBPM_MODULEINSTANCE (PROCESSINSTANCE_);
alter table JBPM_MODULEINSTANCE add constraint FK_TASKMGTINST_TMD foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_MODULEINSTANCE add constraint FK_MODINST_PRCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
create index IDX_PSTATE_SBPRCDEF on JBPM_NODE (SUBPROCESSDEFINITION_);
create index IDX_NODE_SUPRSTATE on JBPM_NODE (SUPERSTATE_);
create index IDX_NODE_PROCDEF on JBPM_NODE (PROCESSDEFINITION_);
create index IDX_NODE_ACTION on JBPM_NODE (ACTION_);
alter table JBPM_NODE add constraint FK_PROCST_SBPRCDEF foreign key (SUBPROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_NODE add constraint FK_NODE_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_NODE add constraint FK_NODE_SCRIPT foreign key (SCRIPT_) references JBPM_ACTION;
alter table JBPM_NODE add constraint FK_NODE_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_NODE add constraint FK_DECISION_DELEG foreign key (DECISIONDELEGATION) references JBPM_DELEGATION;
alter table JBPM_NODE add constraint FK_NODE_SUPERSTATE foreign key (SUPERSTATE_) references JBPM_NODE;
create index IDX_PLDACTR_ACTID on JBPM_POOLEDACTOR (ACTORID_);
create index IDX_TSKINST_SWLANE on JBPM_POOLEDACTOR (SWIMLANEINSTANCE_);
alter table JBPM_POOLEDACTOR add constraint FK_POOLEDACTOR_SLI foreign key (SWIMLANEINSTANCE_) references JBPM_SWIMLANEINSTANCE;
create index IDX_PROCDEF_STRTST on JBPM_PROCESSDEFINITION (STARTSTATE_);
alter table JBPM_PROCESSDEFINITION add constraint FK_PROCDEF_STRTSTA foreign key (STARTSTATE_) references JBPM_NODE;
create index IDX_PROCIN_ROOTTK on JBPM_PROCESSINSTANCE (ROOTTOKEN_);
create index IDX_PROCIN_SPROCTK on JBPM_PROCESSINSTANCE (SUPERPROCESSTOKEN_);
create index IDX_PROCIN_PROCDEF on JBPM_PROCESSINSTANCE (PROCESSDEFINITION_);
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_ROOTTKN foreign key (ROOTTOKEN_) references JBPM_TOKEN;
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_SPROCTKN foreign key (SUPERPROCESSTOKEN_) references JBPM_TOKEN;
create index IDX_RTACTN_PRCINST on JBPM_RUNTIMEACTION (PROCESSINSTANCE_);
create index IDX_RTACTN_ACTION on JBPM_RUNTIMEACTION (ACTION_);
alter table JBPM_RUNTIMEACTION add constraint FK_RTACTN_PROCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_RUNTIMEACTION add constraint FK_RTACTN_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_SWIMLANE add constraint FK_SWL_ASSDEL foreign key (ASSIGNMENTDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_SWIMLANE add constraint FK_SWL_TSKMGMTDEF foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
create index IDX_SWIMLINST_SL on JBPM_SWIMLANEINSTANCE (SWIMLANE_);
alter table JBPM_SWIMLANEINSTANCE add constraint FK_SWIMLANEINST_TM foreign key (TASKMGMTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_SWIMLANEINSTANCE add constraint FK_SWIMLANEINST_SL foreign key (SWIMLANE_) references JBPM_SWIMLANE;
create index IDX_TASK_TSKNODE on JBPM_TASK (TASKNODE_);
create index IDX_TASK_PROCDEF on JBPM_TASK (PROCESSDEFINITION_);
create index IDX_TASK_TASKMGTDF on JBPM_TASK (TASKMGMTDEFINITION_);
alter table JBPM_TASK add constraint FK_TSK_TSKCTRL foreign key (TASKCONTROLLER_) references JBPM_TASKCONTROLLER;
alter table JBPM_TASK add constraint FK_TASK_ASSDEL foreign key (ASSIGNMENTDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_TASK add constraint FK_TASK_TASKNODE foreign key (TASKNODE_) references JBPM_NODE;
alter table JBPM_TASK add constraint FK_TASK_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_TASK add constraint FK_TASK_STARTST foreign key (STARTSTATE_) references JBPM_NODE;
alter table JBPM_TASK add constraint FK_TASK_TASKMGTDEF foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_TASK add constraint FK_TASK_SWIMLANE foreign key (SWIMLANE_) references JBPM_SWIMLANE;
alter table JBPM_TASKACTORPOOL add constraint FK_TSKACTPOL_PLACT foreign key (POOLEDACTOR_) references JBPM_POOLEDACTOR;
alter table JBPM_TASKACTORPOOL add constraint FK_TASKACTPL_TSKI foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_TASKCONTROLLER add constraint FK_TSKCTRL_DELEG foreign key (TASKCONTROLLERDELEGATION_) references JBPM_DELEGATION;
create index IDX_TASKINST_TOKN on JBPM_TASKINSTANCE (TOKEN_);
create index IDX_TASKINST_TSK on JBPM_TASKINSTANCE (TASK_, PROCINST_);
create index IDX_TSKINST_TMINST on JBPM_TASKINSTANCE (TASKMGMTINSTANCE_);
create index IDX_TSKINST_SLINST on JBPM_TASKINSTANCE (SWIMLANINSTANCE_);
create index IDX_TASK_ACTORID on JBPM_TASKINSTANCE (ACTORID_);
alter table JBPM_TASKINSTANCE add constraint FK_TSKINS_PRCINS foreign key (PROCINST_) references JBPM_PROCESSINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TMINST foreign key (TASKMGMTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_SLINST foreign key (SWIMLANINSTANCE_) references JBPM_SWIMLANEINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TASK foreign key (TASK_) references JBPM_TASK;
create index IDX_TOKEN_PROCIN on JBPM_TOKEN (PROCESSINSTANCE_);
create index IDX_TOKEN_SUBPI on JBPM_TOKEN (SUBPROCESSINSTANCE_);
create index IDX_TOKEN_NODE on JBPM_TOKEN (NODE_);
create index IDX_TOKEN_PARENT on JBPM_TOKEN (PARENT_);
alter table JBPM_TOKEN add constraint FK_TOKEN_PARENT foreign key (PARENT_) references JBPM_TOKEN;
alter table JBPM_TOKEN add constraint FK_TOKEN_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_TOKEN add constraint FK_TOKEN_PROCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_TOKEN add constraint FK_TOKEN_SUBPI foreign key (SUBPROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
create index IDX_TKVARMAP_CTXT on JBPM_TOKENVARIABLEMAP (CONTEXTINSTANCE_);
create index IDX_TKVVARMP_TOKEN on JBPM_TOKENVARIABLEMAP (TOKEN_);
alter table JBPM_TOKENVARIABLEMAP add constraint FK_TKVARMAP_CTXT foreign key (CONTEXTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_TOKENVARIABLEMAP add constraint FK_TKVARMAP_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
create index IDX_TRANSIT_TO on JBPM_TRANSITION (TO_);
create index IDX_TRANSIT_FROM on JBPM_TRANSITION (FROM_);
create index IDX_TRANS_PROCDEF on JBPM_TRANSITION (PROCESSDEFINITION_);
alter table JBPM_TRANSITION add constraint FK_TRANSITION_TO foreign key (TO_) references JBPM_NODE;
alter table JBPM_TRANSITION add constraint FK_TRANS_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_TRANSITION add constraint FK_TRANSITION_FROM foreign key (FROM_) references JBPM_NODE;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_TSKCTRL foreign key (TASKCONTROLLER_) references JBPM_TASKCONTROLLER;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_SCRIPT foreign key (SCRIPT_) references JBPM_ACTION;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_PROCST foreign key (PROCESSSTATE_) references JBPM_NODE;
create index IDX_VARINST_TKVARMP on JBPM_VARIABLEINSTANCE (TOKENVARIABLEMAP_);
create index IDX_VARINST_PRCINS on JBPM_VARIABLEINSTANCE (PROCESSINSTANCE_);
create index IDX_VARINST_TK on JBPM_VARIABLEINSTANCE (TOKEN_);
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_TK foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_TKVARMP foreign key (TOKENVARIABLEMAP_) references JBPM_TOKENVARIABLEMAP;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_PRCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VAR_TSKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_VARIABLEINSTANCE add constraint FK_BYTEINST_ARRAY foreign key (BYTEARRAYVALUE_) references JBPM_BYTEARRAY;
create sequence hibernate_sequence;


alter table JBPM_TASKINSTANCE modify create_ date ;	
alter table JBPM_TASKINSTANCE modify start_ date ;			
alter table JBPM_TASKINSTANCE modify end_ date ;
alter table JBPM_TASKINSTANCE modify duedate_ date ;
alter table JBPM_VARIABLEINSTANCE modify datevalue_ date ;
alter table JBPM_TOKEN modify start_ date ;
alter table JBPM_TOKEN modify end_ date ;
alter table JBPM_TOKEN modify nodeenter_ date ;
alter table JBPM_COMMENT modify time_ date ;
alter table JBPM_JOB modify duedate_ date ;
alter table JBPM_JOB modify locktime_ date ;
alter table JBPM_LOG modify date_ date ;
alter table JBPM_LOG modify enter_ date ;
alter table JBPM_LOG modify leave_ date ;
alter table JBPM_LOG modify olddatevalue_ date ;
alter table JBPM_LOG modify newdatevalue_ date ;
alter table JBPM_PROCESSINSTANCE modify start_ date ;
alter table JBPM_PROCESSINSTANCE modify end_ date ;
*/

commit;

 -- INFRASTRUTURE2.0.7.SQL ends

-- Infrastructure2.1.3.sql starts

ALTER TABLE EG_LOCATION
  ADD ISACTIVE NUMBER(1);
  
  
  ALTER TABLE EG_LOCATION
 DROP (IPADDRESS);
 
 
ALTER TABLE EG_LOCATION
  ADD ISLOCATION NUMBER(1);
 
CREATE TABLE EG_LOCATION_IPMAP
(
  ID          INTEGER                           NOT NULL,
  LOCATIONID  INTEGER                           NOT NULL,
  IPADDRESS   VARCHAR2(150 BYTE)                NOT NULL
);


CREATE UNIQUE INDEX PK_EG_LOCATION_IPMAP ON EG_LOCATION_IPMAP
(ID);

CREATE UNIQUE INDEX UNQ_IPADDRESS ON EG_LOCATION_IPMAP
(IPADDRESS);

ALTER TABLE EG_LOCATION_IPMAP ADD (
  CONSTRAINT PK_EG_LOCATION_IPMAP PRIMARY KEY (ID));


ALTER TABLE EG_LOCATION_IPMAP ADD (
  CONSTRAINT FK_LOCATION_ID FOREIGN KEY (LOCATIONID) 
    REFERENCES EG_LOCATION (ID));



CREATE SEQUENCE SEQ_EG_LOCATION_IPMAP
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;


ALTER TABLE EG_USERCOUNTER_MAP
  ADD FROMDATE DATE NOT NULL
  ADD TODATE DATE
  ADD MODIFIEDBY NUMBER NOT NULL
  ADD MODIFIEDDATE DATE NOT NULL;


update eg_user set isactive=1 where isactive is null;

alter table eg_module  Modify  module_name  varchar2(100); 
alter table eg_action Modify url varchar2(150);
alter table eg_action Modify queryparams varchar2(150);


alter table vouchermis drop constraint fk_vmis_schemeid;
alter table worksdetail drop constraint fk_scheme_wd;
drop table scheme; 

CREATE TABLE SCHEME
(
  ID           NUMBER(10)                       NOT NULL,
  CODE         VARCHAR2(20 BYTE),
  NAME         VARCHAR2(50 BYTE),
  VALIDFROM    DATE,
  VALIDTO      DATE,
  ISACTIVE     VARCHAR2(1 BYTE),
  DESCRIPTION  VARCHAR2(255 BYTE),
  FUNDID       INTEGER,
  SECTORID     INTEGER,
  AAES         NUMBER,
  FIELDID      NUMBER
);

ALTER TABLE SCHEME ADD (
  FOREIGN KEY (FUNDID) 
 REFERENCES FUND (ID));

ALTER TABLE SCHEME ADD (
  FOREIGN KEY (FIELDID) 
 REFERENCES EG_BOUNDARY (ID_BNDRY));

CREATE SEQUENCE SEQ_SCHEME
  START WITH 1
  NOMAXVALUE
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE TABLE SUB_SCHEME
(
  ID         NUMBER                PRIMARY KEY,
  CODE       VARCHAR2(50 BYTE)					NOT NULL,
  NAME       VARCHAR2(50 BYTE)					NOT NULL,
  VALIDFROM  DATE		 NOT NULL,
  VALIDTO    DATE,
  ISACTIVE   VARCHAR2(1 BYTE)	  NOT NULL,
  SCHEMEID   NUMBER		NOT NULL,
  lastmodifieddate 		DATE NOT NULL
);

alter table SCHEME modify id number(10) primary key;

ALTER TABLE SUB_SCHEME ADD (
  CONSTRAINT SUB_SCHEME_R01 
 FOREIGN KEY (SCHEMEID) 
 REFERENCES SCHEME (ID));
 

CREATE SEQUENCE SEQ_SUB_SCHEME
  START WITH 1
  NOMAXVALUE
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;
  
-- Infrastructure2.1.3.sql ends  

--menu tree related changes
drop sequence SEQ_EG_MODULE;
drop sequence seq_eg_action;
drop sequence seq_order_number;


Create SEQUENCE SEQ_EG_MODULE
  START WITH 3
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

Create SEQUENCE seq_eg_action
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

-- for ordering in menu tree  
  Create SEQUENCE seq_order_number
    START WITH 1
    MAXVALUE 100
    MINVALUE 1
    NOCYCLE
    NOCACHE
  NOORDER;

alter table eg_module  Modify  module_name  varchar2(100); 
alter table eg_action Modify url varchar2(150);
alter table eg_action Modify queryparams varchar2(150);


  
delete from eg_roleaction_map;
delete from eg_actionrg_map;

delete from menutree;

delete from eg_action;

delete from eg_module where id_module<>2;

  
Insert into EG_MODULE 
(ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
Values
(seq_eg_module.nextVal, 'EGF',sysdate,'1','/EGF',NULL,NULL,'EGF');


--------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
 (seq_eg_module.nextVal, 'Transactions',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),1,'Transactions');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
 (seq_eg_module.nextVal, 'Reports',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),2,'Reports');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Masters',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),3,'Masters');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Processing',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),4,'Processing');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Set-up',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),5,'Set-up');
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Administration',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),6,'Administration');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Deductions',sysdate,'1','',(select id_module from eg_module where module_name='EGF'),7,'Deductions');



--Transactions----------------------------------------------------------------------------------------------------------------



Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Receipts',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),1,'Receipts');


Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Bills Accounting',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),2,'Bills Accounting');


Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Payments',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),3,'Payments');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Journal Proper',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),4,'Journal Proper');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Contra Entries',sysdate,'1','',(select id_module from eg_module where module_name='Transactions'),5,'Contra Entries');

--Reports---------------------------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Financial Statements',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),1,'Financial Statements');
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Accounting Records',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),2,'Accounting Records');
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'MIS Reports',sysdate,'1','',(select id_module from eg_module where module_name='Reports'),3,'MIS Reports');
  
  
----Masters-----------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Chart of Accounts',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),1,'Chart of Accounts');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal,'Procurement Orders',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),2,'Procurement Orders');  
  
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Supplier/Contractors',sysdate,'1','',(select id_module from eg_module where module_name='Masters'),3,'Supplier/Contractors');


------Set-up---------------------------------------------------------------------------------------------------------------

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Report Schedule Mapping',sysdate,'1','',(select id_module from eg_module where module_name='Set-up'),4,'Report Schedule Mapping');


--Deductions---------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Master',sysdate,'1','',(select id_module from eg_module where module_name='Deductions'),1,'Master');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Remittance Recovery',sysdate,'1','',(select id_module from eg_module where module_name='Deductions'),2,'Remitance Recovery');

--Deductions-Master-------------------------------------------------------------------------------------------------------------------------
Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Party Types',sysdate,'1','',(select id_module from eg_module where module_name='Master'),1,'Party Types');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)
 Values
  (seq_eg_module.nextVal, 'Contract Types',sysdate,'1','',(select id_module from eg_module where module_name='Master'),2,'Contract Types');

Insert into EG_MODULE   (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED,  BASEURL, PARENTID, ORDER_NUM, MODULE_DESC)

 Values
  (seq_eg_module.nextVal, 'Recovery Masters',sysdate,'1','',(select id_module from eg_module where module_name='Master'),3,'Recovery Masters');


--end of eg_module
-- ==================================================================

-- LOGIN 
DELETE FROM EG_ACTION WHERE NAME='LOGIN';
Insert into eg_action
   (ID, NAME, UPDATEDTIME, URL, DISPLAY_NAME, IS_ENABLED)
 Values
   (seq_eg_action.nextVal, 'LOGIN', sysdate, '/eGov.jsp', 'LOGIN', 0);
============================

--eg_action details------------------------------------------------
--
--SQL Statement which produced this data:
--  select m.id,m.name,m.parentid,m.actionid,(select name from menutree m2 where m2.id=m.parentid) from menutree m,eg_action e  where actionid is not null and e.id=m.actionid order by parentid,id
--

--Transactions--------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Confirm Vouchers', NULL,NULL, sysdate, 
    '/HTML/ConfirmVoucher.htm?', '', NULL, (select id_module from eg_module where module_name='Transactions'), 6, 
    'Confirm Vouchers', 1, 'HelpAssistance/Confirm Vouchers.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cancel Vouchers', NULL,null, sysdate, 
    '/HTML/CancelVoucher.htm', '', NULL, (select id_module from eg_module where module_name='Transactions'), 7, 
    'Cancel Vouchers', 1, 'HelpAssistance/Cancel Vouchers.htm');

--Masters---------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Code-Screen Mappings', NULL,NULL, sysdate, 
    '/HTML/ftServicesmap.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 4, 
    'Code-Screen Mappings', 1, 'HelpAssistance/Code-Screen Mapping');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'User-defined Codes', NULL,NULL, sysdate, 
    '/HTML/SubCodesEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 5, 
    'User-defined Codes', 1, 'HelpAssistance/User-defined Codes.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Tax Setup-Enquiry', NULL,NULL, sysdate, 
    '/HTML/TaxCodeMapEnq.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 6, 
    'Tax Setup-Enquiry', 1, 'HelpAssistance/Tax Setup-Enquiry.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Source of Financing', NULL,NULL, sysdate, 
    '/HTML/FundSource.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 7, 
    'Source of Financing', 1, 'HelpAssistance/Source of Financing.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Collection/Payment point-View', NULL,NULL, sysdate, 
    '/HTML/BillCollector.htm', '', NULL, (select id_module from eg_module where module_name='Masters'),8, 
    'Collection/Payment point-View', 1, 'HelpAssistance/Collection/Payment point-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Accounting Entity', NULL,NULL, sysdate, 
    '/HTML/AccountingEntity.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 9, 
    'Accounting Entity', 1, 'HelpAssistance/Accounting Entity.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Setup Cheque in Hand/Cash in Hand', NULL,NULL, sysdate, 
    '/HTML/setupCashCheckInHand.htm', '', NULL, (select id_module from eg_module where module_name='Masters'), 10, 
    'Setup Cheque in Hand/Cash in Hand', 1, 'HelpAssistance/Setup Cheque in Hand/Cash in Hand.htm');

--Processing-----------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Financial Year-SetUp', NULL,NULL, sysdate, 
    '/HTML/FinancialYearEnq.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 1, 
    'Financial Year', 1, 'HelpAssistance/Financial Year.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Opening Balance-SetUp', NULL,NULL, sysdate, 
   '/HTML/OpeningBalance.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 2, 
    'Opening Balance', 1, 'HelpAssistance/Opening Balance.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Fiscal periods-SetUp', NULL,NULL, sysdate, 
    '/HTML/SetUp.htm', '', NULL, (select id_module from eg_module where module_name='Processing'), 3, 
    'Close Period', 1, 'HelpAssistance/Close Period.htm');
    
--Set-up--------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Funds', NULL,NULL, sysdate, 
    '/HTML/Fund.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 1, 
    'Funds', 1, 'HelpAssistance/Funds.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Functions', NULL,NULL, sysdate, 
    '/HTML/Function.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 2, 
    'Functions', 1, 'HelpAssistance/Functions.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'ULB Details', NULL,NULL, sysdate, 
    '/HTML/CompanyDetail.htm', '', NULL, (select id_module from eg_module where module_name='Set-up'), 3, 
    'ULB Details', 1, 'HelpAssistance/ULB Details.htm');


--Report Schedule Mapping-----------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Report Schedule Mapping-Create', NULL,NULL, sysdate, 
    '/Reports/ScheduleMaster.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 1, 
    'Report Schedule Mapping-Create', 1, 'HelpAssistance/Report Schedule Mapping-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Report Schedule Mapping-Modify', NULL,NULL, sysdate, 
    '/Reports/ScheduleMaster.jsp', 'showMode=edit', NULL,'' , 0, 
    'Report Schedule Mapping-Modify', 0, 'HelpAssistance/Report Schedule Mapping-Modify.htm');
    
  Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Schedules-Search ', NULL,NULL, sysdate, 
        '/Reports/ScheduleSearch.htm', '', NULL, (select id_module from eg_module where module_name='Report Schedule Mapping'), 2, 
        'View/Modify Report Schedule ',1, 'HelpAssistance/View-Modify Report Schedule.htm');


-- ===============================
-- 3RD LEVEL BEGINS --
--Receipts------------------------------------------------------------------------------------------------------

drop sequence seq_order_number;
Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Property Tax Collection-create', NULL,NULL, sysdate, 
    '/HTML/PT_Field.htm', 'showMode=new', NULL, (select id_module from eg_module where 
    module_name='Receipts'), seq_order_number.nextval, 
    'Property Tax Collection-create', 1, 'HelpAssistance/Property Tax Collection-create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Miscellaneous Receipt-Create', NULL,NULL, sysdate, 
    '/HTML/MiscReceipt.htm', 'showMode=new', NULL, (select id_module from eg_module where 
    module_name='Receipts'), seq_order_number.nextval, 
    'Miscellaneous Receipt-Create', 1, 'HelpAssistance/Miscellaneous Receipts-Create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'View Receipts-Search', 1, 'HelpAssistance/View Receipts.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'modify Receipts-Search', 1, 'HelpAssistance/Modify Receipts-Search.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Receipts-Search', NULL,NULL, sysdate, 
    '/HTML/ReceiptSearch.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Receipts'), seq_order_number.nextval, 
    'Reverse Receipts-Search', 1, 'HelpAssistance/Reverse Receipts-Search.htm');

--Bills Accounting--------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Bill-Create', NULL,NULL, sysdate, 
    '/HTML/ContractorJournal.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 1, 
    'Contractor Bill-Create', 1, 'HelpAssistance/Contractor Bill-Create.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier Bill-Create', NULL,NULL, sysdate, 
    '/HTML/SupplierJournal.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 2, 
    'Supplier Bill-Create', 1, 'HelpAssistance/Supplier Bill-Create.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Bill-create', NULL,NULL, sysdate, 
    '/HTML/JV_Salary.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 3, 
    'Salary Bill-Create', 1, 'HelpAssistance/Salary Bill-create.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View  Bills-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 4, 
    'View  Bills-Search', 1, 'HelpAssistance/View  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Modify  Bills-Search', NULL,NULL, sysdate, 
        '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 5, 
    'Modify  Bills-Search', 1, 'HelpAssistance/Modify  Bills-Search.htm');
    
    Insert into eg_action
       (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
     Values
       (seq_eg_action.nextVal, 'Reverse Bills-Search', NULL,NULL, sysdate, 
        '/HTML/JournalVoucherSearch.jsp', 'journalType=Bill&showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 6, 
    'Reverse Bills-Search', 1, 'HelpAssistance/Reverse Bills-Search.htm');


--Payments---------------------------------------------------------------------------------------------------------------------------------------

drop sequence seq_order_number;
Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Payment-Create', NULL, NULL, sysdate, 
    '/HTML/DirectBankPayment.htm', 'showMode=paymentBank', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Bank Payment-Create', 1, 'HelpAssistance/Bank Payment-Create.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Payments-Create', NULL, NULL, sysdate, 
    '/HTML/DirectCashPayment.htm', 'showMode=paymentCash', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Cash Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Payments-Create','' , NULL, sysdate, 
    '/HTML/AdvanceJournal.htm', '', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Advance Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay Supplier/Contractor-Create', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPayment.jsp', 'showMode=searchPay', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Pay Supplier/Contractor-Create', 1, 'HelpAssistance/Pay Supplier/Contractor-Create');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Salary Payments-Create', NULL, NULL, sysdate, 
    '/HTML/SubledgerSalaryPayment.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Salary Payments-Create', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Payments-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'View Payments-Search', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Payment-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Modify Payment-Search', 1, 'HelpAssistance/');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Payments-Search', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPaymentSearch.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Reverse Payments-Search', 1, 'HelpAssistance/');



----Journal Voucher--------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Create', NULL,NULL, sysdate, 
    '/HTML/JV_General.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Journal Proper'), 1, 
    'Journal Voucher-Create', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=General&showMode=view', NULL, (select id_module from eg_module where module_name='Journal Proper'), 2, 
    'View Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=General&showMode=edit', NULL, (select id_module from eg_module where module_name='Journal Proper'), 3, 
    'Modify Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Journal Vouchers-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=General&showMode=modify', NULL, (select id_module from eg_module where module_name='Journal Proper'), 4, 
    'Reverse Journal Vouchers-Search', 1, 'HelpAssistance/Journal Voucher.htm');
    
-----------------------------------------------------------------------------------------------------------------------------------------

drop sequence seq_order_number;
Create SEQUENCE seq_order_number
  START WITH 1
  MAXVALUE 100
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Create', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_CToB.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 1, 
    'Cash Deposit-Create', 1, 'HelpAssistance/Cash Deposit.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Create', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToC.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Create', 1, 'HelpAssistance/Cash Withdrawal.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Deposit-Create', NULL,NULL, sysdate, 
    '/HTML/PayInSlip.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Cheque Deposit-Create', 1, 'HelpAssistance/Pay in.htm');
    
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Create', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToB.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Create', 1, 'HelpAssistance/Bank to Bank Transfer.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 5, 
    'View Contra Entries-Search', 1, 'HelpAssistance/View Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 6, 
    'Modify Contra Entries-Search', 1, 'HelpAssistance/Modify Contra Entries-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Reverse Contra Entries-Search', NULL,NULL, sysdate, 
    '/HTML/JournalVoucherSearch.jsp', 'journalType=Contra&showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 7, 
    'Reverse Contra Entries-Search', 1, 'HelpAssistance/Reverse Contra Entries-Search.htm');



--Financial Statements---------------------------------------------------


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Income - Expenditures Statement', NULL,NULL, sysdate, 
    '/Reports/IncomeExpenseReports.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 1, 
    'Income - Expenditures Statement', 1, 'HelpAssistance/Income - Expenditures Statement.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt/Payment Report', NULL,NULL, sysdate, 
    '/Reports/RPReport.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 2, 
    'Receipt/Payment Report', 1, 'HelpAssistance/Receipt/Payment Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Balance Sheet', NULL,NULL, sysdate, 
    '/Reports/BSReport.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 3, 
    'Balance Sheet', 1, 'HelpAssistance/Balance Sheet.htm');

--Chart Of Accounts----------------------------------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Chart Of Accounts', NULL,NULL, sysdate, 
    '/commonyui/egov/ChartOfaccountsMenuTree.jsp?eGovAppName=ChartOfAccounts', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 1, 
    'Chart Of Accounts', 1, 'HelpAssistance/Chart Of Accounts.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Add Bank', NULL,NULL, sysdate, 
    '/HTML/BankAdd.htm', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 2, 
    'Add Bank', 1, 'HelpAssistance/Add Bank.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Add/Modify Branch-Modify Bank', NULL,NULL, sysdate, 
    '/HTML/BankEnquiry.htm', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 3, 
    'Add/Modify Branch-Modify Bank', 1, 'HelpAssistance/ADD/Modify BRANCH AND Modify BANK.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Detailed Code-Create/Modify/View', NULL,NULL, sysdate, 
    '/HTML/DetailCodeEnquiry.htm', '', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 4, 
    'Create/Modify/View Detailed Code', 1, 'HelpAssistance/Create/Modify/View Detailed Code.htm');
    

----Accounting Records--------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Trial Balance', NULL,NULL, sysdate, 
    '/Reports/TrialBalance.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 1, 
    'Trial Balance', 1, 'HelpAssistance/Trial Balance.htm');


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Book', NULL,NULL, sysdate, 
    '/Reports/CashBookRcptPmt.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 2, 
    'Cash Book', 1, 'HelpAssistance/Cash Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Book', NULL,NULL, sysdate, 
    '/Reports/BankBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 3, 
    'Bank Book', 1, 'HelpAssistance/Bank Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Book', NULL,NULL, sysdate, 
    '/Reports/JournalBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 4, 
    'Journal Book', 1, 'HelpAssistance/Journal Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'General Ledger', NULL,NULL, sysdate, 
    '/Reports/GeneralLedger.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 5, 
    'General Ledger', 1, 'HelpAssistance/General Ledger.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Sub-Ledger', NULL,NULL, sysdate, 
    '/Reports/SubLedgerReport.jsp', 'reportType=sl', NULL, (select id_module from eg_module where module_name='Accounting Records'), 7, 
    'Sub-Ledger', 1, 'HelpAssistance/Sub-Ledger.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Day Book', NULL,NULL, sysdate, 
    '/Reports/DayBook.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 6, 
    'Day Book', 1, 'HelpAssistance/Day Book.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'SubLedger Schedule', NULL,NULL, sysdate, 
    '/Reports/SubLedgerSchedule.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 8, 
    'SubLedger Schedule', 1, 'HelpAssistance/SubLedger Schedule.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Opening Balance Report', NULL,NULL, sysdate, 
    '/Reports/OpeningBalance.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 9, 
    'Opening Balance Report', 1, 'HelpAssistance/Opening Balance Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque-in-hand Report', NULL,NULL, sysdate, 
    '/Reports/chequeInHand.jsp', '', NULL, (select id_module from eg_module where module_name='Accounting Records'), 11, 
    'Cheque-in-hand Report', 1, 'HelpAssistance/Cheque-in-hand Report.htm');
/*
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheques Received', NULL,NULL, sysdate, 
    '/Reports/ChequesReceived.jsp', 'showMode=view', NULL, (select id_module from eg_module where module_name='Accounting Records'), 10, 
    'Cheques Received', 1, 'HelpAssistance/Cheques Received.htm');
*/    
-----MIS Reports---------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contractor Supplier Report', NULL,NULL, sysdate, 
    '/Reports/rptContractorSupplier.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 1, 
    'Contractor Supplier Report', 1, 'HelpAssistance/Contractor Supplier Report.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Issue Register', NULL,NULL, sysdate, 
    '/Reports/rptChequeRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 2, 
    'Cheque Issue Register', 1, 'HelpAssistance/Cheque Issue Register.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Register of Bills', NULL,NULL, sysdate, 
    '/Reports/RptBillRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 3, 
    'Register of Bills', 1, 'HelpAssistance/Register of Bills.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Receipt Register', NULL,NULL, sysdate, 
    '/Reports/receiptRegister.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 4, 
    'Receipt Register', 1, 'HelpAssistance/Receipt Register.htm');

/*
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Transaction', NULL,NULL, sysdate, 
    '/Reports/BankTransaction.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='MIS Reports'), 5, 
    'Bank Transaction', 1, 'HelpAssistance/Bank Transaction.htm'); 
 */
 
/*Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Advance Register', NULL,NULL, sysdate, 
    '/Reports/AdvanceRegister.jsp', 'showMode=new', NULL, (select id_module from eg_module where module_name='MIS Reports'), 6, 
    'Advance Register', 1, 'HelpAssistance/Advance Register.htm');      
*/  

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Outstanding Liability of Expense for Contractors/Suppliers', NULL,NULL, sysdate, 
    '/Reports/OsStmtForLiabilityExpenses.jsp', '', NULL, (select id_module from eg_module where module_name='MIS Reports'), 7, 
    'Outstanding Liability of Expense for Contractors/Suppliers', 1, 'HelpAssistance/NoHelp.htm');
    
---Procurement Orders-------------------------------------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-Create', NULL,NULL, sysdate, 
    '/HTML/WorksDetailAdd.htm', NULL, NULL, (select id_module from eg_module where module_name='Procurement Orders'), 1, 
    'Create Procurement Order', 1, 'HelpAssistance/Create Procurement Order.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Procurement Orders-Search', NULL,NULL, sysdate, 
    '/HTML/POSearch.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Procurement Orders'), 2, 
    'View Procurement Orders', 1, 'HelpAssistance/View Procurement Orders.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Procurement Orders-Search', NULL,NULL, sysdate, 
    '/HTML/POSearch.htm', 'showMode=modifyWork', NULL, (select id_module from eg_module where module_name='Procurement Orders'), 3, 
    'Modify Procurement Orders', 1, 'HelpAssistance/Modify Procurement Orders.htm');



----Supplier/Contractor-----------------------------------------------------------------
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Supplier/Contractor-Create', NULL,NULL, sysdate, 
    '/HTML/RelationMod.htm', 'showMode=new', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 1, 
    'Create Supplier/Contractor', 1, 'HelpAssistance/Create Supplier/Contractor.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Supplier/Contractor-Search', NULL,NULL, sysdate, 
    '/HTML/Relation.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 2, 
    'View Supplier/Contractor', 1, 'HelpAssistance/View Supplier/Contractor-Search.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Supplier/Contractor-Search', NULL,NULL, sysdate, 
    '/HTML/Relation.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Supplier/Contractors'), 3, 
    'Modify Supplier/Contractor', 1, 'HelpAssistance/Modify Supplier/Contractor-Search.htm');  

   
-----Administration---------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Boundary Settings', NULL,NULL, sysdate, 
    '/administration/adminFrames.jsp', '', NULL, (select id_module from eg_module where module_name='Administration'), 1, 
    'Boundary Settings', 1, 'HelpAssistance/Boundary Settings.htm');



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Role-Based Access Control', NULL,NULL, sysdate, 
    '/rbac/index.jsp', '', NULL, (select id_module from eg_module where module_name='Administration'), 2, 
    'Role-Based Access Control', 1, 'HelpAssistance/Role-Based Access Control.htm');


-- Remittance Recovery ------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Remittance Recovery-Create', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeCreateRemitRecovery', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 1, 
    'Create Remittance Recovery', 1, 'HelpAssistance/Create Remittance Recovery.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Remittance Recovery-Search', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeRemittanceList&mode=view', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 2, 
    'View Remittance Recovery', 1, 'HelpAssistance/View Remittance Recovery-Search.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Remittance Recovery-Search', NULL,NULL, sysdate, 
    '/deduction/remitRecovery.do', 'submitType=beforeRemittanceList&mode=modify', NULL, (select id_module from eg_module where module_name='Remittance Recovery'), 3, 
    'Modify Remittance Recovery', 1, 'HelpAssistance/Modify Remittance Recovery-Search.htm');




----Party Types--------------------------------------------------------------------------------------------------------------------------------------

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Party Type-Create', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforeCreate', NULL, (select id_module from eg_module where module_name='Party Types'), 1, 
    'Create Party Types', 1, 'HelpAssistance/Create Party Type.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Party Types-Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforePartyTypeSearch&mode=view', NULL, (select id_module from eg_module where module_name='Party Types'), 2, 
    'View Party Types', 1, 'HelpAssistance/View Party Types-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Party Type-Search', NULL,NULL, sysdate, 
    '/deduction/PartyTypeMaster.do', 'submitType=beforePartyTypeSearch&mode=modify', NULL, (select id_module from eg_module where module_name='Party Types'), 3, 
    'Modify Party Types', 1, 'HelpAssistance/Modify Party Type-Search.htm');
    
    
    
-------Contract Types---------------------------------------------------------------------------------------------------------------------



Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Contract Type-Create', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeCreate', NULL, (select id_module from eg_module where module_name='Contract Types'), 1, 
    'Create Contract Type', 1, 'HelpAssistance/Create Contract Type.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Contract Types-Search', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeDocumentTypeSearch&mode=view', NULL, (select id_module from eg_module where module_name='Contract Types'), 2, 
    'View Contract Types', 1, 'HelpAssistance/View Contract Types-Search.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Contract Types-Search', NULL,NULL, sysdate, 
    '/deduction/DocumentTypeMaster.do', 'submitType=beforeDocumentTypeSearch&mode=modify', NULL, (select id_module from eg_module where module_name='Contract Types'), 3, 
    'Modify Contract Types', 1, 'HelpAssistance/Modify Contract Types-Search.htm');
    
    -----Recovery Masters-------------------------------------------------------------------------------------


Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Recovery-Create', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toLoad', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 1, 
    'Create Recovery', 1, 'HelpAssistance/Create Recovery.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'View Recoveries-Search', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toView', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 2, 
    'View Recoveries', 1, 'HelpAssistance/View Recovery.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Modify Recoveries-Search', NULL,NULL, sysdate, 
    '/deduction/recoverySetupMaster.do', 'submitType=toModify', NULL, (select id_module from eg_module where module_name='Recovery Masters'), 3, 
    'Modify Recoveries', 1, 'HelpAssistance/Modify Recovery.htm');

-- ======================================
- window.open windows


-- Receipts  

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Property Tax Collection-View', NULL,NULL, sysdate, 
    '/HTML/PT_Field.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Property Tax Collection-View', 0, 'HelpAssistance/Property Tax Collection-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Property Tax Collection-Modify', NULL,NULL, sysdate, 
    '/HTML/PT_Field.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Property Tax Collection-Modify', 0, 'HelpAssistance/Property Tax Collection-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Property Tax Collection-Reverse', NULL,NULL, sysdate, 
    '/HTML/PT_Field.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Property Tax Collection-Reverse', 0, 'HelpAssistance/Property Tax Collection-Reverse.htm');    

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Miscellaneous Receipt-View', NULL,NULL, sysdate, 
    '/HTML/MiscReceipt.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Miscellaneous Receipt-View', 0, 'HelpAssistance/Miscellaneous Receipt-View');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Miscellaneous Receipt-Modify', NULL,NULL, sysdate, 
    '/HTML/MiscReceipt.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Miscellaneous Receipt-Modify', 0, 'HelpAssistance/Miscellaneous Receipt-Modify');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Miscellaneous Receipt-Reverse', NULL,NULL, sysdate, 
    '/HTML/MiscReceipt.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Receipts'), 0, 
    'Miscellaneous Receipt-Reverse', 0, 'HelpAssistance/Miscellaneous Receipt-Reverse');    

 -- bills accounting
 		-- contractor bill
 Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
    (seq_eg_action.nextVal, 'Contractor Bill-View', NULL,NULL, sysdate, 
     '/HTML/ContractorJournal.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
    'Contractor Bill-View', 0, 'HelpAssistance/Contractor Bill-View');
 Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
    (seq_eg_action.nextVal, 'Contractor Bill-Modify', NULL,NULL, sysdate, 
     '/HTML/ContractorJournal.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
    'Contractor Bill-Modify', 0, 'HelpAssistance/Contractor Bill-Modify');
 Insert into eg_action
        (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
      Values
        (seq_eg_action.nextVal, 'Contractor Bill-Reverse', NULL,NULL, sysdate, 
         '/HTML/ContractorJournal.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
    'Contractor Bill-Reverse', 0, 'HelpAssistance/Contractor Bill-Reverse');
    
 		-- supplier bill
 
 Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Supplier Bill-View', NULL,NULL, sysdate, 
      '/HTML/SupplierJournal.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
     'Supplier Bill-View', 0, 'HelpAssistance/Supplier Bill-View');
  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Supplier Bill-Modify', NULL,NULL, sysdate, 
      '/HTML/SupplierJournal.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
     'Supplier Bill-Modify', 0, 'HelpAssistance/Supplier Bill-Modify');
  Insert into eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       Values
         (seq_eg_action.nextVal, 'Supplier Bill-Reverse', NULL,NULL, sysdate, 
          '/HTML/SupplierJournal.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
    'Supplier Bill-Reverse', 0, 'HelpAssistance/Supplier Bill-Reverse');

	-- salary bill
 Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Salary Bill-View', NULL,NULL, sysdate, 
      '/HTML/JV_Salary.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
     'Salary Bill-View', 0, 'HelpAssistance/Salary Bill-View');
  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Salary Bill-Modify', NULL,NULL, sysdate, 
      '/HTML/JV_Salary.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
     'Salary Bill-Modify', 0, 'HelpAssistance/Salary Bill-Modify');
  Insert into eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       Values
    (seq_eg_action.nextVal, 'Salary Bill-Reverse', NULL,NULL, sysdate, 
	'/HTML/JV_Salary.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Bills Accounting'), 0, 
	'Salary Bill-Reverse', 0, 'HelpAssistance/Salary Bill-Reverse');  


	
-- PAYMENTS 
	-- Bank Payments
 Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Bank Payment-View', NULL,NULL, sysdate, 
      '/HTML/DirectBankPayment.htm', 'showMode=viewBank', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
     'Bank Payment-View', 0, 'HelpAssistance/Bank Payment-View');
  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Bank Payment-Modify', NULL,NULL, sysdate, 
      '/HTML/DirectBankPayment.htm', 'showMode=editBank', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
     'Bank Payment-Modify', 0, 'HelpAssistance/Bank Payment-Modify');
  Insert into eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       Values
    (seq_eg_action.nextVal, 'Bank Payment-Reverse', NULL,NULL, sysdate, 
	'/HTML/DirectBankPayment.htm', 'showMode=modifyBank', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
	'Bank Payment-Reverse', 0, 'HelpAssistance/Bank Payment-Reverse');  


	-- Cash Payments
 Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Cash Payment-View', NULL,NULL, sysdate, 
      '/HTML/DirectCashPayment.htm', 'showMode=viewCash', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
     'Cash Payment-View', 0, 'HelpAssistance/Cash Payment-View');
  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Cash Payment-Modify', NULL,NULL, sysdate, 
      '/HTML/DirectCashPayment.htm', 'showMode=editCash', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
     'Cash Payment-Modify', 0, 'HelpAssistance/Cash Payment-Modify');
  Insert into eg_action
         (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
       Values
    (seq_eg_action.nextVal, 'Cash Payment-Reverse', NULL,NULL, sysdate, 
	'/HTML/DirectCashPayment.htm', 'showMode=modifyCash', NULL, (select id_module from eg_module where module_name='Payments'), 0, 
	'Cash Payment-Reverse', 0, 'HelpAssistance/Cash Payment-Reverse');  

 
 
 	-- Advance Payments
  Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, ' Advance Payment-View', NULL,NULL, sysdate, 
       '/HTML/AdvanceJournal.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Advance Payments'), 0, 
      ' Advance Payment-View', 0, 'HelpAssistance/ Advance Payment-View');
   Insert into eg_action
      (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
    Values
      (seq_eg_action.nextVal, ' Advance Payment-Modify', NULL,NULL, sysdate, 
       '/HTML/AdvanceJournal.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Advance Payments'), 0, 
      ' Advance Payment-Modify', 0, 'HelpAssistance/ Advance Payment-Modify');
   Insert into eg_action
          (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
        Values
     (seq_eg_action.nextVal, ' Advance Payment-Reverse', NULL,NULL, sysdate, 
 	'/HTML/AdvanceJournal.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Advance Payments'), 0, 
 	' Advance Payment-Reverse', 0, 'HelpAssistance/ Advance Payment-Reverse');  


 	-- subledger payments
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay Supplier/Contractor-View', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPayment.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Pay Supplier/Contractor-View', 0, 'HelpAssistance/Pay Supplier/Contractor-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay Supplier/Contractor-Modify', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPayment.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Pay Supplier/Contractor-Modify', 0, 'HelpAssistance/Pay Supplier/Contractor-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Pay Supplier/Contractor-Reverse', NULL, NULL, sysdate, 
    '/HTML/SubLedgerPayment.jsp', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Payments') , seq_order_number.nextval, 
    'Pay Supplier/Contractor-Reverse', 0, 'HelpAssistance/Pay Supplier/Contractor-Reverse.htm');
    
-- Journal  Proper
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-View', NULL,NULL, sysdate, 
    '/HTML/JV_General.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Journal Proper'), 0, 
    'Journal Voucher-View', 0, 'HelpAssistance/Journal Voucher-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Modify', NULL,NULL, sysdate, 
    '/HTML/JV_General.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Journal Proper'), 0, 
    'Journal Voucher-Modify', 0, 'HelpAssistance/Journal Voucher-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Journal Voucher-Reverse', NULL,NULL, sysdate, 
    '/HTML/JV_General.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Journal Proper'), 0, 
    'Journal Voucher-Reverse', 0, 'HelpAssistance/Journal Voucher-Reverse.htm');

-- Contra entries
	--c2b
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-View', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_CToB.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 0, 
    'Cash Deposit-View', 0, 'HelpAssistance/Cash Deposit-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Modify', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_CToB.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 0, 
    'Cash Deposit-Modify', 0, 'HelpAssistance/Cash Deposit-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Deposit-Reverse', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_CToB.htm', 'showMode=Modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 0, 
    'Cash Deposit-Reverse', 0, 'HelpAssistance/Cash Deposit-Reverse.htm');
    
   	--b2c 
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-View', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToC.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal-View', 0, 'HelpAssistance/Cash Withdrawal-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Modify', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToC.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Modify', 0, 'HelpAssistance/Cash Withdrawal-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cash Withdrawal-Reverse', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToC.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 2, 
    'Cash Withdrawal-Reverse', 0, 'HelpAssistance/Cash Withdrawal-Reverse.htm');
	
	-- payinslip
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Deposit-View', NULL,NULL, sysdate, 
    '/HTML/PayInSlip.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Cheque Deposit-View', 0, 'HelpAssistance/Pay in-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Deposit-Modify', NULL,NULL, sysdate, 
    '/HTML/PayInSlip.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Cheque Deposit-Modify', 0, 'HelpAssistance/Pay in-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Cheque Deposit-Reverse', NULL,NULL, sysdate, 
    '/HTML/PayInSlip.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 3, 
    'Cheque Deposit-Reverse', 0, 'HelpAssistance/Pay in-Reverse.htm');
    
    --b2b
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-View', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToB.htm', 'showMode=view', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-View', 0, 'HelpAssistance/Bank to Bank Transfer-View.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Modify', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToB.htm', 'showMode=edit', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Modify', 0, 'HelpAssistance/Bank to Bank Transfer-Modify.htm');
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank to Bank Transfer-Reverse', NULL,NULL, sysdate, 
    '/HTML/JV_Contra_BToB.htm', 'showMode=modify', NULL, (select id_module from eg_module where module_name='Contra Entries'), 4, 
    'Bank to Bank Transfer-Reverse', 0, 'HelpAssistance/Bank to Bank Transfer-Reverse.htm');

-- bank branch add
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Branch-New', NULL,NULL, sysdate, 
    '/HTML/BankBranchAdd.htm', 'newBank=1', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 0, 
    'Bank Branch-New', 0, 'HelpAssistance/Bank Branch-New.htm');
	 	
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Branch-Create', NULL,NULL, sysdate, 
    '/HTML/BankBranchAdd.htm', 'newBank=0', NULL, (select id_module from eg_module where module_name='Chart of Accounts'), 0, 
    'Bank Branch-Create', 0, 'HelpAssistance/Bank Branch-Create.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Account-Create', NULL,NULL, sysdate, 
    '/HTML/BankBranchAdd.htm', 'newAccount=1', NULL, '', 0, 
    'Bank Account-Create', 0, 'HelpAssistance/Bank Account-Create.htm');
    
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Bank Account-Modify', NULL,NULL, sysdate, 
    '/HTML/BankBranchAdd.htm', 'newAccount=0', NULL, '', 0, 
    'Bank Account-Modify', 0, 'HelpAssistance/Bank Account-Modify.htm');    

-- ============================
-- Reports 	
    
-- IE Schedules
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'IE Schedules', NULL,NULL, sysdate, 
    '/Reports/IESchedules.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 0, 
    'IE Schedules', 0, 'HelpAssistance/IE Schedules.htm');
    
 -- BS schedules
 Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
    (seq_eg_action.nextVal, 'BS Schedules', NULL,NULL, sysdate, 
     '/Reports/BSSchedules.jsp', '', NULL, (select id_module from eg_module where module_name='Financial Statements'), 0, 
    'BS Schedules', 0, 'HelpAssistance/BS Schedules.htm');
    
-- works detail
Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-View', NULL,NULL, sysdate, 
    '/HTML/WorksDetailEnq.htm', 'showMode=view', '', '', 0, 
    'Procurement Order-View', 0, 'HelpAssistance/Procurement Order-View.htm');

Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
   (seq_eg_action.nextVal, 'Procurement Order-Modify', NULL,NULL, sysdate, 
    '/HTML/WorksDetailAdd.htm', 'showMode=modifyWork', '', '', 0, 
    'Procurement Order-Modify', 0, 'HelpAssistance/Procurement Order-Modify.htm');    
    
 -- suppliers/contractors
 Insert into eg_action
    (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
    (seq_eg_action.nextVal, 'Supplier/Contractor-Modify', NULL,NULL, sysdate, 
     '/HTML/RelationMod.htm', 'showMode=modify', NULL, '', 0, 
     'Supplier/Contractor-Modify', 0, 'HelpAssistance/Create Supplier/Contractor-Modify.htm');
 
 -- SUBLEDGER CODES
  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Subledger Codes-Create', NULL,NULL, sysdate, 
      '/HTML/SubCodesAdd.htm', 'showMode=new', NULL, '', 0, 
      'Subledger Codes-Create', 0, 'HelpAssistance/Subledger Codes-Create.htm');

  Insert into eg_action
     (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
   Values
     (seq_eg_action.nextVal, 'Subledger Codes-Modify', NULL,NULL, sysdate, 
      '/HTML/SubCodesAdd.htm', 'showMode=edit', NULL, '', 0, 
      'Subledger Codes-Modify', 0, 'HelpAssistance/Subledger Codes-Modify.htm');
      
-- taxsetup      
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Tax Code-Modify', NULL,NULL, sysdate, 
  '/HTML/TaxCodeMap.htm', 'tax_code=PT', NULL, '', 0, 
'Tax Code-Modify', 0, 'HelpAssistance/Tax Code-Modify.htm');

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Tax Code-Create', NULL,NULL, sysdate, 
  '/HTML/TaxCodeMap.htm', '', NULL, '', 0, 
'Tax Code-Modify', 0, 'HelpAssistance/Tax Code-Create.htm');

--fundsource
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Financing Source-Create', NULL,NULL, sysdate, 
  '/HTML/FundSourceAdd.htm', 'showMode=new', NULL, '', 0, 
'Financing Source-Create', 0, 'HelpAssistance/Financing Source-Create'); 

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Financing Source-Modify', NULL,NULL, sysdate, 
  '/HTML/FundSourceAdd.htm', 'showMode=edit', NULL, '', 0, 
'Financing Source-Modify', 0, 'HelpAssistance/Financing Source-Modify'); 

-- Financial year
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Financial Year-Create', NULL,NULL, sysdate, 
  '/HTML/FinancialYearAdd.htm', 'showMode=new', NULL, '', 0, 
'Financial Year-Create', 0, 'HelpAssistance/Financial Year-Create'); 

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Financial Year-Modify', NULL,NULL, sysdate, 
  '/HTML/FinancialYearAdd.htm', 'showMode=edit', NULL, '', 0, 
'Financial Year-Modify', 0, 'HelpAssistance/Financial Year-Modify'); 

-- Funds
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Fund-Create', NULL,NULL, sysdate, 
  '/HTML/FundAdd.htm', 'showMode=new', NULL, '', 0, 
'Fund-Create', 0, 'HelpAssistance/Fund-Create.htm'); 

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Fund-Modify', NULL,NULL, sysdate, 
  '/HTML/FundAdd.htm', 'showMode=edit', NULL, '', 0, 
'Fund-Modify', 0, 'HelpAssistance/Fund-Modify.htm'); 


-- Function
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Function-Create', NULL,NULL, sysdate, 
  '/HTML/FunctionAdd.htm', 'showMode=new', NULL, '', 0, 
'Function-Create', 0, 'HelpAssistance/Function-Create.htm'); 

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Function-Modify', NULL,NULL, sysdate, 
  '/HTML/FunctionAdd.htm', 'showMode=edit', NULL, '', 0, 
'Function-Modify', 0, 'HelpAssistance/Function-Modify.htm'); 

-- ULB details
Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'ULB Details-Modify', NULL,NULL, sysdate, 
  '/HTML/CompanyDetailAdd.htm', 'showMode=edit', NULL, '', 0, 
'ULB Details-Modify', 0, 'HelpAssistance/ULB Details-Modify.htm'); 

Insert into eg_action
 (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
Values
 (seq_eg_action.nextVal, 'Chart Of Accounts-Modify', NULL,NULL, sysdate, 
  '/HTML/ChartOfAccounts.htm', 'mode=modifyPosting&fromScreen=detailCodeEnquiry', NULL, '', 0, 
'Chart Of Accounts-Modify', 0, 'HelpAssistance/Chart Of Accounts-Modify.htm'); 


   -- COLLECTION/PAYMENT POINT
 
 Insert into eg_action
  (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
  (seq_eg_action.nextVal, 'Collection/Payment point-Create', NULL,NULL, sysdate, 
   '/HTML/BillCollectorMod.htm', 'showMode=new', NULL, '', 0, 
'Collection/Payment point-Create', 0, 'HelpAssistance/Collection/Payment point-Create.htm'); 

 Insert into eg_action
  (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
 Values
  (seq_eg_action.nextVal, 'Collection/Payment point-Modify', NULL,NULL, sysdate, 
   '/HTML/BillCollectorMod.htm', 'showMode=edit', NULL, '', 0, 
'Collection/Payment point-Modify', 0, 'HelpAssistance/Collection/Payment point-Modify.htm');    

-- billdetails from contractor/supplier report 
  Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
   (seq_eg_action.nextVal, 'Contractor/Supplier Bill Details', NULL,NULL, sysdate, 
    '/Reports/BillDetails.htm', '', NULL, '', 0, 
'Contractor/Supplier Bill Details', 0, 'HelpAssistance/Contractor/Supplier Bill Details.htm'); 

-- paymentdetails from contractor/supplier report 
  Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
   (seq_eg_action.nextVal, 'Contractor/Supplier Payment Details', NULL,NULL, sysdate, 
    '/Reports/PaymentDetails.htm', '', NULL, '', 0, 
'Contractor/Supplier Payment Details', 0, 'HelpAssistance/Contractor/Supplier Payment Details.htm');
 
 --	 BANKBRANCH ENQUIRY
  Insert into eg_action
   (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL)
  Values
   (seq_eg_action.nextVal, 'Bank Branch-View', NULL,NULL, sysdate, 
    '/HTML/BankBranchEnquiry.htm', 'newBank=0', NULL, '', 0, 
'Bank Branch-View', 0, 'HelpAssistance/Bank Branch-View.htm'); 
      
 insert into eg_roleaction_map(roleid,actionid) select 5,id from eg_action;


-- HELPFILES SETUP STARTS
update  eg_action set action_help_url='HelpAssistance/' where id=id;
CREATE TABLE HELPFILESLIST
(
  SCREEN    VARCHAR2(300 BYTE),
  HELPFILE  VARCHAR2(300 BYTE),
  TYPE      VARCHAR2(300 BYTE)
);

Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('AccountingEntity.htm', 'AccountingEntity.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('AdvanceJournal.htm', 'Advance Payment.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('BankAdd.htm', 'AddBank.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('BankBranchAdd.htm', 'BankBranchadd.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('BankEnquiry.htm', 'Add_ModifyBankBranch.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Billcollector.htm', 'Collection_PaymentEnquiry.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Billcollectormod.htm', 'Collection_Paymentpoint.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('CancelVoucher.htm', 'CancelVoucher_help.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ChartOfAccounts.htm', 'ChartofAccountshelp.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Companydetail.htm', 'ULBDetail_view.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Companydetailadd.htm', 'ULBDetail_Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Confirmvoucher.htm', 'ConfirmVoucher_help.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ContractorJournal.htm', 'Contractor Bill.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('DirectBankPayment.htm', 'Bank Payment.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('DirectCashPayment.htm', 'Cash Payment.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('FinancialyearAdd.htm', 'FinancialYear_Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('FinancialYearEnquiry.htm', 'FinancialYear_Setup.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ftServicesMap.htm', 'CodeScreenMapping.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Function.htm', 'Function_Add.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('FunctionAdd.htm', 'Function_Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('FundSource.htm', 'SourceOfFinance_Add.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('FundSource.htm', 'SourceOfFinance_Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('JV_Contra_BToB.htm', 'Bank-to-Bank Transfer.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('JV_Contra_BToC.htm', 'Cash Withdrawal.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('JV_Contra_CToB.htm', 'Cash Deposit.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('JV_General.htm', 'Create Journal Proper.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('JV_Salary.htm', 'Salary Bill.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('MiscReceipt.htm', 'Miscellaneous Receipts.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Openingbalance.htm', 'OpeningBalance_Setup.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('PayInSlip.htm', 'Pay-in.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('POSearch.htm', 'ModifyProcurementOrder-Search.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('POSearch.htm', 'ViewProcurementOrder-Search.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('PT_field.htm', 'Property Tax Collection.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ReceiptSearch.htm', 'View Receipts.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Relation.htm', 'ModifySupplierContractor_Search.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Relation.htm', 'ViewSupplierContractor_Search.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Relationmod.htm', 'Supplier-Contractor-create.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ScheduleMaster.jsp', 'ReportScheduleMapping_Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ScheduleMaster.jsp', 'ReportScheduleMapping.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('ScheduleSearch.htm', 'ReportScheduleMapping_Search.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Setup.htm', 'FiscalPeriod_Setup.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('SetupcashchequeinHand.htm', 'SetupChequeinHand_CashinHand.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('SubcodesAdd.htm', 'Userdefinedsubcodes.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('Subcodesenq.htm', 'Userdefinedsubcodesenquiry.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('SuplierJournal.htm', 'Supplier Bill.htm', 'Existing'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('TaxCodeaMap.htm', 'TaxsetUp-Modify.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('TaxCodeMap.htm', 'TaxsetUp_Add.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('TaxCodeMapEnq.htm', 'TaxsetUp_help.htm', 'New'); 
Insert into HELPFILESLIST (SCREEN, HELPFILE, TYPE) Values ('WorksdetailAdd.htm', 'ProcurementOrder_create.htm', 'New'); 
COMMIT;


update eg_action ega set ega.ACTION_HELP_URL='HelpAssistance/'||(select distinct hlp.HELPFILE from helpfileslist hlp,eg_action eg
	where upper(substr(eg.url,instr(eg.URL,'/',1,2)+1,length(eg.url)))=upper(hlp.screen) and eg.id=ega.id 
	and hlp.screen not in('FundSource.htm','POSearch.htm','Relation.htm','ScheduleMaster.jsp'));
	
update eg_action set 	ACTION_HELP_URL='HelpAssistance/NoHelp.htm' where ACTION_HELP_URL='HelpAssistance/';	


update eg_action set ACTION_HELP_URL='HelpAssistance/SourceOfFinance_Add.htm' where DISPLAY_NAME ='Financing Source-Create';
update eg_action set ACTION_HELP_URL='HelpAssistance/SourceOfFinance_Modify.htm' where DISPLAY_NAME ='Financing Source-Modify';
update eg_action set ACTION_HELP_URL='HelpAssistance/ModifyProcurementOrder-Search.htm' where DISPLAY_NAME ='Modify Procurement Orders-Search';
update eg_action set ACTION_HELP_URL='HelpAssistance/ViewProcurementOrder-Search.htm' where DISPLAY_NAME ='View Procurement Orders-Search';
update eg_action set ACTION_HELP_URL='HelpAssistance/ModifySupplierContractor_Search.htm' where DISPLAY_NAME ='Modify Supplier/Contractor-Search';
update eg_action set ACTION_HELP_URL='HelpAssistance/ViewSupplierContractor_Search.htm' where DISPLAY_NAME ='View Supplier/Contractor-Search';
update eg_action set ACTION_HELP_URL='HelpAssistance/ReportScheduleMapping_Search.htm' where display_NAME ='View/Modify Report Schedule';
update eg_action set ACTION_HELP_URL='HelpAssistance/ReportScheduleMapping.htm' where DISPLAY_NAME ='Report Schedule Mapping-Create';
update eg_action set ACTION_HELP_URL='HelpAssistance/ReportScheduleMapping_Modify.htm' where DISPLAY_NAME ='Report Schedule Mapping-Modify';
commit;
drop table HELPFILESLIST;

-- HELPFILES SETUP  ends




update eg_user set isactive=1 where isactive is null;
drop sequence seq_order_number;

commit;







/* *********************************************
 *
 *	
 *	
 *
 ********************************************* */
 
 commit;
 exit;