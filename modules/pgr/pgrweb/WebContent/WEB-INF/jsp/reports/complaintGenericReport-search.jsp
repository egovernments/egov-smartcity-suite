<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import=" org.egov.infstr.utils.AppConfigTagUtil" %>
<%@ page language="java"%>
<%@ include file="/includes/taglibs.jsp" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="org.egov.infstr.services.GeoLocationConstants"%>
<link type="text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/displaytag.css" rel="stylesheet"  />

<html>
<head>
	<title><s:text name="page.title.complaint.search" /></title>
</head>


<body class="yui-skin-sam">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>

	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
	<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevent caching at the proxy server
%>
	
	<s:form action="complaintGenericReport" theme="simple" name="complaintGenericReportForm" method="post">
	
	<s:push value="model">
			<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name = "complaint.search.title"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
<table width ="100%">	
<script>
function enable_date(o1,o2)
{

	var value = document.getElementById('dateselection').value ;

	if(value == "0")
	{
		  o1.disabled = true;
		  o2.disabled = true;

	}
	else if(value == "1")
	{
		o1.disabled = true;
		o2.disabled = false;
	}
	else if(value == "2")
	{
		o2.disabled = true;
		o1.disabled = false;

	}
	else if(value == "4")
	{
		  o1.disabled = true;
		  o2.disabled = true;
	}
    else if(value == "3" || value == "5")
	{
	    o1.disabled = false;
	    o2.disabled = false;

	}
}

function viewComplaintDtls(complaintId)
		{
		window.open("${pageContext.request.contextPath}/common/complaint!view.action?model.id="+complaintId,"","height=800,width=700,scrollbars=yes,left=0,top=0,status=yes");
			}
	
function checkGreater(fromDate,toDate)
	{
		//alert("checkGreater fromDate :"+fromDate+": to date :"+toDate);
		if(fromDate!= ""  && toDate!= "")
		{
			//var fromDate=value;
			var fromDateArr=fromDate.split("/");
			var toDateArr=toDate.split("/");

			var fromDateObj=new Date(fromDateArr[2],fromDateArr[1]-1,fromDateArr[0]);
			var toDateObj=  new Date(toDateArr[2],toDateArr[1]-1,toDateArr[0]);
			if(fromDateObj>toDateObj)
				return false;
			else 
				return true;
		}
		else
			return false;

	}

	function validateCurrDate(value)
	{
		//alert("checkGreater Date :"+value);
		if(value!="")
		{
			var fromDate=value;
			var fromDateArr=fromDate.split("/");
			var fromDateObj=new Date(fromDateArr[2],fromDateArr[1]-1,fromDateArr[0]);
			var currDateObj=new Date();
			if(fromDateObj>currDateObj)
				return false;
			else 
				return true;
		}
		else
			return false;
	}






function validation()
	{
	 // var value1 = document.getElementById('dateselection').value ;
	  var value2 = document.getElementById('complaintFromDate').value;
	  var value3 = document.getElementById('complaintToDate').value;
	 
	 
	/**	if(value1 == "0" || value1 == "4")
		//{
			if(value2 != "" || value3 != "")
			{
			//	alert('Please dont fill the parameters if date selection is "All Dates" or "Current"');
				document.getElementById('complaintFromDate').value = "";
				document.getElementById('complaintToDate').value = "";
				return false;
			}
	//	}
		else if(value2 == "" && value1 == "2")
		{
			alert('Please fill the date parameters according to the instructions given below');
			document.getElementById('complaintFromDate').value = "";
			document.getElementById('complaintToDate').value = "";
			return false;
		}
		else if(value3 == ""  && value1 == "1")
		{
			alert('Please fill the date parameters according to the instructions given below');
			document.getElementById('complaintFromDate').value = "";
			document.getElementById('complaintToDate').value = "";
			return false;
		}
		else if((value3 == "" || value2 == "") && value1 == "3")
		{
			alert('Please fill the date parameters according to the instructions given below');
			document.getElementById('complaintFromDate').value = "";
			document.getElementById('complaintToDate').value = "";
			return false;
		}**/
		if(value2 != "")
		{
		
			if(!validateCurrDate(value2))
				{
				alert("FromDate should be less than CurrentDate");
				document.getElementById('complaintFromDate').value = "";
				return false;
				}
		}
		if(value3 != "")
		{
		
			if(!validateCurrDate(value3))
				{
				alert("ToDate should be less than CurrentDate");
				document.getElementById('complaintToDate').value = "";
				return false;
				}
		}
		if(value2 != "" && value3 != "")
		{
		
			if(!checkGreater(value2,value3))
			{
				alert("FromDate should be less than ToDate ")
				document.getElementById('complaintFromDate').value = "";
				return false;
			}
			else
				return true;
		}
		return true;//if nothing catches or succeeds
	}
</script>
<!--  <tr>
		<td width="25%" class="greyboxwk"><s:text name="complaint.dateselection" /></td>
		<td width="25%" class="greybox2wk" align="left">
		<select name="dateselection" id="dateselection" onclick ="Javascript:enable_date(document.getElementById('complaintFromDate'),document.getElementById('complaintToDate'));">
		<option value="0"><s:text name='All Dates' /></option>
		<option value="1"> Before </option>
		<option value="2"> After </option>
		<option value="3"> Between </option>
		<option value="4"> Current </option>
		</select> --><tr>
		</td>
		<td width="25%" class="greyboxwk"><s:text name="complaint.status" /></td>
		<td width="25%" class="greybox2wk">
	 <s:select name="redressal.complaintStatus" id="complaintStatus" list="dropdownData.complaintStatus" listKey="id" 
		listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{redressal.complaintStatus.id}" disabled="false"/></td>
 	<td width="25%" class="greybox2wk"></td><td width="25%" class="greybox2wk"></td>
 	</tr>
			
<tr>
		<td width="25%" class="whiteboxwk"><s:text name="complaint.from.date" /></td>
		<td width="25%" class="whitebox2wk"><span class="whiteboxwk"> 
		<s:date name="complaintFromDate" id="compFrmDateFmtd" format="dd/MM/yyyy" /> 
		<s:textfield name="complaintFromDate" id="complaintFromDate"  onblur="validateDateFormat(this);" maxlength="10" size="10" value="%{compFrmDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" readonly = "false" /> 
		<a href="javascript:show_calendar('forms[0].complaintFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> 
		<img src="${pageContext.request.contextPath}/images/calendaricon.jpg" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a></span></td>
		<td width="25%" class="whiteboxwk"><s:text name="complaint.to.date" /></td>
		<td width="25%" class="whitebox2wk"><s:date name="complaintToDate" id="compToDateFmtd" format="dd/MM/yyyy" />
		<s:textfield name="complaintToDate" id="complaintToDate"  onblur="validateDateFormat(this);" maxlength="10" size="10" value="%{compToDateFmtd}" tabindex="4" onkeyup="DateFormat(this,this.value,event,false,'3')" readonly = "false"/>
		<a href="javascript:show_calendar('forms[0].complaintToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
	    <img src="${pageContext.request.contextPath}/images/calendaricon.jpg" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" /> </a></td>
</tr>

         <jsp:include page="../common/boundary.jsp"/>

<tr>
						<td width="25%" class="whiteboxwk"><s:text name="complaint.departments" /></td>
					    <td width="25%" class="whitebox2wk" >
						<s:select id="departmentId" name="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" 
					         headerKey="-1"  headerValue="----Choose----" value="%{department.id}" onChange="setupAjaxDepartment(this);" /></td>
						<egov:ajaxdropdown id="populateUser" fields="['Text','Value']" dropdownId='user'url='common/ajaxPgr!populateUser.action' />
						<td width="25%" class="whiteboxwk"><s:text name="complaint.department.users" /></td>
						<td width="25%" class="whitebox2wk">
						<s:select id="user" name="user" cssClass="selectwk" list="dropdownData.userList" listKey="id" listValue="userName" 
					       headerKey="-1" headerValue="----Choose----" /></td>
						
</tr>
<script>
	function setupAjaxDepartment(elem){
   		 department_id=elem.options[elem.selectedIndex].value;
    		populateuser({departmentId:department_id});
	}
</script>
<tr>
		<td width="25%" class="greyboxwk"><s:text name="complaint.receiving" /></td>
		<td width="25%" class="greybox2wk">
		<s:select name="receivingCenter" id="receivingCenter" list="dropdownData.receivingCenter" listKey="id" 
		listValue="centerName" headerKey="-1" headerValue="----Choose----"  value="%{receivingCenter.id}" disabled="false"/></td>
		<td width="25%" class="greyboxwk"><s:text name="complaint.othercomplainttypes" /></td>
		<td width="25%" class="greybox2wk">
		<s:textfield name="othervalue" id="othervalue" maxlength="50"/></td>
</tr>
</table>
<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<div class="buttonbottom">
		<s:submit name="button32" cssClass="buttonsubmit" id="button32" method="search" value="Search" onclick="return validation();" />
		<s:reset name="button" cssClass="button" id="button" value="Reset" />
		<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close" />
</div>
</tr>
</table>

				   </table>
				   </s:push>
			
			
			
			
			<%  if(null!= request.getAttribute(GeoLocationConstants.GEOLOCATIONLIST_ATTRIBUTE)) {%>
			<jsp:include page="../common/legend.jsp"/>
	<table border="1" width="870" align="center" bgcolor="#EFEFEF"  cellspacing="0"  height="5"><tr>
		<TD width="500" height="300"><div id="map_canvas" style="width:100%; height:100%"></div> </TD></tr>
         	 <c:import url="/commons/googleMapReport.jsp" context="/egi"></c:import>
	</table> <% } int markerIndex= 0;%>
			
			
			<div id="tableData" align="center">
       		 <div id="displaytbl" align="center">	
    		  	<display:table pagesize="10" name="complaintDtlsList" export="false" requestURI="" id="myComplaintsListid" size="2" class="its" uid="currentRowObject" >
    			<div STYLE="display: table-header-group" align="center">
      			
      			 <display:column  title="Map View" style="width:4%;text-align:center">
						<img src="/egi/images/map_icon.png" onclick="javascript:showMarker(<%=markerIndex++%>);" style="width: 0.4;height: 0.4 ;cursor:pointer; cursor:hand;"/>
				</display:column>	
								<display:column  title="Complaint Number"
									style="text-align:center;width:10px;">
									<a onclick="viewComplaintDtls('${currentRowObject.id}')" href="#" >
									<s:if test="%{(#attr.currentRowObject.priority.id)==0}">
									<font color="blue" size="-1" >	${currentRowObject.complaintNumber} </font>
									</s:if>
									<s:if test="%{(#attr.currentRowObject.priority.id)==1}">
									<font color="red"  size="-1">	${currentRowObject.complaintNumber} </font>
									</s:if>
									<s:if test="%{(#attr.currentRowObject.priority.id)==2}">
									<font color="yellow"  size="-1">	${currentRowObject.complaintNumber} </font>
									</s:if>
									<s:if test="%{(#attr.currentRowObject.priority.id)==3}">
									<font color="green"  size="-1">	${currentRowObject.complaintNumber} </font>
									</s:if>
									</a>
									</display:column>
								<display:column property="title" title="Complaint Title"
									style="text-align:center;width:20px;" />
									<display:column property="redressal.redressalOfficer.userName" title="User"
									style="text-align:center;width:10px;" />
									<display:column property="locBndry.bndryNameLocal" title="Location"
									style="text-align:center;width:10px;" />
									<display:column property="redressal.complaintStatus.name" title="Status"
									style="text-align:center;width:10px;" />
									<display:column property="department.deptName" title="Department"
									style="text-align:center;width:10px;" />
									<display:column  title="Last Viewed On" style="text-align:center;width:10px;" >
								<s:property value=" %{getViewedBy(#attr.currentRowObject.complaintNumber)} "/>
									</display:column>
									<display:column property="complaintDate" title="Registration Date"
									style="text-align:center;width:10px;" format="{0,date,dd-MM-yyyy}" />
									<display:column property="expiryDate" title="Expiry Date"
									style="text-align:center;width:10px;" format="{0,date,dd-MM-yyyy}" />
								</div>						
			</display:table>				
		</div>
  	    
  	   </div>
			 </s:form>				
	   </body>
</html>