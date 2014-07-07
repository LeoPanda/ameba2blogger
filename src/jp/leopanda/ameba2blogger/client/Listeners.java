package jp.leopanda.ameba2blogger.client;

import java.util.EventListener;

import jp.leopanda.ameba2blogger.client.BlogSelectorPanel.BlogSelectorPanelEvent;
import jp.leopanda.ameba2blogger.client.InitialSettingPanel.InitialSettingPanelEvent;
import jp.leopanda.ameba2blogger.client.MainHedderPanel.MainHeadderEvent;


public class Listeners {
	//初期設定画面用イベントリスナー
	public interface InitialSettingPanelListener extends EventListener{
		//「次へ」ボタンが押された
		public void click(InitialSettingPanelEvent event);
	}
	//Bloggerブログ選択画面イベントリスナー
	public interface BlogSelectorPanelListener extends EventListener{
		//値が変更された
		public void onChanged(BlogSelectorPanelEvent event);
	}
	//メインパネル用イベントリスナー
	public interface MainPanelListener extends EventListener{
		//エラーが発生した
		public void onFailure(MainPanel.MainPanelEvent event);
	}
	//Amebaブログ投稿年月選択パネル用イベントリスナー
	public interface MainHedderPanelListener extends EventListener{
		//エラーが発生した
		public void onFailure(MainHeadderEvent event);
		//投稿年月セレクターの値が変更された
		public void onSelectChanged(MainHeadderEvent event);
	}
	public interface GoImportFileButtonListerner extends EventListener{
		//ボタンがクリックされた
		public void click();
		//サーバーへimportファイル作成がサブミットされた
		public void onSubmit(
				GoImportFileButton.GoImportFileButtonEvent event,int index);		
		//ImportFileがサーバーから返された
		public void onReturn(
				GoImportFileButton.GoImportFileButtonEvent event,int index);		
	}
}
