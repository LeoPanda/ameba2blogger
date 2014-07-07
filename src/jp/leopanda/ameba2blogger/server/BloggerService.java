package jp.leopanda.ameba2blogger.server;

import java.util.Map;

import org.apache.http.HttpStatus;

import net.arnx.jsonic.JSON;
import jp.leopanda.common.server.UrlService;
import jp.leopanda.common.server.UrlService.ContentType;
import jp.leopanda.common.server.UrlService.Result;
import jp.leopanda.ameba2blogger.shared.Article;
import jp.leopanda.ameba2blogger.shared.BlogResource;
import jp.leopanda.ameba2blogger.shared.BloggerBlogInfo;

public class BloggerService {
	/**
	 * Blogger Importファイル作成用
	 * Ameba記事をリトリーブし、Blogger用に整形し
	 * バッチ投入時のセレクタ位置を添えて返す
	 */
	public String BchMakeImportArticle(Article bloggerArticle){
		BloggerImportAtom postAtom = new BloggerImportAtom();
		postAtom.setTitle(bloggerArticle.getTitle());
		postAtom.setLabel(bloggerArticle.gettag());
		postAtom.setPublished(bloggerArticle.getDate());
		postAtom.setContent(bloggerArticle.getBody());
		postAtom.setDraft(false);
		return postAtom.getPostAtom();
	}
	/**
	 *Ameba記事をBloggerへ転送する
	 *<pre>
	 * @param String articleUrl アメブロ記事のURL
	 * @param String blogID Blogger のblogID
	 * @param String token OAuth2 認証token
	 * </pre>
	 */	
	public String postArticle(Article amebaArticle,String blogID,String token){
		String urlStr = "https://www.blogger.com/feeds/"+ blogID + "/posts/default";
		BloggerImportAtom postAtom = new BloggerImportAtom();
		postAtom.setTitle(amebaArticle.getTitle());
		postAtom.setLabel(amebaArticle.gettag());
		postAtom.setPublished(amebaArticle.getDate());
		postAtom.setContent(amebaArticle.getBody());
		String postStr = postAtom.getPostAtom();
		UrlService urlService = new UrlService();
		Map<Result,String> results = 
				urlService.fetchPost(urlStr, postStr.getBytes(), 
								urlService.addToken(token,
								addBloggerApiV2Header(urlService.setHeader(ContentType.ATOM))));		
		String ret;
		if(Integer.valueOf(results.get(Result.RETCODE)) == HttpStatus.SC_CREATED){
			ret = "OK";
		}else{
			ret = results.get(Result.BODY);
		}
		return ret;
	}
	/**
	 *Blogger User情報の取得
	 *<pre>
	 * @param String token : Google認証token
	 * </pre>
	 */
	public BlogResource[] getbloggerInfo(final String token,String apiKey) {
		String urlStr = "https://www.googleapis.com/blogger/v3/users/self/blogs?key=" + apiKey;
		BlogResource[] ret = null;
		UrlService urlService = new UrlService();
		Map<Result, String> results = urlService.fetchGet(urlStr,
								urlService.addToken(token,
								urlService.setHeader(ContentType.XML)));
		if (Integer.valueOf(results.get(Result.RETCODE)) == HttpStatus.SC_OK) {
			String jsonBase = results.get(Result.BODY);
			BloggerBlogInfo bInfo = JSON.decode(jsonBase,BloggerBlogInfo.class);
			ret =  bInfo.getItems();
		}
		return ret; 
	}
	/**
	 * Blogger API V2用のHTTPヘッダ追加
	 * @param header
	 * @return
	 */
	private Map<String,String> addBloggerApiV2Header(Map<String,String> header){
		header.put("GData-Version", "2");
		return header;
	}
}
