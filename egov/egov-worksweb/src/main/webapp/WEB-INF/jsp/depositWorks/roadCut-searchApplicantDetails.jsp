<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<html>
<head>
	<title>
		<s:text name='depositworks.roadcut.title' />
	</title>
</head>

<script type="text/javascript">

function gotoEditPage(obj){
	if(obj.id=='newUser'){
	document.getElementById("registeredUser").checked="";
	document.getElementById("details").style.display='none';
	document.getElementById("search_Button").style.display='none';
	window.open("${pageContext.request.contextPath}/depositWorks/roadCut!newform.action",'_self','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.id=='registeredUser'){
		document.getElementById("newUser").checked="";
		document.getElementById("details").style.display='block';	
		document.getElementById("search_Button").style.display='block';
	}
}

function validateAndSubmit(){
    if(document.getElementById('emailId').value == "" && document.getElementById('mobileNo').value == "" ){
            document.getElementById("roadCut_error").style.display = 'block';
            document.getElementById("roadCut_error").innerHTML='<s:text name="dw.madatoryfields.notselected"/>';
            return false;
    }
    var mobNo = document.getElementById('mobileNo').value;
    
    	if(isNaN(mobNo))
    	{
    		document.getElementById("roadCut_error").innerHTML='<s:text name="depositworks.roadcut.invalid.mobileno"/>'; 
            document.getElementById("roadCut_error").style.display='';
    		return false;
    	}

    	if(mobNo.replace(".","~").search("~")!=-1)
    	{
    		document.getElementById("roadCut_error").innerHTML='<s:text name="depositworks.roadcut.period.notallowed"/>'; 
            document.getElementById("roadCut_error").style.display='';
    		return false;
    	}
    	if(mobNo.replace("+","~").search("~")!=-1)
    	{
    		document.getElementById("roadCut_error").innerHTML='<s:text name="depositworks.roadcut.plus.notallowed"/>'; 
            document.getElementById("roadCut_error").style.display='';
    		return false;
    	}
        
            document.getElementById("roadCut_error").style.display='none';
            document.getElementById("roadCut_error").innerHTML='';
            document.getElementById("mode").value='registeredUser';
            document.forms[0].action = '${pageContext.request.contextPath}/depositWorks/roadCut!searchRegisteredUser.action';
            document.forms[0].submit();
}
</script>

<body >
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="roadCut_error" style="display: none;"></div>
<s:form name="roadCutForm" action="roadCut" theme="simple">
	<s:push value="model">
	<s:hidden name="applicationRequest.applicant.id" id="citizenId" value="%{applicationRequest.applicant.id}"/>
	<s:hidden name="mode" id="mode"/>
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk"><div
														class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="dw.searchApplicantDetails.header" />
													</div></td>
											</tr>
											<tr>
												<td width="21%" class="whitebox2wk">&nbsp;</td>
												<td class="whitebox2wk"><s:text name="New User" /> <input
													name="newUser" type="radio" id="newUser"
													onClick="gotoEditPage(this);" class="selectwk" /></td>
												<td class="whitebox2wk">&nbsp;</td>
												<td class="whitebox2wk"><s:text name="Registered User" />
													<input name="registeredUser" type="radio"
													id="registeredUser" 
													onClick="gotoEditPage(this);" class="selectwk" /></td>
											</tr>
										</table>
										<table style="display:none" id="details" width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="11%" class="greyboxwk">
													<s:text name="dw.citizen.details.email" />:
													</td>
													<td width="11%" class="greybox2wk">
														<s:textfield name="emailId"
															value="%{emailId}"
															id="emailId" cssClass="selectwk" />
													</td>
													<td width="11%" class="greyboxwk">
													<s:text name="dw.citizen.details.mobileno" />:
													</td>
													<td width="11%" class="greybox2wk">
													<s:textfield name="mobileNumber"
															value="%{mobileNumber}"
															id="mobileNo" maxlength="10" cssClass="selectwk" />
													</td>
												</tr>
										</table>
									</td>
								</tr>
							</table>
							
						</div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   
   <tr>
   		<td colspan="4" class="shadowwk"></td> 
			<td>
				<div class="buttonholderwk" style="display:none;" id="search_Button">				
						<s:submit cssClass="buttonfinal" value="SEARCH" id="saveButton" method="searchRegisteredUser" onclick="return validateAndSubmit();"/>
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
				</div> 
			</td>
		</tr>
   </div><!-- end of formmainbox -->
  
	
	
   </s:push>
</s:form>
</body>
</html>