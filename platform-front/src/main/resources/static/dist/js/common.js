/**
 * Created by No.1 on 2018/12/25.
 */
var domain='http://192.168.2.250:8081';
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

