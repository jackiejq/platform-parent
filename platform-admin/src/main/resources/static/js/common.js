var data=JSON.parse(sessionStorage.getItem('data'));
if(data==null){
    window.location.href='login.html'
}
var headers={
    'Content-Type':'application/json'
};
var limit=data.functions;
var limitType=[];
$.each(limit,function(i,obj){
    limitType.push(obj.id);
});
//判断权限，没有则隐藏
function hasLimit(lid,className){
    if($.inArray(lid, limitType)<0){
        $(className).addClass('none');
    }
}
//判断是否有权限
function hasLimit2(lid){
    if($.inArray(lid, limitType)<0){
        return false;
    }
}
/*-删除*/
function deleteSth(url){
    layer.confirm('确认要删除吗？',function(index){
        var state=ajax(url,'POST','',headers);
        if(state.code!=200){
            layer.alert(state.msg);
        }else {
            layer.msg('已删除!',{icon:1,time:1000});
            setTimeout(function () {
                //window.location.reload();
                $(".layui-laypage-btn").trigger('click');
            },1000)
        }
    });
}
//添加或者修改
function  myAction(url,message,type) {
    layui.use(['form','layer','laydate'], function(){
        $ = layui.jquery;
        var form = layui.form
            ,layer = layui.layer
            ,laydate = layui.laydate;
        form.render();
        //监听提交
        form.on('submit(add)', function(data){
            console.log(data);
            data.field.categoryId=$('[name=categoryId]').val()==''?$('[name=parentCategory]').val():$('[name=categoryId]').val();
            if(data.field.runTimeStart||data.field.runTimeEnd){
                data.field.runTimeStart=data.field.runTimeStart.substring(0,5);
                data.field.runTimeEnd=data.field.runTimeEnd.substring(0,5);
            }
            var state=ajax(url,'POST',data.field, {'Content-Type':'application/x-www-form-urlencoded'});
            if(state.code!=200){
                layer.alert(state.msg);
            }else {
                layer.msg(message, {icon: 6,time: 1500},function () {
                        x_admin_close()
                });
            }
            return false;
        });

    });
}
//前端静态分页
function page(url,element) {
    var state=ajax(url,'GET','',headers);
    if(state.code!=200){
        $('.tableError').html('暂无返回数据');
        return;
    }
    layui.use('laypage', function(){
        var laypage = layui.laypage;
        laypage.render({
            elem: 'page',
            count: state.data.length,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj) {
                //模拟渲染
                document.getElementById('tbody').innerHTML = function () {
                    var arr = [],
                        thisData = state.data.concat().splice(obj.curr * obj.limit - obj.limit, obj.limit);
                    layui.each(thisData, function (index, item) {
                        arr.push(element(item));
                    });
                    return arr.join('');
                }()
            }
        });
    });
}
//配合服务器分页
function servePage(url,element,req) {
    var pageIndex=1,pageSize=10;
    var state=ajax(url,'POST',req,headers);
    layui.use(['laypage','form'], function(){
        var laypage = layui.laypage
            ,form = layui.form;
        laypage.render({
            elem: 'page',
            count: state.total,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj,first) {
                //模拟渲染
                if(!first){
                    pageIndex=obj.curr;
                    pageSize=obj.limit;
                    var newurl=url+'?pageNum='+pageIndex+'&pageSize='+pageSize;
                    state=ajax(newurl,'POST',req,headers);
                }
                document.getElementById('tbody').innerHTML = function () {
                    var arr = [];
                    layui.each(state.data, function (index, item) {
                        arr.push(element(item));
                    });
                    return arr.join('');
                }();
                form.render()
            }
        });
    });
}
//筛选后的分页
function servePage2(url,element) {
    var pageIndex=1,pageSize=10;
    var state=ajax(url,'POST','',headers);

    layui.use('laypage', function(){
        var laypage = layui.laypage
            ,form = layui.form;
        laypage.render({
            elem: 'page',
            count: state.total,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj,first) {
                //模拟渲染
                if(!first){
                    pageIndex=obj.curr;
                    pageSize=obj.limit;
                    var newurl=url+'&pageNum='+pageIndex+'&pageSize='+pageSize;
                    state=ajax(newurl,'POST','',headers);
                }
                document.getElementById('tbody').innerHTML = function () {
                    var arr = [];
                    layui.each(state.data, function (index, item) {
                        arr.push(element(item,index));
                    });
                    return arr.join('');
                }();
                form.render();
            }
        });
    });
}
//同时存在两个表格
function servePage3(url,element) {
    var pageIndex=1,pageSize=10;
    var state=ajax(url,'POST','',headers);

    layui.use('laypage', function(){
        var laypage = layui.laypage ,form = layui.form;
        laypage.render({
            elem: 'page2',
            count: state.total,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj,first) {
                //模拟渲染
                if(!first){
                    pageIndex=obj.curr;
                    pageSize=obj.limit;
                    var newurl=url+'&pageNum='+pageIndex+'&pageSize='+pageSize;
                    state=ajax(newurl,'POST','',headers);
                }
                document.getElementById('tbody2').innerHTML = function () {
                    var arr = [];
                    layui.each(state.data, function (index, item) {
                        arr.push(element(item,index));
                    });
                    return arr.join('');
                }();
                form.render();
            }
        });
    });
}
//保存并下一个
function refreshParentTable() {
    let el=window.parent.document.getElementById('tbody');
    el.innerHTML='';
    window.parent.refresh()
}

//下载图片
function downImage(url,name) {
    let Img = new Image(),
        dataURL='';
    Img.onload=function () {
        let canvas = document.createElement("canvas"), //创建canvas元素
            width=Img.width, //确保canvas的尺寸和图片一样
            height=Img.height;
        canvas.width=width;
        canvas.height=height;
        canvas.getContext("2d").drawImage(Img,0,0,width,height); //将图片绘制到canvas中
        dataURL=canvas.toDataURL(); //转换图片为dataURL
        let dom = document.createElement("a");
        dom.href = dataURL;
        dom.download = name;
        dom.click();
    };
    Img.setAttribute("crossOrigin",'Anonymous');
    Img.src=url;
}

