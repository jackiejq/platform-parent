/**
 * Created by No.1 on 2018/12/25.
 */
var domain='http://test.api.yoocard.com.cn:8083';
var domain2='http://test.api.yoocard.com.cn:8081';
var headers={'Content-Type':'application/x-www-form-urlencoded'};
function ajax(url,type,data,headers){
    var result;
    data=data==null||data==undefined?'':data;
    mui.ajax(url,{
        data:data,
        dataType:'json',//服务器返回json格式数据
        type:type,//HTTP请求类型
        timeout:10000,//超时时间设置为10秒；
        async:false,
        headers:headers,
        success:function(res){
            result = res;
        },
        error:function(xhr,type,errorThrown){
            //异常处理；
            console.log(type);
            if(type=='timeout'){
                mui.toast('请求超时',{ duration:2000, type:'div' });
            }
        }
    });
    return result;
}
function conver(s) {
    return s < 10 ? '0' + s : s;
}
function getDate(date){

    var myDate = new Date(date);

    //获取当前年
    var year = myDate.getFullYear();

    //获取当前月
    var month = myDate.getMonth() + 1;

    //获取当前日
    var date = myDate.getDate();
    var h = myDate.getHours(); //获取当前小时数(0-23)
    var m = myDate.getMinutes(); //获取当前分钟数(0-59)
    var s = myDate.getSeconds();

    //获取当前时间

    return now = year + '-' + conver(month) + "-" + conver(date) + " " + conver(h) + ':' + conver(m) + ":" + conver(s);

}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

//function upDown(pageNum,pageSize,callback,callback2){
//    mui.init({
//        pullRefresh : {
//            container:'#refreshContainer',//待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
//            down : {
//                style:'circle',
//                color:'#2bd009',
//                height:50,//可选,默认50.触发下拉刷新拖动距离,
//                auto: false,//可选,默认false.首次加载自动下拉刷新一次
//                contentdown : "下拉可以刷新",//可选，在下拉可刷新状态时，下拉刷新控件上显示的标题内容
//                contentover : "释放立即刷新",//可选，在释放可刷新状态时，下拉刷新控件上显示的标题内容
//                contentrefresh : "正在刷新...",//可选，正在刷新状态时，下拉刷新控件上显示的标题内容
//                callback :function(){ //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
//
//                    pageNum=1;pageSize=10;
//                    if (typeof callback === "function"){
//                        callback(pageNum,pageSize);
//                    }
//
//                    mui('#refreshContainer').pullRefresh().endPulldownToRefresh();
//                    mui('#refreshContainer').pullRefresh().refresh(true);
//
//                }
//            },
//            up : {
//                height:50,//可选.默认50.触发上拉加载拖动距离
//                auto:false,//可选,默认false.自动上拉加载一次
//                contentrefresh : "正在加载...",//可选，正在加载状态时，上拉加载控件上显示的标题内容
//                contentnomore:'没有更多帖子了',//可选，请求完毕若没有更多数据时显示的提醒内容；
//                callback :function(){
//                    if (typeof callback2 === "function"){
//                        callback2(pageNum,pageSize,this);
//                    }
//                } //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
//            }
//        }
//    });
//}

//--------------上拉加载更多---------------
//获取滚动条当前的位置
function getScrollTop() {
    // 考虑到浏览器版本兼容性问题，解析方式可能会不一样
    return document.documentElement.scrollTop || document.body.scrollTop
}

//获取当前可视范围的高度
function getWinHeight () {
    return document.documentElement.clientHeight || document.body.clientHeight
}

//获取文档完整的高度
function getScrollHeight() {
    let bodyScrollHeight = 0
    let documentScrollHeight = 0
    if (document.body) {
        bodyScrollHeight = document.body.scrollHeight
    }
    if (document.documentElement) {
        documentScrollHeight = document.documentElement.scrollHeight
    }
    // 当页面内容超出浏览器可视窗口大小时，Html的高度包含body高度+margin+padding+border所以html高度可能会大于body高度
    return (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight

}

function isReachBottom () {

    const scrollTop = getScrollTop() // 获取滚动条的高度
    const winHeight = getWinHeight() // 一屏的高度
    const scrollHeight = getScrollHeight() // 获取文档总高度
    return scrollTop >= parseInt(scrollHeight) - winHeight
}

function toHome(father,son){
    mui(father).on('tap',son,function(){
        event.stopPropagation();
        window.location.replace('index.html?uid='+sessionStorage.getItem('uid'));
        //window.location.replace('index.html?uid='+sessionStorage.getItem('uid')+window.location.search);
    });
}

function show(){
    $("#editor").emoji({
        button: "#btn",
        showTab: false,
        animation: 'slide',
        position: 'bottomLeft',
        icons: [{
            name: "QQ表情",
            path: "dist/img/qq/",
            maxNum: 91,
            excludeNums: [41, 45, 54],
            file: ".gif"
        }]
    });
}
function isLogin(){
    var uid=sessionStorage.getItem('uid')=='null'||undefined||''?'':sessionStorage.getItem('uid');
    if(uid==''||null||undefined){
        mui.alert('APP未登录，请先登录');
    }else{
        return true;
    }
}

function reply(id,cid){
    if(isLogin()){
            window.location.href='reply.html?id='+id+'&cid='+cid;
        }

}

