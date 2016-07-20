
Create table EGW_MEASUREMENTSHEET( 
id bigint,
	slNo smallint,
	identifier character(1),  
	remarks varchar(1024),
	no bigint,
	length numeric (13,2),
	width numeric (13,2),
	depthOrHeight numeric (13,2),
	quantity numeric (13,2),
	activityid bigint ,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);
alter table EGW_MEASUREMENTSHEET add constraint pk_EGW_MEASUREMENTSHEET primary key (id);
alter table EGW_MEASUREMENTSHEET add constraint fk_EGW_MEASUREMENTSHEET_activityid  FOREIGN KEY (activityid) REFERENCES EGW_ESTIMATE_ACTIVITY(id);
create sequence seq_EGW_MEASUREMENTSHEET;
