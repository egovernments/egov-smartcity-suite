Create table egcncl_smsdetails( 
	id bigint,
	smsSentDate timestamp  not null,
	smsContent varchar(500),
	meeting bigint not null,
	createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
	lastmodifieddate timestamp without time zone,
	createdby bigint NOT NULL,
	lastmodifiedby bigint,
	version bigint DEFAULT 0
);

alter table egcncl_smsdetails add constraint pk_egcncl_smsdetails primary key (id);
alter table egcncl_smsdetails add constraint fk_egcncl_smsdetails FOREIGN KEY (meeting)  REFERENCES egcncl_meeting (id) ;


create sequence seq_egcncl_smsDetails;