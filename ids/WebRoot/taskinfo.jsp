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
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	StdScheduler scheduler = (StdScheduler) wac.getBean("schedulerFactoryBean");

	String[] triggerGroups = scheduler.getTriggerGroupNames();

	Map<String, String> map = new HashMap<String, String>();
	map.put("4", "STATE_BLOCKED");
	map.put("2", "STATE_COMPLETE");
	map.put("3", "STATE_ERROR");
	map.put("-1", "STATE_NONE");
	map.put("0", "STATE_NORMAL");
	map.put("1", "STATE_PAUSED");
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
		<meta http-equiv="refresh" content="120" />
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
function pauseTask(triggerName, groupName) {
	var url = '<%=path%>' + "/web/task.do?method=pauseTask" + '&triggerName='
			+ triggerName + '&groupName=' + groupName + '&nd='
			+ new Date().getTime();
	$.post(url, function(data) {
		alert(data);
		window.location.reload();
	});

}

function resumeTask(triggerName, groupName) {
	var url = '<%=path%>' + "/web/task.do?method=resumeTask" + '&triggerName='
			+ triggerName + '&groupName=' + groupName + '&nd='
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
							<font color='FFFFFF'><b>Hubs1 IDS 系统 / 任务管理</b> </font>
						</td>
					</tr>
				</table>
				<br>

				<table border="1">

					<tr>
						<th width="200px">
							触发器名称
						</th>
						<th width="200px">
							任务名称
						</th>
						<th width="200px">
							任务状态
						</th>
						<th width="200px">
							操作
						</th>
					</tr>
					<%
						for (int i = 0; i < triggerGroups.length; i++) {

							String[] triggers = scheduler.getTriggerNames(triggerGroups[i]);

							for (int j = 0; j < triggers.length; j++) {

								Trigger tg = scheduler.getTrigger(triggers[j], triggerGroups[i]);
					%>
					<tr>
						<td align=\"center\"><%=tg.getName()%>
						</td>
						<td align=\"center\"><%=tg.getJobName()%></td>
						<td align=\"center\"><%=map.get(String.valueOf(scheduler.getTriggerState(triggers[j], triggerGroups[i])))%></td>
						<td>
							<input value="暂停任务" name="btn_pause" type="button"
								onclick="pauseTask('<%=tg.getName()%>','<%=triggerGroups[i]%>');">
							<input value="恢复任务" name="btn_pause" type="button"
								onclick="resumeTask('<%=tg.getName()%>','<%=triggerGroups[i]%>');">
						</td>
					</tr>
					<%
						}
						}
					%>
				</table>
			</div>

		</td>
	</tr>

</table>
</body>
</html>

