package jp.leopanda.ameba2blogger.client;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import jp.leopanda.ameba2blogger.client.GoImportFileButton.GoImportFileButtonEvent;
import jp.leopanda.ameba2blogger.client.Listeners.GoImportFileButtonListerner;
import jp.leopanda.ameba2blogger.client.MainHedderPanel.MainHeadderEvent;
import jp.leopanda.ameba2blogger.shared.UrlList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 右サイドメインパネル
 * @author LeoPanda
 *
 */
public class MainPanel extends VerticalPanel{
	//画面構成要素
	ArticleListPanel articleListPanel = new ArticleListPanel();
	MainHedderPanel mainHedderPanel = new MainHedderPanel();
	ImportFilePanel importFilePanel = new ImportFilePanel();
	private GoImportFileButton goImportFileButton = new GoImportFileButton();
	//イベントリスナー 
	public Listeners.MainPanelListener listener_;
	//イベントリスナーのセッター
	public void addEventListerner(Listeners.MainPanelListener listener){
		listener_ = listener;
	}
	//イベントオブジェクト
	public class MainPanelEvent extends EventObject{
		private static final long serialVersionUID = 1L;
		public MainPanelEvent(Object arg) {
			super(arg);
		}
	}

	/**
	 * 右サイドメインパネルの表示
	 * @param amebaUrl
	 */
	public void setPanel(String amebaUrl){
		this.clear();
		//メインヘッダの表示
		mainHedderPanel.setPanel(amebaUrl);
		mainHedderPanel.addEventListerner(new OnMainHedderSelected());			
		//メインヘッダにImportFile作成ボタンを追加
		goImportFileButton.addEventListerner(new GoImportFileButtonEvent());
		mainHedderPanel.add(goImportFileButton);
		goImportFileButton.setVisible(false); //記事一覧が表示されるまで非表示
		//パネルの割付
		this.add(mainHedderPanel);
		this.add(articleListPanel);
		this.setWidth("100%");
	}
	/*
	 * Amebaブログ投稿年月選択パネルのイベントハンドラ
	 */
	private class OnMainHedderSelected implements Listeners.MainHedderPanelListener {
		@Override
		//投稿年月セレクタが変更された
		public void onSelectChanged(MainHeadderEvent event) {
			//投稿年月別記事一覧の表示
			articleListPanel.setPanel(mainHedderPanel.getMonth_URL());
			goImportFileButton.setVisible(true);
		}
		@Override
		//投稿年月選択パネルでエラーが発生した
		public void onFailure(MainHeadderEvent event) {
			// MainPanelのエラーイベントを発生させる
			listener_.onFailure(new MainPanelEvent(this));
		}
	}
	/*
	 * 「importファイル作成」ボタン　イベントハンドラ
	 * @author LeoPanda
	 *
	 */
	private class GoImportFileButtonEvent implements
			GoImportFileButtonListerner {
		@Override
		//サブミット処理中の記事を検出
		public void onSubmit(
				jp.leopanda.ameba2blogger.client.GoImportFileButton.GoImportFileButtonEvent event,
				int index) {
			articleListPanel.setBatchStatus(index);
		}

		@Override
		//サブミット処理が完了した記事を検出
		public void onReturn(
				jp.leopanda.ameba2blogger.client.GoImportFileButton.GoImportFileButtonEvent event,
				int index) {
			articleListPanel.resetBatchStatus(index);
		}

		@Override
		//ボタンがクリックされた
		public void click() {
			//一斉セレクトボタンをリセット
			articleListPanel.setAllSelectValue(false);
			//投稿年月別記事一覧のチェックボックスで選択された記事を検索
			List<CheckBox> selector = articleListPanel.getSelector();
			List<Integer> urlListSource = new ArrayList<Integer>();
			int indexOfSelector = 0;
			for (Iterator<CheckBox> iterator = selector.iterator(); iterator.hasNext();) {
					if(iterator.next().getValue()){
						urlListSource.add(new Integer(indexOfSelector));
					}
					indexOfSelector ++;
			}
			if(indexOfSelector == 0){
				Window.alert("記事が一件も選択されていません。");
			} else{
				Integer[] submittedSeqList = (Integer[])urlListSource.toArray(new Integer[urlListSource.size()]);
				UrlList[] amebaArticleList = articleListPanel.getTargetUrlList();
				String[] urlList = new String[amebaArticleList.length];
				for (int i = 0; i < urlList.length; i++) {
					urlList[i] = amebaArticleList[i].getUrl();
				}
				//Importファイル作成をサブミット
				goImportFileButton.go(submittedSeqList, urlList);
			}
		}
	}

}
