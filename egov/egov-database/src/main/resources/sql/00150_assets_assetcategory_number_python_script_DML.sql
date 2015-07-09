INSERT INTO eg_script(id, name, script_type, created_by, created_date, modified_by, modified_date, script, start_date, end_date) VALUES (nextval('eg_script_seq'), 'assets.assetcategorynumber.generator', 'python', null, null, null, null, 'result=sequenceGenerator.getNextNumber("ASSETCATEGORY_NUMBER",1).getFormattedNumber().zfill(3)', '1900-01-01 00:00:00', '2100-01-01 00:00:00');

--rollback delete from eg_script where name='assets.assetcategorynumber.generator';
