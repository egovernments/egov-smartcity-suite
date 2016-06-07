<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	    <meta name="description" content="eGov System" />
	    <meta name="author" content="eGovernments Foundation" />
	
	    <title><tiles:insertAttribute name="title"/></title>
		<link rel="icon" href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
		
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
	    
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="/egi/resources/global/js/ie8/html5shiv.min.js"></script>
			<script src="/egi/resources/global/js/ie8/respond.min.js"></script>
		<![endif]-->
	<script src="<c:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
	</head>
    <body class="page-body" oncontextmenu="return false;">
        <div class="page-container">
            <tiles:insertAttribute name="header"/>
                <div class="main-content">
                    <tiles:insertAttribute name="body"/>
                </div>
				<tiles:insertAttribute name="footer"/>
        </div>
        <div class="modal fade loader-class" data-backdrop="static">
			<div class="modal-dialog">
					<div class="modal-body">
						<div class="row spinner-margin text-center">
							<div class="col-md-12 ">
								<div class="spinner">
									<div class="rect1"></div>
									<div class="rect2"></div>
									<div class="rect3"></div>
									<div class="rect4"></div>
									<div class="rect5"></div>
								</div>
							</div>
							
							<div class="col-md-12 spinner-text">
								Processing your request. Please wait..
							</div>
						</div>
					</div>
			</div>
		</div>
		<script>

	    // jQuery plugin to prevent double submission of forms
		jQuery.fn.preventDoubleSubmission = function() {
		jQuery(this).on('submit',function(e){
		    var $form = jQuery(this);
		    if ($form.data('submitted') === true) {
		      // Previously submitted - don't submit again
		      e.preventDefault();
		    } else {
		      // Mark it so that the next submit can be ignored
		      $form.data('submitted', true);
		    }
		  });
		  // Keep chainability
		  return this;
		};

		jQuery("form").submit(function( event ) {
			jQuery('.loader-class').modal('show', {backdrop: 'static'});
		});
		
		jQuery('form').preventDoubleSubmission();

		try { 
			jQuery(".datepicker").datepicker({
				format: "dd/mm/yyyy",
				autoclose: true 
			}); 

			var d = new Date();
			var currDate = d.getDate();
			var currMonth = d.getMonth();
			var currYear = d.getFullYear();
			var startDate = new Date(currYear,currMonth,currDate);
			jQuery('.today').datepicker('setDate',startDate);

			}catch(e){
			console.warn("No Date Picker");
		}

		/*Restrict back button*/
		history.pushState({ page: 1 }, "Title 1", "#no-back");
		window.onhashchange = function (event) {
		  window.location.hash = "no-back";
		};

		/*Restrict page refresh*/
		window.document.onkeydown = function(event) { 
		   	 switch (event.keyCode) { 
		        case 116 : //F5 button
		            event.returnValue = false;
		            event.keyCode = 0;
		            return false; 
		        case 82 : //R button
		            if (event.ctrlKey) { //Ctrl button
		                event.returnValue = false; 
		                event.keyCode = 0;  
		                return false; 
		            } 
		    }
		}
	  </script>
    </body>
</html>
