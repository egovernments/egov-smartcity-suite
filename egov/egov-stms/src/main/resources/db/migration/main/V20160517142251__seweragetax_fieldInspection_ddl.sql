ALTER TABLE egswtax_fieldinspection_details ADD COLUMN pipelength numeric not null;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN screwsize  bigint not null;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN distance  numeric not null;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN roaddigging boolean;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN roadlength  numeric;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN roadowner  character varying(128);
