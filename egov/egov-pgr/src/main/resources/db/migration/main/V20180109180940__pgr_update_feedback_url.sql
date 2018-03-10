
UPDATE eg_action SET url='/complaint/ivrs/feedbackreview' , name='Complaint Feedback Review' where name ='Complaint Quality Review';

UPDATE eg_action SET url='/complaint/ivrs/feedbackreason/create' ,name='Complaint Feedback Reason' where name ='Feedback Reason';

update eg_action set parentmodule =(select id from eg_module  where name ='IVRS') where name='Complaint Feedback Reason';

update eg_action set parentmodule =(select id from eg_module  where name ='IVRS') where name='Complaint Feedback Review';

