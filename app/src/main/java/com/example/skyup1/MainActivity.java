package com.example.skyup1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonStart; //ボタンビューオブジェクト（おみくじを引くボタン）
    private ImageButton buttonHowTo; //ボタンビューオブジェクト（使い方ボタン）
    private RelativeLayout relativeLayout;  //リレーティブレイアウトオブジェクト


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //画面を縦に固定
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //レイアウトファイル内で設定したボタンのIDを取得
        if(buttonStart == null) {
            buttonStart = (ImageButton) findViewById(R.id.buttonStart);
        }

        if(buttonHowTo == null){
            buttonHowTo = (ImageButton)findViewById(R.id.buttonHowTo);
        }

        //リレーティブレイアウトのIDを取得
        if(relativeLayout == null) {
            relativeLayout = (RelativeLayout) findViewById(R.id.layoutHowTo);
        }


        //ビューオブジェクトとイベントリスナーの関連付け
        buttonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //ボタンをクリックされたら、インテントを使って
                //次の画面（ShakeActivity）の画面へ遷移できるようにする
                Intent intent = new Intent(getApplicationContext(),ShakeActivity.class);

                startActivity(intent);

                //アクティビティの終了
                MainActivity.this.finish();
            }
        });


        //ビューオブジェクトとイベントリスナーの関連付け
        buttonHowTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //ボタンをクリックされたら、インテントを使って
                //次の画面（HowToActivity）の画面へ遷移できるようにする
                Intent intent = new Intent(MainActivity.this,HowToActivity.class);

                startActivity(intent);

                //アクティビティの終了
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        //リソースの開放
        if(buttonStart != null){
            buttonStart.setBackground(null);
        }
//        if(buttonHowTo != null){
//            buttonHowTo.setBackground(null);
//        }
        if(relativeLayout != null){
            relativeLayout.setBackground(null);
        }
    }
}