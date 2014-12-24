#UP

update eg_wf_types set wf_link = '/ptis/cocpt/beforeWFCreateProperty!handleWorkFlow.action?ViewType=WorkFlowObject&id=:ID' where wf_type = 'WorkflowProperty';

#DOWN

update eg_wf_types set wf_link = '/ptis/cocpt/beforeWFCreateProperty!handleWorkFlow.action?ViewType=WorkFlowObject&WorkFlowTrnsfrId=:ID' where wf_type = 'WorkflowProperty';
