<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,java.lang.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
	org.egov.infstr.commons.*,org.egov.payroll.model.*,java.text.SimpleDateFormat"%>

<html>
<head>
<title>Create/Modify Payhead Rule</title>

<script language="JavaScript" type="text/JavaScript">

	function checkOnSubmit()
	{	
		/*var len = document.getElementById("ruleDesc").rows.length;
		for(var i =0;i<len;i++)
		{
		document.payheadRuleForm.effectiveFrom[i].disabled=false;
		document.payheadRuleForm.description[i].disabled=false;
		document.payheadRuleForm.payheadRuleScript[i].disabled=false;
		document.payheadRuleForm.payRuleId[i].disabled=false;
		}*/
	
	    document.payheadRuleForm.salarycode.disabled=false;
	  
		if(document.payheadRuleForm.salarycode.value == ""){
			alert("Select salarycode");
			document.payheadRuleForm.salarycode.focus();
			return false;
		}

		var tb1=document.getElementById("ruleDesc");
	 	var lastRow=tb1.rows.length;

	 	for(var i=0;i<lastRow;i++)
		{
			var rowNo = i+1;
			var rowNoMod = rowNo%10;
			var rowSupScript ='';
			
			if(rowNoMod ==1)
				rowSupScript = rowNo+'st';
			else if(rowNoMod == 2)
				rowSupScript = rowNo+'nd';
			else if(rowNoMod == 3)
				rowSupScript = rowNo+'rd';	
			else
				rowSupScript = rowNo+'th';	

			if(getControlInBranch(tb1.rows[i],'effectiveFrom').value=='')
			{	
				alert("Please enter EffectiveFrom in "+rowSupScript+" Row  ");
				return false;
			}
			else if(getControlInBranch(tb1.rows[i],'description').value=='')
			{
				alert("Please enter Description in "+rowSupScript+" Row  ");
				return false;
			}
			else if(getControlInBranch(tb1.rows[i],'payheadRuleScript').value=='')
			{
				alert("Please select RuleScript in "+rowSupScript+" Row  ");
				return false;
			}
		}
		
		document.payheadRuleForm.action ="${pageContext.request.contextPath}/payhead/payheadRule.do?submitType=createPayheadRule";
		document.payheadRuleForm.submit();
	}

	function addRowToTable(tbl,obj)
	{
	  	tableObj=document.getElementById(tbl);
	  	var rowObj1=getRow(obj);
	  	var tbody=tableObj.tBodies[0];
	 	var lastRow = tableObj.rows.length;

   		if(tbl=='ruleDesc')
  		{
  	  	   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);

	  	   tbody.appendChild(rowObj);
	       var td = rowObj.cells[1];
	       var x=document.all? true : false;
	       var effectFromInputText = x ? td.firstChild : td.firstChild.nextSibling;
	      
	       var lastRowAttr = effectFromInputText.getAttribute('id');
	  	   var lastRowIdIndex =lastRowAttr.substring(13,lastRowAttr.length);// 13 chars for effectiveFrom
	       var idPart = parseInt(lastRowIdIndex)+1;
	       
	       effectFromInputText.setAttribute('id',"effectiveFrom"+idPart);
	       var link = effectFromInputText.nextSibling.nextSibling.firstChild;
	       link.setAttribute("id","spanEffectiveFrom"+idPart);
	       link.href ="javascript:show_calendar('payheadRuleForm.effectiveFrom"+idPart+"')" ;
	       
	       var descInputText = x ? rowObj.cells[3].firstChild : rowObj.cells[3].firstChild.nextSibling;
	       descInputText.setAttribute('id',"description"+idPart);
	       
	       var ruleScriptSelect = x ? rowObj.cells[5].firstChild : rowObj.cells[5].firstChild.nextSibling;
	       ruleScriptSelect.setAttribute('id',"payheadRuleScript"+idPart);
	       
	       var remlen1=document.payheadRuleForm.description.length;
		   document.payheadRuleForm.payRuleId[remlen1-1].value="0";
		   document.payheadRuleForm.description[remlen1-1].value="";
		   document.payheadRuleForm.effectiveFrom[remlen1-1].value="";
		   document.payheadRuleForm.payheadRuleScript[remlen1-1].options[0].selected=true;
  		}
	}
	
	function deleteRow(table,obj)
	{
		if(table=='ruleDesc')
		{
			var tbl = document.getElementById(table);
			var rowObj=getRow(obj);
			var rowNumber=getRow(obj).rowIndex;
			
			if(getControlInBranch(tbl.rows[rowObj.rowIndex],'payRuleId').disabled.toString() =="false")
			{
				if(${fn:length(payheadRuleForm.description)}<(eval(rowNumber)))
				{
				 	var payId = getControlInBranch(tbl.rows[rowObj.rowIndex],'payRuleId').value;
					populateDeleteSet("delPayRule" , payId);
				   	tbl.deleteRow(rowNumber);
				}
				else
				{
				     alert("<bean:message key='alertDeleteRow'/>");
				     return false;
				}
			}
			else
			{
			 	alert("<bean:message key='alertDeleteRow'/>");
				return false;
			}
		}
	}

	function populateDeleteSet(setName, delId)
	{
		if(delId != null && delId != "" && delId!=0)
		{
	    	var http = initiateRequest(); //this initiateRequest method specified in jsCommonMethods.js
			var url = "${pageContext.request.contextPath}/commons/updateDelSets.jsp?type="+setName+"&id="+delId;
			http.open("GET", url, true);
			http.onreadystatechange = function()
			{
				if (http.readyState == 4)
				{
					if (http.status == 200)
					{
					    var statusString =http.responseText.split("^");
					}
				}
			};
			http.send(null);
		}
	}
		
	function checkPayheadRule()
	{		
		var action = "getPayheadRuleBySalarycodeEffectiveForm";
		var salarycode = document.payheadRuleForm.salarycode.value;
		var effectiveFrom = document.payheadRuleForm.effectiveFrom.value
		var url = "<%=request.getContextPath()%>"+"/commons/payheadRuleAJAX.jsp?action=" +action+ "&salarycode="+salarycode+ "&effectiveFrom="+effectiveFrom ;
		var isUnique;
		var req = initiateRequest(); //this initiateRequest method specified in jsCommonMethods.js
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

	function validateEffectiveDate(obj){
		//waterMarkTextOut('effectiveFrom','dd/MM/yyyy');
		if(obj.value!='dd/MM/yyyy')
			validateDateFormat(obj);
	}
	
	function disableRows()
	{
	 	<%
    	SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cale = Calendar.getInstance();
		Date dates=cale.getTime();
		%>
		var dateArray=new Array();
		var toDay="<%=sd.format(dates)%>";
		var len = document.payheadRuleForm.effectiveFrom.length;
		for(var i=0;i<len ;i++)
		{
			dateArray[i]=document.payheadRuleForm.effectiveFrom[i].value;
			if(compareDate(dateArray[i],toDay)==1)
			{
				document.payheadRuleForm.effectiveFrom[i].disabled="true";
				document.payheadRuleForm.payRuleId[i].disabled="true";
				document.payheadRuleForm.description[i].disabled="true";
				document.payheadRuleForm.payheadRuleScript[i].disabled="true";
			}
		}
	}
	
	function checkForUnique(obj)
	{
		var curEff= obj.value;
		
		if(curEff!="")
		{
			var table;
			table = document.getElementById("ruleDesc");
			var rowObj = getRow(obj);
			var len = table.rows.length;
			var row = rowObj.rowIndex;
		
			if(len > 1)
			{
				for(var i=len;i>=1;i--)
				{
					if(i!=eval(row+1))
					{
						var eff = getControlInBranch(table.rows[i-1],'effectiveFrom').value;
				
						if(eff!="" && compareDate(eff,curEff)==0)
						{
						     alert("Duplicate selection of date not allowed");
						     obj.value="";
						     obj.focus();
				             return false;
						}
					}
			    }
			 }
		 }
	}
	
</script>

</head>

<body>


			<html:form action="/payhead/payheadRule">
	
				<table width="100%" cellpadding="0" cellspacing="0" border="0" id="payheadruletable">
					<tr>
						<td colspan="4" class="headingwk">
							<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  			<div class="headplacer"><bean:message key="PayheadRuleHeaderCr"/></div>
                  		</td>
              		</tr>
					
					
					<tr>
			<td class="whiteboxwk">Salarycode<font color="red">*</font></td>
			<td class="whitebox2wk">
				<html:select property="salarycode" styleClass="selectwk" value="${id}" disabled='true'>
					<html:option value="">-----------Select-----------</html:option>
					<c:forEach var="sal" items="${salarycodeList}">
					  				<html:option value="${sal.head}" >${sal.head}</html:option>
					  			</c:forEach>
				</html:select>
			</td>			
		</tr>
					
		</table>
		<br>	
		<div class="ScrollAuto" style="width:100%">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" id="ruleDesc" >
			<c:choose>
				<c:when test ="${fn:length(ruleList)>0}">
					<c:forEach var="PayheadRule" items= "${ruleList}" varStatus="row">
					 	<tr>
							<td class="greyboxwk"><input type ="hidden" name ="payRuleId" id ="payRuleId" value="${PayheadRule.id}" /><span class="mandatory">*</span><bean:message key="PayheadRuleDate"/></td>
							<td class="greybox2wk">
								<input type="text"  name="effectiveFrom"  id="effectiveFrom${row.count-1}" maxlength="10" 
								value="<fmt:formatDate  type="date" value="${PayheadRule.effectiveFrom}" pattern="dd/MM/yyyy"/>" 
								 onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateEffectiveDate(this);checkForUnique(this);" />
								<span><a  href="javascript:show_calendar('payheadRuleForm.effectiveFrom${row.count-1}')" 
								onmouseover="window.status='Date Picker';return true;"	onmouseout="window.status='';return true;"> 
								<img border="0" src="<%=request.getContextPath()%>/common/image/calendar.png"  border=0" /></a></span>
							</td>				
	
							<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDesc"/></td>
							<td class="greybox2wk">
								<html:text property="description" styleId="description${row.count-1}"  onblur="trim1(this,this.value);" value="${PayheadRule.description}"/>
							</td>			
					
							<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleScript"/></td>
							<td class="greybox2wk">
								<html:select property="payheadRuleScript" styleId="payheadRuleScript${row.count-1}" styleClass="selectwk"
								 value="${PayheadRule.ruleScript.id}"  >
						  			<html:option value="">---choose---</html:option>
						  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
						  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
						  			</c:forEach>
						  		</html:select>
							</td>	
							<td class="greybox2wk">
								<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
		 						onclick="addRowToTable('ruleDesc',this);" /></a> 
		 						<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
		 						onclick="deleteRow('ruleDesc',this);"/></a></div>	
		 					</td>	
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="greyboxwk"><input type ="hidden" name ="payRuleId" id ="payRuleId" value="0" /><span class="mandatory">*</span><bean:message key="PayheadRuleDate"/></td>
						<td class="greybox2wk">
							<input type="text"  name="effectiveFrom"  id="effectiveFrom0"  maxlength="10"
							value="<fmt:formatDate  type="date" value="${PayheadRule.effectiveFrom}" pattern="dd/MM/yyyy"/>" 
							 onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateEffectiveDate(this);checkForUnique(this);" />
							<span><a  href="javascript:show_calendar('payheadRuleForm.effectiveFrom0')" 
							onmouseover="window.status='Date Picker';return true;"	onmouseout="window.status='';return true;"> 
							<img border="0" src="<%=request.getContextPath()%>/common/image/calendar.png"  border=0" /></a></span>
						</td>
				
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleDesc"/></td>
						<td class="greybox2wk">
							<html:text property="description" styleId="description0" onblur="trim1(this,this.value);" />
						</td>			
					
						<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PayheadRuleScript"/></td>
						<td class="greybox2wk">
							<html:select property="payheadRuleScript" styleId="payheadRuleScript0" styleClass="selectwk" >
					  			<html:option value="">---choose---</html:option>
					  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
					  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
					  			</c:forEach>
					  		</html:select>
						</td>	
						<td class="greybox2wk">
							<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 						onclick="addRowToTable('ruleDesc',this);" /></a> 
	 						<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 						onclick="deleteRow('ruleDesc',this);"/></a></div>	
	 					</td>	
					</tr>
				</c:otherwise>
			</c:choose>
		</table>
		</div>
		<br>
		
		<table width="95%" cellpadding="0" cellspacing="0" border="0">
			<tr>
               <td colspan="4" class="shadowwk"></td>
           </tr>
           
           <tr>
               <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
           </tr>
		   <tr>
			   <td align="center" colspan="2">
				 <input type="button"  value="submit" onclick="return checkOnSubmit();" class="buttonfinal"/>
				 <input type="button" name="cancel" value="Cancel" onclick="history.go(0)" class="buttonfinal"/>
			  </td>
		   </tr>
		</table>
	</html:form>

</body>
</html>
