package com.example.jinghui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

/**
 * Created by zhaoxin on 2017-04-11.
 */
//注册界面
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText registerAccount, registerPassword, passwordConfirm;
    private MyApplication myApplication;
    private CheckBox passwordShow;
    private Button btnRegister;
    private ProgressBar progressBar;
    private RelativeLayout activity_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setOnListener();
    }

    public void setOnListener() {
        activity_register.setOnClickListener(this);
        registerAccount.setOnClickListener(this);
        registerPassword.setOnClickListener(this);
        passwordConfirm.setOnClickListener(this);
    }

    public void initView() {
        myApplication = MyApplication.getInstance();
        activity_register = (RelativeLayout) findViewById(R.id.activity_register);
        passwordShow = (CheckBox) findViewById(R.id.passwordShow);
        registerAccount = (EditText) findViewById(R.id.registerAccount);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void showPassword(View view) {
        if (passwordShow.isChecked()) {
            registerPassword.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            passwordConfirm.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());

        } else {
            registerPassword.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            passwordConfirm.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    public void reset(View view) {
        registerPassword.setText(null);
        passwordConfirm.setText(null);
    }

    public void registerConfirm(View view) {
        String account = registerAccount.getText().toString();
        String password = registerPassword.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        if (account.length() > 4 && password.equals(passwordConfirm) && password.length() > 5) {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
            String url = myApplication.DEBUG_BASE_URL + "/jhhb/UserManager";   //后台接口    acion：register
            RequestBody requestBody = new FormBody.Builder().add("username", account).add("password", password).add("action", "register").build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            MyOkHttpClient.getOkHttpClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ToastShow(RegisterActivity.this).toastShow("服务出错，请检查你的网络设置！");
                            progressBar.setVisibility(View.INVISIBLE);
                            btnRegister.setVisibility(View.VISIBLE);
                            btnRegister.setEnabled(true);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject json = new JSONObject(result);
                            String field = json.getString("field");
                            Log.d("sss", field);
                            final String msg = json.getString("errorMsg");
                            if (field.equals("success")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        btnRegister.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });

                                timer.start();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        btnRegister.setEnabled(true);
                                        btnRegister.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else if (!password.equals(passwordConfirm)) {
            new ToastShow(this).toastShow("请确认输入密码是否一致");
        } else if (account.isEmpty()) {
            new ToastShow(this).toastShow("请输入账号");
        } else if (password.isEmpty() || passwordConfirm.isEmpty()) {
            new ToastShow(this).toastShow("请输入密码");
        } else if (account.length() < 5) {
            new ToastShow(this).toastShow("账号有误,最小长度为5");
        } else if (password.length() < 6) {
            new ToastShow(this).toastShow("密码最小长度为6");
        }

    }

    private CountDownTimer timer = new CountDownTimer(5000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnRegister.setText((millisUntilFinished / 1000) + "秒后跳转到登陆界面");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    };

    public void registerBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerAccount:
                // registerAccount.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                break;
            case R.id.registerPassword:
                // registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                break;
            case R.id.passwordConfirm:
                // passwordConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                break;
            case R.id.activity_register:
                //设置外部点击也可以隐藏输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }
}
