package za.co.neilson.alarm.weatheractivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zenkig on 15/2/5.
 */

public class ScrathActivity extends Activity {
    int screenWidth = 0;
    int screenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        setContentView(new GuaGuaKa(this));
    }

    class GuaGuaKa extends View {
        android.os.Handler mHandler;
        int messageCount;
        int x = 0;
        int y = 0;
        private int widget, height;
        //        MyThread mThread;
        int[] pixels;


        private Canvas mCanvas = null;
        private Path mPath = null;
        private Paint mPaint = null;
        private Bitmap bitmap = null;
        boolean isComplete;

        public GuaGuaKa(Context context) {
            super(context);
            init(context);
        }

        private void computeScale() {
            Message msg = mHandler.obtainMessage(0);
            msg.obj = ++messageCount;
            mHandler.sendMessage(msg);
        }

        private void init(Context context) {
            setBackground();

            mPath = new Path();
            bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            mPaint = new Paint();
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setTextSize(80);
            mPaint.setStrokeWidth(180);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setAlpha(0);

            mCanvas = new Canvas(bitmap);
            mCanvas.drawColor(Color.LTGRAY);
        }

        private void setBackground() {
            Paint paint = new Paint();
            Bitmap bitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
            paint.setTextSize(80);
            paint.setColor(Color.argb(120,54,65,103));
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.GRAY);
            canvas.drawText("Thanks for the Getup!", 200, 200, paint);
            setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

//            canvas.drawText("Thanks for the Getup!", 100, 100, mPaint);

            if (!isComplete) {
                mCanvas.drawPath(mPath, mPaint);
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }

        private Runnable mRunnable = new Runnable()
        {
            private int[] mPixels;

            @Override
            public void run()
            {

                int w = getWidth();
                int h = getHeight();

                float wipeArea = 0;
                float totalArea = w * h;
                mPixels = new int[w * h];

                /**
                 * all pixels info
                 */
                bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

                /**
                 * traverse the areas
                 */
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        int index = i + j * w;
                        if (mPixels[index] == 0)
                        {
                            wipeArea++;
                        }
                    }
                }

                /**
                 * percentage computation on 65%, done !
                 */
                if (wipeArea > 0 && totalArea > 0)
                {
                    int percent = (int) (wipeArea * 100 / totalArea);
                    Log.e("TAG", percent + "");

                    if (percent > 65)
                    {
                        isComplete = true;
                        postInvalidate();
                        // set a page jumper HERE ! to turn to weather info page

                    }
                }
            }

        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            int currX = (int) event.getX();
            int currY = (int) event.getY();
            switch(action){
                case MotionEvent.ACTION_DOWN:{
                    mPath.reset();
                    x = currX;
                    y = currY;
                    mPath.moveTo(x, y);
                }break;

                case MotionEvent.ACTION_MOVE:{
                    mPath.quadTo(x, y, currX, currY);
                    x = currX;
                    y = currY;
                    postInvalidate();

                }break;

                case MotionEvent.ACTION_UP: {
                    new Thread(mRunnable).start();
                }break;

                case MotionEvent.ACTION_CANCEL:{
                    mPath.reset();
                }break;
            }
            return true;
        }
    }
}