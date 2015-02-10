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

        DisplayMetrics dm = new DisplayMetrics();
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
        private Canvas mCanvas = null;
        private Path mPath = null;
        private Paint mPaint = null;
        private Bitmap bitmap = null;

        private int widget, height;
//        MyThread mThread;
        int[] pixels;



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
            mPaint.setStrokeWidth(160);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setAlpha(0);

            mCanvas = new Canvas(bitmap);
            mCanvas.drawColor(Color.GRAY);

//            // 在字线程中创建Handler接收像素消息
//            mThread = new MyThread();
//            mThread.start();
        }

        private void setBackground() {
            Paint paint = new Paint();
            Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            paint.setTextSize(80);
            paint.setColor(Color.BLACK);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawText("Thanks for your getup!", 100, 100, paint);
            canvas.drawText("Try your luck again?", 100, 250, paint);
            setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mCanvas.drawPath(mPath, mPaint);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            int currX = (int) event.getX();
            int currY = (int) event.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mPath.reset();
                    x = currX;
                    y = currY;
                    mPath.moveTo(x, y);
                }
                break;
                case MotionEvent.ACTION_MOVE: {
                    mPath.quadTo(x, y, currX, currY);
                    x = currX;
                    y = currY;
                    postInvalidate();
//                    computeScale();

                    // compute the percentage on the loop
                    int sum = pixels.length;
                    int num = 0;
                    for (int pixel : pixels) {
                        if (pixel == 0) {
                            num++;
                        }
                    }

                    double percentPixel = (num / (double) sum);
                    if(percentPixel > 0.85){
                        // out put unlock view and content change to weather info, music output as well

                    }
                    System.out.println("百分比:" + percentPixel * 100);

                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    mPath.reset();
                }
                break;
            }
            return true;
        }


//        /**
//         * 异步线程，作用是创建handler接收处理消息。
//         * @author
//         *
//         */
//        class MyThread extends Thread {
//
//            public MyThread() {
//            }
//
//            @Override
//            public void run() {
//                super.run();
//            /*
//             * 创建 handler前先初始化Looper.
//             */
//                Looper.prepare();
//
//                mHandler = new Handler() {
//                    @Override
//                    public void dispatchMessage(Message msg) {
//                 //       super.dispatchMessage(msg);   //temperours closing HERE, needs to change
//                        // 只处理最后一次的百分比
//                        if ((Integer) (msg.obj) != messageCount) {
//                            return;
//                        }
//                        // 取出像素点
//                        synchronized (bitmap) {
//                            if (pixels == null) {
//                                pixels = new int[bitmap.getWidth()
//                                        * bitmap.getHeight()];
//                            }
//                            bitmap.getPixels(pixels, 0, widget, 0, 0, widget,
//                                    height);
//                        }
//
//                        int sum = pixels.length;
//                        int num = 0;
//                        for (int pixel : pixels) {
//                            if (pixel == 0) {
//                                num++;
//                            }
//                        }
//
//                        double percentPixel = (num / (double) sum);
//                        if(percentPixel > 0.85){
//                            // out put unlock view and content change to weather info, music output as well
//
//                        }
//                        System.out.println("百分比:" + percentPixel * 100);
//                    }
//                };
//            /*
//             * 启动该线程的消息队列
//             */
//                Looper.loop();
//            }
//        }
//


   }
}
