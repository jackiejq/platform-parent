function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
function x_admin_show2(title,url,w,h,close){
    if (title == null || title == '') {
        title=false;
    };
    if (url == null || url == '') {
        url="404.html";
    };
    if (w == null || w == '') {
        w=($(window).width()*0.9);
    };
    if (h == null || h == '') {
        h=($(window).height() - 50);
    };
    parent.layer.open({
        type: 2,
        area: [w+'px', h +'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: false,
        shade:0.4,
        title: title,
        content: url,
        cancel:close,
        zIndex: layer.zIndex, //重点1
        success: function(layero){
            //parent.layer.setTop(layero); //重点2
        }
    });
}
function x_admin_show3(title,url,w,h,close){
    if (title == null || title == '') {
        title=false;
    };
    if (url == null || url == '') {
        url="404.html";
    };
    if (w == null || w == '') {
        w=($(window).width()*0.9);
    };
    if (h == null || h == '') {
        h=($(window).height() - 50);
    };
   layer.open({
        type: 2,
        area: [w+'px', h +'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: false,
        shade:0.4,
        title: title,
        content: url,
        cancel:close,
        zIndex: layer.zIndex, //重点1
        success: function(layero){
            //parent.layer.setTop(layero); //重点2
        }
    });
}
// 发送请求，提示，关闭窗口
function point(point,url2,successMsg,type){
    var url2=url2;
    layui.use(['layer', 'form', 'element'], function() {
        var layer = layui.layer
            , form = layui.form
            , element = layui.element;
        var data='';
        layer.confirm(point, function (index) {
            //发异步删除数据
            var url = url2;
            var headers = {
                'sessionId': data.id
            };
            if(type=='duty'){
                data="{}"
            }else{
                data=''
            }
            var state = ajax(url, 'POST', data, headers);
            if (state.code != 200) {
                layer.alert(state.msg);
            } else {
                layer.msg(successMsg, {icon: 1, time: 1000}, function () {
                    // 获得frame索引

                    //关闭当前frame

                    if(successMsg=='撤销成功'){
                        //window.parent.$(".layui-laypage-btn").trigger('click');
                        layer.close(layer.index);
                        $(".layui-laypage-btn").trigger('click');
                        //window.location.reload();
                    }else if(point=='确认取消合并房间吗？'){
                        //var index = parent.layer.getFrameIndex(window.name);
                        //parent.parent.layer.close(index);
                        window.location.reload();
                    }else{
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                        window.parent.location.reload();
                    }
                });
            }
        });
    })
}
//发送请求 提示成功或失败 关闭刷新
function pushData(url,successMsg,method,data){
    data=data==undefined||null?'':data;
    var state = ajax(url,method, data, headers);
    if(state.code==200){
        layer.msg(successMsg,{icon:1,time:1000},function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            window.parent.location.reload();
        })
    }else{
        layer.msg(state.msg);
    }
}
//发送请求 提示成功或失败 刷新
function doSth(url,successMsg,method,data){
    data=data==undefined||null?'':data;
    var state = ajax(url,method, data, headers);
    if(state.code==200){
        layer.msg(successMsg,{icon:1,time:1000},function(){
            window.location.reload();
        })
    }else{
        layer.msg(state.msg);
    }
}
// 点击按钮跳转打开窗口
function jump(title,url,w,h){
    layui.use(['layer', 'form', 'element'], function() {
        var layer = layui.layer
            , form = layui.form
            , element = layui.element;
        if(url.indexOf("?")>0){
            url=url+'&ranparam='+Math.random();
        }else{
            url=url+'?ranparam='+Math.random();
        }

        x_admin_show2(title, url, w, h,close2)
    })
}
//打开窗口，关闭时刷新页面
openWindow=function(msg,url,w,h,num){
    event.preventDefault();
    if(hasLimit2(num)!=false){
        if(url.indexOf("?")>0){
            url=url+'&ranparam='+Math.random();
        }else{
            url=url+'?ranparam='+Math.random();
        }
        x_admin_show3(msg,url,w,h,close3);
    }
};
function close2(){
    window.parent.location.reload()
}
function close3(){
    window.location.reload()
}
//打印
function doPrint(inner) {
    bdhtml=document.body.innerHTML;
    sprnstr="<!--startprint-->"; //开始打印标识字符串有17个字符
    eprnstr="<!--endprint-->"; //结束打印标识字符串
    // prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17); //从开始打印标识之后的内容
    // prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr)); //截取开始标识和结束标识之间的内容
    document.body.innerHTML=inner; //把需要打印的指定内容赋给body.innerHTML
    window.print(); //调用浏览器的打印功能打印指定区域
    document.body.innerHTML=bdhtml; // 最后还原页面
    window.location.href=window.location.href;
}
//转换时间
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
function conver(s) {
    return s < 10 ? '0' + s : s;
}
//限制小数
function extractNumber(obj, decimalPlaces, allowNegative) {
    var temp = obj.value;


    // avoid changing things if already formatted correctly
    var reg0Str = '[0-9]*';
    if (decimalPlaces > 0) {
        reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
    } else if (decimalPlaces < 0) {
        reg0Str += '\\.?[0-9]*';
    }
    reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
    reg0Str = reg0Str + '$';
    var reg0 = new RegExp(reg0Str);
    if (reg0.test(temp)) return true;

    // first replace all non numbers
    var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
    var reg1 = new RegExp(reg1Str, 'g');
    temp = temp.replace(reg1, '');


    if (allowNegative) {
        // replace extra negative
        var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
        var reg2 = /-/g;
        temp = temp.replace(reg2, '');
        if (hasNegative) temp = '-' + temp;
    }


    if (decimalPlaces != 0) {
        var reg3 = /\./g;
        var reg3Array = reg3.exec(temp);
        if (reg3Array != null) {
            // keep only first occurrence of .
            // and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
            var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
            reg3Right = reg3Right.replace(reg3, '');
            reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
            temp = temp.substring(0, reg3Array.index) + '.' + reg3Right;
        }
    }


    obj.value = temp;
}

//获取地址栏所有参数包括中文，一个对象
function getRequest() {
    var url = window.location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            //就是这句的问题
            theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
            //之前用了unescape()
            //才会出现乱码
        }
    }
    return theRequest;
}

//获取下拉框
function getSelect(url,selectName){

        var refereeType = ajax(url, 'GET', '', headers);
        if (refereeType.code == 200) {
            var html = '<option value="">请选择</option>';
            $.each(refereeType.data, function (i, obj) {
                html += `<option value="${obj.id}">${obj.name==null?'':obj.name}</option>`
            });
            $('[name=' + selectName + ']').html(html);

        }

}

//获取详情
function getDetail(url){
    var detail=ajax(url,"GET",'',headers);
    if(detail.code==200){
        return detail.data;
    }
}

//填充表单
function initFormHTML(id,json,callback){
    var data = $("#"+id+" *[name]");
    data.each(function() {
        var value = json[$(this).attr("name")];
        if (typeof callback === "function"){
            callback($(this),json);
        }
        if(value != null){
            var type = $(this).attr("type");
            if(type=="checkbox"||type=="radio"){
                var valueArray = value.split("#");
                for(var i=0; i<valueArray.length;i++){
                    if($(this).val() == valueArray[i])
                        $(this).attr("checked","checked");
                }
            } else{
                $(this).val(value);
            }
        }
    });

}


//普通图片上传
function uploadImg(upload,target){
    var uploadInst = upload.render({
        elem: '#test1'
        ,url: domain1+'/upload'
        ,before: function(obj){
            $(target).attr('disabled',true);
            $(target).addClass('layui-btn-disabled');
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#demo1').attr('src', result); //图片链接（base64）
            });
        }
        ,done: function(res){
            console.log(res);
            if(res.code!=200){
                return layer.msg('上传失败');
            }else{
                layer.msg('上传成功',{icon:1,time:1000},function(){
                    $('[name=imgUrl]').val(res.data.fileUrl);
                    $(target).attr('disabled',false);
                    $(target).removeClass('layui-btn-disabled');
                });
            }
        }
        ,error: function(){
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst.upload();
            });
        }
    });

}