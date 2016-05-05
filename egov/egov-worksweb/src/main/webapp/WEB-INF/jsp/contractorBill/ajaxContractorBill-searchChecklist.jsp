<%@ taglib prefix="s" uri="/struts-tags" %>  
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<script>
function setvalue(val,id){
var combo =dom.get(id);
var option = document.createElement("option");
option.text =val;
option.value = val;
combo.options.add(option);
}
</script>

<div>

 <table  width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr>
		<td width="30%" align="center" class="headingwk" classname="headingwk">Checklist-Name</td>
		<td width="10%" align="center" class="headingwk" classname="headingwk">Checklist-Value</td>
	           </tr>
					
		<s:if test="%{finalBillChecklist.size() == 0}">
		<td align="left" colspan="2" class="whiteboxwkwrap" classname="whiteboxwkwrap">Nothing To Display</td>
		
	 </s:if>
 <tr>
                <s:iterator var="checkVar" value="finalBillChecklist" status="status"> 
	        <s:hidden name="appConfigValueId" value="%{id}"/>
		<td width="30%" align="center" class="whiteboxwkwrap"><s:property  value="%{value}" /></td>
                 <td  width="10%" align="center" class="whiteboxwkwrap">
                          <select id="<s:property value='%{#status.index}'/>" />
                  </td>
	      	<script>
				var val="<s:property value='%{selectedchecklistValue[#status.index]}'/>" ;
                                var id= "<s:property value='%{#status.index}'/>";
	               setvalue(val,id);
	          </script>
	  	</tr>
   </s:iterator>
	</table>
</div>
