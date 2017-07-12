package com.philong.wifichua.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.philong.wifichua.R;

import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtEmailDangKy;
    private EditText edtMatKhauDangKy;
    private EditText edtNhapLaiMatKhauDangKy;
    private Button btnDangKy;
    //Firebase
    private FirebaseAuth mAuth;
    //Google accout
    private GoogleSignInOptions mGso;



    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DangKyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        //init view
        edtEmailDangKy = (EditText) findViewById(R.id.edtEmailDangKy);
        edtMatKhauDangKy = (EditText) findViewById(R.id.edtMatKhauDangKy);
        edtNhapLaiMatKhauDangKy = (EditText) findViewById(R.id.edtNhapLaiatKhauDangKy);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //Google

        //dang ky
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailDangKy.getText().toString();
                String matKhau = edtMatKhauDangKy.getText().toString();
                String nhapLaiMatKhau = edtNhapLaiMatKhauDangKy.getText().toString();
                if(checkEmail(email) && checkMatKhau(matKhau, nhapLaiMatKhau)){
                    createAccount(email, matKhau);
                }else{
                    Toast.makeText(DangKyActivity.this, "Lỗi đăng ký", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createAccount(final String email, final String matkhau){
        mAuth.createUserWithEmailAndPassword(email, matkhau)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(DangNhapActivity.newIntent(DangKyActivity.this, email, matkhau));
                        }else{
                            Toast.makeText(DangKyActivity.this, "Dăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private boolean checkEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean checkMatKhau(String matKhau, String nhapLaiMatKhau){
        if(matKhau.equalsIgnoreCase(nhapLaiMatKhau)) {
            return true;
        }
        return false;
    }
}
