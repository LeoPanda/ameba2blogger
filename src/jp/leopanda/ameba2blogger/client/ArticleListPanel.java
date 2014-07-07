package jp.leopanda.ameba2blogger.client;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
/**
 * Amebaブログ記事の一覧画面
 * 投稿年月セレクタで選択された月の記事一覧を表示する。
 * 明細に表示された記事に対して
 *	・元のAmebaブログのプレビュー確認
 *	・記事のbloggerへの単体送信画面の表示
 *	・チェックボックスでチェックされた記事をまとめてImportファイルを作成する
 * のアクションを行う。
 * @author LeoPanda
 *
 */
public class ArticleListPanel extends FlexTable{
	//画面構成要素
	private CheckBox allSelect = new CheckBox();						//一斉選択チェックボックス
	private List<Label> batchStatus = new ArrayList<Label>();			//バッチ実行のステータス表示
	//ポップアップ画面
	private AmebloPreviewPanel amebloPreviewPanel = new AmebloPreviewPanel();	//元記事確認パネル
	private SinglePostPanel singlePostPanel = new SinglePostPanel();			//単体送信パネル
	private InProgressPanel inProgressPanel = new InProgressPanel(); 			//「処理中」パネル
	//RPCハンドル
	private HostGateServiceAsync hgc_ = Statics.getHgc();
	//メンバ変数
	private UrlList[] amebaArticleList_;								//チェックされた記事のURLリスト	
	private List<CheckBox> selector_ = new ArrayList<CheckBox>();		//記事選択チェックボックスのリスト
	//メンバ変数のgetter
	public UrlList[] getTargetUrlList(){
		return amebaArticleList_;
	}
	public List<CheckBox> getSelector(){
		return selector_;
	}
	//外部からのパネル制御ハンドル
	/**一斉選択チェックボックスのプリセット*/
	public void setAllSelectValue(boolean set){
		allSelect.setValue(set);
	}
	/**バッチステータスのセット*/
	public void setBatchStatus(int i){
		batchStatus.get(i).setText("処理中");
		batchStatus.get(i).setStyleName("onGoing");
	}
	/**バッチステータスのリセット*/
	public void resetBatchStatus(int i){
		selector_.get(i).setValue(false);
		batchStatus.get(i).setText("");
		batchStatus.get(i).setStyleName("normalEnd");
	}
	/**
	 *記事一覧テーブル用メインパネル作成
	 *<pre>
	 * @param パラメータなし
	 * </pre>
	 */
	public void setPanel(String amebaUrl) {
		inProgressPanel.dspPanel();	//「処理中」表示
		//テーブルの初期設定
		this.clear();
		selector_.clear();
		batchStatus.clear();
		this.addStyleName("articleTable");
		makeHedder(); //テーブルのヘッダ作成
		if(null == amebaUrl) return;
	 	//アメブロ投稿年月別記事一覧を取得し明細行を作成する
		hgc_.getAmebaUrlList(amebaUrl,
			new AsyncCallback<UrlList[]>() {
		    	public void onFailure(Throwable caught) {
		    		Window.alert("アメブロ記事一覧の取得に失敗しました。");
		    		inProgressPanel.hide();
		    	}	
		    	@Override
		    	public void onSuccess(UrlList[] result) {
		    		ArticleListPanel.this.amebaArticleList_ =  
		    							new UrlList[result.length];
		    		for (int i = 0; i < result.length; i++){
		    			  String name = result[i].getName();
		    			  String url = result[i].getUrl();
		    			  amebaArticleList_[i] = new UrlList(name,url);
		    			  makeArticleTableColumn(i,name,url); //明細行の作成
		    		  }
		    		 inProgressPanel.hide();
		    	}
	      });
	}
	/**
	 * 記事一覧テーブルのヘッダー作成
	 */
	@SuppressWarnings("unchecked")
	private void makeHedder() {
		allSelect.addValueChangeHandler(new allSelectHandler());
		this.setWidget(0, 0,allSelect);
		this.setText(0, 1, "ステータス");		
		this.setText(0, 2, "タイトル");
		this.setText(0, 3, "単体送信");
		//スタイル名の設定
		this.getRowFormatter().addStyleName(0, "this-head");
		this.getCellFormatter().addStyleName(0, 0, "this-selector");
		this.getCellFormatter().addStyleName(0, 1, "this-status");
		this.getCellFormatter().addStyleName(0, 2, "this-title");
		this.getCellFormatter().addStyleName(0, 3, "articleTable-selector");
	}	 
	 /**
	 *記事一覧テーブルの明細作成
	 *<pre>
	 * @param int i			テーブルの行
	 * @param String name	記事タイトル
	 * @param String url	記事URL
	 * </pre>
	 */
	private void makeArticleTableColumn(int i, final String name,final String url) {
		//1カラム目　記事選択チェックボックス
		CheckBox cb = new CheckBox();
		selector_.add(i,cb);
		this.setWidget(i+1, 0, selector_.get(i));
		//２カラム目　投稿ステータス
		Label st = new Label();
		batchStatus.add(i,st);
		this.setWidget(i+1, 1, batchStatus.get(i));
		//3カラム目　ブログタイトル  
		Anchor previewAnchor = new Anchor(name);
		this.setWidget(i+1, 2, previewAnchor);
		previewAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				amebloPreviewPanel.setPanel(url);
			}
		});	
		//4カラム目 単体送信ボタン
		Button previewButton = new Button(" ");
		previewButton.addStyleDependentName("mini");
		previewButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				singlePostPanel.setPanel(url);
			}
		});
		this.setWidget(i+1, 3, previewButton);	
		//スタイル名の設定
		this.getCellFormatter().addStyleName(i+1, 0, "this-selector");
		this.getCellFormatter().addStyleName(i+1, 2, "this-title");
		this.getCellFormatter().addStyleName(i+1, 3, "articleTable-selector");
	}
	/**
	 * 「一斉選択」チェックボックス　イベントハンドラ
	 * テーブルに埋め込まれたチェックボックスを一斉にON,OFFする。
	 * @author LeoPanda
	 */
	@SuppressWarnings("rawtypes")
	private class allSelectHandler implements ValueChangeHandler{
		@Override
		public void onValueChange(ValueChangeEvent event) {
			if(allSelect.getValue()){
				for(int i=0;i<selector_.size();i++){
					selector_.get(i).setValue(true);}
			} else {
					for(int i=0;i<selector_.size();i++){
						selector_.get(i).setValue(false);}
				}
			}
		}


}
