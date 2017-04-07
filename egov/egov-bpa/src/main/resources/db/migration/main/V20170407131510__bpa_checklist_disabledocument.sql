update EGBPA_MSTR_CHKLISTDETAIL set isactive='false' where checklist in(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION');

update EGBPA_MSTR_CHKLISTDETAIL set isactive='true' where checklist in(select id from EGBPA_MSTR_CHECKLIST where checklisttype='DOCUMENTATION')
and code in('101','102','103','104','201','202','203','204','301','302','303','304','401','402','403','404','501','502','503','504','601','602','603','604','701','702','703','704','801','802','803','804');