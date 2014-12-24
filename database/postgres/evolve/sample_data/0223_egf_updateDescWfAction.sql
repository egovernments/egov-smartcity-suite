#UP

update EG_WF_ACTIONS set description='Forward' where  type='EgBillregister' and name='am_approve';

update EG_WF_ACTIONS set description='Forward' where  type='CVoucherHeader' and name='am_approve';

#DOWN