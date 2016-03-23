<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form" action="create" modelAttribute="cFinancialYear"
	id="cFinancialYearform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<%@ include file="cfinancialyear-form.jsp"%>
	</div>
	</div>
	</div>
	</div>

	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.create' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<script>
	$('#buttonSubmit').click(function(e) {
		if (validateFields()) {
			if ($('form').valid()) {
			}
		} else {
			e.preventDefault();
		}
	});

	function checkforNonEmptyPrevRow() {
		var tbl = document.getElementById("fiscalPeriodTable");
		var lastRow = (tbl.rows.length) - 1;
		var prevRow = (tbl.rows.length) - 2;
		var name = getControlInBranch(tbl.rows[lastRow], 'name').value;
		var startingDate = getControlInBranch(tbl.rows[lastRow], 'startDate').value;
		var endingDate = getControlInBranch(tbl.rows[lastRow], 'endDate').value;
		var finYearEndDate = document.getElementById("endingDate").value;
		if (name == '' || startingDate == '' || endingDate == '') {
			bootbox.alert("Enter all values for existing rows before adding.");
			return false;
		}
		var currDate = new Date();
		var currentDate = currDate.getDate() + "/" + (currDate.getMonth() + 1)
				+ "/" + currDate.getFullYear();
		if (prevRow > 0) {
			var prevEndDate = getControlInBranch(tbl.rows[prevRow], 'endDate').value;
			if (compareDate(formatDate6(prevEndDate), formatDate6(startingDate)) == -1) {
				bootbox.alert('Enter valid Start date');
				getControlInBranch(tbl.rows[lastRow], 'startDate').value = '';
				getControlInBranch(tbl.rows[lastRow], 'startDate').focus();
				return false;
			}
		}
		/*To check whether fiscal End date and financial end date are equal*/
		if (endingDate != finYearEndDate) {
			if (compareDate(formatDate6(endingDate),
					formatDate6(finYearEndDate)) == -1) {
				bootbox.alert('Enter valid End date');
				getControlInBranch(tbl.rows[lastRow], 'endDate').value = '';
				getControlInBranch(tbl.rows[lastRow], 'endDate').focus();
				return false;
			}
		}
		return true;
	}
</script>
<script type="text/javascript"
	src="<c:url value='/resources/app/js/cFinancialYearHelper.js'/>"></script>