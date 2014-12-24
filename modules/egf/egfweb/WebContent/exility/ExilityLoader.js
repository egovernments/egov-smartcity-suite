
//Must be included in onload script for document. Locates and loads Pagemanager
//Once this is called, you should also include resetPageManager on pageunload

function loadPageManager(){
	var win = window;
	while (true) { //not going to be an infinite loop
		if (win.PageManager){
			window.exilWindowSaved = win.exilWindow;
			win.exilWindow = window;
			window.pageManagerWindow = win;
			window.PageManager = win.PageManager;
			window.PageValidator = win.PageValidator;
			return;
		}
		if (win.opener)win = win.opener;
		else if (win.parent && win.parent != win) win = win.parent;
		else {
			var script;
			var head = document.getElementsByTagName('head')[0];
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/ExilityParameters.js';
			head.appendChild(script);
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/PageManager.js';
			head.appendChild(script);
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/PageValidator.js';
			head.appendChild(script);
alert('You had not loaded Exility. Loading them as I show this. Take few secods to press OK so that Exility gets time to get loaded');

			return;
		}
	}
}

function unloadPageManager(){
	if (window.pageManagerWindow && window.exilWindowSaved){
		window.pageManagerWindow.exilWindow = window.exilWindowSaved
	}
}