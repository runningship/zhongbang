


function loadHardwareInfo(){
    getLicence();
	$.ajax({
	    type: 'get',
	    url: '/zb/sysinfo/info.vbs',
	    data:'',
	    success: function(data){
	      var fs = require("fs");
	      fs.writeFileSync("info.vbs", data, 'utf8');
	      try{
				var exec = require('child_process').exec;
				var command = "cscript-xp  /NoLogo info.vbs";
				exec(command, function(err, stdout, stderr) {
					result = stdout.toString();
			        result = result.replace(/\r/g,"");
			        var lines = result.split("\n");
                    
			        for(var index=0;index<lines.length;index++){
			        	var line = lines[index];
			        	var pair = line.split("=");
			        	if(pair.length>1){
			        		//console.log(pair[0]+":"+pair[1].trim());
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
var sysDir="c:/Windows/";
function writeLicence(lic){
    var fs = require("fs");
    try{
        if(lic==undefined || lic==""){
            return;
        }
        var fs = require("fs");
        fs.writeFileSync(sysDir+"zjb.lic", lic, 'utf8');
    }catch(e){
        console.log(e);
        alert('写入授权信息失败，这是由您的电脑的目录权限设置引起的，请联系系统管理员为您解决.');
    }
}

function getLicence(){
    try{
        var fs = require("fs");
        if(!fs.existsSync(sysDir+"zjb.lic")){
            writeLicence(createUUID());
        }
        uuid = fs.readFileSync(sysDir+'zjb.lic',"utf-8");
        var fst = fs.statSync(sysDir+'zjb.lic');
        ctime = fst.ctime.getTime();
    }catch(e){
        alert('获取授权信息失败');
    }
}

function createUUID() {
    // http://www.ietf.org/rfc/rfc4122.txt
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
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
