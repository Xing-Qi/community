//点击置顶，加精，删除按钮事件
$(function (){
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);

})



function like(btn,entityType,entityId,entityUserId,postId){
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function (data){
            data = $.parseJSON(data);
            if(data.code == 0){
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            }else {
                alert(data.msg);
            }
        }
    );
}
//置顶
function setTop(){
    $.post(
        //异步请求路径
        CONTEXT_PATH + "/discuss/top",
        //携带数据
        {"id":$("#postId").val()},
        //处理返回数据
        function (data){
            data = $.parseJSON(data);
            if(data.code==0){
                $("#topBtn").text(data.type==1?'取消置顶':'置顶');
            }else {
                alert(data.msg);
            }
        }
    );
}

//加精
function setWonderful(){
    $.post(
        //异步请求路径
        CONTEXT_PATH + "/discuss/wonderful",
        //携带数据
        {"id":$("#postId").val()},
        //处理返回数据
        function (data){
            data = $.parseJSON(data);
            if(data.code==0){
                $("#wonderfulBtn").text(data.status==1?'取消加精':'加精');
            }else {
                alert(data.msg);
            }
        }
    );
}
//删除
function setDelete(){
    $.post(
        //异步请求路径
        CONTEXT_PATH + "/discuss/delete",
        //携带数据
        {"id":$("#postId").val()},
        //处理返回数据
        function (data){
            data = $.parseJSON(data);
            if(data.code==0){
                //删除成功重新加载页面
                location.href = CONTEXT_PATH + "/index";
            }else {
                alert(data.msg);
            }
        }
    );
}

