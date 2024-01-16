package com.example.cameralibraryjavaexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.kadb.cameralibrary.presentation.CameraIntent;
import kr.co.kadb.cameralibrary.presentation.model.CropSize;
import kr.co.kadb.cameralibrary.presentation.widget.util.IntentKey;
import kr.co.kadb.cameralibrary.presentation.widget.util.UriHelper;

public class MainActivity extends AppCompatActivity {
    private final CropSize cropSize = new CropSize(0.7f, 0.5f);

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
                        //int imageWidth = intent.getIntExtra(IntentKey.EXTRA_WIDTH, 0);
                        // 이미지 세로.
                        //int imageHeight = intent.getIntExtra(IntentKey.EXTRA_HEIGHT, 0);
                        // 이미지 방향.
                        //int imageRotation = intent.getIntExtra(IntentKey.EXTRA_ROTATION, 0);
                        // 썸네일 Bitmap.
                        //Bitmap thumbnailBitmap = (Bitmap) intent.getExtras().get("data");

                        // Uri를 이미지로 변환.
                        Bitmap bitmap = UriHelper.toBitmap(getBaseContext(), imageUri);

                        // 이미지 중앙을 기준으로 원본 사이즈에서 가로:70% 세로:50% 크롭.
                        /*Bitmap bitmap = UriHelper.rotateAndCenterCrop(
                                getBaseContext(), imageUri, cropSize.getWidth(), cropSize.getHeight()
                        );*/

                        // 가로, 세로 중 큰 길이를 640(pixel)에 맞춰 비율 축소.
                        //Bitmap resizeBitmap = BitmapHelper.resize(bitmap, 640);
                        // 가로, 세로 중 큰 길이를 640(pixel)에 가깝게(640이상 ~ 1280미만) 맞춰 비율 축소.
                        // 예) resizePixcel이 640인 경우 결과는 640이상 ~ 1280미만.
                        // 성능 및 좋은 샘플링으로 이미지를 추출.
                        //Bitmap resizeBitmap = BitmapHelper.optimumResize(bitmap, 640);
                        if (bitmap != null) {
                            bitmap.recycle();
                        }

                        // Bitmap 저장.
                        // isPublicDirectory: true: 공용저장소, false: 개별저장소.
                        /*String resizeImageUri = BitmapHelper.save(
                                getBaseContext(), resizeBitmap, true);*/

                        // Base64로 인코딩 된 문자열 반환.
                        /*String base64 = BitmapHelper.toBase64(resizeBitmap);
                        ((TextView) findViewById(R.id.textview)).setText(base64);*/

                        // 촬영 원본 이미지.
                        ((ImageView) findViewById(R.id.imageview)).setImageURI(imageUri);
                        // 촬영 원본을 크롭 및 리사이즈한 이미지.
                        //((ImageView) findViewById(R.id.imageview_thumbnail)).setImageBitmap(resizeBitmap);
                    } else if (IntentKey.ACTION_TAKE_MULTIPLE_PICTURES.equals(intent.getAction())) {
                        // 여러장.
                    } else if (IntentKey.ACTION_DETECT_MILEAGE_IN_PICTURES.equals(intent.getAction()) ||
                            IntentKey.ACTION_DETECT_VIN_NUMBER_IN_PICTURES.equals(intent.getAction())) {
                        // 감지한 텍스트.
                        String detectText = intent.getStringExtra(IntentKey.EXTRA_DETECT_TEXT);
                        ((TextView) findViewById(R.id.textview)).setText(detectText);
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
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_PICTURE);
            // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false);
            // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true);
            // 크롭 영역 설정(Deprecated).
            //intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent);
            // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CROP_SIZE, cropSize);
            // 회전 가능.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true);
            // 수평선 색상.
            intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED);
            // 크롭 인 라인 색상.
            intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN);
            // 크롭 영역 설정 시 원본 대신 크롭 된 사진 저장 후 반환.
            intent.putExtra(IntentKey.EXTRA_IS_SAVE_CROPPED_IMAGE, false);
            startActivityForResult(intent, 0);*/

            // Example 2.
            /*Intent intent = new Intent(IntentKey.ACTION_TAKE_PICTURE);
            // 단말 음소거 시 셔터 무음.
            intent.putExtra(IntentKey.EXTRA_CAN_MUTE, false);
            // 미리보기 화면에 수평선 표시.
            intent.putExtra(IntentKey.EXTRA_HAS_HORIZON, true);
            // 크롭 영역 설정(Deprecated).
            //intent.putExtra(IntentKey.EXTRA_CROP_PERCENT, cropPercent);
            // 크롭 영역 설정.
            intent.putExtra(IntentKey.EXTRA_CROP_SIZE, cropSize);
            // 회전 가능.
            intent.putExtra(IntentKey.EXTRA_CAN_UI_ROTATION, true);
            // 수평선 색상.
            intent.putExtra(IntentKey.EXTRA_HORIZON_COLOR, Color.RED);
            // 크롭 인 라인 색상.
            intent.putExtra(IntentKey.EXTRA_CROP_BORDER_COLOR, Color.GREEN);
            // 크롭 영역 설정 시 원본 대신 크롭 된 사진 저장 후 반환.
            intent.putExtra(IntentKey.EXTRA_IS_SAVE_CROPPED_IMAGE, false);
            mResultLauncher.launch(intent);*/

            // Example 3.
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_TAKE_PICTURE)
                    .setCanMute(false) // 단말 음소거 시 셔터 무음.
                    .setHasHorizon(true) // 미리보기 화면에 수평선 표시.
                    //.setCropPercent(cropPercent) // 크롭 영역 설정(Deprecated).
                    .setCropSize(cropSize) // 크롭 영역 설정.
                    .setCanUiRotation(true) // 회전 가능.
                    .setHorizonColor(Color.RED) // 수평선 색상.
                    .setUnusedAreaBorderColor(Color.GREEN) // 크롭 인 라인 색상.
                    .setSaveCropedImage(false); // 크롭 영역 설정 시 원본 대신 크롭 된 사진 저장 후 반환.
            mResultLauncher.launch(intent.build());
        });

        // 여러장 촬영.
        findViewById(R.id.button_multiple_shoot).setOnClickListener(view -> {
            // 한 장 촬영 시 사용 된 구성 요소 사용 가능.
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_TAKE_MULTIPLE_PICTURES);
            mResultLauncher.launch(intent.build());
        });

        // 주행거리 촬영.
        // 크롭 모드 사용 불가.
        findViewById(R.id.button_mileage_shoot).setOnClickListener(view -> {
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_DETECT_MILEAGE_IN_PICTURES)
                    .setCanMute(false)
                    .setHasHorizon(true)
                    .setCanUiRotation(true)
                    .setHorizonColor(Color.RED);
            mResultLauncher.launch(intent.build());
        });

        // 차대번호 촬영.
        // 크롭 모드 사용 불가.
        findViewById(R.id.button_vin_number_shoot).setOnClickListener(view -> {
            CameraIntent.Build intent = new CameraIntent.Build(this)
                    .setAction(IntentKey.ACTION_DETECT_VIN_NUMBER_IN_PICTURES)
                    .setCanMute(false)
                    .setHasHorizon(true)
                    .setCanUiRotation(true)
                    .setHorizonColor(Color.RED);
            mResultLauncher.launch(intent.build());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Example 1. 결과 수신.(권장되지 않음)
    }
}