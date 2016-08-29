update egw_lineestimate set beneficiary = 'GENERAL' where workcategory ='NON_SLUM_WORK';
update egw_lineestimate set beneficiary = 'SC' where workcategory='SLUM_WORK' and beneficiary = 'SC_ST';
update egw_lineestimate set beneficiary = 'GENERAL' where workcategory='SLUM_WORK' and beneficiary = 'OTHERS';
update egw_lineestimate set workcategory = 'NOTIFIED_SLUM' where workcategory='SLUM_WORK' and typeofslum ='NOTIFIED';
update egw_lineestimate set workcategory = 'NON_NOTIFIED_SLUM' where workcategory='SLUM_WORK' and typeofslum ='NON_NOTIFIED';
update egw_lineestimate set workcategory = 'NON_SLUM' where workcategory ='NON_SLUM_WORK';

--rollback update egw_lineestimate set beneficiary = 'null' where workcategory ='NON_SLUM_WORK';
--rollback update egw_lineestimate set beneficiary = 'SC_ST' where workcategory='SLUM_WORK' and beneficiary = 'SC';
--rollback update egw_lineestimate set beneficiary = 'OTHERS' where workcategory='SLUM_WORK' and beneficiary = 'GENERAL';
--rollback update egw_lineestimate set workcategory = 'SLUM_WORK' where typeofslum ='NOTIFIED';
--rollback update egw_lineestimate set workcategory = 'SLUM_WORK' where typeofslum ='NON_NOTIFIED';
--rollback update egw_lineestimate set workcategory = 'NON_SLUM_WORK' where workcategory ='NON_SLUM';
