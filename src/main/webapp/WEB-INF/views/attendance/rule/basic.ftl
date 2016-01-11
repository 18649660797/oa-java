<!DOCTYPE html>
<html lang="en">
<head>
<#include "../../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="J_Form" class="form-horizontal" method="post" action="/attendance/rule/basic/save">
                <h2>&nbsp;</h2>
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label">上班打卡时间：</label>
                    <div class="controls">
                        <input readonly type="text" data-messages="{regexp:'时间格式:18:00:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]\:[0-6][0-9]$/}"  class="control-text" name="workFit" value="${(rule.workFit)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">下班打卡时间：</label>
                    <div class="controls">
                        <input name="leaveFit" type="text" data-messages="{regexp:'时间格式:18:00:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]\:[0-6][0-9]$/}"  class="control-text" value="${(rule.leaveFit)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">中午休息开始：</label>
                    <div class="controls">
                        <input type="text" data-messages="{regexp:'时间格式:18:00:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]\:[0-6][0-9]$/}"  class="control-text" name="resetBegin" value="${(rule.restBegin)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">中午休息结束：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-messages="{regexp:'时间格式:09:00:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]\:[0-6][0-9]$/}" name="resetEnd" value="${(rule.restEnd)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">上班打卡偏移量：</label>
                    <div class="controls">
                        <input type="text" class="control-text" name="workFitOffset" data-rules="{required:true,number:true}" value="${(rule.workFitOffset)!}" />
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
                    location.href = "/attendance/rule/basic";
                }
            }
        }).render();
    });
</script>
</body>
</html>