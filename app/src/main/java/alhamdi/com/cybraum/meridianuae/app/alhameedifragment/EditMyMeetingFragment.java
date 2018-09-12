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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditMyMeetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditMyMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMyMeetingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static DrawerLayout drawer;

    int error_status=0;
    ProgressDialog pDlog;
    LinearLayout edit_meeting_submit_button;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public EditText edit_meeting_title,edit_meeting_description;
    public TextView edit_meeting_start_time,edit_meeting_date;
    TextView heading,my_meeting_title,my_meeting_date,my_meeting_time,my_meeting_submit_btn;
    private  String str_user_id,str_title,str_date,str_starting_time,str_description,str_meet_id="";
    SessionManager sm;
    private OnFragmentInteractionListener mListener;

    public EditMyMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditMyMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMyMeetingFragment newInstance(String param1, String param2) {
        EditMyMeetingFragment fragment = new EditMyMeetingFragment();
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
        View v= inflater.inflate(R.layout.fragment_edit_my_meeting, container, false);

        HomeMainActivityNew.doubleBackToExitPressedOnce = false;

        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        edit_meeting_title=(EditText)v.findViewById(R.id.edit_meeting_title);
        edit_meeting_start_time=(TextView)v.findViewById(R.id.edit_meeting_start_time);

        edit_meeting_date=(TextView)v.findViewById(R.id.edit_meeting_date);
        edit_meeting_submit_button=(LinearLayout)v.findViewById(R.id.edit_meeting_submit_button);



        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);
        my_meeting_title=(TextView)v.findViewById(R.id.edit_my_meeting_title);
        my_meeting_date=(TextView)v.findViewById(R.id.edit_my_meeting_date);
        my_meeting_time=(TextView)v.findViewById(R.id.edit_my_meeting_time);
        my_meeting_submit_btn=(TextView)v.findViewById(R.id.edit_my_meeting_submit_btn);

        my_meeting_title.setTypeface(myFont1);
        my_meeting_date.setTypeface(myFont1);
        my_meeting_time.setTypeface(myFont1);
        my_meeting_submit_btn.setTypeface(myFont1);

        edit_meeting_title.setTypeface(myFont1);
        edit_meeting_start_time.setTypeface(myFont1);

        edit_meeting_date.setTypeface(myFont1);


/*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        str_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/

/*getting meet_id start*/

        Bundle bundle=this.getArguments();
        if(bundle!=null){
             str_meet_id=bundle.getString("meet_id");
            String str_title2=bundle.getString("title");
            String str_date2=bundle.getString("date");
            String str_starting_time2=bundle.getString("starting_time");


/*formating database date ("MMM yyyy,dd") to datepicker format("dd-MM-yyyy")  --start*/
            DateFormat readFormat=new SimpleDateFormat("MMM yyyy,dd");
            DateFormat writeFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date selecteddate=null;

            try{
                selecteddate=readFormat.parse(str_date2);

            }catch (ParseException e){
                e.printStackTrace();
            }

            String formatedDate="";

            if(selecteddate!=null){
                try{
                formatedDate=writeFormat.format(selecteddate);

                    String[] date_parts = formatedDate.split("-");
                    mDay = Integer.parseInt(date_parts[0]);
                    mMonth  = Integer.parseInt(date_parts[1])-1;
                    mYear  = Integer.parseInt(date_parts[2]);
                    System.out.println("formatedDate : "+formatedDate);







                }catch (Exception e){
                    e.printStackTrace();
                }
            }
/*formating database date ("MMM yyyy,dd") to datepicker format("dd-MM-yyyy")  --end*/

            System.out.println("____________________________________________________________________");
            System.out.println("Inside dundle data , date :"+str_date2);
            System.out.println("____________________________________________________________________");
            edit_meeting_title.setText(str_title2);
            edit_meeting_date.setText(formatedDate);
            edit_meeting_start_time.setText(str_starting_time2);


        }
/*getting meet_id end*/
        edit_meeting_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c=Calendar.getInstance();
              /*  mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);
*/
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {
                        System.out.println("dayofMonth :"+dayofMonth+"\nmonthofYear : "+(monthofYear+1)+"\nyear : "+year);
                        edit_meeting_date.setText(dayofMonth+"-"+(monthofYear+1)+"-"+year);
                        mDay = dayofMonth;
                        mMonth  = monthofYear;
                        mYear  = year;
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        edit_meeting_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                        edit_meeting_start_time.setText(hourofDay+":"+minute+" "+AM_PM);

                    }
                },mHour,mMinute,false);
                timePickerDialog.show();


            }
        });

        edit_meeting_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_date="";
                str_title=edit_meeting_title.getText().toString();
                str_description="description";
                str_date=edit_meeting_date.getText().toString();
                str_starting_time=edit_meeting_start_time.getText().toString();

                if (str_title.isEmpty() && str_date.replaceAll("\\s+","").equals("selectdate") && str_starting_time.replaceAll("\\s+","").equals("selecttime") ) {
                    edit_meeting_title.setError("title can't be empty");

                    edit_meeting_date.setTextColor(Color.parseColor("#FFFF0000"));
                    edit_meeting_start_time.setTextColor(Color.parseColor("#FFFF0000"));


                    error_status = 0;
                    edit_meeting_title.requestFocus();


                } else {

                    if (str_title.isEmpty()) {
                        edit_meeting_title.setError("title can't be empty");
                        error_status = 0;
                    }
                    if(str_date.replaceAll("\\s+","").equals("selectdate")){
                        edit_meeting_date.setTextColor(Color.parseColor("#FFFF0000"));
                        error_status = 0;
                    }
                    if(str_starting_time.replaceAll("\\s+","").equals("selecttime")){
                        edit_meeting_start_time.setTextColor(Color.parseColor("#FFFF0000"));
                        error_status = 0;
                    }


                    if(!str_date.replaceAll("\\s+","").equals("selectdate")){
                        edit_meeting_date.setTextColor(Color.parseColor("#000000"));
                        error_status = 0;
                    }
                    if(!str_starting_time.replaceAll("\\s+","").equals("selecttime")){
                        edit_meeting_start_time.setTextColor(Color.parseColor("#000000"));
                        error_status = 0;
                    }


                }

                if (!str_title.isEmpty()&& !str_date.replaceAll("\\s+","").equals("selectdate") && !str_starting_time.replaceAll("\\s+","").equals("selecttime")) {

                    edit_meeting_date.setTextColor(Color.parseColor("#000000"));
                    edit_meeting_start_time.setTextColor(Color.parseColor("#000000"));
                    if(str_title.length()>25){
                        edit_meeting_title.setError("title length can't exceed 25 characters");
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
    class SentToServer extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog = new ProgressDialog(getActivity(),R.style.MyTheme);

            pDlog.setCancelable(false);
            pDlog.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {



                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/edit_meetings.php");
                System.out.println("____________________________________________________________________");
                System.out.println("Inside SentToServer , date :"+str_date);
                System.out.println("____________________________________________________________________");
                JSONObject postDataParams=new JSONObject();
                postDataParams.put("id",str_meet_id);
                postDataParams.put("title",str_title);
                postDataParams.put("description",str_description);
                postDataParams.put("user_id",str_user_id);
                postDataParams.put("date",str_date);
                postDataParams.put("time_from",str_starting_time);


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
           /* if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){
                getFragmentManager().popBackStackImmediate();
                /*Toast.makeText(getActivity(),"Updated",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(), "Updated", Toast.LENGTH_LONG, true).show();

            }
            else
            {
                /*Toast.makeText(getActivity(),"Failed.Try after some time.",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "Failed.Try after some time", Toast.LENGTH_LONG, true).show();

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
