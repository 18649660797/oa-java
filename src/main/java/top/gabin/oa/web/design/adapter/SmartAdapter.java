/**
 * Copyright (c) 2016 云智盛世
 * Created with SmartAdapter.
 */
package top.gabin.oa.web.design.adapter;

/**
 * 伪装成聪明人的适配器
 * @author linjiabin on  16/5/5
 */
public class SmartAdapter extends VariousGeBright {
    private Cobbler cobbler;

    public SmartAdapter(Cobbler cobbler) {
        this.cobbler = cobbler;
    }

    @Override
    public String idea() {
        String idea = null;
        while ((idea = cobbler.idea()) == null) {

        }
        return idea;
    }
}
