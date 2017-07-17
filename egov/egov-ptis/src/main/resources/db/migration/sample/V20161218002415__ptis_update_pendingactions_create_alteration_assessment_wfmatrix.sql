--updating pendingactions 
update eg_wf_matrix set pendingactions = 'Assistant approval pending' where objecttype = 'PropertyImpl' and additionalrule='NEW ASSESSMENT'
and currentstate='Create:NEW';

update eg_wf_matrix set pendingactions = 'Assistant approval pending' where objecttype = 'PropertyImpl' and additionalrule='ALTER ASSESSMENT'
and currentstate='Alter:NEW';

update eg_wf_matrix set pendingactions = 'Assistant approval pending' where objecttype = 'PropertyImpl' and additionalrule='BIFURCATE ASSESSMENT'
and currentstate='Bifurcate:NEW';

update eg_wf_matrix set pendingactions = 'Assistant approval pending' where objecttype = 'PropertyImpl' and additionalrule='GENERAL REVISION PETITION'
and currentstate='GRP:NEW';