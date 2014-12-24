<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,java.text.*,
org.egov.payroll.client.advance.AdvanceDisbursementByChequeForm,
org.egov.infstr.utils.EGovConfig,
org.egov.payroll.model.Advance"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Create Disbursement By Cheque</title>
<META http-equiv=pragma content=no-cache>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">

<SCRIPT type="text/javascript" src="<%=request.getContextPath()+"/script/calendar.js"%>"></SCRIPT>

<%
	AdvanceDisbursementByChequeForm adf=(AdvanceDisbursementByChequeForm)request.getAttribute("AdvanceDisbursementByChequeForm");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
	NumberFormat nf=new DecimalFormat("##############.00");
%>

<script>
function onBodyLoad()
{	
	<% 				
		String autoGenerateChqNo= EGovConfig.getProperty("egf_config.xml","autoGenerateChqNo","","autoGenerateCheck");		
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
		document.AdvanceDisbursementByChequeForm.action = "${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=beforePrintChequeDisbursement&vhId="+vhId,"";
		document.AdvanceDisbursementByChequeForm.submit();
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
		document.AdvanceDisbursementByChequeForm.fundId.value = "<%=adf.getFundId()%>";
	<% 	
		ArrayList advSanctionList = (ArrayList)request.getSession().getAttribute("advSanctionList");
		System.out.println("size of list----------"+advSanctionList);
		if(advSanctionList !=null && advSanctionList.size()>0)
		{
	%>				
			document.getElementById("srchRow").style.display="block";
			document.getElementById("detailRow").style.display="block";
			document.getElementById("totalRow").style.display="block";
			document.getElementById("vhRow").style.display="block";
			document.getElementById("msgRow").style.display="none";
			document.getElementById("row2").style.display="block";			
			document.getElementById("row3").style.display="none";
			document.getElementById("row4").style.display="none";
			document.AdvanceDisbursementByChequeForm.voucherDate.value = "<%=currDate%>";	
			document.getElementById("srndrChk").style.display="none";	
			document.getElementById("fundRow").style.display="block";			
			document.AdvanceDisbursementByChequeForm.employee.disabled =true;		
			document.AdvanceDisbursementByChequeForm.employeeName.disabled =true;	

			if("<%=autoGenerateChqNo%>"=="Y")
			{
				document.getElementById("chqRow").style.display="none";
			}
			var table=document.getElementById('details');
			for(var i=0;i<table.rows.length;i++)
			{
				table.rows[i].cells[0].style.display="none";
				table.rows[i].cells[1].style.display="none";
				table.rows[i].cells[2].style.display="none";				
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
		window.location="${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=beforeCreateDisbursement";	
		document.getElementById("msgRow").style.display="none";
	}
	
	if(mode == "modify")
	{		
		document.title="Modify Advance Disbursement By Cheque";
		document.getElementById('screenName').innerHTML="Modify Advance Disbursement By Cheque";
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="block";
		document.getElementById("row4").style.display="none";
		document.getElementById("srchRow").style.display="none";
		document.getElementById("detailRow").style.display="block";
		document.getElementById("totalRow").style.display="block";
		document.getElementById("vhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";		
		document.getElementById("voucherNoPrefix").style.display="block";	
		document.getElementById("fundRow").style.display="block";
		document.AdvanceDisbursementByChequeForm.employee.disabled =true;		
		document.AdvanceDisbursementByChequeForm.employeeName.disabled =true;
		document.AdvanceDisbursementByChequeForm.bank.disabled =true;	
		document.AdvanceDisbursementByChequeForm.bankAccount.disabled =true;	
		document.AdvanceDisbursementByChequeForm.chequeNo.disabled =true;		
		document.AdvanceDisbursementByChequeForm.chequeDate.disabled =true;	
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("img2").style.display="none";
		var table=document.getElementById('details');
		for(var i=0;i<table.rows.length;i++)
		{
			table.rows[i].cells[0].style.display="none";	
			table.rows[i].cells[1].style.display="none";	
			table.rows[i].cells[2].style.display="none";						
		}		
				
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementByChequeForm.fundId.value+' order by id', 'bank', 'bankAccount');
		document.AdvanceDisbursementByChequeForm.bankAccount.value="<%=adf.getBankAccount()%>";
		var vnum=document.AdvanceDisbursementByChequeForm.voucherNo.value;
		var subVnum=document.AdvanceDisbursementByChequeForm.voucherNo.value.substring(0,2);
		document.AdvanceDisbursementByChequeForm.voucherNo.value=vnum.substring(2,vnum.length);
		document.AdvanceDisbursementByChequeForm.voucherNoPrefix.value=subVnum;
		var tbl= document.getElementById("details");	
		for(var i=1;i<tbl.rows.length;i++)
		{
			getControlInBranch(tbl.rows[i],'pay1').checked = true;			
		}	
		 getTotalAmount(tbl);	
	}
	
	if(mode == "view")
	{		
		document.title="View Advance Disbursement By Cheque";
		document.getElementById('screenName').innerHTML="View Advance Disbursement By Cheque";
		document.getElementById("row4").style.display="block";	
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="none";
		document.getElementById("srchRow").style.display="block";
		document.getElementById("detailRow").style.display="block";
		document.getElementById("totalRow").style.display="block";
		document.getElementById("vhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("chqAvail1").style.display="none";
		document.getElementById("img1").style.display="none";
		document.getElementById("img2").style.display="none";
		document.getElementById("img3").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";	
		document.getElementById("fundRow").style.display="block";				
		if(document.AdvanceDisbursementByChequeForm.isChqSurrendered.checked==true)
		{
			document.getElementById("newChqRow").style.display="block";
		}
		
		var table=document.getElementById('details');
		for(var i=0;i<table.rows.length;i++)
		{
			table.rows[i].cells[0].style.display="none";
			table.rows[i].cells[1].style.display="none";
			table.rows[i].cells[2].style.display="none";								
		}

		for(var i=0;i<document.AdvanceDisbursementByChequeForm.length;i++)
		{
			if(document.AdvanceDisbursementByChequeForm.elements[i].value != "  Close  ")
			{
				document.AdvanceDisbursementByChequeForm.elements[i].disabled =true;
			}					
		}
		var path="<%=request.getContextPath()%>"+"/commonyui/egov/loadComboAjax.jsp";
				loadSelectData(path, 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementByChequeForm.fundId.value+' order by id', 'bank', 'bankAccount');
		document.AdvanceDisbursementByChequeForm.bankAccount.value="<%=adf.getBankAccount()%>";	
		
		var tbl= document.getElementById("details");	
		for(var i=1;i<tbl.rows.length;i++)
		{
			getControlInBranch(tbl.rows[i],'pay1').checked = true;			
		}	
		 getTotalAmount(tbl);	
	}
}

function ButtonPress(arg)
{	
	if(document.AdvanceDisbursementByChequeForm.voucherDate.value != ""){			
			if(compareDate(document.AdvanceDisbursementByChequeForm.voucherDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertVoucherDateNotLesserThanCurrentDate"/>');
				var temp="document.AdvanceDisbursementByChequeForm.voucherDate.focus();";
				setTimeout(temp,0);	
		    	return;
			}	
		}
	if(document.AdvanceDisbursementByChequeForm.employee.value == "0")
	{
		alert('<bean:message key="alertSelectEmployeeCode"/>');
		var temp="document.AdvanceDisbursementByChequeForm.employee.focus();";
		setTimeout(temp,0);		
		return;
	}
	var mode="${mode}";		
	if(arg=="search")
	{		
		document.AdvanceDisbursementByChequeForm.action = "${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=searchAdvanceDetails";
		document.AdvanceDisbursementByChequeForm.submit();
		return;
	}
	if(!validate())
		return false;	

	document.getElementById("button").value=arg;
	if(!validateAdvanceDisbursementByChequeForm(document.AdvanceDisbursementByChequeForm))
		return;
	if("<%=autoGenerateChqNo%>"!="Y" && mode == "create")
	{
		if(document.AdvanceDisbursementByChequeForm.chequeNo.value=="")
		{
			alert('<bean:message key="alertSelectChequeNumber"/>');
			var temp="document.AdvanceDisbursementByChequeForm.chequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementByChequeForm.chequeDate.value=="")
		{
			alert('<bean:message key="alertChequeDate"/>');
			var temp="document.AdvanceDisbursementByChequeForm.chequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementByChequeForm.voucherDate.value != ""){			
			if(compareDate(document.AdvanceDisbursementByChequeForm.voucherDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertVoucherDateNotLesserThanCurrentDate"/>');
				var temp="document.AdvanceDisbursementByChequeForm.voucherDate.focus();";
				setTimeout(temp,0);	
		    	return;
			}	
		}
		if(document.AdvanceDisbursementByChequeForm.chequeDate.value != ""){
			if(compareDate(document.AdvanceDisbursementByChequeForm.chequeDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertChequeDateNotLesserCurrentDate"/>');
				var temp="document.AdvanceDisbursementByChequeForm.chequeDate.focus();";
				setTimeout(temp,0);	
				return;
			}
		}
	}
	else if("<%=autoGenerateChqNo%>"!="Y" && mode == "modify" && document.AdvanceDisbursementByChequeForm.isChqSurrendered.checked==true)
	{
		if(document.AdvanceDisbursementByChequeForm.newChequeNo.value=="")
		{
			alert('<bean:message key="alertNewChequeNumber"/>');
			var temp="document.AdvanceDisbursementByChequeForm.newChequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementByChequeForm.newChequeDate.value=="")
		{
			alert('<bean:message key="alertNewChequeDate"/>');
			var temp="document.AdvanceDisbursementByChequeForm.newChequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.AdvanceDisbursementByChequeForm.voucherDate.value != ""){			
			if(compareDate(document.AdvanceDisbursementByChequeForm.voucherDate.value,"${currentDate}") == 1){
				alert('<bean:message key="alertVoucherDateNotLesserThanCurrentDate"/>');
				var temp="document.AdvanceDisbursementByChequeForm.voucherDate.focus();";
				setTimeout(temp,0);	
		    	return;
			}	
		}
	}	
	
	document.AdvanceDisbursementByChequeForm.employee.disabled =false;		
	document.AdvanceDisbursementByChequeForm.employeeName.disabled =false;	
		
	if(mode == "create")
	{
		document.AdvanceDisbursementByChequeForm.action = "${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=createDisursementByCheque";
		document.AdvanceDisbursementByChequeForm.submit();	
	}
	if(mode != "create")
	{
		var vhId= "<%=request.getParameter("vhId")%>";
		document.AdvanceDisbursementByChequeForm.bank.disabled =false;	
		document.AdvanceDisbursementByChequeForm.bankAccount.disabled =false;	
		document.AdvanceDisbursementByChequeForm.chequeNo.disabled =false;		
		document.AdvanceDisbursementByChequeForm.chequeDate.disabled =false;
		document.AdvanceDisbursementByChequeForm.action = "${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=modifyDisbursementByCheque&vhId="+vhId;
		document.AdvanceDisbursementByChequeForm.submit();
	}	
}

function validate()
{
	var flag=false;
	var tbl= document.getElementById("details");	
	for(var i=1;i<tbl.rows.length;i++)
	{
		if(getControlInBranch(tbl.rows[i],'pay1').checked == true)
		{
			flag=true;			
		}
	}	
	
	if(flag==false)
	{
		alert('<bean:message key="alertAtleastOnAdvanceTypeToPay"/>');		
		return;
	}
	if(document.AdvanceDisbursementByChequeForm.bank.value == "0")
	{
		alert('<bean:message key="alertBank"/>');
		var temp="document.AdvanceDisbursementByChequeForm.bank.focus();";
		setTimeout(temp,0);		
		return;
	}
	if(document.AdvanceDisbursementByChequeForm.bankAccount.value == "")
	{
		alert('<bean:message key="alertBankAccount"/>');
		var temp="document.AdvanceDisbursementByChequeForm.bankAccount.focus();";
		setTimeout(temp,0);	
		return;
	}	
	return true;
}

function getEmplName(obj)
{
	var emplId=obj.value;
	if(emplId !='0')
	{
		<c:forEach var="as" items="${advSanctionEmpList}">
			if(emplId=="${as.idPersonalInformation}")
			{
				document.AdvanceDisbursementByChequeForm.employeeName.value="${as.employeeName}";
			}		
		</c:forEach>
	}
}

function getTotalAmount(obj)
{
	var tbl=document.getElementById('details');
	var totalAmt=0.00;	
	for(i=1;i<tbl.rows.length;i++)
	{			
		if(getControlInBranch(tbl.rows[i],'pay1').checked==true) 
		{
			totalAmt= eval(totalAmt)+ eval(getControlInBranch(tbl.rows[i],'sanctionAmount').innerHTML);	
			getControlInBranch(tbl.rows[i],'pay').value ="yes";		
		}	
		else
		{
			getControlInBranch(tbl.rows[i],'pay').value ="no";
		}		
	}	
	document.getElementById("total").innerHTML=totalAmt.toFixed(2);
	document.AdvanceDisbursementByChequeForm.totalAmt.value=totalAmt.toFixed(2);	
}

function nextChqNo()
{
	document.AdvanceDisbursementByChequeForm.chequeNo.value="";
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
		sRtn =window.open("<%=request.getContextPath()%>"+"/advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=400pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");
	else
		sRtn =showModalDialog("<%=request.getContextPath()%>"+"/advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","dialogLeft=300;dialogTop=310;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	
	if(sRtn!=undefined) 
		document.AdvanceDisbursementByChequeForm.chequeNo.value=sRtn;		
}

function nextNewChqNo()
{
	document.AdvanceDisbursementByChequeForm.newChequeNo.value="";
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
		sRtn =window.open("<%=request.getContextPath()%>"+"/advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=400pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");
	else
		sRtn =showModalDialog("<%=request.getContextPath()%>"+"/advance/SearchNextChqNo.jsp?accntNoId="+accNoId,"","dialogLeft=300;dialogTop=310;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	
	if(sRtn!=undefined) 
		document.AdvanceDisbursementByChequeForm.newChequeNo.value=sRtn;	
}

function onClickCancel()
{
	document.AdvanceDisbursementByChequeForm.reset();
}
 
function onClickCancelSearch()
{
	window.location="${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=beforeCreateDisbursement";	
}

function hideColumn(index)
{
	var table=document.getElementById('entries');
	for(var i=0;i<table.rows.length;i++)
	{
		table.rows[i].cells[index].style.display="none";
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
	var tbl= document.getElementById("details");	
	for(var i=1;i<tbl.rows.length;i++)
	{
		if(getControlInBranch(tbl.rows[i],'pay1').checked == true)
		{			
			var code=getControlInBranch(tbl.rows[i],'glCode').value;			
			var name=getControlInBranch(tbl.rows[i],'glName').innerHTML;
			var sanctionAmount=getControlInBranch(tbl.rows[i],'sanctionAmount').innerHTML;
		
			var Obj=getControlInBranch(tableObj.rows[1],"display_Code");
			trObj=getRow(Obj);
			 if(i==1)
			 {
				newRow=trObj;
				objt1=getControlInBranch(newRow,'display_Code');
				objt2=getControlInBranch(newRow,'display_Head');
				objt3=getControlInBranch(newRow,'display_Debit');
				objt4=getControlInBranch(newRow,'display_Credit');
				
				if(code=="") code=" -"; if(name=="") name=" -"; if(sanctionAmount=="") sanctionAmount=" 0";  
				objt1.innerHTML=code;
				objt2.innerHTML=name;
				objt3.innerHTML=sanctionAmount;
				objt4.innerHTML="0";							
			 }
			 else
			 {
				var newRow=trObj.cloneNode(true);
				newRow=tableObj.tBodies[0].appendChild(newRow);
				objt1=getControlInBranch(newRow,'display_Code');
				objt2=getControlInBranch(newRow,'display_Head');
				objt3=getControlInBranch(newRow,'display_Debit');
				objt4=getControlInBranch(newRow,'display_Credit');
				if(code=="") 
					code=" -"; 
				if(name=="") 
					name=" -";
				objt1.innerHTML=code;
				objt2.innerHTML=name;
				objt3.innerHTML=sanctionAmount;
				objt4.innerHTML="0";
			}			
		}
	}	
	
	var totalAmount=document.getElementById("total").innerHTML;	
	var accountId=document.AdvanceDisbursementByChequeForm.bankAccount.value;
	var accountGlcode, accountGlname;
	if(accountId != '')
	{		
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?accountId=" +accountId+ "&type=getBankAccountGlcode";
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
	document.AdvanceDisbursementByChequeForm.balance.value="";
	document.AdvanceDisbursementByChequeForm.chequeNo.value="";
	if(accountId != '')
	{
		var voucherDate=document.AdvanceDisbursementByChequeForm.voucherDate.value;
	   	if(!voucherDate)
	   	{
	    	alert('<bean:message key="alertVoucherDate"/>');
	    	var temp="document.AdvanceDisbursementByChequeForm.voucherDate.focus();";
			setTimeout(temp,0);	
	    	return;
	   	}
	   	else
	   	{
		   	var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?accountId=" +accountId+ "&vhDate=" +voucherDate+ "&type=getBankAccountBalance";
			var req2 = initiateRequest();  		
			req2.open("GET", url, false);
			req2.send(null);
			if (req2.status == 200) 
			{
	        	var result=req2.responseText;     	            	   	           	         	           		              					              
	            if(result!= null && result!= "")
	             {							
					document.AdvanceDisbursementByChequeForm.balance.value=result;					
				}  
			} 
	   	}	   	
	}
}

</script>
</head>   

<body onload="onBodyLoad()" onKeyDown ="CloseWindow(event);">
<html:form  action="/salaryadvance/disbursementByCheque">

<!-- <table align=center> -->

 <table align='center' class="tableStyle" style="width:700px" id="mainTable" name="mainTable">
 <tr>
      <td colspan="5" class="tableheader" align="center"><span id="screenName">Create Disbursement By Cheque<span></td> 
 </tr>
 <tr>
 <td> 
 	 <table>
 	 <table>
	 	<tr>	 		
	       	<td colspan="5">&nbsp;</td> 	
		</tr>	
		<tr>
	 		<td class="labelcell" align="center" height="35" ><bean:message key="EmployeeCode"/> <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="center" >
				<html:select  property="employee" styleClass="combowidth1" onchange="getEmplName(this)">
				<html:option value='0'>--Choose--</html:option>	
					<c:forEach var="as" items="${advSanctionEmpList}" > 
						<html:option value="${as.idPersonalInformation}">${as.employeeCode}</html:option>
					</c:forEach> 
				</html:select>
			</td>	
			<td class="labelcell" align="center" height="35" ><bean:message key="EmployeeName"/> &nbsp;&nbsp;</td> 
			<td class="fieldcell" align="center" ><html:text  property="employeeName" readonly="true" tabindex="-1"/>	</td>				
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
		</table>
		<table>
		</td>
		</tr>		
		<tr><td height="10"></td></tr>	
		
		<tr id="fundRow" name="fundRow" style="display:none" >
			<html:hidden property="fundId" />	
			<td colspan="3" class="labelcellforsingletd"><div align="left"><bean:message key="Fund"/>Fund:&nbsp;&nbsp;&nbsp; <%=adf.getFundName()%></div></td>		
		</tr>		
		<% 		
		if(advSanctionList !=null && advSanctionList.size()>0)
		{	
			System.out.println("INSIDElkjjjj-------------------");
		%>
		<tr id="detailRow" name="detailRow" style="display:none">
		<td colspan=5 align="center">
		<table cellpadding="0" cellspacing="0" align="center" id="details" name="details" >			
			<tr>				
				<td class="thStlyle"><div align="center">Id</div></td>
				<td class="thStlyle"><div align="center">GlCode</div></td>
				<td class="thStlyle"><div align="center">GlName</div></td>
				<td class="thStlyle"><div align="center"><bean:message key="AdvanceType"/>  </div></td>
				<td class="thStlyle"><div align="center"><bean:message key="SanctionNo"/> </div></td>
				<td class="thStlyle"><div align="center"><bean:message key="SanctionedBy"/> </div></td>
				<td class="thStlyle"><div align="center"><bean:message key="SanctionAmount"/> </div></td>
				<td class="thStlyle"><div align="center"><bean:message key="AdvancePay"/></div></td>								
			</tr>
		<% 		
		for(Iterator it = advSanctionList.iterator(); it.hasNext(); ) 
		{						
			Advance sa=(Advance)it.next();		
		%>	

			<tr>
				<td><html:hidden property="id" value="<%=sa.getId().toString()%>"/></td>
				<td><html:hidden property="glCode" value="<%=sa.getSalaryCodes().getChartofaccounts().getGlcode()%>"/></td>	
				<td class="tdStlyle"><div align="left" name="glName" id="glName"><%=sa.getSalaryCodes().getChartofaccounts().getName()%></div></td>										
				<td class="tdStlyle"><div align="left" name="advanceType" id="advanceType" style="width:160px"><%=sa.getSalaryCodes().getHead()%></div></td>	
				<td class="tdStlyle"><div align="left" name="sanctionNo" id="sanctionNo" style="width:130px"/><%=sa.getSanctionNum()%></div> </td>
				<td class="tdStlyle"><div align="left" name="sanctionedBy" id="sanctionedBy" style="width:150px"><%=sa.getSanctionedBy().getUserName()%></div> </td>
				<td class="tdStlyle"><div align="left" name="sanctionAmount" id="sanctionAmount" style="width:110px;text-align:right"><%=nf.format(sa.getAdvanceAmt())%></div></td>
				<td class="tdStlyle"><input type=checkbox  name="pay1" styleId="pay1" style="width:90px" onclick="getTotalAmount(this)" />
				<html:hidden property="pay" value="no"/></td>								
			</tr>
			<%
			}			
			%>
			</table>
		</td>
		</tr>		
	
		<tr align="center" id="totalRow" name="totalRow" style="display:none">
		<td colspan=5>
		<table>	
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td style="width:160px"></td>
				<td style="width:120px"></td>				
				<td class="thStlyle"><div style="width:150px" align="center">TOTAL</div></td>
				<td class="tdStlyle">
				<html:hidden property="totalAmt"/>
				<div align="left" name="total" id="total" style="width:110px;text-align:right"><b></b> </div> </td>				
				<td style="width:90px"></td>
			</tr>			
		</table>
		</td>
		</tr>
		</table>
		</td>
		</tr>
		<%
			}			
		%>
		
		<tr id="vhRow" name="vhRow" style="display:none">
		<td colspan=5>
		<table>		
				
		<tr>
			<td class="labelcell" align="right" height="35" ><bean:message key="VoucherNo"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td  colspan="2"><table><tr>
	 		<td class="smallfieldcell"><html:text property="voucherNoPrefix" style="width:20px;display:none" styleId="voucherNoPrefix" readonly="true" tabindex="-1"/>
	 		</td><td class="fieldcell"><html:text property="voucherNo" maxlength="10"/></td>	 		
	 		</tr></table></td>
			<td class="labelcell" align="right" height="35" ><bean:message key="VoucherDate"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td>				
			<td class="smallfieldcell" align="center" ><html:text property= "voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('AdvanceDisbursementByChequeForm.voucherDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a>
			</td>
				 		
		</tr>	
		
		<tr>
		 	<td class="labelcell" align="right" height="35" ><bean:message key="PaidBy"/>  &nbsp;&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="left" >
				<html:select  property="paidBy" styleId="paidBy" style="width:170px" styleClass="bigcombowidth" >
				<html:option value='0'>--Choose--</html:option>
					<c:forEach var="cl" items="${cashierList}" > 
						<html:option value="${cl.id}">${cl.name}</html:option>
					</c:forEach> 	
				</html:select>
			</td>
			<td></td>
			<td></td>
	 	</tr>	 		 	
		
		<tr>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="Bank"/> <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="left" >
				<html:select  property="bank" styleClass="bigcombowidth" style="width:250px" styleId="bank" onchange="loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.AdvanceDisbursementByChequeForm.fundId.value+' order by id', 'bank', 'bankAccount')">
				<html:option value='0'>--Choose--</html:option>	
				<c:forEach var="bankBr" items="${bankBranchList}" > 
					<html:option value="${bankBr.id}">${bankBr.name}</html:option>
				</c:forEach> 				
				</html:select>
			</td>			
			<td class="labelcell" align="right" height="35" ><bean:message key="BankAccount"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="bankAccount" styleId="bankAccount" style="width:170px" styleClass="bigcombowidth" onchange="getAccountBalance(this)">
				<html:option value=''>--Choose--</html:option>	
				</html:select>
			</td>			
	 	</tr>
	 	
	 	<tr>
		 	<td class="labelcell" align="right" height="35" ><bean:message key="Balance"/> &nbsp;&nbsp;</td> 
			<td colspan="2" class="fieldcell" align="left" ><html:text  property="balance" style="text-align:right" readonly="true" tabindex="-1"/></td>
			<td></td>
			<td></td>
	 	</tr>	 	
	 	
	 	<tr id="chqRow" name="chqRow">
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeNumber"/>  <SPAN id="c1" class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="chequeNo" /></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail"><A onclick=nextChqNo() href="#"><IMG id="img" height=22 src="../img/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" valign="center" style="width:100px" id="srndrChk"><html:checkbox property="isChqSurrendered" styleId="isChqSurrendered" onclick="disableNextCheque(this)"/>Is&nbsp;Surrendered&nbsp;(Y/N) </td>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeDate"/>  <SPAN id="c2" class="leadon">*</SPAN>&nbsp;</td>
	 		<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');"/>
			</td><td><a href="javascript:show_calendar('AdvanceDisbursementByChequeForm.chequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img2" src="../images/calendar.gif" width=24 height=22 border=0></a>
			</td>
			</tr></table></td>
	 	</tr>
 
	 	<tr id="newChqRow" name="newChqRow" style="display:none">
	 		<td class="labelcell" align="right" height="35" ><bean:message key="NewChequeNumber"/>   <SPAN class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="newChequeNo"/></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail1"><A onclick=nextNewChqNo() href="#"><IMG height=22 src="../img/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" align="right" height="35" ><bean:message key="ChequeDate"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="newChequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');" />
			</td><td><a href="javascript:show_calendar('AdvanceDisbursementByChequeForm.newChequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img3" src="../images/calendar.gif" width=24 height=22 border=0></a>
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
<tr >
<td colspan=5>
<table width="100%" align=center border="0" cellpadding="0" cellspacing="0" >
<tr  height="25">
 <TD class=displaydata align=middle ><h4><bean:message key="GLEntry"/> </h4></TD></tr>
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

<tr>
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
<html:javascript formName="AdvanceDisbursementByChequeForm"/> 
 

 <%
 request.getSession().removeAttribute("advSanctionList");
 %>
   </html:form >
  </body>
</html>	
