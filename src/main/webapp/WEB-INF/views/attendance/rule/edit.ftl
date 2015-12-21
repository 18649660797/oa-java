<!DOCTYPE html>
<html lang="en">
<head>
<#include "../../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/attendance/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>姓名：</label>
                    <div class="controls">
                        <input readonly type="text" class="control-text" data-rules="{required:true}" name="employeeName" value="${(entity.employee.name)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">考勤号：</label>
                    <div class="controls">
                        <input readonly type="text" class="control-text" value="${(entity.employee.attendanceCN)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">考勤日期：</label>
                    <div class="controls">
                        <input readonly type="text" class="control-text" name="workDate" value="${(entity.workDate)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">上午打卡：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-messages="{regexp:'时间格式:09:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]$/}" name="amTime" value="${(entity.amTime)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">下午打卡：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-messages="{regexp:'时间格式:18:00'}" data-rules="{regexp:/^[0-2][0-9]\:[0-6][0-9]$/}" name="pmTime" value="${(entity.pmTime)!}" />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>