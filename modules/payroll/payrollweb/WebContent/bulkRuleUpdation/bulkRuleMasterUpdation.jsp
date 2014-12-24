<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,java.lang.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
	org.egov.infstr.commons.*,org.egov.payroll.model.*,
	org.egov.infstr.client.filter.EGOVThreadLocals,
	org.egov.pims.utils.*,org.egov.pims.model.*,
	java.math.BigDecimal,
	org.egov.commons.CFinancialYear,
	java.text.SimpleDateFormat,
	org.egov.infstr.utils.EGovConfig "%>

<html>
<head>
<title><bean:message key="bulkUpdateRuleMstr"/></title>

<%
	String mode=null;
	mode=(String)request.getAttribute("mode");
	System.out.println("mode-In BulkMasterJsp-"+mode);
	java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//PayGenUpdationRule payGenUpdationRuleObj = (PayGenUpdationRule)request.getAttribute("paygenupdationruleobj");
%>

<script>
var flag='null';
function callCategory(obj)
	{
		
		
		if(document.BulkMasterForm.categoryType.value == "E")
		{
			
			
			var count = 1;
			 document.BulkMasterForm.payHead.options.length=1;
			<c:forEach var="salObj" items="${allSalaryCodes}">						
				 <c:if test = "${salObj.categoryMaster.catType == 'E' && salObj.calType!='SlabBased' }">							
	 				document.BulkMasterForm.payHead.options[count] = new 
					Option("${salObj.head}","${salObj.id}");	
					count=count+1;
					
				</c:if>	
					
				

			</c:forEach>
		}

		if(document.BulkMasterForm.categoryType.value == "D")
		{
			
			
			var count = 1;
			 document.BulkMasterForm.payHead.options.length=1;
			
			<c:forEach var="salObj" items="${allSalaryCodes}">						
				 <c:if test = "${salObj.categoryMaster.catType == 'D' && salObj.calType!='ComputedValue'}">							
	 				document.BulkMasterForm.payHead.options[count] = new 
					Option("${salObj.head}","${salObj.id}");	
					count=count+1;
					
				</c:if>	
					
				

			</c:forEach>
		}
			
			document.BulkMasterForm.payHead.value='${BulkMasterForm.payHead}';

		
		
	}

	

	function callCalculationType(obj)
	{
		
		var count = 1;
			<c:forEach var="salObj" items="${allSalaryCodes}">	
				if(obj.value == "${salObj.id}"){	
				
				   document.BulkMasterForm.calType.value = "${salObj.calType}";
					
				   if(document.BulkMasterForm.calType.value=='ComputedValue')
					{
					   document.BulkMasterForm.amount.value="";
					   document.getElementById("amount").style.display="none";
					   document.getElementById("percentage").style.display="";
					   flag='comp';

				    }
					else if(document.BulkMasterForm.calType.value=='MonthlyFlatRate')
					{
						  document.BulkMasterForm.percentage.value="";
						  document.getElementById("percentage").style.display="none";
						  document.getElementById("amount").style.display="";
						  flag='flat';
					}
					else if(document.BulkMasterForm.calType.value=='RuleBased')
					{
						  document.BulkMasterForm.percentage.value="";
						  document.getElementById("amount").style.display="none";
						  document.getElementById("percentage").style.display="";
						  flag='rule';
					}
					else
					{
						 document.BulkMasterForm.amount.value="";
						 document.BulkMasterForm.percentage.value="";
						document.getElementById("amount").style.display="none";
						document.getElementById("percentage").style.display="none";
						alert('<bean:message key="noSlabCaptured"/>');
					}	
					
				  
			}		
			</c:forEach>
	}

	function hideFields()
	{
		//document.getElementById("amount").style.display="none";
		//document.getElementById("percentage").style.display="none";
		if(document.BulkMasterForm.payHead.options[document.BulkMasterForm.payHead.selectedIndex].text=='DA')
		{
			document.getElementById("empGrpMstrs").style.display="";
		
		}
		else
		{
			document.getElementById("empGrpMstrs").style.display="none";
		}
	}

   function checkNumeric(obj)
	{
		
			var objt = obj;
			var amt = obj.value;
				if(obj.value < 0 )
				{
				alert('<bean:message key="alertEnterPositiveVal"/>');
				obj.value="";
				objt.focus();
				return false;

				}
				if(isNaN(amt))
				{
				alert('<bean:message key="alertEnterNumericVal"/>');
				obj.value="";
				objt.focus();
				return false;
				}
		

	}

	function checkOnSubmit()
	{
		monthId=document.BulkMasterForm.monthId.value;
		payId=document.BulkMasterForm.payHead.value;
		finId=document.BulkMasterForm.finYear.value;
		empCatId=document.BulkMasterForm.empGrpMstr.value;
		var submitType="";
		var mode="";
		<%
		if(mode.equalsIgnoreCase("modify"))
		{
		%>
			
		submitType="modifyRule";

		<%
		}
		else if(mode.equalsIgnoreCase("create"))
		{
		%>
		submitType="saveRule";
		<%
		}
		%>
		if(document.BulkMasterForm.categoryType.options.selectedIndex ==0)
		{
			alert('<bean:message key="chooseCategoryType"/>');
			document.BulkMasterForm.categoryType.focus();
			return false;
		}

		if(document.BulkMasterForm.payHead.options.selectedIndex ==0 || document.BulkMasterForm.payHead.value=='')
		{
			alert('<bean:message key="choosePayHead"/>');
			document.BulkMasterForm.payHead.focus();
			return false;
		}
		if(document.BulkMasterForm.calType.value!=null)
		{			
		   if(document.BulkMasterForm.calType.value=='ComputedValue')
			{
			   if(document.BulkMasterForm.percentage.value=="")
				{
				   alert('<bean:message key="enterPercentage"/>');
				   document.BulkMasterForm.percentage.focus();
				   return false;
				}

		    }
			else if(document.BulkMasterForm.calType.value=='MonthlyFlatRate')
			{
				if(document.BulkMasterForm.amount.value=="")
				{
					alert('<bean:message key="alertSelectpayHeadAmt"/>');
					document.BulkMasterForm.amount.focus();
					return false;
				}
			}
			else if(document.BulkMasterForm.calType.value=='RuleBased')
			{
				if(document.BulkMasterForm.percentage.value=="")
				{
				   alert('<bean:message key="enterPercentage"/>');
				   document.BulkMasterForm.percentage.focus();
				   return false;
				}
			}
			
			
		}
		if(document.BulkMasterForm.payHead.options[document.BulkMasterForm.payHead.selectedIndex].text=='DA')
		{
			if(document.BulkMasterForm.empGrpMstr.options.selectedIndex ==0)
			{
				alert('Please select the Employee Group');
				document.BulkMasterForm.categoryType.focus();
				return false;
			}
		}	

		if(monthId!=null && payId!=null)
		{
			 var http = initiateRequest();
			<%
			if(mode.equalsIgnoreCase("create"))
			{
			%>
			mode="create";
			var url = "<%=request.getContextPath()%>/bulkRuleUpdation/checkUniqueAjax.jsp?payId="+payId+"&month="+monthId+"&finYear="+finId+"&empCatId="+empCatId;
			http.open("GET", url, false);
			http.send(null);						 							
			 if (http.status == 200)
			  {			
			  	//alert("dasd");				
				var statusString = http.responseText;
				//alert("dasd"+statusString);	
				var s = statusString.split("^");
				//alert(s[0]);
				if(s[0]!="true")
				{		 
					alert('<bean:message key="existingRuleForMonFy"/>');
				    return false;
				}else{
					document.BulkMasterForm.action = "${pageContext.request.contextPath}/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType="+submitType+"&mode="+mode;
					document.BulkMasterForm.submit();
				}
			 }								 
			<%}
			else if(mode.equalsIgnoreCase("modify"))
			{%>
				//alert("modify");
				mode="modify";
				ruleId=document.BulkMasterForm.ruleId.value;
				if(ruleId!=null)
				{
					var url = "<%=request.getContextPath()%>/bulkRuleUpdation/checkRuleAjax.jsp?payId="+payId+"&month="+monthId+"&finYear="+finId+"&ruleId="+ruleId+"&empCatId="+empCatId;
					http.open("GET", url, false);
					http.send(null);																		
					 if (http.status == 200)
					  {							
					  		//alert("dsad");
							var statusString = http.responseText;
							//alert(statusString);
							var s = statusString.split("^");
							if(s[0]!="false")
							{
								alert('<bean:message key="existingRuleForMonFy"/>');
								return false;												
						    }
						   else
						   {
							 document.BulkMasterForm.action = "${pageContext.request.contextPath}/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType="+submitType+"&mode="+mode;
							 document.BulkMasterForm.submit();													
						   }
					  }										
				}						
			<%}%>
		}		
	}

function validPercentage(obj)
{
	var x = obj.value;
	rlength=1;//legnth of fractional value
	if (isNaN(x))
		{    
			// value is out of range
			alert('<bean:message key="enterPercentage"/>');
			document.BulkMasterForm.percentage.value="";
			document.BulkMasterForm.percentage.focus();
			return false;
			
		}
		if(obj.value < 0 )
		{
			alert("Please enter positive value for the Percentage");
			obj.value="";
			obj.focus();
			return false;

		}
		if(obj.value > 0)
		{
			obj.value= Math.round(x*Math.pow(10,rlength))/Math.pow(10,rlength);
		}	
}

function checkMonthToModify()
{

	
	<c:set var= "varMon" value="${BulkMasterForm.monthId}" />
	<c:set var= "varfinyr" value="${BulkMasterForm.finYear}" />
		
	<%

	if(mode != null && mode.equalsIgnoreCase("modify") || mode.equalsIgnoreCase("create"))
	{

		
		    int mon;
			String varMon = (String)pageContext.getAttribute("varMon"); 
			mon = Integer.parseInt(varMon.trim());

			int finYr;
			String varFinyr = (String)pageContext.getAttribute("varfinyr"); 
			finYr = Integer.parseInt(varFinyr.trim());
		
		%>
			
      var newMonth=document.BulkMasterForm.monthId.value;
	  var oldMonth=<%=mon%>;
      var newFinYr=document.BulkMasterForm.finYear.value;
	  var oldFinYr=<%=finYr%>;
	 
		 var http = initiateRequest();
	  var url = "<%=request.getContextPath()%>/bulkRuleUpdation/checkForCurrentMonthAjax.jsp?month="+newMonth+"&financialYr="+newFinYr;
						http.open("GET", url, true);
					 http.onreadystatechange = function()
					{
						

					  if (http.readyState == 4)
					  {
							
						 if (http.status == 200)
						  {
							
							var statusString = http.responseText;
							var s = statusString.split("^");
							if(s[0]!="true")
							  {
								alert('Given month/Financial Year cannot be less than current date ');
								document.BulkMasterForm.monthId.value=<%=mon%>;
								document.BulkMasterForm.finYear.value=<%=finYr%>;
								document.BulkMasterForm.monthId.focus();
								return false;
							}
						}
					  }

					};
					 http.send(null);

	 
	<%}%>
}
function viewFields()
{
	<%

	if(mode != null && mode.equalsIgnoreCase("view"))
	{
		%>
	var category=document.BulkMasterForm.categoryType.value;

		
	 callCategory(category);
	 setCalculationType();
	 setReadOnly();
	 <%}else if(mode.equalsIgnoreCase("modify")){%>

		 var category=document.BulkMasterForm.categoryType.value;

		
	 callCategory(category);
	 setCalculationType();
	 <%}%>

}
function setReadOnly()
{
	document.BulkMasterForm.categoryType.disabled= true;
	document.BulkMasterForm.payHead.disabled= true;
	document.BulkMasterForm.calType.disabled= true;
	document.BulkMasterForm.percentage.disabled= true;
	document.BulkMasterForm.amount.disabled= true;
	document.BulkMasterForm.monthId.disabled= true;
	document.BulkMasterForm.finYear.disabled= true;

}

function showEmpGrpMaster(obj)
{
	var selectedStatus = document.getElementById("payHead").options[obj.selectedIndex].text;
	if(selectedStatus.toUpperCase()=='DA')
	{
		document.getElementById("empGrpMstrs").style.display="";
	}
	else
	{
		document.getElementById("empGrpMstrs").style.display="none";
	}
}

function setCalculationType()
{
	var obj=document.BulkMasterForm.payHead;
	 var count = 1;
			<c:forEach var="salObj" items="${allSalaryCodes}">	
				if(obj.value == "${salObj.id}"){	
				
				   document.BulkMasterForm.calType.value = "${salObj.calType}";
					
				   if(document.BulkMasterForm.calType.value=='ComputedValue')
					{
					   document.BulkMasterForm.amount.value="";
					   document.BulkMasterForm.percentage.value='${BulkMasterForm.percentage}';
					   document.getElementById("amount").style.display="none";
					   document.getElementById("percentage").style.display="";

				    }
					else if(document.BulkMasterForm.calType.value=='MonthlyFlatRate')
					{
						  document.BulkMasterForm.percentage.value="";
						  document.BulkMasterForm.amount.value='${BulkMasterForm.amount}';
						  document.getElementById("percentage").style.display="none";
						  document.getElementById("amount").style.display="";
					}
					else if(document.BulkMasterForm.calType.value=='RuleBased')
					{
						  document.BulkMasterForm.percentage.value='${BulkMasterForm.percentage}';
						  document.getElementById("amount").style.display="none";
						  document.getElementById("percentage").style.display="";
						  flag='rule';
					}
					else
					{
						 document.BulkMasterForm.amount.value="";
						 document.BulkMasterForm.percentage.value="";
						document.getElementById("amount").style.display="none";
						document.getElementById("percentage").style.display="none";
						alert('<bean:message key="noSlabCaptured"/>');
					}	
					
				  
			}		
			</c:forEach>
}

</script>
</head>

<body onload="viewFields();hideFields();">

<html:form action="/bulkRuleUpdation/BulkUpdateMasterAction">
<div class="navibarshadowwk"></div>
		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<div class="datewk"></div>
		<span class="bold">Today : </span>"<%=sdf.format(date)%>"

		<table width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>



	<table  width="95%" cellpadding="0" cellspacing="0" border="0" id="paytable">
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer"><bean:message key="bulkUpdateRuleMstr"/></div></td>
			
		</tr>
		
	<tr>

	<input type=hidden name="ruleId" id="ruleId" value="${BulkMasterForm.ruleId}" />

	<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="catType"/></td>
	<%if("modify".equals(mode)){ %>
			<td class="whitebox2wk">
				<html:select property="categoryType" styleClass="selectwk" onchange="callCategory(this)" disabled="true">
					<html:option value="0"><bean:message key="select"/></html:option>
					<html:option value="D"><bean:message key="deduction"/></html:option>
					<html:option value="E"><bean:message key="Earnings"/></html:option>
				</html:select>
			</td>
			<%}else{%>

			<td class="whitebox2wk">
				<html:select property="categoryType" styleClass="selectwk" onchange="callCategory(this)">
					<html:option value="0"><bean:message key="select"/></html:option>
					<html:option value="D"><bean:message key="deduction"/></html:option>
					<html:option value="E"><bean:message key="Earnings"/></html:option>
				</html:select>
			</td>

			<%}%>
	</tr>


	<tr>
		<td class="greyboxwk">
		<span class="mandatory">*</span><bean:message key="PayHead"/></td>
		
		<%if("modify".equals(mode)){ %>
		<td class="greybox2wk">
		<select class="selectwk" name="payHead" id="payHead" onchange="callCalculationType(this);showEmpGrpMaster(this);" disabled="true">
			<option value='' ><bean:message key="select"/></option>
			</td>
		</select>
		<%}else{%>
		<td class="greybox2wk">
			<select class="selectwk" name="payHead" id="payHead" onchange="callCalculationType(this);showEmpGrpMaster(this);">
				<option value='' ><bean:message key="select"/></option>
				</td>
			</select>
		<%}%>
		
		<td  class="greyboxwk" ><span class="mandatory">*</span><bean:message key="CalculationType"/></td>
		<td   class="greybox2wk" >

		<input type="text"  id="calType" name="calType" value="" readonly>

		</td>
	</tr>
		<tr id="empGrpMstrs" >
			<td class="greyboxwk" colspan="2"/>
				<td class="greyboxwk" ><span class="mandatory">*</span>Employee Group</td>
				
				<td class="greybox2wk"  >
					<select  name="empGrpMstr" id="empGrpMstr" class="selectwk" >		
					<option value='0' ><bean:message key="Choose"/></option>
					<c:forEach items="${empGroupMastersList}" var="empGrpMstrs">
					<c:choose>
						<c:when test="${empGrpMstrs.id == BulkMasterForm.empGrpMstr}">
							<option value="${empGrpMstrs.id}" selected="selected"><c:out value="${empGrpMstrs.name }"/></option>
						</c:when>
						<c:otherwise>
							<option value="${empGrpMstrs.id}"><c:out value="${empGrpMstrs.name }"/></option>
						</c:otherwise>
					</c:choose>	
					</c:forEach>
				</select>
				</td>
		</tr>
	
	
	    <tr id = "amount" >
	    <td  class="whiteboxwk"><span class="mandatory">*</span><bean:message key="Amount"/></td>
	    <td class="whitebox2wk"  colspan="7"> 
	    <input type="text" name="amount" id="amount"  value="" onblur="checkNumeric(this);" class="selectwk">
	    </td>
        </tr>
         <tr id = "percentage" style="display:none" >
     <td  class="whiteboxwk" ><span class="mandatory">*</span>Percentage</td>
     <td   class="whitebox2wk" colspan="7"> 
     <input type="text" name="percentage" id="percentage"  onblur="validPercentage(this);" class="selectwk"> 
     </td>
     </tr>

   <tr>
  <c:set var= "monthId" value="${BulkMasterForm.monthId}" />
  <c:set var= "currfinancialId" value="${BulkMasterForm.finYear}" />
  <%
			//getting current month
			String currMonth= (String)pageContext.getAttribute("monthId"); 
			int currentmonth=new Integer(currMonth).intValue();
			int monReq = currentmonth;

			//getting current financial year
			String currfinancialId= (String)pageContext.getAttribute("currfinancialId"); 
			int fYearReq = Integer.parseInt(currfinancialId.trim());
			%>

			<td  class="greyboxwk" ><bean:message key="Month"/></td>
			<td class="greybox2wk" >
			<select  name="monthId" class="selectwk"   id="monthId" style = "width:200px" onchange="checkMonthToModify();">
			<%

			Map mMap =(Map)session.getAttribute("monthMap");
			TreeSet set = new TreeSet(mMap.keySet());
			for (Iterator it = set.iterator(); it.hasNext(); )
			{
			Integer id = (Integer) it.next();
		
			%>
			<option  value = "<%= id.toString() %>"<%=((((Integer)id).intValue() == monReq)? "selected":"")%>><%= (String)mMap.get(id) %></option>

			<%
			}

			%>
			</select>
			</td>

			</td>

			<td  class="greyboxwk"><bean:message key="FinancialYear"/></td>
			<td class="greybox2wk" >
			<select  name="finYear" class="selectwk"  id="finYear" style = "width:200px" onchange="checkMonthToModify();">
			
			<%

			Map finMap =(Map)session.getAttribute("finMap");
			for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
			{
			Map.Entry entry = (Map.Entry) it.next();
			
			%>
			<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(fYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

			<%
			}

			%>
			</select>
			</td>

			</td>


  </tr>
  <tr>
	<td colspan="4"><div class="buttonholderwk">
	 
	<%if("view".equals(mode)){ %>	
	
		
		<%}else if("modify".equals(mode)){%>

		<html:button styleClass="buttonfinal" value="Modify" property="b4" onclick="return checkOnSubmit();" />	
		<%}else{%>
		<html:button styleClass="buttonfinal" value="Create" property="b4" onclick="return checkOnSubmit();" />	
		<%}%>
		</div>
	</td>
 </tr>
	</table>
</tr>
		</table>
		
		</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
		<div class="buttonholderwk"><input type="submit" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"  />
		</div>
		<div class="urlwk">City Administration System Designed and Implemented by 
		<a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	
</html:form>


</body>
</html>

