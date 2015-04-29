<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title>
			<s:text name='depositworks.roadcut.search.title' />
		</title>
	</head>	
		<script src="<egov:url path='js/works.js'/>"></script>
		<script src="<egov:url path='js/helper.js'/>"></script>
		<script type="text/javascript">
        var appRequestNumberSearchSelectionHandler = function(sType, arguments){ 
        	var oData = arguments[2];
        };
        function generateParameter()
        {
        	var citizenIdVal=document.getElementById("citizenId").value;
        	if(citizenIdVal=='')
            	return '';
        	else
            	return "citizenId="+citizenIdVal;
        }
        var depositCodeSearchSelectionHandler = function(sType, arguments) {  
            var oData = arguments[2];
            dom.get("depositCodeSearch").value=oData[0];
            dom.get("depositCodeId").value = oData[1];
        };
        var depositCodeSelectionEnforceHandler = function(sType, arguments) {
        	alert('<s:text name="estimate.depositworks.search.depCodeloading.failure"/>');
        };
        function clearHiddenDepositCode(obj)
        {
        	if(obj.value=="")
        	{
        		document.getElementById("depositCodeId").value="";
        	}	
        }

        function gotoPage(obj){
    		var currRow=getRow(obj); 
    		var id = getControlInBranch(currRow,'applicationDetailsId');
    		var citizenIdObj = document.getElementById("citizenId");
    		if(citizenIdObj.value!='')
        	{
	    		if(obj.value=='View'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=view&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if (obj.value == 'View Technical Details') {
	    			window.open("${pageContext.request.contextPath}/citizen/bpaRoadCut!view.action?mode=viewTechnicalDetails&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		    	}
	    		if (obj.value == 'Update Technical Details') {
	    			window.open("${pageContext.request.contextPath}/citizen/bpaRoadCut!view.action?mode=updateTechnicalDetails&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		    	}
	    		if (obj.value == 'Modify Technical Details') {
	    			window.open("${pageContext.request.contextPath}/citizen/bpaRoadCut!view.action?mode=modifyTechnicalDetails&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		    	}
	    		if(obj.value=='Modify Application'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Modify'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Damage Fee Communication Letter'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!exportPdf.action?mode=view&sourcepage=damageFeePDF&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Road-Cut Approval Letter'){	 
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewRoadCutApprovalPDF.action?appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Utilization Certificate'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewUtilizationCertificatePDF.action?appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Update Road Cut Date'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=UpdateRoadCutDate&appDetailsId="+id.value+"&citizenId="+citizenIdObj.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
        	}
    		else
        	{
    			if(obj.value=='View'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=view&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Modify'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Damage Fee Communication Letter'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!exportPdf.action?mode=view&sourcepage=damageFeePDF&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Road-Cut Approval Letter'){	 
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewRoadCutApprovalPDF.action?appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Print Utilization Certificate'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewUtilizationCertificatePDF.action?appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
	    		if(obj.value=='Update Road Cut Date'){	
	    			window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=UpdateRoadCutDate&appDetailsId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	    		}
            }	
    	}
    	
        function checkDepositCodeId(){
        	var code = dom.get("depositCodeSearch").value;
        	var id = dom.get("depositCodeId").value;
        	if(code=="" && id!=""){
        		document.getElementById("depositCodeId").value="";
        	}
        }
        </script>
	<body>
	<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
	<div class="errorstyle" id="citizenPortal_error" style="display: none;"></div>
		<s:form name="depositWorksSearchForm" action="depositWorks" theme="simple">
			<s:hidden id="citizenId" name="citizenId" value="%{citizenId}"/>
			<s:push value="model">
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
								        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
								            <div class="headplacer"><s:text name="depositworks.roadcut.search" /></div></td>
								        </tr>
										<tr>
											<td  class="whiteboxwk"><s:text name="depositworks.roadcut.search.applicationno" />:</td>
				        					<td  class="whitebox2wk">
				        						<div class="yui-skin-sam">
	        										<div id="appRequestNumberSearch_autocomplete">
                									<div>
				        								<s:textfield id="appRequestNumberSearch" name="applicationRequest.applicationNo" value="%{applicationRequest.applicationNo}"  cssClass="selectwk" />
				        							</div>
	        										<span id="appRequestNumberSearchResults"></span>
	        										</div>		
	        									</div>
	        									<egov:autocomplete name="appRequestNumberSearch" width="20" field="appRequestNumberSearch" url="ajaxDepositWorks!getAppRequestNos.action?" queryQuestionMark="false" paramsFunction="generateParameter" results="appRequestNumberSearchResults" handler="appRequestNumberSearchSelectionHandler" />	
				        					</td>
				        					<td  class="whiteboxwk"><s:text name="depositworks.roadcut.status" />:</td>
				        					<td  class="whitebox2wk">
				        						<s:select id="appReqStatus" name="appReqStatus" cssClass="selectwk" 
													list="citizenSearchStatusList" headerKey="-1" headerValue="%{getText('list.default.select')}"
													value="%{appReqStatus}" />
											</td>
					        			</tr>		
										<tr> 
											<td class="greyboxwk"><s:text name="depositworks.roadcut.search.fromdate" />:</td>
											<td class="greybox2wk"><s:date name="fromDate" var="fromApplicationDate"	format="dd/MM/yyyy" />
																	<s:textfield name="fromDate" id="fromDate"
																		cssClass="selectwk" value="%{fromApplicationDate}"
																		onfocus="javascript:vDateType='3';"
																		onkeyup="DateFormat(this,this.value,event,false,'3')" />
																	<a	href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
																		onmouseover="window.status='Date Picker';return true;"
																		onmouseout="window.status='';return true;"> <img
																			src="${pageContext.request.contextPath}/image/calendar.png"
																			alt="Calendar" width="16" height="16" border="0"
																			align="absmiddle" />
																	</a>
																<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
											</td>
											<td class="greyboxwk"><s:text name="depositworks.roadcut.search.todate" />:</td>
											<td class="greybox2wk"><s:date name="toDate" var="toApplicationDate"	format="dd/MM/yyyy" />
																				<s:textfield name="toDate" id="toDate"
																					value="%{toApplicationDate}" cssClass="selectwk"
																					onfocus="javascript:vDateType='3';"
																					onkeyup="DateFormat(this,this.value,event,false,'3')" />
																				<a	href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
																					onmouseover="window.status='Date Picker';return true;"
																					onmouseout="window.status='';return true;"> <img
																						src="${pageContext.request.contextPath}/image/calendar.png"
																						alt="Calendar" width="16" height="16" border="0"
																						align="absmiddle" />
																				</a>
																	<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
											</td>	 
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="estimate.depositworks.depositcode" />:</td>
											<td class="whitebox2wk">
												<div class="yui-skin-sam">
									                <div id="depositCodeSearch_autocomplete">
									                <div><s:textfield id="depositCodeSearch" type="text" name="depWrkCode" value="%{depWrkCode}" onBlur="clearHiddenDepositCode(this)"  class="selectwk"/><s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCodeId}"/></div>
									                <span id="depositCodeSearchResults"></span>
									                </div>
												</div>
												<egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="ajaxDepositWorks!searchDepositCodeAjax.action?"  queryQuestionMark="false" paramsFunction="generateParameter" results="depositCodeSearchResults" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
											</td>
											<td width="11%" class="whiteboxwk"><s:text name="depositworks.roadcut.application.type" />:</td>
									   		<td class="whitebox2wk">
									   		<s:select id="applicationType" name="applicationType" headerKey="-1"
															headerValue="%{getText('list.default.select')}" cssClass="selectwk"
															list="dropdownData.applicationTypeList" listKey="value" listValue="label"/>
									   			
								
											</td>
						   				</tr>
						   			</table>
						   		</td>
					   	</tr>
					</table>
					 <div class="buttonholdersearch" align = "center">
					      	<s:submit cssClass="buttonfinal" value="SEARCH" id="saveButton" onclick="checkDepositCodeId();" method="searchList" name="button" />
					      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
					 </div>
					 <div class="errorstyle" id="error_search" style="display: none;"></div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr><td><%@ include file='depositWorks-searchresults.jsp'%></td></tr>
						</table>
					</div>
				</div>
					
				</div>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
