UPDATE EGPAY_SALARYCODES
SET glcodeid=NULL WHERE glcodeid IS NOT NULL AND tds_id IS NOT NULL;


update egpay_salarycodes s set s.PCT_BASIS=(select id from egpay_salarycodes where head like'Basic') where s.CAL_TYPE like'ComputedValue';



INSERT INTO EG_ROLEACTION_MAP
(SELECT 1,id FROM EG_ACTION);