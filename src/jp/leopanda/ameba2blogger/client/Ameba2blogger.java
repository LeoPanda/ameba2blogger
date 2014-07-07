package jp.leopanda.ameba2blogger.client;


import jp.leopanda.ameba2blogger.client.InitialSettingPanel.InitialSettingPanelEvent;
import jp.leopanda.ameba2blogger.client.Listeners.InitialSettingPanelListener;
import jp.leopanda.ameba2blogger.client.Listeners.MainPanelListener;
import jp.leopanda.ameba2blogger.client.MainPanel.MainPanelEvent;
import jp.leopanda.common.client.GoogleLoginBar;
import jp.leopanda.common.client.GoogleLoginBar.InfoEvent;
import jp.leopanda.common.client.GoogleLoginBar.ScopeName;
import jp.leopanda.common.client.LoginBarListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * ameba2blogger
 * AmebaブログからBloggerブログへの記事の移行を援助する。
 * Amebaブログの投稿年月別一覧を表示し、
 *・元記事の確認
 *・一件ごとのbloggerへの記事転送
 *・チェックボタンで選択された記事のBlogger用Importファイルの作成
 *を行う。
 */
public class Ameba2blogger implements EntryPoint {
	//画面要素
	private HorizontalPanel outerPanel = new HorizontalPanel(); //画面の大外枠
	private GoogleLoginBar loginBar;							//Googleログインバー
	private CautionPanel initialCausion = new CautionPanel();	//注意書き（ログイン前）
	private InitialSettingPanel initialSettingPanel;			//初期設定入力パネル
	private MainPanel mainPanel = new MainPanel();				//右側メインパネル
	//Administrators constant
	private final String ADMINISTRATOR 		= "";
	private final String MY_AMEBA_URL 		= "";
	/**
	 *エントリーポイント　画面ロード時実行
	 */
	public void onModuleLoad(){
		// Googleログインバーの表示
		loginBar = new GoogleLoginBar(Statics.getClientId(),
									GoogleLoginBar.addScope(ScopeName.PICASA)
									+GoogleLoginBar.addScope(ScopeName.BLOGGER)
									+GoogleLoginBar.addScope(ScopeName.BLOGGERV2));
		loginBar.addListerner(new LoginControl());
		RootPanel.get("loginBarContainer").add(loginBar);
		//アプリケーションエリアの設定（画面の外枠）
		outerPanel.setVisible(false);	//ログインするまで非表示
		RootPanel.get("outerPanel").add(outerPanel);
		//注意書きパネルをアプリケーションエリアの外側へ設定
		RootPanel.get("introPanel").add(initialCausion);
	}
	
	/**
	 * Googleログインバー　イベントハンドラ
	 * ログオン・ログオフのイベントを拾う
	 * @author LeoPanda
	 *
	 */
	private class LoginControl implements LoginBarListener {
		@Override
		//ログオフされた
		public void onLoggedOff(InfoEvent event) {
			GWT.log("Google Logoffed.");
			initialCausion.setVisible(true);
			outerPanel.setVisible(false);
		}
		@Override
		//ログインされた
		public void onLoggedIn(InfoEvent event) {
			GWT.log("Google Logined.");
			//googleログイン情報をスタティック変数へ
			Statics.setLoginToken(loginBar.getToken());
			Statics.setPicasaUsrName(loginBar.getGmailAdress().split("@")[0]);
			Statics.setGoogleInnerID(loginBar.getGoogleInnerId());
			//初期設定入力画面の表示	
			initialSettingPanel = new InitialSettingPanel();
			if(Statics.getPicasaUsrName().equals(ADMINISTRATOR)){
				initialSettingPanel.setAmebaUrl(MY_AMEBA_URL);
			}
			initialSettingPanel.addEventListerner(new OnGoButton());
			outerPanel.add(initialSettingPanel);	
			initialCausion.setVisible(false);
			outerPanel.setVisible(true);
		}
	}
	/**
	 * 初期設定入力画面イベントハンドラ
	 * 「次へ」ボタンの押下を拾って次画面を表示する
	 */
	private class OnGoButton implements InitialSettingPanelListener {
		@Override
		//「次へ」ボタンが押された
		public void click(InitialSettingPanelEvent event) {
			//選択された値をスタティック変数への代入
			Statics.setAmebaUrl(initialSettingPanel.getAmebaUrl());
			Statics.setBloggerID(initialSettingPanel.getBloggerId());
			//メインパネルの表示
			initialSettingPanel.setEnable(false);
			mainPanel.setPanel(initialSettingPanel.getAmebaUrl());
			mainPanel.setVisible(true);
			mainPanel.addEventListerner(new MainPanelListener() {		
				@Override
				public void onFailure(MainPanelEvent event) {
					// メインパネルでエラーが発生した場合の処理
					mainPanel.setVisible(false);
					initialSettingPanel.setEnable(true);
				}
			});
			//メインパネルの割付
			outerPanel.add(mainPanel);
			outerPanel.setWidth("90%");
			outerPanel.setCellWidth(mainPanel, "65%");
		}	
	}
}
	
	
