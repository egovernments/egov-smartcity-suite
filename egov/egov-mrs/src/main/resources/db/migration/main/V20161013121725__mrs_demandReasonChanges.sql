update  eg_demand_reason_master set reasonmaster='Marriage Certificate Fee', code='CERTIFICATEISSUE' where module=(SELECT ID FROM EG_module where name='Marriage Registration') and code='REISSUE';
update  eg_demand_reason_master set reasonmaster='Marriage Registration Fee', code='MRGREGISTRATION' where module=(SELECT ID FROM EG_module where name='Marriage Registration') and code='REGISTRATION';
