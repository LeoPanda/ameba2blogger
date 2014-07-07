package jp.leopanda.ameba2blogger.server;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jp.leopanda.ameba2blogger.shared.UrlList;
/**
*アメブロ投稿年月別記事のURLリストを取得するためのクラス
*元のブログの一覧表示が複数ページにまたがるので
*ページを再帰的に読み込んでリストを作成する。
*<pre>
* @param パラメータなし
* </pre>
*/
public class UrlListGetter {
	private static UrlList[] UrlListInAllPage  = null;						//最終結果リスト
	private static ArrayList<String> nameList = new ArrayList<String>();	//再帰呼び出し用のスタック
	private static ArrayList<String> urlList = new ArrayList<String>();		//再帰呼び出し用のスタック
	
	/**
	 * 全ページ分のURLリスト
	 * @return
	 */
	public  UrlList[] getUrlListInAllPage(){
		return UrlListGetter.UrlListInAllPage;
	}
	/**
	 * 全ページ分リスト用メンバ変数へ1ページ分のURLリストを追加する
	 * @param url
	 */
	void addUrlListInOnePage(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO 例外処理
			e.printStackTrace();
		}
		//1ページ分のリスト獲得
		Elements elements = doc.getElementsByClass("title");	
		for ( Element element : elements ) {
			Element child = element.child(0);
			String retName = child.text();
			String retUrl  = child.attr("href");   
			UrlListGetter.nameList.add(retName);
			UrlListGetter.urlList.add(retUrl);
		}
		//次ベージ判定
		//TODO 次ページアンカーの検索方法に柔軟性がない
		Elements nextPageAnchors = doc.getElementsByClass("nextPage");
		if(!nextPageAnchors.isEmpty()){
			String nextPageUrl = nextPageAnchors.first().attr("href");
			addUrlListInOnePage(nextPageUrl);
		}  else {
			//最終ページで結果リストへ引き渡し
			int i = 0;
			int listSize = UrlListGetter.nameList.size();
			UrlListGetter.UrlListInAllPage = new UrlList[listSize];
			for (i=0; i < listSize ; i++){
				UrlListInAllPage[i] = new UrlList(UrlListGetter.nameList.get(i),UrlListGetter.urlList.get(i));
			}
			UrlListGetter.nameList.clear();
			UrlListGetter.urlList.clear();
			return;
		}
	}
		
}
