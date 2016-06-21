
Create table egcncl_members( 
id bigint,
	designation varchar(128) not null,
	electionWard bigint,	
	qualification varchar(128),
	caste varchar(128),
	partyAffiliation varchar(128),
	birthDate date,
	electionDate date,
	oathDate date,
	mobileNumber varchar(15),
	emailId varchar(52),
	name varchar(100) NOT NULL,
	gender smallint,
	photo bytea,
	residentialAddress varchar(256),
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version  numeric DEFAULT 0
);
ALTER TABLE egcncl_members
    ADD CONSTRAINT fk_egcncl_electionWard FOREIGN KEY (electionWard) REFERENCES eg_boundary(id);

alter table egcncl_members add constraint pk_egcncl_members primary key (id);
create sequence seq_egcncl_members;


Create table egcncl_caste( 
id bigint,
	name varchar(100)NOT NULL,
	isActive boolean NOT NULL default true,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table egcncl_caste add constraint pk_egcncl_caste primary key (id);
create sequence seq_egcncl_caste;



Create table egcncl_designation( 
id bigint,
	name varchar(100)NOT NULL,
	isActive boolean NOT NULL default true,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table egcncl_designation add constraint pk_egcncl_designation primary key (id);
create sequence seq_egcncl_designation;



Create table egcncl_party( 
id bigint,
	name varchar(100)NOT NULL,
	isActive boolean NOT NULL default true,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table egcncl_party add constraint pk_egcncl_party primary key (id);
create sequence seq_egcncl_party;


Create table egcncl_qualification( 
id bigint,
	name varchar(100)NOT NULL,
	isActive boolean NOT NULL default true,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table egcncl_qualification add constraint pk_egcncl_qualification primary key (id);
create sequence seq_egcncl_qualification;

