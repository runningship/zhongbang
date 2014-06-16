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
    if(!removeTemplate){
        jtemp.css('display','none');
        subCatagory.prepend(jtemp);
    }

    var runscripts = subCatagory.find('.runscript');
    runscripts.each(function(index,obj){
        if(index>0){
            try{
            var val = eval(obj.textContent);
            obj.textContent = val;
            }catch(e){
                console.log(e);
            }
        }
    });
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
