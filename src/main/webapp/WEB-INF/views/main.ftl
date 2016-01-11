<!DOCTYPE HTML>
<html>
<head>
    <title> OA </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "include/resource.ftl"/>
</head>
<body>
<div class="header">
    <div class="dl-title"><span class="">edy simple oa </span></div>
    <div class="dl-log">欢迎您，<span class="dl-log-user">${Static["top.gabin.oa.web.utils.AuthUtils"].getCurrentLoginUserName()}</span>
        <a href="/logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <ul id="J_Nav"  class="nav-list ks-clear">
            <#if menus?? && menus?size gt 0>
                <#list menus as menu>
                    <li class="nav-item"><div class="nav-item-inner nav-storage">${(menu.name)!}</div></li>
                </#list>
            </#if>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>

</div>
<script>
    BUI.use('common/main',function(){
        var config = [
        <#if menus?? && menus?size gt 0>
            <#list menus as menu>
            {
                id: "${(menu.id)!}",
                menu: [
                <#if (menu.childMenus)?? && menu.childMenus?size gt 0>
                    <#list menu.childMenus as menuSub1>
                    {
                        text: "${(menuSub1.name)!}",
                        items: [
                            <#if (menuSub1.childMenus)?? && menuSub1.childMenus?size gt 0>
                                <#list menuSub1.childMenus as menuSub2>
                                    {
                                        id: "${(menuSub2.id)!}",
                                        text: "${(menuSub2.name)!}",
                                        href: "${(menuSub2.url)!}"
                                    },
                                </#list>
                            </#if>
                        ]
                    },
                    </#list>
                </#if>
                ]
            },
            </#list>
        </#if>
        ];
        if (config) {
            new PageUtil.MainPage({
                modulesConfig : config
            });
            return;
        }
        $.post("/permission/menus", {}, function(data) {
            var config = [];
            var nav = $("#J_Nav");
            for (var i = 0; i < data.length; i++) {
                var one = data[i];
                nav.append('<li class="nav-item"><div class="nav-item-inner nav-storage">' + one.name + '</div></li>');
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
            new PageUtil.MainPage({
                modulesConfig : config
            });
        });
    });

</script>
</body>
</html>