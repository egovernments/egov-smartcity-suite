<head>
	<script>
	
		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
		}
		
		function glCodeParams()
		{
			return "accTypes=E,L";  //this for setting value in query like  " type in (E,L) "
		}
		
		function assignGlcode(obj)
		{
			var payheadIDValue= obj.value;
			
			if(payheadIDValue!="-1")
			{
				makeJSONCall(["payheadGLDtl"],'${pageContext.request.contextPath}/nominee/ajaxNominationHeader!payheadGLDtl.action',{payheadId:payheadIDValue},glDtlLoadHandler,glDtlLoadFailureHandler) ;
			}
			else
			{
				document.getElementById("coa").value="";
				document.getElementById("coaSearch").value ="";				
				document.getElementById("accountName").value="";
				document.getElementById("coaSearch").disabled =false;
			}
		}
	
		glDtlLoadHandler = function(req,res){
	  		results=res.results;
	  		
	    	var reslen = results.length;
	    	
	    	for(i=0;i<reslen;i++){
	    		var gldtl = results[i].payheadGLDtl.split('~');
	    		
				if(gldtl=="")
				{
					document.getElementById("msg").innerHTML="<font color='red'>No glcode code found for selected Payhead <b>"+document.getElementById("salaryCodes").options[document.getElementById("salaryCodes").value].text+"</b></font>";
					document.getElementById("coaSearch").disabled =false;
					document.getElementById("salaryCodes").value ="-1";
				}
				else
				{
					document.getElementById("coa").value=gldtl[0];
					document.getElementById("coaSearch").value =gldtl[1];				
					document.getElementById("accountName").value=gldtl[2];
					document.getElementById("coaSearch").disabled =true;
					document.getElementById("msg").style.display='';
					document.getElementById("msg").innerHTML="";
				}
	    	}
		}
		
		glDtlLoadFailureHandler = function(){
		    document.getElementById("msg").style.display='';
			document.getElementById("msg").innerHTML='Problem on loading gl detail';
		}
		
		var coaSearchSelectionHandler = function(sType, arguments) { 
        	
        	var oData = arguments[2];
        	var oData0 = oData[0].split("-");
        	document.getElementById("coaSearch").value=trimAll(oData0[0]);
        	document.getElementById("accountName").value=trimAll(oData0[1]);
       		document.getElementById("coa").value =trimAll(oData[1]);
       		document.getElementById("coaSearch").focus();
   		}

		var coaSelectionEnforceHandler = function(sType, arguments) {
     		warn('improperCOASelection');
 		}
 		
	</script>
	
</head>
	
<s:push value="model">
	<s:token/>
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
              	<div class="headplacer"><s:text name="Nomination.Heading"/></div>
           </td>
       </tr>
	
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Nomination.Code"/> </td>
		<td class="greybox2wk"  colspan="3"><s:textfield name="code" id="code"/> </td>
	</tr>
	
	<tr>
		<td class="whiteboxwk"><s:text name="Nomination.Description"/> </td>
		<td class="whitebox2wk"  colspan="3"><s:textfield name="description" id="description"/> </td>
	</tr>
	
	<tr>
		<td class="greyboxwk"><s:text name="Nomination.PayHead"/> </td>
		<td class="greybox2wk"  colspan="3"><s:select name="salaryCodes" id="salaryCodes" list="dropdownData.salaryCodesList" listKey="id" listValue="head" headerKey="-1" headerValue="----Select----" 
		onchange="assignGlcode(this)" value="%{salaryCodes.id}"  /> </td>
	</tr>
	
	<tr>
		<td class="whiteboxwk"><s:text name="Nomination.AccountCode"/></td>
		<td class="whitebox2wk">
			<div class="yui-skin-sam">
				<div id="coaSearch_autocomplete">
					<div>
						<s:textfield id="coaSearch" size="10" name="coaSearch" value="%{coa.glcode}" onblur=""/>
					</div>
					<span id="coaSearchResults"></span>
				</div>
			</div>
			
			<egovtags:autocomplete name="coaSearch" width="20"  maxResults="8"	field="coaSearch"
				url="${pageContext.request.contextPath}/common/glCodeSearch!glCodeSearch.action"
				queryQuestionMark="true" results="coaSearchResults"
				handler="coaSearchSelectionHandler"
				forceSelectionHandler="coaSelectionEnforceHandler" 
				paramsFunction="glCodeParams"/>
			
			<span class='warning' id="improperCOASelectionWarning"></span>
		</td>
		<td class="whiteboxwk"><s:text name="Nomination.AccountName" /> </td>
		<td class="whitebox2wk"><s:textfield size="30" name="accountName" id="accountName" value="%{coa.name}" readonly="true"/> <s:hidden id="coa" name="coa" value="%{coa.id}"/></td>
	</tr>
	
	<tr>
		<td class="whiteboxwk"><s:text name="Nomination.RuleScript"/> </td>
		<td class="whitebox2wk"><s:select name="ruleScript" id="ruleScript" list="dropdownData.wfActionList" listKey="id" listValue="description" headerKey="-1" headerValue="----Select----"  value="%{ruleScript.id}" /> </td>
	</tr>
	
	<tr>
		<td><s:hidden name="id"/></td>
	</tr>
</s:push>