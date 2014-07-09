YW={
	options:{
		beforeSend: function(XMLHttpRequest){
		      $(document.body).append('<img src="/zb/style/image/ajax-loading.gif" style="display:block;margin-left:auto;margin-right:auto;" id="loading" />');
	    },
	    complete: function(XMLHttpRequest, textStatus){
	      $('#loading').remove();
	    }
	},
	ajax:function(options){
		if(options.beforeSend==undefined){
			options.beforeSend = YW.options.beforeSend;
		}
		if(options.complete==undefined){
			options.complete = YW.options.complete;
		}
		$.ajax(options);
	}
}