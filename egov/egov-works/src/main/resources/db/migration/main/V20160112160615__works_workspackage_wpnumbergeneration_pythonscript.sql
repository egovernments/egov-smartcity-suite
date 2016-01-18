
------------------START------------------
INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (nextval('seq_eg_script'), 'works.wpNumber.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getWPNumber():  
	finYearRange = finYear.getFinYearRange().split(''-'')
	sequenceName = ''WORKSPACKAGE_NUMBER_''+finYearRange[0]+''_''+finYearRange[1]  
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4)  
	return	worksPackage.getDepartment().getCode()+"/WP/"+runningNumber+"/"+finYear.getFinYearRange()
result=getWPNumber()', '1900-01-01 00:00:00', '2100-01-01 00:00:00',0);
-------------------END-------------------

--rollback delete from eg_script where name='works.wpNumber.generator';