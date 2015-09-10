


INSERT INTO egadtax_document_type (id, name, mandatory, transactiontype, version) values (nextval('SEQ_egadtax_document_TYPE'), 'Permission letter', 'f', 'HOARDING' , null);
INSERT INTO egadtax_document_type (id, name, mandatory, transactiontype, version) values (nextval('SEQ_egadtax_document_TYPE'), 'Application form', 'f', 'HOARDING' , null);


INSERT INTO egadtax_revenueinspectors (id, name, active,  version) values (nextval('SEQ_egadtax_revenueinspectors'), 'Julian', 't',  null);
INSERT INTO  egadtax_revenueinspectors ( id, name, active,  version) values (nextval('SEQ_egadtax_revenueinspectors'), 'Ramesh', 't', null);


INSERT INTO egadtax_rates_Class (id, description, active,  version) values (nextval('SEQ_egadtax_ratesClass'), 'Best Rate', 't',  null);
INSERT INTO  egadtax_rates_Class ( id, description, active,  version) values (nextval('SEQ_egadtax_ratesClass'), 'Better Rate', 't', null);
INSERT INTO egadtax_rates_Class (id, description, active,  version) values (nextval('SEQ_egadtax_ratesClass'), 'Average Rate', 't',  null);
INSERT INTO  egadtax_rates_Class ( id, description, active,  version) values (nextval('SEQ_egadtax_ratesClass'), 'Poor Rate', 't', null);


INSERT INTO egadtax_propertytype (id, description, active,  version) values (nextval('SEQ_egadtax_propertytype'), 'Private', 't',  null);
INSERT INTO  egadtax_propertytype ( id, description, active,  version) values (nextval('SEQ_egadtax_propertytype'), 'ULB', 't', null);
INSERT INTO egadtax_propertytype (id, description, active,  version) values (nextval('SEQ_egadtax_propertytype'), 'Government', 't',  null);
INSERT INTO  egadtax_propertytype ( id, description, active,  version) values (nextval('SEQ_egadtax_propertytype'), 'Own', 't', null);


INSERT INTO egadtax_UnitOfMeasure (id,code, description, active, createdby, version) values (nextval('SEQ_egadtax_UnitOfMeasure'), 'SQMT','SQ.MT', 't', 1, null);
INSERT INTO  egadtax_UnitOfMeasure ( id,code, description, active,  createdby,version) values (nextval('SEQ_egadtax_UnitOfMeasure'), 'SQFT','SQ.FT', 't',1, null);
INSERT INTO egadtax_UnitOfMeasure (id,code, description, active,  createdby,version) values (nextval('SEQ_egadtax_UnitOfMeasure'), 'NUMBER','NUMBER', 't',  1,null);
INSERT INTO  egadtax_UnitOfMeasure ( id, code,description, active,  createdby,version) values (nextval('SEQ_egadtax_UnitOfMeasure'), 'EACH','EACH', 't', 1,null);


INSERT INTO egadtax_CATEGORY (id,code, NAME, active, createdby, version) values (nextval('SEQ_egadtax_CATEGORY'), 'NON-ILLUMINATED','NON-ILLUMINATED', 't', 1, null);
INSERT INTO  egadtax_CATEGORY ( id,code, NAME, active,  createdby,version) values (nextval('SEQ_egadtax_CATEGORY'), 'ILLUMINATED','ILLUMINATED', 't',1, null);
INSERT INTO egadtax_CATEGORY (id,code, NAME, active,  createdby,version) values (nextval('SEQ_egadtax_CATEGORY'), 'CINEMAHALL','Advertisement in cinema House', 't',  1,null);
INSERT INTO  egadtax_CATEGORY ( id, code,NAME, active,  createdby,version) values (nextval('SEQ_egadtax_CATEGORY'), 'PRINTEDADVERTISEMENT','Printed Advertisement', 't', 1,null);


INSERT INTO egadtax_subCATEGORY (id,category,code, description, active, createdby, version) values (nextval('SEQ_egadtax_SUBCATEGORY'),
(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='NON-ILLUMINATED'),'ONLANDBUILDING','On Land, Building', 't', 1, null);
INSERT INTO egadtax_subCATEGORY (id,category,code, description, active, createdby, version) values (nextval('SEQ_egadtax_SUBCATEGORY'),
(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='ILLUMINATED'),'illuminated-landbuilding','On Land, Building,wall', 't', 1, null);
INSERT INTO egadtax_subCATEGORY (id,category,code, description, active, createdby, version) values (nextval('SEQ_egadtax_SUBCATEGORY'),
(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='CINEMAHALL'),'CINEMAHALL','In cinema hall', 't', 1, null);
INSERT INTO egadtax_subCATEGORY (id,category,code, description, active, createdby, version) values (nextval('SEQ_egadtax_SUBCATEGORY'),
(SELECT ID FROM  egadtax_CATEGORY WHERE CODE='PRINTEDADVERTISEMENT'),'PRINTEDADVERTISEMENT','Printed advertisement for display', 't', 1, null);


INSERT INTO egadtax_agency (id,code, name,mobilenumber,address, STATUS,  createdby, createddate,version) values (nextval('SEQ_egadtax_AGENCY'),'SAIENTER','Sri Sai Enterprise', '9999999999','333,1st main road, m.g.road, visakhapatnam',(select id from egw_status where MODULETYPE='ADVERTISEAGENCY' and DESCRIPTION='ACTIVE'), 1, CURRENT_DATE,null);
INSERT INTO egadtax_agency (id,code, name,mobilenumber,address, STATUS,  createdby, createddate,version) values (nextval('SEQ_egadtax_AGENCY'),'TIRUMALAENTER','TIRUMALA AGENCY', '9999999999','233,13TH main road, TEMPLE ROAD, TIRUPATHI',(select id from egw_status where MODULETYPE='ADVERTISEAGENCY' and DESCRIPTION='ACTIVE'), 1, CURRENT_DATE,null);


--rollback delete from egadtax_document_type ;
--rollback delete from egadtax_revenueinspectors;
--rollback delete from egadtax_rates_Class;
--rollback delete from egadtax_propertytype ;
--rollback DELETE FROM egadtax_UnitOfMeasure;
--rollback delete from egadtax_subCATEGORY ;
--rollback DELETE FROM egadtax_CATEGORY;
--rollback delete from egadtax_agency;