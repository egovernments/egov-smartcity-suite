
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		         org.egov.infstr.utils.*,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.client.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 org.egov.pims.model.*,
		 		 org.egov.pims.service.*,
		 		 org.egov.pims.commons.dao.DesignationMasterDAO,
		 		 org.egov.commons.CFinancialYear,
		 		 org.egov.commons.dao.CommonsDaoFactory,
		 		 org.egov.commons.dao.FinancialYearDAO,
		 		 org.egov.infstr.commons.*,
		 		 org.egov.pims.commons.client.*,
		 		 org.egov.infstr.commons.dao.*,
		 		 org.egov.commons.service.*,
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
		 		 org.egov.lib.address.dao.*,
		 		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Leave Master</title>


    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>

     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>


<%
String designationId = request.getParameter("Id").trim();
String designationName="";
Map mapOfDesignation = (Map)session.getAttribute("mapOfDesignation");
for (Iterator it = mapOfDesignation.entrySet().iterator(); it.hasNext(); )
{
			Map.Entry entry = (Map.Entry) it.next();
			if(((Integer)entry.getKey()).intValue() == new Integer(designationId).intValue())
			{
				designationName=(String)entry.getValue();
			}
}


Set tpSet = null;
EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
List leaveMasterList = 	empLeaveServiceImpl.getListOfLeaveMastersForDesID(new Integer(designationId));
if(leaveMasterList.isEmpty())
	leaveMasterList.add(new LeaveMaster());

SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<script>

var length =0;

function resetRowValues(lastRow,name)
{
	
			document.leaveMasterForm.typeOfLeaveMstr[lastRow].value="0";
			document.leaveMasterForm.noOfDays[lastRow].value="";
			length =lastRow;


}


function addRow(obj,tableName,row)
{

    	var tbl=tableName;
    	var rowO=tbl.rows.length;
    	var name=obj.value;

        	if(row != null)
        	{
        		var tbody=tbl.tBodies[0];
        		rIndex = 0;
    			var lastRow = tbl.rows.length;
    			var rowObj = row.cloneNode(true);
    			rIndex = rowObj.rowIndex;
    			tbody.appendChild(rowObj);
    			document.leaveMasterForm.leaveMstrId[lastRow-1].value = 0;
    			document.leaveMasterForm.leaveTypeId[lastRow-1].value = 0;  
    			resetRowValues(lastRow-1,name);
		}


}
var firstLength = 0;
var rIndex = 0;
var leaveTypeArray;

  function setLength()
  {
 	var tbl=document.getElementById('TPTable');
 	var rowo=tbl.rows.length;
 	firstLength = rowo;
 	for(i=1;i<rowo;i++)
    	{

    		var rowObj = document.getElementById('TPTable').rows[i];

    		var typeOfLeaveMstr = getControlInBranch(rowObj,"typeOfLeaveMstr");
    		leaveTypeArray[i-1]= typeOfLeaveMstr.options[typeOfLeaveMstr.selectedIndex].value;



    	}



  }
  function deleteRow(obj,tableName,addButtion)
  {
      var tbl=tableName;
      var rowo=tbl.rows.length;
      //alert('firstLength'+firstLength);

      if(rowo<=firstLength)
  	{
  		alert('<bean:message key="alertCannotDelete"/>');
  		return false;
  	}
  	else
  	{

  		tbl.deleteRow(rIndex);
  		return true;
  	}
  }





function ButtonPressNew(arg)
{



	if(arg == "savenew")
	{

		var tbl= document.getElementById("TPTable");
			var rows= tbl.rows.length;

			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					if(document.leaveMasterForm.typeOfLeaveMstr[i].value==0 )
					{
						alert('<bean:message key="alertChoosetypeOfLv"/>');
						document.leaveMasterForm.typeOfLeaveMstr[i].focus();
						return false;
					}
					if(document.leaveMasterForm.noOfDays[i].value =="" || document.leaveMasterForm.noOfDays[i].value ==0 )
					{
						alert('<bean:message key="alertFillNoOfDays"/>');
						document.leaveMasterForm.noOfDays[i].focus();
						return false;
					}


				}
			}
			else
			{				
				if(document.leaveMasterForm.typeOfLeaveMstr.value==0 )
				{
					alert('<bean:message key="alertChoosetypeOfLv"/>');
					document.leaveMasterForm.typeOfLeaveMstr.focus();
					return false;
				}

				if(document.leaveMasterForm.noOfDays.value=="" || document.leaveMasterForm.noOfDays.value==0 )
				{
					alert('<bean:message key="alertFillNoOfDays"/>');
					document.leaveMasterForm.noOfDays.focus();
					return false;
				}

			}

		document.leaveMasterForm.action = "<%=request.getContextPath()%>/leave/AfterLeaveMasterAction.do?submitType=saveLeaveMaster";
		document.leaveMasterForm.submit();
	}

}

function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("leaveMasterForm").action = "<%=request.getContextPath()%>/staff/index.jsp";
		document.forms("leaveMasterForm").submit();
	}


}
function setArrayLength()
{

<%
Map leaveMap =(Map)session.getAttribute("typeOfLeaveMap");
%>
	var lengthOfLT = '<%=leaveMap.keySet().size()%>';

	leaveTypeArray= new Array(lengthOfLT);

}
function populateHidden(obj)
{
	var row = getRow(obj);
	var leaveTypeId = getControlInBranch(row,"leaveTypeId");
	leaveTypeId.value = obj.value;
}
function populateTypeArry(obj)
{
	var row = getRow(obj);
	var typeOfLeaveMstr = getControlInBranch(row,"typeOfLeaveMstr");
	var rIndex = row.rowIndex;
	for(i=0;i<leaveTypeArray.length;i++)
	{
		if(leaveTypeArray[i]!=null)
    		{
    			if(leaveTypeArray[i] == typeOfLeaveMstr.value)
    			{
    				alert('<bean:message key="alertTypeChoosen"/>');
    				obj.focus();
    				var leaveTypeId = getControlInBranch(row,"leaveTypeId");
    				typeOfLeaveMstr.options.value = leaveTypeId.value;
    				return false;
    			}


    		}
	}
	leaveTypeArray[rIndex-1] = typeOfLeaveMstr.value;
	//alert('leaveTypeArray'+leaveTypeArray);
}

</script>
</head>
<body onLoad = "setArrayLength();setLength()"/>


		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">



<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->


<center>
<html:form  action="/leave/AfterLeaveMasterAction.do?submitType=saveLeaveMaster" >
<input type=hidden name="designationId" id="designationId" value="<%= designationId.trim() %> " />
<table  width="100%" cellpadding ="0" cellspacing ="0" border = "0"  >
<tbody>


  <tr>
                <td colspan="5" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="LeaveMaster"/></div></td>
              </tr>

		<tr>
		

<tr>
<td class = "bold"><b><font size="1"><bean:message key="Designation"/>:&nbsp;< <%=designationName%> ></td>

</tr>
</table>
<br>

 <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="TPTable" name="TPTable" >
   <tbody>
   <tr>

   <td   class="greybox2wk" ><span class="mandatory">*</span><bean:message key="LeaveTypeApp"/></td>
   <td   class="greybox2wk" ><span class="mandatory">*</span><bean:message key="NumOfDays"/></td>






  </tr>

 <%
	Iterator itr= leaveMasterList.iterator();
	String type="";
	for(int i=0;itr.hasNext();i++)
	{
		LeaveMaster leaveMaster = (LeaveMaster)itr.next();
		int rowId = i+1;

		type=leaveMaster.getTypeOfLeaveMstr()==null?"0":leaveMaster.getTypeOfLeaveMstr().getId().toString();
		String val = type;

 %>

	<tr id="TPRow">
	<input type = hidden name="leaveTypeId" id="leaveTypeId" value="<%=val%>" />
	<input type = hidden name="leaveMstrId" id="leaveMstrId" value="<%=leaveMaster.getId()==null?"0":leaveMaster.getId().toString()%>" />
	<td class="fieldcell">

		<select  name="typeOfLeaveMstr" id="typeOfLeaveMstr" class="selectwk" onchange = "populateTypeArry(this);populateHidden(this) ">
				<option value='0' selected="selected"><bean:message key="Choose"/></option>
				<%
				Map typeOfLeaveMap =(Map)session.getAttribute("typeOfLeaveMap");
				for (Iterator it = typeOfLeaveMap.entrySet().iterator(); it.hasNext(); )
				{
					Map.Entry entry = (Map.Entry) it.next();
				%>
				<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue() == new Integer(type).intValue()? "selected":"")%>><%= entry.getValue() %></option>
				<%
				}
				%>
		</select>


</td>
	<td class="fieldcell"><input   type="text"  class="selectwk" name="noOfDays" id="noOfDays" value="<%=leaveMaster.getNoOfDays()==null?0:leaveMaster.getNoOfDays().intValue()%>"  ></td>

</tr>
<%
	}
%>
</tbody>
</table>
<br>
<table  width="100%"  cellpadding ="0" cellspacing ="0" border = "0" >
<tr>
   <%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%>
   <td> 
   <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" id="addtpBtn" value="AddRow" name="addtpBtn" onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))" /></a>
   
  
   
   <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" id="deltp" name="deltp" onclick="javascript:deleteRow(this,document.getElementById('TPTable'),document.getElementById('addtpBtn'))" /></a>
   
   </td>
   <%
   }
   %>
   </tr>
   </table>

<table id = "submit" style="width: 810;"  cellpadding ="0" cellspacing ="0" border = "0" value = "submit">
<tr >
<%
if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
{
%>
		<td align="center"></td>
 <%
 }
 %>

 		

</tr>

          
</table>
 
 <tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
          
          
</table>
</div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>


  
  </table>
  
  </div>
<div class="buttonholderwk">&nbsp;
</table>

</table>

<table align="center">
<tr>
<td>
<html:button styleClass="buttonfinal" value="Save" property="b2" onclick="ButtonPressNew('savenew');" />
<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
</td>
</tr>
</table>
</html:form>
</body>
