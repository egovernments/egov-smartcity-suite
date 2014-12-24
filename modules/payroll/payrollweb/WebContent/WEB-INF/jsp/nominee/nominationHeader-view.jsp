<!--
	Program Name : nominationHeader-view.jsp
	Author		: jagadeesan
	Created	on	: 09-02-2010
	Purpose 	: Nomination header master.
 -->
<%@ include file="/includes/taglibs.jsp" %>
<html>  
	<head>  
	    <title>View Nomination Header</title>
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
					
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="nominationHeaderTable">
									<span id="msg">
										<s:actionerror cssClass="mandatory"/>  
										<s:fielderror cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
									</span>
									<tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							               	<div class="headplacer"><s:text name="Nomination.Heading"/></div>
							            </td>
							        </tr>
									
									<tr>
										<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Nomination.Code"/> </td>
										<td class="greybox2wk"><input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.code'/>" /></td>
									</tr>
									
									<tr>
										<td class="whiteboxwk"><s:text name="Nomination.Description"/> </td>
										<td class="whitebox2wk"><input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.description'/>" /></td>
									</tr>
									
									<tr>
										<td class="greyboxwk"><s:text name="Nomination.PayHead"/> </td>
										<td class="greybox2wk"><input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.salaryCodes.head'/>" /></td>
									</tr>
									
									<tr>
										<td class="greyboxwk"><s:text name="Nomination.AccountCode"/> </td>
										<td class="greybox2wk"><input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.coa.glcode'/>" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:text name="Nomination.AccountName"/>&nbsp;&nbsp;<input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.coa.name'/>" /></td>
									</tr>
							
									<tr>
										<td class="whiteboxwk"><s:text name="Nomination.RuleScript"/> </td>
										<td class="whitebox2wk"><input type="text" readOnly="true" value="<s:property value='#attr.nominationHeader.ruleScript.description'/>" /></td>
									</tr>
									
									<tr>
						               	<td colspan="4" class="shadowwk"></td>
						            </tr>
									
					            
						           <tr>
								       <td align="center" colspan="2"></td>
						           </tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk">
					<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
				</div>
				<%@ include file='../common/payrollFooter.jsp'%>
			</center>
		</s:form>
	</body>
</html>