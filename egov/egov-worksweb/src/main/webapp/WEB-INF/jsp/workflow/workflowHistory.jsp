<%@ include file="/includes/taglibs.jsp"%>
<style>
table.workflowHistory {
	background-color : transparent;
	color : navy;
	width :100%;
}
table.workflowHistory th {
	color:black;
	text-align: center;
	font-size: 15px;
	border:1px solid gray;
}

table.workflowHistory td {
	text-align:center;
	padding : 5px;
	color:navy;
	font-size : 11px;
	border : 1px solid gray;
		
}
</style>
<div align="center">
<div class="headingsmallbg">
					<span class="bold"><s:text name='approval.existingdetails.title' />
					</span>
				</div>  		       
	<c:choose>	
			<c:when test="${!workflowHistory.isEmpty()}">
				<table id="workflowHistory" class="workflowHistory"  >
					<thead>
						<th>Date</th>
						<th>Sender</th>
						<th>Status</th>
						<th>Action to Perform</th>
						<th>Comments</th>				
					</thead> 
					<tbody>
							<c:forEach  var="contact"  items="${workflowHistory}" varStatus="counter">
									<tr>
										<td class="blueborderfortd">${contact.lastModifiedDate}</td>
										<td class="blueborderfortd">${contact.senderName}</td>
										<td class="blueborderfortd">${contact.value}</td>
										<td class="blueborderfortd">${contact.nextAction}</td>
										<td class="blueborderfortd">${contact.comments}</td>
									</tr>
							</c:forEach>						
					</tbody>
				</table>
			</c:when>
			<c:otherwise>No data found.</c:otherwise>
	</c:choose>					
</div>					