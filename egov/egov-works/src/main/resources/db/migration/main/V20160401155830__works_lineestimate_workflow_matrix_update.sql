-----------Update script to show Technical Sanctioned status in history---------------
update eg_wf_matrix SET nextstate = 'Techincal sanctioned' where id = (select id from eg_wf_matrix where currentstate = 'Admin sanctioned' and objecttype='LineEstimate');

--rollback update eg_wf_matrix SET nextstate = 'END' where id = (select id from eg_wf_matrix where currentstate = 'Admin sanctioned' and objecttype='LineEstimate');