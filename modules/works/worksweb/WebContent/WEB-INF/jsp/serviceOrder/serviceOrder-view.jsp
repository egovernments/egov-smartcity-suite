<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<html>
<head>
	<title>Service Order View </title>
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

	<s:form action="serviceOrder" theme="simple"name="serviceOrderViewForm">
		
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
							<td width="25%" class="whiteboxwk"> Service Order Number : </td>
							<td width="25%" class="whitebox2wk"> <s:property value='%{serviceordernumber}' /></td>
							<s:date name="serviceorderdate" id="serviceorderdateId" format="dd/MM/yyyy" />
							<td width="25%" class="whiteboxwk"> Service Order Date : </td>
							<td width="25%" class="whitebox2wk"> <s:property value='%{serviceorderdateId}' /></td>
						</tr>
						<tr>
							<td width="25%" class="greyboxwk"> Architech : </td>
							<td width="25%" class="greybox2wk"> <s:property value='%{getArchitect()}' /></td>
							<td width="25%" class="greyboxwk"> Prepared By : </td>
							<td width="25%" class="greybox2wk"> <s:property value='%{preparedby.userName}' /></td>
							
						</tr>
						<tr>
							<td width="25%" class="whiteboxwk"> Comments : </td>
							<td width="25%" class="whitebox2wk"> <s:property value='%{comments}' /></td>
							
						</tr>
				</table></table>
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
									<s:text name="so.esttmptdtls"></s:text>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
						<s:iterator value="serviceOrderObjectDetails" status="s">
							Template details for estimate <b><s:property value="abstractEstimate.estimateNumber"/></b> 
							and template code <b><s:property value="serviceTemplate.templateCode"/>		</b>
			
          					  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            
     
             					 <tr>
              						  <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.slno"></s:text></th>
               						 <th  class="pagetableth" style="width:30%;text-align:center" ><s:text name="so.templtactv.desc"></s:text></th>
               						 <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.templtactv.rate"></s:text> %</th>
             						   <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.templtactv.amount"></s:text></th>
             						   <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.bill.number"></s:text></th>
			 			 </tr><c:set var="tdclass" value="whitebox2wk" scope="request" />
						<s:iterator value="serviceOrderDetails" status="s">
							<tr>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"><s:property value="soTemplateActivities.stageNo"/>&nbsp;</div></td>
								<td class="<c:out value='${tdclass}' />" width="30%"><div align="center"><s:property value="soTemplateActivities.description"/>&nbsp;</div></td>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"><s:property value="ratePercentage"/>&nbsp;</div></td>
								<td class="<c:out value='${tdclass}' />" width="10%"><div align="right"><s:text name="format.number" ><s:param value="%{(getOverHeadValue(abstractEstimate.id)*ratePercentage)/100}"/></s:text></div></td>
								<s:if test="%{soMeasurmentDetail!=null}">				
								<td class="<c:out value='${tdclass}' />" width="10%"><div align="center"><s:property value="soMeasurmentDetail.soMeasurementHeader.egBillregister.billnumber"/>&nbsp;</div></td>
								</s:if>
								<s:else>
									<td class="<c:out value='${tdclass}' />" width="10%"><div align="center"><s:property value=""/>&nbsp;</div></td>
								</s:else>
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
		<td class="<c:out value='${tdclass}' />"><div align="right"><s:text name="format.number" ><s:param value="%{getOverHeadValue(abstractEstimate.id)}"/></s:text></div></td></tr>
					</table>
						
					</s:iterator>
				</table>
				
				</table>
					<br>	
		
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
	</s:push>
	</s:form>


</body>
</html>