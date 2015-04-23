<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<s:if	test="basicProperty.objections.size()>0">
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<s:text name="objection.details.heading" />
				</div>
			</td>
		</tr>
		<tr>
				<td colspan="5" class="bluebgheadtd">

				<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tablebottom">
        	 <tr> 
        	 <th class="bluebgheadtd"><s:text name="slno" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.number" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.received.date" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.received.by"/></th>
        	 
        	 <th class="bluebgheadtd"><s:text name="remarks.head"/></th>
        	 <th class="bluebgheadtd"><s:text name="objection.status"/></th></tr>
        	 <s:iterator var="s" value="basicProperty.objections" status="status">
			<tr>
			<td class="greybox"><div align="center"><s:property value="#status.index+1" /></div></td>
			 <td class="greybox" >
			 <s:hidden name="id" value="%{id}"></s:hidden>
			 <a href="${pageContext.request.contextPath}/objection/objection!viewObjectionDetails.action?objection.objectionNumber=<s:property value="%{objectionNumber}"/>">
			 <s:property value="%{objectionNumber}"/></a> </td>	
			    <td class="greybox" ><div align="center"><s:property default="N/A" value="%{fmtdReceivedOn}" /></div></td>			
			<td class="greybox" ><div align="center"><s:property default="N/A" value="%{recievedBy}"/></div></td>
			
			<td  class="greybox" ><div align="center"><s:property default="N/A" value="%{remarks}"/></div></td>
			<td  class="greybox" ><div align="center"><s:property default="N/A" value="%{egwStatus.description}"/>
			</div></td>
			</tr></s:iterator>
			</table>
			</td>
			</tr>
</s:if>
