<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" import="org.egov.infstr.utils.EgovMasterDataCaching,java.util.ArrayList"%>
<%
	String ipAddress= request.getRemoteAddr();
	String cityName = session.getAttribute("cityname").toString();
	String logoName = session.getAttribute("citylogo").toString();
%>
	<!--
        REMOTE IP ADDRESS USING getRemoteAddr :<%=request.getRemoteAddr()%>
        REMOTE IP ADDRESS USING getHeader("X-Forwarded-For") :<%=request.getHeader("X-Forwarded-For")%>
        FINAL IP ADDRESS GOING TO USE :  <%=ipAddress%>
    -->
	
<html>
	<head>
		<title><%=cityName%> Portal Login</title>		
		<link rel="stylesheet" type="text/css" href="common/css/login.css"/>		
	</head>
	<body onload="bodyonload()" onkeypress="checkCapsLock(event)">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<div class="topbar">
						<div class="logoLeft">
							<img src="../images/<%=logoName%>"  width="91" height="90" />
						</div>
						<div class="logoRight">
							<img src="../images/egov-logo.gif"  width="91" height="90" />
						</div>
						<div class="mainheading">
							<%=cityName%>							
						</div>
					</div>
					<div class="navibarportal">
					</div>
					<div class="formmainbox">
						<div class="insidecontent">
							<div class="rbroundbox2">
								<div class="rbtop2">
									<div></div>
								</div>
								<div class="rbcontent2">
									<form method="post" id="UserValidateForm" name="UserValidateForm" action="${pageContext.request.contextPath}/j_security_check" autocomplete="off">
										<input type="hidden" id="ipAddress" name="ipAddress" value="<%=ipAddress%>"/>
										<input type="hidden" id="loginType" name="loginType" />
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<% if("true".equals(request.getParameter("error"))) { %>
											<tr>
												<td colspan="3" align="center">
													<font color="red">Login failed.<br/> 
													<c:choose>
														<c:when test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message == 'Maximum sessions of {0} for this principal exceeded'}">
														You have already logged in another session. <br/>Please log off from the other session to log in from this machine.
														</c:when>
														<c:when test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message == 'User credentials have expired'}">
														Your Password has expired, Please click <a href="/egi/admin/directChgPassword.do" target="_blank" style="color:blue">here</a> to change your password.
														</c:when>
														<c:otherwise>
														User Name or Password is invalid or you are not allowed to login from this terminal.
														</c:otherwise>
													</c:choose>
													</font>
												</td>
											</tr>
											<%}%>
											<tr>
												<td colspan="3" align="center"><div class="authorized">For Authorized Users Only</div>	</td>
											</tr>
											<tr>
												<td width="26%">																									
												</td>
												<td width="40%">
													<div class="loginboxmain">
														<div class="loginheadingnew3">
															Login
														</div>
														<div class="loginboxnew1">
															<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
																<tr>
																	<td height="25px" colspan="2"/>
																</tr>
																<tr>
																	<td width="50%" align="right">
																		Name / User Name: &nbsp;&nbsp;
																	</td>
																	<td width="50%" align="left">
																		<input type="text" id="j_username" name="j_username" tabindex="1" class="textAlignLeft"	onblur="checkRole()" autocomplete="off"/>
																	</td>
																</tr>
																<tr>
																	<td height="5" colspan="2"></td>
																</tr>
																<tr>
																	<td width="50%" align="right">
																		Password:&nbsp;&nbsp;
																	</td>
																	<td width="50%" align="left">
																		<input type="password" id="j_password" 	name="j_password" tabindex="1" class="textAlignLeft" onkeydown="checkEnterKey(event)" autocomplete="off"/>
																	</td>
																</tr>
																<tr>
																	<td height="5" colspan="2"></td>
																</tr>
																<tr style="display: none" id="locationRow">
																	<td width="50%" align="right">
																		Location<span style="color:red">*</span>:&nbsp;&nbsp;
																	</td>
																	<td width="50%" align="left">
																		<input type="text" id="locationname" name="locationname" tabindex="1" class="fieldinput" readonly="true" /> 
																		<input type="hidden" name="locationId" id="locationId" />
																	</td>
																</tr>
																<tr>
																	<td height="5" colspan="2"></td>
																</tr>
																<tr style="display: none" id="counterRow">
																	<td width="50%" align="right">
																		Counter<span style="color:red">*</span>:&nbsp;&nbsp;
																	</td>
																	<td width="50%" align="left">
																		<select name="counterId" id="counterId" class="fieldinput" tabindex="1"/>
																	</td>
																</tr>
																<tr>
																	<td height="15" colspan="2"></td>
																</tr>
																<tr>
																	<td  align="center" colspan="2" >
																		<input type="button" class="buttongeneral" 	tabindex="1" value="Login" onclick="buttonpress();" />
																		&nbsp;
																		<input type="button" class="buttongeneral" tabindex="1" value="Close" onclick="closeWindow()" />
																	</td>
																</tr>
															</table>
														</div>
														<span id="tooltip" class="capslock">CAPS LOCK : ON</span>														
													</div>
												</td>
												<td width="19%">																								
												</td>
											</tr>
										</table>
									</form>
								</div>
								<div class="rbbot2">
									<div></div>
								</div>
							</div>
						</div>
					</div>
					<div class="buttonbottom"> City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a>&nbsp;&copy; All Rights Reserved
					</div>
				</td>
			</tr>
		</table>
		<script type="text/javascript" src="common/js/login.js"></script>
	</body>
</html>

