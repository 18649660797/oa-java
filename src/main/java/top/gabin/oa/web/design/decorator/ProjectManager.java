/**
 * Copyright (c) 2016 云智盛世
 * Created with ProjectManager.
 */
package top.gabin.oa.web.design.decorator;

/**
 * 项目管理人
 * @author linjiabin on  16/4/30
 */
public class ProjectManager extends Worker {
    private Worker worker;

    public ProjectManager(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void work() {
        worker.work();
        System.out.println("我懂得项目管理,是一个项目经理.");
    }
}
