drop table egwtr_connectionusage;
delete from egwtr_application_documents;
delete from egwtr_connectiondetails;
delete from egwtr_connection;
delete from egwtr_water_source;
delete from egwtr_property_type;
delete from egwtr_application_process_time;
delete from egwtr_donation_header;
delete from egwtr_donation_details;
delete from egwtr_category;
delete from egwtr_securitydeposit;
delete from egwtr_usage_type;
delete from egwtr_pipesize;


INSERT INTO egwtr_water_source(id, code, watersourcetype, description, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_WATER_SOURCE'), 'SURFACEWATER', 'Surface Water', 'Surface Water', true, now(),now() ,1,1);
INSERT INTO egwtr_water_source(id, code, watersourcetype, description, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_WATER_SOURCE'), 'GROUNDWATER', 'Ground Water', 'Ground Water', true, now(),now() ,1,1);
    
INSERT INTO egwtr_property_type(id, code, name, connectioneligibility, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PROPERTY_TYPE'), 'RESIDENTIAL', 'Residential', 'Y', true, now(),now() ,1,1);
INSERT INTO egwtr_property_type(id, code, name, connectioneligibility, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PROPERTY_TYPE'), 'NON-RESIDENTIAL', 'Non-Residential', 'Y', true, now(),now() ,1,1);
    
INSERT INTO egwtr_category(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'GENERAL', 'General', true, now(),now() ,1,1);
INSERT INTO egwtr_category(id, code, name, description, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'BPL', 'BPL', 'Below Poverty Line', true, now(),now() ,1,1);
INSERT INTO egwtr_category(id, code, name, description, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'OYT', 'OYT', 'Own Your Type', true, now(),now() ,1,1);
    
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'DOMESTIC', 'Domestic', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'COMMERCIAL', 'Commercial', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'APARTMENT', 'Apartment', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'HOTEL', 'Hotel', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'LODGES', 'Lodges', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'INDUSTRIAL', 'Industrial', true, now(),now() ,1,1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'RESIDENTIAL', 'Residential', true,now(),now() ,1,1);
    
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '1/4 Inch', 0.25, 6.35, true, now(),now() ,1,1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '1/2 Inch', 0.5, 12.7, true, now(),now() ,1,1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '3/4 Inch', 0.75, 19.05, true, now(),now() ,1,1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '1 Inch', 1, 25.4, true, now(),now() ,1,1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '2 Inch', 2, 50.8, true, now(),now() ,1,1);  
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '3 Inch', 2, 76.2, true, now(),now() ,1,1); 
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, lastmodifieddate, createdby ,lastmodifiedby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '4 Inch', 2, 101.6, true, now(),now() ,1,1);