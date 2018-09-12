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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters.MyDocumentAdapter;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.MyDocumentModel;

import static alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R.id.my_documents_recycler_view;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyDocumentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDocumentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean isConnected=false;
    Snackbar snackbar;
    public static DrawerLayout drawer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<MyDocumentModel> arrayVal;
private RecyclerView recyclerviewMyDocuments;
    private OnFragmentInteractionListener mListener;
    MyDocumentAdapter mda;
    TextView heading;
    TextView empty_msg;
    private int flag=0;


    ProgressDialog pDlog;
    SessionManager sm;
    private String stored_user_id;
    private String json_id,json_user_id,json_title,json_url,json_doc_id;

    public MyDocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDocumentFragment newInstance(String param1, String param2) {
        MyDocumentFragment fragment = new MyDocumentFragment();
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

        arrayVal=new ArrayList<MyDocumentModel>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_document, container, false);
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");



        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        heading=(TextView)v.findViewById(R.id.heading);
        heading.setTypeface(myFont1);
        empty_msg=(TextView)v.findViewById(R.id.document_empty_msg);
        recyclerviewMyDocuments=(RecyclerView)v.findViewById(my_documents_recycler_view);
        recyclerviewMyDocuments.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerviewMyDocuments.setLayoutManager(layoutManager);
/*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/

        ConnectionCheck();
        if(isConnected) {
            recyclerviewMyDocuments.setVisibility(View.VISIBLE);
        new FetchDocumentDetails().execute();
        }
        else{
            recyclerviewMyDocuments.setVisibility(View.GONE);

            ShowSnackbar();
            /*Toast.makeText(getActivity(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
        }


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

    public class FetchDocumentDetails extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDlog = new ProgressDialog(getActivity(), R.style.MyTheme);

                pDlog.setCancelable(false);
                ConnectionCheck();
                if (!isConnected) {
                    recyclerviewMyDocuments.setVisibility(View.GONE);
                    ShowSnackbar();
                } else {
                    recyclerviewMyDocuments.setVisibility(View.VISIBLE);
                    /*pDlog.show();*/
                    HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                    HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {


                HttpHandler h = new HttpHandler();
                String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/document.php?user_id=" + stored_user_id);
                if (jsonString != null) {
                    arrayVal.clear();
                    try {
                        JSONArray imageArray = new JSONArray(jsonString);


                        for (int i = 0; i < imageArray.length(); i++) {
                            JSONObject jsonData = imageArray.getJSONObject(i);
                            json_id = jsonData.getString("id");
                            json_user_id = jsonData.getString("user_id");
                            json_title = jsonData.getString("title");
                            json_doc_id = jsonData.getString("doc_id");
                     /*   json_url=jsonData.getString("url");*/
                            MyDocumentModel mm = new MyDocumentModel();
                            mm.setId(json_id);
                            mm.setUser_id(json_user_id);
                            mm.setTitle(json_title);
                            mm.setDoc_id(json_doc_id);
                        /*mm.setUrl(json_url);*/

                            arrayVal.add(mm);
                            System.out.println("***************************************************");
                            System.out.println(" json_id : " + jsonData.getString("id") + "\n");
                            System.out.println("json_title : " + jsonData.getString("title"));
                            System.out.println("***************************************************");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (jsonString.equals("")) {
                    flag = 1;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(flag==1){
                recyclerviewMyDocuments.setVisibility(View.GONE);
                empty_msg.setVisibility(View.VISIBLE);
                System.out.println("inside flag1");
            }
            else {
                try {
                    empty_msg.setVisibility(View.GONE);
                    recyclerviewMyDocuments.setVisibility(View.VISIBLE);
                    mda = new MyDocumentAdapter(getActivity(), arrayVal);
                    recyclerviewMyDocuments.setAdapter(mda);

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
        snackbar= Snackbar.make(drawer,"No internet connection",Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        new FetchDocumentDetails().execute();
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
}
