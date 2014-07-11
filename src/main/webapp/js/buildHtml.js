function buildHtmlWithJsonArray(id,json,removeTemplate){
    var subCatagory = $('#'+id);
    var dhtml = subCatagory.html();
    var temp = subCatagory.children()[0];
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
                    $(obj).css('display','none');
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
