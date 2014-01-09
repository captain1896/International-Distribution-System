<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>


<table width="100%" border="0" cellspacing="0" cellpadding="0"
	align="left">
	<tr>
		<td height="49" width="100%" valign="top" align="center">
			<ec:table tableId="resvBaseTableId" title="IDS信息日志列表"
				retrieveRowsCallback="org.extremecomponents.table.callback.LimitCallback"
				filterRowsCallback="org.extremecomponents.table.callback.LimitCallback"
				sortRowsCallback="org.extremecomponents.table.callback.LimitCallback"
				items="queryLogData" var="log" filterable="false"
				autoIncludeParameters="false" sortable="false"
				action="listBySearch.action" width="100%" form="OrderInfo_Form"
				onInvokeAction="queryLog();">
				<ec:row>
					<ec:column width="60px" property="id" title="记录号">
					</ec:column>
					<ec:column width="50px" property="exwebCode" title="IATA号" />
					<ec:column width="80px" property="msgId" title="消息号" />
					<ec:column width="60px" property="msgStatus" title="消息状态">
					</ec:column>
					<ec:column width="60px" property="msgRq" title="请求消息" >
						<span onclick="showContent('${log.id}', 'msgRq', this)" style="cursor: hand"> 显示 </span>
					</ec:column>
					<ec:column width="60px" property="msgRs" title="反馈消息" >
						<span onclick="showContent('${log.id}', 'msgRs', this)" style="cursor: hand"> 显示 </span>
					</ec:column>
					<ec:column width="60px" property="msgRsFinal" title="查询消息" >
						<span onclick="showContent('${log.id}', 'msgRsFinal', this)" style="cursor: hand"> 显示 </span>
					</ec:column>
					
					<ec:column width="80px" property="expropId" title="IDS酒店号" />
					<ec:column width="80px" property="fogpropId" title="Fog酒店号" />
					<ec:column width="150px" property="createtime" title="创建时间"
						format="yyyy-MM-dd HH:mm:ss" cell="date" />
					<ec:column width="150px" property="updatetime" title="更新时间"
						format="yyyy-MM-dd HH:mm:ss" cell="date" />
					<ec:column width="80px" property="msgErrorcode" title="错误代码" />
					<ec:column  property="msgError" title="错误信息" />
				</ec:row>
			</ec:table>
			<br>
		</td>
	</tr>

	<tr bgColor=#fcfcfc>
		<td colspan="12" height="10px">
			&nbsp;
		</td>
	</tr>
	<tr bgColor=#fcfcfc>
		<td colspan="12">
			消息信息
		</td>
	</tr>
	<tr bgColor=#fcfcfc>
		<td colspan="12">
			<textarea rows="16" style="font-size: 14.8px; width: 90%"
				id="area_context"></textarea>
		</td>
	</tr>

</table>
