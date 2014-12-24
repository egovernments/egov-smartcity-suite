<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Modify Service Order</title>
</head>

<body class="yui-skin-sam">
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

	<s:form action="serviceOrder" theme="simple"name="serviceOrderForm">
		<s:push value="model">
		
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
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
										<s:text name="so.header.details"></s:text>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<tr>
				<td width="25%" class="whiteboxwk">	<s:text name="so.date"></s:text> </td>
				<td width="25%" class="whitebox2wk">
				<s:date name="serviceorderdate" id="serviceorderdateId" format="dd/MM/yyyy" />
				<s:textfield name="serviceorderdate" id="serviceorderdate" value="%{serviceorderdateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"  />
				<s:if test='%{value.equalsIgnoreCase("billed")}'> <script> dom.get('serviceorderdate').disabled=true;</script> </s:if>
				<a href="javascript:show_calendar('serviceOrderForm.serviceorderdate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
			
				</tr>
				<tr>
					<td class="greyboxwk" width="25%" ><span class="mandatory">*</span><s:text name="so.arch"></s:text> </td>
						<td class="greybox2wk" width="25%" ><s:select headerKey=""headerValue="%{getText('list.default.select')}"
						name="detailkeyid" id="detailkeyid" cssClass="selectwk"list="dropdownData.archtectList" listKey="id"
						listValue='name'/></td>
					<s:if test='%{value.equalsIgnoreCase("billed")}'> <script> dom.get('detailkeyid').disabled=true;</script> </s:if>
					<td class="greyboxwk" width="25%" ><span class="mandatory">*</span><s:text name="so.preparedby"></s:text></td>
						<td class="greybox2wk" width="25%" ><s:select headerKey=""headerValue="%{getText('list.default.select')}"
						name="preparedby" id="preparedby" cssClass="selectwk"list="dropdownData.prepareByList" listKey="id"
						listValue='userName' value='%{preparedby.id}'/>
				</td> 
				
				</tr>
				<tr>
					<td class="whiteboxwk" width="25%" >Department : </td>
						<td class="whitebox2wk" width="25%" ><s:property value="%{departmentId.deptName}" />
						<s:hidden name="departmentId" value="%{departmentId.id}" ></s:hidden> </td>
					<td class="whiteboxwk" width="25%"  ><s:text name="so.comments"></s:text></td>
					<td class="whitebox2wk" width="25%"  ><s:textarea name="serviceOrder.comments" id="comments" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td>
				</tr>
				</table></table>
				<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
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
									<s:text name="so.esttmptdtls"></s:text>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<c:set var="tdclasstext" value="whiteboxwk" scope="request" />
				<c:set var="tdclassdata" value="whitebox2wk" scope="request" />
				<s:iterator value="serviceOrderObjectDetails" status="s">
					<tr>
						
						<td width="25%"class="<c:out value='${tdclasstext}' />"><span class="mandatory">*</span><s:text name="so.estimete"></s:text></td>
						 <td width="25%" class="<c:out value='${tdclassdata}' />">
							<s:textfield name="abstractEstimate.estimateNumber" readOnly="true" />
						</td>
						<td class="<c:out value='${tdclasstext}' />" width="25%" ><span class="mandatory">*</span><s:text name="so.estimate.amount"></s:text></td>
						<td width="25%" class="<c:out value='${tdclassdata}' />" ><s:textfield  name="workValue" id="workValue" value='%{getOverHeadValue(abstractEstimate.id)}' readOnly='true'/></td>
						<td class="<c:out value='${tdclasstext}' />" width="25%" ><span class="mandatory">*</span><s:text name="so.template"></s:text></td>
						<td class="<c:out value='${tdclassdata}' />"width="25%" ><s:select headerKey=""headerValue="%{getText('list.default.select')}"name="templateId" id="templateId%{#s.index}" cssClass="selectwk"list="dropdownData.templateList" listKey="id"listValue='templateCode' onchange="return getTemplateDetails(%{abstractEstimate.id},this,%{#s.index})" value="%{serviceTemplate.id}"/>
				<s:if test='%{soMeasurementHeaders.size >0 }'> <script> var templeteId ="templateId"+<s:property value="%{#s.index}"/> ;dom.get(templeteId).disabled=true;</script> </s:if>
				</td> 
				</tr>
					<c:choose>
						<c:when test="${tdclasstext == 'whiteboxwk'}">
							<c:set var="tdclasstext" value="greyboxwk" scope="request" />
						</c:when>
						<c:otherwise>
							<c:set var="tdclasstext" value="whiteboxwk" scope="request" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${tdclassdata == 'whitebox2wk'}">
							<c:set var="tdclassdata" value="greybox2wk" scope="request" />
						</c:when>
						<c:otherwise>
							<c:set var="tdclassdata" value="whitebox2wk" scope="request" />
						</c:otherwise>
					</c:choose>
					
				</s:iterator >
			
				</table>
				
				</table>
					<br>	
			<div id="loading" class="loading" style="width: 700; height: 700;display: none " align="center" >
				<blink style="color: red">Loading data, Please wait...</blink>
			</div> <br>
			<jsp:include page="serviceOrderModifyActv.jsp"/>
			<s:hidden name="serviceOrderId" value="%{id}"></s:hidden>
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="save" value="Save" cssClass="buttonfinal" method="edit" onClick="return validate();"/>
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
				</s:push>
	</s:form>

<script>
var resultIndex;
var amtfirstIdx;
var amtSecondIdx;
var callback = {
		success: function(o){
			var resultDivId = "result" + resultIndex;
			document.getElementById(resultDivId).innerHTML=o.responseText;
			document.getElementById('loading').style.display ='none';
			},
			failure: function(o) {
			document.getElementById('loading').style.display ='none';
		    }
	
		}
function getTemplateDetails(estmtId,templtObj,index){
	
	document.getElementById('loading').style.display ='block';
		resultIndex = index;
		var url = 'serviceOrder!ajaxTemplateDetails.action?templateId='+templtObj.value+"&estimateId="+estmtId+"&index="+index;
		YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	
	
}

function changeAmount(rateObj,firstIndex,secondindex,estimateId,listSize){
		
		amtfirstIdx = firstIndex;
		amtSecondIdx=secondindex;
		var url = 'serviceOrder!ajaxAmount.action?estimateId='+estimateId+"&rate="+rateObj.value;
		YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
		validateTotalRate(firstIndex,listSize);
		
	}
	var postType = {
success: function(o) {
		var amount= o.responseText;
		document.getElementById("amountActv["+amtfirstIdx+"]["+amtSecondIdx+"]").innerHTML = amount;
    },
    failure: function(o) {
    	alert('failure');
    }
}
function validateTotalRate(firstIndex,listSize){
	
	var lblErrorId = "lblError"+firstIndex;
	var jserrorid = "jserrorid"+ firstIndex;
	
	var totalRate=0;	
	for(var i=0;i<listSize;i++){
		
		totalRate = totalRate + parseFloat(document.getElementById("estTempltDetls["+firstIndex+"]["+i+"].rate").value); 			
	}
	if(totalRate !=100){
		document.getElementById(jserrorid).style.display='block';
		document.getElementById(lblErrorId).innerHTML = "Error : Total rate is not equal to 100 percentage";
		document.getElementById("save").disabled=true;
		
	}else{
		document.getElementById(jserrorid).style.display='none';
		document.getElementById("save").disabled=false;
	}
}
function checkLength(obj){
	if(obj.value.length>256)
	{
		alert('Max 256 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,256);
	}
}
function validate(){

	for(var i=0;i<document.forms[0].length;i++){
		document.forms[0].elements[i].disabled =false;
	}
}

</script>

</body>
</html>