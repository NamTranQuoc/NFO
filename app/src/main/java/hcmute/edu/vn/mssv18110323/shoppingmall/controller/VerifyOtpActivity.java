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

public class VerifyOtpActivity extends AppCompatActivity {

    UserDTO userDTO;
    TextView reSent, back;
    EditText otp;
    Button verify;
    boolean register; //true chuyển đến từ đăng ký, false chuyển đến từ quên mật khẩu
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        //mapping
        reSent = (TextView) findViewById(R.id.lbReSent);
        back = (TextView) findViewById(R.id.lbBack);
        otp = (EditText) findViewById(R.id.txtOTP);
        verify = (Button) findViewById(R.id.btnVerify);
        sharedPreferences = getSharedPreferences("user", SignInActivity.MODE_PRIVATE);

        //get value
        userDTO = (UserDTO) getIntent().getSerializableExtra("user");
        register = (boolean) getIntent().getSerializableExtra("type");

        //even
        reSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserDAO.getInstance().reSentOTP(userDTO, VerifyOtpActivity.this)) {
                    Toast.makeText(VerifyOtpActivity.this, "Đã gửi lại mã", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Gửi lại mã thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("user", userDTO);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                    intent.putExtra("user", userDTO);
                    startActivity(intent);
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText() != null || !otp.getText().toString().equals("")) {
                    UserDTO userDTO1 = UserDAO.getInstance().checkOTP(otp.getText().toString(), userDTO.getUserId(), VerifyOtpActivity.this);
                    if (userDTO1 != null) {
                        if (register) {
                            Toast.makeText(VerifyOtpActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            editor.putString("user", new Gson().toJson(userDTO));
                            editor.commit();
                            startActivity(intent);
                        } else {
                            Toast.makeText(VerifyOtpActivity.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user", new Gson().toJson(userDTO));
                            editor.commit();
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(VerifyOtpActivity.this, "Mã xác thực không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Vui lòng điền OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}