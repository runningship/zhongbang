/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-05 08:55:23
 * @version $Id$

//屏蔽右键菜单
document.oncontextmenu = function (event){
    if(window.event){
        event = window.event;
    }try{
        var the = event.srcElement;
        if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA")){
            return false;
        }
        return true;
    }catch (e){
        return false; 
    } 
}
 */
//屏蔽全选
document.onselectstart = function (event){
    if(window.event){
        event = window.event;
    }try{
        var the = event.srcElement;
        if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA")){
            return false;
        }
        return true;
    }catch (e){
        return false; 
    } 
}
//屏蔽快捷键
$(document).keydown(function(event){  
//alert(event.keyCode)
    if ((event.altKey)&&   
       ((event.keyCode==37)||   //屏蔽 Alt+ 方向键 ←   
        (event.keyCode==39)))   //屏蔽 Alt+ 方向键 →   
    {   
       event.returnValue=false;   
       return false;  
    }   
    if(event.keyCode==8){  
       // return false; //屏蔽退格删除键    
    }  
    if(event.keyCode==116 || event.keyCode=='116'){  
        return false; //屏蔽F5刷新键   
    }  
    if((event.ctrlKey) && (event.keyCode==82)){  
        return false; //屏蔽alt+R   
    }  
});  
//根据titile可拖动窗口
document.addEventListener('mousemove', function (e) {
    if (e.target.classList.contains('title')) {
        hex.setAsTitleBarAreas(e.clientX, e.clientY);
    } else {
        hex.setAsTitleBarAreas(-1, -1);
        hex.setAsNonBorderAreas(-1, -1);
    }
}, false);



//判断字符串长度
function lens(s) {
var l = 0;
var a = s.split("");
for (var i=0;i<a.length;i++) {
if (a[i].charCodeAt(0)<299) {
l++;
} else {
l+=2;
}
}
return l;
}
//拆分字符串判断另个字符串是否存在
function splitIsStr(a,b,c){
var str=a;
if(b==''){b=','};
strs=str.split(b); //字符分割      
for (i=0;i<strs.length ;i++ ){    
if(strs[i]==c){
return 1;
break;
}else{
return 0;
}
} 
}





























/*-=-=-=-=-=-=-=-=-=[ cookies ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
//写入cookies
function SetCookie(name,value){
    var Days = 30;
    var exp = new Date(); 
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//读取cookies
function GetCookie(name){
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)) return unescape(arr[2]);
    else return null;
}
//删除cookies
function DelCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=GetCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//使用示例
//SetAsaiCookie("Asai","isj8.com");
//alert(GetAsaiCookie("Asai"));
