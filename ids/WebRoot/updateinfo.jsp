<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.quartz.Trigger"%>
<%@page import="org.springframework.beans.factory.FactoryBean"%>
<%@page import="org.quartz.SchedulerFactory"%>
<%@page
	import="org.springframework.scheduling.quartz.SchedulerFactoryBean"%>
<%@page import="org.quartz.impl.StdScheduler"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.quartz.Scheduler"%>
<%@page import="org.springframework.jms.listener.DefaultMessageListenerContainer"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	DefaultMessageListenerContainer agodaContainer = (DefaultMessageListenerContainer) wac.getBean("Agoda_queueListenerContainer");
	DefaultMessageListenerContainer bookingcomContainer = (DefaultMessageListenerContainer) wac.getBean("Bookingcom_queueListenerContainer");
	DefaultMessageListenerContainer bookingcom2Container = (DefaultMessageListenerContainer) wac.getBean("Bookingcom2_queueListenerContainer");
	DefaultMessageListenerContainer bookingcom3Container = (DefaultMessageListenerContainer) wac.getBean("Bookingcom3_queueListenerContainer");
	DefaultMessageListenerContainer expediaContainer = (DefaultMessageListenerContainer) wac.getBean("Expedia_queueListenerContainer");
	//DefaultMessageListenerContainer genaresContainer = (DefaultMessageListenerContainer) wac.getBean("Genares_queueListenerContainer");
	DefaultMessageListenerContainer venereContainer = (DefaultMessageListenerContainer) wac.getBean("Venere_queueListenerContainer");
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<title>Hubs1 IDS 管理系统</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<meta http-equiv="refresh" content="20" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="<%=path%>/css/manager.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="<%=path%>/script/jqueryui/themes/ui-lightness/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="<%=path%>/script/jqueryui/themes/ui-lightness/ui.theme.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/css/table/extremecomponents.css" />



		<script src="<%=path%>/script/jquery.js" type="text/javascript">
</script>

		<script src="<%=path%>/script/jqueryui/jquery-ui-1.7.2.custom.min.js"
			type="text/javascript">
</script>

		<script src="<%=path%>/script/jqueryui/js/jquery.bgiframe.min.js"
			type="text/javascript">
</script>

		<script src="<%=path%>/script/jqueryui/js/i18n/jquery-ui-i18n.js"
			type="text/javascript">
</script>

		<script src="<%=path%>/script/JSDateFunction.js"
			type="text/javascript" language="JavaScript">
</script>

		<script language="javascript" type="text/javascript"
			src="<%=path%>/script/datepicker/WdatePicker.js">
</script>

		<script type="text/javascript"
			src="<%=path%>/script/table/extremecomponents.js">
</script>

<script type="text/javascript">
function pauseTask(idsName) {
	var url = '<%=path%>' + "/web/task.do?method=pauseUpdate" + '&idsName='
			+ idsName + '&nd='
			+ new Date().getTime();
	$.post(url, function(data) {
		alert(data);
		window.location.reload();
	});

}

function resumeTask(idsName) {
	var url = '<%=path%>' + "/web/task.do?method=resumeUpdate" + '&idsName='
			+ idsName + '&nd='
			+ new Date().getTime();
	$.post(url, function(data) {
		alert(data);
		window.location.reload();
	});

}
</script>
</head>
	<body>
<table width="95%" border="0" cellspacing="0" cellpadding="0"
	height="100%">

	<tr>
		<td valign="top">


			<br>
			<div id="orderInfo_queryDiv">

				<table width='100%'>
					<tr>
						<td bgcolor='000000'>
							<font color='FFFFFF'><b>Hubs1 IDS 系统 / 更新管理</b> </font>
						</td>
					</tr>
				</table>
				<br>

				<table border="1">

					<tr>
						<th width="200px">
							IDS更新服务名称
						</th>
						<th width="200px">
							监听名称
						</th>
						<th width="200px">
							活动监听数
						</th>
						<th width="200px">
							操作
						</th>
					</tr>
					
					<tr>
						<td align=\"center\">Agoda 更新服务
						</td>
						<td align=\"center\"><%=agodaContainer.getDestination().toString() %></td>
						<td align=\"center\"><%=agodaContainer.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Agoda');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Agoda');">
						</td>
					</tr>
					
					<tr>
						<td align=\"center\">Bookingcom 更新服务
						</td>
						<td align=\"center\"><%=bookingcomContainer.getDestination().toString() %></td>
						<td align=\"center\"><%=bookingcomContainer.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Bookingcom');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Bookingcom');">
						</td>
					</tr>
					
					<tr>
						<td align=\"center\">Bookingcom2 更新服务
						</td>
						<td align=\"center\"><%=bookingcom2Container.getDestination().toString() %></td>
						<td align=\"center\"><%=bookingcom2Container.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Bookingcom2');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Bookingcom2');">
						</td>
					</tr>
					
					<tr>
						<td align=\"center\">Bookingcom3 更新服务
						</td>
						<td align=\"center\"><%=bookingcom3Container.getDestination().toString() %></td>
						<td align=\"center\"><%=bookingcom3Container.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Bookingcom3');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Bookingcom3');">
						</td>
					</tr>
					
					<%-- 
					<tr>
						<td align=\"center\">Genares 更新服务
						</td>
						<td align=\"center\"><%=genaresContainer.getDestination().toString() %></td>
						<td align=\"center\"><%=genaresContainer.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Genares');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Genares');">
						</td>
					</tr>
					--%>
					
					<tr>
						<td align=\"center\">Expedia 更新服务
						</td>
						<td align=\"center\"><%=expediaContainer.getDestination().toString() %></td>
						<td align=\"center\"><%=expediaContainer.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Expedia');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Expedia');">
						</td>
					</tr>
					
					<tr>
						<td align=\"center\">Venere 更新服务
						</td>
						<td align=\"center\"><%=venereContainer.getDestination().toString() %></td>
						<td align=\"center\"><%=venereContainer.getActiveConsumerCount() %></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('Venere');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('Venere');">
						</td>
					</tr>
					
				</table>
			</div>

		</td>
	</tr>

</table>
</body>
</html>

