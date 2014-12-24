#UP

INSERT INTO EG_ROLEACTION_MAP
            (actionid, roleid)
   SELECT *
     FROM (SELECT ID, id_role
             FROM EG_ACTION, EG_ROLES
            WHERE NAME LIKE 'EgiIndex'
              AND role_name IN
                     ('GRIEVANCE_OFFICER',
                      'PGR_ADMINISTRATOR',
                      'PGR_Officer',
                      'PGR_Operator'                     
                     ));
COMMIT;                     
#DOWN
