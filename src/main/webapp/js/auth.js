function hasAuthority(name){
  for(var index=0;index<authorities.length;index++){
    if(authorities[index]['name']==name){
      return true;
    }
  }
  
  return false;
}

function checkAuth(){
  authorities = JSON.parse($('#authorities').text());
  var shows = $('[show]');
  shows.each(function(index,obj){
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
  });
}