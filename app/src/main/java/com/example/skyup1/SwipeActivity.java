package com.example.skyup1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class SwipeActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        String url = "https://www.google.co.jp/maps/@35.7874723,139.7217769,15z?hl=ja";
//        String tag = "t3";
//        TextView msg = mLatitudeText;
//        Double msg = mLastLocation.getLatitude();
//        Log.d(tag,msg.toString());
//        String msg = mLatitudeLabel;
//        Log.d(tag,msg);
//        Log.d(tag,msg.toString());
//        String url = "https://www.google.co.jp/maps/@";
//        url += "35.7874723";
//        url += ",";
//        url += "139.7217769";
//        url += ",15z?hl=ja";
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//
//        startActivity(intent);
        /// ブラウザで開きたいURL
//        String url = "https://www.google.co.jp/maps/@35.7874723,139.7217769,15z?hl=ja";

        /// ブラウザ起動でページ表示
//        Intent intent = new Intent( Intent.ACTION_VIEW );
//        intent.setData( Uri.parse(url) );
//        startActivity( intent );
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));
//        String tag = "t3";
//        Double msg = mLastLocation.getLatitude();
//        Log.d(tag,msg.toString());
//                            String url = "https://www.google.co.jp/maps/@";
//                            url += mLastLocation.getLatitude();
//                            url += ",";
//                            url += mLastLocation.getLongitude();
//                            url += ",15z?hl=ja";
                            String url = "https://skyup-test04.s3-ap-northeast-1.amazonaws.com/skyuptest04.html?";
                            url += mLastLocation.getLatitude();
                            url += ",";
                            url += mLastLocation.getLongitude();
                            url += ",15z?hl=ja";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                            startActivity(intent);
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.layoutSwipe);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(SwipeActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}

/*
    private static final int SWIPE_MIN_DISTANCE_Y = 120; //縦の移動距離の最低値
    private static final int SWIPE_MAX_OFF_PATH_X = 250; //横の移動距離の最高値
    private static final int SWIPE_THRESHOLD_VELOCITY_Y = 200; //縦の移動速度の最低値
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //画面を縦に固定
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            */
/*
             * 画面をスワイプしたときに呼び出される処理
             * @param beforeEvent : 移動前の座標
             * @param afterEvent ; 移動後の座標
             * @param velocityX : 横（X軸）の移動速度
             * @param velocityY : 横（Y軸）の移動速度
             * @return false
             *//*

            @Override
            public boolean onFling(MotionEvent beforeEvent, MotionEvent afterEvent, float velocityX, float velocityY) {

                // 横（X軸）の移動距離が大きすぎる場合は無視
                if (Math.abs(beforeEvent.getX() - afterEvent.getX()) > SWIPE_MAX_OFF_PATH_X) {
                    return false;
                }

                */
/*
                 * 開始位置から終了位置の縦の移動距離が指定値より大きい または 縦の移動速度が指定値より大きいときは
                 * ResultActivityへ遷移
                 *//*

                if (beforeEvent.getY() - afterEvent.getY() > SWIPE_MIN_DISTANCE_Y && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY_Y) {

                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    SwipeActivity.this.finish();
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        //メモリの開放
        mGestureDetector = null;
    }

    */
/*
     * 画面をタッチした時に呼び出される処理
     *//*

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

}*/
