package com.example.smartgoals.navigator_0;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class SpashScreen extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme_Launcher);
        setContentView(R.layout.activity_spash_screen);

        final TextView splash_text = findViewById(R.id.splash_text);
        final ImageView splash_image = findViewById(R.id.splash_image);


//
        splash_image.setImageResource(R.drawable.pyramid_chart);

        final ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        splash_image.setColorFilter(filter);
        ValueAnimator animation = ValueAnimator.ofFloat(1f, 0f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(3000);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                matrix.setSaturation(animation.getAnimatedFraction());
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                splash_image.setColorFilter(filter);
            }
        });

        animation.start();
        ObjectAnimator textViewAnimatorY = ObjectAnimator.ofFloat(splash_text, "translationY", 700f, 0f);
//        ObjectAnimator textViewAnimatorMove = ObjectAnimator.ofFloat(splash_text, "translationX", 700f, 0f);
        ObjectAnimator splash_flip = ObjectAnimator.ofFloat(splash_image, "rotationY", 180f, 0f);
        /*
         * Translation Y values--> refer to end (top) position. 0f = the original position of the text view (
         * or other view), as specified in the screen xml. t4 Note that this means the text is going to END
         *
         * */
//        textViewAnimatorY.setDuration(3000);
//        textViewAnimatorMove.setDuration(3000);
//        splash_flip.setDuration(3000);
        AnimatorSet flip_image_move_text = new AnimatorSet();
        flip_image_move_text.setDuration(3000);
        flip_image_move_text.playTogether(textViewAnimatorY, splash_flip);
        flip_image_move_text.start();
//        textViewAnimatorY.setDuration(3000);
//        textViewAnimatorY.setInterpolator(new AccelerateDecelerateInterpolator());
//        textViewAnimatorY.start();


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SpashScreen.this, MainActivity.class);
                    startActivity(intent);
                    SpashScreen.this.finish();
                } catch (InterruptedException e) {

                } finally {
                    SpashScreen.this.finish();
                }

            }
        };
        thread.start();
    }
}

