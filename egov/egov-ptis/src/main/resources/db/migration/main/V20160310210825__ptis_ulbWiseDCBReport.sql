
create table EGPT_ULBWISE_DCBREPORT_DETAIL (
ID bigint NOT NULL,
ULBWISEDCB bigint NOT NULL,
totalAccessments  bigint default 0,
CURRENT_DEMAND double precision DEFAULT 0,
ARREARS_DEMAND double precision DEFAULT 0, 
CURRENT_DEMAND_collection double precision DEFAULT 0,
ARREARS_DEMAND_collection double precision DEFAULT 0, 
CURRENT_Penalty double precision DEFAULT 0,
ARREARS_Penalty double precision DEFAULT 0, 
CURRENT_Penalty_collection double precision DEFAULT 0,
ARREARS_Penalty_collection double precision DEFAULT 0, 
category  character varying(50)
);

create table EGPT_ULBWISE_DCBREPORT (
ID bigint NOT NULL,
DISTRICT character varying(50) NOT NULL,
ULBNAME character varying(50) NOT NULL,
ULBCODE character varying(50) NOT NULL,
Commissioner_Name character varying(125) ,
Commissioner_MobileNumber character varying(30) ,
Last_calculated_date Date,
isactive boolean NOT NULL DEFAULT true);
  
alter table EGPT_ULBWISE_DCBREPORT add constraint pk_EGPT_ULBWISE_DCBREPORT primary key (id);
ALTER TABLE ONLY EGPT_ULBWISE_DCBREPORT ADD CONSTRAINT unq_DCBREPORT_ulb  UNIQUE (DISTRICT, ULBNAME);

ALTER TABLE EGPT_ULBWISE_DCBREPORT_DETAIL ADD CONSTRAINT pk_EGPT_ULBWISE_DCBREPORT_DETAIL PRIMARY KEY (id);
ALTER TABLE EGPT_ULBWISE_DCBREPORT_DETAIL ADD CONSTRAINT fk_ulbwisedcb_dtl FOREIGN KEY (ULBWISEDCB) REFERENCES EGPT_ULBWISE_DCBREPORT (id);


create sequence SEQ_ULBWISE_DCBREPORT;
create sequence SEQ_ULBWISE_DCBREPORT_dtl;

