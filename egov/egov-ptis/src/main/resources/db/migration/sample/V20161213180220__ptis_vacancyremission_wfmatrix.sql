--------------deleting existing wf matrix----------------------------
delete from  eg_wf_matrix where objecttype='VacancyRemission';
delete from eg_wf_matrix  where objecttype='VacancyRemissionApproval';
----------workflow matrix for Vacancy remission create-------------
insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:NEW','Senior Assistant,Junior Assistant','VacancyRemission:Assistant Forwarded','Commissioner Forward Pending','Commissioner','Assistant Forwarded','Forward','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:Assistant Forwarded','Commissioner','VacancyRemission:Commissioner Forwarded','Assistant Approval Pending','Senior Assistant,Junior Assistant','Commissioner Forwarded','Forward,Reject','2015-04-01','2099-04-01',0);


insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:Commissioner Forwarded','Senior Assistant,Junior Assistant','VacancyRemission:Assistant Approved','Bill Collector Approval Pending','Bill Collector','Assistant Approved','Forward','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:Assistant Approved','Bill Collector','VacancyRemission:Bill Collector Approved','UD Revenue Inspector Approval Pending','UD Revenue Inspector','UD Revenue Inspector Approved','Forward,Reject','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:Bill Collector Approved','UD Revenue Inspector','VacancyRemission:UD Revenue Inspector Approved','END','','','Approve,Reject','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','VacancyRemission:Rejected','Senior Assistant,Junior Assistant','VacancyRemission:END','END','','','Generate Notice','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemission','Created','NULL','VacancyRemission:NEW','Assistant approval pending','Senior Assistant,Junior Assistant','Assistant Approved','Forward','2015-04-01','2099-04-01',0);


-----------Work flow matrix for VR Final Approval----------------
insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','NEW','UD Revenue Inspector','UD Revenue Inspector Approved','Revenue Officer Approval Pending','Revenue Officer','UD Revenue Inspecto Approved','Forward','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','UD Revenue Inspector Approved','Revenue Officer','Revenue Officer Approved','Commissioner Approval Pending','Commissioner','Revenue Officer Approved','Forward,Reject','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','Revenue Officer Approved','Commissioner','Commissioner Approved','Digital Signature Pending','Commissioner','Commissioner Approved','Reject,Approve','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','Commissioner Approved','Commissioner','Digitally Signed','Notice Print Pending','','','Preview,Sign','2015-04-01','2099-04-01',0);
insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','Digitally Signed','Senior Assistant,Junior Assistant','END','Notice Print Pending','','','Generate Notice','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','Rejected','UD Revenue Inspector','UD Revenue Inspector Approved','Revenue Officer Approval Pending','Revenue Officer','Revenue Officer Approved','Forward,Reject','2015-04-01','2099-04-01',0);

insert into eg_wf_matrix(id,department,objecttype,currentstate,currentdesignation,nextstate,nextaction,nextdesignation,
nextstatus,validactions,fromdate,todate,version) values (nextval('seq_eg_wf_matrix'),'ANY','VacancyRemissionApproval','Revenue Inspector Rejected','Senior Assistant,Junior Assistant','','Rejection Notice Generation Pending','','','Forward,Reject','2015-04-01','2099-04-01',0);
