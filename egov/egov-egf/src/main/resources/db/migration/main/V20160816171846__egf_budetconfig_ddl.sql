Create table EGF_BudgetControlType( 
	id bigint,
	value character varying(30),
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table EGF_BudgetControlType add constraint pk_EGF_BudgetControlType primary key (id);
Create table EGF_BudgetControlType_aud( 
	id bigint,
	value  character varying(30),
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
); 

create sequence seq_egf_BudgetControlType;
