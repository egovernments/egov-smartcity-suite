--Vacant Land Plot Area table

CREATE TABLE egpt_vlt_plot_area (
    id                bigint primary key not null,                   
    name              character varying(200) not null,  
    code              character varying(20),     
    createddate       timestamp without time zone,
    createdby         bigint,                     
    lastmodifiedby    bigint,                     
    lastmodifieddate  timestamp without time zone,
    version           numeric default 0                  

);
create sequence seq_egpt_vlt_plot_area;

--Property Detail link
alter table egpt_property_detail add column vlt_plot_area bigint;
alter table egpt_property_detail add constraint fk_id_egpt_vlt_plot_area foreign key (vlt_plot_area) references egpt_vlt_plot_area (id);

--Layout Approval Authority table

CREATE TABLE egpt_layout_approval_authority (
    id                bigint primary key not null,                   
    name              character varying(200) not null,  
    code              character varying(20),     
    createddate       timestamp without time zone,
    createdby         bigint,                     
    lastmodifiedby    bigint,                     
    lastmodifieddate  timestamp without time zone,
    version           numeric default 0                 

);
create sequence seq_egpt_layout_approval_authority;

--Property Detail link
alter table egpt_property_detail add column layout_approval_authority bigint;
alter table egpt_property_detail add constraint fk_id_egpt_layout_approval_authority foreign key (layout_approval_authority) references egpt_layout_approval_authority (id);

--New fields for Property Detail
alter table egpt_property_detail add column layout_permit_number varchar;
alter table egpt_property_detail add column layout_permit_date date;