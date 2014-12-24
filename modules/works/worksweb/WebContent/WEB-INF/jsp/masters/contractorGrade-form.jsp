
<div class="navibarshadowwk"></div>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>  
    
 <div class="rbcontent2">   

<table width="100%" border="0" cellspacing="0" cellpadding="0">
        	<tr>
          		<td colspan="4" class="headingwk" align="left">
          			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            		<div class="headplacer"><s:text name="contractor.grade.header" /></div>
            	</td>
        	</tr>
        	
        <tr>
				<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="contractor.grade.master.grade" />:</td>
            	<td class="whitebox2wk"><s:textfield name="grade" maxlength="20" id="grade"  cssClass="selectwk" value="%{grade}" /></td>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="contractor.grade.master.description" />:</td>
                <td class="whitebox2wk"><s:textfield name="description" maxlength="100" cssClass="selectwk" id="description" value="%{description}"/></td>
        </tr>	
        
        <tr>
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="contractor.grade.master.minamount" />:</td>
            	<td class="greybox2wk"><s:textfield name="minAmount"  id="minAmount"  cssClass="amount" value="%{minAmount}" /></td>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="contractor.grade.master.maxamount" />:</td>
            	<td class="greybox2wk"><s:textfield name="maxAmount"  id="maxAmount"  cssClass="amount" value="%{maxAmount}" /></td>
        </tr>
       <tr>
	          	<td colspan="4" class="shadowwk"></td>
	   </tr>

      <tr>
               <td colspan="4" class="mandatory" align="right">* <s:text name="message.mandatory" /></td>
               
      </tr>

</table>	   

</div>
<div class="rbbot2"><div></div></div>



</div>
</div>
</div>

<script>
<s:if test="%{mode=='view'}">
	for(i=0;i<document.contractorGrade.elements.length;i++){
		document.contractorGrade.elements[i].disabled=true;
		document.contractorGrade.elements[i].readonly=true;
	} 
	
	
</s:if>


</script>