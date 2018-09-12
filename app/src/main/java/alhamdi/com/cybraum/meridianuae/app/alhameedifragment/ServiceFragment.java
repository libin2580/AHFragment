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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DrawerLayout drawer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean isConnected=false;
    Snackbar snackbar;

    private OnFragmentInteractionListener mListener;
    TextView first_box_textview,second_box_textview,third_box_textview,fourth_box_textview,fifth_box_textview,heading;
    TextView first_box_textview_ID,second_box_textview_ID,third_box_textview_ID,fourth_box_textview_ID,fifth_box_textview_ID;


    LinearLayout first_box,second_box,third_box,fourth_box,fifth_box,service_content;
    String cat_id[],cat_name[];

    ProgressDialog pDlog;
    public ServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
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
        View v= inflater.inflate(R.layout.fragment_service, container, false);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);


        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        service_content=(LinearLayout)v.findViewById(R.id.service_content);

        first_box=(LinearLayout)v.findViewById(R.id.first_box);
        second_box=(LinearLayout)v.findViewById(R.id.second_box);
        third_box=(LinearLayout)v.findViewById(R.id.third_box);
        fourth_box=(LinearLayout)v.findViewById(R.id.fourth_box);
        fifth_box=(LinearLayout)v.findViewById(R.id.fifth_box);

        first_box_textview=(TextView) v.findViewById(R.id.first_box_textview);
        second_box_textview=(TextView) v.findViewById(R.id.second_box_textview);
        third_box_textview=(TextView) v.findViewById(R.id.third_box_textview);
        fourth_box_textview=(TextView) v.findViewById(R.id.fourth_box_textview);
        fifth_box_textview=(TextView) v.findViewById(R.id.fifth_box_textview);

        first_box_textview_ID=(TextView) v.findViewById(R.id.first_box_textview_ID);
        second_box_textview_ID=(TextView) v.findViewById(R.id.second_box_textview_ID);
        third_box_textview_ID=(TextView) v.findViewById(R.id.third_box_textview_ID);
        fourth_box_textview_ID=(TextView) v.findViewById(R.id.fourth_box_textview_ID);
        fifth_box_textview_ID=(TextView) v.findViewById(R.id.fifth_box_textview_ID);


        ConnectionCheck();
        if(isConnected) {
            new FetchTextValues().execute();
        }
        else{
            ShowSnackbar();
        }




        first_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String val=first_box_textview_ID.getText().toString();
                /*Toast.makeText(getApplicationContext(),"First box clicked : "+val,Toast.LENGTH_SHORT).show();*/
                Bundle args=new Bundle();
                args.putString("val",val);
                ServiceDetailsFragment sf=new ServiceDetailsFragment();
                sf.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,sf).addToBackStack("").commit();


            }
        });

        second_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val=second_box_textview_ID.getText().toString();
                /*Toast.makeText(getApplicationContext(),"Second box clicked : "+val,Toast.LENGTH_SHORT).show();*/
                Bundle args=new Bundle();
                args.putString("val",val);
                ServiceDetailsFragment sf=new ServiceDetailsFragment();
                sf.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,sf).addToBackStack("").commit();

            }
        });

        third_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val=third_box_textview_ID.getText().toString();
                /*Toast.makeText(getApplicationContext(),"Third box clicked : "+val,Toast.LENGTH_SHORT).show();*/
                Bundle args=new Bundle();
                args.putString("val",val);
                ServiceDetailsFragment sf=new ServiceDetailsFragment();
                sf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.frame,sf).addToBackStack("").commit();

            }
        });

        fourth_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val=fourth_box_textview_ID.getText().toString();
                /*Toast.makeText(getApplicationContext(),"Fourth box clicked : "+val,Toast.LENGTH_SHORT).show();*/
                Bundle args=new Bundle();
                args.putString("val",val);
                ServiceDetailsFragment sf=new ServiceDetailsFragment();
                sf.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,sf).addToBackStack("").commit();


            }
        });
        fifth_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val=fifth_box_textview_ID.getText().toString();
                /*Toast.makeText(getApplicationContext(),"Fifth box clicked : "+val,Toast.LENGTH_SHORT).show();*/
                Bundle args=new Bundle();
                args.putString("val",val);
                ServiceDetailsFragment sf=new ServiceDetailsFragment();
                sf.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,sf).addToBackStack("").commit();


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
    class FetchTextValues extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDlog.setCancelable(false);*/
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
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/allservices.php");
            if (jsonString != null) {
                try {
                    JSONArray valueArray = new JSONArray(jsonString);
                    cat_id=new String[valueArray.length()];
                    cat_name=new String[valueArray.length()];

                    for (int i = valueArray.length()-1; i >= 0; i--) {
                        JSONObject jsonData = valueArray.getJSONObject(i);
                        cat_id[i]=jsonData.getString("cat_id");
                        cat_name[i]=jsonData.getString("cat_name");

                        System.out.println("***************************************************");
                        System.out.println(" i : " + i+ "\n");
                        System.out.println(" cat_id : " + jsonData.getString("cat_id") + "\n");
                        System.out.println("cat_name : " + jsonData.getString("cat_name"));
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
    Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
            first_box_textview.setText(cat_name[4].toString());
            first_box_textview_ID.setText(cat_id[4].toString());
    first_box_textview.setTypeface(myFont1);


            second_box_textview.setText(cat_name[3].toString());
            second_box_textview_ID.setText(cat_id[3].toString());
    second_box_textview.setTypeface(myFont1);

            third_box_textview.setText(cat_name[2].toString());
            third_box_textview_ID.setText(cat_id[2].toString());
    third_box_textview.setTypeface(myFont1);

            fourth_box_textview.setText(cat_name[1].toString());
            fourth_box_textview_ID.setText(cat_id[1].toString());
    fourth_box_textview.setTypeface(myFont1);

            fifth_box_textview.setText(cat_name[0].toString());
            fifth_box_textview_ID.setText(cat_id[0].toString());
    fifth_box_textview.setTypeface(myFont1);
}catch (Exception e){
    e.printStackTrace();
}
ConnectionCheck();
            if(isConnected) {
                service_content.setVisibility(View.VISIBLE);
            }else {


            }
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

        }
    }

    /* CHECKING INTERNET CONNECTION -- START*/
    public boolean ConnectionCheck(){
        try {


            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  isConnected;
    }
/* CHECKING INTERNET CONNECTION -- end*/

    public void ShowSnackbar(){
        snackbar=Snackbar.make(drawer,"No internet connection",Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        new FetchTextValues().execute();
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
