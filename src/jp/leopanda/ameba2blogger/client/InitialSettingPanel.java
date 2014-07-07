package jp.leopanda.ameba2blogger.client;

import java.util.EventObject;

import jp.leopanda.ameba2blogger.client.BlogSelectorPanel.BlogSelectorPanelEvent;
import jp.leopanda.ameba2blogger.client.Listeners.BlogSelectorPanelListener;
import jp.leopanda.ameba2blogger.shared.FieldVerifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 初期設定入力画面
 * ソースのAmebaブログとターゲットのBloggerブログを指定するため、
 * それぞれのURLを入力する画面を表示する。
 * 
 * @author LeoPanda
 *
 */
public class InitialSettingPanel extends VerticalPanel{
	//画面表示要素
	private FlexTable inputPanel		= new FlexTable(); 			//入力表示パネル
	private HorizontalPanel btnPanel 	= new HorizontalPanel();	//ボタン表示パネル
	private TextBox amebaUrlInputBox 	= new TextBox();			//amebaURL入力フィールド
	private Button  goButton 			= new Button("次へ");		//「次へ」ボタン	
	private BlogSelectorPanel blogSelectorPanel
										= new BlogSelectorPanel(); 	//Bloggerブログ選択リストボックス
	//プライベート変数
	private String amebaUrl_ = null;
	private String bloggerUrl_ = null;
	private String bloggerId_ = null;
	//getter
	public String getAmebaUrl() {return amebaUrl_;}
	public String getBloggerUrl() {return bloggerUrl_;}
	public String getBloggerId() {return bloggerId_;}
	//RPCハンドル
	HostGateServiceAsync hgc = Statics.getHgc();
	//イベントリスナー
	private Listeners.InitialSettingPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListerner(Listeners.InitialSettingPanelListener listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class InitialSettingPanelEvent extends EventObject{
		private static final long serialVersionUID = 1L;
		public InitialSettingPanelEvent(Object arg) {
			super(arg);
		}
	}
	/**
	 * コンストラクタ
	 */
	public InitialSettingPanel(){
		//入力表示エリアの構成
		Label amebaLabel = new Label("From:アメーバブログのURL");
		Label bloggerLabel = new Label("To:Bloggerブログ");
		inputPanel.setWidget(0, 0, amebaLabel);
		inputPanel.setWidget(0, 1, amebaUrlInputBox);
		inputPanel.setWidget(1, 0, bloggerLabel);
		inputPanel.setWidget(1, 1, blogSelectorPanel);
		inputPanel.setWidth("100%");
		//amebaURL入力フィールド
		amebaUrlInputBox.setSize("200px", "1.2em");
		amebaUrlInputBox.setStyleName("urlInput");
		//Blogger選択リストボックス
		blogSelectorPanel.setSize("200px", "1.8em");
		blogSelectorPanel.setStyleName("urlInput");
		blogSelectorPanel.addEventListener(new BlogSelectorPanelListener() {			
			@Override
			//ブログが選択された
			public void onChanged(BlogSelectorPanelEvent event) {
				InitialSettingPanel.this.bloggerId_ = InitialSettingPanel.this.blogSelectorPanel.getBlogId();
				InitialSettingPanel.this.goButton.setVisible(true);
			}
		});
		this.add(inputPanel);
		//ボタン表示エリアの構成
		String msg ="<div class=\"gwt-Label\">上の２つの値を入力し、"
						+ "「次へ」ボタンを押してください。　</div>";
		btnPanel.add(new HTML(msg));
		btnPanel.add(goButton);
		goButton.addStyleDependentName("sub");
		goButton.addClickHandler(new chkBtnHandler());
		goButton.setVisible(false); //ブログ選択リストボックスが準備できるまで非表示
		this.add(btnPanel);
	}
	/**
	 * アメーバURLのデフォルト値をセット
	 * @param amebaUrl
	 */
	public void setAmebaUrl(String amebaUrl){
		this.amebaUrl_ = amebaUrl;
		amebaUrlInputBox.setText(amebaUrl_);
	}
	/**
	 * 入力許可設定
	 * @param bool boolean true:入力許可 false:入力禁止
	 */
	public void setEnable(boolean bool){
		amebaUrlInputBox.setEnabled(bool);
		blogSelectorPanel.setEnabled(bool);
		btnPanel.setVisible(bool);
	}
	/**
	 *次へボタン　押下時ハンドラ
	 */	
	private class chkBtnHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			//URL入力チェック
			FieldVerifier chk = new FieldVerifier();
			String AmebaErrMsg = chk.urlInputCheck(amebaUrlInputBox.getText());
			if ( null != AmebaErrMsg ) {
				Window.alert(AmebaErrMsg);
				amebaUrlInputBox.selectAll();
			} else {
			amebaUrl_ = amebaUrlInputBox.getText();
			listener_.click(new InitialSettingPanelEvent(InitialSettingPanel.this));
			}		
		}
	}
}
