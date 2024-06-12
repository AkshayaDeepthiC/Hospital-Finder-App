package com.project.project.acommon;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.data.Users;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etUsernmae;

    @BindView(R.id.editText2)
    EditText etPassword;

    @BindView(R.id.editText3)
    EditText etName;

    @BindView(R.id.editText4)
    EditText etPhone;

    @BindView(R.id.editText5)
    EditText etAddress;

    @BindView(R.id.editText7)
    EditText etLocality;

    @BindView(R.id.editText8)
    EditText etBloodGroup;

    @BindView(R.id.editText6)
    EditText etDepartment;

    @BindView(R.id.button2)
    Button btnSubmit;

    @BindView(R.id.ivpwdhideshow)
    ImageView ivPassword;

    boolean isHide = true;

    private Unbinder unbinderknife;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        unbinderknife = ButterKnife.bind(this);

//        etLocality.setHint("Enter sem");
        etDepartment.setVisibility(View.GONE);
//        etDepartment.setText("cse");
        etLocality.setVisibility(View.VISIBLE);
        etBloodGroup.setVisibility(View.GONE);
        etLocality.setVisibility(View.GONE);

        ivPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHide) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPassword.setImageResource(R.drawable.showpassword);
                    isHide = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPassword.setImageResource(R.drawable.hidepassword);
                    isHide = true;
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = etUsernmae.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                final String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                String locality = etLocality.getText().toString();
                String bloodGroup = etBloodGroup.getText().toString();
                String department = etDepartment.getText().toString();

                if (username.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter username");
                } else if (password.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter password");
                } else if (password.length() < 6) {
                    Utils.showToast(getApplicationContext(), "Password must be 6 characters");
                } else if (name.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter name");
                } else if (phone.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter phone");
                } else if (address.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter address");
                } else {
                    if (phone.length() != 10) {
                        Utils.showToast(getApplicationContext(), "Invalid phone");
                    } else {


                        ParseQuery<ParseObject> query = ParseQuery.getQuery(Users.class.getSimpleName());
                        query.whereEqualTo(Users.username, username);

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    if (objects.size() > 0) {
                                        Utils.showToast(getApplicationContext(), "Username exists.");
                                    } else {


                                        ParseObject parseObject = new ParseObject(Users.class.getSimpleName());
                                        parseObject.put(Users.username, username);
                                        parseObject.put(Users.password, password);
                                        parseObject.put(Users.name, name);
                                        parseObject.put(Users.phone, phone);
                                        parseObject.put(Users.address, address);
                                        parseObject.put("vehicleNo", locality);
                                        parseObject.put(Users.amount, 5000);

                                        parseObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Utils.showToast(getApplicationContext(), "User added successfully");
                                                    Utils.sendSMS(phone, "Welcome to Smart Digital library\n" +
                                                            "\n" +
                                                            "\"Nothing is pleasanter than exploring a library\"");
                                                    finish();
                                                } else {
                                                    e.printStackTrace();
                                                    Utils.showToast(getApplicationContext(), "User add error: " + e.getMessage());
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    e.printStackTrace();
                                    Utils.showToast(getApplicationContext(), "fetch username error: " + e.getMessage());
                                }
                            }
                        });


                    }
                }
            }
        });
    }


    public boolean validDateDOB(String dob) {


        if (dob.equals("")) {
            Utils.showToast(getApplicationContext(), "DOB should not be empty");
            return false;
        }

        String[] temp = dob.split("\\-");
        if (temp.length > 3) {
            Utils.showToast(getApplicationContext(), "Invalid dob entered");
            return false;
        } else if (Integer.parseInt(temp[0]) > 31) {
            Utils.showToast(getApplicationContext(), "Invalid date");
            return false;
        } else if (Integer.parseInt(temp[1]) > 12) {
            Utils.showToast(getApplicationContext(), "Invalid Month");
            return false;
        } else if (temp[2].length() < 4 && Integer.parseInt(temp[2]) > 2020) {
            Utils.showToast(getApplicationContext(), "Invalid year");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }
}
