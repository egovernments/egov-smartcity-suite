create table pt_extnasmtbal_tbl (
	i_slno integer not null,
	dt_addaltdt date,
	d_crnpt numeric(18,2),
	d_crned numeric(18,2),
	d_crnlcs numeric(18,2),
	d_crnuauthcnstplty numeric(18,2),
	i_asmtno integer not null,
	i_ulbobjid smallint NOT NULL,
	c_delflag character(1) NOT NULL DEFAULT 'N'::bpchar,
	ts_dttm timestamp without time zone NOT NULL,
	i_usrid integer NOT NULL,
	ismigrated character varying(1) DEFAULT 'N'::character varying,
	error character varying(512),
	incrementalno bigint default 0
);
