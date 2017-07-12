package com.philong.wifichua.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.philong.wifichua.R;

public class DangNhapActivity extends AppCompatActivity {

    private EditText edtEmailDangNhap;
    private EditText edtMatKhauDangNhap;
    private Button btnDangNhap;
    private TextView txtDangKy;
    private TextView txtQuenMatKhau;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String email;
    private String matKhau;


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DangNhapActivity.class);
        return intent;
    }

    public static Intent newIntent(Context context, String email, String matKhau){
        Intent intent = new Intent(context, DangNhapActivity.class);
        intent.putExtra("Email", email);
        intent.putExtra("MatKhau", matKhau);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            startActivity(MainActivity.newIntent(DangNhapActivity.this));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        //init view
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        edtEmailDangNhap = (EditText) findViewById(R.id.edtEmailDangNhap);
        edtMatKhauDangNhap = (EditText) findViewById(R.id.edtMatKhauDangNhap);
        txtDangKy = (TextView) findViewById(R.id.txtDangKy);
        txtQuenMatKhau = (TextView) findViewById(R.id.txtQuenMatKhau);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        //dang ky
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DangKyActivity.newIntent(DangNhapActivity.this));
            }
        });
        //get email, matkhau from dangkyactivity
        if(getIntent() != null){
            email = getIntent().getStringExtra("Email");
            matKhau = getIntent().getStringExtra("MatKhau");
            edtEmailDangNhap.setText(email);
            edtMatKhauDangNhap.setText(matKhau);
        }
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmailDangNhap.getText().toString();
                matKhau = edtMatKhauDangNhap.getText().toString();
                signInWithEmailAndPass(email, matKhau);
            }
        });

    }

    private void signInWithEmailAndPass(String email, String matKhau){
        mAuth.signInWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.newIntent(DangNhapActivity.this));
                            finish();
                        }else{
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
