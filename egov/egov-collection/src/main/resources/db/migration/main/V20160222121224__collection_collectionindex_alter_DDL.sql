alter table eg_collectionindex drop column installmentfromdate;
alter table eg_collectionindex drop column installmenttodate;
alter table eg_collectionindex add column installmentfrom character varying(50);
alter table eg_collectionindex add column installmentto character varying(50);

