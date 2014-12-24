<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="java.util.*,java.lang.*,java.text.SimpleDateFormat"%>

<html>
<head>
<title>View Payhead Rule</title>


<script language="JavaScript" type="text/JavaScript">

	function checkOnSubmit()
	{		
		
		if(document.payheadRuleForm.salarycode.value == ""){
			alert("Select salarycode");
			document.payheadRuleForm.salarycode.focus();
			return false;
		}
		if(document.payheadRuleForm.effectiveFrom.value == ""){
			alert("Enter effectivefrom");
			document.payheadRuleForm.effectiveFrom.focus();
			return false;
		}
		if(document.payheadRuleForm.description.value == ""){
			alert("Enter description");
			document.payheadRuleForm.description.focus();
			return false;
		}
		if(document.payheadRuleForm.payheadRuleScript.value == ""){
			alert("Select Payhead Rule Script");
			document.payheadRuleForm.payheadRuleScript.focus();
			return false;
		}

		if(checkPayheadRule() == "true"){
			alert("already exist");
			document.payheadRuleForm.salarycode.focus();
			return false;
		}
		document.forms("payheadRuleForm").action ="${pageContext.request.contextPath}/payhead/payheadRule.do?submitType=modifyPayheadRule";
	}

	function checkPayheadRule(){		
		var action = "getPayheadRuleBySalarycodeEffectiveForm";
		var salarycode = document.payheadRuleForm.salarycode.value;
		var effectiveFrom = document.payheadRuleForm.effectiveFrom.value
		var url = "<%=request.getContextPath()%>"+"/commons/payheadRuleAJAX.jsp?action=" +action+ "&salarycode="+salarycode+ "&effectiveFrom="+effectiveFrom ;
		var isUnique;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var payheads = req.responseText
                   	var a = payheads.split("^");
                   	var codes = a[0];
                   	if(codes=="true"){
                   		isUnique = "true";
                   	}
                   	else if(codes=="false"){
         				isUnique = "false";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);		
		return isUnique;
	}
	
	function initiateRequest(){
		if(window.XMLHttRequest){
			return new XMLHttpRequest();
		}
		else if(window.ActiveXObject){
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}


   function trim1(obj,value)	
   {
    	value = value;    
	    while (value.charAt(value.length-1) == " ")
	    {
	     	value = value.substring(0,value.length-1);
	    } 
	    while(value.substring(0,1) ==" ")
	    {
	     	value = value.substring(1,value.length);
	    }
    	obj.value = value;
   		return value ;
}

</script>

</head>

<body >
	
			<html:form action="/payhead/payheadRule">
			
				<table width="100%"  cellpadding="0" cellspacing="0" border="0" id="viewpayheadruletable">
					<tr>
						<td colspan="2" class="headingwk">
							<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  			<div class="headplacer"><bean:message key="PayheadRuleHeaderVw"/></div>
                  		</td>
              		</tr>
					<tr>
						<td class="whiteboxwk"><bean:message key="PayheadRuleSalarycode"/></td>
						<td class="whitebox2wk" >
							<html:select property="salarycode" styleClass="selectwk" value="${id}" disabled="true">
								<html:option value="">-----------Select-----------</html:option>
								<c:forEach var="sal" items="${salarycodeList}">
					  				<html:option value="${sal.id}" >${sal.head}</html:option>
					  			</c:forEach>
					  			</html:select>
						</td>			
					</tr>
					</table>
					<div class="ScrollAuto" style="width:100%">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" id="ruleDesc">
				
			<c:choose>
			<c:when test ="${fn:length(ruleList)>0}">
			<c:forEach var="PayheadRule" items= "${ruleList}">
			
			 <tr>
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDate"/></td>
						<td class="greybox2wk">
							<input type="text"  name="effectiveFrom"  id="effectiveFrom"  disabled="true"
							value="<fmt:formatDate  type="date" value="${PayheadRule.effectiveFrom}" pattern="dd/MM/yyyy"/>" 
							 onfocus="waterMarkTextIn('effectiveFrom','dd/MM/yyyy')"
							 onkeyup="" onblur="" />
							
						</td>			
					
					<input type ="hidden" name ="payRuleId" id ="payRuleId" value="${PayheadRule.id}"  />
					
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDesc"/></td>
						<td class="greybox2wk">
							<html:text property="description" onblur="trim1(this,this.value);" value="${PayheadRule.description}" disabled="true"/>
						</td>			
					
				
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleScript"/></td>
						<td class="greybox2wk">
							<html:select property="payheadRuleScript" styleId="payheadRuleScript" styleClass="selectwk" disabled="true" value="${PayheadRule.ruleScript.id}" >
					  			<html:option value="">---choose---</html:option>
					  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
					  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
					  			</c:forEach>
					  		</html:select>
						</td>	
						
					</tr>
			</c:forEach>
			
			</c:when>
			<c:otherwise>
			<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDate"/></td>
						<td class="greybox2wk">
							<input type="text"  name="effectiveFrom"  id="effectiveFrom"  readOnly="true"
							 
							 onfocus="waterMarkTextIn('effectiveFrom','dd/MM/yyyy')"
							 onkeyup="" onblur="" />
							
						</td>			
					
					  <input type ="hidden" name ="payRuleId" id ="payRuleId" value="0" disabled="true" />
					
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDesc"/></td>
						<td class="greybox2wk">
							<html:text property="description" onblur="trim1(this,this.value);"  disabled="true"/>
						</td>			
					
				
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleScript"/></td>
						<td class="greybox2wk">
							<html:select property="payheadRuleScript" styleId="payheadRuleScript" styleClass="selectwk" disabled="true">
					  			<html:option value="">---choose---</html:option>
					  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
					  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
					  			</c:forEach>
					  		</html:select>
						</td>	
						
					</tr>
			</c:otherwise>
			</c:choose>
			</table>
			</div>
			<br>
			<table width ="100%" border="0" cellspacing="0">
					<tr>
		                <td colspan="4" class="shadowwk"></td>
		            </tr>
				</table>
		</html:form>
       

</body>
</html>
