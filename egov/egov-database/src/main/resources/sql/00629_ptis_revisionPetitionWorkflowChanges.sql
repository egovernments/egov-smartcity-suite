update eg_wf_matrix set nextdesignation='Commissioner'
where  objecttype='RevisionPetition'  and currentstate='Revision Petition:NEW';

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'RevisionPetition', 'Revision Petition:Registered', NULL,'ADD HEARING DATE', NULL, NULL,
 'Revision Petition:Hearing date fixed', 'Generate Hearing Notice', 'Commissioner', 'CREATED', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

