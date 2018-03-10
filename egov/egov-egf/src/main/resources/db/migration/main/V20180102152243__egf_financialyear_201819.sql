INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, isactiveforposting, isclosed, transferclosingbalance) 
SELECT nextval('seq_financialyear'), '2018-19', '01-Apr-2018', '31-Mar-2019', true, current_date, current_date, 1,1,0, false, false, false 
WHERE NOT EXISTS (SELECT 1 FROM financialyear WHERE financialyear='2018-19');

INSERT INTO fiscalperiod (id,name, startingdate, endingdate,isactiveforposting, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, financialyearid) 
SELECT nextval('seq_fiscalperiod'),'201819', '01-Apr-2018', '31-Mar-2019',false, true, current_date, current_date,1,1,0, (select id from financialyear where financialyear='2018-19') 
WHERE NOT EXISTS (SELECT 1 FROM fiscalperiod WHERE name='201819');
