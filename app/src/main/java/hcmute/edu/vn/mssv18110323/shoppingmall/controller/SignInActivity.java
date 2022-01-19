package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;

public class SignInActivity extends AppCompatActivity {

    EditText username, password;
    TextView forgetPassword, register;
    Button login;
    UserDTO userDTO;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //mapping
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        forgetPassword = (TextView) findViewById(R.id.lbForgetPassword);
        register = (TextView) findViewById(R.id.lbRegister);
        login = (Button) findViewById(R.id.btnSignIn);

        sharedPreferences = getSharedPreferences("user", SignInActivity.MODE_PRIVATE);

        this.SignInSession();

        //even
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText() != null && !username.getText().toString().equals("") &&
                        password.getText() != null && !password.getText().toString().equals("")) {
                    userDTO = UserDAO.getInstance().signIn(username.getText().toString(), password.getText().toString(), SignInActivity.this);
                    if (userDTO != null) {
                        if (userDTO.getUserType().equals(Const.user_type_admin)) {
                            Toast.makeText(SignInActivity.this, "Đăng nhập với admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProductManagerActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            editor.putString("user", new Gson().toJson(userDTO));
                            editor.commit();
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SignInSession() {
        String sUser = sharedPreferences.getString("user", "");
        try {
            userDTO = new Gson().fromJson(sUser, UserDTO.class);
        } catch (Exception e) {
            userDTO = null;
        }

        if (userDTO != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}