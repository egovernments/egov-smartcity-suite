#UP
UPDATE EG_APPCONFIG_VALUES SET value='Tender Deposit-Revenue:2205102|Tender Deposit-Capital:46040|Security Deposit-Revenue:1801001|Security Deposit-Capital:3402002|Forms-Publications-Tenders:1501101'
where key_id = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='STANDARD_DEDUCTION');


#DOWN

UPDATE EG_APPCONFIG_VALUES SET value='Tender Deposit-Revenue:2205102|Tender Deposit-Capital:46040|Security Deposit-Revenue:1801001|Security Deposit-Capital:3402002|Retention Money-Revenue:3401002|Retention Money-Capital:3401002|Forms-Publications-Tenders:1501101'
where key_id = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='STANDARD_DEDUCTION');

