DROP TABLE eg_user_jurlevel ;
DROP TABLE eg_user_jurvalues ;

CREATE SEQUENCE seq_egeis_jurisdiction;
CREATE TABLE egeis_jurisdiction (
	id BIGINT CONSTRAINT PK_EGEIS_JURISDICTION_ID PRIMARY KEY,
	employee BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_EMPLOYEE REFERENCES egeis_employee(id) NOT NULL,
	boundarytype BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_BOUNDARYTYPE REFERENCES eg_boundary_type(id) NOT NULL,
	createddate timestamp without time zone,
	lastmodifieddate timestamp without time zone,
	createdby BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_CREATEDBY REFERENCES eg_user(id),
	lastmodifiedby BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_MODIFIEDBY REFERENCES eg_user(id),
	version BIGINT,
	CONSTRAINT UNQ_EGEIS_JURISDICTION_EMP_BNDRYTYPE UNIQUE(employee,boundarytype)
);


CREATE SEQUENCE seq_egeis_jurisdiction_details;
CREATE TABLE egeis_jurisdiction_details (
	id BIGINT CONSTRAINT PK_EGEIS_JRDNDETAILS_ID PRIMARY KEY,
	jurisdiction BIGINT CONSTRAINT FK_EGEIS_JRDNDETAILS_JURISDICTION REFERENCES egeis_jurisdiction(id) NOT NULL,	
	boundary BIGINT CONSTRAINT FK_EGEIS_JRDNDETAILS_BOUNDARY REFERENCES eg_boundary(id) NOT NULL,
	createddate timestamp without time zone,
	lastmodifieddate timestamp without time zone,
	createdby BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_CREATEDBY REFERENCES eg_user(id),
	lastmodifiedby BIGINT CONSTRAINT FK_EGEIS_JURISDICTION_MODIFIEDBY REFERENCES eg_user(id),
	version BIGINT
);
