update eg_action set url='/brs/manualReconciliation-newForm.action',queryparams=null where url='/brs/BankReconciliation.do'
and queryparams='submitType=toLoad';

update eg_action set url='/brs/manualReconciliation-ajaxSearch.action',queryparams=null where url='/brs/BankReconciliation.do'
and queryparams='submitType=showBrsDetails';

update eg_action set url='/brs/manualReconciliation-update.action',queryparams=null where url='/brs/BankReconciliation.do'
and queryparams='submitType=reconcile';

update eg_action set enabled=true where name='AutoReconcile';
