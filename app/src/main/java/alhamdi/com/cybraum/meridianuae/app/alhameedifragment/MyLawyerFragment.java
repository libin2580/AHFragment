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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters.MyLawyerAdapter;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.MyLawyerModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyLawyerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyLawyerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLawyerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerViewMyLawyer;
    MyLawyerAdapter mlAdapter;
    boolean isConnected=false;
    public static DrawerLayout drawer;
    ProgressDialog pDlog;
    SessionManager sm;
    Snackbar snackbar;
    String str_user_id;
    ArrayList<MyLawyerModel> lawyerDetails;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView empty_msg;
    private int flag=0;
    View v;
    public MyLawyerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLawyerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLawyerFragment newInstance(String param1, String param2) {
        MyLawyerFragment fragment = new MyLawyerFragment();
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
         v= inflater.inflate(R.layout.fragment_my_lawyer, container, false);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        lawyerDetails=new ArrayList<MyLawyerModel>();

        empty_msg=(TextView)v.findViewById(R.id.lawyer_empty_msg);

        recyclerViewMyLawyer=(RecyclerView)v.findViewById(R.id.my_lawyer_recyclerview);
        recyclerViewMyLawyer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerViewMyLawyer.setLayoutManager(layoutManager);



    recyclerViewMyLawyer.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            try {
            v.findViewById(R.id.childScroll).getParent().requestDisallowInterceptTouchEvent(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    });



/*getting user id start*/
        sm=new SessionManager(getActivity());

        HashMap<String, String> details = sm.getUserDetails();
        str_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/

        ConnectionCheck();
        if(isConnected) {
            new FetchLawyerDetails().execute();
        }
        else{

            ShowSnackbar();
            /*Toast.makeText(getActivity(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
        }




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
    class FetchLawyerDetails extends AsyncTask<String,String,String> {

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
try{
            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/mylawyer.php");
            if (jsonString != null) {
                flag=0;
                try {
                    JSONArray valueArray = new JSONArray(jsonString);


                    for (int i = 0; i <valueArray.length(); i++) {
                        JSONObject jsonData = valueArray.getJSONObject(i);
                        MyLawyerModel mlm=new MyLawyerModel();
                        mlm.setLaw_id(jsonData.getString("law_id"));
                        mlm.setName(jsonData.getString("name"));
                        mlm.setEmail(jsonData.getString("email"));
                        mlm.setPhone(jsonData.getString("phone"));
                        mlm.setDepartment(jsonData.getString("department"));
                        mlm.setDescription(jsonData.getString("description"));
                        mlm.setImage(jsonData.getString("image"));
                        String ModifiedCase="";


                        try{
                        JSONArray arreyCases= jsonData.getJSONArray("cases");

                        if(arreyCases.length()!=0) {
                            for (int j = 0; j < arreyCases.length(); j++) {
                                ModifiedCase += " - " + arreyCases.getString(j) + "\n";
                            /*if(j!=arreyCases.length()-1)
                            {
                                ModifiedCase=" - "+ModifiedCase+"\n";
                            }*/
                            }
                        }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(ModifiedCase==""||ModifiedCase.equals("")||ModifiedCase==null){
                            ModifiedCase="( Case list empty )";
                        }

                        mlm.setCases(ModifiedCase);



                        lawyerDetails.add(mlm);



                        System.out.println("***************************************************");
                        System.out.println( jsonData.getString("law_id") + "\n");
                        System.out.println( jsonData.getString("name") + "\n");
                        System.out.println( jsonData.getString("email") + "\n");
                        System.out.println( jsonData.getString("phone") + "\n");
                        System.out.println( jsonData.getString("department") + "\n");
                        System.out.println( jsonData.getString("description") + "\n");
                        System.out.println( jsonData.getString("image") + "\n");
                        System.out.println( ModifiedCase + "\n");
                        System.out.println("***************************************************");

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
                recyclerViewMyLawyer.setVisibility(View.GONE);
                empty_msg.setVisibility(View.VISIBLE);
                System.out.println("inside flag1");
            }
            else {
                try {
                    mlAdapter = new MyLawyerAdapter(getActivity(), lawyerDetails);
                    recyclerViewMyLawyer.setAdapter(mlAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

          /*  if(pDlog.isShowing())
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
                        new FetchLawyerDetails().execute();
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
