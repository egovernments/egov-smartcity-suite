INSERT INTO eg_script(id, name, script_type, created_by, created_date, modified_by, modified_date, script, start_date, end_date) VALUES (nextval('eg_script_seq'), 'assets.assetnumber.generator', 'python', null, null, null, null, 'from org.hibernate.exception import SQLGrammarException 
def getAssetCode():  
	sequenceName = ''ASSET_''+asset.getAssetCategory().getCode()  
	try:  
		runningNumber=str(sequenceGenerator.getNextSequence(sequenceName)).zfill(4) 
	except SQLGrammarException, e:  
		runningNumber=str(dbSequenceGenerator.createAndGetNextSequence(sequenceName)).zfill(4)  
	if(asset.getAssetCategory().getAssetType().toString().lower()=="movableasset".lower()):  
  		result=asset.getAssetCategory().getCode()+"/"+runningNumber+"/"+year  
	else:  
  		result=asset.getAssetCategory().getCode()+"/"+asset.getStreet().getBoundaryNum().toString()+"/"+runningNumber+"/"+year  
	return result  
result=getAssetCode()', '1900-01-01 00:00:00', '2100-01-01 00:00:00');

--rollback delete from eg_script where name='assets.assetnumber.generator';


