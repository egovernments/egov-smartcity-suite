#UP

UPDATE EGW_ABSTRACTESTIMATE SET CREATEDDATE=SYSDATE, MODIFIEDDATE=SYSDATE WHERE NAME IN('AbstractEstimate1', 'AbstractEstimate2','AbstractEstimate3','AbstractEstimate4');
UPDATE EG_WF_STATES SET CREATED_BY=(select id_user from eg_employee WHERE NAME='VIMAL KISHORE'),MODIFIED_BY=(select id_user from eg_employee WHERE NAME='VIMAL KISHORE') WHERE TYPE='AbstractEstimate';

#DOWN
