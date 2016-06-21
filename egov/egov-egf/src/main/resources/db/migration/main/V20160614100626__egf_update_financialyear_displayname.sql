update eg_action set displayname = 'Create Financial Year' where displayname='Create FinancialYear' and enabled=true and  application=(select id from eg_module where name='EGF' and parentmodule is null);

update eg_action set displayname = 'View Financial Year' where displayname='View FinancialYear' and enabled=true and  application=(select id from eg_module where name='EGF' and parentmodule is null);

update eg_action set displayname = 'Edit Financial Year' where displayname='Edit FinancialYear' and enabled=true and  application=(select id from eg_module where name='EGF' and parentmodule is null);