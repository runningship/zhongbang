var j=-1;
var temp_str;

//����document.getElementById������ʹ�ã�������$����д�˺���
var $$$=function(node){
	return document.getElementById(node);
}

//��document.getElementsByTagNameҲ����д
var $$=function(node){
	return document.getElementsByTagName(node);
}

//�첽��ȡ���ݿ����ݣ��������ʾ��suggest div��
function ajax_keyword(){
	var xmlhttp;
	try{
		xmlhttp=new XMLHttpRequest();
		}
	catch(e){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	xmlhttp.onreadystatechange=function(){
	if (xmlhttp.readyState==4){
		if (xmlhttp.status==200){
			var data=xmlhttp.responseText;
			if(data){
			$$$("suggest").style.display="block";
			}
			//�Խ������unescape�����Է�ֹ��������
			$$$("suggest").innerHTML=unescape(data);
			j=-1;
			}
		}
	}
	xmlhttp.open("post", "/ajax/house_addr.asp", true);
	xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	xmlhttp.send("area="+escape($$$("area").value));
}

//����������
function keyupdeal(e){
	var keyc;
	if(window.event){
		keyc=e.keyCode;
	}
	else if(e.which){
		keyc=e.which;
	}
	if(keyc!=40 && keyc!=38){
		ajax_keyword();
		temp_str=$$$("area").value;
		temp_strA=$$$("Address").value;
		temp_strQ=$$$("id_quyu").value;
	}
}

//���ý�������ʽ��������ʾ
function set_style(num){
	for(var i=0;i<$$("dd").length;i++){
		var li_node=$$("dd")[i];
		li_node.className="";
	}
	if(j>=0 && j<$$("dd").length){
		var i_node=$$("dd")[j];
		$$("dd")[j].className="select";
	}
}

//ѡ��������ʱ��������ʾ��ǰ��
function mo(nodevalue){
	j=nodevalue;
	set_style(j);
}

//�ύ������
function form_submit(){
	if(j>=0 && j<$$("dd").length){
		$$$("area").value=$$("strong")[j].childNodes[0].nodeValue;
		$$$("id_quyu").value=$$("big")[j].childNodes[0].nodeValue;
		$$$("Address").value=$$("small")[j].childNodes[0].nodeValue;
	}
	//document.search.submit();
	hide_suggest();
}

//���ؽ����
function hide_suggest(){
	var nodes=document.body.childNodes
	for(var i=0;i<nodes.length;i++){
		if(nodes[i]!=$$$("area")){
			$$$("suggest").innerHTML="";
		}
	}
	$$$("suggest").style.display="none";
}
	
//�����򰴼������¼��Ķ���
function keydowndeal(e){
	var keyc;
	if(window.event){
		keyc=e.keyCode;
		}
	else if(e.which){
		keyc=e.which;
		}
	if(keyc==40 || keyc==38){
		if(keyc==40){
			if(j<$$("dd").length){
				j++;
				if(j>=$$("dd").length){
					j=-1;
				}
			}
			if(j>=$$("dd").length){
					j=-1;
			}
		}
		if(keyc==38){
			if(j>=0){
				j--;
				if(j<=-1){
					j=$$("dd").length;
				}
			}
			else{
				j=$$("dd").length-1;
			}
		}
		set_style(j);
		if(j>=0 && j<$$("dd").length){
			$$$("area").value=$$("strong")[j].childNodes[0].nodeValue;
			$$$("id_quyu").value=$$("big")[j].childNodes[0].nodeValue;
			$$$("Address").value=$$("small")[j].childNodes[0].nodeValue;
		}else{
			//alert('asd');
			$$$("area").value=temp_str;
			$$$("id_quyu").value=temp_strQ;
			$$$("Address").value=temp_strA;
		}
	}else{
		if(keyc==9){
			$$$("Address").focus();
		}else if(keyc==13){
//			$$$("Address").focus();
		}
		hide_suggest();return false;
	}
}