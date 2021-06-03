package com.example.skyup1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.List;


public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private static final int SPEED_THRESHOLD = 1000;  //1カウントに必要な端末の最低速度
    private static final int SHAKE_TIMEOUT = 1000;    //端末が最低速度で振られるまでの時間
    private static final int SHAKE_DURATION = 1000;    //端末の加速を検知するまでの時間
    private static final int SHAKE_COUNT = 50; //端末が振るのに必要なカウント数
    private int shakeCount = 0;  //端末が振られたカウント数
    private long lastTime = 0;   //一番最後に端末の加速を検知したときの時間
    private long lastAccel = 0;    //一番最後に端末がSPEED_THRESHOLDの数値を超えた速度で振られた時間
    private long lastShake = 0;    //一番最後に端末が振られたときの時間
    private float xDimen = 0;  //端末が一番最後に振られたときのX座標の位置
    private float yDimen = 0;  //端末が一番最後に振られたときのY座標の位置
    private float zDimen = 0;  //端末が一番最後に振られたときのZ座標の位置
    private SensorManager manager;  //センサーマネージャーオブジェクト
    private RelativeLayout relativeLayout;  //リレーティブレイアウトオブジェクト
    private Vibrator vibrator;  //バイブレーションオブジェクト


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        //画面を縦に固定
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //センサーサービスを利用する
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //レイアウトリソースのIDを取得
        if(relativeLayout == null) {
            relativeLayout = (RelativeLayout) findViewById(R.id.layoutShake);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //加速度センサーの値を取得する
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            //リスナーの登録
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    protected void onPause(){
        super.onPause();

        //メモリの解除
        vibrator = null;

        //イベントリスナーの登録解除
        if(manager != null){
            manager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        //レイアウトを開放
        manager = null;
        if(relativeLayout != null){
            relativeLayout.setBackground(null);
        }

    }


    /*
     * 端末が動いたときに呼び出される処理
     *  @param event : 端末の座標
     *  @return; なし
     */
    @Override
    public void onSensorChanged(SensorEvent event) {


        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // シェイク時間のチェック
        long now = System.currentTimeMillis();
        if(lastTime == 0){
            lastTime = now;
        }
        // SHAKE_TIMEOUT までに次の加速を検知しなかったら shakeCount をリセット
        if(now - lastAccel > SHAKE_TIMEOUT){
            shakeCount = 0;
        }
        // 速度を算出する
        long diff = now - lastTime;

        //端末の座標位置を取得する
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //振られている端末の縦の速度を取得
        float speed = Math.abs(x + y + z - xDimen - yDimen - zDimen) / diff * 10000;

        if(speed > SPEED_THRESHOLD){

            //バイブレーションを振動
//            vibrator.vibrate(40);

            //端末を振るごとに背景画像の切り替え
            if(++shakeCount%2==0){
                relativeLayout.setBackgroundResource(R.drawable.sky_clouds_ue);
            }else{
                relativeLayout.setBackgroundResource(R.drawable.sky_clouds_shita);
            }

            /*
             * shakeCount の加算、SHAKE_COUNT を超えている　または
             * 最後のシェイク時間から SHAKE_DURATION 経過している場合
             * SwipeActivityへ遷移
             */
            if(shakeCount >= SHAKE_COUNT && now - lastShake > SHAKE_DURATION){
                lastShake = now;
                shakeCount = 0;
//                vibrator.vibrate(1000);

                Intent intent = new Intent(getApplicationContext(),SwipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                ShakeActivity.this.finish();
            }
            // SPEED_THRESHOLD を超える速度を検出した時刻をセット
            lastAccel = now;
        }

        //一番最後に端末が振られたときの時間と位置をセット
        lastTime = now;
        xDimen = x;
        yDimen = y;
        zDimen = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}