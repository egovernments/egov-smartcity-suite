<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
	function  enableRentPaid(obj) {
		if(obj.value=="Rental") {
			document.getElementById("rentpaid").disabled=false;
		} else {
			document.getElementById("rentpaid").value="";
			document.getElementById("rentpaid").disabled=true;
		}
	}
	
	function showHotelGrade(obj)
	{
	 var tradeId = obj.value;
	 var subCatList = ${hotelSubCatList};
	 for(var i =0; i<subCatList.length; i++)
	 {
	 
		if(subCatList[i] == tradeId){
			document.getElementById("hotelGradeRow").style.display='';
			return;
        }
        else if(subCatList[i]!= tradeId){
        	document.getElementById("hotelGradeRow").style.display='none';
        }
	 }
	}
</script>

<s:hidden name="hotelSubCatList" id="hotelSubCatList"></s:hidden>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.applicationdate" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="applicationDate" id="applicationDate" onfocus="waterMarkTextIn('applicationDate','dd/mm/yyyy');" onblur="waterMarkTextOut('applicationDate','dd/mm/yyyy');lessThanOrEqualToCurrentDate(this);if(this.value != \"dd/mm/yyyy\"){validateDateFormat(this)};" maxlength="10" size="10" value="%{applicationDate}" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<a href="javascript:show_calendar('forms[0].applicationDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" id="calenderImgId" >
		</a>
	</td>
	<td class="<c:out value="${trclass}"/>"></td>
	<td class="<c:out value="${trclass}"/>"></td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr >
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.establishmentname" />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="nameOfEstablishment" id="nameOfEstablishment" maxlength="100"/>
	</td>
	<td class="greybox">
		<s:text name="license.tradename" />
		<span class="mandatory">*</span>
	</td>
	<td class="greybox" >
		<s:select headerValue="%{getText('license.default.select')}" headerKey="" list="dropdownData.tradeNameList" listKey="id" listValue="name" name="tradeName" disabled="%{sDisabled}" id="tradeName" value="%{tradeName.id}" width="230" style="width: 230px" size="0" onchange="showHotelGrade(this);"/>
	</td>
</tr>

<tr style="display: none" id="hotelGradeRow">
	<td class="bluebox" width="5%">
		&nbsp;
	</td>
	<td class="bluebox">
		<s:text name='license.hotel.grade' />
	</td>
	<td class="bluebox">
		<s:select headerValue="%{getText('license.default.select')}"
			headerKey="" name="hotelGrade" id="hotelGrade"
			list="hotelGradeList" />
	</td>
	<td class="bluebox" colspan="2"></td>

</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.ownbuilding' />
		<span class="mandatory">*</span>
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:radio name="buildingType" list="buildingTypeList" id="buildingtype" onchange="enableRentPaid(this)" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.rentpaid" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="rentPaid" size="20" id="rentpaid" disabled="true" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,6),formatCurrency(rentPaid)" />
	</td>
</tr>
<c:choose>
	<c:when test="${trclass=='greybox'}">
		<c:set var="trclass" value="bluebox" />
	</c:when>
	<c:when test="${trclass=='bluebox'}">
		<c:set var="trclass" value="greybox" />
	</c:when>
</c:choose>
<tr>
	<td class="<c:out value="${trclass}"/>" width="5%">
		&nbsp;
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name='license.numberofrooms' />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:textfield name="noOfRooms" id="license.noOfRooms"  maxlength="3" onKeyPress="return numbersonly(this, event)" />
	</td>
	<td class="<c:out value="${trclass}"/>">
		<s:text name="license.remarks" />
	</td>
	<td class="greybox">
		<s:textarea name="remarks" rows="3" cols="40" maxlength="500"/>
	</td>
</tr>


