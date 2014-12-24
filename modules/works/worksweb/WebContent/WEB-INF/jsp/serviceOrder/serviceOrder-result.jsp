<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:if test="%{tmpltActDtlsList.size()>0}">
	
	Template details for estimate <b><s:property value="estimate.estimateNumber"/></b> and template code <b><s:property value="template.templateCode"/>		
			
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            
     
              <tr>
                <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.slno"></s:text></th>
                <th  class="pagetableth" style="width:30%;text-align:center" ><s:text name="so.templtactv.desc"></s:text></th>
                <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.templtactv.rate"></s:text> %</th>
                <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.templtactv.amount"></s:text></th>
               
		</tr>
		
		<c:set var="tdclass" value="whitebox2wk" scope="request" />
		<s:iterator value="tmpltActDtlsList" status="s">
		<tr>
			<td class="<c:out value='${tdclass}' />" width="8%"><div align="center"><s:property value="stageNo"/>&nbsp;</div></td>
			<td class="<c:out value='${tdclass}' />" style="white-space: normal;" width="25%"><div align="center"><s:property value="desc"/>&nbsp;</div></td>
			<td class="<c:out value='${tdclass}' />" width="1%"><div align="center"><input type="text" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].ratePercentage" id="estTempltDetls[<s:property value='%{estmtTmpltIndex}' />][<s:property value='%{#s.index}' />].rate"  value="<s:property value="%{rate}"/>"  onkeyup="validateDecimal(this);" onblur="changeAmount(this,<s:property value='%{estmtTmpltIndex}' />,<s:property value='%{#s.index}' />,<s:property value="estimate.id"/>,<s:property value='%{tmpltActDtlsList.size()}' />)" style="text-align: right"/></div></td>
			<td class="<c:out value='${tdclass}' />" width="10%"><div align="right" id="amountActv[<s:property value='%{estmtTmpltIndex}' />][<s:property value='%{#s.index}' />]"><s:property value="%{amount}"/></div></td>
		</tr>
		
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].soTemplateActivities.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].serviceOrderDetails[<s:property value='%{#s.index}' />].soTemplateActivities.id" value="<s:property value="%{tmptActvid}"/>" />
		<c:choose>
			<c:when test="${tdclass == 'whitebox2wk'}">
				<c:set var="tdclass" value="greybox2wk" scope="request" />
			</c:when>
			<c:otherwise>
				<c:set var="tdclass" value="whitebox2wk" scope="request" />
			</c:otherwise>
		</c:choose>
		
		
		</s:iterator>
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].abstractEstimate.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].abstractEstimate.id" value="<s:property value="estimate.id"/>" />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].serviceTemplate.id" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].serviceTemplate.id" value="<s:property value="template.id"/>" />
		<input type="hidden" name="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].objectamount" id="serviceOrder.serviceOrderObjectDetails[<s:property value='%{estmtTmpltIndex}' />].objectamount" value='<s:property value="%{getOverHeadValue(estimate.id)}"/>' />
		
</table>
</s:if>
<s:else>No data found</s:else>