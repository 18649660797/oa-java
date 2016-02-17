<!DOCTYPE html>
<html lang="en">
<head>
<#include "../../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/attendance/rule/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>名称：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true}" name="name" value="${(entity.name)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">状态：</label>
                    <div class="controls">
                        <select id="status" name="status">
                            <option value="1">启用</option>
                            <option value="0">关闭</option>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">类型：</label>
                    <div class="controls">
                        <select id="type" name="type">
                            <option value="0">上班打卡时间设置</option>
                            <option value="1">下班打卡时间设置</option>
                            <option value="2">上班不打卡设置</option>
                            <option value="3">下班不打卡设置</option>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">开始日期：</label>
                    <div class="controls">
                        <input type="text" class="calendar" name="beginDate"  <#if (entity.beginDate)?has_content>value="${(entity.beginDate)?string("Y-M-d")}"</#if> />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">结束日期：</label>
                    <div class="controls">
                        <input type="text" class="calendar" name="endDate" <#if (entity.endDate)?has_content>value="${(entity.endDate)?string("YYYY-MM-dd")}"</#if> />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">扩展数据：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-messages="{regexp:'时间格式:18:00:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]\:[0-6][0-9]$/}" name="data" value="${(entity.extraData)!}" />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use('bui/form',function  (Form) {
        new Form.Form({
            srcNode : '#saveForm',
            submitType : 'ajax',
            callback : function(data){
                if (edy.ajaxHelp.handleAjax((data))) {
                    edy.alert(data.message || "操作成功");
                    top.reload();
                }
            }
        }).render();
        $("#type").val("${(entity.type.type)!}");
        $("#status").val("${(entity.status.type)!}");
    });

</script>
</body>
</html>