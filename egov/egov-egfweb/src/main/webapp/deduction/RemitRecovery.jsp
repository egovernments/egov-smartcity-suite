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
<%@ page import="java.util.*,java.text.*,
org.egov.infstr.utils.EgovMasterDataCaching,
org.egov.deduction.client.RemitRecoveryForm,
org.egov.infstr.utils.EGovConfig"%>
<html>
<head>
<title>Create Remit Recovery</title>

<%
	RemitRecoveryForm rrf=(RemitRecoveryForm)request.getAttribute("RemitRecoveryForm");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>
<script>
var m_names = new Array("","January", "February", "March", 
"April", "May", "June", "July", "August", "September", 
"October", "November", "December");
var recoveryGlcode;
var recoveryGlname;
function onBodyLoad()
{	
	<% 
		ArrayList fundList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund");			
		String autoGenerateChqNo= EGovConfig.getProperty("egf_config.xml","autoGenerateChqNo","","autoGenerateCheck");		
	%>	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		bootbox.alert("<%=request.getAttribute("alertMessage")%>");		
	}
	var buttonType="${buttonType}";	
	if(buttonType == "saveclose")
	{
		window.close();
	}
	if(buttonType == "saveview")
	{		
		var remittanceId="<%=request.getAttribute("remittanceId")%>";
		document.RemitRecoveryForm.action = "../deduction/remitRecovery.do?submitType=beforePrintRemittanceRecovery&remittanceId="+remittanceId,"";
		document.RemitRecoveryForm.submit();
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
		document.RemitRecoveryForm.month.value = parseInt("<%=rrf.getMonth()%>")-1;		
		document.RemitRecoveryForm.year.value = "<%=rrf.getYear()%>";		
	}
	if(mode == "create")
	{	
	<% 	if(rrf.getPartyName()!=null)
		{
	%>				
			document.getElementById("srchRow").style.display="block";
			document.getElementById("detailRow").style.display="block";
			document.getElementById("totalRow").style.display="block";
			document.getElementById("pymntVhRow").style.display="block";
			document.getElementById("msgRow").style.display="none";
			document.getElementById("row2").style.display="block";			
			document.getElementById("row3").style.display="none";
			document.getElementById("row4").style.display="none";
			document.getElementById("srndrChk").style.display="none";
			getRemittTo();
			document.RemitRecoveryForm.month.value = parseInt("<%=rrf.getMonth()%>");	
			document.RemitRecoveryForm.pymntVhDate.value = "<%=currDate%>";	
			document.RemitRecoveryForm.fund.disabled =true;		
			document.RemitRecoveryForm.recovery.disabled =true;
			document.RemitRecoveryForm.month.disabled =true;		
			document.RemitRecoveryForm.year.disabled =true;
			if("<%=autoGenerateChqNo%>"=="Y")
			{
				document.getElementById("chqRow").style.display="none";
			}
			var table=document.getElementById('details');
			for(var i=0;i<table.rows.length;i++)
			{
				table.rows[i].cells[0].style.display="none";
				table.rows[i].cells[1].style.display="none";
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
		window.location="../deduction/remitRecovery.do?submitType=beforeCreateRemitRecovery";	
		document.getElementById("msgRow").style.display="none";

	}
	if(mode == "modify")
	{		
		document.title="Modify Remit Recovery";
		//document.getElementById('screenName').innerHTML="Modify Remit Recovery";
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="block";
		document.getElementById("row4").style.display="none";
		document.getElementById("srchRow").style.display="none";
		document.getElementById("detailRow").style.display="block";
		document.getElementById("totalRow").style.display="block";
		document.getElementById("pymntVhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";
		//document.getElementById("srndrChqRow").style.display="block";
		document.getElementById("pymntVhNoPrefix").style.display="block";
		getRemittTo();
		document.RemitRecoveryForm.fund.disabled =true;		
		document.RemitRecoveryForm.recovery.disabled =true;
		document.RemitRecoveryForm.month.disabled =true;		
		document.RemitRecoveryForm.year.disabled =true;
		document.RemitRecoveryForm.bank.disabled =true;	
		document.RemitRecoveryForm.bankAccount.disabled =true;	
		document.RemitRecoveryForm.chequeNo.disabled =true;		
		document.RemitRecoveryForm.chequeDate.disabled =true;	
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("img2").style.display="none";
		var table=document.getElementById('details');
		for(var i=0;i<table.rows.length;i++)
		{
			table.rows[i].cells[0].style.display="none";
			table.rows[i].cells[1].style.display="none";
		}		
				
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.RemitRecoveryForm.fund.value+' order by id', 'bank', 'bankAccount');				
		document.RemitRecoveryForm.bankAccount.value="<%=rrf.getBankAccount()%>";
		var vnum=document.RemitRecoveryForm.pymntVhNo.value;
		var subVnum=document.RemitRecoveryForm.pymntVhNo.value.substring(0,2);
		document.RemitRecoveryForm.pymntVhNo.value=vnum.substring(2,vnum.length);
		document.RemitRecoveryForm.pymntVhNoPrefix.value=subVnum;
	}
	if(mode == "view")
	{		
		document.title="View Remit Recovery";
		//document.getElementById('screenName').innerHTML="View Remit Recovery";
		document.getElementById("row4").style.display="block";	
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="none";
		document.getElementById("srchRow").style.display="block";
		document.getElementById("detailRow").style.display="block";
		document.getElementById("totalRow").style.display="block";
		document.getElementById("pymntVhRow").style.display="block";
		document.getElementById("msgRow").style.display="none";
		document.getElementById("chqAvail").style.display="none";
		document.getElementById("chqAvail1").style.display="none";
		document.getElementById("img1").style.display="none";
		document.getElementById("img2").style.display="none";
		document.getElementById("img3").style.display="none";
		document.getElementById("c1").style.display="none";
		document.getElementById("c2").style.display="none";
		//document.getElementById("srndrChk").style.display="none";		
		if(document.RemitRecoveryForm.isChqSurrendered.checked==true)
		{
			document.getElementById("newChqRow").style.display="block";
		}

		getRemittTo();
		var table=document.getElementById('details');
		for(var i=0;i<table.rows.length;i++)
		{
			table.rows[i].cells[0].style.display="none";
			table.rows[i].cells[1].style.display="none";
		}

		for(var i=0;i<document.RemitRecoveryForm.length;i++)
		{
			if(document.RemitRecoveryForm.elements[i].value != "  Close  ")
			{
				document.RemitRecoveryForm.elements[i].disabled =true;
			}					
		}
		loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.RemitRecoveryForm.fund.value+' order by id', 'bank', 'bankAccount');				
		document.RemitRecoveryForm.bankAccount.value="<%=rrf.getBankAccount()%>";		
	}
}

function ButtonPress(arg)
{	
	var mode="${mode}";
	if(document.RemitRecoveryForm.fund.value == "0")
	{
		bootbox.alert("Select Fund");
		var temp="document.RemitRecoveryForm.fund.focus();";
		setTimeout(temp,0);		
		return;
	}
	if(document.RemitRecoveryForm.recovery.value == "0")
	{
		bootbox.alert("Select Recovery Name");		
		var temp="document.RemitRecoveryForm.recovery.focus();";
		setTimeout(temp,0);	
		return;
	}
	if(document.RemitRecoveryForm.month.value == "" || document.RemitRecoveryForm.month.value == "0")
	{
		bootbox.alert("Select Month");		
		var temp="document.RemitRecoveryForm.month.focus();";
		setTimeout(temp,0);	
		return;
	}
	
	if(document.RemitRecoveryForm.year.value == "0")
	{
		bootbox.alert("Select Year");		
		var temp="document.RemitRecoveryForm.year.focus();";
		setTimeout(temp,0);	
		return;
	}
	
	if(arg=="search")
	{
		document.RemitRecoveryForm.fund.disabled =false;		
		document.RemitRecoveryForm.recovery.disabled =false;
		document.RemitRecoveryForm.month.disabled =false;		
		document.RemitRecoveryForm.year.disabled =false;
		document.RemitRecoveryForm.action = "../deduction/remitRecovery.do?submitType=search";
		document.RemitRecoveryForm.submit();
		return;
	}
	if(!validate())
		return false;	

	document.getElementById("button").value=arg;
	if(!validateRemitRecoveryForm(document.RemitRecoveryForm))
		return;
	if("<%=autoGenerateChqNo%>"!="Y" && mode == "create")
	{
		if(document.RemitRecoveryForm.chequeNo.value=="")
		{
			bootbox.alert("Select Cheque Number!!");
			var temp="document.RemitRecoveryForm.chequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.RemitRecoveryForm.chequeDate.value=="")
		{
			bootbox.alert("Enter Cheque Date!!");
			var temp="document.RemitRecoveryForm.chequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
	}
	else if("<%=autoGenerateChqNo%>"!="Y" && mode == "modify" && document.RemitRecoveryForm.isChqSurrendered.checked==true)
	{
		if(document.RemitRecoveryForm.newChequeNo.value=="")
		{
			bootbox.alert("Select New Cheque Number!!");			
			var temp="document.RemitRecoveryForm.newChequeNo.focus();";
			setTimeout(temp,0);	
			return;
		}
		if(document.RemitRecoveryForm.newChequeDate.value=="")
		{
			bootbox.alert("Enter New Cheque Date!!");
			var temp="document.RemitRecoveryForm.newChequeDate.focus();";
			setTimeout(temp,0);	
			return;
		}
	}
	
	document.RemitRecoveryForm.fund.disabled =false;		
	document.RemitRecoveryForm.recovery.disabled =false;
	document.RemitRecoveryForm.month.disabled =false;		
	document.RemitRecoveryForm.year.disabled =false;	
	if(mode == "create")
	{
		document.RemitRecoveryForm.action = "../deduction/remitRecovery.do?submitType=createRemitRecovery";
		document.RemitRecoveryForm.submit();	
	}
	if(mode != "create")
	{
		var id= "<%=request.getParameter("id")%>";
		document.RemitRecoveryForm.bank.disabled =false;	
		document.RemitRecoveryForm.bankAccount.disabled =false;	
		document.RemitRecoveryForm.chequeNo.disabled =false;		
		document.RemitRecoveryForm.chequeDate.disabled =false;
		document.RemitRecoveryForm.action = "../deduction/remitRecovery.do?submitType=modifyRemitRecovery&id="+id;
		document.RemitRecoveryForm.submit();
	}	
}

function validate()
{
	if(document.RemitRecoveryForm.bank.value == "0")
	{
		bootbox.alert("Select Bank");
		var temp="document.RemitRecoveryForm.bank.focus();";
		setTimeout(temp,0);		
		return;
	}
	if(document.RemitRecoveryForm.bankAccount.value == "")
	{
		bootbox.alert("Select Bank Account");		
		var temp="document.RemitRecoveryForm.bankAccount.focus();";
		setTimeout(temp,0);	
		return;
	}
	return true;
}
function nextChqNo()
{
	document.RemitRecoveryForm.chequeNo.value="";
	var obj=document.getElementById("bankAccount");
	var bankBr=document.getElementById("bank");
	if(bankBr.value=="0")
	{
		bootbox.alert("Select Bank and Bank Account!!");
	  return;
	}

	if(obj.value=="")
	{
		bootbox.alert("Select Bank Account!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.value;
	var sRtn =showModalDialog("../HTML/SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=undefined) 
		document.RemitRecoveryForm.chequeNo.value=sRtn;
}

function nextNewChqNo()
{
	document.RemitRecoveryForm.newChequeNo.value="";
	var obj=document.getElementById("bankAccount");
	var bankBr=document.getElementById("bank");
	if(bankBr.value=="0")
	{
		bootbox.alert("Select Bank and Bank Account!!");
	  return;
	}

	if(obj.value=="")
	{
		bootbox.alert("Select Bank Account!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.value;
	var sRtn =showModalDialog("../HTML/SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=undefined) 
		document.RemitRecoveryForm.newChequeNo.value=sRtn;
}


function getTotalRemittAmt()
{	
	var tbl=document.getElementById('details');
	var totalRemittAmt=0.00;	
	for(i=2;i<tbl.rows.length;i++)
	{	
		 if(getControlInBranch(tbl.rows[i],'remittAmt').value !="")
		 {

		 	if(isNaN(getControlInBranch(tbl.rows[i],'remittAmt').value))
			{			
		 		bootbox.alert("Remitt Amount must be an numeric value");	
				getControlInBranch(tbl.rows[i],'remittAmt').value="";			
				getControlInBranch(tbl.rows[i],'remittAmt').focus();
				return;
			}
			else
			{
				totalRemittAmt= eval(totalRemittAmt)+ eval(getControlInBranch(tbl.rows[i],'remittAmt').value);					
			}	
		}	
	}	
	document.RemitRecoveryForm.totalRemittAmt.value=totalRemittAmt.toFixed(2);
}

function validateRmtAmt(obj)
{
	var rmtAmt=obj.value;
	var mode="${mode}";	
	var tbl=document.getElementById('details');
	for(i=2;i<tbl.rows.length;i++)
	{
		if(rmtAmt!="")
		{			
			if(isNaN(getControlInBranch(tbl.rows[i],'remittAmt').value))
			{			
				bootbox.alert("Remitt Amount must be a positive numeric value");	
				getControlInBranch(tbl.rows[i],'remittAmt').value="";			
				getControlInBranch(tbl.rows[i],'remittAmt').focus();
				return;
			}
			else if(parseFloat(getControlInBranch(tbl.rows[i],'remittAmt').value)<0)
			{
				bootbox.alert("Remitt Amount must be a positive numeric value");	
				getControlInBranch(tbl.rows[i],'remittAmt').value="";			
				getControlInBranch(tbl.rows[i],'remittAmt').focus();
				return;
			}
			else
			{
				if(mode=="create")
				{
					<%
					if(rrf.getPartyName()!=null)
					{
						for(int j=0; j<rrf.getPartyName().length;j++)
						{				
					%>
							if("<%=rrf.getRemittanceGldtlId()[j]%>"==getControlInBranch(tbl.rows[i],'remittanceGldtlId').value)
							{
								if(eval(getControlInBranch(tbl.rows[i],'remittAmt').value)>eval("<%=rrf.getRemittAmt()[j]%>"))
								{
									bootbox.alert("Remitt Amount cannot be greater than the sum of Deducted Amount and already Remitted Amount!!");
									getControlInBranch(tbl.rows[i],'remittAmt').value="";
									getControlInBranch(tbl.rows[i],'remittAmt').focus();
									return false;
								}
							}
					<%	}
					}%>
				}
				else if(mode=="modify")
				{
					<c:forEach var="rd" items="${rmtDtlList}" > 			
						if(getControlInBranch(tbl.rows[i],'remittanceGldtlId').value== "${rd.egRemittanceGldtl.id}")
						{	
							if((eval(getControlInBranch(tbl.rows[i],'remittAmt').value)-eval("${rd.remittedamt}"))>(eval("${rd.egRemittanceGldtl.gldtlamt}")-eval("${rd.egRemittanceGldtl.remittedamt}")))
							{
								bootbox.alert("Remitt Amount cannot be greater than the sum of Deducted Amount and already Remitted Amount!!");
								getControlInBranch(tbl.rows[i],'remittAmt').value="";
								getControlInBranch(tbl.rows[i],'remittAmt').focus();
								return false;
							}											
						}
					</c:forEach> 
				}
			}		
		}
	}
}

function onClickCancel()
{
	document.RemitRecoveryForm.reset();
}
 
function onClickCancelSearch()
{
	window.location="../deduction/remitRecovery.do?submitType=beforeCreateRemitRecovery";	
}

function getRemittTo()
{
	var recovery=document.RemitRecoveryForm.recovery.value;
	if(recovery!=0)
	{
		<c:forEach var="rec" items="${recoveryList}" > 			
			if(recovery== "${rec.id}")
			{	
				document.RemitRecoveryForm.remitTo.value="${rec.remitted}";
				recoveryGlcode="${rec.chartofaccounts.glcode}";
				recoveryGlname="${rec.chartofaccounts.name}";		
				if("${rec.bank}"!='')	
				{		
					document.getElementById("bankLoan").style.display="block";
					document.RemitRecoveryForm.bankName.value="${rec.bank.name}";
				}
				else
				{
					document.getElementById("bankLoan").style.display="none";
				}
			}
		</c:forEach> 
	}
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
	var remittAmount=document.RemitRecoveryForm.totalRemittAmt.value;	
	if(remittAmount>0)
	{
		var Obj=getControlInBranch(tableObj.rows[1],"display_Code");
		trObj=getRow(Obj);
		var newRow=trObj;		
		objt1=getControlInBranch(newRow,'display_Code');
		objt2=getControlInBranch(newRow,'display_Head');
		objt3=getControlInBranch(newRow,'display_Debit');
		objt4=getControlInBranch(newRow,'display_Credit');
		if(recoveryGlcode=="") 
			recoveryGlcode=" -"; 
		if(recoveryGlname=="") 
			recoveryGlname=" -";
		objt1.innerHTML=recoveryGlcode;
		objt2.innerHTML=recoveryGlname;
		objt3.innerHTML=remittAmount;
		objt4.innerHTML="0";
	}	
	
	var accountId=document.RemitRecoveryForm.bankAccount.value;
	var accountGlcode, accountGlname;
	if(accountId != '')
	{		
		var url = "../commons/Process.jsp?accountId=" +accountId+ "&type=getBankAccountGlcode";
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
	if(remittAmount>0)
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
		objt4.innerHTML=remittAmount;
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

function print()
{		
	window.open("../deduction/Reports/RemittanceRecoveryReport.jsp","","height=650,width=980,scrollbars=yes,left=30,top=30,status=yes");						
}


</script>

</head>   

<body onload="onBodyLoad()" onKeyDown ="CloseWindow(event);" >
<html:form  action="/deduction/remitRecovery">
 <table align='center' class="tableStyle" id="mainTable" name="mainTable">
 
 <tr>
 <td> 
 	 <table>
 	 <table>
	 	<tr>
	       	<td colspan="5">&nbsp;</td> 	
		</tr>	
		<tr>
	 		<td class="labelcell" align="right" height="35" >Fund<SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="fund" styleClass="combowidth1">
				<html:option value='0'>--Choose--</html:option>	
				<c:forEach var="fund" items="<%=fundList%>" > 
					<html:option value="${fund.id}">${fund.name}</html:option>
				</c:forEach> 
				</html:select>
			</td>			
			<td class="labelcell" align="right" height="35" >Recovery Name<SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="recovery" styleClass="combowidth1" onchange="getRemittTo()" >
				<html:option value='0'>--Choose--</html:option>	
					<c:forEach var="rec" items="${recoveryList}" > 
						<html:option value="${rec.id}">${rec.type}</html:option>
					</c:forEach> 
				</html:select>
			</td>				
	 		<td class="fieldcell" id="bankLoan" style="display:none"><input type="text" id="bankName" name="bankName" readonly="true" tabindex="-1"/></td>		
	 	</tr> 
	 	
		<tr>
	 		<td class="labelcell" align="right" height="35" >Month<SPAN class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="smallfieldcell">
	 			<html:select  property="month" styleClass="combowidth1">
					<html:option value='0'>--Choose--</html:option>					
					<html:option value='1'>January</html:option>
					<html:option value='2'>February</html:option>	
					<html:option value='3'>March</html:option>
					<html:option value='4'>April</html:option>	
					<html:option value='5'>May</html:option>
					<html:option value='6'>June</html:option>
					<html:option value='7'>July</html:option>
					<html:option value='8'>August</html:option>
					<html:option value='9'>September</html:option>	
					<html:option value='10'>October</html:option>
					<html:option value='11'>November</html:option>
					<html:option value='12'>December</html:option>				
				</html:select>
			</td>	 		
	 		<td class="labelcell" align="right" height="35" >Year<SPAN class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="smallfieldcell">
	 			<html:select  property="year" styleClass="combowidth1" >
				<html:option value='0'>--Choose--</html:option>	
					<c:forEach var="fy" items="${finYearList}" > 
						<html:option value="${fy.id}">${fy.finYearRange}</html:option>
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
		</table>
		<table>
		</td>
		</tr>		
		<tr><td height="10"></td></tr>		
		<tr id="detailRow" name="detailRow" style="display:none">
		<td colspan=5 align="center">
		<table cellpadding="0" cellspacing="0" align="center" id="details" name="details" >
			<tr>
				<td class="thStlyle"><div align="center">RemGldtlId</div></td>
				<td class="thStlyle"><div align="center">RelId</div></td>
				<td colspan="3" class="thStlyle"><div align="center">Party</div></td>
				<td colspan="2" class="thStlyle"><div align="center">Reference</div></td>
				<td colspan="2" class="thStlyle"><div align="center">Amount</div></td>	
			</tr>
			<tr>
				<td class="thStlyle"><div align="center"></div></td>
				<td class="thStlyle"><div align="center"></div></td>
				<td class="thStlyle"><div align="center">Name</div></td>
				<td class="thStlyle"><div align="center">PAN/GIR #</div></td>
				<td class="thStlyle"><div align="center">Address</div></td>
				<td class="thStlyle"><div align="center">Doc No</div></td>
				<td class="thStlyle"><div align="center">Date</div></td>
				<td class="thStlyle"><div align="center">Deducted</div></td>
				<td class="thStlyle"><div align="center">To Remitt</div></td>				
			</tr>
		<% 	if(rrf.getPartyName()!=null)
			{
				for(int i=0; i<rrf.getPartyName().length;i++)
				{
		%>	

			<tr>
				<td><html:text property="remittanceGldtlId" value="<%=rrf.getRemittanceGldtlId()[i]%>"/></td>				
				<td><html:text property="relationId" value="<%=rrf.getRelationId()[i]%>"/></td>				
				<td class="tdStlyle"><div align="left" name="partyName" id="partyName" style="width:130px"><%=rrf.getPartyName()[i]%></div></td>	
				<td class="tdStlyle"><div align="left" name="partyPAN" id="partyPAN" style="width:85px" styleClass="narrationfieldinput2" readonly="true" tabindex="-1"/> <%=rrf.getPartyPAN()[i]%>  </td>
				<td class="tdStlyle"><div align="left" name="partyAddress" id="partyAddress" style="width:180px"><%=rrf.getPartyAddress()[i]%></div> </td>
				<td class="tdStlyle"><div align="left" name="refNo" id="refNo" style="width:90px"><%=rrf.getRefNo()[i]%></div></td>
				<td class="tdStlyle"><div align="left" name="refDate" id="refDate" style="width:70px"><%=rrf.getRefDate()[i]%></div></td>
				<td class="tdStlyle"><div align="left" name="dedAmount" id="dedAmount" style="width:80px;text-align:right"><%=rrf.getDedAmount()[i]%></div> </td>				
				<td class="tdStlyle"><html:text property="remittAmt" value="<%=rrf.getRemittAmt()[i]%>" style="width:80px;text-align:right" onblur="validateRmtAmt(this);getTotalRemittAmt()"/></td>				
			</tr>
			<%
			}
			}
			%>
			</table>
		</td>
		</tr>		
	
		<tr align="center" id="totalRow" name="totalRow" style="display:none">
		<td colspan=5>
		<table>	
			<tr>
				<td style="width:135px"></td>
				<td style="width:85px"></td>
				<td style="width:200px"></td>
				<td style="width:90px"></td>
				<td class="thStlyle"><div style="width:70px" align="center">TOTAL</div></td>
				<td class="tdStlyle"><div align="left" name="totalDedAmt" id="totalDedAmt" style="width:80px;text-align:right"><b><%=rrf.getTotalDedAmt()%></b> </div> </td>
				<td class="tdStlyle"><html:text property="totalRemittAmt" value="<%=rrf.getTotalRemittAmt()%>" readonly="true" tabindex="-1" style="width:80px;text-align:right"/></td>				
			</tr>			
		</table>
		</td>
		</tr>
		</table>
		</td>
		</tr>
				
		<tr id="pymntVhRow" name="pymntVhRow" style="display:none">
		<td colspan=5>
		<table>
		
		<tr>		
			<td class="labelcell" align="right" height="35" >Remit To&nbsp;&nbsp;</td>
	 		<td colspan="3" class="largeFieldcell"><html:text property="remitTo" readonly="true" tabindex="-1"/></td>
	 		<td></td>			
			<td></td>
		</tr>
		
		<tr>
			<td class="labelcell" align="right" height="35" >Voucher Number<SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td  colspan="2"><table><tr>
	 		<td class="smallfieldcell"><html:text property="pymntVhNoPrefix" style="width:20px;display:none" styleId="pymntVhNoPrefix" readonly="true" tabindex="-1"/>
	 		</td><td class="fieldcell"><html:text property="pymntVhNo" maxlength="10"/></td>	 		
	 		</tr></table></td>
			<td class="labelcell" align="right" height="35" >Voucher Date<SPAN class="leadon">*</SPAN>&nbsp;</td>				
			<td class="smallfieldcell" align="center" ><html:text property= "pymntVhDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('RemitRecoveryForm.pymntVhDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
			</td>
				 		
		</tr>	
		
		<tr>
	 		<td class="labelcell" align="right" height="35" >Bank<SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td colspan="2" class="smallfieldcell" align="left" >
				<html:select  property="bank" styleClass="bigcombowidth" style="width:250px" styleId="bank" onchange="loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'BANKACCOUNT', 'ID', 'ACCOUNTNUMBER', 'branchId=#1 and isactive=1 and fundid='+document.RemitRecoveryForm.fund.value+' order by id', 'bank', 'bankAccount')">
				<html:option value='0'>--Choose--</html:option>	
				<c:forEach var="bankBr" items="${bankBranchList}" > 
					<html:option value="${bankBr.id}">${bankBr.name}</html:option>
				</c:forEach> 				
				</html:select>
			</td>			
			<td class="labelcell" align="right" height="35" >Bank Account<SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="bankAccount" styleId="bankAccount" styleClass="bigcombowidth" style="width:220px">
				<html:option value=''>--Choose--</html:option>	
				</html:select>
			</td>			
	 	</tr>
	 	
	 	<!-- <tr id="srndrChqRow" name="srndrChqRow" style="display:none">-->
	 	<tr id="chqRow" name="chqRow">
	 		<td class="labelcell" align="right" height="35" >Cheque Number<SPAN id="c1" class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="chequeNo" /></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail"><A onclick=nextChqNo() href="#"><IMG id="img" height=22 src="/egi/resources/erp2/images/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" valign="center" style="width:100px" id="srndrChk"><html:checkbox property="isChqSurrendered" styleId="isChqSurrendered" onclick="disableNextCheque(this)"/>Is&nbsp;Surrendered&nbsp;(Y/N) </td>
	 		<td class="labelcell" align="right" height="35" >Cheque Date<SPAN id="c2" class="leadon">*</SPAN>&nbsp;</td>
	 		<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');"/>
			</td><td><a href="javascript:show_calendar('RemitRecoveryForm.chequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img2" src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
			</td>
			</tr></table></td>
	 	</tr>
 
	 	<tr id="newChqRow" name="newChqRow" style="display:none">
	 		<td class="labelcell" align="right" height="35" >New Cheque Number<SPAN class="leadon">*</SPAN>&nbsp;</td>
	 		<td class="fieldcell"><html:text property="newChequeNo"/></td>
	 		<td valign="center" style="width:100px" title="Show Next Cheque Number" nowrap id="chqAvail1"><A onclick=nextNewChqNo() href="#"><IMG height=22 src="/egi/resources/erp2/images/arrowright.gif" width=22 ></A></td>
	 		<td class="labelcell" align="right" height="35" >Cheque Date<SPAN class="leadon">*</SPAN>&nbsp;</td>
			<td><table><tr>		
			<td class="smallfieldcell" align="center" nowrap ><html:text property="newChequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="DateFormat(this,this.value,event,true,'3');" />
			</td><td><a href="javascript:show_calendar('RemitRecoveryForm.newChequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img nowrap id="img3" src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
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
	
  
<tr>
<td>
<table width="100%" border=0 cellpadding="3" cellspacing="0" id="showEntries" name="showEntries" style="DISPLAY: none">
<tr >
<td colspan=4>
<table width="100%" align=center border="0" cellpadding="0" cellspacing="0" >
<tr  height="25">
 <TD class=displaydata align=middle ><h4>GLEntry</h4></TD></tr>
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
	<td class="tdStlyle" ><div name="display_Code"  id="display_Code">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Head"  id="display_Head">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Debit"  id="display_Debit" style="text-align:right">&nbsp;</div></td>
	<td class="tdStlyle" ><div name="display_Credit"  id="display_Credit" style="text-align:right">&nbsp;</div></td>
	
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
 <html:javascript formName="RemitRecoveryForm"/> 
 </html:form >
 
  </body>
</html>	
