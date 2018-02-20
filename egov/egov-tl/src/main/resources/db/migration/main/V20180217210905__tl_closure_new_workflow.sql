-- closure forward/appprove-- 

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'PUBLIC HEALTH AND SANITATION','TradeLicense','Start','',
'Closure Initiated Pending','','EXTERNALCLOSUREAPPLICATION','Closure Initiated','JA/SA Approval Pending',
'Junior Assistant,Senior Assistant','Closure Initiated','Save',null,null,'2015-04-01',
'2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Closure Initiated','',
'JA/SA Approval Pending','Junior Assistant,Senior Assistant','CLOSURELICENSE',
'JA/SA Approved','Sanitory Inspector Approval Pending','Sanitary inspector','JA/SA Approved',
'Forward,Reassign,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Start','',
'JA/SA Approval Pending','Junior Assistant,Senior Assistant','CLOSURELICENSE',
'JA/SA Approved','Sanitory Inspector Approval Pending','Sanitary inspector','JA/SA Approved',
'Forward',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Rejected','',
'','Junior Assistant,Senior Assistant','CLOSURELICENSE',
'JA/SA Approved','Sanitory Inspector Approval Pending','Sanitary inspector','JA/SA Approved',
'Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

--JA Approved / SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','JA/SA Approved','',
'Sanitory Inspector Approval Pending','Sanitary inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'Sanitory Inspector Approval','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

-- SI Approved/ SS Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved','',
'SS Verification Pending','Sanitary Supervisor','CLOSURELICENSE','Sanitary Supervisor Verified','',
'Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner','',
'Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SI Approved / AMOH Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved','',
'Assistant Medical Officer of Health Approval Pending','Assistant Medical Officer of Health','CLOSURELICENSE',
'Assistant Medical Officer of Health Approved','',
'Municipal Health Officer,Chief Medical Officer of Health,Commissioner','','Forward,Approve,Reject',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SI Approved / MHO Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved','',
'Municipal Health Officer Approval Pending','Municipal Health Officer','CLOSURELICENSE','Municipal Health Officer Approved',
'','Chief Medical Officer of Health,Commissioner','','Forward,Approve,Reject',null,null,
'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SI Approved / CMOH Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved','',
'Chief Medical Officer of Health Approval Pending','Chief Medical Officer of Health','CLOSURELICENSE',
'Chief Medical Officer of Health Approved','','Commissioner','',
'Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SI Approved / Commissioner Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved','',
'Commissioner Approval Pending','Commissioner','CLOSURELICENSE','END','END','Commissioner','',
'Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SS Approved / AMOH Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Assistant Medical Officer of Health Approval Pending','Assistant Medical Officer of Health','CLOSURELICENSE',
'Assistant Medical Officer of Health Approved','','Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SS Approved / CMOH Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Chief Medical Officer of Health Approval Pending','Chief Medical Officer of Health','CLOSURELICENSE',
'Chief Medical Officer of Health Approved','','Commissioner','','Forward,Approve,Reject',null,null,
'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SS Approved / MHO Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Municipal Health Officer Approval Pending','Municipal Health Officer','CLOSURELICENSE',
'Municipal Health Officer Approved','','Chief Medical Officer of Health,Commissioner','',
'Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--SS Forward / Commissioner Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSE','END','END','Commissioner','',
'Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--AMOH Forward / CMOH Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','Chief Medical Officer of Health Approval Pending','Chief Medical Officer of Health','CLOSURELICENSE',
'Chief Medical Officer of Health Approved','','Commissioner','',
'Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--AMOH Forward / MHO Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','Municipal Health Officer Approval Pending','Municipal Health Officer','CLOSURELICENSE',
'Municipal Health Officer Approved','','Chief Medical Officer of Health,Commissioner','',
'Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--AMOH Forward / Commissioner Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSE','END','END','Commissioner',
'','Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--MHO Forward / CMHO Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense',
'Municipal Health Officer Approved','','Chief Medical Officer of Health Approval Pending',
'Chief Medical Officer of Health','CLOSURELICENSE','Chief Medical Officer of Health Approved','','Commissioner',
'','Forward,Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--MHO Forward / Commissoner Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Municipal Health Officer Approved',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSE','END','END','Commissioner',
'','Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

--CMHO Forward / Commissioner Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense',
'Chief Medical Officer of Health Approved','','Commissioner Approval Pending','Commissioner','CLOSURELICENSE',
'END','END','Commissioner','','Approve,Reject',null,null,'2015-04-01','2099-04-01',0,'all',
false,true,null,null);

-- SS Reject / SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Rejected','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

--MHO Reject / SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Municipal Health Officer Rejected','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

-- AMOH Reject / SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Rejected','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

-- CMOH Reject / SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Chief Medical Officer of Health Rejected','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

-- Commissioner Reject /SI Inbox
Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Commissioner Rejected','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSE','Sanitory Inspector Approved','',
'Sanitary Supervisor,Assistant Medical Officer of Health,Municipal Health Officer,Chief Medical Officer of Health,Commissioner',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

-- Closure Rejection--

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','JA/SA Approved','',
'Sanitory Inspector Approval Pending','Sanitary Inspector','CLOSURELICENSEREJECT','Sanitory Inspector Rejected',
'JA/SA Approval Pending','Senior Assistant,Junior Assistant','','Forward,Cancel',null,null,'2015-04-01',
'2099-04-01',0,'all',true,true,null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved',
'','SS Verification Pending','Sanitary Supervisor','CLOSURELICENSEREJECT','Sanitary Supervisor Rejected',
'Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',
false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Rejected',
'','','Sanitary Inspector','CLOSURELICENSEREJECT','Sanitory Inspector Rejected','JA/SA Approval Pending',
'Senior Assistant,Junior Assistant','','Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,
null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved',
'','Assistant Medical Officer of Health Approval Pending','Assistant Medical Officer of Health','CLOSURELICENSEREJECT',
'Assistant Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense',
'Assistant Medical Officer of Health Rejected','','','Sanitary Inspector','CLOSURELICENSEREJECT',
'Sanitory Inspector Rejected','JA/SA Approval Pending','Senior Assistant,Junior Assistant','',
'Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved',
'','Municipal Health Officer Approval Pending','Municipal Health Officer','CLOSURELICENSEREJECT',
'Municipal Health Officer Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,
'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Municipal Health Officer Rejected',
'','','Sanitary Inspector','CLOSURELICENSEREJECT','Sanitory Inspector Rejected','JA/SA Approval Pending',
'Senior Assistant,Junior Assistant','','Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,
null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved',
'','Chief Medical Officer of Health Approval Pending','Chief Medical Officer of Health','CLOSURELICENSEREJECT',
'Chief Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Chief Medical Officer of Health Rejected',
'','','Sanitary Inspector','CLOSURELICENSEREJECT','Sanitory Inspector Rejected','JA/SA Approval Pending',
'Senior Assistant,Junior Assistant','','Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,
null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitory Inspector Approved',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected',
'Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',
false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Commissioner Rejected','','',
'Sanitary Inspector','CLOSURELICENSEREJECT','Sanitory Inspector Rejected','JA/SA Approval Pending',
'Senior Assistant,Junior Assistant','','Forward,Cancel',null,null,'2015-04-01','2099-04-01',0,'all',true,true,
null,true);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected',
'Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',
true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Chief Medical Officer of Health Approved',
'','','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Municipal Health Officer Approved',
'','','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Municipal Health Officer Approved',
'','','Chief Medical Officer of Health','CLOSURELICENSEREJECT','Chief Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','','Chief Medical Officer of Health','CLOSURELICENSEREJECT','Chief Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Assistant Medical Officer of Health Approved',
'','','Municipal Health Officer','CLOSURELICENSEREJECT','Municipal Health Officer Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector',
'','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Assistant Medical Officer of Health Approval Pending','Assistant Medical Officer of Health','CLOSURELICENSEREJECT',
'Assistant Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Municipal Health Officer Approval Pending','Municipal Health Officer','CLOSURELICENSEREJECT',
'Municipal Health Officer Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,
'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Chief Medical Officer of Health Approval Pending','Chief Medical Officer of Health','CLOSURELICENSEREJECT',
'Chief Medical Officer of Health Rejected','Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',
null,null,'2015-04-01','2099-04-01',0,'all',false,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Sanitary Supervisor Verified',
'','Commissioner Approval Pending','Commissioner','CLOSURELICENSEREJECT','Commissioner Rejected',
'Sanitory Inspector Approval Pending','Sanitary Inspector','','Forward,Reject',null,null,'2015-04-01','2099-04-01',0,'all',
false,true,null,null);
