package com.thayer.idsservice.task.update.bean;

import java.util.ArrayList;
import java.util.List;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;

/**
 * <B>Function :</B> update更新bean<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-12-2<br>
 * @version : v1.0
 */
public class AvailBean {

    /**
     * 计划级更新内容(IDS接口使用)
     */
    private List<RateDataBO> rateDataBOList;

    /**
     * 房型级或者酒店级更新内容(GDS接口使用)
     */
    private List<AvailAllow> availAllowList;

    private int[] weeks = null;

    public AvailBean() {
        rateDataBOList = new ArrayList<RateDataBO>();
        availAllowList = new ArrayList<AvailAllow>();
    }

    public List<RateDataBO> getRateDataBOList() {
        return rateDataBOList;
    }

    public void setRateDataBOList(List<RateDataBO> rateDataBOList) {
        this.rateDataBOList = rateDataBOList;
    }

    public List<AvailAllow> getAvailAllowList() {
        return availAllowList;
    }

    public void setAvailAllowList(List<AvailAllow> availAllowList) {
        this.availAllowList = availAllowList;
    }

    public int[] getWeeks() {
        return weeks;
    }

    public void setWeeks(int[] weeks) {
        this.weeks = weeks;
    }
}
