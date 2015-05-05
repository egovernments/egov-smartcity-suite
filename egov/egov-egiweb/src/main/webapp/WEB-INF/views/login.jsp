<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="description" content="eGov Urban Portal" />
<meta name="author" content="eGovernments Foundation" />

<title>eGov Urban Portal Login</title>

<link rel="icon" href="/egi/resources/global/images/chennai_fav.ico"
	sizes="32x32">
<link rel="stylesheet"
	href="/egi/resources/global/css/bootstrap/bootstrap-datepicker.css">
<link rel="stylesheet"
	href="/egi/resources/global/css/bootstrap/bootstrap.css">
<link rel="stylesheet"
	href="/egi/resources/global/css/font-icons/entypo/css/entypo.css">
<link rel="stylesheet" href="/egi/resources/global/css/egov/custom.css">
<link rel="stylesheet"
	href="/egi/resources/global/css/egov/header-custom.css">

<script src="/egi/resources/global/js/jquery/jquery.js"></script>
</head>
<body class="page-body">
	<div class="page-container">
		<header class="navbar navbar-fixed-top">
			<!-- set fixed position by adding class "navbar-fixed-top" -->
			<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
				<div class="container-fluid">
					<div class="navbar-header col-md-10 col-xs-10">
						<a class="navbar-brand" href="javascript:void(0);"> <img
							src="/egi/resources/global/images/${sessionScope.citylogo}"
							height="60">
							<div>
								<span class="title2">${sessionScope.cityname}</span>
							</div>
						</a>
					</div>

					<div class="nav-right-menu col-md-2 col-xs-2">
						<ul class="hr-menu text-right">
							<li class="ico-menu"><a href="http://www.egovernments.org"
								target="_blank"> <img
									src="/egi/resources/global/images/logo@2x.png"
									title="Powered by eGovernments" height="20px">
							</a></li>
						</ul>
					</div>
				</div>
			</nav>
		</header>

		<div class="main-content">
			<div class="login-container ">
				<c:if test="${param.error}">
					<div class="text-center  error-msg">
						<c:choose>
							<c:when
								test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message == 'Maximum sessions of {0} for this principal exceeded'}">
							You have already logged in another session. <br />Please log off from the other session to log in from this machine.
						</c:when>
							<c:when
								test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message == 'User credentials have expired'}">
							Your password has been expired, Please click 
							<a href="#" target="_blank" style="color: blue">here</a> to change your password.
						</c:when>
							<c:otherwise>
							User Name or Password is invalid or you are not allowed to login from this terminal.
						</c:otherwise>
						</c:choose>

					</div>
				</c:if>
				<c:if test="${param.citizenActivationSuccess}">
					<div class="text-center  error-msg">Activation Successful,
						Please login using your credentials.</div>
				</c:if>
				<c:if test="${param.citizenActivationFailed}">
					<div class="text-center  error-msg">
						Activation Failed, you may entered wrong activation code or your
						registration got expired. <input type="hidden"
							name="citizenActivationFailed" id="citizenActivationFailed"
							value="true" />
					</div>
				</c:if>
				<c:if test="${param.activationCodeSendingFailed}">
					<div class="text-center  error-msg">
						Neither email nor mobile activation send. <input type="hidden"
							name="activationCodeSendingFailed"
							id="activationCodeSendingFailed" value="true" />
					</div>
				</c:if>
				<c:if test="${param.mobInvalid}">
					<input type="hidden" name="mobInvalid" id="mobInvalid" value="true" />
				</c:if>
				<c:if test="${param.emailInvalid}">
					<input type="hidden" name="emailInvalid" id="emailInvalid"
						value="true" />
				</c:if>
				<c:if test="${param.citizenActivation}">
					<input type="hidden" name="citizenActivation"
						id="citizenActivation" value="true" />
				</c:if>
				<c:if test="${param.passwordSendingSuccess}">
					<div class="text-center  error-msg">Your recovered password
						has been sent to registered mobile and email.</div>
				</c:if>
				<c:if test="${param.passwordSendingFailed}">
					<div class="text-center  error-msg">Your password recovery
						request failed.</div>
				</c:if>

				<div class="login-content login-content-margin signup-section">

					<div class="login-header">
						<a href="securityLogin.jsp" class="logo"> <img
							src="/egi/resources/global/images/egov_logo_tr_h.png" alt=""
							height="37" />
						</a>
					</div>
					<div class="login-toggle">
						<div class="row">
							<div class="col-md-12 col-xs-12">
								<div
									class="col-md-6 col-xs-6 sign-in sign sign-active arrow_box_left"
									data-sign="in">Sign In</div>
								<div class="col-md-6 col-xs-6 sign-up sign sign-notactive"
									data-sign="up">Sign Up</div>
							</div>
						</div>
					</div>
					<div class="login-body">
						<form method="post" role="form" id="signupform"
							modelAttribute="citizen" class="display-hide">

							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon style-label">
										<i class="entypo-mobile theme-color style-color"></i>
									</div>

									<input type="text"
										class="form-control style-form is_valid_number"
										name="mobileNumber" maxlength="10" id="mobileNumber"
										placeholder="Mobile number" autocomplete="off"
										required="required" />
									<div id="mobnumberValid" style="display: none">
										<div class="text-right add-margin error-msg">Mobile
											Number should be 10 characters</div>
									</div>
									<div id="mobnumValid" style="display: none">
										<div class="text-right add-margin error-msg">Mobile
											Number already registered, Please try another Mobile Number</div>
									</div>
									<span class="mandatory set-mandatory"></span>
								</div>
							</div>

							<div class="form-group">
								<div class="row">
									<div class="col-md-6">
										<div class="input-group">
											<div class="input-group-addon style-label">
												<i class="entypo-key theme-color style-color"></i>
											</div>

											<input type="password"
												class="form-control style-form check-password"
												name="password" id="password" placeholder="Password"
												autocomplete="off" required="required" /> <span
												class="mandatory set-mandatory"></span>
											<div id="passwordValid" style="display: none">
												<div class="text-right add-margin error-msg">Password
													should be more than 8 characters</div>
											</div>
										</div>
									</div>
									<div class="col-md-6 margin-sm-top">
										<div class="input-group">
											<div class="input-group-addon style-label">
												<i class="entypo-key theme-color style-color"></i>
											</div>

											<input type="password"
												class="form-control style-form check-password"
												name="con-password" id="con-password"
												placeholder="Confirm password" autocomplete="off"
												required="required" /> <span
												class="mandatory set-mandatory"></span>
										</div>
									</div>

								</div>
								<div
									class="text-right add-margin error-msg display-hide password-error">These
									passwords don't match. Try again?</div>
							</div>

							<div class="add-margin overflow-section"></div>

							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon style-label">
										<i class="entypo-user theme-color style-color"></i>
									</div>

									<input type="text" class="form-control style-form" name="name"
										id="name" placeholder="Full name" autocomplete="off"
										required="required" /> <span class="mandatory set-mandatory"></span>
								</div>

							</div>

							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon style-label">
										<i class="entypo-mail theme-color style-color"></i>
									</div>

									<input type="text" class="form-control style-form"
										name="emailId" id="emailId" placeholder="Email"
										autocomplete="off" />

									<div id="emailValid" style="display: none">
										<div class="text-right add-margin error-msg">Not a
											well-formed email address</div>
									</div>
									<div id="emailidValid" style="display: none">
										<div class="text-right add-margin error-msg">Email
											already registered, Please try another Email</div>
									</div>
								</div>
							</div>

							<div class="form-group text-left">

								<div class="checkbox">
									<label> <input type="checkbox" required="required">Accept
										the <span><a href="javascript:void(0);"
											data-toggle="modal" data-target="#myModal"
											data-backdrop="static">Terms Of Use</a></span>
									</label> <span class="mandatory set-mandatory"></span>
								</div>

							</div>

							<div class="form-group">
								<button type="submit" id="signup-submit"
									class="btn btn-primary btn-block btn-login">
									<i class="entypo-login"></i> Sign Up
								</button>
							</div>

						</form>
						<form method="post" role="form" id="signinform"
							action="${pageContext.request.contextPath}/j_security_check"
							autocomplete="off">
							<input type="hidden" id="ipAddress" name="ipAddress"
								value="${pageContext.request.remoteAddr}" /> <input
								type="hidden" id="loginType" name="loginType" />
							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon style-label">
										<i class="entypo-user theme-color style-color"></i>
									</div>

									<input type="text" class="form-control style-form"
										name="j_username" id="j_username"
										placeholder="Username or Mobile number" autocomplete="off"
										required="required" /> <span class="mandatory set-mandatory"></span>
								</div>

							</div>
							<div class="form-group">

								<div class="input-group">
									<div class="input-group-addon style-label">
										<i class="entypo-key theme-color style-color"></i>
									</div>

									<input type="password" class="form-control style-form"
										name="j_password" id="j_password" placeholder="Password"
										autocomplete="off" required="required" /> <span
										class="mandatory set-mandatory"></span>
								</div>

							</div>
							<div class="form-group">
								<button type="submit"
									class="btn btn-primary btn-block btn-login">
									<i class="entypo-login"></i> Sign In
								</button>
							</div>

							<!--div class="form-group">
									<div class="row">
										<div class="col-md-12 col-xs-12 text-right">
											<a href="javascript:void(0)" data-toggle="modal"
												data-target="#fpassword" data-backdrop="static">Forgot
												Password?</a>
										</div>
									</div>
								</div-->
						</form>
					</div>
				</div>

				<div
					class="login-content login-content-margin otp-section display-hide">
					<div class="login-header">
						<a href="securityLogin.jsp" class="logo"> <img
							src="/egi/resources/global/images/logo@2x.png" alt="" />
						</a>
						<h4 class="header-description">OTP Activation</h4>
					</div>
					<div class="login-body">
						<form method="post" role="form" id="citizenactivationform"
							modelAttribute="citizen">
							<div class="form-group text-left">Registration Successful.
								Enter your 5 digit activation code sent to your registered email
								or mobile to activate your account here.</div>
							<div class="form-group">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="entypo-key theme-color"></i>
									</div>

									<input type="password" class="form-control "
										name="activationCode" id="activationCode"
										placeholder="Activation Code" autocomplete="off"
										required="required" /> <input id="citizenId" type="hidden"
										name="citizenId" value="${param.citizenId}" />
								</div>
							</div>
							<div class="form-group text-right">
								<button type="submit" id="activation-submit"
									class="btn btn-primary btn-login">Activate</button>
							</div>
						</form>
						<div class="form-group text-left">Registration will get
							automatically deleted if you do not activate account within next
							48 hours.</div>
					</div>
				</div>
			</div>
		</div>
		<footer class="main">
			Powered by <a href="http://eGovernments.org" target="_blank">eGovernments
				Foundation</a>
		</footer>
	</div>

	<div class="modal fade" id="fpassword" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Forgot Password</h4>
				</div>
				<div class="modal-body">
					<form method="post" role="form" id="forgotPasswordForm">

						<div class="form-group">

							<div class="input-group">
								<div class="input-group-addon style-label">
									<i class="entypo-user style-color"></i>
								</div>

								<input type="text" class="form-control style-form"
									name="emailOrMobileNum" id="emailOrMobileNum"
									required="required" placeholder="Email or Mobile number"
									autocomplete="off" />
							</div>
							<div id="emailOrMobileNoReq" class="error-msg"
								style="display: none">Email or Mobile number is required</div>

						</div>
						<div class="form-group text-right">
							<button type="submit" id="recovrbtn" class="btn btn-primary">Recover</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Terms & Conditions</h4>
				</div>
				<div class="modal-body">
					<p>This website is designed, developed and maintained by
						eGovernments Foundation under the supervision of
						${sessionScope.cityname}, India.</p>

					<p>Though all efforts have been made to ensure the accuracy and
						currency of the content on this website, the same should not be
						construed as a statement of law or used for any legal purposes.
						Incase of any ambiguity or doubts, users are advised to
						verify/check with the Department(s) and/or other source(s), and to
						obtain appropriate professional advice.</p>

					<p>Under no circumstances will this Department be liable for
						any expense, loss or damage including, without limitation,
						indirect or consequential loss or damage, or any expense, loss or
						damage whatsoever arising from use, or loss of use, of data,
						arising out of or in connection with the use of this website.
						These terms and conditions shall be governed by and construed in
						accordance with the Indian Laws. Any dispute arising under these
						terms and conditions shall be subject to the jurisdiction of the
						courts of India.</p>

					<p>The information posted on this website could include
						hypertext links or pointers to information created and maintained
						by non-Government/private organisations. ${sessionScope.cityname}
						is providing these links and pointers solely for your information
						and convenience. When you select a link to an outside website, you
						are leaving the ${sessionScope.cityname} website and are subject
						to the privacy and security policies of the owners/sponsors of the
						outside website. ${sessionScope.cityname}, does not guarantee the
						availability of such linked pages at all times.</p>

					<p>${sessionScope.cityname},cannot authorise the use of
						copyrighted materials contained in linked websites. Users are
						advised to request such authorisation from the owner of the linked
						website. ${sessionScope.cityname}, does not guarantee that linked
						websites comply with Indian Government Web Guidelines.</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<script src="/egi/resources/global/js/bootstrap/bootstrap.js"></script>
	<script
		src="/egi/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js"></script>
	<script
		src="/egi/resources/global/js/bootstrap/bootstrap-datepicker.js"></script>
	<script src="/egi/resources/global/js/egov/custom.js"></script>
	<script src="/egi/resources/js/app/login.js"></script>
</body>
</html>
