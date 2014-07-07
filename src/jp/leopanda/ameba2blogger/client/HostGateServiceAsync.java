package jp.leopanda.ameba2blogger.client;

import jp.leopanda.ameba2blogger.shared.Article;
import jp.leopanda.ameba2blogger.shared.BlogResource;
import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HostGateServiceAsync {

	void getAmebaMonthList(String url, AsyncCallback<UrlList[]> callback);
	void getAmebaUrlList(String url, AsyncCallback<UrlList[]> callback);	
	void getAmebaArticle(String url, AsyncCallback<Article> callback);	
	void get2BloggerArticle(String url,String picasaUserName,String token, AsyncCallback<Article> callback);	
	void getbloggerInfo(String token,AsyncCallback<BlogResource[]> callback);
	void postBloggerArticle(Article amebaArticle,String blogID,String token, AsyncCallback<String> callback);
	void BchMakeImportArticle(String url,String picasaUserName,String token,AsyncCallback<String> callback);

}
