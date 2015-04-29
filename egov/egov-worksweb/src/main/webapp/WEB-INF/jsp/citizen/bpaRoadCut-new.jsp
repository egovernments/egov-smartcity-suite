<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
	<head>
		<title>
			<s:text name="dw.bpa.title" />
		</title>
	</head>	
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script>
		window.history.forward(1);
		function noBack() {
			window.history.forward(); 
		}

		var serviceConnections = new Array();
		
		function onclickOfBPASearch() {
            if(document.getElementById("bpaNumberSpanId")){
                window.open("${pageContext.request.contextPath}/../bpa/search/search!searchApplForm.action?rowId=","","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
            }
	    }
	    
		function showBPAViewPage() {
    		window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+dom.get("bpaNumber").value,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

		function update(elemValue) {	
			if(elemValue!="" || elemValue!=null) {
				var a = elemValue.split("`~`");
				rowId=a[0];
				var bpaNumber=a[1];
				var zoneId=a[2];
				wardId=a[3];
				areaId=a[4];
				localityId=a[5];
				streetId=a[6];
				dom.get("bpaNumber").value=bpaNumber;
				dom.get("zoneId").value=zoneId;
				dom.get("wardId").value=wardId;
				dom.get("areaId").value=areaId;
				dom.get("localityId").value=localityId;
				dom.get("streetId").value=streetId;
				document.getElementById("bpaNumberSpanId").innerHTML=bpaNumber;
				enableServiceConnections();
				disableServiceConnections(bpaNumber);
			}
		}

		function enableServiceConnections() {
			var ids = jQuery("input[id^=srvConn]");
			for (var i = 0; i < ids.length; i++) {
				ids[i].disabled = false;
				ids[i].checked = false;
			}
		}
		
		function disableServiceConnections(bpaNumber) {
			jQuery.ajax({
				url: "/egworks/depositWorks/ajaxDepositWorks!getAppReqGeneratedDwTypesByBpaNumber.action?bpaNumber=" + bpaNumber,
				dataType: "json",
				success: function(result){
					var res = result.ResultSet.Result;
					var count = 0;
					for (var i = 0; i < res.length; i++) {
						if (serviceConnections.indexOf(res[i].key) != "-1") {
							jQuery('#srvConn' +res[i].key+ '')[0].checked = false;
							jQuery('#srvConn' +res[i].key+ '').attr("disabled", true);
							count++;
						}
					}
					if (count == 3) {
						dom.get("citizenPortal_error").style.display="block";
						document.getElementById("citizenPortal_error").innerHTML='<s:text name="depositworks.bpaCut.serviceConneciton.error.msg" />';
					}
				}
			});
		}
		
		function validateInput() {
			dom.get("citizenPortal_error").style.display="block";
			if(dom.get("bpaNumber").value=="") {
	           	document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.bpa.bpaNumber.mandatory" />';
				return false;
	        }

			var ids = jQuery("input[id^=srvConn]");
			var checked = false;
			for (var i = 0; i < ids.length; i++) {
				if (ids[i].checked) {
					checked = true;
					break;
				}
			}
			if (!checked) {
				document.getElementById("citizenPortal_error").innerHTML='<s:text name="dw.bpa.check.serviceConn" />';
				return false;
			}
		    dom.get("citizenPortal_error").style.display='none'; 
		  	dom.get("citizenPortal_error").innerHTML='';
			return true;
		}

		function resetFormData() {
			var refNumIds = jQuery("input[id^=refNumber]");
			for (var i = 0; i < refNumIds.length; i++) {
				refNumIds[i].value = "";
			}
			enableServiceConnections();
			var hiddenIds = jQuery("input[id^=appDetailsList]");
			for (var i = 0; i < hiddenIds.length; i++) {
				hiddenIds[i].value = "";
			}
			dom.get("bpaNumber").value = "";
			document.getElementById("bpaNumberSpanId").innerHTML = "";
			dom.get("zoneId").value="";
			dom.get("wardId").value="";
			dom.get("areaId").value="";
			dom.get("localityId").value="";
			dom.get("streetId").value="";
		  	dom.get("citizenPortal_error").innerHTML='';
		  	dom.get("citizenPortal_error").style.display='none';
		  	if (dom.get("errorstyle") != null) {
		  		dom.get("errorstyle").innerHTML='';
		  		dom.get("errorstyle").style.display='none';
		  	}
		}

		function load() {
			dom.get("bpaNumber").value = '<s:property value="%{bpaNumber}"/>';
			document.getElementById("bpaNumberSpanId").innerHTML = '<s:property value="%{bpaNumber}"/>';
			var srvConnections = new Array();
			<s:if test="%{mode == 'view'}">
				jQuery("#srvConn")[0].checked = true;
			</s:if>
			<s:else>
				<s:iterator value="appDetailsList" status="rowstatus">
					<s:if test="%{applicationRequest.depositWorksType.id != null}">
						srvConnections[<s:property value="%{#rowstatus.index}"/>] = '<s:property value="applicationRequest.depositWorksType.id"/>';
					</s:if>
				</s:iterator>
			</s:else>
			var ids = jQuery("input[id^=srvConn]");
			for (var i = 0; i < ids.length; i++) {
				var id = ids[i].id.split("srvConn")[1];
				if (srvConnections.indexOf(id) != "-1") {
					ids[i].checked = true;
				}
			}
			<s:iterator value="serviceConnectionTypes" status="rowstatus">
				serviceConnections[<s:property value="%{#rowstatus.index}"/>] = '<s:property value="id"/>';
			</s:iterator>
			<s:if test="hasErrors() || hasActionErros()">
				disableServiceConnections('<s:property value="%{bpaNumber}"/>');
			</s:if>
		}

		function setServiceConnValue(obj, hiddenid, value) {
			if (obj.checked) {
				hiddenid.value = value;
			} else {
				hiddenid.value = '';
			}
		}

		function enableOrDisableElements() {
			<s:if test="%{mode == 'view'}">
				dom.get("searchAnchor").onclick=function(){return false;};
				jQuery("#appRequestDate").attr('disabled', true);
				var srvConnIds = jQuery("input[id^=srvConn]");
				var refNumIds = jQuery("input[id^=refNumber]");
				for (var i = 0; i < srvConnIds.length; i++) {
					srvConnIds[i].disabled = true;
					refNumIds[i].readOnly = true;
				}
				jQuery("#saveButton").hide();
				jQuery("#resetButton").hide();
			</s:if>
		}
	</script>
	<body onload="load(); enableOrDisableElements(); noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
	<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
	<div class="errorstyle" id="citizenPortal_error" style="display: none;"></div>
		<s:form name="bpaRoadCutForm" action="bpaRoadCut" theme="simple">
		<s:hidden name="dwCategory" value="BPA"/>
		<s:token />
		<s:hidden id="citizenId" name="citizenId" value="%{citizenId}"/>
				<div class="formmainbox">
				<div class="insidecontent">
				<div class="rbroundbox2">
				<div class="rbtop2"><div></div></div>
				<div class="rbcontent2">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						&nbsp;
					</tr>
					<tr>
				    	<td>
				    		<div id="requestDetails_div">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" > 
									<tr>
										<td class="greyboxwk" width="25%">
											<span class="mandatory">*</span><s:text name="depositworks.roadcut.bpaNumber" />:
										</td>
										<td class="greybox2wk" width="25%">
											<s:hidden name="bpaNumber" id="bpaNumber" value="%{bpaNumber}"/>
											<s:hidden name="zoneId" id="zoneId" value="%{zoneId}"/>
											<s:hidden name="wardId" id="wardId" value="%{wardId}"/>
											<s:hidden name="areaId" id="areaId" value="%{areaId}"/>
											<s:hidden name="localityId" id="localityId" value="%{localityId}"/>
											<s:hidden name="streetId" id="streetId" value="%{streetId}"/>
											<a href="javascript:showBPAViewPage()" id="bpaNumberSpanId" name="bpaNumberSpanId">
											</a>
											<a href="javascript:void(0)" id = "searchAnchor" name="searchAnchor" onclick="javascript:onclickOfBPASearch()">
											<img src="${pageContext.request.contextPath}/image/magnifier.png" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>
										</td>
										<td class="greyboxwk" width="25%">
											<s:text name="depositworks.aplication.date"/>:
										</td>
										<td class="greybox2wk" width="25%">
											<s:date name="applicationDate" var="appRequestDateFormat" format="dd/MM/yyyy"/>
											<b><s:property value="%{appRequestDateFormat}"/></b>
										</td>
									</tr>
									<tr>
										<td colspan="4" class="whiteboxwk">&nbsp;</td>
									</tr>
								</table>
							</div>	
							<div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td class="headingwk" width="25%">&nbsp;</td>
										<td class="headingwk">
											<span class="mandatory">*</span><s:text name="depositworks.roadcut.service.connection" />:
										</td>
										<td class="headingwk">
											<s:text name="depositworks.roadcut.refNumber"/>:
										</td>
										<td class="headingwk" width="25%">&nbsp;</td>
									</tr>
									<s:if test="%{mode == 'view'}">
										<tr>
												<td class="greyboxwk">
													&nbsp;
												</td>
												<td class="greybox2wk">
													<s:checkbox name="srvConn" id="srvConn"/>
													<b><s:property value="%{applicationDetails.applicationRequest.depositWorksType.code}"/></b>
												</td>
												<td class="greybox2wk">
													<s:textfield name="applicationDetails.applicationRequest.referenceNumber" id="refNumber" value="%{applicationDetails.applicationRequest.referenceNumber}"/>
												</td>
												<td class="greyboxwk">
													&nbsp;
												</td>
											</tr>
									</s:if>
									<s:else>
										<s:iterator value="dropdownData.typesOfRoadCut" var="typeOfRoadCut" status="rowstatus">
											<tr>
												<td class="<s:if test="#rowstatus.odd == true ">greyboxwk</s:if><s:else>whiteboxwk</s:else>">
													&nbsp;
												</td>
												<td class="<s:if test="#rowstatus.odd == true ">greybox2wk</s:if><s:else>whitebox2wk</s:else>">
													<s:hidden name="appDetailsList[%{#rowstatus.index}].applicationRequest.depositWorksType.id" id="appDetailsList%{#rowstatus.index}" />
													<s:checkbox name="srvConn" id="srvConn%{id}" onchange='setServiceConnValue(this, appDetailsList%{#rowstatus.index} , %{id});'/>
													<b><s:property value="code"/></b>
												</td>
												<td class="<s:if test="#rowstatus.odd == true ">greybox2wk</s:if><s:else>whitebox2wk</s:else>">
													<s:textfield name="appDetailsList[%{#rowstatus.index}].applicationRequest.referenceNumber" id="refNumber%{id}"
													 value="%{appDetailsList[#rowstatus.index].applicationRequest.referenceNumber}"/>
												</td>
												<td class="<s:if test="#rowstatus.odd == true ">greyboxwk</s:if><s:else>whiteboxwk</s:else>">
													&nbsp;
												</td>
											</tr>
										</s:iterator>									
									</s:else>
								</table>
							</div>
						</td>		
				    </tr>
				</table>
				<br/>			
				<div class="buttonholdersearch" align = "center">
			      	<s:submit value="APPLY" cssClass="buttonfinal" onclick="return validateInput();" id="saveButton" method="save" name="button" />
			      	<input type="button" value="RESET" class="buttonfinal" onclick="resetFormData();" id="resetButton" />
			      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
				</div>
				</div>
				</div>
				</div>
				</div>
			</s:form>
	</body>
</html>
