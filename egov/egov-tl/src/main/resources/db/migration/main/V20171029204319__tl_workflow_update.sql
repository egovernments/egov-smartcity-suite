Insert into egtl_mstr_status values (nextval('seq_egtl_mstr_status'),'Collection Pending',true,'COLLECTIONPENDING',9,0);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Second Level Collection Paid','',
'Digital Signature Pending','Chief Medical Officer of Health','NEWLICENSE','Second Level Collection Paid','','Commissioner','',
'Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0,'parentBoundary,boundary',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Second Level Collection Paid',''
,'Digital Signature Pending','Assistant Medical Officer of Health','NEWLICENSE','Second Level Collection Paid','',
'Municipal Health Officer,Chief Medical Officer of Health,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0,'parentBoundary,boundary',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Second Level Collection Paid','',
'Digital Signature Pending','Chief Medical Officer of Health','RENEWLICENSE','Second Level Collection Paid','','Commissioner','',
'Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0,'parentBoundary,boundary',true,true,null,null);

Insert into eg_wf_matrix values(nextval('seq_eg_wf_matrix'),'ANY','TradeLicense','Second Level Collection Paid',''
,'Digital Signature Pending','Assistant Medical Officer of Health','RENEWLICENSE','Second Level Collection Paid','',
'Municipal Health Officer,Chief Medical Officer of Health,Commissioner','','Forward,Preview,Sign',null,null,'2015-04-01','2099-04-01',0,'parentBoundary,boundary',true,true,null,null);

update eg_wf_matrix set nextdesignation ='Municipal Health Officer,Chief Medical Officer of Health,Commissioner'  where objecttype='TradeLicense' and currentstate='Assistant Medical Officer of Health Approved' and additionalrule like'%NEWLICENSE' and currentdesignation='Assistant Medical Officer of Health';

update eg_wf_matrix set rejectenabled=true where additionalrule='RENEWLICENSEREJECT' and currentstate='Sanitary Inspector Approved' and currentdesignation='Assistant Medical Officer of Health';

update eg_wf_matrix set nextaction='Digital Signature Pending' where additionalrule like '%NEWLICENSE' AND CURRENTSTATE='Sanitary Inspector Approved' AND CURRENTDESIGNATION in ('Assistant Medical Officer of Health','Municipal Health Officer','Chief Medical Officer of Health','Commissioner'); 
