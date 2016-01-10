<!DOCTYPE HTML>
<html>
<head>
    <title> OA 管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "include/resource.ftl"/>
</head>
<body>
<div class="header">
    <div class="dl-title"><span class="">简单OA 管理系统</span></div>
    <div class="dl-log">欢迎您，<span class="dl-log-user">${Static["top.gabin.oa.web.utils.AuthUtils"].getCurrentLoginUserName()}</span>
        <a href="/logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <ul id="J_Nav"  class="nav-list ks-clear">
            <#--<li class="nav-item dl-selected"><div class="nav-item-inner nav-storage">权限</div></li>-->
            <#--<li class="nav-item"><div class="nav-item-inner nav-inventory">考勤</div></li>-->
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>

</div>
<script>
    BUI.use('common/main',function(){
        $.post("/permission/menus", {}, function(data) {
            var config = [];
            var nav = $("#J_Nav");
            for (var i = 0; i < data.length; i++) {
                var one = data[i];
                nav.append('<li class="nav-item dl-selected"><div class="nav-item-inner nav-storage">' + one.name + '</div></li>');
                var configItem = {
                    id: one.id,
                    menu: (function(childs2) {
                        var result2 = [];
                        for (var j = 0; j < childs2.length; j++) {
                            var one2 = childs2[j];
                            result2.push({
                                text: one2.name,
                                items: (function(childs3) {
                                    var result3 = [];
                                    for (var k = 0; k < childs3.length; k++) {
                                        var one3 = childs3[k];
                                        result3.push({
                                            id: one3.id,
                                            text: one3.name,
                                            href: one3.url
                                        });
                                    }
                                    return result3;
                                } (one2.childMenus))
                            });
                        }
                        return result2;
                    } (one.childMenus))
                };
                config.push(configItem);
            }
            console.log(config);
            new PageUtil.MainPage({
                modulesConfig : config
            });
        });
    });

</script>
</body>
</html>