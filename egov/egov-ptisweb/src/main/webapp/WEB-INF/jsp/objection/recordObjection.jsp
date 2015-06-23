<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="recordObjection.title"></s:text>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="25%"><s:text name="objection.received.date" /><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%"><s:date name="recievedOn" id="recievedOnId" format="dd/MM/yyyy"  />
				<s:textfield name="recievedOn" id="recievedOn" value="%{recievedOnId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
				<a href="javascript:show_calendar('objectionViewForm.recievedOn',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="/egi/resources/erp2/images/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
				</td>
			
				<td class="bluebox" width="25%"><s:text name="objection.received.by"/><span class="mandatory1">*</span></td>
				<td class="bluebox" width="25%"><s:textfield name="recievedBy" id="recievedBy" onblur="chkReceivedByLen(this);"/></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" width="25%"><s:text name="objection.details"/><span class="mandatory1">*</span></td>
				<td class="greybox" width="25%"><s:textarea name="details" id="details" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td>
			
				<td class="greybox" width="25%"><s:text name="objection.upload.document"/></td>
				<td class="greybox" width="25%"><input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();" /></td>
				<s:hidden name="docNumberObjection" id="docNumber" />

			</tr>
		</table>
		</td>
	</tr>

</table>
<script>
function chkReceivedByLen(obj){
	if(obj.value.length>256)
	{
		alert('Max 256 characters are allowed for received by text. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,256);
	}
}

</script>
