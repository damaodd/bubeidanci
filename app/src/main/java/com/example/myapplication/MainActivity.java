package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Switch switchFloatingButton;
    private Switch switchAccessibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //无障碍开关
        switchAccessibility = findViewById(R.id.switch_accessibility);
        switchAccessibility.setChecked(isAccessibilityServiceEnabled(this, MyAccessibilityService.class));

        switchAccessibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !isAccessibilityServiceEnabled(this, MyAccessibilityService.class)) {
                // 用户尝试开启服务，但服务未激活，引导用户去设置
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Toast.makeText(this, "Please enable the service in Settings", Toast.LENGTH_LONG).show();
            }
        });
        //悬浮开关
        switchFloatingButton = findViewById(R.id.switch_floating_button);
        switchFloatingButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startService(new Intent(MainActivity.this, FloatingButtonService.class));
            } else {
                stopService(new Intent(MainActivity.this, FloatingButtonService.class));
            }
        });


        // 保持 Switch 状态与服务状态一致
    }
    private void logButtonCoordinates(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location); // 获取按钮在屏幕上的位置
        int x = location[0];
        int y = location[1];

        Log.d("ButtonCoordinates", "Button is at: x=" + x + ", y=" + y); // 将坐标输出到Logcat
    }
    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends android.accessibilityservice.AccessibilityService> service) {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString != null && prefString.contains(context.getPackageName() + "/" + service.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 更新Switch状态，以防用户在设置中更改了服务状态
         switchAccessibility.setChecked(isAccessibilityServiceEnabled(this, MyAccessibilityService.class));
    }


}




