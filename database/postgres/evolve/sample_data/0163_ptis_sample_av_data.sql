#UP

INSERT INTO egpt_demandcalculations (id, id_demand, MODIFIED_DATE)
SELECT seq_demandcalculations.NEXTVAL, id, SYSDATE FROM eg_demand;
    
INSERT INTO eg_attributevalues
    (id, appl_domainid, att_typeid, att_value, domaintxnid)
VALUES
    (seq_eg_attributevalues.NEXTVAL,
     (SELECT id FROM eg_appl_domain WHERE name = 'org.egov.ptis.DCB.model.PTDemandCalculations'),
     (SELECT id FROM eg_attributetype WHERE att_name = 'ANNUALVALUE'),
     '44444.44',
    1
);
INSERT INTO eg_attributevalues
    (id, appl_domainid, att_typeid, att_value, domaintxnid)
VALUES
    (seq_eg_attributevalues.NEXTVAL,
     (SELECT id FROM eg_appl_domain WHERE name = 'org.egov.ptis.DCB.model.PTDemandCalculations'),
     (SELECT id FROM eg_attributetype WHERE att_name = 'ANNUALVALUE'),
     '55555.55',
    2
);
INSERT INTO eg_attributevalues
    (id, appl_domainid, att_typeid, att_value, domaintxnid)
VALUES
    (seq_eg_attributevalues.NEXTVAL,
     (SELECT id FROM eg_appl_domain WHERE name = 'org.egov.ptis.DCB.model.PTDemandCalculations'),
     (SELECT id FROM eg_attributetype WHERE att_name = 'ANNUALVALUE'),
     '66666.66',
    3
);



#DOWN

DELETE FROM eg_attributevalues WHERE att_typeid = (SELECT id FROM eg_attributetype WHERE att_name = 'ANNUALVALUE');
DELETE FROM egpt_demandcalculations;
