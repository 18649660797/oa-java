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
            <li class="nav-item dl-selected"><div class="nav-item-inner nav-storage">权限</div></li>
            <li class="nav-item"><div class="nav-item-inner nav-inventory">考勤</div></li>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>

</div>
<script>
    BUI.use('common/main',function(){
        var config = [{
            id:'permission',
            menu:[{
                text:'权限管理',
                items:[
                    {id:'admin-menu',text:'管理员设置',href:'/admin/list'}
                ]
            }
            ]
        },{
            id:'attendance',
            menu:[{
                text:'部门人员',
                items:[
                    {id:'department-menu',text:'部门管理',href:'/department/list'},
                    {id:'employee-menu',text:'员工管理',href:'/employee/list'}
                ]
            },
            {
                text:'数据规则',
                items:[
                    {id:'attendance-menu',text:'考勤数据',href:'/attendance/list'},
                    {id:'exception-menu',text:'行政登记',href:'/leave/list'},
                    {id:'basic-rule-menu',text:'基础配置',href:'/attendance/rule/basic'}
                ]
            },
            {
                text:'高级规则',
                items:[
                    {id:'high-rule-menu',text:'特殊规则',href:'/attendance/rule/list'}
                ]
            }]
        }];
        new PageUtil.MainPage({
            modulesConfig : config
        });
    });

</script>
</body>
</html>