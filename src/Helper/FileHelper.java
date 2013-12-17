package Helper;

public class FileHelper {
	
	/**
	 * ÅÐ¶ÏÊÇÊ²Ã´±àÂë
	 * @param s
	 * @return
	 */
	public static String judgeEncode(String s) {
        //Ä¬ÈÏGBK±àÂë
        try {
            if (Character.UnicodeBlock.of(new String(s.getBytes("ISO8859_1"), "UTF-8").charAt(0)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                return "UTF-8";
            } else {
                return "GBK";
            }
        } catch(Exception e) {
            return "GBK";
        }
    }
}
