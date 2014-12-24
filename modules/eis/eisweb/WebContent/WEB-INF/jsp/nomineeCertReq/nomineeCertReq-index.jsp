<!--
	Program Name : nomineeCertReq-index.jsp
	Author		: jagadeesan
	Created	on	: 18-03-2010
	Purpose 	: Nomination certification required list.
 -->
<%@ include file="/includes/taglibs.jsp" %>		
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>  

<html>  
	<head>  
		<title>Nominee Certificaton Required List</title>  
	
	<script type="text/javascript">
		
		function setModeValue(val)
		{
			document.getElementById('mode').value=val;
		}
		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
			//document.getElementById('relationType').value="-1";
		}
	
	</script>
	</head>  
	  
	<body> 
		<s:form action="nomineeCertReq" theme="simple" > 
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
								        		<div class="headplacer"><s:text name="NomineeCertReq.Heading"/></div>
								        	</td>
								       	</tr>	
									  	
									  	<tr>
											<td class="greyboxwk"><span class="mandatory">*</span><s:text name="NomineeCertReq.RelationType"/></td>
											<td class="greybox2wk"><s:select name="relationType" id="relationType" list="dropdownData.eisRelationTypeList" listKey="id" 
												listValue="nomineeType" headerKey="-1" headerValue="----Select----"  value="%{relationType.id}"/>
											</td>
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
			</center>
		</s:form>
	</body>  
</html>