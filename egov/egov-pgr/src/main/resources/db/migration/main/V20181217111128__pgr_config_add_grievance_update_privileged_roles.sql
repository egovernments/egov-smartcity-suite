INSERT INTO egpgr_configuration
VALUES (nextval('seq_egpgr_configuration'), 'GRIEVANCE_UPDATE_PRIVILEGED_ROLES', 'Grievance Officer',
        'User roles who can update grievance at any level',
        (select id from eg_user where username='system'),now(),
        (select id from eg_user where username='system'),now(),0);
