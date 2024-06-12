package com.project.project.acommon;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.data.Users;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etUsernmae;


    @BindView(R.id.button1)
    Button btnSubmit;

    private Unbinder unbinderknife;

    public static String name = "", username, password, phone, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        unbinderknife = ButterKnife.bind(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsernmae.getText().toString();

                if (username.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter username");
                } else {

                    String tablename = "Users";
//                    String tablename = "Faculty";
                    /*if (HomeActivity.registerActivity.getSimpleName().contains("Hospital")) {
                        tablename = Hospital.class.getSimpleName();
                    } else {
                        tablename = Users.class.getSimpleName();
                    }*/


//                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Users.class.getSimpleName());
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(tablename);
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {

                                if (objects.size() > 0) {
                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(objects.get(0).getString(Users.phone), null, "Your password: " + objects.get(0).getString(Users.password), null, null);
                                    Utils.showToast(getApplicationContext(), "Password  sent to register phone number. " +objects.get(0).getString(Users.phone));
                                } else {
                                    Utils.showToast(getApplicationContext(), "Invalid username");
                                }


                            } else {
                                e.printStackTrace();
                                Utils.showToast(getApplicationContext(), "fetch username error: " + e.getMessage());
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }
}
