<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="java.util.List"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
%>

<script type="text/javascript">

function queryProp() {
	if ($('#exwebCode').val() == null || $('#exwebCode').val().length == 0) {
		alert("请选择IDS网站!");
		return;
	}
	if ($('#prop').val() == null || $('#prop').val().length == 0) {
		alert("请输入酒店ID!");
		return;
	}
	
	$("#orderInfo_processData").show();
	$("#orderInfo_queryDiv").show();
	var parameterMap = $("#OrderInfo_Form").serialize();
	$.post("<%=path%>/web/prop.do?method=queryProperty", parameterMap, function(
			data) {
		$("#orderInfo_tableData").html(data);
		$("#orderInfo_processData").hide();
	});
}


</script>
<table width="95%" border="0" cellspacing="0" cellpadding="0"
	height="100%">

	<tr>
		<td valign="top">


			<br>
			<div id="orderInfo_queryDiv">
				<form id="OrderInfo_Form" name="OrderInfo_Form" method="post"
					action="<%=path%>/web/prop.do?method=querProperty">
					<table width='100%'>
						<tr>
							<td bgcolor='000000'>
								<font color='FFFFFF'><b>Hubs1 IDS 系统 / 酒店信息发布</b> </font>
							</td>
						</tr>
					</table>
					<br>

					<table width='100%'>
						<tr>
							<td>
								IDS网站：
							</td>
							<td>
								<select id="exwebCode" name="exwebCode">
									<option value="asiarooms">
										Laterooms
									</option>
								</select>
							</td>
							<td>
								Fog系统酒店号：
							</td>
							<td>
								<input size="20" name="prop" id="prop" type="text" />
							</td>


						</tr>

						<tr align="center">
							<td colspan="4">
								<input type="button" id="btn_orderInfoQuery" value="查询酒店信息"
									onclick="queryProp();" />
							</td>

						</tr>

					</table>
				</form>
			</div>
			<div id="orderInfo_processData" style="display: none;">
				<br />
				<div align='center'>
					<span style='color: #FF0000; font-weight: bold;'>数据加载中，请稍后。。。。。。</span>
				</div>
			</div>

			<div id="orderInfo_tableData">

			</div>


		</td>
	</tr>

</table>

