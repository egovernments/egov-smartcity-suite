#UP

----- Schedule category for Buildings---

insert into egw_schedulecategory values(EGW_SCHEDULECATEGORY_SEQ.nextval,'BLDG-NORTH',1,sysdate,'Buildings-North Zone',null,null,null);
insert into egw_schedulecategory values(EGW_SCHEDULECATEGORY_SEQ.nextval,'BLDG-CENTRE',1,sysdate,'Buildings-CENTRE Zone',null,null,null);
insert into egw_schedulecategory values(EGW_SCHEDULECATEGORY_SEQ.nextval,'BLDG-SOUTH',1,sysdate,'Buildings-SOUTH Zone',null,null,null);

---------uom category---

insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Numbers','Numbers',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Volume','Volume',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Weight','Weight',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Door','Door',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Hinge','Hinge',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'Distance','Distance',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'pdlk','pdlk',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'post','post',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'rm','rm',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'set','set',sysdate,sysdate,1,1);
insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'windows','windows',sysdate,sysdate,1,1);
--insert into eg_uomcategory values(seq_eg_uomcategory.nextval,'AREA','AREA',sysdate,sysdate,1,1);

---------uom----------

insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Numbers'),'Each','each',1,0,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Numbers'),'No','number',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Volume'),'pnt','pint',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Weight'),'cu.m','cubic meter',1,0,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Door'),'door','door',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Hinge'),'hinge','hinge',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Weight'),'kg','killo gram',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Distance'),'m','meter',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='Distance'),'p/m','p/m',1,0,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='pdlk'),'pdlk','pdlk',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='post'),'post','post',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='rm'),'R/M','r/m',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='set'),'set','set',1,1,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='AREA'),'sq.m','square meter',1,0,sysdate,sysdate,1,1);
insert into eg_uom values(seq_eg_uom.nextval,(select id from eg_uomcategory where category='windows'),'wdw','window',1,1,sysdate,sysdate,1,1);

COMMIT;
#DOWN
