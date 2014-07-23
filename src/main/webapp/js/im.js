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
	chats:[],


	loadContacts : function(callback){
		YW.ajax({
	        url:'/zb/c/im/getContacts?userId='+IM.myId,
	        data:'',
	        timeout:30000,
	        dataType:'json',
	        success:function (data, textStatus) {
	            IM.contacts = data["contacts"];
	            IM.contacts = IM.sortContacts(IM.contacts);
	            buildHtmlWithJsonArray("contact",IM.contacts);
	            IM.fixImg('contact');
	            if(callback!=null){
	            	callback();
	        	}
	        }
	    });
	},

	getChat:function(contactId){
		for(var i=0;i<this.chats.length;i++){
			if(this.chats[i].contactId==contactId){
				return this.chats[i];
			}
		}
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
		$('#myAvatar').attr('src','/zb/style/image/avatar/'+IM.avatarId+'.jpg');
	},

	login : function(){
		var data = JSON.parse("{}");
		data["type"]="login";
		data["userId"]=IM.myId;
		data["username"]="xzye";
		IM.ws.send(JSON.stringify(data));
	},

	openChat : function(contactId){
		
		var oldChat = IM.getChat(IM.receiverId);
		if(oldChat!=null){
			oldChat.chatHistory = IM.msgContainer.html();
		}
		IM.receiverId = contactId;
		var contact = IM.getContact(contactId);
		$('#cname').text(contact["contactName"]);

		var msgCount = $($('#'+IM.receiverId).children(0)[1]);
		msgCount.text(0);
		msgCount.css('display','none');

		IM.msgContainer.empty();

		var openChat = IM.getChat(contactId);
		if(openChat==null){
			openChat = Object.create(Chat);
			openChat.contactId = contactId;
			IM.chats.push(openChat);
			IM.loadHistory(contactId);
		}else{
			IM.msgContainer.html(openChat.chatHistory);
		}
		
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
		if(sender==null){
			IM.addContact(data['senderId'],function(){
				IM.renderReceivedMsg(data);
			});
		}else{
			IM.renderReceivedMsg(data);
		}
		
	},

	renderReceivedMsg:function(data){
		var sender = IM.getContact(data['senderId']);
		if(IM.receiverId==data['senderId']){
			$('#recvAvatar').attr('src','/zb/style/image/avatar/'+sender['avatar']+'.jpg');
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
		$('#sendAvatar').attr('src','/zb/style/image/avatar/'+IM.avatarId+'.jpg');
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

	buildHistory : function(contactId,list){
		// IM.msgContainer.empty();
		var lastTimelineSpan = IM.msgContainer.find('span[name=sendtime]');
		var lastTimeline;
		if(lastTimelineSpan.length==0){
			lastTimeline = new Date().getTime();
		}else{
			lastTimeline = Date.parse(lastTimelineSpan[0].innerText);
		}
		
		for(var i=0;i<list.length;i++){
			var msg = list[i];
			if(IM.myId==msg['senderId']){
				$('#sendAvatar').attr('src','/zb/style/image/avatar/'+IM.avatarId+'.jpg');
				var dhtml = $('#sendTmp').html();
				dhtml = dhtml.replace('${msg}',msg['content']);
				// dhtml = dhtml.replace('${me}','我自己');
				dhtml = dhtml.replace('${avatarId}',IM.avatarId);
				dhtml = dhtml.replace('display:none','');
				IM.msgContainer.prepend(dhtml);

			}else if(IM.myId==msg['receiverId']){
				var sender = IM.getContact(msg['senderId']);
				$('#recvAvatar').attr('src','/zb/style/image/avatar/'+sender['avatar']+'.jpg');
				var dhtml = $('#recvTmp').html();
				dhtml = dhtml.replace('${msg}',msg['content']);
				dhtml = dhtml.replace('${contact}',sender['contactName']);
				// dhtml = dhtml.replace('${avatarId}',sender['avatar']);
				dhtml = dhtml.replace('display:none','');
				IM.msgContainer.prepend(dhtml);
			}
			var timeline = '<div style="text-align:center"><span name="sendtime" style="background-color:#ddd">'+msg['sendtime']+'</span></div>';
			// IM.msgContainer.prepend(timeline);
			var sendtime = Date.parse(msg['sendtime']);
			if(lastTimeline-sendtime>=3600*1000){
				IM.msgContainer.prepend(timeline);
				lastTimeline = sendtime;
			}
			// if(i==0){
			// 	IM.msgContainer.prepend(timeline);
			// }else{
			// 	var tnew = Date.parse(msg['sendtime']);
			// 	var told = Date.parse(list[i-1]['sendtime']);
			// 	if(tnew-told>=3600*1000){
			// 		IM.msgContainer.prepend(timeline);
			// 	}
			// }
		}
		if(list.length>0){
			IM.msgContainer.prepend('<div style="text-align:center"><a onclick="$(this).remove();IM.loadHistory('+contactId+')" href="javascript:void(0)">查看更多</a></div>');
		}
		IM.msgContainer.scrollTop(IM.msgContainer.scroll(0)[0].scrollHeight);
	},


	loadHistory : function(cid){
		var chat = this.getChat(cid);
		chat.page++;
		var data = JSON.parse('{}');
		data['type']='history';
		data['myId'] = IM.myId;
		data['contactId']=cid;
		data['page']=chat.page;
		IM.ws.send(JSON.stringify(data));
	},

	search : function(){
		var txt = $('#searchInput').val();
		if(txt==IM.defSearchStr){
			return;
		}
		IM.openSearchPanel();
		YW.ajax({
	        url:'/zb/c/im/search?ownerId='+IM.myId+'&txt='+txt+'&currentPageNo='+IM.currentPageNo,
	        timeout:10000,
	        dataType:'json',
	        type:'post',
	        success:function (data, textStatus) {
	            var userSearchResult = data["contacts"]["data"];
	            buildHtmlWithJsonArray("userSearchResult",userSearchResult);
	        }
	    });
	},
	addContact : function(contactId,callback){
		var url = '/zb/c/im/addContact?ownerId='+IM.myId+'&contactId='+contactId;
		YW.ajax({
	        url:url,
	        data:'',
	        type:'post',
	        timeout:10000,
	        dataType:'json',
	        success:function (data, textStatus) {
	            IM.closeSearchPanel();
	            IM.loadContacts(callback);
	        }
	    });
	},

	delContact : function(contactId){
		event.cancelBubble=true;
		var url = '/zb/c/im/delContact?ownerId='+IM.myId+'&contactId='+contactId;
		YW.ajax({
	        url:url,
	        data:'',
	        type:'post',
	        timeout:10000,
	        dataType:'json',success:function (data, textStatus) {
	            IM.loadContacts();
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
		var url = '/zb/c/im/allAvatars?';
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
	        	alert(XMLHttpRequest.responseText);
	        }
	    });
	},
	setAvatar : function(aId){
		IM.avatarId = aId;
		$('#myAvatar').attr('src','/zb/style/image/avatar/'+IM.avatarId+'.jpg');
	},
	saveAvatar : function(){
		$.ajax({
	        url:'/zb/c/im/setAvatar?userId='+IM.myId+'&avatarId='+IM.avatarId,
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
			IM.login(IM.ws);
		};
		IM.ws.onmessage = function(e) { 
			var json = JSON.parse(e.data);
			if(json['type']=='msg'){
				IM.onReceiveMsg(json);
			}else if(json['type']=='history'){
				IM.buildHistory(json['contactId'],json['history']);
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

var Chat = {
	contactId:null,
	chatHistory:null,
	page:0
}
