delete from chartofaccounts where glcode=4106006;

update chartofaccounts set name='Network Equipment' where glcode=4106007;

delete from chartofaccountdetail where glcodeid=858;
delete from chartofaccounts where glcode =3502009;
delete from chartofaccounts where glcode =3502052;
--Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
-- Values
--   (seq_chartofaccounts.nextval, '4106007', 'Network Equipment', 1, 243, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4106008', 'Other office equipment', 1, 243, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4107006', 'Others furniture,fixtures and fittings', 1, 244, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4115007', 'Others vehicles', 1, 253, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4116007', 'Network equipment', 1, 254, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4116008', 'Other office equipment', 1, 254, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4117006', 'Other furniture,fixtures and fittings', 1, 255, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

 delete from chartofaccounts where id in(1082,1083);
 
Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311902', 'Sewerage Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311903', 'Conservancy Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311904', 'Lighting Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);



Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311905', 'Education Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311906', 'Vehicle Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311907', 'Tax on Animals', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311908', 'Trade and Professional Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);
Advertisement Tax

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311909', 'Advertisement Tax', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4311910', 'Other Taxes', 1, 281, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, TYPE, CLASSIFICATION)
 Values
   (seq_chartofaccounts.nextval, '4608002', 'Interest Receivable on Loans and Advances', 1, 321, 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('08/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 4);


update chartofaccounts set name='License Fees' where glcode=4313001;

update chartofaccounts set name='Advertisement Fees' where glcode=4313002;

Insert into chartofaccounts
   (ID, GLCODE, NAME, ISACTIVEFORPOSTING, PARENTID, ISACTIVE, LASTMODIFIED, MODIFIEDBY, CREATED, GROUPID, OPERATION, TYPE, CLASSIFICATION, SCHEDULEID, PAYMENTSCHEDULEID, PAYMENTOPERATION)
 Values
   (seq_chartofaccounts.nextval, '43040', 'Other Receivables', 0, 40, 1, TO_DATE('02/13/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('02/13/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1, 'A', 'A', 2, 17, 41, 'A');


COMMIT;


