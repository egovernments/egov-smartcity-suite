<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script>
function setvalue(val,id){
	var combo =dom.get(id);
	combo.value=val;
}
</script>

<div>
<table id="checkListTable"  width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td width="30%" align="center" class="headingwk" classname="headingwk">Checklist-Name</td>
	<td width="10%" align="center" class="headingwk" classname="headingwk">Checklist-Value<span class="mandatory">*</span></td>
	<td width="30%" align="center" class="headingwk" classname="headingwk">Remarks</td>
</tr>
					
<s:if test="%{securityDepositChecklist.size() == 0}">
	<td align="left" colspan="3" class="whiteboxwkwrap" classname="whiteboxwkwrap">Nothing To Display</td>
</s:if>

	<s:iterator var="checkVar" value="securityDepositChecklist" status="status"> 
	<tr>
		<s:hidden name="appConfigValueId" value="%{id}"/>
		<td width="30%" align="center" class="whiteboxwkwrap">
			<span style="font-size:12px;">
				<s:property  value="%{value}" />
			</span>
		</td>
		
		<td  width="10%" align="center" class="whiteboxwkwrap">
            <select id="<s:property value='%{#status.index}'/>" name="selectedchecklistValue" />
 				<s:iterator var="checkVar1" value="checklistValues" status="status1"> 
					<option value="<s:property/>"><s:property/></option>
                 </s:iterator>
          	</select>
		</td>
		
  		<td width="30%" class="whiteboxwkwrap">
			<textarea class="selectmultilinewk" name=checkListremarks cols="10" rows="1" id="checkListremarks"><s:property  value="%{checkListremarks[#status.index]}" /> </textarea>
		</td>
   		<script>
			var val="<s:property value='%{selectedchecklistValue[#status.index]}'/>" ;
            var id= "<s:property value='%{#status.index}'/>";
            setvalue(val,id);
       </script>
       </tr>
	</s:iterator>
</table>
</div>