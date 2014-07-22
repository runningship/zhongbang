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
//窗口调用的公共函数
function WinMin(){/*最小化*/
  hex.minimize();
}
function WinMax(){/*最大化*/
  hex.maximize();
}
function WinRevert(){/*恢复*/
  hex.restore();
}
function WinClose(){/*退出*/
  YW.ajax({
    type: 'POST',
    url: '/zb/c/user/logout',
    data:'',
    success: function(data){
      alert('正在关闭...');
      hex.close();
    },
    error:function(data){
      hex.close();
    }
  });
}
//相同class点击填充背景，导航选中状态
function aBtnNavFun(a){
  if($(document).on!=undefined){
    $(document).on('click', a, function(event) {
        $(a).removeClass('acurr');
        $(this).addClass('acurr');
    });
  }
}
//根据titile可拖动窗口
document.addEventListener('mousemove', function (e) {
    if (e.target.classList.contains('title')) {
        hex.setAsTitleBarAreas(e.clientX, e.clientY);
    } else {
        hex.setAsTitleBarAreas(-1, -1);
        hex.setAsNonBorderAreas(-1, -1);
    }
}, false);
//设置网页内容高度
function setMainHeight(){
  var getTop,getFoot,getMain,getTopH=0,getMainH=0,getFootH=0;
  getTop=$('.header');
  getFoot=$('.footer');
  getMain=$('.mainer');
  if(getTop.length>0){
    getTopH=getTop.height()+12;
  }
  if(getFoot.length>0){
    getFootH=getFoot.height()+1;
  }
  if(getMain.length>0){
    $('.mainer').css({
      top:getTopH,
      bottom:getFootH
    });
  }
}

$(document).ready(function() {
  //页面侧边动作
  aBtnNavFun('.aNavBtn');
  //改善btn-group的操作感受
  if($('.btn-group').length>0){
    var btn_group_time=null;
    $('.btn-group').on('hide.bs.dropdown', function () {
      return false;
    }).on('show.bs.dropdown', function () {
      $(".btn-group:not(this)").removeClass('open');
    }).on('mousemove', function(event) {
      clearTimeout(btn_group_time);
    }).on('mouseout', function () {
      btn_group_time=setTimeout(function(){
        $(".btn-group").removeClass('open');
      },500);
    });
  } 

  //设置内容高度
  setMainHeight();
});




//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"),
decode;
decode=(reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
decode=decodeURIComponent(decode)
return decode;
}




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


//转换form表单成json
$.fn.serializeObject = function(){
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
}; 



























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
