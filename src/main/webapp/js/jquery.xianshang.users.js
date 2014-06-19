/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-13 17:38:04
 * @version $Id$
 */
/**
 * 其他地方调用时使用
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
        getComp.append('<option value="">请选择区域</option>');
        $.each(getUserTreeStr.result, function(index, item) {
            getComp.append('<option value="'+item.deptId+'">'+item.name+'</option>');
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
        getQuyu.append('<option value="">请选择分公司</option>');
        if($(".get_comp").length>0){
            var comp_index=$(".get_comp").prop('selectedIndex')-1;
            //alert(getUserTreeStr.result[comp_index].children)
            if(getUserTreeStr.result[comp_index]){
                $.each(getUserTreeStr.result[comp_index].children, function(index, item) {
                    getQuyu.append('<option value="'+item.deptId+'">'+item.name+'</option>');
                });
            }
        }else{
            $.each(getUserTreeStr.result, function(index, item) {
                //alert(item.name);
                var optgroups='';
                $.each(item.children, function(indexs, items) {
                    //alert(items.name);
                    optgroups=optgroups + '<option value="'+items.deptId+'">'+items.name+'</option>';
                }); 
                //alert(optgroups)
                getQuyu.append('<optgroup label="'+item.name+'">'+optgroups+'</optgroup>');
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
        var comp_index=$(".get_comp").prop('selectedIndex')-1,
        quyu_index=$(".get_quyu").prop('selectedIndex')-1,
        getUserTreeStr_User='';
        getUser.empty();
        getUser.append('<option value="">请选择业务员</option>');
        if(getUserTreeStr.result[comp_index] && getUserTreeStr.result[comp_index].children[quyu_index]){
            getUserTreeStr_User=getUserTreeStr.result[comp_index].children[quyu_index].children;
            //alert(getUserTreeStr.result[comp_index].children[quyu_index].children);//输出root的子对象数量
            $.each(getUserTreeStr_User, function(index, item) {
                getUser.append('<option value="'+item.userId+'">'+item.name+'</option>');
            });
        }
        
    }
}

});





/**
 * [get_user 登陆时使用]
 * @return {[type]} [description]
 */
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



function formSerialize(form){
    var quyuId=form.find('.get_comp').val();
    var dianId=form.find('.get_quyu').val();
    var userId=form.find('.get_user').val();
    form.find('.userInput').val(quyuId + dianId + userId);
    return form.serialize();
}
