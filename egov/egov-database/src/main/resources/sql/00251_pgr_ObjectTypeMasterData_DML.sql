
create sequence seq_egeis_position_hierarchy;

insert into eg_object_type(id,type,description,lastmodifieddate) values
(nextval('SEQ_OBJECT_TYPE'), 'Complaint','Complaint',now());
