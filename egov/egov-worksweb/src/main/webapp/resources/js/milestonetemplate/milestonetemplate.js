function replaceStatus() {
	var status = document.getElementById('status').innerHTML;
	if(status == 1)
		status = status.replace("1", "ACTIVE");
	status = status.replace("0", "INACTIVE");
    document.getElementById("status").innerHTML = status;
}

function modifyMilestoneTemplateData() {
	var id = document.getElementById('id').value;
	window.location = 'milestoneTemplate-edit.action?mode=edit&id='+id;
}

function createNewMilestoneTemplate() {
	window.location = "milestoneTemplate-newform.action";
}

function validateFormBeforeSubmit() {
		if (document.getElementById("code").value == '') {
	    	var message = document.getElementById('templateCode').value;
	        showMessage('milestonetemplateerror', message);
	        return false;
	
	    }
        if (document.getElementById("name").value == '') {
        	var message = document.getElementById('templateName').value;
            showMessage('milestonetemplateerror', message);
            return false;

        }
        if (document.getElementById("description").value == '') {
        	var message = document.getElementById('templateDesc').value;
            showMessage('milestonetemplateerror', message);
            return false;
        }
        if (document.getElementById("typeOfWork").value == '' || document.getElementById("typeOfWork").value == '-1') {
        	var message = document.getElementById('selectTypeOfWork').value;
            showMessage('milestonetemplateerror', message);
        	return false;
        }
    return true;
}

function modifyMilestoneTemplate(){
	var id=document.getElementById('selectedCode').value;
	if (id == '' || id == null) {
		var message = document.getElementById('selectMilestoneTemplate').value;
        showMessage('milestonetemplateerror', message);
        window.scrollTo(0, 0);
    } else{
	   window.open("milestoneTemplate-edit.action?mode=edit&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
}

function viewMilestoneTemplate(){
	var id=document.getElementById('selectedCode').value;
	if (id == '' || id == null) {
		var message = document.getElementById('selectMilestoneTemplate').value;
        showMessage('milestonetemplateerror', message);
		window.scrollTo(0, 0);
    } else{
	window.open("milestoneTemplate-edit.action?mode=view&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
}
