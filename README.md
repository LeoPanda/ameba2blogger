#ameba2blogger amebaブログ移行ツール
##主な機能
*エキスポートファイルの作成支援
amebaブログの記事をインタラクティブに選択し、エキスポートファイルを作成します。
ameba記事の画像データをあらかじめエキスポートしてpicasa webアルバムへアップロードしておくと、
エキスポートファイルの該当箇所を自動で修正してくれます。


##セットアップ方法

このプログラムはGWT(google web tool kit)を使用し、GAE(google app engie)上で動作するよう
設計されています。また、実行にあたっては独自のGoogleアカウントが必要です。
プログラムを利用するためにはGWTをダウンロードし、GAE上にアカウントとプロジェクトを作成してください。

###１）jp.leopanda.amabe2blogger.client.Statics.javaの下記固定値を設定してください。

```java
	//Google client ID = デプロイ先Google　App　Engineのアプリケーション識別子
	private static final String googleClientId  		= "_________________"; 	// set your own.
```
*googleClientId

googleアカウント用OAuthクライアントID。
Google Developpers console(https://console.developers.google.com/project)へアクセスし、取得してください。
詳しくはhttps://developers.google.com/adsense/tutorials/oauth-generic?hl=jaを参照してください。

###2)jp.loepanda.ameba2blogger.server.HostGateServiceImpl.javaの下記固定値を設定してください。

```java
	private static final String API_KEY = "_____________"; //set your own.
```

 *API_EKY
Google Developpers console(https://console.developers.google.com/project)へアクセスし、
プロジェクトの認証情報から　APIキーを取得してください。
 

###3）commonServiceのライブラリを入手してください。
https://github.com/LeoPanda/gwtOauthLoginPanelへアクセスしcommonServiceのライブラリを入手し、
ビルドバスに追加するかsrcフォルダへのソースリンクを作成してください。


