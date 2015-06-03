ALTER TABLE eg_address ADD COLUMN id numeric;
ALTER TABLE eg_address ADD PRIMARY KEY (id);
ALTER TABLE eg_address ADD COLUMN "version" bigint default 0;
ALTER TABLE eg_address ALTER "type" type varchar(50);

CREATE TABLE eg_permanent_address( 
 id numeric,
 "version" numeric default 0
);

CREATE TABLE eg_correspondence_address( 
 id numeric,
 "version" numeric default 0
);
