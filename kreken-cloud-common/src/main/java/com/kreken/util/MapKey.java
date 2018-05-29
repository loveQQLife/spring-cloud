package com.kreken.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapKey {

	/**
	 * 判断里面的所有值时否为空
	 * @param map
	 * @return
	 */
	public static boolean notEmpty(Map<String, ?> map){
		boolean boo = true;
		if(map != null){
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				String key= it.next();
				if(map.get(key) == null || map.get(key) == ""){
					return boo = false;
				}
			}
		}
		return boo;
	}
	
	/**
	 * map是否为空
	 * @param map
	 * @return
	 */
	public static boolean isEmptyMap(Map<String, ?> map){
		if(map == null){
			return true;
		}
		return false;
	}
}
