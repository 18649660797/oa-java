<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理员列表</title>
    <#include "../include/resource.ftl"/>
</head>
<body>
<div class="row">
    <form id="J_FORM" class="form-panel" action="data" method="post">
        <ul class="panel-content">
            <li>
                <label>名称：</label>
                <input type="text" class="control-text" name="like_username" value="" />
            </li>
            <li>
                <button type="submit" class="button button-primary">查询>></button>
            </li>
        </ul>
    </form>
</div>
<div id="grid"></div>
<script>
    (function($) {
        $(function() {
            var Grid = BUI.Grid,
                    Data = BUI.Data;
            var Grid = Grid,
                Store = Data.Store,
                columns = [
                    {title: 'id', dataIndex: 'id', width: 60},
                    {title: '名称', dataIndex: 'username', width: 60},
                    {title: '操作', dataIndex: 'id', width: 100, renderer: function(val, row) {
                        return edy.rendererHelp.createLink("/index.php/home/admin/edit?id=" + val, "编辑");
                    }}
                ];
            var store = new Store({
                url : '/admin/grid',
                params: {gt_id: 2},
                autoLoad:true, //自动加载数据
                pageSize:30	// 配置分页数目
            }),
            editing = new Grid.Plugins.CellEditing({
                triggerSelected : false //触发编辑的时候不选中行
            }),
            grid = new Grid.Grid({
                render:'#grid',
                columns : columns,
                loadMask: true, //加载数据时显示屏蔽层
                store: store,
                // 底部工具栏
                bbar:{
                    // pagingBar:表明包含分页栏
                    pagingBar:true
                },
                tbar:{ //添加、删除
                    items : [{
                        btnCls : 'button button-small',
                        text : '<i class="icon-plus"></i>添加',
                        listeners : {
                            'click' : function() {
                                location.href = "/admin/add";
                            }
                        }
                    },
                    {
                        btnCls : 'button button-small',
                        text : '<i class="icon-remove"></i>删除',
                        listeners : {
                            'click' : function() {
                                var ids = getSelections();
                                if (!ids) {
                                    return edy.alert("至少选择一个记录");
                                }
                                $.post("/index.php/home/admin/delete", {ids: ids}, function(data) {
                                    if (edy.ajaxHelp.handleAjax(data)) {
                                        edy.alert("删除成功！");
                                        reload();
                                    }
                                });
                            }
                        }
                    }]
                },
                plugins : [editing,Grid.Plugins.CheckSelection],
            });
            grid.render();

            var form = new BUI.Form.HForm({
                srcNode : '#J_FORM'
            }).render();

            form.on('beforesubmit',function(ev) {
                //序列化成对象
                var obj = form.serializeToObject();
                obj.start = 0; //返回第一页
                store.load(obj);
                return false;
            });
            function getSelections() {
                var selections = grid.getSelection();
                var ids = [];
                for (var key in selections) {
                    ids.push(selections[key].id);
                }
                return ids.join(",");
            }
            function reload() {
                store.load();
            }
        });
    } (jQuery));
</script>
</body>
</html>