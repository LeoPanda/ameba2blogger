package jp.leopanda.ameba2blogger.shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
	
	/**
	 *URL入力チェック 
	 *<pre>
	 * @param String url	チェック対象のURL
	 * @return String 		エラーメッセージ
	 * </pre>
	 */
	public String urlInputCheck(String url){
		String returnMsg = null;
		String chk_URL = "^(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)$";
		
		if ( url.length() > 0) {
			if ( ! url.matches(chk_URL)) { returnMsg = "URLの構文が間違っているか、使えない文字が含まれています。"; }
		} else {
			returnMsg = "URLを入力してください。"; }
				
		return returnMsg;

		}

	/**
	 *IDなどの名前チェック
	 *<pre>
	 * @param String str	チェック対象の文字列
	 * @return String 		エラーメッセージ
	 * </pre>
	 */
	public String nameInputCheck(String str){
		String returnMsg = null;
		String chk_numAlpha = "^[a-zA-Z0-9._@]+$";
		
		if ( str.length() > 0) {
			if ( ! str.matches(chk_numAlpha)) { returnMsg = "使用出来ない文字列が含まれています。"; }
		} else {
			returnMsg = "入力してください。"; }
				
		return returnMsg;
		
		}

}
