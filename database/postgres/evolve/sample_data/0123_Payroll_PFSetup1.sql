#UP

/** Sample data for PFSetup  master and PF Interest calculation service **/

INSERT INTO EG_NUMBERS ( ID, VOUCHERTYPE, VOUCHERNUMBER, FISCIALPERIODID,
MONTH ) VALUES ( 
SEQ_EG_NUMBERS.NEXTVAL, 'MJ',  (select id from voucherheader where cgn='SAL1' and vouchernumber='MJSAL16/12/0910' and cgvn='MJ00001' ), 6, NULL); 


COMMIT;

#DOWN





