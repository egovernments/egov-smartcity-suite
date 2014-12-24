<%@ include file="/includes/taglibs.jsp" %>
    <tr><td colspan="5"><div><table width="100%" border="0" cellpadding="0" cellspacing="0"  >
     <tr id="oldBillComplete">   
	    <td class="bluebox2" width="4%">&nbsp;</td>         
	    <td class="bluebox" width="15%">
	    	<s:text name="AmalgProp"/>:<span class="mandatory">*</span>
	    </td>
	    <td class="bluebox" colspan="3" width="80%">     
	    <table width="35%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="AmalgTable" >
	    <s:if test="amalgPropIds==null || amalgPropIds[0]==''">
     		<tr id="amalgRow">
        		<td class="blueborderfortd" width="5%" colspan="2"><s:textfield name="amalgPropIds" id="amalgPropId" 
        			size="20" maxlength="9" onblur="trim(this,this.value);validateNumeric(this);" value="%{amalgPropIds[0]}"/>
        		</td>
        		<td class="blueborderfortd" width="3%" colspan="2"> 
        			<a href="#" onclick="return getAmalgPropStatus(this);"><s:text name="get.status"></s:text></a>
        		</td>
        		<td class="blueborderfortd" width="4%" id="AddRemoveOldPropBill">
       				<img id="addRowBtn" name="addRowBtn" src="${pageContext.request.contextPath}/image/addrow.gif" alt="Add" 
       					onclick="addAmalgPropId(); return false;" width="18" height="18" border="0" />
            		<img id="removeRowImg" name="removeRowImg" src="${pageContext.request.contextPath}/image/removerow.gif" alt="Remove" 
            			onclick="deleteAmalgPropId(this); return false;" width="18" height="18" border="0" />
       			</td>
      		</tr>
      	</s:if>
      	<s:else>
       		<s:iterator status="AmalgPropStat" value="amalgPropIds">
       		<s:if test="amalgPropIds!=null && amalgPropIds[#AmalgPropStat.index]!=''">
       			<tr id="amalgRow">
        			<td class="blueborderfortd" width="10%" colspan="2"><s:textfield name="amalgPropIds" id="amalgPropId" size="20" 
        				maxlength="9" onblur="trim(this,this.value);validateNumeric(this);" value="%{amalgPropIds[#AmalgPropStat.index]}"/>
        			</td>
        			<td class="blueborderfortd" width="10%" colspan="2"> 
        				<a href="#" onclick="return getAmalgPropStatus(this);"><s:text name="get.status"></s:text></a>
        			</td>
        			<td class="blueborderfortd" width="10%" id="AddRemoveOldPropBill">
       					<img id="addRowBtn" name="addRowBtn" src="${pageContext.request.contextPath}/image/addrow.gif" alt="Add" 
       						onclick="addAmalgPropId(); return false;" width="18" height="18" border="0" />
            			<img id="removeRowImg" name="removeRowImg" src="${pageContext.request.contextPath}/image/removerow.gif" alt="Remove" 
            				onclick="deleteAmalgPropId(this); return false;" width="18" height="18" border="0" />
       				</td>
      			</tr>
      		</s:if>
      		</s:iterator>
      </s:else>
   </table>
   </td>      
</tr>
</table></div> </td> </tr>