package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;

import android.accessibilityservice.GestureDescription;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;


public class MyAccessibilityService extends AccessibilityService {

    private static MyAccessibilityService instance;

    public static MyAccessibilityService getInstance() {
        return instance;
    }
    private static final String TAG = "ooooooooo";
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
        // 配置服务
    }

    private BroadcastReceiver clickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.myapplication.PERFORM_CLICK".equals(intent.getAction())) {
                int x = intent.getIntExtra("x", 0);
                int y = intent.getIntExtra("y", 0);
                performClick(x, y);
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("com.example.myapplication.PERFORM_CLICK");
        registerReceiver(clickReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        unregisterReceiver(clickReceiver);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 处理辅助功能事件
    }

    @Override
    public void onInterrupt() {
        // 中断服务时的处理
    }

    public void performClick(float x, float y) {


        // 创建点击手势
        GestureDescription.Builder gestureBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gestureBuilder = new GestureDescription.Builder();
        }
        Path clickPath = new Path();
        clickPath.moveTo(x, y);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gestureBuilder.addStroke(new GestureDescription.StrokeDescription(clickPath, 0, 1));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dispatchGesture(gestureBuilder.build(), null, null);
        }
    }
}
