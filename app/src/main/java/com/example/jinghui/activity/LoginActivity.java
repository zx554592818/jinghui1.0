package com.example.jinghui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.util.MyOkHttpClient;
import com.example.jinghui.R;
import com.example.jinghui.ui.ToastShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//登陆
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button loginBtn, resetBtn;
    private EditText accountEdt, passwordEdt;
    private CheckBox check_password;
    private SharedPreferences sp;
    private String account, password;
    private ProgressBar progressBar;
    private MyApplication myApplication;
    private RelativeLayout activity_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loginBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        accountEdt.setOnClickListener(this);
        passwordEdt.setOnClickListener(this);
        activity_login.setOnClickListener(this);
        //从 数据库找寻文件 添加到文本框
        if (sp.getBoolean("ISCHECK", false)) {
            check_password.setChecked(true);
            accountEdt.setText(sp.getString("ACCOUNT", ""));
            passwordEdt.setText(sp.getString("PASSWORD", ""));
        }
        //切换CheckBox的选中状态  改变 ISCHECK 的值
        check_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check_password.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });


    }

    //点击更多执行这里
    public void clickMore(View view) {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.popwindowlayout, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view1,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(LoginActivity.this.findViewById(R.id.clickMore),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        Button register = (Button) view1.findViewById(R.id.register);
        Button regard = (Button) view1.findViewById(R.id.regard);
        Button exit = (Button) view1.findViewById(R.id.exit);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        regard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent = new Intent(LoginActivity.this, RegardActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("确认退出?");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.create().show();
            }

        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    public void initView() {
        myApplication = MyApplication.getInstance();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        accountEdt = (EditText) findViewById(R.id.accuntEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        check_password = (CheckBox) findViewById(R.id.check_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        activity_login = (RelativeLayout) findViewById(R.id.activity_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetBtn:
                //重置清空输入框
                accountEdt.setText(null);
                passwordEdt.setText(null);
                break;
            case R.id.accuntEdt:
                //默认弹出数字字母框
                // accountEdt.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                break;
            case R.id.passwordEdt:
                // passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.activity_login:
                //设置外部点击也可以隐藏输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            //首页登陆界面  执行登陆
            case R.id.loginBtn:
                //3.登陆成功才保存  账号和密码 到数据库
                account = accountEdt.getText().toString();
                password = passwordEdt.getText().toString();
                if (!account.isEmpty() && !password.isEmpty()) {
                    loginPost();
                } else {
                    new ToastShow(LoginActivity.this).toastShow("请输入账号和密码");
                }
                //Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                //startActivity(intent);
                break;
        }
    }

    //基于OKHttp执行网络请求localhost:8080
    public void loginPost() {
        progressBar.setVisibility(View.VISIBLE);//点击登陆展示滚动进度条
        loginBtn.setEnabled(false);
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/UserManager";   //后台接口    acion：login
        RequestBody requestBody = new FormBody.Builder().add("username", account).add("password", password).add("action", "login").build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        MyOkHttpClient.getOkHttpClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        loginBtn.setEnabled(true);
                        new ToastShow(LoginActivity.this).toastShow("服务出错，请检查你的网络设置");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //登陆成功
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject json = new JSONObject(result);
                            String success = json.getString("field");
                            final String msg = json.getString("errorMsg");
                            //后台规定返回值为"success"就登陆成功
                            Log.d("sss", success);
                            if (success.equals("success")) {
                                //Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                //  Intent intent = new Intent(LoginActivity.this, AirQualityActivity.class);
                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setEnabled(true);
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginBtn.setEnabled(true);
                                        new ToastShow(LoginActivity.this).toastShow(msg);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (check_password.isChecked()) {
                    //如果登陆成功，又是选择状态则把账号和密码存进储存库 sp
                    Editor editor = sp.edit();
                    editor.putString("ACCOUNT", account);
                    editor.putString("PASSWORD", password);
                    editor.commit();
                }
            }
        });
    }

}