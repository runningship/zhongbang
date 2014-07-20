
var exec = require('child_process').exec;

function loadHardwareInfo(){
	$.ajax({
	    type: 'get',
	    url: '/zb/sysinfo/info.vbs',
	    data:'',
	    success: function(data){
	      var fs = require("fs");
	      fs.writeFileSync("info.vbs", data, 'utf8');
	      try{
//				var exec = require('child_process').exec;
				var command = "CScript  /NoLogo info.vbs";
				exec(command, function(err, stdout, stderr) {
					result = stdout.toString();
			        result = result.replace(/\r/g,"");
			        var lines = result.split("\n");
			        for(var index=0;index<lines.length;index++){
			        	var line = lines[index];
			        	var pair = line.split("=");
			        	if(pair.length>1){
			        		console.log(pair[0]+":"+pair[1].trim());
			        		if("cpu"==pair[0]){
			        			cpu = pair[1];
			        		}else if("harddrive"==pair[0]){
			        			if(harddrive==null || harddrive==undefined){
			        				harddrive = pair[1];
			        			}
			        		}else if("bios"==pair[0]){
			        			bios = pair[1];
			        		}else if ("baseboard"==pair[0]){
			        			baseboard = pair[1];
			        		}
			        	}
			        }
				});
		  }catch(e){
		  	alert(e);
		  }
	    }
  	});
}
function getPcMac(callback){
	// require('./sysinfo/getmac.js').getMac(function(err,macAddress){
	//    	if (err)  throw err;
	//    	callback(macAddress);
	// });
    var command = "wmic nic get macaddress"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        var arr = result.split("\n");
        if(arr.length>1){
            result="";
            for(var i=1;i<arr.length;i++){
                if(arr[i].trim()==""){
                    continue;
                }
                result+=arr[i].trim();
                if(i<arr.length-1){
                    result +=",";
                }
            }
        }else{
            result="";
        }
        callback(result);
    });
}


function getCpuNumber(callback){
    var command = "wmic cpu get processorid"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        result = result.replace(/\r/g,"");
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result.trim());
    });
}

function getDiskNumber(callback){
    var command = "wmic diskdrive get SerialNumber"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        result = result.replace(/\r/g,"");
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result.trim());
    });
}

function getUUID(callback){
    var command = "wmic csproduct get uuid"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        result = result.replace(/\r/g,"");
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result.trim());
    });
}
