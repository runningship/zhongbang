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
                result = arr[i].trim();
                break;
                // result+=arr[i].trim();
                // if(i<arr.length-1){
                //     result +=",";
                // }
            }
        }else{
            result="";
        }
        callback(result);
    });
}



var exec = require('child_process').exec;

function getCpuNumber(callback){
    var command = "wmic cpu get processorid"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result);
    });
}

function getDiskNumber(callback){
    var command = "wmic diskdrive get SerialNumber"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result);
    });
}

function getUUID(callback){
    var command = "wmic csproduct get uuid"
    exec(command, function(err, stdout, stderr) {
        if (err) {
          throw err;
        }
        result = stdout.toString();
        var arr = result.split("\n");
        if(arr.length>1){
            result = arr[1];
        }else{
            result="";
        }
        callback(result);
    });
}

function readConfig(){
    var rf=require("fs");
    var data=rf.readFileSync("config.ini","utf-8");
    return data;
}