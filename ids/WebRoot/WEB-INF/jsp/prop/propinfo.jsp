<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="java.util.List"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
%>

<script type="text/javascript">
<!--
	function prop_submit(){
		
		if(window.confirm("请确认是否提交信息？")){
		
			var fields = $("#propInofForm").serializeArray();	
			var isTransfer = true;
			jQuery.each(fields, function(i, field){
				if((field.value == null ||field.value.length == 0) && field.name != 'idsProp' && field.name != 'rateCode'){
					alert("所有信息必须完整输入！");
					isTransfer = false;
					return;
				}
	      	}); 
			
			if(isTransfer){
				var parameterMap = $("#propInofForm").serialize();
				$("#btn_submit").toggle();
				$("#btn_convert").toggle();
				$("#btn_createRate").toggle();
				$("#btn_modifyRate").toggle();
				$("#btn_removeRate").toggle();
				$("#btn_publish").toggle();
				$("#btn_publishRate").toggle();
				
				
				$.post("<%=path%>/web/prop.do?method=transProperty", parameterMap, function(data) {
					alert(data);
					$("#btn_publish").toggle();
					$("#btn_submit").toggle();
					$("#btn_convert").toggle();
					$("#btn_createRate").toggle();
					$("#btn_modifyRate").toggle();
					$("#btn_removeRate").toggle();
					$("#btn_publishRate").toggle();
					
				});
			}
		}
	}
	
	function prop_convert(){
		if ($('#idsProp').val() == null || $('#idsProp').val().length == 0) {
			alert("请输入IDS酒店Code!");
			return;
		}
		prop_submit();
	}
	
	function rate_create(){
		if ($('#rateCode').val() == null || $('#rateCode').val().length == 0) {
			alert("请选择IDS酒店Rate Code!");
			return;
		}
		$('#ratePlanNotifyType').val('New');
		$('#transferType').val('3');
		prop_submit();
	
	}
	
	function rate_modify(){
		if ($('#rateCode').val() == null || $('#rateCode').val().length == 0) {
			alert("请选择IDS酒店Rate Code!");
			return;
		}
		$('#ratePlanNotifyType').val('Overlay');
		$('#transferType').val('3');
		prop_submit();
	
	}
	
	
	function rate_remove(){
		if ($('#rateCode').val() == null || $('#rateCode').val().length == 0) {
			alert("请选择IDS酒店Rate Code!");
			return;
		}
		$('#ratePlanNotifyType').val('Remove');
		$('#transferType').val('3');
		prop_submit();
	
	}
	
//-->
</script>

<style>
<!--
 .td_bg {
	background-color: silver;
	font-weight: bold;
}
-->
</style>

<form method="post" name="propInofForm" id="propInofForm">
	<table width="95%" cellspacing="0" cellpadding="3" border="1">
		<tbody>
			<tr>
				<td class="td_bg">
					酒店Code
				</td>
				<td>
					<input name="prop" id="prop" type="text" value="${propInfo.prop}" />
				</td>
				<td class="td_bg">
					IDS酒店Code
				</td>
				<td>
					<input name="idsProp" id="idsProp" type="text" value="" size="60"/>
				</td>
				
			</tr>
			
			<tr>
				<td class="td_bg">
					酒店名称(中文)
				</td>
				<td>
					<input name="propName_zh" id="propName_zh" type="text" value="${propInfo.propName_zh}" size="60"/>
				</td>
				<td class="td_bg">
					酒店名称(英文)
				</td>
				<td>
					<input name="propName_en" id="propName_en" type="text" value="${propInfo.propName_en}" size="60"/>
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					酒店品牌 ChainCode
				</td>
				<td>
					<input name="orgId" id="orgId" type="text" value="${propInfo.orgId}" size="60"/>
				</td>
				
				<td class="td_bg">
					货币
				</td>
				<td>
					<input name="currencycode" id="currencycode" type="text" value="${propInfo.currencycode}" />
				</td>
			</tr>
			<tr>
				<td class="td_bg">
					酒店星级
				</td>
				<td>
					<input name="starRating" id="starRating" type="text" value="${propInfo.starRating }" />
				</td>
				
				<td class="td_bg">
					营业时间
				</td>
				<td>
					<input name="startDate" id="startDate" type="text" value="00:00:00" />～～<input name="endDate" id="endDate" type="text" value="23:59:59" />
				</td>
			</tr>
			<tr>
				<td class="td_bg">
					酒店位置(Latitude)
				</td>
				<td>
					<input name="latitude" id="latitude" type="text" value="${propInfo.latitude}" />
				</td>
				<td class="td_bg">
					酒店位置(Longitude)
				</td>
				<td>
					<input name="longitude" id="longitude" type="text" value="${propInfo.longitude}" />
				</td>
			</tr>
			<tr>
				<td class="td_bg">
					酒店描述(EN)
				</td>
				<td>
					<textarea rows="5" cols="50" name="simplDesc_en" id="simplDesc_en">${propInfo.simplDesc_en}</textarea>
				</td>
				<td class="td_bg">
					酒店描述(ZH)
				</td>
				<td>
					<textarea rows="5" cols="50" name="simplDesc_zh" id="simplDesc_zh">${propInfo.simplDesc_zh}</textarea>
				</td>
			</tr>
			<tr>
				<td class="td_bg">
					入住时间
				</td>
				<td>
					
					<input name="checkInStr" id="checkInStr" type="text" value="<fmt:formatDate pattern="HH:mm:ss" value="${propInfo.checkIn}" type="both"/> " />
				</td>
				<td class="td_bg">
					退房时间
				</td>
				<td>
					<input name="checkOutStr" id="checkOutStr" type="text" value="<fmt:formatDate pattern="HH:mm:ss" value="${propInfo.checkOut}" type="both"/> " />
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					联系人一姓名
				</td>
				<td>
					<input name="contactName1_givenName" id="contactName1_givenName" type="text" value="Christal" size="30"/>
					<input name="contactName1_surname" id="contactName1_surname" type="text" value="Ye" size="30"/>
				</td>
				<td class="td_bg">
					联系人一JobTitle
				</td>
				<td>
					<input name="contactName1_jobTitle" id="contactName1_jobTitle" type="text" value="Reservations manager" size="40"/>
				</td>
				
			</tr>
			
			<tr>
				<td class="td_bg">
					联系人二姓名
				</td>
				<td>
					<input name="contactName2_givenName" id="contactName2_givenName" type="text" value="Erin" size="30"/>
					<input name="contactName2_surname" id="contactName2_surname" type="text" value="Qian" size="30"/>
				</td>
				<td class="td_bg">
					联系人二JobTitle
				</td>
				<td>
					<input name="contactName2_jobTitle" id="contactName2_jobTitle" type="text" value="Account Supervisor" size="40"/>
				</td>
				
			</tr>
			
			<tr>
				<td class="td_bg">
					联系人三姓名
				</td>
				<td>
					<input name="contactName3_givenName" id="contactName3_givenName" type="text" value="Erin" size="30"/>
					<input name="contactName3_surname" id="contactName3_surname" type="text" value="Qian" size="30"/>
				</td>
				<td class="td_bg">
					联系人三JobTitle
				</td>
				<td>
					<input name="contactName3_jobTitle" id="contactName3_jobTitle" type="text" value="Account Supervisor" size="40"/>
				</td>
				
			</tr>
			<tr>
				<td class="td_bg">
					酒店地址一
				</td>
				<td>
					<input name="address1_en" id="address1_en" type="text" value="${propInfo.address1_en}" size="60"/>
				</td>
				<td class="td_bg">
					酒店地址二
				</td>
				<td>
					<input name="address2_en" id="address2_en" type="text" value="${propInfo.address2_en}" size="60"/>
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					城市代码
				</td>
				<td>
					<input name="cityId" id="cityId" type="text" value="${propInfo.cityId}" />
				</td>
				<td class="td_bg">
					国家代码
				</td>
				<td>
					<input name="countryId" id="countryId" type="text" value="${propInfo.countryId}" />
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					财务联系Mail
				</td>
				<td>
					<input name="billMail" id="billMail" type="text" value="account@hubs1.net" size="60"/>
				</td>
				<td class="td_bg">
					预订联系人Mail
				</td>
				<td>
					<input name="reservationMail" id="reservationMail" type="text" value="tprsvns@hubs1.net" size="60"/>
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					MaxChildAge
				</td>
				<td >
					<input name="maxChildAge" id="maxChildAge" type="text" value="12" />
				</td>
				<td class="td_bg">
					酒店网址
				</td>
				<td>
					<input name="url" id="url" type="text" value="${propInfo.website}" size="60"/>
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					邮编
				</td>
				<td >
					<input name="zip" id="zip" type="text" value="40"  value="${propInfo.zip}"/>
				</td>
				<td class="td_bg">
					备用
				</td>
				<td>
					
				</td>
			</tr>
			
			<tr>
				<td class="td_bg">
					房型信息
				</td>
				<td colspan="3">
					<c:forEach items="${rooms}" var="room">
					<input type="checkbox" name="roomCode" id="roomCode" value="${room.room.roomId}">${room.room.roomId} —— ${room.room.name_zh}( ${room.room.name_en} ) &nbsp; &nbsp; &nbsp;
					</c:forEach>
				</td>
				
			</tr>
			<tr>
				<td colspan="4" align="center">
					<input type="button" name="btn_submit" id="btn_submit" value="发布酒店信息" onclick="prop_submit();"/>&nbsp;&nbsp;&nbsp;<input type="button" name="btn_convert" id="btn_convert" value="转换酒店信息" onclick="prop_convert();"/>
					&nbsp;&nbsp;&nbsp;<input type="button" name="btn_publish" id="btn_publish" value="发布中，请稍候..." disabled="disabled" style="display: none;"/>
				</td>
				
			</tr>
			<tr>
				<td class="td_bg">
					价格信息
				</td>
				<td colspan="3">
					<c:forEach items="${rates}" var="rate">
					<input type="checkbox" name="rateCode" id="rateCode" value="${rate.rateId}">${rate.rateId} —— ${rate.name_zh}( ${rate.name_en} ) &nbsp; &nbsp; &nbsp;
					</c:forEach>
				</td>
				
			</tr>
			
			<tr>
				<td colspan="4" align="center">
					<input type="button" name="btn_createRate" id="btn_createRate" value="发布价格代码" onclick="rate_create();"/>
					<input type="button" name="btn_modifyRate" id="btn_modifyRate" value="更新价格代码" onclick="rate_modify();"/>
					<input type="button" name="btn_removeRate" id="btn_removeRate" value="删除价格代码" onclick="rate_remove();"/>
					&nbsp;&nbsp;&nbsp;<input type="button" name="btn_publishRate" id="btn_publishRate" value="发布中，请稍候..." disabled="disabled" style="display: none;"/>
				</td>
				
			</tr>
		</tbody>
	</table>
	<input name="exwebCode" id="exwebCode" type="hidden" value="${exwebCode}"/>
	<input name="transferType" id="transferType" type="hidden" value="1"/>
	<input name="ratePlanNotifyType" id="ratePlanNotifyType" type="hidden" value="New"/>
</form>


