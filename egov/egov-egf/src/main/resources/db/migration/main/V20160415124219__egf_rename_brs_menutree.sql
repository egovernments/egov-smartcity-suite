update eg_module set enabled=true,displayname='Bank Reconciliation' where name='BRS';
update eg_action set enabled=true where name ='AutoReconcile';
update eg_action set enabled=false where name ='Dishonored Cheque Search';
update eg_action set displayname='Reconcile with Bank-Manual' where name='Reconcile with Bank';
update eg_action set displayname='Auto reconcile with Bank',ordernumber=2 where name='AutoReconcile-Upload';
update eg_action set displayname='Auto reconciliation Report',ordernumber=4 where name='AutoReconcile';
update eg_action set displayname='Reconciliation Summary Report',ordernumber=5 where name='Reconciliation Summary-New';