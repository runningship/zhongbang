function buildHtmlWithJsonArray(id,json){
    var subCatagory = $('#'+id);
    var dhtml = subCatagory.html();
    $(subCatagory.children()).remove();
    //subCatagory.children()[0].remove();
    for(var i=0;i<json.length;i++){
        //subCatagory.append(html);
        var html = buildHtmlWithJson(dhtml,json[i]);
        subCatagory.append(html);
    }
}
function buildHtmlWithJson(dhtml,json){
    
    for(var key in json){
        var v = json[key];
        if(v==null){
            v="";
        }
        dhtml = dhtml.replace(new RegExp("\\${"+key+"}","gm"),v);
    }
    return dhtml;
}
function getParams() {
  //alert($('#hiddenParams').text());
  return $('#hiddenParams').text();
}

function xx(){
    print(aa);
}