package com.thayer.idsservice.ids.venere.util;

public class VenereStatus {
	public static enum RES_STATUS {
		OK("OK", "OK IB"), CANC("CO", "CANC_OK"), UDR("UD", "UDR"), A_VOID("VA", "A-VOID");
		private String value = null;
		private String desc = null;

		RES_STATUS(String value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public String getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static boolean isNewResv(String status) {
		if (VenereStatus.RES_STATUS.OK.getValue().equals(status)) {
			return true;
		}
		return false;
	}

	public static boolean isCxlResv(String status) {
		if (VenereStatus.RES_STATUS.CANC.getValue().equals(status)) {
			return true;
		}
		if (VenereStatus.RES_STATUS.UDR.getValue().equals(status)) {
			return true;
		}
		if (VenereStatus.RES_STATUS.A_VOID.getValue().equals(status)) {
			return true;
		}
		return false;
	}

	public static String getResStatusDesc(String status) {
		String result = null;
		for (RES_STATUS s : RES_STATUS.values()) {
			if (s.getValue().equals(status)) {
				result = s.getDesc();
				break;
			}
		}
		return result;
	}
}
