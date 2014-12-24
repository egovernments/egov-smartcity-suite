#UP
update eg_script set script ='from java.text import SimpleDateFormat  
from org.egov.commons.service import CommonsServiceImpl  
from java.lang import Long  
commonsManager = CommonsServiceImpl()
financialYear = commonsManager.getFinancialYearByDate(bill.getBilldate())  
year=financialYear.getFinYearRange()  
result="MN"+"/"+sequenceGenerator.getNextNumber("MN",1).getFormattedNumber().zfill(4)+"/"+year' where name='autobillnumber';

#DOWN
update eg_script set script ='from java.text import SimpleDateFormat  
from org.egov.commons.service import CommonsManager  
from org.egov.infstr.utils import ServiceLocator  
from org.egov.commons.service import CommonsManagerHome  
from java.lang import Long  
commonsManagerHome = ServiceLocator.getInstance().getLocalHome(''CommonsManagerHome'')  
commonsManager = commonsManagerHome.create()  
financialYearId = commonsManager.getFinancialYearId(SimpleDateFormat(''dd/MM/yyyy'').format(bill.getBilldate()))  
financialYear= commonsManager.getFinancialYearById(Long.valueOf(financialYearId))  
year=financialYear.getFinYearRange()  
result="MN"+"/"+sequenceGenerator.getNextNumber("MN",1).getFormattedNumber().zfill(4)+"/"+year' where name='autobillnumber';
