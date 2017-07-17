alter table egmrs_document add column mandatory boolean default false;
update egmrs_document set mandatory='t' where code in ('MoM','CF_STAMP');

alter table egmrs_document ADD column documentprooftype character varying(100);
update egmrs_document set documentprooftype='COMMON' WHERE individual='f';
update egmrs_document set documentprooftype='AGE_PROOF' WHERE code in ('SLC','BC','DCA','DCSWA','NotaryAffidavit');
update egmrs_document set CODE='TelephoneBill' where code='TelephoneBill';
update egmrs_document set CODE='Others' where code='Others ';
update egmrs_document set documentprooftype='ADDRESS_PROOF' WHERE code in ('RationCard','MSEBBILL','TelephoneBill','Others');
update egmrs_document set documentprooftype='COMMON' WHERE code in ('Aadhar','Passport');