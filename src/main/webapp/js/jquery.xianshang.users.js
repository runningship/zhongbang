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
    var dataScope=getParam('dataScope');
    YW.ajax({
        url:'/zb/c/user/getUserTree?dataScope='+dataScope,
        data:'',
        timeout:3000,
        async:false,
        dataType:'json',
        success:function (data, textStatus) {
            if(data.result.length>0){
                getUserTreeStr=data;
                //alert(getUserTreeStr.result)
                //alert(getUserTreeStr.result[0].children)
                fun_get_comp('');
            }else{
                $('.get_comp').parent().remove();
            }
        }
    })
});

function fun_get_comp(a){
    if(a){as='_'+a}else{as=''}
    if($(".get_comp"+as).length>0){
        var getComp=$(".get_comp"+as);
        //var dataObj=eval("("+data+")");//转换为json对象 
        //alert(data.result.length);//输出root的子对象数量
        getComp.empty();
        if(getUserTreeStr.result.length>1){
        	getComp.append('<option value="">全部</option>');
        }
        $.each(getUserTreeStr.result, function(index, item) {
            getComp.append('<option value="'+item.deptId+'">'+item.text+'</option>');
        });
        if($(".get_quyu"+as).length>0){
            fun_get_quyu(a);
            getComp.change(function(){fun_get_quyu(a);});
        }
    }else{fun_get_quyu(a);}
}
function fun_get_quyu(a){
    if(a){as='_'+a}else{as=''}
    if($(".get_quyu"+as).length>0){
        var getQuyu=$(".get_quyu"+as);
        getQuyu.empty();
        if($(".get_comp"+as).length>0){
            var comp_index = $(".get_comp"+as).prop('selectedIndex')-1;
            if($(".get_comp"+as).children().length==1){
            	comp_index = $(".get_comp"+as).prop('selectedIndex');
            }else{
            	comp_index = $(".get_comp"+as).prop('selectedIndex')-1;
            }

            //alert(getUserTreeStr.result[comp_index].children)
            if(getUserTreeStr.result[comp_index]){
            	if(getUserTreeStr.result[comp_index].children.length>1){
            		getQuyu.append('<option value="">全部</option>');	
            	}
            	$.each(getUserTreeStr.result[comp_index].children, function(index, item) {
                    if(item.deptId!=undefined && item.deptId!=null){
                        getQuyu.append('<option value="'+item.deptId+'">'+item.text+'</option>');
                    }
                });
            }else{
            	getQuyu.append('<option value="">全部</option>');
            }
        }else{
            $.each(getUserTreeStr.result, function(index, item) {
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
        if($(".get_user"+as).length>0){
            fun_get_user(a);
            getQuyu.change(function(){fun_get_user(a);});
        }
    }
}
function fun_get_user(a){
    if(a){as='_'+a}else{as=''}
    if($(".get_user"+as).length>0){
        var getUser=$(".get_user"+as);
        var comp_index;
        var quyu_index;
        if($(".get_comp"+as).children().length==1){
        	comp_index=0;
        }else{
        	comp_index = $(".get_comp"+as).prop('selectedIndex')-1;	
        }
        if($(".get_quyu"+as).children().length==1){
        	quyu_index = 0;
        }else{
        	quyu_index=$(".get_quyu"+as).prop('selectedIndex')-1;
        }
        getUserTreeStr_User='';
        getUser.empty();
        
        if(getUserTreeStr.result[comp_index] && getUserTreeStr.result[comp_index].children[quyu_index]){
        	if(getUserTreeStr.result[comp_index].children[quyu_index].children.length>1){
        		getUser.append('<option value="">全部</option>');	
        	}
            getUserTreeStr_User=getUserTreeStr.result[comp_index].children[quyu_index].children;
            //alert(getUserTreeStr.result[comp_index].children[quyu_index].children);//输出root的子对象数量
            $.each(getUserTreeStr_User, function(index, item) {
            	if(item.userId!=null){
                	getUser.append('<option value="'+item.userId+'">'+item.text+'</option>');
            	}
            });
        }else{

        	getUser.append('<option value="">全部</option>');
        }
        
    }
}






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
    if(userId==undefined){
        userId="";
    }
    var xpath =quyuId;
    if(dianId!=null && dianId!=undefined && dianId!=""){
        xpath=xpath+"-"+dianId;
    }
    if(userId!=null && userId!=undefined && userId!=""){
        xpath=xpath+"-"+userId;
    }
    if(xpath!=undefined){
        form.find('input[name=xpath]').val(xpath);    
    }
    
    return form.serialize();
}
