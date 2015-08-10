package com.vann.myhome.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.util.DialogUtil;

/** 登录窗口
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtUser;
    private EditText edtPassword;
    /**
     * 保存密码
     */
    private CheckBox savePwd;
    /**
     * 设置
     */
    private ImageButton imgbtnSetting;

    private Button login;

    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    /**
     * 记住密码
     */
    private boolean isRemeber;

    private static final String CONFIG = "config";
    private static final String USER = "userName";
    private static final String PASSWORD = "password";
    private static final String REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initVariable();
        initView();
        initListener();
    }

    private void initVariable() {
        pref = getSharedPreferences(CONFIG, MODE_PRIVATE);
        edit = pref.edit();
        isRemeber = pref.getBoolean(REMEMBER, false);
    }

    private void initView() {
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        savePwd = (CheckBox) findViewById(R.id.ckbSavePwd);
        imgbtnSetting = (ImageButton) findViewById(R.id.imgbtnSetting);
        login = (Button) findViewById(R.id.btnLogin);
        edtUser.setText(pref.getString(USER, "admin"));
        if (isRemeber) {
            edtPassword.setText(pref.getString(PASSWORD, ""));
            savePwd.setChecked(true);
        } else {
            savePwd.setChecked(false);
        }
    }

    private void initListener() {
        login.setOnClickListener(this);
        imgbtnSetting.setOnClickListener(this);
        savePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                savePwd.setChecked(isChecked);
                isRemeber = isChecked;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.imgbtnSetting:
                config();
                break;
        }
    }

    private void login() {
        String user = "";
        String pwd = "";
        if (edtUser.getText() != null) {
            user = edtUser.getText().toString();
        }
        if (edtPassword.getText() != null) {
            pwd = edtPassword.getText().toString();
        }
        if (checkPwd(user, pwd)) {
            saveConfig(edtUser.getText().toString(), edtPassword.getText()
                    .toString(), isRemeber);
            openActivity(MainActivity.class);

        } else {
            showMsg("账户或密码错误！");
        }
    }

    @SuppressWarnings("static-access")
    private void config() {
        final View view = getLayoutInflater().from(this).inflate(
                R.layout.login_config, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("设置");
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText user = (EditText) view
                                .findViewById(R.id.edt_userNameEdit);
                        EditText oldPwd = (EditText) view
                                .findViewById(R.id.edt_oldPwd);
                        EditText newPwd = (EditText) view
                                .findViewById(R.id.edt_newPwd);
                        String userName = "";
                        String oldPassword = "";
                        String newPassword = "";
                        if (user.getText() != null) {
                            userName = user.getText().toString();
                        }
                        if (oldPwd.getText() != null) {
                            oldPassword = oldPwd.getText().toString();
                        }
                        if (newPwd.getText() != null) {
                            newPassword = newPwd.getText().toString();
                        }
                        if (checkPwd(userName, oldPassword)) {
                            saveConfig(userName, newPassword,
                                    pref.getBoolean(REMEMBER, false));
                        } else {
                            DialogUtil.createWarnDialog(LoginActivity.this,
                                    "旧密码错误不能更改！");
                            return;
                        }

                    }
                });
        dialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    /**
     * 保存账号密码
     *
     * @param useName
     * @param pwd
     */
    private void saveConfig(String useName, String pwd, boolean isRemeber) {
        edit.putString(USER, useName);
        edit.putString(PASSWORD, pwd);
        edit.putBoolean(REMEMBER, isRemeber);
        edit.commit();
    }

    /**
     * 校验密码是否正确
     *
     * @param userInput
     * @param pwdInput
     * @return
     */
    private boolean checkPwd(String userInput, String pwdInput) {
        String userName = pref.getString(USER, "admin");
        String password = pref.getString(PASSWORD, "admin");
        if (TextUtils.equals(userName, userInput)
                && TextUtils.equals(password, pwdInput)) {
            return true;
        }
        return false;
    }
}
