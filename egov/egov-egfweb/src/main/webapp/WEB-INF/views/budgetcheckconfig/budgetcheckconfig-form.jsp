<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Budget Control Type Configuration</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.value" /> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="value" id="value"
							cssClass="form-control" required="required"
							cssErrorClass="form-control error">
							<form:options items="${configoptions}" />
						</form:select>
							<form:errors path="value" cssClass="error-msg" />
						</div>
						<input type="hidden" name="budgetControlType"
							value="${budgetControlType.id}" />