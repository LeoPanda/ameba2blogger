package jp.leopanda.ameba2blogger.client;

import java.util.EventObject;

import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Amebaブログ投稿年月選択パネル
 * @author LeoPanda
 *
 */
public class MainHedderPanel extends HorizontalPanel{
	//画面構成要素
	private ListBox amebaMonthListBox = new ListBox();
	private InProgressPanel inProgressPanel = new InProgressPanel(); //「処理中」パネル
	//参照配列
	private UrlList[] amebaMonthList;
	//RPCハンドル
	HostGateServiceAsync hgc_ = Statics.getHgc();
	//入力値の保持エリア
	private String AMEBA_MONTH_URL = null;
	//入力値Getter
	public String getMonth_URL() {return AMEBA_MONTH_URL;}
	//イベントリスナー
	private Listeners.MainHedderPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListerner(Listeners.MainHedderPanelListener listener){listener_ = listener;}
	//投稿年月取得イベントオブジェクト
	public class MainHeadderEvent extends EventObject{
		private static final long serialVersionUID = 1L;
		public MainHeadderEvent(Object arg) {
			super(arg);
		}
	}
	/**
	 * ヘッダーパネルの表示
	 * @param amebaUrl
	 */
	public void setPanel(String amebaUrl){
		inProgressPanel.dspPanel();
		initializeListBox(amebaUrl);
		this.add(new Label("記事の投稿年月を選んでください。"));
		this.add(amebaMonthListBox);
	}
	/**
	 * リストボックスの初期化
	 * @param amebaUrl
	 */
	private void initializeListBox(String amebaUrl){
		amebaMonthListBox.clear();
		this.clear();
		amebaMonthListBox.addChangeHandler(new listBoxChaneged());
		this.add(amebaMonthListBox);
		//Amebaブログ投稿年月リストの取得
	    hgc_.getAmebaMonthList(amebaUrl,
	    	new AsyncCallback<UrlList[]>() {
		    	public void onFailure(Throwable caught) {
		    	// 取得エラー
		    		inProgressPanel.hide();
		    		Window.alert("Amebaブログが確認できないか、ブログページに月別アーカイブガジェットを見つけられませんでした。");
		    		listener_.onFailure(new MainHeadderEvent(this)); //エラーイベントを発生させる
		    	}	
		    	@Override
		    	public void onSuccess(UrlList[] result) {
		    		//リストボックスに投稿年月リストをセット
		    		  amebaMonthList = new UrlList[result.length];
		    		  for (int i = 0; i < result.length; i++){
		    			  String name = result[i].getName();
		    			  String url = result[i].getUrl();
		    			  amebaMonthList[i] = new UrlList(name,url);
		    			  amebaMonthListBox.addItem(name);
		    		  }
		    		  AMEBA_MONTH_URL = amebaMonthList[0].getUrl();
		    		  inProgressPanel.hide();
		    		  listener_.onSelectChanged(new MainHeadderEvent(MainHedderPanel.this));
		    	}
		      });    
	}
	/**
	 *リストボックス　変更ハンドラ
	 */
	public class listBoxChaneged implements ChangeHandler{
		@Override
		public void onChange(ChangeEvent event) {
			int index = amebaMonthListBox.getSelectedIndex();
			AMEBA_MONTH_URL = amebaMonthList[index].getUrl();
			listener_.onSelectChanged(new MainHeadderEvent(MainHedderPanel.this));
		}
		
	}
}
