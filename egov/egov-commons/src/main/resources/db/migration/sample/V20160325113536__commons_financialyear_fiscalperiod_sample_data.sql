update egf_budget set financialyearid=(select id from financialyear where financialyear = '2002-03') where financialyearid=(select id from financialyear where financialyear = '2016-17');
delete from fiscalperiod where financialyearid in (select id from financialyear where financialyear = '2016-17') ;

delete from financialyear where financialyear = '2016-17';

INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, isactiveforposting, isclosed, transferclosingbalance) VALUES (nextval('seq_financialyear'), '2016-17', '2016-04-01 00:00:00', '2017-03-31 00:00:00', true, '2012-03-30 00:00:00', '2012-03-30 00:00:00', 1,1,0, true, false, true);

INSERT INTO fiscalperiod (id,name, startingdate, endingdate,isactiveforposting, isactive, createddate, lastmodifieddate,lastmodifiedby,createdby,version, financialyearid)VALUES (nextval('seq_fiscalperiod'),'201617', '2016-04-01 00:00:00', '2017-03-31 00:00:00',true, true, current_date, current_date,1,1,0, (select id from financialyear where financialyear='2016-17'));

update egf_budget set financialyearid=(select id from financialyear where financialyear = '2016-17') where financialyearid=(select id from financialyear where financialyear = '2002-03');