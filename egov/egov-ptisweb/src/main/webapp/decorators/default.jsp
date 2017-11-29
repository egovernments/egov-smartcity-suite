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

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<html oncontextmenu="return false;">
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>

		<link href="<cdn:url value='/resources/css/propertytax.css?${app_release_no}'/>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
		<link rel="stylesheet" type="text/css" href="<cdn:url value='/resources/yui2.8/fonts/fonts-min.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<cdn:url value='/resources/yui2.8/assets/skins/sam/datatable.css'/>"/>
		<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="<cdn:url value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>"/>

		<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"> </script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/yuiloader/yuiloader-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/element/element-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/connection/connection-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/datasource/datasource-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/datatable/datatable-min.js'/>"></script>
		
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/yahoo/yahoo-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/dom/dom-min.js'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/yui2.8/animation/animation-min.js'/>"></script>
		
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/propertyTax.js?${app_release_no}'/>"></script>
	    <script type="text/javascript" src="<cdn:url value='/resources/javascript/helper.js?${app_release_no}'/>"></script>
	    <script type="text/javascript" src="<cdn:url value='/resources/javascript/WorkFlow.js?${app_release_no}'/>"></script>
	    <script type="text/javascript" src="<cdn:url value='/resources/erp2/js/calender.js?${app_release_no}'/>"></script>
		
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/validations.js?${app_release_no}'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/SASvalidation.js?${app_release_no}'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/dateValidation.js?${app_release_no}'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/createProperty.js?${app_release_no}'/>"></script>
		<script type="text/javascript" src="<cdn:url value='/resources/javascript/json2.js?${app_release_no}'/>"></script>
		
	    <script type="text/javascript" src="<cdn:url value='/resources/javascript/jquery/ajax-script.js?${app_release_no}'/>"></script>
	    <script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	    <script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
    	
    	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="<cdn:url  value='/resources/global/js/ie8/html5shiv.min.js'/>"></script>
			<script src="<cdn:url  value='/resources/global/js/ie8/respond.min.js'/>"></script>
		<![endif]-->
    	
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

		function disableRefresh(e) {
            if ((e.which || e.keyCode) == 116)
                    e.preventDefault();
            if (e.ctrlKey)
                    if ((e.which || e.keyCode) == 82)
                            e.preventDefault();
        }
        jQuery(document).on("keydown", disableRefresh);

    	window.location.hash = "no-back-button";
        window.location.hash = "Again-No-back-button";//again because google chrome does not insert first hash into history
        window.onhashchange = function() {
            window.location.hash = "no-back-button";
        };
        function reCalculateTotalFooterWhenExport(tableIdWithOutPrefix)
        {
        	
        	jQuery("#"+tableIdWithOutPrefix+" tfoot td").each(function( index ) {
           		 if(index!==0)
           		 {
           			 var totals=jQuery(this).html().split("(");
        	    		 var str=""+totals[1];
        	    		 str=str.slice(0,-1);
        	    		 jQuery(this).html(str);
           		 }
           	 });
             setTimeout(function(){ jQuery('select[name="'+ tableIdWithOutPrefix +'_length"]').trigger('change'); }, 10);
        }
	  </script>
	  
	   
    </body>
</html>
