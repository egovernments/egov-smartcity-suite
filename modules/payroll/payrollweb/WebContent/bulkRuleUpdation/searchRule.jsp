<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,
java.text.SimpleDateFormat,java.math.BigDecimal,
org.egov.payroll.model.*" %>


<html>

<head>


	<title>Search Bulk Updation Rule </title>
	

	<style type="text/css">
		#payheadContainer {position:absolute;left:11em;width:9%}
		#payheadContainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#payheadContainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#payheadContainer ul {padding:5px 0;width:80%;}
		#payheadContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#payheadContainer li.yui-ac-highlight {background:#ff0;}
		#payheadContainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String mode=null;
	mode=(String)request.getAttribute("mode");
	System.out.println("mode--"+mode);
	String link = "";
	java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>


<script language="JavaScript"  type="text/JavaScript">

	
	function checkOnSubmit()
	{
		
		document.BulkMasterForm.action ="<%=request.getContextPath()%>/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=viewRuleMaster";
		 document.BulkMasterForm.submit();
	}	  
  
  function execute()
{
	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }
		

}

 		
 	
</script>
</head>

<body onload ="execute()">
<html:form action="/bulkRuleUpdation/BulkUpdateMasterAction">
 <div class="navibarshadowwk"></div>
		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<div class="datewk"></div>
		<span class="bold">Today :  </span>"<%=sdf.format(date)%>"

		<table width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>
 <input type="hidden" id="mode" name="mode" value="${mode}" />


	<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		<tr>
	<%if("view".equals(mode)){ %>	
	    
	    <td colspan="4" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">View Bulk Updation Rule Master </div></td>
	    
	<%} %>    
	<%if("modify".equals(mode)){ %>
	    
	     <td colspan="4" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">Modify Bulk Updation Rule Master</div></td>
	<%} %>    
	    </tr>
		<tr>
			<td height="15" class="tablesubcaption" colspan="6" align="center"></td>
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

			<td  class="greyboxwk"><bean:message key="Month"/></td>
			<td class="greybox2wk" >
			<select  name="monthId"   id="monthId" style = "stylewk">
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
			<select  name="finYear"   id="finYear" style = "stylewk">
			
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


	</table> 
<br>


<table width="95%" cellpadding ="0" cellspacing ="0" id="table2" border="0">	
 	<tr>
 	<td colspan="4"><div class="buttonholderwk">
		  <html:submit styleClass="buttonfinal" property="submit" value="Submit" onclick="return checkOnSubmit();"/>
	    	</div>
	    	</td>
		  </tr>	
			</table>	
			</center>
	
<tr>
			<td colspan="4">
<%
	Integer ruleId = null;
	BigDecimal monthVar = null;
	String mode1="";
	String monthStr="";
	Map<Integer,String> monthMap= (Map<Integer,String>) session.getAttribute("monthMap");	
%>
<c:if test="${payGenRuleList!=null}">
	<display:table name="${payGenRuleList}" id="payGenRuleList" cellspacing="0" style="width:95%;" 
 	 export="false" defaultsort="2" pagesize = "20" sort="list"  class="its">

		<c:set var= "ruleId" value="${payGenRuleList.id}" />
		<c:set var= "mode" value="${mode}" />
		<c:set var= "monthVar" value="${payGenRuleList.month}" />
		<%
			ruleId = (Integer)pageContext.getAttribute("ruleId"); 
			mode1 = (String)pageContext.getAttribute("mode"); 
			monthVar = (BigDecimal)pageContext.getAttribute("monthVar");
			monthStr=(String)monthMap.get(Integer.valueOf(monthVar.intValue()));
		%>

		<display:column style="width:5%"   title="PayHead Name" property="salaryCodes.head"/> 			
		<display:column style="width:5%"   title="Calculation Type" property="salaryCodes.calType"/> 			
		<display:column style="width:5%"   title="Financial Year" property="financialyear.finYearRange"/> 		
		<display:column style="width:5%" title="Month" ><%= monthStr %></display:column>
		<display:column style="width:5%" title="Employee Group" property="empGroupMstrs.name"/>			
		<display:column media="html" style="width:5%">
		
		<%			
			link =request.getContextPath()+"/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=setIdForMasterDetails"+"&ruleId="+ruleId+"&mode="+mode1;
		%>
	
	<a href="<%=link%>"><%=mode%></a>
	</display:column>
	 </display:table>	
	 </c:if>
     </td>
	</tr>
    </div>
	</table>
	
	<div class="rbbot2">
	<div></div></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
	<input type="submit" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()" />
	</div>
	<div class="urlwk">
	City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">
	eGovernments Foundation</a> All Rights Reserved </div>
</html:form>
</body>
</html>

