<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script src="<egov:url path='js/works.js'/>"></script> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name="revisionWO.page.header.negotiation" /> 
			</div>
		</td>
	</tr>
	<tr>
		<td width="25%" class="whiteboxwk"><s:text name="workOrder.originalWONumber" />:</td> 
				<td width="25%" class="whitebox2wk" >
		<a href="#" onclick="showWorkOrderDetails(<s:property  value="%{parent.id}"/>)"><s:property  value="%{parent.workOrderNumber}"   /></a>
		</td>
		 
		<td width="25%" class="whiteboxwk"><s:text name="revisionWO.No" />:</td> 
		<td width="25%" class="whitebox2wk" >
				<s:if test="%{revisionWOList.size() != 0}">
					<s:iterator var="re" value="revisionWOList" status="status">
					<s:if test="%{id!=revWorkOrderId}">
						<a href="#" onclick="showRevisionWorkOrderDetails(<s:property  value="%{id}"/>)"><s:property  value="%{workOrderNumber}"   /></a>
						<br>
					</s:if> 
					</s:iterator> 
				</s:if>  
		</td> 
	</tr>
	
	<tr>
		<td class="greyboxwk">
			<s:text name='revisionWO.executingDepartment' />:
		</td>
		<td class="greybox2wk"><s:property value="%{revisionEstimate.executingDepartment.deptName}" /></td> 
		
		<td class="greyboxwk">
				Revision <s:text name='revisionWO.estimateNo' /> :
		</td>
		<td  class="greybox2wk"><s:property value="%{revisionEstimate.estimateNumber}" /></td>
	</tr>
	
	<tr>
		<td class="whiteboxwk">
				<s:text name='revisionWO.nameOfWork'/> :
		 </td>
        <td class="whitebox2wk"><s:property value="%{revisionEstimate.name}" /></td> 
        <td class="whiteboxwk">
				<s:text name="estimate.date" /> : 
		</td>
        <td class="whitebox2wk"><s:date name="revisionEstimate.estimateDate" var="estDateFormat" format="dd/MM/yyyy"/>
       	<s:property value="%{estDateFormat}" /></td>
   </tr> 
   
    <tr>
   		<td class="greyboxwk">
			<s:text name="workorder.amount"/> : 
		</td> 
		<td class="greybox2wk"><s:text name="contractor.format.number" ><s:param value="%{parent.workOrderAmount}" /></s:text></td>	
		
        <td class="greyboxwk"> 
        		<s:text name='revisionWO.projectCode'/>
        </td>
        <td class="greybox2wk"><s:property value="%{revisionEstimate.parent.projectCode.code}" /></td>   
   </tr> 
    
    <tr>
    
		<td class="whiteboxwk"><s:text name='revisionWO.nameOfContractor'/> : </td>
		<td class="whitebox2wk"><s:property value="%{contractor.name}" /></td>
 
		<td class="whiteboxwk"><s:text name="contractor.code"/> :</td>
		<td class="whitebox2wk"><s:property value="%{contractor.code}"/></td>
	</tr>
	
	<tr>
   		<td width="11%" class="greyboxwk">
   		<s:text name="wp.emp"/> :
   		</td>
        <td width="21%" class="greybox2wk"><s:property value="%{empName}" />
        <s:hidden name="workOrderPreparedBy" id="workOrderPreparedBy" value="%{preparedBy}" />
	    </td>
	    
	    <td width="25%" class="greyboxwk"> <s:text name="re.workorder.amount" />:</td>
		<td width="25%" class="greybox2wk"><s:text name="contractor.format.number" ><s:param  value="%{workOrderAmount}"   /></s:text></td>
   </tr>
	
	<tr> 
	 	<td class="whiteboxwk">
   			 <s:text name="sec.deposit"/> :
		</td>
		<td class="whitebox2wk">
			<s:textfield name="securityDeposit" value="%{securityDeposit}" id="securityDeposit" cssClass="selectamountwk" 
			onblur="roundOffSecurityDepositAmount()"/>
		</td>	
		<td width="25%" class="whiteboxwk"></td>
		<td width="25%" class="whitebox2wk"> </td>
	</tr>
	    
  </table>
