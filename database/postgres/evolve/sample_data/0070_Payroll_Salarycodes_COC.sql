#UP

update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='лююд';

#DOWN

update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='BASICPAY' where upper(head)='Basic';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='DEARNESS ALLOWANCE' where head='DA';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='HRA' where head='HRA';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='CITY ALLOWANCE' where head='CCA';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='PROVIDENT FUND' where head='PF';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='PT' where head='PT';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='H.Rent Allowance' where head='H.REN';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='pctOfBasicHRA' where head='pctOfBasicHRA';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='VEHICLE ADVANCE' where head='VEHDED';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='Grade pay' where head='GradePay';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='rule based payhead pct on basic' where head='pctOfBasicRule';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='rule based payhead pct on basic plus gradepay' where head='pctOfBasicGradePay';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='rule based payhead pct on basic and attendance based' where head='pctOfBasicAttendanceY';
update EGPAY_SALARYCODES   set LOCAL_LANG_DESC='MonthlyFlatAttendanceBased' where head='MonthlyFlatAttendance';