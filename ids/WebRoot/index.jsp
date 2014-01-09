<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>

<%
	String path = request.getContextPath();
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
		$(function() {
			$("#tabs").tabs( {
				ajaxOptions : {
					error : function(xhr, status, index, anchor) {
						//$(anchor.hash).html("Couldn't load this tab. We'll try to fix this as soon as possible. ");
					}
				}
			});
		
		});
</script>

	</head>
	<body>
		<div id="tabs">
			<ul>

				<li>
					<a href="<%=path%>/log_query.jsp" title="Todo Overview">更新日志管理 </a>
				</li>

				<li>
					<a href="<%=path%>/taskinfo_tag.jsp" title="Todo Overview">订单下载服务管理 </a>
				</li>
				
				<li>
					<a href="<%=path%>/updateinfo_tag.jsp" title="Todo Overview">更新服务管理 </a>
				</li>
				<li>
					<a href="<%=path%>/hotel.jsp" title="Todo Overview">酒店发布管理 </a>
				</li>
			</ul>
		</div>

		<div id="Todo_Overview"></div>
	</body>
</html>
