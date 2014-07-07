package jp.leopanda.ameba2blogger.client;

import java.util.EventObject;

import jp.leopanda.ameba2blogger.client.Listeners.GoImportFileButtonListerner;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
/**
 * 「Importファイル作成ボタン」の生成と
 * Bloggerブログ用Importファイルの作成実行を行う。
 * @author LeoPanda
 *
 */
public class GoImportFileButton extends Button{
	//ImportFile表示ポップアップ画面
	ImportFilePanel importFilePanel = new ImportFilePanel();
	//メンバ変数
	private Integer[] submittedSeqList_; //サブミット指示されたURLのシーケンス番号リスト
	private String[] urlList_;  //投稿年月別記事のURLリスト
	private String loginToken_;	//Googleログイントークン
	private String bloggerImportXML_ = "";//Importファイル
	private Timer timer_;//バッチ投入遅延用タイマー
	private int submittedSeqIndex_;//現在サブミット処理中のシーケンス番号
	//RPCハンドル
	HostGateServiceAsync hgc_ = Statics.getHgc();
	//イベントリスナー
	public GoImportFileButtonListerner listener_;
	//イベントリスナーのセッター
	public void addEventListerner(GoImportFileButtonListerner listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class GoImportFileButtonEvent extends EventObject{
		private static final long serialVersionUID = 1L;
		public GoImportFileButtonEvent(Object arg){
			super(arg);			
		}
		private int index_=0;
		public GoImportFileButtonEvent(Object arg,int seq) {
			super(arg);
			index_ = seq;
		}
		public int getIndex(){
			return index_;
		}
	}
	/**
	 * コンストラクタ
	 */
	public GoImportFileButton(){
		this.setText("Importファイルの作成");
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listener_.click();
			}
		});
	}
	/**
	 * サブミットの実行
	 * インデックスリストにセットされた記事をURLリストを参照してURLを取り出しサブミットし
	 * Import用のATOMを得る。
	 * 得られたATOMはヘッダー情報を付加してひとつのファイルにまとめる。
	 * @param submittedSeqList Integer[] サブミット依頼された記事のインデックスリスト
	 * @param urlList	String[] 一覧に表示されている記事のURLリスト
	 */
	public void go(Integer[] submittedSeqList,String[] urlList) {
		//パラメータをメンバ変数へ保存
		submittedSeqList_ = submittedSeqList;
		urlList_ = urlList;
			//バッチ投入指示された投稿記事を検索
			if(submittedSeqList.length == 0){ 
				Window.alert("記事が１件もチェックされていません。");
			} else {
				//最初の一件目をサブミット依頼
				submittedSeqIndex_ = 0; 
				bloggerImportXML_ = getImportHeader(); //Importファイルのヘッダ情報セット
				/* GAEのインスタンス持続時間には制限があるので
					時間間隔をおいて一件づつ順番にバッチ投入する
				*/
				timer_ = new Timer(){
					@Override
					public void run() {
						addImportArticle(urlList_[submittedSeqList_[submittedSeqIndex_]]);
						//イベントを発生させる
						listener_.onSubmit(new GoImportFileButtonEvent(GoImportFileButton.this), 
												submittedSeqList_[submittedSeqIndex_]);
					}
				};
				timer_.schedule(500);
				GoImportFileButton.this.setVisible(false);
		}
	}

	/**
	 * Blogger ImportFileサーバーへの作成依頼
	 * 一件分のImportファイル作成依頼をサーバーへサブミットする
	 */
	private void addImportArticle(String url){
	      String picasaUserName = Statics.getPicasaUsrName();
	      loginToken_ = Statics.getLoginToken();
	      hgc_.BchMakeImportArticle(url,picasaUserName,loginToken_,	
			new AsyncCallback<String>() {
		    	public void onFailure(Throwable caught) {
		    		Window.alert("Blogger Importファイル作成に失敗しました。");
		    	}	
		    	@Override
		    	public void onSuccess(String result) {
		    		//結果をImportファイルへ追加
	    			bloggerImportXML_ +=  result + "\n";
					//イベントの発生
	    			listener_.onReturn(new GoImportFileButtonEvent(GoImportFileButton.this),
	    								submittedSeqList_[submittedSeqIndex_]);
		    		//次の記事があればバッチ投入する
	    			submittedSeqIndex_++;
		    		if(submittedSeqIndex_<submittedSeqList_.length){
		    			timer_.schedule(1000);
		    		} else {
		    			//次の記事がなければ終結処理
		    			timer_.cancel();		
		    			bloggerImportXML_ +=  "</feed>"; //終端文字列の追加
		    			importFilePanel.setPanel(bloggerImportXML_);
		    			GoImportFileButton.this.setVisible(true);
			    	}
			    }
		      });		      
	}
	/**
	 * blogger Importファイルのヘッダー取得
	 * @return
	 */
	private String getImportHeader() {
		String xh = "<?xml version='1.0' encoding='UTF-8'?>" + "\n"
				+ "<?xml-stylesheet href=\"http://www.blogger.com/styles/atom.css\" type=\"text/css\"?>" + "\n"
				+ "<feed xmlns='http://www.w3.org/2005/Atom' xmlns:openSearch='http://a9.com/-/spec/opensearchrss/1.0/' "
				+ "xmlns:georss='http://www.georss.org/georss' xmlns:gd='http://schemas.google.com/g/2005' "
				+ "xmlns:thr='http://purl.org/syndication/thread/1.0'>"+ "\n"
				+ "<title>Ameba blog</title>"+ "\n"
				+ "<generator>Blogger</generator>"+ "\n"
				+ "<link href=\"http://www.blogger.com/\" rel=\"self\" type=\"application/atom+xml\" />" + "\n"
				+ "<link href=\"http://www.blogger.com/\" rel=\"alternate\" type=\"text/html\" />" + "\n"
				+ "<updated>2013-08-07T10:27:18Z</updated>"+ "\n";
		return xh;
	}	
}
