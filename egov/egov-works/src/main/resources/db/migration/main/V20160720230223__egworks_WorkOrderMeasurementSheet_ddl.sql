
Create table EGW_WO_MEASUREMENTSHEET( 
id bigint,
	slNo smallint,
	identifier character(1) ,
	remarks varchar(1024),
	no bigint,
	length numeric (13,2),
	width numeric (13,2),
	depthOrHeight numeric (13,2),
	quantity numeric (13,2),
	woactivityid bigint ,
	msheetid bigint ,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table EGW_WO_MEASUREMENTSHEET add constraint pk_EGW_WO_MEASUREMENTSHEET primary key (id);
alter table EGW_WO_MEASUREMENTSHEET add constraint fk_EGW_WO_MEASUREMENTSHEET_woactivityid  FOREIGN KEY (woactivityid) REFERENCES EGW_WORKORDER_ACTIVITY(id);
alter table EGW_WO_MEASUREMENTSHEET add constraint fk_EGW_WO_MEASUREMENTSHEET_msheetid  FOREIGN KEY (msheetid) REFERENCES EGW_MEASUREMENTSHEET(id);
create sequence seq_EGW_WO_MEASUREMENTSHEET;
