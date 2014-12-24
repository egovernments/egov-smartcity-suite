<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<script>
var statusEmp;
var NomineeIdLastValue=0;
<% //FIXME : arg parameter not required. Review this. %>
function validateOnSubmit()
{
	if(employeePensionForm.statusMaster!=undefined && employeePensionForm.statusMaster.value =="Deceased")
	{
		   document.getElementById("employeeDisbursementType").readOnly="true";
	}
	else
	{
		   if(employeePensionForm.employeeDisbursementType!=undefined && employeePensionForm.employeeDisbursementType.value =="0")
		   {
			   alert('<bean:message key="alertDisType"/>');
			   employeePensionForm.employeeDisbursementType.focus();
			   return false;
		   }
	}
	
	if(employeePensionForm.statusMaster!=undefined && (employeePensionForm.statusMaster.value =="Deceased"))
	{ 
	        
		   			var nomTable= document.getElementById("NomineeTable");
		  			 var rowLen= nomTable.rows.length-1;
					   if(rowLen==2)
					   {
					      if(document.employeePensionForm.firstName.value =="0")
			  				 {
			  				    alert('Please Enter the Nominee details');
			  				    return false;
			  				 }
					   }
		   
	   			
			  
	}
	if(document.getElementById("bankTable").style.display!="none")
	{
		   if(employeePensionForm.employeeBank.value =='0')
		   {
			   alert('<bean:message key="alertChooseBnkNme"/>');
			   employeePensionForm.employeeBank.focus();
			   return false;
		   }
		   if(employeePensionForm.employeeBranch.value =='0')
		   {
			   alert('<bean:message key="alertChooseBrnchNme"/>');
			   employeePensionForm.employeeBranch.focus();
			   return false;
		   }
		   if(employeePensionForm.employeeAccountNumber.value =='0')
		   {
			   alert('<bean:message key="alertChooseAccnum"/>');
			   employeePensionForm.employeeAccountNumber.focus();
			   return false;
		   }
		   
		}
	if(document.getElementById("NomineeTable").style.display!="none")
	{
	       var nomTable= document.getElementById("NomineeTable");
		   var rowLen= nomTable.rows.length-1;
		   var pctValue = 0;
		   
		   if(rowLen==2)
		   {
		       if(document.employeePensionForm.firstName.value =="0")
			   {
				   alert('Please select the FirstName');
				   document.employeePensionForm.firstName.focus();
				   return false;
			   }
			   if(document.employeePensionForm.status.value =="")
			   {
				   alert('Please Update the status');
				   document.employeePensionForm.status.focus();
				   return false;
			   }
			   
			   if(document.employeePensionForm.percentage.value =="")
			   {
				   alert('Please fill the percentage');
				   document.employeePensionForm.percentage.focus();
				   return false;
			   }
			   
						   if(document.employeePensionForm.percentage!=null)
						   {
							    
							   	 	pctValue += eval(document.employeePensionForm.percentage.value);
							   
						   	}
			   
			   
			       if(pctValue != 100)
			    		{
			    			alert("Single row Percentage value has to be 100");
			    			return false;
			    		}
		    		
					 
					 
					 		   
		   
				
			}
			else
		   {
			   for(var i=2;i<=rowLen;i++)
			   {
			       var j = i-2;
				   if(document.employeePensionForm.firstName[j].value =="0")
				   {
					   alert('<bean:message key="alertEnterFirstName"/>');
					   document.employeePensionForm.firstName[j].focus();
					   return false;
				   }
			   if(document.employeePensionForm.status[j].value =="")
			   {
				   alert('Please Update the status');
				   return false;
			   }
			   
			   if(document.employeePensionForm.percentage[j].value =="")
			   {
				   alert('Please fill the percentage');
				   document.employeePensionForm.percentage[j].focus();
				   return false;
			   }
				   
				   
				  pctValue += eval(document.employeePensionForm.percentage[i-2].value);
		    		
                   
				}
				if(pctValue > 100)
		    		{
		    			alert("Total percentage can't be greater than 100 !!!");
		    			return false;
		    		}
		    		if(pctValue < 100)
		    		{
		    		   alert("Total percentage has to be 100 !!!");
		    			return false;
		    		}
				
			}
		}
		<%
		if(request.getAttribute("mode") != null && ((String)(request.getAttribute("mode"))).trim().equals("modify"))
		{
       %>
	document.employeePensionForm.action = "${pageContext.request.contextPath}/empPension/AfterPensionHeaderAction.do?submitType=modifyDetails";
	if(document.getElementById("NomineeTable").style.display!="none")
	{
	      
		   var nomTable= document.getElementById("NomineeTable");
		   var rowLen= nomTable.rows.length-1;
		   if(rowLen==2)
		   {
		     
			   document.employeePensionForm.relationId.disabled=false;
			   document.employeePensionForm.isAlive.disabled=false;
			   document.employeePensionForm.maritialStatus.disabled=false;
			   document.employeePensionForm.employed.disabled=false;
			   document.employeePensionForm.nomDisType.disabled=false;
			   document.employeePensionForm.bank.disabled=false;
			   document.employeePensionForm.branchNominee.disabled=false;
			   document.employeePensionForm.dateOfBirth.disabled=false;
			  
			   
		   }
		   else
		   {
		      for(var i=2;i<=rowLen;i++)
			   {
				   var j = i-2;
			      	document.employeePensionForm.relationId[j].disabled=false;
				   document.employeePensionForm.isAlive[j].disabled=false;
				   document.employeePensionForm.maritialStatus[j].disabled=false;
				   document.employeePensionForm.employed[j].disabled=false;
				   document.employeePensionForm.nomDisType[j].disabled=false;
				   document.employeePensionForm.bank[j].disabled=false;
				   document.employeePensionForm.branchNominee[j].disabled=false;
				   document.employeePensionForm.dateOfBirth[j].disabled=false;
			   }
		   }
	}
	document.employeePensionForm.submit();
   <%
	}
	else
	{
	%>
	document.employeePensionForm.action = "${pageContext.request.contextPath}/empPension/AfterPensionHeaderAction.do?submitType=saveDetails";
	if(document.getElementById("NomineeTable").style.display!="none")
	{
	      
		   var nomTable= document.getElementById("NomineeTable");
		   var rowLen= nomTable.rows.length-1;
		   if(rowLen==2)
		   {
		     
			   document.employeePensionForm.relationId.disabled=false;
			   document.employeePensionForm.isAlive.disabled=false;
			   document.employeePensionForm.maritialStatus.disabled=false;
			   document.employeePensionForm.employed.disabled=false;
			   document.employeePensionForm.nomDisType.disabled=false;
			   document.employeePensionForm.bank.disabled=false;
			   document.employeePensionForm.branchNominee.disabled=false;
			   document.employeePensionForm.dateOfBirth.disabled=false;
			  
			   
		   }
		   else
		   {
		      for(var i=2;i<=rowLen;i++)
			   {
				   var j = i-2;
			      	document.employeePensionForm.relationId[j].disabled=false;
				   document.employeePensionForm.isAlive[j].disabled=false;
				   document.employeePensionForm.maritialStatus[j].disabled=false;
				   document.employeePensionForm.employed[j].disabled=false;
				   document.employeePensionForm.nomDisType[j].disabled=false;
				   document.employeePensionForm.bank[j].disabled=false;
				   document.employeePensionForm.branchNominee[j].disabled=false;
				   document.employeePensionForm.dateOfBirth[j].disabled=false;
			   }
		   }
	}
	
	
	document.employeePensionForm.submit();
	<%
	}
	%>
}

function validateOnSubmitForModify()
{
   if(employeePensionForm.statusMaster!=undefined && employeePensionForm.statusMaster.value =="Deceased")
	{
		   document.getElementById("employeeDisbursementType").readOnly="true";
	}
	else
	{
		   if(employeePensionForm.employeeDisbursementType!=undefined && employeePensionForm.employeeDisbursementType.value =="0")
		   {
			   alert('<bean:message key="alertDisType"/>');
			   employeePensionForm.employeeDisbursementType.focus();
			   return false;
		   }
	}
	
	if(employeePensionForm.statusMaster!=undefined && (employeePensionForm.statusMaster.value =="Deceased"))
	{ 
	        
		   			var nomTable= document.getElementById("NomineeTable");
		  			 var rowLen= nomTable.rows.length-1;
					   if(rowLen==2)
					   {
					      if(document.employeePensionForm.firstName.value =="0")
			  				 {
			  				    alert('Please Enter the Nominee details');
			  				    return false;
			  				 }
					   }
		   
	   			
			  
	}
	if(document.getElementById("bankTable").style.display!="none")
	{
		   if(employeePensionForm.employeeBank.value =='0')
		   {
			   alert('<bean:message key="alertChooseBnkNme"/>');
			   employeePensionForm.employeeBank.focus();
			   return false;
		   }
		   if(employeePensionForm.employeeBranch.value =='0')
		   {
			   alert('<bean:message key="alertChooseBrnchNme"/>');
			   employeePensionForm.employeeBranch.focus();
			   return false;
		   }
		   if(employeePensionForm.employeeAccountNumber.value =='0')
		   {
			   alert('<bean:message key="alertChooseAccnum"/>');
			   employeePensionForm.employeeAccountNumber.focus();
			   return false;
		   }
		   
		}
	if(document.getElementById("NomineeTable").style.display!="none")
	{
	       var nomTable= document.getElementById("NomineeTable");
		   var rowLen= nomTable.rows.length-1;
		   var pctValue = 0;
		   
		   if(rowLen==2)
		   {
		       
			   if(document.employeePensionForm.firstName.value!='0')
			   {
				   if(document.employeePensionForm.status.value =="")
				   {
					   alert('Please Update the status');
					   document.employeePensionForm.status.focus();
					   return false;
				   }
			   
				   if(document.employeePensionForm.percentage.value =="")
				   {
					   alert('Please fill the percentage');
					   document.employeePensionForm.percentage.focus();
					   return false;
				   }
			   
						   if(document.employeePensionForm.percentage!=null)
						   {
							    
							   	 	pctValue += eval(document.employeePensionForm.percentage.value);
							   
						   	}
			   
			   
			       if(pctValue != 100)
			    		{
			    			alert("Single row Percentage value has to be 100");
			    			return false;
			    		}
			    }
		    		
			
				
			}
			else
		   {
			   for(var i=2;i<=rowLen;i++)
			   {
					       var j = i-2;
						   if(document.employeePensionForm.firstName[j].value=="0")
						   {
							   alert('<bean:message key="alertEnterFirstName"/>');
							   document.employeePensionForm.firstName[j].focus();
							   return false;
						   }
					   if(document.employeePensionForm.status[j].value =="")
					   {
						   alert('Please Update the status');
						   return false;
					   }
					   
					   if(document.employeePensionForm.percentage[j].value =="")
					   {
						   alert('Please fill the percentage');
						   document.employeePensionForm.percentage[j].focus();
						   return false;
					   }
						   
						   
						  pctValue += eval(document.employeePensionForm.percentage[i-2].value);
				    		
		                   
				}
				
				//outside for loop
				if(pctValue > 100)
		    		{
		    			alert("Total percentage can't be greater than 100 !!!");
		    			return false;
		    		}
		    		if(pctValue < 100)
		    		{
		    		   alert("Total percentage has to be 100 !!!");
		    			return false;
		    		}
				
			}
		}
		<%
		if(request.getAttribute("mode") != null && ((String)(request.getAttribute("mode"))).trim().equals("modify"))
		{
       %>
	document.employeePensionForm.action = "${pageContext.request.contextPath}/empPension/AfterPensionHeaderAction.do?submitType=modifyDetails";
	if(document.getElementById("NomineeTable").style.display!="none")
	{
	      
		   var nomTable= document.getElementById("NomineeTable");
		   var rowLen= nomTable.rows.length-1;
		   if(rowLen==2)
		   {
		     
			   document.employeePensionForm.relationId.disabled=false;
			   document.employeePensionForm.isAlive.disabled=false;
			   document.employeePensionForm.maritialStatus.disabled=false;
			   document.employeePensionForm.employed.disabled=false;
			   document.employeePensionForm.nomDisType.disabled=false;
			   document.employeePensionForm.bank.disabled=false;
			   document.employeePensionForm.branchNominee.disabled=false;
			   document.employeePensionForm.dateOfBirth.disabled=false;
			   document.employeePensionForm.bankAccount.disabled=false;
			  
			   
		   }
		   else
		   {
		      for(var i=2;i<=rowLen;i++)
			   {
				   var j = i-2;
			      	document.employeePensionForm.relationId[j].disabled=false;
				   document.employeePensionForm.isAlive[j].disabled=false;
				   document.employeePensionForm.maritialStatus[j].disabled=false;
				   document.employeePensionForm.employed[j].disabled=false;
				   document.employeePensionForm.nomDisType[j].disabled=false;
				   document.employeePensionForm.bank[j].disabled=false;
				   document.employeePensionForm.branchNominee[j].disabled=false;
				   document.employeePensionForm.dateOfBirth[j].disabled=false;
				   document.employeePensionForm.bankAccount[j].disabled=false;
			   }
		   }
	}
	
	if(NomineeIdLastValue!=0)
	{
	  populateDeleteSet("delNominees" , NomineeIdLastValue);
	}
	document.employeePensionForm.submit();
   <%
	}
	
	%>
}

<% //FIXME : give proper name, instead of obj %>
function checkBranch(obj,table)
{
    if(table=="bankTable")
    {
     loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKBRANCH', 'ID', 'BRANCHNAME', 'BANKID=#1 order by ID', 'employeeBank' , 'employeeBranch');
    }
    if(table=="NomineeTable")
    {
        var tbl= document.getElementById("NomineeTable");
        var len = tbl.rows.length;
        var row=getRow(obj);
        if(row.rowIndex==1)
        {
            loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKBRANCH', 'ID', 'BRANCHNAME', 'BANKID=#1 order by ID', 'bank' , 'branchNominee');
        }
        else
        {
            loadSelectDataForCurrentRow('../commonyui/egov/loadComboAjax.jsp', 'BANKBRANCH', 'ID', 'BRANCHNAME', 'BANKID=#1 order by ID', 'bank', 'branchNominee',obj,'NomineeTable');
        }

   }
}

function showNominee()
{
   document.getElementById('NomineeTable').style.display="block";
   document.employeePensionForm.updateStatus.style.display="block";
   

}

function hideNominee()
{
   document.getElementById('NomineeTable').style.display="none";
   document.employeePensionForm.updateStatus.style.display="none";
    ///document.employeePensionForm.addRow.style.display="none";
    deleteAllRows("NomineeTable");
}

function hideTable()
{
    if(employeePensionForm.employeeDisbursementType.value!="cash" ||employeePensionForm.employeeDisbursementType.value!="0"||employeePensionForm.employeeDisbursementType.value!="cheque")
    {
        document.getElementById("bankTable").style.display = "block";
    }
    if(employeePensionForm.employeeDisbursementType.value=="cheque"||employeePensionForm.employeeDisbursementType.value=="cash"||employeePensionForm.employeeDisbursementType.value =="0")
    {
        document.getElementById("bankTable").style.display = "none";
    }
}

function showBank(obj)
{
    var tbl = document.getElementById("NomineeTable");
    var len= tbl.rows.length;
    var row=getRow(obj);
    var check=row.rowIndex;
    if( check==2)
    {
        if(obj.value =="dbt")
        {
            getControlInBranch(row,"bank").disabled= false;
            getControlInBranch(row,"branchNominee").disabled= false;
            getControlInBranch(row,"bankAccount").disabled= false;
        }
        else
        {
            getControlInBranch(row,"bank").disabled= true;
            getControlInBranch(row,"branchNominee").disabled= true;
            getControlInBranch(row,"bankAccount").disabled= true;
        }
    }
    else
    {
        for(var i=0;i<check;i++)
        {
            if(obj.value =="dbt")
            {
                getControlInBranch(row,"bank").disabled= false;
                getControlInBranch(row,"branchNominee").disabled= false;
                getControlInBranch(row,"bankAccount").disabled= false;
            }
            else
            {
                getControlInBranch(row,"bank").disabled= true;
                getControlInBranch(row,"branchNominee").disabled= true;
                getControlInBranch(row,"bankAccount").disabled= true;
            }
        }

    }
}

function addTitleAttributes(obj)
{
    var objName = obj.name;
    numOptions = document.getElementById(objName).options.length;
    for (i = 0; i < numOptions; i++)
        document.getElementById(objName).options[i].title = document.getElementById(objName).options[i].text;
}

function bankOnLoad()
{
    document.employeePensionForm.bank.disabled= true;
    document.employeePensionForm.branchNominee.disabled= true;
    document.employeePensionForm.bankAccount.disabled= true;
}

//Added a Single button to add a row at the end of table.
//Modified the code accordingly
function addRowToTable(tbl)
{
    tableObj=document.getElementById(tbl);
    var tbody=tableObj.tBodies[0];
    var lastRow = tableObj.rows.length;
    
   if(checkPreviousValues(lastRow))
   {
		var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
		tbody.appendChild(rowObj);
		var remlen = document.employeePensionForm.firstName.length;
		resetRowValues(remlen-1);
	}

}

function checkPreviousValues(rowCount)
{
   if(rowCount==3)
   {
  		
  		if(document.employeePensionForm.firstName.value=='0')
  		{
  		  alert('Please choose the Nominee Name');
  		  document.employeePensionForm.firstName.focus();
  		  return false;
  		}
  		if(document.employeePensionForm.status.value=='')
  		{
  		    alert('Please Click On Update Status');
  		   return false;
  		}
  		if(document.employeePensionForm.percentage.value=='')
  		{
  		    alert('Please enter the Percentage');
  		    document.employeePensionForm.percentage.focus();
  		   return false;
  		}
  		
  	}
  	else
  	{
  	   if(document.employeePensionForm.firstName[rowCount-3].value=='0')
  		{
  		  alert('Please choose the Nominee Name');
  		  document.employeePensionForm.firstName[rowCount-3].focus();
  		  return false;
  		}
  		if(document.employeePensionForm.status[rowCount-3].value=='')
  		{
  		    alert('Please Click On Update Status');
  		   return false;
  		}
  		if(document.employeePensionForm.percentage[rowCount-3].value=='')
  		{
  		    alert('Please enter the Percentage');
  		    document.employeePensionForm.percentage[rowCount-3].focus();
  		   return false;
  		}
  	  
  	}
  	return true;
  	
}


function resetRow(rowIndex)
{
   if(rowIndex==2)
   {
     if(document.employeePensionForm.nomineeId != null)
     {
				document.employeePensionForm.nomineeId.value="";
				}
			document.employeePensionForm.firstName.value="0";
			document.employeePensionForm.nomineeMstrId.value="";
			document.employeePensionForm.relationId.value="0";
			document.employeePensionForm.dateOfBirth.value="";
			document.employeePensionForm.isAlive.value="-1";
			document.employeePensionForm.employed.value="-1";
			document.employeePensionForm.maritialStatus.value="-1";
			document.employeePensionForm.status.value="";
			document.employeePensionForm.nomDisType.value="cash";
			document.employeePensionForm.bank.disabled="true";
			
			document.employeePensionForm.bankAccount.value="";
			document.employeePensionForm.percentage.value=0;
			
   }
}
function resetRowValues(rowIndex)
{
      
       
      if(rowIndex>0 && rowIndex  <document.employeePensionForm.firstName.length )
		{
			
			if(document.employeePensionForm.nomineeId != null)
			{
				document.employeePensionForm.nomineeId[rowIndex].value="";
				}
				
			document.employeePensionForm.firstName[rowIndex].value="0";
			document.employeePensionForm.nomineeMstrId[rowIndex].value="";
			document.employeePensionForm.relationId[rowIndex].value="0";
			document.employeePensionForm.dateOfBirth[rowIndex].value="";
			document.employeePensionForm.isAlive[rowIndex].value="-1";
			document.employeePensionForm.employed[rowIndex].value="-1";
			document.employeePensionForm.maritialStatus[rowIndex].value="-1";
			document.employeePensionForm.status[rowIndex].value="";
			document.employeePensionForm.nomDisType[rowIndex].value="cash";
			
			document.employeePensionForm.bankAccount[rowIndex].value="";
			document.employeePensionForm.percentage[rowIndex].value=0;
			
	   }
	else
	{

		   	if(document.employeePensionForm.nomineeId != null)
				document.employeePensionForm.nomineeId.value="";
			document.employeePensionForm.firstName.value="0";
			document.employeePensionForm.nomineeMstrId.value="";
			document.employeePensionForm.relationId.value="0";
			document.employeePensionForm.dateOfBirth.value="";
			document.employeePensionForm.isAlive.value="-1";
			document.employeePensionForm.employed.value="-1";
			document.employeePensionForm.maritialStatus.value="-1";
			document.employeePensionForm.status.value="";
			document.employeePensionForm.nomDisType.value="cash";
			document.employeePensionForm.bank.disabled="true";
			
			document.employeePensionForm.bankAccount.value="";
			document.employeePensionForm.percentage.value=0;
			


	}
}
function deleteRow(table,obj)
{
   var tbl = document.getElementById(table);
   var rowNumber=getRow(obj).rowIndex;
    var nomineeHiddenIdObj = getControlInBranch(tbl.rows[rowNumber],"nomineeHiddenId");
     var relationObj = getControlInBranch(tbl.rows[rowNumber],"relationId");
     //
     var firstNameObj = getControlInBranch(tbl.rows[rowNumber],"firstName");
     var isAliveObj = getControlInBranch(tbl.rows[rowNumber],"isAlive");
     var maritialStatusObj = getControlInBranch(tbl.rows[rowNumber],"maritialStatus");
     var dateOfBirthObj = getControlInBranch(tbl.rows[rowNumber],"dateOfBirth");
     var employedObj = getControlInBranch(tbl.rows[rowNumber],"employed");
      var statusObj = getControlInBranch(tbl.rows[rowNumber],"status");
     var nomDisTypeObj = getControlInBranch(tbl.rows[rowNumber],"nomDisType");
     var bankObj = getControlInBranch(tbl.rows[rowNumber],"bank");
     var branchNomineeObj = getControlInBranch(tbl.rows[rowNumber],"branchNominee");
      var bankAccountObj = getControlInBranch(tbl.rows[rowNumber],"bankAccount");
       var percentageObj = getControlInBranch(tbl.rows[rowNumber],"percentage");
     
   var nomineeId =	getControlInBranch(tbl.rows[rowNumber],'nomineeId').value
   <%
			if(request.getAttribute("mode") != null && ((String)(request.getAttribute("mode"))).trim().equals("modify"))
			{
   %>
   			if(getControlInBranch(tbl.rows[rowNumber],'nomineeId') != null)
   			{


					if(nomineeId != null && nomineeId != "")
					{
					    if(tbl.rows.length > 3)
		   				{
							populateDeleteSet("delNominees" , nomineeId);
						}
					}
				}
		<%
			}
		%>
		  
		   if(tbl.rows.length > 3)
		   {
				tbl.deleteRow(rowNumber);
			}
		   else
		   {
			   <%
				if(request.getAttribute("mode") != null && ((String)(request.getAttribute("mode"))).trim().equals("modify"))
				{
   			%>
   			            if(tbl.rows.length==3)
		   				{
		   				    if(nomineeHiddenIdObj!=null && nomineeHiddenIdObj!=undefined)
		   				    {
		   				           
		   				           relationObj.value='0';
		   				           firstNameObj.value='0';
		   				           isAliveObj.value='-1';
		   				           maritialStatusObj.value='-1';
		   				           dateOfBirthObj.value='';
		   				           employedObj.value='-1';
		   				           nomDisTypeObj.value='cash';
		   				           statusObj.value='';
		   				           bankObj.value='0';
		   				           branchNomineeObj.value='0';
		   				           bankAccountObj.value='';
		   				           percentageObj.value='';
		   				 		   deleteLastRowInModify(nomineeHiddenIdObj.value);
		   				    }
		   				    
		   				   
						}
			<%}else{%>
			       alert('Row cannot be deleted For Create');
			<%}%>
			   return false;
		   }

}
function disp_confirm()
{
var r=confirm("This will delete the existing nominees.Wish to continue");
if (r==true)
  {
  hideNominee();

  }
else
  {
  document.getElementById("nomineeRadio").checked="false";

  }
}


function deleteAllRows(table)
{
   
	 var tbl = document.getElementById(table);
	 var len = tbl.rows.length;
    if(document.employeePensionForm.nomineeId!= null)
   var lent=document.employeePensionForm.nomineeId.length;

   for(var j=0;j<lent;j++)
   {

   var nomineeId=document.employeePensionForm.nomineeId[j].value;
   populateDeleteSet("delNominees" , nomineeId);
    resetRowValues(len);
}
if(len==3)
{

	if(document.employeePensionForm.nomineeId!= null)

	var nomineeId=document.employeePensionForm.nomineeId.value;
	 resetRowValues(len);
   populateDeleteSet("delNominees" , nomineeId);
}
	 if(len > 0)
	 {

		if(len == 2)

		  resetRowValues(len);

	 }
}


function checkFirstNameAndRelation()
{
		var nomTable= document.getElementById("NomineeTable");
		var rowLen= nomTable.rows.length-1;
		if(rowLen == 2)
   		 {
			if(document.employeePensionForm.firstName.value =="0")
			{
			   alert('<bean:message key="alertEnterFirstName"/>');
			   document.employeePensionForm.firstName.focus();
			   return false;
			}
			
		}
		else
		{
			for(var i=2;i<=rowLen;i++)
			{
			   var j = i-2;
			   if(document.employeePensionForm.firstName[j].value =="0")
			   {
				   alert('<bean:message key="alertEnterFirstName"/>');
				   document.employeePensionForm.firstName[j].focus();
				   return false;
			   }
			   
			}
		}
		return true;
}

/*
* This method checks for the nominee eligibilty status.
*/
function checkEligible()
{
    
	 var checkNameRel = checkFirstNameAndRelation();
		if(checkNameRel == true)
		{
		   
			var nomTable= document.getElementById("NomineeTable");
			var rowLen= nomTable.rows.length-1;
			var relationId = new Array();
			var maritialStatus = new Array();
			var isAlive = new Array();
			var employed = new Array();
			var dateOfBirth = new Array();
			var percentage = new Array();

			if(rowLen == 2)
			{
			   
				relationId[0]=document.employeePensionForm.relationId.value;
				maritialStatus[0]=document.employeePensionForm.maritialStatus.value;
				isAlive[0]=document.employeePensionForm.isAlive.value;
				employed[0]=document.employeePensionForm.employed.value;
				dateOfBirth[0]=document.employeePensionForm.dateOfBirth.value;
			}
			else
			{
				for(var i=2;i<=rowLen;i++)
				{
				 
					var j = i-2;
					relationId[j]=document.employeePensionForm.relationId[j].value;
					maritialStatus[j]=document.employeePensionForm.maritialStatus[j].value;
					isAlive[j]=document.employeePensionForm.isAlive[j].value;
					employed[j]=document.employeePensionForm.employed[j].value;
					dateOfBirth[j]=document.employeePensionForm.dateOfBirth[j].value;
					
				}
			}

		   var url;
		   url = "${pageContext.request.contextPath}/pension/checkNomineeEligibility.do?relationId="+relationId+"&maritialStatus="+maritialStatus+"&isAlive="+isAlive+"&employed="+employed+"&dateOfBirth="+dateOfBirth;
		   if(url != undefined)
		   {
				http = initiateRequest();
				http.open("GET", url, true);
				http.onreadystatechange = function()
				{
					 if (http.readyState == 4)
					{
						if (http.status == 200)
						{
							var statusString =http.responseText.split("^");
							if(statusString.length == 2)
							{
								if(statusString[0] == "1")
								{
									document.employeePensionForm.status.value="Eligible";
								}
								else if(statusString[0] == "0")
								{
								    document.employeePensionForm.status.value="Not Eligible";
								    document.employeePensionForm.percentage.value="0";
									
								}
								else if(statusString[0] == "2")
								{
								    document.employeePensionForm.status.value="Unknown";
								}
							}
							else
							{
							  
								for(var i=0; i<statusString.length; i++)
								{
								    var popup = statusString[i];
									//alert("popup="+popup);
									if(statusString[i] == "1")
									{
										document.employeePensionForm.status[i].value="Eligible";
										
									}
									else if(statusString[i] == "0")
									{
										document.employeePensionForm.status[i].value="Not Eligible";
										document.employeePensionForm.percentage[i].value="0";
										
									}
									else if(statusString[i] == "2")
									{
										document.employeePensionForm.status[i].value="Unknown";
									}
								}
							}
						}
					}
				};
				http.send(null);
     	}
		}
}

function onLoadModifyEligible()
{
    
	        var nomTable= document.getElementById("NomineeTable");
			var rowLen= nomTable.rows.length-1;
			var relationId = new Array();
			var maritialStatus = new Array();
			var isAlive = new Array();
			var employed = new Array();
			var dateOfBirth = new Array();
			var percentage = new Array();

			if(rowLen == 2)
			{
			   
				relationId[0]=document.employeePensionForm.relationId.value;
				maritialStatus[0]=document.employeePensionForm.maritialStatus.value;
				isAlive[0]=document.employeePensionForm.isAlive.value;
				employed[0]=document.employeePensionForm.employed.value;
				dateOfBirth[0]=document.employeePensionForm.dateOfBirth.value;
			}
			else
			{
				for(var i=2;i<=rowLen;i++)
				{
				 
					var j = i-2;
					relationId[j]=document.employeePensionForm.relationId[j].value;
					maritialStatus[j]=document.employeePensionForm.maritialStatus[j].value;
					isAlive[j]=document.employeePensionForm.isAlive[j].value;
					employed[j]=document.employeePensionForm.employed[j].value;
					dateOfBirth[j]=document.employeePensionForm.dateOfBirth[j].value;
					
				}
			}

		   var url;
		   url = "${pageContext.request.contextPath}/pension/checkNomineeEligibility.do?relationId="+relationId+"&maritialStatus="+maritialStatus+"&isAlive="+isAlive+"&employed="+employed+"&dateOfBirth="+dateOfBirth;
		   if(url != undefined)
		   {
				http = initiateRequest();
				http.open("GET", url, true);

				http.onreadystatechange = function()
				{
					 if (http.readyState == 4)
					{
						if (http.status == 200)
						{
							var statusString =http.responseText.split("^");
							//alert("statusString.length="+statusString.length);
							if(statusString.length == 2)
							{
								
								if(statusString[0] == "1")
								{
									document.employeePensionForm.status.value="Eligible";
								}
								else if(statusString[0] == "0")
								{
								    document.employeePensionForm.status.value="Not Eligible";
								   
								}
								else if(statusString[0] == "2")
								{
								    document.employeePensionForm.status.value="Unknown";
								}
							}
							else
							{
							  
								for(var i=0; i<statusString.length; i++)
								{
								    var popup = statusString[i];
									//alert("popup="+popup);
									if(statusString[i] == "1")
									{
										document.employeePensionForm.status[i].value="Eligible";
										
									}
									else if(statusString[i] == "0")
									{
										document.employeePensionForm.status[i].value="Not Eligible";
										
										
									}
									else if(statusString[i] == "2")
									{
										document.employeePensionForm.status[i].value="Unknown";
									}
								}
							}
						}
					}
				};
				http.send(null);
     	}
		
}

/*function ifNotAlive(obj)
{
    var row=getRow(obj);
    var table= document.getElementById("NomineeTable");
    if(obj.value ==1 || obj.value=="-1")
    {
        getControlInBranch(table.rows[row.rowIndex],'status').value = "";
        getControlInBranch(row,"employed").disabled= false;
        getControlInBranch(row,"maritialStatus").disabled= false;
        getControlInBranch(row,"dateOfBirth").disabled=false;
        getControlInBranch(row,"nomDisType").disabled= false;
        getControlInBranch(row,"bank").disabled= false;
        getControlInBranch(row,"bank").disabled= false;
        getControlInBranch(row,"branchNominee").disabled=  false;
        getControlInBranch(row,"bankAccount").disabled=  false;
    }
    else
    {
        getControlInBranch(table.rows[row.rowIndex],'status').value = "Not Eligible";
        getControlInBranch(row,"employed").disabled= true;
        getControlInBranch(row,"maritialStatus").disabled= true;
        getControlInBranch(row,"dateOfBirth").disabled= true;
        getControlInBranch(row,"nomDisType").disabled= true;
        getControlInBranch(row,"bank").disabled= true;
        getControlInBranch(row,"bank").disabled= true;
        getControlInBranch(row,"branchNominee").disabled= true;
        getControlInBranch(row,"bankAccount").disabled= true;
   }
}*/

function display()
{
   
    if(employeePensionForm.employeeDisbursementType.value =="dbt")
    {
        document.getElementById("bankTable").style.display = "block";
    }
    
     if(document.getElementById("NomineeTable").style.display =="")
    {
      ///document.getElementById("nomineeRadio").checked=true;
    }
    
    
}

function readyOnlyOnLoad()
{
<%
    if(((String)(request.getAttribute("mode"))).trim().equals("view"))
    {
    %>
        for(var i=0;i<document.forms[0].length;i++)
        {
           
            if(document.forms[0].elements[i].value!='Close')
            {
          		document.forms[0].elements[i].disabled =true;
          	}
          
        }
    <%
    }
%>
}
<% //FIXME : Move generic scripts to common js %>
function DataTrimStr(obj)
{
    if(obj.value != " "|| obj.value != null)
    {
      var str = obj.value;
      while(str.charAt(0) == (" ") )
      {
        str = str.substring(1);
      }
      while(str.charAt(str.length-1) == " " )
      {
        str = str.substring(0,str.length-1);
      }

      obj.value=str;
    }
}


function validateName( strValue)
{
   var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?0123456789";
    for (var i = 0; i < strValue.value.length; i++)
    {
        if (iChars.indexOf(strValue.value.charAt(i)) != -1)
        {
        alert('<bean:message key="alertValNme"/>');
        strValue.value="";
        strValue.focus();
        return false;
        }
    }
}



function checkAlphaNumeric(obj)
{
    if(obj.value!="")
    {
    var num=obj.value;
    var objRegExp  = /^[a-zA-Z  .]+$/;
        if(!objRegExp.test(num))
        {
        alert('<bean:message key="alertEnterValNme"/>');
        obj.value="";
        obj.focus();
        }
    }
}

function checkMaxLengthName(obj)
{
    if(obj.value !="")
    {
        var strNmae = obj.value;
        if(strNmae.length > 256)
        {
            alert( '<bean:message key="alertCharExPermissibleLn"/>' );
            obj.focus();
            obj.value="";
            return false;
        }
        return true;
    }
}

function checkBank(obj,table)
{
    if(table=="bankTable")
    {
    var tbl= document.getElementById("bankTable");
    var row= getRow(obj);
    var employeeBank= getControlInBranch(row,"employeeBank")
        if(obj.options.selectedIndex != "0")
        {
            if(employeeBank.options.selectedIndex =="0")
            {
                alert('<bean:message key="alertChooseBnkNme"/>');
                obj.options.selectedIndex.value =="0";
                document.employeePensionForm.employeeBank.focus();
                return false;
            }
        }
   }
   else if(table=="NomineeTable")
   {
        var tbl= document.getElementById("NoimneeTable");
        var row= getRow(obj);
        var bnk= getControlInBranch(row,"bank")
        if(obj.options.selectedIndex != "0")
        {
            if(bnk.options.selectedIndex=="0")
            {
                alert('<bean:message key="alertChooseBnkNme"/>');
                obj.options.selectedIndex.value =="0";
                bnk.focus();
                return false;
            }
       }
   }
}

/*function checkDob(obj)
{
    var row=getRow(obj);
    var table= document.getElementById("NomineeTable");
    var brth= getControlInBranch(table.rows[row.rowIndex],'dateOfBirth').value;

    var brthYear=brth.substr(6,4);
    alert(brthYear);
    var today = new Date();
    alert(today);
    var curyear = today.value;
    alert(curyear);
    if(compareDate(brth,curyear == 1))
    {
        return false;
    }
}*/

function validateDateSSS(obj)
{
    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getYear();
    var strValue=obj.value;

    if(strValue!="")
    {
        if (strValue.substr(6,4) < year )
        {
            return true;
        }
        else if(strValue.substr(6,4) > year)
        {
            alert('pls enter the valid date');
            obj.value="";
            obj.focus();
            return false;
        }
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {
                return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
                alert('pls enter the valid date');
                obj.value="";
                obj.focus();
                return false;
            }
            if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
                alert('pls enter the valid date');
                obj.value="";
                obj.focus();
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }
     }
    else
    {
        return true;
    }
}

function disableDisType()
{
    var statusEmp=document.getElementById("statusMaster").value;

    var disType=document.getElementById("employeeDisbursementType").value;

    var mode="<%=(String)request.getAttribute("mode")%>";
    var length=document.getElementById("NomineeTable").style.display;

    if(statusEmp == "Deceased"  )
    {
		document.getElementById("employeeDisbursementType").disabled="true";
        document.getElementById("employeeDisbursementType").value="dbt";
        document.getElementById('bankTable').style.display="none";
    }
    if(statusEmp == "Deceased" && mode=='modify' && length == 'none')
    {
	    alert('<bean:message key="alertNoNominee"/>');
		document.getElementById("employeeDisbursementType").disabled="true";
		document.getElementById("employeeDisbursementType").value="dbt";
		document.getElementById('bankTable').style.display="none";
	}
	if(statusEmp == "Deceased" && mode =='view' )
	 {

		document.getElementById('bankTable').style.display="block";
    }
    if(disType =="cash")
    {
		document.getElementById('bankTable').style.display="none";
	}




}

function populateDeleteSet(setName, delId)
{
	if(delId != null && delId != "")
	{
	var http = initiateRequest();
	
	var url = "../empPayroll/updateDelSets.jsp?type="+setName+"&id="+delId;
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

function disableBankOnLoad()
                   {
                   <%
		      if(request.getAttribute("mode") != null && ((String)(request.getAttribute("mode"))).trim().equals("modify"))
		      {
   %>
                   
if(document.getElementById("NomineeTable").style.display != 'none')
     {
  
var tbl =document.getElementById("NomineeTable");
var len = tbl.rows.length;

if(len == 3 && document.employeePensionForm.nomDisType.value != "dbt")
  {

		document.employeePensionForm.bank.disabled = true;
		document.employeePensionForm.branchNominee.disabled =true;
		document.employeePensionForm.bankAccount.disabled =true;
   }
else if(len >3)
            {
      for(var i=2;i<len;i++)
		  {
		 var j = i-2;
		 if( document.employeePensionForm.nomDisType[j].value != "dbt")
			    {

				 document.employeePensionForm.bank[j].disabled = true;
				 document.employeePensionForm.branchNominee[j].disabled =true;
				 document.employeePensionForm.bankAccount[j].disabled =true;
			    }
                  }
           }
    }
    <%
    }
    %>
}

/**
  By passing Nominee Id get the nominee Obj
*/
function populateNomineeMstr(value,obj)
{
   
   var txtObj=obj;
   var relation="",idActive="",isMarried="",dob="",isEmployeed="",branchId="",accountNum="",bankId="",nomineeId="";
   var tbl= document.getElementById('NomineeTable');
   var row=getRow(obj);
   var rowCount = row.rowIndex;
   
 
   var relationObj = getControlInBranch(tbl.rows[rowCount],"relationId");
   var idActiveObj = getControlInBranch(tbl.rows[rowCount],"isAlive");
   var isMarriedObj = getControlInBranch(tbl.rows[rowCount],"maritialStatus");
   var dobObj = getControlInBranch(tbl.rows[rowCount],"dateOfBirth");
   var isEmployeedObj = getControlInBranch(tbl.rows[rowCount],"employed");
   var branchObj = getControlInBranch(tbl.rows[rowCount],"branchNominee");
   
   var accountObj = getControlInBranch(tbl.rows[rowCount],"bankAccount");
   
   var bankObj = getControlInBranch(tbl.rows[rowCount],"bank");
   
   var dispTypeObj = getControlInBranch(tbl.rows[rowCount],"nomDisType");
   
   var nomineeMstrIdObj = getControlInBranch(tbl.rows[rowCount],"nomineeMstrId");
   
    var statusObj = getControlInBranch(tbl.rows[rowCount],"status");
    
    var percentageObj = getControlInBranch(tbl.rows[rowCount],"percentage");
    
   
   
   var type="nomieePopulate";
   
   var request = initiateRequest();

		if (request==null)
		{
		alert ("Your browser does not support AJAX!");
		return;
		}

		var url = "<%=request.getContextPath() %>"+ "/pension/PensionHeaderAJAX.do?value="+value+"&type="+type ;
		request.open("GET",url, true);
		request.onreadystatechange = function()
		{
		if (request.readyState == 4)
		{
		if (request.status == 200)
		{
		
		var response=request.responseText;
		var results = response.split("^");
		
		relation = results[0].split("+");
		if(relation!='')
		{
			relationObj.value=relation;
			dispTypeObj.value = "dbt";
			statusObj.value='';
			percentageObj.value=0;
			NomineeIdLastValue=0;
			
		}
		else
		{
		   relationObj.value='0';
		   percentageObj.value='';
		   dispTypeObj.value = "-1";
		   statusObj.value='';
		   
		   
		}
		
		
		idActive = results[1].split("+");
		if(idActive!='')
		{
				if(idActive==2)
				{
				   idActive=2;
				   idActiveObj.value=idActive;
				}
				else
				{
					idActiveObj.value=idActive;
				}
		}
		else
		{
		  idActiveObj.value='-1';
		}
		
		
		isMarried = results[2].split("+");
		if(isMarried!='')
		{
			if(isMarried==2)
			{   isMarried=2;
				isMarriedObj.value=isMarried;
			}
			else
			{
			  isMarriedObj.value=isMarried;
			}
		}
		else
		{
		  isMarriedObj.value='-1';
		}
		
		dob = results[3].split("+");
		dobObj.value=dob;
		
		isEmployeed = results[4].split("+");
		if(isEmployeed!='')
		{
			if(isEmployeed==2)
			{
			  isEmployeed=2;
			  isEmployeedObj.value=isEmployeed;
			}
			else
			{
				isEmployeedObj.value=isEmployeed;
			}
		}
		else
		{
		   isEmployeedObj.value ='-1';
		}
		
		branchId = results[5].split("+");
		if(branchId!='')
		{
			branchObj.value=branchId;
		}
		else
		{
		  branchObj.value ='0';
		}
		
		
		accountNum = results[6].split("+");
		accountObj.value=accountNum;
		
		bankId = results[7].split("+");
		if(bankId!='')
		{
			bankObj.value= bankId;
		}
		else
		{
		  bankObj.value = '0';
		}
		
		
		
		nomineeId = results[8].split("+");
		nomineeMstrIdObj.value= nomineeId;
		
		
		
		}
		}
		};
		
		request.send(null);
}

function deleteLastRowInModify(nomineeId)
{
   NomineeIdLastValue = nomineeId;
}

</script>
