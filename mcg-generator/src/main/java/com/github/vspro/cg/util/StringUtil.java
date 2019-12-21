package com.github.vspro.cg.util;

public  class StringUtil {


	private StringUtil(){}

	public static boolean stringHasValue(String s){
		return s != null && s.length() > 0;
	}



	public static String combineWithSp(char separator, String ...args) {
		StringBuilder sb = new StringBuilder();
		if (args == null){
			throw new RuntimeException("参数不能为空");
		}

		boolean first = true;
		for (String str: args){
			if (stringHasValue(str)) {
				if (first){
					sb.append(str);
					first = false;
				}else {
					sb.append(separator).append(str);
				}
			}
		}
		return sb.toString();
	}

	public static boolean stringContainsSpace(String str){
		return str!=null && str.indexOf(' ') !=-1;
	}






	public static String combineWithDot(String ...args){
		return combineWithSp('.', args);
	}


	public static void main(String[] args) {
//		System.out.println(combineWithDot("hello", "world", "you"));

		String url = "jdbc:mysql://localhost:3306/oauth2?serverTimezone=UTC&amp;" +
				"characterEncoding=utf8&amp;allowMultiQueries=true";
//		System.out.println(schema(url));

		String pkg = "com.github.vspro.model";
		pkg = pkg.replaceAll("\\.", "/");
		System.out.println(pkg);
	}


}
