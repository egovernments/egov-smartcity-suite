INSERT
INTO eg_script
  (
    id,
    name,
    type,
    createdby,
    createddate,
    lastmodifiedby,
    lastmodifieddate,
    script,
    startdate,
    enddate,
    version
  )
  VALUES
  (
    nextval('seq_eg_script'),
    'autobillnumber',
    'python',
    NULL,
    NULL,
    NULL,
    NULL,
    'financialYear = commonsService.getFinancialYearByDate(bill.getBilldate())
year=financialYear.getFinYearRange()
result=bill.getEgBillregistermis().getEgDepartment().getCode()+"/"+"MN"+"/"+sequenceGenerator.getNextNumber("MN",1).getFormattedNumber().zfill(4)+"/"+year',
    '1900-01-01 00:00:00',
    '2100-01-01 00:00:00',0);
    

