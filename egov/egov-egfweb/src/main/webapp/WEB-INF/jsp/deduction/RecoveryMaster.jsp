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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.egov.deduction.client.RecoverySetupForm"%>
<%@ page import="org.egov.infstr.utils.EgovMasterDataCaching"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>eGov - Recovery Masters</title>

	<%
	RecoverySetupForm rsf = (RecoverySetupForm)request.getAttribute("RecoverySetupForm");
	Logger logger = Logger.getLogger(getClass().getName()); 

	ArrayList glCodeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-coaCodesForLiability");
	ArrayList empglCodeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-AllCoaCodesOfEarning");
	ArrayList partyMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-partyTypeMaster");
	ArrayList tdsTypeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-tdsType");
	ArrayList bankList=(ArrayList)EgovMasterDataCaching.getInstance().get("egf-bank");
	//logger.infor("calculation type : "+rsf.getCalculationType());
%>

<link href="resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<link href="css/commonegov.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<!-- script type="text/javascript" src="common/js/budget.js"></script> -->
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/recoveryMasterHelper.js?rnd=${app_release_no}'/>"></script>
<script language=javascript>
// ---- Manual Script ---STARTS--
var myrowId;
var booleanValue;

function uniqueCheckForAccCode()
{
	
	<% 
	if(rsf.getRecovAccCodeId()==null)
	{%>
			
		// For create Mode	
		booleanValue=uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'recovAccCodeId', 'no', 'no');
		if(booleanValue==false)
		{
			bootbox.alert("This Account code already used for some other Recoveries!!!!");
			document.RecoverySetupForm.recovAccCodeId.focus();
			return false;
		}
	<%
	}
	else
	{
	%>
		// For Modify Mode
		var accCodeIdNew=document.getElementById('recovAccCodeId').value;
		var accCodeIdOld="<%=(rsf.getRecovAccCodeId())%>";
		
		//bootbox.alert("accCodeIdNew"+accCodeIdNew);
		//bootbox.alert("accCodeIdOld"+accCodeIdOld);
		
		if(accCodeIdNew!=accCodeIdOld)
		{
			booleanValue = uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'recovAccCodeId', 'no', 'no');
			if(booleanValue==false)
			{
	
			bootbox.alert("This Account code already used for some other Recoveries!!!!");
			document.RecoverySetupForm.recovAccCodeId.focus();			
			return false;
			}		
		}	
			
	<%
	}
			
	%>
	return true;
}

function uniqueCheckForRecoveryCode()
{
	<% 
	if(rsf.getRecovCode()==null)
	{%>

		// For create Mode	
		if(document.getElementById('recovCode').value != '' ) {
			booleanValue=uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'type', 'recovCode', 'no', 'no');
			if(booleanValue==false) {
				bootbox.alert("This Recovery code already used for some other Recoveries!!!!");
				document.getElementById('recovCode').value = "";
				return false;
			}
		}
	<%
	}
	else
	{
	%>
		// For Modify Mode
		var recovCodeNew=document.getElementById('recovCode').value;
		var recovCodeOld="<%=(rsf.getRecovCode())%>";
		//bootbox.alert("recovCodeNew"+recovCodeNew);
		//bootbox.alert("recovCodeOld"+recovCodeOld);
		if(recovCodeNew!=recovCodeOld)
		{
			booleanValue=uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'type', 'recovCode', 'no', 'no');
			if(booleanValue==false)
			{
				bootbox.alert("This Recovery code already used for some other Recoveries!!!!");
				document.getElementById('recovCode').value = recovCodeOld;
				return false;
			}		
		}	

	<%
	}
	%>	
}

function selected(e)
{
	myrowId=e.rowIndex;
}

function keyPressed() 
	{

	  var F2 = 113;
	  var tbl = document.getElementById('gridRecoverySetup');
	  var rCount=tbl.rows.length-1;
	  if ((event.keyCode == F2) && (myrowId==rCount)) 
	  {
		addRow();

	  }
	}
function addRow()
{
	var tbl = document.getElementById('gridRecoverySetup');
	var tbody=tbl.tBodies[0];
	var lastRow = tbl.rows.length;
	var rowObj = document.getElementById('detailsRow').cloneNode(true);
	tbody.appendChild(rowObj);
	//bootbox.alert("lastRow :"+lastRow);
	
	//document.forms[0].docType[lastRow-1].options.length=0;
	//document.forms[0].subType[lastRow-1].options.length=0;
	
	document.forms[0].recovDateFrom[lastRow-1].value="";
	document.forms[0].recovDateTo[lastRow-1].value="";
	document.forms[0].lowAmount[lastRow-1].value="";
	document.forms[0].highAmount[lastRow-1].value="";
	
	document.forms[0].cumulativeAmountLow[lastRow-1].value="";
	document.forms[0].cumulativeAmountHigh[lastRow-1].value="";
	
	document.forms[0].ITPercentage[lastRow-1].value="";
	document.forms[0].surPercentage[lastRow-1].value="";
	document.forms[0].eduCessPercentage[lastRow-1].value="";
	document.forms[0].totalPercentage[lastRow-1].value="";
	document.forms[0].flatAmount[lastRow-1].value="";
}
function deleteRow(obj)
{

   var tbl = document.getElementById('gridRecoverySetup');
   var lastRow = (tbl.rows.length)-1;
   //bootbox.alert("lastRow :"+lastRow);
   var selRow = getTableRow(obj);
   var selRowIndex = selRow.rowIndex;
   //bootbox.alert("selRowIndex in delete method :"+selRowIndex);

	if(lastRow ==1)
	{
		 bootbox.alert("This row can not be deleted");
		return false;
	 }
	/*else if(selRowIndex == -1 ) // this will delete the last row.
	{
		tbl.deleteRow(lastRow);
		return true;
	} */else {
		tbl.deleteRow(selRowIndex);
		return true;
	}

}
function calcTotalForViewModify()
{
	var table= document.getElementById("gridRecoverySetup");
	var itPer=0;
	var surPer=0;
	var eduPer=0;
	var totalPer=0;
			
	for(var i=1;i<table.rows.length;i++)
	{
		itPer=getControlInBranch(table.rows[i],"ITPercentage").value;
		surPer=getControlInBranch(table.rows[i],"surPercentage").value;
		eduPer=getControlInBranch(table.rows[i],"eduCessPercentage").value;
				
		if(itPer=="")
		itPer=0;
		if(surPer=="")
		surPer=0;
		if(eduPer=="")
		eduPer=0;
		
		totalPer=parseFloat(itPer)+parseFloat(surPer)+parseFloat(eduPer);
		
		getControlInBranch(table.rows[i],"totalPercentage").value=totalPer.toFixed(2);
		var totalVal=getControlInBranch(table.rows[i],"totalPercentage").value;
		if(totalVal==0.00)
		getControlInBranch(table.rows[i],"totalPercentage").value="";
	}
}

function calcTotalPer(obj)
{
			var rowobj=getRow(obj);
			var table= document.getElementById("gridRecoverySetup");
			var itPer=0;
			var surPer=0;
			var eduPer=0;
			var totalPer=0;
			
	for(var i=1;i<table.rows.length;i++)
	{
			itPer=getControlInBranch(table.rows[rowobj.rowIndex],"ITPercentage").value;
			surPer=getControlInBranch(table.rows[rowobj.rowIndex],"surPercentage").value;
			eduPer=getControlInBranch(table.rows[rowobj.rowIndex],"eduCessPercentage").value;

			if(isNaN(itPer))
			{
				bootbox.alert("IT should be a Numeric value");
				return false;
			}
			
			if(surPer!="")
			{
				if(isNaN(surPer))
				{
				bootbox.alert("SurCharge should be a Numeric value");
				return false;
				}
			}
			if(eduPer!="")
			{
				if(isNaN(eduPer))
				{
				bootbox.alert("Education Cess should be in numeric value");
				return false;
				}
			}
			
			if(!(itPer!=""))
				itPer=0;

			if(!(surPer!=""))
				surPer=0;

			if(!(eduPer!=""))
				eduPer=0;

			var a=0;
			var b=0;
			var c=0;
			var d=0;

			a=parseFloat(totalPer);
			b=parseFloat(itPer);
			c=parseFloat(surPer);
			d=parseFloat(eduPer);
			
              getControlInBranch(table.rows[rowobj.rowIndex],"totalPercentage").value=(a+b+c+d).toFixed(2);
              if(getControlInBranch(table.rows[rowobj.rowIndex],"totalPercentage").value>100.00)
              {
              	 	bootbox.alert("Total Percentage cannot be more than 100");
			return false;
              }
              
              var totalRowVal=getControlInBranch(table.rows[rowobj.rowIndex],"totalPercentage").value;
              if(totalRowVal==0.00)
              getControlInBranch(table.rows[rowobj.rowIndex],"totalPercentage").value="";
              
	
	} //for
}

function checkEmptyFieldFromTable()
{
	var table=document.getElementById("gridRecoverySetup");
	var col1,col2,col3,col4;
	
	for(var i=1;i<table.rows.length;i++)
	{
		col1=getControlInBranch(table.rows[i],"partyType").value;
		col2=getControlInBranch(table.rows[i],"docType").value;
		col3=getControlInBranch(table.rows[i],"recovDateFrom").value;
		col4=getControlInBranch(table.rows[i],"lowAmount").value;
		if(!(col3!=0 && col3!="")) {
			bootbox.alert("In Row "+i+", From Date cannot be empty");
			getControlInBranch(table.rows[i],'recovDateFrom').focus();
			return false;
		}
		if(!(col4!=0 && col4!=""))
		{
			bootbox.alert("In Row "+i+", Amount Low Limit Cannot be empty");
			getControlInBranch(table.rows[i],'lowAmount').focus();
			return false;
		}
	} // for
	return true;
}

function checkLimitsAll()
{
	var table=document.getElementById("gridRecoverySetup");
	var col1,col2,col3,col4;
	//bootbox.alert(table.rows.length);
	
 	for(var i=1;i<table.rows.length;i++)
	{
		col1=getControlInBranch(table.rows[i],"recovDateFrom").value;
		col2=getControlInBranch(table.rows[i],"recovDateTo").value;
		col3=getControlInBranch(table.rows[i],"lowAmount").value;
		col4=getControlInBranch(table.rows[i],"highAmount").value;
		
		col5=getControlInBranch(table.rows[i],"cumulativeAmountLow").value;
		col6=getControlInBranch(table.rows[i],"cumulativeAmountHigh").value;
	
		if(col2!="" && col2.length>0)
		{
			if((compareDate(formatDate6(col1),formatDate6(col2)) !=1))
			 {
				 bootbox.alert("In Row "+i+", To Date must be higher than the From Date");
				 getControlInBranch(table.rows[i],'recovDateTo').focus();				
				 return false;
			 }
		 }
		 if(col3!="")
		 {
		 	if(isNaN(col3))
		 	{
				bootbox.alert("Low Limit value should be a numeric value");
				return false;
			}
			minVal=parseFloat(col3);
			if(minVal>parseFloat(9999999999999.99))
			{
				bootbox.alert("Low Limit Values  cannot be greater than 9999999999999.99");
				getControlInBranch(table.rows[i],'lowAmount').focus();				
				return false;
			}
		 
		 }
		 
		 if(col4!="")
		 {
			if(isNaN(col4))
			{
				bootbox.alert("High Limit value should a numeric value");
				return false;
			}
			maxVal=parseFloat(col4);
			if(maxVal==0)
			{
				bootbox.alert(" High Limit value cannot be Zero ");
				getControlInBranch(table.rows[i],'highAmount').focus();
				return false;
			}
			if(minVal >= maxVal)
			{
				bootbox.alert("In Row "+i+", Amount High value must be higher than the Amount Low value.");
				getControlInBranch(table.rows[i],'highAmount').focus();				
				return false;
			}
			if(maxVal>parseFloat(9999999999999.99))
			{
				bootbox.alert("Amount High Value cannot be greater than 9999999999999.99");
				getControlInBranch(table.rows[i],'highAmount').focus();				
				return false;
			}
		}
		 // ---- cumulative amount calculation.
		 if(col5 != "")
		 {
		 	if(isNaN(col5))
		 	{
				bootbox.alert("Cumulative Low Limit value should be a numeric value");
				return false;
			}
			minVal=parseFloat(col5);
			if(minVal > parseFloat(9999999999999.99))
			{
				bootbox.alert("Cumulative Low Limit Values  cannot be greater than 9999999999999.99");
				getControlInBranch(table.rows[i],'cumulativeAmountLow').focus();				
				return false;
			}
		 
		 }
		 
		 if(col6!="")
		 {
			if(isNaN(col6))
			{
				bootbox.alert("Cumulative High Limit value should be a numeric value");
				return false;
			}
			maxVal=parseFloat(col6);
			if(maxVal==0)
			{
				bootbox.alert("Cumulative High Limit value cannot be Zero ");
				getControlInBranch(table.rows[i],'cumulativeAmountHigh').focus();
				return false;
			}
			if(minVal >= maxVal)
			{
				bootbox.alert("In Row "+i+", Cumulative Amount High value must be higher than the Low Amount value.");
				getControlInBranch(table.rows[i],'cumulativeAmountHigh').focus();				
				return false;
			}
			if(maxVal>parseFloat(9999999999999.99))
			{
				bootbox.alert("Cumulative High Limit value cannot be greater than 9999999999999.99");
				getControlInBranch(table.rows[i],'cumulativeAmountHigh').focus();				
				return false;
			}
		}
	} // for
	return true;
}


function checkLimitsWithCombination()
{
	var table=document.getElementById("gridRecoverySetup");
	var row1col1,row1col2,row1col3,row1col4,row1col5,row1col6;
	var rowcol1,rowcol2,rowcol3,rowcol4,rowcol5,rowcol6;
	var minVal,maxVal,previousHigh=0;
	var curfromDateVal,toDateVal,previousMaxDate;
	
	for(var i=1;i<table.rows.length-1;i++)
	{
		row1col1=getControlInBranch(table.rows[i],"partyType");
		row1col2=getControlInBranch(table.rows[i],"docType");
		//row1col3=getControlInBranch(table.rows[i],"subType");
		
		row1col4=getControlInBranch(table.rows[i],"recovDateFrom").value;
		row1col5=getControlInBranch(table.rows[i],"lowAmount").value;
		row1col6=getControlInBranch(table.rows[i],"highAmount").value;
		row1col7=getControlInBranch(table.rows[i],"recovDateTo").value;
		
		row1col8=getControlInBranch(table.rows[i],"cumulativeAmountLow").value;
		row1col9=getControlInBranch(table.rows[i],"cumulativeAmountHigh").value;
		//bootbox.alert("cumulativeAmountLow :"+row1col8);

	
		for(var j=i+1;j<table.rows.length;j++)
		{
			rowcol1=getControlInBranch(table.rows[j],"partyType");
			rowcol2=getControlInBranch(table.rows[j],"docType");
			//rowcol3=getControlInBranch(table.rows[j],"subType");
			
			rowcol4=getControlInBranch(table.rows[j],"recovDateFrom").value;
			rowcol5=getControlInBranch(table.rows[j],"lowAmount").value;
			rowcol6=getControlInBranch(table.rows[j],"highAmount").value;
			rowcol7=getControlInBranch(table.rows[j],"recovDateTo").value;
	
	
	if((row1col1.value==rowcol1.value))
	{	
	   if((row1col2.value==rowcol2.value) && rowcol2.value.length>0)
	   {
    
	     //if((row1col3.value==rowcol3.value)) 
		 //{ 
		
		  if((compareDate(formatDate6(row1col4),formatDate6(rowcol4)) == 0) && rowcol4.length>0)
		   {
			// for date validation
			if(rowcol4!="" && rowcol4.length>0)
			 {
			 	curfromDateVal=rowcol4;
			 }
			 
			 if(row1col7!="" && row1col7.length>0)
			 {	 toDateVal=row1col7;
				previousMaxDate=toDateVal;
			 }
			 else
			 {
				toDateVal=row1col4;
				previousMaxDate=toDateVal;
			 }
			  
			// Payment Limit validation
			if(rowcol5>=0 && rowcol5!="")
			{
				minVal=parseInt(rowcol5);
				if(minVal>parseFloat(9999999999999.99))
				{
					bootbox.alert("Amount Low value cannot be greater than 9999999999999.99");
					getControlInBranch(table.rows[j],'lowAmount').focus();				
					return false;
				}
			}
			
			if(row1col6>0 && row1col6!="")
			{
				maxVal=parseInt(row1col6);
			}
			else
			{
				maxVal=parseInt(row1col5);
			}

			previousHigh=maxVal;

			if(previousHigh !="0")
			{
				if(previousHigh >= minVal)
				{
					bootbox.alert("In Row "+j+", Amount Low value must be higher than the "+i+" Row Amount Limits");
					getControlInBranch(table.rows[j],'lowAmount').focus();				
					return false;
				}
			}
	       } // If From Date equal
	       
	       else 
	       {
	               	// for date validation
			if(rowcol4!="" && rowcol4.length>0)
			 {
				curfromDateVal=rowcol4;
			 }

			 if(row1col7!="" && row1col7.length>0)
			 {	 toDateVal=row1col7;
				previousMaxDate=toDateVal;
			 }
			 else
			 {
				toDateVal=row1col4;
				previousMaxDate=toDateVal;
			 }

			  if((compareDate(formatDate6(previousMaxDate),formatDate6(curfromDateVal)) != 1))
			  {
				 bootbox.alert("In Row "+j+", From Date must be higher than the "+i+" Row Date Range");
				 getControlInBranch(table.rows[j],'recovDateFrom').focus();				
				 return false;
			  }
			 

			// Payment Limit validation

			if(rowcol5>=0 && rowcol5!="")
			{
				minVal=parseInt(rowcol5);
				if(minVal>parseFloat(9999999999999.99))
				{
				bootbox.alert("Amount Low value cannot be greater than 9999999999999.99");
				getControlInBranch(table.rows[j],'lowAmount').focus();				
				return false;
				}
			}
			
			if(row1col6>0 && row1col6!="")
			{
				maxVal=parseInt(row1col6);
			}
			else
			{
				maxVal=parseInt(row1col5);
			}
			previousHigh=maxVal;

			/*if(previousHigh !="0")
			{
				if(previousHigh >= minVal)
				{
					bootbox.alert("In Row "+j+" Row Low Limit  value must be Higher than the "+i+" Row Payment Limits");
					getControlInBranch(table.rows[j],'lowAmount').focus();				
					return false;
				}
			}
			*/
	       }//else From Date Not equal
	    // } // doc sub type
	   } // doc type 

	  } //party type
	 
	 } // for j

	} // for i
	return true;
}
function checkDuplicatesFromTable()
{
		var table=document.getElementById("gridRecoverySetup");
		var row1col1,row1col2,row1col3,row1col4,row1col5;
		var rowcol1,rowcol2,rowcol3,rowcol4,rowcol5;
		
		for(var i=1;i<table.rows.length-1;i++)
		{
			row1col1=getControlInBranch(table.rows[i],"partyType");
			row1col2=getControlInBranch(table.rows[i],"docType");
			//row1col3=getControlInBranch(table.rows[i],"subType");
			row1col4=getControlInBranch(table.rows[i],"recovDateFrom");
			row1col5=getControlInBranch(table.rows[i],"lowAmount");
			
						
			for(var j=i+1;j<table.rows.length;j++)
			{
				rowcol1=getControlInBranch(table.rows[j],"partyType");
				rowcol2=getControlInBranch(table.rows[j],"docType");
				//rowcol3=getControlInBranch(table.rows[j],"subType");
				rowcol4=getControlInBranch(table.rows[j],"recovDateFrom");
				rowcol5=getControlInBranch(table.rows[j],"lowAmount");
				
				if((row1col1.value==rowcol1.value))
				{
				   if((row1col2.value==rowcol2.value) && rowcol2.value.length>0)
				   {
					 if((compareDate(formatDate6(row1col4.value),formatDate6(rowcol4.value)) == 0) && rowcol4.value.length>0)
					 	{
					 		if((row1col5.value==rowcol5.value) && rowcol5.value.length>0)
					 		{
						 		bootbox.alert("Row "+j+" Data cannot be same as Row "+i+" Data");
						 		getControlInBranch(table.rows[j],'recovDateFrom').focus();
						 		return false;
					 		}
					 	}
				   }
				}
			} // for j
		} // for i
	return true;
}

function checkNegativeInputs()
{
	var table=document.getElementById("gridRecoverySetup");
	var col1,col2,col3,col4,col5,col6, col7, col8;
	
	var typeOfCalc = document.getElementById("calculationType").value;
		
	for(var i=1;i<table.rows.length;i++)
	{
	col1=getControlInBranch(table.rows[i],"lowAmount").value;
	col2=getControlInBranch(table.rows[i],"highAmount").value;
	
	col3=getControlInBranch(table.rows[i],"ITPercentage").value;
	col4=getControlInBranch(table.rows[i],"surPercentage").value;
	col5=getControlInBranch(table.rows[i],"eduCessPercentage").value;
	col6=getControlInBranch(table.rows[i],"flatAmount").value;
	
	col7 = getControlInBranch(table.rows[i],"cumulativeAmountLow").value;
	col8 = getControlInBranch(table.rows[i],"cumulativeAmountHigh").value;	
	
	var valPresentPer=false;
	var valPresentFlat=false;
	
		if(col1!="")
		{
			if(isNaN(col1))
			{
				bootbox.alert("Amount Low value should be a numeric value");
				getControlInBranch(table.rows[i],'lowAmount').focus();
				return false;
			}
			else
			col1=parseFloat(col1)
			if(col1<0)
			{
				bootbox.alert("In Row "+i+", Amount Low value should not be a Negative value");
				getControlInBranch(table.rows[i],'lowAmount').focus();
				return false;
			}
		}
		if(col2!="")
		{
			if(isNaN(col2))
			{
				bootbox.alert("Amount High value should be a numeric value");
				getControlInBranch(table.rows[i],'highAmount').focus();
				return false;
			}
			else
				col2=parseFloat(col2)
			if(col2<0)
			{
				bootbox.alert("In Row "+i+", Amount High value should not be a Negative value");
				getControlInBranch(table.rows[i],'highAmount').focus();
				return false;
			}
			
		}
		
		if(typeOfCalc == "Percentage") {
			if(col3!="")
			{
				if(isNaN(col3))
				{
					bootbox.alert("IT should be a Number between 0 to 100");
					getControlInBranch(table.rows[i],'ITPercentage').focus();
					return false;
				}
				else
				col3=parseFloat(col3)
				if(col3<0)
				{
					bootbox.alert("In Row "+i+", IT value should not be a Negative value");
					getControlInBranch(table.rows[i],'ITPercentage').focus();
					return false;
				}
				valPresentPer=true;
			}
			if(col4!="")
			{
				if(isNaN(col4))
				{
					bootbox.alert("Sur Charge value should be a Number between 0 to 100");
					getControlInBranch(table.rows[i],'surPercentage').focus();
					return false;
				}
				else
					col4=parseFloat(col4)
				
				if(col4<0)
				{
					bootbox.alert("In Row "+i+", Sur Charge value should not be a Negative value");
					getControlInBranch(table.rows[i],'surPercentage').focus();
					return false;
				}
				valPresentPer=true;
			}
			if(col5!="")
			{
				if(isNaN(col5))
				{
					bootbox.alert("EDU Cess value should be a Number between 0 to 100");
					getControlInBranch(table.rows[i],'eduCessPercentage').focus();
					return false;
				}
				else
					col5=parseFloat(col5)
				
				if(col5<0)
				{
					bootbox.alert("In Row "+i+", EDU Cess value should not be a Negative value");
					getControlInBranch(table.rows[i],'eduCessPercentage').focus();
					return false;
				}
				valPresentPer=true;
			}
		} else {
			if(col6!="")
			{
				if(isNaN(col6))
				{
					bootbox.alert("Flat Amount should be a numeric value");
					getControlInBranch(table.rows[i],'flatAmount').focus();
					return false;
				}
				else
					col6=parseFloat(col6)
				
				if(col6<0)
				{
					bootbox.alert("In Row "+i+", Flat Amount should not be a Negative value");
					getControlInBranch(table.rows[i],'flatAmount').focus();
					return false;
				}
				valPresentFlat=true;
			}
		}
		
		if(col7!="")
		{
			if(isNaN(col7))
			{
				bootbox.alert("Cumulative Low value should be a numeric value");
				getControlInBranch(table.rows[i],'cumulativeAmountLow').focus();
				return false;
			}
			else
				col7=parseFloat(col7)
			
			if(col7<0)
			{
				bootbox.alert("In Row "+i+", Cumulative Low value should not be a Negative value");
				getControlInBranch(table.rows[i],'cumulativeAmountLow').focus();
				return false;
			}
		}
		
		if(col8!="")
		{
			if(isNaN(col8))
			{
				bootbox.alert("Cumulative High value should be a numeric value");
				getControlInBranch(table.rows[i],'cumulativeAmountHigh').focus();
				return false;
			}
			else
				col8=parseFloat(col8)
			
			if(col8<0)
			{
				bootbox.alert("In Row "+i+", Cumulative High value should not be a Negative value");
				getControlInBranch(table.rows[i],'cumulativeAmountHigh').focus();
				return false;
			}
		}
		
		
		if(typeOfCalc == "Flat" && !valPresentFlat ) {
			bootbox.alert("In Row "+i+", Enter Flat Amount value");
			getControlInBranch(table.rows[i],'flatAmount').focus();
			return false;
		}
		if(typeOfCalc == "Percentage" && !valPresentPer) {
			bootbox.alert("In Row "+i+", Enter IT/SC/EC values");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
		}

	}//for
	
return true;
}

function checkDates(){
	var table=document.getElementById("gridRecoverySetup");
	var rowcol1,rowcol2,rowcol3,rowcol4;
	var data = {};
	for(var i=1;i<table.rows.length;i++){
		rowcol1=getControlInBranch(table.rows[i],"partyType").value;
		rowcol2=getControlInBranch(table.rows[i],"docType").value;
		rowcol3=getControlInBranch(table.rows[i],"recovDateFrom").value;
		rowcol4=getControlInBranch(table.rows[i],"recovDateTo").value;
		var key = ""+rowcol1+""+rowcol2;
		if(data[key] == null)
			data[key] = ""+i+"~"+rowcol4;
		else{
			if(data[key].split("~")[1] == ''){
				bootbox.alert("Enter To Date for Row "+data[key].split("~")[0])
				return false;
			}
		}
	}
	return true;
}
/*
function checkEitherPerOrFlat()
{
		var table=document.getElementById("gridRecoverySetup");
		var row1col1,row1col2;
		var rowcol1,rowcol2;
				
		for(var i=1;i<table.rows.length-1;i++)
		{
			row1col1=getControlInBranch(table.rows[i],"totalPercentage").value;
			row1col2=getControlInBranch(table.rows[i],"flatAmount").value;
			
			//var previousPer=false;
			//var previousFlat=false;
									
			for(var j=i+1;j<table.rows.length;j++)
			{
				rowcol1=getControlInBranch(table.rows[j],"totalPercentage").value;
				rowcol2=getControlInBranch(table.rows[j],"flatAmount").value;
				
				var previousPer=false;
				var previousFlat=false;
				var currPer=false;
				var currFlat=false;
				
				if(row1col1!="")
				{
					previousPer=true;
				}
				if(row1col2!="")
				{
					previousFlat=true;
				}
				
				if(rowcol1!="")
				{
					currPer=true;
				}
				if(rowcol2!="")
				{
					currFlat=true;
				}
				
				if(previousPer)
				{
					if(currFlat)
					{
					bootbox.alert("In Recovery Master-->"+j+" Row should not enter Flat Amount.Enter All rows should be Flat OR Percentage");
					getControlInBranch(table.rows[j],'flatAmount').focus();
					return false;

					}
						
				}
				if(previousFlat)
				{
					if(currPer)
					{
					bootbox.alert("In Recovery Master-->"+j+" Row should not enter Flat Amount.Enter All rows should be Flat OR Percentage");
					getControlInBranch(table.rows[j],'ITPercentage').focus();
					return false;

					}
										
				}
				
				
			} // for j
			
			
		} // for i
return true;		
} */



function ButtonPress(arg)
{	
    if(arg =="saveclose" || arg == "savenew" ) {
    	if( document.RecoverySetupForm.recMode[0].checked == false &&  document.RecoverySetupForm.recMode[1].checked == false ) {
    		bootbox.alert("Select the Method");
    		return false;
    	}
    	
		if(document.RecoverySetupForm.recovCode.value=="") {
			bootbox.alert("Enter the value for Recovery Code");
			var temp="document.RecoverySetupForm.recovCode.focus();";
			setTimeout(temp,0);		
			return;
		}
		if(document.RecoverySetupForm.recovName.value=="") {
			bootbox.alert("Enter the value for Recovery Name");
			var temp="document.RecoverySetupForm.recovName.focus();";
			setTimeout(temp,0);		
			return;
		}
		if(document.RecoverySetupForm.recovAppliedTo.value == "0") {
				bootbox.alert("Select Applied To");
				var temp="document.RecoverySetupForm.recovAppliedTo.focus();";
				setTimeout(temp,0);		
				return;
		}

		if(document.RecoverySetupForm.isEarning.value == 1) {
				if(document.RecoverySetupForm.emprecovAccCodeId.value == "0") {
						bootbox.alert("Select Account Code");
						var temp="document.RecoverySetupForm.emprecovAccCodeId.focus();";
						setTimeout(temp,0);		
						return;
				} else {
					if(!uniqueCheckForEmpEarningCode())
						return false;
				}
		} else {
			if(document.RecoverySetupForm.recovAccCodeId.value == "0") {
					bootbox.alert("Select Account Code");
					var temp="document.RecoverySetupForm.recovAccCodeId.focus();";
					setTimeout(temp,0);		
					return;
			}
		}
		
		if(document.RecoverySetupForm.recMode[0].checked==true) {
			if(document.RecoverySetupForm.recovRemitTo.value=="") {
				bootbox.alert("Enter the value for Remitted To");
				var temp="document.RecoverySetupForm.recovRemitTo.focus();";
				setTimeout(temp,0);
				return;
			}
			/*if(document.RecoverySetupForm.recovBSRCode.value=="")
			{
				bootbox.alert("BSR Code is required!");
				var temp="document.RecoverySetupForm.recovBSRCode.focus();";
				setTimeout(temp,0);		
				return;
			}*/				
			
			if(!checkEmptyFieldFromTable())return;	

			if(!checkDuplicatesFromTable())return;
			
			if(!checkNegativeInputs())return;
			
			//if(!checkEitherPerOrFlat())return;
			
			if(!checkLimitsAll())return;
			
			if(!checkLimitsWithCombination())return;
			
			if(!checkDates())return;
		}
		else
		{			
			if(document.RecoverySetupForm.isBankLoan.checked==false && document.RecoverySetupForm.recovRemitTo.value=="")
			{
				bootbox.alert("Remitted to is required!");
				var temp="document.RecoverySetupForm.recovRemitTo.focus();";
				setTimeout(temp,0);		
				return;
			}
			/*if(document.RecoverySetupForm.isBankLoan.checked==false && document.RecoverySetupForm.recovBSRCode.value=="")
			{
				bootbox.alert("BSR Code is required!");
				var temp="document.RecoverySetupForm.recovBSRCode.focus();";
				setTimeout(temp,0);		
				return;
			}*/	
			if(document.RecoverySetupForm.isBankLoan.checked==true && document.RecoverySetupForm.bank.value=="0")	
			{
				bootbox.alert("Select Bank!");
				var temp="document.RecoverySetupForm.bank.focus();";
				setTimeout(temp,0);		
				return;
			}
		}		
		
		/*if(document.RecoverySetupForm.isBankLoan.checked==false)
		{
			if(!uniqueCheckForAccCode())
				return;
		}*/
		if(document.RecoverySetupForm.recovAppliedTo.options[document.RecoverySetupForm.recovAppliedTo.selectedIndex].text!='Employee')
		{
			document.RecoverySetupForm.isEarning.value='';
			document.RecoverySetupForm.emprecovAccCodeId.value=0;
		}
		if(document.RecoverySetupForm.recovAppliedTo.options[document.RecoverySetupForm.recovAppliedTo.selectedIndex].text=='Employee')
		{
			if(	document.RecoverySetupForm.isEarning.value==0)
			{
				document.RecoverySetupForm.emprecovAccCodeId.value=0;
			}
		
		}
		var mode="${mode}";
		//bootbox.alert(mode);		
		document.getElementById("button").value=arg;
		if(document.RecoverySetupForm.isBankLoan.checked==false)
			document.RecoverySetupForm.bankLoan.value="off";
		document.RecoverySetupForm.recMode[0].disabled = false;
		document.RecoverySetupForm.recMode[1].disabled = false;
		if(mode == "create")
		{
			document.getElementById("button").value=arg;
			//bootbox.alert("Create submit"+mode);		
			document.RecoverySetupForm.action = "../deduction/recoverySetupMaster.do?submitType=createRecoveryMaster";
			document.RecoverySetupForm.submit();	
		}
		if(mode == "modify")
		{
			//bootbox.alert("Modify submit"+mode);

			
			document.RecoverySetupForm.action = "../deduction/recoverySetupMaster.do?submitType=modifyRecoveryMaster";
			document.RecoverySetupForm.submit();
		}
	}// main if
	
	if(arg=="view")
	{
		if(document.RecoverySetupForm.tdsTypeId.value == "0")
		{
			bootbox.alert("Select Recovery Code First");
			var temp="document.RecoverySetupForm.tdsTypeId.focus();";
			setTimeout(temp,0);		
			return;
		}
		document.getElementById("button").value=arg;
		document.RecoverySetupForm.action = "../deduction/recoverySetupMaster.do?submitType=viewRecoveryMaster";
		document.RecoverySetupForm.submit();
	}
	if(arg=="modify")
	{
		if(document.RecoverySetupForm.tdsTypeId.value == "0")
		{
			bootbox.alert("Select Recovery Code First");
			var temp="document.RecoverySetupForm.tdsTypeId.focus();";
			setTimeout(temp,0);		
			return;
		}
		document.getElementById("button").value=arg;
		document.RecoverySetupForm.action = "../deduction/recoverySetupMaster.do?submitType=beforeModifyRecoveryMaster";
		document.RecoverySetupForm.submit();
	}
	
	if(arg == "backView")
	{
		window.location="../deduction/recoverySetupMaster.do?submitType=toView";	
		return;
	}
	
	if(arg == "backModify")
	{
		window.location="../deduction/recoverySetupMaster.do?submitType=toModify";	
		return;
	}
	
}


function splitGlCode()
{
	if(document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].value!=0)
	{
		var arr=document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].text.split("`-`");
		document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].text=arr[0];
		document.getElementById("recAccDesc").value=arr[1];
		document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].text=arr[0]+"`-`"+arr[1];
	}
	else
	{
	document.getElementById("recAccDesc").value="";
	}
}



function empsplitGlCode()
{


	if(document.RecoverySetupForm.emprecovAccCodeId.options[document.RecoverySetupForm.emprecovAccCodeId.selectedIndex].value!=0)
	{
		var arr=document.RecoverySetupForm.emprecovAccCodeId.options[document.RecoverySetupForm.emprecovAccCodeId.selectedIndex].text.split("`-`");
		//bootbox.alert(arr[0]);
		//bootbox.alert(arr[1]);
	
		document.RecoverySetupForm.emprecovAccCodeId.options[document.RecoverySetupForm.emprecovAccCodeId.selectedIndex].text=arr[0];
		document.getElementById("emprecAccDesc").value=arr[1];
		document.RecoverySetupForm.emprecovAccCodeId.options[document.RecoverySetupForm.emprecovAccCodeId.selectedIndex].text=arr[0]+"`-`"+arr[1];
	}
	else
	{
	document.RecoverySetupForm.emprecAccDesc.value="";
	}
}
function onBodyLoad()
{	
	document.RecoverySetupForm.recMode[0].checked=true;	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	var buttonType="${buttonType}";
	
	if(target!="null")
	{
		bootbox.alert("<%=request.getAttribute("alertMessage")%>");		
	}
	var mode="${mode}";
	if(buttonType == "saveclose") {
		window.close();
	}
	if(target!="null" && buttonType =="savenew") {
		window.location= "../deduction/recoverySetupMaster.do?submitType=toLoad";
	}

	if(mode == "create") {
		document.getElementById("calculationType").value = "Flat";
		callTheType('OL');
	}
		
	if(mode == "searchView")
	{
			document.title="View Recovery Master";
			//document.getElementById('screenName').innerHTML="View-Recovery Master";
			document.getElementById("hideRow1").style.display="";
			document.getElementById("SectionRowId").style.display="none";
			document.getElementById("IFSCCodeRowId").style.display="none";
			
			hideRowsViewModify();
			document.getElementById("hidegridRecovery").style.display="none";
			//document.RecoverySetupForm.recovAccCodeId.options.selectedIndex=0;
			//document.RecoverySetupForm.recovAppliedTo.options.selectedIndex=0;
			document.getElementById("hideRow9").style.display="";
			document.getElementById("MandatoryFieldsId").style.display="none";
			document.getElementById("flagRow").style.display="none";
	}
	if(mode == "searchModify")
	{
			document.title="Modify Recovery Master";
			//document.getElementById('screenName').innerHTML="Modify-Recovery Master";
			document.getElementById("SectionRowId").style.display="none";
			document.getElementById("IFSCCodeRowId").style.display="none";
			document.getElementById("hideRow1").style.display="";
			hideRowsViewModify();
			document.getElementById("hidegridRecovery").style.display="none";
			//document.RecoverySetupForm.recovAccCodeId.options.selectedIndex=0;
			//document.RecoverySetupForm.recovAppliedTo.options.selectedIndex=0;
			document.getElementById("hideRow9").style.display="";
			document.getElementById("MandatoryFieldsId").style.display="none";
			document.getElementById("flagRow").style.display="none";
	}
	if(mode == "view")
	{	
		document.title="View Recovery Master";
		//document.getElementById("hidegridRecovery").style.display="block";
		document.RecoverySetupForm.recMode[0].disabled = true;
		document.RecoverySetupForm.recMode[1].disabled = true;
		
		document.getElementById("AddDelId").style.display="none";
		document.getElementById("hideRow8").style.display="none";
		document.getElementById("hideRow9").style.display="none";
		document.getElementById("hideRow12").style.display="none";
		document.getElementById("hideRow10").style.display="";		
		//document.getElementById('screenName').innerHTML="View-Recovery Master";
		if("<%=rsf.getRecMode()%>"=="Automatic")
		{
			document.RecoverySetupForm.recMode[0].checked=true;
			document.getElementById("bankListRow").style.display="none";
			document.getElementById("bankLoanCheckBox").style.display="none";
			callTheType('OL');
		}
		else
		{
			document.getElementById("hidegridRecovery").style.display="none";
			document.RecoverySetupForm.recMode[1].checked=true;
			
			document.getElementById("CalculationTypeId1").style.display="none";
			document.getElementById("CalculationTypeId2").style.display="none";
		}
		if(document.RecoverySetupForm.bankLoan.value=="on")
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=true;			
			document.getElementById("bankLoanCheckBox").style.display="";
			document.getElementById("bankListRow").style.display="";
			//document.getElementById("hideRow6").style.display="none";
		}
		else
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=false;		
			//document.getElementById("bankLoanCheckBox").style.display="none";
			document.getElementById("bankListRow").style.display="none";
			//document.getElementById("hideRow6").style.display="block";
		}
		
		for(var i=0;i<document.RecoverySetupForm.length;i++)
		{
			if(document.RecoverySetupForm.elements[i].value != "Back" && document.RecoverySetupForm.elements[i].value != "Close")
			{
				document.RecoverySetupForm.elements[i].disabled =true;
			}					
		}
			
	}
	if(mode == "modify")
	{		
		document.title="Modify Recovery Master";
		//document.getElementById("hidegridRecovery").style.display="block";
		document.getElementById("hideRow8").style.display="none";
		document.getElementById("hideRow11").style.display="";
		document.getElementById("hideRow9").style.display="none";
		document.getElementById("hideRow12").style.display="none";
		document.getElementById("hideRow10").style.display="none";
		//document.getElementById('screenName').innerHTML="Modify-Recovery Master";
		document.RecoverySetupForm.recovAppliedToHidden.value = (document.RecoverySetupForm.recovAppliedTo).selectedIndex;
		if("<%=rsf.getRecMode()%>"=="Automatic")
		{
			document.RecoverySetupForm.recMode[0].checked=true;
			document.getElementById("bankListRow").style.display="none";
			document.getElementById("bankLoanCheckBox").style.display="none";
			callTheType('OL');
		}
		else
		{
			document.getElementById("hidegridRecovery").style.display="none";
			document.RecoverySetupForm.recMode[1].checked=true;
			
			document.getElementById("CalculationTypeId1").style.display="none";
			document.getElementById("CalculationTypeId2").style.display="none";
		}
		
		document.RecoverySetupForm.recMode[0].disabled = true;
		document.RecoverySetupForm.recMode[1].disabled = true;
		
		if(document.RecoverySetupForm.bankLoan.value=="on")
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=true;			
			document.getElementById("bankLoanCheckBox").style.display="";
			document.getElementById("bankListRow").style.display="";
			//document.getElementById("hideRow6").style.display="none";
		}
		else
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=false;		
			//document.getElementById("bankLoanCheckBox").style.display="none";
			document.getElementById("bankListRow").style.display="none";
			//document.getElementById("hideRow6").style.display="block";
		}

	}
	
	if(mode == "view" || mode == "modify")
	{
		
		  <% 
		  if(rsf.getIsEarning()!=null) {
		  		if(rsf.getIsEarning().equalsIgnoreCase("1"))
		  		{
			  		rsf.setEmprecovAccCodeId(rsf.getRecovAccCodeId());
			  		%>
			  		document.getElementById("hideRow4").style.display="none";
			  		document.getElementById("onlyEmp").style.display="";
					empsplitGlCode();
			  		<%
		  		}
		  	}
		%>
		var table=document.getElementById("gridRecoverySetup");
		for(var i=1;i<table.rows.length;i++)
		{
			var currRowObj=getControlInBranch(table.rows[i],"appliedToHiddenId");
			loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID','CODE','parentid=#1 and PARENTID is not null order by code','appliedToHiddenId','partyType' ,currRowObj,'gridRecoverySetup');
			loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','appliedToHiddenId' ,'docType',currRowObj,'gridRecoverySetup');
			//loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','appliedToHiddenId' ,'subType',currRowObj,'gridRecoverySetup');
		}

		var j=1;
		var q=1;
		<% 
		if(rsf.getPartyType()!=null)
		{
			if((rsf.getPartyType()!=null && !session.getAttribute("mode").equals("create")))
			{
				for(int i=0;i<rsf.getPartyType().length;i++)
				{
					%>
					getControlInBranch(table.rows[j],"partyType").value="<%=(rsf.getPartyType()[i])%>";
					j++;
					<%
				} // After loading combo-Assigned values
			}
					
		}
		
		if(rsf.getDocType()!=null)
		{

			if((rsf.getDocType()!=null && !session.getAttribute("mode").equals("create")))
			{
				for(int i=0;i<rsf.getDocType().length;i++)
				{

				%>
				getControlInBranch(table.rows[q],"docType").value="<%=(rsf.getDocType()[i])%>";
				q++;
				<%
				} // After loading combo-Assigned values
			}
			
		}
		%>
		//for(var i=2;i<table.rows.length;i++)
		//{
		//	var currRowObj=getControlInBranch(table.rows[i],"docType");
		//	loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARENTID=#1 order by code','docType','subType',currRowObj,'gridRecoverySetup');
		//} //for
	} // main if
	
	/*if(buttonType == "saveclose")
	{
		window.close();
	}
	if(target!="null" && buttonType!="saveclose")
	{
		window.location= "../deduction/recoverySetupMaster.do?submitType=toLoad";
	}*/
			
	<% 
		if(rsf.getDocType()!=null)
		{%>
		if(mode!="create")
		calcTotalForViewModify();
		<%
		}
	%>
}

function hideRowsViewModify()
{
document.getElementById("hideRow2").style.display="none";
document.getElementById("hideRow3").style.display="none";
document.getElementById("hideRow4").style.display="none";
document.getElementById("hideRow5").style.display="none";
document.getElementById("hideRow6").style.display="none";
document.getElementById("hideRow8").style.display="none";
document.getElementById("descriptionId").style.display="none";
//document.getElementById("hideRow9").style.display="block";

}

function getSelected(obj)
{
	if(obj.value=="Automatic")
	{
		document.getElementById("hidegridRecovery").style.display="";
		document.getElementById("bankLoanCheckBox").style.display="none";
		document.getElementById("CalculationTypeId1").style.display="";
		document.getElementById("CalculationTypeId2").style.display="";
		
		getGridData(document.RecoverySetupForm.recovAppliedTo);	
	}
	else
	{		
		document.getElementById("bankLoanCheckBox").style.display="";
		document.getElementById("hidegridRecovery").style.display="none";
		document.getElementById("CalculationTypeId1").style.display="none";
		document.getElementById("CalculationTypeId2").style.display="none";
		
	}
}

function changeAppliedTo(obj) {
	if( document.RecoverySetupForm.recMode[0].checked ) {
		if(document.RecoverySetupForm.recovAppliedToHidden.value != null && document.RecoverySetupForm.recovAppliedToHidden.value != ""){
			if(!confirm('Changing the this value will reset the data in below table if anything. \n Do you want to continue this?')){
				obj.selectedIndex = document.RecoverySetupForm.recovAppliedToHidden.value;
				return false;
			}
		} 
	}
	document.RecoverySetupForm.recovAppliedToHidden.value = obj.selectedIndex;
	getGridData(obj);
	deleteExtraRow();
}

function getGridData(obj)
{

	//document.RecoverySetupForm.appliedToHidded.value = obj.options[obj.selectedIndex].value;
	document.RecoverySetupForm.isBankLoan.checked=false;
	if(document.RecoverySetupForm.recMode[0].checked) {
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID', 'CODE', 'parentid=#1 order by code', 'recovAppliedTo', 'partyType');
		//loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','recovAppliedTo' ,'partyType');
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','recovAppliedTo' ,'docType');
		//loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','recovAppliedTo' ,'subType');
	}
	else if(document.RecoverySetupForm.recMode[1].checked && obj.options[obj.selectedIndex].text=='Employee')
	{		
		document.getElementById("bankLoanCheckBox").style.display="";		
	}
	else
	{
		document.getElementById("bankLoanCheckBox").style.display="none";
		//document.getElementById("hideRow6").style.display="block";	
		document.getElementById("bankListRow").style.display="none";
	}
	
	if(obj.options[obj.selectedIndex].text.toLowerCase()=='employee')
	{
		document.getElementById("EarDedRow").style.display="";
	}
	else
	{
		document.getElementById("EarDedRow").style.display="none";
		document.getElementById("hideRow4").style.display="";
		document.RecoverySetupForm.emprecovAccCodeId.value=0;
		document.getElementById("onlyEmp").style.display="none";
	}
}

function toggleAccountCodes()
{
	if(document.RecoverySetupForm.isEarning.value==1)
	{
		document.getElementById("hideRow4").style.display="none";
		document.getElementById("onlyEmp").style.display="";
		document.getElementById("recAccDesc").value="";
		document.RecoverySetupForm.emprecAccDesc.value="";
		document.RecoverySetupForm.recovAccCodeId.value=0;
	}
	else
	{
		document.getElementById("hideRow4").style.display="";
		document.getElementById("onlyEmp").style.display="none";
		document.RecoverySetupForm.emprecAccDesc.value="";
		if(document.RecoverySetupForm.recovAccCodeId.value == 0)
			document.getElementById("recAccDesc").value="";
		document.RecoverySetupForm.emprecovAccCodeId.value=0;
	}
}


function getBankList(obj)
{
	if(obj.checked==true)
	{
		document.RecoverySetupForm.isEarning.value=0;
		document.RecoverySetupForm.isEarning.disabled=true;
		//document.getElementById("hideRow6").style.display="none";	
		document.getElementById("bankListRow").style.display="";
		document.RecoverySetupForm.bankLoan.value="on";
	}
	else
	{
		//document.getElementById("hideRow6").style.display="block";
		document.RecoverySetupForm.isEarning.disabled=false;
		document.getElementById("bankListRow").style.display="none";
		document.RecoverySetupForm.bankLoan.value="off";
	}	
	toggleAccountCodes();
}

function checkDocumentType(obj)
{
	var currRow=getRow(obj);
	var col1 =getControlInBranch(currRow,'docType').value;
	var col2=getControlInBranch(currRow,"subType").value;
	
	//bootbox.alert("Current row Doc Type"+col1);
	//bootbox.alert("Current row Sub Type"+col2);
	
	if((col2 !=0 && col2 !=""))
	{
		if(!(col1 !=0 && col1 !=""))
		{
		bootbox.alert("Select Document Type First !!!");
		getControlInBranch(currRow,'docType').focus();
		return false;
		}
	}
		
}
function deleteExtraRow()
{
	var table=document.getElementById('gridRecoverySetup');
	var len=table.rows.length;
		if(len<2) return;
		else
		{
			for (var j=2;j<len;j=j+1)
			{
				table.deleteRow(1);

			}
		}
		
		//if(document.getElementById('recovAppliedTo').value != 0) {
		//	loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID', 'CODE', 'parentid=#1 and PARENTID is not null order by code', 'recovAppliedTo', 'partyType');
		//	loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','recovAppliedTo' ,'docType');
		//}
}

//donot move this method up 
function uniqueCheckForEmpEarningCode()                  
{
	<% 
	if(rsf.getEmprecovAccCodeId()==null)
	{
		%>
		booleanValue = uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'emprecovAccCodeId', 'no', 'no');
		//bootbox.alert("booleanValue "+booleanValue);
		if(booleanValue==false) {
			//document.RecoverySetupForm.emprecovAccCodeId.focus();	
			bootbox.alert("This Account code already used for some other Earnings!!!!");
			document.RecoverySetupForm.emprecovAccCodeId.focus();	
			//document.RecoverySetupForm.emprecovAccCodeId.value=0;		
			return false;
		}
		<%
	}
	else
	{
	%>
		// For Modify Mode
		var accCodeIdNew=document.getElementById('emprecovAccCodeId').value;
		var accCodeIdOld="<%=(rsf.getEmprecovAccCodeId())%>";
		
		//bootbox.alert("accCodeIdNew"+accCodeIdNew);
		//bootbox.alert("accCodeIdOld"+accCodeIdOld);
		
		if(accCodeIdNew!=accCodeIdOld)
		{
			booleanValue=uniqueIdentifierBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'emprecovAccCodeId', 'no', 'no');
			if(booleanValue==false)
			{
	
			bootbox.alert("This Account code already used for some other Earnings!!!!");
			document.RecoverySetupForm.emprecovAccCodeId.focus();			
			return false;
			}		
		}	
	<%
	}
	%>
	return true;
}

function loadRecoveryMasterDetails() {
	var mode="${mode}";
	var submitTy = "viewRecoveryMaster";
	
	if(mode == 'searchModify') {
		submitTy ="beforeModifyRecoveryMaster";
	}
	else if(mode == 'view' || mode == 'searchView') {
		submitTy ="viewRecoveryMaster";
	}
		
	document.RecoverySetupForm.action = "../deduction/recoverySetupMaster.do?submitType="+submitTy;
	document.RecoverySetupForm.submit();
}

function callTheType(arg0) {
	var argValue = document.getElementById('calculationType').value;
	if(arg0 == 'DD') {
		//var table=document.getElementById('gridRecoverySetup');
		//var len = table.rows.length;
		//if(len < 1) {
		if(!confirm('Changing the Calculation type will reset the data in below table if anything. \n Do you want to continue this?')){
			if(argValue == "Flat") document.getElementById('calculationType').value = "Percentage";
			else  document.getElementById('calculationType').value = "Flat";
			return false;
		} else {
			deleteExtraRow();
		}
		//}
	}
	if(argValue == "Percentage") {
		document.getElementById('ItScEcId').style.display = "";	
		document.getElementById('FlatAmtId').style.display = "none";
			
		document.getElementById('ItScEcTextId1').style.display = "";
		document.getElementById('ItScEcTextId2').style.display = "";
		document.getElementById('ItScEcTextId3').style.display = "";
		document.getElementById('ItScEcTextId4').style.display = "";
		
		document.getElementById('flatAmountTextId').style.display = "none";
	} else {
		document.getElementById('ItScEcId').style.display = "none";
		document.getElementById('FlatAmtId').style.display = "";
		
		document.getElementById('ItScEcTextId1').style.display = "none";
		document.getElementById('ItScEcTextId2').style.display = "none";
		document.getElementById('ItScEcTextId3').style.display = "none";
		document.getElementById('ItScEcTextId4').style.display = "none";
		
		document.getElementById('flatAmountTextId').style.display = "";
	} 
}

function getTableRow(obj){
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}
function validateIFSCCode(obj)
{
	   var data=obj.value;
	   var dataLength=obj.value.length;
	   var filter=/^[a-zA-Z0-9]+$/;
   if(dataLength>0){
	   if(!filter.test(data)){
	       bootbox.alert("Please enter alphanumeric only");
	       obj.value='';
	       obj.focus();
	       return false;
	       }
	   if(obj.value.length!=11){
		bootbox.alert("IFSC Code must be 11 digits long.");
		obj.value='';
		obj.focus();
		return false;
	    }
     }
}
function validateBankAccount(obj)
{
	   var data=obj.value;
	   var dataLength=obj.value.length;
	   var filter=/^[0-9]+$/;
	   if(dataLength>0){
   			if(!filter.test(data))
			 {
	       bootbox.alert("Please enter numbers only");
	       obj.value='';
	       obj.focus();
	       return false;
	  		}
	   }
}

//----------END


</script>

</head>
<body onload="onBodyLoad();" onKeyDown="CloseWindow(window.self);"
	onKeyUp="keyPressed();">
	<html:form action="/deduction/recoverySetupMaster.do">
		<div class="topbar">
			<div class="egov">
				<img src="/egi/resources/erp2/images/eGov.png" alt="eGov" width="54"
					height="58" />
			</div>
			<div class="gov">
				<img src="/egi/resources/erp2/images/india.png" alt="India"
					width="54" height="58" />
			</div>
			<div class="mainheading">
				Corporation of Chennai <br />
				<!-- Online Cash Collection System  -->
			</div>
		</div>
		<!-- <div class="navibar"><div align="right">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
           <td><div align="left"><ul id="tabmenu" class="tabmenu">
<li><a href="collectionSRSHome.html">Home</a></li>
        <li><a href="#" target="_parent">Log out</a></li>
        </ul></div></td>
           <td width="63" align="right"><img src="/egi/resources/erp2/images/print.gif" alt="Print" width="18" height="18" border="0" align="absmiddle" /> <a href="#">Print</a></td>
      <td width="63" align="right"><img src="/egi/resources/erp2/images/help.gif" alt="Help" width="18" height="18" border="0" align="absmiddle" /> <a href="#">Help</a></td>
    </tr>
  </table> 
  </div>
</div> -->

		<div class="subheadsmallnew">
			<span class="subheadnew">&nbsp;</span> <span class="subheadnew">Recovery
				Master</span>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr id="flagRow">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><span class="bold">Method</span>:<span
					class="mandatory">*</span></td>
				<td colspan="3" class="bluebox">
					<!--  
	    <input name="radiobutton" type="radio" value="radiobutton" align="absmiddle" onclick="show('recoverytable');" checked="checked"/>
	    <span class="bold" >Automatic</span>
	    <input name="radiobutton" type="radio" value="radiobutton" align="absmiddle" onclick="hide('recoverytable')"/>
		<span class="bold">Manual</span>
		--> <span class="bold">Automatic</span>&nbsp;<html:radio
						property="recMode" value="Automatic" onclick="getSelected(this)" />&nbsp;&nbsp;
					<span class="bold">Manual</span>&nbsp;<html:radio
						property="recMode" value="Manual" onclick="getSelected(this)" />
				</td>
			</tr>
			<!-- This Row For view/modify purpose-->
			<tr style="display: none" id="hideRow1">
				<td class="greybox" width="35%">&nbsp;</td>
				<td class="greybox">&nbsp;Recovery Code:<span class="mandatory">*</span></td>
				<td class="greybox"><html:select property="tdsTypeId"
						onchange="loadRecoveryMasterDetails();" styleClass="bigcombowidth">
						<html:option value='0'>----choose----</html:option>
						<c:forEach var="tdsTypeVar" items="<%=tdsTypeList%>">
							<html:option value="${tdsTypeVar.id}">${tdsTypeVar.name}</html:option>
						</c:forEach>
					</html:select></td>
				<td class="greybox" width="15%">&nbsp;</td>
				<td class="greybox" colspan="2" width="20%">&nbsp;</td>
			</tr>

			<tr id="hideRow2">
				<td class="greybox">&nbsp;</td>
				<td class="greybox">Recovery Code:<span class="mandatory">*</span></td>
				<input type=hidden name="tdsIdHidden" id="tdsIdHidden" />
				<td class="greybox"><html:text styleId="recovCode"
						property="recovCode" onblur="uniqueCheckForRecoveryCode(this);" />
				</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" colspan="2">&nbsp;</td>
			</tr>
			<tr id="hideRow3">
				<td width="10%" class="bluebox">&nbsp;</td>
				<td width="22%" class="bluebox"><span class="greybox">Recovery
						Name</span>:<span class="mandatory">*</span></td>
				<td width="22%" class="bluebox"><span class="greybox">
						<html:text styleId="recovName" property="recovName" size="30" />
				</span></td>
				<td width="18%" class="bluebox" id="CalculationTypeId1">Calculation
					Type:<span class="mandatory">*</span>
				</td>
				<td width="34%" class="bluebox" id="CalculationTypeId2"><span
					class="greybox"> <html:select styleId="calculationType"
							property="calculationType" onchange="callTheType('DD');"
							styleClass="bigcombowidth">
							<html:option value='Flat'>Flat</html:option>
							<html:option value='Percentage'>Percentage</html:option>
						</html:select>
				</span></td>
			</tr>
			<tr id="hideRow5">
				<td class="greybox">&nbsp;</td>
				<td class="greybox">Applied To:<span class="mandatory">*</span></td>
				<td class="greybox"><html:select styleId="recovAppliedTo"
						property="recovAppliedTo" onchange="changeAppliedTo(this);"
						styleClass="bigcombowidth">
						<html:option value='0'>----choose----</html:option>
						<c:forEach var="applyVar" items="<%=partyMasterList%>">
							<html:option value="${applyVar.id}">${applyVar.name}</html:option>
						</c:forEach>
					</html:select> <input type="hidden" name="recovAppliedToHidden" /></td>
				<td class="greybox" id="bankLoanCheckBox" name="bankLoanCheckBox"
					align="right" style="display: none">Bank Loan&nbsp;<input
					type="checkbox" name="isBankLoan" id="isBankLoan"
					onclick="getBankList(this)" />
				</td>
				<html:hidden property="bankLoan" />
				<td class="greybox" colspan="2"></td>
			</tr>

			<tr id="EarDedRow" style="display: none">
				<td class="greybox">&nbsp;</td>
				<td class="greybox">Type</td>
				<td class="greybox"><html:select property="isEarning"
						styleClass="bigcombowidth" value="<%=rsf.getIsEarning()%>"
						onchange="toggleAccountCodes();">
						<html:option value='0'>Deduction</html:option>
						<html:option value='1'>Earning</html:option>
					</html:select></td>
				<td class="greybox" colspan="2"></td>
			</tr>
			<tr id="bankListRow" style="display: none">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">Bank<span class="mandatory">*</span></td>
				<td class="bluebox"><html:select property="bank"
						styleClass="bigcombowidth">
						<html:option value='0'>----Choose----</html:option>
						<c:forEach var="b" items="<%=bankList%>">
							<html:option value="${b.id}">${b.name}</html:option>
						</c:forEach>
					</html:select></td>
				<td class="bluebox" colspan="2"></td>
			</tr>

			<!--old-->
			<tr id="hideRow4">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">Account Code:<span class="mandatory">*</span></td>
				<td class="bluebox"><html:select styleId="recovAccCodeId"
						property="recovAccCodeId" onchange="splitGlCode();"
						styleClass="bigcombowidth">
						<html:option value='0'>----choose----</html:option>
						<c:forEach var="glcodeVar" items="<%=glCodeList%>">
							<html:option value="${glcodeVar.id}">${glcodeVar.name}</html:option>
						</c:forEach>
					</html:select></td>
				<td class="bluebox" colspan="2"><html:text styleId="recAccDesc"
						readonly="true" tabindex="-1" style="width:250px;text-align:left"
						property="recAccDesc" /></td>
			</tr>

			<tr id="onlyEmp" style="display: none">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" align="right">Account Code<span
					class="mandatory">*</span></td>
				<td class="bluebox" align="center" colspan="3"><html:select
						styleId="emprecovAccCodeId" property="emprecovAccCodeId"
						onchange="empsplitGlCode();" styleClass="bigcombowidth">
						<html:option value='0'>----choose----</html:option>
						<c:forEach var="empglcodeVar" items="<%=empglCodeList%>">
							<html:option value="${empglcodeVar.id}">${empglcodeVar.name}</html:option>
						</c:forEach>
					</html:select>&nbsp; <html:text styleId="emprecAccDesc" readonly="true"
						tabindex="-1" style="width:250px;text-align:left"
						property="emprecAccDesc" /></td>
			</tr>


			<tr id="descriptionId">
				<td class="greybox">&nbsp;</td>
				<td class="greybox">Description:</td>
				<td colspan="3" class="greybox"><html:textarea
						styleId="description" style="width: 580px;" property="description" /></td>
			</tr>
			<tr id="hideRow6">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">Remitted To:<span class="mandatory">*</span></td>
				<td class="bluebox"><html:text styleId="recovRemitTo"
						style="text-align:left" property="recovRemitTo" size="35" /></td>
				<td class="bluebox">BSR Code:</td>
				<td class="bluebox"><html:text styleId="recovBSRCode"
						style="text-align:left" property="recovBSRCode" size="35" /></td>
			</tr>
			<tr id="IFSCCodeRowId">
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">IFSC Code:</td>
				<td class="bluebox"><html:text styleId="recovIFSCCode"
						style="text-align:left" property="recovIFSCCode"
						onchange="validateIFSCCode(this)" size="35" /></td>
				<td class="bluebox">Bank Account:</td>
				<td class="bluebox"><html:text styleId="recovBankAccount"
						style="text-align:left" property="recovBankAccount"
						onchange="validateBankAccount(this)" size="35" /></td>
			</tr>

			<tr id="SectionRowId">
				<td class="greybox">&nbsp;</td>
				<td class="greybox">Section:</td>
				<td class="greybox"><html:text styleId="section"
						style="text-align:left" property="section" size="20" /></td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>

			<tr>
				<td class="bluebox" colspan="5">&nbsp;</td>
			</tr>

		</table>


		<div style="overflow-x: auto; overflow-y: hidden; width: 100%;"
			id="hidegridRecovery" align="center">
			<table width="75%" border="0" cellpadding="0" cellspacing="0"
				class="tablebottom" id="gridRecoverySetup" name="gridRecoverySetup">
				<tr>
					<th width="5%" class="bluebgheadtd">Party Sub-type</th>
					<th width="10%" class="bluebgheadtd">Document Type</th>
					<th width="10%" class="bluebgheadtd">From Date</th>
					<th width="10%" class="bluebgheadtd">To Date</th>
					<th width="20%" colspan="2">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<th class="bluebgheadtd" colspan="2" height="25"><div
										align="center">Amount</div></th>
							</tr>
							<tr>
								<th class="bluebgheadtd" width="50%"><div align="center">Low</div></th>
								<th class="bluebgheadtd" width="50%"><div align="center">High</div></th>
							</tr>
						</table>
					</th>
					<th width="20%" colspan="2">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<th class="bluebgheadtd" colspan="2" height="25"><div
										align="center">Cumulative Amount</div></th>
							</tr>
							<tr>
								<th class="bluebgheadtd" width="50%"><div align="center">Low</div></th>
								<th class="bluebgheadtd" width="50%"><div align="center">High</div></th>
							</tr>
						</table>
					</th>
					<th colspan="4" width="20%" id="ItScEcId">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<th class="bluebgheadtd" colspan="4"><div align="center">IT/SC/EC</div></th>
							</tr>
							<tr>
								<th class="bluebgheadtd" width="25%"><div align="center">IT</div></th>
								<th class="bluebgheadtd" width="25%"><div align="center">Sur
										Charge</div></th>
								<th class="bluebgheadtd" width="25%"><div align="center">EDU
										Cess</div></th>
								<th class="bluebgheadtd" width="25%"><div align="center">Total</div></th>
							</tr>
						</table>
					</th>
					<th width="5%" class="bluebgheadtd" id="FlatAmtId">Flat Amount</th>
					<th width="5%" class="bluebgheadtd" id="AddDelId">Add/del</th>
				</tr>

				<% 
		if(rsf.getLowAmount()==null && session.getAttribute("mode").equals("create"))
		{	
			logger.info("INSIDE JSP --------------->CREATE MODE");
	%>
				<tr id="detailsRow" name="detailsRow" onClick="selected(this);"
					height="40">
					<!--
			<td class="tdStlyle"><div align="left" id=recovery_srNo name="recovery_srNo"></div></td>
			-->
					<td style="display: none" class="blueborderfortd"><html:text
							styleId="id" property="id" value="" /></td>
					<td style="display: none" class="blueborderfortd"><html:text
							styleId="appliedToHiddenId" property="appliedToHiddenId" value="" /></td>
					<td class="blueborderfortd"><div align="center">
							<html:select property="partyType" styleId="partyType" onchange="">
								<html:option value='0'>--Choose--</html:option>
							</html:select>
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:select property="docType" styleId="docType" onchange="">
								<html:option value='0'>--Choose--</html:option>
							</html:select>
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="recovDateFrom" value="" size="12"
								maxlength="10"
								onkeyup="DateFormat(this,this.value,event,false,'3')" />
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="recovDateTo" value="" size="12"
								maxlength="10"
								onkeyup="DateFormat(this,this.value,event,false,'3')" />
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="lowAmount" style="text-align:right" value=""
								size="12" maxlength="16" onblur="" />
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="highAmount" style="text-align:right"
								value="" size="12" maxlength="16" onblur="" />
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="cumulativeAmountLow"
								style="text-align:right" value="" size="12" maxlength="16"
								onblur="" />
						</div></td>
					<td class="blueborderfortd"><div align="center">
							<html:text property="cumulativeAmountHigh"
								style="text-align:right" value="" size="12" maxlength="16"
								onblur="" />
						</div></td>
					<td class="blueborderfortd" id="ItScEcTextId1"><div
							align="center">
							<html:text property="ITPercentage" style="text-align:right"
								value="" size="12" maxlength="5" onblur="calcTotalPer(this);" />
						</div></td>
					<td class="blueborderfortd" id="ItScEcTextId2"><div
							align="center">
							<html:text property="surPercentage" style="text-align:right"
								value="" size="12" maxlength="5" onblur="calcTotalPer(this);" />
						</div></td>
					<td class="blueborderfortd" id="ItScEcTextId3"><div
							align="center">
							<html:text property="eduCessPercentage" style="text-align:right"
								value="" size="12" maxlength="5" onblur="calcTotalPer(this);" />
						</div></td>
					<td class="blueborderfortd" id="ItScEcTextId4"><div
							align="center">
							<html:text property="totalPercentage" style="text-align:right"
								value="" size="12" maxlength="5" tabindex="-1" readonly="true" />
						</div></td>
					<td class="blueborderfortd" id="flatAmountTextId"><div
							align="center">
							<html:text property="flatAmount" style="text-align:right"
								value="" size="12" maxlength="16" />
						</div></td>
					<td class="blueborderfortd">
						<div align="center">
							<a href="#"><img src="/egi/resources/erp2/images/addrow.gif"
								alt="Add" width="18" height="18" border="0"
								onclick="javascript:addRow();" /></a> <a href="#"><img
								src="/egi/resources/erp2/images/removerow.gif" alt="Del"
								width="18" height="18" border="0"
								onclick="javascript:return deleteRow(this);" /></a>
						</div>
					</td>
				</tr>
				<%
		}
		else if((rsf.getLowAmount()!=null && !session.getAttribute("mode").equals("create")))
		{
		
			 String FlatAmtStyleMaker = "", PercentageAmtStyleMaker = "";
			 if(rsf.getCalculationType() != null && rsf.getCalculationType().equals("Percentage")) {
					FlatAmtStyleMaker = "style='display:none'"; 
			 } else {
					PercentageAmtStyleMaker = "style='display:none'"; 
			 }
			
		   for(int i=0;i<rsf.getDocType().length;i++)
		   {
		 	logger.info("INSIDE JSP --------------->VIEW/MODIFY MODE");
		 	logger.info("PARTY TYPE ID------------>"+rsf.getPartyType()[i]);
			//logger.info("DOC TYPE ID-------------->"+rsf.getDocType()[i]);
			//logger.info("DOC SUBTYPE ID------------>"+rsf.getSubType()[i]); 
			//logger.info("LIST OF PTYPE------------>"+session.getAttribute("partyTypeList")); 
 	 	  %>
				<tr id="detailsRow" name="detailsRow" onClick="selected(this);"
					height="40">
					<!--
			<td class="tdStlyle"><div align="left" id=recovery_srNo name="recovery_srNo"></div></td>
			-->
					<td style="display: none" class="blueborderfortd"><html:text
							property="id" value="<%= (rsf.getId()[i])%>" /></td>
					<td style="display: none" class="blueborderfortd"><html:text
							property="appliedToHiddenId"
							value="<%= (rsf.getAppliedToHiddenId()[i])%>" /></td>
					<td class="blueborderfortd"><html:select property="partyType"
							styleId="partyType" style="width:90 px" onchange="">
							<html:option value='0'>--Choose--</html:option>
						</html:select></td>
					<td class="blueborderfortd"><html:select property="docType"
							styleId="docType" style="width:90 px" onchange="">
							<html:option value='0'>--Choose--</html:option>
						</html:select></td>

					<td class="blueborderfortd"><html:text
							property="recovDateFrom"
							value="<%= (rsf.getRecovDateFrom()[i])%>" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /></td>
					<td class="blueborderfortd"><html:text property="recovDateTo"
							value="<%= (rsf.getRecovDateTo()[i])%>" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /></td>

					<td class="blueborderfortd"><html:text property="lowAmount"
							style="text-align:right" value="<%= (rsf.getLowAmount()[i])%>"
							maxlength="16" onblur="" /></td>
					<td class="blueborderfortd"><html:text property="highAmount"
							style="text-align:right" value="<%= (rsf.getHighAmount()[i])%>"
							maxlength="16" onblur="" /></td>

					<td class="blueborderfortd"><html:text
							property="cumulativeAmountLow" style="text-align:right"
							value="<%= (rsf.getCumulativeAmountLow()[i])%>" maxlength="16"
							onblur="" /></td>
					<td class="blueborderfortd"><html:text
							property="cumulativeAmountHigh" style="text-align:right"
							value="<%= (rsf.getCumulativeAmountHigh()[i])%>" maxlength="16"
							onblur="" /></td>

					<td class="blueborderfortd" id="ItScEcTextId1"
						<%=PercentageAmtStyleMaker%>><html:text
							property="ITPercentage" style="text-align:right"
							value="<%= (rsf.getITPercentage()[i])%>" maxlength="5"
							onblur="calcTotalPer(this);" /></td>
					<td class="blueborderfortd" id="ItScEcTextId2"
						<%=PercentageAmtStyleMaker%>><html:text
							property="surPercentage" style="text-align:right"
							value="<%= (rsf.getSurPercentage()[i])%>" maxlength="5"
							onblur="calcTotalPer(this);" /></td>
					<td class="blueborderfortd" id="ItScEcTextId3"
						<%=PercentageAmtStyleMaker%>><html:text
							property="eduCessPercentage" style="text-align:right"
							value="<%= (rsf.getEduCessPercentage()[i])%>" maxlength="5"
							onblur="calcTotalPer(this);" /></td>
					<td class="blueborderfortd" id="ItScEcTextId4"
						<%=PercentageAmtStyleMaker%>><html:text
							property="totalPercentage" style="text-align:right"
							readonly="true" tabindex="-1" value="" maxlength="5" /></td>

					<td class="blueborderfortd" id="flatAmountTextId"
						<%=FlatAmtStyleMaker%>><html:text property="flatAmount"
							style="text-align:right" value="<%= (rsf.getFlatAmount()[i])%>"
							maxlength="16" /></td>

					<%
				if( ((String)session.getAttribute("mode")).equalsIgnoreCase("modify")) { %>
					<td class="blueborderfortd">
						<div align="center">
							<a href="#"><img src="/egi/resources/erp2/images/addrow.gif"
								alt="Add" width="18" height="18" border="0"
								onclick="javascript:addRow();" /></a> <a href="#"><img
								src="/egi/resources/erp2/images/removerow.gif" alt="Del"
								width="18" height="18" border="0"
								onclick="javascript:return deleteRow(this);" /></a>
						</div>
					</td>
					<% }
			 %>

				</tr>
				<%
		   }
		}
      %>
			</table>
		</div>

		<div id="MandatoryFieldsId">
			<div class="subheadsmallnew"></div>
			<div align="left" class="mandatory">* Mandatory Fields</div>
		</div>
		</div>

		<div class="buttonbottom">
			<table align="center">
				<tr id="hideRow8">
					<td><input type="hidden" name="button" id="button" /> <html:button
							styleClass="buttonsubmit" value="Save & Close" property="b1"
							onclick="ButtonPress('saveclose')" /> <html:button
							styleClass="buttonsubmit" value="Save & New" property="b2"
							onclick="ButtonPress('savenew')" /></td>
					<td><html:reset styleClass="button" value="Cancel"
							property="b4" /></td>
					<td><html:button styleClass="button" value="Close"
							property="b3" onclick="window.close();" /></td>
				</tr>
				<tr id="hideRow11" style="display: none">
					<td><html:button styleClass="buttonsubmit"
							value=" Save & Close" property="b1"
							onclick="ButtonPress('saveclose')" /> <html:button
							styleClass="button" value="Back" property="b2"
							onclick="ButtonPress('backModify')" /> <html:button
							styleClass="button" value="Close" property="b3"
							onclick="window.close();" /></td>
				</tr>

				<tr id="hideRow9" style="display: none">
					<td align="center">
						<!-- <html:button styleClass="buttonsubmit" value="  View   " property="b1" onclick="ButtonPress('view')" /> -->
						<html:button styleClass="button" value="  Close  " property="b3"
							onclick="window.close();" />
					</td>
				</tr>
				<tr id="hideRow12" style="display: none">
					<td align="center"><html:button styleClass="buttonsubmit"
							value="  Modify  " property="b4" onclick="ButtonPress('modify')" />
						<html:button styleClass="button" value="  Close  " property="b3"
							onclick="window.close();" /></td>
				</tr>

				<tr id="hideRow10" style="display: none" name="hideRow10">
					<td align="center"><html:button styleClass="button"
							value="Back" property="b3" onclick="ButtonPress('backView')" />
						<html:button styleClass="button" value="Close" property="b4"
							onclick="window.close();" /></td>
				</tr>
			</table>
		</div>


		<html:javascript formName="RecoverySetupForm" />
	</html:form>
</body>
</html>
