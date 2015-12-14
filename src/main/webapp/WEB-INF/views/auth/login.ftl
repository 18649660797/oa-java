<!DOCTYPE html>
<html>
<head>
    <title>OA 内测</title>
    <#include "../include/resource.ftl"/>
</head>
<body class="login-page">
<div class="login-box">
    <form id="J_Form" action="/j_spring_security_check" method="post" class="form-horizontal">
        <input style="display: none;" checked type="checkbox" name="_spring_security_remember_me">
        <div class="control-group">
            <label class="control-label">用户名：</label>
            <div class="controls">
                <input type="text" name="j_username" data-rules="{required:true}" /><br>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">密码：</label>
            <div class="controls">
                <input type="password" name="j_password" data-rules="{required:true}" /><br>
            </div>
        </div>
        <hr/>
        <div class="row">
            <div class="form-actions span13 offset3">
                <button type="submit" class="button button-primary">登录</button>
                <button type="reset" class="button">重置</button>
            </div>
        </div>
    </form>
</div>
<script>
    (function($) {
        $(function() {
            var Form = BUI.Form;
            new Form.Form({
                srcNode : '#J_Form',
                submitType : 'normal',
                callback : function(data){
                    if (edy.ajaxHelp.handleAjax((data))) {
                        BUI.Message.Alert("登录成功");
                        location.href = "/";
                    }
                }
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>