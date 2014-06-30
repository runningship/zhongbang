var IM = {
	chatWindow:null,
	chatWindowOpen:false,
	chatWindowLeft:null,
	chatWindowTop:null,
	contacts:null,
	receiverId:null,
	ws:null,
	myId:null,
	msgContainer:null,
	defSearchStr : '手机号码/姓名拼音',
	currentPageNo : 1,
	avatarId:null,
	defMsgInputHeight:36,
	defMsgInputDivHeight:50,

	loadContacts : function(){
		$.ajax({
	        url:'/zb/im/getContacts?userId='+IM.myId,
	        data:'',
	        timeout:30000,
	        dataType:'json',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            IM.contacts = data["contacts"];
	            IM.contacts = IM.sortContacts(IM.contacts);
	            buildHtmlWithJsonArray("contact",IM.contacts);
	            IM.fixImg('contact');
	        },
	        complete: function(XMLHttpRequest, textStatus){},
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	console.error(XMLHttpRequest.responseText);
	        }
	    });
	},

	sortContacts : function(contactArr){
		var sorted = [];
		for(var i=0;i<contactArr.length;i++){
			if(contactArr[i].state=='离线'){
				sorted.push(contactArr[i]);
			}else{
				var tmp = [];
				tmp.push(contactArr[i]);
				sorted = tmp.concat(sorted);
			}
		}
		return sorted;
	},

	increaseHeight : function(){
		if(document.activeElement.id!='message'){
			return ;
		}
		if(IM.defMsgInputHeight>100){
			return ;
		}
		IM.defMsgInputHeight+=30;
		IM.defMsgInputDivHeight+=30;
		$('#message').css('height',IM.defMsgInputHeight);
		$('#messageInputWrap').css('height',IM.defMsgInputDivHeight);
	},

	setUserStatus : function(json){
		IM.loadContacts();
	},

	setUserProfile : function(profile){
		$('#me').text(profile['username']);
		IM.avatarId = profile['avatarId'];
		$('#myAvatar').attr('src','/style/image/avatar/'+IM.avatarId+'.jpg');
	},

	login : function(){
		var data = JSON.parse("{}");
		data["type"]="login";
		data["userId"]=IM.myId;
		data["username"]="xzye";
		IM.ws.send(JSON.stringify(data));
	},

	openChat : function(userId){
		IM.receiverId = userId;
		var contact = IM.getContact(userId);
		$('#cname').text(contact["contactName"]);


		var msgCount = $($('#'+IM.receiverId).children(0)[1]);
		msgCount.text(0);
		msgCount.css('display','none');

		IM.loadHistory(userId);
		$('#chatWindow').css('display','');
		// if(IM.chatWindow!=null && IM.chatWindowOpen){
		// 	// chatWindowLeft = chatWindow.DOM.wrap[0].offsetLeft;
		// 	// chatWindowTop = chatWindow.DOM.wrap[0].offsetTop;
		// 	IM.chatWindow.close();
		// }
		// IM.chatWindow = art.dialog({
		//     content: document.getElementById('chatWindow'),
		//     padding:0,
		//     resize:false,
		//     close:function(){
		//     	IM.chatWindowOpen=false;
		//     	IM.chatWindowLeft = IM.chatWindow.DOM.wrap[0].offsetLeft;
		// 		IM.chatWindowTop = IM.chatWindow.DOM.wrap[0].offsetTop;
		//     }
		// });
		// IM.chatWindowOpen = true;
		// if(IM.chatWindowLeft!=null && IM.chatWindowTop!=null){
		// 	IM.chatWindow.position(IM.chatWindowLeft , IM.chatWindowTop);
		// }
	},
	getContact : function(cid){
		for(var i=0;i<IM.contacts.length;i++){
			if(IM.contacts[i]["contactId"]==cid){
				return IM.contacts[i];
			}
		}
	},
	onReceiveMsg : function(data){
		var sender = IM.getContact(data['senderId']);
		if(IM.receiverId==data['senderId']){
			$('#recvAvatar').attr('src','style/image/avatar/'+sender['avatar']+'.jpg');
			var dhtml = $('#recvTmp').html();
			dhtml = dhtml.replace('${msg}',data['content']);
			dhtml = dhtml.replace('${contact}',sender['contactName']);
			dhtml = dhtml.replace('display:none','');
			IM.msgContainer.append(dhtml);
			IM.msgContainer.scrollTop(IM.msgContainer.scroll(0)[0].scrollHeight);
		}else{
			var msgCount = $($('#'+data['senderId']).children(0)[1]);
			var count = parseInt(msgCount.text());
			count++;
			msgCount.text(count);
			msgCount.css('display','');	
		}
	},

	send : function(){
		var text = $('#message').val();
		if(text==''){
			return;
		}
		$('#message').val('');
		$('#sendAvatar').attr('src','style/image/avatar/'+IM.avatarId+'.jpg');
		var dhtml = $('#sendTmp').html();
		dhtml = dhtml.replace('${msg}',text);
		// dhtml = dhtml.replace('${me}','我自己');
		dhtml = dhtml.replace('display:none','');
		IM.msgContainer.append(dhtml);
		IM.msgContainer.scrollTop(IM.msgContainer.scroll(0)[0].scrollHeight);
		var data = JSON.parse('{}');
		data['senderId']=IM.myId;
		data['type']='msg';
		data['receiverId']=IM.receiverId;
		data['receiverType']=1;
		data['content']=text;
		if(IM.ws!=null){
			IM.ws.send(JSON.stringify(data));
		}
	},

	buildHistory : function(list){
		IM.msgContainer.empty();
		for(var i=0;i<list.length;i++){
			var msg = list[i];
			if(IM.myId==msg['senderId']){
				$('#sendAvatar').attr('src','style/image/avatar/'+IM.avatarId+'.jpg');
				var dhtml = $('#sendTmp').html();
				dhtml = dhtml.replace('${msg}',msg['content']);
				// dhtml = dhtml.replace('${me}','我自己');
				dhtml = dhtml.replace('${avatarId}',IM.avatarId);
				dhtml = dhtml.replace('display:none','');
				IM.msgContainer.prepend(dhtml);

			}else if(IM.myId==msg['receiverId']){
				var sender = IM.getContact(msg['senderId']);
				$('#recvAvatar').attr('src','style/image/avatar/'+sender['avatar']+'.jpg');
				var dhtml = $('#recvTmp').html();
				dhtml = dhtml.replace('${msg}',msg['content']);
				dhtml = dhtml.replace('${contact}',sender['contactName']);
				// dhtml = dhtml.replace('${avatarId}',sender['avatar']);
				dhtml = dhtml.replace('display:none','');
				IM.msgContainer.prepend(dhtml);
			}
		}
		IM.msgContainer.scrollTop(IM.msgContainer.scroll(0)[0].scrollHeight);
	},

	loadHistory : function(cid){
		var data = JSON.parse('{}');
		data['type']='history';
		data['myId'] = IM.myId;
		data['contactId']=cid;
		IM.ws.send(JSON.stringify(data));
	},

	search : function(){
		var txt = $('#searchInput').val();
		if(txt==IM.defSearchStr){
			return;
		}
		IM.openSearchPanel();
		$.ajax({
	        url:'/zb/im/search?ownerId='+IM.myId+'&txt='+txt+'&currentPageNo='+IM.currentPageNo,
	        timeout:10000,
	        dataType:'json',
	        type:'post',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            var userSearchResult = data["contacts"]["data"];
	            buildHtmlWithJsonArray("userSearchResult",userSearchResult);
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	console.error(XMLHttpRequest.responseText);
	        }
	    });
	},
	addContact : function(contactId){
		var url = '/zb/im/addContact?ownerId='+IM.myId+'&contactId='+contactId;
		$.ajax({
	        url:url,
	        data:'',
	        type:'post',
	        timeout:10000,
	        dataType:'json',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            IM.closeSearchPanel();
	            IM.loadContacts();
	        },
	        complete: function(XMLHttpRequest, textStatus){},
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	console.error(XMLHttpRequest.responseText);
	        }
	    });
	},

	delContact : function(contactId){
		event.cancelBubble=true;
		var url = '/zb/im/delContact?ownerId='+IM.myId+'&contactId='+contactId;
		$.ajax({
	        url:url,
	        data:'',
	        type:'post',
	        timeout:10000,
	        dataType:'json',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            IM.loadContacts();
	        },
	        complete: function(XMLHttpRequest, textStatus){},
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	console.error(XMLHttpRequest.responseText);
	        }
	    });
	},

	closeSearchPanel : function(){
		$('#searchPanel').css('display','none');
		$('#searchBtn').css('display','');
		$('#closeSearchBtn').css('display','none');
	},

	openSearchPanel : function(){
		$('#searchPanel').css('display','');
		$('#closeSearchBtn').css('display','');
	},

	nextPage : function(){
		IM.currentPageNo++;
		IM.search();
	},
	prePage : function(){
		if(IM.currentPageNo>1){
			IM.currentPageNo--;
			IM.search();	
		}
	},
	fixImg : function(container){
		$('#'+container).find('img').each(function(index,obj){
			var img = $(obj);
			if(index>0){
				img.attr('src',img.attr('srcx'));
			}
		});
	},

	openAvatarPanel : function(){
		art.dialog({
		    content: document.getElementById('chooseAvatarPanel'),
		    padding:0,
		    resize:false
		});
		var url = '/zb/im/allAvatars?';
		$.ajax({
	        url:url,
	        data:'',
	        type:'get',
	        timeout:10000,
	        dataType:'json',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            buildHtmlWithJsonArray("avatars",data['avatars']);
	            IM.fixImg('avatars');
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	console.error(XMLHttpRequest.responseText);
	        }
	    });
	},
	setAvatar : function(aId){
		IM.avatarId = aId;
		$('#myAvatar').attr('src','/style/image/avatar/'+IM.avatarId+'.jpg');
	},
	saveAvatar : function(){
		$.ajax({
	        url:'/zb/im/setAvatar?userId='+IM.myId+'&avatarId='+IM.avatarId,
	        data:'',
	        type:'get',
	        timeout:10000,
	        dataType:'json',
	        beforeSend: function(XMLHttpRequest){
	            
	        },success:function (data, textStatus) {
	            alert('头像更新成功');
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {
	        	alert('头像更新失败');
	        }
	    });
	},

	Init : function(){
		IM.myId = getParam("userId");
		IM.msgContainer = $('#msgContainer');
		$('#searchInput').val(IM.defSearchStr);
		IM.loadContacts();

		IM.ws = new WebSocket("ws://192.168.1.125:9099");
		IM.ws.onopen = function() { 
			console.log("open"); 
			IM.login(IM.ws);
		};
		IM.ws.onmessage = function(e) { 
			var json = JSON.parse(e.data);
			if(json['type']=='msg'){
				IM.onReceiveMsg(json);
			}else if(json['type']=='history'){
				IM.buildHistory(json['history']);
			}else if(json['type']=='userprofile'){
				IM.setUserProfile(json);
			}else if(json['type']=='status'){
				IM.setUserStatus(json);
			}
		};
		IM.ws.onclose = function(e) {
			console.log("closed"); 
		};
		IM.ws.onerror = function(e){
			console.log(e); 
		}
		
		jQuery.hotkeys.add('ctrl+return',function(e){
			IM.send();
		});


		jQuery.hotkeys.add('return',{propagate: true},function(e){
			IM.increaseHeight();
		});

		// art.dialog({
		//     content: document.getElementById('lxr'),
		//     padding:0,
		//     resize:false,
		//     zIndex:10
		// });
		// $('#lxr').parents().find('.aui_close').css('display','none');
	}
}
