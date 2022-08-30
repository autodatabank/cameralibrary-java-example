package com.example.cameralibraryjavaexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.kadb.cameralibrary.presentation.CameraIntent;
import kr.co.kadb.cameralibrary.presentation.widget.util.BitmapHelper;
import kr.co.kadb.cameralibrary.presentation.widget.util.IntentKey;
import kr.co.kadb.cameralibrary.presentation.widget.util.UriHelper;

public class MainActivity extends AppCompatActivity {
    private final Float[] cropPercent = {0.7f, 0.5f};

    // Activity for result.
    // Example 2. 3. 의 결과 수신.
    private final ActivityResultLauncher<Intent> mResultLauncher = registerForActivityResult(
            new StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() == null) {
                        return;
                    }

                    Intent intent = result.getData();
                    if (IntentKey.ACTION_TAKE_PICTURE.equals(intent.getAction())) {
                        // 이미지 URI.
                        Uri imageUri = intent.getData();
                        // 이미지 가로.
                        int imageWidth = intent.getIntExtra(IntentKey.EXTRA_WIDTH, 0);
                        // 이미지 세로.
                        int imageHeight = intent.getIntExtra(IntentKey.EXTRA_HEIGHT, 0);
                        // 이미지 방향.
                        int imageRotation = intent.getIntExtra(IntentKey.EXTRA_ROTATION, 0);
                        // 썸네일 Bitmap.
                        Bitmap thumbnailBitmap = (Bitmap) intent.getExtras().get("data");

                        // 이미지 중앙을 기준으로 원본 사이즈에서 가로:70% 세로:50% 크롭.
                        Bitmap cropBitmap = UriHelper.rotateAndCenterCrop(
                                getBaseContext(), imageUri, cropPercent
                        );

                        // 가로, 세로 중 큰 길이를 640(pixel)에 맞춰 비율 축소.
                        Bitmap resizeBitmap = BitmapHelper.resize(cropBitmap, 640);
                        if (cropBitmap != null) {
                            cropBitmap.recycle();
                        }

                        // Bitmap 저장.
                        // isPublicDirectory: true: 공용저장소, false: 개별저장소.
                        String resizeImageUri = BitmapHelper.save(
                                getBaseContext(), resizeBitmap, true);

                        // Base64로 인코딩 된 문자열 반환.
                        String base64 = BitmapHelper.toBase64(resizeBitmap);

                        // 촬영 원본 이미지.
                        ((ImageView) findViewById(R.id.imageview)).setImageURI(imageUri);
                        // 촬영 원본을 크롭 및 리사이즈한 이미지.
                        ((ImageView) findViewById(R.id.imageview_thumbnail)).setImageBitmap(resizeBitmap);
                    } else if (IntentKey.ACTION_TAKE_MULTIPLE_PICTURES.equals(intent.getAction())) {
                        // 여러장.
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 한장 촬영.
        findViewById(R.id.button_one_shoot).setOnClickListener(view -> {
            // Example 1.(권장되지 않음)
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_PICTURE); // 한 장.
            //intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false); // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true); // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent); // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true); // 회전 가능.
            //intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED); // 수평선 색상.
            //intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN); // 크롭 인 라인 색상.
            startActivityForResult(intent, 0);*/

            // Example 2.
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_PICTURE); // 한 장.
            //intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false); // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true); // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent); // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true); // 회전 가능.
            //intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED); // 수평선 색상.
            //intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN); // 크롭 인 라인 색상.
            mResultLauncher.launch(intent);*/

            // Example 3.
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_TAKE_PICTURE) // 한 장.
                    //.setCanMute(false) // 단말 음소거 시 셔터 무음.
                    .setHasHorizon(true) // 미리보기 화면에 수평선 표시.
                    .setCropPercent(cropPercent) // 크롭 영역 설정.
                    .setCanUiRotation(true); // 회전 가능.
            //.setHorizonColor(Color.RED) // 수평선 색상.
            //.setUnusedAreaBorderColor(Color.GREEN); // 크롭 인 라인 색상.
            mResultLauncher.launch(intent.build());
        });

        // 여러장 촬영.
        findViewById(R.id.button_multiple_shoot).setOnClickListener(view -> {
            // Example 1.(권장되지 않음)
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_MULTIPLE_PICTURES); // 여러 장.
            //intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false); // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true); // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent); // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true); // 회전 가능.
            //intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED); // 수평선 색상.
            //intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN); // 크롭 인 라인 색상.
            startActivityForResult(intent, 0);*/

            // Example 2.
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_MULTIPLE_PICTURES); // 여러 장.
            //intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false); // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true); // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent); // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true); // 회전 가능.
            //intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED); // 수평선 색상.
            //intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN); // 크롭 인 라인 색상.
            mResultLauncher.launch(intent);*/

            // Example 3.
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_TAKE_MULTIPLE_PICTURES) // 여러 장.
                    //.setCanMute(false) // 단말 음소거 시 셔터 무음.
                    .setHasHorizon(true) // 미리보기 화면에 수평선 표시.
                    .setCropPercent(cropPercent) // 크롭 영역 설정.
                    .setCanUiRotation(true); // 회전 가능.
            //.setHorizonColor(Color.RED) // 수평선 색상.
            //.setUnusedAreaBorderColor(Color.GREEN); // 크롭 인 라인 색상.
            mResultLauncher.launch(intent.build());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Example 1. 결과 수신.(권장되지 않음)
    }
}