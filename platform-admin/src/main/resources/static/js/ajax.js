//测试环境
// var domain1='http://192.168.123.180:8082';
 var domain1='';

function ajax(url,method,data,headers) {
    var result;
    data=data==null||data==undefined?'':data;
    var ajaxTimeOut=$.ajax({
        url:url,
        data:data,
        cache:false,
        type:method,
        async:false,
        headers:headers,
        contentType:'application/json',
        success:function(res){
            result = res;
        },
        error:function (err){
            console.log(err);
        },
        complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
            console.log(status);
            if(status=='timeout'){//超时,status还有success,error等值的情况
                ajaxTimeOut.abort(); //取消请求
                layui.use(['layer', 'form', 'element'], function() {
                    var layer = layui.layer;
                    layer.alert("请求超时，请重试");
                })

            }
        }
    });
    return result;
}




//获取短信验证码
function getcode(url,tel) {
    var regmobile=/^[1][3,4,5,6,7,8,9][0-9]{9}$/;//检测手机号
    send.setAttribute('disabled','disabled');
    if (!regmobile.test(tel)) {
        $('[name=phone]').focus();
        layer.msg('手机号格式不正确', {icon: 2,time: 2000});
        send.removeAttribute('disabled');
        return false;
    }
   var newheaders='';
    if(typeof headers!='undefined'){
        newheaders=headers;
    }
    var state=ajax(url,'POST','',newheaders);
    if(state.code!=200){
        layer.alert(state.msg);
        send.removeAttribute('disabled');
        return
    }else {
        layer.msg('发送成功', {icon: 1,time: 1500},function () {

        });
    }
    // 定时器
    timer = setInterval(djs,1000);
}
//短信验证码倒计时
function djs(){
    send.value = times+"秒";
    send.style.background='#ccc';
    send.style.cursor='not-allowed';
    send.style.border='1px solid #ccc';
    times--;
    if(times <= 0){
        send.value = "验证码";
        send.removeAttribute('disabled');
        send.style.cursor='pointer';
        times = 60;
        clearInterval(timer);
    }
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
//全屏
fullscreen=function(){
    document.documentElement.webkitRequestFullScreen();
};