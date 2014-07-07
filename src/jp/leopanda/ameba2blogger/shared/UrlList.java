package jp.leopanda.ameba2blogger.shared;

import java.io.Serializable;

	@SuppressWarnings("serial")
	public class UrlList implements Serializable {
		
		private  String name;
		private  String url;
		
		public UrlList(){
		}
		
		public UrlList(String name,String url){
			this.name = name;
			this.url = url;
		}
		
		public String getName() {
			return name;
		}
		public  String getUrl() {
			return url;
		}

}
