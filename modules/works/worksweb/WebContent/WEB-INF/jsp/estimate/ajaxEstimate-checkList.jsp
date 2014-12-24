<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

 <table  width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
	<td width="30%" align="center" class="headingwk" classname="headingwk">Checklist-Name</td>
	<td width="10%" align="center" class="headingwk" classname="headingwk">Checklist-Value</td>
	<td width="30%" align="center" class="headingwk" classname="headingwk">Remarks</td>
</tr>
					
<s:if test="%{estimateChecklist.size() == 0}">
		<td align="left" colspan="3" class="whiteboxwkwrap" classname="whiteboxwkwrap">Nothing To Display</td>
</s:if>

	<s:iterator var="checkVar" value="estimateChecklist" status="status"> 
	<tr>
		<s:hidden name="appConfigValueId" value="%{id}"/>
		<td  width="30%" class="whiteboxwkwrap">
			<s:property  value="%{value}" />
		</td>
        <td width="10%" class="whiteboxwkwrap"> 
		  <select id="<s:property value='%{#status.index}'/>" name="selectedchecklistValue" />
 				<s:iterator var="checkVar1" value="checklistValues" status="status1"> 
					<option value="<s:property/>"><s:property/></option>
                 </s:iterator>
          </select>
		</td>
		<td width="30%" class="whiteboxwkwrap">
			<textarea class="selectmultilinewk" name="checkListremarks" cols="10" rows="1" id="checkListremarks"><s:property  value="%{checkListremarks}" /> </textarea>
		</td>
	</tr>
	</s:iterator>
</table>
