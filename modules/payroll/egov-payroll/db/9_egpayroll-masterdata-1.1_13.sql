#UP

/***disable advance disbursement********/
UPDATE EG_MODULE SET isenabled=0 WHERE  module_name='Adv Disbursement';

/**added grade and type in paysclae ******/

 
 
  delete from eg_appconfig_values e where e.key_ID=(select id from eg_appconfig a where a.KEY_NAME='DA_PAYCOMPONENT');
 delete from eg_appconfig a where a.KEY_NAME='DA_PAYCOMPONENT';
 
 
/****** Help file integration for PF ******/
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_pfsetup.html' WHERE name='PF Setup';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/8_payroll_payrolltxn_pfreports' WHERE name='PF Report';

UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_pension_gratuity_create.html' WHERE name='createGratuity';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_pension_gratuity_view.html' WHERE name='viewGratuity';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_pension_recrecoveries_create.html' WHERE name='RecordRecoveries';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_pension_gratuity_disburse_create.html' WHERE name='createDisburseGratuity';
UPDATE EG_ACTION SET action_help_url='/onlineHelp/9_payroll_pension_gratuity_disburse_view.html' WHERE name='viewDisburseGratuity';

#DOWN