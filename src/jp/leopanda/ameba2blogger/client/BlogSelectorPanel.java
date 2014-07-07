package jp.leopanda.ameba2blogger.client;

import java.util.EventObject;

import jp.leopanda.ameba2blogger.client.Listeners.BlogSelectorPanelListener;
import jp.leopanda.ameba2blogger.shared.BlogResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

/**
 * ログインされたGoogleユーザーがオーナーのBloggerブログを検索し
 * セレクタに表示するためのクラス。
 * intialSettingPanel()クラスの部品
 * 
 * @author LeoPanda
 *
 */
public class BlogSelectorPanel extends ListBox{
	//メンバ変数
	private String blogId_;						//BloggerブログID
	private BlogResource[] blogResource_;		//Bloggerブログ記事情報
	//メンバ変数getter
	public String getBlogId() {
		return blogId_;
	}
	//google認証トークン
	private String loginToken_;
	//PRCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc();
	//イベントリスナー
	private BlogSelectorPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListener(BlogSelectorPanelListener listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class BlogSelectorPanelEvent extends EventObject{
		private static final long serialVersionUID = 1L;
		public BlogSelectorPanelEvent(Object arg) {
			super(arg);
		}
	}
	/**
	 * コンストラクタ
	 */
	public BlogSelectorPanel(){
		//BloggerAPIへアクセスし、ユーザーが保有するBlogの一覧を取得する
		loginToken_ = Statics.getLoginToken();
		hgc_.getbloggerInfo(loginToken_,
			new AsyncCallback<BlogResource[]>() {
				@Override
				//RPC接続失敗
				public void onFailure(Throwable caught) {
					GWT.log("getbloggerInfo -> onFailure");
					Window.alert("bloggerブログ検索中にエラーが発生しました。");
				}
				@Override
				//RPC接続成功
				public void onSuccess(BlogResource[] result) {
					if(result != null){
						blogResource_ = result;
						blogId_ = result[0].getId();
						intializeListBox(result);	//リストボックスの初期化
						listener_.onChanged(new BlogSelectorPanelEvent(BlogSelectorPanel.this));	//通知イベントの発生
					}else{
						Window.alert("bloggerブログが見つかりませんでした。");
					}
				}
			});	
	}
	/**
	 * Bloggerブログ選択リストボックスの初期化
	 * ブログの一覧をリストボックスへセットする。
	 */
	private void intializeListBox(BlogResource[] blogResource) {
		//セレクタ
		if(blogResource != null){
			for (int i = 0; i < blogResource.length; i++) {
				this.addItem(blogResource[i].getName());}
		}
		if(this != null){
			this.addChangeHandler(new onSelected());
		}
	}
	/**
	 * ブログの選択時ハンドラ
	 * BloggerのブログIDをメンバ変数へストアし、イベントを発生させる
	 * @author LeoPanda
	 *
	 */
	private class onSelected implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			blogId_ = blogResource_[BlogSelectorPanel.this.getSelectedIndex()].getId();
			listener_.onChanged(new BlogSelectorPanelEvent(BlogSelectorPanel.this)); //通知イベントの発生
		}	
	}

}
