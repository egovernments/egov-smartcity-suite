delete from eg_roleaction where actionid in(select id from eg_action where name in('Create Budget Group','View Budget Group','Modify Budget Group','Create Budget Definition','Modify Budget Definition'));
delete from eg_action where name in('Create Budget Group','View Budget Group','Modify Budget Group','Create Budget Definition','Modify Budget Definition');
delete from eg_module where name in('Chart Of Account','Budget Definition');

