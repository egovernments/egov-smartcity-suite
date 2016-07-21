alter table egpt_mutation_registration_details add documentlink character varying(256);

alter table egpt_mutation_registration_details alter COLUMN seller type character varying(128);
alter table egpt_mutation_registration_details alter COLUMN buyer type character varying(128);
alter table egpt_mutation_registration_details alter COLUMN typeoftransfer type character varying(128);
alter table egpt_mutation_registration_details alter COLUMN documentno type character varying(128);
alter table egpt_mutation_registration_details alter COLUMN doorno type character varying(128);
