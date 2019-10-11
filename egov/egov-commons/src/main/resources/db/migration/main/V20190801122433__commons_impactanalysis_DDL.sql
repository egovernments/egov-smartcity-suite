drop table if exists eg_impactanalysis;
create table eg_impactanalysis
(
  id bigint NOT NULL,
  asondate timestamp without time zone,
  receiptcount bigint,
  receiptamount bigint,
  complaintcount bigint,
  vouchercount bigint,
  applicationcount bigint,
  citizenRegistered bigint,
  employeeRegistered bigint,
  agreementcount bigint,
  CONSTRAINT pk_eg_impactanalysis PRIMARY KEY (id)
);

create sequence seq_eg_impactanalysis 
start with 1
increment by 1;

create index idx_eg_impactanalysis on eg_impactanalysis(asondate);