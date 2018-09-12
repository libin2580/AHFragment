package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import es.dmoral.toasty.Toasty;

import static alhamdi.com.cybraum.meridianuae.app.alhameedifragment.HomeMainActivityNew.skiped;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Snackbar snackbar;
    int flag=0;
    int password_correct_status=0;
    String json_password,json_status;
    TextView forgot_document_password_textview,document_forgot_password_status;
    boolean isConnected=false;
    private PopupWindow mPopupWindow,mPopupWindowFgtPass;
    private ProgressDialog pDlog;
    TextView heading,myaccount_camera_txt_view1,myaccount_camera_txt_view2,myaccount_my_documents,myaccount_my_meetings,myaccount_account_settings,myaccount_my_lawyer;
EditText document_password,document_forgot_pass_email;
    LinearLayout unlock_button,forgot_document_password,receive_document_password;
    String str_password="",stored_user_id="",stored_user_email;
    SessionManager sm;
    public static DrawerLayout drawer;
    TextView status;

    ProgressBar popup_progressbar;

    LinearLayout box1,box2,box3,box4,attach_documents;
    private  ImageView my_documents,my_meetings,account_settings,my_lawyer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     View customView,customview_forgot_password;
    ImageButton document_pass_check_close,forgot_document_pass_check_close;
    LinearLayout my_account_layout;
    int check_status=0;

    private OnFragmentInteractionListener mListener;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
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
        View v= inflater.inflate(R.layout.fragment_my_account, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);
        attach_documents=(LinearLayout)v.findViewById(R.id.attach_documents);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        box1=(LinearLayout)v.findViewById(R.id.first_box_my_documents);
        box2=(LinearLayout)v.findViewById(R.id.second_box_my_meetings);
        box3=(LinearLayout)v.findViewById(R.id.third_box_account_settings);
        box4=(LinearLayout)v.findViewById(R.id.fourth_box_my_lawyer);

        my_documents=(ImageView)v.findViewById(R.id.img_my_documents);
        my_meetings=(ImageView)v.findViewById(R.id.img_my_meetings);
        account_settings=(ImageView)v.findViewById(R.id.img_account_settings);
        my_lawyer=(ImageView)v.findViewById(R.id.img_my_lawyer);


        my_documents.setImageResource(R.drawable.my_document);
        my_meetings.setImageResource(R.drawable.mymeeting);
        account_settings.setImageResource(R.drawable.accountsettings);
        my_lawyer.setImageResource(R.drawable.mylawyer);

        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);

        myaccount_camera_txt_view1=(TextView)v.findViewById(R.id.myaccount_camera_txt_view1) ;
        myaccount_camera_txt_view2=(TextView)v.findViewById(R.id.myaccount_camera_txt_view2) ;
        myaccount_my_documents=(TextView)v.findViewById(R.id.myaccount_my_documents) ;
        myaccount_my_meetings=(TextView)v.findViewById(R.id.myaccount_my_meetings) ;
        myaccount_account_settings=(TextView)v.findViewById(R.id.myaccount_account_settings) ;
        myaccount_my_lawyer=(TextView)v.findViewById(R.id.myaccount_my_lawyer) ;

        myaccount_camera_txt_view1.setTypeface(myFont1);
        myaccount_camera_txt_view2.setTypeface(myFont1);
        myaccount_my_documents.setTypeface(myFont1);
        myaccount_my_meetings.setTypeface(myFont1);
        myaccount_account_settings.setTypeface(myFont1);
        myaccount_my_lawyer.setTypeface(myFont1);
/*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        stored_user_email=details.get(sm.KEY_EMAIL);
        skiped=details.get(sm.KEY_IS_SKIPED);

        System.out.println("Email from session : "+stored_user_email);
        /*getting user id end*/

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
         customView = inflator.inflate(R.layout.popup_check_document_password, null);
        try {
            document_password = (EditText) customView.findViewById(R.id.document_password);
            unlock_button = (LinearLayout) customView.findViewById(R.id.unlock_document);
            status = (TextView) customView.findViewById(R.id.document_status);
            forgot_document_password=(LinearLayout)customView.findViewById(R.id.forgot_document_password);
            forgot_document_password_textview=(TextView)customView.findViewById(R.id.forgot_document_password_textview);
            document_password.setTypeface(myFont1);
            status.setTypeface(myFont1);
            forgot_document_password_textview.setTypeface(myFont1);


        }catch (Exception e){
            e.printStackTrace();
        }



        LayoutInflater inflater2=(LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customview_forgot_password=inflater2.inflate(R.layout.popup_forgot_document_password,null);
        try{
            document_forgot_pass_email = (EditText) customview_forgot_password.findViewById(R.id.document_forgot_pass_email);
            receive_document_password = (LinearLayout) customview_forgot_password.findViewById(R.id.receive_document_password);
            document_forgot_password_status = (TextView) customview_forgot_password.findViewById(R.id.document_forgot_password_status);
            popup_progressbar=(ProgressBar)customview_forgot_password.findViewById(R.id.popup_progressbar);
            document_forgot_pass_email.setText(stored_user_email);

            document_forgot_pass_email.setTypeface(myFont1);
            document_forgot_password_status.setTypeface(myFont1);


        }catch (Exception e){
            e.printStackTrace();
        }


        my_account_layout = (LinearLayout) v.findViewById(R.id.my_account_layout);
        attach_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDocumentFragment adf=new AddDocumentFragment();
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,adf).addToBackStack("").commit();

            }
        });





        box1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        my_documents.setImageResource(R.drawable.my_document_red);
                        break;
                    case MotionEvent.ACTION_UP:

                        if(skiped.equalsIgnoreCase("true")){
                            /*Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/
                            new AlertDialog.Builder(getActivity())

                                    .setMessage("You must login to continue")
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            Intent i = new Intent(getActivity(), Login.class);

                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                                            getActivity().finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            new checkLocked().execute();
                        }







                        /*MyDocumentFragment mf=new MyDocumentFragment();
                        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,mf).addToBackStack("").commit();
*/                        my_documents.setImageResource(R.drawable.my_document);
                        break;
                }


                return true;
            }
        });
        box2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        my_meetings.setImageResource(R.drawable.mymeeting_red);

                        break;
                    case MotionEvent.ACTION_UP:
                        if(skiped.equalsIgnoreCase("true")){
                            /*Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/
                            new AlertDialog.Builder(getActivity())

                                    .setMessage("You must login to continue")
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            Intent i = new Intent(getActivity(), Login.class);

                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                                            getActivity().finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            MyMeetingFragment mf = new MyMeetingFragment();
                            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, mf).addToBackStack("").commit();
                        }
                        my_meetings.setImageResource(R.drawable.mymeeting);
                        break;
                }


                return true;
            }
        });


        box3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        account_settings.setImageResource(R.drawable.accountsettings_red);

                        break;
                    case MotionEvent.ACTION_UP:
                        if(skiped.equalsIgnoreCase("true")){
                            /*Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/
                            new AlertDialog.Builder(getActivity())

                                    .setMessage("You must login to continue")
                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            Intent i = new Intent(getActivity(), Login.class);

                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                                            getActivity().finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            AccountSettingsFragment mf = new AccountSettingsFragment();
                            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, mf).addToBackStack("").commit();
                        }
                        account_settings.setImageResource(R.drawable.accountsettings);
                        break;
                }


                return true;
            }
        });

        box4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        my_lawyer.setImageResource(R.drawable.mylawyer_red);

                        break;
                    case MotionEvent.ACTION_UP:
                        MyLawyerFragment ml=new MyLawyerFragment();
                        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,ml).addToBackStack("").commit();

                        my_lawyer.setImageResource(R.drawable.mylawyer);
                        break;
                }


                return true;
            }
        });


        unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    str_password = document_password.getText().toString();
                    if (str_password.isEmpty()||str_password=="") {


                        status.setText("password field can't be empty");
                        status.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                       /* if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_password.trim()).matches()) {
                            status.setText("Invalied e-mail");
                            status.setVisibility(View.VISIBLE);
                        }
                        else {*/
                           status.setVisibility(View.GONE);

                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            str_password=document_password.getText().toString();

                        if(str_password.equals(json_password))
                        {
                            mPopupWindow.dismiss();


                            MyDocumentFragment mf=new MyDocumentFragment();
                            getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,mf).addToBackStack("").commit();

                        }
                        else{
                            status.setVisibility(View.VISIBLE);
                            status.setText("Incorrect Password");
                        }
                       /* }*/
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        forgot_document_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    mPopupWindow.dismiss();
                    displayPopupForgotDocPassword();

            }
        });

        receive_document_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
              /*      str_user_email = document_forgot_pass_email.getText().toString();
                    if (str_user_email.isEmpty()||str_user_email=="") {

                        document_forgot_password_status.setText("Enter your e-mail");
                        document_forgot_password_status.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_user_email.trim()).matches()) {
                            document_forgot_password_status.setText("Invalied E-mail");
                            document_forgot_password_status.setVisibility(View.VISIBLE);
                        }
                        else {
                            document_forgot_password_status.setVisibility(View.GONE);

                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
                    if (ConnectionCheck()) {




                        new sendPassword().execute();




                    } else {

                        /*final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Alert");

                        alertDialog.setMessage("No Internet");


                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                            }
                        });
                        alertDialog.show();*/
                        Toasty.info(getActivity(), "Something went wrong.\nCheck your network connection", Toast.LENGTH_LONG, true).show();



                    }

                 /*       }
                    }*/

                }catch (Exception e){
                    e.printStackTrace();
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
    public void displayPopup()
    {
        try {
            document_password.setText("");
            status.setVisibility(View.GONE);
            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);



            document_pass_check_close = (ImageButton) customView.findViewById(R.id.document_pass_check_close);
            document_pass_check_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                }
            });


            mPopupWindow.showAtLocation(my_account_layout, Gravity.CENTER, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayPopupForgotDocPassword()
    {
        try {
            document_forgot_pass_email.setText(stored_user_email.toString());
            document_forgot_password_status.setVisibility(View.GONE);
            mPopupWindowFgtPass = new PopupWindow(customview_forgot_password, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindowFgtPass.setElevation(5.0f);
            }
            mPopupWindowFgtPass.setFocusable(true);
            mPopupWindowFgtPass.setAnimationStyle(R.style.popupAnimation);



            forgot_document_pass_check_close = (ImageButton) customview_forgot_password.findViewById(R.id.forgot_document_pass_check_close);
            forgot_document_pass_check_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindowFgtPass.dismiss();
                }
            });


            mPopupWindowFgtPass.showAtLocation(my_account_layout, Gravity.CENTER, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*class sendPassword extends AsyncTask<String, String, String> {

        String entered_password;
        public sendPassword(String pwd)
        {
            entered_password=pwd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDlog.setCancelable(false);

            pDlog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/password_protected.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("password",str_password);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 *//* milliseconds *//*);
                conn.setConnectTimeout(15000 *//* milliseconds *//*);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pDlog.isShowing())
                pDlog.dismiss();
            try{
                if(s.contentEquals("\"success\"")){
                    mPopupWindow.dismiss();
                   *//* Toast.makeText(getActivity(),"Password is successfully mailed",Toast.LENGTH_LONG).show();*//*

                    MyDocumentFragment mf=new MyDocumentFragment();
                        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,mf).addToBackStack("").commit();

                }
                else {
                    status.setVisibility(View.VISIBLE);
                    status.setText("Incorrect Password");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

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






    class checkLocked extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag=3;
            pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDlog.setCancelable(false);

            ConnectionCheck();
            if(!isConnected){
                ShowSnackbar();
            }
            else {
                /*pDlog.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/password_protected.php?user_id="+stored_user_id);

            if (jsonString != null) {

                try {
                    JSONArray valueArray = new JSONArray(jsonString);



                        JSONObject jsonData = valueArray.getJSONObject(0);

                       json_password=jsonData.getString("password").toString();
                    json_status=jsonData.getString("status").toString();

                    if(json_status.contentEquals("no"))
                    {
                        flag=0;
                    }
                    if(json_status.contentEquals("yes")) {
                        flag = 1;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                flag=3;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println(" flag : "+flag);


                if(flag==0){
                    MyDocumentFragment mf=new MyDocumentFragment();
                    getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,mf).addToBackStack("").commit();

                }
            if(flag==1)
            {
                displayPopup();
            }
            if(flag==3)
            {
                /*Toast.makeText(getActivity(),"Something went wrong.\nCheck your network connection.",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "Something went wrong.\nCheck your network connection", Toast.LENGTH_LONG, true).show();
            }


           /* if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

        }
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
        snackbar= Snackbar.make(drawer,"No internet connection",Snackbar.LENGTH_INDEFINITE)
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
    class sendPassword extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            password_correct_status=0;
            /*pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDlog.setCancelable(false);

            pDlog.show();*/
            /*HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);*/
            popup_progressbar.setVisibility(View.VISIBLE);
            receive_document_password.setVisibility(View.GONE);


        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url3 = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/doc_forgotpassword.php?email="+stored_user_email);

                HttpHandler h = new HttpHandler();
                String jsonString2 = h.makeServiceCall(url3.toString());


                if (jsonString2!= null) {

                    System.out.println("inside jsonstring2 !=null");

                    System.out.println("jsonstring2 : "+jsonString2);
                    if (jsonString2.trim().contentEquals("\"success\"")||jsonString2.trim()!=null || !jsonString2.isEmpty()) {

                        password_correct_status=1;
                        System.out.println("inside success");
                    }
                    else {

                        password_correct_status=0;
                        System.out.println("inside failed");
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
            /*if(pDlog.isShowing())
                pDlog.dismiss();*/
           /* HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);*/
            popup_progressbar.setVisibility(View.GONE);
            receive_document_password.setVisibility(View.VISIBLE);

            System.out.println("password_correct_status : "+password_correct_status);

                if(password_correct_status==1){
                    mPopupWindowFgtPass.dismiss();
                    /*Toast.makeText(getActivity(),"Password is successfully mailed",Toast.LENGTH_LONG).show();*/
                    Toasty.success(getActivity(), "Password is successfully mailed", Toast.LENGTH_LONG, true).show();
                    new checkLocked().execute();
                }
                else {
                    document_forgot_password_status.setVisibility(View.VISIBLE);
                    document_forgot_password_status.setText("Please try again");
                }

        }
    }

}
