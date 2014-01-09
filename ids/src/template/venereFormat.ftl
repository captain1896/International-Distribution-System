<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<style>
BODY {
	FONT: x-small arial,helvetica,clean,sans-serif
}
TABLE {
	FONT-WEIGHT: normal; FONT-SIZE: 100%; LINE-HEIGHT: normal; FONT-STYLE: normal; FONT-VARIANT: normal
}
PRE {
	FONT-SIZE: 108%; LINE-HEIGHT: 100%; FONT-FAMILY: monospace
}
CODE {
	FONT-SIZE: 108%; LINE-HEIGHT: 100%; FONT-FAMILY: monospace
}
KBD {
	FONT-SIZE: 108%; LINE-HEIGHT: 100%; FONT-FAMILY: monospace
}
SAMP {
	FONT-SIZE: 108%; LINE-HEIGHT: 100%; FONT-FAMILY: monospace
}
TT {
	FONT-SIZE: 108%; LINE-HEIGHT: 100%; FONT-FAMILY: monospace
}

TD {
	FONT-SIZE: 10pt; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
BODY {
	MARGIN-TOP: 0px; FONT-SIZE: 10pt; MARGIN-LEFT: 10px; MARGIN-RIGHT: 5px; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
INPUT {
	FONT-SIZE: 10pt; FONT-FAMILY: Courier New,Courier,monospace
}
TEXTAREA {
	FONT-FAMILY: Courier New,Courier,monospace
}
.reservationTitle {
	FONT-WEIGHT: bold; FONT-SIZE: 17px; COLOR: #652890; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.txtBody {
	FONT-SIZE: 12px; COLOR: #000000; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.hpa_preno_confirmed {
	FONT-SIZE: 11px; COLOR: #009900; LINE-HEIGHT: 14pt; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif; TEXT-ALIGN: center
}
.sectionTitle {
	FONT-WEIGHT: bold; FONT-SIZE: 14px; COLOR: #652890; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.operationsSectionTD {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; PADDING-TOP: 10px
}
.reservationInfoNames {
	FONT-WEIGHT: bold; FONT-SIZE: 12px; COLOR: #000000; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.reservationInfoTD {
	VERTICAL-ALIGN: top
}
.vtable_u {
	BORDER-RIGHT: #3366cc 0px solid; BORDER-TOP: #3366cc 1px solid; BORDER-LEFT: #3366cc 0px solid; BORDER-BOTTOM: #3366cc 1px solid
}
.roomsDetailTitleTD {
	PADDING-RIGHT: 10px; PADDING-LEFT: 10px; FONT-SIZE: 12px; PADDING-BOTTOM: 5px; COLOR: #ffffff; PADDING-TOP: 5px; TEXT-ALIGN: center
}
.htitle_t {
	FONT-WEIGHT: bold; FONT-SIZE: 12px; COLOR: #ffffff; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif; BACKGROUND-COLOR: #79a7ec
}
.vtable_l {
	BORDER-RIGHT: #3366cc 0px solid; BORDER-TOP: #3366cc 0px solid; BORDER-LEFT: #3366cc 0px solid; BORDER-BOTTOM: #3366cc 1px solid
}
.vtable_r {
	BORDER-RIGHT: #3366cc 0px solid; BORDER-TOP: #3366cc 0px solid; BORDER-LEFT: #3366cc 0px solid; BORDER-BOTTOM: #3366cc 1px solid
}
.htxt_t {
	FONT-SIZE: 12px; COLOR: #2c4178; LINE-HEIGHT: 14pt; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.vtable_lb {
	BORDER-RIGHT: #3366cc 0px solid; BORDER-TOP: #3366cc 0px solid; BORDER-LEFT: #3366cc 0px solid; BORDER-BOTTOM: #3366cc 0px solid
}
.vtable_rb {
	BORDER-RIGHT: #3366cc 0px solid; BORDER-TOP: #3366cc 0px solid; BORDER-LEFT: #3366cc 0px solid; BORDER-BOTTOM: #3366cc 0px solid
}
.reservationTxt {
	FONT-SIZE: 12px; COLOR: #000000; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.reservationClientValuesTD {
	PADDING-LEFT: 10px; FONT-SIZE: 12px; VERTICAL-ALIGN: top; COLOR: #000000; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
.hpa_preno_cancelled {
	FONT-SIZE: 11px; COLOR: #cc0000; LINE-HEIGHT: 14pt; FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif; TEXT-ALIGN: center
}
</style>
</head>

<body>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD><FONT class=reservationTitle>Reservation details 
                        ${detailsBean.resIDValue} for ${detailsBean.propId?if_exists} </FONT>
                        <#if detailsBean.cancelled>
                        <DIV class=txtBody><B><FONT 
	                        class=hpa_preno_cancelled>CANCELLED:</FONT></B> 
	                        Reservation cancelled (Status <B><FONT 
	                        class=hpa_preno_cancelled>${detailsBean.resStatusText?if_exists}</FONT></B>) 
                    	</DIV>
                    	<#else>
                    	<DIV class=txtBody><B><FONT 
                        class=hpa_preno_confirmed>CONFIRMED:</FONT></B> 
                        Reservation confirmed (Status <B><FONT 
                        class=hpa_preno_confirmed>${detailsBean.resStatusText?if_exists} 
                    		</FONT></B>)
                    	</DIV>
                    	</#if>
                    	</TD></TR></TBODY></TABLE><BR>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD vAlign=top width="50%">
                        <TABLE cellSpacing=0 cellPadding=0 width="100%" 
border=0>
                          <TBODY>
                          <TR>
                            <TD class=operationsSectionTD><FONT 
                              class=sectionTitle>Reservation information 
                              </FONT><BR>
                              <TABLE cellSpacing=0 cellPadding=0 border=0>
                                <TBODY>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Reservation 
                                number: </TD>
                                <TD class=reservationDataValuesTD>${detailsBean.resIDValue} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Check 
                                in: </TD>
                                <TD class=reservationDataValuesTD>${detailsBean.start.getTime()?string('yyyy-MM-dd')} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Check 
                                out: </TD>
                                <TD class=reservationDataValuesTD>${detailsBean.end.getTime()?string('yyyy-MM-dd')} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Number 
                                of nights: </TD>
                                <TD class=reservationDataValuesTD>${detailsBean.nights} </TD></TR>
                                </TBODY></TABLE><BR></TD></TR>
                          <TR>
                            <TD class=operationsSectionTD>
                              <TABLE cellSpacing=0 cellPadding=4>
                                <TBODY>
                                <TR>
                                <TD class="vtable_u htitle_t roomsDetailTitleTD" 
                                align=left>Day / Rooms</TD>
                                <#list detailsBean.roomDetails as room>
								  	<TD 
		                                class="vtable_u htitle_t roomsDetailTitleTD">${room.rooms} 
		                                ${room.roomDesc} </TD>
								</#list>
                                <#assign col=1/>
                                <TD 
                                class="vtable_u htitle_t roomsDetailTitleTD">Total</TD></TR>
                                <#list mailcontent as content>
                                	<TR>
	                                <TD class="vtable_l htxt_t" align=left>
	                                ${content.date?if_exists}
	                                </TD>
	                                <#list content.dayRateList as dayRate>
	                                	<TD class="vtable_r htxt_t" 
	                                		align=middle>#{dayRate; m2M2}</TD>
	                                </#list>
	                                <TD class="vtable_r htxt_t" 
	                                align=middle>#{content.dayTotal; m2M2}</TD></TR>
                                </#list>
                                 <TR>
                                <TD class="vtable_lb htxt_t" align=left 
                                colSpan=${mutliRooms}><B>FOG Total amount of stay </B></TD>
                                <TD class="vtable_rb htxt_t" 
                                align=middle><B>#{fogTotal; m2M2}&nbsp;${detailsBean.currencyCode?if_exists}</B></TD></TR>
                                <TR>
                                <TD class="vtable_lb htxt_t" align=left 
                                colSpan=${mutliRooms}><B>Venere Total amount of stay </B></TD>
                                <TD class="vtable_rb htxt_t" 
                                align=middle><B>#{detailsBean.totalAmountAfterTax; m2M2}&nbsp;${detailsBean.currencyCode?if_exists}</B></TD></TR></TBODY></TABLE><BR><BR><BR></TD></TR>
                          <TR>
                            <TD class=operationsSectionTD><FONT 
                              class=sectionTitle>CLIENT INFORMATION</FONT> 
                              <BR><FONT class=reservationTxt>The guest's 
                              information is visible up until 7 days after the 
                              check-out date for reservations in status OK 
                              </FONT><BR>
                              <TABLE cellSpacing=0 cellPadding=0 border=0>
                                <TBODY>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Name: 
                                </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.personDetail.firstName} 
                                ${detailsBean.personDetail.lastName?if_exists} </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Country: 
                                </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.personDetail.countryName?if_exists} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Telephone: 
                                </TD>
                                <TD 
                                class=reservationClientValuesTD>${detailsBean.personDetail.phoneNumber?if_exists} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Fax: 
                                </TD>
                                <TD class=reservationClientValuesTD></TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Email: 
                                </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.personDetail.email?if_exists}
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Guest's 
                                notes </TD></TD>
                                <TD class=reservationClientValuesTD>
                                <#list detailsBean.remarks as remark>
								  	${remark}<br/>
								</#list>
								</TD></TR></TBODY></TABLE><BR></TD></TR>
                          <TR>
                            <TD class=operationsSectionTD><FONT 
                              class=sectionTitle>CREDIT CARD DETAILS</FONT> 
                              <BR><FONT class=reservationTxt>The credit card 
                              information will be visible up until 72 hours 
                              after the reservation has been confirmed, and 
                              between the check-in and until a week after the 
                              check-out date.</FONT> <BR>
                              <TABLE cellSpacing=0 cellPadding=0 border=0>
                                <TBODY>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Type 
                                of credit card: </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.creditCardDetail.cardCode?if_exists} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Holder: 
                                </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.creditCardDetail.cardHolderName?if_exists} </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Expiry 
                                date: </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.creditCardDetail.expireDate?if_exists} 
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Number: 
                                </TD>
                                <TD 
                                class=reservationClientValuesTD>${detailsBean.creditCardDetail.cardNumber?if_exists}${detailsBean.creditCardDetail.maskedCardNumber?if_exists}  
                                </TD></TR>
                                <TR>
                                <TD 
                                class="reservationInfoNames reservationInfoTD">Card 
                                security code: </TD>
                                <TD class=reservationClientValuesTD>${detailsBean.creditCardDetail.seriesCode?if_exists} 
                                </TD></TR></TBODY></TABLE>
</body>
</html>
