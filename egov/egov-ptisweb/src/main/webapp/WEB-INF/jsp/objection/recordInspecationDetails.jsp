<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="objection.add.inspection" />
			</div>
		</td>
	</tr>
	<tr>
		<td >
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<s:if test="%{inspections == null ||inspections.size()==0 }"> <s:set var="inspectionIdx" value="0"/></s:if>
				<s:else>  <s:set var="inspectionIdx" value="%{inspections.size()-1}"/> </s:else>
				<tr>
					<td class="bluebox" width="25%">
						<s:text name="inspection.remarks" />
					</td>
					<td class="bluebox" width="25%">
						<s:textarea name="objection.inspections[%{inspectionIdx}].inspectionRemarks"
							id="inspectionRemarks" cols="40" rows="2" onblur="checkLength(this)"></s:textarea>
					</td>
					<td class="bluebox" width="25%"><s:text name="objection.upload.document"/></td>
					<td class="bluebox" width="25%">
						<input type="button" class="button" value="Upload Document"
							id="docUploadButton" onclick="showDocumentManager();" />
						<s:hidden name="objection.inspections[%{inspectionIdx}].documentNumber" id="docNumber" />
					</td>

				</tr>
				
			</table>
		</td>
	</tr>
</table>