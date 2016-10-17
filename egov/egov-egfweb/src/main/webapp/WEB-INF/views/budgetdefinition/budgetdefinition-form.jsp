<input type="hidden" value="${modify}" id="modify" />
<input type="hidden" value="${mode}" id="mode" />
<c:forEach items="${validationMessage}" var="basMessage">
	<c:out value="${basMessage}" />
	<br />
</c:forEach>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Definition</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.name" /> </label>
						<div class="col-sm-6 add-margin">
							<form:input path="name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isbere" /> </label>
						<div class="col-sm-2 col-xs-12 add-margin">
							<div class="radio">
								<label><form:radiobutton path="isbere" id="isBeRe"
										value="BE" required="required" checked="checked" />BE</label>
							</div>
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<div class="radio">
								<label><form:radiobutton path="isbere" id="isBeRe"
										value="RE" required="required" />RE</label>
							</div>
						</div>
						<form:errors path="isbere" cssClass="error-msg" />
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.financialyear" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="financialYear" id="financialYear"
								cssClass="form-control disablefield" cssErrorClass="form-control error"
								onchange="getParentByFinancialYear(this.value);getReferenceBudgets(this.value)"
								disabled="disabled">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<c:forEach items="${financialYearList}" var="mc">
									<option value="${mc.id}">${mc.finYearRange}</option>
								</c:forEach>
							</form:select>
							<form:errors path="financialYear" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.parent" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="parent" id="parent" cssClass="form-control disablefield"
								cssErrorClass="form-control error" disabled="disabled">
								<c:if test="${empty modify}">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
								</c:if>
								<form:options items="${budgets}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="parent" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /> </label>
						<div class="col-sm-6 add-margin">
							<form:input path="description"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="250" />
							<form:errors path="description" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactivebudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:checkbox path="isActiveBudget" />
							<form:errors path="isActiveBudget" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isprimarybudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:checkbox path="isPrimaryBudget" id="isPrimaryBudget" />
							<form:errors path="isPrimaryBudget" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.referencebudget" /> </label>
						<div class="col-sm-6 add-margin">
							<form:select path="referenceBudget" id="referenceBudget"
								cssClass="form-control disablefield" cssErrorClass="form-control error" disabled="disabled">
								<c:if test="${empty modify}">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
								</c:if>
								<form:options items="${budgets}" itemValue="id" itemLabel="name" />
							</form:select>
							<form:errors path="referenceBudget" cssClass="error-msg" />
						</div>
					</div>
					<input type="hidden" name="budget" id="budgetId" value="${budget.id}" /> <input
						type="hidden" id="financialYearId"
						value="${budget.financialYear.id}" /> <input type="hidden"
						id="parentValue" value="${budget.parent.name}" /> <input
						type="hidden" id="referenceValue"
						value="${budget.referenceBudget.id}" /> 
						
					<script type="text/javascript"
						src="<c:url value='/resources/app/js/budgetDefinitionHelper.js'/>"></script>