
delete from EGADTAX_BATCHDEMANDGENERATE;

alter table EGADTAX_BATCHDEMANDGENERATE drop column financialyear;

alter table EGADTAX_BATCHDEMANDGENERATE add column installment  bigint NOT NULL;

ALTER TABLE ONLY EGADTAX_BATCHDEMANDGENERATE ADD CONSTRAINT fk_adtax_batchDmdGen_installment FOREIGN KEY (installment) REFERENCES EG_INSTALLMENT_MASTER(id);

update eg_installment_master set financial_year='2015-16'  where ID_MODULE=(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null) and description='ADTAX/15-16';

update eg_installment_master set financial_year='2016-17'  where ID_MODULE=(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null) and description='ADTAX/16-17';