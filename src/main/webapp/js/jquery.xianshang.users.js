/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-13 17:38:04
 * @version $Id$
 */
var getUserTreeStr;
$(document).ready(function() {
    

    $.ajax({
        url:'/zb/user/getUserTree',
        data:'',
        timeout:3000,
        async:false,
        dataType:'json',
        beforeSend: function(XMLHttpRequest){
        },success:function (data, textStatus) {
            if(data!=""){
                getUserTreeStr=data;
                //alert(getUserTreeStr.result)
                //alert(getUserTreeStr.result[0].children)
            }
        },
        complete: function(XMLHttpRequest, textStatus){},
        error:function (XMLHttpRequest, textStatus, errorThrown) {}
    })

    if($(".get_comp").length>0){
        var getComp=$(".get_comp");
        //var dataObj=eval("("+data+")");//转换为json对象 
        //alert(data.result.length);//输出root的子对象数量
        getComp.empty();
        $.each(getUserTreeStr.result, function(index, item) {
            getComp.prepend('<option value="'+item.name+'">'+item.name+'</option>');
        });
        if($(".get_quyu").length>0){
            fun_get_quyu();
            getComp.change(function(){fun_get_quyu();});
        }
    }else{fun_get_quyu();}
function fun_get_quyu(){
    if($(".get_quyu").length>0){
        var getQuyu=$(".get_quyu");
        getQuyu.empty();
        //alert($(".get_comp").length)
        if($(".get_comp").length>0){
            var comp_index=$(".get_comp").prop('selectedIndex');
            //alert(getUserTreeStr.result[comp_index].children)
            $.each(getUserTreeStr.result[comp_index].children, function(index, item) {
                getQuyu.prepend('<option value="'+item.name+'">'+item.name+'</option>');
            });
        }else{
            $.each(getUserTreeStr.result, function(index, item) {
                //alert(item.name);
                var optgroups='';
                $.each(item.children, function(indexs, items) {
                    //alert(items.name);
                    optgroups=optgroups + '<option value="'+items.deptId+'">'+items.name+'</option>';
                }); 
                //alert(optgroups)
                getQuyu.prepend('<optgroup label="'+item.name+'">'+optgroups+'</optgroup>');
            });
        }
        if($(".get_user").length>0){
            fun_get_user();
            getQuyu.change(function(){fun_get_user();});
        }
    }
}
function fun_get_user(){
    if($(".get_user").length>0){
        var getUser=$(".get_user");
        var comp_index=$(".get_comp").prop('selectedIndex'),
        quyu_index=$(".get_quyu").prop('selectedIndex'),
        getUserTreeStr_User=getUserTreeStr.result[comp_index].children[quyu_index].children;
        //alert(getUserTreeStr.result[comp_index].children[quyu_index].children);//输出root的子对象数量
        getUser.empty();
        $.each(getUserTreeStr_User, function(index, item) {
            getUser.prepend('<option value="'+item.userId+'">'+item.name+'</option>');
        });
    }
}

});
function get_user(){
var compStr=$('.company').find('option:selected').parent().attr('label'),
    compStrs=$('.company').val()
    //alert(compStr)
    $.each(getUserTreeStr.result, function(index, item) {
        //alert(item.name);
        if(item.name==compStr){
            //alert(item.name)
            var optgroups='';
            $.each(item.children, function(index, items) {
                if(items.deptId==compStrs){
                    //alert(items.name)
                    $(".user").empty();
                    $.each(items.children, function(index, itemu) {
                        $(".user").prepend('<option value="'+itemu.userId+'" >'+itemu.name+'</option>');
                    })
                }
            })
            if(uidc){$(".user ").val(uidc);$('.password').focus();}
        }
    });  
}
function get_company_quyu(){
    //alert(1)
    //var dataObj=eval("("+data+")");//转换为json对象 
    //alert(data.result.length);//输出root的子对象数量 
    $(".get_quyu").empty();
    $.each(data.result, function(index, item) {
        //alert(item.name);
        var optgroups='';
        $.each(item.children, function(indexs, items) {
            //alert(items.name);
            optgroups=optgroups + '<option value="'+items.deptId+'">'+items.name+'</option>';
        }); 
        //alert(optgroups)
        var optgroup=$(".company").prepend('<optgroup label="'+item.name+'">'+optgroups+'</optgroup>');
    });
    if(didc){$(".company").val(didc);}
    get_user();
}
function get_company(){
    //alert(1)
    //var dataObj=eval("("+data+")");//转换为json对象 
    //alert(data.result.length);//输出root的子对象数量
    $(".get_comp").empty();
    $.each(data.result, function(index, item) {
        //alert(item.name);
        var optgroups='';
        $.each(item.children, function(indexs, items) {
            //alert(items.name);
            optgroups=optgroups + '<option value="'+items.deptId+'">'+items.name+'</option>';
        }); 
        //alert(optgroups)
        var optgroup=$(".company").prepend('<optgroup label="'+item.name+'">'+optgroups+'</optgroup>');
    });
    if(didc){$(".company").val(didc);}
    get_user();
}



