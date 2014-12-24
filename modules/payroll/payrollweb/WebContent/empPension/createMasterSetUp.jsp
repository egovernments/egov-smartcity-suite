<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,

org.egov.infstr.utils.HibernateUtil,
org.egov.EGOVRuntimeException,
org.apache.log4j.Logger,
java.text.SimpleDateFormat,
org.egov.payroll.model.*,
org.egov.payroll.model.pension.NomineeDetails,
org.egov.payroll.client.*,
org.egov.commons.Bankbranch,
org.egov.pims.utils.*, 
org.egov.commons.service.*,
org.egov.commons.Bankaccount,
org.egov.infstr.config.*,

org.egov.pims.model.*"

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Master Setup</title>

<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>

<script language="JavaScript"  type="text/JavaScript">
function validateOnSubmit()
{
	

        <%
	
	AppConfig appObj= new AppConfig();
	String keyName = "";
	
	List appConfig= null;
	appConfig  = (List)request.getAttribute("appDataKey");
	for(Iterator it =appConfig.iterator();it.hasNext();)
	{
	  appObj = (AppConfig)it.next();
	  keyName = appObj.getKeyName();
	
	%>
	  var name = '<%=keyName%>';
	 
	if(name!=null && name =='BankPension')
		{
		 var tableObj=document.getElementById("BankTable");
		 var tableLen=tableObj.rows.length;
		
	 if(tableLen ==3)
	 	{
	 if(document.getElementById("bank").disabled !="true")
		 {
	 
		   if(masterDataForm.bank.value =="")
		    {
		    alert('<bean:message key="alertChooseBnkNme"/>');
		    masterDataForm.bank.focus();
		    return false;
		    }
		    if(masterDataForm.bankBranch.value =="")
		    		    {
		    		    alert('<bean:message key="alertChooseBrnchNme"/>');
		    		   
		    		     masterDataForm.bankBranch.focus();
		    		    return false;
		    }
		    if(masterDataForm.bankAccount.value =="")
		    		    {
		    		    
		    		    alert('<bean:message key="alertChooseAccnum"/>');
		    		    masterDataForm.bankAccount.focus();
		    		    return false;
		    }
		    if(masterDataForm.bankEffectiveFrom.value =="")
		    		    {
		    		    		    
				   alert('please enter the date');
				   masterDataForm.bankEffectiveFrom.focus();
				   return false;
		    }
		}  
	 }
	 else 
	 {

	 for(var i=0;i<tableLen-2;i++)
	 {
	   if(masterDataForm.bank[i].value =="")
	  		    {
	  		    alert('<bean:message key="alertChooseBnkNme"/>');
	  		    masterDataForm.bank[i].focus();
	  		    return false;
	  		    }
	  		    if(masterDataForm.bankBranch[i].value =="")
	  		    		    {
	  		    		    alert('<bean:message key="alertChooseBrnchNme"/>');
	  		    		   
	  		    		     masterDataForm.bankBranch[i].focus();
	  		    		    return false;
	  		    }
	  		    if(masterDataForm.bankAccount[i].value =="")
	  		    		    {
	  		    		    
					    alert('<bean:message key="alertChooseAccnum"/>');
					    masterDataForm.bankAccount[i].focus();
					    return false;
		    }
		    if(masterDataForm.bankEffectiveFrom[i].value =="")
		    {
		    		    
					   alert('please enter the date');
					   masterDataForm.bankEffectiveFrom[i].focus();
					   return false;
		    }
		    }
	}
	}
	else
	  {
	var len = masterDataForm.values.length;
	
	  if(len==1)
	  {
	
	 if(masterDataForm.values.value ==null || masterDataForm.values.value=="")
	    {
	    alert('please enter the value');
	   masterDataForm.values.focus();
	    return false;
	}
	if(masterDataForm.effectiveFrom.value ==null)
	{

		alert('please enter the date');
		masterDataForm.effectiveFrom.focus();
		return false;
		    }
		    }
		    else
		    {
		     for(var j=0;j<=len-1;j++)
	             {
		    if(masterDataForm.values[j].value =="")
		    	    {
		    	    alert('please enter the  value');
		    	    masterDataForm.values[j].focus();
		    	    return false;
		    	}
		    	if(masterDataForm.effectiveFrom[j].value =="")
		    	{
		    
		    		alert('please enter the  date');
		    		masterDataForm.effectiveFrom[j].focus();
		    		return false;
		    }
		    }
		    }
	  	 
	 	  
	  }
	 <%
	 }
 %>

	document.forms("masterDataForm").action = "${pageContext.request.contextPath}/empPension/MasterSetUpAction.do?submitType=createNewValues";
	
	document.forms("masterDataForm").submit();
	
	
}
function deleteRow(table,obj)
{
   var tbl = document.getElementById(table);
   var rowNumber=getRow(obj).rowIndex;
 
if(tbl.id !='BankTable')
{
  if(rowNumber >1 && getControlInBranch(tbl.rows[rowNumber],'keyId').value == "")
  {
  
  tbl.deleteRow(rowNumber);
  }
  }
  else
  {
  if(rowNumber >2)
  {
   tbl.deleteRow(rowNumber);
  }
  }

}
function whichButtonMaster(tbl,obj,objr)
{
   if(objr=="config")
    addRowToTable(tbl,obj);
   if( objr=="bank")
   {
   
    addRowToTable(tbl,obj);
    }
   if( objr=="configVal"  )
    addRowToTable(tbl,obj);
}
function addRowToTable(tbl,obj)
{
<%
CommonsManager cmnsManger= EisManagersUtill.getCommonsManager();
AppConfig apObj= new AppConfig();
String key = "";
List appConfigList = null;
appConfigList  = (List)request.getAttribute("appDataKey");
for(Iterator it =appConfigList.iterator();it.hasNext();)
{
  apObj = (AppConfig)it.next();
  key = apObj.getKeyName();


%>
  var name = '<%=key%>';
 
 if(tbl==name)
   {
   tableObj=document.getElementById(tbl);
     
     var rowObj1=getRow(obj);
     var tbody=tableObj.tBodies[0];
   var lastRow = tableObj.rows.length;
   
   	   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
        tbody.appendChild(rowObj);
         var rownum=rowObj1.rowIndex+1;  
            getControlInBranch(tableObj.rows[rownum],'values').readOnly=false;
   	   getControlInBranch(tableObj.rows[rownum],'effectiveFrom').readOnly=false;
   	   getControlInBranch(tableObj.rows[rownum],'values').value="";
   	   getControlInBranch(tableObj.rows[rownum],'effectiveFrom').value="";
   	   getControlInBranch(tableObj.rows[rownum],'keyId').value="";
   	 
  	  
   }
  <%
  }
 %>
 if(tbl=='BankTable' )
 {
 tableObj=document.getElementById(tbl);
   
   var rowObj1=getRow(obj);
   var tbody=tableObj.tBodies[0];
  var lastRow = tableObj.rows.length;
  var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
       tbody.appendChild(rowObj);
       
 var rownum=rowObj1.rowIndex+1;  
          getControlInBranch(tableObj.rows[rownum],'bank').disabled=false;
	   getControlInBranch(tableObj.rows[rownum],'bankBranch').disabled=false;
	   getControlInBranch(tableObj.rows[rownum],'bankAccount').disabled=false;
  	   getControlInBranch(tableObj.rows[rownum],'bankEffectiveFrom').disabled=false;
  	   
  	   getControlInBranch(tableObj.rows[rownum],'bank').value="";
  	   getControlInBranch(tableObj.rows[rownum],'bankBranch').value="";
  	   getControlInBranch(tableObj.rows[rownum],'bankAccount').value="";
  	   getControlInBranch(tableObj.rows[rownum],'bankEffectiveFrom').value="";
  	   
  	   
 }
  

 

}

 function callBankBranch(obj){	
 		var bankId = obj.value;
 		var tbl = document.getElementById('BankTable');
 		var len = tbl.rows.length;
 		var br=document.getElementsByName("BankTable");
 		var row=getRow(obj).rowIndex-2;
 	       
 		
 		if(row ==0)
 		{
 		
 		for(var resLen=1;resLen<document.masterDataForm.bankBranch.length;resLen+1){
 		document.masterDataForm.bankBranch.options[resLen]=null;
 		}	
 		var count = 1;
 		<c:forEach var="bankBranchObj" items="${masterDataForm.bankBranchList}">	
 		if("${bankBranchObj.bank.id}" == bankId)
 		{				
 			document.masterDataForm.bankBranch.options[count] = new Option("${bankBranchObj.branchname}","${bankBranchObj.id}");
 			count=count+1;
 		}					
 		</c:forEach>	
 		callBankAccount(document.masterDataForm.bankBranch);
 		}
 		else 
 		{
 		
 		document.masterDataForm.bankBranch[row].options.length=0;
 					
 				var count = 1;
 				
 				<c:forEach var="bankBranchObj" items="${masterDataForm.bankBranchList}">	
 		if("${bankBranchObj.bank.id}" == bankId)
 				{				
 					document.masterDataForm.bankBranch[row].options[count]  = new Option("${bankBranchObj.branchname}","${bankBranchObj.id}");
 					count=count+1;
 				}					
 				</c:forEach>	
 		callBankAccount(document.masterDataForm.bankBranch[row]);
 		}
 	
 	}
 		 
 function callBankAccount(obj){	
 		
 		 var bankBranchId = obj.value;
 		
 		 	var tbl = document.getElementById('BankTable');
 		var len = tbl.rows.length;
 		var br=document.getElementsByName("BankTable");
 		var row=getRow(obj).rowIndex-2;
 		if(row ==0)
 		{
 		 for(var resLen=1;resLen<document.masterDataForm.bankAccount.length;resLen+1){
 		 				document.masterDataForm.bankAccount.options[resLen]=null;
 		 }	
 		 var count = 1;
 		 <c:forEach var="bankAccountObj" items="${masterDataForm.bankAccountList}">	
 			 if("${bankAccountObj.bankbranch.id}" == bankBranchId){				
 				document.masterDataForm.bankAccount.options[count] = new Option("${bankAccountObj.accountnumber}","${bankAccountObj.id}");
 				count=count+1;
 			 }					
 		 </c:forEach>
 		 }
 		 else
 		 {
 		
 		
 	                         document.masterDataForm.bankAccount[row].options.length=0;
 				
 				
 				 var count = 1;
 				 <c:forEach var="bankAccountObj" items="${masterDataForm.bankAccountList}">	
 					 if("${bankAccountObj.bankbranch.id}" == bankBranchId){				
 						document.masterDataForm.bankAccount[row].options[count] = new Option("${bankAccountObj.accountnumber}","${bankAccountObj.id}");
 						count=count+1;
 					 }					
 		 </c:forEach>
 		 
 		 }
		 }


</script>

</head>
<body>
<%
AppConfig ap= new AppConfig();
final Logger logger = Logger.getLogger("masterSetUp.jsp");
List appKeyList = null;
appKeyList = (List)request.getAttribute("appDataKey");
 String module =null;
System.out.println(">>>>>>>>>>>>>>>EisConstants.MODULE_KEY_FOR_BANK"+"EisConstants.MODULE_KEY_FOR_BANK");
System.out.println(">>>>>>>>>>>>>>>>>>>.l???????????ist"+appKeyList.size());
for(Iterator it = appKeyList.iterator();it.hasNext();)
{
  ap = (AppConfig)it.next();
    module = ap.getModule();
   Set appValSeT = ap.getAppDataValues();
  System.out.println(" appValSeT>>>>>"+ appValSeT.size()); 
  
}

%>

<html:form action="/empPension/MasterSetUpAction.do?submitType=createNewValues">
<P ALIGN=CENTER><font color="#0000A0">Master SetUp Screen for <%=module%></font></P>

<table  WIDTH=90%  cellpadding ="0" cellspacing ="0" border = "1" id="mainTable" name="mainTable" >

<c:forEach var="appConfig" items="${appDataKey}">
<c:choose>
<c:when test="${appConfig.keyName !='BankPension'}">

<input type=hidden name="keyName" id="keyName" value="${appConfig.keyName} " />
<tr id="keyRowId">
<td class="fieldcell">
<label for="description"> ${appConfig.description}</label>
</td>
</tr>
<td colspan="7">
<table id="${appConfig.keyName}" align="left" class="eGovInnerTable" border="0" cellpadding="0"cellspacing="0">
<tr>
<td class="labelcell">Value</td>
<td class="labelcell">Effective From</td>
</tr>
<c:choose>
<c:when test="${appConfig.appDataValues  != null && fn:length(appConfig.appDataValues) > 0}">
<c:forEach var="configValues" items="${appConfig.appDataValues}">
<tr>
<td class="labelcell" >
<input type=hidden name="count" id="count"  value="${appConfig.keyName}" />
<input type=hidden name="keyId" id="keyId"  value="${configValues.id}" />
<input type="text" id="values" name="values" value="${configValues.value}" readOnly>
</td>
<td class="labelcell">
<input type="text"  name="effectiveFrom"  id="effectiveFrom"   value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>" onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);" readOnly  />
</td>
<td>
 <input  onclick="deleteRow('${appConfig.keyName}',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="delF"> 
 </td>
 <td>
 <input  onclick="whichButtonMaster('${appConfig.keyName}',this,'configVal');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
  </td>
  </tr>
</c:forEach>
</c:when>
<c:otherwise>
<tr>
<td class="labelcell" >
<input type=hidden name="count" id="count" value="${appConfig.keyName}" />
<input type=hidden name="keyId" id="keyId"  value="" />
<input type="text" id="values" name="values"   >
</td>
<td class="labelcell">
<input type="text"  name="effectiveFrom"  id="effectiveFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);"  />
</td>
<td>
     <input  onclick="deleteRow('${appConfig.keyName}',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="delF"> 
  </td>
   <td>
   <input  onclick="whichButtonMaster('${appConfig.keyName}',this,'config');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
  </td>
  </tr>
</c:otherwise>
</c:choose>
</table>
</c:when>


<c:otherwise>
${appConfig.keyName}
<tr>
<td colspan="7">
<table id="BankTable" align="left" class="eGovInnerTable" border="0" cellpadding="0"cellspacing="0">
<input type=hidden name="keyName" id="keyName" value="${appConfig.keyName} " />

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

<tr id="BankRowId" >	
<c:set var= "configId" value="${configValues.value}" />
<% 
String configId = (String)pageContext.getAttribute("configId"); 
System.out.println(">>>>>>>>>>>>>>>"+configId);
%>		
	  	<td class="labelcell">
	  	<input type=hidden name="count" id="count"  value="${appConfig.keyName}" />
	  	
	  		<html:select  property="bank" styleClass="fieldcell" value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getBankbranch().getBank().getId().toString()%>"onblur="callBankBranch(this)" disabled="true">
				<html:option value="">-----------Select-----------</html:option>
				<c:forEach var="bankObj" items="${masterDataForm.bankList}">
					<html:option value="${bankObj.id}">${bankObj.name}</html:option>
				</c:forEach>
			</html:select>
	  	</td>
	
	
	
	 	
	  	<td class="labelcell">
	  		<html:select property="bankBranch" styleClass="fieldcell"value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getBankbranch().getId().toString()%>" onchange="callBankAccount(this);"disabled="true">
				<html:option value="">-----------Select-----------</html:option>
				<c:forEach var="bankBranchObj" items="${masterDataForm.bankBranchList}">
				<html:option value="${bankBranchObj.id}">${bankBranchObj.branchname}</html:option>
				</c:forEach>
			</html:select>
	  	</td>	  	
	  	
	  	<td class="labelcell">
			<html:select property="bankAccount"  styleClass="fieldcell" value="<%=cmnsManger.getBankaccountById(new Integer(Integer.parseInt(configId))).getId().toString()%>"disabled="true">
				<html:option value="">-----------Select-----------</html:option>
				<c:forEach var="bankAccountObj" items="${masterDataForm.bankAccountList}">
				<html:option value="${bankAccountObj.id}">${bankAccountObj.accountnumber}</html:option>
				</c:forEach>
			</html:select>	  		
	  	</td>
	  	
	  	<td class="labelcell">
	  	
                <input type="text"  name="bankEffectiveFrom"  id="bankEffectiveFrom"  value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>"onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);"disabled="true"/>
                </td>
                <td>
		     <input  onclick="deleteRow('BankTable',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="delF"> 
  </td>
                <td>
                 <input  onclick="whichButtonMaster('BankTable',this,'bank');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
  </td>
 </tr> 
 
  </c:forEach>
  </c:when>
 
 <c:otherwise>
 <tr id="BankRowId" >	
 <c:set var= "configId" value="${configValues.value}" />
 <% 
 String configId = (String)pageContext.getAttribute("configId"); 
 System.out.println(">>>>>>>>>>>>>>>"+configId);
 %>		
 	  	<td class="labelcell">
 	  	<input type=hidden name="bankName" id="bankName"  value="${appConfig.keyName}" />
 	  	
 	  		<html:select  property="bank" styleClass="fieldcell" onblur="callBankBranch(this)">
 				<html:option value="">-----------Select-----------</html:option>
 				<c:forEach var="bankObj" items="${masterDataForm.bankList}">
 					<html:option value="${bankObj.id}">${bankObj.name}</html:option>
 				</c:forEach>
 			</html:select>
 	  	</td>
 	
 	
 	
 	 	
 	  	<td class="labelcell">
 	  		<html:select property="bankBranch" styleClass="fieldcell"onchange="callBankAccount(this);">
 				<html:option value="">-----------Select-----------</html:option>
 				<c:forEach var="bankBranchObj" items="${masterDataForm.bankBranchList}">
 				<html:option value="${bankBranchObj.id}">${bankBranchObj.branchname}</html:option>
 				</c:forEach>
 			</html:select>
 	  	</td>	  	
 	  	
 	  	<td class="labelcell">
 			<html:select property="bankAccount"  styleClass="fieldcell" >
 				<html:option value="">-----------Select-----------</html:option>
 				<c:forEach var="bankAccountObj" items="${masterDataForm.bankAccountList}">
 				<html:option value="${bankAccountObj.id}">${bankAccountObj.accountnumber}</html:option>
 				</c:forEach>
 			</html:select>	  		
 	  	</td>
 	  	
 	  	<td class="labelcell">
 	  	
                 <input type="text"  name="bankEffectiveFrom"  id="bankEffectiveFrom"  value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>"onkeyup="DateFormat(this,this.value,event,false,'3')"onblur="validateDateFormat(this);"/>
                 </td>
                 <td>
		      <input  onclick="deleteRow('BankTable',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="delF"> 
  </td>
                 <td>
                  <input  onclick="whichButtonMaster('BankTable',this,'bank');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
   </td>
 </tr> 
 
 </c:otherwise>
 </c:choose>
 </table>
 </tr>

</c:otherwise>
</c:choose>
</c:forEach>

<tr>
<td colspan="10">
<table id = "submit" WIDTH="80%"  cellpadding ="0" cellspacing ="0" border = "1" >
<tr>
<td WIDTH="30%" align="center"><html:button styleClass="button" value="Save" property="b4"  onclick="validateOnSubmit();"  />
</td>
<tr>
</table>
</td>
</tr>

</table>
</html:form>
</body>
</html>