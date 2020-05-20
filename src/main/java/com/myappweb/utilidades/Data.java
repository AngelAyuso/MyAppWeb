package com.myappweb.utilidades;

import java.util.Collection;
import java.util.Date;

public class Data {
	
	public static boolean isValid(String string) {
		return string != null && !string.isEmpty();
	}
	
	public static boolean isValid(Number number) {
		return number != null;
	}
	
	public static boolean isValid(Collection<?> collection){
        return collection != null && collection.size() > 0;
	}
	
	public static boolean isValid(Date date) {
		return date != null;
	}
	public static boolean isValid(Object obj) {
		return obj != null;
	}
	
	public static boolean isValidAndTrue(Boolean bool) {
		return bool != null && bool;
	}
	
	public static Integer toInt(Number number) {
		Integer integer = new Integer(0);
		if (isValid(number)) {
			integer = number.intValue();
		}
		return integer;
	}
	public static Integer toInt(String string) {
		Integer integer = new Integer(0);
		if (isValid(string)) {
			integer = Integer.parseInt(string);
		}
		return integer;
	}
	public static Double num2Doub (Number number) {
		Double doubleValue = 0.0;
		if (Data.isValid(number)) {
			doubleValue = number.doubleValue();
		}
		return doubleValue;	
	}
	public static String num2Str (Number number) {
		String stringValue = "";
		if(Data.isValid(number)){
			stringValue = number.toString();
		}
		return stringValue;	
	}
	public static Number str2Num (String str) {
		Number number = 0;
		if(Data.isValid(str)){
			number = Long.valueOf(str);
		}
		return number;
	}
	/**
	 * Inicializa a "false" los boolean que entren a "null"
	 * @param inputBoolean
	 * @return
	 */
	public static Boolean initBoolean(Boolean inputBoolean) {
		Boolean outputBoolean = new Boolean(false);
		if (inputBoolean != null && inputBoolean) {
			outputBoolean = inputBoolean;
		}
		return outputBoolean;
	}

	public static String toString(Number number) {
		if(isValid(number)){
			return number.toString();
		}
		return null;
	}
}