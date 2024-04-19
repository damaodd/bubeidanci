package com.example.myapplication;






import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.content.ContextCompat;


public class FloatingButtonService extends Service {
    private WindowManager windowManager;
    private ImageButton button1, button2, button3, button4;
    private int[] button3Position = new int[2];
    private int[] button4Position = new int[2];
    int width = 250;  // 宽度200像素
    int height = 150; // 高度100像素
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        button1 = createFloatingButton("Button 1");
        button2 = createFloatingButton("Button 2");
//        button3 = createFloatingButton("Button 3");
//        button4 = createFloatingButton("Button 4");
    }

    private ImageButton createFloatingButton(String buttonText) {
        ImageButton floatingButton = new ImageButton(this);

        floatingButton.setBackgroundResource(R.drawable.backgroundcolor);

//        floatingButton.setText(buttonText);
        if ("Button 1".equals(buttonText)) {
            floatingButton.setImageResource(R.drawable.ic_yes);  // 指定按钮图像
            floatingButton.setScaleType(ImageView.ScaleType.FIT_CENTER);  // 图像适应按钮大小但保持比例
        } else if ("Button 2".equals(buttonText)) {
            floatingButton.setImageResource(R.drawable.ic_no);
            floatingButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width, height,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.TOP | Gravity.LEFT; // 确保窗口对齐到屏幕左上角
        params.x = 0;
        params.y = 100 * Integer.parseInt(buttonText.split(" ")[1]);

        windowManager.addView(floatingButton, params);

        floatingButton.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            private int lastAction;
            private boolean isDragging;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        lastAction = event.getAction();
                        isDragging = false;  // 初始化为非拖动状态
                        v.setBackgroundResource(R.color.hui);  // 显式设置按下状态的背景
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isDragging) {
                            // 只有当不是拖动操作时，才处理点击事件
                            if ("Button 1".equals(buttonText)) {
                                simulateClick(223, 2533);
                            } else if ("Button 2".equals(buttonText)) {
                                simulateClick(944, 2533);
                            }
                            Toast.makeText(FloatingButtonService.this, buttonText + " clicked", Toast.LENGTH_SHORT).show();
                        }
                        v.setBackgroundResource(R.drawable.backgroundcolor);  // 恢复 selector 控制的背景
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // 计算移动的距离
                        int deltaX = (int) (event.getRawX() - initialTouchX);
                        int deltaY = (int) (event.getRawY() - initialTouchY);
                        if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) { // 如果移动距离超过10像素，认为是拖动
                            isDragging = true;
                            params.x = initialX + deltaX;
                            params.y = initialY + deltaY;
                            windowManager.updateViewLayout(floatingButton, params);
                        }
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });


        return floatingButton;
    }

    private void simulateClick(int x, int y) {
        Intent intent = new Intent("com.example.myapplication.PERFORM_CLICK");
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (button1 != null) windowManager.removeView(button1);
        if (button2 != null) windowManager.removeView(button2);
        if (button3 != null) windowManager.removeView(button3);
        if (button4 != null) windowManager.removeView(button4);
    }
}


