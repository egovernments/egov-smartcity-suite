update eg_action set displayname = 'Update/View Employee' where name='Search Employee' and enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'Create Employee' where name='Create Employee' and  enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'Search Position' where name='Search Position' and enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'Create Position' where name='Create Position' and enabled=true and  application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'View Designation' where name='View Designation' and enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'Modify Designation' where name='Update Designation' and enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);

update eg_action set displayname = 'Create Designation' where name='Create Designation' and enabled=true and application=(select id from eg_module where name='EIS' and parentmodule is null);