<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
  

<html>
<head>
	  <s:if test="%{rptNo == '26C'}"><title><s:text name="form26.report.title" /></title> </s:if>  <s:elseif test="%{rptNo == '42'}"> <title><s:text name="form42.report.title" /></title> </s:elseif>
</head>
<body>
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

	<s:form action="taxDeductionStatement" theme="simple" name="taxDeductionStatementForm">
		<div class="formmainbox">
		<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
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
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="search.criteria" />
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
					<tr>
						<td class="whiteboxwk"> <span class="mandatory">*</span>From Date</td>
						<td class="whitebox2wk"><s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy"/>
						<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
						<a href="javascript:show_calendar('taxDeductionStatementForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
						</td>
						<td class="whiteboxwk"><span class="mandatory">*</span>To Date</td>
						<td class="whitebox2wk">
						<s:date name="toDate" id="toDateId" format="dd/MM/yyyy"/>
						<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
						<a href="javascript:show_calendar('taxDeductionStatementForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
					</tr>
					<tr>
						<td class="greyboxwk"><span class="mandatory">*</span>Recovery Code</td>
						<td class="greybox2wk"><s:select headerKey=""headerValue="%{getText('list.default.select')}"name="recoveryglcodeId" 	
							id="recoveryglcodeId" cssClass="selectwk"list="dropdownData.recoveryList" listKey="chartofaccounts.id"listValue='type'/></td>
						<td class="greyboxwk"></td>
						<td class="greybox2wk"></td>
					</tr>
					<tr>
            			<td colspan="4"><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          			</tr>
					<s:hidden name="rptNo" id="rptNo"></s:hidden>
				</table>
				</table>
				<table width="100%">
					<tr>
						<td colspan="4">
							<div class="buttonholderwk">
								<s:submit cssClass="buttonfinal" value="SEARCH"
									id="submitButton" method="list" onclick="return validate();"/>
								<input type="button" class="buttonfinal" value="CLOSE"
									id="closeButton" name="button" onclick="window.close();" />
							</div>
						</td>
					</tr>
				</table>
					
		<div id="loading" class="loading" style="width: 700; height: 700;display: none " align="center" >
				<blink style="color: red">Searching processing, Please wait...</blink>
			</div> <br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
			<s:if test="%{searchResult.fullListSize != 0}">
			<tr align="center">
				<td >
					<display:table name="searchResult" id="searchResultid" uid="currentRowObject" cellpadding="0" cellspacing="0"
					requestURI="" sort="external"  style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
										
						<display:column  title="Serial No. (1)" headerClass="pagetableth" class="pagetabletd" style="width:3%;text-align:left">
								<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
						</display:column>			
							
						<display:column  title="Name and Address of the contractor/sub-contractor to whom payment is made (2)" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left" property="name" />
						<s:if test="%{rptNo == '26C'}">
							<display:column  title="Gross Value of the contract (3)" headerClass="pagetableth" class="pagetabletd" style="width:8%;text-align:right" property="grossValue" />
						</s:if>
						<s:else>
							<display:column  title="Gross Value of the contract and its description (3)" headerClass="pagetableth" class="pagetabletd" style="width:8%;text-align:right" property="grossValue" />
						</s:else>
						<display:column  title="Amount credited or paid in pursuance of contract (4)" headerClass="pagetableth" class="pagetabletd" style="width:8%;text-align:right" property="netPayble" />	
						<display:column title="Date of credit of payment (5)" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left"  property="paymentDate" />				
						<display:column  title="Date on which tax was deducted at source (6)" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left" property="voucherDate"/>					
						<display:column  title="Amount of tax deducted at source (7)" headerClass="pagetableth" class="pagetabletd" style="width:7%;text-align:right" property="taxDeductedAmt"/>	
						<display:column title="Date on which the tax deducted at source was paid to the credit of central Government (8)" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left" property="remittedDate"   />				
						<s:if test="%{rptNo == '26C'}">
						<display:caption  media="html"style='font-weight:bold ' >
								 FORM No.  26 C <br> SEE RULE 37 (2-C) <br> 
		 Statement of deduction of tax under section 194-C of the Income tax act 1961 from payment made to contractors or sub-contractors Name 
		 and Address of person responsible for making the payment : NAGPUR MUNICIPAL CORPORATION, NAGPUR.
						   </display:caption> 
						</s:if>
						<s:elseif test="%{rptNo == '42'}">
						  
		
						<display:caption media="html"style='font-weight:bold ' >
								   FORM No XXXXII  <br> SEE Section6(3) Under work Contract Act  <br>
		 Statement of deduction of tax under section 14 of the Maharashtra tax laws(levy,Amendment and validation) act 19 from payment made to contractors or sub-contractors <br> Name and Address of person responsible for making the payment : NAGPUR MUNICIPAL CORPORATION, NAGPUR.
						   </display:caption> 
						</s:elseif>
					  
					</display:table>
					
				  </td>
			 </tr>
			 </table>
			 <br>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			 	<tr  align = "center">
			 		<td colspan="7" align="center">
		     			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" onclick="return validate();" method="exportToPdf" name="button" />
		     			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" onclick="return validate();" method="exportToExcel" name="button" />
		     			<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;"># <s:text name="lwc.report.footnote" /></div>
		     		</td>	
    			</tr>
    		</table>
			</s:if>
			<s:elseif test="%{searchResult.fullListSize == 0}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="7" align="center"><font color="red">No record Found.</font></td>
					</tr>
				</table>
			</s:elseif>
		</table>
		</div>
		</div>
		</div>
		</div>
	</s:form>
<script>
	function validate(){
		document.getElementById('lblError').innerHTML = "";
		if(document.getElementById('fromDate').value == ""){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please enter from date";
			return false;
		}
		else if(document.getElementById('toDate').value == ""){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please enter to date";
			return false;
		}
		if(document.getElementById('recoveryglcodeId').value == "")
		{
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please select recovery code";
			return false;
		}	
	return true;
	}
</script>
</body>
</html>