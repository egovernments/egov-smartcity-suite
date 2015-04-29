<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
	<script>
		

		var serviceConnections = new Array();
		
		function viewBPA() {
    		window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+dom.get("bpaNumber").value,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }
		

		function enableServiceConnections() {
			var ids = jQuery("input[id^=srvConn]");
			for (var i = 0; i < ids.length; i++) {
				ids[i].disabled = false;
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
						dom.get("roadcut_error").style.display="block";
						document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.bpaCut.serviceConneciton.error.msg" />';
					}
				}
			});
		}
		
		function validateInputBPA() {
			dom.get("roadcut_error").style.display="block";
			dom.get("dwCategory").value='BPA';
			if(document.getElementById('citizenName').value=="")
			{
				document.getElementById("roadcut_error").innerHTML='<s:text name="depositworks.roadcut.enter.name" />';
				return false;
			}
			if(!validateApplicantDetails()){
				return false;
			}

			if(dom.get("bpaNumber").value=="") {
	           	document.getElementById("roadcut_error").innerHTML='<s:text name="dw.bpa.bpaNumber.mandatory" />';
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
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.bpa.check.serviceConn" />';
				return false;
			}

		    dom.get("roadcut_error").style.display='none'; 
		  	dom.get("roadcut_error").innerHTML='';
			return true;
		}

		function loadBPA() {
			dom.get("bpaNumber").value = '<s:property value="%{bpaNumber}"/>';
			document.getElementById("bpaNumberSpanId").innerHTML = '<s:property value="%{bpaNumber}"/>';
			var srvConnections = new Array();
			<s:if test="%{mode == 'view' || mode == 'rejectForResubmission' || mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate'}">
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
			<s:if test="%{mode != 'view' && mode != 'rejectForResubmission' && mode!='UpdateRoadCutDate' && mode!='UpdateRoadCutRestorationDate'}">
				disableServiceConnections(dom.get("bpaNumber").value);
			</s:if>
		}

		function setServiceConnValue(obj, hiddenid, value) {
			if (obj.checked) {
				hiddenid.value = value;
			} else {
				hiddenid.value = '';
			}
		}

		function enableOrDisableElementsBPA() {
			<s:if test="%{mode == 'view' || mode == 'rejectForResubmission' || mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate'}">
				dom.get("searchAnchor").onclick=function(){return false;};
				jQuery("#appRequestDate").attr('disabled', true);
				var srvConnIds = jQuery("input[id^=srvConn]");
				var refNumIds = jQuery("input[id^=refNumber]");
				for (var i = 0; i < srvConnIds.length; i++) {
					srvConnIds[i].disabled = true;
					refNumIds[i].readOnly = true;
				}
				jQuery("#bpaSubmitButton").hide();
	
			</s:if>
		}
	</script>

		<s:hidden name="dwCategory" id="dwCategory" value="%{dwCategory}"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						&nbsp;
					</tr>
					<tr>
				    	<td>
				    		<div id="bpaRequestDetails_div">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" > 
									<tr>
										<td class="greyboxwk">
											<span class="mandatory">*</span><s:text name="depositworks.roadcut.bpaNumber" />:
										</td>
										<td class="greybox2wk">
											<s:hidden name="bpaNumber" id="bpaNumber" value="%{bpaNumber}"/>
											<s:hidden name="zoneId" id="zoneId" value="%{zoneId}"/>
											<s:hidden name="wardId" id="wardId" value="%{wardId}"/>
											<s:hidden name="areaId" id="areaId" value="%{areaId}"/>
											<s:hidden name="localityId" id="localityId" value="%{localityId}"/>
											<s:hidden name="streetId" id="streetId" value="%{streetId}"/>
										
											
											<a href="javascript:viewBPA()" id="bpaNumberSpanId" name="bpaNumberSpanId">
											</a>
											<a href="javascript:void(0)" id = "searchAnchor" name="searchAnchor" onclick="javascript:onclickOfBPASearch();">
											<img src="${pageContext.request.contextPath}/image/magnifier.png" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>
										</td>
										<td class="greyboxwk">
											<span class="mandatory">*</span><s:text name="depositworks.aplication.date">:</s:text>
										</td>
										<td class="greybox2wk">
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
									<s:if test="%{mode == 'view' || mode == 'rejectForResubmission' || mode=='UpdateRoadCutDate' || mode=='UpdateRoadCutRestorationDate'}">
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
										<s:iterator value="dropdownData.bpaServiceConnections" var="typeOfRoadCut" status="rowstatus">
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
	
