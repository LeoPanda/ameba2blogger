package jp.leopanda.ameba2blogger.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * blogger Importファイルのダウンロード画面
 * ダウンロード用の画面をポップアップし、
 * 右クリックでファイルをダウンロードできるよう中身の文字列をURLタグにリンクする。
 * @author LeoPanda
 *
 */
public class ImportFilePanel extends PopupPanel{
	//画面構成要素
	private VerticalPanel innerPanel = new VerticalPanel(); 
	private Button closeButton = new Button("x");
	/**
	 * ダウンロード画面をポップアップする
	 * @param importFile importファイルの中身
	 */
	public void setPanel(String importFile){
		this.clear();
		Anchor dlAnchor = new Anchor();
		dlAnchor.setHref(getDownLoadUrl(importFile));
		dlAnchor.setText("ここを右クリックからダウンロード");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ImportFilePanel.this.hide();
			}
		});
		innerPanel.clear();
		innerPanel.add(closeButton);
		innerPanel.add(new HTML(getMessage()));
		innerPanel.add(dlAnchor);
		innerPanel.add(new HTML("<br/><br>"));
		innerPanel.addStyleName("message");
		this.add(innerPanel);
		this.setPopupPosition(300, 50);
		this.show();						
	}
	/**
	 * ウィンドウに表示される指示メッセージ
	 * @return
	 */
	private String getMessage() {
		String msg = 	"<h3>Blogger用importファイルが生成されました。</h3>"
					 + 	"<div class=\"message\">"
					 + 	"下のリンクにカーソルを合わせ、右クリックから「リンク先を別名で保存」を選び、<br/>"
					 + 	"ファイル名をつけなおしてダウンロードしてください。<br/>"
					 + 	"このとき、拡張子を.xmlとしておくとインポート作業をスムースに行えます。<br><br></div>";
		return msg;
	}
	/**
	 * ダウンロードリンクの作成
	 * @param str
	 * @return
	 */
	private native String  getDownLoadUrl(String str)/*-{
	  var blob = new Blob([ str ], { "type" : "text/xml" });
	  $wnd.URL = window.URL || $wnd.webkitURL;
	  return $wnd.URL.createObjectURL(blob);
	}-*/;
}
