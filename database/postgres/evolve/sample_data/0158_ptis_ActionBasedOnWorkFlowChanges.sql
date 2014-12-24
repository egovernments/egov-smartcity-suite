#UP

---script for collection : bifur and amalg


UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'WorkFlowBasedActions';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'transferOwnerProp = persistService.find(''from PropWorkFlowMutation pwfm where pwfm.propertyid=? and pwfm.state.value!= ?'', [properrtyId,''END''])
modifyProp = persistService.find(''from WorkFlowModifyProperty pwfm where pwfm.propertyid=? and pwfm.state.value!= ?'', [properrtyId,''END''])
bifur = persistService.find(''from WorkflowProperty wfp where wfp.oldpropbillnos like ? and wfp.creationReason=? and wfp.state.value!= ?'', ["%"+properrtyId+"%",''BIFUR'',''END''])
amalg = persistService.find(''from WorkflowProperty wfp where wfp.oldpropbillnos like ? and wfp.creationReason=? and wfp.state.value!= ?'', ["%"+properrtyId+"%",''AMALG'',''END''])'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeDeactivateProp.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.Deactivate.Msg'')'
WHERE name = 'WorkFlowBasedActions';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeChangeProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:          result = (''false'',''WorkFlowAuth.ChangeOwner.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/viewCollectTax.do'':  
    if bifur == None and amalg == None:  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.CollectTax.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'if ActionName == ''/property/beforeModifyProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:          result = (''false'',''WorkFlowAuth.ModifyProp.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'if ActionName == ''/property/beforeChangeProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')      else:  
        result = (''false'',''WorkFlowAuth.ChangProp.Msg'')'WHERE name = 'WorkFlowBasedActions';
#DOWN

UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'WorkFlowBasedActions';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'transferOwnerProp = persistService.find(''from PropWorkFlowMutation pwfm where pwfm.propertyid=? and pwfm.state.value!= ?'', [properrtyId,''END''])
modifyProp = persistService.find(''from WorkFlowModifyProperty pwfm where pwfm.propertyid=? and pwfm.state.value!= ?'', [properrtyId,''END''])'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeDeactivateProp.do'':  
    if (transferOwnerProp == None and modifyProp == None):  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.Deactivate.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeChangeProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.ChangeOwner.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeModifyProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.ModifyProp.Msg'')'
WHERE name = 'WorkFlowBasedActions';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
if ActionName == ''/property/beforeChangeProperty.do'':  
    if transferOwnerProp == None and modifyProp == None:  
        result = (''true'','' '')  
    else:  
        result = (''false'',''WorkFlowAuth.ChangProp.Msg'')'
WHERE name = 'WorkFlowBasedActions';
