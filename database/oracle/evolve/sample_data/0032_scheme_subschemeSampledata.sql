#UP

insert into scheme(id,code,name,validfrom,validto,isactive,description,fundid) values
 (seq_scheme.nextval,'MLA990','ACDP (MLS GRANT)','01-Apr-2008','31-Mar-2012',1,
'ACDP (MLS GRANT)',(select id from fund where code='0101'));

insert into scheme(id,code,name,validfrom,validto,isactive,description,fundid) values
 (seq_scheme.nextval,'APURMSP479','APURMSP','01-Apr-2008','31-Mar-2012',1,
'APURMSP',(select id from fund where code='0101'));

insert into scheme(id,code,name,validfrom,validto,isactive,description,fundid) values
 (seq_scheme.nextval,'SWDJNNURM','SWDJNNURM','01-Apr-2008','31-Mar-2012',1,
'SWDJNNURM',(select id from fund where code='0101'));


insert into scheme(id,code,name,validfrom,validto,isactive,description,fundid) values
 (seq_scheme.nextval,'EQ175','EARTH QUACK','01-Apr-2008','31-Mar-2012',1,
'EARTH QUACK',(select id from fund where code='0102'));


insert into sub_scheme(id,code,name,validfrom,validto,isactive,schemeid,lastmodifieddate) values(seq_sub_scheme.nextval,'EQ175','EARTH QUACK','01-Apr-2008','31-Mar-2012',1,
(select id from scheme where code='EQ175'),sysdate);


insert into sub_scheme(id,code,name,validfrom,validto,isactive,schemeid,lastmodifieddate) values(seq_sub_scheme.nextval,'MLA990','ACDP (MLS GRANT)','01-Apr-2008','31-Mar-2012',1,
(select id from scheme where code='MLA990'),sysdate);

insert into sub_scheme(id,code,name,validfrom,validto,isactive,schemeid,lastmodifieddate) values(seq_sub_scheme.nextval,'MLA991','ACDP (OTHER GRANT)','01-Apr-2008','31-Mar-2012',1,
(select id from scheme where code='MLA990'),sysdate);

insert into sub_scheme(id,code,name,validfrom,validto,isactive,schemeid,lastmodifieddate) values(seq_sub_scheme.nextval,'APURMSP479','APURMSP','01-Apr-2008','31-Mar-2012',1,
(select id from scheme where code='APURMSP479'),sysdate);

insert into sub_scheme(id,code,name,validfrom,validto,isactive,schemeid,lastmodifieddate) values(seq_sub_scheme.nextval,'SWDJNNURM','SWDJNNURM','01-Apr-2008','31-Mar-2012',1,
(select id from scheme where code='SWDJNNURM'),sysdate);

#DOWN
