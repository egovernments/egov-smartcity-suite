DROP SEQUENCE seq_eg_position;
CREATE SEQUENCE seq_eq_position START WITH 7;

UPDATE egeis_deptdesig set outsourcedposts=0,sanctionedposts=1 where id<6;
