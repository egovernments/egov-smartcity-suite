<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/assets/skins/sam/autocomplete.css" />
	<script type="text/javascript" src="/egi/commonyui/yui2.7/animation/animation-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>

	<title>Measurement Book search</title>
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

	<s:form action="measurementBook" theme="simple"name="mesurementBookSearchForm">
		
		
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
				<td class="whiteboxwk" width="25%" ><s:text name="so.department"></s:text> <span class="mandatory">*</span></td>
						<td class="whitebox2wk" width="25%" ><s:select headerKey="-1"headerValue="%{getText('list.default.select')}"
						name="soObjDetail.serviceOrder.departmentId.id" id="departmentId" cssClass="selectwk"list="dropdownData.departmentList" listKey="id"listValue='deptName' /></td>	
				</tr>
				<tr>
					
					<td width="25%" class="greyboxwk"><s:text name="so.number" /><span class="mandatory">*</span></td>
					<td width="25%" class="greybox2wk">
						 
						<div id="sonum_autocomplete" style="width:15em;padding-bottom:1em;"> 
							<div><s:textfield  name="soObjDetail.serviceOrder.serviceordernumber" id="serviceordernumber"  onblur="populateEstimate(this)"/></div>
						  	<span id="soNumResults"></span>
						</div>
					<egov:autocomplete name="sonum" width="50" field="serviceordernumber" url="measurementBook!ajaxFilterSOBydept.action" results="soNumResults" paramsFunction="soNumSearchParameters"  />
					
					</td>			
					<egov:ajaxdropdown id="estimate"fields="['Text','Value']" dropdownId="estimateid" url="serviceOrder/measurementBook!ajaxFilterAbsByDeptAndSOydept.action" />
					<td width="25%" class="greyboxwk"><s:text name="estimete.number" /><span class="mandatory">*</span></td>
					<td width="25%" class="greybox2wk">
					
					<s:select list="dropdownData.estimateList" name="soObjDetail.abstractEstimate.estimateNumber" id="estimateid" listKey="estimateNumber" listValue="estimateNumber" headerKey="-1" headerValue="----Choose----" />
					
					</td>	
			
				</tr>
				<!--<tr>
					
				<td width="25%" class="whiteboxwk"><s:text name="so.fromdate"></s:text> </td>
				<td width="25%" class="whitebox2wk">
				<s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy" />
				<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('mesurementBookSearchForm.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td width="25%" class="whiteboxwk"><s:text name="so.todate"></s:text> </td>
				<td width="25%" class="whitebox2wk">
				<s:date name="toDate" id="toDateId" format="dd/MM/yyyy" />
				<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('mesurementBookSearchForm.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>	
				</tr>
				
				--></table></table>
				
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonfinal" method="list" onclick="return validate();" />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>

				<s:if test="%{soObjDetail.serviceOrderDetails.size()>0}">
          					  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            
     
             					 <tr>
              						  <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.slno"></s:text></th>
               					
               						 <th  class="pagetableth" style="width:60%;text-align:center" ><s:text name="so.templtactv.desc"></s:text> </th>
               						  <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.templtactv.rate"></s:text> </th>
             						  <th  class="pagetableth" style="width:15%;text-align:center" ><s:text name="so.templtactv.amount"></s:text></th>
               						<th  class="pagetableth" style="width:8%;text-align:center" ><s:text name="so.completed"></s:text></th>
			 			 </tr><c:set var="tdclass" value="whitebox2wk" scope="request" />
			 			 <s:hidden name="soObjDetail.id"/><s:hidden name="soObjDetail.objectamount"/>
						<s:iterator value="soObjDetail.serviceOrderDetails" status="s">
							
							<tr><s:hidden name="soObjDetail.serviceOrderDetails[%{#s.index}].soTemplateActivities.stageNo"/>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"><s:hidden name="soObjDetail.serviceOrderDetails[%{#s.index}].id"/><s:property value="soObjDetail.serviceOrderDetails[#s.index].soTemplateActivities.stageNo"/></div></td>
								<td class="<c:out value='${tdclass}' />" width="60%"><div align="center"><s:hidden name="soObjDetail.serviceOrderDetails[%{#s.index}].soTemplateActivities.description"/><s:property value="soObjDetail.serviceOrderDetails[#s.index].soTemplateActivities.description"/></div></td>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"><s:hidden name="soObjDetail.serviceOrderDetails[%{#s.index}].ratePercentage"/><s:property value="soObjDetail.serviceOrderDetails[#s.index].ratePercentage"/></div></td>
								<td class="<c:out value='${tdclass}' />" width="15%"><div align="right"><s:text name="format.number" ><s:param value="%{getAmount(soObjDetail.objectamount,soObjDetail.serviceOrderDetails[#s.index].ratePercentage)}"/></s:text></div></td>
								<td class="<c:out value='${tdclass}' />" width="8%"><div align="center">
									<s:if test="%{soObjDetail.serviceOrderDetails[#s.index].iscompleted}">
										<s:checkbox name="soObjDetail.serviceOrderDetails[%{#s.index}].iscompleted" onclick="totalAmount(this,%{soObjDetail.objectamount},%{soObjDetail.serviceOrderDetails[#s.index].ratePercentage})"  disabled='true' />
									</s:if>
									<s:else>
										<s:checkbox name="soObjDetail.serviceOrderDetails[%{#s.index}].iscompleted" onclick="totalAmount(this,%{soObjDetail.objectamount},%{soObjDetail.serviceOrderDetails[#s.index].ratePercentage})"  />
									</s:else>
								</div></td>
						</tr>
					<c:choose>
						<c:when test="${tdclass == 'whitebox2wk'}">
							<c:set var="tdclass" value="greybox2wk" scope="request" />
						</c:when>
						<c:otherwise>
							<c:set var="tdclass" value="whitebox2wk" scope="request" />
						</c:otherwise>
					</c:choose>
					
					</s:iterator>
					<tr><td></td><td></td><td class="<c:out value='${tdclass}' />"> <div align="center">Total Amount</div> </td> 
		<td class="<c:out value='${tdclass}' />"><div align="right"><s:textfield id="totalAmt"readOnly='true' style="text-align: right"/></div></td></tr>
					</table>
						<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="GENERATE BILL" cssClass="buttonfinal" method="beforeCreate" onclick="return validateCheck();" />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
			</s:if>	
		
	</s:form>

<script>
	function soNumSearchParameters(){
		return "deptId="+dom.get('departmentId').value+"&serviceordernumber="+dom.get('serviceordernumber').value;	
	}
	
        function validate(){
		
		if(document.getElementById('departmentId').value == '-1'){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please select department";
			return false;
		}
		else if(document.getElementById('serviceordernumber').value == ''){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please enter service order number";
			return false;
		}
		else if(document.getElementById('estimateid').value == '-1'){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please enter estimate number";
			return false;
		}
		
		return true;
 	}
	function validateCheck(){
		 if(dom.get('totalAmt').value==0.00 || dom.get('totalAmt').value==''){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError').innerHTML = "Please select atleast one order";
			return false;
		}
		return true;
	}
var totAmount=0;
function addAmount(elem,index){
	
	var amount = <s:text name="format.number" ><s:param value="%{(soObjDetail.objectamount/100)*soObjDetail.serviceOrderDetails[index].ratePercentage}"/></s:text>
		if(elem.checked == true){
			totAmount =totAmount + amount; 
		}else{
			totAmount = totAmount - amount; 
		}
	dom.get('totalAmt').value = totAmount;
	
}

function totalAmount(elem,overHeadAmt,rate){
	var amount = parseFloat((overHeadAmt/100)*rate);
	
	if(elem.checked == true){
		totAmount = parseFloat(totAmount) + parseFloat(amount);
	}else{
		totAmount = parseFloat(totAmount) - parseFloat(amount);
	}
	dom.get('totalAmt').value=totAmount.toFixed(2);
}
function populateEstimate(soObj){
	if(soObj.value!="" && dom.get('departmentId').value != -1){
		populateestimateid({serviceordernumber:soObj.value,deptId:dom.get('departmentId').value});
		
	}
}
</script>
</body>
</html>