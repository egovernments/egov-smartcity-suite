<!--
	Program Name : nominationHeader-edit.jsp
	Author		: jagadeesan
	Created	on	: 09-02-2010
	Purpose 	: Nomination header master.
 -->

<%@ include file="/includes/taglibs.jsp" %>
<html>  
	<head>  
	    <title>Edit Nomination Header</title>
	    <style type="text/css">
			#coaSearch_autocomplete { z-index:9001; }  
		</style>
	</head>
	
	<script language="javascript">
		
		function disableForm()
		{
			document.getElementById('code').readOnly =true;
			if(document.getElementById("salaryCodes").value !="-1")
			{
				document.getElementById('coaSearch').disabled = true;
			}
		}
		
	</script>
	
	<body onload="disableForm()">  
		<s:form action="nominationHeader" theme="simple" >  
			<center>
			
			<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
								    <span class="bold">Today:</span><egovtags:now/>
								</div>	
					
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="nominationHeaderTable">
									<span id="msg" style="height:1px">
										<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
										<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
									</span>
									
									<%@ include file='nominationHeader-form.jsp'%>

									<tr>
						               	<td colspan="4" class="shadowwk"></td>
						            </tr>
									
									<tr>
									 	<td colspan="2" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
						            
						           <tr>
								       <td align="center" colspan="2"> 
											<s:hidden  name="mode" value="Edit" />	
									   </td>
						           </tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk">
					<s:submit method="edit" value="Save" cssClass="buttonfinal"/>
					<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
					<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
				</div>
				<%@ include file='../common/payrollFooter.jsp'%>
			</center>
		</s:form>
	</body>
</html>