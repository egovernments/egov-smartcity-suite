<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <spring:eval expression="@environment.getProperty('analytics.enabled')" scope="application" var="analyticsEnabled"/>
    <c:if test="${analyticsEnabled}">
        <spring:eval expression="@environment.getProperty('analytics.config')" scope="application"/>
    </c:if>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="eGov Urban Portal"/>
    <meta name="author" content="eGovernments Foundation"/>
    <spring:eval expression="@environment.getProperty('user.pwd.strength')" var="pwdstrengthmsg"/>
    <spring:message code="usr.pwd.strength.msg.${pwdstrengthmsg}" var="pwdmsg"/>
    <title>Citizen Signup</title>
    <link rel="icon" href="<cdn:url value='/resources/global/images/favicon.png' context='/egi'/>" sizes="32x32">
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
    <link rel="stylesheet"
          href="<cdn:url value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>">
    <script src="<cdn:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>" type="text/javascript"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="<cdn:url value='/resources/global/js/ie8/html5shiv.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/ie8/respond.min.js' context='/egi'/>"></script>
    <![endif]-->
</head>
<body class="page-body">
<div class="page-container">
    <header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->
        <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header col-md-10 col-xs-10">
                    <a class="navbar-brand" href="javascript:void(0);">
                        <img src="<c:url value='/downloadfile/logo' context='/egi'/>" height="60">
                        <div>
                            <span class="title2">${sessionScope.citymunicipalityname}</span>
                        </div>
                    </a>
                </div>
                <div class="nav-right-menu col-md-2 col-xs-2">
                    <ul class="hr-menu text-right">
                        <li class="ico-menu">
                            <a href="http://www.egovernments.org" data-strwindname="egovsite" class="open-popup">
                                <img src="<cdn:url value='/resources/global/images/egov_logo_tr_h.png' context='/egi'/>" title="Powered by eGovernments" height="37" alt="">
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>
    <div class="main-content">
        <div class="login-content login-content-margin signup-section">
            <c:if test="${not empty message}">
                <div class="alert alert-success" role="alert">
                    <spring:message code="${message}"/> <br/>
                </div>
                <script>
                    setTimeout(function () {
                        if (window.opener && window.opener.document.getElementById("j_username")) {
                            window.opener.document.getElementById("j_username").value = '${mobileNo}';
                        }
                        window.close();
                    }, 5000)

                </script>
            </c:if>
            <div class="login-body">
                <form:form method="post" role="form" id="signupform" modelAttribute="citizen" autocomplete="off">
                    <div class="form-group text-left">
                        <div class="signin-title">
                            <spring:message code="title.signup"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-mobile fa-fw theme-color style-color"></i>
                            </div>
                            <form:input path="mobileNumber" cssClass="form-control style-form is_valid_number" id="mobileNumber" placeholder="Mobile number" title="Enter valid mobile number!" minlength="10" maxlength="10" autocomplete="off" required="required"/>
                            <span class="mandatory set-mandatory"></span>
                            <form:hidden path="username" id="username"/>
                            <div class="text-right error-msg display-hide mobile-error" style="margin:0;">
                                <spring:message code="msg.mobileno.missing"/>
                            </div>
                            <div class="userormobileerror text-right" style="margin:0;">
                                <form:errors path="username" cssClass="error-msg"/>
                            </div>
                        </div>
                    </div>
                    <input style="display:none" type="password">
                    <div class="form-group overflow" id="wrap">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-key fa-fw theme-color style-color"></i>
                            </div>
                            <form:password path="password" cssClass="form-control style-form check-password" id="password" placeholder="Password" maxlength="32" autocomplete="new-password" required="required" data-container="#wrap" data-toggle="popover" data-content='${pwdmsg}'/>
                            <span class="mandatory set-mandatory"></span>
                            <div class="input-group-addon" style="background:#fff;border:none;border-bottom:1px solid #D0D2D7;cursor:default;">
                                <i class="fa fa-eye show password-view" data-view="show" aria-hidden="true"></i>
                            </div>
                        </div>
                        <label id="password-error" class="error align-top pull-right display-hide" for="password">Required</label>
                        <label class="text-right align-top add-margin error-msg display-hide password-invalid" style="margin:0;">
                                ${pwdmsg}
                        </label>
                        <div class="text-right" style="margin:0;"><form:errors path="password" cssClass="error-check add-margin error-msg font-12"/></div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-key fa-fw theme-color style-color"></i>
                            </div>
                            <input type="password" class="form-control style-form check-password" name="con-password" id="con-password" placeholder="Confirm password"
                                   autocomplete="new-password" required="required" maxlength="32"/>
                            <span class="mandatory set-mandatory"></span>
                            <label id="con-password-error" class="error pull-right display-hide" for="con-password">Required</label>
                            <div class="text-right add-margin error-msg display-hide password-error" style="margin:0;">
                                <spring:message code="error.pwd.mismatch"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-user fa-fw theme-color style-color"></i>
                            </div>
                            <form:input path="name" cssClass="form-control style-form patternvalidation" data-pattern="alphabetwithspace" id="name" placeholder="Full name" minlength="2" maxlength="100" autocomplete="off" required="required"/>
                            <span class="mandatory set-mandatory"></span>
                            <label id="name-error" class="error pull-right display-hide" for="name">Required</label>
                            <div class="text-right" style="margin:0;"><form:errors path="name" cssClass="add-margin error-msg font-12"/></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-envelope fa-fw theme-color style-color"></i>
                            </div>
                            <form:input path="emailId" cssClass="form-control style-form" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" title="Enter vaild Email ID!" id="emailId" placeholder="Email (Optional)" minlength="5" maxlength="128" autocomplete="off"/>
                            <form:errors path="emailId" cssClass="add-margin error-msg font-12"/>
                        </div>
                    </div>
                    <div class="form-group overflow" id="otp-section">
                        <div class="input-group">
                            <div class="input-group-addon style-label">
                                <i class="fa fa-key fa-fw theme-color style-color"></i>
                            </div>
                            <form:password path="activationCode" id="activationcode" cssClass="form-control style-form" placeholder="Enter OTP" minlength="5" maxlength="5" autocomplete="off" required="required"/>
                            <span class="mandatory set-mandatory"></span>
                            <div class="input-group-addon" style="background:#fff;border:none;border-bottom:1px solid #D0D2D7;cursor:default;">
                                <i class="fa fa-eye show otp-view" data-view="show" aria-hidden="true"></i>
                            </div>
                        </div>
                        <label id="activationcode-error" class="error align-top pull-right display-hide" for="activationcode">Required</label>
                    </div>
                    <div class="form-group signup-leftpadding text-center" id="otpbtn-section">
                        <form:errors path="activationCode" cssClass="add-margin error-msg"/>
                        <button type="button" class="btn btn-primary btn-block" id="otpbtn">
                            <i class="fa fa-key" aria-hidden="true"></i> <spring:message code="lbl.generate.otp"/>
                        </button>
                    </div>
                    <div class="form-group signup-leftpadding" id="signup-section">
                        <button type="submit" class="btn btn-custom btn-block btn-login signup-submit" id="signupbtn">
                            <i class="fa fa-sign-out"></i> <spring:message code="btn.signup"/>
                        </button>
                    </div>
                    <div class="form-group text-left" style="font-size:12px;color:#777;margin-top:10px;">
                        <spring:message code="lbl.signup.termsofuse1"/> <span>
								<a href="javascript:void(0);" data-toggle="modal" data-target="#myModal" data-backdrop="static">
								<spring:message code="lbl.signup.termsofuse2"/></a> &
								<a href="javascript:void(0);" data-toggle="modal" data-target="#myModal2" data-backdrop="static">
								<spring:message code="lbl.signup.termsofuse3"/></a></span>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    <footer class="main">
        Powered by <a href="http://eGovernments.org" target="_blank">eGovernments Foundation</a>
    </footer>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Terms of use</h4>
            </div>
            <div class="modal-body font-12">
                <p>This website is designed, developed and maintained by
                    eGovernments Foundation under the supervision of
                    ${sessionScope.citymunicipalityname}, India.</p>

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
                    by non-Government/private organisations. ${sessionScope.citymunicipalityname}
                    is providing these links and pointers solely for your information
                    and convenience. When you select a link to an outside website, you
                    are leaving the ${sessionScope.citymunicipalityname} website and are subject
                    to the privacy and security policies of the owners/sponsors of the
                    outside website. ${sessionScope.citymunicipalityname}, does not guarantee the
                    availability of such linked pages at all times.</p>

                <p>${sessionScope.citymunicipalityname},cannot authorise the use of
                    copyrighted materials contained in linked websites. Users are
                    advised to request such authorisation from the owner of the linked
                    website. ${sessionScope.citymunicipalityname}, does not guarantee that linked
                    websites comply with Indian Government Web Guidelines.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Privacy & Security Policies</h4>
            </div>
            <div class="modal-body font-12">
                <h5>Privacy Policy</h5>
                <p>
                    ${sessionScope.citymunicipalityname} Portal does not automatically capture any specific personal information from you
                    (like name, phone number or e-mail address), that allows us to identify you individually.
                    If you choose to provide us with your personal information, like names or addresses, when
                    you visit our website, we use it only to fulfill your request for information. To use this website
                    requires user registration. Information so collected is used to facilitate interaction.</p>
                <p>
                    We do not sell or share any personally identifiable information volunteered on this site to
                    any third party (public/private). Any information provided to this website will be protected
                    from loss, misuse, unauthorized access or disclosure, alteration, or destruction.</p>
                <p>
                    We gather certain information about the User, such as Internet protocol (IP) address,
                    domain name, browser type, operating system, the date and time of the visit and the pages
                    visited. We make no attempt to link these addresses with the identity of individuals visiting
                    our site unless an attempt to damage the site has been detected.</p>
                <p>
                <h6>Use of Cookies:</h6>
                <i>
                    A cookie is a piece of software code that an internet web site sends to your browser
                    when you access information at that site. A cookie is stored as a simple text file on
                    your computer or mobile device by a website’s server and only that server will be able
                    to retrieve or read the contents of that cookie. Cookies let you navigate between pages
                    efficiently as they store your preferences, and generally improve your experience of a
                    website.</i></p>
                <p>
                    We are using following types of cookies in our site:
                <ul>
                    <li>Non-persistent cookies a.k.a per-session cookies. Per-session cookies serve
                        technical purposes, like providing seamless navigation through this website.
                        These cookies do not collect personal information on users and they are
                        deleted as soon as you leave our website. The cookies do not permanently
                        record data and they are not stored on your computer’s hard drive. The
                        cookies are stored in memory and are only available during an active
                        browser session. Again, once you close your browser, the cookie disappears.
                    </li>
                </ul>
                You may note additionally that when you visit sections of ${sessionScope.citymunicipalityname} Portal
                where you are prompted to log in, or which are customizable, you may
                be required to accept cookies. If you choose to have your browser refuse cookies, it is
                possible that some sections of our web site may not function properly.
                </p>
                <br/>
                <h5>Security Policy</h5>
                <p>
                <ul>
                    <li>${sessionScope.citymunicipalityname} Portal, has been placed in protected zones with implementation of firewalls
                        and IDS (Intrusion Detection System) and high availability solutions.
                    </li>
                    <li>${sessionScope.citymunicipalityname} Portal, simulated penetration tests have been conducted.
                        Penetration testing has also been conducted 1 time after the launch of the ${sessionScope.citymunicipalityname} Portal.
                    </li>
                    <li>${sessionScope.citymunicipalityname} Portal has been audited for known application level vulnerabilities before
                        the launch and all the known vulnerability has been addressed.
                    </li>
                    <li>Hardening of servers has been done as per the guideline of Cyber Security division
                        before the launch of the${sessionScope.citymunicipalityname} Portal.
                    </li>
                    <li>Access to web servers hosting the ${sessionScope.citymunicipalityname} is restricted both physically and through the
                        network as far as possible.
                    </li>
                    <li>Logs at 2 different locations are maintained for authorized physical
                        access of ${sessionScope.citymunicipalityname} servers.
                    </li>
                    <li>Web-servers hosting the ${sessionScope.citymunicipalityname} are configured behind IDS, IPS (Intrusion Prevention
                        System) and with system firewalls on them.
                    </li>
                    <li>All the development work is done on separate development environment and is
                        well tested on staging server before updating it on the production server.
                    </li>
                    <li>
                        After testing properly on the staging server the applications are uploaded to the
                        production server using SSH and VPN through a single point.
                    </li>
                    <li>
                        The content contributed by/from remote locations is duly authenticated & is not
                        published on the production server directly. Any content contributed has to go
                        through the moderation process before final publishing to the production server.
                    </li>
                    <li>All contents of the web pages are checked for intentional or unintentional malicious
                        content before final upload to web server pages.
                    </li>
                    <li>Audit and Log of all activities involving the operating system, access to the system,
                        and access to applications are maintained and archived. All rejected accesses and
                        services are logged and listed in exception reports for further scrutiny.
                    </li>
                    <li>Help Desk staff at the ${sessionScope.citymunicipalityname} monitor the ${sessionScope.citymunicipalityname} Portal at intervals of
                        week to check the web pages to confirm that the web pages are up and
                        running, that no unauthorized changes have been made, and that no unauthorized
                        links have been established.
                    </li>
                    <li>All newly released system software patches; bug fixes and upgrades are expediently
                        and regularly reviewed and installed on the web server.
                    </li>
                    <li>On Production web servers, Internet browsing, mail and any other desktop
                        applications are disabled. Only server administration related task is performed.
                    </li>
                    <li>Server passwords are changed at the interval of 1 months and are shared
                        by responsible persons.
                    </li>
                    <li>Responsible persons have been designated as Administrator for the ${sessionScope.citymunicipalityname} Portal and
                        shall be responsible for implementing this policy for each of the web servers. The
                        administrator shall also coordinate with the Audit Team for required auditing of
                        the server(s).
                    </li>
                    <li>${sessionScope.citymunicipalityname} Portal has been re-audited for the application level vulnerability after major
                        modification in application development [Not applicable at first launch].
                    </li>
                </ul>
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/js/signup.js?rnd=${app_release_no}'/>"></script>
</body>
</html>						