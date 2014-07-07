package jp.leopanda.ameba2blogger.client;

import com.google.gwt.core.client.GWT;

/**
 *静的共通値保存用クラス
 *<pre>
 * @param パラメータなし
 * </pre>
 */
public class Statics {
	private static final String googleClientId  = "_________";  //set your own.
	private static String loginToken; 		//Google認証Token
	private static String amebaUrl; 		//アメブロのURL
	private static String picasaUsrName;	//Picasaアルバムのユーザー名
	private static String googleInnerID;	//Google innerID
	private static String picasaAlbumUrl;	//アメブロから別手段で移された写真を格納するpicasaアルバムのURL
	private static String bloggerID;		//BloggerのblogID
	private static HostGateServiceAsync hgc = GWT
					.create(HostGateService.class);	//RPC Service Access class
	
	public static String getClientId(){
		return Statics.googleClientId;
	}
	public static HostGateServiceAsync getHgc(){
		return hgc;
	}
	public static String getLoginToken() {
		return loginToken;
	}
	public static void setLoginToken(String loginToken) {
		Statics.loginToken = loginToken;
	}
	public static String getAmebaUrl() {
		return amebaUrl;
	}
	public static void setAmebaUrl(final String amebaUrl) {
		Statics.amebaUrl = amebaUrl;
	}
	public static String getPicasaUsrName() {
		return picasaUsrName;
	}
	public static void setPicasaUsrName(final String picasaUsrID) {
		Statics.picasaUsrName = picasaUsrID;
	}
	public static String getPicasaAlbumUrl() {
		return picasaAlbumUrl;
	}
	public static void setPicasaAlbumUrl(final String picasaAlbumUrl) {
		Statics.picasaAlbumUrl = picasaAlbumUrl;
	}
	public static String getGoogleInnerID() {
		return googleInnerID;
	}
	public static void setGoogleInnerID(final String googleUserID) {
		Statics.googleInnerID = googleUserID;
	}
	public static String getBloggerID() {
		return bloggerID;
	}
	public static void setBloggerID(final String bloggerID) {
		Statics.bloggerID = bloggerID;
	}
}
