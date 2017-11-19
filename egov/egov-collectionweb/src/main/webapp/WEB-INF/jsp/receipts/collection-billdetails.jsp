
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
<%@ page language="java" pageEncoding="UTF-8"%>

<%int i=1;%>
<%int rcptDtlCnt=0; %>

	 <tr>
    	<td width="100%" class="greyboxwithlink" >
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" align="left">
		<tr>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.billnumber"/></span></td>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.additionalinfo"/></span></td>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.consumerNo"/></span></td>
			<td width="30%"><span class="boldblue"><s:text name="billreceipt.payeename.description"/></span></td>
			<td width="10%">&nbsp;</td>
		</tr>
		<tr>
			<td class="textholder"><s:property value="%{referencenumber}"/></td>
			<td class="textholder"><s:property value="%{displayMsg}"/></td>
			<td class="textholder"><s:property value="%{consumerCode}"/></td>
			<td class="textholderl"><s:property value="%{payeeName}"/>,<s:property value="%{referenceDesc}"/></td>
			<td class="textholderl"><div id="bobcontent-title" class="billdetailaction"><s:text name="billreceipt.accountdetails"/></div></td>	
		</tr>
		</table>
        </td>
 	</tr>

 	<tr>
	  <td>
	    <div class="switchgroup1" id="bobcontent">
		      <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		        <tr>
		          <td class="blueborderfortd"><div class="billscroller">
		            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="accountdetailtable<%=i%>" name="accountdetailtable<%=i%>" >
		              <tr>
		                <th class="bluebgheadtd" width="14%" ><s:text name="billreceipt.accountdetails.accountcode"/></th>
		                <th class="bluebgheadtd" width="38%" ><s:text name="billreceipt.accountdetails.name"/></th>
		                <th class="bluebgheadtd" width="37%" ><s:text name="billreceipt.accountdetails.description"/></th>
		                <th class="bluebgheadtd" width="11%" ><s:text name="billreceipt.payement.amounttobecollected"/></th>
		                <!--th class="bluebgheadtd" width="11%" ><s:text name="billreceipt.payement.actualamountcollected"/></th-->
			      </tr>
		                <s:iterator value="%{receiptDetails}" status="rowreceiptdetailstatus">
			              	<tr>

				                <td class="blueborderfortd"><s:property  value="%{accounthead.glcode}" />
				                </td>
				                <td class="blueborderfortd"><s:property  value="%{accounthead.name}" /></td>
				                <td class="blueborderfortd">
				                 <s:property  value="%{description}" />
				                </td>

				                <td class="blueborderfortd">
				                   <div align="center">
				                	<input id="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" name="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" value='<s:property value="%{cramountToBePaid}"/>' type="text" readonly="true" disabled="disabled" size="12"/>
				                	
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].cramount" value="0"  id="receiptDetailList[<%=rcptDtlCnt%>].cramount"  size="12" onblur='checkandcalculatecredittotal(<%=rcptDtlCnt%>,this);'/>
									<!-- the below two fields are included to be matched in the action --> 
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" id="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" value='<s:property value="ordernumber"/>' />
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" id="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" value='<s:property value="referencenumber"/>'/>
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].dramount" id="receiptDetailList[<%=rcptDtlCnt%>].dramount"  value='<s:property value="dramount"/>'/>
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" id="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" value='<s:property value="isActualDemand"/>' />	
				                   </div>
				                </td>
			              	</tr>
			              	<%rcptDtlCnt=rcptDtlCnt+1;%>
		                </s:iterator> <!--  Finished iterating through the account heads (receipt detail) -->
		            </table> <!-- End of accountdetailtable i -->
	            </div>
	           </td>
	          </tr>
	          <%i=i+1;%>
	        </table> <!-- End of table enclosing all account detail tables -->
	      </div></td>
	  </tr>
