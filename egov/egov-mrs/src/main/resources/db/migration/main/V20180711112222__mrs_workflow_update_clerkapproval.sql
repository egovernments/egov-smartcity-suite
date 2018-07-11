update eg_wf_matrix set nextaction ='Clerk Approval Pending' where nextaction ='Revenue Clerk Approval Pending' and objecttype in('MarriageRegistration','ReIssue');

update eg_wf_matrix set currentstate  ='Clerk Approved' where currentstate ='Revenue Clerk Approved' and objecttype in('MarriageRegistration','ReIssue');

update eg_wf_matrix set pendingactions ='Clerk Approval Pending' where pendingactions ='Revenue Clerk Approval Pending' and objecttype in('MarriageRegistration','ReIssue');

update eg_wf_matrix set nextstate='Clerk Approved' where nextstate='Revenue Clerk Approved' and objecttype in('MarriageRegistration','ReIssue');