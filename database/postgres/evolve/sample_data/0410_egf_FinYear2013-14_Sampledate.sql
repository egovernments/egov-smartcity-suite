#UP
INSERT INTO FINANCIALYEAR (id, FINANCIALYEAR, startingdate, endingdate, isactive, CREATEd, lastmodified, MODIFIEDBY, 
isActiveForPosting, isClosed, TransferClosingBalance) 
VALUES (SEQ_FINANCIALYEAR.nextval, '2013-14', '01-Apr-2013', '31-Mar-2014', 1, '01-Apr-2013', '01-Apr-2013', 1, 1, 0, 0);

INSERT INTO FISCALPERIOD (id, TYPE, name, startingdate, endingdate, parentid, ISactiveforposting, isactive, modifiedby, lastmodified, created, FINANCIALYEARID) 
VALUES (seq_fiscalperiod.NEXTVAL, NULL, 'FP201314', '01-Apr-2013', '31-Mar-2014', NULL, 1, 1, 1,'01-Apr-2013','01-Apr-2013', (select id from financialyear where FINANCIALYEAR='2013-14'));

#DOWN
delete from fiscalperiod where name='FP201314';
delete from financialyear where FINANCIALYEAR='2013-14';
