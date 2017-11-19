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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/includes/taglibs.jsp" %> 
<!DOCTYPE html>
<html>
<head>

<title>Upload Photos</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">  
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">   
<link rel="stylesheet" href="../resources/css/jquerymobile/jquery.mobile-1.3.1.min.css" />
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script src="../resources/js/jquerymobile/jquery.mobile-1.3.1.min.js"></script>
<style>
@media screen and (min-width: 20em) {
	   .my-custom-class th.ui-table-priority-1,
	   .my-custom-class td.ui-table-priority-1 {
	     display: table-cell;
	   }
	}
	/* Show priority 2 at 480px (30em x 16px) */
	@media screen and (min-width: 30em) {
	   .my-custom-class  th.ui-table-priority-2,
	   .my-custom-class td.ui-table-priority-2 {
	     display: table-cell;
	   }
	}
</style>
<script>

var PIC_INDEX = 0;
var CAN_ADD_NEW_PIC = false;
var IS_PIC_ADDED = false;
getcurrentlats();
function getcurrentlats(){
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successFunction, errorFunction);
	} else {
 		alert('It seems like Geolocation, which is required for this page, is not enabled in your browser. Please use a browser which supports it.');
	}
}
function errorFunction(){
	alert("Error");
}

function successFunction(position) {
 jQuery('#latitude').val(position.coords.latitude);
 jQuery('#longitude').val(position.coords.longitude);
}
function fileSelected() {
    var fup = document.getElementById('estimatePhotographsList['+PIC_INDEX+'].fileUpload').files[0];
    var fileName = fup.name;
    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
    if(ext == "gif" || ext == "GIF" || ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "png" || ext == "PNG"  )
    {
  	  if (fup) {
            ParseFile(fup);
			return true;
  	  }
    } 
    else
    {		
		document.getElementById('estimatePhotographsList['+PIC_INDEX+'].fileUpload').value = '';
		alert("Upload files with these formats : gif,jpg,jpeg,png");
		return false;
    }
}

function ParseFile(file) {

	Output(
		"<p>File information: <strong>" + file.name +
		"</strong> type: <strong>" + file.type +
		"</strong> size: <strong>" + file.size +
		"</strong> bytes</p>"
	);

	// display an image
	if (file.type.indexOf("image") == 0) {
		var reader = new FileReader();
		reader.onload = function(e) {
			Output(
				"<p><strong>" + file.name + ":</strong><br />" +
				'<img width=50% height=50% src="' + e.target.result + '" /></p>'
			);
		}
		reader.readAsDataURL(file);
	}

	// display text
	if (file.type.indexOf("text") == 0) {
		var reader = new FileReader();
		reader.onload = function(e) {
			Output(
				"<p><strong>" + file.name + ":</strong></p><pre width=50% height=50%>" +
				e.target.result.replace(/</g, "&lt;").replace(/>/g, "&gt;") +
				"</pre>"
			);
		}
		reader.readAsText(file);
	}
}

function Output(msg) {
	var m = document.getElementById('messages['+PIC_INDEX+']');
	m.innerHTML ="";
	m.innerHTML = msg + m.innerHTML;
	CAN_ADD_NEW_PIC = true;
	if(!IS_PIC_ADDED)
		IS_PIC_ADDED=true;
	var textareaDivs = document.getElementsByName("textareaDiv");
	textareaDivs[textareaDivs.length-1].style.display="";
	
}
function addPhoto()
{
	if(CAN_ADD_NEW_PIC)
		CAN_ADD_NEW_PIC = false;
	else
	{	
		alert("Please attach photo before attaching another");
		return;
	}
	var uploadDivs = document.getElementsByName("photoUploadDiv");
	var textareaDivs = document.getElementsByName("textareaDiv");
	var noOfDivs = uploadDivs.length;
	for(var i = 0; i<noOfDivs;i++)
	{
		uploadDivs[i].style.display="none";
		textareaDivs[i].style.display="none";
	}	
	var m = document.getElementById("uploadfiles");
	PIC_INDEX++;
	var tempElement = document.createElement('div');
	tempElement.innerHTML=generateNewFileAttachmentDiv();
    while (tempElement.firstChild) {
       	m.appendChild(tempElement.firstChild);
    }
}
function generateNewFileAttachmentDiv()
{
	var divString = '<div data-role="content"  align="center" name="photoUploadDiv" class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c ui-content">'+
	'<label for="estimatePhotographsList['+PIC_INDEX+'].fileUpload"> Attach Photo<br/> </label>'+ 
	'<div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="file" name="estimatePhotographsList['+PIC_INDEX+'].fileUpload" id="estimatePhotographsList['+PIC_INDEX+'].fileUpload" accept="/*;capture=camera" onchange="fileSelected();" /></div>'+
	'<div id="messages['+PIC_INDEX+']"></div>'+
	'<s:hidden id="estimatePhotographsList['+PIC_INDEX+'].imageDetail" name="estimatePhotographsList['+PIC_INDEX+'].imageDetail" />'+
	'</div>'+
	'<div data-role="content" class="ui-content" align="center" name="textareaDiv" style="display:none;">'+
	'<label data-mini="true" id="whatLabel" for="estimatePhotographsList['+PIC_INDEX+'].description">Details :</label>'+
	'<textarea data-mini="true" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset ui-mini" cols="40" rows="8" id="estimatePhotographsList['+PIC_INDEX+'].description" name="estimatePhotographsList['+PIC_INDEX+'].description"  maxlength="512"></textarea>'+
	'</div>';
	return divString;

}
function validateUpload()
{
	if(document.getElementById("latitude").value=='' || document.getElementById("longitude").value=='')
	{
		alert("Latitude and longitude was not captured. Please enable location sharing and proceed");
		return false;
	}
	if(IS_PIC_ADDED)
	{
		return true;
	}
	else
	{
		alert("Please attach at least one photo");
		return false;
	}	
}
</script>
</head>

<body >
	<s:form action="uploadEstimatePhotos!savePhotos.action" method="post"
		enctype="multipart/form-data" theme="simple" name="tablet" id="tablet"
		data-ajax="false" onsubmit="return validateUpload();">
		<s:token />
		<s:push value="model">
			<s:hidden id="latitude" name="latitude" />
			<s:hidden id="longitude" name="longitude" />
			<s:hidden id="estId" name ="estId" />
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div class="errorstyle">
					<s:actionmessage />
				</div>
			</s:if>
			<div id="uploadfiles" data-role="page" data-add-back-btn="true" class="pageclass">
				<div data-theme="b" data-role="header" data-position="fixed">
					<h5>Select File</h5>
				</div>
				<fieldset class="ui-grid-a"  align="center" >
					<div class="ui-block-a">
						<button type="submit" data-mini="true" id="submit" onclick="return validateUpload();" data-theme="b">Save</button>
					</div>
					<div class="ui-block-b">
						<button type="button" data-mini="true" id="cancel" onclick="addPhoto();" data-theme="d">Attach Another Photo</button>
					</div>
				</fieldset>
				<div data-role="content" align="center" name="photoUploadDiv" >
					<label for="estimatePhotographsList[0].fileUpload"> Attach Photo </label>
					<input type="file" name="estimatePhotographsList[0].fileUpload" id="estimatePhotographsList[0].fileUpload"
						accept="/*;capture=camera" onchange="fileSelected();" />
					<div id="messages[0]"></div>
					<s:hidden id="estimatePhotographsList[0].imageDetail" name="estimatePhotographsList[0].imageDetail" />
				</div>
				<div data-role="content"  align="center" name="textareaDiv" style="display:none;" >
					<label data-mini="true" id="whatLabel" for="estimatePhotographsList[0].description">Photo Details :</label>
					<s:textarea data-mini="true" cols="40" rows="8" id="estimatePhotographsList[0].description" name="estimatePhotographsList[0].description" maxlength="512"/>
				</div>
			</div>
		</s:push>
	</s:form>
</body>
</html>
