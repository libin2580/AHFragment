package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class UserRegister extends AppCompatActivity {
private EditText location,email,password,confirm_password,name,phone,username;
private String str_username="username",str_location,str_email,str_password,str_cnf_password,str_name="name",str_phone;
    TextView reg_button_txt_view;
    int error_status=0;
    ProgressDialog pDlog;
    public MKLoader progress_loader;
    public  LinearLayout progress_loader_layout;
    LinearLayout register_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_register);
        getSupportActionBar().hide();

        Typeface myFont1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Regular.ttf");


        username=(EditText)findViewById(R.id.reg_username);
        username.setTypeface(myFont1);

        location=(EditText)findViewById(R.id.reg_location);
        location.setTypeface(myFont1);

        email=(EditText)findViewById(R.id.reg_email);
        email.setTypeface(myFont1);

        progress_loader_layout=(LinearLayout)findViewById(R.id.progress_loader_layout);
        progress_loader=(MKLoader)findViewById(R.id.progress_loader);
       /* name=(EditText)findViewById(R.id.reg_name);
        name.setTypeface(myFont1);*/

        phone=(EditText)findViewById(R.id.reg_phone);
        phone.setTypeface(myFont1);

        password=(EditText)findViewById(R.id.reg_password);
        password.setTypeface(myFont1);

        confirm_password=(EditText)findViewById(R.id.reg_cnf_password);
        confirm_password.setTypeface(myFont1);

        reg_button_txt_view=(TextView)findViewById(R.id.reg_button_txt_view);
        reg_button_txt_view.setTypeface(myFont1);

        register_button=(LinearLayout)findViewById(R.id.user_register_button);

        username.requestFocus();


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_username = username.getText().toString();
                str_location = location.getText().toString();
                str_email = email.getText().toString();
                str_password = password.getText().toString();
                str_cnf_password = confirm_password.getText().toString();
                /*str_name=name.getText().toString();*/
                str_phone=phone.getText().toString();
                String regexStr ="^[+]?[0-9]{10,13}$";

                if(str_username.isEmpty()&&str_location.isEmpty()&&str_email.isEmpty()&&str_password.isEmpty()&&str_cnf_password.isEmpty()&&/*str_name.isEmpty()&&*/str_phone.isEmpty()) {
                    username.setError("username can't be empty");
                    location.setError("location can't be empty");
                    email.setError("email can't be empty");
                    password.setError("password can't be empty");
                    confirm_password.setError("confirm your password");
              /*      name.setError("name can't be empty");*/
                    phone.setError("phone number can't be empty");
                    error_status = 0;
                  /*  name.requestFocus();*/


                }else {

                        if (str_username.isEmpty()) {
                            username.setError("username can't be empty");

                            error_status = 0;

                        }
                   /* if (str_name.isEmpty()) {
                        name.setError("name can't be empty");

                        error_status = 0;
                    }*/
                    if (str_phone.isEmpty()) {
                        phone.setError("phone number can't be empty");

                        error_status = 0;
                    }
                        if (str_location.isEmpty()) {
                            location.setError("location can't be empty");

                            error_status = 0;
                        }
                        if (str_email.isEmpty()) {
                            email.setError("email can't be empty");

                            error_status = 0;
                        }
                        if (str_password.isEmpty()) {
                            password.setError("password can't be empty");

                            error_status = 0;
                        }
                        if (str_cnf_password.isEmpty()) {
                            confirm_password.setError("confirm your password");

                            error_status = 0;
                        }

                }

                if(!str_username.isEmpty()&&!str_location.isEmpty()&&!str_email.isEmpty()&&!str_password.isEmpty()&&!str_cnf_password.isEmpty()/*&&!str_name.isEmpty()*/&&!str_phone.isEmpty()){

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_email.trim()).matches()) {
                        email.setError("invalid email");
                        email.requestFocus();
                        error_status=0;
                    }
                    else if (str_password.length()<6 || str_password.length()>15) {
                        password.setError("password must contain minimum 6 and maximum 15 character");

                        error_status = 0;
                    }
                    else if (str_cnf_password.length()<6 || str_cnf_password.length()>15) {
                        confirm_password.setError("confirm password must contain minimum 6 and maximum 15 character");

                        error_status = 0;
                    }

                    else if(!str_password.equals(str_cnf_password)){
                        password.setError("passwords does not match");
                        password.setText("");
                        confirm_password.setText("");
                        password.requestFocus();
                        error_status=0;
                    }
                    else if (!str_phone.matches(regexStr)) {
                        phone.setError("invalid phone number");
                        phone.requestFocus();
                        error_status=0;
                    }
                    else
                    {
                        error_status=1;
                    }


                }

                if(error_status==1)
                {
                    /*Toast.makeText(getApplicationContext(),"No Error",Toast.LENGTH_LONG).show();*/

/* CHECKING INTERNET CONNECTION -- START*/
                    ConnectivityManager cm =
                            (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
/* CHECKING INTERNET CONNECTION -- END*/

                    if(isConnected) {
                        new SendToServer().execute();
                    }
                    else{
                       /* Toast.makeText(getApplicationContext(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
                        Toasty.info(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG, true).show();
                    }

                }

            }
        });








    }
    class SendToServer extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog=new ProgressDialog(UserRegister.this,R.style.MyTheme);

            pDlog.setCancelable(false);
            pDlog.show();*/
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress_loader_layout.setVisibility(View.VISIBLE);
            progress_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/registration.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("name",str_name);
                postDataParams.put("phone",str_phone);
                postDataParams.put("username",str_username);
                postDataParams.put("location",str_location);
                postDataParams.put("email",str_email);
                postDataParams.put("password",str_cnf_password);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*if(pDlog.isShowing())
                pDlog.cancel();*/
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress_loader_layout.setVisibility(View.GONE);
            progress_loader.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){

                /*Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();*/

                Toasty.success(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG, true).show();

                finish();

            }
            else if(result.contentEquals("\"already registerd\""))
            {
                /*Toast.makeText(getApplicationContext(),"Alraedy Registered",Toast.LENGTH_LONG).show();*/
                Toasty.info(getApplicationContext(), "Alraedy Registered", Toast.LENGTH_LONG, true).show();

            }
            else{
               /* Toast.makeText(getApplicationContext(),"Please try again.",Toast.LENGTH_LONG).show();*/
                Toasty.error(getApplicationContext(), "Something went wrong.Please try again", Toast.LENGTH_LONG, true).show();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
