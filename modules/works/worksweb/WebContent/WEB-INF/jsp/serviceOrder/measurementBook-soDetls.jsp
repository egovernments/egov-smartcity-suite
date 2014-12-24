<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            
     			<tr>
				<td width="25%" class="greyboxwk">Measurement Date: <span class="mandatory">*</span> </td>
				<td width="25%" class="greybox2wk">
				<s:date name="measurementDate" id="measurementDateId" format="dd/MM/yyyy" />
				<s:textfield name="measurementDate" id="measurementDate" value="%{measurementDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="loadBillDate(this);"/>
				<a href="javascript:show_calendar('mBookBillForm.measurementDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td class="greyboxwk" width="25%" ><s:text name="so.preparedby"></s:text><span class="mandatory">*</span></td>
						<td class="greybox2wk" width="25%" ><s:select headerKey=""headerValue="%{getText('list.default.select')}"
						name="preparedby" id="preparedby" cssClass="selectwk" list="dropdownData.prepareByList" listKey="userMaster.id"
						listValue='employeeName+ "-" +desigId.designationName' value='%{preparedby.id}'/>
						</td> 
				</tr>
				<tr>
				
					<td class="whiteboxwk" width="25%"  ><s:text name="so.comments"></s:text></td>
					<td class="whitebox2wk" width="25%"  ><s:textarea name="comments" id="comments" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td>
				</tr></table>
     			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
             				<tr>
              						  <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.slno"></s:text></th>
               						 
               						 <th  class="pagetableth" style="width:40%;text-align:center" ><s:text name="so.templtactv.desc"></s:text> </th>
               						  <th  class="pagetableth" style="width:5%;text-align:center" ><s:text name="so.templtactv.rate"></s:text> </th>
             						  <th  class="pagetableth" style="width:10%;text-align:center" ><s:text name="so.templtactv.amount"></s:text></th>
             						 
               
			 			 </tr><c:set var="tdclass" value="whitebox2wk" scope="request" />
			 			<s:hidden name="serviceOrderObjectDetail.id" value="%{soObjDetail.id}"></s:hidden>
					<s:iterator value="soObjDetail.serviceOrderDetails" status="s">
							
							<tr>
							 <s:hidden name="soMeasurmentDetails[%{#s.index}].serviceOrderDetails.id" value="%{soObjDetail.serviceOrderDetails[#s.index].id}"></s:hidden>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"> <s:property value="soObjDetail.serviceOrderDetails[#s.index].soTemplateActivities.stageNo" /></div></td>
								<td class="<c:out value='${tdclass}' />" width="40%"><div align="center"><s:property value="soObjDetail.serviceOrderDetails[#s.index].soTemplateActivities.description"/></div></td>
								<td class="<c:out value='${tdclass}' />" width="5%"><div align="center"><s:property value="soObjDetail.serviceOrderDetails[#s.index].ratePercentage"/></div></td>
								<td class="<c:out value='${tdclass}' />" width="10%"><div align="right"><s:text name="format.number" ><s:param value="%{getAmount(soObjDetail.objectamount,soObjDetail.serviceOrderDetails[#s.index].ratePercentage)}"/></s:text></div></td>
								
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
					
					</table>

<script>
function checkLength(obj){
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}
function loadBillDate(dateObj){
	
	if(dom.get('billdate').value == ''){
		
		dom.get('billdate').value = dateObj.value
	}
	
}
</script>
