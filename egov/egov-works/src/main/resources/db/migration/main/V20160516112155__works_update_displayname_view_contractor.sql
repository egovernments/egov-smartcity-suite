--------------Start--------------------
update eg_action set displayname  = 'View/Modify Contractor' where name='WorksContractorSearch' and contextroot = 'egworks';

--rollback update eg_action set displayname  = 'View/Edit Contractor' where name='WorksContractorSearch' and contextroot = 'egworks';

--------------End----------------------