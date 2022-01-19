package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextView lbSignIn;
    Button verify;
    EditText userName;
    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //mapping
        lbSignIn = (TextView) findViewById(R.id.lbBack);
        verify = (Button) findViewById(R.id.btnVerify);
        userName = (EditText) findViewById(R.id.txtUsername);

        //get value
        try {
            userDTO = (UserDTO) getIntent().getSerializableExtra("user");
            userName.setText(userDTO.getEmail());
        } catch (Exception e) {
            userDTO = new UserDTO();
        }

        //even
        lbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText() != null && !userName.getText().toString().equals("")) {
                    userDTO = UserDAO.getInstance().SentOTP(userName.getText().toString(), ForgetPasswordActivity.this);
                    if (userDTO != null) {
                        Toast.makeText(ForgetPasswordActivity.this, "Đã gửi OTP xác thực đến email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                        intent.putExtra("user", userDTO);
                        intent.putExtra("type", false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "Email hoặc số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Email hoặc số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}