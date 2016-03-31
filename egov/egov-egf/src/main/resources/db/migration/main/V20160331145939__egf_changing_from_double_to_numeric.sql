ALTER TABLE eg_billregister
    ALTER COLUMN billamount TYPE numeric(13,2),
    ALTER COLUMN passedamount TYPE numeric(13,2),
    ALTER COLUMN advanceadjusted TYPE numeric(13,2);
ALTER TABLE eg_billdetails
    ALTER COLUMN debitamount TYPE numeric(13,2),
    ALTER COLUMN creditamount TYPE numeric(13,2);
ALTER TABLE tds
    ALTER COLUMN rate TYPE numeric(13,2),
    ALTER COLUMN caplimit TYPE numeric(13,2);
ALTER TABLE eg_billpayeedetails
    ALTER COLUMN debitamount TYPE numeric(13,2),
    ALTER COLUMN creditamount TYPE numeric(13,2);
ALTER TABLE paymentheader
    ALTER COLUMN paymentamount TYPE numeric(13,2);
ALTER TABLE miscbilldetail
    ALTER COLUMN amount TYPE numeric(13,2),
    ALTER COLUMN passedamount TYPE numeric(13,2),
    ALTER COLUMN paidamount TYPE numeric(13,2);  
ALTER TABLE transactionsummary
    ALTER COLUMN openingdebitbalance TYPE numeric(13,2),
    ALTER COLUMN openingcreditbalance TYPE numeric(13,2);
ALTER TABLE eg_deduction_details
    ALTER COLUMN lowlimit TYPE numeric(13,2),
    ALTER COLUMN highlimit TYPE numeric(13,2),
    ALTER COLUMN amount TYPE numeric(13,2),
    ALTER COLUMN cumulativelowlimit TYPE numeric(13,2),
    ALTER COLUMN cumulativehighlimit TYPE numeric(13,2);  
ALTER TABLE eg_remittance_detail
    ALTER COLUMN remittedamt TYPE numeric(13,2);
ALTER TABLE eg_remittance_gldtl
    ALTER COLUMN gldtlamt TYPE numeric(13,2),
    ALTER COLUMN remittedamt TYPE numeric(13,2);
ALTER TABLE egf_instrumentheader
    ALTER COLUMN instrumentamount TYPE numeric(13,2);    
ALTER TABLE egf_instrumentotherdetails
    ALTER COLUMN reconciledamount TYPE numeric(13,2);    
ALTER TABLE eg_advancerequisition
    ALTER COLUMN advancerequisitionamount TYPE numeric(13,2);    
ALTER TABLE eg_advancerequisitiondetails
    ALTER COLUMN creditamount TYPE numeric(13,2),
    ALTER COLUMN debitamount TYPE numeric(13,2);    
ALTER TABLE eg_advancereqpayeedetails
    ALTER COLUMN creditamount TYPE numeric(13,2),
    ALTER COLUMN debitamount TYPE numeric(13,2);    
ALTER TABLE egf_loangrantheader
    ALTER COLUMN projectcost TYPE numeric(13,2),
    ALTER COLUMN revisedcost TYPE numeric(13,2),
    ALTER COLUMN sanctionedcost TYPE numeric(13,2);    
ALTER TABLE egf_loangrantdetail
    ALTER COLUMN loanamount TYPE numeric(13,2),
    ALTER COLUMN grantamount TYPE numeric(13,2);   
ALTER TABLE egf_loangrantreceiptdetail
    ALTER COLUMN amount TYPE numeric(13,2);   
ALTER TABLE egf_fixeddeposit
    ALTER COLUMN amount TYPE numeric(13,2),
    ALTER COLUMN interestrate TYPE numeric(13,2),
    ALTER COLUMN gjvamount TYPE numeric(13,2),
    ALTER COLUMN maturityamount TYPE numeric(13,2),
    ALTER COLUMN receiptamount TYPE numeric(13,2);  
ALTER TABLE egf_grant
    ALTER COLUMN accrualamount TYPE numeric(13,2),   
    ALTER COLUMN grantamount TYPE numeric(13,2);
ALTER TABLE egf_budgetdetail
    ALTER COLUMN originalamount TYPE numeric(13,2),   
    ALTER COLUMN anticipatory_amount TYPE numeric(13,2),   
    ALTER COLUMN budgetavailable TYPE numeric(13,2),   
    ALTER COLUMN approvedamount TYPE numeric(13,2);    
ALTER TABLE egf_budget_usage
    ALTER COLUMN consumedamt TYPE numeric(13,2),   
    ALTER COLUMN releasedamt TYPE numeric(13,2);    
ALTER TABLE bankreconciliation
    ALTER COLUMN amount TYPE numeric(13,2);
ALTER TABLE generalledger
    ALTER COLUMN debitamount TYPE numeric(13,2),   
    ALTER COLUMN creditamount TYPE numeric(13,2);         
ALTER TABLE generalledgerdetail
    ALTER COLUMN amount TYPE numeric(13,2);    
