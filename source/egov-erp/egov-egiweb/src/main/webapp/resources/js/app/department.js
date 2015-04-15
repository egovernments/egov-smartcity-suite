$("#createDept").click(
		function() {
			$("#departmentViewForm").attr("method", "get");
			$("#departmentViewForm").attr("action",	"/egi/department/create");
		})

$("#editDept").click(function() {
	var url = "/egi/department/update/" + $("#deptName").val();
	$("#departmentViewForm").attr("method", "get");
	$("#departmentViewForm").attr("action", url);
})