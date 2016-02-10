package com.app.pfh.zhihudaily.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SplashActivity extends AppCompatActivity {

    private ImageView mStartImageView;
    private File imageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage_layout);
        initImg();
        downloadStartImage();
    }

    private void downloadStartImage() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   String result = NetUtils.sendGet(UrlUtils.START_IMAGE_URL);
                   JSONObject resultObject = new JSONObject(result);
                   String url = (String) resultObject.get("img");
//                   Log.e("ZhihuDaily",url);
                   ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                       @Override
                       public void onLoadingStarted(String s, View view) {

                       }

                       @Override
                       public void onLoadingFailed(String s, View view, FailReason failReason) {

                       }

                       @Override
                       public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                           saveStartImg(bitmap);
                       }

                       @Override
                       public void onLoadingCancelled(String s, View view) {

                       }
                   });
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }).start();

    }

    private void initImg() {
        mStartImageView = (ImageView) findViewById(R.id.iv_start);
        File dir = getFilesDir();
        imageFile = new File(dir, "start.jpg");
        if (imageFile.exists()) {
            mStartImageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        } else {
            mStartImageView.setImageResource(R.mipmap.splash);
        }
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(1000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mStartImageView.startAnimation(scaleAnim);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void saveStartImg(Bitmap loadedImage) {
        FileOutputStream fos = null;
        imageFile.delete();
        try {
            fos = new FileOutputStream(imageFile);
            loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
