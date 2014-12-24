<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.math.BigDecimal" %>
				
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
  
<style type="text/css">

</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>	
<script>
function validateFormAndSubmit(){
	if(document.getElementById("assetPreparedBy").value=='-1'){
		dom.get("page_error").style.display='';
		document.getElementById("page_error").innerHTML='Please select Asset Capitalisation By'
		document.getElementById("assetPreparedBy").focus();
		return false;
	}
	if(document.getElementById("dateOfCapitalisation").value==''){
		dom.get("page_error").style.display='';
		document.getElementById("page_error").innerHTML='Please select Date Of Capitalisation'
		document.getElementById("dateOfCapitalisation").focus();
		return false;
	}
	if( document.getElementById("assetIndrctExpns").value==''){
		dom.get("page_error").style.display='';
		document.getElementById("page_error").innerHTML='Please enter the asset indirect expense value'
		document.getElementById("assetIndrctExpns").focus();
		return false;
	}
 	var msg = "The voucher will be generated. Do you want to Continue.";
 	var ans=confirm(msg);	
	if(ans) {
		document.capitalisationDetailForm.action='${pageContext.request.contextPath}/capitaliseAsset/capitaliseAsset!capitaliseAsset.action';
 		document.capitalisationDetailForm.submit();
	}
	else {
		return false;		
	}
 
}


function setDefaultDate(){
	var cdate=document.getElementById('dateOfCapitalisation').value;
	if(cdate=='') {
		document.getElementById('dateOfCapitalisation').value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}else{	
		document.getElementById('dateOfCapitalisation').value=cdate;
	}
}

designationLoadHandler = function(req,res){
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("page_error").style.display='';
	document.getElementById("page_error").innerHTML='Unable to load designation';
}

function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/capitaliseAsset/ajaxCapitaliseAsset!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}
function validateIndrctExpns(){
	dom.get("page_error").style.display='none';
	var totalExpns = <s:property value="%{totalIndrctExpns}" />;
	var assetIndrctExpns = document.getElementById("assetIndrctExpns").value;
	if(parseFloat(totalExpns)<parseFloat(assetIndrctExpns)){
		 dom.get("page_error").style.display='';
		 document.getElementById("page_error").innerHTML="asset indirect expenses exceeds the total project indirect expenses";
		 document.getElementById("assetIndrctExpns").value ="";
	}
	var totalAssetVal = parseFloat(assetIndrctExpns) + parseFloat(<s:property value="%{totalCapitalisationValue}" />);
        document.getElementById("totalAssetVal").value = totalAssetVal.toFixed(2);
	
}

</script>
<html>
<head>
	<title><s:text name="page.title.asset.capitalisation.details" /></title>
</head>
<body onload="setDefaultDate();">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
	<s:form action="capitaliseAsset" theme="simple"
		name="capitalisationDetailForm">
		<div class="errorstyle" id="page_error" style="display:none;"></div>
		<!-- s:push value="model"-->
		&nbsp;
		<div class="formmainbox">
		<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2">
		<div></div>
		</div>
		<s:hidden name="assetId" value="%{assetId}"/>
		<div class="rbcontent2">
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="catSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="cap.details" />
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td>
									<table>
										<tr>
											<td width="25%" class="whiteboxwk">
												<s:text name="asset.code" />:
											</td>
											<td width="25%" class="whitebox2wk">
												<s:property value="asset.code"/>
											</td>
											<td width="25%" class="whiteboxwk">
												<s:text name="asset.name" />:
											</td>
											<td width="25%" class="whitebox2wk">
												<s:property value="asset.name"/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="greyboxwk">
												<s:text name="asset.status" />:
											</td>
											<td  width="25%" class="greybox2wk">
												<s:property value="asset.status.description"/>
											</td>
											<td width="25%" class="greyboxwk">
												<s:text name="schedCategory.description" />:
											</td>
											<td  width="25%" class="greybox2wk">
												<s:property value="asset.assetCategory.name"/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="whiteboxwk">
												<span class="mandatory">*</span> 
												<s:text name="date.of.capitalisation" />:
											</td>
											<td width="25%" class="whitebox2wk" colspan="3">
												<s:date name="dateOfCapitalisation" var="dateOfCapitalisationFormat"
													format="dd/MM/yyyy" />
												<s:textfield name="dateOfCapitalisation" id="dateOfCapitalisation"
													value="%{dateOfCapitalisationFormat}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].dateOfCapitalisation',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" />
												</a>
											</td>
										</tr>
										<tr>
							                <td width="17%" class="greyboxwk">
												<span class="mandatory">*</span>
												<s:text	name="asset.capitaliseby" />:
											</td>
											<td width="25%" class="greybox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="preparedId"
													value="%{preparedId}"
													id="assetPreparedBy" cssClass="selectwk"
													list="dropdownData.preparedByList" listKey="id"
													listValue="employeeName" onchange='showDesignation(this);' />
											</td>
											<td width="25%" class="greyboxwk">
												<s:text name="emp.designation" />
												:
											</td>
											<td width="25%" class="greybox2wk">
												<s:textfield value="%{assetPreparedByView.desigId.designationName}"
													type="text" disabled="true"
													cssClass="selectboldwk" id="designation" />
											</td>
							            </tr>
										<tr>
											<td width="25%" class="whiteboxwk">
												<s:text name="asset.remark" />:
											</td>
											<td colspan="3" width="21%" class="whitebox2wk">
												<s:textarea name="remark" value="%{remark}" cols="30" rows="3"/>
											</td>
										</tr>
										<tr>
											<td colspan="4">
												<div align="right" class="mandatory"
													style="font-size: 11px; padding-right: 20px;">
													*
													<s:text name="default.message.mandatory" />
												</div>
											</td>
										</tr>
										</table>
										<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
													
														<tr>
															<td class="tablesubheadwk">
																Estimate Name
															</td>
															<td  class="tablesubheadwk">
																Project Code
															</td>
															<td   class="tablesubheadwk">
																Bill Number
															</td>
															<td  class="tablesubheadwk">
																Bill Date
															</td>
															<td  class="tablesubheadwk">
																Amount
															</td>
															<td  class="tablesubheadwk">
																Debit Account code
															</td>
															
															<td  class="tablesubheadwk">
																Voucher Number
															</td>
															<td  class="tablesubheadwk">
																Voucher Date
															</td>
															
														</tr>
														<s:iterator id="iterator" value="capitalisationDetails"
															status="row_status">
															<tr>
																<td  ><s:property value="%{workOrderEstimate.estimate.estimateNumber}" /></td>
																<td ><s:property value="%{workOrderEstimate.estimate.projectCode.code}" /></td>
																<td ><s:property value="%{egbill.billnumber}" /></td>
																<td><s:date name="egbill.billdate"  format="dd/MM/yyyy"/></td>
																<td align="right"><s:property value="%{amount}" /></td>
														
																<td  align="center"><s:property value="%{coa.glcode}"/>-<s:property value="%{coa.name}" /></td>
																<td  align="center"><s:property value="%{egbill.egBillregistermis.voucherHeader.voucherNumber}" /></td>
																<td ><s:date name="egbill.egBillregistermis.voucherHeader.voucherDate"  format="dd/MM/yyyy" /></td>
																
																
															</tr>
														</s:iterator>
												
													
														<tr >
														<td></td><td></td><td></td>
															<td>
																Total Capitalisation Value:  
															</td>
															<td align="right">
																<s:property value="%{totalCapitalisationValue}" />
															</td>
														</tr>
													<tr >
														<td></td><td></td><td></td>
															<td>
																Other Project Expenses:  
															</td>
															<td align="right">
																<s:property value="%{totalIndrctExpns}" />
																<s:hidden name="totalIndrctExpns"></s:hidden>
															</td>
														</tr>
													<tr >
														<td></td><td></td><td></td>
															<td>
																Expenses towards Asset:  
															</td>
															<td align="right">
																<s:textfield name="assetIndrctExpns"  id="assetIndrctExpns" style="text-align:right" onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);validateIndrctExpns();"/>
															</td>
														</tr>
<tr >
														<td></td><td></td><td></td>
															<td>
																Total Project Value:  
															</td>
															<td align="right">
																<s:textfield name="totalAssetVal"  id="totalAssetVal" style="text-align:right" readOnly="true"/>
															</td>
														</tr>
													
												</table>
											</td>
										</tr>
										
									</table>
								</td>
							</tr>
							<s:hidden name="projectType"id="projectType"/>
							<tr>
							<td align="center" colspan="4">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Capitalise Asset" id="addButton"
									name="capitaliseAssetButton" onclick="validateFormAndSubmit()"
									align="center" />
								<input type="button" class="buttonfinal" value="CANCEL"
									id="closeButton" name="button"
									onclick="window.close();" />
							</P>
							</td>
							</tr>
						</table>
				
		</div>
		<div class="rbbot2">
		<div></div>
		</div>
		</div>
		</div>
		</div>
		<!-- /s:push-->
	</s:form>
<script>
function validateDigitsAndDecimal(obj)
{
	if(isNaN(obj.value.trim()))
	{
		alert("Invalid Amount")
		obj.value=0;
		obj.focus();
	}
	else
	{
		if(parseFloat(obj.value.trim())<0)
		{
			alert("Negetive Amount is not allowed");
			obj.value=0;
			obj.focus();
		}
		else
		{
			obj.value=obj.value.replace("+","");	
			obj.value=obj.value.trim();
			if(isNaN(parseFloat(obj.value)))
			{
				obj.value=0;	
			}
			else
			{
				var resultNum = parseFloat(obj.value);
				obj.value=amountConverter(resultNum);
			}
		}
	}
}
function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
}
</script>
</body>
</html>