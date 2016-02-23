<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form role="form" action="../update" modelAttribute="recovery"
	id="recoveryform" cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<%@ include file="recovery-form.jsp"%>
	<input type="hidden" name="recovery" value="${recovery.id}" />
	<input type="hidden" name="chartofaccountsid" id="chartofaccountsid"
		value="${recovery.chartofaccounts.id}" />

	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
</form:form>
<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
	if ($("#bankLoan").val() && $('select#bank option:selected').val())
		if ($("#bankDiv").hasClass("display-hide"))
			$('#bankDiv').removeClass('display-hide');
	$("#type").attr('readonly', 'readonly');
	$("#egPartytype").attr('disabled', 'disabled');
	$('#chartofaccounts').val($("#chartofaccountsid").val()).attr("selected",
			"selected");
	$("#chartofaccounts").attr('disabled', 'disabled');
</script>

<script type="text/javascript"
	src="<c:url value='/resources/app/js/recoveryHelper.js'/>"></script>