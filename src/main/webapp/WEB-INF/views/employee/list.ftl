<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="row">
    <form id="J_FORM" class="form-panel" action="data" method="post" style="margin-bottom:0;">
        <div class="panel-title">
            <span>
                <label>姓名：</label>
                <!--<input type="text" class="control-text" name="like_real_name" value="" />-->
                <div id="realName" class="suggest"></div>
                <label>考勤号：</label>
                <div id="attendanceCn" class="suggest"></div>
                <!--<input type="text" class="control-text" name="like_attendance_cn" value="" />-->
                <label>部门：</label>
                <select name="eq_department.id">
                    <option value="">全部</option>
                    <#if departmentList?? && departmentList?size gt 0>
                        <#list departmentList as department>
                            <option value="${(department.id)!}">${(department.name)!}</option>
                        </#list>
                    </#if>
                </select>
            </span>
        </div>
        <ul class="panel-content">
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
            var Grid = BUI.Grid, Data = BUI.Data;
            var editing = new Grid.Plugins.CellEditing({
                triggerSelected : false //触发编辑的时候不选中行
            });
            var Grid = Grid,
                Store = Data.Store,
                columns = [
                    {title: '操作', dataIndex: 'id', width: 60, renderer: function(val, row) {
                        return edy.rendererHelp.createJavaScriptLink("edit", val, "编辑");
                    }},
                    {title: 'id', dataIndex: 'id', width: 60},
                    {title: '姓名', dataIndex: 'name', width: 60},
                    {title: '考勤号', dataIndex: 'attendanceCN', width: 60},
                    {title: '部门', dataIndex: 'department', width: 100}
                ];
            var store = new Store({
                url : '/employee/grid',
                autoLoad:false, //自动加载数据
//                        params : $("#J_FORM").serialize(),
                pageSize:10	// 配置分页数目
            }),
            grid = new Grid.Grid({
                height: edy.getSuggestGridHeight(),
                render:'#grid',
                columns : columns,
                loadMask: true, //加载数据时显示屏蔽层
                store: store,
                // 底部工具栏
                bbar:{
                    // pagingBar:表明包含分页栏
                    pagingBar:true,
                },
                tbar:{ //添加、删除
                    items : [{
                        btnCls : 'button button-small',
                        text : '<i class="icon-plus"></i>添加',
                        listeners : {
                            'click' : edit
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
                                edy.confirm("确认要删除选中的员工：" + getSelectionNames(), function() {
                                    $.post("/employee/delete", {ids: ids}, function(data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            edy.alert("删除成功！");
                                            reload();
                                        }
                                    });
                                });
                            }
                        }
                    }, {
                        btnCls : 'button button-small',
                        text : '<i class="icon-plus"></i>导入',
                        listeners : {
                            'click' : function() {
                                var dialog = new top.BUI.Overlay.Dialog({
                                    title: '导入员工',
                                    width:430,
                                    height:150,
                                    closeAction: "destroy",
                                    loader : {
                                        url : '/employee/importView',
                                        autoLoad : false, //不自动加载
                                        lazyLoad : false, //不延迟加载
                                    },
                                    mask:true,
                                    success: function() {
                                        top.$.ajaxFileUpload({
                                            url : '/employee/import',
                                            secureuri: false,
                                            fileElementId: "file",
                                            dataType : 'json',
                                            method : 'post',
                                            success: function (data) {
                                                edy.alert("导入成功！");
                                                reload();
                                            },
                                            error: function (data, status, e) {
                                                edy.alert("导入失败！");
                                            }
                                        });
                                        this.close();
                                    }
                                });
                                dialog.show();
                                dialog.get('loader').load()
                            }
                        }
                    }]
                },
                plugins : [editing,Grid.Plugins.CheckSelection,Grid.Plugins.ColumnResize],
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
            var obj = form.serializeToObject();
            store.load(obj);
            var Select = BUI.Select;
            var suggest = new Select.Suggest({
                render:'#realName',
                name:'like_name',
                url:'/employee/suggest/name'
            });
            suggest.render();
            var suggestAttendance = new Select.Suggest({
                render:'#attendanceCn',
                name:'like_attendanceCN',
                url:'/employee/suggest/attendanceCN'
            });
            suggestAttendance.render();
            function getSelections() {
                var selections = grid.getSelection();
                var ids = [];
                for (var key in selections) {
                    ids.push(selections[key].id);
                }
                return ids.join(",");
            }
            function getSelectionNames() {
                var selections = grid.getSelection();
                var ids = [];
                for (var key in selections) {
                    ids.push(selections[key].name);
                }
                return ids.join(",");
            }
            function reload() {
                store.load();
            }
            top.reload = reload;
            function edit () {
                var id = $(this).attr("data-edit");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: (id && '编辑' || '新增') + '员工',
                    width:430,
                    height:250,
                    closeAction: "destroy",
                    loader : {
                        url : '/employee/edit',
                        autoLoad : false, //不自动加载
                        lazyLoad : false, //不延迟加载
                    },
                    mask:true,
                    success: function() {
                        top.$("#saveForm").submit();
                        this.close();
                    }
                });
                dialog.show();
                dialog.get('loader').load({id : id})
            }

            $(document).on("click", "[data-edit]", edit);
        });
    } (jQuery));
</script>
</body>
</html>