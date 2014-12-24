#UP

--Budget check only for department, function ---

update eg_appconfig_values set value='department,function' where key_id in(select id from eg_appconfig where key_name='budgetaryCheck_groupby_values' and module='EGF');

-- Inserting to budget group for 2201201 --

insert into egf_budgetgroup values(seq_egf_budgetgroup.nextval,null,577,577,'2201201-Telephone','2201201-Telephone','DEBIT','REVENUE_EXPENDITURE',1,sysdate);

-- Making a budget entry for 2201201 glcode ---
Insert into egf_budgetdetail
   (ID, EXECUTING_DEPARTMENT, FUNCTION, BUDGET, BUDGETGROUP, ORIGINALAMOUNT, APPROVEDAMOUNT, BUDGETAVAILABLE, BOUNDARY, MODIFIEDDATE, MODIFIEDBY, CREATEDDATE, CREATEDBY, FUND)
 Values
   (seq_egf_budgetdetail.nextval, (select id_dept from eg_department where dept_code='H'), (select id from function where code='3100'), 
   (select id from egf_budget where name='2009-2010-BE'),(select id from egf_budgetgroup where name='2201201-Telephone'), 500000, 500000, 700000,
   null, TO_DATE('03/09/2010 21:07:43', 'MM/DD/YYYY HH24:MI:SS'), 1, TO_DATE('03/09/2010 21:07:43', 'MM/DD/YYYY HH24:MI:SS'), 1, 
   (select id from fund where code='0101') );

-- Making budget check required for 2201201 ---

update chartofaccounts set budgetcheckreq=1 where glcode=2201201;

#DOWN
