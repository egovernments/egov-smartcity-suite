delete from eg_roleaction_map where actionid in (
select id from eg_action where context_root='egi' and is_enabled=1 and url not like '/controller%'
);

delete from eg_action where context_root='egi' and is_enabled=1 and url not like '/controller%';

delete from eg_roleaction_map where actionid in (
select id  from eg_action where context_root='egi' and is_enabled=0 
and id not in (select id from eg_action where  url like '/controller%' or url like '/login/securityLogin.jsp'
or url like '/j_security_check'
));



delete  from eg_action where context_root='egi' and is_enabled=0 
and id not in (select id from eg_action where  url like '/controller%' or url like '/login/securityLogin.jsp'
or url like '/j_security_check'
);

delete from eg_roleaction_map where actionid in (
select id from eg_action where context_root='pgr' and is_enabled=1 and module_id not in 
(Select id_module from eg_module where module_name in ('PGRComplaints','Pgr Masters') and parentid=(select id_module from eg_module where module_name='PGR')
)
);

delete from eg_action where context_root='pgr' and is_enabled=1 and module_id not in 
(Select id_module from eg_module where module_name in ('PGRComplaints','Pgr Masters') and parentid=(select id_module from eg_module where module_name='PGR')
);


delete from eg_roleaction_map where actionid in (
select id from eg_action where context_root='pgr' and is_enabled=0  and id not in (Select id from eg_action
where (url like '/complaint%' or url like '/view-complaint' or url like '/ajax-%')));

delete  from eg_action where context_root='pgr' and is_enabled=0  and id not in (Select id from eg_action
where (url like '/complaint%' or url like '/view-complaint' or url like '/ajax-%'));

delete from eg_roleaction_map where actionid in ( 
select id  from eg_action where url='/staff/validateComplaintNo.jsp');

delete from eg_action where url='/staff/validateComplaintNo.jsp';


update eg_module set isenabled=0 where module_name='EIS';

