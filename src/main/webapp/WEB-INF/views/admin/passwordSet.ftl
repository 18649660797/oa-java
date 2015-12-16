<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="pwdForm" class="form-horizontal" method="post" action="/admin/reset">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>账号：</label>
                    <div class="controls">
                        <input type="text" class="control-text" readonly name="name" value="${(entity.name)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">密码：</label>
                    <div class="controls">
                        <input id="password" data-rules="{required:true}" type="password" class="control-text" name="password" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">确认密码：</label>
                    <div class="controls">
                        <input type="password" data-rules="{equalTo:'#password'}" class="control-text" name="passwordConfirm"  />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use(['bui/form'],function  (Form) {
        Form.Rules.add({
            name : 'uniqueName',  //规则名称
            msg : '管理员名称必须唯一',//默认显示的错误信息
            validator : function(value,baseValue,formatMsg,group){ //验证函数，验证值、基准值、格式化后的错误信息、goup控件
                var flag = false;
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: "/admin/uniqueCheck",
                    data: {name: $.trim(value), id: "${(entity.id)!}"},
                    success: function(data) {
                        flag = data && data.result
                    },
                    async: false
                });
                if (!flag) {
                    return formatMsg;
                }
            }
        });
        var form = new Form.Form({
            srcNode : '#pwdForm',
            submitType : 'ajax',
            callback : function(data){
                if (edy.ajaxHelp.handleAjax((data))) {
                    edy.alert(data.message || "操作成功");
                    top.reload();
                }
            }
        });
        form.render();
    });
</script>
<!-- script end -->
</body>
</html>