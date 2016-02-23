INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where (name) LIKE 'Advertisement Tax Admin') ,(select id FROM eg_action  WHERE name = 'AjaxSubCategoryByCategoryId'));

update egcl_servicedetails set VOUCHERCREATION = false , ISVOUCHERAPPROVED = false where code ='ADTAX';

update eg_action set enabled =true where name='renewalsearch' and contextroot='adtax';