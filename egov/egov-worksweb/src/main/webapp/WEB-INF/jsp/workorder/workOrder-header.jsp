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
		 <s:hidden name="tenderRespId" id="tenderRespId"  value="%{tenderRespId}"></s:hidden>
		 <s:hidden name="tenderRespContrId" id="tenderRespContrId"  value="%{tenderRespContrId}"/>
		  <s:hidden name="tenderNumber" id="tenderNumber"  value="%{tenderResponse.tenderEstimate.tenderHeader.tenderNo}"></s:hidden>
		  <s:hidden name="negotiationNumber" id="negotiationNumber"  value="%{tenderResponse.negotiationNumber}"></s:hidden>
		   <s:hidden name="estimateId" id="estimateId"  value="%{estimateId}"></s:hidden>
		  
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="/egworks/resources/erp2/images/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name="page.header.negotiation" />
			</div>
		</td>
	</tr>
	<tr>
		<td class="greyboxwk">
			<s:text name='tenderNegotiation.executingDepartment' />:
		</td>
		<td class="greybox2wk">
			<s:select id="department" name="deptId" headerKey="-1"  headerValue="%{getText('estimate.default.select')}"
				cssClass="selectwk" list="%{dropdownData.departmentList}" value="%{deptId}"
				listKey="id" listValue="deptName" 
				onchange= "populateDesignation1();setupPreparedByList(this)"/>
			<egov:ajaxdropdown id="assignedTo1"fields="['Text','Value']" 
			dropdownId="assignedTo1" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
			<egov:ajaxdropdown id="assignedTo2"fields="['Text','Value']" 
			dropdownId="assignedTo2" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
		     <s:hidden name="defaultDepartmentId" id="defaultDepartmentId"  />
		     <s:hidden name="loggedInUserEmployeeCode" id="loggedInUserEmployeeCode"  />
		</td>
		<td class="greyboxwk">
			<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
					<s:text name='tenderNegotiation.estimateNo' /> :
			</s:if>
			<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
				<s:text name='tenderNegotiation.WorksPackageNo' /> :
			</s:elseif>
			</td>
			<td class="greybox2wk">
			<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
					<input type="text" name="estimateNo" id="estimateNo"
						value='<s:property value="%{tenderResponse.tenderEstimate.abstractEstimate.estimateNumber}" />'
						readonly="readonly" tabIndex="-1" class="selectboldwk" />
			</s:if>
			<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
				<input type="text" name="estimateNo" id="estimateNo"
						value='<s:property value="%{tenderResponse.tenderEstimate.worksPackage.wpNumber}" />'
						readonly="readonly" tabIndex="-1" class="selectboldwk" />
						 <s:hidden name="packageNumber" id="packageNumber"  value="%{tenderResponse.tenderEstimate.worksPackage.wpNumber}"></s:hidden>
			</s:elseif>
		</td>
	</tr>
		
	<tr>
		<td class="whiteboxwk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
				<s:text name='tenderNegotiation.nameOfWork'/> :
			</s:if>
			<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
				<s:text name='tenderNegotiation.nameOfWork'/> :
			</s:elseif>
		 </td>
        <td class="whitebox2wk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
        		<input type="text" name="nameOfWork" id="nameOfWork" 
        			value='<s:property value="%{tenderResponse.tenderEstimate.abstractEstimate.name}" />'  
        			readonly="readonly" tabIndex="-1" class="selectboldwk" />
        	</s:if>
        	<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
        		<input type="text" name="nameOfWork" id="nameOfWork" 
        			value='<s:property value="%{tenderResponse.tenderEstimate.worksPackage.name}" />' 
         			readonly="readonly" tabIndex="-1" class="selectboldwk" />
        	</s:elseif>
        </td>
        <td class="whiteboxwk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
				<s:text name="estimate.date" /> :
			</s:if>
			<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
				<s:text name="wp.date"/> :
			</s:elseif>
		</td>
        <td class="whitebox2wk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
        		<s:date name="tenderResponse.tenderEstimate.abstractEstimate.estimateDate" var="estDateFormat" format="dd/MM/yyyy"/>
        		<s:textfield name="estimateDate" value="%{estDateFormat}" id="estimateDate" readonly="true" cssClass="selectboldwk" />
        	</s:if>
        	<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
        		<s:date name="tenderResponse.tenderEstimate.worksPackage.wpDate" var="wpDateFormat" format="dd/MM/yyyy"/>
        		<s:textfield name="wpDate" value="%{wpDateFormat}" id="wpDate" readonly="true" cssClass="selectboldwk"/>
        	</s:elseif>
        </td>
   </tr>
   
   <s:if test="%{tenderResponseType!=null && percTenderType.equalsIgnoreCase(tenderResponseType)}" >
   <tr>
   		<td class="greyboxwk">
			<s:text name="estimate.amount"/> :
		</td>
		<s:if test="%{tenderResponse!=null && tenderResponse.getTenderResponseContractors().size()>1}" >
		<td class="greybox2wk">
			<input type="text" id="estimateAmount"
				readonly="readonly" tabIndex="-1" class="selectamountwk" value='<s:property value="%{estimateAmount}" />'  />
		</td>
        <td class="greyboxwk">
        		<s:text name='activity.assigned.contractor.amount'/>
        </td>
        <td class="greybox2wk">
        		<input type="text" name="activityAssignedAmt" id="activityAssignedAmt" 
        			value='<s:property value="%{activityAssignedAmt}" />' 
        				readonly="readonly" tabIndex="-1" class="selectboldwk"/>
       </td>
		</s:if>
		<s:else>
			<td class="greybox2wk" colspan="3">
				<input type="text" id="estimateAmount"
					readonly="readonly" tabIndex="-1" class="selectamountwk" value='<s:property value="%{estimateAmount}" />'  />
			</td>
		</s:else>	
   </tr>
     
   <tr>
   		<td class="whiteboxwk">
			<s:text name="workorder.percentage.negotiated"/> :
		</td>
		<td class="whitebox2wk" colspan="3">
			<input type="text" id="tenderpercentage"
				readonly="readonly" tabIndex="-1" class="selectamountwk" value='<s:property value="%{tenderResponse.percNegotiatedAmountRate}" />'  />
		</td>	
   </tr>
   </s:if>
   <tr>
   		<td class="greyboxwk">
			<s:text name="workorder.amount"/> :
		</td>
		<td class="greybox2wk">
			<input type="text" name="workOrderAmount" id="workOrderAmount"
				readonly="readonly" tabIndex="-1" class="selectamountwk" value='<s:property value="%{workOrderAmount}" />' />
		</td>	
        <td class="greyboxwk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
        		<s:text name='tenderNegotiation.projectCode'/>
        	</s:if>
        	<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
        		<s:text name='tenderNegotiation.tenderFileNo'/>
        	</s:elseif> :
        </td>
        <td class="greybox2wk">
        	<s:if test="%{tenderResponse!=null && tenderResponse.tenderEstimate.abstractEstimate!=null}">
        		<input type="text" name="projectCode" id="projectCode" 
        			value='<s:property value="%{tenderResponse.tenderEstimate.abstractEstimate.projectCode.code}" />' 
        				readonly="readonly" tabIndex="-1" class="selectboldwk"/>
        	</s:if>
        	<s:elseif test="%{tenderResponse!=null && tenderResponse.tenderEstimate.worksPackage!=null}">
        		<input type="text" name="tenderFileNo" id="tenderFileNo" 
        			value='<s:property value="%{tenderResponse.tenderEstimate.worksPackage.tenderFileNumber}" />' 
        				readonly="readonly" tabIndex="-1" class="selectboldwk"/>
        	</s:elseif>
       </td>
   </tr>
    <tr>
		<td class="whiteboxwk"><s:text name='tenderNegotiation.nameOfContractor'/> : </td>
		<td class="whitebox2wk">
			<s:if test="%{tenderResponseContractor!=null}">
				<input type="text" name="contractorName" id="contractorName" 
					value='<s:property value="%{tenderResponseContractor.contractor.name}" />' 
				 		class="selectboldwk" readonly="readonly"/>
				 <s:hidden name="contractor" id="contractor"  value="%{tenderResponseContractor.contractor.id}"></s:hidden>
			</s:if>
		</td>
		<td class="whiteboxwk"><s:text name="contractor.code"/> :</td>
		<td class="whitebox2wk">
		<s:if test="%{tenderResponseContractor!=null}">
			<input type="text" name="contractorCode" id="contractorCode" 
				value='<s:property value="%{tenderResponseContractor.contractor.code}"/>' 
			 		class="selectboldwk" readonly="readonly"/>
		</s:if>
		</td>
	</tr>
    <tr>
   		 <td class="greyboxwk"><s:text name="payment.terms"/> :</td>
		<td class="greybox2wk">
			<s:textarea name="paymentTerms" cols="27" rows="4" cssClass="selectwk" id="paymentTerms" />
		</td>
		 <td class="greyboxwk">
        	<s:text name="workorder.details"/> :
        </td>
        <td class="greybox2wk">
			<s:textarea name="workOrderDetails" cols="27" rows="4" cssClass="selectwk" id="workOrderDetails" />
		</td>	
   </tr>
    <tr>
   		<td class="whiteboxwk"> 
   			<s:text name="agg.details"/> :
   		</td>
        <td colspan="3" class="whitebox2wk">
       	 <s:textarea name="agreementDetails" cols="56" rows="4" cssClass="selectwk" id="agreementDetails" />
        </td>
   </tr>
    <tr>
   		<s:if test="%{siteHandOverDate!=null}">
   		<td class="whiteboxwk"><s:text name="date.site"/> :</td>
		<td class="whitebox2wk">
   			<s:date name="siteHandOverDate" var="siteHandOverDateFormat" format="dd/MM/yyyy"/>
        	<s:textfield name="siteHandOverDate" value="%{siteHandOverDateFormat}" id="siteHandOverDate" readonly="true" cssClass="selectboldwk" />
        	</td>
   		</s:if>
   		<s:else>
   		<td class="whiteboxwk">&nbsp;</td>
		<td class="whitebox2wk">
			&nbsp;
		</td>
		</s:else>
		<s:if test="%{workCommencedDate!=null}">
		<td class="whiteboxwk">
        	<s:text name="date.workcommenced"/> :
        </td>
        <td class="whitebox2wk">
			<s:date name="workCommencedDate" var="workCommencedDateFormat" format="dd/MM/yyyy"/>
        	<s:textfield name="workCommencedDate" value="%{workCommencedDateFormat}" id="workCommencedDate" readonly="true" 
        	cssClass="selectboldwk" />
        	</td>
		</s:if>
		<s:else>
		 <td class="whiteboxwk">
        	&nbsp;
        </td>
        <td class="whitebox2wk">
			&nbsp;
		</td>	
		</s:else>
   </tr>
    <tr>
   		<td width="11%" class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="wp.emp"/> :
   		</td>
        <td width="21%" class="greybox2wk">
	         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workOrderPreparedBy" 
	         id="preparedBy" cssClass="selectwk" 
	         list="dropdownData.preparedByList" listKey="id" listValue="%{employeeName + ' ~ ' + desigId.designationName }" value="%{workOrderPreparedBy.id}"/>
	         <s:hidden name="defaultPreparedBy" id="defaultPreparedBy"  />
	    </td>
	    <td width="11%" class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="wo.date"/> :
   		</td>
        <td width="21%" class="greybox2wk">
	        <s:date name="workOrderDate" var="workOrderDateFormat" format="dd/MM/yyyy"/>
		    <s:textfield name="workOrderDate" value="%{workOrderDateFormat}" id="workOrderDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="isvalidFormat(this)"/>
        		 <a href="javascript:show_calendar('forms[0].workOrderDate',null,null,'DD/MM/YYYY');" id="dateHref2"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a> 
	    </td>
   </tr>
   
    <tr>
   		<td class="whiteboxwk">
   			 <s:text name="sec.deposit"/> :
		</td>
		<td class="whitebox2wk">
			<s:textfield name="securityDeposit" value="%{securityDeposit}" id="securityDeposit" cssClass="selectamountwk" 
			onblur="roundOffEmdAmountDeposited()"/>
		</td>	
        <td class="whiteboxwk">
        	<s:text name="labour.welfund"/> :
        </td>
        <td class="whitebox2wk">
        	<s:textfield name="labourWelfareFund" value="%{labourWelfareFund}" id="labourWelfareFund" cssClass="selectamountwk" 
        	onblur="roundOffEmdAmountDeposited()"/>
        </td>
   </tr>   
   <tr>
   		<td class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="emd.amt"/> :
		</td>
		<td class="greybox2wk">
			<s:textfield name="emdAmountDeposited" cssClass="selectamountwk" id="emdAmountDeposited" cssClass="selectamountwk" 
			onblur="roundOffEmdAmountDeposited()"/>
		</td>
       <td class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="con.period"/> : 
		</td>
		<td class="greybox2wk">
        	<s:textfield name="contractPeriod" value="%{contractPeriod}" id="contractPeriod" cssClass="selectwk" onkeypress="return isNumberKey(event)" />
        </td>
       
   </tr>
   <tr>
        <td class="whiteboxwk" ><span class="mandatory">*</span>
   			<s:text name="defect.liability.period"/> :</td>
		<td class="whitebox2wk" colspan="3">
			<s:textfield  name="defectLiabilityPeriod" value="%{defectLiabilityPeriod}" id="defectLiabilityPeriod" cssClass="selectamountwk" onblur="roundOffDLP()" />
		</td>
	</tr>	
    <tr>
   		<td class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="wo.allocated.to1"/> :
		</td>
		<td class="greybox2wk">
			<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo1" 
	         id="assignedTo1" value="%{assignedTo1}" cssClass="selectwk" 
	         list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser1(this)"/>
	         <egov:ajaxdropdown id="engineerIncharge" fields="['Text','Value']" dropdownId='engineerIncharge' 
		        	  url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
		</td>	
        <td class="greyboxwk">
        	<span class="mandatory">*</span><s:text name="wo.user1"/> :
        </td>
        <td class="greybox2wk">
        	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge" 
	         id="engineerIncharge"  cssClass="selectwk" 
	         list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" onchange="validateWOAllocatedUsers();" value="%{engineerIncharge.id}"/>
        </td>
   </tr>
   <s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
   <tr>
   		<td class="whiteboxwk">
   			 <s:text name="wo.allocated.to2"/> :
		</td>
		<td class="whitebox2wk">
			<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo2" 
	         id="assignedTo2" value="%{assignedTo2}" cssClass="selectwk" 
	         list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser2(this)" />
	         <egov:ajaxdropdown id="engineerIncharge2" fields="['Text','Value']" dropdownId='engineerIncharge2' 
		        	  url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
		</td>	
        <td class="whiteboxwk">
        	<s:text name="wo.user2"/> :
        </td>
        <td class="whitebox2wk">
        	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge2" 
	         id="engineerIncharge2"  cssClass="selectwk" 
	         list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" onchange="validateWOAllocatedUsers();" value="%{engineerIncharge2.id}"/>
        </td>

   </tr>
   </s:if> 
  </table>
