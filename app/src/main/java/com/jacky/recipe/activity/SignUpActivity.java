package com.jacky.recipe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.jacky.recipe.R;
import com.jacky.recipe.Retrofit.MyService;
import com.jacky.recipe.Retrofit.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
//import rx.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {




    TextView msignup;
    EditText memail, mpassword;
    private TextView mText;
    Button mlogin;

    CompositeDisposable compositeDisposable  = new CompositeDisposable();
    MyService myService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mText = findViewById(R.id.text);


        //Init Service

        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);

        //Init view
        memail = (EditText) findViewById(R.id.email);
        mpassword = (EditText) findViewById(R.id.password);
        mlogin  = (Button) findViewById(R.id.login);






        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(memail .getText().toString(),
                        mpassword.getText().toString());
            }
        });


        msignup = (TextView) findViewById(R.id.signup);
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View lyt_register = LayoutInflater.from(SignUpActivity.this)
                        .inflate(R.layout.lyt_register, null);

                new MaterialStyledDialog.Builder(SignUpActivity.this) //set the whole signup to pop up
                        .setIcon(R.drawable.person)
                        .setTitle("Registration")
                        .setDescription("Please fill to register")
                        .setCustomView(lyt_register)

                        .setNegativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Register")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                EditText name = (EditText)lyt_register.findViewById(R.id.name);
                                EditText email = (EditText)lyt_register.findViewById(R.id.email);
                                EditText password = (EditText)lyt_register.findViewById(R.id.password);
                                EditText password2 = (EditText)lyt_register.findViewById(R.id.confirmPassword);



                                registerUser(name.getText().toString(),
                                        email.getText().toString(),
                                        password.getText().toString(), password2.getText().toString());



                            }
                        }).show();
            }
        });

    }

    private void registerUser( String name,String email, String password, String password2) {

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password2)){
            Toast.makeText(this, "Confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(myService.registerUser(name, email, password, password2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(SignUpActivity.this, "" +response, Toast.LENGTH_SHORT).show();

                        String logged = "{\"message\":\"Welcome!\"}";


                        if(logged.equals(response)){
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(SignUpActivity.this, "Email already registered or mismatched passwords", Toast.LENGTH_SHORT).show();
                        }

                    }
                }));
    }


    private void loginUser(String email, String password) {

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this, "Email cannot be null!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "Password cannot be null!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                compositeDisposable.add(myService.loginUser(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                Toast.makeText(SignUpActivity.this, "" +response, Toast.LENGTH_SHORT).show();
                                String logged = "{\"message\":\"Successful\"}";


                                if(logged.equals(response)){
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                        ));





            }

}
