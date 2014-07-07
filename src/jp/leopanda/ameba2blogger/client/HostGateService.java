package jp.leopanda.ameba2blogger.client;

import jp.leopanda.ameba2blogger.shared.Article;
import jp.leopanda.ameba2blogger.shared.BlogResource;
import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("HostGateService")
public interface HostGateService  extends RemoteService {	
	UrlList[] getAmebaMonthList(String url);
	UrlList[] getAmebaUrlList(String url);
	Article getAmebaArticle(String url);
	Article get2BloggerArticle(String url,String picasaUserName,String token);
	BlogResource[] getbloggerInfo(String token);	
	String postBloggerArticle(Article amebaArticle,String blogID,String token);	
	String BchMakeImportArticle(String url,String picasaUserName,String token);
	}
