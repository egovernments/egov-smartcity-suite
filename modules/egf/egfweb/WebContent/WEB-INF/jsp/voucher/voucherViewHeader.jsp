<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
	<div align="center">
	<c:set var="tdclass" value="bluebox" scope="request" />
	<table border="0" width="100%" cellspacing="0">
		<tr>
		<s:if test="%{shouldShowHeaderField('fund')}">
			<td width="25%"  class="<c:out value='${tdclass}' />">Fund</td>
			<td width="25%"  class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('fund')}"/></td>
			
		</s:if>
		<s:if test="%{shouldShowHeaderField('scheme')}">
			<td width="25%" class="<c:out value='${tdclass}' />">Scheme &nbsp;</td>
			<td width="25%" class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('scheme')}"/></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('fund') && shouldShowHeaderField('scheme')}"/>
		<s:elseif test="%{!shouldShowHeaderField('fund') && !shouldShowHeaderField('scheme')}"/>
		<s:else >
			<td width="25%"  class="<c:out value='${tdclass}' />"></td>
			<td width="25%" class="<c:out value='${tdclass}' />"></td>
		</s:else>
		<s:if test="%{shouldShowHeaderField('fund') || shouldShowHeaderField('scheme')}">
			<c:choose>
				<c:when test="${tdclass == 'bluebox'}">
					<c:set var="tdclass" value="greybox" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="tdclass" value="bluebox" scope="request" />
				</c:otherwise>
			</c:choose>
		</s:if>
		</tr>
		<tr>
		<s:if test="%{shouldShowHeaderField('subscheme')}">
			<td width="25%" class="<c:out value='${tdclass}' />">Sub Scheme &nbsp;</td>
			<td width="25%" class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('subscheme')}"/></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('fundsource')}">
			<td width="25%"  class="<c:out value='${tdclass}' />">Financing Source &nbsp; </td>
			<td width="25%"  class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('fundsource')}"/></td>
			
		</s:if>
		<s:if test="%{shouldShowHeaderField('fundsource') && shouldShowHeaderField('subscheme')}"/>
		<s:elseif test="%{!shouldShowHeaderField('fundsource') && !shouldShowHeaderField('subscheme')}"/>
		<s:else >
			<td width="25%"  class="<c:out value='${tdclass}' />"></td>
			<td width="25%" class="<c:out value='${tdclass}' />"></td>
		</s:else>
		<s:if test="%{shouldShowHeaderField('fundsource') || shouldShowHeaderField('subscheme')}">
			<c:choose>
				<c:when test="${tdclass == 'bluebox'}">
					<c:set var="tdclass" value="greybox" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="tdclass" value="bluebox" scope="request" />
				</c:otherwise>
			</c:choose>
		</s:if>
		</tr>
		<tr>
		<s:if test="%{shouldShowHeaderField('department')}">
			<td width="25%"  class="<c:out value='${tdclass}' />">Department</td>
			<td width="25%"   class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('department')}"/></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('functionary')}">
			<td width="25%"  class="<c:out value='${tdclass}' />">Functionary &nbsp; </td>
			<td width="25%" class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('functionary')}"/></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('department') && shouldShowHeaderField('functionary')}"/>
		<s:elseif test="%{!shouldShowHeaderField('department') && !shouldShowHeaderField('functionary')}"/>
		<s:else >
			<td width="25%"  class="<c:out value='${tdclass}' />"></td>
			<td width="25%" class="<c:out value='${tdclass}' />"></td>
		</s:else>
		<s:if test="%{shouldShowHeaderField('department') || shouldShowHeaderField('functionary')}">
			<c:choose>
				<c:when test="${tdclass == 'bluebox'}">
					<c:set var="tdclass" value="greybox" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="tdclass" value="bluebox" scope="request" />
				</c:otherwise>
			</c:choose>
		</s:if>
		</tr>
		
		<tr>
		<s:if test="%{shouldShowHeaderField('field')}">
			<td width="25%"  class="<c:out value='${tdclass}' />">Field &nbsp;</td>
			<td width="25%"  class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('field')}"/></td>
			<td width="25%"  class="<c:out value='${tdclass}' />"></td>
			<td width="25%" class="<c:out value='${tdclass}' />"></td>
			<c:choose>
				<c:when test="${tdclass == 'bluebox'}">
					<c:set var="tdclass" value="greybox" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="tdclass" value="bluebox" scope="request" />
				</c:otherwise>
			</c:choose>
		</s:if>
		</tr>
		
		<tr>
			<td width="25%"  class="<c:out value='${tdclass}' />">Narration &nbsp;</td>
			<td colspan="3"  class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('narration')}"/></td>
			<c:choose>
				<c:when test="${tdclass == 'bluebox'}">
					<c:set var="tdclass" value="greybox" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="tdclass" value="bluebox" scope="request" />
				</c:otherwise>
			</c:choose>
			
		</tr>
	</table>
	</div>