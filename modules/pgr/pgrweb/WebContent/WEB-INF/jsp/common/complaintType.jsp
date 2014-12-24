<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
			<tr>
						<td width="25%" class="whiteboxwk"><s:text name="complaintType.top5"/></td>
						<td width="25%" class="whitebox2wk">
						<s:select name="complaintType" id="complaintType[0]" list="dropdownData.top5Complaints" listKey="id" 
						listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{complaintType.id}" 
							onchange="loadTitle(this);"/>
						</td><td width="25%" class="whiteboxwk"></td><td width="25%" class="whitebox2wk"></td>
						
						
			</tr>
			<c:set var="tdclass1" value="whiteboxwk" scope="request" />
			<c:set var="tdclass2" value="whitebox2wk" scope="request" />
			<s:iterator value="compGroupTypeMap" status="status">
				<s:if test="#status.odd == true "> <tr> 
				
				<c:choose>
						<c:when test="${tdclass1 == 'whiteboxwk'}">
							<c:set var="tdclass1" value="greyboxwk" scope="request" />
						</c:when>
						<c:otherwise>
							<c:set var="tdclass1" value="whiteboxwk" scope="request" />
						</c:otherwise>
					</c:choose>
				
					<c:choose>
						<c:when test="${tdclass2 == 'whitebox2wk'}">
							<c:set var="tdclass2" value="greybox2wk" scope="request" />
						</c:when>
						<c:otherwise>
							<c:set var="tdclass2" value="whitebox2wk" scope="request" />
						</c:otherwise>
					</c:choose>
				
				 </s:if>
								
						<td width="25%" class="<c:out value='${tdclass1}' />"> <s:property value="key"/> </td>
						<td width="25%" class="<c:out value='${tdclass2}' />"> 
						
						<s:select name="complaintType" id="complaintType[%{#status.index}]" list="value" listKey="id" 
						listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{complaintType.id}" 
							onchange="loadTitle(this);" />
						</td>
					<s:if test="#songStatus.odd == true "> 
						</tr> 
					
					</s:if>
					
				</s:iterator>
			
		

			