function buildHtmlWithJsonArray(id,json,removeTemplate ,tempIndex){
    var subCatagory = $('#'+id);

    var dhtml = subCatagory.html();
    if(dhtml==undefined){
        return;
    }
    if(tempIndex==null){
        tempIndex=0;
    }
    var temp = subCatagory.children()[tempIndex];
    //var temp = $(first);
    var jtemp=$(temp);
    $(subCatagory).empty();

    for(var i=0;i<json.length;i++){
        var html = buildHtmlWithJson(temp,json[i] ,i);
        subCatagory.append(html);
    }

    var shows = subCatagory.find('[show]');
    shows.each(function(index,obj){
        // if(index>0){
            var script = $(obj).attr('show');
            try{
                if(eval(script)){
                    $(obj).css('display','');
                }else{
                    // $(obj).css('display','none');
                    $(obj).remove();
                }
            }catch(e){

            }
        // }
    });

    var runscripts = subCatagory.find('.runscript');
    runscripts.each(function(index,obj){
        // if(index>0){
            var val="";
            try{
                val = eval(obj.textContent);
                if(obj.tagName=='INPUT'){
                    obj.value = val;        
                }else{
                    // obj.textContent = val;  
                    obj.innerHTML = val;  
                }
            }catch(e){
                console.log(e);
                console.log(obj.textContent);
                obj.textContent = "";
            }
        // }
    });

    if(!removeTemplate){
        jtemp.css('display','none');
        subCatagory.prepend(jtemp);
    }
}
function buildHtmlWithJson(temp,json , rowIndex){
    temp.style.display='';
    var dhtml = temp.outerHTML;
    for(var key in json){
        var v = json[key];
        if(v==null){
            v="";
        }
        dhtml = dhtml.replace("${rowIndex}",rowIndex);
        dhtml = dhtml.replace(new RegExp("\\${"+key+"}","gm"),v);
    }
    return dhtml;
}

function getEnumTextByCode(enumArr,code){
    if(code==null){
        return "";
    }
    for(var i=0;i<enumArr.length;i++){
        if(enumArr[i]['code']==code){
            return enumArr[i]['name'];
        }
    }
}

//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
return (reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
}

window.blockAlert = window.alert;
window.alert = function(data){
    art.dialog.tips(data);
}

YW={
    options:{
        beforeSend: function(XMLHttpRequest){
            $('.block-submit').attr('disabled','disabled');
            $(document.body).append('<img src="/zb/style/image/ajax-loading.gif" style="display:block;margin-left:auto;margin-right:auto;" id="loading" />');
        },
        complete: function(XMLHttpRequest, textStatus){
          $('#loading').remove();
          $('.block-submit').removeAttr('disabled');
            searching=false;
        },
        error: function(data){
            if(data.status==500){
                alert('操作不成功，请联系管理员.');
            }else if(data.status==400){
                json = JSON.parse(data.responseText);
                if(json.type=='ParameterMissingError'){
                    var field = json.field;
                    var arr = $('[name="'+field+'"]');
                    var desc;
                    if(arr!=null && arr.length>0){
                        desc = $(arr[0]).attr('desc');
                    }
                    if(desc==undefined){
                        desc = field;
                    }
                    $(arr[0]).focus();
                    alert("请先填写 "+ desc);

                }else if(json.type=='ParameterTypeError'){
                    var field = json.field;
                    var arr = $('[name="'+field+'"]');
                    var desc;
                    if(arr!=null && arr.length>0){
                        desc = $(arr[0]).attr('desc');
                    }
                    if(desc==undefined){
                        desc = field;
                    }
                    $(arr[0]).focus();
                    alert(desc+json.msg);
                }else if(json.type=='LoginFromOtherException'){
                    alert("您的账号在别处登录，您已被迫下线..",5);
                }else{
                    alert(json['msg']);   
                }
                
            }else if(data.status!=0){
//            	alert(data.status);
                alert('请求服务失败，请稍后重试');
            }
        },
        success:function(data){
            alert('操作成功');
        }
    },
    ajax:function(options){
         if(options.beforeSend==undefined){
             options.beforeSend = YW.options.beforeSend;
         }
         if(options.complete==undefined){
             options.complete = YW.options.complete;
         }
        if(options.error==undefined){
            options.error = YW.options.error;
        }
        
        if(options.success==undefined){
            options.success = YW.options.success;
        }
        $.ajax(options);
    }
}

function fillData(data){
  $('.form-control').each(function(index,obj){
    $(obj).val(data[$(obj).attr('name')]);
  });
}

function fixTableFullScreen(ue){
  $(ue.container).find('.edui-editor-iframeholder').css('width','100%');
  $(ue.body).find('table').css('width','100%');
}


function onPasteHandler(editor,e){
    var data = e.clipboardData.getData('text/html');
    if(data==""){
        data = e.clipboardData.getData('text/plain');
    }

    data=data.replace('<!--table');
    var xx = $(data);
    var style=null;
    for(var i=0;i<xx.length;i++){
        if(xx[i].tagName=='STYLE'){
          style = xx[i];
          break;
        }
    }
    // editor.document.execCommand('paste');
    editor.document.body.appendChild(style);
    data = data+" <br/><br/>"
    editor.setContent(data,true);
    $(editor.document).find('td').css('border','solid 1px black');
    // editor.focus(true);
    return false;
}