package jp.leopanda.ameba2blogger.client;

import jp.leopanda.ameba2blogger.shared.Article;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 *Ameba記事の確認画面
 */
public class AmebloPreviewPanel extends PopupPanel{
	//画面の構成要素
	private VerticalPanel innerPanel = new VerticalPanel();
	private Button closeButon = new Button("x");
	//RPCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc();

	/**
	 * 確認画面にAmebaブログの記事を表示する
	 * @param url String Ameba記事のURL
	 */
	public void setPanel(String url) {
		this.clear();
		innerPanel.clear();
		closeButon.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				AmebloPreviewPanel.this.hide();
			}});		
	 	// アメブロ記事の取得
	      hgc_.getAmebaArticle(url,	
	    		new AsyncCallback<Article>() {
		    		@Override
			    	public void onFailure(Throwable caught) {
			    		Window.alert("アメブロ記事を取得できませんでした。");
			    		AmebloPreviewPanel.this.hide();
			    	}	
			    	@Override
			    	//ポップアップ画面の表示
			    	public void onSuccess(Article result) {
			    		String titleStr = "<h2>"+result.getTitle()+"</h2>"
			    				+ "<h3>タグ:" + result.gettag()
			    				+ ":" + result.getDate() + "</h3>";
			    		HTML title = new HTML(titleStr);
			    		HTML article = new HTML(result.getBody());
			    		innerPanel.add(closeButon);
			    		innerPanel.add(title);
			    		innerPanel.add(article);
			    		AmebloPreviewPanel.this.add(innerPanel);
			    		AmebloPreviewPanel.this.setPopupPosition(400, 50);
			    		AmebloPreviewPanel.this.show();
			    	}
	      		}
	      );
	}
}
