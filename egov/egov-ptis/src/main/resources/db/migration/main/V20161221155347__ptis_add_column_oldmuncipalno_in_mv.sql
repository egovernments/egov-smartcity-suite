
--add a column oldmuncipalnumber in MV
alter table egpt_mv_propertyinfo add column oldmuncipalnumber character varying(64);

--update oldmuncipalnumber in MV
update egpt_mv_propertyinfo set oldmuncipalnumber = (select municiapl_no_old from egpt_basic_property where propertyid=upicno);
