
create table egcncl_councilsequence( 
        id bigint,
	preambleSeqNumber varchar(15)  unique,
	agendaSeqNumber varchar(15)  unique,
	resolutionSeqNumber varchar(15)  unique
	);
	
	create sequence seq_egcncl_councilsequence;
