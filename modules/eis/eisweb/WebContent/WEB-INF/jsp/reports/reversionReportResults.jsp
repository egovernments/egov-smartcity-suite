<h1><display:caption style="text-align: center">List of Employees applied for Reversion request</display:caption></h1>
<br>
<display:column title="Sl No" style="width:100" >
						<s:property
							value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}" />
					</display:column>
					
						<br>
					<display:column title="EMPLOYEE CODE" style="width:150">
						<c:out value="${currentRowObject[0]}" />
					</display:column>
					
					<display:column title="EMPLOYEE NAME" style="width:150">
						<c:out value="${currentRowObject[1]}" />
					</display:column>
					
					<display:column title="DEPARTMENT" style="width:150">
						<c:out value="${currentRowObject[2]}" />
					</display:column>
					
					<display:column title="STATUS" style="width:150">
						<c:out value="${currentRowObject[3]}" />
					</display:column>
					
					<display:column title="View Details" style="width:150">
					  <a href="javascript:viewReversionDetails();" >view</a>
					  </display:column>