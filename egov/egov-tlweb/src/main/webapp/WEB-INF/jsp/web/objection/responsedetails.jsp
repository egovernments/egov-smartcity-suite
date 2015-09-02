<s:set var="cntr" value="0" />
<s:iterator var="act" value="activities" status="status">
	<s:if test="#act.type == 'Response'">
		<s:if test="#cntr==0">
			<s:set var="cntr" value="1" />
			<tr>
				<td colspan="5" class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20" />
					</div>
					<div class="headplacer">
						<s:property value="#act.type" />
						Details
					</div>
				</td>
			</tr>
		</s:if>
		<%@ include file='objectionactivities.jsp'%>
	</s:if>
</s:iterator>
