<%@ taglib prefix="s" uri="/struts-tags" %>  
<script>
function setvalue(val,id){
var combo =dom.get(id);
var option = document.createElement("option");
option.text =val;
option.value = val;
combo.options.add(option);
}
</script>

<div>

 <table  width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr>
		<td width="30%" align="center" class="headingwk" classname="headingwk">Checklist-Name</td>
		<td width="10%" align="center" class="headingwk" classname="headingwk">Checklist-Value</td>
	           </tr>
					
		<s:if test="%{finalBillChecklist.size() == 0}">
		<td align="left" colspan="2" class="whiteboxwkwrap" classname="whiteboxwkwrap">Nothing To Display</td>
		
	 </s:if>
 <tr>
                <s:iterator var="checkVar" value="finalBillChecklist" status="status"> 
	        <s:hidden name="appConfigValueId" value="%{id}"/>
		<td width="30%" align="center" class="whiteboxwkwrap"><s:property  value="%{value}" /></td>
                 <td  width="10%" align="center" class="whiteboxwkwrap">
                          <select id="<s:property value='%{#status.index}'/>" />
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
