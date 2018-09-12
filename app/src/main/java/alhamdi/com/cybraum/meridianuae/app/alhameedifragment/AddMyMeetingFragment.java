package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMyMeetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMyMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMyMeetingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DrawerLayout drawer;
    int error_status=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog pDlog;
    LinearLayout add_meeting_submit_button;
    private int mYear, mMonth, mDay, mHour, mMinute;
public EditText add_meeting_title,add_meeting_description;
   public TextView add_meeting_start_time,add_meeting_date;

    private  String str_user_id,str_title,str_date,str_starting_time,str_ending_time,str_description;
    SessionManager sm;
    TextView heading,my_meeting_title,my_meeting_date,my_meeting_time,my_meeting_submit_btn;

    private OnFragmentInteractionListener mListener;

    public AddMyMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMyMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMyMeetingFragment newInstance(String param1, String param2) {
        AddMyMeetingFragment fragment = new AddMyMeetingFragment();
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
        View v= inflater.inflate(R.layout.fragment_add_my_meeting, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;

        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        add_meeting_title=(EditText)v.findViewById(R.id.add_meeting_title);
        add_meeting_start_time=(TextView)v.findViewById(R.id.add_meeting_start_time);

        add_meeting_date=(TextView)v.findViewById(R.id.add_meeting_date);
        add_meeting_submit_button=(LinearLayout)v.findViewById(R.id.add_meeting_submit_button);
/*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        str_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);
        my_meeting_title=(TextView)v.findViewById(R.id.my_meeting_title);
        my_meeting_date=(TextView)v.findViewById(R.id.my_meeting_date);
        my_meeting_time=(TextView)v.findViewById(R.id.my_meeting_time);
        my_meeting_submit_btn=(TextView)v.findViewById(R.id.my_meeting_submit_btn);

        my_meeting_title.setTypeface(myFont1);
        my_meeting_date.setTypeface(myFont1);
        my_meeting_time.setTypeface(myFont1);
        my_meeting_submit_btn.setTypeface(myFont1);

        add_meeting_title.setTypeface(myFont1);
        add_meeting_start_time.setTypeface(myFont1);

        add_meeting_date.setTypeface(myFont1);

        add_meeting_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {


                        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
                        String current_date=mYear+"-"+mMonth+"-"+mDay;
                        String selected_date=year+"-"+monthofYear+"-"+dayofMonth;
                        try {

                            if (dfDate.parse(selected_date).before(dfDate.parse(current_date)))
                            {
                                Toasty.info(getActivity(), "can't select past date", Toast.LENGTH_LONG, true).show();
                            }
                            else {
                                add_meeting_date.setText(dayofMonth + "-" + (monthofYear + 1) + "-" + year);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }




                           /* Snackbar.with(getActivity(),null)
                                    .type(Type.ERROR)
                                    .message("can't select past date")
                                    .duration(Duration.LONG)

                                    .show();*/

                           /* Toast.makeText(getActivity(), "can't select past date", Toast.LENGTH_SHORT).show();*/





                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        add_meeting_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {
                        String AM_PM="";
                        if(hourofDay<12){
                            AM_PM="AM";

                        }
                        else{
                            AM_PM="PM";}

                        if (hourofDay > 12) {
                            hourofDay -= 12;
                        }

                        if(hourofDay==0)
                            hourofDay=12;

                       /* add_meeting_start_time.setText(hourofDay+":"+minute+" "+AM_PM);*/
                        //--------------------------------
                        System.out.println("------------------------------------------------------------");
                        System.out.println("hour of day :"+hourofDay);
                        System.out.println("am_pm :"+AM_PM);
                        System.out.println("------------------------------------------------------------");

                        if(AM_PM.equalsIgnoreCase("am")){

                            if(hourofDay<8 || hourofDay==12)
                            {
                                Toasty.info(getActivity(), "Sorry.Appointment time is from 8 Am to 6 Pm", Toast.LENGTH_LONG, true).show();

                                add_meeting_start_time.setText("select time");
                            }

                            else
                            {
                                add_meeting_start_time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            }
                        }
                        else if(AM_PM.equalsIgnoreCase("pm")) {
                            if (hourofDay >= 6 && hourofDay != 12) {
                                if (minute > 0) {
                                    Toasty.info(getActivity(), "Sorry.Appointment time is from 8 Am to 6 Pm", Toast.LENGTH_LONG, true).show();

                                    add_meeting_start_time.setText("select time");
                                } else {
                                    add_meeting_start_time.setText(hourofDay + ":" + minute + " " + AM_PM);
                                }
                            } else if (hourofDay < 6) {
                                add_meeting_start_time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            } else if (hourofDay == 12) {
                                add_meeting_start_time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            }
                        }

                        //--------------------------------

                    }
                },mHour,mMinute,false);
                timePickerDialog.show();


            }
        });

add_meeting_submit_button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        str_title=add_meeting_title.getText().toString();
        str_description="description";
        str_date=add_meeting_date.getText().toString();
        str_starting_time=add_meeting_start_time.getText().toString();

        if (str_title.isEmpty() && str_date.replaceAll("\\s+","").equals("selectdate") && str_starting_time.replaceAll("\\s+","").equals("selecttime")  ) {
            add_meeting_title.setError("title can't be empty");

            add_meeting_date.setTextColor(Color.parseColor("#FFFF0000"));
            add_meeting_start_time.setTextColor(Color.parseColor("#FFFF0000"));


            error_status = 0;
            add_meeting_title.requestFocus();


        } else {

            if (str_title.isEmpty()) {
                add_meeting_title.setError("title can't be empty");

                error_status = 0;

            }
            if(str_date.replaceAll("\\s+","").equals("selectdate")){
                add_meeting_date.setTextColor(Color.parseColor("#FFFF0000"));
                error_status = 0;
            }
            if(str_starting_time.replaceAll("\\s+","").equals("selecttime")){
                add_meeting_start_time.setTextColor(Color.parseColor("#FFFF0000"));
                error_status = 0;
            }



            if(!str_date.replaceAll("\\s+","").equals("selectdate")){
                add_meeting_date.setTextColor(Color.parseColor("#000000"));
                error_status = 0;
            }
            if(!str_starting_time.replaceAll("\\s+","").equals("selecttime")){
                add_meeting_start_time.setTextColor(Color.parseColor("#000000"));
                error_status = 0;
            }


        }

        if (!str_title.isEmpty()&& !str_date.replaceAll("\\s+","").equals("selectdate") && !str_starting_time.replaceAll("\\s+","").equals("selecttime") ) {

            add_meeting_date.setTextColor(Color.parseColor("#000000"));
            add_meeting_start_time.setTextColor(Color.parseColor("#000000"));


            if(str_title.length()>25){
                add_meeting_title.setError("title length can't exceed 25 characters");
                error_status = 0;

            }else {

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
                new SentToServer().execute();
                 imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {


                /*Toast.makeText(getActivity(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();*/
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
    class SentToServer extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog = new ProgressDialog(getActivity(),R.style.MyTheme);

            pDlog.setCancelable(false);
            pDlog.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
try {
   /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date1=formatter.parse(str_date);
    String new_date=formatter.format(date1);
   str_date=date1.toString();*/

    String dateparts[]=str_date.split("-");
    String  new_date_val=dateparts[2]+"-"+dateparts[1]+"-"+dateparts[0];
    str_date=new_date_val;
    System.out.println("str_date : "+new_date_val);

}catch (Exception e){
    e.printStackTrace();
}

        }

        @Override
        protected String doInBackground(String... strings) {
            try {



System.out.println("////////////////////////////////////   str_user_id : "+str_user_id);

                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/meetings.php");



                JSONObject postDataParams=new JSONObject();
                postDataParams.put("title",str_title);
                postDataParams.put("description",str_description);
                postDataParams.put("user_id",str_user_id);


                postDataParams.put("date",str_date);
                postDataParams.put("time_from",str_starting_time);
                /*postDataParams.put("time_to",str_ending_time);*/

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
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){
                getFragmentManager().popBackStackImmediate();

                /*Toast.makeText(getActivity(),"Added",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(), "Added", Toast.LENGTH_LONG, true).show();

            }
            else
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
}
