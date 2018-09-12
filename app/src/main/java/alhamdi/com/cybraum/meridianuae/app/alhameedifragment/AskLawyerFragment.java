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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AskLawyerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AskLawyerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskLawyerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DrawerLayout drawer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner cases_spinner;
    ImageView dropDownImage;
    EditText description,specificReason,name,email,phone;
    LinearLayout sentButton;
    String S_description,S_specification,S_name,S_email,S_phone;
    int error_status=0;
    ProgressBar progress;
    String[] case_id;
    ProgressDialog pDlog;
    String[] cases;
    boolean isConnected=false;
    Snackbar snackbar;
    String selected_caseid="";
    SessionManager sm;
    private String stored_user_id;
    TextView cases_txt_view,cases_description_txt_view,cases_specific_reason_txt_view,cases_name_txt_view,cases_email_txt_view,cases_phone_txt_view,heading,cases_sent_btn_txt_view;

    private OnFragmentInteractionListener mListener;

    public AskLawyerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskLawyerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AskLawyerFragment newInstance(String param1, String param2) {
        AskLawyerFragment fragment = new AskLawyerFragment();
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
        View v= inflater.inflate(R.layout.fragment_ask_lawyer, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        cases_txt_view=(TextView)v.findViewById(R.id.cases_txt_view);
        cases_description_txt_view=(TextView)v.findViewById(R.id.description_txt_view);
        cases_specific_reason_txt_view=(TextView)v.findViewById(R.id.specific_reason_txt_view);
        cases_name_txt_view=(TextView)v.findViewById(R.id.cases_name_txt_view);
        cases_email_txt_view=(TextView)v.findViewById(R.id.cases_email_txt_view);
        cases_phone_txt_view=(TextView)v.findViewById(R.id.cases_phone_txt_view);
        cases_sent_btn_txt_view=(TextView)v.findViewById(R.id.cases_sent_btn_txt_view);
        heading=(TextView)v.findViewById(R.id.heading);


        heading.setTypeface(myFont1);
        cases_txt_view.setTypeface(myFont1);
        cases_description_txt_view.setTypeface(myFont1);
        cases_specific_reason_txt_view.setTypeface(myFont1);
        cases_name_txt_view.setTypeface(myFont1);
        cases_email_txt_view.setTypeface(myFont1);
        cases_phone_txt_view.setTypeface(myFont1);
        cases_sent_btn_txt_view.setTypeface(myFont1);






        cases_spinner=(Spinner)v.findViewById(R.id.cases_spinner);
        dropDownImage=(ImageView)v.findViewById(R.id.dropDownImage);
        description=(EditText)v.findViewById(R.id.description);
        specificReason=(EditText)v.findViewById(R.id.specific_reason);
        name=(EditText)v.findViewById(R.id.name);
        email=(EditText)v.findViewById(R.id.email);
        phone=(EditText)v.findViewById(R.id.phone);
        sentButton=(LinearLayout)v.findViewById(R.id.sent_button);

        description.setTypeface(myFont1);
        specificReason.setTypeface(myFont1);
        name.setTypeface(myFont1);
        email.setTypeface(myFont1);
        phone.setTypeface(myFont1);


        /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/

        ConnectionCheck();
        if(isConnected) {
            new FetchCases().execute();
        }
        else{
            ShowSnackbar();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(cases_spinner);


            // Set popupWindow height to 500px
            popupWindow.setHeight(700);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S_description = description.getText().toString();
                S_specification = specificReason.getText().toString();
                S_name = name.getText().toString();
                S_email = email.getText().toString();
                S_phone=phone.getText().toString();
                String regexStr ="^[+]?[0-9]{10,13}$";


                if (S_description.isEmpty() && S_specification.isEmpty() && S_name.isEmpty() && S_email.isEmpty() && S_phone.isEmpty()) {
                    description.setError("description can't be empty");
                    specificReason.setError("specificreason can't be empty");
                    email.setError("email can't be empty");
                    name.setError("name can't be empty");
                    email.setError("email can't be empty");
                    phone.setError("phone number can't be empty");
                    error_status = 0;
                    description.requestFocus();


                } else {

                    if (S_description.isEmpty()) {
                        description.setError("description can't be empty");

                        error_status = 0;

                    }
                    if (S_specification.isEmpty()) {
                        specificReason.setError("specificreason can't be empty");

                        error_status = 0;
                    }
                    if (S_email.isEmpty()) {
                        email.setError("email can't be empty");

                        error_status = 0;
                    }
                    if (S_name.isEmpty()) {
                        name.setError("name can't be empty");

                        error_status = 0;
                    }
                    if (S_phone.isEmpty()) {
                        phone.setError("phone number can't be empty");

                        error_status = 0;
                    }

           /* if (str_cnf_password.isEmpty()) {
                confirm_password.setError("confirm your password");

                error_status = 0;
            }*/
                }

                if (!S_description.isEmpty() && !S_specification.isEmpty() && !S_email.isEmpty() && !S_name.isEmpty() && !S_phone.isEmpty()) {

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(S_email.trim()).matches()) {
                        email.setError("invalid email");
                        email.requestFocus();
                        error_status = 0;
                    }else
                    if (!S_phone.matches(regexStr)) {
                        phone.setError("invalid phone number");
                        phone.requestFocus();
                        error_status=0;
                    }

           /* else if(!str_password.equals(str_cnf_password)){
                password.setError("passwords does not match");
                password.setText("");
                confirm_password.setText("");
                password.requestFocus();
                error_status=0;
            }*/
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
                        new SendToServer1().execute();
                    } else {
                        /*Toast.makeText(getActivity(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();*/
                        Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();

                    }

                }
            }
        });

        try {


            cases_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    String caseid = case_id[pos];
                    selected_caseid = caseid;

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cases_spinner.performClick();
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
    private class SendToServer1 extends AsyncTask<String,String,String> {
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
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/ask_lawyer.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("name",S_name);
                postDataParams.put("email",S_email);
                postDataParams.put("phone",S_phone);
                postDataParams.put("spe_reason",S_specification);
                postDataParams.put("description",S_description);
                postDataParams.put("case_id",selected_caseid);

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
           /* if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.trim().contentEquals("\"success\"")){


                /*Toast.makeText(getActivity(),"Successfully Sent",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(), "Successfully Sent", Toast.LENGTH_LONG, true).show();
                getFragmentManager().popBackStackImmediate();




            }
            else
            {
               /* Toast.makeText(getActivity(),"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "Failed.Try again later", Toast.LENGTH_LONG, true).show();

            }
        }
    }
    class FetchCases extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/allcases.php");
            if (jsonString != null) {
                try {
                    JSONArray imageArray = new JSONArray(jsonString);
                    case_id=new String[imageArray.length()];
                    cases=new String[imageArray.length()];

                    for (int i = 0; i < imageArray.length(); i++) {
                        JSONObject jsonData = imageArray.getJSONObject(i);
                        case_id[i]=jsonData.getString("case_id");
                        cases[i]=jsonData.getString("cases");

                        System.out.println("***************************************************");
                        System.out.println(" case_id : " + jsonData.getString("case_id") + "\n");
                        System.out.println("cases : " + jsonData.getString("cases"));
                        System.out.println("***************************************************");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



try{


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.spinner_text_style, cases);
            dataAdapter.setDropDownViewResource(R.layout.spinner_text_style);
            cases_spinner.setAdapter(dataAdapter);

}catch (Exception e){
    e.printStackTrace();
}



            /*if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

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
                        new FetchCases().execute();
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
