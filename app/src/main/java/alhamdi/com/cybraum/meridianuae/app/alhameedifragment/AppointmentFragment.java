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
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import at.blogc.android.views.ExpandableTextView;
import es.dmoral.toasty.Toasty;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView dropDownImage,dropDownImage2,from_my_lawyers_dropDownImage,from_my_lawyers_dropDownImage2;
    private PopupWindow mPopupWindow;
    LinearLayout appointment_mylawyer_section,appointment_home_section;
    View customView;
    String law_id_from_adpater;
    LinearLayout my_account_layout;
    String popup_law_id,popup_law_name,popup_law_email,popup_law_phone,popup_law_department,popup_law_desc,popup_law_image,popup_case_id[],popup_cases[];
    String str_popup_lawyer_cases="";
    int flag=0;
    ImageView popup_lawyer_image;
    ScrollView popup_childScroll;
    ProgressBar popup_gallery_progressbar;
    ExpandableTextView popup_lawyer_address;
    TextView popup_lawyer_view_more,popup_lawyer_name,popup_lawyer_department,popup_lawyer_email,popup_lawyer_phone,popup_lawyer_cases,from_my_lawyers_appointment_lawyer_txt_view,from_my_lawyers_appointment_case_txt_view;
    LinearLayout popup_ok;
    ImageButton check_close;

    Bundle bundle;

    boolean isConnected=false;
    Snackbar snackbar;
    String selectedlawyername;

    public static DrawerLayout drawer;
    NDSpinner lawyer_spinner;
    Spinner cases_spinner,from_my_lawyers_cases_spinner,from_my_lawyers_lawyer_spinner;


    EditText description,specificReason,name,email,phone;
    TextView date,time;
    LinearLayout submitButton/*,appointment_view_lawyer*/;
    ProgressDialog pDlog;
private String stored_user_id;
    int error_status=0;
SessionManager sm;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String strCase,strLawyer,strDescription,strName,strEmail,strPhone,strDate,strTime;
    String[] case_id;
    String[] cases;

    String [] lawyer_id;
    String [] lawyer_case;
    String [] lawyer_name;


    String selected_caseid="",selected_lawyer_id="";

    TextView heading,appointment_case_txt_view,appointment_lawyer_txt_view,appointment_description_txt_view,appointment_name_txt_view,appointment_email_txt_view,appointment_phone_txt_view,appointment_date_txt_view,appointment_time_txt_view,appointment_submit_btn_txt_view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
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
        View v= inflater.inflate(R.layout.fragment_appointment, container, false);

        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);


        my_account_layout = (LinearLayout) v.findViewById(R.id.my_account_layout);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);


        appointment_mylawyer_section=(LinearLayout)v.findViewById(R.id.appointment_mylawyer_section);
        appointment_home_section=(LinearLayout)v.findViewById(R.id.appointment_home_section);

        cases_spinner=(Spinner)v.findViewById(R.id.cases_spinner);
        lawyer_spinner=(NDSpinner)v.findViewById(R.id.lawyer_spinner);//nd spinner is used because normal spinner onitemselected function doesn.t work for selected item if it is again selected


        dropDownImage=(ImageView)v.findViewById(R.id.dropDownImage);
        dropDownImage2=(ImageView)v.findViewById(R.id.dropDownImage2);
        description=(EditText)v.findViewById(R.id.description);


        from_my_lawyers_appointment_case_txt_view=(TextView)v.findViewById(R.id.from_my_lawyers_appointment_case_txt_view);
        from_my_lawyers_cases_spinner=(Spinner)v.findViewById(R.id.from_my_lawyers_cases_spinner);
        from_my_lawyers_dropDownImage=(ImageView)v.findViewById(R.id.from_my_lawyers_dropDownImage);
        from_my_lawyers_appointment_lawyer_txt_view=(TextView)v.findViewById(R.id.from_my_lawyers_appointment_lawyer_txt_view);
        from_my_lawyers_lawyer_spinner=(Spinner)v.findViewById(R.id.from_my_lawyers_lawyer_spinner);
        /*from_my_lawyers_dropDownImage2=(ImageView)v.findViewById(R.id.from_my_lawyers_dropDownImage2);*/


        name=(EditText)v.findViewById(R.id.name);
        email=(EditText)v.findViewById(R.id.email);
        phone=(EditText)v.findViewById(R.id.phone);
        date=(TextView) v.findViewById(R.id.date);
        time=(TextView) v.findViewById(R.id.time);
        submitButton=(LinearLayout)v.findViewById(R.id.submit_button);

        description.setTypeface(myFont1);

        name.setTypeface(myFont1);
        email.setTypeface(myFont1);
        phone.setTypeface(myFont1);
        date.setTypeface(myFont1);
        time.setTypeface(myFont1);

        appointment_case_txt_view=(TextView)v.findViewById(R.id.appointment_case_txt_view);
        appointment_lawyer_txt_view=(TextView)v.findViewById(R.id.appointment_lawyer_txt_view);
        appointment_description_txt_view=(TextView)v.findViewById(R.id.appointment_description_txt_view);
        appointment_name_txt_view=(TextView)v.findViewById(R.id.appointment_name_txt_view);
        appointment_email_txt_view=(TextView)v.findViewById(R.id.appointment_email_txt_view);
        appointment_phone_txt_view=(TextView)v.findViewById(R.id.appointment_phone_txt_view);
        appointment_date_txt_view=(TextView)v.findViewById(R.id.appointment_date_txt_view);
        appointment_time_txt_view=(TextView)v.findViewById(R.id.appointment_time_txt_view);
        appointment_submit_btn_txt_view=(TextView)v.findViewById(R.id.appointment_submit_btn_txt_view);
     /*   appointment_view_lawyer=(LinearLayout)v.findViewById(R.id.appointment_view_lawyer);*/

        appointment_case_txt_view.setTypeface(myFont1);
        appointment_lawyer_txt_view.setTypeface(myFont1);
        appointment_description_txt_view.setTypeface(myFont1);
        appointment_name_txt_view.setTypeface(myFont1);
        appointment_email_txt_view.setTypeface(myFont1);
        appointment_phone_txt_view.setTypeface(myFont1);
        appointment_date_txt_view.setTypeface(myFont1);
        appointment_time_txt_view.setTypeface(myFont1);
        appointment_submit_btn_txt_view.setTypeface(myFont1);


         bundle=this.getArguments();

       /* if(bundle!=null) {

            appointment_mylawyer_section.setVisibility(View.VISIBLE);
            appointment_home_section.setVisibility(View.GONE);
        }
        else {
            appointment_mylawyer_section.setVisibility(View.GONE);
            appointment_home_section.setVisibility(View.VISIBLE);
        }*/




        pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDlog.setCancelable(false);

        /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("stored user id : "+stored_user_id);
        /*getting user id end*/
if(bundle!=null){
    appointment_mylawyer_section.setVisibility(View.VISIBLE);
    appointment_home_section.setVisibility(View.GONE);
    law_id_from_adpater = bundle.getString("law_id");
    System.out.println("law_id_from_adpater : "+law_id_from_adpater);

    new FetchSelectedLawyerDetails(law_id_from_adpater).execute();

}
        else {
    appointment_mylawyer_section.setVisibility(View.GONE);
    appointment_home_section.setVisibility(View.VISIBLE);
    ConnectionCheck();
    if (isConnected) {
        new FetchCases().execute();


    } else {
        ShowSnackbar();
    }
}


        LayoutInflater inflater2=(LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView=inflater2.inflate(R.layout.popup_appointment_lawyer,null);


        popup_lawyer_image=(ImageView)customView.findViewById(R.id.popup_lawyer_image);
        popup_lawyer_name=(TextView)customView.findViewById(R.id.popup_lawyer_name);
        popup_lawyer_department=(TextView)customView.findViewById(R.id.popup_lawyer_department);
        popup_lawyer_email=(TextView)customView.findViewById(R.id.popup_lawyer_email);
        popup_lawyer_phone=(TextView)customView.findViewById(R.id.popup_lawyer_phone);
        popup_lawyer_address=(ExpandableTextView)customView.findViewById(R.id.popup_lawyer_address);
        popup_lawyer_cases=(TextView)customView.findViewById(R.id.popup_lawyer_cases);
        popup_gallery_progressbar=(ProgressBar)customView.findViewById(R.id.popup_gallery_progressbar);
        popup_childScroll=(ScrollView)customView.findViewById(R.id.popup_childScroll);
        popup_ok=(LinearLayout)customView.findViewById(R.id.popup_ok);
        popup_lawyer_view_more=(TextView)customView .findViewById(R.id.popup_lawyer_view_more);

        popup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });



        dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cases_spinner.performClick();
            }
        });
        dropDownImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    lawyer_spinner.performClick();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        from_my_lawyers_dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_my_lawyers_cases_spinner.performClick();
            }
        });
       /* from_my_lawyers_dropDownImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_my_lawyers_lawyer_spinner.performClick();
            }
        });*/

        // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        popup_lawyer_address.setAnimationDuration(1000L);

        // set interpolators for both expanding and collapsing animations
        popup_lawyer_address.setInterpolator(new OvershootInterpolator());

// or set them separately
        popup_lawyer_address.setExpandInterpolator(new OvershootInterpolator());
        popup_lawyer_address.setCollapseInterpolator(new OvershootInterpolator());


// toggle the ExpandableTextView
        popup_lawyer_view_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                popup_lawyer_address.toggle();
                popup_lawyer_view_more.setText(popup_lawyer_address.isExpanded() ? "view less" : "view more");
            }
        });

// but, you can also do the checks yourself
        popup_lawyer_view_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (popup_lawyer_address.isExpanded())
                {
                    popup_lawyer_address.collapse();
                    popup_lawyer_view_more.setText("view more");
                }
                else
                {
                    popup_lawyer_address.expand();
                    popup_lawyer_view_more.setText("view less");
                }
            }
        });

// listen for expand / collapse events
        popup_lawyer_address.setOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d("TAG", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d("TAG", "ExpandableTextView collapsed");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                date.setText(dayofMonth + "-" + (monthofYear + 1) + "-" + year);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

                datePickerDialog.show();
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
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
                            AM_PM="AM";}
                        else{
                            AM_PM="PM";}

                        if (hourofDay > 12) {
                            hourofDay -= 12;
                        }

                        if(hourofDay==0)
                            hourofDay=12;
                        time.setText(hourofDay+":"+minute+" "+AM_PM);


                    //--------------------------------
                        System.out.println("------------------------------------------------------------");
                        System.out.println("hour of day :"+hourofDay);
                        System.out.println("am_pm :"+AM_PM);
                        System.out.println("------------------------------------------------------------");

                        if(AM_PM.equalsIgnoreCase("am")){

                            if(hourofDay<8 || hourofDay==12)
                            {
                                Toasty.info(getActivity(), "Sorry.Appointment time is from 8 Am to 6 Pm", Toast.LENGTH_LONG, true).show();

                                time.setText("select time");
                            }

                            else
                            {
                                time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            }
                        }
                        else if(AM_PM.equalsIgnoreCase("pm")) {
                            if (hourofDay >= 6 && hourofDay != 12) {
                                if (minute > 0) {
                                    Toasty.info(getActivity(), "Sorry.Appointment time is from 8 Am to 6 Pm", Toast.LENGTH_LONG, true).show();

                                    time.setText("select time");
                                } else {
                                    time.setText(hourofDay + ":" + minute + " " + AM_PM);
                                }
                            } else if (hourofDay < 6) {
                                time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            } else if (hourofDay == 12) {
                                time.setText(hourofDay + ":" + minute + " " + AM_PM);
                            }
                        }

                        //--------------------------------




                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        try {


            cases_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    String caseid = case_id[pos];
                    selected_caseid = caseid;
                   /* appointment_view_lawyer.setVisibility(View.GONE);*/
                    lawyer_spinner.setAdapter(null);

                        new FetchLaeyers(caseid).execute();


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


        try{

            lawyer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                    try {

                        popup_lawyer_address.collapse();
                        popup_lawyer_view_more.setText("view more");
                        popup_childScroll.scrollTo(popup_childScroll.getVerticalScrollbarPosition(),0);

                        System.out.println("~~~~~~~~~~~~~~~~~inside on item selected~~~~~~~~~~~~~~~~~~~~");


                        String lyr_id = lawyer_id[pos];
                        selected_lawyer_id = lyr_id;
                         selectedlawyername=lawyer_name[pos].replaceAll("\\s+","");

                        if(selectedlawyername.contentEquals("NoLawyers")||selectedlawyername.contentEquals("selectlawyer")){
                           /* appointment_view_lawyer.setVisibility(View.GONE);*/


                        }else{

                            try{

                            new FetchSelectedLawyerDetails(selected_lawyer_id).execute();




                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            /*appointment_view_lawyer.setVisibility(View.VISIBLE);*/
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    /*appointment_view_lawyer.setVisibility(View.GONE);*/
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }









        try {

            from_my_lawyers_cases_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    String caseid = popup_case_id[pos];
                    selected_caseid = caseid;
                    System.out.println("selected_caseid : "+selected_caseid);
                   /* appointment_view_lawyer.setVisibility(View.GONE);*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

       /* appointment_view_lawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyLawyerFragment lwyr = new MyLawyerFragment();   // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, lwyr).addToBackStack("").commit();

            }
        });*/



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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                /*selectedlawyername = lawyer_spinner.getSelectedItem().toString().replaceAll("\\s+", "");
                System.out.println("selected lawyer : " + selectedlawyername);
*/
if(bundle==null) {
    selectedlawyername = lawyer_spinner.getSelectedItem().toString().replaceAll("\\s+", "");
}
                strDescription = description.getText().toString();
                strName = name.getText().toString();
                strEmail = email.getText().toString();
                strPhone = phone.getText().toString();
                strDate = date.getText().toString();
                strTime = time.getText().toString();
                String regexStr = "^[+]?[0-9]{10,13}$";


                if (strDescription.isEmpty() && strName.isEmpty() && strEmail.isEmpty() && strPhone.isEmpty()/*&&strDate.equals("")&&strTime.equals("")*/ && selected_lawyer_id == "") {
                    description.setError("Description can't be empty");
                    name.setError("Name can't be empty");
                    email.setError("Email can't be empty");

                    phone.setError("Phone can't be empty");


                    date.setTextColor(Color.parseColor("#FFFF0000"));
                    time.setTextColor(Color.parseColor("#FFFF0000"));

                    /*date.setError("Select date");
                    time.setError("Select time");*/
                    error_status = 0;
                    description.requestFocus();


                } else {

                    if (strDescription.isEmpty()) {
                        description.setError("Description can't be empty");

                        error_status = 0;

                    }
                    if (strName.isEmpty()) {
                        name.setError("Name can't be empty");

                        error_status = 0;
                    }
                    if (strEmail.isEmpty()) {
                        email.setError("Email can't be empty");

                        error_status = 0;
                    }
                    if (strPhone.isEmpty()) {
                        phone.setError("Phone number can't be empty");

                        error_status = 0;
                    }
                    if (strDate.replaceAll("\\s+", "").equals("selectdate")) {
                        date.setTextColor(Color.parseColor("#FFFF0000"));
                        error_status = 0;
                    }
                    if (strTime.replaceAll("\\s+", "").equals("selecttime")) {
                        time.setTextColor(Color.parseColor("#FFFF0000"));
                        error_status = 0;
                    }
                    if (!strDate.replaceAll("\\s+", "").equals("selectdate")) {
                        date.setTextColor(Color.parseColor("#000000"));
                        error_status = 0;
                    }
                    if (!strTime.replaceAll("\\s+", "").equals("selecttime")) {
                        time.setTextColor(Color.parseColor("#000000"));
                        error_status = 0;
                    }
                    if(bundle==null) {
                        if (selectedlawyername.contentEquals("NoLawyers")) {
                            /*Toast.makeText(getActivity(), "No lawyers avilable for selected case", Toast.LENGTH_LONG).show();*/
                            Toasty.info(getActivity(), "No lawyers avilable for selected case", Toast.LENGTH_LONG, true).show();
                            error_status = 0;
                            selected_lawyer_id = "0";
                        }
                    }

                }

                if (!strDescription.isEmpty() && !strName.isEmpty() && !strEmail.isEmpty() && !strPhone.isEmpty()/*&&!strDate.equals("")&&!strTime.equals("")*/) {

                    if(bundle==null) {//directed from main home
                                    if (selectedlawyername.contentEquals("NoLawyers")) {
                                        /*Toast.makeText(getActivity(), "No lawyers avilable for selected case", Toast.LENGTH_LONG).show();*/
                                        Toasty.info(getActivity(), "No lawyers avilable for selected case", Toast.LENGTH_LONG, true).show();
                                        error_status = 0;
                                    }
                                    else if (selectedlawyername.contentEquals("selectlawyer")) {
                                        /*Toast.makeText(getActivity(), "No lawyers avilable for selected case", Toast.LENGTH_LONG).show();*/
                                        Toasty.info(getActivity(), "select a lawyer", Toast.LENGTH_LONG, true).show();
                                        error_status = 0;
                                    }
                                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail.trim()).matches()) {
                                        email.setError("Invalid email");
                                        email.requestFocus();
                                        error_status = 0;
                                    } else if (!strPhone.matches(regexStr)) {
                                        phone.setError("invalid phone number");
                                        phone.requestFocus();
                                        error_status = 0;
                                    } else if (strDate.replaceAll("\\s+", "").equals("selectdate")) {
                                        date.setTextColor(Color.parseColor("#FFFF0000"));
                                        error_status = 0;
                                    } else if (strTime.replaceAll("\\s+", "").equals("selecttime")) {
                                        time.setTextColor(Color.parseColor("#FFFF0000"));
                                        error_status = 0;
                                    } else {
                                        date.setTextColor(Color.parseColor("#000000"));
                                        time.setTextColor(Color.parseColor("#000000"));
                                        error_status = 1;
                                    }
                    }
                    else{//directed from mylawyers
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail.trim()).matches()) {
                                        email.setError("Invalid email");
                                        email.requestFocus();
                                        error_status = 0;
                                    } else if (!strPhone.matches(regexStr)) {
                                        phone.setError("invalid phone number");
                                        phone.requestFocus();
                                        error_status = 0;
                                    } else if (strDate.replaceAll("\\s+", "").equals("selectdate")) {
                                        date.setTextColor(Color.parseColor("#FFFF0000"));
                                        error_status = 0;
                                    } else if (strTime.replaceAll("\\s+", "").equals("selecttime")) {
                                        time.setTextColor(Color.parseColor("#FFFF0000"));
                                        error_status = 0;
                                    } else {
                                        date.setTextColor(Color.parseColor("#000000"));
                                        time.setTextColor(Color.parseColor("#000000"));
                                        error_status = 1;
                                    }
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
    class SendToServer extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


               /* pDlog.show();*/
               HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/appointment.php");
                String dateparts[]=strDate.split("-");
                String  new_date_val=dateparts[2]+"-"+dateparts[1]+"-"+dateparts[0];
                strDate=new_date_val;

                System.out.println("strDate : "+strDate);

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("case_id",selected_caseid);
                postDataParams.put("lawyer_id",selected_lawyer_id);
                postDataParams.put("description",strDescription);
                postDataParams.put("name",strName);
                postDataParams.put("email",strEmail);
                postDataParams.put("phone",strPhone);
                postDataParams.put("date",strDate);
                postDataParams.put("time",strTime);

                System.out.println("params"+postDataParams.toString());

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
                pDlog.cancel();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(" (inside on postexecute) result : " + result + "\n");

            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.trim().contentEquals("\"success\"")){

                /*Toast.makeText(getActivity(),"Successfully Registered",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(), "Successfully Registered", Toast.LENGTH_LONG, true).show();
                getFragmentManager().popBackStackImmediate();
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
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(" result : " + result + "\n");

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return result.toString();
    }
    class FetchCases extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ConnectionCheck();
            if(!isConnected){
                ShowSnackbar();
            }
            else {
               /* pDlog.show();*/
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




try {


    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
            R.layout.spinner_text_style, cases);
    dataAdapter.setDropDownViewResource(R.layout.spinner_text_style);
    cases_spinner.setAdapter(dataAdapter);
}catch (Exception e){
    e.printStackTrace();
}



           /* if(pDlog.isShowing())
                pDlog.dismiss();*/
          /*  HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);*/

        }
    }
    class FetchLaeyers extends AsyncTask<String,String,String>{

        String caseid;
        int flag=0;

        public FetchLaeyers(String id){
            caseid=id;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag=0;

            ConnectionCheck();
            if(!isConnected){
                ShowSnackbar();
            }
            else {
               /* pDlog.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected String doInBackground(String... strings) {
        int arreylength=0;
            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/lawyers_bycaseid.php?caseid="+caseid);
            if (jsonString != null || !jsonString.trim().contentEquals("null")||!jsonString.isEmpty()||jsonString!="null") {

                try {

                    JSONArray imageArray = new JSONArray(jsonString);
                    arreylength=imageArray.length();
                    lawyer_id=new String[(arreylength+1)];
                    lawyer_case=new String[(arreylength+1)];
                    lawyer_name=new String[(arreylength+1)];
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("lawyer_name orgi length : "+arreylength);
                    System.out.println("lawyer_name new length : "+(arreylength+1));
                    System.out.println("-------------------------------------------------------------");
                    if(arreylength==0)
                    {
                        flag=0;
                    }
                    else {
                        flag=1;
                        for (int i = 0; i <=arreylength; i++) {
                            System.out.println("-------------------------------------------------------------");
                            System.out.println("i : "+i);
                            System.out.println("-------------------------------------------------------------");
                            if(i==0){
                                lawyer_id[i] = "firstid";
                                lawyer_case[i] ="firstcase";
                                lawyer_name[i] = "select lawyer";   //for adding select lawyer at top

                            }else {

                                JSONObject jsonData = imageArray.getJSONObject(i-1);//i is decremented
                                lawyer_id[i] = jsonData.getString("id");
                                lawyer_case[i] = jsonData.getString("cases");
                                lawyer_name[i] = jsonData.getString("name");


                                System.out.println("***************************************************");
                                System.out.println(" lawyer_id : " + jsonData.getString("id") + "\n");
                                System.out.println("lawyer_case : " + jsonData.getString("cases") + "\n");
                                System.out.println("lawyer_name : " + jsonData.getString("name"));
                                System.out.println("***************************************************");
                            }
                        }
                        System.out.println("-------------------------------------------------------------");
                        for(int i=0;i<lawyer_name.length;i++)
                        {
                            System.out.println(lawyer_name[i]);
                        }
                        System.out.println("-------------------------------------------------------------");


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {


                flag=0;
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("flag : "+flag);

            try {


                if(flag==1){



                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.spinner_text_style, lawyer_name);

                dataAdapter.setDropDownViewResource(R.layout.spinner_text_style);
                lawyer_spinner.setAdapter(dataAdapter);



                   /* if(bundle!=null) {
                        law_id_from_adpater = bundle.getString("law_id");
                        System.out.println("law_id_from_adpater : "+law_id_from_adpater);

                        new FetchSelectedLawyerDetails(law_id_from_adpater).execute();





                    }*/



                }
                else{
                   /* appointment_view_lawyer.setVisibility(View.GONE);*/
                    lawyer_name=new String[1];

                    lawyer_name[0]="No Lawyers";

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.spinner_text_style, lawyer_name);

                    dataAdapter.setDropDownViewResource(R.layout.spinner_text_style);
                    lawyer_spinner.setAdapter(dataAdapter);

                    System.out.println("No lawyers for swelected case");

                }
            }catch (Exception e){
                e.printStackTrace();
            }


            /*if(pDlog.isShowing())
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
    public void displayPopup()
    {
        try {


            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);



           check_close = (ImageButton) customView.findViewById(R.id.check_close);
            check_close.setOnClickListener(new View.OnClickListener() {
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
    class FetchSelectedLawyerDetails extends AsyncTask<String,String,String> {
        String lw_id;
        public FetchSelectedLawyerDetails(String lawyer_id)
        {
            lw_id=lawyer_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ConnectionCheck();
            if(!isConnected){
                ShowSnackbar();
            }
            else {
               /* pDlog.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpHandler h = new HttpHandler();
                String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/lawyerdetailsbyid.php?law_id="+lw_id);
                if (jsonString != null) {
                    flag=0;
                    try {
                        /*JSONArray valueArray = new JSONArray(jsonString);*/
                        JSONObject urlObject=new JSONObject(jsonString);

                        JSONArray valueArray = urlObject.getJSONArray("lawyer_details");
                        for (int i = 0; i <valueArray.length(); i++) {
                            JSONObject jsonData = valueArray.getJSONObject(i);

                            popup_law_id=jsonData.getString("law_id");
                            popup_law_name=jsonData.getString("law_name");
                            popup_law_email=jsonData.getString("law_email");
                            popup_law_phone=jsonData.getString("law_phone");
                            popup_law_department=jsonData.getString("law_department");
                            popup_law_desc=jsonData.getString("law_desc");
                            popup_law_image=jsonData.getString("law_image");
                            System.out.println("popup_law_name : " + popup_law_name);
                        }
                        JSONArray caseDetails=urlObject.getJSONArray("case_details");
                        popup_case_id=new String[caseDetails.length()];
                        popup_cases=new String[caseDetails.length()];
                        for(int k=0;k<caseDetails.length();k++)
                        {
                            JSONObject caseObject=caseDetails.getJSONObject(k);
                            popup_case_id[k]=caseObject.getString("case_id");
                            popup_cases[k]=caseObject.getString("cases");


                            str_popup_lawyer_cases+=" - "+popup_cases[k]+"\n";


                            System.out.println("--------------------------------------------------");
                            System.out.println("case_id : "+popup_case_id[k]);
                            System.out.println("cases : "+popup_cases[k]);
                            System.out.println("--------------------------------------------------");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(jsonString.equals("")||jsonString==null ||jsonString.isEmpty()){
                    flag=1;
                }}catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(flag==1){
                /*Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "Something went wrong.Try again", Toast.LENGTH_SHORT, true).show();
                getFragmentManager().popBackStackImmediate();

            }
            else {
                try {

                    if(bundle!=null) {



                        lawyer_name=new String[1];

                        lawyer_name[0]=popup_law_name;
                        /*System.out.println("popup_law_name : " + popup_law_name);*/
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_text_style, lawyer_name);

                        dataAdapter.setDropDownViewResource(R.layout.spinner_text_style);
                        from_my_lawyers_lawyer_spinner.setAdapter(dataAdapter);
                        selected_lawyer_id=popup_law_id;

                        try {


                            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinner_text_style, popup_cases);
                            dataAdapter2.setDropDownViewResource(R.layout.spinner_text_style);
                            from_my_lawyers_cases_spinner.setAdapter(dataAdapter2);

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }


                    else {


                        Picasso.with(getActivity()).load(popup_law_image)
                                .transform(new CircleTransform())
                                .into(popup_lawyer_image, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        popup_gallery_progressbar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                        popup_lawyer_name.setText(popup_law_name);
                        popup_lawyer_email.setText(popup_law_email);
                        popup_lawyer_phone.setText(popup_law_phone);
                        popup_lawyer_department.setText(popup_law_department);
                        popup_lawyer_address.setText(popup_law_desc);

                        System.out.println("str_popup_lawyer_cases : " + str_popup_lawyer_cases);
                        popup_lawyer_cases.setText(str_popup_lawyer_cases);


                        displayPopup();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /*if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

        }
    }

}
