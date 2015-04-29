<script>
function setupServiceDeptInDtls(elem){
	var orgId = elem.value;
   	populatercDtlsServiceDept({organizationId:orgId});
}
function setTypeOfCut(elem, fromOnChange)
{
	var typeOfCutObj = document.getElementById("typeOfCut");
	var orgznId='';
	var typeOfCutId='';
	var elemOrgznId;
	if(elem==null|| elem=='')
		return;
	typeOfCutObj.disabled=false;
	if(fromOnChange)
		elemOrgznId  = elem.value;
	else
		elemOrgznId  = elem;
	<s:iterator id="typesOfRoadCutList" value="dropdownData.typesOfRoadCut" status="typeOfCut_Status">
		orgznId="<s:property value='organization.id'/>";
		typeOfCutId="<s:property value='id'/>";
		if(orgznId==elemOrgznId)
		{
			typeOfCutObj.value=typeOfCutId;
			showOrHideSchemeDetails(typeOfCutObj);
			typeOfCutObj.disabled=true;		
		}	
	</s:iterator>
}
</script>
<table width="100%" cellspacing="0" cellpadding="0" border="0" > 
			<tr>
				<td colspan="6" class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/image/arrow.gif" />
					</div>
					<div class="headplacer"><s:text name="depositworks.roadcut.app.request.details" /></div>
				</td>
			</tr>
			<tr>
				<td class="whiteboxwk">&nbsp;</td>
				<td class="whitebox2wk"><s:radio name="applicationRequest.depositWorksCategory" id="depositWorksCategory" list="%{depositWorksCategoryMap}" onchange="toggleShowBPADetails(this)" /></td>
				<td class="whiteboxwk">&nbsp;</td>
				<td class="whitebox2wk">&nbsp;</td>
			</tr>
			<tr>
				<td class="greyboxwk">
					<span class="mandatory">*</span><s:text name="depositworks.roadcut.applicant.name.desig" />:
				</td>
				<td class="greybox2wk" colspan="5">
					<s:textfield name="applicationRequest.applicantName" id="applicantName" size="30" />
					&nbsp&nbsp&nbsp&nbsp<span class="mandatory">#<s:text name="depositworks.roadcut.applicant.name.warning" /></span>
				</td>
			</tr>
			<tr id="newOrgznServiceDeptTR" style="display: none;">
				<td class="whiteboxwk">
					<span class="mandatory"></span><s:text name="depositworks.applicant.organization" />:
				</td>
				<td class="whitebox2wk">
					<s:select id="rcDtlsOrganization" name="rcDtlsOrganization" class="input" onchange="setupServiceDeptInDtls(this);setTypeOfCut(this,true);"
						list="dropdownData.organizationList" headerKey="-1" headerValue="--- Select ---"
						listKey="id" listValue="name" value="%{applicationRequest.organization.id}" />
					<egov:ajaxdropdown id="rcDtlsServiceDeptDropdown" fields="['Text','Value']" dropdownId='rcDtlsServiceDept' url='depositWorks/ajaxDepositWorks!getAllServiceDepartmentsForOrganization.action' selectedValue="%{applicationRequest.organization.id}"/>
				</td>
				<td class="whiteboxwk">
					<span class="mandatory"></span><s:text name="depositworks.applicant.service.dept" />:
				</td>
				<td class="whitebox2wk">
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="rcDtlsServiceDept" id="rcDtlsServiceDept" cssClass="selectwk" list="dropdownData.serviceDeptInDtlsList" listKey="id" listValue="name" value="%{applicationRequest.serviceDepartment.id}"  />
				</td>
				<td class="whiteboxwk"></td>
				<td class="whitebox2wk"></td>
			</tr>
			<tr id="mirrorApplicantOrgznServDeptTR" style="display: none;">
				<td class="whiteboxwk">
					<span class="mandatory"></span><s:text name="depositworks.applicant.organization" />:
				</td>
				<td class="whitebox2wk">
					<s:select id="mirrorApplicantOrgzn" name="mirrorApplicantOrgzn" class="input" disabled="true"
						list="dropdownData.organizationList" headerKey="-1" headerValue="--- Select ---"
						listKey="id" listValue="name" value="%{applicationRequest.applicant.organization.id}" />
				</td>
				<td class="whiteboxwk">
					<span class="mandatory"></span><s:text name="depositworks.applicant.service.dept" />:
				</td>
				<td class="whitebox2wk">
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" disabled="true" name="mirrorApplicantServDept" id="mirrorApplicantServDept" cssClass="selectwk" list="dropdownData.serviceDeptList" listKey="id" listValue="name" value="%{applicationRequest.applicant.serviceDepartment.id}"  />
				</td>
				<td class="whiteboxwk"></td>
				<td class="whitebox2wk"></td>
			</tr>
			<tr id="serviceDeptTR" style="display: none;">
				<td class="whiteboxwk">
					<span class="mandatory"></span><s:text name="depositworks.roadcut.service.department" />:
				</td>
				<td class="whitebox2wk">
					<s:select id="serviceDept" name="serviceDeptId" cssClass="selectwk" 
						list="dropdownData.serviceDeptList" headerKey="-1" headerValue="--- Select ---"
						listKey="id" listValue="name" value="%{applicationRequest.applicant.serviceDepartment.id}" />
				</td>
				<td class="whiteboxwk" colspan="4" />
			</tr>
			<tr>
				<td class="greyboxwk">
					<span class="mandatory">*</span><s:text name="depositworks.roadcut.typeofcut" />:
				</td>
				<td class="greybox2wk">
					<s:select id="typeOfCut" name="applicationRequest.depositWorksType.id" cssClass="selectwk" 
						list="dropdownData.typesOfRoadCut" headerKey="-1" headerValue="--- Select ---"
						listKey="id" listValue="code" onchange="showOrHideSchemeDetails(this)" value="%{applicationRequest.depositWorksType.id}" />
				</td>
				<td class="greyboxwk">
					<span class="mandatory">*</span><s:text name="depositworks.aplication.date">:</s:text>
				</td>
				<td class="greybox2wk">
					<s:if test="%{applicationRequest.applicationDate!=null}">
						<s:date name="applicationRequest.applicationDate" var="appRequestDateFormat" format="dd/MM/yyyy"/>
			         	<s:textfield name="applicationRequest.applicationDate" value="%{appRequestDateFormat}" id="appRequestDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" disabled="true" />			         	
		         	</s:if>
		         	<s:else>
		         		<s:textfield name="todaysDate" disabled="true" />
		         	</s:else>
				</td>
				 <td class="greyboxwk" id="purposeOfCutLabelTD" style="visibility:hidden">
		            <span class="mandatory">*</span><s:text name="dw.roadcut.details.purposeofcut" />:
		        </td>
		        <td class="greybox2wk" id="purposeOfCutFieldTD" style="visibility:hidden">
		            <s:select id="purposeOfRoadCut" name="applicationRequest.purposeOfRoadCut" cssClass="selectwk" 
		                list="dropdownData.purposeOfRoadCutList" headerKey="-1" headerValue="--- Select ---"
		                 value="%{applicationRequest.purposeOfRoadCut}" onchange="defaultDepthOfCut(this)" />
		        </td>
			</tr>
			<tr id="schemeDetailsTR" style="visibility:hidden">
			
				<td class="whiteboxwk" id="isSchemeBasedTextTD"  style="visibility:hidden">
					<span class="mandatory">*</span><s:text name="depositworks.roadcut.schemebasedcut" />:
				</td>
				<td class="whitebox2wk" id="isSchemeBasedTD"  style="visibility:hidden">
					<s:radio name="applicationRequest.isSchemeBased" id="isSchemeBasedRadio" onchange="showOrHideAdd(this);" list="#{true:'Yes',false:'No'}" value="%{applicationRequest.isSchemeBased}"/>
				</td>
				<td class="whiteboxwk" id="schemenameTextTD">
					<span class="mandatory">*</span><s:text name="depositworks.roadcut.schemename" />:
				</td>
				<td class="whitebox2wk" id="schemenameTD">
					<s:textfield name ="applicationRequest.schemeName" id="applicationRequest.schemeName" />
				</td>
				<td class="whiteboxwk" id="schemedetailsTextTD">
					<span class="mandatory">*</span><s:text name="depositworks.roadcut.schemedetails" />:
				</td>
				<td class="whitebox2wk" id="schemedetailsTD">
					<s:textfield name ="applicationRequest.schemeDetails" id="applicationRequest.schemeDetails" />
				</td>
			</tr>
			</table>
		        
		       	<div align="left" style="width:1200px;overflow:auto">
			        <div class="yui-skin-sam">
			            <div id="jurisdictionDetailsTableDiv"></div>
			        </div>
			        <br /><br />
		        </div>
				<script>
					makeJurisdictionDetailsTable();
					var rowCount = 0;
					<s:if test="%{applicationRequest.roadCutDetailsList!=null && applicationRequest.roadCutDetailsList.size()!=0}" >
					var iteratorIndex =0;
					var zoneDD,wardDD,areaDD,localityDD,streetDD;
		            <s:iterator var="rcDetailsList" value="applicationRequest.roadCutDetailsList" status="row_status">
		        
			         	jurisdictionDetailsTable.addRow({Id:"<s:property value='id'/>",
		                                        SlNo:"<s:property value='#row_status.count'/>",
		                                        BPANumber:"<s:property value='bpaNumber'/>",
		                                        LocationName:"<s:property value='locationNameJS'/>",
		                                        Length:"<s:property value='roadLength'/>",
		                                        Breadth:"<s:property value='roadBreadth'/>",
		                                        Depth:"<s:property value='roadDepth'/>",
		                                        Remarks:"<s:property value='remarksJS'/>",
		                                        DepthDropDown:"<s:property value='roadDepth'/>",
		                                        RemarksEmergency:"<s:property value='remarksJS'/>",
		                                        Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                        Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});

		         	    <s:if test="%{bpaNumber!= null && bpaNumber!=''}" >
		                    jurisdictionDetailsTable.showColumn("BPANumber");
		                </s:if>
		                <s:if test="%{model.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@EMERGENCYCUT || model.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
			                jurisdictionDetailsTable.hideColumn("Depth");
			                jurisdictionDetailsTable.hideColumn("Remarks");
			                jurisdictionDetailsTable.showColumn("DepthDropDown");
			                jurisdictionDetailsTable.showColumn("RemarksEmergency");
			                document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].roadDepthEmergency")[0].value=roundTo("<s:property value='roadDepth'/>",2);
			                <s:if test="model.applicationRequest.purposeOfRoadCut==@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE">
			                	document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].roadDepthEmergency")[0].disabled=true;
			                </s:if>
			            </s:if>
			         	zoneDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].zone.id")[0];
		        		wardDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].ward.id")[0];
						areaDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].area.id")[0];
						localityDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].locality.id")[0];
						streetDD = document.getElementsByName(DETAILS_ELEMENT_PREFIX+"["+rowCount+"].street.id")[0];
		
						wardDD.length=0;
						areaDD.length=0;
						localityDD.length=0;
						streetDD.length=0;
						
						wardDD.options[0]=new Option("---Choose---","-1");
						areaDD.options[0]=new Option("---Choose---","-1");
						localityDD.options[0]=new Option("---Choose---","-1");
						streetDD.options[0]=new Option("---Choose---","-1");
		
						iteratorIndex=0;
						
						<s:iterator id="wardDDList" value="#rcDetailsList.jurisdictionDropDownLists.wardList" status="ward_status">
							wardDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
							iteratorIndex++;
						</s:iterator>	 
						   
						iteratorIndex = 0;
						      
						<s:iterator id="areaDDList" value="#rcDetailsList.jurisdictionDropDownLists.areaList" status="area_status">
							areaDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
							iteratorIndex++;
						</s:iterator>
		
						iteratorIndex = 0;
							          
						<s:iterator id="localityDDList" value="#rcDetailsList.jurisdictionDropDownLists.localityList" status="locality_status">
							localityDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
							iteratorIndex++;
						</s:iterator>
		
						iteratorIndex = 0;
						         
						<s:iterator id="streetDDList" value="#rcDetailsList.jurisdictionDropDownLists.streetList" status="street_status">
							streetDD.options[iteratorIndex]=new Option("<s:property value='name'/>",<s:property value="id"/>);
							iteratorIndex++;
						</s:iterator>	          
				
						rowCount++;
		
			          	zoneDD.value=<s:property value="#rcDetailsList.zone.id" />;
			          	wardDD.value=<s:property value="#rcDetailsList.ward.id" />;
			          	areaDD.value=<s:property value="#rcDetailsList.area.id" />;
			          	localityDD.value=<s:property value="#rcDetailsList.locality.id" />;
			          	streetDD.value=<s:property value="#rcDetailsList.street.id" />;
			          	
			        </s:iterator> 
			        </s:if>
				</script>