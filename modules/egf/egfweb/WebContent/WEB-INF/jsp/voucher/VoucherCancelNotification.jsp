<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<div align="center"  ><br>	
	<div  class="subheadnew" >Notification Details</div><br>	
	<div align="left">
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
				<tbody>
					<tr>
						<td  class="greybox">
							Notification Receiver
						</td>
						<td class="greybox">
							<Strong><s:property value="%{userName}"/></Strong>
						</td>
						<td class="greybox">
						</td>
						<td class="greybox">
						</td>
					</tr>
					<tr>
						<td class="greybox">
							Comments<span class="mandatory">*</span>
						</td>
						<td class="greybox">
							<s:textarea name="fileSummary" cols="150" rows="3" tabindex="1" id="fileSummary"></s:textarea>
						</td>
					</tr>					
				</tbody>
				</table>
	</div>	
</div>