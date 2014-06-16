//创建和初始化地图函数：
var oldMarker;
    function initMap(){
        createMap();//创建地图
        setMapEvent();//设置地图事件
        addMapControl();//向地图添加控件
        //addMarker();//向地图中添加marker
    }
    
    //创建地图函数：
    function createMap(){
        var map = new BMap.Map("map");//在百度地图容器中创建一个地图
        
        window.map = map;//将map变量存储在全局
    }
    
    //地图事件设置函数：
    function setMapEvent(){
        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
        map.enableKeyboard();//启用键盘上下左右键移动地图
    }
    
    //地图控件添加函数：
    function addMapControl(){
        //向地图中添加缩放控件
	var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
	map.addControl(ctrl_nav);
        //向地图中添加缩略图控件
	var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
	map.addControl(ctrl_ove);
        //向地图中添加比例尺控件
	var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
	map.addControl(ctrl_sca);
    }
	
function addMarker(){
    map.removeOverlay(oldMarker);
    
	var content = address;
	//创建检索信息窗口对象
    var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
        title  : areas,      //标题
        //width  : 290,             //宽度
        //height : 105,              //高度
        panel  : "panel",         //检索结果面板
        enableAutoPan : true,     //自动平移
        searchTypes   :[
            BMAPLIB_TAB_SEARCH,   //周边检索
            BMAPLIB_TAB_TO_HERE,  //到这里去
            BMAPLIB_TAB_FROM_HERE //从这里出发
        ]
    });
    if(lngs==0 || lats==0){
        alert("没有坐标");
    }
    var point = new BMap.Point(lngs,lats);//定义一个中心点坐标
	var marker = new BMap.Marker(point); //创建marker对象
    marker.enableDragging(); //marker可拖拽
    marker.addEventListener("click", function(e){
	    searchInfoWindow.open(marker);
    })
	marker.addEventListener("dragend", function(e){
		//alert(e.point.lng+","+e.point.lat)
	    searchInfoWindow.open(marker);
		$("#lng").val(e.point.lng);
		$("#lat").val(e.point.lat);
		map.panTo(new BMap.Point(e.point.lng,e.point.lat));
	});
    oldMarker = marker;
    map.addOverlay(marker); //在地图中添加marker
    searchInfoWindow.open(marker); //在marker上打开检索信息串口
	
    map.centerAndZoom(point,16);//设定地图的中心点和坐标并将地图显示在地图容器中
}

	
