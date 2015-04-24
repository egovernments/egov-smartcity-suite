<%@ taglib prefix="s" uri="/struts-tags" %> 
			  
<html>  
	<head>  
		<title><s:text name="list.title.serviceCategory"/></title>  
	</head>  

	<body>  
	  	<div class="subheadnew"><s:text name="serviceCategory.edit.title"/></div>
	  	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		        <tr>  
		            <th class="bluebgheadtd" width="18%" style="display:none">ID</th>  
					<th class="bluebgheadtd" width="25%"><s:text name="masters.serviceCategory.serviceCategoryName"/></th>
					<th class="bluebgheadtd" width="25%"><s:text name="masters.serviceCategory.servicCategoryeCode"/></th>
					<th class="bluebgheadtd" width="16%"><s:text name="masters.edit"/></th>

				</tr>  
				<s:iterator var="p" value="serviceCategoryList">  
	            <tr>  
	                <td class="blueborderfortd" style="display:none">  
	                    <s:property value="%{id}" />
	                </td>  
					
					<s:if test="name != null">								
					<td class="blueborderfortd">
					<div align="center">  
					<s:property value="%{name}" /> 
					</div>
					</td>
					</s:if>

					<s:else>
					<td class="blueborderfortd">							
					&nbsp;	
					</td>
					</s:else>
			
					<s:if test="code!= null">								
					<td class="blueborderfortd">
					<div align="center">  
					<s:property value="%{code}" /> 
					</td>
					</div>
					</s:if>

					<s:else>
					<td class="blueborderfortd">							
					&nbsp;	
					</td>
					</s:else>
						
					<td class="blueborderfortd">
					<div align="center">  
   						<a href="serviceCategory!edit.action?id=<s:property value='%{id}'/>"><s:text name="masters.edit"/></a>  
   					</div>
					</td>  
	            </tr>  
	        	</s:iterator> 
			</table> 
			<s:form action="courtMaster" theme="simple" >  
				<div class="buttonbottom">
			 		<input name="button2" type="button" class="buttonsubmit" id="button" onclick="location.href='serviceCategory!newform.action'" value="Add New"/>
					<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
				</div>
         </s:form>   
	</body>  
</html>