<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/includes/taglibs.jsp" %>
<script src="<egov:url path='resources/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
<script src="<cdn:url value='/resources/javascript/jquery/jquery-1.7.2.min.js' context='/ptis'/>"></script>
<meta charset="UTF-8">
<title>Court Case Search Application Result</title>

<script type="text/javascript">
	
	jQuery(document).ready(function(){
		function searchForResult(){
			alert("here");
			
		}
	});
	
	function searchForResult1(){
		alert("inside");
		var $formData = new FormData($('form[name=searchForm]')[0]);
		$.ajax({
            type		: 'POST',
            url			: '/ptis/report/searchResultSet',
            data		: $formData,
            dataType	: 'JSON',
            contentType	: false,
            processData	: false,
            success		: function(response){
                if(response.code == '200'){
                	
                }else{
                	
                }
            }
        });
	}

</script>
</head>
<body>
	<form id="searchForm" class="form-horizontal form-groups-bordered"  method="get">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong>Search Court Case Application</strong>
                    </div>
                </div>
                <div class="panel-body"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-right"> Application No.</label>
                    <div class="col-sm-3 add-margin">
                        <span class="twitter-typeahead" style="position: relative; display: inline-block; direction: ltr;"><input type="text" id="applicationNumber" class="form-control typeahead tt-input" placeholder="" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: regular; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><span class="tt-dropdown-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none; right: auto;"><div class="tt-dataset-0"></div></span></span>
                        <input id="applicationNumber" name="applicationNumber" type="hidden" value="">
                    </div>
                    <!-- <label class="col-sm-2 control-label text-right"> License Number</label>
                    <div class="col-sm-3 add-margin">
                        <span class="twitter-typeahead" style="position: relative; display: inline-block; direction: ltr;"><input type="text" id="licenseNumber" class="form-control typeahead tt-input" placeholder="" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: regular; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><span class="tt-dropdown-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none; right: auto;"><div class="tt-dataset-1"></div></span></span>
                        <input id="licenseNumber" name="licenseNumber" type="hidden" value="">
                    </div> -->
                      <label class="col-sm-2 control-label text-right"> Assessment No.</label>
                      <div class="col-sm-3 add-margin">
                          <span class="twitter-typeahead" style="position: relative; display: inline-block; direction: ltr;"><input type="text" id="propertyAssessmentNo" class="form-control typeahead tt-input" placeholder="" autocomplete="off" spellcheck="false" dir="auto" style="position: relative; vertical-align: top;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: regular; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: 0px; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><span class="tt-dropdown-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none; right: auto;"><div class="tt-dataset-5"></div></span></span>
                          <input id="propertyAssessmentNo" name="propertyAssessmentNo" type="hidden" value="">
                      </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label text-right"> Application Type</label>
                    <div class="col-sm-3 add-margin">
                        <select id="appType" name="applicationTypeId" class="form-control">
                            <option value="">
                                --- Select ---
                            </option>
                            <option value="1">Court Case</option>
                            <option value="2">Write Off</option>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label text-right"> Application Status</label>
                    <div class="col-sm-3 add-margin">
                        <select id="status" name="statusId" class="form-control">
                            <option value="">
                                --- Select ---
                            </option>
                            <option value="1">INPROGRESS</option>
                            <option value="2">CLOSED</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
					<label class="col-sm-3 control-label text-right"> From Date:<span class="mandatory1">*</span></label>
					<div class="col-sm-3 add-margin">	
						<div class="greybox"> 
							<input type="text" name="fromDate" value="" id="fromDate" 
							class="form-control datepicker" onkeyup="DateFormat(this,this.value,event,false,'3')" 
							data-inputmask="'mask': 'd/m/y'" placeholder="DD/MM/YYYY">
						</div>
					</div>
					
					<label class="col-sm-2 control-label text-right"> To Date:<span class="mandatory1">*</span></label>
					<div class="col-sm-3 add-margin">
						<div class="greybox"> 
							<input type="text" name="toDate" value="" id="toDate" class="form-control datepicker" 
							onkeyup="DateFormat(this,this.value,event,false,'3')" data-inputmask="'mask': 'd/m/y'" 
							placeholder="DD/MM/YYYY">
						</div>
					</div>
				</div>                
            </div>
            <div class="row">
                <div class="text-center">
                    <button type="submit" id="searchForResult" class="btn btn-primary" onclick="searchForResult1()">
                        Search
                    </button>
                    <button type="reset" class="btn btn-default">Reset</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                        Close
                    </button>
                </div>
            </div>
        </form>
</body>
</html>