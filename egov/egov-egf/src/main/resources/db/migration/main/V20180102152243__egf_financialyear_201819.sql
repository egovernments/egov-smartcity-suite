
delete from fiscalperiod  where name ='201819' and financialyearid=(select id from financialyear where financialyear='2018-19');
delete from financialyear  where financialyear ='2018-19';



INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, isactiveforposting, isclosed, transferclosingbalance) VALUES (nextval('seq_financialyear'), '2018-19', '01-Apr-2018', '31-Mar-2019', true, current_date, current_date, 1,1,0, false, false, false);

INSERT INTO fiscalperiod (id,name, startingdate, endingdate,isactiveforposting, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, financialyearid)VALUES (nextval('seq_fiscalperiod'),'201819', '01-Apr-2018', '31-Mar-2019',false, true, current_date, current_date,1,1,0, (select id from financialyear where financialyear='2018-19'));
