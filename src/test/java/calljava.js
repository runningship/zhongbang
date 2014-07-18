
var spawn = require('child_process').spawn;
exe(["/c","java com.youwei.test.zjb.sys.NodeJSTest"]);
 
function exe(command){
    
    var cmd = spawn("cmd",command);
 
    cmd.stdout.setEncoding("ASCII");
    cmd.stdout.on("data",function(data){
	console.log("------------------------------");
	console.log("exec",command);
	console.log("stdout:"+data);
    });
 
    cmd.stderr.on("data",function(data){
	console.log("------------------------------");
	console.log("stderr:"+data);
	console.log("------------------------------");
    });
 
    cmd.on("exit",function(code){
	console.log("exited with code:"+code);
	console.log("------------------------------");
    });
    
};
