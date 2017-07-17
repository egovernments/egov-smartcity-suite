INSERT INTO egp_portalservice(
            id, module, code, version, url, isactive, name, userservice, 
            businessuserservice, helpdoclink, createdby, createddate)
    VALUES (nextval('seq_egp_portalservice'), (select id from eg_module  where contextroot = 'pgr' and parentmodule is null), 'Register Grievance',0,'/pgr/complaint/citizen/show-reg-form' , 't', 'Register Grievance', 't', 't', 
            '/pgr/complaint/citizen/show-reg-form', 1, now());