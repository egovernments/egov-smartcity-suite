<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
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
