package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.RegularExpressions;

public class RegisterActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView lbSignIn;
    EditText name, phoneNumber, email, password;
    Button register;
    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mapping
        btnClose = (ImageView) findViewById(R.id.btnClose);
        lbSignIn = (TextView) findViewById(R.id.lbSignIn);
        name = (EditText) findViewById(R.id.txtName);
        phoneNumber = (EditText) findViewById(R.id.txtPhone);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        register = (Button) findViewById(R.id.btnRegister);

        //reload
        try {
            userDTO = (UserDTO) getIntent().getSerializableExtra("user");
            name.setText(userDTO.getName());
            phoneNumber.setText(userDTO.getPhoneNumber());
            email.setText(userDTO.getEmail());
            password.setText(userDTO.getPassword());
        } catch (Exception e) {
            userDTO = new UserDTO();
        }

        //even
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        lbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (name.getText() != null && !name.getText().toString().equals("") &&
                            phoneNumber.getText() != null && !phoneNumber.getText().toString().equals("") &&
                            email.getText() != null && !email.getText().toString().equals("") &&
                            password.getText() != null && !password.getText().toString().equals("")) {

                        userDTO.setName(name.getText().toString());
                        if (!RegularExpressions.isPhoneNumber(phoneNumber.getText().toString())) {
                            throw new Exception("S??? ??i???n tho???i kh??ng ????ng ?????nh d???ng (10 s???)");
                        }
                        userDTO.setPhoneNumber(phoneNumber.getText().toString());
                        if (!RegularExpressions.isEmail(email.getText().toString())) {
                            throw new Exception("Email kh??ng ????ng ?????nh d???ng");
                        }
                        userDTO.setEmail(email.getText().toString());
                        userDTO.setPassword(password.getText().toString());
                        if (UserDAO.getInstance().existUserName(email.getText().toString(), phoneNumber.getText().toString(), RegisterActivity.this)) {
                            throw new Exception("Email ho???c s??? ??i???n tho???i ???? ???????c s??? d???ng");
                        }
                        userDTO = UserDAO.getInstance().sendOTPAndCreateUser(userDTO, RegisterActivity.this);
                        if (userDTO != null) {
                            Toast.makeText(RegisterActivity.this, "???? g???i m?? x??c th???c ?????n email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                            intent.putExtra("user", userDTO);
                            intent.putExtra("type", true);
                            startActivity(intent);
                        } else {
                            throw new Exception("Email ho???c s??? ??i???n tho???i ???? ???????c s??? d???ng");
                        }
                    } else {
                        throw new Exception("Vui l??ng nh???p ??i???n ?????y ????? th??ng tin ????ng k??");
                    }
                } catch (Exception t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}