<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
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

<table width="100%" border="0" cellspacing="0" cellpadding="0">

<tr>
	<td colspan="8" align="center" class="headingwk" style="border-left-width: 0px">
      		<s:text name="bill.nameofWork" /> : <s:property value="%{workcompletionInfo.workName}"/>
    </td>
</tr>
<tr>
	<td class="whiteboxwk"><s:text name="bill.estNo" /> : </td>
    <td class="whitebox2wk"><s:property value="%{workcompletionInfo.estimateNo}"/></td>
    <td class="whiteboxwk"><s:text name="bill.appNo" /> : </td>        
    <td class="whitebox2wk"><s:property value="%{workcompletionInfo.apprNo}"/></td>
    
</tr>
<tr>
<td class="whiteboxwk"><s:text name="bill.conName" /> : </td>        
<td class="whitebox2wk"><s:property value="%{workcompletionInfo.contractorName}"/></td>
<s:if test="%{workcompletionInfo.workCommencedOn!=null}">
<td class="whiteboxwk"><s:text name="bill.workCommDate" /> : </td>        
<td class="whitebox2wk"><s:text name="format.date"><s:param value="%{workcompletionInfo.workCommencedOn}"/></s:text></td>
</s:if>
<s:else>
<td class="whiteboxwk" colspan="2"></td>  
</s:else>
</tr>
<tr>
	<td class="whiteboxwk"><s:text name="bill.estAmt" /> : </td>
    <td class="whitebox2wk"><s:property value="%{workcompletionInfo.estimateAmount}"/></td>
    <td class="whiteboxwk"><s:text name="bill.projectCode" /> : </td>        
    <td class="whitebox2wk"><s:property value="%{workcompletionInfo.projectCode}"/></td>
 
</tr>
<tr>
   <td class="whiteboxwk"><s:text name="bill.mbno" /> : </td>        
    <td class="whitebox2wk"><s:property value="%{workcompletionInfo.allMBNO}"/></td>
    <td class="whiteboxwk"><s:text name="bill.workCompletedDate" /> : </td>        
    <td class="whitebox2wk">
    <s:if test="%{workcompletionInfo.workCompletedDate!=null}">
    <s:text name="format.date"><s:param value="%{workcompletionInfo.workCompletedDate}"/></s:text>
    </s:if>
    </td>
<tr>
<tr>
<td colspan="4">
 <table cellspacing="0" cellpadding="4" border="1" width="100%">
    <col width="10"></col>
    <col width="65"></col>
    <col width="40"></col>
    <col width="45"></col>
    <col width="45"></col>
    <col width="43"></col>
    <col width="50"></col>
    <col width="51"></col>
    <col width="50"></col>
    <col width="50"></col>
    <col width="102"></col>
    <tbody>
      <tr valign="top">
        <th width="10" rowspan="2"><p align="center">SL No</p> </th>
        <th width="65" rowspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="bill.descwork" /></p> </th>
        <th width="146" colspan="3" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="bill.aspertender" /></p></th>
        <th width="160" colspan="3" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="bill.asperexec" /></p></th>
        <th width="108" colspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="bill.diff" /></p></th>
        <th width="102" rowspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="mbpdf.remarks" /></p></th>
      </tr>
      <tr valign="top">
        <th width="40"><p align="center"><s:text name="bill.qty" /></p></th>
        <th width="45"><p align="center"><s:text name="bill.rate" /></p></th>
        <th width="45"><p align="center"><s:text name="bill.amount" /></p></th>
        <th width="43"><p align="center"><s:text name="bill.qty" /></p></th>
        <th width="50"><p align="center"><s:text name="bill.rate" /></p></th>
        <th width="51"><p align="center"><s:text name="bill.amount" /></p></th>
        <th width="50"><p align="center"><s:text name="bill.qty" /></p></th>
        <th width="50"><p align="center"><s:text name="bill.amount" /></p></th>
      </tr>
      <s:iterator var="comList" value="workCompletionDetailInfo" status="s"> 
      <tr valign="top">
        <td width="10"><p align="center"><s:property value='%{#s.count}' /></p></td>
        <td width="65" style="WORD-BREAK:BREAK-ALL"><p align="left"><s:if test="%{workOrderActivity.activity.schedule!=null}">
        <s:property value="%{workOrderActivity.activity.schedule.description}"/>
        </s:if>
        <s:else>
        <s:property value="%{workOrderActivity.activity.nonSor.description}"/>
        </s:else></p></td>
        <td width="40" style="WORD-BREAK:BREAK-ALL"><p align="right">
        <s:property value="%{workOrderActivity.activity.quantity}"/>
        </p></td>
        <td width="45" style="WORD-BREAK:BREAK-ALL"><p align="right">
	<s:if test="%{workOrderActivity.activity.schedule!=null}">
        	<s:property value="%{workOrderActivity.activity.SORCurrentRate.value}"/>
        </s:if>
        <s:else>
        	<s:property value="%{workOrderActivity.activity.rate.value}"/>
        </s:else></p></td>
        <td width="43" style="WORD-BREAK:BREAK-ALL"><p align="right">
         <s:text name="contractor.format.number" >
            <s:param name="value" value="%{tenderAmount}"/> 
         </s:text>
        </p></td>
        <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right"><s:property value="%{executionQuantity}"/></p></td>
        <td width="51" style="WORD-BREAK:BREAK-ALL"><p align="right"><s:property value="%{executionRate}"/></p></td>
        <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right">
        <s:text name="contractor.format.number" >
            <s:param name="value" value="%{executionAmount}"/> 
         </s:text>
        </p></td>
        <s:if test="%{executionQuantity>workOrderActivity.activity.quantity}">
        <td width="45" style="WORD-BREAK:BREAK-ALL"><font color="red"><b>
        <p align="right"><s:property value="%{executionQuantity-workOrderActivity.activity.quantity}"/></p></font></b></td>
        </s:if>
        <s:else>
         <td width="45" style="WORD-BREAK:BREAK-ALL">
        <p align="right"><s:property value="%{executionQuantity-workOrderActivity.activity.quantity}"/></p></td>
        </s:else>
        <s:if test="%{executionAmount>tenderAmount}">
        <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right"><font color="red"><b>
        <s:text name="contractor.format.number" >
            <s:param name="value" value="%{executionAmount-tenderAmount}"/> 
         </s:text></p></font></b></td>
          </s:if>
        <s:else>
         <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right">
        <s:text name="contractor.format.number" >
            <s:param name="value" value="%{executionAmount-tenderAmount}"/> 
         </s:text></p></td>
        </s:else>
       <td width="10%" >
       <input type="text" name="remarks" id="remarks" maxlength="256" style="height:150px" value='<s:property value="%{workOrderActivity.remarks}" />'  tabIndex="-1" class="selectboldwk" />
      </td>
      </tr>
     </s:iterator>
    </tbody>
  </table> 
  </td>
</tr>
</table>
