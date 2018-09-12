package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    String str_phone_number="";
    private PopupWindow mPopupWindow,mPopupWindow2;

    View customView,customView2;
    LinearLayout my_account_layout,call_yes,call_no,email_yes,email_no;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DrawerLayout drawer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String REQUEST_URL="http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/contact.php";
    NavigationView navigationView;
    TextView nav_drawer_name, nav_drawer_address;
    ImageView navOpen, nav_image,prev_image;
    private String loged_id, loged_username, loged_name, loged_email, loged_location, loged_phone;
    SessionManager sm;
    String con_id,phone1,address1,email1;
    boolean isConnected=false;
    Snackbar snackbar;

 /*   ArrayList<Contact_us_Model> contact1=new ArrayList<>();*/
    LinearLayout contactus_email_box,contactus_call_box;
    TextView address,phone,email,about_us_txt_view,heading;
    private ProgressDialog pDialog;
    CheckAndRequestPermission cp;
    // ArrayList<HashMap<String, String>> contactlist;
    JSONArray contact=null;


    private static String TAG_phone="phone";
    private static String TAG_email="email";
    private static String TAG_address="address";

    private OnFragmentInteractionListener mListener;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
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
      /*  HomeMainActivityNew.drawer.setBackgroundResource(R.color.white);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View v=inflater.inflate(R.layout.content_contact_us_new, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        address=(TextView)v.findViewById(R.id.contact_us_address);
        phone=(TextView)v.findViewById(R.id.contact_us_phone);
        email=(TextView)v.findViewById(R.id.contact_us_email);
        contactus_call_box=(LinearLayout)v.findViewById(R.id.contactus_call_box);
        contactus_email_box=(LinearLayout)v.findViewById(R.id.contactus_email_box);


        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);

        address.setTypeface(myFont1);
        phone.setTypeface(myFont1);
        email.setTypeface(myFont1);

        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.VISIBLE);

        cp = new CheckAndRequestPermission();

        ConnectionCheck();
        if(isConnected) {
            new Getcontact().execute();
        }
        else{
            /*Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();*/
            Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
        }



//        navOpen=(ImageView)v.findViewById(R.id.nav_open_contact);
//        navOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                HomeMainActivityNew.drawer.openDrawer(HomeMainActivityNew.navigationView);
//            }
//        });


        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflator.inflate(R.layout.popup_call_request, null);
        try {

            call_yes = (LinearLayout) customView.findViewById(R.id.call_yes);
            call_no = (LinearLayout) customView.findViewById(R.id.call_no);



        }catch (Exception e){
            e.printStackTrace();
        }


        LayoutInflater inflator2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView2 = inflator2.inflate(R.layout.popup_email_request, null);
        try {

            email_yes = (LinearLayout) customView2.findViewById(R.id.email_yes);
            email_no = (LinearLayout) customView2.findViewById(R.id.email_no);



        }catch (Exception e){
            e.printStackTrace();
        }



        my_account_layout = (LinearLayout) v.findViewById(R.id.contactus_fragment_layout);


        contactus_call_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean chk = cp.checkAndRequestPermissions(getActivity());
                if (chk) {


                    displayPopup();




                }else {
                    /*Toast.makeText(getActivity(),"You must grant permission to call", Toast.LENGTH_SHORT).show();*/
                    Toasty.info(getActivity(), "You must grant permission to call", Toast.LENGTH_LONG, true).show();

                }
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

        contactus_email_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    displayPopup2();




            }
        });





        email_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow2.dismiss();
            }
        });

        email_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent emailIntent =new Intent(Intent.ACTION_SEND);

                String emailList[]={email.getText().toString()};

                emailIntent.putExtra(Intent.EXTRA_EMAIL,emailList);
/*                emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");*/

                /*emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My subject");*/

                emailIntent.setType("plain/text");
                /*emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My message body.");*/


                try{
                    mPopupWindow2.dismiss();
                    startActivity(emailIntent);

                }catch (Exception e){
                    e.printStackTrace();
                }


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
    private class Getcontact extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/contact.php");
            System.out.println("______________________________________________________");
            System.out.println("jsonStr : "+jsonStr);
            System.out.println("______________________________________________________");
            ConnectionCheck();
            if(!isConnected){
                /*Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
            }
            else {
                if (jsonStr != null) {
                    try {
                        // Getting JSON Array node
                        JSONArray jsonarray = new JSONArray(jsonStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            con_id = jsonobject.getString("con_id");
                            phone1 = jsonobject.getString("phone");
                            email1 = jsonobject.getString("email");
                            address1 = jsonobject.getString("address");
                            str_phone_number=phone1;
                            HashMap<String, String> contact = new HashMap<>();

                        }

                    } catch (final JSONException e) {

                        /*Toast.makeText(getActivity(),
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

                    Toasty.info(getActivity(), "Something went wrong.Please try again", Toast.LENGTH_LONG, true).show();

                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(false);
            ConnectionCheck();
            if(!isConnected){
                /*Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
            }
            else {
               /* pDialog.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
            }

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);
            try {
            phone.setText(phone1);
            email.setText(email1);
            address.setText(address1);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();


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

    public void displayPopup2()
    {
        try {


            mPopupWindow2 = new PopupWindow(customView2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow2.setElevation(5.0f);
            }
            mPopupWindow2.setFocusable(true);
            mPopupWindow2.setAnimationStyle(R.style.popupAnimation);

           /* document_pass_check_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow2.dismiss();
                }
            });*/
            mPopupWindow2.showAtLocation(my_account_layout, Gravity.CENTER, 0, 0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
