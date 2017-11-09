create sequence seq_supplier;
Create table supplier( 
	id bigint,
	code varchar(50)NOT NULL,
	name varchar(50)NOT NULL,
	address varchar(300),
	mobile varchar(10),
	email varchar(25),
	narration varchar(250),
	isactive boolean,
	panno varchar(10),
	tinno varchar(20),
	regno varchar(25),
	bankaccount varchar(25),
	ifsccode varchar(12),
	bank bigint ,
	lastmodifieddate date,
	createddate date,
	modifiedby bigint,
	createdby bigint,
	version numeric
	);

ALTER TABLE Supplier ADD CONSTRAINT supplier_code_unq UNIQUE (code);
ALTER TABLE accountdetailtype ADD CONSTRAINT accoountdetailtype_name_unq UNIQUE (name);
update accountdetailtype set tablename='Supplier' where name='Supplier';


