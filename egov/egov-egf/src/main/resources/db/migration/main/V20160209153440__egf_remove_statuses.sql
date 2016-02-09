delete from egw_status where moduletype='BUDGET' and code in('Closed','Verified','Confirmed','Checked','Mark For Checking','Concurrence','Planning');
delete from egw_status where moduletype='CBILL';