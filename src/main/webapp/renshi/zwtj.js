/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2014-06-13 17:38:04
 * @version $Id$
 */
/**
 * 其他地方调用时使用
 */
var zwtz_getUserTreeStr;
$(document).ready(function() {
    YW.ajax({
        url:'/zb/c/user/getUserTree?dataScope=all',
        data:'',
        timeout:3000,
        async:false,
        dataType:'json',
        success:function (data, textStatus) {
            if(data.result.length>0){
                zwtz_getUserTreeStr=data;
                zwtz_fun_get_comp('');
            }else{
                $('.zwtz_get_comp').parent().remove();
            }
        }
    })
});

function zwtz_fun_get_comp(a){
    if(a){as='_'+a}else{as=''}
    if($(".zwtz_get_comp"+as).length>0){
        var getComp=$(".zwtz_get_comp"+as);
        //var dataObj=eval("("+data+")");//转换为json对象 
        //alert(data.result.length);//输出root的子对象数量
        getComp.empty();
        if(zwtz_getUserTreeStr.result.length>1){
        	getComp.append('<option value="">全部</option>');
        }
        $.each(zwtz_getUserTreeStr.result, function(index, item) {
            getComp.append('<option value="'+item.deptId+'">'+item.text+'</option>');
        });
        if($(".zwtz_get_quyu"+as).length>0){
            zwtz_fun_get_quyu(a);
            getComp.change(function(){zwtz_fun_get_quyu(a);});
        }
    }else{zwtz_fun_get_quyu(a);}
}
function zwtz_fun_get_quyu(a){
    if(a){as='_'+a}else{as=''}
    if($(".zwtz_get_quyu"+as).length>0){
        var getQuyu=$(".zwtz_get_quyu"+as);
        getQuyu.empty();
        if($(".zwtz_get_comp"+as).length>0){
            var comp_index = $(".zwtz_get_comp"+as).prop('selectedIndex')-1;
            if($(".zwtz_get_comp"+as).children().length==1){
            	comp_index = $(".zwtz_get_comp"+as).prop('selectedIndex');
            }else{
            	comp_index = $(".zwtz_get_comp"+as).prop('selectedIndex')-1;
            }

            //alert(zwtz_getUserTreeStr.result[comp_index].children)
            if(zwtz_getUserTreeStr.result[comp_index]){
            	if(zwtz_getUserTreeStr.result[comp_index].children.length>1){
            		getQuyu.append('<option value="">全部</option>');	
            	}
            	$.each(zwtz_getUserTreeStr.result[comp_index].children, function(index, item) {
                    if(item.deptId!=undefined && item.deptId!=null){
                        getQuyu.append('<option value="'+item.deptId+'">'+item.text+'</option>');
                    }
                });
            }else{
            	getQuyu.append('<option value="">全部</option>');
            }
        }else{
            $.each(zwtz_getUserTreeStr.result, function(index, item) {
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
    }
}



