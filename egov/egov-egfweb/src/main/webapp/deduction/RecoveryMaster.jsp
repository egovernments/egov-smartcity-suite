<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="java.util.*,java.text.SimpleDateFormat,java.text.*,java.math.BigDecimal, 
		org.apache.log4j.Logger,org.egov.infstr.utils.EgovMasterDataCaching,
		org.egov.commons.EgPartytype,org.egov.commons.EgwTypeOfWork,
		java.text.DecimalFormat,org.egov.lib.deduction.client.RecoverySetupForm"
%>

<html>
<head>

	<title>Recovery Master</title>
	
<%
RecoverySetupForm rsf=(RecoverySetupForm)request.getAttribute("RecoverySetupForm");
%>	
<script>
var myrowId;
var booleanValue;



function uniqueCheckForAccCode()
{
	
	<% 
	if(rsf.getRecovAccCodeId()==null)
	{%>
			
		// For create Mode	
		booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'recovAccCodeId', 'no', 'no');
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
			booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'recovAccCodeId', 'no', 'no');
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
		booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'type', 'recovCode', 'no', 'no');

			if(booleanValue==false)
			{
				bootbox.alert("This Recovery code already used for some other Recoveries!!!!");
				var temp="document.RecoverySetupForm.recovCode.focus();";
				setTimeout(temp,0);		
				return false;
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
			booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'type', 'recovCode', 'no', 'no');

			if(booleanValue==false)
			{
				bootbox.alert("This Recovery code already used for some other Recoveries!!!!");
			var temp="document.RecoverySetupForm.recovCode.focus();";
			setTimeout(temp,0);	
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
	
	//document.forms[0].docType[lastRow-1].options.length=0;
	//document.forms[0].subType[lastRow-1].options.length=0;
	
	document.forms[0].recovDateFrom[lastRow-1].value="";
	document.forms[0].recovDateTo[lastRow-1].value="";
	document.forms[0].lowAmount[lastRow-1].value="";
	document.forms[0].highAmount[lastRow-1].value="";
	document.forms[0].ITPercentage[lastRow-1].value="";
	document.forms[0].surPercentage[lastRow-1].value="";
	document.forms[0].eduCessPercentage[lastRow-1].value="";
	document.forms[0].totalPercentage[lastRow-1].value="";
	document.forms[0].flatAmount[lastRow-1].value="";
}
function deleteRow()
{

  var tbl = document.getElementById('gridRecoverySetup');
  var lastRow = (tbl.rows.length)-1;

	if(lastRow ==1)
	{
		bootbox.alert("This row can not be deleted");
		return false;
	 }
	else
	{
		tbl.deleteRow(lastRow);
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
				bootbox.alert("IT should be in Number");
				return false;
			}
			
			if(surPer!="")
			{
				if(isNaN(surPer))
				{
					bootbox.alert("SurCharge should be in Number");
				return false;
				}
			}
			if(eduPer!="")
			{
				if(isNaN(eduPer))
				{
					bootbox.alert("Education Cess should be in Number");
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
	//col1=getControlInBranch(table.rows[i],"partyType").value;
	col2=getControlInBranch(table.rows[i],"docType").value;
	col3=getControlInBranch(table.rows[i],"recovDateFrom").value;
	col4=getControlInBranch(table.rows[i],"lowAmount").value;
	//bootbox.alert(document.RecoverySetupForm.recovAppliedTo.options[document.RecoverySetupForm.recovAppliedTo.selectedIndex].text);
	// If Applied To Not a Employee means Document Type is mandatory
	if(!(document.RecoverySetupForm.recovAppliedTo.options[document.RecoverySetupForm.recovAppliedTo.selectedIndex].text.toLowerCase()=='employee'))
	{
		//bootbox.alert("If Not a Employee");
		
		/*if(!(col1!=0 && col1!=""))
		{
		bootbox.alert("In Recovery Master-->"+i+" Row Party Type Cannot be empty");
		getControlInBranch(table.rows[i],'partyType').focus();
		return false;
		}
		*/
		if(!(col2!=0 && col2!=""))
		{
			bootbox.alert("In Recovery Master-->"+i+" Row Document Type Cannot be empty");
		getControlInBranch(table.rows[i],'docType').focus();
		return false;
		}
	}	
		if(!(col3!=0 && col3!=""))
		{
			bootbox.alert("In Recovery Master-->"+i+" Row DateFrom Cannot be empty");
		getControlInBranch(table.rows[i],'recovDateFrom').focus();
		return false;
		}
		if(!(col4!=0 && col4!=""))
		{
			bootbox.alert("In Recovery Master-->"+i+" Row Payment Low Limit Cannot be empty");
		getControlInBranch(table.rows[i],'lowAmount').focus();
		return false;
		}
				
	}// for
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
		
	
		if(col2!="" && col2.length>0)
		{
			if((compareDate(formatDate6(col1),formatDate6(col2)) !=1))
			 {
				bootbox.alert("In Recovery Master-->"+i+" Row To Date must be Higher than the From Date");
			 getControlInBranch(table.rows[i],'recovDateTo').focus();				
			 return false;
			 }
		 }
		 if(col3!="")
		 {
		 	if(isNaN(col3))
		 	{
		 		bootbox.alert("Low Limit value should be in Number");
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
				bootbox.alert("High Limit value should be in Number");
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
				bootbox.alert("In Recovery Master-->"+i+" Row High Limit  value must be Higher than the Low Limit value");
			getControlInBranch(table.rows[i],'highAmount').focus();				
			return false;
			}
			if(maxVal>parseFloat(9999999999999.99))
			{
				bootbox.alert("High Limit Values  cannot be greater than 9999999999999.99");
			getControlInBranch(table.rows[i],'highAmount').focus();				
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
	row1col3=getControlInBranch(table.rows[i],"subType");
	
	row1col4=getControlInBranch(table.rows[i],"recovDateFrom").value;
	row1col5=getControlInBranch(table.rows[i],"lowAmount").value;
	row1col6=getControlInBranch(table.rows[i],"highAmount").value;
	row1col7=getControlInBranch(table.rows[i],"recovDateTo").value;

	
	for(var j=i+1;j<table.rows.length;j++)
	{
	rowcol1=getControlInBranch(table.rows[j],"partyType");
	rowcol2=getControlInBranch(table.rows[j],"docType");
	rowcol3=getControlInBranch(table.rows[j],"subType");
	
	rowcol4=getControlInBranch(table.rows[j],"recovDateFrom").value;
	rowcol5=getControlInBranch(table.rows[j],"lowAmount").value;
	rowcol6=getControlInBranch(table.rows[j],"highAmount").value;
	rowcol7=getControlInBranch(table.rows[j],"recovDateTo").value;
	
	
	if((row1col1.value==rowcol1.value))
	{	
	   if((row1col2.value==rowcol2.value) && rowcol2.value.length>0)
	   {
    
	     if((row1col3.value==rowcol3.value)) 
		 { 
		
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

			  /*if((compareDate(formatDate6(previousMaxDate),formatDate6(curfromDateVal)) != 1))
			  {
				 bootbox.alert("In Recovery Master-->"+j+" Row From Date must be Higher than the "+i+" Row Date Range");
				 getControlInBranch(table.rows[j],'recovDateFrom').focus();				
				 return false;
			  }
			  */
			  
			// Payment Limit validation
			
							 
			if(rowcol5>=0 && rowcol5!="")
			{
				minVal=parseInt(rowcol5);
				if(minVal>parseFloat(9999999999999.99))
				{
					bootbox.alert("Low Limit Values  cannot be greater than 9999999999999.99");
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
					bootbox.alert("In Recovery Master-->"+j+" Row Low Limit  value must be Higher than the "+i+" Row Payment Limits");
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
				  bootbox.alert("In Recovery Master-->"+j+" Row From Date must be Higher than the "+i+" Row Date Range");
				 getControlInBranch(table.rows[j],'recovDateFrom').focus();				
				 return false;
			  }
			 

			// Payment Limit validation

			if(rowcol5>=0 && rowcol5!="")
			{
				minVal=parseInt(rowcol5);
				if(minVal>parseFloat(9999999999999.99))
				{
					bootbox.alert("Low Limit Values  cannot be greater than 9999999999999.99");
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
					bootbox.alert("In Recovery Master-->"+j+" Row Low Limit  value must be Higher than the "+i+" Row Payment Limits");
					getControlInBranch(table.rows[j],'lowAmount').focus();				
					return false;
				}
			}
			*/
			
         
	       }//else From Date Not equal
	     } // doc sub type
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
			row1col3=getControlInBranch(table.rows[i],"subType");
			row1col4=getControlInBranch(table.rows[i],"recovDateFrom");
			row1col5=getControlInBranch(table.rows[i],"lowAmount");
			
						
			for(var j=i+1;j<table.rows.length;j++)
			{
				rowcol1=getControlInBranch(table.rows[j],"partyType");
				rowcol2=getControlInBranch(table.rows[j],"docType");
				rowcol3=getControlInBranch(table.rows[j],"subType");
				rowcol4=getControlInBranch(table.rows[j],"recovDateFrom");
				rowcol5=getControlInBranch(table.rows[j],"lowAmount");
				
				if((row1col1.value==rowcol1.value))
				{
				
				   if((row1col2.value==rowcol2.value) && rowcol2.value.length>0)
				   {
				     
				     //sub-type is entered
				     if((row1col3.value==rowcol3.value)) 
					 {
					 if((compareDate(formatDate6(row1col4.value),formatDate6(rowcol4.value)) == 0) && rowcol4.value.length>0)
					 	{
					 	
					 	
					 		if((row1col5.value==rowcol5.value) && rowcol5.value.length>0)
					 		{
					 			bootbox.alert("In Recovery Master-->"+j+" Row Data  Cannot be Same as "+i+" Row Data");
					 		getControlInBranch(table.rows[j],'recovDateFrom').focus();
					 		return false;
					 		}
					 		
					 	}
					 }
					
					else if(row1col3.value==0 && rowcol3.value==0)// sub-type is not entered
					{
						 if((compareDate(formatDate6(row1col4.value),formatDate6(rowcol4.value)) == 0) && rowcol4.value.length>0)
						{
							if((row1col5.value==rowcol5.value) && rowcol5.value.length>0)
							{
								bootbox.alert("In Recovery Master-->"+j+" Row Data  Cannot be Same as "+i+" Row Data");
							getControlInBranch(table.rows[j],'recovDateFrom').focus();
							return false;
							}

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
	var col1,col2,col3,col4,col5,col6;
		
	for(var i=1;i<table.rows.length;i++)
	{
	col1=getControlInBranch(table.rows[i],"lowAmount").value;
	col2=getControlInBranch(table.rows[i],"highAmount").value;
	
	col3=getControlInBranch(table.rows[i],"ITPercentage").value;
	col4=getControlInBranch(table.rows[i],"surPercentage").value;
	col5=getControlInBranch(table.rows[i],"eduCessPercentage").value;
	col6=getControlInBranch(table.rows[i],"flatAmount").value;
	
	var valPresentPer=false;
	var valPresentFlat=false;
	
		if(col1!="")
		{
			if(isNaN(col1))
			{
				bootbox.alert(" Low Limit value should be in Number");
			getControlInBranch(table.rows[i],'lowAmount').focus();
			return false;
			}
			else
			col1=parseFloat(col1)
			if(col1<0)
			{
				bootbox.alert("In Recovery Master-->"+i+" Row Low Limit value should not be Negative");
			getControlInBranch(table.rows[i],'lowAmount').focus();
			return false;
			}
		}
		if(col2!="")
		{
			if(isNaN(col2))
			{
				bootbox.alert(" High Limit value should be in Number");
			getControlInBranch(table.rows[i],'highAmount').focus();
			return false;
			}
			else
			col2=parseFloat(col2)
			if(col2<0)
			{bootbox.alert("In Recovery Master-->"+i+" Row High Limit value should not be Negative");
			getControlInBranch(table.rows[i],'highAmount').focus();
			return false;
			}
			
		}
		if(col3!="")
		{
			if(isNaN(col3))
			{
				bootbox.alert(" IT percentage should be Number in 0 to 100");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
			}
			else
			col3=parseFloat(col3)
			if(col3<0)
			{bootbox.alert("In Recovery Master-->"+i+" Row ITPercentage value should not be Negative");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
			}
			valPresentPer=true;
		}
		if(col4!="")
		{
			if(isNaN(col4))
			{
				bootbox.alert(" Surcharge value should be Number in 0 to 100");
			getControlInBranch(table.rows[i],'surPercentage').focus();
			return false;
			}
			else
			col4=parseFloat(col4)
			if(col4<0)
			{bootbox.alert("In Recovery Master-->"+i+" Row Surcharge value should not be Negative");
			getControlInBranch(table.rows[i],'surPercentage').focus();
			return false;
			}
			valPresentPer=true;
		}
		if(col5!="")
		{
			if(isNaN(col5))
			{
				bootbox.alert(" Education Cess value should be Number in 0 to 100");
			getControlInBranch(table.rows[i],'eduCessPercentage').focus();
			return false;
			}
			else
			col5=parseFloat(col5)
			if(col5<0)
			{bootbox.alert("In Recovery Master-->"+i+" Row Education Cess value should not be Negative");
			getControlInBranch(table.rows[i],'eduCessPercentage').focus();
			return false;
			}
			valPresentPer=true;
		}
		if(col6!="")
		{
			if(isNaN(col6))
			{
				bootbox.alert(" Flat Amount should be in Number");
			getControlInBranch(table.rows[i],'flatAmount').focus();
			return false;
			}
			else
			col6=parseFloat(col6)
			if(col6<0)
			{bootbox.alert("In Recovery Master-->"+i+" Row Flat Amount should not be Negative");
			getControlInBranch(table.rows[i],'flatAmount').focus();
			return false;
			}
			valPresentFlat=true;
		}
		
		if(valPresentPer)
		{
			if(valPresentFlat)
			{
				bootbox.alert("In Recovery Master-->"+i+" Row Enter Either Percentage value OR Flat Amount value");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
			
			}
		
		}
		else if(valPresentFlat)
		{
		
			if(valPresentPer)
			{
				bootbox.alert("In Recovery Master-->"+i+" Row Enter Either Percentage value OR Flat Amount value");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
						
			}
		
		}
		else
		{
			bootbox.alert("In Recovery Master-->"+i+" Row Enter Either Percentage value OR Flat Amount value->It should not be empty");
			getControlInBranch(table.rows[i],'ITPercentage').focus();
			return false;
		}
		
		
	
	}//for
	
return true;
}
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
}
function ButtonPress(arg)
{	
   
    if(arg!="view" && arg!="modify" && arg!="backView" && arg!="backModify")
    {
     	
		if(document.RecoverySetupForm.isEarning.value==1)
		{
				if(document.RecoverySetupForm.emprecovAccCodeId.value == "0")
				{
					bootbox.alert("Select Account Code");
						var temp="document.RecoverySetupForm.emprecovAccCodeId.focus();";
						setTimeout(temp,0);		
						return;
				}
				else
				{
				if(!uniqueCheckForEmpEarningCode())
					{
								return false;
					}
				}

						
				if(document.RecoverySetupForm.recovAppliedTo.value == "0")
				{
					bootbox.alert("Select Applied To");
						var temp="document.RecoverySetupForm.recovAppliedTo.focus();";
						setTimeout(temp,0);		
						return;
				}
		
		}
		else
		{
		if(document.RecoverySetupForm.recovAccCodeId.value == "0")
		{
			bootbox.alert("Select Account Code");
				var temp="document.RecoverySetupForm.recovAccCodeId.focus();";
				setTimeout(temp,0);		
				return;
		}
		if(document.RecoverySetupForm.recovAppliedTo.value == "0")
		{
			bootbox.alert("Select Applied To");
				var temp="document.RecoverySetupForm.recovAppliedTo.focus();";
				setTimeout(temp,0);		
				return;
		}

		}
		if(!validateRecoverySetupForm(document.RecoverySetupForm))
		return;
		
		if(document.RecoverySetupForm.flag[0].checked==true)
		{
			if(document.RecoverySetupForm.recovRemitTo.value=="")
			{
				bootbox.alert("Remitted to is required!");
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
			
			if(!checkEitherPerOrFlat())return;
			
			if(!checkLimitsAll())return;
			
			if(!checkLimitsWithCombination())return;
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
		
		if(document.RecoverySetupForm.isBankLoan.checked==false)
		{
			if(!uniqueCheckForAccCode())
				return;
		}
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
		document.getElementById("flagRow").disabled=false;
		if(mode == "create")
		{
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
function onClickCancel()
{
	document.RecoverySetupForm.reset();
}
function splitGlCode()
{


	if(document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].value!=0)
	{
		var arr=document.RecoverySetupForm.recovAccCodeId.options[document.RecoverySetupForm.recovAccCodeId.selectedIndex].text.split("`-`");
		//bootbox.alert(arr[0]);
		//bootbox.alert(arr[1]);
	
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
	document.RecoverySetupForm.flag[0].checked=true;	
	<% 
		ArrayList glCodeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-coaCodesForLiability");
		ArrayList empglCodeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-AllCoaCodesOfEarning");
		ArrayList partyMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-partyTypeMaster");
		ArrayList tdsTypeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-tdsType");
		ArrayList bankList=(ArrayList)EgovMasterDataCaching.getInstance().get("egf-bank");
	%>
		
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		bootbox.alert("<%=request.getAttribute("alertMessage")%>");		
	}
	
	var mode="${mode}";
	
	if(mode == "searchView")
	{
			document.title="View Recovery Master";
			//document.getElementById('screenName').innerHTML="View-Recovery Master";
			document.getElementById("hideRow1").style.display="block";
			hideRowsViewModify();
			document.getElementById("hidegridRecovery").style.display="none";
			//document.RecoverySetupForm.recovAccCodeId.options.selectedIndex=0;
			//document.RecoverySetupForm.recovAppliedTo.options.selectedIndex=0;
			document.getElementById("hideRow9").style.display="block";
			document.getElementById("flagRow").style.display="none";
	}
	if(mode == "searchModify")
	{
			document.title="Modify Recovery Master";
			//document.getElementById('screenName').innerHTML="Modify-Recovery Master";
			document.getElementById("hideRow1").style.display="block";
			hideRowsViewModify();
			document.getElementById("hidegridRecovery").style.display="none";
			//document.RecoverySetupForm.recovAccCodeId.options.selectedIndex=0;
			//document.RecoverySetupForm.recovAppliedTo.options.selectedIndex=0;
			document.getElementById("hideRow12").style.display="block";
			document.getElementById("flagRow").style.display="none";
	}
	if(mode == "view")
	{	
		document.title="View Recovery Master";
		//document.getElementById("hidegridRecovery").style.display="block";
		document.getElementById("flagRow").disabled=true;
		document.getElementById("hideRow8").style.display="none";
		document.getElementById("hideRow9").style.display="none";
		document.getElementById("hideRow12").style.display="none";
		document.getElementById("hideRow10").style.display="block";		
		//document.getElementById('screenName').innerHTML="View-Recovery Master";
		if("<%=rsf.getRecMode()%>"=="automatic")
		{
			document.RecoverySetupForm.flag[0].checked=true;
			document.getElementById("bankListRow").style.display="none";
			document.getElementById("bankLoanCheckBox").style.display="none";
		}
		else
		{
			document.getElementById("hidegridRecovery").style.display="none";
			document.RecoverySetupForm.flag[1].checked=true;
		}
		if(document.RecoverySetupForm.bankLoan.value=="on")
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=true;			
			document.getElementById("bankLoanCheckBox").style.display="block";
			document.getElementById("bankListRow").style.display="block";
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
		document.getElementById("flagRow").disabled=true;
		document.getElementById("hideRow8").style.display="none";
		document.getElementById("hideRow11").style.display="block";
		document.getElementById("hideRow9").style.display="none";
		document.getElementById("hideRow12").style.display="none";
		document.getElementById("hideRow10").style.display="none";
		//document.getElementById('screenName').innerHTML="Modify-Recovery Master";
		if("<%=rsf.getRecMode()%>"=="automatic")
		{
			document.RecoverySetupForm.flag[0].checked=true;
			document.getElementById("bankListRow").style.display="none";
			document.getElementById("bankLoanCheckBox").style.display="none";
		}
		else
		{
			document.getElementById("hidegridRecovery").style.display="none";
			document.RecoverySetupForm.flag[1].checked=true;
		}
		if(document.RecoverySetupForm.bankLoan.value=="on")
		{			
			getGridData(document.RecoverySetupForm.recovAppliedTo);
			document.RecoverySetupForm.isBankLoan.checked=true;			
			document.getElementById("bankLoanCheckBox").style.display="block";
			document.getElementById("bankListRow").style.display="block";
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
		  
		  <% if(rsf.getIsEarning()!=null){
		  
		  if(rsf.getIsEarning().equalsIgnoreCase("1"))
		  		{
		  		rsf.setEmprecovAccCodeId(rsf.getRecovAccCodeId());
		  		%>
		  		document.getElementById("hideRow4").style.display="none";
		  		document.getElementById("onlyEmp").style.display="block";
				empsplitGlCode();
		  		<%
		  		}
		  		}
		%>
		var table=document.getElementById("gridRecoverySetup");
		

		for(var i=1;i<table.rows.length;i++)
		{
		
		var currRowObj=getControlInBranch(table.rows[i],"appliedToHiddenId");
		loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID','CODE','parentid=#1 order by code','appliedToHiddenId','partyType' ,currRowObj,'gridRecoverySetup');
		loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','appliedToHiddenId' ,'docType',currRowObj,'gridRecoverySetup');
		//loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','appliedToHiddenId' ,'subType',currRowObj,'gridRecoverySetup');

		} //for 

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
		
			
		for(var i=1;i<table.rows.length;i++)
		{
			
			var currRowObj=getControlInBranch(table.rows[i],"docType");
			loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARENTID=#1 order by code','docType','subType',currRowObj,'gridRecoverySetup');
					
		} //for
		

		var k=1;
		<% 
		
		if(rsf.getDocType()!=null)
		{
			if((rsf.getSubType()!=null && !session.getAttribute("mode").equals("create")))
			{
				for(int i=0;i<rsf.getSubType().length;i++)
				{

				%>
				getControlInBranch(table.rows[k],"subType").value="<%=(rsf.getSubType()[i])%>";
				k++;
				<%
				} // After loading combo-Assigned values
			}
		}	
		%>
		
	   
	} // main if
	
	var buttonType="${buttonType}";
	//bootbox.alert(buttonType);
	if(buttonType == "saveclose")
	{
		window.close();
	}
	if(target!="null" && buttonType!="saveclose")
	{
		window.location= "../deduction/recoverySetupMaster.do?submitType=toLoad";
			
	}
			
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
//document.getElementById("hideRow9").style.display="block";

}

function getSelected(obj)
{
	if(obj.value=="automatic")
	{
		document.getElementById("hidegridRecovery").style.display="block";
		document.getElementById("bankLoanCheckBox").style.display="none";
		getGridData(document.RecoverySetupForm.recovAppliedTo);	
	}
	else
	{		
		document.getElementById("hidegridRecovery").style.display="none";
		
	}
}

function getGridData(obj)
{	
	document.RecoverySetupForm.isBankLoan.checked=false;
	if(document.RecoverySetupForm.flag[0].checked)
	{		
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID', 'CODE', 'parentid=#1 order by code', 'recovAppliedTo', 'partyType');
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','recovAppliedTo' ,'docType');
		//loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','recovAppliedTo' ,'subType');
	}
	
	else if(document.RecoverySetupForm.flag[1].checked && obj.options[obj.selectedIndex].text=='Employee')
	{		
		document.getElementById("bankLoanCheckBox").style.display="block";		
	}
	else
	{
		document.getElementById("bankLoanCheckBox").style.display="none";
		//document.getElementById("hideRow6").style.display="block";	
		document.getElementById("bankListRow").style.display="none";
	}
	if(obj.options[obj.selectedIndex].text.toLowerCase()=='employee')
	{
	
		document.getElementById("EarDedRow").style.display="block";
		
		
	}
	else
	{
	document.getElementById("EarDedRow").style.display="none";
	document.getElementById("hideRow4").style.display="block";
	document.RecoverySetupForm.emprecovAccCodeId.value=0;
	document.getElementById("onlyEmp").style.display="none";
	}
	
}

function toggleAccountCodes()
{
if(document.RecoverySetupForm.isEarning.value==1)
{
document.getElementById("hideRow4").style.display="none";
document.getElementById("onlyEmp").style.display="block";
document.getElementById("recAccDesc").value="";
document.RecoverySetupForm.emprecAccDesc.value="";
document.RecoverySetupForm.recovAccCodeId.value=0;
}
else
{
document.getElementById("hideRow4").style.display="block";
document.getElementById("onlyEmp").style.display="none";
document.RecoverySetupForm.emprecAccDesc.value="";
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
		document.getElementById("bankListRow").style.display="block";
		document.RecoverySetupForm.bankLoan.value="on";
	}
	else
	{
		//document.getElementById("hideRow6").style.display="block";
		document.RecoverySetupForm.isEarning.disabled=false;
		document.getElementById("bankListRow").style.display="none";
		document.RecoverySetupForm.bankLoan.value="off";
	}	
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
		
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_PARTYTYPE', 'ID', 'CODE', 'parentid=#1 order by code', 'recovAppliedTo', 'partyType');
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is null order by code','recovAppliedTo' ,'docType');
		//loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARTYTYPEID=#1 and PARENTID is not null order by code','recovAppliedTo' ,'subType');
}
//donot move this method up 
function uniqueCheckForEmpEarningCode()                  
{
	<% 
	if(rsf.getEmprecovAccCodeId()==null)
	{
		%>
booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'emprecovAccCodeId', 'no', 'no');
		if(booleanValue==false)
		{
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
			booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'tds', 'glcodeid', 'emprecovAccCodeId', 'no', 'no');
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
</script>
</head>
<body bgcolor="#ffffff" onload="onBodyLoad();" onKeyDown ="CloseWindow(window.self);" onKeyUp="keyPressed();">
		<% 	Logger logger = Logger.getLogger(getClass().getName()); 
			logger.info(">>       INSIDE JSP   >>");
		%>

<html:form  action="/deduction/recoverySetupMaster.do" >
<input type="hidden" name="booleanVal" id="booleanVal" value="true">

		<table align="center" class="tableStyle"><!-------- table main2 start------------>
		
		
		<tr><td colspan=4>&nbsp;</td></tr>
		
		<TR>
		<TD>
		<TABLE>
		<tr id="flagRow">		
		 <td class="labelcellforsingletd" colspan="4">
		 Automatic&nbsp;&nbsp;<html:radio property="recMode" value="Automatic" onclick="getSelected(this)"/>
		 Manual&nbsp;&nbsp;<html:radio property="recMode" value="Manual" onclick="getSelected(this)"/>		
		</td>
		</tr>
		
		<tr style="DISPLAY: none"  id="hideRow1">
<!-- This List For view/modify purpose-->
			<td class="labelcell" align="right">Recovery Code<SPAN class="leadon">*</SPAN></td>
			<td class="smallfieldcell" align="center" >
			<html:select  property="tdsTypeId" onchange="" styleClass="bigcombowidth">
				<html:option value='0'>----choose----</html:option>
				<c:forEach var="tdsTypeVar" items="<%=tdsTypeList%>" > 
				<html:option value="${tdsTypeVar.id}">${tdsTypeVar.name}</html:option>
				</c:forEach> 
			</html:select>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr id="hideRow2">
		<td class="labelcell" align="right">Recovery&nbsp;Code<SPAN class="leadon">*&nbsp;&nbsp;</SPAN>
		<input type=hidden name="tdsIdHidden" id="tdsIdHidden"/>
		</td>
		<td class="fieldcell" align="center"><html:text  onblur="uniqueCheckForRecoveryCode();" style=";text-align:left" property="recovCode"  /> </td>
		<td></td>
		<td></td>
		</tr>
		
		<tr id="hideRow3">
		<td class="labelcell" align="right">Recovery&nbsp;Name<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
		<td class="fieldcell" align="center"><html:text  style=";text-align:left" property="recovName"  /> </td>
		<td class="labelcellforsingletd" align="right">Cap&nbsp;Limit&nbsp;&nbsp;</td>
		<td class="smallfieldcell" align="center"><html:text style="text-align:right" property="capLimit"/> </td>
		</tr>
		
		
		
		<tr id="hideRow5">
		<td class="labelcellforsingletd" align="right">Applied To<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
		<td class="smallfieldcell" align="center" >
		<html:select styleId="recovAppliedTo" property="recovAppliedTo" onchange="getGridData(this);deleteExtraRow();" styleClass="bigcombowidth">
			<html:option value='0'>----choose----</html:option>
		<c:forEach var="applyVar" items="<%=partyMasterList%>" > 
			<html:option value="${applyVar.id}">${applyVar.name}</html:option>
		</c:forEach> 
		</html:select>
		</td>
		<td class="labelcell" id="bankLoanCheckBox" name="bankLoanCheckBox" align="right" style="display:none">Bank Loan
		<input type="checkbox" name="isBankLoan" id="isBankLoan" onclick="getBankList(this)" /></td>
		<html:hidden property="bankLoan"/>
		<td></td>
		</tr>
		<tr id="EarDedRow" style="DISPLAY: none">
				
				<td class="labelcell" align="right" >Type</td>
					<td class="smallfieldcell" align="left">	
				
						<html:select property="isEarning" styleClass="bigcombowidth" value="<%=rsf.getIsEarning()%>" onchange="toggleAccountCodes();" >
						
						
						
						<html:option value='0' >Deduction</html:option>
						<html:option value='1'>Earning</html:option>
						</html:select>
						</td>
						
								
		</tr>
		
		<tr id="bankListRow" style="display:none">
			<td class="labelcell" align="right" >Bank<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
			<td class="smallfieldcell" align="left">			
				<html:select property="bank" styleClass="bigcombowidth" >
				<html:option value='0'>----Choose----</html:option>
				<c:forEach var="b" items="<%=bankList%>" > 
					<html:option value="${b.id}">${b.name}</html:option>
				</c:forEach>
				</html:select>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>		
			<td style="width:200px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
		</tr>
		<tr id="hideRow4">
						<td class="labelcellforsingletd" align="right">Account Code<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
						<td class="smallfieldcell" align="center" >
						<html:select  property="recovAccCodeId" onchange="splitGlCode();"  styleClass="bigcombowidth">
							<html:option value='0'>----choose----</html:option>
							<c:forEach var="glcodeVar" items="<%=glCodeList%>" > 
							<html:option value="${glcodeVar.id}">${glcodeVar.name}</html:option>
							</c:forEach> 			
						</html:select>
						</td>
						<td class="largefieldcell" align="right"><html:text  readonly="true" tabindex="-1" style="width:250px;text-align:left" property="recAccDesc"   /> </td>
						<td></td>
		</tr>
		<tr id="onlyEmp" style="Display:none">
								<td class="labelcellforsingletd" align="right">Account Code<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
								<td class="smallfieldcell" align="center" >
								<html:select  property="emprecovAccCodeId" onchange="empsplitGlCode();"  styleClass="bigcombowidth" >
									<html:option value='0'>----choose----</html:option>
									<c:forEach var="empglcodeVar" items="<%=empglCodeList%>" > 
									<html:option value="${empglcodeVar.id}">${empglcodeVar.name}</html:option>
									</c:forEach> 			
								</html:select>
								</td>
								<td class="largefieldcell" align="right"><html:text  readonly="true" tabindex="-1" style="width:250px;text-align:left" property="emprecAccDesc"   /> </td>
								<td></td>
		</tr>
		
				
		<tr id="hideRow6">
		<td class="labelcell" align="right">Remitted To<SPAN class="leadon">*&nbsp;&nbsp;</SPAN></td>
		<td class="largefieldcell" align="center"><html:text  style="width:250px;text-align:left" property="recovRemitTo"  /> </td>
		
		<td class="labelcellforsingletd" align="right">BSR Code&nbsp;</td>
		<td class="smallfieldcell" align="center"><html:text  style=";text-align:left" property="recovBSRCode"  /> </td>
		</tr>
		
		<tr>
		<td colspan=4>&nbsp;</td>
		</tr>
		
		</TABLE>
		</TD>
		</TR>
		
		
		<tr id="hidegridRecovery">
		<td colspan=4>
		<div class="tbl-containerLessHeight" id="divSpec">
			<table  cellpadding="0" cellspacing="0" align="center" id="gridRecoverySetup" name="gridRecoverySetup" >
			<tr >
			<!--<td class="thStlyle"><div align="center">SNo</div></td>-->
			<td style="display:none" class="thStlyle"><div align="center">Recovery Id</div></td>
			<td style="display:none" class="thStlyle"><div align="center">AppliedTo Id</div></td>
			<td class="thStlyle"><div align="center">Sub-Party Type</div></td>
			
			<td class="thStlyle" colspan=2 width="10%">
				<table cellpadding="0" cellspacing="0" border="0">
				<tr ><td class="thStlyle" colspan=2 width="10%"><div align="center">Document</div></td></tr>
				<tr >
				<td class="thStlyle" width="10%"><div align="center">Type</div></td>
				<td class="thStlyle" width="10%"><div align="center">Sub-Type</div></td>
				</tr>
				</table>
			</td>
			<td class="thStlyle"><div align="center">Date From</div></td>
			<td class="thStlyle"><div align="center">Date To</div></td>
			
			<td class="thStlyle" colspan=2 width="10%">
				<table cellpadding="0" cellspacing="0" border="0">
				<tr ><td class="thStlyle" colspan=2 width="10%"><div align="center">Payment Limit</div></td></tr>
				<tr >
				<td class="thStlyle"><div align="center">Low</div></td>
				<td class="thStlyle"><div align="center">High</div></td>
				</tr>
				</table>
			</td>
			
			<td class="thStlyle" colspan=4 width="100%">
				<table cellpadding="0" cellspacing="0" border="0">
				<tr ><td class="thStlyle" colspan=4 width="100%"><div align="center">Percentage</div></td></tr>
				<tr >
				<td class="thStlyle" width="25%"><div align="center">IT</div></td>
				<td class="thStlyle" width="25%"><div align="center">Sur charge</div></td>
				<td class="thStlyle" width="25%"><div align="center">EDU Cess</div></td>
				<td class="thStlyle" width="25%"><div align="center">Total</div></td>
				</tr>
				</table>
			</td>
			<td class="thStlyle"><div align="center">Flat Amount</div></td>
			</tr>
			
	<% 
		if(rsf.getLowAmount()==null && session.getAttribute("mode").equals("create"))
		{	logger.info("INSIDE JSP --------------->CREATE MODE");
	%>
		
			<tr id="detailsRow" name="detailsRow" onClick="selected(this);">
			<!--
			<td class="tdStlyle"><div align="left" id=recovery_srNo name="recovery_srNo"></div></td>
			-->
			<td style="display:none" class="smallfieldcell"><html:text  property="id" value="" /></td> 
			<td style="display:none" class="smallfieldcell"><html:text  property="appliedToHiddenId" value="" /></td> 
			<td class="smallfieldcell"><html:select  property="partyType" styleId="partyType" style="width:90 px" onchange="">
			<html:option value='0'>--Choose--</html:option>
			</html:select>
			</td> 	
			<td class="smallfieldcell"><html:select  property="docType" styleId="docType" style="width:90 px" onchange="loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARENTID=#1 order by code','docType','subType',this,'gridRecoverySetup');">
			<html:option value='0'>--Choose--</html:option>
			</html:select>
			</td> 	
			<td class="smallfieldcell"><html:select  property="subType"  onclick="return checkDocumentType(this);" onblur="return checkDocumentType(this);" styleId="subType" style="width:90 px">
			<html:option value='0'>--Choose--</html:option>
			</html:select>
			</td> 	
			
			<td class="smallfieldcell4"><html:text  property="recovDateFrom"  value=""  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td> 
			<td class="smallfieldcell4"><html:text  property="recovDateTo"  value=""  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" /></td> 
			
			
			<td class="smallfieldcell"><html:text  property="lowAmount"  style="text-align:right" value=""  maxlength="16" onblur=""/></td> 
			<td class="smallfieldcell"><html:text  property="highAmount"  style="text-align:right" value=""  maxlength="16" onblur=""/></td> 
			<td class="smallfieldcell3"><html:text   property="ITPercentage"  style="text-align:right" value=""  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text   property="surPercentage"  style="text-align:right" value=""  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text  property="eduCessPercentage"  style="text-align:right" value=""  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text  readonly="true"  tabindex="-1" property="totalPercentage"  style="text-align:right" value=""  maxlength="5"/></td> 
			<td class="smallfieldcell"><html:text  property="flatAmount"  style="text-align:right" value=""  maxlength="16"/></td> 
			</tr>
			
			
		<%
		}
		else if((rsf.getLowAmount()!=null && !session.getAttribute("mode").equals("create")))
		{
		  
		   for(int i=0;i<rsf.getDocType().length;i++)
		   {
		 	logger.info("INSIDE JSP --------------->VIEW/MODIFY MODE");
		 	//logger.info("PARTY TYPE ID------------>"+rsf.getPartyType()[i]);
			//logger.info("DOC TYPE ID-------------->"+rsf.getDocType()[i]);
			//logger.info("DOC SUBTYPE ID------------>"+rsf.getSubType()[i]); 
			//logger.info("LIST OF PTYPE------------>"+session.getAttribute("partyTypeList")); 
		 	  
		 	  %>
			<tr id="detailsRow" name="detailsRow" onClick="selected(this);">
			<!--
			<td class="tdStlyle"><div align="left" id=recovery_srNo name="recovery_srNo"></div></td>
			-->
			<td style="display:none" class="smallfieldcell"><html:text  property="id" value="<%= (rsf.getId()[i])%>"  /></td> 
			<td style="display:none" class="smallfieldcell"><html:text  property="appliedToHiddenId" value="<%= (rsf.getAppliedToHiddenId()[i])%>" /></td> 
			<td class="smallfieldcell"><html:select  property="partyType" styleId="partyType" style="width:90 px" onchange="">
			<html:option value='0'>--Choose--</html:option>
				
			</html:select>
			</td> 	
			<td class="smallfieldcell"><html:select  property="docType" styleId="docType" style="width:90 px" onchange="loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'egw_typeofwork', 'ID', 'CODE', 'PARENTID=#1 order by code','docType','subType',this,'gridRecoverySetup');">
				<html:option value='0'>--Choose--</html:option>
				
			</html:select>
			</td> 	
			<td class="smallfieldcell"><html:select  property="subType" onclick="return checkDocumentType(this);" onblur="return checkDocumentType(this);" styleId="subType" style="width:90 px">
			<html:option value='0'>--Choose--</html:option>
				
			</html:select>
			</td> 	

			<td class="smallfieldcell4"><html:text  property="recovDateFrom"  value="<%= (rsf.getRecovDateFrom()[i])%>"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td> 
			<td class="smallfieldcell4"><html:text  property="recovDateTo"  value="<%= (rsf.getRecovDateTo()[i])%>"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" /></td> 


			<td class="smallfieldcell"><html:text  property="lowAmount"  style="text-align:right" value="<%= (rsf.getLowAmount()[i])%>"  maxlength="16" onblur=""/></td> 
			<td class="smallfieldcell"><html:text  property="highAmount"  style="text-align:right" value="<%= (rsf.getHighAmount()[i])%>"  maxlength="16" onblur=""/></td> 
			<td class="smallfieldcell3"><html:text   property="ITPercentage"  style="text-align:right" value="<%= (rsf.getITPercentage()[i])%>"  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text   property="surPercentage"  style="text-align:right" value="<%= (rsf.getSurPercentage()[i])%>"  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text  property="eduCessPercentage"  style="text-align:right" value="<%= (rsf.getEduCessPercentage()[i])%>"  maxlength="5" onblur="calcTotalPer(this);"/></td> 
			<td class="smallfieldcell3"><html:text  readonly="true"  tabindex="-1" property="totalPercentage"  style="text-align:right" value=""  maxlength="5"/></td> 
			<td class="smallfieldcell"><html:text  property="flatAmount"  style="text-align:right" value="<%= (rsf.getFlatAmount()[i])%>"  maxlength="16"/></td> 
			</tr>
		    <%
		   }
		}
      %>
			</table>
			
			<!-- Add/Delete button start-->
			<table>
			<tr id="hideRow7" name="hideRow7">
			<td colspan=4>
			<input class="button" type="button" name="addDetail"   value="Add Row" onclick="javascript:addRow();" />
			<input class="button" type="button"   value="Delete"  name="deleteDetail" onclick="javascript:return deleteRow();" />
			</td>		              	 
			</tr>
			</table>
	<p>&nbsp;</p>
	</div>		
			<!-- End-->			
			
	</td>
	</tr>
	<tr>
	<td>
	<!-- Svae/cancel/close button start-->
			<table align=center>
			<tr></td>&nbsp;</td></tr>
			
			<tr class="row1" id="hideRow8">
			<td>
			<input type=hidden name="button" id="button"/>
			<html:button styleClass="button" value="Save & Close" property="b1" onclick="ButtonPress('saveclose')" />
			<html:button styleClass="button" value="Save & New" property="b2" onclick="ButtonPress('savenew')" /></td>
			<td><html:button styleClass="button" value="Cancel" property="b4" onclick="onClickCancel()" /></td>
			<td><html:button styleClass="button" value="Close" property="b3" onclick="window.close();" /></td>
			</tr>
			
			<tr style="DISPLAY: none" class="row1" id="hideRow11">
			<td>
			<html:button styleClass="button" value="Submit" property="b1" onclick="ButtonPress('saveclose')" />
			<html:button styleClass="button" value="Back" property="b2" onclick="ButtonPress('backModify')" />
			<td><html:button styleClass="button" value="Close" property="b3" onclick="window.close();" /></td>
			</tr>
			
			</table>
			<!-- End-->

		<tr id="hideRow9" style="DISPLAY: none">
		<td align="center" colspan=4>
		<html:button styleClass="button" value="  View   " property="b1" onclick="ButtonPress('view')" />
		<html:button styleClass="button" value="  Close  " property="b3" onclick="window.close();" />
		</td>
		</tr>
		<tr id="hideRow12" style="DISPLAY: none">
		<td align="center" colspan=4>
		<html:button styleClass="button" value="  Modify  " property="b4" onclick="ButtonPress('modify')" />
		<html:button styleClass="button" value="  Close  " property="b3" onclick="window.close();" />
		</td>
		</tr>
		
		<tr style="DISPLAY: none" id="hideRow10" name="hideRow10">
		<td  align="center" colspan="4">
		<html:button styleClass="button" value="Back" property="b3" onclick="ButtonPress('backView')" />
		<html:button styleClass="button" value="Close" property="b4" onclick="window.close();" />
		</td>
		</tr>	
	</td>
	</tr>
	</table><!------------------ table main2 end--------------------->
<html:javascript formName="RecoverySetupForm"/>	
</html:form>

</body>
</html>
