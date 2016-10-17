<c:forEach items="${majorCode}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>
<c:forEach items="${maxCode}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>

<input type="hidden" value="${mode }" id="mode" />
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Group</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> <span class="mandatory"></span> </label>
						<div class="col-sm-6 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="250" required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.accounttype" /><span class="mandatory"></span></label>
						<div class="col-sm-4 add-margin">
							<form:select path="accountType" id="accountType"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${budgetAccountTypes}" />
							</form:select>
							<form:errors path="accountType" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.budgetingtype" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="budgetingType" id="budgetingType"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:options items="${budgetingTypes}" />
							</form:select>
							<form:errors path="budgetingType" cssClass="error-msg" />
						</div>

					</div>
					<input type="hidden" id="majorCodeValue"
						value="${budgetGroup.majorCode.id}" /> <input type="hidden"
						id="maxCodeValue" value="${budgetGroup.maxCode.id}" /> <input
						type="hidden" id="minCodeValue" value="${budgetGroup.minCode.id}" />
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.majorcode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="majorCode" id="majorCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${majorCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="majorCode" cssClass="error-msg" />
						</div>
					</div>
					<tr>
						<div class="form-group">
							<label class="col-sm-6 control-label text-center">OR</label>
						</div>
					</tr>
					<br>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.mincode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="minCode" id="minCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${minCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="minCode" cssClass="error-msg" />
						</div>

					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.maxcode" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="maxCode" id="maxCode"
								cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${minCodeList}" var="mc">
									<option value="${mc.id}">${mc.glcode}----${mc.name}</option>
								</c:forEach>
							</form:select>
							<form:errors path="maxCode" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /> </label>
						<div class="col-sm-6 add-margin">
							<form:textarea path="description" type="text" placeholder=""
								autocomplete="off" class="form-control low-width"
								maxlength="250" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.bgisactive" /> </label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isActive" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="budgetGroup" value="${budgetGroup.id}" />