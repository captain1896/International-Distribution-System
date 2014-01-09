/*****************************************************************<br>
 * <B>FILE :</B> PriceValidResult.java <br>
 * <B>CREATE DATE :</B> 2011-5-6 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.bean;

import java.util.ArrayList;
import java.util.List;

import com.thayer.idsservice.task.download.bean.TResvRate;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-5-6<br>
 * @version : v1.0
 */
public class PriceValidResult {

	private boolean vaildResult;
	private List<TResvRate> resvRateList = new ArrayList<TResvRate>();;

	/**
	 * @return the vaildResult
	 */
	public boolean isVaildResult() {
		return vaildResult;
	}

	/**
	 * @param vaildResult
	 *            the vaildResult to set
	 */
	public void setVaildResult(boolean vaildResult) {
		this.vaildResult = vaildResult;
	}

	/**
	 * @return the resvRateList
	 */
	public List<TResvRate> getResvRateList() {
		return resvRateList;
	}

	/**
	 * @param resvRateList
	 *            the resvRateList to set
	 */
	public void setResvRateList(List<TResvRate> resvRateList) {
		this.resvRateList = resvRateList;
	}

}
