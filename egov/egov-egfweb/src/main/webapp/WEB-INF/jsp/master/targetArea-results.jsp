#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>

	<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
	</span>
<s:if test="%{targetAreaList.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">

		<tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Sl No</th>
				<th class="bluebgheadtd">Target Area Code</th>
				<th class="bluebgheadtd">Target Area Name</th>
                <th class="bluebgheadtd">Bill User</th>
		  </tr>
		<s:iterator value="targetAreaList" status="stat" var="TargetArea">
			<tr>
				<td class="blueborderfortd"><div align="left"><s:property value="#stat.index+1"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"> <a href="#" onclick="return urlLoad('<s:property value="%{id}"/>','<s:property value="%{mode}"/>')" > <s:property value="code"/></a>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="name"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="position.name"/>&nbsp;</div></td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table>
	</s:if>
<s:else>No Records Found</s:else>
