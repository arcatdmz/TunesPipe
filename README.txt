﻿TunesPipe
Copyright (C) 2011 Jun KATO

version 1.0.1
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

TunesPipe(以下、本ソフトウェア)は、SSHポートフォワーディングを
用いて、リモートで実行されているDAAPサービスをローカルホストの
任意のポートに転送します。

DAAPクライアントでリモートサーバの音楽を聴くためには、以下の条
件が満たされた状態で本ソフトウェアを起動する必要があります。

・リモートサーバでDAAPサービス(iTunesなど)が動作していること

・リモートサーバが静的グローバルIPを持っていること
　(静的IPを持っていない場合はDDNSが設定されていること)

・リモートサーバでSSHサービスが動作していること

・リモートサーバが外部からのSSH接続を受け付けること

詳しい説明は公式サイトをご覧ください。

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

本ソフトウェアはJava用のライブラリ 「Ganymed SSH2」と「JmDNS」
を用いて開発されました。各ライブラリのライセンスについて詳しく
は lib フォルダ内の各テキストファイルをご覧ください。

本ソフトウェアのアイコンは「90 Vector Icons」を「iPhone / iPad
icon PSD template」と共に改変して作成しました。ライセンス等に
ついて詳しくは resources フォルダ内のテキストファイルをご覧く
ださい。

本ソフトウェア自体は Apache License, Version 2.0 に従って配布
されており、GitHubでソースコードを入手できます。
https://github.com/arcatdmz/TunesPipe

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
http://digitalmuseum.jp/software/rtunes/
arc (at) digitalmuseum.jp
