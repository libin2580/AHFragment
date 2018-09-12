package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONObject;

import java.net.URL;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.permission.CheckAndRequestPermission;
import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    private EditText username, password, forgot_email;
    private LinearLayout login, register, receive_password;
    private TextView forgot_password,skip_login;
    private String usernameTxt, passwordTxt,emailTxt;
    private ProgressDialog pDlog;
    private String jsonId, jsonUserName, jsonName, jsonEmail, jsonLocation, jsonPhone;
    String str_forgot_email="";
    private int loggedIn = 0, textFieldError = 0,status=0;
    CheckAndRequestPermission cp;
    SessionManager sm;
    ImageButton forgot_close;
    TextView forgot_status;

public MKLoader progress_loader;
    public  LinearLayout progress_loader_layout;
    LinearLayout activityLogin;

    private PopupWindow mPopupWindow;
    ProgressBar popup_progressbar;

    TextView login_reg_button_txt_view,login_button_txt_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sm = new SessionManager(getApplicationContext());


        Typeface myFont1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Regular.ttf");

        username = (EditText) findViewById(R.id.username);
        username.setTypeface(myFont1);

        password = (EditText) findViewById(R.id.password);
        password.setTypeface(myFont1);

        progress_loader_layout=(LinearLayout)findViewById(R.id.progress_loader_layout);
        progress_loader=(MKLoader)findViewById(R.id.progress_loader);

        login_button_txt_view=(TextView)findViewById(R.id.login_button_txt_view);
        login_button_txt_view.setTypeface(myFont1);

        login_reg_button_txt_view=(TextView)findViewById(R.id.login_reg_button_txt_view);
        login_reg_button_txt_view.setTypeface(myFont1);


        login = (LinearLayout) findViewById(R.id.login_button);
        register = (LinearLayout) findViewById(R.id.register_button);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        skip_login=(TextView)findViewById(R.id.skip_login);

        forgot_password.setTypeface(myFont1);
        skip_login.setTypeface(myFont1);


        cp = new CheckAndRequestPermission();
        cp.checkAndRequestPermissions(Login.this);

        activityLogin = (LinearLayout) findViewById(R.id.activity_login);

        LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customView = inflator.inflate(R.layout.popup_forgot_password, null);

try {
    forgot_email = (EditText) customView.findViewById(R.id.forgot_email);
    receive_password = (LinearLayout) customView.findViewById(R.id.receive_password);
    forgot_status = (TextView) customView.findViewById(R.id.forgot_status);
    popup_progressbar=(ProgressBar)customView.findViewById(R.id.popup_progressbar);
}catch (Exception e){
    e.printStackTrace();
}

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameTxt = username.getText().toString();
                passwordTxt = password.getText().toString();


                if (usernameTxt.isEmpty() && passwordTxt.isEmpty()) {
                    username.setError("Enter username / E-mail");
                    password.setError("Enter Password");
                    textFieldError = 0;
                    username.requestFocus();
                } else {

                    if (usernameTxt.isEmpty()) {
                        username.setError("Enter username / E-mail");
                        textFieldError = 0;
                        username.requestFocus();
                    }
                    if (passwordTxt.isEmpty()) {
                        password.setError("Enter Password");
                        textFieldError = 0;
                        password.requestFocus();
                    }

                }

                if (!usernameTxt.isEmpty() && !passwordTxt.isEmpty()) {
                    if (passwordTxt.length()<6 || passwordTxt.length()>15) {
                        password.setError("password must contain minimum 6 and maximum 15 character");

                        textFieldError = 0;
                    }
                    else {

                        textFieldError = 1;
                    }
                }
                if (textFieldError == 1) {
/* CHECKING INTERNET CONNECTION -- START*/
                    ConnectivityManager cm =
                            (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
/* CHECKING INTERNET CONNECTION -- END*/
                    if (isConnected) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        new UserLogin().execute();
                    } else {
                       /* Toast.makeText(getApplicationContext(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();*/
                        Toasty.info(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG, true).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserRegister.class);

                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
try {
    forgot_email.setText("");
    forgot_status.setVisibility(View.GONE);
    mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    if (Build.VERSION.SDK_INT >= 21) {
        mPopupWindow.setElevation(5.0f);
    }
    mPopupWindow.setFocusable(true);
    mPopupWindow.setAnimationStyle(R.style.popupAnimation);



    forgot_close = (ImageButton) customView.findViewById(R.id.forgot_close);
    forgot_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
        }
    });


    mPopupWindow.showAtLocation(activityLogin, Gravity.CENTER, 0, 0);
}catch (Exception e){
    e.printStackTrace();
}
            }
        });



        skip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), HomeMainActivityNew.class);
                sm.userSkipedLogin();
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        });

        receive_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                str_forgot_email = forgot_email.getText().toString();
                if (str_forgot_email.isEmpty()||str_forgot_email=="") {

                    forgot_status.setText("Enter your e-mail");
                    forgot_status.setVisibility(View.VISIBLE);
                }
                else
                {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_forgot_email.trim()).matches()) {
                        forgot_status.setText("Invalied e-mail");
                        forgot_status.setVisibility(View.VISIBLE);
                    }
                    else {
                        forgot_status.setVisibility(View.GONE);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        new sendPassword().execute();
                    }
                }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    class UserLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog = new ProgressDialog(Login.this, R.style.MyTheme);
            pDlog.setCancelable(false);
            pDlog.show();*/
            progress_loader_layout.setVisibility(View.VISIBLE);
            progress_loader.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url ;

                if(usernameTxt.indexOf("@")>=0){
                    url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/login.php?password=" + passwordTxt+"&email="+usernameTxt);
                }
                else {
                    url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/login.php?username=" + usernameTxt + "&password=" + passwordTxt);

                }


                HttpHandler h = new HttpHandler();
                String jsonString = h.makeServiceCall(url.toString());

                if (jsonString != null) {
                    if (jsonString.equals("\"failed\"")) {
                        loggedIn = 0;

                    } else {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        jsonId = jsonObj.get("id").toString();
                        jsonUserName = jsonObj.get("username").toString();
                        jsonName = jsonObj.get("name").toString();
                        jsonEmail = jsonObj.get("email").toString();
                        jsonLocation = jsonObj.get("location").toString();
                        jsonPhone = jsonObj.get("phone").toString();

                        System.out.println("____________________________________________________________________________");
                        System.out.println("Inside data fetching from url");
                        System.out.println("jsonId:" + jsonId + "\n" +
                                "jsonUserName:" + jsonUserName + "\n" +
                                "jsonName:" + jsonName + "\n" +
                                "jsonEmail:" + jsonEmail + "\n" +
                                "jsonLocation:" + jsonLocation + "\n" +
                                "jsonPhone:" + jsonPhone + "\n");
                        System.out.println("____________________________________________________________________________");
                        loggedIn = 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*if (pDlog.isShowing())
                pDlog.dismiss();*/
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress_loader.setVisibility(View.GONE);
            progress_loader_layout.setVisibility(View.GONE);
            if (loggedIn == 1) {
                sm.CreateLoginSession(jsonId, jsonUserName, jsonName, jsonEmail, jsonLocation, jsonPhone,passwordTxt,"false");
                Intent i = new Intent(getApplicationContext(), HomeMainActivityNew.class);

                finish();
                startActivity(i);
               /* overridePendingTransition(R.anim.enter, R.anim.exit);*/
            } else {
               /* Toast.makeText(getApplicationContext(), "Invalied username or password", Toast.LENGTH_LONG).show();*/

                Toasty.error(getApplicationContext(), "Invalied username or password", Toast.LENGTH_LONG, true).show();
            }
        }
    }

    class sendPassword extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog = new ProgressDialog(Login.this, R.style.MyTheme);
            pDlog.setCancelable(false);

            pDlog.show();*/
            /*progress_loader_layout.setVisibility(View.VISIBLE);
            progress_loader.setVisibility(View.VISIBLE);*/
            popup_progressbar.setVisibility(View.VISIBLE);
            receive_password.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url2 = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/forgotpassword.php?email="+str_forgot_email);

                HttpHandler h = new HttpHandler();
                String jsonString = h.makeServiceCall(url2.toString());


                if (jsonString!= null) {
                    System.out.println("inside jsonstring !=null");
                    System.out.println("jsonstring : "+jsonString);
                    if (jsonString.trim().contentEquals("\"success\"")) {
                        System.out.println("inside success");
                        status=1;
                    }
                    else{
                        System.out.println("inside failed");
                        status=0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if(pDlog.isShowing())
                pDlog.dismiss();*/
           /* progress_loader.setVisibility(View.GONE);
            progress_loader_layout.setVisibility(View.GONE);*/
            popup_progressbar.setVisibility(View.GONE);
            receive_password.setVisibility(View.VISIBLE);
try{
            if(status==1){
                mPopupWindow.dismiss();
                /*Toast.makeText(getApplicationContext(),"Success.Check your email for password.",Toast.LENGTH_LONG).show();*/
                Toasty.success(getApplicationContext(), "Success.Check your email", Toast.LENGTH_LONG, true).show();

            }
            else {
                forgot_status.setVisibility(View.VISIBLE);
                forgot_status.setText("Not a registered e-mail id");
            }
}
catch (Exception e){
    e.printStackTrace();
}
        }
    }
}
