<%@ include file="/includes/taglibs.jsp"%>
<div align="center">
<div class="headingsmallbg">
					<span class="bold"><s:text name='approval.existingdetails.title' />
					</span>
				</div>  		       
	<c:choose>	
			<c:when test="${!workflowBean.workFlowHistoryItems.isEmpty()}">
				<table id="workflowHistory" class="table table-bordered"  >
					<thead>
						<th>Date</th>
						<th>Sender</th>
						<th>Status</th>
						<th>Action to Perform</th>
						<th>Comments</th>
				
					</thead> 
					<tbody>
									<c:forEach  var="contact"  items="${workflowBean.workFlowHistoryItems}" 
										varStatus="counter">
										<tr>
									<td class="blueborderfortd"><input type="text" class="form-control" value="${contact.lastModifiedDate}"  readonly="readonly"> </td>
									<td class="blueborderfortd"><input type="text" class="form-control" value="${contact.senderName}"  readonly="readonly"> </td>
									<td class="blueborderfortd"><input type="text" class="form-control" value="${contact.value}"  readonly="readonly"> </td>
									<td class="blueborderfortd"><input type="text" class="form-control" value="${contact.nextAction}"  readonly="readonly"> </td>
									<td class="blueborderfortd"><input type="text" class="form-control" value="${contact.comments}"  readonly="readonly"> </td>
											</tr>
									</c:forEach>
								</tbody>
						</table>
					</c:when>
				<c:otherwise>No data found.</c:otherwise>
					</c:choose>
					
</div>					