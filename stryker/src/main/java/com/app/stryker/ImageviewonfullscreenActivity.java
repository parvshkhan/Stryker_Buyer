package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageviewonfullscreenActivity extends Activity {
    private com.app.stryker.TouchImageView imageView;
    ImageView img_back_icon_product;
    Context context;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewonfullscreen);
        imageView = (com.app.stryker.TouchImageView) findViewById(R.id.img);
        img_back_icon_product = (ImageView) findViewById(R.id.img_back_icon_product);

        context = this;
        Bundle b = getIntent().getExtras();
        Log.e("----------->", "*" + context.getString(R.string.image_base_url) + b.getString("imgurl"));
        if (b != null) {
            try {
                Picasso.with(context)
                        .load(context.getString(R.string.image_base_url)
                                + b.getString("imgurl"))
                        .placeholder(R.drawable.circle_1)
                        //.transform(new CircleTransform())
                        .resize(1200, 800)
                        //.centerCrop()
                        .into(imageView);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        img_back_icon_product.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 0.5f));
            matrix.setScale(scaleFactor, scaleFactor);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }
}
