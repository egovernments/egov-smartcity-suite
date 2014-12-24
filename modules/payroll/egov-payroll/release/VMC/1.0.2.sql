/************ Leave Cancel ********/

Insert into egeis_leave_status
   (STATUS, ID)
 Values
   ('Cancelled', 5);


********* set cancel instead of modify in UI *********
UPDATE  EG_ACTION SET display_name ='Cancel' WHERE name LIKE '%Leave_Modify%';


**************UI changes ****************************
UPDATE  EG_ACTION SET display_name ='Approve CompensatoryOff' WHERE display_name LIKE '%Approve Compoff%';

UPDATE  EG_ACTION SET display_name ='Designation-Leave Mapping' WHERE display_name LIKE '%Leave Masters%';

UPDATE EG_MODULE SET module_name='Attendance' where module_name LIKE '%Attendence%';


********************** Leave Opening Balance *******************************





INSERT INTO EG_ACTION
   (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 VALUES
   (SEQ_EG_ACTION.NEXTVAL, 'LeaveOpenbalance', TO_DATE('09/10/2008 15:18:22', 'MM/DD/YYYY HH24:MI:SS'), '/leave/BeforeOpeningBalanceAction.do', 'submitType=view\&mode=save', (SELECT id_module FROM EG_MODULE WHERE module_name = 'Leave Masters'), 5, 'Create/Update Leave Opening Balance', 1);

//RoleAction mapping

    INSERT INTO EG_ROLEACTION_MAP
   (ROLEID, ACTIONID)
 VALUES
   (5, (SELECT id FROM EG_ACTION WHERE name='LeaveOpenbalance'));

/**** Pay Head Changes *****/

alter table EGPAY_SALARYCODES add isAttendanceBased varchar(1) ;
alter table EGPAY_SALARYCODES add isRecomputed varchar(1) ;
alter table EGPAY_SALARYCODES add isRecurring varchar(1) ;
update EGPAY_SALARYCODES  SET isAttendanceBased ='Y',isRecomputed ='Y',isRecurring ='Y';
alter table EGPAY_SALARYCODES modify isAttendanceBased not null;
alter table EGPAY_SALARYCODES modify isRecurring not null;
alter table EGPAY_SALARYCODES modify isRecomputed not null;


/************ Pay Scale Increments slabs changes **********************/
CREATE TABLE EGPAY_PAYSCALE_INCRDETAILS
(
  id NUMBER NOT NULL,
  incSlabAmt NUMBER(12,5) NOT NULL,
  incSlabToAmt NUMBER(12,5) NOT NULL,
  incSlabFrmAmt NUMBER(12,5) NOT NULL,
  ID_PAYHEADER NUMBER NOT NULL,  
  CONSTRAINT PK_EGPAY_PAYSCALE_INCRDETAILS PRIMARY KEY (id )
);

CREATE SEQUENCE SEQ_EGPAY_PAYSCALE_INCRDETAILS
START WITH 0
INCREMENT BY 1
MINVALUE 0
MAXVALUE 999999999999999999999999999
NOCACHE 
NOCYCLE 
NOORDER ;


ALTER TABLE EGPAY_PAYSCALE_INCRDETAILS ADD (
   CONSTRAINT FK_ID_PAYHEADER FOREIGN KEY (ID_PAYHEADER) 
    REFERENCES EGPAY_PAYSCALE_HEADER (ID));

insert into egpay_payscale_incrdetails select SEQ_EGPAY_PAYSCALE_INCRDETAILS.nextVal,incrementamount,amountfrom,amountto,id from egpay_payscale_header;

alter table EGPAY_PAYSCALE_HEADER drop column INCREMENTAMOUNT;
