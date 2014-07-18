var spawn = require('child_process').spawn
var command=["/c","java",NodeJSTest]
var cmd = spawn("cmd",command);
cmd.stdout.on("data",function(data){
	console.log("------------------------------");
	console.log("exec",command);
	console.log("stdout:"+data);
});