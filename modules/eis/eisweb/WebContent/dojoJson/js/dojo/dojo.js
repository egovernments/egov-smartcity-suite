/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

var dj_global=this;
function dj_undef(_1,_2){
if(!_2){
_2=dj_global;
}
return (typeof _2[_1]=="undefined");
}
if(dj_undef("djConfig")){
var djConfig={};
}
var dojo;
if(dj_undef("dojo")){
dojo={};
}
dojo.version={major:0,minor:2,patch:2,flag:"",revision:Number("$Rev: 2836 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalObjPath=function(_3,_4){
if(typeof _3!="string"){
return dj_global;
}
if(_3.indexOf(".")==-1){
if((dj_undef(_3,dj_global))&&(_4)){
dj_global[_3]={};
}
return dj_global[_3];
}
var _5=_3.split(/\./);
var _6=dj_global;
for(var i=0;i<_5.length;++i){
if(!_4){
_6=_6[_5[i]];
if((typeof _6=="undefined")||(!_6)){
return _6;
}
}else{
if(dj_undef(_5[i],_6)){
_6[_5[i]]={};
}
_6=_6[_5[i]];
}
}
return _6;
};
dojo.errorToString=function(_8){
return ((!dj_undef("message",_8))?_8.message:(dj_undef("description",_8)?_8:_8.description));
};
dojo.raise=function(_9,_a){
if(_a){
_9=_9+": "+dojo.errorToString(_a);
}
var he=dojo.hostenv;
if((!dj_undef("hostenv",dojo))&&(!dj_undef("println",dojo.hostenv))){
dojo.hostenv.println("FATAL: "+_9);
}
throw Error(_9);
};
dj_throw=dj_rethrow=function(m,e){
dojo.deprecated("dj_throw and dj_rethrow deprecated, use dojo.raise instead");
dojo.raise(m,e);
};
dojo.debug=function(){
if(!djConfig.isDebug){
return;
}
var _e=arguments;
if(dj_undef("println",dojo.hostenv)){
dojo.raise("dojo.debug not available (yet?)");
}
var _f=dj_global["jum"]&&!dj_global["jum"].isBrowser;
var s=[(_f?"":"DEBUG: ")];
for(var i=0;i<_e.length;++i){
if(!false&&_e[i] instanceof Error){
var msg="["+_e[i].name+": "+dojo.errorToString(_e[i])+(_e[i].fileName?", file: "+_e[i].fileName:"")+(_e[i].lineNumber?", line: "+_e[i].lineNumber:"")+"]";
}else{
try{
var msg=String(_e[i]);
}
catch(e){
if(dojo.render.html.ie){
var msg="[ActiveXObject]";
}else{
var msg="[unknown]";
}
}
}
s.push(msg);
}
if(_f){
jum.debug(s.join(" "));
}else{
dojo.hostenv.println(s.join(" "));
}
};
dojo.debugShallow=function(obj){
if(!djConfig.isDebug){
return;
}
dojo.debug("------------------------------------------------------------");
dojo.debug("Object: "+obj);
for(i in obj){
dojo.debug(i+": "+obj[i]);
}
dojo.debug("------------------------------------------------------------");
};
var dj_debug=dojo.debug;
function dj_eval(s){
return dj_global.eval?dj_global.eval(s):eval(s);
}
dj_unimplemented=dojo.unimplemented=function(_15,_16){
var _17="'"+_15+"' not implemented";
if((!dj_undef(_16))&&(_16)){
_17+=" "+_16;
}
dojo.raise(_17);
};
dj_deprecated=dojo.deprecated=function(_18,_19,_1a){
var _1b="DEPRECATED: "+_18;
if(_19){
_1b+=" "+_19;
}
if(_1a){
_1b+=" -- will be removed in version: "+_1a;
}
dojo.debug(_1b);
};
dojo.inherits=function(_1c,_1d){
if(typeof _1d!="function"){
dojo.raise("superclass: "+_1d+" borken");
}
_1c.prototype=new _1d();
_1c.prototype.constructor=_1c;
_1c.superclass=_1d.prototype;
_1c["super"]=_1d.prototype;
};
dj_inherits=function(_1e,_1f){
dojo.deprecated("dj_inherits deprecated, use dojo.inherits instead");
dojo.inherits(_1e,_1f);
};
dojo.render=(function(){
function vscaffold(_20,_21){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_20};
for(var x in _21){
tmp[x]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _24={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_24;
}else{
for(var _25 in _24){
if(typeof djConfig[_25]=="undefined"){
djConfig[_25]=_24[_25];
}
}
}
var djc=djConfig;
function _def(obj,_28,def){
return (dj_undef(_28,obj)?def:obj[_28]);
}
return {name_:"(unset)",version_:"(unset)",pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_2a,_2b){
this.modulePrefixes_[_2a]={name:_2a,value:_2b};
},getModulePrefix:function(_2c){
var mp=this.modulePrefixes_;
if((mp[_2c])&&(mp[_2c]["name"])){
return mp[_2c].value;
}
return _2c;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
},getLibraryScriptUri:function(){
dojo.unimplemented("getLibraryScriptUri","");
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
var _30=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
dojo.hostenv.setBaseScriptUri=function(uri){
djConfig.baseScriptUri=uri;
};
dojo.hostenv.loadPath=function(_32,_33,cb){
if((_32.charAt(0)=="/")||(_32.match(/^\w+:/))){
dojo.raise("relpath '"+_32+"'; must be relative");
}
var uri=this.getBaseScriptUri()+_32;
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return ((!_33)?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_33,cb));
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return;
}
var _38=this.getText(uri,null,true);
if(_38==null){
return 0;
}
this.loadedUris[uri]=true;
var _39=dj_eval(_38);
return 1;
};
dojo.hostenv.loadUriAndCheck=function(uri,_3b,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return ((ok)&&(this.findModule(_3b,false)))?true:false;
};
dojo.loaded=function(){
};
dojo.hostenv.loaded=function(){
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
dojo.loaded();
};
dojo.addOnLoad=function(obj,_41){
if(arguments.length==1){
dojo.hostenv.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dojo.hostenv.modulesLoadedListeners.push(function(){
obj[_41]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
}
};
dojo.hostenv.moduleLoaded=function(_42){
var _43=dojo.evalObjPath((_42.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_42)).toLowerCase()]=_43;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_44,_45,_46){
if(!_44){
return;
}
_46=this._global_omit_module_check||_46;
var _47=this.findModule(_44,false);
if(_47){
return _47;
}
if(dj_undef(_44,this.loading_modules_)){
this.addedToLoadingCount.push(_44);
}
this.loading_modules_[_44]=1;
var _48=_44.replace(/\./g,"/")+".js";
var _49=_44.split(".");
var _4a=_44.split(".");
for(var i=_49.length-1;i>0;i--){
var _4c=_49.slice(0,i).join(".");
var _4d=this.getModulePrefix(_4c);
if(_4d!=_4c){
_49.splice(0,i,_4d);
break;
}
}
var _4e=_49[_49.length-1];
if(_4e=="*"){
_44=(_4a.slice(0,-1)).join(".");
while(_49.length){
_49.pop();
_49.push(this.pkgFileName);
_48=_49.join("/")+".js";
if(_48.charAt(0)=="/"){
_48=_48.slice(1);
}
ok=this.loadPath(_48,((!_46)?_44:null));
if(ok){
break;
}
_49.pop();
}
}else{
_48=_49.join("/")+".js";
_44=_4a.join(".");
var ok=this.loadPath(_48,((!_46)?_44:null));
if((!ok)&&(!_45)){
_49.pop();
while(_49.length){
_48=_49.join("/")+".js";
ok=this.loadPath(_48,((!_46)?_44:null));
if(ok){
break;
}
_49.pop();
_48=_49.join("/")+"/"+this.pkgFileName+".js";
if(_48.charAt(0)=="/"){
_48=_48.slice(1);
}
ok=this.loadPath(_48,((!_46)?_44:null));
if(ok){
break;
}
}
}
if((!ok)&&(!_46)){
dojo.raise("Could not load '"+_44+"'; last tried '"+_48+"'");
}
}
if(!_46){
_47=this.findModule(_44,false);
if(!_47){
dojo.raise("symbol '"+_44+"' is not defined after loading '"+_48+"'");
}
}
return _47;
};
dojo.hostenv.startPackage=function(_50){
var _51=_50.split(/\./);
if(_51[_51.length-1]=="*"){
_51.pop();
}
return dojo.evalObjPath(_51.join("."),true);
};
dojo.hostenv.findModule=function(_52,_53){
var lmn=(new String(_52)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _55=dojo.evalObjPath(_52);
if((_52)&&(typeof _55!="undefined")&&(_55)){
this.loaded_modules_[lmn]=_55;
return _55;
}
if(_53){
dojo.raise("no loaded module named '"+_52+"'");
}
return null;
};
if(typeof window=="undefined"){
dojo.raise("no window object");
}
(function(){
if(djConfig.allowQueryConfig){
var _56=document.location.toString();
var _57=_56.split("?",2);
if(_57.length>1){
var _58=_57[1];
var _59=_58.split("&");
for(var x in _59){
var sp=_59[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _5d=document.getElementsByTagName("script");
var _5e=/(__package__|dojo)\.js([\?\.]|$)/i;
for(var i=0;i<_5d.length;i++){
var src=_5d[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_5e);
if(m){
root=src.substring(0,m.index);
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=root;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=root;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var dua=drh.UA=navigator.userAgent;
var dav=drh.AV=navigator.appVersion;
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _68=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_68>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_68+6,_68+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
dr.vml.capable=drh.ie;
dr.svg.capable=f;
dr.svg.support.plugin=f;
dr.svg.support.builtin=f;
dr.svg.adobe=f;
if(document.implementation&&document.implementation.hasFeature&&document.implementation.hasFeature("org.w3c.dom.svg","1.0")){
dr.svg.capable=t;
dr.svg.support.builtin=t;
dr.svg.support.plugin=f;
dr.svg.adobe=f;
}else{
if(navigator.mimeTypes&&navigator.mimeTypes.length>0){
var _69=navigator.mimeTypes["image/svg+xml"]||navigator.mimeTypes["image/svg"]||navigator.mimeTypes["image/svg-xml"];
if(_69){
dr.svg.adobe=_69&&_69.enabledPlugin&&_69.enabledPlugin.description&&(_69.enabledPlugin.description.indexOf("Adobe")>-1);
if(dr.svg.adobe){
dr.svg.capable=t;
dr.svg.support.plugin=t;
}
}
}else{
if(drh.ie&&dr.os.win){
var _69=f;
try{
var _6a=new ActiveXObject("Adobe.SVGCtl");
_69=t;
}
catch(e){
}
if(_69){
dr.svg.capable=t;
dr.svg.support.plugin=t;
dr.svg.adobe=t;
}
}else{
dr.svg.capable=f;
dr.svg.support.plugin=f;
dr.svg.adobe=f;
}
}
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
var DJ_XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _6b=null;
var _6c=null;
try{
_6b=new XMLHttpRequest();
}
catch(e){
}
if(!_6b){
for(var i=0;i<3;++i){
var _6e=DJ_XMLHTTP_PROGIDS[i];
try{
_6b=new ActiveXObject(_6e);
}
catch(e){
_6c=e;
}
if(_6b){
DJ_XMLHTTP_PROGIDS=[_6e];
break;
}
}
}
if(!_6b){
return dojo.raise("XMLHTTP not available",_6c);
}
return _6b;
};
dojo.hostenv.getText=function(uri,_70,_71){
var _72=this.getXmlhttpObject();
if(_70){
_72.onreadystatechange=function(){
if((4==_72.readyState)&&(_72["status"])){
if(_72.status==200){
_70(_72.responseText);
}
}
};
}
_72.open("GET",uri,_70?true:false);
_72.send(null);
if(_70){
return null;
}
return _72.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_73){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_73);
}else{
try{
var _74=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_74){
_74=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_73));
_74.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_73+"</div>");
}
catch(e2){
window.status=_73;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_76,_77,fp,_79){
var _7a=_76["on"+_77]||function(){
};
_76["on"+_77]=function(){
fp.apply(_76,arguments);
_7a.apply(_76,arguments);
};
return true;
}
dj_addNodeEvtHdlr(window,"load",function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
dojo.hostenv.modulesLoaded();
});
dojo.hostenv.makeWidgets=function(){
var _7b=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_7b=_7b.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_7b=_7b.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_7b.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
try{
var _7c=new dojo.xml.Parse();
if(_7b.length>0){
for(var x=0;x<_7b.length;x++){
var _7e=document.getElementById(_7b[x]);
if(!_7e){
continue;
}
var _7f=_7c.parseElement(_7e,null,true);
dojo.widget.getParser().createComponents(_7f);
}
}else{
if(djConfig.parseWidgets){
var _7f=_7c.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_7f);
}
}
}
catch(e){
dojo.debug("auto-build-widgets error:",e);
}
}
}
};
dojo.hostenv.modulesLoadedListeners.push(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.write("<style>v:*{ behavior:url(#default#VML); }</style>");
document.write("<xml:namespace ns=\"urn:schemas-microsoft-com:vml\" prefix=\"v\"/>");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
dojo.hostenv.byId=dojo.byId=function(id,doc){
if(typeof id=="string"||id instanceof String){
if(!doc){
doc=document;
}
return doc.getElementById(id);
}
return id;
};
dojo.hostenv.byIdArray=dojo.byIdArray=function(){
var ids=[];
for(var i=0;i<arguments.length;i++){
if((arguments[i] instanceof Array)||(typeof arguments[i]=="array")){
for(var j=0;j<arguments[i].length;j++){
ids=ids.concat(dojo.hostenv.byIdArray(arguments[i][j]));
}
}else{
ids.push(dojo.hostenv.byId(arguments[i]));
}
}
return ids;
};
dojo.hostenv.conditionalLoadModule=function(_85){
var _86=_85["common"]||[];
var _87=(_85[dojo.hostenv.name_])?_86.concat(_85[dojo.hostenv.name_]||[]):_86.concat(_85["default"]||[]);
for(var x=0;x<_87.length;x++){
var _89=_87[x];
if(_89.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_89);
}else{
dojo.hostenv.loadModule(_89);
}
}
};
dojo.hostenv.require=dojo.hostenv.loadModule;
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireAfter=dojo.require;
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(dojo.render[arguments[0]].capable)){
var _8a=[];
for(var i=1;i<arguments.length;i++){
_8a.push(arguments[i]);
}
dojo.require.apply(dojo,_8a);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.conditionalRequire=dojo.requireIf;
dojo.kwCompoundRequire=function(){
dojo.hostenv.conditionalLoadModule.apply(dojo.hostenv,arguments);
};
dojo.hostenv.provide=dojo.hostenv.startPackage;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_8c,_8d){
return dojo.hostenv.setModulePrefix(_8c,_8d);
};
dojo.profile={start:function(){
},end:function(){
},dump:function(){
}};
dojo.exists=function(obj,_8f){
var p=_8f.split(".");
for(var i=0;i<p.length;i++){
if(!(obj[p[i]])){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.provide("dojo.lang");
dojo.provide("dojo.AdapterRegistry");
dojo.provide("dojo.lang.Lang");
dojo.lang.mixin=function(obj,_93){
var _94={};
for(var x in _93){
if(typeof _94[x]=="undefined"||_94[x]!=_93[x]){
obj[x]=_93[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_93["toString"])&&_93["toString"]!=obj["toString"]){
obj.toString=_93.toString;
}
return obj;
};
dojo.lang.extend=function(_96,_97){
this.mixin(_96.prototype,_97);
};
dojo.lang.extendPrototype=function(obj,_99){
this.extend(obj.constructor,_99);
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_9a,_9b){
var nso=(_9b||dojo.lang.anon);
if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true)){
for(var x in nso){
if(nso[x]===_9a){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_9a;
return ret;
};
dojo.lang.hitch=function(_9f,_a0){
if(dojo.lang.isString(_a0)){
var fcn=_9f[_a0];
}else{
var fcn=_a0;
}
return function(){
return fcn.apply(_9f,arguments);
};
};
dojo.lang.forward=function(_a2){
return function(){
return this[_a2].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,_a4){
var _a5=[];
ns=ns||dj_global;
if(dojo.lang.isString(_a4)){
_a4=ns[_a4];
}
for(var x=2;x<arguments.length;x++){
_a5.push(arguments[x]);
}
var _a7=_a4.length-_a5.length;
function gather(_a8,_a9,_aa){
var _ab=_aa;
var _ac=_a9.slice(0);
for(var x=0;x<_a8.length;x++){
_ac.push(_a8[x]);
}
_aa=_aa-_a8.length;
if(_aa<=0){
var res=_a4.apply(ns,_ac);
_aa=_ab;
return res;
}else{
return function(){
return gather(arguments,_ac,_aa);
};
}
}
return gather([],_a5,_a7);
};
dojo.lang.curryArguments=function(ns,_b0,_b1,_b2){
var _b3=[];
var x=_b2||0;
for(x=_b2;x<_b1.length;x++){
_b3.push(_b1[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,_b0].concat(_b3));
};
dojo.lang.setTimeout=function(_b5,_b6){
var _b7=window,argsStart=2;
if(!dojo.lang.isFunction(_b5)){
_b7=_b5;
_b5=_b6;
_b6=arguments[2];
argsStart++;
}
if(dojo.lang.isString(_b5)){
_b5=_b7[_b5];
}
var _b8=[];
for(var i=argsStart;i<arguments.length;i++){
_b8.push(arguments[i]);
}
return setTimeout(function(){
_b5.apply(_b7,_b8);
},_b6);
};
dojo.lang.isObject=function(wh){
return typeof wh=="object"||dojo.lang.isArray(wh)||dojo.lang.isFunction(wh);
};
dojo.lang.isArray=function(wh){
return (wh instanceof Array||typeof wh=="array");
};
dojo.lang.isArrayLike=function(wh){
if(dojo.lang.isString(wh)){
return false;
}
if(dojo.lang.isArray(wh)){
return true;
}
if(typeof wh!="undefined"&&wh&&dojo.lang.isNumber(wh.length)&&isFinite(wh.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(wh){
return (wh instanceof Function||typeof wh=="function");
};
dojo.lang.isString=function(wh){
return (wh instanceof String||typeof wh=="string");
};
dojo.lang.isAlien=function(wh){
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(wh));
};
dojo.lang.isBoolean=function(wh){
return (wh instanceof Boolean||typeof wh=="boolean");
};
dojo.lang.isNumber=function(wh){
return (wh instanceof Number||typeof wh=="number");
};
dojo.lang.isUndefined=function(wh){
return ((wh==undefined)&&(typeof wh=="undefined"));
};
dojo.lang.whatAmI=function(wh){
try{
if(dojo.lang.isArray(wh)){
return "array";
}
if(dojo.lang.isFunction(wh)){
return "function";
}
if(dojo.lang.isString(wh)){
return "string";
}
if(dojo.lang.isNumber(wh)){
return "number";
}
if(dojo.lang.isBoolean(wh)){
return "boolean";
}
if(dojo.lang.isAlien(wh)){
return "alien";
}
if(dojo.lang.isUndefined(wh)){
return "undefined";
}
for(var _c4 in dojo.lang.whatAmI.custom){
if(dojo.lang.whatAmI.custom[_c4](wh)){
return _c4;
}
}
if(dojo.lang.isObject(wh)){
return "object";
}
}
catch(E){
}
return "unknown";
};
dojo.lang.whatAmI.custom={};
dojo.lang.find=function(arr,val,_c7){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _c9=dojo.lang.isString(arr);
if(_c9){
arr=arr.split("");
}
if(_c7){
for(var i=0;i<arr.length;++i){
if(arr[i]===val){
return i;
}
}
}else{
for(var i=0;i<arr.length;++i){
if(arr[i]==val){
return i;
}
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_cd){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _cf=dojo.lang.isString(arr);
if(_cf){
arr=arr.split("");
}
if(_cd){
for(var i=arr.length-1;i>=0;i--){
if(arr[i]===val){
return i;
}
}
}else{
for(var i=arr.length-1;i>=0;i--){
if(arr[i]==val){
return i;
}
}
}
return -1;
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(arr,val){
return dojo.lang.find(arr,val)>-1;
};
dojo.lang.getNameInObj=function(ns,_d4){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_d4){
return new String(x);
}
}
return null;
};
dojo.lang.has=function(obj,_d7){
return (typeof obj[_d7]!=="undefined");
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _da=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_da++;
break;
}
}
return (_da==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.forEach=function(arr,_dd,_de){
var _df=dojo.lang.isString(arr);
if(_df){
arr=arr.split("");
}
var il=arr.length;
for(var i=0;i<((_de)?il:arr.length);i++){
if(_dd(arr[i],i,arr)=="break"){
break;
}
}
};
dojo.lang.map=function(arr,obj,_e4){
var _e5=dojo.lang.isString(arr);
if(_e5){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_e4)){
_e4=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_e4){
var _e6=obj;
obj=_e4;
_e4=_e6;
}
}
if(Array.map){
var _e7=Array.map(arr,_e4,obj);
}else{
var _e7=[];
for(var i=0;i<arr.length;++i){
_e7.push(_e4.call(obj,arr[i]));
}
}
if(_e5){
return _e7.join("");
}else{
return _e7;
}
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(_eb,cb,_ed,_ee){
if(!_eb.length){
if(typeof _ee=="function"){
_ee();
}
return;
}
if((typeof _ed=="undefined")&&(typeof cb=="number")){
_ed=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_ed){
_ed=0;
}
}
}
setTimeout(function(){
(_eb.shift())();
cb();
dojo.lang.delayThese(_eb,cb,_ed,_ee);
},_ed);
};
dojo.lang.shallowCopy=function(obj){
var ret={},key;
for(key in obj){
if(dojo.lang.isUndefined(ret[key])){
ret[key]=obj[key];
}
}
return ret;
};
dojo.lang.every=function(arr,_f2,_f3){
var _f4=dojo.lang.isString(arr);
if(_f4){
arr=arr.split("");
}
if(Array.every){
return Array.every(arr,_f2,_f3);
}else{
if(!_f3){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_f3=dj_global;
}
for(var i=0;i<arr.length;i++){
if(!_f2.call(_f3,arr[i],i,arr)){
return false;
}
}
return true;
}
};
dojo.lang.some=function(arr,_f7,_f8){
var _f9=dojo.lang.isString(arr);
if(_f9){
arr=arr.split("");
}
if(Array.some){
return Array.some(arr,_f7,_f8);
}else{
if(!_f8){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_f8=dj_global;
}
for(var i=0;i<arr.length;i++){
if(_f7.call(_f8,arr[i],i,arr)){
return true;
}
}
return false;
}
};
dojo.lang.filter=function(arr,_fc,_fd){
var _fe=dojo.lang.isString(arr);
if(_fe){
arr=arr.split("");
}
if(Array.filter){
var _ff=Array.filter(arr,_fc,_fd);
}else{
if(!_fd){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_fd=dj_global;
}
var _ff=[];
for(var i=0;i<arr.length;i++){
if(_fc.call(_fd,arr[i],i,arr)){
_ff.push(arr[i]);
}
}
}
if(_fe){
return _ff.join("");
}else{
return _ff;
}
};
dojo.AdapterRegistry=function(){
this.pairs=[];
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_102,wrap,_104){
if(_104){
this.pairs.unshift([name,_102,wrap]);
}else{
this.pairs.push([name,_102,wrap]);
}
},match:function(){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[1].apply(this,arguments)){
return pair[2].apply(this,arguments);
}
}
throw new Error("No match found");
},unregister:function(name){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[0]==name){
this.pairs.splice(i,1);
return true;
}
}
return false;
}});
dojo.lang.reprRegistry=new dojo.AdapterRegistry();
dojo.lang.registerRepr=function(name,_10b,wrap,_10d){
dojo.lang.reprRegistry.register(name,_10b,wrap,_10d);
};
dojo.lang.repr=function(obj){
if(typeof (obj)=="undefined"){
return "undefined";
}else{
if(obj===null){
return "null";
}
}
try{
if(typeof (obj["__repr__"])=="function"){
return obj["__repr__"]();
}else{
if((typeof (obj["repr"])=="function")&&(obj.repr!=arguments.callee)){
return obj["repr"]();
}
}
return dojo.lang.reprRegistry.match(obj);
}
catch(e){
if(typeof (obj.NAME)=="string"&&(obj.toString==Function.prototype.toString||obj.toString==Object.prototype.toString)){
return o.NAME;
}
}
if(typeof (obj)=="function"){
obj=(obj+"").replace(/^\s+/,"");
var idx=obj.indexOf("{");
if(idx!=-1){
obj=obj.substr(0,idx)+"{...}";
}
}
return obj+"";
};
dojo.lang.reprArrayLike=function(arr){
try{
var na=dojo.lang.map(arr,dojo.lang.repr);
return "["+na.join(", ")+"]";
}
catch(e){
}
};
dojo.lang.reprString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.lang.reprNumber=function(num){
return num+"";
};
(function(){
var m=dojo.lang;
m.registerRepr("arrayLike",m.isArrayLike,m.reprArrayLike);
m.registerRepr("string",m.isString,m.reprString);
m.registerRepr("numbers",m.isNumber,m.reprNumber);
m.registerRepr("boolean",m.isBoolean,m.reprNumber);
})();
dojo.lang.unnest=function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.toArray=function(_119,_11a){
var _11b=[];
for(var i=_11a||0;i<_119.length;i++){
_11b.push(_119[i]);
}
return _11b;
};
dojo.provide("dojo.dom");
dojo.require("dojo.lang");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=dojo.lang.isDomNode=function(wh){
if(typeof Element=="object"){
try{
return wh instanceof Element;
}
catch(E){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.lang.whatAmI.custom["node"]=dojo.dom.isNode;
dojo.dom.getTagName=function(node){
var _11f=node.tagName;
if(_11f.substr(0,5).toLowerCase()!="dojo:"){
if(_11f.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_11f.substring(4).toLowerCase();
}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((node.getAttributeNS)&&(node.getAttributeNS(this.dojoml,"type"))){
return "dojo:"+node.getAttributeNS(this.dojoml,"type").toLowerCase();
}
try{
djt=node.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((!dj_global["djConfig"])||(!djConfig["ignoreClassNames"])){
var _121=node.className||node.getAttribute("class");
if((_121)&&(_121.indexOf)&&(_121.indexOf("dojo-")!=-1)){
var _122=_121.split(" ");
for(var x=0;x<_122.length;x++){
if((_122[x].length>5)&&(_122[x].indexOf("dojo-")>=0)){
return "dojo:"+_122[x].substr(5).toLowerCase();
}
}
}
}
}
return _11f.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_125,_126){
var node=_125.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_126&&node&&node.tagName&&node.tagName.toLowerCase()!=_126.toLowerCase()){
node=dojo.dom.nextElement(node,_126);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_128,_129){
var node=_128.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_129&&node&&node.tagName&&node.tagName.toLowerCase()!=_129.toLowerCase()){
node=dojo.dom.prevElement(node,_129);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_12c){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_12c&&_12c.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_12c);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_12e){
if(!node){
return null;
}
if(_12e){
_12e=_12e.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_12e&&_12e.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_12e);
}
return node;
};
dojo.dom.moveChildren=function(_12f,_130,trim){
var _132=0;
if(trim){
while(_12f.hasChildNodes()&&_12f.firstChild.nodeType==dojo.dom.TEXT_NODE){
_12f.removeChild(_12f.firstChild);
}
while(_12f.hasChildNodes()&&_12f.lastChild.nodeType==dojo.dom.TEXT_NODE){
_12f.removeChild(_12f.lastChild);
}
}
while(_12f.hasChildNodes()){
_130.appendChild(_12f.firstChild);
_132++;
}
return _132;
};
dojo.dom.copyChildren=function(_133,_134,trim){
var _136=_133.cloneNode(true);
return this.moveChildren(_136,_134,trim);
};
dojo.dom.removeChildren=function(node){
var _138=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _138;
};
dojo.dom.replaceChildren=function(node,_13a){
dojo.dom.removeChildren(node);
node.appendChild(_13a);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_13d,_13e){
var _13f=[];
var _140=dojo.lang.isFunction(_13d);
while(node){
if(!_140||_13d(node)){
_13f.push(node);
}
if(_13e&&_13f.length>0){
return _13f[0];
}
node=node.parentNode;
}
if(_13e){
return null;
}
return _13f;
};
dojo.dom.getAncestorsByTag=function(node,tag,_143){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_143);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_148,_149){
if(_149&&node){
node=node.parentNode;
}
while(node){
if(node==_148){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
};
dojo.dom.createDocumentFromText=function(str,_14c){
if(!_14c){
_14c="text/xml";
}
if(typeof DOMParser!="undefined"){
var _14d=new DOMParser();
return _14d.parseFromString(str,_14c);
}else{
if(typeof ActiveXObject!="undefined"){
var _14e=new ActiveXObject("Microsoft.XMLDOM");
if(_14e){
_14e.async=false;
_14e.loadXML(str);
return _14e;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _150=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_150.importNode(tmp.childNodes.item(i),true);
}
return _150;
}
return tmp.document&&tmp.document.firstChild?tmp.document.firstChild:tmp;
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_153){
if(_153.firstChild){
_153.insertBefore(node,_153.firstChild);
}else{
_153.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_156){
if(_156!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _157=ref.parentNode;
_157.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_15a){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_15a!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_15a);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_15e){
if((!node)||(!ref)||(!_15e)){
return false;
}
switch(_15e.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_160,_161){
var _162=_160.childNodes;
if(!_162.length){
_160.appendChild(node);
return true;
}
var _163=null;
for(var i=0;i<_162.length;i++){
var _165=_162.item(i)["getAttribute"]?parseInt(_162.item(i).getAttribute("dojoinsertionindex")):-1;
if(_165<_161){
_163=_162.item(i);
}
}
if(_163){
return dojo.dom.insertAfter(node,_163);
}else{
return dojo.dom.insertBefore(node,_162.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _168="";
if(node==null){
return _168;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_168+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_168+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _168;
}
};
dojo.dom.collectionToArray=function(_16a){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead");
return dojo.lang.toArray(_16a);
};
dojo.dom.hasParent=function(node){
if(!node||!node.parentNode||(node.parentNode&&!node.parentNode.tagName)){
return false;
}
return true;
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
var arr=dojo.lang.toArray(arguments,1);
return arr[dojo.lang.find(node.tagName,arr)]||"";
}
return "";
};
dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");
dojo.xml.Parse=function(){
this.parseFragment=function(_16e){
var _16f={};
var _170=dojo.dom.getTagName(_16e);
_16f[_170]=new Array(_16e.tagName);
var _171=this.parseAttributes(_16e);
for(var attr in _171){
if(!_16f[attr]){
_16f[attr]=[];
}
_16f[attr][_16f[attr].length]=_171[attr];
}
var _173=_16e.childNodes;
for(var _174 in _173){
switch(_173[_174].nodeType){
case dojo.dom.ELEMENT_NODE:
_16f[_170].push(this.parseElement(_173[_174]));
break;
case dojo.dom.TEXT_NODE:
if(_173.length==1){
if(!_16f[_16e.tagName]){
_16f[_170]=[];
}
_16f[_170].push({value:_173[0].nodeValue});
}
break;
}
}
return _16f;
};
this.parseElement=function(node,_176,_177,_178){
var _179={};
var _17a=dojo.dom.getTagName(node);
_179[_17a]=[];
if((!_177)||(_17a.substr(0,4).toLowerCase()=="dojo")){
var _17b=this.parseAttributes(node);
for(var attr in _17b){
if((!_179[_17a][attr])||(typeof _179[_17a][attr]!="array")){
_179[_17a][attr]=[];
}
_179[_17a][attr].push(_17b[attr]);
}
_179[_17a].nodeRef=node;
_179.tagName=_17a;
_179.index=_178||0;
}
var _17d=0;
for(var i=0;i<node.childNodes.length;i++){
var tcn=node.childNodes.item(i);
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
_17d++;
var ctn=dojo.dom.getTagName(tcn);
if(!_179[ctn]){
_179[ctn]=[];
}
_179[ctn].push(this.parseElement(tcn,true,_177,_17d));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_179[ctn][_179[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_179[_17a].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _179;
};
this.parseAttributes=function(node){
var _182={};
var atts=node.attributes;
for(var i=0;i<atts.length;i++){
var _185=atts.item(i);
if((dojo.render.html.capable)&&(dojo.render.html.ie)){
if(!_185){
continue;
}
if((typeof _185=="object")&&(typeof _185.nodeValue=="undefined")||(_185.nodeValue==null)||(_185.nodeValue=="")){
continue;
}
}
var nn=(_185.nodeName.indexOf("dojo:")==-1)?_185.nodeName:_185.nodeName.split("dojo:")[1];
_182[nn]={value:_185.nodeValue};
}
return _182;
};
};
dojo.require("dojo.lang");
dojo.provide("dojo.event");
dojo.event=new function(){
this.canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _18a=dojo.lang.nameAnonFunc(args[2],ao.adviceObj);
ao.adviceFunc=_18a;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _18a=dojo.lang.nameAnonFunc(args[0],ao.srcObj);
ao.srcFunc=_18a;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _18a=dojo.lang.nameAnonFunc(args[1],dj_global);
ao.srcFunc=_18a;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
break;
}
if((typeof ao.srcFunc).toLowerCase()!="string"){
ao.srcFunc=dojo.lang.getNameInObj(ao.srcObj,ao.srcFunc);
}
if((typeof ao.adviceFunc).toLowerCase()!="string"){
ao.adviceFunc=dojo.lang.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&((typeof ao.aroundFunc).toLowerCase()!="string")){
ao.aroundFunc=dojo.lang.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
return ao;
}
this.connect=function(){
var ao=interpolateArgs(arguments);
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this._kwConnectImpl=function(_192,_193){
var fn=(_193)?"disconnect":"connect";
if(typeof _192["srcFunc"]=="function"){
_192.srcObj=_192["srcObj"]||dj_global;
var _195=dojo.lang.nameAnonFunc(_192.srcFunc,_192.srcObj);
_192.srcFunc=_195;
}
if(typeof _192["adviceFunc"]=="function"){
_192.adviceObj=_192["adviceObj"]||dj_global;
var _195=dojo.lang.nameAnonFunc(_192.adviceFunc,_192.adviceObj);
_192.adviceFunc=_195;
}
return dojo.event[fn]((_192["type"]||_192["adviceType"]||"after"),_192["srcObj"]||dj_global,_192["srcFunc"],_192["adviceObj"]||_192["targetObj"]||dj_global,_192["adviceFunc"]||_192["targetFunc"],_192["aroundObj"],_192["aroundFunc"],_192["once"],_192["delay"],_192["rate"],_192["adviceMsg"]||false);
};
this.kwConnect=function(_196){
return this._kwConnectImpl(_196,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_199){
return this._kwConnectImpl(_199,true);
};
};
dojo.event.MethodInvocation=function(_19a,obj,args){
this.jp_=_19a;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_1a2){
this.object=obj||dj_global;
this.methodname=_1a2;
this.methodfunc=this.object[_1a2];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a4){
if(!obj){
obj=dj_global;
}
if(!obj[_1a4]){
obj[_1a4]=function(){
};
}else{
if((!dojo.lang.isFunction(obj[_1a4]))&&(!dojo.lang.isAlien(obj[_1a4]))){
return null;
}
}
var _1a5=_1a4+"$joinpoint";
var _1a6=_1a4+"$joinpoint$method";
var _1a7=obj[_1a5];
if(!_1a7){
var _1a8=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_1a8=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_1a5,_1a6,_1a4]);
}
}
obj[_1a6]=obj[_1a4];
_1a7=obj[_1a5]=new dojo.event.MethodJoinPoint(obj,_1a6);
obj[_1a4]=function(){
var args=[];
if((_1a8)&&(!arguments.length)&&(window.event)){
args.push(dojo.event.browser.fixEvent(window.event));
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_1a8)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x]));
}else{
args.push(arguments[x]);
}
}
}
return _1a7.run.apply(_1a7,args);
};
}
return _1a7;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
},run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _1ad=[];
for(var x=0;x<args.length;x++){
_1ad[x]=args[x];
}
var _1af=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _1b1=marr[0]||dj_global;
var _1b2=marr[1];
if(!_1b1[_1b2]){
dojo.raise("function \""+_1b2+"\" does not exist on \""+_1b1+"\"");
}
var _1b3=marr[2]||dj_global;
var _1b4=marr[3];
var msg=marr[6];
var _1b6;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _1b1[_1b2].apply(_1b1,to.args);
}};
to.args=_1ad;
var _1b8=parseInt(marr[4]);
var _1b9=((!isNaN(_1b8))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _1bc=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_1af(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_1b4){
_1b3[_1b4].call(_1b3,to);
}else{
if((_1b9)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_1b1[_1b2].call(_1b1,to);
}else{
_1b1[_1b2].apply(_1b1,args);
}
},_1b8);
}else{
if(msg){
_1b1[_1b2].call(_1b1,to);
}else{
_1b1[_1b2].apply(_1b1,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_1af,true);
}
var _1bf;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_1bf=mi.proceed();
}else{
if(this.methodfunc){
_1bf=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_1af,true);
}
return (this.methodfunc)?_1bf:null;
},getArr:function(kind){
var arr=this.after;
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
arr=this.before;
}else{
if(kind=="around"){
arr=this.around;
}
}
return arr;
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_1c4,_1c5,_1c6,_1c7,_1c8,_1c9,once,_1cb,rate,_1cd){
var arr=this.getArr(_1c8);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_1c4,_1c5,_1c6,_1c7,_1cb,rate,_1cd];
if(once){
if(this.hasAdvice(_1c4,_1c5,_1c8,arr)>=0){
return;
}
}
if(_1c9=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_1d0,_1d1,_1d2,arr){
if(!arr){
arr=this.getArr(_1d2);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
if((arr[x][0]==_1d0)&&(arr[x][1]==_1d1)){
ind=x;
}
}
return ind;
},removeAdvice:function(_1d6,_1d7,_1d8,once){
var arr=this.getArr(_1d8);
var ind=this.hasAdvice(_1d6,_1d7,_1d8,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_1d6,_1d7,_1d8,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_1dc){
if(!this.topics[_1dc]){
this.topics[_1dc]=new this.TopicImpl(_1dc);
}
return this.topics[_1dc];
};
this.registerPublisher=function(_1dd,obj,_1df){
var _1dd=this.getTopic(_1dd);
_1dd.registerPublisher(obj,_1df);
};
this.subscribe=function(_1e0,obj,_1e2){
var _1e0=this.getTopic(_1e0);
_1e0.subscribe(obj,_1e2);
};
this.unsubscribe=function(_1e3,obj,_1e5){
var _1e3=this.getTopic(_1e3);
_1e3.unsubscribe(obj,_1e5);
};
this.publish=function(_1e6,_1e7){
var _1e6=this.getTopic(_1e6);
var args=[];
if((arguments.length==2)&&(_1e7.length)&&(typeof _1e7!="string")){
args=_1e7;
}else{
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
}
_1e6.sendMessage.apply(_1e6,args);
};
};
dojo.event.topic.TopicImpl=function(_1ea){
this.topicName=_1ea;
var self=this;
self.subscribe=function(_1ec,_1ed){
var tf=_1ed||_1ec;
var to=(!_1ed)?dj_global:_1ec;
dojo.event.kwConnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.unsubscribe=function(_1f0,_1f1){
var tf=(!_1f1)?_1f0:_1f1;
var to=(!_1f1)?null:_1f0;
dojo.event.kwDisconnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.registerPublisher=function(_1f4,_1f5){
dojo.event.connect(_1f4,_1f5,self,"sendMessage");
};
self.sendMessage=function(_1f6){
};
};
dojo.provide("dojo.event.browser");
dojo.require("dojo.event");
dojo_ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_1f9){
var na;
var tna;
if(_1f9){
tna=_1f9.getElementsByTagName("*");
na=[_1f9];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _1fd={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
if(el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
na=null;
};
};
if(dojo.render.html.ie){
window.onunload=function(){
dojo_ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo_ie_clobber.clobberNodes=[];
};
}
dojo.event.browser=new function(){
var _201=0;
this.clean=function(node){
if(dojo.render.html.ie){
dojo_ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo_ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_205){
this.addClobberNode(node);
for(var x=0;x<_205.length;x++){
node.__clobberAttrs__.push(_205[x]);
}
};
this.removeListener=function(node,_208,fp,_20a){
if(!_20a){
var _20a=false;
}
_208=_208.toLowerCase();
if(_208.substr(0,2)=="on"){
_208=_208.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_208,fp,_20a);
}
};
this.addListener=function(node,_20c,fp,_20e,_20f){
if(!node){
return;
}
if(!_20e){
var _20e=false;
}
_20c=_20c.toLowerCase();
if(_20c.substr(0,2)!="on"){
_20c="on"+_20c;
}
if(!_20f){
var _210=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt));
if(_20e){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_210=fp;
}
if(node.addEventListener){
node.addEventListener(_20c.substr(2),_210,_20e);
return _210;
}else{
if(typeof node[_20c]=="function"){
var _213=node[_20c];
node[_20c]=function(e){
_213(e);
return _210(e);
};
}else{
node[_20c]=_210;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_20c]);
}
return _210;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_216,_217){
if(typeof _216!="function"){
dojo.raise("listener not a function: "+_216);
}
dojo.event.browser.currentEvent.currentTarget=_217;
return _216.call(_217,dojo.event.browser.currentEvent);
};
this.stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this.preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt){
if((!evt)&&(window["event"])){
var evt=window.event;
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if((dojo.render.html.ie)&&(evt["type"]=="keypress")){
evt.charCode=evt.keyCode;
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=evt.srcElement;
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
if(evt.fromElement){
evt.relatedTarget=evt.fromElement;
}
if(evt.toElement){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this.stopPropagation;
evt.preventDefault=this.preventDefault;
}
return evt;
};
this.stopEvent=function(ev){
if(window.event){
ev.returnValue=false;
ev.cancelBubble=true;
}else{
ev.preventDefault();
ev.stopPropagation();
}
};
};
dojo.hostenv.conditionalLoadModule({common:["dojo.event","dojo.event.topic"],browser:["dojo.event.browser"]});
dojo.hostenv.moduleLoaded("dojo.event.*");
dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _21c={};
var _21d=[];
this.getUniqueId=function(_21e){
return _21e+"_"+(_21c[_21e]!=undefined?++_21c[_21e]:_21c[_21e]=0);
};
this.add=function(_21f){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_21f);
if(_21f.widgetId==""){
if(_21f["id"]){
_21f.widgetId=_21f["id"];
}else{
if(_21f.extraArgs["id"]){
_21f.widgetId=_21f.extraArgs["id"];
}else{
_21f.widgetId=this.getUniqueId(_21f.widgetType);
}
}
}
if(this.widgetIds[_21f.widgetId]){
dojo.debug("widget ID collision on ID: "+_21f.widgetId);
}
this.widgetIds[_21f.widgetId]=_21f;
dojo.profile.end("dojo.widget.manager.add");
};
this.destroyAll=function(){
for(var x=this.widgets.length-1;x>=0;x--){
try{
this.widgets[x].destroy(true);
delete this.widgets[x];
}
catch(e){
}
}
};
this.remove=function(_221){
var tw=this.widgets[_221].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_221,1);
};
this.removeById=function(id){
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
return this.widgetIds[id];
};
this.getWidgetsByType=function(type){
var lt=type.toLowerCase();
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(x.widgetType.toLowerCase()==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsOfType=function(id){
dj_deprecated("getWidgetsOfType is depecrecated, use getWidgetsByType");
return dojo.widget.manager.getWidgetsByType(id);
};
this.getWidgetsByFilter=function(_22b){
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_22b(x)){
ret.push(x);
}
});
return ret;
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.byId=this.getWidgetById;
this.byType=this.getWidgetsByType;
this.byFilter=this.getWidgetsByFilter;
var _22e={};
var _22f=["dojo.widget","dojo.webui.widgets"];
for(var i=0;i<_22f.length;i++){
_22f[_22f[i]]=true;
}
this.registerWidgetPackage=function(_231){
if(!_22f[_231]){
_22f[_231]=true;
_22f.push(_231);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_22f,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_233,_234,_235){
var impl=this.getImplementationName(_233);
if(impl){
var ret=new impl(_234);
return ret;
}
};
this.getImplementationName=function(_238){
var _239=_238.toLowerCase();
var impl=_22e[_239];
if(impl){
return impl;
}
if(!_21d.length){
for(var _23b in dojo.render){
if(dojo.render[_23b]["capable"]===true){
var _23c=dojo.render[_23b].prefixes;
for(var i=0;i<_23c.length;i++){
_21d.push(_23c[i].toLowerCase());
}
}
}
_21d.push("");
}
for(var i=0;i<_22f.length;i++){
var _23e=dojo.evalObjPath(_22f[i]);
if(!_23e){
continue;
}
for(var j=0;j<_21d.length;j++){
if(!_23e[_21d[j]]){
continue;
}
for(var _240 in _23e[_21d[j]]){
if(_240.toLowerCase()!=_239){
continue;
}
_22e[_239]=_23e[_21d[j]][_240];
return _22e[_239];
}
}
for(var j=0;j<_21d.length;j++){
for(var _240 in _23e){
if(_240.toLowerCase()!=(_21d[j]+_239)){
continue;
}
_22e[_239]=_23e[_240];
return _22e[_239];
}
}
}
throw new Error("Could not locate \""+_238+"\" class");
};
this.resizing=false;
this.onResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _242=this.topWidgets[id];
if(_242.onResized){
_242.onResized();
}
}
}
finally{
this.resizing=false;
}
};
if(typeof window!="undefined"){
dojo.addOnLoad(this,"onResized");
dojo.event.connect(window,"onresize",this,"onResized");
}
};
dojo.widget.getUniqueId=function(){
return dojo.widget.manager.getUniqueId.apply(dojo.widget.manager,arguments);
};
dojo.widget.addWidget=function(){
return dojo.widget.manager.add.apply(dojo.widget.manager,arguments);
};
dojo.widget.destroyAllWidgets=function(){
return dojo.widget.manager.destroyAll.apply(dojo.widget.manager,arguments);
};
dojo.widget.removeWidget=function(){
return dojo.widget.manager.remove.apply(dojo.widget.manager,arguments);
};
dojo.widget.removeWidgetById=function(){
return dojo.widget.manager.removeById.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetById=function(){
return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetsByType=function(){
return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetsByFilter=function(){
return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager,arguments);
};
dojo.widget.byId=function(){
return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager,arguments);
};
dojo.widget.byType=function(){
return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager,arguments);
};
dojo.widget.byFilter=function(){
return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager,arguments);
};
dojo.widget.all=function(n){
var _244=dojo.widget.manager.getAllWidgets.apply(dojo.widget.manager,arguments);
if(arguments.length>0){
return _244[n];
}
return _244;
};
dojo.widget.registerWidgetPackage=function(){
return dojo.widget.manager.registerWidgetPackage.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetImplementation=function(){
return dojo.widget.manager.getImplementation.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetImplementationName=function(){
return dojo.widget.manager.getImplementationName.apply(dojo.widget.manager,arguments);
};
dojo.widget.widgets=dojo.widget.manager.widgets;
dojo.widget.widgetIds=dojo.widget.manager.widgetIds;
dojo.widget.root=dojo.widget.manager.root;
dojo.provide("dojo.string");
dojo.require("dojo.lang");
dojo.string.trim=function(str,wh){
if(!dojo.lang.isString(str)){
return str;
}
if(!str.length){
return str;
}
if(wh>0){
return str.replace(/^\s+/,"");
}else{
if(wh<0){
return str.replace(/\s+$/,"");
}else{
return str.replace(/^\s+|\s+$/g,"");
}
}
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.paramString=function(str,_24a,_24b){
for(var name in _24a){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_24a[name]);
}
if(_24b){
str=str.replace(/%\{([^\}\s]+)\}/g,"");
}
return str;
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _24f=str.split(" ");
var _250="";
var len=_24f.length;
for(var i=0;i<len;i++){
var word=_24f[i];
word=word.charAt(0).toUpperCase()+word.substring(1,word.length);
_250+=word;
if(i<len-1){
_250+=" ";
}
}
return new String(_250);
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _257=escape(str);
var _258,re=/%u([0-9A-F]{4})/i;
while((_258=_257.match(re))){
var num=Number("0x"+_258[1]);
var _25a=escape("&#"+num+";");
ret+=_257.substring(0,_258.index)+_25a;
_257=_257.substring(_258.index+_258[0].length);
}
ret+=_257.replace(/\+/g,"%2B");
return ret;
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}else{
return str.substring(0,len).replace(/\.+$/,"")+"...";
}
};
dojo.string.escape=function(type,str){
var args=[];
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_262){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_262){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.repeat=function(str,_267,_268){
var out="";
for(var i=0;i<_267;i++){
out+=str;
if(_268&&i<_267-1){
out+=_268;
}
}
return out;
};
dojo.string.endsWith=function(str,end,_26d){
if(_26d){
str=str.toLowerCase();
end=end.toLowerCase();
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_271,_272){
if(_272){
str=str.toLowerCase();
_271=_271.toLowerCase();
}
return str.indexOf(_271)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i]>-1)){
return true;
}
}
return false;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.string.normalizeNewlines=function(text,_283){
if(_283=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_283=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_285){
var _286=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_285){
_286.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_286.push(str.substr(prevcomma));
return _286;
};
dojo.string.addToPrototype=function(){
for(var _288 in dojo.string){
if(dojo.lang.isFunction(dojo.string[_288])){
var func=(function(){
var meth=_288;
switch(meth){
case "addToPrototype":
return null;
break;
case "escape":
return function(type){
return dojo.string.escape(type,this);
};
break;
default:
return function(){
var args=[this];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
dojo.debug(args);
return dojo.string[meth].apply(dojo.string,args);
};
}
})();
if(func){
String.prototype[_288]=func;
}
}
}
};
dojo.provide("dojo.widget.Widget");
dojo.provide("dojo.widget.tags");
dojo.require("dojo.lang");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");
dojo.require("dojo.string");
dojo.widget.Widget=function(){
this.children=[];
this.extraArgs={};
};
dojo.lang.extend(dojo.widget.Widget,{parent:null,isTopLevel:false,isModal:false,isEnabled:true,isHidden:false,isContainer:false,widgetId:"",widgetType:"Widget",toString:function(){
return "[Widget "+this.widgetType+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.isEnabled=true;
},disable:function(){
this.isEnabled=false;
},hide:function(){
this.isHidden=true;
},show:function(){
this.isHidden=false;
},create:function(args,_28f,_290){
this.satisfyPropertySets(args,_28f,_290);
this.mixInProperties(args,_28f,_290);
this.postMixInProperties(args,_28f,_290);
dojo.widget.manager.add(this);
this.buildRendering(args,_28f,_290);
this.initialize(args,_28f,_290);
this.postInitialize(args,_28f,_290);
this.postCreate(args,_28f,_290);
return this;
},destroy:function(_291){
this.uninitialize();
this.destroyRendering(_291);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(_292){
_292=(!_292)?function(){
return true;
}:_292;
for(var x=0;x<this.children.length;x++){
var tc=this.children[x];
if((tc)&&(_292(tc))){
tc.destroy();
}
}
},destroyChildrenOfType:function(type){
type=type.toLowerCase();
this.destroyChildren(function(item){
if(item.widgetType.toLowerCase()==type){
return true;
}else{
return false;
}
});
},getChildrenOfType:function(type,_298){
var ret=[];
type=type.toLowerCase();
for(var x=0;x<this.children.length;x++){
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
if(_298){
ret=ret.concat(this.children[x].getChildrenOfType(type,_298));
}
}
return ret;
},getDescendants:function(){
var _29b=[];
var _29c=[this];
var elem;
while(elem=_29c.pop()){
_29b.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_29c.push(elem);
});
}
return _29b;
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _2a3;
var _2a4=dojo.widget.lcArgsCache[this.widgetType];
if(_2a4==null){
_2a4={};
for(var y in this){
_2a4[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_2a4;
}
var _2a6={};
for(var x in args){
if(!this[x]){
var y=_2a4[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_2a6[x]){
continue;
}
_2a6[x]=true;
if((typeof this[x])!=(typeof _2a3)){
if(typeof args[x]!="string"){
this[x]=args[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=args[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(args[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(args[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);
dojo.event.connect(this,x,this,tn);
}else{
if(dojo.lang.isArray(this[x])){
this[x]=args[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(args[x]));
}else{
if(typeof this[x]=="object"){
var _2a8=args[x].split(";");
for(var y=0;y<_2a8.length;y++){
var si=_2a8[y].indexOf(":");
if((si!=-1)&&(_2a8[y].length>si)){
this[x][dojo.string.trim(_2a8[y].substr(0,si))]=_2a8[y].substr(si+1);
}
}
}else{
this[x]=args[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x]=args[x];
}
}
},postMixInProperties:function(){
},initialize:function(args,frag){
return false;
},postInitialize:function(args,frag){
return false;
},postCreate:function(args,frag){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(){
dj_unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dj_unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},cleanUp:function(){
dj_unimplemented("dojo.widget.Widget.cleanUp");
return false;
},addedTo:function(_2b0){
},addChild:function(_2b1){
dj_unimplemented("dojo.widget.Widget.addChild");
return false;
},addChildAtIndex:function(_2b2,_2b3){
dj_unimplemented("dojo.widget.Widget.addChildAtIndex");
return false;
},removeChild:function(_2b4){
dj_unimplemented("dojo.widget.Widget.removeChild");
return false;
},removeChildAtIndex:function(_2b5){
dj_unimplemented("dojo.widget.Widget.removeChildAtIndex");
return false;
},resize:function(_2b6,_2b7){
this.setWidth(_2b6);
this.setHeight(_2b7);
},setWidth:function(_2b8){
if((typeof _2b8=="string")&&(_2b8.substr(-1)=="%")){
this.setPercentageWidth(_2b8);
}else{
this.setNativeWidth(_2b8);
}
},setHeight:function(_2b9){
if((typeof _2b9=="string")&&(_2b9.substr(-1)=="%")){
this.setPercentageHeight(_2b9);
}else{
this.setNativeHeight(_2b9);
}
},setPercentageHeight:function(_2ba){
return false;
},setNativeHeight:function(_2bb){
return false;
},setPercentageWidth:function(_2bc){
return false;
},setNativeWidth:function(_2bd){
return false;
},getDescendants:function(){
var _2be=[];
var _2bf=[this];
var elem;
while(elem=_2bf.pop()){
_2be.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_2bf.push(elem);
});
}
return _2be;
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(type){
var _2c3=type.toLowerCase();
this[_2c3]=function(_2c4,_2c5,_2c6,_2c7,_2c8){
return dojo.widget.buildWidgetFromParseTree(_2c3,_2c4,_2c5,_2c6,_2c7,_2c8);
};
};
dojo.widget.tags.addParseTreeHandler("dojo:widget");
dojo.widget.tags["dojo:propertyset"]=function(_2c9,_2ca,_2cb){
var _2cc=_2ca.parseProperties(_2c9["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_2cd,_2ce,_2cf){
var _2d0=_2ce.parseProperties(_2cd["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_2d3,_2d4,_2d5,_2d6){
var _2d7=type.split(":");
_2d7=(_2d7.length==2)?_2d7[1]:type;
var _2d8=_2d6||_2d3.parseProperties(frag["dojo:"+_2d7]);
var _2d9=dojo.widget.manager.getImplementation(_2d7);
if(!_2d9){
throw new Error("cannot find \""+_2d7+"\" widget");
}else{
if(!_2d9.create){
throw new Error("\""+_2d7+"\" widget object does not appear to implement *Widget");
}
}
_2d8["dojoinsertionindex"]=_2d5;
var ret=_2d9.create(_2d8,frag,_2d4);
return ret;
};
dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.string");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_2db){
this.propertySetsList=[];
this.fragment=_2db;
this.createComponents=function(_2dc,_2dd){
var _2de=dojo.widget.tags;
var _2df=[];
for(var item in _2dc){
var _2e1=false;
try{
if(_2dc[item]&&(_2dc[item]["tagName"])&&(_2dc[item]!=_2dc["nodeRef"])){
var tn=new String(_2dc[item]["tagName"]);
var tna=tn.split(";");
for(var x=0;x<tna.length;x++){
var ltn=dojo.string.trim(tna[x]).toLowerCase();
if(_2de[ltn]){
_2e1=true;
_2dc[item].tagName=ltn;
var ret=_2de[ltn](_2dc[item],this,_2dd,_2dc[item]["index"]);
_2df.push(ret);
}else{
if((dojo.lang.isString(ltn))&&(ltn.substr(0,5)=="dojo:")){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
}
}
}
catch(e){
dojo.debug("fragment creation error:",e);
}
if((!_2e1)&&(typeof _2dc[item]=="object")&&(_2dc[item]!=_2dc.nodeRef)&&(_2dc[item]!=_2dc["tagName"])){
_2df.push(this.createComponents(_2dc[item],_2dd));
}
}
return _2df;
};
this.parsePropertySets=function(_2e7){
return [];
var _2e8=[];
for(var item in _2e7){
if((_2e7[item]["tagName"]=="dojo:propertyset")){
_2e8.push(_2e7[item]);
}
}
this.propertySetsList.push(_2e8);
return _2e8;
};
this.parseProperties=function(_2ea){
var _2eb={};
for(var item in _2ea){
if((_2ea[item]==_2ea["tagName"])||(_2ea[item]==_2ea.nodeRef)){
}else{
if((_2ea[item]["tagName"])&&(dojo.widget.tags[_2ea[item].tagName.toLowerCase()])){
}else{
if((_2ea[item][0])&&(_2ea[item][0].value!="")){
try{
if(item.toLowerCase()=="dataprovider"){
var _2ed=this;
this.getDataProvider(_2ed,_2ea[item][0].value);
_2eb.dataProvider=this.dataProvider;
}
_2eb[item]=_2ea[item][0].value;
var _2ee=this.parseProperties(_2ea[item]);
for(var _2ef in _2ee){
_2eb[_2ef]=_2ee[_2ef];
}
}
catch(e){
dojo.debug(e);
}
}
}
}
}
return _2eb;
};
this.getDataProvider=function(_2f0,_2f1){
dojo.io.bind({url:_2f1,load:function(type,_2f3){
if(type=="load"){
_2f0.dataProvider=_2f3;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_2f4){
for(var x=0;x<this.propertySetsList.length;x++){
if(_2f4==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_2f6){
var _2f7=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl["componentClass"]||cpl["componentType"]||null;
if((cpcc)&&(propertySetId==cpcc[0].value)){
_2f7.push(cpl);
}
}
return _2f7;
};
this.getPropertySets=function(_2fb){
var ppl="dojo:propertyproviderlist";
var _2fd=[];
var _2fe=_2fb["tagName"];
if(_2fb[ppl]){
var _2ff=_2fb[ppl].value.split(" ");
for(propertySetId in _2ff){
if((propertySetId.indexOf("..")==-1)&&(propertySetId.indexOf("://")==-1)){
var _300=this.getPropertySetById(propertySetId);
if(_300!=""){
_2fd.push(_300);
}
}else{
}
}
}
return (this.getPropertySetsByType(_2fe)).concat(_2fd);
};
this.createComponentFromScript=function(_301,_302,_303){
var ltn="dojo:"+_302.toLowerCase();
if(dojo.widget.tags[ltn]){
_303.fastMixIn=true;
return [dojo.widget.tags[ltn](_303,this,null,null,_303)];
}else{
if(ltn.substr(0,5)=="dojo:"){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(name){
if(!name){
name="dojo";
}
if(!this._parser_collection[name]){
this._parser_collection[name]=new dojo.widget.Parse();
}
return this._parser_collection[name];
};
dojo.widget.createWidget=function(name,_307,_308,_309){
function fromScript(_30a,name,_30c){
var _30d=name.toLowerCase();
var _30e="dojo:"+_30d;
_30c[_30e]={dojotype:[{value:_30d}],nodeRef:_30a,fastMixIn:true};
return dojo.widget.getParser().createComponentFromScript(_30a,name,_30c,true);
}
if(typeof name!="string"&&typeof _307=="string"){
dojo.deprecated("dojo.widget.createWidget","argument order is now of the form "+"dojo.widget.createWidget(NAME, [PROPERTIES, [REFERENCENODE, [POSITION]]])");
return fromScript(name,_307,_308);
}
_307=_307||{};
var _30f=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_308){
_30f=true;
_308=tn;
if(h){
dojo.html.body().appendChild(_308);
}
}else{
if(_309){
dojo.dom.insertAtPosition(tn,_308,_309);
}else{
tn=_308;
}
}
var _312=fromScript(tn,name,_307);
if(!_312[0]||typeof _312[0].widgetType=="undefined"){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
if(_30f){
if(_312[0].domNode.parentNode){
_312[0].domNode.parentNode.removeChild(_312[0].domNode);
}
}
return _312[0];
};
dojo.widget.fromScript=function(name,_314,_315,_316){
dojo.deprecated("dojo.widget.fromScript"," use "+"dojo.widget.createWidget instead");
return dojo.widget.createWidget(name,_314,_315,_316);
};
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.joinPath=function(){
var arr=[];
for(var i=0;i<arguments.length;i++){
arr.push(arguments[i]);
}
return arr.join("/").replace(/\/{2,}/g,"/").replace(/((https*|ftps*):)/i,"$1/");
};
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _31c=new dojo.uri.Uri(arguments[i].toString());
var _31d=new dojo.uri.Uri(uri.toString());
if(_31c.path==""&&_31c.scheme==null&&_31c.authority==null&&_31c.query==null){
if(_31c.fragment!=null){
_31d.fragment=_31c.fragment;
}
_31c=_31d;
}else{
if(_31c.scheme==null){
_31c.scheme=_31d.scheme;
if(_31c.authority==null){
_31c.authority=_31d.authority;
if(_31c.path.charAt(0)!="/"){
var path=_31d.path.substring(0,_31d.path.lastIndexOf("/")+1)+_31c.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_31c.path=segs.join("/");
}
}
}
}
uri="";
if(_31c.scheme!=null){
uri+=_31c.scheme+":";
}
if(_31c.authority!=null){
uri+="//"+_31c.authority;
}
uri+=_31c.path;
if(_31c.query!=null){
uri+="?"+_31c.query;
}
if(_31c.fragment!=null){
uri+="#"+_31c.fragment;
}
}
this.uri=uri.toString();
var _321="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_321));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_321="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_321));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.hostenv.conditionalLoadModule({common:["dojo.uri.Uri",false,false]});
dojo.hostenv.moduleLoaded("dojo.uri.*");
dojo.provide("dojo.widget.DomWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.string");
dojo.require("dojo.widget.Widget");
dojo.require("dojo.dom");
dojo.require("dojo.xml.Parse");
dojo.require("dojo.uri.*");
dojo.widget._cssFiles={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.buildFromTemplate=function(obj,_324,_325,_326){
var _327=_324||obj.templatePath;
var _328=_325||obj.templateCssPath;
if(!_328&&obj.templateCSSPath){
obj.templateCssPath=_328=obj.templateCSSPath;
obj.templateCSSPath=null;
dj_deprecated("templateCSSPath is deprecated, use templateCssPath");
}
if(_327&&!(_327 instanceof dojo.uri.Uri)){
_327=dojo.uri.dojoUri(_327);
dj_deprecated("templatePath should be of type dojo.uri.Uri");
}
if(_328&&!(_328 instanceof dojo.uri.Uri)){
_328=dojo.uri.dojoUri(_328);
dj_deprecated("templateCssPath should be of type dojo.uri.Uri");
}
var _329=dojo.widget.DomWidget.templates;
if(!obj["widgetType"]){
do{
var _32a="__dummyTemplate__"+dojo.widget.buildFromTemplate.dummyCount++;
}while(_329[_32a]);
obj.widgetType=_32a;
}
if((_328)&&(!dojo.widget._cssFiles[_328])){
dojo.html.insertCssFile(_328);
obj.templateCssPath=null;
dojo.widget._cssFiles[_328]=true;
}
var ts=_329[obj.widgetType];
if(!ts){
_329[obj.widgetType]={};
ts=_329[obj.widgetType];
}
if(!obj.templateString){
obj.templateString=_326||ts["string"];
}
if(!obj.templateNode){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_327)){
var _32c=dojo.hostenv.getText(_327);
if(_32c){
var _32d=_32c.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_32d){
_32c=_32d[1];
}
}else{
_32c="";
}
obj.templateString=_32c;
ts.string=_32c;
}
if(!ts["string"]){
ts.string=obj.templateString;
}
};
dojo.widget.buildFromTemplate.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.attachTemplateNodes=function(_32e,_32f,_330){
var _331=dojo.dom.ELEMENT_NODE;
if(!_32e){
_32e=_32f.domNode;
}
if(_32e.nodeType!=_331){
return;
}
var _332=_32e.getElementsByTagName("*");
var _333=_32f;
for(var x=-1;x<_332.length;x++){
var _335=(x==-1)?_32e:_332[x];
var _336=[];
for(var y=0;y<this.attachProperties.length;y++){
var _338=_335.getAttribute(this.attachProperties[y]);
if(_338){
_336=_338.split(";");
for(var z=0;z<this.attachProperties.length;z++){
if((_32f[_336[z]])&&(dojo.lang.isArray(_32f[_336[z]]))){
_32f[_336[z]].push(_335);
}else{
_32f[_336[z]]=_335;
}
}
break;
}
}
var _33a=_335.getAttribute(this.templateProperty);
if(_33a){
_32f[_33a]=_335;
}
var _33b=_335.getAttribute(this.eventAttachProperty);
if(_33b){
var evts=_33b.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _33d=null;
var tevt=dojo.string.trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _33f=tevt.split(":");
tevt=dojo.string.trim(_33f[0]);
_33d=dojo.string.trim(_33f[1]);
}
if(!_33d){
_33d=tevt;
}
var tf=function(){
var ntf=new String(_33d);
return function(evt){
if(_333[ntf]){
_333[ntf](dojo.event.browser.fixEvent(evt));
}
};
}();
dojo.event.browser.addListener(_335,tevt,tf,false,true);
}
}
for(var y=0;y<_330.length;y++){
var _343=_335.getAttribute(_330[y]);
if((_343)&&(_343.length)){
var _33d=null;
var _344=_330[y].substr(4);
_33d=dojo.string.trim(_343);
var tf=function(){
var ntf=new String(_33d);
return function(evt){
if(_333[ntf]){
_333[ntf](dojo.event.browser.fixEvent(evt));
}
};
}();
dojo.event.browser.addListener(_335,_344,tf,false,true);
}
}
var _347=_335.getAttribute(this.onBuildProperty);
if(_347){
eval("var node = baseNode; var widget = targetObj; "+_347);
}
_335.id="";
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var evts=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<evts.length;x++){
if(evts[x].legth<1){
continue;
}
var cm=evts[x].replace(/\s/,"");
cm=(cm.slice(0,cm.length-1));
if(!lem[cm]){
lem[cm]=true;
ret.push(cm);
}
}
return ret;
};
dojo.widget.buildAndAttachTemplate=function(obj,_350,_351,_352,_353){
this.buildFromTemplate(obj,_350,_351,_352);
var node=dojo.dom.createNodesFromText(obj.templateString,true)[0];
this.attachTemplateNodes(node,_353||obj,dojo.widget.getDojoEventsFromStr(_352));
return node;
};
dojo.widget.DomWidget=function(){
dojo.widget.Widget.call(this);
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
};
dojo.inherits(dojo.widget.DomWidget,dojo.widget.Widget);
dojo.lang.extend(dojo.widget.DomWidget,{templateNode:null,templateString:null,preventClobber:false,domNode:null,containerNode:null,addChild:function(_355,_356,pos,ref,_359){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
this.addWidgetAsDirectChild(_355,_356,pos,ref,_359);
this.registerChild(_355,_359);
}
return _355;
},addWidgetAsDirectChild:function(_35a,_35b,pos,ref,_35e){
if((!this.containerNode)&&(!_35b)){
this.containerNode=this.domNode;
}
var cn=(_35b)?_35b:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
ref=cn.lastChild;
}
if(!_35e){
_35e=0;
}
_35a.domNode.setAttribute("dojoinsertionindex",_35e);
if(!ref){
cn.appendChild(_35a.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_35a.domNode,ref.parentNode,_35e);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_35a.domNode);
}else{
dojo.dom.insertAtPosition(_35a.domNode,cn,pos);
}
}
}
},registerChild:function(_360,_361){
_360.dojoInsertionIndex=_361;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<_361){
idx=i;
}
}
this.children.splice(idx+1,0,_360);
_360.parent=this;
_360.addedTo(this);
delete dojo.widget.manager.topWidgets[_360.widgetId];
},removeChild:function(_364){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_364){
this.children.splice(x,1);
break;
}
}
return _364;
},getFragNodeRef:function(frag){
if(!frag["dojo:"+this.widgetType.toLowerCase()]){
dojo.raise("Error: no frag for widget type "+this.widgetType+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return (frag?frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"]:null);
},postInitialize:function(args,frag,_369){
var _36a=this.getFragNodeRef(frag);
if(_369&&(_369.snarfChildDomOutput||!_36a)){
_369.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_36a);
}else{
if(_36a){
if(this.domNode&&(this.domNode!==_36a)){
var _36b=_36a.parentNode.replaceChild(this.domNode,_36a);
}
}
}
if(_369){
_369.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.isContainer){
var _36c=dojo.widget.getParser();
_36c.createComponents(frag,this);
}
},startResize:function(_36d){
dj_unimplemented("dojo.widget.DomWidget.startResize");
},updateResize:function(_36e){
dj_unimplemented("dojo.widget.DomWidget.updateResize");
},endResize:function(_36f){
dj_unimplemented("dojo.widget.DomWidget.endResize");
},buildRendering:function(args,frag){
var ts=dojo.widget.DomWidget.templates[this.widgetType];
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var ts=dojo.widget.DomWidget.templates[this.widgetType];
if(ts){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _376=false;
var node=null;
var tstr=new String(this.templateString);
if((!this.templateNode)&&(this.templateString)){
_376=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_376){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_376.length;i++){
var key=_376[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?this[key.substring(5)]:hash[key];
var _37d;
if((kval)||(dojo.lang.isString(kval))){
_37d=(dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval;
tstr=tstr.replace(_376[i],_37d);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
ts.node=this.templateNode;
}
}
if((!this.templateNode)&&(!_376)){
dojo.debug("weren't able to create template!");
return false;
}else{
if(!_376){
node=this.templateNode.cloneNode(true);
if(!node){
return false;
}
}else{
node=this.createNodesFromText(tstr,true)[0];
}
}
this.domNode=node;
this.attachTemplateNodes(this.domNode,this);
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_37f,_380){
if(!_380){
_380=this;
}
return dojo.widget.attachTemplateNodes(_37f,_380,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
var _381=this.domNode.parentNode.removeChild(this.domNode);
delete _381;
}
catch(e){
}
},cleanUp:function(){
},getContainerHeight:function(){
return dojo.html.getInnerHeight(this.domNode.parentNode);
},getContainerWidth:function(){
return dojo.html.getInnerWidth(this.domNode.parentNode);
},createNodesFromText:function(){
dj_unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.widget.DomWidget.templates={};
dojo.provide("dojo.math");
dojo.math.degToRad=function(x){
return (x*Math.PI)/180;
};
dojo.math.radToDeg=function(x){
return (x*180)/Math.PI;
};
dojo.math.factorial=function(n){
if(n<1){
return 0;
}
var _385=1;
for(var i=1;i<=n;i++){
_385*=i;
}
return _385;
};
dojo.math.permutations=function(n,k){
if(n==0||k==0){
return 1;
}
return (dojo.math.factorial(n)/dojo.math.factorial(n-k));
};
dojo.math.combinations=function(n,r){
if(n==0||r==0){
return 1;
}
return (dojo.math.factorial(n)/(dojo.math.factorial(n-r)*dojo.math.factorial(r)));
};
dojo.math.bernstein=function(t,n,i){
return (dojo.math.combinations(n,i)*Math.pow(t,i)*Math.pow(1-t,n-i));
};
dojo.math.gaussianRandom=function(){
var k=2;
do{
var i=2*Math.random()-1;
var j=2*Math.random()-1;
k=i*i+j*j;
}while(k>=1);
k=Math.sqrt((-2*Math.log(k))/k);
return i*k;
};
dojo.math.mean=function(){
var _391=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var mean=0;
for(var i=0;i<_391.length;i++){
mean+=_391[i];
}
return mean/_391.length;
};
dojo.math.round=function(_394,_395){
if(!_395){
var _396=1;
}else{
var _396=Math.pow(10,_395);
}
return Math.round(_394*_396)/_396;
};
dojo.math.sd=function(){
var _397=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
return Math.sqrt(dojo.math.variance(_397));
};
dojo.math.variance=function(){
var _398=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var mean=0,squares=0;
for(var i=0;i<_398.length;i++){
mean+=_398[i];
squares+=Math.pow(_398[i],2);
}
return (squares/_398.length)-Math.pow(mean/_398.length,2);
};
dojo.math.range=function(a,b,step){
if(arguments.length<2){
b=a;
a=0;
}
if(arguments.length<3){
step=1;
}
var _39e=[];
if(step>0){
for(var i=a;i<b;i+=step){
_39e.push(i);
}
}else{
if(step<0){
for(var i=a;i>b;i+=step){
_39e.push(i);
}
}else{
throw new Error("dojo.math.range: step must be non-zero");
}
}
return _39e;
};
dojo.provide("dojo.graphics.color");
dojo.require("dojo.lang");
dojo.require("dojo.string");
dojo.require("dojo.math");
dojo.graphics.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.graphics.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.graphics.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_3a5){
if(_3a5){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.graphics.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},toHsv:function(){
return dojo.graphics.color.rgb2hsv(this.toRgb());
},toHsl:function(){
return dojo.graphics.color.rgb2hsl(this.toRgb());
},blend:function(_3a6,_3a7){
return dojo.graphics.color.blend(this.toRgb(),new Color(_3a6).toRgb(),_3a7);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_3aa){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_3aa);
}
if(!_3aa){
_3aa=0;
}else{
if(_3aa>1){
_3aa=1;
}else{
if(_3aa<-1){
_3aa=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_3aa));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_3b0){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_3b0));
};
dojo.graphics.color.extractRGB=function(_3b1){
var hex="0123456789abcdef";
_3b1=_3b1.toLowerCase();
if(_3b1.indexOf("rgb")==0){
var _3b3=_3b1.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_3b3.splice(1,3);
return ret;
}else{
var _3b5=dojo.graphics.color.hex2rgb(_3b1);
if(_3b5){
return _3b5;
}else{
return dojo.graphics.color.named[_3b1]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _3b7="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_3b7+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_3b7.indexOf(rgb[i].charAt(0))*16+_3b7.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.graphics.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
return ["#",dojo.string.pad(r.toString(16),2),dojo.string.pad(g.toString(16),2),dojo.string.pad(b.toString(16),2)].join("");
};
dojo.graphics.color.rgb2hsv=function(r,g,b){
if(dojo.lang.isArray(r)){
b=r[2]||0;
g=r[1]||0;
r=r[0]||0;
}
var h=null;
var s=null;
var v=null;
var min=Math.min(r,g,b);
v=Math.max(r,g,b);
var _3c4=v-min;
s=(v==0)?0:_3c4/v;
if(s==0){
h=0;
}else{
if(r==v){
h=60*(g-b)/_3c4;
}else{
if(g==v){
h=120+60*(b-r)/_3c4;
}else{
if(b==v){
h=240+60*(r-g)/_3c4;
}
}
}
if(h<0){
h+=360;
}
}
h=(h==0)?360:Math.ceil((h/360)*255);
s=Math.ceil(s*255);
return [h,s,v];
};
dojo.graphics.color.hsv2rgb=function(h,s,v){
if(dojo.lang.isArray(h)){
v=h[2]||0;
s=h[1]||0;
h=h[0]||0;
}
h=(h/255)*360;
if(h==360){
h=0;
}
s=s/255;
v=v/255;
var r=null;
var g=null;
var b=null;
if(s==0){
r=v;
g=v;
b=v;
}else{
var _3cb=h/60;
var i=Math.floor(_3cb);
var f=_3cb-i;
var p=v*(1-s);
var q=v*(1-(s*f));
var t=v*(1-(s*(1-f)));
switch(i){
case 0:
r=v;
g=t;
b=p;
break;
case 1:
r=q;
g=v;
b=p;
break;
case 2:
r=p;
g=v;
b=t;
break;
case 3:
r=p;
g=q;
b=v;
break;
case 4:
r=t;
g=p;
b=v;
break;
case 5:
r=v;
g=p;
b=q;
break;
}
}
r=Math.ceil(r*255);
g=Math.ceil(g*255);
b=Math.ceil(b*255);
return [r,g,b];
};
dojo.graphics.color.rgb2hsl=function(r,g,b){
if(dojo.lang.isArray(r)){
b=r[2]||0;
g=r[1]||0;
r=r[0]||0;
}
r/=255;
g/=255;
b/=255;
var h=null;
var s=null;
var l=null;
var min=Math.min(r,g,b);
var max=Math.max(r,g,b);
var _3d9=max-min;
l=(min+max)/2;
s=0;
if((l>0)&&(l<1)){
s=_3d9/((l<0.5)?(2*l):(2-2*l));
}
h=0;
if(_3d9>0){
if((max==r)&&(max!=g)){
h+=(g-b)/_3d9;
}
if((max==g)&&(max!=b)){
h+=(2+(b-r)/_3d9);
}
if((max==b)&&(max!=r)){
h+=(4+(r-g)/_3d9);
}
h*=60;
}
h=(h==0)?360:Math.ceil((h/360)*255);
s=Math.ceil(s*255);
l=Math.ceil(l*255);
return [h,s,l];
};
dojo.graphics.color.hsl2rgb=function(h,s,l){
if(dojo.lang.isArray(h)){
l=h[2]||0;
s=h[1]||0;
h=h[0]||0;
}
h=(h/255)*360;
if(h==360){
h=0;
}
s=s/255;
l=l/255;
while(h<0){
h+=360;
}
while(h>360){
h-=360;
}
if(h<120){
r=(120-h)/60;
g=h/60;
b=0;
}else{
if(h<240){
r=0;
g=(240-h)/60;
b=(h-120)/60;
}else{
r=(h-240)/60;
g=0;
b=(360-h)/60;
}
}
r=Math.min(r,1);
g=Math.min(g,1);
b=Math.min(b,1);
r=2*s*r+(1-s);
g=2*s*g+(1-s);
b=2*s*b+(1-s);
if(l<0.5){
r=l*r;
g=l*g;
b=l*b;
}else{
r=(1-l)*r+2*l-1;
g=(1-l)*g+2*l-1;
b=(1-l)*b+2*l-1;
}
r=Math.ceil(r*255);
g=Math.ceil(g*255);
b=Math.ceil(b*255);
return [r,g,b];
};
dojo.graphics.color.hsl2hex=function(h,s,l){
var rgb=dojo.graphics.color.hsl2rgb(h,s,l);
return dojo.graphics.color.rgb2hex(rgb[0],rgb[1],rgb[2]);
};
dojo.graphics.color.hex2hsl=function(hex){
var rgb=dojo.graphics.color.hex2rgb(hex);
return dojo.graphics.color.rgb2hsl(rgb[0],rgb[1],rgb[2]);
};
dojo.provide("dojo.style");
dojo.require("dojo.dom");
dojo.require("dojo.uri.Uri");
dojo.require("dojo.graphics.color");
dojo.style.boxSizing={marginBox:"margin-box",borderBox:"border-box",paddingBox:"padding-box",contentBox:"content-box"};
dojo.style.getBoxSizing=function(node){
if(dojo.render.html.ie||dojo.render.html.opera){
var cm=document["compatMode"];
if(cm=="BackCompat"||cm=="QuirksMode"){
return dojo.style.boxSizing.borderBox;
}else{
return dojo.style.boxSizing.contentBox;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _3e5=dojo.style.getStyle(node,"-moz-box-sizing");
if(!_3e5){
_3e5=dojo.style.getStyle(node,"box-sizing");
}
return (_3e5?_3e5:dojo.style.boxSizing.contentBox);
}
};
dojo.style.isBorderBox=function(node){
return (dojo.style.getBoxSizing(node)==dojo.style.boxSizing.borderBox);
};
dojo.style.getUnitValue=function(_3e7,_3e8,_3e9){
var _3ea={value:0,units:"px"};
var s=dojo.style.getComputedStyle(_3e7,_3e8);
if(s==""||(s=="auto"&&_3e9)){
return _3ea;
}
if(dojo.lang.isUndefined(s)){
_3ea.value=NaN;
}else{
var _3ec=s.match(/([\d.]+)([a-z%]*)/i);
if(!_3ec){
_3ea.value=NaN;
}else{
_3ea.value=Number(_3ec[1]);
_3ea.units=_3ec[2].toLowerCase();
}
}
return _3ea;
};
dojo.style.getPixelValue=function(_3ed,_3ee,_3ef){
var _3f0=dojo.style.getUnitValue(_3ed,_3ee,_3ef);
if(isNaN(_3f0.value)){
return 0;
}
if((_3f0.value)&&(_3f0.units!="px")){
return NaN;
}
return _3f0.value;
};
dojo.style.getNumericStyle=dojo.style.getPixelValue;
dojo.style.isPositionAbsolute=function(node){
return (dojo.style.getComputedStyle(node,"position")=="absolute");
};
dojo.style.getMarginWidth=function(node){
var _3f3=dojo.style.isPositionAbsolute(node);
var left=dojo.style.getPixelValue(node,"margin-left",_3f3);
var _3f5=dojo.style.getPixelValue(node,"margin-right",_3f3);
return left+_3f5;
};
dojo.style.getBorderWidth=function(node){
var left=(dojo.style.getStyle(node,"border-left-style")=="none"?0:dojo.style.getPixelValue(node,"border-left-width"));
var _3f8=(dojo.style.getStyle(node,"border-right-style")=="none"?0:dojo.style.getPixelValue(node,"border-right-width"));
return left+_3f8;
};
dojo.style.getPaddingWidth=function(node){
var left=dojo.style.getPixelValue(node,"padding-left",true);
var _3fb=dojo.style.getPixelValue(node,"padding-right",true);
return left+_3fb;
};
dojo.style.getContentWidth=function(node){
return node.offsetWidth-dojo.style.getPaddingWidth(node)-dojo.style.getBorderWidth(node);
};
dojo.style.getInnerWidth=function(node){
return node.offsetWidth;
};
dojo.style.getOuterWidth=function(node){
return dojo.style.getInnerWidth(node)+dojo.style.getMarginWidth(node);
};
dojo.style.setOuterWidth=function(node,_400){
if(!dojo.style.isBorderBox(node)){
_400-=dojo.style.getPaddingWidth(node)+dojo.style.getBorderWidth(node);
}
_400-=dojo.style.getMarginWidth(node);
if(!isNaN(_400)&&_400>0){
node.style.width=_400+"px";
return true;
}else{
return false;
}
};
dojo.style.getContentBoxWidth=dojo.style.getContentWidth;
dojo.style.getBorderBoxWidth=dojo.style.getInnerWidth;
dojo.style.getMarginBoxWidth=dojo.style.getOuterWidth;
dojo.style.setMarginBoxWidth=dojo.style.setOuterWidth;
dojo.style.getMarginHeight=function(node){
var _402=dojo.style.isPositionAbsolute(node);
var top=dojo.style.getPixelValue(node,"margin-top",_402);
var _404=dojo.style.getPixelValue(node,"margin-bottom",_402);
return top+_404;
};
dojo.style.getBorderHeight=function(node){
var top=(dojo.style.getStyle(node,"border-top-style")=="none"?0:dojo.style.getPixelValue(node,"border-top-width"));
var _407=(dojo.style.getStyle(node,"border-bottom-style")=="none"?0:dojo.style.getPixelValue(node,"border-bottom-width"));
return top+_407;
};
dojo.style.getPaddingHeight=function(node){
var top=dojo.style.getPixelValue(node,"padding-top",true);
var _40a=dojo.style.getPixelValue(node,"padding-bottom",true);
return top+_40a;
};
dojo.style.getContentHeight=function(node){
return node.offsetHeight-dojo.style.getPaddingHeight(node)-dojo.style.getBorderHeight(node);
};
dojo.style.getInnerHeight=function(node){
return node.offsetHeight;
};
dojo.style.getOuterHeight=function(node){
return dojo.style.getInnerHeight(node)+dojo.style.getMarginHeight(node);
};
dojo.style.setOuterHeight=function(node,_40f){
if(!dojo.style.isBorderBox(node)){
_40f-=dojo.style.getPaddingHeight(node)+dojo.style.getBorderHeight(node);
}
_40f-=dojo.style.getMarginHeight(node);
if(!isNaN(_40f)&&_40f>0){
node.style.height=_40f+"px";
return true;
}else{
return false;
}
};
dojo.style.setContentWidth=function(node,_411){
if(dojo.style.isBorderBox(node)){
_411+=dojo.style.getPaddingWidth(node)+dojo.style.getBorderWidth(node);
}
if(!isNaN(_411)&&_411>0){
node.style.width=_411+"px";
return true;
}else{
return false;
}
};
dojo.style.setContentHeight=function(node,_413){
if(dojo.style.isBorderBox(node)){
_413+=dojo.style.getPaddingHeight(node)+dojo.style.getBorderHeight(node);
}
if(!isNaN(_413)&&_413>0){
node.style.height=_413+"px";
return true;
}else{
return false;
}
};
dojo.style.getContentBoxHeight=dojo.style.getContentHeight;
dojo.style.getBorderBoxHeight=dojo.style.getInnerHeight;
dojo.style.getMarginBoxHeight=dojo.style.getOuterHeight;
dojo.style.setMarginBoxHeight=dojo.style.setOuterHeight;
dojo.style.getTotalOffset=function(node,type,_416){
var _417=(type=="top")?"offsetTop":"offsetLeft";
var _418=(type=="top")?"scrollTop":"scrollLeft";
var _419=(type=="top")?"y":"x";
var _41a=0;
if(node["offsetParent"]){
if(dojo.render.html.safari&&node.style.getPropertyValue("position")=="absolute"&&node.parentNode==dojo.html.body()){
var _41b=dojo.html.body();
}else{
var _41b=dojo.html.body().parentNode;
}
if(_416&&node.parentNode!=document.body){
_41a-=dojo.style.sumAncestorProperties(node,_418);
}
do{
_41a+=node[_417];
node=node.offsetParent;
}while(node!=_41b&&node!=null);
}else{
if(node[_419]){
_41a+=node[_419];
}
}
return _41a;
};
dojo.style.sumAncestorProperties=function(node,prop){
if(!node){
return 0;
}
var _41e=0;
while(node){
var val=node[prop];
if(val){
_41e+=val-0;
}
node=node.parentNode;
}
return _41e;
};
dojo.style.totalOffsetLeft=function(node,_421){
return dojo.style.getTotalOffset(node,"left",_421);
};
dojo.style.getAbsoluteX=dojo.style.totalOffsetLeft;
dojo.style.totalOffsetTop=function(node,_423){
return dojo.style.getTotalOffset(node,"top",_423);
};
dojo.style.getAbsoluteY=dojo.style.totalOffsetTop;
dojo.style.getAbsolutePosition=function(node,_425){
var _426=[dojo.style.getAbsoluteX(node,_425),dojo.style.getAbsoluteY(node,_425)];
_426.x=_426[0];
_426.y=_426[1];
return _426;
};
dojo.style.styleSheet=null;
dojo.style.insertCssRule=function(_427,_428,_429){
if(!dojo.style.styleSheet){
if(document.createStyleSheet){
dojo.style.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.style.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.style.styleSheet.cssRules){
_429=dojo.style.styleSheet.cssRules.length;
}else{
if(dojo.style.styleSheet.rules){
_429=dojo.style.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.style.styleSheet.insertRule){
var rule=_427+" { "+_428+" }";
return dojo.style.styleSheet.insertRule(rule,_429);
}else{
if(dojo.style.styleSheet.addRule){
return dojo.style.styleSheet.addRule(_427,_428,_429);
}else{
return null;
}
}
};
dojo.style.removeCssRule=function(_42b){
if(!dojo.style.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_42b){
_42b=dojo.style.styleSheet.rules.length;
dojo.style.styleSheet.removeRule(_42b);
}
}else{
if(document.styleSheets[0]){
if(!_42b){
_42b=dojo.style.styleSheet.cssRules.length;
}
dojo.style.styleSheet.deleteRule(_42b);
}
}
return true;
};
dojo.style.insertCssFile=function(URI,doc,_42e){
if(!URI){
return;
}
if(!doc){
doc=document;
}
if(doc.baseURI){
URI=new dojo.uri.Uri(doc.baseURI,URI);
}
if(_42e&&doc.styleSheets){
var loc=location.href.split("#")[0].substring(0,location.href.indexOf(location.pathname));
for(var i=0;i<doc.styleSheets.length;i++){
if(doc.styleSheets[i].href&&URI.toString()==new dojo.uri.Uri(doc.styleSheets[i].href.toString())){
return;
}
}
}
var file=doc.createElement("link");
file.setAttribute("type","text/css");
file.setAttribute("rel","stylesheet");
file.setAttribute("href",URI);
var head=doc.getElementsByTagName("head")[0];
if(head){
head.appendChild(file);
}
};
dojo.style.getBackgroundColor=function(node){
var _434;
do{
_434=dojo.style.getStyle(node,"background-color");
if(_434.toLowerCase()=="rgba(0, 0, 0, 0)"){
_434="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_434,["transparent",""]));
if(_434=="transparent"){
_434=[255,255,255,0];
}else{
_434=dojo.graphics.color.extractRGB(_434);
}
return _434;
};
dojo.style.getComputedStyle=function(_435,_436,_437){
var _438=_437;
if(_435.style.getPropertyValue){
_438=_435.style.getPropertyValue(_436);
}
if(!_438){
if(document.defaultView){
var cs=document.defaultView.getComputedStyle(_435,"");
if(cs){
_438=cs.getPropertyValue(_436);
}
}else{
if(_435.currentStyle){
_438=_435.currentStyle[dojo.style.toCamelCase(_436)];
}
}
}
return _438;
};
dojo.style.getStyle=function(_43a,_43b){
var _43c=dojo.style.toCamelCase(_43b);
var _43d=_43a.style[_43c];
return (_43d?_43d:dojo.style.getComputedStyle(_43a,_43b,_43d));
};
dojo.style.toCamelCase=function(_43e){
var arr=_43e.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.style.toSelectorCase=function(_441){
return _441.replace(/([A-Z])/g,"-$1").toLowerCase();
};
dojo.style.setOpacity=function setOpacity(node,_443,_444){
node=dojo.byId(node);
var h=dojo.render.html;
if(!_444){
if(_443>=1){
if(h.ie){
dojo.style.clearOpacity(node);
return;
}else{
_443=0.999999;
}
}else{
if(_443<0){
_443=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_443*100+")";
}
}
node.style.filter="Alpha(Opacity="+_443*100+")";
}else{
if(h.moz){
node.style.opacity=_443;
node.style.MozOpacity=_443;
}else{
if(h.safari){
node.style.opacity=_443;
node.style.KhtmlOpacity=_443;
}else{
node.style.opacity=_443;
}
}
}
};
dojo.style.getOpacity=function getOpacity(node){
if(dojo.render.html.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
dojo.style.clearOpacity=function clearOpacity(node){
var h=dojo.render.html;
if(h.ie){
if(node.filters&&node.filters.alpha){
node.style.filter="";
}
}else{
if(h.moz){
node.style.opacity=1;
node.style.MozOpacity=1;
}else{
if(h.safari){
node.style.opacity=1;
node.style.KhtmlOpacity=1;
}else{
node.style.opacity=1;
}
}
}
};
dojo.provide("dojo.html");
dojo.require("dojo.dom");
dojo.require("dojo.style");
dojo.require("dojo.string");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.lang.mixin(dojo.html,dojo.style);
dojo.html.clearSelection=function(){
try{
if(window["getSelection"]){
if(dojo.render.html.safari){
window.getSelection().collapse();
}else{
window.getSelection().removeAllRanges();
}
}else{
if(document.selection){
if(document.selection.empty){
document.selection.empty();
}else{
if(document.selection.clear){
document.selection.clear();
}
}
}
}
return true;
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.html.disableSelection=function(_44c){
_44c=dojo.byId(_44c)||dojo.html.body();
var h=dojo.render.html;
if(h.mozilla){
_44c.style.MozUserSelect="none";
}else{
if(h.safari){
_44c.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_44c.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_44e){
_44e=dojo.byId(_44e)||dojo.html.body();
var h=dojo.render.html;
if(h.mozilla){
_44e.style.MozUserSelect="";
}else{
if(h.safari){
_44e.style.KhtmlUserSelect="";
}else{
if(h.ie){
_44e.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_450){
_450=dojo.byId(_450);
if(document.selection&&dojo.html.body().createTextRange){
var _451=dojo.html.body().createTextRange();
_451.moveToElementText(_450);
_451.select();
}else{
if(window["getSelection"]){
var _452=window.getSelection();
if(_452["selectAllChildren"]){
_452.selectAllChildren(_450);
}
}
}
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _453=window.getSelection();
if(dojo.lang.isString(_453)){
return _453=="";
}else{
return _453.isCollapsed;
}
}
}
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=window.event||{};
}
if(evt.srcElement){
return evt.srcElement;
}else{
if(evt.target){
return evt.target;
}
}
return null;
};
dojo.html.getScrollTop=function(){
return document.documentElement.scrollTop||dojo.html.body().scrollTop||0;
};
dojo.html.getScrollLeft=function(){
return document.documentElement.scrollLeft||dojo.html.body().scrollLeft||0;
};
dojo.html.getDocumentWidth=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportWidth();
};
dojo.html.getDocumentHeight=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportHeight();
};
dojo.html.getDocumentSize=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportSize();
};
dojo.html.getViewportWidth=function(){
var w=0;
if(window.innerWidth){
w=window.innerWidth;
}
if(dojo.exists(document,"documentElement.clientWidth")){
var w2=document.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
return w;
}
if(document.body){
return document.body.clientWidth;
}
return 0;
};
dojo.html.getViewportHeight=function(){
if(window.innerHeight){
return window.innerHeight;
}
if(dojo.exists(document,"documentElement.clientHeight")){
return document.documentElement.clientHeight;
}
if(document.body){
return document.body.clientHeight;
}
return 0;
};
dojo.html.getViewportSize=function(){
var ret=[dojo.html.getViewportWidth(),dojo.html.getViewportHeight()];
ret.w=ret[0];
ret.h=ret[1];
return ret;
};
dojo.html.getScrollOffset=function(){
var ret=[0,0];
if(window.pageYOffset){
ret=[window.pageXOffset,window.pageYOffset];
}else{
if(dojo.exists(document,"documentElement.scrollTop")){
ret=[document.documentElement.scrollLeft,document.documentElement.scrollTop];
}else{
if(document.body){
ret=[document.body.scrollLeft,document.body.scrollTop];
}
}
}
ret.x=ret[0];
ret.y=ret[1];
return ret;
};
dojo.html.getParentOfType=function(node,type){
dojo.deprecated("dojo.html.getParentOfType has been deprecated in favor of dojo.html.getParentByType*");
return dojo.html.getParentByType(node,type);
};
dojo.html.getParentByType=function(node,type){
var _45d=dojo.byId(node);
type=type.toLowerCase();
while((_45d)&&(_45d.nodeName.toLowerCase()!=type)){
if(_45d==(document["body"]||document["documentElement"])){
return null;
}
_45d=_45d.parentNode;
}
return _45d;
};
dojo.html.getAttribute=function(node,attr){
node=dojo.byId(node);
if((!node)||(!node.getAttribute)){
return null;
}
var ta=typeof attr=="string"?attr:new String(attr);
var v=node.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){
return (node.getAttributeNode(ta)).value;
}else{
if(node.getAttribute(ta)){
return node.getAttribute(ta);
}else{
if(node.getAttribute(ta.toLowerCase())){
return node.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(node,attr){
node=dojo.byId(node);
return dojo.html.getAttribute(node,attr)?true:false;
};
dojo.html.getClass=function(node){
node=dojo.byId(node);
if(!node){
return "";
}
var cs="";
if(node.className){
cs=node.className;
}else{
if(dojo.html.hasAttribute(node,"class")){
cs=dojo.html.getAttribute(node,"class");
}
}
return dojo.string.trim(cs);
};
dojo.html.getClasses=function(node){
node=dojo.byId(node);
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_469){
node=dojo.byId(node);
return dojo.lang.inArray(dojo.html.getClasses(node),_469);
};
dojo.html.prependClass=function(node,_46b){
node=dojo.byId(node);
if(!node){
return false;
}
_46b+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_46b);
};
dojo.html.addClass=function(node,_46d){
node=dojo.byId(node);
if(!node){
return false;
}
if(dojo.html.hasClass(node,_46d)){
return false;
}
_46d=dojo.string.trim(dojo.html.getClass(node)+" "+_46d);
return dojo.html.setClass(node,_46d);
};
dojo.html.setClass=function(node,_46f){
node=dojo.byId(node);
if(!node){
return false;
}
var cs=new String(_46f);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_46f);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(node,_472,_473){
node=dojo.byId(node);
if(!node){
return false;
}
var _472=dojo.string.trim(new String(_472));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_473){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_472)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_472){
nca.push(cs[i]);
}
}
}
dojo.html.setClass(node,nca.join(" "));
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_478,_479){
node=dojo.byId(node);
dojo.html.removeClass(node,_479);
dojo.html.addClass(node,_478);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_47a,_47b,_47c,_47d){
_47b=dojo.byId(_47b);
if(!_47b){
_47b=document;
}
var _47e=_47a.split(/\s+/g);
var _47f=[];
if(_47d!=1&&_47d!=2){
_47d=0;
}
var _480=new RegExp("(\\s|^)(("+_47e.join(")|(")+"))(\\s|$)");
if(!_47c){
_47c="*";
}
var _481=_47b.getElementsByTagName(_47c);
outer:
for(var i=0;i<_481.length;i++){
var node=_481[i];
var _484=dojo.html.getClasses(node);
if(_484.length==0){
continue outer;
}
var _485=0;
for(var j=0;j<_484.length;j++){
if(_480.test(_484[j])){
if(_47d==dojo.html.classMatchType.ContainsAny){
_47f.push(node);
continue outer;
}else{
_485++;
}
}else{
if(_47d==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_485==_47e.length){
if(_47d==dojo.html.classMatchType.IsOnly&&_485==_484.length){
_47f.push(node);
}else{
if(_47d==dojo.html.classMatchType.ContainsAll){
_47f.push(node);
}
}
}
}
return _47f;
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _489=e.pageX||e.clientX+dojo.html.body().scrollLeft;
var _48a=e.pageY||e.clientY+dojo.html.body().scrollTop;
with(dojo.html){
var _48b=getAbsoluteX(node)+(getInnerWidth(node)/2);
var _48c=getAbsoluteY(node)+(getInnerHeight(node)/2);
}
with(dojo.html.gravity){
return ((_489<_48b?WEST:EAST)|(_48a<_48c?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.overElement=function(_48d,e){
_48d=dojo.byId(_48d);
var _48f=e.pageX||e.clientX+dojo.html.body().scrollLeft;
var _490=e.pageY||e.clientY+dojo.html.body().scrollTop;
with(dojo.html){
var top=getAbsoluteY(_48d);
var _492=top+getInnerHeight(_48d);
var left=getAbsoluteX(_48d);
var _494=left+getInnerWidth(_48d);
}
return (_48f>=left&&_48f<=_494&&_490>=top&&_490<=_492);
};
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _496="";
if(node==null){
return _496;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _498="unknown";
try{
_498=dojo.style.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_498){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_496+="\n";
_496+=dojo.html.renderedTextContent(node.childNodes[i]);
_496+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_496+="\n";
}else{
_496+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _49a="unknown";
try{
_49a=dojo.style.getStyle(node,"text-transform");
}
catch(E){
}
switch(_49a){
case "capitalize":
text=dojo.string.capitalize(text);
break;
case "uppercase":
text=text.toUpperCase();
break;
case "lowercase":
text=text.toLowerCase();
break;
default:
break;
}
switch(_49a){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
text=text.replace(/\s+/," ");
if(/\s$/.test(_496)){
text.replace(/^\s/,"");
}
break;
}
_496+=text;
break;
default:
break;
}
}
return _496;
};
dojo.html.setActiveStyleSheet=function(_49b){
var i,a,main;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_49b){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i,a;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i,a;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.body=function(){
return document.body||document.getElementsByTagName("body")[0];
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=dojo.string.trim(txt);
}
var tn=document.createElement("div");
tn.style.visibility="hidden";
document.body.appendChild(tn);
var _4a2="none";
if((/^<t[dh][\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_4a2="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody>"+txt+"</tbody></table>";
_4a2="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table>"+txt+"</table>";
_4a2="section";
}
}
}
tn.innerHTML=txt;
tn.normalize();
var _4a3=null;
switch(_4a2){
case "cell":
_4a3=tn.getElementsByTagName("tr")[0];
break;
case "row":
_4a3=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_4a3=tn.getElementsByTagName("table")[0];
break;
default:
_4a3=tn;
break;
}
var _4a4=[];
for(var x=0;x<_4a3.childNodes.length;x++){
_4a4.push(_4a3.childNodes[x].cloneNode(true));
}
tn.style.display="none";
document.body.removeChild(tn);
return _4a4;
};
if(!dojo.evalObjPath("dojo.dom.createNodesFromText")){
dojo.dom.createNodesFromText=function(){
dojo.deprecated("dojo.dom.createNodesFromText","use dojo.html.createNodesFromText instead");
return dojo.html.createNodesFromText.apply(dojo.html,arguments);
};
}
dojo.html.isVisible=function(node){
node=dojo.byId(node);
return dojo.style.getComputedStyle(node||this.domNode,"display")!="none";
};
dojo.html.show=function(node){
node=dojo.byId(node);
if(node.style){
node.style.display=dojo.lang.inArray(["tr","td","th"],node.tagName.toLowerCase())?"":"block";
}
};
dojo.html.hide=function(node){
node=dojo.byId(node);
if(node.style){
node.style.display="none";
}
};
dojo.html.toggleVisible=function(node){
if(dojo.html.isVisible(node)){
dojo.html.hide(node);
return false;
}else{
dojo.html.show(node);
return true;
}
};
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var arr=dojo.lang.map(dojo.lang.toArray(arguments,1),function(a){
return String(a).toLowerCase();
});
return arr[dojo.lang.find(node.tagName.toLowerCase(),arr)]||"";
}
return "";
};
dojo.html.toCoordinateArray=function(_4ad,_4ae){
if(dojo.lang.isArray(_4ad)){
while(_4ad.length<4){
_4ad.push(0);
}
while(_4ad.length>4){
_4ad.pop();
}
var ret=_4ad;
}else{
var node=dojo.byId(_4ad);
var ret=[dojo.html.getAbsoluteX(node,_4ae),dojo.html.getAbsoluteY(node,_4ae),dojo.html.getInnerWidth(node),dojo.html.getInnerHeight(node)];
}
ret.x=ret[0];
ret.y=ret[1];
ret.w=ret[2];
ret.h=ret[3];
return ret;
};
dojo.html.placeOnScreen=function(node,_4b2,_4b3,_4b4,_4b5){
if(dojo.lang.isArray(_4b2)){
_4b5=_4b4;
_4b4=_4b3;
_4b3=_4b2[1];
_4b2=_4b2[0];
}
if(!isNaN(_4b4)){
_4b4=[Number(_4b4),Number(_4b4)];
}else{
if(!dojo.lang.isArray(_4b4)){
_4b4=[0,0];
}
}
var _4b6=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth+_4b4[0];
var h=node.offsetHeight+_4b4[1];
if(_4b5){
_4b2-=_4b6.x;
_4b3-=_4b6.y;
}
var x=_4b2+w;
if(x>view.w){
x=view.w-w;
}else{
x=_4b2;
}
x=Math.max(_4b4[0],x)+_4b6.x;
var y=_4b3+h;
if(y>view.h){
y=view.h-h;
}else{
y=_4b3;
}
y=Math.max(_4b4[1],y)+_4b6.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.placeOnScreenPoint=function(node,_4be,_4bf,_4c0,_4c1){
if(dojo.lang.isArray(_4be)){
_4c1=_4c0;
_4c0=_4bf;
_4bf=_4be[1];
_4be=_4be[0];
}
var _4c2=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth;
var h=node.offsetHeight;
if(_4c1){
_4be-=_4c2.x;
_4bf-=_4c2.y;
}
var x=-1,y=-1;
if(_4be+w<=view.w&&_4bf+h<=view.h){
x=_4be;
y=_4bf;
}
if((x<0||y<0)&&_4be<=view.w&&_4bf+h<=view.h){
x=_4be-w;
y=_4bf;
}
if((x<0||y<0)&&_4be+w<=view.w&&_4bf<=view.h){
x=_4be;
y=_4bf-h;
}
if((x<0||y<0)&&_4be<=view.w&&_4bf<=view.h){
x=_4be-w;
y=_4bf-h;
}
if(x<0||y<0||(x+w>view.w)||(y+h>view.h)){
return dojo.html.placeOnScreen(node,_4be,_4bf,_4c0,_4c1);
}
x+=_4c2.x;
y+=_4c2.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.BackgroundIframe=function(){
if(this.ie){
this.iframe=document.createElement("<iframe frameborder='0' src='about:blank'>");
var s=this.iframe.style;
s.position="absolute";
s.left=s.top="0px";
s.zIndex=2;
s.display="none";
dojo.style.setOpacity(this.iframe,0);
dojo.html.body().appendChild(this.iframe);
}else{
this.enabled=false;
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{ie:dojo.render.html.ie,enabled:true,visibile:false,iframe:null,sizeNode:null,sizeCoords:null,size:function(node){
if(!this.ie||!this.enabled){
return;
}
if(dojo.dom.isNode(node)){
this.sizeNode=node;
}else{
if(arguments.length>0){
this.sizeNode=null;
this.sizeCoords=node;
}
}
this.update();
},update:function(){
if(!this.ie||!this.enabled){
return;
}
if(this.sizeNode){
this.sizeCoords=dojo.html.toCoordinateArray(this.sizeNode,true);
}else{
if(this.sizeCoords){
this.sizeCoords=dojo.html.toCoordinateArray(this.sizeCoords,true);
}else{
return;
}
}
var s=this.iframe.style;
var dims=this.sizeCoords;
s.width=dims.w+"px";
s.height=dims.h+"px";
s.left=dims.x+"px";
s.top=dims.y+"px";
},setZIndex:function(node){
if(!this.ie||!this.enabled){
return;
}
if(dojo.dom.isNode(node)){
this.iframe.zIndex=dojo.html.getStyle(node,"z-index")-1;
}else{
if(!isNaN(node)){
this.iframe.zIndex=node;
}
}
},show:function(node){
if(!this.ie||!this.enabled){
return;
}
this.size(node);
this.iframe.style.display="block";
},hide:function(){
if(!this.ie){
return;
}
var s=this.iframe.style;
s.display="none";
s.width=s.height="1px";
},remove:function(){
dojo.dom.removeNode(this.iframe);
}});
dojo.provide("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.DomWidget");
dojo.require("dojo.html");
dojo.require("dojo.string");
dojo.widget.HtmlWidget=function(args){
dojo.widget.DomWidget.call(this);
};
dojo.inherits(dojo.widget.HtmlWidget,dojo.widget.DomWidget);
dojo.lang.extend(dojo.widget.HtmlWidget,{widgetType:"HtmlWidget",templateCssPath:null,templatePath:null,allowResizeX:true,allowResizeY:true,resizeGhost:null,initialResizeCoords:null,toggle:"plain",toggleDuration:150,animationInProgress:false,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
dojo.lang.mixin(this,dojo.widget.HtmlWidget.Toggle[dojo.string.capitalize(this.toggle)]||dojo.widget.HtmlWidget.Toggle.Plain);
},getContainerHeight:function(){
dj_unimplemented("dojo.widget.HtmlWidget.getContainerHeight");
},getContainerWidth:function(){
return this.parent.domNode.offsetWidth;
},setNativeHeight:function(_4d4){
var ch=this.getContainerHeight();
},startResize:function(_4d6){
_4d6.offsetLeft=dojo.html.totalOffsetLeft(this.domNode);
_4d6.offsetTop=dojo.html.totalOffsetTop(this.domNode);
_4d6.innerWidth=dojo.html.getInnerWidth(this.domNode);
_4d6.innerHeight=dojo.html.getInnerHeight(this.domNode);
if(!this.resizeGhost){
this.resizeGhost=document.createElement("div");
var rg=this.resizeGhost;
rg.style.position="absolute";
rg.style.backgroundColor="white";
rg.style.border="1px solid black";
dojo.html.setOpacity(rg,0.3);
dojo.html.body().appendChild(rg);
}
with(this.resizeGhost.style){
left=_4d6.offsetLeft+"px";
top=_4d6.offsetTop+"px";
}
this.initialResizeCoords=_4d6;
this.resizeGhost.style.display="";
this.updateResize(_4d6,true);
},updateResize:function(_4d8,_4d9){
var dx=_4d8.x-this.initialResizeCoords.x;
var dy=_4d8.y-this.initialResizeCoords.y;
with(this.resizeGhost.style){
if((this.allowResizeX)||(_4d9)){
width=this.initialResizeCoords.innerWidth+dx+"px";
}
if((this.allowResizeY)||(_4d9)){
height=this.initialResizeCoords.innerHeight+dy+"px";
}
}
},endResize:function(_4dc){
var dx=_4dc.x-this.initialResizeCoords.x;
var dy=_4dc.y-this.initialResizeCoords.y;
with(this.domNode.style){
if(this.allowResizeX){
width=this.initialResizeCoords.innerWidth+dx+"px";
}
if(this.allowResizeY){
height=this.initialResizeCoords.innerHeight+dy+"px";
}
}
this.resizeGhost.style.display="none";
},resizeSoon:function(){
if(this.isVisible()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},_old_buildFromTemplate:dojo.widget.DomWidget.prototype.buildFromTemplate,buildFromTemplate:function(args,frag){
if(dojo.widget.DomWidget.templates[this.widgetType]){
var ot=dojo.widget.DomWidget.templates[this.widgetType];
dojo.widget.DomWidget.templates[this.widgetType]={};
}
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
if(args["templatepath"]){
args["templatePath"]=args["templatepath"];
}
dojo.widget.buildFromTemplate(this,args["templatePath"],args["templateCssPath"]);
this._old_buildFromTemplate(args,frag);
dojo.widget.DomWidget.templates[this.widgetType]=ot;
},destroyRendering:function(_4e4){
try{
var _4e5=this.domNode.parentNode.removeChild(this.domNode);
if(!_4e4){
dojo.event.browser.clean(_4e5);
}
delete _4e5;
}
catch(e){
}
},isVisible:function(){
return dojo.html.isVisible(this.domNode);
},doToggle:function(){
this.isVisible()?this.hide():this.show();
},show:function(){
this.animationInProgress=true;
this.showMe();
},onShow:function(){
this.animationInProgress=false;
},hide:function(){
this.animationInProgress=true;
this.hideMe();
},onHide:function(){
this.animationInProgress=false;
}});
dojo.widget.HtmlWidget.Toggle={};
dojo.widget.HtmlWidget.Toggle.Plain={showMe:function(){
dojo.html.show(this.domNode);
if(dojo.lang.isFunction(this.onShow)){
this.onShow();
}
},hideMe:function(){
dojo.html.hide(this.domNode);
if(dojo.lang.isFunction(this.onHide)){
this.onHide();
}
}};
dojo.widget.HtmlWidget.Toggle.Fade={showMe:function(){
dojo.fx.html.fadeShow(this.domNode,this.toggleDuration,dojo.lang.hitch(this,this.onShow));
},hideMe:function(){
dojo.fx.html.fadeHide(this.domNode,this.toggleDuration,dojo.lang.hitch(this,this.onHide));
}};
dojo.widget.HtmlWidget.Toggle.Wipe={showMe:function(){
dojo.fx.html.wipeIn(this.domNode,this.toggleDuration,dojo.lang.hitch(this,this.onShow));
},hideMe:function(){
dojo.fx.html.wipeOut(this.domNode,this.toggleDuration,dojo.lang.hitch(this,this.onHide));
}};
dojo.widget.HtmlWidget.Toggle.Explode={showMe:function(){
dojo.fx.html.explode(this.explodeSrc,this.domNode,this.toggleDuration,dojo.lang.hitch(this,this.onShow));
},hideMe:function(){
dojo.fx.html.implode(this.domNode,this.explodeSrc,this.toggleDuration,dojo.lang.hitch(this,this.onHide));
}};
dojo.hostenv.conditionalLoadModule({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"]});
dojo.hostenv.moduleLoaded("dojo.widget.*");
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error"];
dojo.io.Request=function(url,_4e7,_4e8,_4e9){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_4e7){
this.mimetype=_4e7;
}
if(_4e8){
this.transport=_4e8;
}
if(arguments.length>=4){
this.changeUrl=_4e9;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,evt){
},error:function(type,_4ee){
},handle:function(){
},abort:function(){
},fromKwArgs:function(_4ef){
if(_4ef["url"]){
_4ef.url=_4ef.url.toString();
}
if(!_4ef["method"]&&_4ef["formNode"]&&_4ef["formNode"].method){
_4ef.method=_4ef["formNode"].method;
}
if(!_4ef["handle"]&&_4ef["handler"]){
_4ef.handle=_4ef.handler;
}
if(!_4ef["load"]&&_4ef["loaded"]){
_4ef.load=_4ef.loaded;
}
if(!_4ef["changeUrl"]&&_4ef["changeURL"]){
_4ef.changeUrl=_4ef.changeURL;
}
_4ef.encoding=dojo.lang.firstValued(_4ef["encoding"],djConfig["bindEncoding"],"");
_4ef.sendTransport=dojo.lang.firstValued(_4ef["sendTransport"],djConfig["ioSendTransport"],true);
var _4f0=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_4f0(_4ef[fn])){
continue;
}
if(_4f0(_4ef["handle"])){
_4ef[fn]=_4ef.handle;
}
}
dojo.lang.mixin(this,_4ef);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_4f7){
if(!(_4f7 instanceof dojo.io.Request)){
try{
_4f7=new dojo.io.Request(_4f7);
}
catch(e){
dojo.debug(e);
}
}
var _4f8="";
if(_4f7["transport"]){
_4f8=_4f7["transport"];
if(!this[_4f8]){
return _4f7;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_4f7))){
_4f8=tmp;
}
}
if(_4f8==""){
return _4f7;
}
}
this[_4f8].bind(_4f7);
_4f7.bindSuccess=true;
return _4f7;
};
dojo.io.queueBind=function(_4fb){
if(!(_4fb instanceof dojo.io.Request)){
try{
_4fb=new dojo.io.Request(_4fb);
}
catch(e){
dojo.debug(e);
}
}
var _4fc=_4fb.load;
_4fb.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_4fc.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _4fe=_4fb.error;
_4fb.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_4fe.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_4fb);
dojo.io._dispatchNextQueueBind();
return _4fb;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
dojo.io.bind(dojo.io._bindQueue.shift());
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_501){
var _502=new Object();
var _503="";
var enc=/utf/i.test(_501||"")?encodeURIComponent:dojo.string.encodeAscii;
for(var x in map){
if(!_502[x]){
_503+=enc(x)+"="+enc(map[x])+"&";
}
}
return _503;
};
dojo.provide("dojo.io.BrowserIO");
dojo.require("dojo.io");
dojo.require("dojo.lang");
dojo.require("dojo.dom");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
dojo.io.checkChildrenForFile=function(node){
var _507=false;
var _508=node.getElementsByTagName("input");
dojo.lang.forEach(_508,function(_509){
if(_507){
return;
}
if(_509.getAttribute("type")=="file"){
_507=true;
}
});
return _507;
};
dojo.io.formHasFile=function(_50a){
return dojo.io.checkChildrenForFile(_50a);
};
dojo.io.encodeForm=function(_50b,_50c){
if((!_50b)||(!_50b.tagName)||(!_50b.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
var enc=/utf/i.test(_50c||"")?encodeURIComponent:dojo.string.encodeAscii;
var _50e=[];
for(var i=0;i<_50b.elements.length;i++){
var elm=_50b.elements[i];
if(elm.disabled||elm.tagName.toLowerCase()=="fieldset"||!elm.name){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_50e.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_50e.push(name+"="+enc(elm.value));
}
}else{
if(!dojo.lang.inArray(type,["file","submit","reset","button"])){
_50e.push(name+"="+enc(elm.value));
}
}
}
}
var _514=_50b.getElementsByTagName("input");
for(var i=0;i<_514.length;i++){
var _515=_514[i];
if(_515.type.toLowerCase()=="image"&&_515.form==_50b){
var name=enc(_515.name);
_50e.push(name+"="+enc(_515.value));
_50e.push(name+".x=0");
_50e.push(name+".y=0");
}
}
return _50e.join("&")+"&";
};
dojo.io.setIFrameSrc=function(_516,src,_518){
try{
var r=dojo.render.html;
if(!_518){
if(r.safari){
_516.location=src;
}else{
frames[_516.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_516.contentWindow.document;
}else{
if(r.moz){
idoc=_516.contentWindow;
}else{
if(r.safari){
idoc=_516.document;
}
}
}
idoc.location.replace(src);
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.io.XMLHTTPTransport=new function(){
var _51b=this;
this.initialHref=window.location.href;
this.initialHash=window.location.hash;
this.moveForward=false;
var _51c={};
this.useCache=false;
this.preventCache=false;
this.historyStack=[];
this.forwardStack=[];
this.historyIframe=null;
this.bookmarkAnchor=null;
this.locationTimer=null;
function getCacheKey(url,_51e,_51f){
return url+"|"+_51e+"|"+_51f.toLowerCase();
}
function addToCache(url,_521,_522,http){
_51c[getCacheKey(url,_521,_522)]=http;
}
function getFromCache(url,_525,_526){
return _51c[getCacheKey(url,_525,_526)];
}
this.clearCache=function(){
_51c={};
};
function doLoad(_527,http,url,_52a,_52b){
if((http.status==200)||(location.protocol=="file:"&&http.status==0)){
var ret;
if(_527.method.toLowerCase()=="head"){
var _52d=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _52d;
};
var _52e=_52d.split(/[\r\n]+/g);
for(var i=0;i<_52e.length;i++){
var pair=_52e[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_527.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_527.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_527.mimetype=="application/xml")||(_527.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_52b){
addToCache(url,_52a,_527.method,http);
}
_527[(typeof _527.load=="function")?"load":"handle"]("load",ret,http);
}else{
var _531=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_527[(typeof _527.error=="function")?"error":"handle"]("error",_531,http);
}
}
function setHeaders(http,_533){
if(_533["headers"]){
for(var _534 in _533["headers"]){
if(_534.toLowerCase()=="content-type"&&!_533["contentType"]){
_533["contentType"]=_533["headers"][_534];
}else{
http.setRequestHeader(_534,_533["headers"][_534]);
}
}
}
}
this.addToHistory=function(args){
var _536=args["back"]||args["backButton"]||args["handle"];
var hash=null;
if(!this.historyIframe){
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
(document.body||document.getElementsByTagName("body")[0]).appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if((!args["changeUrl"])||(dojo.render.html.ie)){
var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
setTimeout("window.location.href = '"+hash+"';",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
var _539=_536;
var lh=null;
var hsl=this.historyStack.length-1;
if(hsl>=0){
while(!this.historyStack[hsl]["urlHash"]){
hsl--;
}
lh=this.historyStack[hsl]["urlHash"];
}
if(lh){
_536=function(){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+lh+"';",1);
}
_539();
};
}
this.forwardStack=[];
var _53c=args["forward"]||args["forwardButton"];
var tfw=function(){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_53c){
_53c();
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.io.XMLHTTPTransport.checkLocation();",200);
}
}
}
}
this.historyStack.push({"url":url,"callback":_536,"kwArgs":args,"urlHash":hash});
};
this.checkLocation=function(){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash)||(window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
};
this.iframeLoaded=function(evt,_540){
var isp=_540.href.split("?");
if(isp.length<2){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
var _542=isp[1];
if(this.moveForward){
this.moveForward=false;
return;
}
var last=this.historyStack.pop();
if(!last){
if(this.forwardStack.length>0){
var next=this.forwardStack[this.forwardStack.length-1];
if(_542==next.url.split("?")[1]){
this.handleForwardButton();
}
}
return;
}
this.historyStack.push(last);
if(this.historyStack.length>=2){
if(isp[1]==this.historyStack[this.historyStack.length-2].url.split("?")[1]){
this.handleBackButton();
}
}else{
this.handleBackButton();
}
};
this.handleBackButton=function(){
var last=this.historyStack.pop();
if(!last){
return;
}
if(last["callback"]){
last.callback();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(last);
};
this.handleForwardButton=function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
};
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setInterval("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
for(var x=this.inFlight.length-1;x>=0;x--){
var tif=this.inFlight[x];
if(!tif){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
if(this.inFlight.length==0){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
}
}
};
var _549=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_54a){
return _549&&dojo.lang.inArray((_54a["mimetype"]||"".toLowerCase()),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&dojo.lang.inArray(_54a["method"].toLowerCase(),["post","get","head"])&&!(_54a["formNode"]&&dojo.io.formHasFile(_54a["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_54b){
if(!_54b["url"]){
if(!_54b["formNode"]&&(_54b["backButton"]||_54b["back"]||_54b["changeUrl"]||_54b["watchForURL"])&&(!djConfig.preventBackButtonFix)){
this.addToHistory(_54b);
return true;
}
}
var url=_54b.url;
var _54d="";
if(_54b["formNode"]){
var ta=_54b.formNode.getAttribute("action");
if((ta)&&(!_54b["url"])){
url=ta;
}
var tp=_54b.formNode.getAttribute("method");
if((tp)&&(!_54b["method"])){
_54b.method=tp;
}
_54d+=dojo.io.encodeForm(_54b.formNode,_54b.encoding);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_54b["file"]){
_54b.method="post";
}
if(!_54b["method"]){
_54b.method="get";
}
if(_54b.method.toLowerCase()=="get"){
_54b.multipart=false;
}else{
if(_54b["file"]){
_54b.multipart=true;
}else{
if(!_54b["multipart"]){
_54b.multipart=false;
}
}
}
if(_54b["backButton"]||_54b["back"]||_54b["changeUrl"]){
this.addToHistory(_54b);
}
var _550=_54b["content"]||{};
if(_54b.sendTransport){
_550["dojo.transport"]="xmlhttp";
}
do{
if(_54b.postContent){
_54d=_54b.postContent;
break;
}
if(_550){
_54d+=dojo.io.argsFromMap(_550,_54b.encoding);
}
if(_54b.method.toLowerCase()=="get"||!_54b.multipart){
break;
}
var t=[];
if(_54d.length){
var q=_54d.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_54b.file){
if(dojo.lang.isArray(_54b.file)){
for(var i=0;i<_54b.file.length;++i){
var o=_54b.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_54b.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_54d=t.join("\r\n");
}
}while(false);
var _556=_54b["sync"]?false:true;
var _557=_54b["preventCache"]||(this.preventCache==true&&_54b["preventCache"]!=false);
var _558=_54b["useCache"]==true||(this.useCache==true&&_54b["useCache"]!=false);
if(!_557&&_558){
var _559=getFromCache(url,_54d,_54b.method);
if(_559){
doLoad(_54b,_559,url,_54d,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject();
var _55b=false;
if(_556){
this.inFlight.push({"req":_54b,"http":http,"url":url,"query":_54d,"useCache":_558});
this.startWatchingInFlight();
}
if(_54b.method.toLowerCase()=="post"){
http.open("POST",url,_556);
setHeaders(http,_54b);
http.setRequestHeader("Content-Type",_54b.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_54b.contentType||"application/x-www-form-urlencoded"));
http.send(_54d);
}else{
var _55c=url;
if(_54d!=""){
_55c+=(_55c.indexOf("?")>-1?"&":"?")+_54d;
}
if(_557){
_55c+=(dojo.string.endsWithAny(_55c,"?","&")?"":(_55c.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_54b.method.toUpperCase(),_55c,_556);
setHeaders(http,_54b);
http.send(null);
}
if(!_556){
doLoad(_54b,http,url,_54d,_558);
}
_54b.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_55e,days,path,_561,_562){
var _563=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_563=d.toGMTString();
}
_55e=escape(_55e);
document.cookie=name+"="+_55e+";"+(_563!=-1?" expires="+_563+";":"")+(path?"path="+path:"")+(_561?"; domain="+_561:"")+(_562?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.indexOf(name+"=");
if(idx==-1){
return null;
}
value=document.cookie.substring(idx+name.length+1);
var end=value.indexOf(";");
if(end==-1){
end=value.length;
}
value=value.substring(0,end);
value=unescape(value);
return value;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_56d,_56e,_56f){
if(arguments.length==5){
_56f=_56d;
_56d=null;
_56e=null;
}
var _570=[],cookie,value="";
if(!_56f){
cookie=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!cookie){
cookie={};
}
for(var prop in obj){
if(prop==null){
delete cookie[prop];
}else{
if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){
cookie[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in cookie){
_570.push(escape(prop)+"="+escape(cookie[prop]));
}
value=_570.join("&");
}
dojo.io.cookie.setCookie(name,value,days,path,_56d,_56e);
};
dojo.io.cookie.getObjectCookie=function(name){
var _573=null,cookie=dojo.io.cookie.getCookie(name);
if(cookie){
_573={};
var _574=cookie.split("&");
for(var i=0;i<_574.length;i++){
var pair=_574[i].split("=");
var _577=pair[1];
if(isNaN(_577)){
_577=unescape(pair[1]);
}
_573[unescape(pair[0])]=_577;
}
}
return _573;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _578=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_578=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.hostenv.conditionalLoadModule({common:["dojo.io",false,false],rhino:["dojo.io.RhinoIO",false,false],browser:[["dojo.io.BrowserIO",false,false],["dojo.io.cookie",false,false]]});
dojo.hostenv.moduleLoaded("dojo.io.*");
dojo.provide("dojo.math.curves");
dojo.require("dojo.math");
dojo.math.curves={Line:function(_579,end){
this.start=_579;
this.end=end;
this.dimensions=_579.length;
for(var i=0;i<_579.length;i++){
_579[i]=Number(_579[i]);
}
for(var i=0;i<end.length;i++){
end[i]=Number(end[i]);
}
this.getValue=function(n){
var _57d=new Array(this.dimensions);
for(var i=0;i<this.dimensions;i++){
_57d[i]=((this.end[i]-this.start[i])*n)+this.start[i];
}
return _57d;
};
return this;
},Bezier:function(pnts){
this.getValue=function(step){
if(step>=1){
return this.p[this.p.length-1];
}
if(step<=0){
return this.p[0];
}
var _581=new Array(this.p[0].length);
for(var k=0;j<this.p[0].length;k++){
_581[k]=0;
}
for(var j=0;j<this.p[0].length;j++){
var C=0;
var D=0;
for(var i=0;i<this.p.length;i++){
C+=this.p[i][j]*this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,i);
}
for(var l=0;l<this.p.length;l++){
D+=this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,l);
}
_581[j]=C/D;
}
return _581;
};
this.p=pnts;
return this;
},CatmullRom:function(pnts,c){
this.getValue=function(step){
var _58b=step*(this.p.length-1);
var node=Math.floor(_58b);
var _58d=_58b-node;
var i0=node-1;
if(i0<0){
i0=0;
}
var i=node;
var i1=node+1;
if(i1>=this.p.length){
i1=this.p.length-1;
}
var i2=node+2;
if(i2>=this.p.length){
i2=this.p.length-1;
}
var u=_58d;
var u2=_58d*_58d;
var u3=_58d*_58d*_58d;
var _595=new Array(this.p[0].length);
for(var k=0;k<this.p[0].length;k++){
var x1=(-this.c*this.p[i0][k])+((2-this.c)*this.p[i][k])+((this.c-2)*this.p[i1][k])+(this.c*this.p[i2][k]);
var x2=(2*this.c*this.p[i0][k])+((this.c-3)*this.p[i][k])+((3-2*this.c)*this.p[i1][k])+(-this.c*this.p[i2][k]);
var x3=(-this.c*this.p[i0][k])+(this.c*this.p[i1][k]);
var x4=this.p[i][k];
_595[k]=x1*u3+x2*u2+x3*u+x4;
}
return _595;
};
if(!c){
this.c=0.7;
}else{
this.c=c;
}
this.p=pnts;
return this;
},Arc:function(_59b,end,ccw){
var _59e=dojo.math.points.midpoint(_59b,end);
var _59f=dojo.math.points.translate(dojo.math.points.invert(_59e),_59b);
var rad=Math.sqrt(Math.pow(_59f[0],2)+Math.pow(_59f[1],2));
var _5a1=dojo.math.radToDeg(Math.atan(_59f[1]/_59f[0]));
if(_59f[0]<0){
_5a1-=90;
}else{
_5a1+=90;
}
dojo.math.curves.CenteredArc.call(this,_59e,rad,_5a1,_5a1+(ccw?-180:180));
},CenteredArc:function(_5a2,_5a3,_5a4,end){
this.center=_5a2;
this.radius=_5a3;
this.start=_5a4||0;
this.end=end;
this.getValue=function(n){
var _5a7=new Array(2);
var _5a8=dojo.math.degToRad(this.start+((this.end-this.start)*n));
_5a7[0]=this.center[0]+this.radius*Math.sin(_5a8);
_5a7[1]=this.center[1]-this.radius*Math.cos(_5a8);
return _5a7;
};
return this;
},Circle:function(_5a9,_5aa){
dojo.math.curves.CenteredArc.call(this,_5a9,_5aa,0,360);
return this;
},Path:function(){
var _5ab=[];
var _5ac=[];
var _5ad=[];
var _5ae=0;
this.add=function(_5af,_5b0){
if(_5b0<0){
dojo.raise("dojo.math.curves.Path.add: weight cannot be less than 0");
}
_5ab.push(_5af);
_5ac.push(_5b0);
_5ae+=_5b0;
computeRanges();
};
this.remove=function(_5b1){
for(var i=0;i<_5ab.length;i++){
if(_5ab[i]==_5b1){
_5ab.splice(i,1);
_5ae-=_5ac.splice(i,1)[0];
break;
}
}
computeRanges();
};
this.removeAll=function(){
_5ab=[];
_5ac=[];
_5ae=0;
};
this.getValue=function(n){
var _5b4=false,value=0;
for(var i=0;i<_5ad.length;i++){
var r=_5ad[i];
if(n>=r[0]&&n<r[1]){
var subN=(n-r[0])/r[2];
value=_5ab[i].getValue(subN);
_5b4=true;
break;
}
}
if(!_5b4){
value=_5ab[_5ab.length-1].getValue(1);
}
for(j=0;j<i;j++){
value=dojo.math.points.translate(value,_5ab[j].getValue(1));
}
return value;
};
function computeRanges(){
var _5b8=0;
for(var i=0;i<_5ac.length;i++){
var end=_5b8+_5ac[i]/_5ae;
var len=end-_5b8;
_5ad[i]=[_5b8,end,len];
_5b8=end;
}
}
return this;
}};
dojo.provide("dojo.animation");
dojo.provide("dojo.animation.Animation");
dojo.require("dojo.lang");
dojo.require("dojo.math");
dojo.require("dojo.math.curves");
dojo.animation.Animation=function(_5bc,_5bd,_5be,_5bf,rate){
if(dojo.lang.isArray(_5bc)){
_5bc=new dojo.math.curves.Line(_5bc[0],_5bc[1]);
}
this.curve=_5bc;
this.duration=_5bd;
this.repeatCount=_5bf||0;
this.rate=rate||25;
if(_5be){
if(dojo.lang.isFunction(_5be.getValue)){
this.accel=_5be;
}else{
var i=0.35*_5be+0.5;
this.accel=new dojo.math.curves.CatmullRom([[0],[i],[1]],0.45);
}
}
};
dojo.lang.extend(dojo.animation.Animation,{curve:null,duration:0,repeatCount:0,accel:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,handler:null,_animSequence:null,_startTime:null,_endTime:null,_lastFrame:null,_timer:null,_percent:0,_active:false,_paused:false,_startRepeatCount:0,play:function(_5c2){
if(_5c2){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return;
}
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._lastFrame=this._startTime;
var e=new dojo.animation.AnimationEvent(this,null,this.curve.getValue(this._percent),this._startTime,this._startTime,this._endTime,this.duration,this._percent,0);
this._active=true;
this._paused=false;
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
e.type="begin";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onBegin=="function"){
this.onBegin(e);
}
}
e.type="play";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPlay=="function"){
this.onPlay(e);
}
if(this._animSequence){
this._animSequence._setCurrent(this);
}
this._cycle();
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return;
}
this._paused=true;
var e=new dojo.animation.AnimationEvent(this,"pause",this.curve.getValue(this._percent),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent,0);
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPause=="function"){
this.onPause(e);
}
},playPause:function(){
if(!this._active||this._paused){
this.play();
}else{
this.pause();
}
},gotoPercent:function(pct,_5c6){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_5c6){
this.play();
}
},stop:function(_5c7){
clearTimeout(this._timer);
var step=this._percent/100;
if(_5c7){
step=1;
}
var e=new dojo.animation.AnimationEvent(this,"stop",this.curve.getValue(step),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent,Math.round(fps));
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onStop=="function"){
this.onStop(e);
}
this._active=false;
this._paused=false;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
fps=1000/(curr-this._lastFrame);
this._lastFrame=curr;
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if(this.accel&&this.accel.getValue){
step=this.accel.getValue(step);
}
var e=new dojo.animation.AnimationEvent(this,"animate",this.curve.getValue(step),this._startTime,curr,this._endTime,this.duration,this._percent,Math.round(fps));
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onAnimate=="function"){
this.onAnimate(e);
}
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
e.type="end";
this._active=false;
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onEnd=="function"){
this.onEnd(e);
}
if(this.repeatCount>0){
this.repeatCount--;
this.play(true);
}else{
if(this.repeatCount==-1){
this.play(true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
if(this._animSequence){
this._animSequence._playNext();
}
}
}
}
}
}});
dojo.animation.AnimationEvent=function(anim,type,_5cf,_5d0,_5d1,_5d2,dur,pct,fps){
this.type=type;
this.animation=anim;
this.coords=_5cf;
this.x=_5cf[0];
this.y=_5cf[1];
this.z=_5cf[2];
this.startTime=_5d0;
this.currentTime=_5d1;
this.endTime=_5d2;
this.duration=dur;
this.percent=pct;
this.fps=fps;
};
dojo.lang.extend(dojo.animation.AnimationEvent,{coordsAsInts:function(){
var _5d6=new Array(this.coords.length);
for(var i=0;i<this.coords.length;i++){
_5d6[i]=Math.round(this.coords[i]);
}
return _5d6;
}});
dojo.animation.AnimationSequence=function(_5d8){
this._anims=[];
this.repeatCount=_5d8||0;
};
dojo.lang.extend(dojo.animation.AnimationSequence,{repeateCount:0,_anims:[],_currAnim:-1,onBegin:null,onEnd:null,onNext:null,handler:null,add:function(){
for(var i=0;i<arguments.length;i++){
this._anims.push(arguments[i]);
arguments[i]._animSequence=this;
}
},remove:function(anim){
for(var i=0;i<this._anims.length;i++){
if(this._anims[i]==anim){
this._anims[i]._animSequence=null;
this._anims.splice(i,1);
break;
}
}
},removeAll:function(){
for(var i=0;i<this._anims.length;i++){
this._anims[i]._animSequence=null;
}
this._anims=[];
this._currAnim=-1;
},clear:function(){
this.removeAll();
},play:function(_5dd){
if(this._anims.length==0){
return;
}
if(_5dd||!this._anims[this._currAnim]){
this._currAnim=0;
}
if(this._anims[this._currAnim]){
if(this._currAnim==0){
var e={type:"begin",animation:this._anims[this._currAnim]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onBegin=="function"){
this.onBegin(e);
}
}
this._anims[this._currAnim].play(_5dd);
}
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
}
},playPause:function(){
if(this._anims.length==0){
return;
}
if(this._currAnim==-1){
this._currAnim=0;
}
if(this._anims[this._currAnim]){
this._anims[this._currAnim].playPause();
}
},stop:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].stop();
}
},status:function(){
if(this._anims[this._currAnim]){
return this._anims[this._currAnim].status();
}else{
return "stopped";
}
},_setCurrent:function(anim){
for(var i=0;i<this._anims.length;i++){
if(this._anims[i]==anim){
this._currAnim=i;
break;
}
}
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return;
}
this._currAnim++;
if(this._anims[this._currAnim]){
var e={type:"next",animation:this._anims[this._currAnim]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onNext=="function"){
this.onNext(e);
}
this._anims[this._currAnim].play(true);
}else{
var e={type:"end",animation:this._anims[this._anims.length-1]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onEnd=="function"){
this.onEnd(e);
}
if(this.repeatCount>0){
this._currAnim=0;
this.repeatCount--;
this._anims[this._currAnim].play(true);
}else{
if(this.repeatCount==-1){
this._currAnim=0;
this._anims[this._currAnim].play(true);
}else{
this._currAnim=-1;
}
}
}
}});
dojo.hostenv.conditionalLoadModule({common:["dojo.animation.Animation",false,false]});
dojo.hostenv.moduleLoaded("dojo.animation.*");
dojo.provide("dojo.fx.html");
dojo.require("dojo.html");
dojo.require("dojo.style");
dojo.require("dojo.lang");
dojo.require("dojo.animation.*");
dojo.require("dojo.event.*");
dojo.require("dojo.graphics.color");
dojo.fx.duration=500;
dojo.fx.html._makeFadeable=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
dojo.fx.html.fadeOut=function(node,_5e4,_5e5,_5e6){
return dojo.fx.html.fade(node,_5e4,dojo.style.getOpacity(node),0,_5e5,_5e6);
};
dojo.fx.html.fadeIn=function(node,_5e8,_5e9,_5ea){
return dojo.fx.html.fade(node,_5e8,dojo.style.getOpacity(node),1,_5e9,_5ea);
};
dojo.fx.html.fadeHide=function(node,_5ec,_5ed,_5ee){
node=dojo.byId(node);
if(!_5ec){
_5ec=150;
}
return dojo.fx.html.fadeOut(node,_5ec,function(node){
node.style.display="none";
if(typeof _5ed=="function"){
_5ed(node);
}
});
};
dojo.fx.html.fadeShow=function(node,_5f1,_5f2,_5f3){
node=dojo.byId(node);
if(!_5f1){
_5f1=150;
}
node.style.display="block";
return dojo.fx.html.fade(node,_5f1,0,1,_5f2,_5f3);
};
dojo.fx.html.fade=function(node,_5f5,_5f6,_5f7,_5f8,_5f9){
node=dojo.byId(node);
dojo.fx.html._makeFadeable(node);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line([_5f6],[_5f7]),_5f5||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
dojo.style.setOpacity(node,e.x);
});
if(_5f8){
dojo.event.connect(anim,"onEnd",function(e){
_5f8(node,anim);
});
}
if(!_5f9){
anim.play(true);
}
return anim;
};
dojo.fx.html.slideTo=function(node,_5fe,_5ff,_600,_601){
if(!dojo.lang.isNumber(_5fe)){
var tmp=_5fe;
_5fe=_5ff;
_5ff=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slide(node,_5fe,[left,top],_5ff,_600,_601);
};
dojo.fx.html.slideBy=function(node,_607,_608,_609,_60a){
if(!dojo.lang.isNumber(_607)){
var tmp=_607;
_607=_608;
_608=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slideTo(node,_607,[left+_608[0],top+_608[1]],_609,_60a);
};
dojo.fx.html.slide=function(node,_610,_611,_612,_613,_614){
if(!dojo.lang.isNumber(_610)){
var tmp=_610;
_610=_612;
_612=_611;
_611=tmp;
}
node=dojo.byId(node);
if(dojo.style.getComputedStyle(node,"position")=="static"){
node.style.position="relative";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_611,_612),_610||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
with(node.style){
left=e.x+"px";
top=e.y+"px";
}
});
if(_613){
dojo.event.connect(anim,"onEnd",function(e){
_613(node,anim);
});
}
if(!_614){
anim.play(true);
}
return anim;
};
dojo.fx.html.colorFadeIn=function(node,_61a,_61b,_61c,_61d,_61e){
if(!dojo.lang.isNumber(_61a)){
var tmp=_61a;
_61a=_61b;
_61b=tmp;
}
node=dojo.byId(node);
var _620=dojo.html.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _622=bg=="transparent"||bg=="rgba(0, 0, 0, 0)";
while(_620.length>3){
_620.pop();
}
var rgb=new dojo.graphics.color.Color(_61b).toRgb();
var anim=dojo.fx.html.colorFade(node,_61a||dojo.fx.duration,_61b,_620,_61d,true);
dojo.event.connect(anim,"onEnd",function(e){
if(_622){
node.style.backgroundColor="transparent";
}
});
if(_61c>0){
node.style.backgroundColor="rgb("+rgb.join(",")+")";
if(!_61e){
setTimeout(function(){
anim.play(true);
},_61c);
}
}else{
if(!_61e){
anim.play(true);
}
}
return anim;
};
dojo.fx.html.highlight=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeFrom=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeOut=function(node,_627,_628,_629,_62a,_62b){
if(!dojo.lang.isNumber(_627)){
var tmp=_627;
_627=_628;
_628=tmp;
}
node=dojo.byId(node);
var _62d=new dojo.graphics.color.Color(dojo.html.getBackgroundColor(node)).toRgb();
var rgb=new dojo.graphics.color.Color(_628).toRgb();
var anim=dojo.fx.html.colorFade(node,_627||dojo.fx.duration,_62d,rgb,_62a,_629>0||_62b);
if(_629>0){
node.style.backgroundColor="rgb("+_62d.join(",")+")";
if(!_62b){
setTimeout(function(){
anim.play(true);
},_629);
}
}
return anim;
};
dojo.fx.html.unhighlight=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFadeTo=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFade=function(node,_631,_632,_633,_634,_635){
if(!dojo.lang.isNumber(_631)){
var tmp=_631;
_631=_633;
_633=_632;
_632=tmp;
}
node=dojo.byId(node);
var _637=new dojo.graphics.color.Color(_632).toRgb();
var _638=new dojo.graphics.color.Color(_633).toRgb();
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_637,_638),_631||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.backgroundColor="rgb("+e.coordsAsInts().join(",")+")";
});
if(_634){
dojo.event.connect(anim,"onEnd",function(e){
_634(node,anim);
});
}
if(!_635){
anim.play(true);
}
return anim;
};
dojo.fx.html.wipeShow=function(node,_63d,_63e,_63f){
node=dojo.byId(node);
var _640=dojo.html.getStyle(node,"overflow");
node.style.overflow="hidden";
node.style.height=0;
dojo.html.show(node);
var anim=new dojo.animation.Animation([[0],[node.scrollHeight]],_63d||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=e.x+"px";
});
dojo.event.connect(anim,"onEnd",function(){
node.style.overflow=_640;
node.style.height="auto";
if(_63e){
_63e(node,anim);
}
});
if(!_63f){
anim.play();
}
return anim;
};
dojo.fx.html.wipeHide=function(node,_644,_645,_646){
node=dojo.byId(node);
var _647=dojo.html.getStyle(node,"overflow");
node.style.overflow="hidden";
var anim=new dojo.animation.Animation([[node.offsetHeight],[0]],_644||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=e.x+"px";
});
dojo.event.connect(anim,"onEnd",function(){
node.style.overflow=_647;
dojo.html.hide(node);
if(_645){
_645(node,anim);
}
});
if(!_646){
anim.play();
}
return anim;
};
dojo.fx.html.wiper=function(node,_64b){
this.node=dojo.byId(node);
if(_64b){
dojo.event.connect(dojo.byId(_64b),"onclick",this,"toggle");
}
};
dojo.lang.extend(dojo.fx.html.wiper,{duration:dojo.fx.duration,_anim:null,toggle:function(){
if(!this._anim){
var type="wipe"+(dojo.html.isVisible(this.node)?"Hide":"Show");
this._anim=dojo.fx[type](this.node,this.duration,dojo.lang.hitch(this,"_callback"));
}
},_callback:function(){
this._anim=null;
}});
dojo.fx.html.wipeIn=function(node,_64e,_64f,_650){
node=dojo.byId(node);
var _651=dojo.html.getStyle(node,"height");
dojo.html.show(node);
var _652=node.offsetHeight;
var anim=dojo.fx.html.wipeInToHeight(node,_64e,_652,function(e){
node.style.height=_651||"auto";
if(_64f){
_64f(node,anim);
}
},_650);
};
dojo.fx.html.wipeInToHeight=function(node,_656,_657,_658,_659){
node=dojo.byId(node);
var _65a=dojo.html.getStyle(node,"overflow");
node.style.height="0px";
node.style.display="none";
if(_65a=="visible"){
node.style.overflow="hidden";
}
var _65b=dojo.lang.inArray(node.tagName.toLowerCase(),["tr","td","th"])?"":"block";
node.style.display=_65b;
var anim=new dojo.animation.Animation(new dojo.math.curves.Line([0],[_657]),_656||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=Math.round(e.x)+"px";
});
dojo.event.connect(anim,"onEnd",function(e){
if(_65a!="visible"){
node.style.overflow=_65a;
}
if(_658){
_658(node,anim);
}
});
if(!_659){
anim.play(true);
}
return anim;
};
dojo.fx.html.wipeOut=function(node,_660,_661,_662){
node=dojo.byId(node);
var _663=dojo.html.getStyle(node,"overflow");
var _664=dojo.html.getStyle(node,"height");
var _665=node.offsetHeight;
node.style.overflow="hidden";
var anim=new dojo.animation.Animation(new dojo.math.curves.Line([_665],[0]),_660||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=Math.round(e.x)+"px";
});
dojo.event.connect(anim,"onEnd",function(e){
node.style.display="none";
node.style.overflow=_663;
node.style.height=_664||"auto";
if(_661){
_661(node,anim);
}
});
if(!_662){
anim.play(true);
}
return anim;
};
dojo.fx.html.explode=function(_669,_66a,_66b,_66c,_66d){
var _66e=dojo.html.toCoordinateArray(_669);
var _66f=document.createElement("div");
with(_66f.style){
position="absolute";
border="1px solid black";
display="none";
}
dojo.html.body().appendChild(_66f);
_66a=dojo.byId(_66a);
with(_66a.style){
visibility="hidden";
display="block";
}
var _670=dojo.html.toCoordinateArray(_66a);
with(_66a.style){
display="none";
visibility="visible";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_66e,_670),_66b||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_66f.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_66f.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_66a.style.display="block";
_66f.parentNode.removeChild(_66f);
if(_66c){
_66c(_66a,anim);
}
});
if(!_66d){
anim.play();
}
return anim;
};
dojo.fx.html.implode=function(_674,end,_676,_677,_678){
var _679=dojo.html.toCoordinateArray(_674);
var _67a=dojo.html.toCoordinateArray(end);
_674=dojo.byId(_674);
var _67b=document.createElement("div");
with(_67b.style){
position="absolute";
border="1px solid black";
display="none";
}
dojo.html.body().appendChild(_67b);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_679,_67a),_676||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_674.style.display="none";
_67b.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_67b.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_67b.parentNode.removeChild(_67b);
if(_677){
_677(_674,anim);
}
});
if(!_678){
anim.play();
}
return anim;
};
dojo.fx.html.Exploder=function(_67f,_680){
_67f=dojo.byId(_67f);
_680=dojo.byId(_680);
var _681=this;
this.waitToHide=500;
this.timeToShow=100;
this.waitToShow=200;
this.timeToHide=70;
this.autoShow=false;
this.autoHide=false;
var _682=null;
var _683=null;
var _684=null;
var _685=null;
var _686=null;
var _687=null;
this.showing=false;
this.onBeforeExplode=null;
this.onAfterExplode=null;
this.onBeforeImplode=null;
this.onAfterImplode=null;
this.onExploding=null;
this.onImploding=null;
this.timeShow=function(){
clearTimeout(_684);
_684=setTimeout(_681.show,_681.waitToShow);
};
this.show=function(){
clearTimeout(_684);
clearTimeout(_685);
if((_683&&_683.status()=="playing")||(_682&&_682.status()=="playing")||_681.showing){
return;
}
if(typeof _681.onBeforeExplode=="function"){
_681.onBeforeExplode(_67f,_680);
}
_682=dojo.fx.html.explode(_67f,_680,_681.timeToShow,function(e){
_681.showing=true;
if(typeof _681.onAfterExplode=="function"){
_681.onAfterExplode(_67f,_680);
}
});
if(typeof _681.onExploding=="function"){
dojo.event.connect(_682,"onAnimate",this,"onExploding");
}
};
this.timeHide=function(){
clearTimeout(_684);
clearTimeout(_685);
if(_681.showing){
_685=setTimeout(_681.hide,_681.waitToHide);
}
};
this.hide=function(){
clearTimeout(_684);
clearTimeout(_685);
if(_682&&_682.status()=="playing"){
return;
}
_681.showing=false;
if(typeof _681.onBeforeImplode=="function"){
_681.onBeforeImplode(_67f,_680);
}
_683=dojo.fx.html.implode(_680,_67f,_681.timeToHide,function(e){
if(typeof _681.onAfterImplode=="function"){
_681.onAfterImplode(_67f,_680);
}
});
if(typeof _681.onImploding=="function"){
dojo.event.connect(_683,"onAnimate",this,"onImploding");
}
};
dojo.event.connect(_67f,"onclick",function(e){
if(_681.showing){
_681.hide();
}else{
_681.show();
}
});
dojo.event.connect(_67f,"onmouseover",function(e){
if(_681.autoShow){
_681.timeShow();
}
});
dojo.event.connect(_67f,"onmouseout",function(e){
if(_681.autoHide){
_681.timeHide();
}
});
dojo.event.connect(_680,"onmouseover",function(e){
clearTimeout(_685);
});
dojo.event.connect(_680,"onmouseout",function(e){
if(_681.autoHide){
_681.timeHide();
}
});
dojo.event.connect(document.documentElement||dojo.html.body(),"onclick",function(e){
if(_681.autoHide&&_681.showing&&!dojo.dom.isDescendantOf(e.target,_680)&&!dojo.dom.isDescendantOf(e.target,_67f)){
_681.hide();
}
});
return this;
};
dojo.lang.mixin(dojo.fx,dojo.fx.html);
dojo.hostenv.conditionalLoadModule({browser:["dojo.fx.html"]});
dojo.hostenv.moduleLoaded("dojo.fx.*");

