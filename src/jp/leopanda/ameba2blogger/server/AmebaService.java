package jp.leopanda.ameba2blogger.server;

import java.io.IOException;

import jp.leopanda.ameba2blogger.shared.Article;
import jp.leopanda.ameba2blogger.shared.UrlList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmebaService {
	/**
	 *アメブロ投稿年月一覧の取得
	 *	HTTPページに直接アクセスし、Jsoupでパース。
	 *　タイトルとURLを抜き出してリストクラスへ格納
	 *<pre>
	 * @param String url : アメブロのアーカイブwidgetを表示しているページのURL
	 * @param String idKey :アーカイブウィジェットの開始 divタグにつけられたIDの属性値
	 * @return ListUrl[] アーカイブ年月とアーカイブ記事URLのリスト
	 * </pre>
	 */
	public UrlList[] getMonthList(String url,String idKey){
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.timeout(20000)
					.get();
		} catch (IOException e) {
			// TODO 例外処理
			e.printStackTrace();
		}
		Elements elements = doc.getElementById(idKey).getElementsByTag("li");
		int numOfElements = elements.size();
		UrlList[] resultList = new UrlList[numOfElements];
		
		int i = 0;
		String retName = null;
		String retUrl = null;
				for ( Element element : elements ) {
					Element child = element.child(0);
					retName = child.text();
					retUrl  = child.attr("href");   
					resultList[i] = new UrlList(retName,retUrl);
					i++;
				}

		return resultList;
		
	}
	/**
	 *アメブロ記事投稿年月別一覧の取得
	 *	アーカイブ一覧から取得したURL（年月別の記事の羅列）へアクセスし、Jsoupでパース。
	 *　記事のタイトルとURLを抜き出してリストクラスへ格納。
	 *　記事は複数ページにまたがるので、
	 *　次ページを再帰的に呼び出して同年月の記事のタイトルとURLをすべて抜き出す。
	 *<pre>
	 * @param String url : アメブロのURL
	 * @return ListUrl[] アーカイブ年月とアーカイブ記事URLのリスト
	 * </pre>
	 */
	public UrlList[] geArticleUrlList(String url){
		UrlListGetter urlListGetter = new UrlListGetter();
		urlListGetter.addUrlListInOnePage(url);
		return urlListGetter.getUrlListInAllPage();	
	}
	/**
	 *アメブロ記事本文の取得
	 *<pre>
	 * @param String url : アメブロ記事ページのURL
	 * </pre>
	 */
	public Article getArticle(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.timeout(20000)
					.get();
		} catch (IOException e) {
			// TODO 例外処理
			e.printStackTrace();
		}		
		Element titleElement = doc.getElementsByClass("title").first();
		Element tagElement = doc.getElementsByClass("theme").first();
		Element dateElement = doc.getElementsByClass("date").first();
		Element bodyElement = doc.getElementsByClass("subContents").first();
		//本文下の不要部カット
		if (null != bodyElement.getElementsByClass("gpt-frame")) {bodyElement.getElementsByClass("gpt-frame").remove();}
		if (null != bodyElement.getElementsByClass("subAdBannerHeader")) {bodyElement.getElementsByClass("subAdBannerHeader").remove();}		
		if (null != bodyElement.getElementById("themeImageBox"))  {bodyElement.getElementById("themeImageBox").remove();};
		if (null != bodyElement.getElementById("subAdBannerHeader")){bodyElement.getElementById("subAdBannerHeader").remove();}
		if (null != bodyElement.getElementById("exLinkBtn")){bodyElement.getElementById("exLinkBtn").remove();}
		if (null != bodyElement.getElementById("iineBtnWrap")){bodyElement.getElementById("iineBtnWrap").remove();}
		if (null != bodyElement.getElementsByClass("articleBtnArea")){bodyElement.getElementsByClass("articleBtnArea").remove();}
		if (null != bodyElement.getElementsByClass("articleLinkArea")){bodyElement.getElementsByClass("articleLinkArea").remove();}
		if (null != bodyElement.getElementsByClass("afc-frame")){bodyElement.getElementsByClass("afc-frame").remove();}
		
		String date,title,tag,articleBody = null;
		if( null != dateElement ){ 	date = dateElement.text();
			} else {				date = "ERR:日付取得失敗";	}
		if( null != titleElement.child(0) ){title = titleElement.child(0).text();
			} else {						title = "ERR:タイトル取得失敗"; }
		if( null != tagElement.child(0) ) {	tag = tagElement.child(0).text();
			} else {						tag = "ERR:タグ取得失敗";	}		
		if( null != bodyElement.child(0) ) {articleBody = bodyElement.child(0).toString();
			} else { 						articleBody = "ERR:本文取得失敗";	}

		return new Article(title,date,tag,articleBody);		
	}
	/**
	 * Ameba記事をBlogger用に整形する。
	 * @param picasaUserName	String Picasaユーザー名
	 * @param token				String Google認証token
	 * @param amebaArticle		Article Ameba記事
	 * @return
	 */
	public Article format2Blogger(String picasaUserName, String token,
			Article amebaArticle) {
		Document doc = Jsoup.parse(amebaArticle.getBody(), "");
		PicasaService picasaService = new PicasaService();
		//写真のanchor検索
		Elements anchors = doc.getElementsByTag("a");
		for(Element anchor:anchors){
			Element img = anchor.getElementsByTag("img").first();
			if( null != img){
				//写真anchorのリンクとimgタグのソースをpicasaに変更
				String imgFullName = img.attr("src");
				String imgName = imgFullName.substring(imgFullName.lastIndexOf("/")+1);
				String changeImgName = picasaService.getPhotoUrl(imgName,picasaUserName,token);
				img.attr("src",changeImgName);
				anchor.attr("href",changeImgName);
				//imgタグのstyleを削除し、幅をセット（bloggerの幅自動調整を利用するため）
				String styleAtr = img.attr("style");
				if( styleAtr != null && !styleAtr.isEmpty() ){
					if(styleAtr.indexOf("width:") > 0){
						String sWidth = styleAtr.split("width:")[1];
						String width = sWidth.split("px")[0].trim();
						img.attr("style","");
						img.attr("ratio","");
						img.attr("width",width);}
				}
			}
		}
		//XMLの整合
		String docBody = doc.toString();
		//XML not supported &nbsp;(半角スペース）
		if(docBody.indexOf("&nbsp;")>0){
			docBody = docBody.replaceAll("&nbsp;", "");}
		//<HTML>〜＜BODY>を除外
		if(docBody.indexOf("<body>") > 0){ 
			docBody = docBody.split("<body>")[1].split("</body>")[0];}
		if(docBody.matches(".+<!--.+")){
			docBody = docBody.split("-->")[1].split("<!-")[0].trim();}
		String retBody = docBody;
		amebaArticle.setBody(retBody);
		return amebaArticle;
	}

	
	
	
	
}
