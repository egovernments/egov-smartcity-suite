create table egpt_assessment_transactions (
id		bigint primary key,
basicproperty	bigint,
property	bigint,
mutation	bigint,
vacancyremission bigint,
id_demand	bigint,
transactiontype	character varying(64),
zone		bigint,
ward		bigint,
street		bigint,
block		bigint,
locality	bigint,
electionward	bigint,
ownersname	character varying(256),
doorno		character varying(64),
propertytype	bigint,
usage		character varying(256),
tax_effectivedate     	date,
transaction_date	date,
version         	numeric default 1,
createdby       	numeric default 1,
createddate     	timestamp without time zone default now(),
lastmodifiedby  	numeric default 1,
lastmodifieddate	timestamp without time zone default now()
);

create sequence seq_egpt_assessment_transactions;

alter table egpt_assessment_transactions add constraint fk_basicproperty foreign key (basicproperty) references egpt_basic_property (id);

alter table egpt_assessment_transactions add constraint fk_property foreign key (property) references egpt_property (id);

alter table egpt_assessment_transactions add constraint fk_mutation foreign key (mutation) references egpt_property_mutation (id);

alter table egpt_assessment_transactions add constraint fk_vacancyremission foreign key (vacancyremission) references egpt_vacancy_remission (id);

alter table egpt_assessment_transactions add constraint fk_zone foreign key (zone) references eg_boundary (id);

alter table egpt_assessment_transactions add constraint fk_ward foreign key (ward) references eg_boundary (id);
alter table egpt_assessment_transactions add constraint fk_street foreign key (street) references eg_boundary (id);
alter table egpt_assessment_transactions add constraint fk_block foreign key (block) references eg_boundary (id);
alter table egpt_assessment_transactions add constraint fk_locality foreign key (locality) references eg_boundary (id);
alter table egpt_assessment_transactions add constraint fk_electionward foreign key (electionward) references eg_boundary (id);

alter table egpt_assessment_transactions add constraint fk_propertytype foreign key (propertytype) references egpt_property_type_master (id);

create table egpt_installment_demand_info (
id				bigint primary key,
assessment_transactions		bigint,
installment			bigint,
demand				double precision,
totalcollection			double precision,
advance				double precision,
writeoff			double precision,
version         		numeric default 1,
createdby       		numeric default 1,
createddate     		timestamp without time zone default now(),
lastmodifiedby  		numeric default 1,
lastmodifieddate		timestamp without time zone default now()
);

alter table egpt_installment_demand_info add constraint fk_assessment_transactions foreign key (assessment_transactions) references egpt_assessment_transactions (id);

alter table egpt_installment_demand_info add constraint fk_id_installment foreign key (installment) references eg_installment_master (id);

create sequence seq_egpt_installment_demand_info;

create table egpt_installment_collection_info (
id			bigint primary key,
installment_demand_info	bigint,
collectiondate		date,
receiptnumber		character varying(64),
collectionmode		character varying(64),
amount			double precision,
version         	numeric default 1,
createdby       	numeric default 1,
createddate     	timestamp without time zone default now(),
lastmodifiedby  	numeric default 1,
lastmodifieddate	timestamp without time zone default now()
);

alter table egpt_installment_collection_info add constraint fk_id_installment_demand_info foreign key (installment_demand_info) references egpt_installment_demand_info (id);

create sequence seq_egpt_installment_collection_info;
