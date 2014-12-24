<%@ include file="/includes/taglibs.jsp" %>



<%@ page import="java.util.*,
				 java.text.SimpleDateFormat,
				 org.egov.payroll.model.*,
				 java.math.*,
				 org.egov.payroll.services.payslip.*,
				 org.egov.payroll.utils.PayrollManagersUtill,
				 org.egov.infstr.commons.*,
				 org.egov.payroll.model.IncrementSlabsForPayScale,
				 org.egov.payroll.model.PayScaleHeader,
				 org.egov.payroll.client.payslip.SlabsComparator,org.egov.infstr.utils.EgovMasterDataCaching" %>
<html>

<head>


	<title>Payscale Modify</title>
	
<script language="JavaScript"  type="text/JavaScript">
<% PayScaleHeader payHeader = (PayScaleHeader) session.getAttribute("payHeader");

%>

function onBodyLoad()
 {
	<%
		PayRollService manualGenManager = PayrollManagersUtill.getPayRollService();
		ArrayList salaryCodes = (ArrayList)manualGenManager.getOrderedSalaryCodes();
		String check = (String)session.getAttribute("chekPayscale");
		Collection c=(Collection)payHeader.getIncrSlabs();
		SortedSet tempset=new TreeSet(new SlabsComparator());
		for(Iterator iter = payHeader.getIncrSlabs().iterator(); iter.hasNext();)
		{
   		IncrementSlabsForPayScale incDetails = (IncrementSlabsForPayScale)iter.next();
   		tempset.add(incDetails);
   		}
		if("exist".equals(check)){
	%>		
		document.salaryPaySlipForm.payScaleName.readOnly=true;
		document.salaryPaySlipForm.payCommision.readOnly=true;
		document.salaryPaySlipForm.effectiveFrom.readOnly=true;		
		document.salaryPaySlipForm.amountFrom.readOnly=true;		
		
		var table = document.getElementById("paytable");	
		
		var len = table.rows.length;
		if(len == 2){
			document.salaryPaySlipForm.payHead.disabled=true;
		}
		else{
			
			var length = document.salaryPaySlipForm.payHead.length;
			for(var i=0; i<length; i++){
				document.salaryPaySlipForm.payHead[i].disabled=true;				
			}		
		}
	<%	
		}
	%>
	
	//To reset the values in increment rows if rulescript is selected when onload.
	enableOrDisableIncrementRows(document.getElementById("ruleScript"),'onload');
	
 }
/* if will invoke addRowToTable() method based on key pressed */
function whichButtonEarnings(tbl,obj,objr){
	
	if( objr=="increments" && document.getElementById("ruleScript").value=="" && checkIncrements(obj,"addrow") ){
          addRowToTable(tbl,obj); 
    }  	
	
	if(objr=="earnings" && checkPayHead() ){	   	
	   	 if("exist" != "${chekPayscale}")
		   	 addRowToTable(tbl,obj);
	}

}
function deleteRow(table,obj)
{

	var tbl = document.getElementById(table);
	var rowNumber=getRow(obj).rowIndex;
		
	if(table=='paytable')
	{
		
		if("exist" != "${chekPayscale}")
		{
			if(${fn:length(salaryPaySlipForm.payHead)}<(eval(rowNumber)-1))
			   tbl.deleteRow(rowNumber);
			else
			{
			     alert("You cannot delete this row");
			     return false;
			}
		}
		collectionSum();
	}
	if(table=='incrementdetails' && document.getElementById("ruleScript").value=="")
	{	
		//alert("inside");
		var incslabslength=<%=payHeader.getIncrSlabs().size() %>
		//alert(incslabslength);
		if(rowNumber == 1 )
		{
			alert("You cannot delete this row");
		     return false;
		}
		else
		{		
		 tbl.deleteRow(rowNumber);
		}
	}



}
function addRowToTable(tbl,obj)
{
  tableObj=document.getElementById(tbl);
  var rowObj1=getRow(obj);
  var tbody=tableObj.tBodies[0];
  var lastRow = tableObj.rows.length;
   if(tbl=='paytable' && lastRow<getControlInBranch(tableObj.rows[rowObj1.rowIndex],'payHead').options.length)
  {
  	   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
       tbody.appendChild(rowObj);
  	   var remlen1=document.salaryPaySlipForm.payHeadAmount.length;
 	   document.salaryPaySlipForm.pct[remlen1-1].value="";
 	   document.salaryPaySlipForm.payHeadAmount[remlen1-1].value="";
 	   document.salaryPaySlipForm.calculationType[remlen1-1].value="";
 	   document.salaryPaySlipForm.pctBasis[remlen1-1].value="";
	   //document.salaryPaySlipForm.yearToDate[remlen1-1].value="";
  }
  else
  {
	  if(tbl=='paytable')
	  {
	    alert("No pay Heads Available to insert");
	    return false;
	  }
  } 
  if(tbl=='incrementdetails')  
    {
            var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
             tbody.appendChild(rowObj);
             var rownum=rowObj1.rowIndex+1;  
             getControlInBranch(tableObj.rows[rownum],'incSlabFrmAmt').value="";
             getControlInBranch(tableObj.rows[rownum],'incSlabToAmt').value="";
             getControlInBranch(tableObj.rows[rownum],'incSlabAmt').value="";          
      	   
  }

}
function checkPayHead()
{
	var tbl = document.getElementById('paytable');
	var rCount=tbl.rows.length-2;
	if(tbl.rows.length == 2)
  	{
		if(document.salaryPaySlipForm.payHead.options[document.salaryPaySlipForm.payHead.selectedIndex].value =="0")
		{
			alert("Please select the payHead!!!");
			document.salaryPaySlipForm.payHead.focus();
			return false;
		}

		if(document.salaryPaySlipForm.payHeadAmount.value=="")
		{
			alert("Please Enter the Amount for the Selected PayHead!!!");
			document.salaryPaySlipForm.payHeadAmount.value="";
			document.salaryPaySlipForm.payHeadAmount.focus();
			return false;
		}
		if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
		{
			alert("Please Enter the Percentage for the Selected PayHead!!!");
			document.salaryPaySlipForm.pct.value="";
			document.salaryPaySlipForm.pct.focus();
			return false;
		}
	}
	else
	{
		if(tbl.rows.length>2)
		{
			if(document.salaryPaySlipForm.calculationType[rCount-1].value !="SlabBased"){
				if(document.salaryPaySlipForm.payHead[rCount-1].options[document.salaryPaySlipForm.payHead[rCount-1].selectedIndex].value =="0")
				{
					alert("Please select payHead!!!");
					document.salaryPaySlipForm.payHead[rCount-1].focus();
					return false;
				 }
				 if(document.salaryPaySlipForm.payHeadAmount[rCount-1].value=="" && document.salaryPaySlipForm.calculationType[rCount-1].value !="RuleBased")
				{
					alert("Please Enter the Amount for the Selected PayHead!!!");
					document.salaryPaySlipForm.payHeadAmount[rCount-1].value="";
					document.salaryPaySlipForm.payHeadAmount[rCount-1].focus();
					return false;
				}
				if(document.salaryPaySlipForm.pct[rCount-1].value=="" && document.salaryPaySlipForm.calculationType[rCount-1].value=="ComputedValue")
				{
					alert("Please Enter the Percentage for the Selected PayHead!!!");
					document.salaryPaySlipForm.pct[rCount-1].value="";
					document.salaryPaySlipForm.pct[rCount-1].focus();
					return false;
				}
			}
		}
	 }
	 return true;
 }
 /**
  *   this API will check the increments ranges
  * 
  **/
 function checkIncrements(obj,type)
 {
 	 if(document.getElementById("ruleScript").value=="")//it means no rulescript selected
 	 {
	 	 var rownum = 0 ;
	 	 var table = document.getElementById("incrementdetails");
	 	 rownum = table.rows.length - 1;
	  	 for(i=1;i<=rownum;i++)
	 	 {
	 		var payscaleAmtFrm=document.getElementById("amountFrom").value;
	 		var payscaleAmtTo=document.getElementById("amountTo").value;
	 		var incSlabFrmAmt=getControlInBranch(table.rows[i],'incSlabFrmAmt');
	 		var incSlabToAmt=getControlInBranch(table.rows[i],'incSlabToAmt');
	 		var incSlabAmt=getControlInBranch(table.rows[i],'incSlabAmt');
	 		if(incSlabFrmAmt.value=="" || eval(incSlabFrmAmt.value)<=0)
	 		{
	 			   alert("Please Enter the Amount From");	
	 			   incSlabFrmAmt.focus();
	 			   return false;
	 		}
	 		if(i==1)
	 		{
	 			if(eval(payscaleAmtFrm)!= eval(incSlabFrmAmt.value))
	 			{
	 				 alert("Amount From should be equal to Pay Scale Amount From");		 
	 				 incSlabFrmAmt.focus();
	 				 return false;
	 			}
	 		}
	 		else	if(i>1)
	 		{
	 			var rowtemp=(i-1);
	 			var prevToValue=getControlInBranch(table.rows[rowtemp],'incSlabToAmt');
	 			if((eval(prevToValue.value)+1)!= eval(incSlabFrmAmt.value))
	 			{
	 			 alert("Please Enter the correct Range Values");		 
	 			 incSlabFrmAmt.focus();
	 			 return false;
	 			}
	 		}
	 		if(incSlabToAmt.value=="" || eval(incSlabToAmt.value)<=0)
	 		{
	 		   alert("Please Enter Amount To");	
	 		   incSlabToAmt.focus();
	 		   return false;
	 		}
	 		if(eval(incSlabToAmt.value)<=eval(incSlabFrmAmt.value))
	 		{
	 		   alert("Amount To should be greater than 'Amount From'");	
	 		   incSlabToAmt.focus();
	 		   return false;
	 		}
	 
	 		 if(payscaleAmtTo<eval(incSlabToAmt.value))
	 		 {
	 			 alert("Amount To Should be less than or equal to Payscale Amount to ");
	 			 document.getElementById("amountTo").focus();
	 			 return false;
	 		 }
	 		 if(incSlabAmt.value == "" || eval(incSlabAmt.value)<0 )
	 		 {
	 		    alert("Please Enter Increment Amount");	
	 		    incSlabAmt.focus();
	 		    return false;
	 
	 		 }
	 	 }
	 }
	 else
	 {
	 	return false;
	 }
 	 return true;
}

function enableOrDisableIncrementRows(obj,when)
{
	var rownum = 0 ;
	var table = document.getElementById("incrementdetails");
	rownum = table.rows.length - 1;
	var isDisable=false;
	
	if(obj.value=="")//Value of rulescript
	{
		isDisable=false;
	}
	else
	{
		isDisable=true;
	}

	for(i=rownum;i>=1;i--)
	{
		if(i==1)
		{
			var incSlabFrmAmt=getControlInBranch(table.rows[i],'incSlabFrmAmt');
			var incSlabToAmt=getControlInBranch(table.rows[i],'incSlabToAmt');
			var incSlabAmt=getControlInBranch(table.rows[i],'incSlabAmt');
			var incSlabAddBut=getControlInBranch(table.rows[i],'addBut');
			var incSlabDelBut=getControlInBranch(table.rows[i],'delBut');
			
			if(when=='onchange')
			{
				incSlabFrmAmt.value = "";
				incSlabToAmt.value = "";
				incSlabAmt.value = "";
			}
			
			incSlabFrmAmt.disabled = isDisable;
			incSlabToAmt.disabled = isDisable;
			incSlabAmt.disabled = isDisable;
			incSlabAddBut.disabled = isDisable;		
			incSlabDelBut.disabled = isDisable;	
		}
		else
		{
			if(when=='onchange')
			{
				table.deleteRow(i);	
			}		
		}
	 }
}

 function checkPctBasis(obj)
  {
 	 var tbl = document.getElementById('paytable').rows.length;
 	 var rowObj=getRow(obj);
     var pctBasisId;
     var table= document.getElementById("paytable");
     
     getControlInBranch(table.rows[rowObj.rowIndex],'pct').value="";
	 getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value="";
	 //getControlInBranch(table.rows[rowObj.rowIndex],'yearToDate').value="";	 
	 <c:forEach var="payHeadObj" items="${salaryCodes}">		  
	  if(getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value=="${payHeadObj.id}"){
		<c:if test="${payHeadObj.calType=='ComputedValue'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=true;
			pctBasisId = "${payHeadObj.salaryCodes.id}"
			var result=funPctBasis(pctBasisId);
			if(result!=false){
				getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value =result;
			}
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=false;
		</c:if>
		<c:if test="${payHeadObj.calType=='MonthlyFlatRate'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value = "";
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=true;
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=false;
		</c:if>
		<c:if test="${payHeadObj.calType=='SlabBased'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value = "";
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=true;
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=false;
		</c:if>
		<c:if test="${payHeadObj.calType=='RuleBased'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=true;
			getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value = "";
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=true;
		</c:if>
	  }
	</c:forEach>
	collectionSum();
}
 function calAmount(obj)
 {
	 checkdecimalval(obj,obj.value);
	 var sum1;
	 var pctBasisText;
	 var rowObj=getRow(obj);
	 var table= document.getElementById("paytable");
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly==false)
	 {
		 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'pct').value>100)
		 {

			 alert("Please enter the percentage between 0-100!!!");
			 getControlInBranch(table.rows[rowObj.rowIndex],'pct').focus();
			 return false;
		 }
		 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'pct').value>0)
		 {
			  sum1 = getControlInBranch(table.rows[rowObj.rowIndex],'pct').value/100;
			  pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value
		  }
		  else
		  {
			   sum1=0;
			   pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value
	   	  }
		  var remlen1=document.salaryPaySlipForm.pctBasis.length;
	 for(var i=0;i<remlen1-1;i++)
	 {
		if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].text==pctBasisText)
		 {
			 sum1= sum1*document.salaryPaySlipForm.payHeadAmount[i].value;
			 getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value =Math.round(sum1);

	  	 }
	 }
	 collectionSum();

	}

 }
 function funPctBasis(pctBasisId)
 {
 	var result="";
 	<c:forEach var="payHeadObj" items="${salaryCodes}" >
 	if(pctBasisId=="${payHeadObj.id}")
 	{
		result = "${payHeadObj.head}";
	}
  </c:forEach>
  if(result=="")
  {
	  return false
  }
  return result;
}

function checkDuplicate(obj)
{
	 var rowObj=getRow(obj);
	 var hit=0;
	 var caught=0;
	 var table= document.getElementById("paytable");
	 var tbl = document.getElementById('paytable').rows.length;
	 var pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value;
	 if(tbl>2)
	{
		for(var i=0;i<tbl;i++)
		{
			for(var j=i+1;j<tbl-1;j++)
			{

				if(i!=j)
				{
					if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].text==document.salaryPaySlipForm.payHead[j].options[document.salaryPaySlipForm.payHead[j].selectedIndex].text)
					{
						alert("Duplicate Selection of Pay Head!!!");
						document.salaryPaySlipForm.payHead[j].focus();
						return false;
					 }
				}
			}
		 }
 	 }
}
function removeZero(obj)
{
	if(obj.value==0)
	{

		obj.value="";
		obj.focus();

	}
		return true;
}

function addZero(obj)
{
	if(obj.value=="")
	{
		obj.value=0;
	}
	collectionSum();
	calOnchangeAmount(obj);
	return;
}
function checkdecimalval(obj,amount)
{
    var objt = obj;
    var amt = amount;
    if(amt != null && amt != "")
    {
      if(amt < 0 )
        {
            alert("Please enter positive value for the amount");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            alert("Please enter a numeric value for the amount");
            objt.focus();
            return false;

        }
           objt.value= Math.round(amt);
           collectionSum();
           calOnchangeAmount(objt);

    }
}
function calOnchangeAmount(objt)
{

	var sum1;
	var rowObj=getRow(objt);
	var payText;
	var table= document.getElementById("paytable");
	if(getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly==false)
	 {
		if(getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value>0)
		 {
			  var payHeadValue= getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value;
			  <c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
			  if(payHeadValue=="${payHeadObj.id}")
			  {
					payText="${payHeadObj.head}"

			  }
			  </c:forEach>
			  sum1 = getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value

		  }
		  else
		  {

			  var payHeadValue= getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value;
			  <c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
			  if(payHeadValue=="${payHeadObj.id}")
			  {
					payText="${payHeadObj.head}"

			  }
			  </c:forEach>
			  sum1=0;
		  }
	 var remlen1=document.salaryPaySlipForm.pctBasis.length;
	 for(var i=0;i<remlen1;i++)
	 {
		var sum=0;
		if(document.salaryPaySlipForm.pctBasis[i].value==payText && sum1!=0)
		 {
			  sum= sum1*(document.salaryPaySlipForm.pct[i].value/100);
			  document.salaryPaySlipForm.payHeadAmount[i].value =Math.round(sum);

		 }
		else
		 {
			 if(document.salaryPaySlipForm.pctBasis[i].value==payText && sum1==0)
			 {
				 document.salaryPaySlipForm.payHeadAmount[i].value =sum1;
			 }
		 }
	}
	collectionSum();

   }
 }

function collectionSum()
{
	 var sum=0;
	if(document.salaryPaySlipForm.payHeadAmount.length>0)
  {
	 for(var i=0;i<document.salaryPaySlipForm.payHeadAmount.length;i++)
	 {
		if(document.salaryPaySlipForm.payHeadAmount[i].value!="")
		{
			sum = sum+ eval(document.salaryPaySlipForm.payHeadAmount[i].value);
		}
		else
		{
			document.salaryPaySlipForm.payHeadAmount[i].value=0;
		}
	 }
   }
   else
   {
	   if(document.salaryPaySlipForm.payHeadAmount.value!="")
	   {
		   sum = sum+ eval(document.salaryPaySlipForm.payHeadAmount.value);
	   }
	   else
	   {
		   document.salaryPaySlipForm.payHeadAmount.value=0;
	   }
   }
   document.salaryPaySlipForm.grossPay.value=Math.round(sum);

	return true;
}

function getRow(obj)
 {
 	if(!obj)return null;
 	tag = obj.nodeName.toUpperCase();
 	while(tag != 'BODY'){
 		if (tag == 'TR') return obj;
 		obj=obj.parentNode;
 		tag = obj.nodeName.toUpperCase();
 	}
 	return null;
 }


function getControlInBranch(obj,controlName)
{
	if (!obj || !(obj.getAttribute)) return null;
	if (obj.getAttribute('name') == controlName) return obj;

	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}
function checkNotSpecial(obj)
{
 var iChars = "!@$%^*+=[']\\';{}|\":<>?1234567890";
for (var i = 0; i < obj.value.length; i++)
{

 if (iChars.indexOf(obj.value.charAt(i)) != -1)
 {
		alert("The Field has special characters and Numbers. \n          These are not allowed.\n" );
		obj.value="";
		obj.focus();
		return false;
  }
 }
 return true;
}
 function validation(arg)
 {
	 if(document.salaryPaySlipForm.modifyRemarks.value=="")
	 {
		 alert("Please Enter Remarks");
		 document.salaryPaySlipForm.modifyRemarks.focus();
		 return false;
	 }
	 /*if(document.getElementById("payCommision").value=="")
	 {
		 alert("Please select the Pay commision");
		 document.getElementById("payCommision").focus();
		 return false;
	 }*/
	 if(document.getElementById("payScaleName").value=="")
	 {
		 alert("Please Enter the Pay Scale Name");
		 document.getElementById("payScaleName").focus();
		 return false;
	 }
	 if(document.getElementById("payScaleName").value!="${payHeader.name}")
	 {
	 	if(!uniqueCheckingBoolean('<%=request.getContextPath()%>/commonyui/egov/uniqueCheckAjax.jsp', 'EGPAY_PAYSCALE_HEADER', 'NAME', 'payScaleName', 'no', 'no'))
	 	{
			alert("The Pay Scale Name already exists !!!");
			document.getElementById("payScaleName").value="";
			document.getElementById("payScaleName").focus();
			return false;
	 	}
 	}

 	
	/* if(document.getElementById("type").value=="-1")
	 {
 		 alert("Please choose the type");
 		 document.getElementById("type").focus();
 		 return false;
	 }*/
 	
	 if(document.getElementById("effectiveFrom").value=="")
	 {
		 alert("Please Enter the Effective From Date");
		 document.getElementById("effectiveFrom").focus();
		 return false;
	 }
	 if(document.getElementById("amountFrom").value=="")
	 {
		 alert("Please Enter the To Amount");
		 document.getElementById("amountFrom").focus();
		 return false;
	 }
	 if(document.getElementById("amountTo").value=="")
	 {
		 alert("Please Enter the To Amount");
		 document.getElementById("amountTo").focus();
		 return false;
	 }

	 if(document.getElementById("amountFrom").value!="" && document.getElementById("amountTo").value!="")
	 {
		 if(eval(document.getElementById("amountFrom").value)>eval(document.getElementById("amountTo").value))
		 {
		    alert("Please Enter the To Amount greater than From Amount");
		    document.getElementById("amountTo").focus();
		    return false;
	     }
	     if(document.salaryPaySlipForm.grossPay.value!=0 && document.salaryPaySlipForm.grossPay.value!="")
	     {
			 if((eval(document.getElementById("payHeadAmount").value)<eval(document.getElementById("amountFrom").value)) ||
			 (eval(document.getElementById("payHeadAmount").value)>eval(document.getElementById("amountTo").value)))
			 {
				 alert("Basic pay amount should be with in the \n  range of pay scale from and to amount!!!");
				 return false;

			 }
		 }
	 }
	 if(document.salaryPaySlipForm.payHeadAmount.length>0)
	   {
	 	 for(var i=0;i<document.salaryPaySlipForm.payHeadAmount.length;i++)
	 	 {
	 		if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].value =="0")
			{
				alert("Please Select the PayHead!!!");
				document.salaryPaySlipForm.payHead[i].focus();
				return false;
			}
	 		if(document.salaryPaySlipForm.pct[i].value=="" && document.salaryPaySlipForm.calculationType[i].value=="ComputedValue")
			{
				alert("Please Enter the Percentage for the Selected PayHead!!!");
				document.salaryPaySlipForm.pct[i].focus();
				return false;
			}
	 	  }
	    }
	    else
	    {
	 	   if(document.salaryPaySlipForm.payHead.options[document.salaryPaySlipForm.payHead.selectedIndex].value =="0")
			{
				alert("Please Select the PayHead!!!");
				document.salaryPaySlipForm.payHead.focus();
				return false;
			}
	 	   if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
			{
				alert("Please Enter the Percentage for the Selected PayHead!!!");
				document.salaryPaySlipForm.pct.focus();
				return false;
			}

	   }
	   		var remlen1=document.salaryPaySlipForm.pctBasis.length;
			var payBasisText="";
			if(remlen1>0)
			{
				//alert("greate then zero");
				for(var j=0;j<remlen1;j++)
				{
					//alert("drill 11");
					if(document.salaryPaySlipForm.calculationType[j].value=="ComputedValue" && document.salaryPaySlipForm.pctBasis[j].value!="")
					{

						payBasisText =payBasisText+","+document.salaryPaySlipForm.pctBasis[j].value;
						//alert("payBasisText  "+payBasisText);

					}
				}
			   var d1 = payBasisText;
				d1 = d1.split(',');
				var hit=0;
				 for(var i=0;i<d1.length;i++)
				{
					for(var j=0;j<document.salaryPaySlipForm.payHead.length;j++)
						{
							if(d1[i]==document.salaryPaySlipForm.payHead[j].options[document.salaryPaySlipForm.payHead[j].selectedIndex].text
							&& d1[i]!="" )
							{
								hit=1;
							}
						}

				}
				if(hit==0 && payBasisText!="")
				{
					alert("Please the select PctBasis as above");
					return false;
				}
		  }
		  else
		  {
			  if(document.salaryPaySlipForm.pctBasis.value!="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
			  {
					alert("Please the select flat value type first");
					document.salaryPaySlipForm.payHead.focus();
					return false;
			  }
		}
		if(document.salaryPaySlipForm.grossPay.value==0)
		{
			alert("Please Enter the Amount greater than Zero!!!");
			return false;
		}
	 	if(arg=="save")
	   	{
	   	 	document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/modifyGenPayScale.do?mode=modify&id=${payHeader.id}";
	   	}
	   	else if(arg=="saveNew")
	   	{
	   		document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/BeforeviewGenPaySlips.do?path=back";
	   	}
	   	var table = document.getElementById("paytable");	
		
		var len = table.rows.length;
		if(len == 2){
			document.salaryPaySlipForm.payHead.disabled=false;
		}
		else{
			//alert("sasas");
			var length = document.salaryPaySlipForm.payHead.length;
			for(var i=0; i<length; i++){
				document.salaryPaySlipForm.payHead[i].disabled=false;				
			}		
		}   	
    	
    	if(document.getElementById("ruleScript").value=="")
	   	{
	    	if(checkIncrements(null,"formsubmit"))
	    	{
    			var table = document.getElementById("incrementdetails");
    			var payscaleAmtTo=document.getElementById("amountTo").value;
    			var rowtemp=table.rows.length-1;
    			var incSlabToAmt=getControlInBranch(table.rows[rowtemp],'incSlabToAmt');
    			if(payscaleAmtTo!=eval(incSlabToAmt.value))
    			{
    				alert("Amount To Should be equal to Payscale Amount to ");
    				document.getElementById("amountTo").focus();
    				return false;
    			}
	   		 }
	   		 else
	   		 {
	   		   	return false;
	   		 }
	   	}

    	document.salaryPaySlipForm.submit();
  }

</script>
</head>


<body onLoad="onBodyLoad();">

   <html:form method="POST" action="/payslip/generatePaySlips" >
  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
	<tr>
    	<td colspan="4" class="headingwk">
    		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
            <div class="headplacer">Modify Pay Scale</div></td>
    	</tr>
	<tr>
	 	<td class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="PayscaleName"/></td>
	  	<td class="whitebox2wk" >
	  	<input type="text" class="selectwk" name="payScaleName" id ="payScaleName" value="<c:out value="${payHeader.name}" escapeXml="false"/>" autocomplete="off" onblur="trim(this,this.value)">
	  	</td>
	  	<td class="whiteboxwk" ><span class="mandatory">*</span><bean:message key="PayCommission"/> </td>
	  	<td class="whitebox2wk">
	  		
	  		<html:select property="payCommision" styleId="payCommision" styleClass="selectwk" value="${payHeader.payCommision.id}">
				<html:option value="">---------Choose---------</html:option>		
		    	<html:options collection="payFixedList" property="id" labelProperty="name" />
		    </html:select>	
	  	</td>
	  </tr>
 <tr>
    <td class="greyboxwk"><span class="mandatory">*</span>Amount From</td>
    <td class="greybox2wk">
    	<input type="text"  class="selectwk" name="amountFrom" id ="amountFrom" size="10" value="<c:out value="${payHeader.amountFrom}" escapeXml="false"/>" onblur="return checkfornumber(this,this.value);trim(this,this.value)">
     	&nbsp;<b>to</b>&nbsp;
    	<input type="text" class="selectwk" name="amountTo" id ="amountTo" size="10" value="<c:out value="${payHeader.amountTo}" escapeXml="false"/>" onblur="return checkfornumber(this,this.value);trim(this,this.value)">
    </td>
    <td class="greyboxwk"><span class="mandatory">*</span>Type</td>
    <td class="greybox2wk">
			<html:select property="type" styleId="type" styleClass="selectwk" value="${payHeader.type}">
			<html:option value="-1">---choose---</html:option>
			<html:option value="Ordinary">Ordinary</html:option>
			<html:option value="Selection">Selection</html:option>
                    <html:option value="Special">Special</html:option>    	
			</html:select>
  	</td>
  
  </tr>
  <tr>
    <td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="EffectiveFrom(dd/mm/yyyy)"/></td>
    <td class="whitebox2wk">
    	<input type="text" class="selectwk" name="effectiveFrom" id ="effectiveFrom" value="<fmt:formatDate value="${payHeader.effectiveFrom}" pattern="dd/MM/yyyy" />"  onblur="validateDateFormat(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
		<a href="javascript:show_calendar('forms[0].effectiveFrom');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
	  	</a>    	
    </td>
    <td class="whiteboxwk">Rule Script</td>
  	   	<td class="whitebox2wk">
  			<html:select property="ruleScript" styleId="ruleScript" value="${payHeader.ruleScript.id}" styleClass="selectwk" onchange="enableOrDisableIncrementRows(this,'onchange')">
	  			<html:option value="">---choose---</html:option>
	  			<html:options collection="wfActionList" property="id" labelProperty="description"/>
  			</html:select>
		</td>
   </tr>
   <tr>
  
    
    </tr>
 </table>
   <div class="ScrollAuto">                  
  <table  width="100%" cellpadding ="0" cellspacing ="0" border = "3" bordercolor="" id="paytable">
  <tr>
                    <td colspan="7" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                      <div class="headplacer">Earnings</div></td>
                  </tr>
  <tr >
      <td class="tablesubheadwk" width="12%"><bean:message key="PayHead"/> </td>
      <td class="tablesubheadwk" width="14%"><bean:message key="CalculationType"/></td>
      <td class="tablesubheadwk" width="12%"><bean:message key="%Amount"/></td>
      <td class="tablesubheadwk" width="14%"><bean:message key="%Basis"/></td>
      <td class="tablesubheadwk" width="12%"><bean:message key="Amount"/></td>
      <td class="tablesubheadwk" width="5%" >Add/Del</td>
   </tr>
<%BigDecimal grossTotalpay = new BigDecimal(0);
for(int i=0;i<salaryCodes.size();i++)
{
	SalaryCodes sal = (SalaryCodes)salaryCodes.get(i);

for(Iterator iter = payHeader.getPayscaleDetailses().iterator(); iter.hasNext();)
{
	PayScaleDetails payDetails = (PayScaleDetails)iter.next();
	if(sal.getId().equals(payDetails.getSalaryCodes().getId())){
	grossTotalpay = grossTotalpay.add(payDetails.getAmount());
		%>
  		<tr id="earnings" >
     	 <td class="whitebox3wk">
     	 	<html:select styleClass="selectwk" property="payHead" style="width:180px" 
     	 				onchange="checkPctBasis(this)" onblur="checkDuplicate(this);trim(this,this.value)">
     	    	<html:option value="0">Choose</html:option>
     	    <%
			for(int j=0;j<salaryCodes.size();j++)
			{
					SalaryCodes sal1 = (SalaryCodes)salaryCodes.get(j);
				if(sal1.getCategoryMaster().getCatType().equals("E"))
     	    			{%>
				   <option value="<%=sal1.getId()%>" <%=((sal1.getId().equals(payDetails.getSalaryCodes().getId()))? "selected":"")%>><%=sal1.getHead()%></option>
				<%}
  			}%>
     	    </html:select>
     	   </td>
     	 <td class="whitebox3wk">
     	 <html:text styleClass="selectwk" property="calculationType"  value="<%=payDetails.getSalaryCodes().getCalType()%>"  readonly="true"/>
		 </td>
	 <% if(payDetails.getSalaryCodes().getCalType().equals("ComputedValue"))
	 {%>
     	 <td class="whitebox3wk">
     	 <input type="text" class="amount" name="pct" id="pct" value="<%if(payDetails.getPct()!=null){%><%=payDetails.getPct().toString()%><%}else{%>""<%}%>" style="width:125px" onblur="calAmount(this);trim(this,this.value)" >
     	 </td>
     <%}
	  else
	 {%>
	 <td class="whitebox3wk">
	 <input type="text" class="amount" name="pct" id="pct" value="<%if(payDetails.getPct()!=null){%><%=payDetails.getPct().toString()%><%}else{%>""<%}%>" style="width:125px" onblur="calAmount(this);trim(this,this.value)" readonly>
	 </td>
	 <%}%>
     	<td class="whitebox3wk">
     	<input type="text" class="amount" name="pctBasis" id="pctBasis" value="<%if(payDetails.getSalaryCodes().getSalaryCodes()!=null){%><%=payDetails.getSalaryCodes().getSalaryCodes().getHead()%><%}else{%>""<%}%>" style="width:125px" readonly>
	 </td>
	 <% if(payDetails.getSalaryCodes().getCalType().equals("ComputedValue") ||  payDetails.getSalaryCodes().getCalType().equals("RuleBased"))
	 {%>
	 <td class="whitebox3wk"><html:text styleClass="amount" property="payHeadAmount" styleId="payHeadAmount" value="<%=payDetails.getAmount().toString()%>" onfocus="return removeZero(this)" onchange="return checkdecimalval(this,this.value)"  onblur="return addZero(this);trim(this,this.value)" readonly="true"/></td>
	 <%}
	 else
	 {%>
		<td class="whitebox3wk"><html:text styleClass="amount" property="payHeadAmount"  styleId="payHeadAmount" value="<%=payDetails.getAmount().toString()%>" onfocus="return removeZero(this)" onchange="return checkdecimalval(this,this.value)"  onblur="return addZero(this);trim(this,this.value)" /></td>
	 <%}%>
         <td class="whitebox3wk"  ><p>
	 		<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonEarnings('paytable',this,'earnings');" /></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('paytable',this);"/></a></div>
     	 </td>
     	 
     	 
     	 
     	 
     	<!-- <td class="labelcell">&nbsp;<input  type="checkbox" name="isTaxable" id="isTaxable" style="width:50px"></td>-->
   </tr>
   <%}
   }
   }%>
   </table>
   </div>
    <br>
  </td></tr>
  <tr>
  <td colspan="6">
  
  </td>
  </tr>
  <br>
  <table  width="100%" colspan="6" cellpadding ="0" cellspacing ="0" border = "0">
 <td colspan="6" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                            <div class="headplacer">Increment Details</div></td>
                        </tr>
  <tr><td colspan="8">
   <table  width="100%" colspan="6" cellpadding ="0" cellspacing ="0" border = "0" id="incrementdetails">
     <tr>                
         <td class="tablesubheadwk"><bean:message key="incSlabFrmAmt"/></td>
         <td class="tablesubheadwk"><bean:message key="incSlabToAmt"/></td>
         <td class="tablesubheadwk"><bean:message key="Increment"/></td>
         <td class="tablesubheadwk">Add/Del</td>
         <!--<td class="labelcell"><P align="center">Taxable</p></td>-->
      </tr>
   <%
   
   
   for(Iterator iter = tempset.iterator(); iter.hasNext();)
   {
   	IncrementSlabsForPayScale incDetails = (IncrementSlabsForPayScale)iter.next();
   	
   		%>
     	<tr id="increments" >
	  		<td class="whitebox3wk">
				<html:text styleClass="amount" property="incSlabFrmAmt"   onblur="return checkfornumber(this,this.value);"  value="<%=incDetails.getIncSlabFrmAmt().toString()%>" />
	  		</td>
	  		<td class="whitebox3wk">
	    		<html:text styleClass="amount" property="incSlabToAmt"  value="<%=incDetails.getIncSlabToAmt().toString()%>"   onblur="return checkfornumber(this,this.value);" />
	  		</td>
	  		<td class="whitebox3wk">
	    		<input type="text" class="amount" name="incSlabAmt" id="incSlabAmt" value="<%=incDetails.getIncSlabAmt().toString()%>"  onblur="return checkfornumber(this,this.value);"/>
	  		</td>           
	  
	  		<td width="8%" class="whitebox3wk" ><div align="center">
       	 		<a href="#"><img src="../common/image/add.png" alt="Add" name="addBut" id="addBut" width="16" height="16" border="0"  onclick="whichButtonEarnings('incrementdetails',this,'increments');"/></a>
       	  		<a href="#"><img src="../common/image/cancel.png" alt="Del" name="delBut" id="delBut" width="16" height="16" border="0" onclick="deleteRow('incrementdetails',this);" /></a></div>
     	 	</td>
      	</tr>
      <%
      } 
       
       if(tempset.isEmpty())
       {
       %>	  
     	<tr id="increments" >
	      	 <td class="whitebox3wk">
	      		<html:text styleClass="amount" property="incSlabFrmAmt"   onblur="return checkfornumber(this,this.value);"  value="" />
	      	 </td>
	      	 <td class="whitebox3wk">
	      	    <html:text styleClass="amount" property="incSlabToAmt"  value=""   onblur="return checkfornumber(this,this.value);" />
	      	 </td>
	      	 <td class="whitebox3wk">
	      	    <input type="text" class="amount" name="incSlabAmt" id="incSlabAmt" value=""  onblur="return checkfornumber(this,this.value);"/>
	      	 </td>           
	      	  
	      	 <td width="8%" class="whitebox3wk" ><div align="center">
	           <a href="#"><img src="../common/image/add.png" alt="Add" name="addBut" id="addBut" width="16" height="16" border="0"  onclick="whichButtonEarnings('incrementdetails',this,'increments');"/></a>
	           <a href="#"><img src="../common/image/cancel.png" alt="Del" name="delBut" id="delBut" width="16" height="16" border="0" onclick="deleteRow('incrementdetails',this);" /></a></div>
	         </td>
          </tr>
     <%
       }
      %>
   </table>
   <table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
       <tr>
       <td class="greyboxwk"><div align="right" class="bold">Gross Pay</div></td>
        <td class="greybox2wk" colspan="6"><html:text styleClass="amount" property="grossPay" value="<%=grossTotalpay.toString()%>" readonly="true"/></td>
  </tr>
  </table>
   </center>
   </div>
   </table>
 
 <tr>
 <br>
       <td class="whiteboxwk"><b><span class="mandatory">*</span>Remarks</b></td>
      <td class="whitebox2wk">
      <input type="text" class="selectwk" name="modifyRemarks" />
      </td>
      <br>
      <br>
      
    
   <tr>
                <td colspan="9" class="shadowwk"></td>
              </tr>
              <tr>
                <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
              </tr>
 <td><input type="button" class="buttonfinal" name="save" value="Save & View" onclick="return validation('save');"/></td>
<!-- <td><input type="button" class="button" name="back" value="Back" onclick="return validation('saveNew');"/></td>	-->
 <td><input type="button" class="buttonfinal" name="cancel" value="Cancel" onclick="history.go(0)"/></td>
</tr>
</html:form>

<% session.removeAttribute("payHeader");
%>
</body>
</html>
