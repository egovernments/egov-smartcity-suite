/*
	HelpSmith Web Help System 1.4
	Copyright (c) 2008-2009 Divcom Software
	http://www.helpsmith.com/
*/

function lookupAlphaIndex(ch) {
	ch = ch.toLowerCase();
	for (var i = 0; i < gAlphaIndex.length; i++) {
		if (ch == gAlphaIndex[i][0]) {
			return i;
		}
	}
	return -1;
}

function splitSearchQuery(query) {
	query = query.replace(/(\s+)/g, "\x20");
	query = query.replace(/^\s+|\s+$/g, "");
	return query.split("\x20");
}

function mergeTopicIndexes(source, dest) {

	function valueExists(arr, value) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == value) return true;
		}
		return false;
	}
	
	var value;
	for (var i = 0; i < source.length; i++) {
		value = source[i];
		if (!valueExists(dest, value)) {
			dest[dest.length] = value;
		}
	}	
}

function findByWord(word) {	
	if (word.length == 0) return null;
	var idx = lookupAlphaIndex(word.charAt(0));	
	if (idx < 0) return null;
	var start = gAlphaIndex[idx][1];
	var end = gAlphaIndex[idx][2];
	var list = [];
	for (var i = start; i <= end; i++) {
		var s = gWordList[i][0].substr(0, word.length);	
		if (s == word.toLowerCase()) {			
			mergeTopicIndexes(gWordList[i][1], list);
		}
	}
	return list;
}

function findByQuery(query) {
	var q = splitSearchQuery(query);
	if (q.length == 0) return null;
	
	var topicIndexes;
	var idxList = [];
	for (var i = 0; i < q.length; i++) {
		topicIndexes = findByWord(q[i]);
		if (topicIndexes) {
			idxList[idxList.length] = topicIndexes;
		}
	}
	if (idxList.length == 0) return null;
	
	var resIndexes = [];
	for (var i = 0; i < idxList.length; i++) {
		mergeTopicIndexes(idxList[i], resIndexes);
	}
	return resIndexes;
}

function searchInfo(title, topicRef) {
	this.title = title;
	this.topicRef = topicRef;	
}

function getSearchInfoPos(list, info) {
	var l = 0;
	var h = list.length - 1;
	var i, c;
	while (l <= h) {
		i = (l + h) >> 1;
		c = compareStrings(list[i].title, info.title);
		if (c < 0) {
			l = i + 1;
		}
		else {
			h = i - 1;
		}
	}
	return l;
}

function search(query) {
	var list = [];
	var topicIndexes = findByQuery(query);
	if (topicIndexes && topicIndexes.length) {
		var topicInfo, infoPos, info;
		for (var i = 0; i < topicIndexes.length; i++) {
			topicInfo = gTopicInfo[topicIndexes[i]];
			info = new searchInfo(topicInfo[0], topicInfo[1]);
			infoPos = getSearchInfoPos(list, info);
			list.splice(infoPos, 0, info);
		}
	}
	return list;
}

function searchAndFill(query, select) {
	select.length = 0;
        var list = search(query);
	if (!list.length) {
		alert(whTextLabels.NoTopicsFoundMsg);
	}
	else {            		
		for (var i = 0; i < list.length; i++) {
			var o = document.createElement("option");
			o.text = list[i].title;
			o.value = list[i].topicRef;
			select[select.length] = o;
		}
	}
}

function openTopicFromSelect(select) {
	open(select.options[select.selectedIndex].value, 'topic');
}
