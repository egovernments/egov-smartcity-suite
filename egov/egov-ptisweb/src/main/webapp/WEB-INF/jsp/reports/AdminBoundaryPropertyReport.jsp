<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>eGov- Property Tax-Admin Boundary Property Report</title>
<script type="text/javascript">

<jsp:useBean id="today" class="java.util.Date" />	
  <fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${today}" />
  
function getLocationHirarchy(type,boundaryType)
     {
     	var boundaryId;
		var wardNo=document.getElementById("wardNo").value; 
		boundaryId = wardNo;

  		var link = "/ptis/commonjsp/getLocationHirarchy.jsp?type=" + type+"&bndryId=" + boundaryId + "&bndryType=" + boundaryType + "";
  		var request = initiateRequest();
  		var isUnique;
  		request.onreadystatechange = function() 
  		{	 
  			if (request.readyState == 4) 
  			{
  				if (request.status == 200) 
  				{
  					var response = request.responseText;
  					var result = response.split("**");
  					if(result!=null)
  					{
  						street = result[0].split("^");
						loadBoundaryCombo(street,'streetNameList');				
  					}
  				}
  			}
  		};
  		request.open("GET", link, true);
  		request.send(null);
  		return isUnique;
  }
   function loadBoundaryCombo(val1,val2)
   {
   	var boundaryOpt = document.getElementById(val2);
   	while (boundaryOpt.options.length > 0) {
   	boundaryOpt.remove(0);
   	}
   	boundaryOpt.options[0] = new Option('--Choose--',0);
   	var j=1;
   	for(i = 0; i  <  val1.length-1; i++)
   	{
   		bboundary = val1[i].split("*");
   		boundary_name = bboundary[1];
   		boundary_id = bboundary[0];
   		boundaryOpt.options[j] = new Option(boundary_name,boundary_id);
   		j++;		
   	} 
   }
   
function checkBeforeSubmitReportDetails()
{
	fromSitalArea = document.adminBndryPropReportForm.fromSitalArea.value;
 	toSitalArea = document.adminBndryPropReportForm.toSitalArea.value;
 	fromBuiltUpArea = document.adminBndryPropReportForm.fromBuiltUpArea.value;
	toBuiltUpArea = document.adminBndryPropReportForm.toBuiltUpArea.value;
	fromCurrentDemand = document.adminBndryPropReportForm.fromCurrentDemand.value;
	toCurrentDemand = document.adminBndryPropReportForm.toCurrentDemand.value;
	fromArrearDemand = document.adminBndryPropReportForm.fromArrearDemand.value;
	toArrearDemand = document.adminBndryPropReportForm.toArrearDemand.value;
	fromTotalDemand = document.adminBndryPropReportForm.fromTotalDemand.value;
	toTotalDemand = document.adminBndryPropReportForm.toTotalDemand.value;

	if(document.adminBndryPropReportForm.zoneNo.options[document.adminBndryPropReportForm.zoneNo.selectedIndex].text=="Choose")
		{
			bootbox.alert("Please select Zone Number");
			document.adminBndryPropReportForm.zoneNo.focus();
			return false;
		}
	
	if(((fromSitalArea == "From Area" || fromSitalArea == "") && (toSitalArea != "To Area" && toSitalArea != "")) || 
		((fromSitalArea != "From Area" && fromSitalArea !="") && (toSitalArea == "To Area" || toSitalArea == "")))
		{
			bootbox.alert("In case of Sital Area, Both From Area and To Area are mandatory");
			document.adminBndryPropReportForm.fromSitalArea.focus();
			return false;
		}
	if(((fromBuiltUpArea == "From Area" || fromBuiltUpArea == "") && (toBuiltUpArea != "To Area" && toBuiltUpArea != "")) || 
		((fromBuiltUpArea != "From Area" && fromBuiltUpArea !="") && (toBuiltUpArea == "To Area" || toBuiltUpArea == "")))
		{
			bootbox.alert("In case of Total Built Up Area, Both From Area and To Area are mandatory");
			document.adminBndryPropReportForm.fromBuiltUpArea.focus();
			return false;
		}
	if(((fromCurrentDemand == "From Demand" || fromCurrentDemand == "") && (toCurrentDemand != "To Demand" && toCurrentDemand != "")) || 
		((fromCurrentDemand != "From Demand" && fromCurrentDemand !="") && (toCurrentDemand == "To Demand" || toCurrentDemand == "")))
		{
			bootbox.alert("In case of Current Demand, Both From Demand and To Demand are mandatory");
			document.adminBndryPropReportForm.fromCurrentDemand.focus();
			return false;
		}
	if(((fromArrearDemand == "From Demand" || fromArrearDemand == "") && (toArrearDemand != "To Demand" && toArrearDemand != "")) || 
		((fromArrearDemand != "From Demand" && fromArrearDemand !="") && (toArrearDemand == "To Demand" || toArrearDemand == "")))
		{
			bootbox.alert("In case of Arrear Demand, Both From Demand and To Demand are mandatory");
			document.adminBndryPropReportForm.fromArrearDemand.focus();
			return false;
		}
	if(((fromTotalDemand == "From Demand" || fromTotalDemand == "") && (toTotalDemand != "To Demand" && toTotalDemand != "")) || 
		((fromTotalDemand != "From Demand" && fromTotalDemand !="") && (toTotalDemand == "To Demand" || toTotalDemand == "")))
		{
			bootbox.alert("In case of Total Demand, Both From Demand and To Demand are mandatory");
			document.adminBndryPropReportForm.fromTotalDemand.focus();
			return false;
		}
		
		
		
	if((fromSitalArea != "From Area" && fromSitalArea != "") && (toSitalArea != "To Area" && toSitalArea != ""))
		{
			if(fromSitalArea > toSitalArea)
			{
				bootbox.alert("To Sital Area should be greater than From Sital Area");
				document.adminBndryPropReportForm.fromSitalArea.focus();
				return false;
			}
		}
	if((fromBuiltUpArea != "From Area" && fromBuiltUpArea != "") && (toBuiltUpArea != "To Area" && toBuiltUpArea != ""))
		{
			if(fromBuiltUpArea > toBuiltUpArea)
			{
				bootbox.alert("To Built Up Area should be greater than From Built Up Area");
				document.adminBndryPropReportForm.fromBuiltUpArea.focus();
				return false;
			}
		}
	if((fromCurrentDemand != "From Demand" && fromCurrentDemand != "") && (toCurrentDemand != "To Demand" && toCurrentDemand != ""))
		{
			if(fromCurrentDemand > toCurrentDemand)
			{
				bootbox.alert("To Current Demand should be greater than From Current Demand");
				document.adminBndryPropReportForm.fromCurrentDemand.focus();
				return false;
			}
		}
	if((fromArrearDemand != "From Demand" && fromArrearDemand != "") && (toArrearDemand != "To Demand" && toArrearDemand != ""))
		{
			if(fromArrearDemand > toArrearDemand)
			{
				bootbox.alert("To Arrear Demand should be greater than From Arrear Demand");
				document.adminBndryPropReportForm.fromArrearDemand.focus();
				return false;
			}
		}
	if((fromTotalDemand != "From Demand" && fromTotalDemand != "") && (toTotalDemand != "To Demand" && toTotalDemand != ""))
		{
			if(fromTotalDemand > toTotalDemand)
			{
				bootbox.alert("To Total Demand should be greater than From Total Demand");
				document.adminBndryPropReportForm.fromTotalDemand.focus();
				return false;
			}
		}
}

function loadOnStartUp()
{ 
		waterMarkInitialize('fromSitalAreaId','From Area');
		waterMarkInitialize('toSitalAreaId','To Area');
		waterMarkInitialize('fromBuiltUpAreaId','From Area');
		waterMarkInitialize('toBuiltUpAreaId','To Area');
		waterMarkInitialize('fromCurrentDemandId','From Demand');
		waterMarkInitialize('toCurrentDemandId','To Demand');
		waterMarkInitialize('fromArrearDemandId','From Demand');
		waterMarkInitialize('toArrearDemandId','To Demand');
		waterMarkInitialize('fromTotalDemandId','From Demand');
		waterMarkInitialize('toTotalDemandId','To Demand');
}

function checkFromArea(obj)
{
	fromArea = obj.value;
	if(fromArea < 0 && fromArea != 'From Area')
	{
		bootbox.alert("From Area should be greater than or equal to 0");
		obj.value="";
		obj.focus();
		return false;
	}
}

function checkToArea(obj)
{
	toArea = obj.value;
	if(toArea >= 9999999 && toArea != 'To Area')
	{
		bootbox.alert("To Area should be less than 99,99,999");
		obj.value="";
		obj.focus();
		return false;
	}
}

function checkFromDemand(obj)
{
	fromDemand = obj.value;
	if(fromDemand < -99999999 && fromDemand != 'From Demand')
	{
		bootbox.alert("From Demand should be greater than or equal to -9,99,99,999");
		obj.value="";
		obj.focus();
		return false;
	}
}

function checkToDemand(obj)
{
	toDemand = obj.value;
	if(toDemand >= 99999999 && toDemand != 'To Demand')
	{
		bootbox.alert("To Demand should be less than 9,99,99,999");
		obj.value="";
		obj.focus();
		return false;
	}
}

function checkForNumeric(obj,msg,text)
{
	var objt = obj;
	var value = obj.value;
	if ((value != null) && (value !="") && (value != text))
	{
		if(isNaN(value))
		{
			bootbox.alert("Please Enter valid "+msg);
			objt.value="";
			objt.focus();
			return false;
		}
	}
}

</script>
</head>

<html:form action="/reports/adminBndryPropReportResult.do">
<body onload="loadOnStartUp();">
<%
String searchValue=(String)request.getAttribute("searchValue");
%>
<div class="formmainbox"><div class="formheading"></div>
<div class="headingbg"><bean:message key="admBndry.header"/></div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

	<tr>
	<td class="bluebox" width="5%">&nbsp;</td>
	<td class="bluebox" colspan="4">
	<table width="50%" border="0" align="left"  cellspacing="0" cellpadding="2">
	<tr>
	    <jsp:include page="../search/BndryPluginForZoneWardStreet.jsp">
		<jsp:param name="hType" value="ADMINISTRATION"/>
		<jsp:param name="bType" value="ZONE"/>
		<jsp:param name="zone" value="true"/>
		<jsp:param name="ward" value="false"/>
		<jsp:param name="streetExist" value="true"/>
		</jsp:include>
	</tr></table>
	</td>
    </tr>
    
     <tr>
     <td class="greybox" width="10%">&nbsp;</td>
     <td class="greybox"><bean:message key="streetName"/>:</td>
      <td class="greybox" colspan="3"><label>
      <select name="street" id="streetNameList" class="selectnew">
      	  <option value ="0">--Choose--</option>	  
      </select>
      </label>
    </td>
     </tr>
   
     <tr>
      <td class="bluebox" width="10%">&nbsp;</td>
      <td class="bluebox"><bean:message key="typeOfBuilding"/>:</td>
      <td class="bluebox" colspan="3">
      
      <html:select property="propertyType" styleClass="selectnew">
    	 <html:option value="0">--Choose--</html:option>
    	 <html:options collection="PropertyTypeMasterList" property="id" labelProperty="type"/>	          
	  </html:select>
      </td>	
    </tr>
    
    <tr>
      <td class="greybox" width="10%" >&nbsp;</td>
      <td class="greybox"><bean:message key="propUsage"/>:</td>
      <td class="greybox" colspan="3">
      <html:select  property="propertyUsage" styleClass="selectnew">
	    	<html:option value="0">--Choose--</html:option>
	    	<html:options collection="PropertyUsgMasterList" property="idUsage" labelProperty="usageName"/>
	    </html:select>
      </td>	
    </tr>

	<tr>
      <td class="bluebox" width="10%">&nbsp;</td>
      <td class="bluebox"><bean:message key="propStatus"/>:</td>
      <td class="bluebox" colspan="3">
      <html:select  property="propertyStatus" styleClass="selectnew">
	    	<html:option value="0">--Choose--</html:option>
	    	<html:options collection="PropertyStatusMasterList" property="ID" labelProperty="name"/>
	    </html:select>
      </td>	
    </tr>
	
     <tr>
      <td class="greybox" width="10%">&nbsp;</td>
      <td class="greybox"><bean:message key="sitalArea"/>:</td>
      <td class="greybox"><html:text  property="fromSitalArea" maxlength="64" styleId="fromSitalAreaId" onblur = "trim(this,this.value);waterMarkTextOut('fromSitalAreaId','From Area');checkFromArea(this);checkForNumeric(this,'From Sital Area','From Area');" onfocus = "waterMarkTextIn('fromSitalAreaId','From Area');"/>  </td>	
      <td class="greybox"><html:text  property="toSitalArea" maxlength="64" styleId="toSitalAreaId" onblur = "trim(this,this.value);waterMarkTextOut('toSitalAreaId','To Area');checkToArea(this);checkForNumeric(this,'To Sital Area','To Area');" onfocus = "waterMarkTextIn('toSitalAreaId','To Area');"/>  </td>
      <td class="greybox"><span class="highlight2"><bean:message key="areaMndtry"/></span></td>
    </tr>
  
     <tr>
      <td class="bluebox" width="10%">&nbsp;</td>
      <td class="bluebox"><bean:message key="totalBuiltUpArea"/>:</td>
      <td class="bluebox"><html:text  property="fromBuiltUpArea" maxlength="64" styleId="fromBuiltUpAreaId" onblur = "trim(this,this.value);waterMarkTextOut('fromBuiltUpAreaId','From Area');checkFromArea(this);checkForNumeric(this,'From Built Area','From Area');" onfocus = "waterMarkTextIn('fromBuiltUpAreaId','From Area');"/>  </td>	
      <td class="bluebox"><html:text  property="toBuiltUpArea" maxlength="64" styleId="toBuiltUpAreaId" onblur = "trim(this,this.value);waterMarkTextOut('toBuiltUpAreaId','To Area');checkToArea(this);checkForNumeric(this,'To Built Up Area','To Area');" onfocus = "waterMarkTextIn('toBuiltUpAreaId','To Area');"/>  </td>
      <td class="greybox"><span class="highlight2"><bean:message key="totalAreaMndtry"/></span></td>
    </tr>
    
    <tr>
      <td class="greybox" width="10%">&nbsp;</td>
      <td class="greybox"><bean:message key="currDemand"/>:</td>
      <td class="greybox"><html:text  property="fromCurrentDemand" maxlength="64" styleId="fromCurrentDemandId" onblur = "trim(this,this.value);waterMarkTextOut('fromCurrentDemandId','From Demand');checkFromDemand(this);checkForNumeric(this,'From Current Demand','From Demand');" onfocus = "waterMarkTextIn('fromCurrentDemandId','From Demand');"/>  </td>	
      <td class="greybox"><html:text  property="toCurrentDemand" maxlength="64" styleId="toCurrentDemandId" onblur = "trim(this,this.value);waterMarkTextOut('toCurrentDemandId','To Demand');checkToDemand(this);checkForNumeric(this,'To Current Demand','To Demand');" onfocus = "waterMarkTextIn('toCurrentDemandId','To Demand');"/>  </td>
      <td class="greybox"><span class="highlight2"><bean:message key="currentDemandMndtry"/></span></td>
    </tr>

     <tr>
      <td class="bluebox" width="10%">&nbsp;</td>
      <td class="bluebox"><bean:message key="arrearDemand"/>:</td>
      <td class="bluebox"><html:text  property="fromArrearDemand" maxlength="64" styleId="fromArrearDemandId" onblur = "trim(this,this.value);waterMarkTextOut('fromArrearDemandId','From Demand');checkFromDemand(this);checkForNumeric(this,'From Arrear Demand','From Demand');" onfocus = "waterMarkTextIn('fromArrearDemandId','From Demand');"/>  </td>	
      <td class="bluebox"><html:text  property="toArrearDemand" maxlength="64" styleId="toArrearDemandId" onblur = "trim(this,this.value);waterMarkTextOut('toArrearDemandId','To Demand');checkToDemand(this);checkForNumeric(this,'To Arrear Demand','To Demand');" onfocus = "waterMarkTextIn('toArrearDemandId','To Demand');"/>  </td>
      <td class="greybox"><span class="highlight2"><bean:message key="arrearDemandMndtry"/></span></td>
    </tr>
	
     <tr>
      <td class="greybox" width="10%">&nbsp;</td>
      <td class="greybox"><bean:message key="totalDemand"/>:</td>
      <td class="greybox"><html:text  property="fromTotalDemand" maxlength="64" styleId="fromTotalDemandId" onblur = "trim(this,this.value);waterMarkTextOut('fromTotalDemandId','From Demand');checkFromDemand(this);checkForNumeric(this,'From Total Demand','From Demand');" onfocus = "waterMarkTextIn('fromTotalDemandId','From Demand');"/>  </td>	
      <td class="greybox"><html:text  property="toTotalDemand" maxlength="64" styleId="toTotalDemandId" onblur = "trim(this,this.value);waterMarkTextOut('toTotalDemandId','To Demand');checkToDemand(this);checkForNumeric(this,'To Total Demand','To Demand');" onfocus = "waterMarkTextIn('toTotalDemandId','To Demand');"/>  </td>
      <td class="greybox"><span class="highlight2"><bean:message key="totalDemandMndtry"/></span></td>
    </tr>
   
</table>

<font size="1"><div align="left" class="mandatory"><bean:message key="mandtryFlds"/></div></font>
<table>
<tr>
 <c:if test="${fn:length(links)==0 && fn:contains(msg,'yes')}" >
 <div class="searchvalue1" align="left"><font color="red"><b>Search Criteria:</b></font> <%=searchValue%></div>
  <div class="searchvalue1" align="left">
  	<p align="left"><font color="red"><b>Total Properties:</b></font><c:out value="${fn:length(links)-1}"/></p>
  	<p align="right"><font color="red"><b>Today's Date:</b></font> <c:out value="${currDate}" /></p>
  </div>
  <div class="headingbg"><font color="red"><bean:message key="noPropMsg"/></font></div>
 </c:if>
</tr>

<tr>
<c:if test="${fn:length(links)>1000}">
<div class="searchvalue1" align="left"><font color="red"><b>Search Criteria:</b></font> <%=searchValue%></div>
<div class="searchvalue1" align="left">
  	<p align="left"><font color="red"><b>Total Properties:</b></font><c:out value="${fn:length(links)-1}"/></p>
  	<p align="right"><font color="red"><b>Today's Date:</b></font> <c:out value="${currDate}" /></p>
  </div>
	<% String resultExceed=(String)request.getAttribute("resultExceeds");%>
 	<div class="headingbg"><font color="red"> <%=resultExceed%></font></div>
</c:if>
</tr>
</table>

<div class="buttonbottom" align="center">
<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
 <input type="submit" name="button3" id="button3" value="Search" align="center" class="buttonsubmit" onclick = "return checkBeforeSubmitReportDetails();"/>
</div>

</div>
 
 <div class="formmainbox">
  <c:if test = "${fn:length(links)>0 && fn:length(links)<=1000}">
  <div class="formheading"></div>
  <div class="searchvalue1" align="left"><font color="red"><b>Search Criteria:</b></font> <%=searchValue%></div>
  <div class="searchvalue1" align="left">
  	<p align="left"><font color="red"><b>Total Properties:</b></font><c:out value="${fn:length(links)-1}"/></p>
  	<p align="right"><font color="red"><b>Today's Date:</b></font> <c:out value="${currDate}" /></p>
  </div>
  <div class="headingbg"><bean:message key="adminPropReportResult" /></div>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
    <tr>
    <display:table name="links" export="true" class="tablebottom" style="width:100%" requestURI="/reports/adminBndryPropReportResult.do">
    <display:column property="serialNo" title="Serial Number" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="billNumber" title="Bill Number" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
	<display:column property="ownerName"  title="Owner Name" headerClass="bluebgheadtd" class="blueborderfortd" style="width:17%;text-align:left" />	
	<display:column property="propAddress"  title="Property Address"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:left" />
	<display:column property="propertyUsage"  title="Property Usage"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:center" />
	<display:column property="aggregateCurrentDemand" title="Current Demand"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:right" />
	<display:column property="aggregateArrearDemand" title="Arrear Demand"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:13%;text-align:right" />
	<display:column property="totalDemand" title="Total Demand" headerClass="bluebgheadtd" class="blueborderfortd" style="width:20%;text-align:right" />
	
	<display:setProperty name="paging.banner.item" value="Record"/>
	<display:setProperty name="paging.banner.items_name" value="Records"/>
    </display:table>
    </tr>
</table>
</c:if>
 </div>

</body>
</html:form>

</html>
