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

    $.post("${baseUrl}v1/addAccount",paramsJson,fun_call_default);
    
    function fun_call_default(res) {
        console.log(res);
    }
});
</script>
</body>
</html>
