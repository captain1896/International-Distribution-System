package com.thayer.idsservice.bean;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.filter.ExportFilterUtils;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.LimitFactory;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.TableLimitFactory;
import org.springframework.web.util.WebUtils;


 /**
 * <B>Function :</B>表格控件extremecomponents 分页辅助bean <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * @author : Jimmy.Chen<br>
 * @since : Jul 5, 2010<br>
 * @version : v1.0
 */
public class PageSortModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3693563992346427890L;
	/**
	 * 每页的记录数
	 */
	private int pageSize = 100;
	/**
	 * 页号
	 */
	private int pageNo = 1;// 页号
	/**
	 * 记录总数
	 */
	private transient int totalRows = 0;
	/**
	 * 总页数
	 */
	private transient int pageCount = 0;
	private boolean all = false;
	private Map orderMap = null;
	private Limit limit;
	private HttpServletRequest request;

	public PageSortModel(HttpServletRequest request, String tableId) {
		this.request = request;
		if (tableId == null)
			tableId = "ec";
		Context context = new HttpServletRequestContext(request);
		LimitFactory limitFactory = new TableLimitFactory(context, tableId);
		limit = new TableLimit(limitFactory);
		this.pageNo = limit.getPage();
		String rcdStr = context.getParameter(tableId + "_" + TableConstants.CURRENT_ROWS_DISPLAYED);
		if (StringUtils.isNotBlank(rcdStr)) {
			this.pageSize = Integer.parseInt(rcdStr);
		}
		if (ExportFilterUtils.isExported(context))
			all = true;
		orderMap = WebUtils.getParametersStartingWith(request, tableId + "_" + TableConstants.SORT);

	}

	public PageSortModel(int pageNo) {
		this.pageNo = pageNo;
	}

	public Map getOrderMap() {
		return orderMap;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		if (limit != null) {
			limit.setRowAttributes(totalRows, pageSize);
		}
		request.setAttribute("totalRows", new Integer(totalRows));

	}

	public int getRowStart() {
		return (pageNo - 1) * pageSize;

	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

}
