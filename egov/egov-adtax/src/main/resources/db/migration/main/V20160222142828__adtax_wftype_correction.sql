update eg_wf_types set module =(select id from eg_module where name='PGR' and parentmodule is null) where type='Complaint';
update eg_wf_types set module =(select id from eg_module where name='Budgeting') where type='BudgetDetail';
update eg_wf_types set module =(select id from eg_module where name='Budgeting') where type='BudgetReAppropriationMisc';
update eg_wf_types set module =(select id from eg_module where name='EGF') where type='Paymentheader';
update eg_wf_types set module =(select id from eg_module where name='Budgeting') where type='Budget';
update eg_wf_types set module =(select id from eg_module where name='EGF') where type='CVoucherHeader';
update eg_wf_types set module =(select id from eg_module where name='EGF') where type='ContraJournalVoucher';
update eg_wf_types set module =(select id from eg_module where name='Bill Registers') where type='EgBillregister';
update eg_wf_types set module =(select id from eg_module where name='EGF') where type='ReceiptVoucher';
update eg_wf_types set module =(select id from eg_module where name='Collection' and parentmodule is null) where link like '/collection%';
update eg_wf_types set module =(select id from eg_module where name='Property Tax' and parentmodule is null) where type='PropertyImpl';
update eg_wf_types set module =(select id from eg_module where name='Property Tax' and parentmodule is null) where link like '/ptis%';
update eg_wf_types set module =(select id from eg_module where name='Water Tax Management' and parentmodule is null) where link like '/wtms%';
update eg_wf_types set module =(select id from eg_module where name='Trade License' and parentmodule is null) where link like '/tl%';
update eg_wf_types set module =(select id from eg_module where name='Works Management' and parentmodule is null) where link like '/egworks%';

