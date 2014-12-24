<!--
	Program Name : cpfTrigger-new.jsp
	Author		: Jagadeesan M
	Created	on	: 09-10-2009
	Purpose 	: To trigger/ batch process of Contribution of Provident Fund
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ page import="java.util.*"%>

<html>  
	<head>  
	    <title>CPF Batch Process</title>
	</head>
	
	<body onload="disable()" >  
		<s:form action="cpfTrigger" theme="simple" onsubmit="enable()">  
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
								    <span class="bold">Today:</span> <egov:now/>
								</div>	
					
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="cpfheadertable">
									
									<s:if test="%{pfHeader.tds==null && pfHeader.ruleScript==null}">
									    <s:fielderror cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
										<jsp:include page="cpfTrigger-form.jsp"/>
									</s:if>
									<s:else>
										<span id="msg">
											<s:actionerror cssClass="mandatory"/>  
											<s:fielderror cssClass="mandatory"/>
											<s:actionmessage cssClass="actionmessage"/>
										</span>
										<jsp:include page="cpfTrigger-form.jsp"/>
										
										<tr>
							               	<td colspan="4" class="shadowwk"></td>
							            </tr>

							           <tr>
									       <td align="center" colspan="2"> 
												
										   </td>
							           </tr>
								       <tr>
										   <td><font color="blue">Note : Make sure that payslip generated for <br>all employees for selected month and year.</font></td>
										   <td colspan="2" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									   </tr>
							        </s:else>
							          
								</table>
							</div>
						</div>
					</div>
				</div>
				<s:if test="%{pfHeader.tds!=null && pfHeader.ruleScript!=null}">
					<div class="buttonholderwk">
						<s:submit method="create" value="Trigger" cssClass="buttonfinal"/>
						<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
					</div>
				</s:if>
				<%@ include file='../common/payrollFooter.jsp'%>
			</center>
		</s:form>
		
		<script language="javascript">
			function disable()
			{
				//To disable the fields while onload of form
				document.forms[0].tds.disabled=true;
				document.forms[0].ruleScript.disabled=true;
				document.forms[0].pfIntExpAccountGlcode.disabled=true;
				document.forms[0].pfIntExpAccountName.disabled=true;
			}
			function enable()
			{
				//To enable the fields while submitting the form
				document.forms[0].tds.disabled=false;
				document.forms[0].ruleScript.disabled=false;
				document.forms[0].pfIntExpAccountGlcode.disabled=false;
				document.forms[0].pfIntExpAccountName.disabled=false;
			}
		</script>
	</body>
</html>