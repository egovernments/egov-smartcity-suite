
Create table EGW_MB_MEASUREMENTSHEET( 
id bigint,
	slNo smallint,
	identifier character(1),
	remarks varchar(1024),
	no bigint,
	length numeric (13,2),
	width numeric (13,2),
	depthOrHeight numeric (13,2),
	quantity numeric (13,2),
	mbdetails bigint ,
	womsheetid bigint ,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table EGW_MB_MEASUREMENTSHEET add constraint pk_EGW_MB_MEASUREMENTSHEET primary key (id);
alter table EGW_MB_MEASUREMENTSHEET add constraint fk_EGW_MB_MEASUREMENTSHEET_mbdetails  FOREIGN KEY (mbdetails) REFERENCES EGW_MB_DETAILS(id);
alter table EGW_MB_MEASUREMENTSHEET add constraint fk_EGW_MB_MEASUREMENTSHEET_msheetid  FOREIGN KEY (womsheetid) REFERENCES EGW_WO_MEASUREMENTSHEET(id);
create sequence seq_EGW_MB_MEASUREMENTSHEET;
