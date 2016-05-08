package com.innovation.app.net.tools;

public class StringsUtils {
	/**
	 * 去除字符串的前后括号
	 * @param str
	 * @return
	 */
	public static String removeParentheses(String str){
		int temp = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i)=='(') {
				temp = i;
				break;
			}
		}
		try {
			str = str.substring(temp+1, (str.lastIndexOf(')')==-1)?str.length():str.lastIndexOf(')'));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 去除字符串的前后的指定的字符
	 * @param str
	 * @param firstRe 指定的字符串
	 * @return
	 */
	public static String removeParentheses(String str,char firstRe,char lastRe){
		int temp = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i)==firstRe) {
				temp = i;
				break;
			}
		}
		try {
			int start = temp+1;
			int last = (str.lastIndexOf(lastRe)==-1)?str.length():str.lastIndexOf(lastRe);
			if (last<=start&&start!=0) {
				str = str.substring(start,str.length());
			}else{
				str = str.substring(start,last);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 判断两个值的大小
	 * @param value1
	 * @param value2
	 * @return 1:value1>value2,-1:value<value2,0:相等,-2:格式化错误
	 */
	public static int compareTime(String value1,String value2) {
		// TODO Auto-generated method stub
		try {
			Long long1 = Long.parseLong(value1);
			Long long2 = Long.parseLong(value2);
			if (long1>long2) {
				return 1;
			}else if (long1<long2) {
				return -1;
			}else {
				return 0;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}
	}
	/**
	 * 手机号码加密
	 * @param phone
	 * @return eg:123****1234
	 */
	public static String phone2Encode(String phone){
		String temp = phone.replaceAll(
				"(\\d{3})(\\d{4})(\\d{4})", "$1****$3");
		return temp;
	}
}
