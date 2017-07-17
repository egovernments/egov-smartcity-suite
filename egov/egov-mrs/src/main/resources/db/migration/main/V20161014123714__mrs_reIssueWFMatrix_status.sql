------------------ egmrs_reissue -------------------------

ALTER TABLE egmrs_reissue drop COLUMN certificateissued;

ALTER TABLE egmrs_reissue ALTER COLUMN status type bigint using cast(status as bigint);

ALTER TABLE ONLY egmrs_reissue
    ADD CONSTRAINT fk_reissue_status FOREIGN KEY (status) REFERENCES egw_status(id);

ALTER TABLE egmrs_reissue ADD COLUMN isactive boolean DEFAULT false;

------------------ wf_matrix -------------------------

update eg_wf_matrix set validactions='Forward,Print Rejection Certificate,Cancel ReIssue' where objecttype='ReIssue' and  validactions='Forward,Print Rejection Certificate, Close ReIssue' and additionalrule='MARRIAGE REGISTRATION';

update eg_wf_matrix set nextaction='Certificate Print Pending' where nextstate='Assistant Engineer Approved' and nextaction='Fee Collection Pending' and nextdesignation='Revenue Clerk' and nextstatus='Assistant Engineer Approved' and objecttype='ReIssue' and additionalrule='MARRIAGE REGISTRATION';

Delete from eg_wf_matrix where objecttype='ReIssue' and currentstate='Assistant Engineer Approved' and additionalrule='MARRIAGE REGISTRATION' and nextstate='Fee Collected' and nextaction='Certificate Print Pending';

update eg_wf_matrix set pendingactions='Certificate Print Pending', currentstate='Assistant Engineer Approved' where currentstate='Fee Collected' and pendingactions is null and nextstate='END' and objecttype='ReIssue' and additionalrule='MARRIAGE REGISTRATION';

---------------------- eg_roleaction -----------------------------

insert into eg_roleaction values ((select id from eg_role where name='Marriage Registration Creator'),(select id from eg_action where name='AjaxDesignationsByDepartment'));

insert into eg_roleaction values ((select id from eg_role where name='Marriage Registration Creator'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'CreateReIssue'));

