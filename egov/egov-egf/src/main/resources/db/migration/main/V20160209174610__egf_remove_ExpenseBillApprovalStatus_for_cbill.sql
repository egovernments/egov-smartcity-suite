delete  from EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME like '%ExpenseBillApprovalStatus') and value = 'CBILL|APPROVED';

