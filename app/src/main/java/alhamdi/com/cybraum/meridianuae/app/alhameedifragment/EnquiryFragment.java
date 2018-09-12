package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnquiryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnquiryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnquiryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int error_status=0;
    private String str_name,str_email,str_phone,str_enquiry;
    EditText enquiry,name,email,phone;
    LinearLayout submitButton;
    ProgressDialog pDlog;
    public static DrawerLayout drawer;
    String from;
    TextView heading,enquiry_name_txt_view,enquiry_phone_txt_view,enquiry_email_txt_view,enquiry_txt_view,enquiry_submit_btn_txt_view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EnquiryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnquiryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnquiryFragment newInstance(String param1, String param2) {
        EnquiryFragment fragment = new EnquiryFragment();
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
        View v= inflater.inflate(R.layout.fragment_enquiry, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);

        try {
            from=getArguments().getString("from");
        }catch (Exception e){
            e.printStackTrace();
        }

if(from.equals("servicedetails")) {
    HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
    HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);
}else{
    HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
    HomeMainActivityNew.prev_icon_nav.setVisibility(View.VISIBLE);
}



        enquiry=(EditText)v.findViewById(R.id.enquiry_description);
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        name=(EditText)v.findViewById(R.id.enquiry_name);
        phone=(EditText)v.findViewById(R.id.enquiry_phone);
        email=(EditText)v.findViewById(R.id.enquiry_email);
        submitButton=(LinearLayout)v.findViewById(R.id.enquiry_submit_button);

        enquiry.setTypeface(myFont1);
        name.setTypeface(myFont1);
        phone.setTypeface(myFont1);
        email.setTypeface(myFont1);

        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);

        enquiry_name_txt_view=(TextView)v.findViewById(R.id.enquiry_name_txt_view);
        enquiry_phone_txt_view=(TextView)v.findViewById(R.id.enquiry_phone_txt_view);
        enquiry_email_txt_view=(TextView)v.findViewById(R.id.enquiry_email_txt_view);
        enquiry_txt_view=(TextView)v.findViewById(R.id.enquiry_txt_view);
        enquiry_submit_btn_txt_view=(TextView)v.findViewById(R.id.enquiry_submit_btn_txt_view);

        enquiry_name_txt_view.setTypeface(myFont1);
        enquiry_phone_txt_view.setTypeface(myFont1);
        enquiry_email_txt_view.setTypeface(myFont1);
        enquiry_txt_view.setTypeface(myFont1);
        enquiry_submit_btn_txt_view.setTypeface(myFont1);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_name = name.getText().toString();
                str_email = email.getText().toString();

                str_phone = phone.getText().toString();
                str_enquiry = enquiry.getText().toString();
                String regexStr ="^[+]?[0-9]{10,13}$";

                if(str_name.isEmpty()&&str_email.isEmpty()&&str_phone.isEmpty()&&str_enquiry.isEmpty()) {
                    name.setError("name can't be empty");
                    email.setError("email can't be empty");
                    phone.setError("phone number can't be empty");
                    enquiry.setError("enquiry can't be empty");

                    error_status = 0;
                    name.requestFocus();


                }else {

                    if (str_name.isEmpty()) {
                        name.setError("name can't be empty");

                        error_status = 0;

                    }
                    if (str_email.isEmpty()) {
                        email.setError("email can't be empty");

                        error_status = 0;
                    }
                    if (str_phone.isEmpty()) {
                        phone.setError("phone number can't be empty");

                        error_status = 0;
                    }
                    if (str_enquiry.isEmpty()) {
                        enquiry.setError("enquiry can't be empty");

                        error_status = 0;
                    }

                }

                if(!str_name.isEmpty()&&!str_email.isEmpty()&&!str_phone.isEmpty()&&!str_enquiry.isEmpty()){

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_email.trim()).matches()) {
                        email.setError("invalid email");
                        email.requestFocus();
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
                            (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
/* CHECKING INTERNET CONNECTION -- END*/

                    if(isConnected) {
                        new SendToServer().execute();
                    }
                    else{
                        /*Toast.makeText(getActivity(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
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
    class SendToServer extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDlog=new ProgressDialog(getActivity(),R.style.MyTheme);

            pDlog.setCancelable(false);
            pDlog.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/enquiry.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("name",str_name);
                postDataParams.put("email",str_email);
                postDataParams.put("phone",str_phone);
                postDataParams.put("enquiry",str_enquiry);

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
                    System.out.println("sb.toString();"+sb.toString());
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
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){

                /*Toast.makeText(getActivity(),"Enquiry Sent",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(), "Enquiry Sent", Toast.LENGTH_LONG, true).show();
                getFragmentManager().popBackStackImmediate();


                /*finish();*/

            }
            else
            {
                /*Toast.makeText(getActivity(),"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                Toasty.error(getActivity(), "Failed.Try again later", Toast.LENGTH_LONG, true).show();

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
}
