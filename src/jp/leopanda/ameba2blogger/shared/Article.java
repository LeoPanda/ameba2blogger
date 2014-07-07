package jp.leopanda.ameba2blogger.shared;

import java.io.Serializable;
/**
*アメブロ記事を保持するためのコンストラクタ
*<pre>
* @param パラメータなし
* </pre>
*/
@SuppressWarnings("serial")
public class Article implements Serializable{
	private String title = null;
	private String date = null;
	private String tag = null;
	private String body = null;

	public Article(){
	}
	
	public Article(String title,String date,String tag,String body){
		this.title = title;
		this.date = date;
		this.tag = tag;
		this.body = body;
	}
	
	public void setTitle(final String title){
		this.title = title;
	}
	public void setDate(final String date){
		this.date = date;
	}
	public void setTag(final String tag){
		this.tag = tag;
	}
	public void setBody(final String body){
		this.body = body;
	}

	public String getTitle(){
		return this.title;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String gettag(){
		return this.tag;
	}
	
	public String getBody(){
		return this.body;
	}
}

