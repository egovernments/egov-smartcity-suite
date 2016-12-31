----delete existing WF Matrix----
delete from eg_wf_matrix where objecttype='RevisionPetition';

---------RP Commom Matrix -------
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:NEW','','','','REVISION PETITION','RP:CREATED','FORWARD TO COMMISSIONER APPROVAL','Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','CREATED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:CREATED','','FORWARD TO COMMISSIONER APPROVAL','','REVISION PETITION','RP:Registration','ADD HEARING DATE','Commissioner','CREATED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Registration','','ADD HEARING DATE','','REVISION PETITION','RP:Hearing date fixed','Generate Hearing Notice','Assistant','HEARING DATE FIXED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Rejected','','','','REVISION PETITION','RP:Inspection completed','Verify inspection details','Revenue officer','INSPECTION COMPLETED','Save,Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Registered','','ADD HEARING DATE','','REVISION PETITION','RP:Hearing date fixed','Generate Hearing Notice','Commissioner','','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Hearing date fixed','','Generate Hearing Notice','','REVISION PETITION','RP:Hearing completed','Record inspection details','UD Revenue Inspector','HEARING COMPLETED','Forward,Print HearingNotice',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Hearing completed','','Record inspection details','UD Revenue Inspector','REVISION PETITION','RP:Inspection completed','Verify inspection details','Revenue officer','INSPECTION COMPLETED','Save,Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection completed','','Verify inspection details','Revenue officer','REVISION PETITION','RP:Inspection verified','Final approval','Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','INSPECTION VERIFY','Forward,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------New WF 
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection verified','','Assistant Commissioner Approval Pending','Assistant Commissioner','REVISION PETITION','RP:Assistant Commissioner Approved','Print Endoresement','Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','Assistant Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection verified','','Zonal Commissioner Approval Pending','Zonal Commissioner','REVISION PETITION','RP:Zonal Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','Zonal Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection verified','','Deputy Commissioner Approval Pending','Deputy Commissioner','REVISION PETITION','RP:Deputy Commissioner Approved','Print Endoresement','Additional Commissioner,Commissioner','Deputy Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection verified','','Additional Commissioner Approval Pending','Additional Commissioner','REVISION PETITION','RP:Additional Commissioner Approved','Print Endoresement','Commissioner','Additional Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Inspection verified','','Commissioner Approval Pending','Commissioner','REVISION PETITION','RP:Commissioner Approved','Print Endoresement','Commissioner','Commissioner Approved','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-----Asst COMM Forward 
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Forwarded','','Zonal Commissioner Approval Pending','Zonal Commissioner','REVISION PETITION','RP:Zonal Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Forwarded','','Deputy Commissioner Approval Pending','Deputy Commissioner','REVISION PETITION','RP:Deputy Commissioner Approved','Print Endoresement','Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','REVISION PETITION','RP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','REVISION PETITION','RP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-----------Zonal COMM forward

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Forwarded','','Deputy Commissioner Approval Pending','Deputy Commissioner','REVISION PETITION','RP:Deputy Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','REVISION PETITION','RP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','REVISION PETITION','RP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-------------Deputy COMM Forward

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','REVISION PETITION','RP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','REVISION PETITION','RP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------Additional COMM forward
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Additional Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','REVISION PETITION','RP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------commaon COMM Approve
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Print Endoresement','Assistant Commissioner','REVISION PETITION','RP:Print Endoresement','Digital Signature Pending','Assistant Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Approved','','Print Endoresement','Zonal Commissioner','REVISION PETITION','RP:Print Endoresement','Digital Signature Pending','Zonal Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Approved','','Print Endoresement','Deputy Commissioner','REVISION PETITION','RP:Print Endoresement','Digital Signature Pending','Deputy Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Additional Commissioner Approved','','Print Endoresement','Additional Commissioner','REVISION PETITION','RP:Print Endoresement','Digital Signature Pending','Additional Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Commissioner Approved','','Print Endoresement','Commissioner','REVISION PETITION','RP:Print Endoresement','Digital Signature Pending','Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);
---------------Asst -Dig.Sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Digital Signature Pending','Assistant Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Digital Signature Pending','Zonal Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Digital Signature Pending','Additional Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Assistant Commissioner Approved','','Digital Signature Pending','Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);
---------------Zonal Dig.Sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Approved','','Digital Signature Pending','Zonal Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Approved','','Digital Signature Pending','Additional Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Zonal Commissioner Approved','','Digital Signature Pending','Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);
--------------Deputy
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Approved','','Digital Signature Pending','Additional Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Deputy Commissioner Approved','','Digital Signature Pending','Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

-------Additional
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Additional Commissioner Approved','','Digital Signature Pending','Additional Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Additional Commissioner Approved','','Digital Signature Pending','Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

------Comm dig.sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Commissioner Approved','','Digital Signature Pending','Commissioner','REVISION PETITION','RP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

---------------special notice
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','RP:Digitally Signed','','Print Special Notice','','REVISION PETITION','RP:END','END','ASSISTANT','','Print Special Notice',null,null,'2015-04-01','2099-04-01',0);


---------GRP Commom Matrix ----------
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:NEW','','','','GENERAL REVISION PETITION','GRP:CREATED','FORWARD TO COMMISSIONER APPROVAL','Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','CREATED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:CREATED','','FORWARD TO COMMISSIONER APPROVAL','','GENERAL REVISION PETITION','GRP:Registration','ADD HEARING DATE','Commissioner','CREATED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Registration','','ADD HEARING DATE','','GENERAL REVISION PETITION','GRP:Hearing date fixed','Generate Hearing Notice','Assistant','HEARING DATE FIXED','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Rejected','','','','GENERAL REVISION PETITION','GRP:Inspection completed','Verify inspection details','Revenue officer','INSPECTION COMPLETED','Save,Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Registered','','ADD HEARING DATE','','GENERAL REVISION PETITION','GRP:Hearing date fixed','Generate Hearing Notice','Commissioner','','Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Hearing date fixed','','Generate Hearing Notice','','GENERAL REVISION PETITION','GRP:Hearing completed','Record inspection details','UD Revenue Inspector','HEARING COMPLETED','Forward,Print HearingNotice',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Hearing completed','','Record inspection details','UD Revenue Inspector','GENERAL REVISION PETITION','GRP:Inspection completed','Verify inspection details','Revenue officer','INSPECTION COMPLETED','Save,Forward',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection completed','','Verify inspection details','Revenue officer','GENERAL REVISION PETITION','GRP:Inspection verified','Final approval','Assistant Commissioner,Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','INSPECTION VERIFY','Forward,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------New WF 
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection verified','','Assistant Commissioner Approval Pending','Assistant Commissioner','GENERAL REVISION PETITION','GRP:Assistant Commissioner Approved','Print Endoresement','Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','Assistant Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection verified','','Zonal Commissioner Approval Pending','Zonal Commissioner','GENERAL REVISION PETITION','GRP:Zonal Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','Zonal Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection verified','','Deputy Commissioner Approval Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Deputy Commissioner Approved','Print Endoresement','Additional Commissioner,Commissioner','Deputy Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection verified','','Additional Commissioner Approval Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Additional Commissioner Approved','Print Endoresement','Commissioner','Additional Commissioner Approved','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Inspection verified','','Commissioner Approval Pending','Commissioner','GENERAL REVISION PETITION','GRP:Commissioner Approved','Print Endoresement','Commissioner','Commissioner Approved','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-----Asst COMM Forward 
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Forwarded','','Zonal Commissioner Approval Pending','Zonal Commissioner','GENERAL REVISION PETITION','GRP:Zonal Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Forwarded','','Deputy Commissioner Approval Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Deputy Commissioner Approved','Print Endoresement','Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','GENERAL REVISION PETITION','GRP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-----------Zonal COMM forward

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Forwarded','','Deputy Commissioner Approval Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Deputy Commissioner Approved','Print Endoresement','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','GENERAL REVISION PETITION','GRP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

-------------Deputy COMM Forward

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Forwarded','','Additional Commissioner Approval Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Additional Commissioner Approved','Print Endoresement','Commissioner','','Forward,Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','GENERAL REVISION PETITION','GRP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------Additional COMM forward
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Additional Commissioner Forwarded','','Commissioner Approval Pending','Commissioner','GENERAL REVISION PETITION','GRP:Commissioner Approved','Print Endoresement','Commissioner','','Approve,Reject Inspection',null,null,'2015-04-01','2099-04-01',0);

---------commaon COMM Approve
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Print Endoresement','Assistant Commissioner','GENERAL REVISION PETITION','GRP:Print Endoresement','Digital Signature Pending','Assistant Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Approved','','Print Endoresement','Zonal Commissioner','GENERAL REVISION PETITION','GRP:Print Endoresement','Digital Signature Pending','Zonal Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Approved','','Print Endoresement','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Print Endoresement','Digital Signature Pending','Deputy Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Additional Commissioner Approved','','Print Endoresement','Additional Commissioner','GENERAL REVISION PETITION','GRP:Print Endoresement','Digital Signature Pending','Additional Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Commissioner Approved','','Print Endoresement','Commissioner','GENERAL REVISION PETITION','GRP:Print Endoresement','Digital Signature Pending','Commissioner','','Print Endoresement',null,null,'2015-04-01','2099-04-01',0);
---------------Asst -Dig.Sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Digital Signature Pending','Assistant Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Zonal Commissioner,Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Digital Signature Pending','Zonal Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Digital Signature Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Assistant Commissioner Approved','','Digital Signature Pending','Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);
---------------Zonal Dig.Sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Approved','','Digital Signature Pending','Zonal Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Deputy Commissioner,Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Approved','','Digital Signature Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Zonal Commissioner Approved','','Digital Signature Pending','Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);
--------------Deputy
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Approved','','Digital Signature Pending','Deputy Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Additional Commissioner,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Approved','','Digital Signature Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Deputy Commissioner Approved','','Digital Signature Pending','Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

-------Additional
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Additional Commissioner Approved','','Digital Signature Pending','Additional Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Additional Commissioner Approved','','Digital Signature Pending','Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

------Comm dig.sign
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Commissioner Approved','','Digital Signature Pending','Commissioner','GENERAL REVISION PETITION','GRP:Digitally Signed','Digital Signature Pending','','','Preview,Sign',null,null,'2015-04-01','2099-04-01',0);

---------------special notice
insert into eg_wf_matrix values (nextval('seq_eg_wf_matrix'),'ANY','RevisionPetition','GRP:Digitally Signed','','Print Special Notice','','GENERAL REVISION PETITION','GRP:END','END','ASSISTANT','','Print Special Notice',null,null,'2015-04-01','2099-04-01',0);









