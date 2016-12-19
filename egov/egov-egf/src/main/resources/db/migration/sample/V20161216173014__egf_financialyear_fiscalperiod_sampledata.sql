
INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, isactiveforposting, isclosed, transferclosingbalance) VALUES (nextval('seq_financialyear'), '2017-18', '01-Apr-2017', '31-Mar-2018', true, current_date, current_date, 1,1,0, false, false, false);

INSERT INTO fiscalperiod (id,name, startingdate, endingdate,isactiveforposting, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, financialyearid)VALUES (nextval('seq_fiscalperiod'),'201718', '01-Apr-2017', '31-Mar-2018',false, true, current_date, current_date,1,1,0, (select id from financialyear where financialyear='2017-18'));

