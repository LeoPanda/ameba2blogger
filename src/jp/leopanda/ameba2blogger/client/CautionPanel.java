package jp.leopanda.ameba2blogger.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 注意書きパネル
 * 
 * @author LeoPanda
 *
 */
public class CautionPanel extends VerticalPanel {
	/**
	 * コンストラクタ
	 */
	public CautionPanel(){
		this.add(new HTML(MESSAGE));
	}
	/**
	 * 注意書き本文の作成
	 */
	private String MESSAGE ="<div class=\"message\">"
			+ "<h3>始める前に</h3>"
			+ "<b>1.</b>このプログラムは、あなたのgoogleアカウントを通じて"
			+ "あなたのpicasa web Albumとbloggerへアクセスしますので、"
			+ "<b><font color=\"red\">googleアカウント</font></b>が必要です。<br/>"
			+ "<b>2.</b>このプログラムは、アメーバブログ上に"
			+ "<b><font color=\"red\">「アーカイブ」ガジェット</font></b>（年月別に記事の一覧を表示する部品）が"
			+ "埋め込まれていないと<b><font color=\"red\">アメブロの記事を取り込めません。</font></b><br/>"
			+ "<b>3.</b>アメーバブログの日付表示形式は<b><font color=\"red\">yyyy-mm-dd hh:mm:ssのみ</font></b>"
			+ "をサポートしています。それ以外の表示形式では、bloggerへのimportまたは単体送信の投稿でエラーになります。"
			+ "アメーバブログの設定をあらかじめ変更しておいてください。<br/>"
			+ "<b>4.</b>アメーバブログの画像データは、あらかじめダウンロードし、"
			+ "あなたのpicasa web album へアップロードしておいてください。<br/>"
			+ "アメーバブログの<b>&lt;img&gt;</b>に記された画像ファイルと<b><font color=\"red\">同じファイル名</font></b>の画像を"
			+ "picasa web albumから探し出し自動的に<b>&lt;img&gt;</b>を書き換えます。<br/>"
			+ "サポートされるのは<b><font color=\"red\">jpegのみ</font></b>で、アメーバ固有のアイコンは放置されます。"
			+ "picasa web albumに該当するファイルが見当たらないときは、"
			+ "<b>&lt;img&gt;</b>のソースロケーションに<b><font color=\"red\">ERR:</font></b>を記入します。"
			+ "当のファイルをアップロードしてやり直すか、"
			+ "手動で<b>&lt;img&gt;</b>を書き直してください。</br>"
			+ "<b>5.</b>このプログラムのpicasa web Albumへのアクセス権限は約１時間に制限されています。"
			+ "時間を過ぎる画像ファイルを検索できなくなるので、<b>&lt;img&gt;</b>ファイルのソースロケーションはすべてERR:になります。"
			+ "ブラウザのリロードボタンを押して、作業をやり直してください。<br/>"
			+ "<b>6.</b>訂正作業のために、「単体送信」のオプションを利用することができます。</br>"
			+ "「単体送信」画面の最下部、「投稿」ボタンを押すと、"
			+ "Bloggerへ「下書き」として記事が送信されます。<br/>"
			+ "「単体送信」はBlogger側の制限で<b><font color=\"red\">１日５０件以上</font></b>の投稿はできません。"
			+ "また、<b><font color=\"red\">公開日付は自動で現在日付に設定</font></b>しなおされてしまいますので、"
			+ "手動で訂正する必要があります。<br/>"
			+ "大量の記事を送信する場合は「importファイルの作成」を利用してください。<br/><br/></div>"; 
}
