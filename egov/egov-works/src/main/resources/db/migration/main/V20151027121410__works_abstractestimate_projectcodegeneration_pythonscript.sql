
------------------START------------------
INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (nextval('seq_eg_script'), 'works.projectcode.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getProjectCode():  
	finYearRange = finYear.getFinYearRange().split(''-'')
	sequenceName = ''PROJECTCODE_''+finYearRange[0]+''_''+finYearRange[1]  
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4)  
	return	estimate.getFundSource().getCode()+"/"+estimate.getExecutingDepartment().getCode()+"/"+runningNumber+"/"+finYear.getFinYearRange()
result=getProjectCode()', '1900-01-01 00:00:00', '2100-01-01 00:00:00',0);
-------------------END-------------------

--rollback delete from eg_script where name='works.projectcode.generator';