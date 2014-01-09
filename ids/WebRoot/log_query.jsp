<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="java.util.List"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();

	String[] menus = (String[])request.getSession().getAttribute("menus");
%>

<script type="text/javascript">
	
	function queryLog(){
		if($('#exwebCode').val() == null || $('#exwebCode').val().length==0){
			alert("请选择IDS网站!");
			return;
		}
		$("#orderInfo_processData").show();
		$("#orderInfo_queryDiv").show();
		var parameterMap = $("#OrderInfo_Form").serialize();
		 $.post("<%=path%>/web/log.do?method=queryData", parameterMap,
		         function(data){
				    $("#orderInfo_tableData").html(data);
				    $("#orderInfo_processData").hide();
			 	} 
	      ); 
	}
	
	function showContent(id, type, obj) {
 			
		var url = "<%=path%>/web/log.do?method=queryMsg";
		var pars = 'id=' + id +'&type='+type+"&t="+new Date().getTime();
		$.post(url,pars,function(data){		
				$('#area_context').val(data);	
			}
		);
	}

</script>
<table width="95%" border="0" cellspacing="0" cellpadding="0"
	height="100%">

	<tr>
		<td valign="top">
			<form id="OrderInfo_Form" name="OrderInfo_Form" method="post"
				action="<%=path%>/web/log.do?method=queryData">

				<br>
				<div id="orderInfo_queryDiv">

					<table width='100%'>
						<tr>
							<td bgcolor='000000'>
								<font color='FFFFFF'><b>Hubs1 IDS 系统 / 日志查询</b> </font>
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
									<option value="" selected="selected">请选择....</option>
									<option value="960007">Agoda</option>
									<option value="960002">Expedia</option>
									<option value="970003">Bookingcom</option>
									<option value="970020">Genares</option>
									<option value="970013">Venere</option>
									<option value="970011">LateRooms</option>
								</select>
							</td>
							<td>
								Message ID：(Agoda:TicketID)
							</td>
							<td>
								<input size="20" name="msgId" id="msgId" type="text"/>
							</td>
							<td>
								Message Status:
							</td>
							<td>
								<input size="20" name="msgStatus" id="msgStatus" type="text"/>
							</td>

							<td>
								错误代码：
							</td>
							<td>
								<input size="20" name="msgErrorcode" id="msgErrorcode" type="text"/>
							</td>
							<td>
								FOG 酒店号：
							</td>
							<td>
								<input size="20" name="fogpropId" id="fogpropId" type="text"/>
							</td>
						</tr>
						<tr>
							<td>
								IDS酒店号：
							</td>
							<td>
								<input size="20" name="expropId" id="expropId" type="text"/>
							</td>
							
							<td>
								创建起始日期：
							</td>
							<td>
								<input id="checkInSince" name="createtimeQuerySince"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"
									cssClass="Wdate" readonly="true" cssStyle="width:120px" />

							</td>
							<td>
								创建结束日期：
							</td>
							<td colspan="5" >
								<input id="checkInTill" name="createtimeQueryTill" readonly="true"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"
									cssClass="Wdate" cssStyle="width:120px" />
							</td>
							
						</tr>
						
						<tr align="center">
							<td colspan="10">
								<input type="button" id="btn_orderInfoQuery" value="查询"
									onclick="queryLog();" />
								<input type="reset" id="btn_orderInfoReset" value="重置条件" />
							</td>
						
						</tr>

					</table>
				</div>
				<div id="orderInfo_processData" style="display: none;">
					<br />
					<div align='center'>
						<span style='color: #FF0000; font-weight: bold;'>数据加载中，请稍后。。。。。。</span>
					</div>
				</div>

				<div id="orderInfo_tableData">

				</div>

			</form>
		</td>
	</tr>

</table>

