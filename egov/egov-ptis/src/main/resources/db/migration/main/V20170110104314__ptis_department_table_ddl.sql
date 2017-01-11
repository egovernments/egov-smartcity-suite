--New table for property department

CREATE TABLE egpt_department (
    id                bigint primary key not null,                   
    name              character varying(200) not null,  
    category          character varying(10) not null,
    createddate       timestamp without time zone,
    code              character varying(20),     
    createdby         bigint,                     
    lastmodifiedby    bigint,                     
    lastmodifieddate  timestamp without time zone,
    version           bigint                     

);

create sequence seq_egpt_department;

-- dependency of property department on property detail
alter table egpt_property_detail add column pt_department bigint;
alter table egpt_property_detail add constraint fk_id_egpt_department foreign key (pt_department) references egpt_department (id);

