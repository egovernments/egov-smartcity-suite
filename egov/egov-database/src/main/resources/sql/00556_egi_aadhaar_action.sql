INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'AadhaarInfo', '/aadhaar', null, 
    (select id from eg_module where name='EGI-COMMON'), 0, 'AadhaarInfo', false, 'egi', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Administration' and parentmodule is null));