#UP
   
 Insert into financial_institution
    (ID, NAME)
 Values
   (SEQ_financial_institution.nextval,'BALBOA FEDERAL CREDIT UNION');
   
   
 Insert into financial_institution
    (ID, NAME)
 Values
   (SEQ_financial_institution.nextval,'DBA Financial, Inc.');
   
   
 Insert into financial_institution
    (ID, NAME)
 Values
   (SEQ_financial_institution.nextval,'PAC IBM EFCU');
   
 Insert into financial_institution
    (ID, NAME)
 Values
   (SEQ_financial_institution.nextval,'Imperial Savings and Loan Association');
   
 Insert into financial_institution
    (ID, NAME)
 Values
   (SEQ_financial_institution.nextval,'Granite State Bank' );
   

update fundsource set subschemeid=(select id from sub_scheme where code='EQ175') where code='10';

update fundsource set subschemeid=(select id from sub_scheme where code='MLA990') where code='50';

update fundsource set subschemeid=(select id from sub_scheme where code='MLA991') where code='90';

update fundsource set subschemeid=(select id from sub_scheme where code='APURMSP479') where code='02';

update fundsource set subschemeid=(select id from sub_scheme where code='SWDJNNURM') where code='20';

update fundsource set subschemeid=(select id from sub_scheme where code='APURMSP479') where code='5555';

update fundsource set subschemeid=(select id from sub_scheme where code='SWDJNNURM') where code='56';

update fundsource set subschemeid=(select id from sub_scheme where code='DEP000') where code='57';

#DOWN