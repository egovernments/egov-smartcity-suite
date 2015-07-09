#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Tender Negotiation</title>
<body onload="refreshInbox();">
<script>
function refreshInbox(){
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>
		
		<s:if test="%{option != null && option == 'setStatus'}">
			<s:text name="%{getText(messageKey)}" /> <br />
			<s:if test="%{workOrder != null && workOrder.workOrderNumber!=null}">	
				Work order number is  &nbsp;&nbsp; <s:property value="%{workOrder.workOrderNumber}"/><br />					
			</s:if>
			
		</s:if>
		<s:else>
				<s:if test="%{model.egwStatus != null && model.egwStatus.code == 'APPROVED'}">
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name='tenderResponse.approved' />		
				</s:if>
				<s:elseif test="%{model.egwStatus != null && model.egwStatus.code == 'CANCELLED'}">
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />	
				</s:elseif>		  	 		
				<s:else>
					<s:property value="%{negotiationNumber}"/>&nbsp; <s:text name="%{getText(messageKey)}" />
					<br>
					<s:if test="%{employeeName != null}"> 
						<s:text name="common.forwardmessage" /> &nbsp;<s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
					</s:if>
				</s:else>
		</s:else>	
</body>
</html>
