package jp.leopanda.ameba2blogger.client;

import jp.leopanda.ameba2blogger.shared.Article;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SinglePostPanel extends PopupPanel{
	//画面構成要素
	private TextArea articleView = new TextArea();
	private TextBox articleTitle = new TextBox();
	private TextBox articleDate = new TextBox();
	private TextBox articleTag	= new TextBox();
	private VerticalPanel innerPanel = new VerticalPanel();
	private Button postButton = new Button("投稿");
	private Button closeButton = new Button("☓");
	private InProgressPanel inProgressPanel = new InProgressPanel();
	//メンバ変数
	private Article postArticle_ = null; //ポストするAmeba記事
	//RPCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc(); 
	/**
	 *blogger単体送信確認画面作成
	 *アメーバの記事HTMLの写真URLをpicasaの写真に入れ替えて
	 *HTMLを表示する
	 *<pre>
	 * @param String url	記事のアメーバURL
	 * </pre>
	 */
	public void setPanel(final String url){
		inProgressPanel.dspPanel();//処理中表示
		this.clear();
		innerPanel.clear();
		postButton.addClickHandler(new Dopost());
		postArticle_	=null;
	 	//ポスト用に整形されたAmebaブログ記事を取得する。
		String picasaId = Statics.getPicasaUsrName();
	    String loginToken = Statics.getLoginToken();
	    hgc_.get2BloggerArticle(url, picasaId,loginToken,
	    	new AsyncCallback<Article>() {
		    	public void onFailure(Throwable caught) {
		    		Window.alert("変換後アメブロ記事の取得に失敗しました。");
		    	}	
		    	//記事の取得に成功
		    	public void onSuccess(Article result) {
		    		inProgressPanel.hide();
		    		postArticle_ = result;	//ポストする記事を設定
		    		dspPostInPanel();		//ホストする記事を表示
		    	}
	     	}
	    );	
	}
	/**
	 * ポストする内容をパネルに表示する
	 */
	private void dspPostInPanel(){
		FlexTable previewHeader = new FlexTable();
		previewHeader.setText(0, 0, "タイトル");
		previewHeader.setWidget(0, 1, articleTitle);
		previewHeader.setText(1, 0, "ラベル:");
		previewHeader.setWidget(1, 1, articleTag);
		previewHeader.setText(2, 0, "日付:");
		previewHeader.setWidget(2, 1, articleDate);
		String date = new String(postArticle_.getDate());
		articleTitle.setText(postArticle_.getTitle());
		articleTag.setText(postArticle_.gettag());
		articleDate.setText(date);
		articleView.setText(postArticle_.getBody());
		articleTitle.setSize("180px", "1.2em");
		articleTag.setSize("180px", "1.2em");
		articleDate.setSize("180px", "1.2em");
		articleView.setVisibleLines(40);
		articleView.setWidth("800px");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SinglePostPanel.this.hide();
			}
		});
		innerPanel.add(closeButton);
		innerPanel.add(previewHeader);
		innerPanel.add(articleView);
		innerPanel.add(postButton);
		innerPanel.addStyleName("message");		
		this.add(innerPanel);
		this.setPopupPosition(400, 50);
		this.show();
	}
	/**
	 *「投稿」ボタンが押された時の処理 
	 *Ameba記事のBloggerへのポストする
	 *
	 *<pre>
	 * @param String articleUrl アメーバ記事のUrl
	 * @param String token OAuth2の認証token
	 * </pre>
	 */		
	private class Dopost implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			// 投稿記事の作成
			postArticle_.setTitle(articleTitle.getText());
			postArticle_.setTag(articleTag.getText());
			postArticle_.setDate(articleDate.getText());
			postArticle_.setBody(articleView.getText());
			String blogID = Statics.getBloggerID();
			String token = Statics.getLoginToken();
			//記事をBloggerへ投稿する
			hgc_.postBloggerArticle(postArticle_,blogID,token,
				new AsyncCallback<String>() {
			    	public void onFailure(Throwable caught) {
			    		Window.alert("Blogger記事の投稿に失敗しました。");
			    	}	
			    	@Override
			    	public void onSuccess(String result) {
			    		String postMessage = result;
						innerPanel.clear();
						if(postMessage.equals("OK")){
							postMessage = "Bloggerへ下書きとして投稿しました。";};
						HTML msg = new HTML(postMessage);
						innerPanel.add(closeButton);
						innerPanel.add(msg);
			    		  }
			});	
			postArticle_ = null;
		}
	}
}
