package com.example.skyup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class HowToActivity extends AppCompatActivity {

    private ImageButton buttonBack; //ボタンビューオブジェクト
    private RelativeLayout relativeLayout;  //リレーティブレイアウトオブジェクト

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        //画面を縦に固定
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //レイアウトファイル内で設定したボタンのIDを取得
        if (buttonBack == null) {
            buttonBack = (ImageButton) findViewById(R.id.buttonBack);
        }

        //リレーティブレイアウトのIDを取得
        if (relativeLayout == null) {
            relativeLayout = (RelativeLayout) findViewById(R.id.layoutHowTo);
        }

        //イベントリスナークラスを生成
        class MyOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {

                //ボタンをクリックされたら、インテントを使って
                //次の画面（ShakeActivity）の画面へ遷移できるようにする
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

                //アクティビティの終了
                HowToActivity.this.finish();
            }
        }

        //ビューオブジェクトとイベントリスナーの関連付け
        buttonBack.setOnClickListener(new MyOnClickListener());

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        //リソースの開放
        if(buttonBack != null){
            buttonBack.setBackground(null);
        }
        if(relativeLayout != null){
            relativeLayout.setBackground(null);
        }
    }

}