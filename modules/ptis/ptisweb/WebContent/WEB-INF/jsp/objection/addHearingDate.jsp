<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="objection.add.hearingDate" />
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<s:if test="%{hearings == null}"> <s:set var="hearingIdx" value="0"/></s:if>
				<s:elseif test="%{state.text1.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARINGDATE_SAVED)}">  <s:set var="hearingIdx" value="%{hearings.size()-1}"/> </s:elseif>
				<s:else>  <s:set var="hearingIdx" value="%{hearings.size()}"/>  </s:else>
				<tr>
					<td class="bluebox" width="10%">
						<s:text name="objection.planned.hearingDate" />
						<span class="mandatory1">*</span>
					</td>
					<td class="bluebox" width="25%">
						<s:date name="objection.hearings[%{hearingIdx}].plannedHearingDt"
							id="plannedHearingDtId" format="dd/MM/yyyy" />
						<s:textfield name="objection.hearings[%{hearingIdx}].plannedHearingDt"
							id="plannedHearingDt" value="%{plannedHearingDtId}" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" />
						<a
							href="javascript:show_calendar('objectionViewForm.plannedHearingDt',null,null,'DD/MM/YYYY');"
							style="text-decoration: none">&nbsp;<img
								src="${pageContext.request.contextPath}/image/calendaricon.gif"
								border="0" /> </a>(dd/mm/yyyy)
					</td>
					<td class="bluebox" width="25%" colspan="3"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>