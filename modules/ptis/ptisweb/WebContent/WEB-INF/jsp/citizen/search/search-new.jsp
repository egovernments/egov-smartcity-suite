<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<sx:head />
		<script type="text/javascript">
			function populateWard() {
				populatewardId( {
					zoneId : document.getElementById("zoneId").value
				});
			}
			
		</script>
		<title><s:text name="searchProp.title"></s:text></title>
	</head>
	<body>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
				</div>
			</s:if>
			<center>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<s:form action="search" name="indexform" theme="simple">
							<div class="formheading"></div>
							
							<tr>
								<td width="100%" colspan="4" class="headingbg">												
									<div class="headingbg">					
										<s:text name="search.index.num" />									
									</div>									
								</td>
							</tr>					
														
							<tr>
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">
									<s:text name="prop.Id" />
									<span class="mandatory">*</span> :
								</td>
								
								<td class="bluebox">
									<s:textfield name="indexNum" id="indexNum" value="%{indexNum}" maxlength="50"/>
								</td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
							
							<tr>
								<td class="bluebox" colspan="4">
									&nbsp; &nbsp; &nbsp;
								</td>
							</tr>
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox" colspan="2">
									<div class="greybox" style="text-align:center">
										<s:hidden id="mode" name="mode" value="index"></s:hidden>
										<s:submit name="search" value="Search" cssClass="buttonsubmit" method="srchByIndex"></s:submit>
									</div>
								</td>								
								<td class="greybox">&nbsp;</td>
							</tr>
						</s:form>
					</table>			

					
			<div align="left" class="mandatory" style="font-size: 11px">
			* <s:text name="mandtryFlds"></s:text>
			</div>
					
			</center>
		</div>
	</body>
</html>
