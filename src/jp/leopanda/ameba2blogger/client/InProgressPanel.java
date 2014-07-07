package jp.leopanda.ameba2blogger.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * 「処理中」ポップアップ画面の表示
 * @author LeoPanda
 *
 */
public class InProgressPanel extends PopupPanel{
	public void dspPanel(){
		this.clear();
		this.add(new HTML("<div class='onGoing'><h2>データ取得中....</h2></div>"));
		this.setPopupPosition(650, 150);
		this.show();
	}
}
