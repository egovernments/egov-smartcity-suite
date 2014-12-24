var isIE = navigator.userAgent.indexOf("MSIE")!= -1;

function nsAreaInit() {
	window.onresize = wndResize;
	wndResize();	
}

function getWndClientHeight() {
	var height = 0;
	if (self.innerHeight) { // Mozilla/Netscape
		height = self.innerHeight;
	}	
	else if (document.body) { // IE
		height = document.body.clientHeight;
	}	
	return height;
}

function wndResize() {
	var nsArea = document.getElementById('nsArea');
	if (!nsArea) return;
	var container = document.getElementById('container');
	if (!container) return;		

	
	var nsAreaHeight = nsArea.offsetHeight;
	var wndClientHeight = getWndClientHeight();
	if (wndClientHeight < nsAreaHeight) {
		wndClientHeight = nsAreaHeight + 1;
	}
	var containerHeight = wndClientHeight - nsAreaHeight;
	container.style.height = containerHeight + 'px';

	var content = document.getElementById('content');
	if (content && !isIE) {
		var back = document.getElementById('back');
		if (back) {	
			back.style.height = ((content.clientHeight > containerHeight) ? 
				content.clientHeight : containerHeight) + 'px';
		}
	}
}