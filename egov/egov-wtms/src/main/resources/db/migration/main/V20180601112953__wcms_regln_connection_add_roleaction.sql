
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),(select id from eg_action where name='WaterTaxCreateRegularisedConnectionNewForm'));

