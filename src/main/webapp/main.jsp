<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String  baseUrl = request.getScheme()+"://"
            +request.getServerName()+":"
            +request.getServerPort()
            +request.getContextPath()+"/";
    session.setAttribute("baseUrl",baseUrl);
%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<body>
<script src="${baseUrl}js/jquery-1.7.1.min.js"></script>
<h2>Ordercar  main success!</h2>
<span id="btn" style="cursor: pointer;">提交数据</span>
<script>
$('#btn').click(function () {
    var paramsJson={
        "id":"test003",
        "drivingname":"西安白鹭驾校3",
        "concat":"张校长3",
        "address":"西安白鹭原地区3",
        "phone":"15300998877",
        "wxno":"wx0023",
        "appid":"wx1010281223",
        "appkey":"k00120182023",
        "account":"19129021323",
        "password":"12312323",
        "status":"1",
        "createdate":"2020-09-07"
    };
   // paramsJson ={"id":"car001","carname":"周末班训练C1","carimg":"/001.jpg","cartype":"C1","orderno":1,"status":"1","belong":"100010009","createtime":"2020-09-07 10:20:19"};
    $.ajax({
        type:"POST",
        url:"${baseUrl}v1/addAccount",
        //url:"${baseUrl}v1/addCarInfo",
        contentType:"application/json",  //发送信息至服务器时内容编码类型。
        dataType:"json",  // 预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断，比如XML MIME类型就被识别为XML。
        data:JSON.stringify(paramsJson),
        success:fun_call_default
    });
    function fun_call_default(res) {
        console.log(res);
    }
});
</script>
</body>
</html>
