alter table eg_wf_matrix alter column currentstate type character varying(100);
alter table eg_wf_matrix alter column nextstate type character varying(100);

update eg_wf_matrix  set additionalrule = 'NEW ASSESSMENT' where objecttype = 'PropertyImpl' and additionalrule is null;
update eg_wf_matrix set currentstate = 'Alter' || ':' || currentstate, nextstate = 'Alter' || ':' || nextstate where objecttype = 'PropertyImpl' and additionalrule = 'ALTER ASSESSMENT';
update eg_wf_matrix set currentstate = 'New' || ':' || currentstate, nextstate = 'New' || ':' || nextstate where objecttype = 'PropertyImpl' and additionalrule = 'NEW ASSESSMENT';
update eg_wf_matrix set currentdesignation = 'Revenue Clerk' where currentdesignation = 'Operator' and objecttype = 'PropertyImpl';
update eg_wf_matrix set currentdesignation = 'Revenue Clerk' where currentdesignation = 'Opertor' and objecttype = 'PropertyImpl';
update eg_wf_matrix set nextdesignation = 'Revenue Clerk' where nextdesignation = 'Operator' and objecttype = 'PropertyImpl';
update eg_wf_matrix set currentstate = 'Alter:Revenue Clerk Approved' where currentstate = 'Alter:Operator Approved';
update eg_wf_matrix set currentstate = 'New:Revenue Clerk Approved' where currentstate = 'New:Operator Approved';
update eg_wf_matrix set nextstate = 'Alter:Revenue Clerk Approved' where nextstate = 'Alter:Operator Approved';
update eg_wf_matrix set nextstate = 'New:Revenue Clerk Approved' where nextstate = 'New:Operator Approved';
update eg_wf_matrix set nextaction = 'Revenue Clerk Approved' where nextaction = 'Operator Approved';
update eg_wf_matrix set nextstatus = 'Revenue Clerk Approved' where nextstatus = 'Operator Approved';

update eg_wf_types set displayname = 'Assessment' where type = 'PropertyImpl';

--rollback update eg_wf_types set displayname = 'Property' where type = 'PropertyImpl';
--rollback update eg_wf_matrix set nextstatus = 'Operator Approved' where nextstatus = 'Revenue Clerk Approved';
--rollback update eg_wf_matrix set nextaction = 'Operator Approved' where nextaction = 'Revenue Clerk Approved';
--rollback update eg_wf_matrix set nextstate = 'New:Operator Approved' where nextstate = 'New:Revenue Clerk Approved';
--rollback update eg_wf_matrix set nextstate = 'Alter:Operator Approved' where nextstate = 'Alter:Revenue Clerk Approved';
--rollback update eg_wf_matrix set currentstate = 'New:Operator Approved' where currentstate = 'New:Revenue Clerk Approved';
--rollback update eg_wf_matrix set currentstate = 'Alter:Operator Approved' where currentstate = 'Alter:Revenue Clerk Approved';
--rollback update eg_wf_matrix set nextdesignation = 'Operator' where nextdesignation = 'Revenue Clerk' and objecttype = 'PropertyImpl';
--rollback update eg_wf_matrix set currentdesignation = 'Operator' where currentdesignation = 'Revenue Clerk' and objecttype = 'PropertyImpl';
--rollback update eg_wf_matrix set currentstate = substring(currentstate, position(':' in currentstate) + 1), nextstate = substring(nextstate, position(':' in currentstate) + 1) where objecttype = 'PropertyImpl' and additionalrule = 'NEW ASSESSMENT';
--rollback update eg_wf_matrix set currentstate = substring(currentstate, position(':' in currentstate) + 1), nextstate = substring(nextstate, position(':' in currentstate) + 1) where objecttype = 'PropertyImpl' and additionalrule = 'ALTER ASSESSMENT' 
--rollback update eg_wf_matrix  set additionalrule = null where objecttype = 'PropertyImpl' and additionalrule = 'NEW ASSESSMENT';

--rollback alter table eg_wf_matrix alter column currentstate type character varying(30);
--rollback alter table eg_wf_matrix alter column nextstate type character varying(30);