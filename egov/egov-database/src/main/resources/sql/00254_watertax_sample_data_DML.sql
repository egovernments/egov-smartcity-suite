INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'NEWCONNECTION', 'New connection', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'ADDNLCONNECTION', 'Additional connection', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'CHANGEOFUSE', 'Change of use', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'CLOSINGCONNECTION', 'Closing connection', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'RECONNECTION', 'Reconnection', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'HOLDINGCONNECTION', 'Holding connection', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'TITLETRANSFER', 'Title transfer', true, now(), 1);
INSERT INTO egwtr_application_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_TYPE'), 'REGLZNCONNECTION', 'Regularization connection', true, now(), 1);


INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'COMMERCIAL', 'Commercial', true, now(), 1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'DOMESTIC', 'Domestic', true, now(), 1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'RESIDENTIAL', 'Residential', true, now(), 1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'DRINKING', 'Drinking', true, now(), 1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'INDUSTRIAL', 'Industrial', true, now(), 1);
INSERT INTO egwtr_usage_type(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_USAGE_TYPE'), 'NONRESIDENTIAL', 'Non-residential', true, now(), 1);

INSERT INTO egwtr_category(id, code, name, description, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'BPL', 'BPL', 'Below Poverty Line', true, now(), 1);
INSERT INTO egwtr_category(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'COMMERCIAL', 'Commercial', true, now(), 1);
INSERT INTO egwtr_category(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'DOMESTIC', 'Domestic', true, now(), 1);
INSERT INTO egwtr_category(id, code, name, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'GENERAL', 'General', true, now(), 1);
INSERT INTO egwtr_category(id, code, name, description, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CATEGORY'), 'OYT', 'OYT', 'Own Your Type', true, now(), 1);

INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '1 Inch', 1, 25.4, true, now(), 1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '1/2 Inch', 0.5, 12.7, true, now(), 1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), '3/4 Inch', 0.75, 19.05, true, now(), 1);
INSERT INTO egwtr_pipesize(id, code, sizeininch, sizeinmilimeter, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PIPESIZE'), 'Zero Inch', 0, 0, true, now(), 1);

INSERT INTO egwtr_penalty(id, penaltytype, active, fromdate, todate, percentage, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PENALTY'), 'Late Payment', true, to_date('01-04-2015','DD-MM-YYYY'), null, 1.00, now(), 1); 
INSERT INTO egwtr_penalty(id, penaltytype, active, fromdate, todate, percentage, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PENALTY'), 'Interest on Arrears', true, to_date('01-04-2015','DD-MM-YYYY'), null, 1.50, now(), 1);


INSERT INTO egwtr_property_type(id, code, name, connectioneligibility, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PROPERTY_TYPE'), 'GROUNDFLOOR', 'Ground Floor', 'Y', true, now(), 1);
INSERT INTO egwtr_property_type(id, code, name, connectioneligibility, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PROPERTY_TYPE'), 'ONESTOREBLDG', 'One store building', 'Y', true, now(), 1);
INSERT INTO egwtr_property_type(id, code, name, connectioneligibility, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_PROPERTY_TYPE'), 'TWOSTOREBLDG', 'Two store building', 'Y', true, now(), 1);


INSERT INTO egwtr_water_source(id, code, watersourcetype, description, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_WATER_SOURCE'), 'GROUNDWATER', 'Ground Water', 'Ground Water', true, now(), 1);
INSERT INTO egwtr_water_source(id, code, watersourcetype, description, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_WATER_SOURCE'), 'SURFACEWATER', 'Surface Water', 'Surface Water', true, now(), 1);


INSERT INTO egwtr_connectioncharges(id, type, active, fromdate, todate, amount, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CONNECTIONCHARGES'), 'Connection fee', true, to_date('01-04-2015','DD-MM-YYYY'), null, 5000.00, now(), 1);
INSERT INTO egwtr_connectioncharges(id, type, active, fromdate, todate, amount, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CONNECTIONCHARGES'), 'Holding connection fee', true, to_date('01-04-2015','DD-MM-YYYY'), null, 2000.00, now(), 1);
INSERT INTO egwtr_connectioncharges(id, type, active, fromdate, todate, amount, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CONNECTIONCHARGES'), 'Reconnection fee', true, to_date('01-04-2015','DD-MM-YYYY'), null, 1000.00, now(), 1);
INSERT INTO egwtr_connectioncharges(id, type, active, fromdate, todate, amount, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_CONNECTIONCHARGES'), 'Title Transfer fee', true, to_date('01-04-2015','DD-MM-YYYY'), null, 500.00, now(), 1);


INSERT INTO egwtr_demandnotice_penalty_period(id, issueofdemandnotice, penaltyperiod, min_con_holding_months, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DEMANDNOTICE_PENALTY_PERIOD'), 'HALFYEARLY', 15, 6, now(), 1);


--rollback delete from egwtr_demandnotice_penalty_period;
--rollback delete from egwtr_connectioncharges;
--rollback delete from egwtr_water_source;
--rollback delete from egwtr_property_type;
--rollback delete from egwtr_penalty;
--rollback delete from egwtr_pipesize;
--rollback delete from egwtr_category;
--rollback delete from egwtr_usage_type;
--rollback delete from egwtr_application_type;
