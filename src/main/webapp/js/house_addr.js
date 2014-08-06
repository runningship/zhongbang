/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-08-06 09:17:25
 * @version $Id$
 */

var j=-1;
var temp_str;

//由于document.getElementById经常被使用，我们用$来简写此函数
var $$$=function(node){
    return document.getElementById(node);
}

//对document.getElementsByTagName也做简写
var $$=function(node){
    return document.getElementsByTagName(node);
}

//异步读取数据库数据，将结果显示在suggest div中
function ajax_keyword(){
	YW.ajax({
    type: 'POST',
    url: '/zb/c/areas/search',
    data:'area='+$$$("area").value,
    success: function(data){
    	if(data){
        	$$$("suggest").style.display="block";
        }
        $$$("suggest").innerHTML=unescape(data);
        j=-1;
    }
  });

    // var xmlhttp;
    // try{
    //     xmlhttp=new XMLHttpRequest();
    //     }
    // catch(e){
    //     xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    //     }
    // xmlhttp.onreadystatechange=function(){
    // if (xmlhttp.readyState==4){
    //     if (xmlhttp.status==200){
    //         var data=xmlhttp.responseText;
    //         if(data){
    //         $$$("suggest").style.display="block";
    //         }
    //         //对结果进行unescape解码以防止中文乱码
    //         $$$("suggest").innerHTML=unescape(data);
    //         j=-1;
    //         }
    //     }
    // }
    // xmlhttp.open("post", "/zb/c/areas/search", true);
    // xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    // xmlhttp.send("area="+escape($$$("area").value));
}

//处理按键动作
function keyupdeal(e){
    var keyc;
    if(window.event){
        keyc=e.keyCode;
    }
    else if(e.which){
        keyc=e.which;
    }
    if(keyc!=40 && keyc!=38){
        ajax_keyword();
        temp_str=$$$("area").value;
        temp_strA=$$$("Address").value;
        temp_strQ=$$$("id_quyu").value;
    }
}

//设置建议框的样式表，高亮显示
function set_style(num){
    for(var i=0;i<$$("dd").length;i++){
        var li_node=$$("dd")[i];
        li_node.className="";
    }
    if(j>=0 && j<$$("dd").length){
        var i_node=$$("dd")[j];
        $$("dd")[j].className="select";
    }
}

//选定建议项时，高亮显示当前行
function mo(nodevalue){
    j=nodevalue;
    set_style(j);
}

//提交表单动作
function form_submit(){
    if(j>=0 && j<$$("dd").length){
        $$$("area").value=$$("strong")[j].childNodes[0].nodeValue;
        $$$("id_quyu").value=$$("big")[j].childNodes[0].nodeValue;
        $$$("Address").value=$$("small")[j].childNodes[0].nodeValue;
    }
    //document.search.submit();
    hide_suggest();
}

//隐藏建议框
function hide_suggest(){
    var nodes=document.body.childNodes
    for(var i=0;i<nodes.length;i++){
        if(nodes[i]!=$$$("area")){
            $$$("suggest").innerHTML="";
        }
    }
    $$$("suggest").style.display="none";
}
    
//搜索框按键按下事件的动作
function keydowndeal(e){
    var keyc;
    if(window.event){
        keyc=e.keyCode;
        }
    else if(e.which){
        keyc=e.which;
        }
    if(keyc==40 || keyc==38){
        if(keyc==40){
            if(j<$$("dd").length){
                j++;
                if(j>=$$("dd").length){
                    j=-1;
                }
            }
            if(j>=$$("dd").length){
                    j=-1;
            }
        }
        if(keyc==38){
            if(j>=0){
                j--;
                if(j<=-1){
                    j=$$("dd").length;
                }
            }
            else{
                j=$$("dd").length-1;
            }
        }
        set_style(j);
        if(j>=0 && j<$$("dd").length){
            $$$("area").value=$$("strong")[j].childNodes[0].nodeValue;
            $$$("id_quyu").value=$$("big")[j].childNodes[0].nodeValue;
            $$$("Address").value=$$("small")[j].childNodes[0].nodeValue;
        }else{
            //alert('asd');
            $$$("area").value=temp_str;
            $$$("id_quyu").value=temp_strQ;
            $$$("Address").value=temp_strA;
        }
    }else{
        if(keyc==9){
            $$$("Address").focus();
        }else if(keyc==13){
//          $$$("Address").focus();
        }
        hide_suggest();return false;
    }
}