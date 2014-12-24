#UP


INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Arrears',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ARREARS','1',SYSDATE,SYSDATE);


--------UPDATED FOR SHOP-------------
UPDATE eglems_demandreasontype
SET description='Advance',reasonname='ADVANCE'
WHERE type = 'SHOP' AND description ='ADVANCE';

UPDATE eglems_demandreasontype
SET description='Rent',reasonname='RENT'
WHERE type = 'SHOP' AND description ='RENT';

UPDATE eglems_demandreasontype
SET description='Arrears on Rent',reasonname='ARREARSONRENT'
WHERE type = 'SHOP' AND description ='ARREARSONRENT';

UPDATE eglems_demandreasontype
SET description='Advance Deposit',reasonname='ADVANCEDEPOSIT'
WHERE type = 'SHOP' AND description ='ADVANCEDEPOSIT';

UPDATE eglems_demandreasontype
SET description='Ground Rent',reasonname='GROUNDRENT'
WHERE type = 'SHOP' AND description ='GROUNDRENT';

UPDATE eglems_demandreasontype
SET description='Extra Charges',reasonname='EXTRACHARGES'
WHERE type = 'SHOP' AND description ='EXTRACHARGES';

UPDATE eglems_demandreasontype
SET description='Maintenance Charges',reasonname='MCHARGE'
WHERE type = 'SHOP' AND description ='MCHARGE';

UPDATE eglems_demandreasontype
SET description='Arrears on Maintenance Charge',reasonname='ARRSMCHARGE'
WHERE type = 'SHOP' AND description ='ARRSMCHARGE';

UPDATE eglems_demandreasontype
SET description='Arrears on Ground Rent',reasonname='ARRSGROUNDRENT'
WHERE type = 'SHOP' AND description ='ARRSGROUNDRENT';

UPDATE eglems_demandreasontype
SET description='Discount',reasonname='DISCOUNT'
WHERE type = 'SHOP' AND description ='DISCOUNT';

------UPDATED FOR LAND------------------------------

UPDATE eglems_demandreasontype
SET description='Arrears',reasonname='ARREARS'
WHERE type = 'LAND' AND description ='ARREARS';

UPDATE eglems_demandreasontype
SET description='Non Agricultural Tax Per Annum',reasonname='NONAGRTAX'
WHERE type = 'LAND' AND description ='NONAGRTAX';

UPDATE eglems_demandreasontype
SET description='Incremental Tax Per Annum',reasonname='INCREMENTALTAX'
WHERE type = 'LAND' AND description ='INCREMENTALTAX';

UPDATE eglems_demandreasontype
SET description='Extra Charges',reasonname='EXTRACHARGES'
WHERE type = 'LAND' AND description ='EXTRACHARGES';

UPDATE eglems_demandreasontype
SET description='Discount',reasonname='DISCOUNT'
WHERE type = 'LAND' AND description ='DISCOUNT';

UPDATE eglems_demandreasontype
SET description='Ground Rent Per Annum',reasonname='GROUNDRENT'
WHERE type = 'LAND' AND description ='GROUNDRENT';

--------UPDATED FOR HOARDING -----------------------------------

UPDATE eglems_demandreasontype
SET reasonname = 'RENT' , description = 'Rent',glcode='4502100',purposeid='LandEstate_Rent_purpose'
WHERE type='HOARDINGS' AND description = 'RENT';

UPDATE eglems_demandreasontype
SET reasonname = 'ADVANCEDEPOSIT' , description = 'Advance Deposit',glcode='3408000',purposeid='LandEstate_Deposit_purpose'
WHERE type='HOARDINGS' AND description = 'DEPOSIT';

UPDATE eglems_demandreasontype
SET reasonname = 'EXTRACHARGES' , description = 'Extra Charges',glcode='4502100',purposeid='LandEstate_Rent_purpose'
WHERE type='HOARDINGS' AND description = 'EXTRACHARGES';

UPDATE eglems_demandreasontype
SET reasonname = 'DISCOUNT' , description = 'Discount',glcode='2408001',purposeid='LandEstate_Discount_purpose'
WHERE type='HOARDINGS' AND description = 'DISCOUNT';

UPDATE eglems_demandreasontype
SET reasonname = 'ARREARS' , description = 'Arrears',glcode='4502100',purposeid='LandEstate_Rent_purpose'
WHERE type='HOARDINGS' AND description = 'ARREARS';


#DOWN

DELETE FROM eg_demand_reason_master 
WHERE code ='ARREARS' AND module_id = (SELECT id_module FROM eg_module WHERE module_name='LandAndEstate');
