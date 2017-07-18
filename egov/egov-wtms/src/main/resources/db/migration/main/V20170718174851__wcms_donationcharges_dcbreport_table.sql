create table egwtr_mv_donation_dcb_view (
id bigint not null,
consumernumber character varying(32),
propertyid character varying(32),
applicationnumber character varying(32),
username character varying(256),
address character varying(512),
mobileno character varying(32),
connectiondetailsid bigint,
applicationdate date,
donation_demand double precision,
donation_coll double precision,
donation_balance double precision,
constraint pk_egwtr_mv_donation_dcb_view_id PRIMARY KEY (id)
);

create sequence seq_egwtr_mv_donation_dcb_view;