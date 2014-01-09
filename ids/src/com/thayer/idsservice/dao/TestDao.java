package com.thayer.idsservice.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.thayer.idsservice.service.DbCacheFilterService;

public class TestDao {
	public static void main(String[] args) {

		TestDao test = new TestDao();
		test.test();
	}

	public void test() {
			

		String[] CONFIG_FILES = {

				"E:\\Users\\irywu\\Workspaces\\MyEclipse 8.5\\Hubs1_IDS_Service\\src\\applicationContext-dao.xml",
				"E:\\Users\\irywu\\Workspaces\\MyEclipse 8.5\\Hubs1_IDS_Service\\src\\applicationContext-ids-init-test.xml",
				"E:\\Users\\irywu\\Workspaces\\MyEclipse 8.5\\Hubs1_IDS_Service\\src\\idsconfig\\applicationContext-ids-service.xml",
				"E:\\Users\\irywu\\Workspaces\\MyEclipse 8.5\\Hubs1_IDS_Service\\src\\idsconfig\\bookingcom\\applicationContext-ids-service-bookingcom.xml"};
		ApplicationContext factory = null;
		factory = new FileSystemXmlApplicationContext(CONFIG_FILES);
		EIdsCacheDAO dao = (EIdsCacheDAO) factory.getBean("eIdsCacheDAO");
		DbCacheFilterService server = (DbCacheFilterService)factory.getBean("bookingcom_dbCacheFilterService");
		insert(dao);
//		update(dao);
//		findByPK(dao);
//		findByProperty(dao);
//		put(server);
//		getMap(server);

	}
	
	public void getMap(DbCacheFilterService server){
		Calendar c  = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		Map<String,EIdsCache> map = server.getMap("bookingcom", "1323", "13",Calendar.getInstance().getTime(),c.getTime());
		if(map != null && map.size()>0){
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				System.out.println(iterator.next());
			}
		}else{
			System.out.println("map is null");
		}
		
	}
	
	public void put(DbCacheFilterService server){
		EIdsCacheKey key = new EIdsCacheKey();
		key.setProp("13");
		key.setPlanCode("1323");
		key.setIdsType("bookingcom");
		key.setAvailDate(Calendar.getInstance().getTime());
		EIdsCache data = new EIdsCache(key, "221", 112, 2);
		data.setSingleRate(11.88D);
		data.setDoubleRate(11.88D);
		data.setTripleRate(11.88D);
		data.setQuadRate(11.88D);
		List<EIdsCache>  list = new ArrayList<EIdsCache>();
		list.add(data);
		server.put(list);
		
	}
	
	public void findByPK(EIdsCacheDAO dao){
		EIdsCache  result =  dao.findByPK("bookingcom", "1123", "12", Calendar.getInstance().getTime());
		if(result == null){
			System.out.println("result is null");
		}else{
			System.out.println(result.getMinLos());
		}
	}
	public void findByProperty(EIdsCacheDAO dao){
		Calendar c  = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		
		List<EIdsCache>  result =  dao.findByProperty("bookingcom", "1123", "12", Calendar.getInstance().getTime(),c.getTime());
		if(result == null || result.size()<=0){
			System.out.println("result is null");
		}else{
			System.out.println("result.size = "+result.size());
		}
	}

	public void update(EIdsCacheDAO dao) {
		EIdsCacheKey key = new EIdsCacheKey();
		key.setProp("12");
		key.setPlanCode("1123");
		key.setIdsType("bookingcom");
		key.setAvailDate(Calendar.getInstance().getTime());
		EIdsCache data = new EIdsCache(key, "22", 12, 1);
		data.setSingleRate(11.22D);
		data.setDoubleRate(11.22D);
		data.setTripleRate(11.22D);
		data.setQuadRate(11.22D);
		dao.update(data);
		
	}
	public void insert(EIdsCacheDAO dao) {
		EIdsCacheKey key = new EIdsCacheKey();
		key.setProp("12");
		key.setPlanCode("1123");
		key.setIdsType("bookingcom");
		key.setAvailDate(Calendar.getInstance().getTime());
		EIdsCache data = new EIdsCache(key, "22", 12, 1);
		data.setSingleRate(11.89D);
		data.setDoubleRate(11.89D);
		data.setTripleRate(11.89D);
		data.setQuadRate(11.89D);
		data.setBreakfastNum(1);
		dao.save(data);

	}

}
