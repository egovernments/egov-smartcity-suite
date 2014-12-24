<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,java.text.*,
org.egov.payroll.client.advance.AdvanceDisbursementForm,
org.egov.infstr.utils.EGovConfig,
org.egov.payroll.model.Advance,
org.egov.infstr.utils.EgovMasterDataCaching"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Create Disbursement By Cash</title>
<META http-equiv=pragma content=no-cache>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
<SCRIPT type="text/javascript" src="../javascript/calender.js" ></SCRIPT>
<SCRIPT type="text/javascript" src="../script/calendar.js" ></SCRIPT>
<SCRIPT type="text/javascript" src="../script/jsCommonMethods.js"></Script>
<SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>
<%
	AdvanceDisbursementForm adf=(AdvanceDisbursementForm)request.getAttribute("AdvanceDisbursementForm");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
	NumberFormat nf=new DecimalFormat("##############.00");
%>

<script>
var advTypeGlcode, advTypeGlname;
function onBodyLoad()
{	
	<% 				
		String autoGenerateChqNo= EGovConfig.getProperty("egf_config.xml","autoGenerateChqNo","","autoGenerateCheck");	
		String paidTo= EGovConfig.getProperty("egf_config.xml","paidTo","","chequePaidTo");		
		System.out.println("paidTo>>>"+paidTo);
		ArrayList deptList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
		ArrayList fundList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund");	
	%>	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");		
	}
	var buttonType="${buttonType}";	
	if(buttonType == "saveclose")
	{
		window.close();
	}
	if(buttonType == "saveview")
	{		
		var vhId="<%=request.getAttribute("vhId")%>";
		document.AdvanceDisbursementForm.action = "${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=beforePrintDisbursement&vhId="+vhId,"";
		document.AdvanceDisbursementForm.submit();
		return;		
	}	

	var mode="${mode}";			
	if(mode == "search")
	{		
		document.getElementById("srchRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("row2").style.display="none";			
		document.getElementById("row3").style.display="none";
		document.getElementById("row4").style.display="none";			
	}
	if(mode == "create")
	{		
	<% 	
		ArrayList advSanctionList = (ArrayList)request.getSession().getAttribute("advSanctionList");		
	
		if(advSanctionList !=null && advSanctionList.size()>0)
		{
	%>				
			document.getElementById("srchRow").style.display="block";			
			document.getElementById("totalRow").style.display="block";
			document.getElementById("vhRow").style.display="block";
			document.getElementById("msgRow").style.display="none";
			document.getElementById("row2").style.display="block";			
			document.getElementById("row3").style.display="none";
			document.getElementById("row4").style.display="none";
			document.AdvanceDisbursementForm.voucherDate.value = "<%=currDate%>";	
			document.AdvanceDisbursementForm.paidTo.value = "<%=paidTo%>";
			getGlcode();
			document.getElementById("srndrChk").style.display="none";		
			document.AdvanceDisbursementForm.disbMethod.disabled =true;		
			document.AdvanceDisbursementForm.department.disabled =true;	
			document.AdvanceDisbursementForm.advanceType.disabled =true;		
			document.AdvanceDisbursementForm.fund.disabled =true;
			
			if("<%=autoGenerateChqNo%>"=="Y")
			{
				document.getElementById("chqRow").style.display="none";
			}			
	<%
		}
		else
		{
	%>	
			document.getElementById("row2").style.display="none";
			document.getElementById("msgRow").style.display="block";
	<%	} %>
	}

	if(target!="null")
	{
		window.location="${pageContext.request.contextPath}/advance/advanceDisbursement.do?submitType=beforeCreateDisbursement";	
		document.getElementById("msgRow").style.display="none";
	}
	
	if(mode == "modify")
	{		
		document.title="Modify Advance Disbursement By Cash/Direct Transfer";
		document.getElementById('screenName').innerHTML="Modify Advance Disbursement By Cash/Direct Transfer";
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="block";
		document.getElementById("row4").style.display="none";
		document.getElementById("srchRow").style.display="none";		
		document.getElementById("totalRow").style.display="block";
		document.getElementById("vhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";		
		document.getElementById("voucherNoPrefix").style.display="block";
		getGlcode();	
		document.AdvanceDisbursementForm.disbMethod.disabled =true;	
		document.AdvanceDisbursementForm.department.disabled =true;	
		document.AdvanceDisbursementForm.advanceType.disabled =true;		
		document.AdvanceDisbursementForm.fund.disabled =true;		
		document.AdvanceDisbursementForm.bank.disabled =true;	
		document.AdvanceDisbursementForm.bankAccount.disabled =true;	
		document.AdvanceDisbursementForm.chequeNo.disabled =true;		
		document.AdvanceDisbursementForm.chequeDate.disabled =true;	
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("img2").style.display="none";
						
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementForm.fund.value+' order by id', 'bank', 'bankAccount');
		document.AdvanceDisbursementForm.bankAccount.value="<%=adf.getBankAccount()%>";
		var vnum=document.AdvanceDisbursementForm.voucherNo.value;
		var subVnum=document.AdvanceDisbursementForm.voucherNo.value.substring(0,2);
		document.AdvanceDisbursementForm.voucherNo.value=vnum.substring(2,vnum.length);
		document.AdvanceDisbursementForm.voucherNoPrefix.value=subVnum;
	}
	
	if(mode == "view")
	{		
		document.title="View Advance Disbursement By Cash/Direct Transfer";
		document.getElementById('screenName').innerHTML="View Advance Disbursement By Cash/Direct Transfer";
		document.getElementById("row4").style.display="block";	
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="none";
		document.getElementById("srchRow").style.display="block";		
		document.getElementById("totalRow").style.display="block";
		document.getElementById("vhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("chqAvail1").style.display="none";
		getGlcode();
		document.getElementById("img1").style.display="none";
		document.getElementById("img2").style.display="none";
		document.getElementById("img3").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";			
		if(document.AdvanceDisbursementForm.isChqSurrendered.checked==true)
		{
			document.getElementById("newChqRow").style.display="block";
		}
		
		for(var i=0;i<document.AdvanceDisbursementForm.length;i++)
		{
			if(document.AdvanceDisbursementForm.elements[i].value != "  Close  ")
			{
				document.AdvanceDisbursementForm.elements[i].disabled =true;
			}					
		}
				loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementForm.fund.value+' order by id', 'bank', 'bankAccount');
		document.AdvanceDisbursementForm.bankAccount.value="<%=adf.getBankAccount()%>";				
	}
}

function ButtonPress(arg)
{	
	if(document.AdvanceDisbursementForm.voucherDate.value != ""){			
			if(compareDate(document.AdvanceDisbursementForm.voucherDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertVoucherDateNotLesserThanCurrentDate"/>');
				var temp="document.AdvanceDisbursementForm.voucherDate.focus();";
				setTimeout(temp,0);	
		    	return;
			}	
		}
	if(document.AdvanceDisbursementForm.disbMethod.value == "0")
	{
		alert('<bean:message key="alertDisbursementMethod"/>');
		var temp="document.AdvanceDisbursementForm.disbMethod.focus();";
		setTimeout(temp,0);		
		return;
	}
	if(document.AdvanceDisbursementForm.advanceType.value == "0")
	{
		alert('<bean:message key="alertAdvanceType"/>');
		var temp="document.AdvanceDisbursementForm.advanceType.focus();";
		setTimeout(temp,0);		
		return;
	}	
	if(document.AdvanceDisbursementForm.fund.value == "0")
	{
		alert('<bean:message key="alertFund"/>');
		var temp="document.AdvanceDisbursementForm.fund.focus();";
		setTimeout(temp,0);		
		return;
	}

	var mode="${mode}";		
	if(arg=="search")
	{		
		document.AdvanceDisbursementForm.action = "${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=searchAdvanceDetails";
		document.AdvanceDisbursementForm.submit();
		return;
	}
	if(!validate())
		return false;	

	document.getElementById("button").value=arg;
	if(!validateAdvanceDisbursementForm(document.AdvanceDisbursementForm))
		return;
	if("<%=autoGenerateChqNo%>"!="Y" && mode == "create")
	{
		if(document.AdvanceDisbursementForm.chequeNo.value=="")
		{
			alert('<bean:message key="alertSelectChequeNumber"/>');
			var temp="document.AdvanceDisbursementForm.chequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementForm.chequeDate.value=="")
		{
			alert('<bean:message key="alertChequeDate"/>');
			var temp="document.AdvanceDisbursementForm.chequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementForm.voucherDate.value != ""){			
			if(compareDate(document.AdvanceDisbursementForm.voucherDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertVoucherDateNotLesserThanCurrentDate"/>');
				var temp="document.AdvanceDisbursementForm.voucherDate.focus();";
				setTimeout(temp,0);	
		    	return;
			}	
		}
		if(document.AdvanceDisbursementForm.chequeDate.value != ""){
			if(compareDate(document.AdvanceDisbursementForm.chequeDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertChequeDateNotLesserCurrentDate"/>');
				var temp="document.AdvanceDisbursementForm.chequeDate.focus();";
				setTimeout(temp,0);	
				return;
			}
		}
		
	}
	else if("<%=autoGenerateChqNo%>"!="Y" && mode == "modify" && document.AdvanceDisbursementForm.isChqSurrendered.checked==true)
	{
		if(document.AdvanceDisbursementForm.newChequeNo.value=="")
		{
			alert('<bean:message key="alertNewChequeNumber"/>');			
			var temp="document.AdvanceDisbursementForm.newChequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementForm.newChequeDate.value=="")
		{
			alert('<bean:message key="alertNewChequeDate"/>');
			var temp="document.AdvanceDisbursementForm.newChequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
	}	
	
	document.AdvanceDisbursementForm.disbMethod.disabled =false;		
	document.AdvanceDisbursementForm.department.disabled =false;		
	document.AdvanceDisbursementForm.advanceType.disabled =false;		
	document.AdvanceDisbursementForm.fund.disabled =false;	

	if(mode == "create")
	{
		document.AdvanceDisbursementForm.action = "${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=createDisursement";
		document.AdvanceDisbursementForm.submit();	
	}
	if(mode != "create")
	{
		var vhId= "<%=request.getParameter("vhId")%>";
		document.AdvanceDisbursementForm.bank.disabled =false;	
		document.AdvanceDisbursementForm.bankAccount.disabled =false;	
		document.AdvanceDisbursementForm.chequeNo.disabled =false;		
		document.AdvanceDisbursementForm.chequeDate.disabled =false;
		document.AdvanceDisbursementForm.action = "${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=modifyDisbursement&vhId="+vhId;
		document.AdvanceDisbursementForm.submit();
	}	
}

function validate()
{	
	if(document.AdvanceDisbursementForm.bank.value == "0")
	{
		alert('<bean:message key="alertBank"/>');
		var temp="document.AdvanceDisbursementForm.bank.focus();";
		setTimeout(temp,0);		
		return;
	}
	if(document.AdvanceDisbursementForm.bankAccount.value == "")
	{
		alert('<bean:message key="alertBankAccount"/>');		
		var temp="document.AdvanceDisbursementForm.bankAccount.focus();";
		setTimeout(temp,0);	
		return;
	}	
	return true;
}

function nextChqNo()
{
	document.AdvanceDisbursementForm.chequeNo.value="";
	var mozillaFirefox=document.getElementById&&!document.all;
	var obj=document.getElementById("bankAccount");
	var bankBr=document.getElementById("bank");
	if(bankBr.value=="0")
	{
	  alert('<bean:message key="alertBankAndBankAccount"/>');
	  return;
	}

	if(obj.value=="")
	{
	  alert('<bean:message key="alertBankAccount"/>');
	  return;
	}
	var accNoId=obj.value;
	var sRtn;
	if(mozillaFirefox)	
		sRtn =window.open("../advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=400pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");
	else
		sRtn =showModalDialog("../advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","dialogLeft=300;dialogTop=310;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	
	if(sRtn!=undefined) 
		document.AdvanceDisbursementForm.chequeNo.value=sRtn;		
}

function nextNewChqNo()
{
	document.AdvanceDisbursementForm.newChequeNo.value="";
	var mozillaFirefox=document.getElementById&&!document.all;
	var obj=document.getElementById("bankAccount");
	var bankBr=document.getElementById("bank");
	if(bankBr.value=="0")
	{
	  alert('<bean:message key="alertBankAndBankAccount"/>');
	  return;
	}

	if(obj.value=="")
	{
	  alert('<bean:message key="alertBankAccount"/>');
	  return;
	}	
	var accNoId=obj.value;
		var sRtn;
	if(mozillaFirefox)	
		sRtn =window.open("../advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=400pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");
	else
		sRtn =showModalDialog("../advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","dialogLeft=300;dialogTop=310;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	
	if(sRtn!=undefined) 
		document.AdvanceDisbursementForm.newChequeNo.value=sRtn;	
}

function onClickCancel()
{
	document.AdvanceDisbursementForm.reset();
}
 
function onClickCancelSearch()
{
	window.location="${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=beforeCreateDisbursement";	
}

function hideColumn(index)
{
	var table=document.getElementById('entries');
	for(var i=0;i<table.rows.length;i++)
	{
		table.rows[i].cells[index].style.display="none";
	}
}

function getGlcode()
{
	var advanceType=document.AdvanceDisbursementForm.advanceType.value;
	if(advanceType!=0)
	{
		<c:forEach var="sc" items="${salCodesList}" > 			
			if(advanceType== "${sc.id}")
			{				
				advTypeGlcode="${sc.chartofaccounts.glcode}";
				advTypeGlname="${sc.chartofaccounts.name}";					
			}
		</c:forEach> 
	}
}

function showglEntry()
{
	if(!validate())
		return false;	
	hideColumn(0);	
	var trObj;	
	var tObj=document.getElementById("showEntries");
	tObj.style.display="block";
	var cr=0,dr=0,dedAmount=0;	
	
	var tableObj=document.getElementById("entries");
	for(var i=tableObj.rows.length-1;i>=2;i--)
	{
		tableObj.deleteRow(i);
	}	
			
	var totalAmount=document.AdvanceDisbursementForm.totalAmount.value;		
	if(totalAmount>0)
	{
		var Obj=getControlInBranch(tableObj.rows[1],"display_Code");
		trObj=getRow(Obj);
		var newRow=trObj;		
		objt1=getControlInBranch(newRow,'display_Code');
		objt2=getControlInBranch(newRow,'display_Head');
		objt3=getControlInBranch(newRow,'display_Debit');
		objt4=getControlInBranch(newRow,'display_Credit');
		if(advTypeGlcode=="") 
			advTypeGlcode=" -"; 
		if(advTypeGlname=="") 
			advTypeGlname=" -";
		objt1.innerHTML=advTypeGlcode;
		objt2.innerHTML=advTypeGlname;		
		objt3.innerHTML=totalAmount;
		objt4.innerHTML="0";
	}		
	
	var accountId=document.AdvanceDisbursementForm.bankAccount.value;
	var accountGlcode, accountGlname;
	if(accountId != '')
	{		
		var url = "../commons/process.jsp?accountId=" +accountId+ "&type=getBankAccountGlcode";
		var req2 = initiateRequest();  		
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200) 
		{
        	var result=req2.responseText.split("`-`");     	            	   	           	         	           		              					              
            if(result!= null && result!= "")
             {							
				accountGlcode=result[0];
				accountGlname=result[1];
			}  
		} 
	}
	if(totalAmount>0)
	{
		var newRow=trObj.cloneNode(true);
		newRow=tableObj.tBodies[0].appendChild(newRow);
		objt1=getControlInBranch(newRow,'display_Code');
		objt2=getControlInBranch(newRow,'display_Head');
		objt3=getControlInBranch(newRow,'display_Debit');
		objt4=getControlInBranch(newRow,'display_Credit');
		if(accountGlcode=="") 
			accountGlcode=" -"; 
		if(accountGlname=="") 
			accountGlname=" -";
		objt1.innerHTML=accountGlcode;
		objt2.innerHTML=accountGlname;
		objt3.innerHTML="0";		
		objt4.innerHTML=totalAmount;
	}
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
	objt1.innerHTML=" ";
	objt2.innerHTML=" ";
	objt3.innerHTML=dSum.toFixed(2);
	objt4.innerHTML=cSum.toFixed(2);	
}

function disableNextCheque(obj)
{	
	if(obj.checked==true && "<%=autoGenerateChqNo%>"!="Y")
	{	
		document.getElementById("newChqRow").style.display="block";		
	}
	else
	{			
		document.getElementById("newChqRow").style.display="none";		
	}
}

function getAccountBalance(obj)
{
	var accountId = obj.value;
	document.AdvanceDisbursementForm.balance.value="";
	document.AdvanceDisbursementForm.chequeNo.value="";
	if(accountId != '')
	{
		var voucherDate=document.AdvanceDisbursementForm.voucherDate.value;
	   	if(!voucherDate)
	   	{
	    	alert('<bean:message key="alertVoucherDate"/>');
	    	var temp="document.AdvanceDisbursementForm.voucherDate.focus();";
			setTimeout(temp,0);	
	    	return;
	   	}		
	   	else
	   	{
		   	var url = "../commons/process.jsp?accountId=" +accountId+ "&vhDate=" +voucherDate+ "&type=getBankAccountBalance";
			var req2 = initiateRequest();  		
			req2.open("GET", url, false);
			req2.send(null);
			if (req2.status == 200) 
			{
	        	var result=req2.responseText;     	            	   	           	         	           		              					              
	            if(result!= null && result!= "")
	             {							
					document.AdvanceDisbursementForm.balance.value=result;					
				}  
			} 
	   	}	   	
	}
}

function getDetails()
{	
	var mode="${mode}";
	var disbMethod=document.AdvanceDisbursementForm.disbMethod.value;
	var department=document.AdvanceDisbursementForm.department.value;
	var advanceType=document.AdvanceDisbursementForm.advanceType.value;
	var fund=document.AdvanceDisbursementForm.fund.value;	
	window.open("${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=getEmployeeDetails&mode="+mode+"&disbMethod="+disbMethod+"&department="+department+"&advanceType="+advanceType+"&fund="+fund,"","height=500pt, width=700pt,scrollbars=yes,left=100,top=100,status=yes");
}

function checkDate(obj){
	alert("${currentDate}");
}

</script>
</head>   

<body onload="onBodyLoad()" onKeyDown ="CloseWindow(event);">
<html:form  action="/salaryadvance/advanceDisbursement">

<!-- <table align=center> -->

 <table align='center' class="tableStyle" style="width:700px" id="mainTable" name="mainTable">
 <tr>
      <td colspan="5" class="tableheader" align="center"><span id="screenName">Create Disbursement By Cash/Direct Transfer<span></td> 
 </tr>
 <tr>
 <td> 
 	 <table>
 	 <table>
	 	<tr>	 		
	       	<td colspan="5">&nbsp;</td> 	
		</tr>
		
		<tr>			
	 		<td class="labelcell" align="right" height="35" ><bean:message key="DisbursementMethod"/> <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="disbMethod" styleClass="bigcombowidth">
				<html:option value='0'>--Choose--</html:option>					
					<html:option value="cash">Cash</html:option>
					<html:option value="dbt">Direct Bank Transfer</html:option>
				</html:select>
			</td>							
	 	
	 		<td class="labelcell" align="right" height="35" ><bean:message key="Department"/>&nbsp;&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="department" styleClass="bigcombowidth">
				<html:option value='0'>--Choose--</html:option>
				<c:forEach var="department" items="<%=deptList%>" > 
					<html:option value="${department.id}">${department.deptName}</html:option>
				</c:forEach> 
				</html:select>
			</td>							
	 	</tr> 
	 	
		<tr>			
	 		<td class="labelcell" align="right" height="35" ><bean:message key="AdvanceType"/> <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="advanceType" styleClass="bigcombowidth" onchange="getGlcode()">
				<html:option value='0'>--Choose--</html:option>
				<c:forEach var="sc" items="${salCodesList}" > 
					<html:option value="${sc.id}">${sc.head}</html:option>
				</c:forEach> 
				</html:select>
			</td>
			<td class="labelcell" align="right" height="35" ><bean:message key="Fund"/><SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="fund" styleClass="bigcombowidth" >
				<html:option value='0'>--Choose--</html:option>
				<c:forEach var="fu" items="<%=fundList%>" > 
					<html:option value="${fu.id}">${fu.name}</html:option>
				</c:forEach> 
				</html:select>
			</td>	 				
	 	</tr> 	 	
	 		
		<tr>
			<td colspan=5>
			<table align="center">
				<tr id="srchRow" name="srchRow">
					<td width="70px"></td>
					<td align="center" colspan=5>
					<html:button styleClass="button" value="Search" property="b5" onclick="ButtonPress('search')" />
					<html:button styleClass="button" value="Cancel" property="b6" onclick="onClickCancelSearch()" />
					</td>
				</tr>		
			</table>			
		</td>
		</tr>
		</table>
				
		<tr><td height="10"></td></tr>	
		
		<tr id="vhRow" name="vhRow" style="display:none">
		<td colspan=5>
		<table>				
		<tr align="left" id="totalRow" name="totalRow" style="display:none">
			<html:hidden property="totalAmount"/>
			<td class="thStlyle" align="right"><bean:message key="SanctionedAmount"/>&nbsp;:&nbsp;</td> 
			<td class="tdStlyle" align="left" ><A style="text-decoration: none;" onClick="getDetails()" href="#"><div style="text-align:right" ><%= adf.getTotalAmount()%></div></A></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>		
		<tr>
			<td class="labelcell" align="right" height="35" ><bean:message key="VoucherNo"/> <SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td  colspan="2"><table><tr>
	 		<td class="smallfieldcell"><html:text property="voucherNoPrefix" style="width:20px;display:none" styleId="voucherNoPrefix" readonly="true" tabindex="-1"/>
	 		</td><td class="fieldcell"><html:text property="voucherNo" maxlength="10"/></td>	 		
	 		</tr></table></td>
			<td class="labelcell" align="right" height="35" ><bean:message key="VoucherDate"/> <SPAN class="leadon">*</SPAN>&nbsp;</td>				
			<td class="smallfieldcell" align="center" ><html:text property= "voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" />
			<a href="javascript:show_calendar('AdvanceDisbursementForm.voucherDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a>
			</td>
				 		
		</tr>	
		
		<tr>
		 	<td class="labelcell" align="right" height="35" ><bean:message key="PaidBy"/> &nbsp;&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="left" >
				<html:select  property="paidBy" styleId="paidBy" style="width:160px" styleClass="bigcombowidth" >
				<html:option value='0'>--Choose--</html:option>
					<c:forEach var="cl" items="${cashierList}" > 
						<html:option value="${cl.id}">${cl.name}</html:option>
					</c:forEach> 	
				</html:select>
			</td>
			<td class="labelcell" align="right"><bean:message key="PaidTo"/> &nbsp;:&nbsp;</td> 
			<td class="fieldcell" align="left" ><html:text  property="paidTo" readonly="true" tabindex="-1"/></td>
		</tr>	 		 	
		
		<tr>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="Bank"/><SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="left" >
				<html:select  property="bank" styleClass="bigcombowidth" style="width:250px" styleId="bank" onchange="loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementForm.fund.value+' order by id', 'bank', 'bankAccount')">
				<html:option value='0'>--Choose--</html:option>	
				<c:forEach var="bankBr" items="${bankBranchList}" > 
					<html:option value="${bankBr.id}">${bankBr.name}</html:option>
				</c:forEach> 				
				</html:select>
			</td>			
			<td class="labelcell" align="right" height="35" ><bean:message key="BankAccount"/> <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="bankAccount" styleId="bankAccount" style="width:170px" styleClass="bigcombowidth" onchange="getAccountBalance(this)">
				<html:option value=''>--Choose--</html:option>	
				</html:select>
			</td>			
	 	</tr>
	 	
	 	<tr>
		 	<td class="labelcell" align="right" height="35" ><bean:message key="Balance"/>&nbsp;&nbsp;</td> 
			<td colspan="2" class="fieldcell" align="left" ><html:text  property="balance" style="text-align:right" readonly="true" tabindex="-1"/></td>
			<td></td>
			<td></td>
	 	</tr>	 	
	 	
	 	<tr id="chqRow" name="chqRow">
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeNumber"/>&nbsp;<SPAN id="c1" class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="chequeNo" /></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail"><A onclick=nextChqNo() href="#"><IMG id="img" height=22 src="../img/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" valign="center" style="width:100px" id="srndrChk"><html:checkbox property="isChqSurrendered" styleId="isChqSurrendered" onclick="disableNextCheque(this)"/>Is&nbsp;Surrendered&nbsp;(Y/N) </td>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeDate"/> <SPAN id="c2" class="leadon">*</SPAN>&nbsp;</td>
	 		<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');"/>
			</td><td><a href="javascript:show_calendar('AdvanceDisbursementForm.chequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img2" src="../images/calendar.gif" width=24 height=22 border=0></a>
			</td>
			</tr></table></td>
	 	</tr>
 
	 	<tr id="newChqRow" name="newChqRow" style="display:none">
	 		<td class="labelcell" align="right" height="35" ><bean:message key="NewChequeNumber"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="newChequeNo"/></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail1"><A onclick=nextNewChqNo() href="#"><IMG height=22 src="../img/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeDate"/> <SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="newChequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');" />
			</td><td><a href="javascript:show_calendar('AdvanceDisbursementForm.newChequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img3" src="../images/calendar.gif" width=24 height=22 border=0></a>
			</td>
			</tr></table></td>
		</tr>
	 	<tr>
	 		<td class="labelcell" align="right" height="35"></td>
	 		<td class="fieldcell" style="width:150px"></td>
			<td valign="center" style="width:100px" nowrap ></td>
			<td class="labelcell" align="right" height="35" ></td>
			<td class="smallfieldcell" align="center" ></td>
	 	</tr>
	 	</table>	 	
	 	</td>
	 	</tr>	
  
<tr align="center">
<td>
<table width="100%" align="center" border=0 cellpadding="3" cellspacing="0" id="showEntries" name="showEntries" style="DISPLAY: none">
<tr>
<td colspan=5>
<table width="100%" align=center border="0" cellpadding="0" cellspacing="0" >
<tr  height="25">
 <TD class=displaydata align=middle ><h4><bean:message key="GLEntry"/></h4></TD></tr>
</table>
</td>
</tr>
<tr><td>
<table width="80%" border="1" cellpadding="0" cellspacing="0" align=center id="entries" name="entries">
<tr >
	<td class="thStlyle"><div align="center" valign="center" > Code Type</div></td>
	<td  class="thStlyle"><div align="center" valign="center" > Account Code</div></td>
	<td  class="thStlyle"><div align="center" valign="center" > Account Head</div></td>
	<td  class="thStlyle"><div align="center" valign="center" > Debit </div></td>
	<td  class="thStlyle"><div align="center" valign="center" > Credit</div></td>
	
</tr>
<tr >
	<td class="tdStlyle" ><div name="display_CodeType"  id="display_CodeType">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Code"  id="display_Code" style="width:100px">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Head"  id="display_Head" style="width:280px">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Debit"  id="display_Debit" style="width:100px;text-align:right">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Credit"  id="display_Credit" style="width:100px;text-align:right">&nbsp;</div></td>
	
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>

<table align="center">
<tr id="msgRow" name="msgRow">
<td align="center" class="labelcell1" style="color:#ff6300"> No Records Found</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr  id="row2" name="row2">
<td  align="center" colspan=5>
<input type=hidden name="button" id="button"/> 
<html:button styleClass="button" value="Save & Close" property="b2" onclick="ButtonPress('saveclose')" />
<html:button styleClass="button" value="Save & New" property="b4" onclick="ButtonPress('savenew')" />
 <html:button styleClass="button" value="Save & View" property="b6" onclick="ButtonPress('saveview')" />
<html:reset styleClass="button" value="Cancel" property="b1" onclick="onClickCancel()" />
<html:button styleClass="button" value="Show GLEntry" property="b3" onclick="showglEntry()" />
<html:button styleClass="button" value="Close" property="b5" onclick="window.close();" />
</td>
</tr>
<tr style="DISPLAY: none" id="row3" name="row3">
<td  align="center" colspan=5>
<html:button styleClass="button" value=" Save " property="b4" onclick="ButtonPress('saveclose')" />
<html:button styleClass="button" value="Show GLEntry" property="b3" onclick="showglEntry()" />
<html:button styleClass="button" value="Save & View" property="b6" onclick="ButtonPress('saveview')" />
<html:button styleClass="button" value="Close" property="b5" onclick="window.close();" />
</td>
</tr>
<tr style="DISPLAY: none" id="row4" name="row4">
<td  align="center" colspan=5>
<html:button styleClass="button" value="  Close  " property="b3" onclick="window.close();" />
</td>
</tr>
</table>
 </table>
<html:javascript formName="AdvanceDisbursementForm"/> 

<%
request.getSession().removeAttribute("advSanctionList");
%>
   </html:form >
  </body>
</html>	
