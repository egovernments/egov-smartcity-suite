<%@ page language="java"  %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="ht" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<%@ page import="java.util.*,
		org.apache.log4j.Logger,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.lib.rjbac.dept.DepartmentImpl,org.egov.lib.rjbac.dept.Department,org.egov.commons.CChartOfAccounts,com.exilant.eGov.src.transactions.VoucherTypeForULB,
		java.text.DecimalFormat,org.egov.billsaccounting.client.WorksBillForm,org.egov.model.recoveries.Recovery,org.egov.infstr.utils.EGovConfig"
%>
<egov:config category="defaultValues" key="worksBillDefaultPurposeId" configFile="egf_config.xml" variable="defaultNetPay"/>
<%String defaultNetPay=(String)request.getAttribute("defaultNetPay");%>
<html>
<% WorksBillForm worksbill=(WorksBillForm)request.getAttribute("WorksBillForm");
List deptList=(List)request.getAttribute("deptList");

%>
<head>
<title>Works Bill</title>
<script>
var acccodeArray;
var codeObj;
var funcArray;
var funcObj;
var billarray
var billObj
var yuiflag = new Array();
var RowVal;
function ButtonPress(arg)
{

var total=eval(document.forms[0].totalWorkOrder.value);
var billed=eval(document.forms[0].totalBilledAmount.value);
var passd=eval(document.forms[0].passedAmount.value);
if(eval(passd)<=0)
{
	alert("The Passed  Amount cannot be Zero");
	return false;
}
billed=eval(billed+passd);
if(billed>total)
{
alert("The Bill Amount Exeeds Total WorkOrder Amount  Bill Canot be Created");
return false;
}
	if(!validateAll())
  		return false;
	if(!validateWorksBillForm(WorksBillForm))
 		return false;

	/*if(document.forms[0].dept.value=="0")
	{ 
 	alert("Select Bill Type ");
  	return false;
	} */
	//getpamt();
	if(!chkAmount())
		return false;
	if(!checkAdvanceAmount())
		return false;
	if(!checkTotal())
    	return false;
    if(!getNetamt())
   		return false;
   		


document.forms[0].buttonType.value=arg;
//validate();
if(arg=='saveclose'||arg=='savenew'||arg=='approve')
{
document.forms[0].dept.disabled = false;
var mode='<%=request.getAttribute("mode")%>'; 
document.forms[0].action="worksBill.do?submitType="+mode+"&buttonType="+arg;
document.forms[0].submit();
}
if(arg=='close'||arg=='cancel')
{
window.close();
}

}


function checkForFunction_AccountCode(table,funId,glCode){ //,checkEntityDuplication){
	//alert('inside function checkForFunction_AccountCode');
	var funcIdtemp1,glcodeTemp1,funcIdtemp2,glcodeTemp2;
	var tableObj = document.getElementById(table);
//	alert("length:"+tableObj.rows.length);
	

	for(i=1; i<tableObj.rows.length && tableObj.rows.length>2; i++)
	{ 
		funcIdtemp1=getControlInBranch(tableObj.rows[i],funId);
		glcodeTemp1=getControlInBranch(tableObj.rows[i],glCode);
		//if(funcIdtemp1!="" && glcodeTemp1!="")continue; COMMENTED BY MANI
		//	alert('glcodeTemp1.getAttribute(id):'+ glcodeTemp1.getAttribute('id'));
		if(glcodeTemp1.value == '')
		 {
		  alert("delete empty row :"+i); return false;
		 } 
		
		for(j=i+1; j<tableObj.rows.length; j++)
		{ 
			funcIdtemp2=getControlInBranch(tableObj.rows[j],funId);
		//	alert("funcIdtemp1:"+funcIdtemp1.value+";;;"+"funcIdtemp2:"+funcIdtemp2.value);
			glcodeTemp2=getControlInBranch(tableObj.rows[j],glCode);
			 if((glcodeTemp1.value  == glcodeTemp2.value)  && (funcIdtemp1.value == funcIdtemp2.value))
			 {
				alert('Same Account Code & Function Name can not appear more than once...CHECK ACCOUNT : ' + glcodeTemp1.value);
				return false;
	  		}
	 	}
	}
	return true;
	

}

function validateAll()

{

if(document.forms[0].bill_Type.value=="0")
	{ 
 	alert("Select Bill Type ");
  	return false;
	}
	
	if(deptMandatory=='Y')
	{
	if(document.forms[0].dept.value=="0")
	{
		alert("Select Department");	
		var temp="document.forms[0].dept.focus();";
		setTimeout(temp,0);		
		return;
	}
	
	}
if(!checkForFunction_AccountCode('debitGrid','deb_cv_fromFunctionCodeId','deb_chartOfAccounts_glCode')) return false ;
if(!checkForFunction_AccountCode('creditGrid','ded_cv_fromFunctionCodeId','ded_chartOfAccounts_glCode')) return false ;
if(!checkDate() ) return false;
return true;	
}
function checkDate()
{
	var woDate=document.forms[0].workOrderDate.value;//2009-06-28
	var billDate=document.forms[0].billDate.value;//30/06/2009
	var woDateArr=woDate.split("-");
	var billDateArr=billDate.split("/");
	var woDateObj=new Date(woDateArr[0],woDateArr[1]-1,woDateArr[2]);
	var billDateObj=  new Date(billDateArr[2],billDateArr[1]-1,billDateArr[0]);
	if(woDateObj>billDateObj)
		{
		var msg="Bill Date Should be more than PO  Date( "+woDateArr[2]+"/"+woDateArr[1]+"/"+woDateArr[0]+")";
		alert(msg);
		document.forms[0].billDate.focus();
		return false;
		}	
	else 
		{
			return true;
		}	

	}  

function chkAmount()
{
	var adjstAmt=document.forms[0].adjustmentAmount.value;
	if(adjstAmt==null || adjstAmt=="") 
	document.forms[0].adjustmentAmount.value=new Number(0);
	if(isNaN(adjstAmt))
	{
	   alert("Enter Only Numbers!!!...");
	   document.forms[0].adjustmentAmount.focus();
	   return false;
	}
	var advAmt=document.forms[0].advanceAmount.value;
	advAmt=isNaN(advAmt)?0:advAmt;
	adjstAmt=new Number(adjstAmt);
	advAmt=isNaN(advAmt)?0:advAmt;
	advAmt=new Number(advAmt);
	/*
	if(adjstAmt>advAmt)
	{
	   alert("Adjustment Amount Cannot be greater than Advance Amount");
	   document.getElementById("worksDetail_adjustmentAmount").focus();
	   return false;
	}*/
	var passedAmt=document.forms[0].passedAmount.value;
	passedAmt=isNaN(passedAmt)?0:passedAmt;
	passedAmt=new Number(passedAmt);
	var billAmt=document.forms[0].bill_Amount.value;
	billAmt=isNaN(billAmt)?0:billAmt;
	billAmt=new Number(billAmt);

	if(billAmt<passedAmt)
	{
	   alert("Passed Amount Cannot be greater than Bill Amount..");
	  // document.getElementById("contractorBillDetail_passedAmount").focus();
	   return false;

	}
	return true;
}
function checkAdvanceAmount()
{
	var adjustmentAmt=parseFloat(document.forms[0].adjustmentAmount.value);
	var passedAmt=parseFloat(document.forms[0].passedAmount.value);
			
		if(adjustmentAmt!="0" && adjustmentAmt!="")
		{
	       var advanceTotalAmt=document.forms[0].advanceAmount.value;
	       
	       		if(advanceTotalAmt==0 || document.forms[0].advanceAmount.length==0)
	       		{
	      		 alert("Advance Amount Not Yet Paid");
	      		 document.forms[0].adjustmentAmount.focus();
			return false;
	       		}
	       
		 }
		 if(adjustmentAmt!="0" && adjustmentAmt!="")
		{
		 	if(adjustmentAmt>passedAmt)
		 	{
		 	alert("Advance Adjust Amt must be less than or equal to  the Passed Amt");
		 	document.forms[0].adjustmentAmount.focus();
		 	return false;
		 	}
		}
	
	 return true;
}
function checkTotal()
         {
  	      var debitTotal = parseFloat(document.forms[0].debitTotal.value);
   	      var passAmt=parseFloat(document.forms[0].passedAmount.value);
   	      if(passAmt != debitTotal)
   	      {
   	      alert(" Total Debit Amount must be equal to the Passed Amount");
   	       return false;
   	      }
   	      return true;
       }
function getNetamt()
{

	passedAmt=parseFloat(document.forms[0].passedAmount.value);
	passedAmt=isNaN(passedAmt)?0:passedAmt;
	adjustAmt=parseFloat(document.forms[0].adjustmentAmount.value);
	adjustAmt=isNaN(adjustAmt)?0:adjustAmt;
	var table= document.getElementById("creditGrid");
	

	var debit=0;
	for(var i=1;i<table.rows.length;i++)
	{
		var amt=parseFloat(getControlInBranch(table.rows[i],"creditAmount").value);
		//alert("Amt:"+amt);
		amt=isNaN(amt)?0:amt;
		debit=debit+amt;
		//alert("Debit:"+debit);
	}
	document.forms[0].creditTotal.value=Math.round(debit*100)/100;
	netAmt=passedAmt-debit;//alert(netAmt+" "+passedAmt+" "+credit+" "+debit);
	
	
	if(netAmt<0)
	{
	   alert("Deductions Cannot be more than Passed Amount");
	   return false;

	}
	else if(adjustAmt>0 && netAmt<adjustAmt)
	{
	  alert("advance Adjusted Cannot be more than  Advance not yet adjusted  "+adjustAmt);
	  return false;
	}
	var advnotyetpaid=parseFloat(document.forms[0].advanceAmount.value);
	if(adjustAmt>advnotyetpaid)
	{
	  alert("Cannot Adjust Amount: "+adjustAmt );
	  return false;

	}
	
	document.forms[0].totalAmount.value=Math.round((netAmt-adjustAmt)*100)/100;
	return true;
}
function getpamt()
{
	var obj=document.forms[0].bill_Amount;
	if(obj.value==null || obj.value=="")
		return false;
	var pasedAmt=document.forms[0].passedAmount.value;
	//if(pasedAmt==null || pasedAmt=="")
		document.forms[0].passedAmount.value=obj.value;

}


function validateAcrossTds(obj)
{
var selectedAcc=obj.value;
//selectedAcc=getControlInBranch(getRow(obj),'ded_chartOfAccounts_glCode').value;
var tdsAcc=getControlInBranch(getRow(obj),'tds_code');
tdsAcc.value="0";
for(var i=1;i<tdsAcc.length;i++)
{
	var sRtn=tdsAcc.options[i].value;	
	var	a;
	 if(sRtn != '')
		{
		 a= sRtn.split("`~`");
		}
	if(selectedAcc==a[1])
		{
		alert("This Code  "+selectedAcc+"  Already Used In TDS master Please Select From Type Field ");
		obj.value="";//reset AccountCode
		var x = getControlInBranch(obj.parentNode.parentNode,'ded_chartOfAccounts_name');//Reset AccountHead
		x.value="";
		}
	}
}


function getNetpayable()
{
 var debit=document.forms[0].debitTotal.value;
 var credit=document.forms[0].creditTotal.value;
 if(isNaN(eval(credit))) credit=0;
 if(isNaN(eval(debit))) debit=0;
 var netPayable=eval(debit)-eval(credit);
 document.forms[0].totalAmount.value=netPayable;
 document.forms[0].net_Amount.value=netPayable;
}



/*function getNetamt()
{

	passedAmt=parseFloat(document.forms[0].passedAmount.value);
	passedAmt=isNaN(passedAmt)?0:passedAmt;
	adjustAmt=parseFloat(document.forms[0].adjustmentAmount.value);
	adjustAmt=isNaN(adjustAmt)?0:adjustAmt;
	var table= document.getElementById("creditGrid");

	var debit=0;
	for(var i=1;i<table.rows.length;i++)
	{
		var amt=parseFloat(getControlInBranch(table.rows[i],"creditAmount").value);
		//alert("Amt:"+amt);
		amt=isNaN(amt)?0:amt;
		debit=debit+amt;
		//alert("Debit:"+debit);
	}
	document.forms[0].creditTotal.value=Math.round(debit*100)/100;
	netAmt=passedAmt-debit;//alert(netAmt+" "+passedAmt+" "+credit+" "+debit);
	
	
	if(netAmt<0)
	{
	   alert("Deductions Cannot be more than Passed Amount");
	   return false;

	}
	else if(adjustAmt>0 && netAmt<adjustAmt)
	{
	  alert("Cannot Adjust Amount: "+adjustAmt);
	  return false;
	}
	document.forms[0].net_Amount.value=Math.round((netAmt-adjustAmt)*100)/100;
	return true;
} */

function getTds(obj)
	{
	var table=document.getElementById("creditGrid");
				Row=getRow(obj);
				RowVal=Row.rowIndex;
			
			var seltdsid=getControlInBranch(obj,"tds_code").value;
			
			if(seltdsid==0)
			{
			objt1=getControlInBranch(table.rows[RowVal],'creditAmount');
			objt1.value='';
			getControlInBranch(table.rows[RowVal],'ded_chartOfAccounts_glCode').value='';
			getControlInBranch(table.rows[RowVal],'ded_chartOfAccounts_name').value='';
			
			}
			
			var td=seltdsid.split("`~`");
		//	alert(seltdsid);
		//	alert(td[0]);
		//	alert(td[1]);
			
			var seltdsid=td[0];
			var amt=document.forms[0].bill_Amount.value;
						if(amt==undefined||amt=='')
			{
			alert("Please Enter the Bill Amount");
			getControlInBranch(obj,"tds_code").value="0";
			return ;
			}
				//seltdsid=1;
			 	var type='getTdsAmount';
				var url = "../commons/Process.jsp?type="+type+"&id="+seltdsid+"&amt="+amt;
				var req2 = initiateRequest();
				req2.onreadystatechange = function()
				{
					  if (req2.readyState == 4)
					  {
						  if (req2.status == 200)
						  {
							var codes2=req2.responseText;

							var a = codes2.split("^");
							var codes = a[0];
							dedAcccodeArray=codes.split("+");
							dedCodeObj = new YAHOO.widget.DS_JSArray(dedAcccodeArray);

//getRateAnd FillOtherDetails

							if(dedAcccodeArray!='')
							{
							//alert(dedAcccodeArray);
							var oneRow=dedAcccodeArray[0];
							det=oneRow.split("`-`");
							var rate=det[0];
							//alert(rate);
							var amount=det[1];
							var glcode=det[2];
							var glname=det[3];
//							alert(rate+"                  "+amount+" "+glcode+" "+glname);		
							 if(!isNaN(parseFloat(rate))&& rate>0)
								{	
		 					 	if(isNaN(parseFloat(document.forms[0].bill_Amount.value)))
		  							{
									  alert(' Amount');
		 								 return;
		  							}
		 						 else if(rate>0)
		  							{
		 							 passedAmt=parseFloat(document.forms[0].bill_Amount.value);
		 							 var dedAmt=Math.round(passedAmt*rate)/100;
	  								 }
        						}
       						 else
       						 if(!isNaN(parseFloat(amount)))
								{
       							 dedAmt=amount;
      
								}
							else
       						 {
       	 						alert("No Rate Or Deduction Defined For this Deduction Type");
        						dedAmt="";
      						  }
         					 var table= document.getElementById("creditGrid");
		 //  trObj=getRow(dedGridObj);
	 			 			objt1=getControlInBranch(table.rows[RowVal],'creditAmount');
							objt1.value=dedAmt;
							getControlInBranch(table.rows[RowVal],'ded_chartOfAccounts_glCode').value=glcode;
							getControlInBranch(table.rows[RowVal],'ded_chartOfAccounts_name').value=glname;


							colAllTotalSingleColCredit('creditAmount');
							}
						  }
					  }
				};
				req2.open("GET", url, true);
				req2.send(null);
				

				}
				

function replaceAll( str, replacements ) {
    for ( i = 0; i < replacements.length; i++ ) {
        var idx = str.indexOf( replacements[i][0] );

        while ( idx > -1 ) {
            str = str.replace( replacements[i][0], replacements[i][1] ); 
            idx = str.indexOf( replacements[i][0] );
        }

    }

    return str;
}
function autocompletecodeFunction(obj)
 {
   			 // set position of dropdown
   			var src = obj;
   			var target = document.getElementById('codescontainer');
   			//target.style.position="absolute";
   			var posSrc=findPos(src);
   			target.style.left=posSrc[0];
   			target.style.top=posSrc[1]+25;
   			target.style.width=250;

   			var currRow=getRow(obj);
   			var coaCodeObj = getControlInBranch(currRow,obj.name);
   			//40 --> Down arrow, 38 --> Up arrow
   		if(yuiflag[currRow.rowIndex] == undefined)
   		{
   			if(event.keyCode != 40 )
   			{
   				if(event.keyCode != 38 )
   				{
   						var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', funcObj);
   						oAutoComp.queryDelay = 0;
   						oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
   						oAutoComp.useShadow = true;
   						oAutoComp.maxResultsDisplayed = 15;
   						oAutoComp.useIFrame = true;
   				}
   			}
   		yuiflag[currRow.rowIndex] = 1;
   		}
}
function autocompletecode(obj)
   	{
   	
   		
		var currRow=getRow(obj);
   		var coaCodeObj = getControlInBranch(currRow,obj.name);
  		container=document.getElementById('codescontainer');
   		objPosition=findPos(obj);
   		container.style.position="absolute";
   		container.style.left=objPosition[0];
   		container.style.top=objPosition[1]+20;
   		container.style.width=500;

   		if(yuiflag[currRow.rowIndex] == undefined)
   		{
   					if(event.keyCode != 40 )
   					{
   						if(event.keyCode != 38 )
   						{
  							
   								var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
   							
   								oAutoComp.queryDelay = 0;
   								oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
   								oAutoComp.useShadow = true;
   								oAutoComp.maxResultsDisplayed = 25;
   								oAutoComp.useIFrame = true;
   								
   						}
   					}
   
   					yuiflag[currRow.rowIndex] = 'undefined';
   		}
   
}
var i=0;
function fillNeibrAfterSplit(obj,neibrObjName) 
 {
 	if(obj.value=='')
 		return;
 	var temp = obj.value; 
 	temp = temp.split("`-`");
 	obj.value=temp[0];
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
 	if(temp.length==1)
 	{
		var url="../commons/Process.jsp?type=validateGLcode&glcode="+temp[0];
		var req3 = initiateRequest();
		req3.onreadystatechange = function()
		{
			  if (req3.readyState == 4)
			  {
				  if (req3.status == 200)
				  {
					var codes2=req3.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					codes = codes.split("`-`");
					if(codes.length>1)
					{
						neibrObj.value=codes[1];
					}
					else
					{
						alert('Invalid Code\nPlease use autocomplete option');
						neibrObj.value='';
						obj.value='';
						return;
					}
				 }
			}
		};
		req3.open("GET", url, true);
		req3.send(null);
 	}
 	else
 	{
		if(obj.value!='' && temp.length<2)
		{
			alert('Invalid Code\nPlease use autocomplete option');
			neibrObj.value='';
			obj.value='';	
			return;
		}
		if(temp[1]==null) return; else 	neibrObj.value = temp[1];
	}
 }
 function fillNeibrAfterSplitFunction(obj,neibrObjName) 
 {
 	if(obj.value=='')
 		return;
 	var temp = obj.value; 
 	temp = temp.split("`-`");
 	obj.value=temp[0];
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
 	if(temp.length==1)
 	{
		var url="../commons/Process.jsp?type=validateFunctionName&functionName="+obj.value;
		var req3 = initiateRequest();
		req3.onreadystatechange = function()
		{
			  if (req3.readyState == 4)
			  {
				  if (req3.status == 200)
				  {
					var codes2=req3.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					codes = codes.split("`-`");
					if(codes.length>1)
					{
						neibrObj.value=codes[1];
					}
					else
					{
						alert('Invalid Code\nPlease use autocomplete option');
						neibrObj.value='';
						obj.value='';
						return;
					}
				 }
			}
		};
		req3.open("GET", url, true);
		req3.send(null);
 	}
 	else
 	{
		if(obj.value!='' && temp.length<2)
		{
			alert('Invalid Code\nPlease use autocomplete option');
			neibrObj.value='';
			obj.value='';	
			return;
		}
		if(temp[1]==null) return; else 	neibrObj.value = temp[1];
	}
 }
 function loadDropDownCodes()
	 {

	 	var type='getAllCoaCodes';
		var url = "../commons/Process.jsp?type=" +type+ " ";
		var req2 = initiateRequest();
		req2.onreadystatechange = function()
		{
			  if (req2.readyState == 4)
			  {
				  if (req2.status == 200)
				  {
					var codes2=req2.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					acccodeArray=codes.split("+");
					codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);

				  }
			  }
		};
		req2.open("GET", url, true);
		req2.send(null);
	 }
	 
 	
//loads Tds Items Dynamically  
function loadDropDownTDSCodes()
			 {
			 
				
				var worksdetailid=document.forms[0].codeList.value;
				//worksdetailid=1;
			 	var type='getAllTdsCodes';
				var url = "../commons/Process.jsp?type="+type+"&id="+worksdetailid;
				var req2 = initiateRequest();
				req2.onreadystatechange = function()
				{
					  if (req2.readyState == 4)
					  {
						  if (req2.status == 200)
						  {
							var codes2=req2.responseText;
							var a = codes2.split("^");
							var codes = a[0];
							dedAcccodeArray=codes.split("+");
							dedCodeObj = new YAHOO.widget.DS_JSArray(dedAcccodeArray);
				 			for(var j=0;j<dedAcccodeArray.length;j++)
 							{
 							var oneRow=dedAcccodeArray[j];
 							eachRow= oneRow.split("`-`");
 							//alert(eachRow);
 							var tdsid =eachRow[0];
 							var tdsname=eachRow[1];
 							var relationid=eachRow[2];
 							var relationName=eachRow[3];
 							var glcode=eachRow[4]; 
 							
 							var newtdsid=""+tdsid+"`~`"+glcode+"";		
							var mode='<%= request.getAttribute("mode")%>';
							if(mode!="view"&& mode!="modify" && mode!="approve")							
							{		
 							document.forms[0].tds_code.options[j+1]=new Option(tdsname,newtdsid); 
 							}
 							else
 							{
 							var x=document.getElementsByName("tds_code");
 							for(var i=0;i<x.length;i++)
 						    	x[i].options[j+1]=new Option(tdsname,newtdsid); 
 							}
 							
							}
						  }
					  }
					  
				};
				req2.open("GET", url, true);
				req2.send(null);
				
	 }
 function loadConSupName()
	 {
		var wdid=document.forms[0].codeList.value;
	 	var type='getConSupName';
		var url = "../commons/Process.jsp?type=" +type+"&worksdetailid="+wdid+"";
		var req2 = initiateRequest();
		req2.onreadystatechange = function()
		{
			  if (req2.readyState == 4)
			  {
				  if (req2.status == 200)
				  {
					var codes2=req2.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					alert(codes);
					var	name=codes.split('`-`');
					alert(name[0]);
					document.forms[0].CSId.value=name[0];
					document.forms[0].CSName.value=name[1];
				  }
			  }
		};
		req2.open("GET", url, true);
		req2.send(null);
		//loadDropDownTDSCodes();
	 }


function loadDropDownFuncNames()
	 {

	 	var type='getAllFunctionName';
		var url = "../commons/Process.jsp?type=" +type+ " ";
		var req2 = initiateRequest();
		req2.onreadystatechange = function()
		{
			  if (req2.readyState == 4)
			  {
				  if (req2.status == 200)
				  {
					var codes2=req2.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					funcArray=codes.split("+");
					funcObj= new YAHOO.widget.DS_JSArray(funcArray);


				  }
			  }
		};
		req2.open("GET", url, true);
		req2.send(null);
	 }


	function funClear(obj, tableName)
		 {
		   if(tableName.toLowerCase() == 'chartofaccounts')
			{
				var v =getControlInBranch(obj.parentNode.parentNode,'function_code')
				var w =getControlInBranch(obj.parentNode.parentNode,'cv_fromFunctionCodeId')
				if(v.value=="")
				{
				v.value = "";
				w.value = "";
				}
			}
		}



	function openSearch(obj,tableName,prefix)
	{
	var a = new Array(2);
    //var sRtn = showModalDialog("Search.html?screenName=directPayment","","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
    var sRtn =showModalDialog("../HTML/Search.html?filterServiceID=4","","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
  
    if ( sRtn != '' )
	{
		a = sRtn.split("`~`");
		var curr=getRow(obj);
		var table=document.forms[0].debitGrid;
		var x =getControlInBranch(curr, prefix+'chartOfAccounts_glCode');
		var y =getControlInBranch(curr, prefix+'chartOfAccounts_name');
		x.value = a[0];
		y.value = a[1];
		}
}	
function openSearch1(obj, tableName,prefix){
		var a = new Array(2);
		var sRtn;
		var str="";
		
		str = "../HTML/Search.html?tableNameForCode=function";
		sRtn= showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if(sRtn != '')
		{
				a = sRtn.split("`~`");
				getControlInBranch(obj.parentNode,prefix+'cv_fromFunctionCodeId').value=a[2];
				getControlInBranch(obj.parentNode,prefix+'function_code').value=a[1];

			
		}
	
	

  }
  function openSearch2(obj, tableName){
		var a = new Array(2);
		var sRtn;
		var str="";
		if(tableName.toLowerCase()=='function'){
		 str = "../Search.html?tableNameForCode=function";

		sRtn= showModalDialog(str,"",
				"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if(sRtn != '')
		{
			a = sRtn.split("`~`");

			if(tableName.toLowerCase() == 'function')
			{
				//alert("a 0 value= "+a[0]+" a 1 value = " +a[1]+" a 2 value ="+a[2]);
				var y = getControlInBranch(obj.parentNode,'function_code1');
				y.value = a[1];
				var x = getControlInBranch(obj.parentNode,'cv_fromFunctionCodeId1');
				x.value = a[2];

			}
		}
	}
  }
  
  function getwlist()
  {
var wopo=document.forms[0].codeList.value
var woponame=document.forms[0].codeName.value
if(wopo=="")
{
alert("First Select WO/PO Number ")
document.forms[0].bill_Amount.value=0;
return false;
}
getpamt();
 }
  
  function splitCoaName(obj,neibrObjName) 
 { 
 			var temp = obj.value;
 			//alert(obj.value);
 			temp = temp.split("`-`");
 			obj.value=temp[0];
 			
 			//alert(temp[0]);
 			//alert(temp[1]);
 			//alert(temp[2]);
 			
 			PageManager.DescService.onblur(neibrObjName);
			var currRow=getRow(obj);
			yuiflag[currRow.rowIndex] = undefined;
			neibrObj=getControlInBranch(currRow,neibrObjName);
			if(temp[1]==null) return;
			else 
			{
			neibrObj.value = temp[1];
			document.getElementByName("glcodeId").value=temp[2];
			//alert(document.getElementByName("glcodeId").value);
			
			}
			
}
function splitCoaNameDed(obj,neibrObjName) 
 { 
 			var temp = obj.value;
 			//alert(obj.value);
 			temp = temp.split("`-`");
 			obj.value=temp[0];
 			
 			
 			PageManager.DescService.onblur(neibrObjName);
			var currRow=getRow(obj);
			yuiflag[currRow.rowIndex] = undefined;
			neibrObj=getControlInBranch(currRow,neibrObjName);
			if(temp[1]==null) return;
			else 
			{
			neibrObj.value = temp[1];
			//document.getElementByName("glcodeId").value=temp[2];
			getControlInBranch(obj.parentNode.parentNode,"glcodeId").value=temp[2];
			}
}
function fillNetPayNameAfterSplit()
{
var netPay=document.forms[0].net_chartOfAccounts_glCode.value;
var x=netPay.split('~');
document.forms[0].net_chartOfAccounts_name.value=x[2];
}

function onBodyLoad()
{
	<%
		ArrayList functionaryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-functionary");	
		VoucherTypeForULB voucherType=new VoucherTypeForULB();
		String deptMandatory=(String)voucherType.readIsDepartmentMandtory();
		%>
	 deptMandatory='<%=deptMandatory%>';
	if(deptMandatory!="Y")
	{
		var deptStarObj=document.getElementById('departmentStar');
		if(deptStarObj!=null && deptStarObj!=undefined)
		{
					deptStarObj.style.display='none';
		}
	}
	loadDropDownCodes();
 	loadDropDownFuncNames();
 	var mode="${mode}";	
	if(mode == "modify")
	{
	document.getElementById("savenNew").style.display="none";
	getNetamt();	//
	//colAllTotalSingleColCredit('creditAmount');
	//colAllTotalSingleColDebit('debitAmount');

	}
	if(mode == "approve")
	{
	
	document.getElementById("submitGrid2").style.display="none";
	document.getElementById("glButtons").style.display="block";
	//document.getElementById("entries").style.display="block";
	//colAllTotalSingleColCredit('creditAmount');
	//colAllTotalSingleColDebit('debitAmount');
	getNetamt();

		//document.getElementById("glEntry").style.display="block";


	}
	if(mode == "view")
	{
	for(var i=0;i<document.forms[0].length;i++)
				document.forms[0].elements[i].disabled=true;
	document.getElementById("backbuttons").disabled=false;

	document.getElementById("submitGrid2").style.display="none";
	document.getElementById("backbuttons").style.display="block";
document.getElementById("backbutton").disabled=false;


	}

	if(mode=="modify"|| mode == "approve"||mode == "view")		
	{
	
	document.getElementById("bt").style.display="block";
	document.getElementById("bv").style.display="block";
	document.getElementById("snNo").style.display="block";
	document.getElementById("snText").style.display="block";

	colAllTotalSingleColCredit('creditAmount');
	colAllTotalSingleColDebit('debitAmount');
	getNetamt();
	}
	var target='<%=(request.getAttribute("alertMessage"))%>';
	var alertmsg='<%=request.getAttribute("alertMessage")%>';
	if(alertmsg!="null" && alertmsg!='')
	{
		alert(alertmsg);  		
	}
	var billCreation="${billCreation}";	
	if(billCreation=='failed')
	return;
	
	if(mode=="create")
	{			
		<%
		//this portion sets the default netpayable for the bill
		List nplist=(List)request.getAttribute("netPayList");
		Iterator itr=nplist.iterator();
		while(itr.hasNext())
		{
		 		CChartOfAccounts acc=(CChartOfAccounts)itr.next();
				if(acc.getPurposeId().equals(defaultNetPay))
				{
			
		%>
		document.forms[0].net_chartOfAccounts_glCode.value="<%=acc.getId()+"~"+acc.getGlcode()+"~"+acc.getName()%>";
		document.forms[0].net_chartOfAccounts_name.value="<%=acc.getName() %>";
		<%
			}
		}
		%>
		//Defaulting dept is disabled for the works/PO order.
		var deptid = document.forms[0].dept.value;
		var tds ='<%=request.getAttribute("tdsList")%>';  
		if(tds && deptid && deptid != 0){
			document.forms[0].dept.disabled = true;
		}
		
		 
	
	}
	var buttonType1='<%=request.getAttribute("buttonType")%>';	
	if(buttonType1 == "saveclose")
	{
	
		window.close();
	}
	if(buttonType1 == "saveview")
	{		
		var billno="<%=request.getAttribute("billId")%>";
		document.forms[0].action = "../billsaccounting/worksBill.do?submitType=beforeCreate","";
		document.forms[0].submit();
		return;		
	}	
	if(buttonType1 == "savenew")
	{		
		
		document.forms[0].action = "../billsaccounting/worksBill.do?submitType=beforeCreate","";
		document.forms[0].submit();
		return;		 
	}	
}  

 function disableControls(frmIndex, isDisable)
 {
	for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =isDisable;
 }


function callGenericScreenSchedule(obj)
{
	getWPCodeNameList();
	window.showModalDialog("../commonyui/egov/genericScreenNoParent.jsp"+"?xmlconfigname="+"egf_config.xml"+"&categoryname="+"WPNworks",window,'dialogHeight:500px;dialogWidth:600px');      
	if(nameValue!="")
	{
		document.forms[0].worksName.value=nameValue;
		document.forms[0].codeList.value=idValue;
		document.forms[0].codeName.value=descValue;
		
		nameValue="";idValue="";descValue="";
		var wno=document.forms[0].codeList.value;
		
		var mode="<%=request.getAttribute("mode")%>";
		document.forms[0].action = "../billsaccounting/worksBill.do?submitType=getTdsAndotherdtls&mode="+mode;
		document.forms[0].submit();
	}
}

/* checking for duplicate account codes */
function checkDuplicateAcc(table,prefix)
{
}
	
function getWPCodeNameList() 
{
	document.forms[0].name.value="";
	document.forms[0].codeName.value="";
	document.forms[0].CSName.value="";
	document.forms[0].codeList.value='';
	document.forms[0].totalBilledAmount.value=0;
	document.forms[0].worksName.value='';
	document.forms[0].totalWorkOrder.value=0;
	document.forms[0].codeList.disabled=false;
	document.forms[0].name.disabled=true;
	document.forms[0].CSName.disabled=true;
} 

function addRowDebitGrid(tablename)
{
	var tbl = document.getElementById(tablename);
	var tbody=tbl.tBodies[0];
	var lastRow = tbl.rows.length;
	var rowObj = tbl.rows[lastRow-1].cloneNode(true);
	
	tbody.appendChild(rowObj);	
/*	for(var i=0;i<tbl.rows[lastRow-1].cells.length;i++)
	{
	tbl.rows[lastRow-1].cells[i].value="";
	}*/
	document.forms[0].deb_cv_fromFunctionCodeId[lastRow-1].value="";
	document.forms[0].deb_function_code[lastRow-1].value="";
	document.forms[0].deb_chartOfAccounts_glCode[lastRow-1].value="";
	document.forms[0].deb_chartOfAccounts_name[lastRow-1].value="";
	document.forms[0].debitAmount[lastRow-1].value=""; 
	//document.forms[0].deb_chartOfAccounts_glCode[lastRow-1].focus();
	tbl.rows[tbl.rows.length-1].cells[0].className='normaltext';
}
function addRow(tablename)
{
	var tbl = document.getElementById(tablename);
	var tbody=tbl.tBodies[0];
	var lastRow = tbl.rows.length;
	var rowObj = tbl.rows[lastRow-1].cloneNode(true);
	tbody.appendChild(rowObj);
	document.forms[0].ded_cv_fromFunctionCodeId[lastRow-1].value="";
	document.forms[0].ded_function_code[lastRow-1].value="";
	document.forms[0].ded_chartOfAccounts_glCode[lastRow-1].value="";
	document.forms[0].ded_chartOfAccounts_name[lastRow-1].value="";
	document.forms[0].creditAmount[lastRow-1].value=""; 
	document.forms[0].tds_code[lastRow-1].value="0"; 
	//document.forms[0].ded_chartOfAccounts_glCode[lastRow-1].focus();
	tbl.rows[tbl.rows.length-1].cells[0].className='normaltext';
}



function deleteRow(tablename)
{

  var tbl = document.getElementById(tablename);
  var lastRow = (tbl.rows.length)-1;

	if(lastRow ==1)
	{
		 alert("This row can not be deleted");
		return false;
	 }
	else
	{
		tbl.deleteRow(lastRow);
		return true;
	}

}


function changeColor(currObj,obj)
{
	var table=document.getElementById(obj);
	var x=getRow(currObj);
	var y=x.rowIndex;
	var tempclassname=currObj.className;
	for(var i=1;i<table.rows.length;i++)
	{
		rowSel=true;
		if(table.rows[i].cells[0].name!=currObj.name)
		continue;
		getControlInBranch(table.rows[i],currObj.name).className='normaltext';
		table.rows[i].cells[0].className='normaltext';
	}
	if(tempclassname!='rowRev')
	{
		currObj.className='rowRev';
		getControlInBranch(table.rows[getRow(currObj).rowIndex],'selectTd').className='rowRev';
	}
	else
	{		
		currObj.className='';
		getControlInBranch(table.rows[getRow(currObj).rowIndex],'selectTd').className='';
	}
}

function colAllTotalSingleColDebit(objname1)
 {	
 	var a = document.getElementsByName(objname1);
 	count = 0;
 	for(var i = 0; i<=a.length -1;i++) if(a[i].value)
 		count += parseFloat(a[i].value);
 		count=Math.round(count*100)/100 ;
 		document.forms[0].debitTotal.value = count;
 		getNetpayable();
 }
 function colAllTotalSingleColCredit(objname1)
 {	
 	var a = document.getElementsByName(objname1);
 	count = 0;
 	for(var i = 0; i<=a.length -1;i++) if(a[i].value)
 		count += parseFloat(a[i].value);
 		count=Math.round(count*100)/100 ;
 		document.forms[0].creditTotal.value = count;
 		getNetpayable();
 }
 
 	function showEntry()
		{
			var trObj;
			
			var tObj=document.getElementById("entries");
			tObj.style.display="block";
			var cr=0,dr=0,dedAmount=0;
			tObj.style.display="block";
			var tableObj=document.getElementById("entries");
			var gltable= document.getElementById("debitGrid");
			for(var i=tableObj.rows.length-1;i>=2;i--)
			{
				tableObj.deleteRow(i);
			}
			for(var i=1;i<gltable.rows.length;i++)
			{

				code=getControlInBranch(gltable.rows[i],'deb_chartOfAccounts_glCode').value;
				if(code)
				{
				name=getControlInBranch(gltable.rows[i],'deb_chartOfAccounts_name').value;
				debit=getControlInBranch(gltable.rows[i],'debitAmount').value;
				if(isNaN(parseFloat(debit,10)))
				dr=dr+0;
				else
				dr=dr+parseFloat(debit,10);
			//	narration=getControlInBranch(gltable.rows[i],'voucherDetail_narration').value;
				var Obj=getControlInBranch(tableObj.rows[1],"display_Code");
				trObj=getRow(Obj);
				 if(i==1){
								newRow=trObj;
								objt1=getControlInBranch(newRow,'display_Code');
				 				objt2=getControlInBranch(newRow,'display_Head');
				 				objt3=getControlInBranch(newRow,'display_Debit');
				 				objt4=getControlInBranch(newRow,'display_Credit');
				 			//	objt5=getControlInBranch(newRow,'display_Narration');
				 				if(code=="") code=" -"; if(name=="") name=" -"; if(debit=="") debit=" 0"; /// if(narration=="") narration=" -";
				 				objt1.innerHTML=code;
				 				objt2.innerHTML=name;
				 				objt3.innerHTML=debit;
				 				//objt4.innerHTML=credit;
							//	objt5.innerHTML=narration
				 }else{

						 var newRow=trObj.cloneNode(true);
						newRow=tableObj.tBodies[0].appendChild(newRow);
						objt1=getControlInBranch(newRow,'display_Code');
						objt2=getControlInBranch(newRow,'display_Head');
						objt3=getControlInBranch(newRow,'display_Debit');
						objt4=getControlInBranch(newRow,'display_Credit');
					//	objt5=getControlInBranch(newRow,'display_Narration');
						if(code=="") code=" -"; if(name=="") name=" -"; if(debit=="") debit=" 0";// if(narration=="") narration=" -";
						objt1.innerHTML=code;
						objt2.innerHTML=name;
						objt3.innerHTML=debit;
						//objt4.innerHTML=credit;
						//objt5.innerHTML=narration;
				}
			  }
			}

			var dedTable= document.getElementById("creditGrid");
			for(var i=1;i<dedTable.rows.length;i++)
			{
				var code=getControlInBranch(dedTable.rows[i],'ded_chartOfAccounts_glCode').value;
				if(code)
				{
				var name=getControlInBranch(dedTable.rows[i],'ded_chartOfAccounts_name').value;
				var amount=getControlInBranch(dedTable.rows[i],'creditAmount').value;
				dedAmount=dedAmount+parseFloat(amount,10);
				var newRow=trObj.cloneNode(true);
				newRow=tableObj.tBodies[0].appendChild(newRow);
				objt1=getControlInBranch(newRow,'display_Code');
				objt2=getControlInBranch(newRow,'display_Head');
				objt3=getControlInBranch(newRow,'display_Debit');
				objt4=getControlInBranch(newRow,'display_Credit');
				//objt5=getControlInBranch(newRow,'display_Narration');
				if(code=="") code=" -"; if(name=="") name=" -";
				objt1.innerHTML=code;
				objt2.innerHTML=name;
				objt3.innerHTML="0";
				objt4.innerHTML=amount;
			//	objt5.innerHTML=" -";

				}
			}



if(isNaN(parseFloat(document.getElementById('passedAmount').value)))
			passedAmount=0;
			else
			passedAmount=parseFloat(document.getElementById('passedAmount').value);

			if(isNaN(parseFloat(document.getElementById('adjustmentAmount').value)))
			adjAmt=0;
			else
			adjAmt=	parseFloat(document.getElementById('adjustmentAmount').value);
			//totalamount=document.getElementById("worksDetail_NetAmount").value;
			totalamount=(passedAmount-cr)-dedAmount;


		//	var iRow=glNameCode.split(";");
		//	var rowContents=null;tempObj=null;
		//	for(var i=1;i<(iRow.length)-1;i++)
		//	{
			//	rowContents=iRow[i].split("^");
				var newRow=trObj.cloneNode(true);
				newRow=tableObj.tBodies[0].appendChild(newRow);
				tempObj1=getControlInBranch(newRow,'display_CodeType');
				//tempObj1.innerHTML=rowContents[0];
				tempObj2=getControlInBranch(newRow,'display_Code');
				tempObj2.innerHTML=document.forms[0].net_chartOfAccounts_glCode.value;
				tempObj3=getControlInBranch(newRow,'display_Head');		
				tempObj3.innerHTML=document.forms[0].net_chartOfAccounts_name.value;
				
				tempObj4=getControlInBranch(newRow,'display_Debit');	
				tempObj4.innerHTML="0"
				tempObj5=getControlInBranch(newRow,'display_Credit');
				tempObj5.innerHTML=Math.round(totalamount*100)/100;

			//	tempObj5=getControlInBranch(newRow,'display_Narration');
			//	tempObj5.innerHTML=" -";

		//	}
	/*		if(isNaN(parseFloat(document.getElementById('passedAmount').value)))
			passedAmount=0;
			else
			passedAmount=parseFloat(document.getElementById('passedAmount').value);

			if(isNaN(parseFloat(document.getElementById('adjustmentAmount').value)))
			adjAmt=0;
			else
			adjAmt=	parseFloat(document.getElementById('adjustmentAmount').value);
			//totalamount=document.getElementById("worksDetail_NetAmount").value;
			totalamount=(passedAmount-cr)-dedAmount; */
		
				


		/*	col1=getControlInBranch(trObj,'display_Credit');
			col1.innerHTML=Math.round(totalamount*100)/100; */
	
		/*	for(var i=1;i<tableObj.rows.length;i++)
			{
				var typeObj=getControlInBranch(tableObj.rows[i],"display_CodeType");
				var type=typeObj.innerHTML;
				if(type =='ConPayCode')
				{
					var trObj=getRow(typeObj);
					col1=getControlInBranch(trObj,'display_Credit');
					col1.innerHTML=Math.round(totalamount*100)/100;
					col2=getControlInBranch(trObj,'display_Debit');
					col2.innerHTML="0";
				}
			}

			for(var i=1;i<tableObj.rows.length;i++)
			{
				var typeObj=getControlInBranch(tableObj.rows[i],"display_CodeType");
				var type=typeObj.innerHTML;
				if(type =='ConAdvCode')
				{
					var trObj=getRow(typeObj);
					col1=getControlInBranch(trObj,'display_Credit');
					col1.innerHTML=Math.round(adjAmt*100)/100;
					col2=getControlInBranch(trObj,'display_Debit');
					col2.innerHTML="0";
				}
			}*/
			 var dSum=0;
			 var cSum=0;
			for(var i=1;i<tableObj.rows.length;i++)
			{

			  var dObj=getControlInBranch(tableObj.rows[i],"display_Debit");
			  var cObj=getControlInBranch(tableObj.rows[i],"display_Credit");
			 if(!isNaN(parseFloat(dObj.innerHTML)))
			 {
			   var sum=(parseFloat(dObj.innerHTML));
			   dSum=dSum+sum;
			 }
			 if(!isNaN(parseFloat(cObj.innerHTML)))
			 {
			   var sum=(parseFloat(cObj.innerHTML));
			   cSum=cSum+sum;
			 }
			}
			dSum=Math.round(dSum*100)/100;
			cSum=Math.round(cSum*100)/100;
			var newRow=trObj.cloneNode(true);
			newRow=tableObj.tBodies[0].appendChild(newRow);
			objt1=getControlInBranch(newRow,'display_Code');
			objt2=getControlInBranch(newRow,'display_Head');
			objt3=getControlInBranch(newRow,'display_Debit');
			objt4=getControlInBranch(newRow,'display_Credit');
		//	objt5=getControlInBranch(newRow,'display_Narration');
			objt1.innerHTML=" ";
			objt2.innerHTML=" ";
			objt3.innerHTML=dSum;
			objt4.innerHTML=cSum;
		//	objt5.innerHTML=" ";

	}
	function onClickCancel()
	{ 
		window.location='worksBill.do?submitType=beforeCreate';
	}

</script>
</head>
<body onload="onBodyLoad();">

<ht:form action="/billsaccounting/worksBill.jsp" >
<ht:hidden property="buttonType" value="<%=worksbill.buttonType%>" />
<ht:hidden property="totalAmount"  />
<ht:hidden property="workOrderDate"  />
<table class=tableStyle>
<tr>
<td>

<table width=100%>
<tr>
<td width=100% height=23></td>
</tr>

</table>

<table align=center  width=100% >

<tr>

<td class=labelcell align="right" id="bt" style="Display: none" ><div align=right><bean:message key="worksbill.BillNumber"/>BillNumber</div>
</td>
<td class=fieldcell id="bv"   style="Display: none" ><ht:hidden property="billId"  styleClass="fieldinput" value="<%=worksbill.billId%>" /> 
<ht:text property="billNo" maxlength="16" styleClass="fieldinput" value="<%=worksbill.billNo%>" /> </td>

<td class=labelcell align="right"><div align=right><bean:message key="worksbill.billDate"/>BillDate</div></td>
<td class="smallfieldcell" align="center" >
<ht:text property="billDate" onkeyup="DateFormat(this,this.value,event,false,'3')" />
<a href="javascript:show_calendar('forms[0].billDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
<img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a></td>
</tr>


<tr>
<td class=labelcell align="right" ><div align="right"  ><bean:message key="worksbill.expenditureType"/>Expenditure Type<span class="leadon">*</span></div></td>
<td class=smallfieldcell >
<ht:text styleClass="combowidth1" property="expenditure_Type" value="Works"  readonly="true">
</ht:text></td>
<td class=labelcell align="right"  ><div align="right"  ><bean:message key="worksbill.billType"/>Bill Type<span class="leadon">*</span></div></td>
<td class=smallfieldcell >
<ht:select styleClass="combowidth1" property="bill_Type" value="<%=worksbill.bill_Type%>"     >
<ht:option value="0">----Choose----</ht:option>
<ht:option value="Running Bill">Running Bill</ht:option>
<ht:option value="Final Bill">Final Bill</ht:option>
</ht:select></td>
</tr>
<tr>
<td class=labelcell align="right"><div align="right" >WO/PO Number</div></td>
<td class=smallfieldcell colspan=3>
<ht:text   property="codeList"   value="<%=worksbill.codeList%>" style="Display: none"  /><ht:text   property="codeName"  styleClass="fieldinput" style="WIDTH: 250px " />
<span id="genricimage"><img src="../images/plus1.gif"   onclick="callGenericScreenSchedule(this);" ></span>
<ht:text property="worksName" styleClass="fieldinput"  style="WIDTH: 250px "  readonly="true" /></td>
</tr>
<tr>
<td class=labelcell align="right"><div align="right" ><bean:message key="worksbill.contractorsupplier"/>Contractor</div></td>

<td class=fieldcell>
<ht:hidden  property="CSId" value="<%=worksbill.CSId%>"/>
<ht:text  tabindex="-1" property="CSName"  styleClass="fieldinput"  readonly="true" value="<%=worksbill.CSName%>" /></td>
<td class=labelcell align="right"><div align=right><bean:message key="worksbill.department"/>Department<span id="departmentStar" class="leadon">*</span></div></td>
<td class=smallfieldcell align=left >
<%System.out.println("00000000000000000"+worksbill.getDept());%>
<ht:select styleClass="combowidth1" property="dept" value="<%=worksbill.dept%>" >
<ht:option value='0'>--Choose--</ht:option>	
<c:forEach var="dep" items="${deptList}">
<ht:option value="${dep.id}">${dep.deptName}</ht:option>	
</c:forEach>
</ht:select></td>

</tr>
</tr>
<tr>
<td class=labelcell align="right" height="14"><div align=right><bean:message key="worksbill.totalWorkOrder"/>Total WorkOrder Amount </div></td>
<td class=fieldcell height="14">
<ht:text styleClass="fieldinput-right"  property="totalWorkOrder" value="<%=worksbill.totalWorkOrder%>" readonly="true" />

</td>
<td class=labelcell align="right" height="14"><div align="right" ><bean:message key="worksbill.totalBilledAmount"/>Total Billed Amount </div></td>
<td class=fieldcell height="14"><ht:text property="totalBilledAmount" maxlength="16" styleClass="fieldinput-right" value="<%=worksbill.totalBilledAmount%>"  onblur="getwlist()" readonly="true" /></td>

		

</tr>




<tr>
<td class=labelcell align="right" height="14"><div align=right><bean:message key="worksbill.advanceAmount"/>Advance Not Yet Adjusted </div></td>
<td class=fieldcell height="14">
<ht:text styleClass="fieldinput-right"  property="advanceAmount" value="<%=worksbill.advanceAmount%>"  readonly="true"/>

</td>
<td class=labelcell align="right" height="14"><div align="right" ><bean:message key="worksbill.billAmt"/>Bill Amount <span class="leadon" >*</span></div></td>
<td class=fieldcell height="14"><ht:text property="bill_Amount" maxlength="16" styleClass="fieldinput-right" value="<%=worksbill.bill_Amount%>"  onchange="getwlist()" /></td>

		

</tr>
<tr>
	  


		

<td align="right" valign="center" class="labelcell" ><div align="right"><bean:message key="worksbill.adjustmentAmount"/>Advance Adjusted 
</div></td>
<td class=fieldcell> <ht:text maxlength="16" styleClass="fieldinput-right" value="<%=worksbill.adjustmentAmount%>" property="adjustmentAmount" onblur="getNetamt()" />
</td>

<td class=labelcell><div align="right" valign="center" ><bean:message key="worksbill.passedAmount"/>Passed Amount</div>
									</td>
									<td class=fieldcell>
				                    	<ht:text property="passedAmount" styleClass="fieldinput-right" value="<%=worksbill.passedAmount%>" tabindex="-1"  maxlength="16"/></td>

 </tr>
 <tr>
<td class=labelcell align="right"><div align=right><bean:message key="worksbill.MBookRefNo"/>M B Ref No</div>
</td>

<td class=fieldcell><ht:text property="MBrefNo" maxlength="16" styleClass="fieldinput" value="<%=worksbill.MBrefNo%>" /> </td>
<td class="labelcell" align="right" >Functionary&nbsp;&nbsp;</td> 
			<td class="smallfieldcell">
			<html:select  property="functionaryId" styleClass="bigcombowidth">
			<html:option value='0'>--Choose--</html:option>
			<c:forEach var="functionary" items="<%=functionaryList%>"> 
				<html:option value="${functionary.id}">${functionary.name}</html:option>
			</c:forEach> 
			</html:select>
			</td> 
</tr>
<tr>
<td class=labelcell align="right" id=snText style="Display: none"><div align=right><bean:message key="worksbill.sanctionNo"/>Sanction No</div>
</td>
<td class=fieldcell id=snNo style="Display: none"><ht:text property="sanctionNo" maxlength="16" styleClass="fieldinput"  value="<%=worksbill.sanctionNo%>" /> </td>
<td class=labelcell  align=right ><div align=right ><bean:message key="worksbill.narration"/>Narration</div> </td>

<td class=fieldcell ><ht:textarea property="narration"  styleClass="narrationfieldinput"  value="<%=worksbill.narration%>" /> </td>
</tr>

</table>
 <table width="100%" align=center border="1" cellpadding="0" cellspacing="0" id="debitGrid">

                                      <tr >
                                     	<td class="thStlyle-small" height="29" >&nbsp;&nbsp;</td>
                                        <td class="thStlyle-small" height="29" ><div align="center"><bean:message key="worksbill.functionName"/>Function Name</div></td>
                                        <td class="thStlyle-small" height="29" ><div align="center"><bean:message key="worksbill.accountCode"/>Account&nbsp;Code</div></td>
                                        <td class="thStlyle-small" height="29" ><div align="center"><bean:message key="worksbill.accountHead"/>Account Head</div></td>
                                        <td class="thStlyle-small" height="29" ><div align="center"><bean:message key="worksbill.DrAmt"/>Debit Amount</div></td>
                                        <td class="thStlyle-small" height="29" ><div align="center"><bean:message key="worksbill.details"/>&nbsp;</div></td>
                                     </tr>
<% 
try{
System.out.println("worksbill.deb_chartOfAccounts_glCode:"+worksbill.deb_chartOfAccounts_glCode);
if(worksbill.deb_chartOfAccounts_glCode!=null)
{
for(int i=0;i<worksbill.deb_chartOfAccounts_glCode.length;i++)
{
if(worksbill.deb_chartOfAccounts_glCode[i]!=null)
{
//if(worksbill.deb_function_code[i]==null)worksbill.deb_function_code[i]="";
System.out.println(":"+worksbill.deb_cv_fromFunctionCodeId[i]+":");
System.out.println(":"+worksbill.deb_function_code[i]+":");
%>
                        <tr>
                        <td class="tdStlyle" onClick="changeColor(this,'debitGrid');" name="selectTd" id="selectTd1" height="24" ></td>
                        <td class=fieldcelldesc height="24" >
                        <ht:hidden property="deb_cv_fromFunctionCodeId" value="<%=worksbill.deb_cv_fromFunctionCodeId[i]%>" />
                        <ht:text styleClass="fieldinputlarge"   property="deb_function_code" value="<%=worksbill.deb_function_code[i]%>"   onkeyup="autocompletecodeFunction(this);"  onblur="fillNeibrAfterSplitFunction(this,'deb_cv_fromFunctionCodeId','deb_chartOfAccounts_glCode');" /><IMG   id=IMG1 onclick=openSearch1(this,'deb_cv_fromFunctionCodeId','deb_') src="../images/plus1.gif"  >
						</td>
                         <td class=smallfieldcell height="24" >
                
			<ht:text styleClass="fieldinput-right" property="deb_chartOfAccounts_glCode" value="<%=worksbill.deb_chartOfAccounts_glCode[i]%>"  onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'deb_chartOfAccounts_name','debitAmount')" maxlength="20" tabindex="1"/>
			<IMG id=IMG2 onclick="openSearch(this,'chartOfAccounts','deb_')" src="../images/plus1.gif"  >
                         </td>
                         <td class="fieldcelldesc" height="24" >
			<ht:text styleClass="fieldinputlarge" property="deb_chartOfAccounts_name" value="<%=worksbill.deb_chartOfAccounts_name[i]%>" maxlength="50" readonly="true"  tabindex="-1"  />
                         </td>
                       
                         <td class=fieldcell height="24" >
			<ht:text styleClass="fieldinput-right" property="debitAmount" value="<%=worksbill.debitAmount[i]%>" onblur="colAllTotalSingleColDebit('debitAmount');getNetamt();" maxlength="16"  onkeydown ="addNewRow('debitGrid','chartOfAccounts_glCode')" tabindex="1" />
                         </td>
                       
                        </tr>
                        <% 
                        }//inner if
                        }//for
                        }//if
                        else
                        {
                        %>
                        <tr>
                        <td class="tdStlyle" onClick="changeColor(this,'debitGrid');" name="selectTd"  ></td>
                        <td class=fieldcelldesc >
                        <input type="hidden" name="deb_cv_fromFunctionCodeId" >
                        <ht:text styleClass="fieldinputlarge"   property="deb_function_code" value=""   onkeyup="autocompletecodeFunction(this);"   onblur="fillNeibrAfterSplitFunction(this,'deb_cv_fromFunctionCodeId','deb_chartOfAccounts_glCode');" /><IMG   id=IMG1 onclick=openSearch1(this,'deb_cv_fromFunctionCodeId','deb_') src="../images/plus1.gif"  >
						</td>
                         <td class=smallfieldcell >
                         <ht:hidden styleClass="fieldinput-right" property="deb_chartOfAccounts_glCodeId" value=""  />  
			<ht:text  styleClass="fieldinput-right" property="deb_chartOfAccounts_glCode" value=""   onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'deb_chartOfAccounts_name','debitAmount')" maxlength="20" tabindex="1" />
			<IMG id=IMG2 onclick="openSearch(this,'chartOfAccounts','deb_')" src="../images/plus1.gif"  >
                         </td>
                         <td class="fieldcelldesc" >
			<ht:text styleClass="fieldinputlarge" property="deb_chartOfAccounts_name" value="" maxlength="50"    readonly="true" tabindex="-1" />
                         </td>
                       
                         <td class=fieldcell >
						<ht:text styleClass="fieldinput-right" property="debitAmount" value=""  maxlength="16"  onkeydown ="addNewRow('debitGrid','chartOfAccounts_glCode')" tabindex="1"/>
                         </td>
                       
                        </tr>
                        <%
                        
                        }
                        }catch(Exception e)
                        {
                        System.out.println("EXCEPTION IN JSP: "+e.getMessage());
                        e.printStackTrace();
                        }
                        
                        %> 	
                         
                          </table>
                          <table width=100%>
					      	<tr><td colspan=2 nowrap>
							<input type="button"  class="button" value="Delete Row" id="deleteDetail" name="deleteDetail" onclick=" deleteRowTable('debitGrid');colAllTotalSingleColDebit('debitAmount');" >
							<input type="button"  class="button" value="Add Row" id="addDetail" name="addDetail" onclick="addRowDebitGrid('debitGrid');" >
					      	</td>
					      	</tr>
<tr>
<td class=labelcell colspan=10 align=right> </td>
<td class=labelcell  align=right><div  align=right>Debit Total</div> </td>


<td class=fieldcell>
<input name=debitTotal id=debitTotal class=fieldinput-right onchange="getNetpayable();" readonly >
</td>
<td>
&nbsp;
</td></tr>
</table>
<table>
<tr >  
<td colspan=6  class=labelCell2>Deductions</td><td></td></tr>
</table>
<table width="100%" align=center border="1" cellpadding="0" cellspacing="0" id="creditGrid" >
                                      <tr >
                                     	<td class="thStlyle-small" width="2%">&nbsp;&nbsp;</td>
                                        <td class="thStlyle-small"><div align="center"> <bean:message key="worksbill.functionName"/>Function Name</div></td>
	<td width="9%" height="34" class="thStlyle-small"><div align="center" ><bean:message key="worksbill.type"/>Type</div></td>                                       
 <td class="thStlyle-small" width="18%"><div align="center"><bean:message key="worksbill.accountCode"/>Account&nbsp;Code</div></td>
                                        <td class="thStlyle-small" width="30%"><div align="center"><bean:message key="worksbill.accountHead"/>Account Head</div></td>
                                        
                                        <td class="thStlyle-small" width="15%"><div align="center"><bean:message key="worksbill.CrAmt"/>
											Credit Amount</div></td>
                                       
                                        <td class="thStlyle-small" width="1%"><div align="center"><bean:message key="worksbill.details"/>&nbsp;</div></td>
                                     </tr>
<%
System.out.println("==========worksbill.ded_chartOfAccounts_glCode  :"+ worksbill.ded_chartOfAccounts_glCode);
if(worksbill.ded_chartOfAccounts_glCode!=null )
{
	System.out.println("worksbill.ded_chartOfAccounts_glCode.length =="+ worksbill.ded_chartOfAccounts_glCode.length);

for(int i=0;i<worksbill.ded_chartOfAccounts_glCode.length;i++)
{
if(worksbill.ded_chartOfAccounts_glCode[i]!=null)
{
%>

                        <tr>
                        <td class="tdStlyle" onClick="changeColor(this,'creditGrid');" name="selectTd" id="selectTd" width="2%" height="23"></td>
                        <td class=smallfieldcell height="23" >
                        <ht:hidden property="ded_cv_fromFunctionCodeId" value="<%=worksbill.ded_cv_fromFunctionCodeId[i]%>" />
                        <ht:text styleClass="fieldinput:left"  value ="<%=worksbill.ded_function_code[i]%>" property="ded_function_code" style="WIDTH: 160px"    onkeyup="autocompletecodeFunction(this);" 
                         onblur="fillNeibrAfterSplitFunction(this,'ded_cv_fromFunctionCodeId','ded_chartOfAccounts_glCode');funClear(this,'creditGrid')" /><IMG   id=IMG1 onclick=openSearch1(this,'ded_cv_fromFunctionCodeId','ded_') src="../images/plus1.gif"   >
			</td>
			<td class="smallfieldcell" height="23">
			<% System.out.println("TDS VALUE FOR ----"+i+" ttttt   "+worksbill.tds_code[i]); %>
			
			<ht:select styleClass="combowidth"   property="tds_code" value="<%=worksbill.tds_code[i]%>"  onchange="getTds(this),colAllTotalSingleColCredit('creditAmount');" >
			<ht:option value="0">Select</ht:option>
			<c:forEach var="tds" items="${tdsList}" >
			<ht:option value="${tds.id}`~`${tds.chartofaccounts.glcode}">${tds.type}</ht:option>
			</c:forEach> 
			</ht:select>
			</td>

                         <td class=smallfieldcell height="23" >
			<ht:text styleClass="fieldinput-right" property="ded_chartOfAccounts_glCode" value="<%=worksbill.ded_chartOfAccounts_glCode[i]%>" onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'ded_chartOfAccounts_name','creditAmount'),validateAcrossTds(this)" maxlength="20" style="WIDTH: 65px" />
			<IMG id=IMG2   onclick="openSearch(this,'ded_chartOfAccounts','ded_')" src="../images/plus1.gif"  >
                         </td>
                         <td class="fieldcelldesc" height="23" >
			<ht:text styleClass="fieldinputlarge" property="ded_chartOfAccounts_name" value="<%=worksbill.ded_chartOfAccounts_name[i]%>" maxlength="50" readonly="true" tabindex="-1" />
                         </td>
                       
                         <td class=fieldcell height="23" >
			<ht:text styleClass="fieldinput-right" property="creditAmount" value="<%=worksbill.creditAmount[i]%>" onblur="colAllTotalSingleColCredit('creditAmount');getNetamt();" maxlength="16"  onkeydown ="addNewRow('creditGrid','ded_chartOfAccounts_glCode')"/>
                         </td>
                       
                         </tr>
                        <%
                        }//inner If
                        }//for
                        }//if
                        else
                       {
                       %>
                      <tr>
                        <td class="tdStlyle" onClick="changeColor(this,'creditGrid');" name="selectTd"  width="2%"></td>
                        <td class=smallfieldcell  >
                        <ht:hidden property="ded_cv_fromFunctionCodeId" value="" />
                        <ht:text styleClass="fieldinput:left "  value ="" property="ded_function_code"  style="WIDTH: 160px"   onkeyup="autocompletecodeFunction(this);" 
                         onblur="fillNeibrAfterSplitFunction(this,'ded_cv_fromFunctionCodeId','ded_chartOfAccounts_glCode');funClear(this,'creditGrid')" /><IMG   id=IMG1 onclick=openSearch1(this,'ded_cv_fromFunctionCodeId','ded_') src="../images/plus1.gif"   >
			</td>
			<td class="smallfieldcell">
			
			<ht:select styleClass="combowidth"   property="tds_code"   onchange="getTds(this);getNetpayable();"    >
			<ht:option value="0">Select</ht:option>
			<c:forEach var="tds" items="${tdsList}" >
			
			<ht:option value="${tds.id}`~`${tds.chartofaccounts.glcode}">${tds.type}</ht:option>

			</c:forEach> 

			</ht:select>
									</td>

                         <td class=smallfieldcell  >
            <ht:hidden styleClass="fieldinput-right" property="ded_chartOfAccounts_glCodeId" value=""  />       
			<ht:text styleClass="fieldinput-right" property="ded_chartOfAccounts_glCode" value="" onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'ded_chartOfAccounts_name','creditAmount');validateAcrossTds(this)" maxlength="20" style="WIDTH: 65px" />
			<IMG  id=IMG2 onclick="openSearch(this,'ded_chartOfAccounts','ded_')" src="../images/plus1.gif"  >
                         </td>
                         <td class="fieldcelldesc" >
			<ht:text styleClass="fieldinputlarge" property="ded_chartOfAccounts_name" value="" maxlength="50"  readonly="true" tabindex="-1"  />
                         </td>
                       
                         <td class=fieldcell >
			<ht:text styleClass="fieldinput-right" property="creditAmount" value="" onblur="colAllTotalSingleColCredit('creditAmount');getNetpayable();" maxlength="16"  onkeydown ="addNewRow('creditGrid','ded_chartOfAccounts_glCode')"/>
                         </td>
                       
                        </tr>
  
                       
                   <%   
                    } 
                        
                    %>
                      </table>
                      <table width=100% > 
                       <tr>
							<td colspan="2" ><input type="button"  class="button" value="Delete Row" id="deleteDetail" name="deleteDetail" onclick=" deleteRowTable('creditGrid');colAllTotalSingleColCredit('creditAmount');" >
							<input type="button"  class="button" value="Add Row" id="addDetail" name="addDetail" onclick="addRow('creditGrid');" ></td>
					      	</tr>

                        <tr >
                        <td colspan=4 height="23"> </td>
<td class=labelcell  align=right height="23" ><div align=right >Total Deductions </div> </td>

<td class=fieldcell height="23">
<input name=creditTotal id=creditTotal class=fieldinput-right onChange="getNetpayable();" readonly value="0">
</td>
<td>
&nbsp;</td>
</tr>
<tr>
<td height=23></td>
</tr>
</table>

<table width=100%>
<tr>
<td >
</td>

</table>




                        
                         <table width="100%" align=center border="1" cellpadding="0" cellspacing="0" ID="totalGrid"  >

                                      <tr >
                                     	<td   width="2%">&nbsp;&nbsp;</td>
                                        <td   width="32%" ><divalign="center"><!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; --> </div></td>
                                        <td class="thStlyle-small" width="18%"><div align="center"><bean:message key="worksbill.accountCode"/>Account&nbsp;Code</div></td>
                                        <td class="thStlyle-small" width="30%"><div align="center"><bean:message key="worksbill.accountHead"/>Account Head</div></td>
                                        
                                        <td class="thStlyle-small" width="15%"><div align="center"><bean:message key="worksbill.CrAmt"/>
											Total Amount</div></td>
                                       
                                        <td class="thStlyle-small" width="1%"><div align="center"><bean:message key="worksbill.details"/>&nbsp;</div></td>
                                     </tr>

                        <tr>
                        <td   width="2%"></td>
                        <td class=fieldcelldesc width="32%"  >
                        <!--<ht:text property="net_cv_fromFunctionCodeId" value="<%=worksbill.net_cv_fromFunctionCodeId%>" disabled="true"/> 
                       
                      <IMG   id=IMG1 onclick=openSearch1(this,'function') src="../images/plus1.gif"  disabled >  -->
						</td>
                         <td class=smallfieldcell width="18%">
						<ht:select styleClass="fieldinput" property="net_chartOfAccounts_glCode"  value="<%=worksbill.net_chartOfAccounts_glCode%>" onchange="fillNetPayNameAfterSplit()">
				<%
				List npl=(List)request.getAttribute("netPayList");
				System.out.println("npl-----------------------------------------------"+npl);
				 %>	
				<c:forEach var="coa" items="${netPayList}" > 
					<ht:option value="${coa.id}~${coa.glcode}~${coa.name}">${coa.glcode}</ht:option>
					</c:forEach>
						</ht:select>
							
							 
                         </td>
                         <td class="fieldcelldesc" width="30%">
							<input styleClass="fieldinputlarge" name="net_chartOfAccounts_name" value="<%=worksbill.net_chartOfAccounts_name%>" maxlength="50"  >
                         </td>
                       
                         <td class=fieldcell width="15%">
							<ht:text styleClass="fieldinput-right" property="net_Amount" value="<%=worksbill.net_Amount%>" />
                         </td>
                       
                        </tr>
						

                                       </table>

                                      <table>
									  <tr>
						<td class="labelcell"><SPAN class="leadon">*</SPAN> - Mandatory Fields</td>
						</tr>
						</table>
                                       
                                       <table align=center>
                                       <tr>
                                       <td witdth=100% hieght=23>
                                       </td>
                                       </tr>
                                       <tr name="submitGrid2" id="submitGrid2"  >
						<td align="right">
						<input type=button class=button id=savenNew onclick=ButtonPress('savenew') href="#" value="Save & New" ></td>
						<td align="right">
						&nbsp;</td>                                         
						<td align="right"><input type=button class=button id=savenClose onclick=ButtonPress('saveclose') href="#" value="Save & Close"></td>
						<td align="right">
						<input type=button class=button  onclick=onClickCancel(); href="#" value="Cancel" ></td>
						<td align="right">
						<input type=button  class=button onclick=window.close() href="#" value="Close"></td>
						<td align="right">
						&nbsp;</td> 
						</tr>
						</table>
                         <p>&nbsp;</p>
                         <table border="0" cellpadding="0" cellspacing="0" id="glButtons" align=center style="Display: none">
						<tr ><td align="center">
						<input type=button class=button id="approve" onclick="ButtonPress('approve')"  href="#" value="Modofy & Approve"></td>
						<td align="center">
						<input type=button class=button id="glentry" onclick="showEntry();"  href="#" value="Show GlEntry"></td>

						</tr>
						
						</table>

					
                         </table>
                         <p>&nbsp;</p>
                         <table border="0" cellpadding="0" cellspacing="0" id="backbuttons" align=center style="Display: none">
						<tr ><td align="center">
						<input type=button class=button id="backbutton" onclick="back(window.self)"  href="#" value="Back"></td>
						</tr>
						</table>
						<table width="80%" border="1" cellpadding="0" cellspacing="0" align=center id="entries" name="entries" style="Display: none">
						<tr class="tableheader">
							<td class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.codeType"/><br> Code Type</div></td>
							<td  class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.accountCode"/><br> Account Code</div></td>
							<td  class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.accountHead"/><br> Account Head</div></td>
							<td  class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.debit"/><br> Debit </div></td>
							<td  class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.credit"/><br> Credit</div></td>
							<td  class="thStlyle"><div align="center" valign="center" ><bean:message key="worksbill.narration"/><br> Narration</div></td>
						</tr>
						<tr class="tdStlyle" align=center>
							<td ><div name="display_CodeType"  id="display_CodeType"  readOnly maxLength="10" >&nbsp;</div></td>
							<td ><div name="display_Code"  id="display_Code"   readOnly maxLength="10" >&nbsp;</div></td>
							<td ><div name="display_Head"  id="display_Head"  readOnly maxLength="10" >&nbsp;</div></td>
							<td ><div name="display_Debit"  id="display_Debit" style="text-align:right"  readOnly maxLength="16" >&nbsp;</div></td>
							<td ><div name="display_Credit"  id="display_Credit" style="text-align:right"  readOnly maxLength="16" >&nbsp;</div></td>
							<td ><div name="display_Narration" id="display_Narration"  readOnly maxLength="10" >&nbsp;</div></td>
						</tr>
						</table>



<div id="codescontainer" name="codescontainer" ></div>	

</td></tr>
</table>
<ht:javascript formName="WorksBillForm"/> 
</ht:form>


</body>

</html>
