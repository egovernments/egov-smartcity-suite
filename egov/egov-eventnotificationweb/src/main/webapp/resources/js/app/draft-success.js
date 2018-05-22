$("#buttonClose").click(function(event){
		window.opener.location.reload();
		self.close();
		return true;
	});