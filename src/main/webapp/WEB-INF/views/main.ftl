<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>my dear edy</title>
    <#include "include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <!-- script start -->
    <script type="text/javascript">
        BUI.use(['bui/layout','bui/tab','bui/data','bui/tree'],function (Layout,Tab,Data,Tree) {
            var nodes = [
                {text: '权限管理', id: '1', href: '/admin/list', expanded: true, children: [
                    {text: '管理员设置', id: '12', href: '/admin/list'}
                ]},
                {text: '考勤管理', id: '3', expanded: true, children: [
                    {text: '员工管理', id: '30', href: '/index.php/home/employee/viewList'},
                    {text: '考勤数据', id: '31', href: '/index.php/home/attendance/viewList'},
                    {text: '行政登记', id: '32', href: '/index.php/home/exception/viewList'}
                ]}
            ];
            var control = new Layout.Viewport({
                width:600,
                height:500,
                elCls : 'ext-border-layout',
                children : [
                {
                    layout : {
                        region : 'north',
                        height : 25
                    },
                    xclass : 'controller',
                    content : "<div style='float: right;margin-right:80px;'>登录会员:${Static["top.gabin.oa.web.utils.AuthUtils"].getCurrentLoginUserName()}<a style='margin-left: 60px;' href='/j_spring_security_logout'>退出登录</a></div>"
                },{
                    xclass : 'controller',
                    layout : {
                        region : 'south',
                        fit : 'height',
                        height : 20
                    },
                    width : 'auto',
                    content : '<div style="text-align: center;width:100%;">copy right <b>gabin</b> <a href="mailto:18649660797@163.com">18649660797@163.com</a></div>'
                },{
                    xclass : 'controller',
                    layout : {
                        region : 'east',
                        fit : 'both',
                        collapsable : true,
                        collapsed : true,
                        width : 150
                    },
                    elCls : 'red',
                    content : ""
                },{
                    layout : {
                        region : 'west',
                        fit : 'both', //height,width,both,none
                        collapsable : true,
                        width : 250
                    },
                    xclass : 'tree-list',//生成tree
                    id : 'mytree',
                    nodes : nodes

                },{
                    xclass : 'nav-tab', //Grid
                    layout : {
                        region : 'center',
                        fit : 'both'
                    },
                    id : 'tab'
                }],
                plugins : [Layout.Border]
            });

            control.render();
            var tab = control.getChild('tab'),//通过id获取
                    tree = control.getChild('mytree',true);  //级联查找树节点

            tree.on('itemclick',function (ev) {
                var node = ev.item,
                    id = node.id,
                    text = node.text,
                    href = node.href;
                if (node.leaf) {
                    console.log(tab.getItemById(id));
                    if (tab.getItemById(id)) {
                        tab.getItemById(id).reload();
                        tab.setActived(id);
                    } else {
                        tab.addTab({
                            title : text,
                            href : href,
                            id : id
                        });
                    }

                }
            });
        });
    </script>
    <!-- script end -->
</div>
</body>
</html>