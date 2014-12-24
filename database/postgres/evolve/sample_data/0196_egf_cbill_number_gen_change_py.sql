#UP
update eg_script 
set script='result=sItem.getEgBillregistermis().getEgDepartment().getDeptCode()+"/"+"EJV"+"/"+sequenceGenerator.getNextNumber("EJV",1).getFormattedNumber().zfill(4)+"/"+year' where name='egf.bill.number.generator';
#DOWN