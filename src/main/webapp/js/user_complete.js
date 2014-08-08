function suggestUser(val){
    YW.ajax({
        type: 'POST',
        url: '/zb/c/user/search',
        data:'username='+val,
        success: function(data){
            if(data!='empty'){
                buildResult(data);
            }
        }
      });
    
}

function buildResult(options){
    var obj = $('#user_suggest');
    obj.empty();
    var data = '<select id="user_suggest_result" onclick="select(this.selectedOptions[0])" style="width:100px;max-height:300px;">'+options+'</select>'
    obj.append(data);
    obj.css('display','');
    var result = $('#user_suggest_result')[0];
    result.size=result.length;
}

function select(opt){
    $('#ywy').val(opt.textContent);
    $('#userId').val(opt.value);
    $('#fortel').val(opt.attributes['tel'].value);
    $('#ywy').focus();
    $('#user_suggest').empty();
}