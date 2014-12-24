--== TAXSETUP ISSUE=
-- delete taxcode entry other than  'ADVT','PT'
delete from egf_Cess_master where taxcodeid not in(select id from egf_tax_code where code in ('ADVT','PT'));
delete from egf_tax_account_mapping where taxcodeid not in(select id from egf_tax_code where code in ('ADVT','PT'));
delete from egf_tax_code where id not in( select id from egf_tax_code where code in ('ADVT','PT'));

--delete cess entries for which taxentry financialrow doesn't exists
delete from egf_cess_master where financialyearid not in(select financialyear from egf_tax_account_mapping);

--delete financialyear whose financialyearid is null
delete from egf_tax_account_mapping where financialyearid is null;

-- update financialyear to correct format
update egf_tax_account_mapping  etcm set etcm.financialyear=(select financialyear from financialyear where substr(etcm.financialyear,1,4)=substr(financialyear,1,4));
update egf_cess_master  ecm set ecm.financialyearid=(select financialyear from financialyear where substr(ecm.financialyearid,1,4)=substr(financialyear,1,4));

-- delete duplicate entries in cess master
delete from egf_cess_master where id in(
	   select b.id from egf_Cess_master a, egf_Cess_master b where a.taxcodeid=b.taxcodeid and  a.glcodeid=b.glcodeid and a.financialyearid=b.financialyearid and b.id>a.id
	   );
delete from egf_tax_account_mapping where id in(
	   select b.id from egf_tax_account_mapping a, egf_tax_account_mapping b where a.taxcodeid=b.taxcodeid and  a.financialyearid=b.financialyearid and b.id>a.id
	   );
commit;	   
