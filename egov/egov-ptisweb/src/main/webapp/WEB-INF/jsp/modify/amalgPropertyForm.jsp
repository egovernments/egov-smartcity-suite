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

<%@ include file="/includes/taglibs.jsp" %>
    <tr><td colspan="5"><div><table width="100%" border="0" cellpadding="0" cellspacing="0"  >
     <tr id="oldBillComplete">   
	    <td class="bluebox2" width="4%">&nbsp;</td>         
	    <td class="bluebox" width="15%">
	    	<s:text name="AmalgProp"/>:<span class="mandatory">*</span>
	    </td>
	    <td class="bluebox" colspan="3" width="80%">     
	    <table width="35%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="AmalgTable" >
	    <s:if test="amalgPropIds==null || amalgPropIds[0]==''">
     		<tr id="amalgRow">
        		<td class="blueborderfortd" width="5%" colspan="2"><s:textfield name="amalgPropIds" id="amalgPropId" 
        			size="20" maxlength="15" onblur="trim(this,this.value);validateNumeric(this);" value="%{amalgPropIds[0]}"/>
        		</td>
        		<td class="blueborderfortd" width="3%" colspan="2"> 
        			<a href="#" onclick="return getAmalgPropStatus(this);"><s:text name="get.status"></s:text></a>
        		</td>
        		<td class="blueborderfortd" width="4%" id="AddRemoveOldPropBill">
       				<img id="addRowBtn" name="addRowBtn" src="${pageContext.request.contextPath}/image/addrow.gif" alt="Add" 
       					onclick="addAmalgPropId(); return false;" width="18" height="18" border="0" />
            		<img id="removeRowImg" name="removeRowImg" src="${pageContext.request.contextPath}/image/removerow.gif" alt="Remove" 
            			onclick="deleteAmalgPropId(this); return false;" width="18" height="18" border="0" />
       			</td>
      		</tr>
      	</s:if>
      	<s:else>
       		<s:iterator status="AmalgPropStat" value="amalgPropIds">
       		<s:if test="amalgPropIds!=null && amalgPropIds[#AmalgPropStat.index]!=''">
       			<tr id="amalgRow">
        			<td class="blueborderfortd" width="10%" colspan="2"><s:textfield name="amalgPropIds" id="amalgPropId" size="20" 
        				maxlength="9" onblur="trim(this,this.value);validateNumeric(this);" value="%{amalgPropIds[#AmalgPropStat.index]}"/>
        			</td>
        			<td class="blueborderfortd" width="10%" colspan="2"> 
        				<a href="#" onclick="return getAmalgPropStatus(this);"><s:text name="get.status"></s:text></a>
        			</td>
        			<td class="blueborderfortd" width="10%" id="AddRemoveOldPropBill">
       					<img id="addRowBtn" name="addRowBtn" src="${pageContext.request.contextPath}/image/addrow.gif" alt="Add" 
       						onclick="addAmalgPropId(); return false;" width="18" height="18" border="0" />
            			<img id="removeRowImg" name="removeRowImg" src="${pageContext.request.contextPath}/image/removerow.gif" alt="Remove" 
            				onclick="deleteAmalgPropId(this); return false;" width="18" height="18" border="0" />
       				</td>
      			</tr>
      		</s:if>
      		</s:iterator>
      </s:else>
   </table>
   </td>      
</tr>
</table></div> </td> </tr>
