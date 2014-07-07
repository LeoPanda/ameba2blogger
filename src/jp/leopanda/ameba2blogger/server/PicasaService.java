package jp.leopanda.ameba2blogger.server;

import java.util.Map;

import jp.leopanda.common.server.UrlService.ContentType;
import jp.leopanda.common.server.UrlService.Result;
import jp.leopanda.common.server.UrlService;

import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class PicasaService {
	/**
	 *picasa から写真のURLを取得
	 *<pre>
	 *@param userName picasa User Name
	 *@param 写真のファイル名
	 *@param token OAuth2 google token
	 *@param 戻り値　Picasa上の写真のURL
	 *</pre>
	 */
	public String getPhotoUrl(String imgName,String userName,String token){
		String retUrl = null;		
		if(token == null){
			retUrl = "ERR:NotSignIn";
		}else{
			Document doc = null;
			String baseXml = null;
			String urlStr = "https://picasaweb.google.com/data/feed/api/user/" + userName 
					+"?kind=photo&q=" + imgName + "&access_token=" + token;
			UrlService urlService = new UrlService();
			Map<Result, String> results = urlService.fetchGet(urlStr,
							urlService.addToken(token,
									urlService.setHeader(ContentType.XML)));
			if(Integer.valueOf(results.get(Result.RETCODE)) != HttpStatus.SC_OK){
				retUrl = "ERR:ApiAccessDenied";
			}else{
				baseXml = results.get(Result.BODY);
				doc = Jsoup.parse(baseXml,"",Parser.xmlParser());
				Elements entrys = doc.getElementsByTag("entry");
				if (null == entrys | entrys.isEmpty()) {
					retUrl = "ERR:NoImg";
				} else {
					Element entry = entrys.first();
					Elements elements = entry.getElementsByTag("content");
					if (null == elements | elements.isEmpty() ) { retUrl = "ERR:NoImg";
					} else {	Element element = elements.first();
								retUrl = element.attr("src");
					}
				}
			}
		}
		return retUrl;		
	}
}
