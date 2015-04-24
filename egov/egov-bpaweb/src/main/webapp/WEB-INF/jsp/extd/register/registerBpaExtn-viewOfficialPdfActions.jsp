<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td colspan="4">
		<div class="headingbg">
		<span class="bold">
			<s:text name="page.title.viewPdf.details" />
		</span>
		</div>
	</td>
</tr>
	
<tr> 
<td>
	<div>
	  	<table width="100%" cellpadding="0" cellspacing="1" border="0"  class="tablebottom" id="regnOfficialActionsList" style="border:1px;width:100%;empty-cells:show;">
			<tr>
			<th class="bluebgheadtd" style="text-align:center;"> <s:text name="slNo.label" /> </th>
			<th  class="bluebgheadtd" style="text-align:center;"> <s:text name="regPdf.label" /> </th>
				<s:iterator value="regnOfficialActionsList[0].officialActions" status="s1"> 
					<th class="bluebgheadtd"  style="text-align:center;">
						<table width="100%" border="0" cellpadding="0" class="tablebottom" cellspacing="1" style="border:1px;width:100%;empty-cells:show;">
				       		<tr>
				       			<s:property value="userName"/>
				       		</tr>
			       			</table>						
					</th>
				</s:iterator>
			</tr>
			<s:iterator id="t" var="mo" value="regnOfficialActionsList" status="s">
			<tr>
				<td class="bluebox" style="text-align:center" ><s:property value='#s.index+1'/></td>	 		  	        		
				<td class="bluebox" style="text-align:left;" ><s:property value="viewType"/></td>
			
				 <s:iterator id="t2" var="mod" value="officialActions" status="s2">
				 	<td class="bluebox">					 	
				 	<table width="100%" border="0" cellpadding="0" cellspacing="1" style="border:0px;width:100%;empty-cells:show;">					       		
				       		<tr>
				       		<td class="bluebox" width="40%" style="white-space: nowrap;">
				       		<s:checkbox id="viewTypeCheckBox" name='viewTypeCheckBox' value="%{actionsValue}"></s:checkbox> 
						</td>					       			
				       		</tr>
			       		</table>						
				 </td> 
				 </s:iterator>
				
			</tr>						 
		        </s:iterator>				
		</table>
	</div>
</td>	
</tr>
</table>