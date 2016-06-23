update EG_ACTION set url='/searchletterofacceptance/setloaofflinestatus' where name='WorksSearchLOAToSetOfflineStattus' and contextroot='egworks';
update EG_ACTION set displayname='Offline Status for Letter Of Acceptance' where name='WorksSearchLOAToSetOfflineStattus' and contextroot='egworks';
update EG_ACTION set url='/letterofacceptance/ajaxsearch-loanumber' where name='AjaxWorkOrderNumberForMilestone' and contextroot='egworks';
update EG_ACTION set url='/letterofacceptance/ajaxsearch-loatosetofflinestatus' where name='LOASearchResultToSetOfflineStattus' and contextroot='egworks'; 
update EG_ACTION set url='/letterofacceptance/ajaxestimatenumbers-loa' where name='LOAEstimateNumbersForSetOfflineStattus' and contextroot='egworks';

--rollback update EG_ACTION set url='/offlinestatus/setloaofflinestatus' where name='WorksSearchLOAToSetOfflineStattus' and contextroot='egworks';
--rollback update EG_ACTION set displayname='Search/View LOA' where name='WorksSearchLOAToSetOfflineStattus' and contextroot='egworks';
--rollback update EG_ACTION set url='/letterofacceptance/ajaxloanumber-milestone' where name='AjaxWorkOrderNumberForMilestone' and contextroot='egworks';
--rollback update EG_ACTION set url='/offlinestatus/ajaxsearch-loa' where name='LOASearchResultToSetOfflineStattus' and contextroot='egworks';
--rollback update EG_ACTION set url='/offlinestatus/ajaxestimatenumbers' where name='LOAEstimateNumbersForSetOfflineStattus' and contextroot='egworks';
