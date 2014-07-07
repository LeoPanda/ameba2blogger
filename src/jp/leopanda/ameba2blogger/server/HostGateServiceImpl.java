package jp.leopanda.ameba2blogger.server;

import jp.leopanda.ameba2blogger.client.HostGateService;
import jp.leopanda.ameba2blogger.shared.Article;
import jp.leopanda.ameba2blogger.shared.BlogResource;
import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HostGateServiceImpl extends RemoteServiceServlet implements
		HostGateService {
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "___________"; //set your own.
	private AmebaService amebaService = new AmebaService();
	private BloggerService bloggerService = new BloggerService();
	
	 //Amebaブログ投稿年月一覧の取得
	public UrlList[] getAmebaMonthList(String url){
		String idKey = "archives";
		return amebaService.getMonthList(url, idKey);
	}
	//Ameba投稿年月別ブログ一覧の取得
	public UrlList[] getAmebaUrlList(String url){
		return amebaService.geArticleUrlList(url);
	}
	//Amebaブログ記事本文の取得
	public Article getAmebaArticle(String url){
		return amebaService.getArticle(url);
	}
	//Blogger用に整形したアメブロ記事の取得
	public Article get2BloggerArticle(String url,String picasaUserName,String token){
		Article amebaArticle = amebaService.getArticle(url);
		return amebaService.format2Blogger(picasaUserName, token, amebaArticle);
	}
	//Blogger Importファイルの作成
	public String BchMakeImportArticle(String url,String picasaUserName,String token){
		Article bloggerArticle = get2BloggerArticle(url,picasaUserName,token);
		return bloggerService.BchMakeImportArticle(bloggerArticle);
	}
	//Ameba記事をBloggerへポストする
	public String postBloggerArticle(Article amebaArticle,String blogID,String token){
		return bloggerService.postArticle(amebaArticle, blogID, token);
	}
	//BLoggerブログ情報の取得
	public BlogResource[] getbloggerInfo(String token){
		return bloggerService.getbloggerInfo(token, API_KEY);
	}
}

