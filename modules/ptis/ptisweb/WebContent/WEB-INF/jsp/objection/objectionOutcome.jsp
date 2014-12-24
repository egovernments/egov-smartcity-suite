<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="objection.outcome.header" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td class="bluebox" width="25%">
						<s:text name="objection.rejected.button" />
					</td>
					<td class="bluebox" width="25%">
						<s:radio name="objectionRejected"
							list="#{'true':'Yes','false':'No'}" id="objectionRejected" />
					</td>
					<td class="bluebox" width="25%">
						<s:text name="outcome.date" />
						<span class="mandatory1">*</span>
					</td>
					<td class="bluebox" width="25%">
						<s:date name="dateOfOutcome" id="dateOfOutcomeId" format="dd/MM/yyyy" />
						<s:textfield name="dateOfOutcome" id="dateOfOutcome"
							value="%{dateOfOutcomeId}" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" />
						<a
							href="javascript:show_calendar('objectionViewForm.dateOfOutcome',null,null,'DD/MM/YYYY');"
							style="text-decoration: none">&nbsp;<img
								src="${pageContext.request.contextPath}/image/calendaricon.gif"
								border="0" />
						</a>(dd/mm/yyyy)
					</td>
				</tr>
				<tr>
					<td class="greybox" width="25%">
						<s:text name="outcome.remarks" />
						<span class="mandatory1">*</span>
					</td>
					<td class="greybox" width="25%">
						<s:textarea name="remarks" id="outcomeRemarks" cols="40" rows="2" onblur="checkLength(this)"></s:textarea>
					</td>
					<td class="greybox" width="25%">
						<s:text name="objection.upload.document" />
					</td>
					<td class="greybox" width="25%">
						<input type="button" class="button" value="Upload Document"
							id="docUploadButton" onclick="showDocumentManager();" />
						<s:hidden name="docNumberOutcome" id="docNumber" />
					</td>

				</tr>

			</table>
		</td>
	</tr>
</table>