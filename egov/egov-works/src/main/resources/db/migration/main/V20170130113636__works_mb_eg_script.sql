--eg_script--
INSERT INTO EG_SCRIPT VALUES(nextval('SEQ_EG_SCRIPT'),'MB-APPROVALRULES','nashorn',1,now(),1,now(),
'function getWorkFlowApproverDetails() {
      var map = new java.util.HashMap();
      if((cityGrade.equals("III") || cityGrade.equals("NP")) && mbAmount <= 25000.1)
           map.put("createAndApproveFieldsRequired",true);
	  else
	   	   map.put("createAndApproveFieldsRequired",false);
      return map;
   }
   result = getWorkFlowApproverDetails();',now(),'01-Jan-2100',0); 