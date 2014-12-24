#UP
drop SEQUENCE  SEQ_FUNDSOURCE;
CREATE SEQUENCE SEQ_FUNDSOURCE 
  START WITH 11;

insert into fundsource
   (ID, CODE, NAME, TYPE, LLEVEL, ISACTIVE, CREATED, LASTMODIFIEDDATE, LASTMODIFIEDBY, ISNOTLEAF)
 Values
   (seq_fundsource.nextval, '57', 'Deposit Works', ' ', 0, 1, TO_DATE('06/22/2006 18:32:06', 'MM/DD/YYYY HH24:MI:SS'), 
   TO_DATE('02/18/2007 00:09:24', 'MM/DD/YYYY HH24:MI:SS'), 1, 0);

#DOWN
delete from fundsource where name='Deposit Works';
