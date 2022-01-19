package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText pass, passAgain;
    Button save;
    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //mapping
        pass = (EditText) findViewById(R.id.txtPassword);
        passAgain = (EditText) findViewById(R.id.txtPasswordAgain);
        save = (Button) findViewById(R.id.btnSave);

        //get value
        userDTO = UserDAO.getInstance().getSharedPreferences(ChangePasswordActivity.this);

        //even
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText() != null && !pass.getText().toString().equals("") &&
                        passAgain.getText() != null && !passAgain.getText().toString().equals("")) {
                    if (passAgain.getText().toString().equals(pass.getText().toString())) {
                        if (UserDAO.getInstance().updatePassword(userDTO.getUserId(), pass.getText().toString(), ChangePasswordActivity.this)) {
                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.putExtra("user", userDTO);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Nhập lại mật khẩu trùng với mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}