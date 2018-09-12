package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DrawerLayout drawer;

    EditText current_password,new_password,confirm_password;
    LinearLayout sentButton;
    String str_current_password,str_new_password,str_confirm_password;
    int error_status=0;

    private String stored_user_id,stored_current_password;
    SessionManager sm;
TextView sent_button_text;
    ProgressDialog pDlog;

    boolean isConnected=false;
    Snackbar snackbar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChangePassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePassword newInstance(String param1, String param2) {
        ChangePassword fragment = new ChangePassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_change_password, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");



        sent_button_text=(TextView)v.findViewById(R.id.change_password_btn_txt_view);
        sent_button_text.setTypeface(myFont1);








        current_password=(EditText)v.findViewById(R.id.current_password);
        new_password=(EditText)v.findViewById(R.id.new_password);
        new_password.setTypeface(myFont1);
        confirm_password=(EditText)v.findViewById(R.id.confirm_password);
        confirm_password.setTypeface(myFont1);

        sentButton=(LinearLayout)v.findViewById(R.id.change_password_submit_button);

        /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        stored_current_password = details.get(sm.KEY_CUR_PASSWORD);

        System.out.println("________________________________________________");
        System.out.println("Cur Pass : "+stored_current_password);
        System.out.println("________________________________________________");
        /*getting user id end*/


        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_current_password = /*current_password.getText().toString()*/"null";
                str_new_password = new_password.getText().toString();
                str_confirm_password = confirm_password.getText().toString();


                if (/*str_current_password.isEmpty() && */str_new_password.isEmpty() && str_confirm_password.isEmpty()) {
                   /* current_password.setError("current passsword can't be empty");*/
                    new_password.setError("new password can't be empty");
                    confirm_password.setError("confirm password can't be empty");

                    error_status = 0;
                    current_password.requestFocus();


                } else {

                    /*if (str_current_password.isEmpty()) {
                        current_password.setError("current passsword can't be empty");

                        error_status = 0;

                    }*/
                    if (str_new_password.isEmpty()) {
                        new_password.setError("new password can't be empty");

                        error_status = 0;
                    }
                    if (str_confirm_password.isEmpty()) {
                        confirm_password.setError("confirm password can't be empty");

                        error_status = 0;
                    }

           /* if (str_cnf_password.isEmpty()) {
                confirm_password.setError("confirm your password");

                error_status = 0;
            }*/
                }

                if (/*!str_current_password.isEmpty() &&*/ !str_new_password.isEmpty() && !str_confirm_password.isEmpty() ) {

                   /* if (!str_current_password.equals(stored_current_password)) {
                        current_password.setError("Does'nt match");
                        current_password.requestFocus();
                        error_status = 0;
                    }
                    else */
                     if (str_new_password.length()<6 || str_new_password.length()>15) {
                         new_password.setError("password must contain minimum 6 and maximum 15 character");

                        error_status = 0;
                    }
                    else if (str_confirm_password.length()<6 || str_confirm_password.length()>15) {
                        confirm_password.setError("confirm password must contain minimum 6 and maximum 15 character");

                        error_status = 0;
                    }
                    else if(!str_new_password.equals(str_confirm_password))
                    {
                        new_password.setError("new password and confrim password doesn't match");
                        new_password.requestFocus();
                        error_status=0;

                    }
                    else {
                        error_status = 1;
                    }


                }

                if (error_status == 1) {
                    /*Toast.makeText(getApplicationContext(),"No Error",Toast.LENGTH_LONG).show();*/

/* CHECKING INTERNET CONNECTION -- START*/
                    ConnectivityManager cm =
                            (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
/* CHECKING INTERNET CONNECTION -- END*/

                    if (isConnected) {
                        new SendToServer().execute();
                    } else {
                       /* Toast.makeText(getActivity(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();*/

                        Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
                    }

                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class SendToServer extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progress.setVisibility(View.VISIBLE);*/
            /*pDlog = new ProgressDialog(getActivity(),R.style.MyTheme);
            pDlog.setCancelable(false);
            pDlog.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/changepassword.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("c_password",stored_current_password);
                postDataParams.put("new_password",str_new_password);


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
            /*progress.setVisibility(View.GONE);*/
            /*if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){



                SessionManager sm=new SessionManager(getActivity());
                sm.logoutUser();
                /*Toast.makeText(getActivity(),"Successfully Changed",Toast.LENGTH_LONG).show();*/

                Toasty.success(getActivity(), "Successfully Changed", Toast.LENGTH_LONG, true).show();


            }
            else if(result.contentEquals("\"failed\""))
            {
                /*Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();*/
                Toasty.error(getActivity(), "Failed", Toast.LENGTH_LONG, true).show();

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

    /* CHECKING INTERNET CONNECTION -- START*/
    public boolean ConnectionCheck(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }
/* CHECKING INTERNET CONNECTION -- end*/

    public void ShowSnackbar(){
        snackbar=Snackbar.make(drawer,"No internet connection",Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            /* Changing message text color*/
        snackbar.setActionTextColor(Color.parseColor("#FFEC6363"));

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            snackbar.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
