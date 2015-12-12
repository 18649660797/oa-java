<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>考勤数据</title>
<#include "../include/resource.ftl"/>
</head>
<body>
<ul class="breadcrumb">
    <li><a href="/admin/list">管理员列表</a> <span class="divider">/</span></li>
    <li class="active">${entity?has_content?string('编辑', '添加')}管理员</li>
</ul>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="J_Form" class="form-horizontal" method="post" action="/admin/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <h3>员工信息：</h3>
                <div class="control-group">
                    <label class="control-label"><s>*</s>账号：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true}" name="name" value="${(entity.username)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">密码：</label>
                    <div class="controls">
                        <input type="password" class="control-text" name="password" value="${(entity.password)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">权限：</label>
                    <div class="controls">
                        <div id="s1">
                            <input type="hidden" name="groups" id="groups"/>
                        </div>
                    </div>
                </div>
                <div class="form-actions span5 offset3">
                    <button id="btnSearch" type="submit" class="button button-primary">提交</button>
                    <button type="reset" class="button button-primary">重置</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use('bui/form',function  (Form) {
        new Form.Form({
            srcNode : '#J_Form',
            submitType : 'ajax',
            callback : function(data){
                if (edy.ajaxHelp.handleAjax((data))) {
                    BUI.Message.Alert(data.message || "操作成功");
                    location.href = "/admin/list";
                }
            }
        }).render();
        $.get("/permission/grid?gt_id=0", function(data) {
            var items = data;
            var select = new BUI.Select.Select({
                render:'#s1',
                width: 500,
                valueField : '#groups',//显示tag的Combox必须存在valueField
                items:items,
                multipleSelect: true,
                forbitInput : true //只能从列表中选择，输入无效,一般用于suggest中
            });
            select.render();
            select.setSelectedValue("${groupIds!}");
        });
    });
</script>
<!-- script end -->
</body>
</html>