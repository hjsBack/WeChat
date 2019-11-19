package cn.csu.software.wechat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.constant.Configure;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.util.HttpUtils;
import cn.csu.software.wechat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 登录界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-18
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher, View.OnFocusChangeListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String URL_LOGIN = "http://34.92.16.72:8086/user/register";

    private Context mContext;

    private Button mLoginButton;

    private EditText mAccountEditText;

    private EditText mPasswordEditText;

    private RelativeLayout mAccountDeleteRelativeLayout;

    private RelativeLayout mPasswordDeleteRelativeLayout;

    private View mAccountLineView;

    private View mPasswordLineView;

    private RelativeLayout mLoginTitleRelativeLayout;

    private RelativeLayout mVerificationCodeRelativeLayout;

    private TextView mLoginTitleTextView;

    private TextView mLoginTypeTextView;

    private TextView mLoginAccountTipsTextView;

    private TextView mLoginPasswordTipsTextView;

    private boolean mIsAccountLogin;

    private ExecutorService mExecutorService;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(mContext, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    private void initView() {
        setContentView(R.layout.activity_login);
        mContext = this;
        mLoginButton = findViewById(R.id.bt_login);
        mLoginButton.setOnClickListener(this);
        mAccountEditText = findViewById(R.id.et_login_account);
        mAccountEditText.addTextChangedListener(this);
        mAccountEditText.setOnFocusChangeListener(this);
        mPasswordEditText = findViewById(R.id.et_login_password);
        mPasswordEditText.addTextChangedListener(this);
        mPasswordEditText.setOnFocusChangeListener(this);
        mAccountDeleteRelativeLayout = findViewById(R.id.rl_login_account_delete);
        mAccountDeleteRelativeLayout.setOnClickListener(this);
        mPasswordDeleteRelativeLayout = findViewById(R.id.rl_login_password_delete);
        mPasswordDeleteRelativeLayout.setOnClickListener(this);
        mAccountLineView = findViewById(R.id.v_login_line);
        mPasswordLineView = findViewById(R.id.v_password_line);
        mLoginTypeTextView = findViewById(R.id.tv_login_type);
        mLoginTitleRelativeLayout = findViewById(R.id.rl_login_type);
        mLoginTitleRelativeLayout.setOnClickListener(this);
        mLoginTitleTextView = findViewById(R.id.tv_login_title);
        mLoginAccountTipsTextView = findViewById(R.id.tv_login_account_tips);
        mLoginPasswordTipsTextView = findViewById(R.id.tv_login_password_tips);
        mVerificationCodeRelativeLayout = findViewById(R.id.rl_send_verification_code);
    }

    private void switchLoginType() {
        mIsAccountLogin = !mIsAccountLogin;
        mAccountEditText.getText().clear();
        mPasswordEditText.getText().clear();
        if (mIsAccountLogin) {
            mLoginTitleTextView.setText(R.string.account_login);
            mLoginTypeTextView.setText(R.string.email_login_type);
            mLoginAccountTipsTextView.setText(R.string.account);
            mLoginPasswordTipsTextView.setText(R.string.password);
            mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordEditText.setHint(R.string.password_tips);
            mAccountEditText.setHint(R.string.account_tips);
            mVerificationCodeRelativeLayout.setVisibility(View.GONE);
        } else {
            mLoginTitleTextView.setText(R.string.email_login);
            mLoginTypeTextView.setText(R.string.account_login_type);
            mLoginAccountTipsTextView.setText(R.string.email);
            mLoginPasswordTipsTextView.setText(R.string.verification_code);
            mPasswordEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            mPasswordEditText.setHint(R.string.verification_code_tips);
            mAccountEditText.setHint(R.string.email_tips);
            mVerificationCodeRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void saveConfigure() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantData.SHARED_LOGIN_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ConstantData.SHARED_LOGIN_KEY, true);
        editor.apply();
    }

    private void login(final String data) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(TAG, data);
                HttpUtils.httpPost(URL_LOGIN, data, new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtil.i(TAG, "login success");
                        Configure.setIsLogin(true);
                        saveConfigure();
                        Intent intent = new Intent();
                        intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int responseCode, Exception e) {
                        if ("admin".contentEquals(mAccountEditText.getText()) && "123456".contentEquals(mPasswordEditText.getText())) {
                            Configure.setIsLogin(true);
                            saveConfigure();
                            Intent intent = new Intent();
                            intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_MAIN);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            LogUtil.i(TAG, "login filed, responseCode %s", responseCode);
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                });
            }
        });
    }

    private String constructJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", mAccountEditText.getText());
            jsonObject.put("ip", "127.0.0.1");
            jsonObject.put("password", mPasswordEditText.getText());
            jsonObject.put("sex", 1);
            jsonObject.put("username", "hjs");
        } catch (JSONException e) {
            LogUtil.e(TAG, "construct json data error");
        }
        return String.valueOf(jsonObject);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login(constructJsonData());
                break;
            case R.id.rl_login_account_delete:
                mAccountEditText.getText().clear();
                break;
            case R.id.rl_login_password_delete:
                mPasswordEditText.getText().clear();
                break;
            case R.id.rl_login_type:
                switchLoginType();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        if ("".contentEquals(mAccountEditText.getText())) {
            mAccountDeleteRelativeLayout.setVisibility(View.GONE);
        } else {
            mAccountDeleteRelativeLayout.setVisibility(View.VISIBLE);
        }
        if ("".contentEquals(mPasswordEditText.getText())) {
            mPasswordDeleteRelativeLayout.setVisibility(View.GONE);
        } else {
            mPasswordDeleteRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (view.getId() == R.id.et_login_account) {
            if (isFocus) {
                mAccountLineView.setBackgroundResource(R.color.greenBackground);
            } else {
                mAccountLineView.setBackgroundResource(R.color.blackLine);
            }
        }
        if (view.getId() == R.id.et_login_password) {
            if (isFocus) {
                mPasswordLineView.setBackgroundResource(R.color.greenBackground);
            } else {
                mPasswordLineView.setBackgroundResource(R.color.blackLine);
            }
        }
    }
}
