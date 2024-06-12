package com.project.project.acommon;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.data.Users;
import com.project.project.ambulance.HomeActivity;
import com.project.project.ambulance.ViewBookingsActivity;
import com.project.project.ambulance.data.Hospital;
import com.project.project.attendance.data.Faculty;
import com.project.project.foodc.ViewOrdersRiderActivity;
import com.project.project.healthchatbot.ChatBotActivity;
import com.project.project.healthchatbot.MainActivity;
import com.project.project.hospitalfinder.ViewAppointmentsListActivity;
import com.project.project.travelguide.PreferenceHandler;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.extra)
    EditText etExtra;

    @BindView(R.id.editText)
    EditText etUsernmae;

    @BindView(R.id.editText2)
    EditText etPassword;

    @BindView(R.id.button1)
    Button btnLogin;

    @BindView(R.id.button2)
    Button btnRegister;

    @BindView(R.id.forgot)
    Button btnForgotpassword;

    @BindView(R.id.ivpwdhideshow)
    ImageView ivPassword;

    @BindView(R.id.guest)
    Button btnGuest;

    @BindView(R.id.chatnowlogin)
    Button btnChatnow;

    boolean isHide = true;

    private Unbinder unbinderknife;

    public static String name = "", username = "", password,
            phone = "8747858632", address, fatherPhone, type = "", subjects, busNo;

    public static ParseObject userObject;

    public static boolean isHod;

    private String TAG = "LoginActivity";

    public static boolean isAdmin;

    public static int walletAmount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unbinderknife = ButterKnife.bind(this);


        etExtra.setVisibility(View.GONE);
        checkPermissionStatus();
//        Utils.showToast(getApplicationContext(), "Common login");
        if (getString(R.string.app_name).equalsIgnoreCase("Tripinfo")) {
            btnGuest.setVisibility(View.VISIBLE);
        }

        if (getString(R.string.app_name).equalsIgnoreCase("Aushadi")) {
            btnChatnow.setVisibility(View.VISIBLE);
        }

//        btnRegister.setVisibility(View.GONE);
//        btnForgotpassword.setVisibility(View.GONE);
//        btnRegister.setVisibility(View.GONE);

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.username = "guest";
                LoginActivity.name = "guest";

                Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                startActivity(intent);
            }
        });

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

        btnChatnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                openSettings();

                // this is only for ambulance project
//                Intent intent = new Intent(LoginActivity.this, HomeActivity.registerActivity);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // this is common
//                Intent intent = new Intent(LoginActivity.this, StudentRegisterActivity.class);
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsernmae.getText().toString();
                String password = etPassword.getText().toString();
                String busNo = etExtra.getText().toString();

//                username = "daya";
//                password = "123456";

                if (username.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter username");
                } else if (password.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter password");
                } else {

                    if (username.equals("hod") && password.equals("hod")) {
                        Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                        intent.putExtra("hod", true);
                        isHod = true;
                        startActivity(intent);
                        return;
                    }

                    if (username.equals("driver") && password.equals("driver")) {
                        Intent intent = new Intent(LoginActivity.this, com.project.project.bustracking.HomeActivity.class);
                        intent.putExtra("driver", true);
                        LoginActivity.busNo = busNo;
                        startActivity(intent);

                        return;
                    }

                    if (username.equals("admin") && password.equals("admin")) {
                        /*Intent intent = new Intent(LoginActivity.this, MyAccountActivity.class);
                        intent.putExtra("admin", true);
                        isHod = true;
                        startActivity(intent);
*/
                        isAdmin = true;

//                        Intent intent = new Intent(LoginActivity.this, com.project.project.recruitment.HomeActivity.class);
//                        Intent intent = new Intent(LoginActivity.this, ViewBooksActivity.class);
                        Intent intent = new Intent(LoginActivity.this, ViewAppointmentsListActivity.class);
//                        Intent intent = new Intent(LoginActivity.this, AdminHomeCropsActivity.class);
//                        Intent intent = new Intent(LoginActivity.this, ViewProblemsListActivity.class);
                        LoginActivity.username = "admin";
                        intent.putExtra("admin", true);
                        isHod = true;
                        startActivity(intent);

//                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
//                        LoginActivity.username = "admin";
//                        intent.putExtra("admin", true);
//                        isHod = true;
//                        startActivity(intent);
                        return;

                    }

                    if (username.equals("hospital") && password.equals("1234")) {
                        Intent intent = new Intent(LoginActivity.this, ViewBookingsActivity.class);
                        intent.putExtra("hod", true);
                        isHod = true;
                        startActivity(intent);
                        return;
                    }

                    if (username.equals("doctor") && password.equals("doctor")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        return;
                    }

                    if (HomeActivity.registerActivity != null && HomeActivity.registerActivity.getSimpleName().contains("Hospital")) {
                        validateHospitalCredentials(username, password);
                    } else {
                        validateCredentials(username, password);
                    }


                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }

    public void validateCredentials(String username, String password) {

        Log.i(TAG, "Username: " + username + " Password: " + password);

//        ParseQuery<ParseObject> query = ParseQuery.getQuery(Faculty.class.getSimpleName());// attendance project
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Users.class.getSimpleName());
        query.whereEqualTo(Users.username, username);
        query.whereEqualTo(Users.password, password);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        userObject = objects.get(0);
                        LoginActivity.username = userObject.getString("username");
                        LoginActivity.phone = userObject.getString("phone");
                        LoginActivity.name = userObject.getString("name");
                        LoginActivity.walletAmount = userObject.getInt(Users.amount);
                        subjects = userObject.getString(Faculty.subjects);

                        etUsernmae.setText("");
                        etPassword.setText("");
                        if (userObject.getInt(Users.type) == 0) {


                            isAdmin = false;
//                            Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                            PreferenceHandler.init(LoginActivity.this);
                            if (PreferenceHandler.isPaymentDone()) {
                                Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                                intent.putExtra("hod", false);
                                isHod = false;
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                                intent.putExtra("hod", false);
                                isHod = false;
                                startActivity(intent);
                            }


                        } else {
                            Intent intent = new Intent(LoginActivity.this, ViewOrdersRiderActivity.class);
                            intent.putExtra("hod", false);
                            isHod = false;
                            startActivity(intent);
                        }

                    } else {
                        Utils.showToast(getApplicationContext(), "Invalid credentials");
                    }

                } else {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "fetch username error: " + e.getMessage());
                }
            }
        });
    }


    public void validateHospitalCredentials(String username, String password) {

        Log.i(TAG, "Username: " + username + " Password: " + password);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Hospital.class.getSimpleName());
        query.whereEqualTo(Users.username, username);
        query.whereEqualTo(Users.password, password);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {
                        userObject = objects.get(0);
                        LoginActivity.username = userObject.getString("username");
                        LoginActivity.phone = userObject.getString("phone");
                        LoginActivity.name = userObject.getString("name");

//                        subjects = userObject.getString(Faculty.subjects);


                        if (userObject.getInt(Users.type) == 0) {
                            Intent intent = new Intent(LoginActivity.this, ViewBookingsActivity.class);
                            intent.putExtra("hod", false);
                            isHod = false;
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, ViewOrdersRiderActivity.class);
                            intent.putExtra("hod", false);
                            isHod = false;
                            startActivity(intent);
                        }

                    } else {
                        Utils.showToast(getApplicationContext(), "Invalid credentials");
                    }

                } else {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "fetch username error: " + e.getMessage());
                }
            }
        });
    }

    public void saveLogingData() {
        SharedPreferences pref = getSharedPreferences("project", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("login", true);
        editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences("project", MODE_PRIVATE);
        return pref.getBoolean("login", false);
    }


    public void getInfo() {
        WifiManager wifii = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo d = wifii.getDhcpInfo();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public void openSettings() {

//        ACTION_APPLICATION_DETAILS_SETTINGS


//        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.tv.settings");
//        Log.i(TAG, "LaunchIntent: "+launchIntent);
//        startActivity(launchIntent);

//        startActivity(new Intent(Settings.ACTION_SETTINGS));


        try {
            ComponentName name = new ComponentName("com.android.tv.settings",
                    "com.android.tv.settings.MainSettings");


            Intent i = new Intent(Intent.ACTION_MAIN);

            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);

            startActivity(i);

        } catch (Exception e) {
            Log.i(TAG, "inside exception");
            e.printStackTrace();
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }

//        Intent myAppSettings = new Intent(Settings.ACTION, Uri.parse("package:" + "com.android.tv.settings"));
////        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(myAppSettings);
    }


    Integer permissionRequestCode = 1;
    String[] permisionsArray = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE};

    public void checkPermissionStatus() {
        for (String permission : permisionsArray) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
                break;
            }
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permisionsArray, permissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
//                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED) ;
//                Toast.makeText(this, "Microphone Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }


    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    Log.i(TAG, "onActivityResult: called imageuri: " + imageUri.toString());

                    /*getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView) findViewById(R.id.ImageView);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        imageView.setImageBitmap(bitmap);
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }*/
                }
        }
    }


}
