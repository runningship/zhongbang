/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-13 17:38:04
 * @version $Id$
 */
/**
 * 其他地方调用时使用
 */
var bizman_getUserTreeStr;
$(document).ready(function() {
    var dataScope=getParam('dataScope');
    YW.ajax({
        url:'/zb/c/user/getBizmanTree',
        async:false,
        dataType:'json',
        success:function (data, textStatus) {
            if(data.result.length>0){
                bizman_getUserTreeStr=data;
                bizman_fun_get_comp('');
            }else{
                $('.bizman_get_comp').parent().remove();
            }
        }
    })
});

function bizman_fun_get_comp(a){
    if(a){as='_'+a}else{as=''}
    if($(".bizman_get_comp"+as).length>0){
        var getComp=$(".bizman_get_comp"+as);
        //var dataObj=eval("("+data+")");//转换为json对象 
        //alert(data.result.length);//输出root的子对象数量
        getComp.empty();
        if(bizman_getUserTreeStr.result.length>1){
            // getComp.append('<option value="">全部</option>');
        }
        $.each(bizman_getUserTreeStr.result, function(index, item) {
            getComp.append('<option value="'+item.deptId+'">'+item.text+'</option>');
        });
        if($(".bizman_get_quyu"+as).length>0){
            bizman_fun_get_quyu(a);
            getComp.change(function(){bizman_fun_get_quyu(a);});
        }
    }else{bizman_fun_get_quyu(a);}
}
function bizman_fun_get_quyu(a){
    if(a){as='_'+a}else{as=''}
    if($(".bizman_get_quyu"+as).length>0){
        var getQuyu=$(".bizman_get_quyu"+as);
        getQuyu.empty();
        if($(".bizman_get_comp"+as).length>0){
            var comp_index = $(".bizman_get_comp"+as).prop('selectedIndex');
            if($(".bizman_get_comp"+as).children().length==1){
                comp_index = $(".bizman_get_comp"+as).prop('selectedIndex');
            }else{
                comp_index = $(".bizman_get_comp"+as).prop('selectedIndex');
            }

            //alert(bizman_getUserTreeStr.result[comp_index].children)
            if(bizman_getUserTreeStr.result[comp_index]){
                if(bizman_getUserTreeStr.result[comp_index].children.length>1){
                    // getQuyu.append('<option value="">全部</option>'); 
                }
                $.each(bizman_getUserTreeStr.result[comp_index].children, function(index, item) {
                    if(item.deptId!=undefined && item.deptId!=null){
                        getQuyu.append('<option value="'+item.deptId+'">'+item.text+'</option>');
                    }
                });
            }else{
                // getQuyu.append('<option value="">全部</option>');
            }
        }else{
            $.each(bizman_getUserTreeStr.result, function(index, item) {
                //alert(item.name);
                var optgroups='';
                $.each(item.children, function(indexs, items) {
                    //alert(items.name);
                    optgroups=optgroups + '<option value="'+items.deptId+'">'+items.text+'</option>';
                }); 
                //alert(optgroups)
                getQuyu.append('<optgroup label="'+item.text+'">'+optgroups+'</optgroup>');
            });
        }
        if($(".bizman_get_user"+as).length>0){
            bizman_fun_get_user(a);
            getQuyu.change(function(){bizman_fun_get_user(a);});
        }
    }
}
function bizman_fun_get_user(a){
    if(a){as='_'+a}else{as=''}
    if($(".bizman_get_user"+as).length>0){
        var getUser=$(".bizman_get_user"+as);
        var comp_index;
        var quyu_index;
        if($(".bizman_get_comp"+as).children().length==1){
            comp_index=0;
        }else{
            comp_index = $(".bizman_get_comp"+as).prop('selectedIndex');
        }
        if($(".bizman_get_quyu"+as).children().length==1){
            quyu_index = 0;
        }else{
            quyu_index=$(".bizman_get_quyu"+as).prop('selectedIndex');
        }
        bizman_getUserTreeStr_User='';
        getUser.empty();
        
        if(bizman_getUserTreeStr.result[comp_index] && bizman_getUserTreeStr.result[comp_index].children[quyu_index]){
            if(bizman_getUserTreeStr.result[comp_index].children[quyu_index].children.length>1){
                // getUser.append('<option value="">全部</option>'); 
            }
            bizman_getUserTreeStr_User=bizman_getUserTreeStr.result[comp_index].children[quyu_index].children;
            //alert(bizman_getUserTreeStr.result[comp_index].children[quyu_index].children);//输出root的子对象数量
            $.each(bizman_getUserTreeStr_User, function(index, item) {
                if(item.userId!=null){
                    getUser.append('<option value="'+item.userId+'">'+item.text+'</option>');
                }
            });
        }else{

            // getUser.append('<option value="">全部</option>');
        }
        
    }
}

