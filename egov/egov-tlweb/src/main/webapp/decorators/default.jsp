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
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>
        
        <link rel="icon" href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
        
     	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/fonts/fonts-min.css"/>
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css"/>	
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/assets/skins/sam/autocomplete.css" />	
		
		<script type="text/javascript" src="/egi/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js"></script> 
		<script type="text/javascript" src="/egi/commonyui/yui2.8/dragdrop/dragdrop-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/element/element-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/connection/connection-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/datasource/datasource-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/datatable/datatable-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/animation/animation-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/container/container_core-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/menu/menu-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/button/button-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/editor/editor-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/autocomplete/autocomplete-min.js" ></script>
		        
		<link href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
	    <script type="text/javascript" src="<c:url value='/commonjs/calender.js' context='/egi'/>" ></script> 
		<script type="text/javascript" src="<c:url value='/resources/javascript/validations.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/SASvalidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/dateValidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/license-common.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/resources/js/helper.js'/>"></script>
		
   		<script type="text/javascript" src="<c:url value='/resources/js/pt.js'/>"></script> 
   		
	    <script type="text/javascript" src="<c:url value='/resources/js/ajax-script.js'/>"></script>
	    <script type="text/javascript" src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	    <script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
	    <link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
        <script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
        <script src="<c:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
        
    	<decorator:head/>
    </head>
    
	<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	  <div class="page-container">
		    <!-- header -->
		    <egov:breadcrumb/>
		    
		    <!-- pagecontent -->
		    <div class="main-content">
		       <decorator:body/>
		    </div>
		    
		    <!-- footer -->
		    <footer class="main">
			    Powered by <a href="http://egovernments.org/" target="_blank">eGovernments Foundation</a>
			</footer>
	  </div>
	 
	 <!-- loading indicator --> 
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
