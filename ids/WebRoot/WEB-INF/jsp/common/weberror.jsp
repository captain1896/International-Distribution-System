<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+String.valueOf(request.getServerPort())+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><spring:message code="payPal.fail"></spring:message>
		</title>
		<link href="<%=basePath%><spring:theme code="theme.paymentCenter"/>"
			rel="stylesheet" type="text/css" />
		<link href="<%=basePath%><spring:theme code="theme.paymentSetin"/>"
			rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div class="logoTop"></div>
		<div class="blueLine"></div>
		<div class="infoBlock2">
			<div class="payInfo">
				<div class="title">
					<c:set value="${exception}" var="ee" />
					<spring:message code="${ee.message}"></spring:message>
				</div>
				<div class="payError"></div>
				<div class="close" align="center">
					<a href="javascript:window.close();"><spring:message
							code="payPal.close"></spring:message> </a>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		

	</body>
</html>
