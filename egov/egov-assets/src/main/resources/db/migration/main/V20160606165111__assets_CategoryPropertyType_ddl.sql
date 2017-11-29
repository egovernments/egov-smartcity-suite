create sequence seq_egasset_category_propertytype;
Create table egasset_category_propertytype( 
id bigint,
	name varchar(50),
	dataType varchar(50),
	format varchar(50),
	categoryid bigint ,
	isActive boolean,
	isMandatory boolean,
	enumvalues varchar(300),
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table egasset_category_propertytype add constraint pk_egasset_category_propertytype primary key (id);
alter table egasset_category_propertytype add constraint fk_egasset_category_propertytype_categoryid  FOREIGN KEY (categoryid) REFERENCES EGASSET_ASSET_CATEGORY(id);
