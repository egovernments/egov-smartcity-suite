<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ taglib prefix="s" uri="/struts-tags" %>
			  
<html>  
	<head>  
		<title><s:text name="list.title.serviceCategory"/></title>  
		<script>
function onSubmit(obj){
	document.forms[0].action=obj;
	document.forms[0].submit;
   return true;
}
</script>
	</head>  

	<body>  
	<s:form action="serviceCategory" theme="simple" > 
	  	<div class="subheadnew"><s:text name="serviceCategory.edit.title"/></div>
	  	<br/>
	  	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		        <tr>  
		            <th class="bluebgheadtd" width="18%" style="display:none">ID</th>  
		            <th class="bluebgheadtd" width="15%"><s:text name="masters.serviceCategory.servicCategoryeCode"/></th>
					<th class="bluebgheadtd" width="15%"><s:text name="masters.serviceCategory.serviceCategoryName"/></th>
				    <th class="bluebgheadtd" width="15%"><s:text name="masters.serviceCategory.serviceCategoryeEnabled" /></th>
					<th class="bluebgheadtd" width="15%"><s:text name="masters.edit"/></th>
				</tr>  
				<s:iterator var="p" value="serviceCategoryList">  
	            <tr>  
					<s:if test="code != null">								
					<td class="blueborderfortd">
					<div align="center">  
					<s:property value="%{code}" /> 
					</div>
					</td>
					</s:if>

					<s:else>
					<td class="blueborderfortd">							
					&nbsp;	
					</td>
					</s:else>
			
					<s:if test="name!= null">								
					<td class="blueborderfortd">
					<div align="center">  
					<s:property value="%{name}" /> 
					</td>
					</div>
					</s:if>

					<td class="blueborderfortd">
						<div align="center">
							<s:if test="isActive">
								<s:text name="text.yes"></s:text>
							</s:if>
							<s:else>
								<s:text name="text.no"></s:text>
							</s:else>
						</div>
					</td>

					<s:else>
					<td class="blueborderfortd">							
					&nbsp;	
					</td>
					</s:else>
						
					<td class="blueborderfortd">
					<div align="center">  
   						<a href="serviceCategory-edit.action?code=<s:property value='%{code}'/>"><s:text name="masters.edit"/></a>  
   					</div>
					</td>  
	            </tr>  
	        	</s:iterator> 
			</table> 
				<div class="buttonbottom">
			 		<input name="button2" type="submit" class="buttonsubmit" id="button" onclick="return onSubmit('serviceCategory-newform.action');" value="Add New"/>
					<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
				</div>
         </s:form>   
	</body>  
</html>
