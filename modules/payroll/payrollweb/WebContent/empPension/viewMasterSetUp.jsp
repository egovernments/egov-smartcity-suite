<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,

org.egov.infstr.utils.HibernateUtil,
org.egov.EGOVRuntimeException,
org.apache.log4j.Logger,
java.text.SimpleDateFormat,
org.egov.commons.Bankbranch,
org.egov.infstr.config.*,
org.egov.pims.utils.*,
org.egov.commons.Bankaccount,
org.egov.commons.service.*,
org.egov.pims.model.*"

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Master Setup</title>



<script language="JavaScript"  type="text/JavaScript">

</script>

</head>
<body>
<%
CommonsManager cmnsManger= EisManagersUtill.getCommonsManager();
AppConfig ap= new AppConfig();
final Logger logger = Logger.getLogger("masterSetUp.jsp");
List appKeyList = null;
appKeyList = (List)request.getAttribute("appDataKey");
 String module =null;

System.out.println(">>>>>>>>>>>>>>>>>>>.l???????????ist"+appKeyList.size());
for(Iterator it = appKeyList.iterator();it.hasNext();)
{
  ap = (AppConfig)it.next();
    module = ap.getModule();
   Set appValSeT = ap.getAppDataValues();
  System.out.println(" appValSeT>>>>>"+ appValSeT.size()); 
  
}
%>
<html:form action="empPension/MasterSetUpAction.do?submitType=createNewValues">

<P ALIGN=CENTER><font color="#0000A0">Master SetUp Screen for <%=module%></font></P>

<table  WIDTH=90%  cellpadding ="0" cellspacing ="0" border = "1" id="mainTable" name="mainTable" >


<c:forEach var="appConfig" items="${appDataKey}">
<c:choose>
<c:when test="${appConfig.keyName!='BankPension'}">

<tr id="keyRowId">
<td class="fieldcell">
<label for="description"> ${appConfig.description}</label>
</td>
</tr>

<td colspan="7">
<table id="secondtable" align="left" class="eGovInnerTable" border="0" cellpadding="0"cellspacing="0">

<tr>
<td class="labelcell">Value</td>
<td class="labelcell">Effective From</td>
</tr>

<c:choose>
<c:when test="${appConfig.appDataValues  != null && fn:length(appConfig.appDataValues) > 0}">
<c:forEach var="configValues" items="${appConfig.appDataValues}">



<tr>
<td class="labelcell" >
<input type="text" id="values" name="values" value="${configValues.value}" readOnly>
</td>

<td class="labelcell">
<input type="text"  name="effectiveFrom"  id="effectiveFrom"   value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>" readOnly/>
</td>
</tr>

</c:forEach>
</c:when>


<c:otherwise>

<tr>

<td class="labelcell" >
<input type="text" id="values" name="values" readOnly>
</td>

<td class="labelcell">
<input type="text"  name="effectiveFrom"  id="effectiveFrom" readOnly  />
</td>

</tr>

</c:otherwise>

</c:choose>

</table>
</c:when>
<c:otherwise>

<tr>

<td colspan="7">
<table id="BankTable" align="left" class="eGovInnerTable" border="0" cellpadding="0"cellspacing="0">
<tr id="keyRowId">
<td class="fieldcell">
<label for="description"> ${appConfig.description}</label>
</td>
</tr>
<tr>
<td class="labelcell">Bank</td>
<td class="labelcell">Branch</td>
<td class="labelcell">Account Number</td>
<td class="labelcell">Effective From</td>
</tr>
<c:choose>
<c:when test="${appConfig.appDataValues  != null && fn:length(appConfig.appDataValues) > 0}">
<c:forEach var="configValues" items="${appConfig.appDataValues}">
<tr id="bankRowId" >
<c:set var= "configId" value="${configValues.value}" />
<% 
String configId = (String)pageContext.getAttribute("configId"); 
%>

 	
	  	<td class="labelcell">
			<input type="text" name="bank" value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getBankbranch().getBank().getName()%>"  readOnly/>
	  	</td>	
		
	  	<td class="labelcell">
			<input type="text" name="bankBranch" value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getBankbranch().getBranchname()%>"  readOnly/>
	  	</td>	  	
	  	
	  	<td class="labelcell">
	  		<input type="text"   name="bankAccount" value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getAccountnumber()%>" readOnly/>
	  	</td>
	  	<td class="labelcell">
		<input type="text"  name="bankEffectiveFrom"  id="bankEffectiveFrom"   value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>" readOnly/>
</td>
    </tr> 
    </c:forEach>
    </c:when>
    <c:otherwise>
    <tr id="BankRowId" >	
     
   		
     	  	<td class="labelcell">
		<input type="text" name="bank"   readOnly/>
		</td>	
				
		<td class="labelcell">
			<input type="text" name="bankBranch"   readOnly/>
		</td>	  	

		<td class="labelcell">
			<input type="text"   name="bankAccount"  readOnly/>
		</td>
		<td class="labelcell">
		<input type="text"  name="bankEffectiveFrom"  id="bankEffectiveFrom"   readOnly/>
		</td>
                    
     </tr> 
 </c:otherwise>
 </c:choose>
    </table>
</c:otherwise>

</c:choose>

</c:forEach>

</table>

</html:form>
</body>
</html>