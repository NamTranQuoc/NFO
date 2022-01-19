package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.RegularExpressions;

public class InformationActivity extends AppCompatActivity {

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    ImageView imgAvatar, cancel;
    UserDTO user;
    EditText phoneNumber, email, name, dOB;
    RadioButton male, female;
    Button save;
    RadioGroup gender;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        phoneNumber = (EditText) findViewById(R.id.txtPhone);
        email = (EditText) findViewById(R.id.txtEmail);
        name = (EditText) findViewById(R.id.txtName);
        male = (RadioButton) findViewById(R.id.rbBoy);
        female = (RadioButton) findViewById(R.id.rbGirl);
        dOB = (EditText) findViewById(R.id.txtDOB);
        save = (Button) findViewById(R.id.btnSave);
        gender = (RadioGroup) findViewById(R.id.rgGender);
        cancel = (ImageView) findViewById(R.id.btnCancel);

        user = UserDAO.getInstance().getSharedPreferences(InformationActivity.this);
        sharedPreferences = getSharedPreferences("user", SignInActivity.MODE_PRIVATE);

        setData();

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (RegularExpressions.isDate(dOB.getText().toString())) {
                        user.setName(name.getText().toString());
                        int iGender = gender.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) findViewById(iGender);
                        if (radioButton.getText().toString().equals("Nam")) {
                            user.setGender(true);
                        } else {
                            user.setGender(false);
                        }
                        user.setdOB(FormatUtils.StringToTimestamp(dOB.getText().toString().trim()));
                        BitmapDrawable drawable = (BitmapDrawable) imgAvatar.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        user.setAvatar(Base64Utils.bitmapToString(bitmap));
                        if (!phoneNumber.getText().toString().equals(user.getPhoneNumber())) {
                            if (UserDAO.getInstance().existUserName(phoneNumber.getText().toString(), InformationActivity.this)) {
                                throw new Exception("Số điện thoại đã được sử dụng");
                            } else {
                                user.setPhoneNumber(phoneNumber.getText().toString());
                            }
                        }
                        if (UserDAO.getInstance().update(user, InformationActivity.this) != 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user", new Gson().toJson(user));
                            editor.commit();
                            Toast.makeText(InformationActivity.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();

                        } else {
                            throw new Exception("Cập nhập thất bại");
                        }
                    } else {
                        throw new Exception("Ngày sinh: dd-MM-yyyy");
                    }
                } catch (Exception t) {
                    Toast.makeText(InformationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        phoneNumber.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        name.setText(user.getName());
        if (user.getGender()) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        dOB.setText(FormatUtils.TimestampToString(user.getdOB()));
        imgAvatar.setImageBitmap(Base64Utils.stringToBitmap(user.getAvatar()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgAvatar.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Hủy hành động", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }
}