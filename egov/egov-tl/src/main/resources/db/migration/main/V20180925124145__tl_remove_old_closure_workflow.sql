delete from eg_wf_matrix where additionalrule='CLOSURELICENSE' and currentstate in('Rejected','SI/SS Approved','Revenue Clerk/JA Approved','NEW')
and nextstate in('Revenue Clerk/JA Approved','END','SI/SS Approved','Revenue Clerk/JA Approved')
and nextstatus in('Closure Initiated','License Cancelled','SI/SS Approved','Closure Initiated');
