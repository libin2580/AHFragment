package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int flag=0;
    String json_password,json_status;
    boolean isConnected=false;
    Snackbar snackbar;
    ProgressDialog pDlog;
    private String stored_user_id;

    public static DrawerLayout drawer;
    ImageView account_settings_profile_image;
    LinearLayout account_settings_box1,account_settings_box2,account_settings_box3,account_settings_box4,account_settings_box5,account_content_layout;
    TextView profile_name;
    SessionManager sm;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettingsFragment newInstance(String param1, String param2) {
        AccountSettingsFragment fragment = new AccountSettingsFragment();
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
        View v= inflater.inflate(R.layout.fragment_account_settings, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);
        account_settings_profile_image=(ImageView)v.findViewById(R.id.account_settings_profile_image);
        profile_name=(TextView) v.findViewById(R.id.account_settings_name);

        account_content_layout=(LinearLayout)v.findViewById(R.id.account_content_layout);

        /*account_settings_box1=(LinearLayout) v.findViewById(R.id.account_settings_first_box);*/
        account_settings_box2=(LinearLayout)v.findViewById(R.id.account_settings_second_box);
        account_settings_box3=(LinearLayout)v.findViewById(R.id.account_settings_third_box);
        account_settings_box4=(LinearLayout)v.findViewById(R.id.account_settings_fourth_box);
        account_settings_box5=(LinearLayout)v.findViewById(R.id.account_settings_fifth_box);
        /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        String str_name = details.get(sm.KEY_USER_NAME);
        stored_user_id = details.get(sm.KEY_ID);


        new checkIfLocked().execute();



        /*getting user id end*/
        profile_name.setText(str_name);


      /*  account_settings_box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        account_settings_box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        account_settings_box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword cp=new ChangePassword();
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,cp).addToBackStack("").commit();

            }
        });
        account_settings_box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaqFragment fq=new FaqFragment();
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,fq).addToBackStack("").commit();

            }
        });

        account_settings_box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDocumentFragment fq=new SetDocumentFragment();
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,fq).addToBackStack("").commit();

            }
        });

        account_settings_box5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeDocumentPassword cp=new ChangeDocumentPassword();
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,cp).addToBackStack("").commit();

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
    class checkIfLocked extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag=3;
            /*pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDlog.setCancelable(false);*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
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
                    System.out.println("json_password (inside checkIfLocked) : "+json_password);

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
               //hiding change password box

                account_settings_box5.setVisibility(View.GONE);
                account_settings_box2.setVisibility(View.VISIBLE);

                account_content_layout.setVisibility(View.VISIBLE);

            }
            if(flag==1)
            {
                //hiding create password box
                account_settings_box5.setVisibility(View.VISIBLE);
                account_settings_box2.setVisibility(View.GONE);

                account_content_layout.setVisibility(View.VISIBLE);

            }
            if(flag==3)
            {
                snackbar.show();
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
                        new checkIfLocked().execute();
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
