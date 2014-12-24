#UP

update EG_SCRIPT set SCRIPT=
'seqString = ''ASSET_''+asset.getAssetCategory().getCode()
if(sequenceGenerator.checkObjectType(seqString)==0):
  print ''Generating sequence....'' + seqString
  sequenceGenerator.insertObjectType(seqString,0)
if(asset.getAssetCategory().getAssetType().getName().lower()=="movableasset".lower()):
  result=asset.getAssetCategory().getCode()+"/"+sequenceGenerator.getNextNumber(seqString,1).getFormattedNumber()+"/"+finYear.getFinYearRange()
else:
  result=asset.getAssetCategory().getCode()+"/"+sequenceGenerator.getNextNumber(seqString,1).getFormattedNumber()+"/"+finYear.getFinYearRange()
'
where name='assets.assetnumber.generator';

#DOWN

update EG_SCRIPT set SCRIPT='
from org.egov.infstr.utils.seqgen import DatabaseSequence
from org.egov.infstr.utils.seqgen import DatabaseSequenceFirstTimeException
from java.lang import Long
from java.lang import Integer
from java.lang import String
from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov.infstr.utils import ServiceLocator
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')
commonsManager = commonsManagerHome.create()
seqString = ''ASSET_''+asset.getAssetCategory().getCode()+finYear.getFinYearRange().replace(''-'','''')[2:6]
validationErrors=None
result=None
try:
	try:
		transNumber = DatabaseSequence.named(seqString).createIfNecessary().nextVal()
	except DatabaseSequenceFirstTimeException,e:
		raise ValidationException,[ValidationError(e.getMessage(),e.getMessage())]
except ValidationException,e:
	validationErrors=e.getErrors()
print ''Generating sequence....'' + seqString
if validationErrors==None:
	result=asset.getAssetCategory().getCode()+String.valueOf(transNumber).zfill(6)+"-"+finYear.getFinYearRange().replace(''-'','''')[2:6]'
where name='assets.assetnumber.generator';
