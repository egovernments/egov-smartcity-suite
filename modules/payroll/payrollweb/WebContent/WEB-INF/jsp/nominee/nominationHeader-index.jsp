<!--
	Program Name : nominationHeader-index.jsp
	Author		: jagadeesan
	Created	on	: 09-02-2010
	Purpose 	: Nomination header master list.
 -->
<%@ include file="/includes/taglibs.jsp" %>		  
<html>  
	<head>  
		<title>Nomination Header List</title>  
	
	<script type="text/javascript">
		
		function setModeValue(val)
		{
			document.getElementById('mode').value=val;
		}
		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
		}
	
	</script>
	</head>  
	  
	<body> 
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
									
									<table width="95%" cellpadding="0" cellspacing="0" border="0">
								        
								        <span id="msg">
											<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
											<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
											<s:actionmessage cssClass="actionmessage"/>
										</span>
								        <tr>
								           <td colspan="2" class="headingwk">
								        		<div class="arrowiconwk">
								            		<img src="../common/image/arrow.gif" />
								        		</div>
								        		<div class="headplacer">Nomination Header</div>
								        	</td>
								       	</tr>	
									  	
									  	<tr>
											<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Nomination.PayHead"/> </td>
											<td class="greybox2wk"><s:select name="id" id="id" list="dropdownData.nominationHeaderList" listKey="id" listValue="code" headerKey="" headerValue="----Select----"  value="%{id.id}" /> </td>
										</tr>
										<tr>
											<td colspan="2">
												<input type="hidden" id="mode" name="mode" value=""/> 
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="buttonholderwk">
						<s:submit method="loadToViewOrEdit" value="View" onclick="setModeValue('View')" cssClass="buttonfinal"/>
						<s:submit method="loadToViewOrEdit" value="Edit" onclick="setModeValue('Edit')" cssClass="buttonfinal"/>
						<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
						<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
					</div>
					<%@ include file='../common/payrollFooter.jsp'%>
			</center>
		</s:form>
	</body>  
</html>