<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:iterator value="serviceOrderObjectDetails" status="estmtTmpltIndex" >	
				
	<div id="result<s:property value='%{#estmtTmpltIndex.index}' />" >
	
	Template details for estimate <b><s:property value="abstractEstimate.estimateNumber"/></b> and template code <b><s:property value="serviceTemplate.templateCode"/>		
			
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            
     
              <tr>
                <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.slno"></s:text></th>
                <th  class="pagetableth" style="width:30%;text-align:center" ><s:text name="so.templtactv.desc"></s:text></th>
                <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.templtactv.rate"></s:text> %</th>
                <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.templtactv.amount"></s:text></th>
                <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.bill.number"></s:text></th>
		</tr>
		
		<c:set var="tdclass" value="whitebox2wk" scope="request" />
		<s:iterator value="serviceOrderDetails" status="s">
		<tr>
			<td class="<c:out value='${tdclass}' />" width="8%"><div align="center"><s:property value="soTemplateActivities.stageNo"/>&nbsp;</div></td>
			<td class="<c:out value='${tdclass}' />" style="white-space: normal;" width="25%"><div align="center"><s:property value="soTemplateActivities.description"/>&nbsp;</div></td>
			<td class="<c:out value='${tdclass}' />" width="1%"><div align="center"><input type="text" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].ratePercentage" id="estTempltDetls[<s:property value='%{#estmtTmpltIndex.index}' />][<s:property value='%{#s.index}' />].rate"  value="<s:property value="%{ratePercentage}"/>"  onkeyup="validateDecimal(this);" onblur="changeAmount(this,<s:property value='%{#estmtTmpltIndex.index}' />,<s:property value='%{#s.index}' />,<s:property value="abstractEstimate.id"/>,<s:property value='%{serviceOrderDetails.size()}' />)" style="text-align: right"/></div></td>
			<s:if test='%{iscompleted}'> <script> var rateId ="estTempltDetls["+<s:property value='%{#estmtTmpltIndex.index}' />+"]["+<s:property value='%{#s.index}' />+"].rate" ;dom.get(rateId).disabled=true;</script> </s:if>
			<td class="<c:out value='${tdclass}' />" width="10%"><div align="right"  id="amountActv[<s:property value='%{#estmtTmpltIndex.index}' />][<s:property value='%{#s.index}' />]"><s:text name="format.number" ><s:param value="%{(serviceOrderObjectDetails[#estmtTmpltIndex.index].objectamount/100)*ratePercentage}"/></s:text></div></td>
			<td class="<c:out value='${tdclass}' />" width="10%"><div align="center"> <s:property value="soMeasurmentDetail.soMeasurementHeader.egBillregister.billnumber"/></div></td>
		</tr>
		
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].soTemplateActivities.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].soTemplateActivities.id" value='<s:property value="%{soTemplateActivities.id}"/>' />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].id" value='<s:property value="%{id}"/>' />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].iscompleted" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].iscompleted" value='<s:property value="%{iscompleted}"/>' />
		<c:choose>
			<c:when test="${tdclass == 'whitebox2wk'}">
				<c:set var="tdclass" value="greybox2wk" scope="request" />
			</c:when>
			<c:otherwise>
				<c:set var="tdclass" value="whitebox2wk" scope="request" />
			</c:otherwise>
		</c:choose>
		
		
		</s:iterator>
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].abstractEstimate.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].abstractEstimate.id" value="<s:property value="abstractEstimate.id"/>" />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceTemplate.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].serviceTemplate.id" value="<s:property value="serviceTemplate.id"/>" />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].objectamount" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].objectamount" value="<s:property value="%{getOverHeadValue(abstractEstimate.id)}"/>" />
<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{#estmtTmpltIndex.index}' />].id" value='<s:property value="id"/>' />
		
</table>
</div>
<div id="jserrorid<s:property value='%{#estmtTmpltIndex.index}' />" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError<s:property value='%{#estmtTmpltIndex.index}' />" ></p>
</div>
</s:iterator>
