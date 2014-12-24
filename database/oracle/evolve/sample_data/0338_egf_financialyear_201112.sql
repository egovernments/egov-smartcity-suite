#UP
delete from financialyear where FINANCIALYEAR='2011-12';

INSERT INTO FINANCIALYEAR (id, FINANCIALYEAR, startingdate, endingdate, isactive, CREATEd, lastmodified, MODIFIEDBY, 
isActiveForPosting, isClosed, TransferClosingBalance) 
VALUES (SEQ_FINANCIALYEAR.nextval, '2011-12', '01-Apr-2011', '31-Mar-2012', 1, '01-Apr-2011', '01-Apr-2011', 1, 1, 0, 0);

INSERT INTO FISCALPERIOD (id, TYPE, name, startingdate, endingdate, parentid, ISactiveforposting, isactive, modifiedby, lastmodified, created, FINANCIALYEARID) 
VALUES (seq_fiscalperiod.NEXTVAL, NULL, 'FP2011-12', '01-Apr-2011', '31-Mar-2012', NULL, 1, 1, 1,'01-Apr-2011','01-Apr-2011', (select id from financialyear where FINANCIALYEAR='2011-12'));

#DOWN

delete from fiscalperiod where name='FP2011-12';
delete from financialyear where FINANCIALYEAR='2011-12';
