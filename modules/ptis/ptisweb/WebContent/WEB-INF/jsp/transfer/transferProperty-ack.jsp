<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><s:text name='transOwnAck' />
		</title>
		<script type="text/javascript">
  function viewProperty(){
  	window.location="../view/viewProperty!viewForm.action?propertyId=<s:property value='%{indexNumber}'/>";
  }
  </script>
	</head>
	<body onload=" refreshParentInbox(); ">
		<s:form name="transPropAckForm" theme="simple">
			<s:push value="model">
			<s:token/>
				<div class="formmainbox">
					<div class="formheading"></div>
					<div class="headingbg">
						<s:text name="transOwnAck" />
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="5"
								style="background-color: #FDF7F0; font-size: 15px;"
								align="center">
									<s:if test="%{nextUser != null }" >
                                 <span ><s:text name="transferdet.head"></s:text> 
									 </span>
									<s:property value="%{indexNumber}" />
									 <span > 
									<s:text name="forward.success"></s:text>  </span>
								    <s:property value="%{nextUser}" />
									</s:if>
									<s:else>
									 <span ><s:text name="transferOwner.ack"></s:text> </span>
									<a href="#"
									onclick='viewProperty();'> <s:property value="%{indexNumber}" /> </a>
									</s:else>
								
							</td>
						</tr>
					</table>
				</div>
				<div class="buttonbottom" align="center">
					<td>
						<input type="button" name="button2" id="button2" value="Close"
							class="button" onclick="window.close();" />
					</td>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
