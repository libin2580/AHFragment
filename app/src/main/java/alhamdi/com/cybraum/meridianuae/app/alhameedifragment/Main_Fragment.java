package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.permission.CheckAndRequestPermission;
import es.dmoral.toasty.Toasty;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String str_phone_number="";
    private PopupWindow mPopupWindow;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ImageButton document_pass_check_close;
    View customView;
    LinearLayout my_account_layout,call_yes,call_no;

    private ImageView asklawyer_image, services_image, appointments_image, my_account_image;
    LinearLayout box1, box2, box3, box4, footerBox_1, footerBox_2;

    TextView box1_txt_view,box2_txt_view,box3_txt_view,box4_txt_view,call_us_txt_view,enquiry_txt_view;
    public static ImageView navOpen;

    String skiped;

    SessionManager sm;

    CheckAndRequestPermission cp;

    public static DrawerLayout drawer;


    private OnFragmentInteractionListener mListener;

    public Main_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Main_Fragment newInstance() {
        Main_Fragment fragment = new Main_Fragment();
        Bundle args = new Bundle();

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
      View v=inflater.inflate(R.layout.content_home_main_new, container, false);

        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.homebg);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        box1_txt_view=(TextView)v.findViewById(R.id.box1_txt_view);
        box2_txt_view=(TextView)v.findViewById(R.id.box2_txt_view);
        box3_txt_view=(TextView)v.findViewById(R.id.box3_txt_view);
        box4_txt_view=(TextView)v.findViewById(R.id.box4_txt_view);
        call_us_txt_view=(TextView)v.findViewById(R.id.call_us_view);
        enquiry_txt_view=(TextView)v.findViewById(R.id.enquiry_txt_view);

        box1_txt_view.setTypeface(myFont1);
        box2_txt_view.setTypeface(myFont1);
        box3_txt_view.setTypeface(myFont1);
        box4_txt_view.setTypeface(myFont1);
        call_us_txt_view.setTypeface(myFont1);
        enquiry_txt_view.setTypeface(myFont1);

        HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_white_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.user_icon.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        box1 = (LinearLayout)v. findViewById(R.id.square_boxes_layout_horizontal_section_1_box_1);
        box2 = (LinearLayout) v.findViewById(R.id.square_boxes_layout_horizontal_section_1_box_2);
        box3 = (LinearLayout)v. findViewById(R.id.square_boxes_layout_horizontal_section_2_box_1);
        box4 = (LinearLayout)v. findViewById(R.id.square_boxes_layout_horizontal_section_2_box_2);



        footerBox_1 = (LinearLayout) v.findViewById(R.id.footer_contact_layout_box_1);
        footerBox_2 = (LinearLayout)v. findViewById(R.id.footer_contact_layout_box_2);

        asklawyer_image = (ImageView)v. findViewById(R.id.asklawyer_image);
        services_image = (ImageView) v.findViewById(R.id.services_image);
        appointments_image = (ImageView) v.findViewById(R.id.appointment_image);
        my_account_image = (ImageView)v. findViewById(R.id.my_account_image);

        asklawyer_image.setImageResource(R.drawable.askalawyer);
        services_image.setImageResource(R.drawable.services);
        appointments_image.setImageResource(R.drawable.appointment);
        my_account_image.setImageResource(R.drawable.my_account);


        my_account_layout=(LinearLayout)v.findViewById(R.id.main_fragment_layout);

        cp = new CheckAndRequestPermission();

//checking if skiped login screen --start
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        skiped=details.get(sm.KEY_IS_SKIPED);
//checking if skiped login screen --end

try {
    new Getcontact().execute();
}
catch (Exception e) {
    e.printStackTrace();
}


        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflator.inflate(R.layout.popup_call_request, null);
        try {

            call_yes = (LinearLayout) customView.findViewById(R.id.call_yes);
            call_no = (LinearLayout) customView.findViewById(R.id.call_no);



        }catch (Exception e){
            e.printStackTrace();
        }

        my_account_layout = (LinearLayout) v.findViewById(R.id.main_fragment_layout);





        box1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        asklawyer_image.setImageResource(R.drawable.askalawyer_red);



                        break;
                    case MotionEvent.ACTION_UP:

                        if(skiped.equalsIgnoreCase("true")){
                           /* Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/

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
                            AskLawyerFragment alf = new AskLawyerFragment();
                            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, alf).addToBackStack("").commit();
                        }
                        asklawyer_image.setImageResource(R.drawable.askalawyer);
                        break;
                }


                return true;
            }
        });
        box2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        services_image.setImageResource(R.drawable.services_red);


                        break;
                    case MotionEvent.ACTION_UP:
                        ServiceFragment sf=new ServiceFragment();
                        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, sf).addToBackStack("").commit();
                        services_image.setImageResource(R.drawable.services);
                        break;
                }


                return true;
            }
        });


        box3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        appointments_image.setImageResource(R.drawable.appointment_red);

                        break;
                    case MotionEvent.ACTION_UP:
                        if(skiped.equalsIgnoreCase("true")){
                            /*Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/


                           /* new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)

                                    .setContentText("You must login to continue")
                                    .setCancelText("Cancel")
                                    .setConfirmText("Login")
                                    .showCancelButton(true)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent i = new Intent(getActivity(), Login.class);

                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                                            getActivity().finish();
                                        }
                                    })
                                    .show();*/


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
                            AppointmentFragment sf = new AppointmentFragment();
                            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, sf).addToBackStack("").commit();
                        }
                        appointments_image.setImageResource(R.drawable.appointment);
                        break;
                }


                return true;
            }
        });

        box4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        my_account_image.setImageResource(R.drawable.my_account_red);



                        break;
                    case MotionEvent.ACTION_UP:
                        MyAccountFragment sf=new MyAccountFragment();
                        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, sf).addToBackStack("").commit();
                        my_account_image.setImageResource(R.drawable.my_account);
                        break;
                }


                return true;
            }
        });


        footerBox_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean chk = cp.checkAndRequestPermissions(getActivity());
                if (chk) {


                    displayPopup();
                    try{
                    new Getcontact().execute();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }



                }else {
                   /* Toast.makeText(getActivity(),"You must grant permission to call",Toast.LENGTH_SHORT).show();*/
                    Toasty.info(getActivity(), "You must grant permission to call.", Toast.LENGTH_LONG, true).show();
                }



            }
        });





        footerBox_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnquiryFragment sf=new EnquiryFragment();

                Bundle enquirybundle = new Bundle();
                enquirybundle.putSerializable("from", "mainfragment");
                sf.setArguments(enquirybundle);
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, sf).addToBackStack("").commit();

            }
        });



        call_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        call_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(str_phone_number=="" || str_phone_number.isEmpty() || str_phone_number==null)
                {
                    str_phone_number="971 4 3212266";
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+str_phone_number));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mPopupWindow.dismiss();
                startActivity(callIntent);


            }
        });


return  v;
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


            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);

           /* document_pass_check_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                }
            });*/
            mPopupWindow.showAtLocation(my_account_layout, Gravity.CENTER, 0, 0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private class Getcontact extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/contact.php");
                System.out.println("______________________________________________________");
                System.out.println("jsonStr : " + jsonStr);
                System.out.println("______________________________________________________");

                if (jsonStr != null) {
                    try {
                        // Getting JSON Array node
                        JSONArray jsonarray = new JSONArray(jsonStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);

                            str_phone_number = jsonobject.getString("phone");


                        }

                    } catch (final JSONException e) {

                      /*  Toast.makeText(getActivity(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();*/
                        e.printStackTrace();


                    }
                } else {
                    //     Log.e(TAG, "Couldn't get json from server.");

                   /* Toast.makeText(getActivity(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();*/
                    Toasty.info(getActivity(), "Something went wrong.", Toast.LENGTH_LONG, true).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(false);
            ConnectionCheck();
            if(!isConnected){
                Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();
            }
            else {
                pDialog.show();
            }*/

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            try {
               /* phone.setText(phone1);
                email.setText(email1);
                address.setText(address1);*/
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        protected void onPostExecute(JSONObject json) {
            /*pDialog.dismiss();*/
        }
    }


}
