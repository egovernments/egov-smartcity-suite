update eg_script set script='from org.hibernate.exception import SQLGrammarException 
def getAssetCategoryCode():  
	sequenceName = ''ASSETCATEGORY_NUMBER'' 
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4) 
  	return runningNumber
result=getAssetCategoryCode()' where name='assets.assetcategorynumber.generator';

--rollback update eg_script set script='result=sequenceGenerator.getNextNumber("ASSETCATEGORY_NUMBER",1).getFormattedNumber().zfill(3)' where name='assets.assetcategorynumber.generator';
