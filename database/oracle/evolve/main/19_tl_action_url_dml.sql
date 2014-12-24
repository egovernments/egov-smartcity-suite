#UP
update eg_module set baseurl='tradelicense' where baseurl='egtradelicense';
update eg_action set context_root='tradelicense' where context_root='egtradelicense';

update eg_action set url = replace(url,'/web/','/') where context_root='tradelicense';
update eg_action set url = '/common'||url where context_root='tradelicense' and name='Populate Divisions for Trade License';
update eg_action set url = '/common'||url where context_root='tradelicense' and name='Populate Users for Trade License';
update eg_action set url = '/common'||url where context_root='tradelicense' and name='Populate Designation for Trade License';
update eg_wf_types set wf_link='/tradelicense/viewtradelicense/viewTradeLicense!showForApproval.action?model.id=:ID' where wf_type='TradeLicense';

#DOWN

update eg_module set baseurl='egtradelicense' where baseurl='tradelicense';
update eg_action set context_root='egtradelicense' where context_root='tradelicense';
