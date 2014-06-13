function buildHtmlWithJsonArray(id,json,removeTemplate){
    var subCatagory = $('#'+id);
    var dhtml = subCatagory.html();
    var temp = subCatagory.children()[0];
    //var temp = $(first);
    var jtemp=$(temp);
    $(subCatagory).empty();

    for(var i=0;i<json.length;i++){
        var html = buildHtmlWithJson(temp,json[i]);
        subCatagory.append(html);
    }
    if(!removeTemplate){
        jtemp.css('display','none');
        subCatagory.prepend(jtemp);
    }
    
}
function buildHtmlWithJson(temp,json){
    temp.style.display='';
    var dhtml = temp.outerHTML;
    for(var key in json){
        var v = json[key];
        if(v==null){
            v="";
        }
        dhtml = dhtml.replace(new RegExp("\\${"+key+"}","gm"),v);
    }
    return dhtml;
}
