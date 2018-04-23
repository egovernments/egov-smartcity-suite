Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'PUBLIC HEALTH AND SANITATION','TradeLicense','New','','Renewal Initiation Pending',
'','RENEWALWITHOUTFEE','Renewal Initiated','Assistant Approval Pending','Senior Assistant,Junior Assistant','','Save',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Renewal Initiated','','Assistant Approval Pending',
'Senior Assistant,Junior Assistant','RENEWLICENSE','Assistant Approved','SI Approval Pending','Sanitary Inspector','','Forward,Cancel,Generate Provisional Certificate,Reassign',
null,null,'2015-04-01','2099-04-01',0,'adhaarId,mobilePhoneNumber,applicantName,fatherOrSpouseName,emailId,licenseeAddress,tradeArea_weight',
true,false,null,true);

update eg_wf_matrix set pendingactions='SI Approval Pending' where objecttype='TradeLicense' and additionalrule like '%NEWLICENSEREJECT' and pendingactions='' and currentstate like '%Rejected';
