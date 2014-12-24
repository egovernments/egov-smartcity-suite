#UP

INSERT INTO EG_USER
   (ID_USER, FIRST_NAME, USER_NAME, PWD, PWD_REMINDER, UPDATETIME, IS_SUSPENDED, ISACTIVE, FROMDATE, TODATE)
 VALUES
   (seq_eg_user.NEXTVAL, 'landestate', 'landestate', 't27o223b7q3k0mtic20k1u32n', 'egovfinancials', sysdate, 'N', 1, TO_DATE('01/01/2010 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2015 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO eg_userrole
(id_role, id_user, ID,fromdate, todate, is_history)
VALUES ((SELECT id_role FROM eg_roles WHERE ROLE_NAME='LEAdmin'), 
(SELECT id_user FROM eg_user WHERE user_name = 'landestate'), seq_eg_userrole.NEXTVAL,
TO_DATE('01-01-2010','mm-dd-yyyy'), '', 'N');


#DOWN

DELETE FROM EG_USERROLE WHERE id_user IN (SELECT id_user FROM EG_USER WHERE user_name='landestate');
DELETE FROM EG_USER WHERE user_name='landestate';
