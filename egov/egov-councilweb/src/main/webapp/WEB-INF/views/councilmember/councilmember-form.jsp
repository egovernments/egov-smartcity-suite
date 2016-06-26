		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">CouncilMember</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.electionward" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="electionWard" id="electionWard"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${boundarys}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="electionWard" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.designation" /> <span class="mandatory"></span> </label>
						<div class="col-sm-2 add-margin">
							<form:select path="designation" id="designation"
								cssClass="form-control" required="required"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${councilDesignations}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="designation" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.qualification" /> <span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="qualification" id="qualification"
								cssClass="form-control" required="required" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${councilQualifications}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="qualification" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.caste" /> <span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="caste" id="caste"  required="required" cssClass="form-control"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${councilCastes}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="caste" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.name" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="100" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.gender" /> <span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="gender" id="gender" required="required"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${gender}"/> 
							</form:select>
							<form:errors path="gender" cssClass="error-msg" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.mobilenumber"/><span class="mandatory"></span></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" cssClass="form-control patternvalidation" data-pattern="number" data-inputmask="'mask': '9999999999'"  placeholder="Mobile Number"  path="mobileNumber" minlength="10" maxlength="10" id="mobileNumber" required="required"/>
                            <form:errors path="mobileNumber" cssClass="error-msg"/>
                        </div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.birthdate" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							    <form:input type="text" cssClass="form-control datepicker" path="birthDate" id="birthDate" data-date-end-date="-18y" required="required"/>
                 			<form:errors path="birthDate" cssClass="error-msg" />
						</div>
					</div>
				    <div class="form-group">
                        
                        <label class="col-sm-2 control-label text-right"><spring:message code="lbl.emailid"/><span class="mandatory"></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" cssClass="form-control patternvalidation"   data-pattern="regexp_alphabetspecialcharacters"  path="emailId" id="emailId"  maxlength="32"/>
                            <form:errors path="emailId" cssClass="error-msg"/>
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.residentialaddress" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="residentialAddress" id="residentialAddress" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace" required="required" minlength="5" maxlength="256"/>
                 			<form:errors path="residentialAddress" cssClass="error-msg" />
						</div>
                    </div>
                   
		 	<input type="hidden" name="councilMember" value="${councilMember.id}" />
			<form:hidden path="status" id="status" value="${councilMember.status}" />		
				
				</div>
			</div>	
		</div>	
		
		
		
		
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body">
                   <div class="form-group">
                   <label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.electiondate" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							    <form:input type="text" cssClass="form-control dateval" path="electionDate" id="electionDate" data-date-end-date="0d" required="required"/>
                 			<form:errors path="electionDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.oathdate" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							    <form:input type="text" cssClass="form-control dateval" path="oathDate" id="oathDate" data-date-end-date="0d" required="required"/>
                 			<form:errors path="oathDate" cssClass="error-msg" />
						</div>
					</div> 
					 <div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.partyaffiliation" /> <span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:select path="partyAffiliation" id="partyAffiliation" required="required"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${councilPartys}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="partyAffiliation" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.photo" /> </span> </label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="attachments" name="attachments" data-id="1"	class="filechange inline btn" 	 />
							<form:errors path="attachments" cssClass="error-msg" />
						</div>
					</div> 
				</div>
			</div>	
		</div>			
		
		