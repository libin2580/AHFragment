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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView  service_details_heading;
    WebView service_details_description;
    TextView sent_enquiry;
    ImageView service_image_view;
    ProgressDialog pDlog;
    String service_id,service_name,ser_image,description;
    TextView service_details_sent_btn_txt_view;
    boolean isConnected=false;
    public static DrawerLayout drawer;
    Snackbar snackbar;
    ProgressBar service_details_progressbar;
    String id;
    LinearLayout sent_enquiry_button;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceDetailsFragment newInstance(String param1, String param2) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
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
        View v= inflater.inflate(R.layout.fragment_service_details, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;

        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);
        service_image_view=(ImageView)v.findViewById(R.id.service_image_view);
        sent_enquiry_button=(LinearLayout)v.findViewById(R.id.sent_enquiry_button);
        service_details_description=(WebView) v.findViewById(R.id.service_details_description);
        service_details_sent_btn_txt_view =(TextView)v.findViewById(R.id.service_details_sent_btn_txt_view);
        service_details_heading=(TextView)v.findViewById(R.id.service_details_heading);
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");


        service_details_heading.setTypeface(myFont1);
        service_details_sent_btn_txt_view.setTypeface(myFont1);

        service_details_progressbar=(ProgressBar)v.findViewById(R.id.service_details_progressbar);

       /* service_details_description.setTypeface(myFont1);*/


        id=getArguments().getString("val");
        ConnectionCheck();
        if(isConnected) {
            new FetchServiceDetails(id).execute();
        }
        else{
            ShowSnackbar();
        }

        sent_enquiry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnquiryFragment eq=new EnquiryFragment();
                Bundle enquirybundle = new Bundle();
                enquirybundle.putSerializable("from", "servicedetails");
                eq.setArguments(enquirybundle);

                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,eq).addToBackStack("").commit();

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

    class FetchServiceDetails extends AsyncTask<String,String,String> {
        String value_id;
        public FetchServiceDetails(String id){
            value_id=id;
        }

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
              /*  pDlog.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/service_detail.php?cat_id="+value_id);
            if (jsonString != null) {
                try {
                    JSONArray valueArray = new JSONArray(jsonString);

                    JSONObject jsonData = valueArray.getJSONObject(0);
                    service_id=jsonData.getString("service_id");
                    service_name=jsonData.getString("service_name");
                    ser_image=jsonData.getString("ser_image");
                    description=jsonData.getString("description");


                    System.out.println("***************************************************");
                    System.out.println(" service_name : " + jsonData.getString("service_name") + "\n");
                    System.out.println(" ser_image : " + jsonData.getString("ser_image"));
                    System.out.println("***************************************************");


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

                service_details_heading.setText(service_name);
                Glide.with(getActivity())
                        .load(ser_image)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                service_details_progressbar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(service_image_view);
                /*service_details_description.getSettings().setJavaScriptEnabled(true);*/
                service_details_description.setBackgroundColor(0x00000000);

                String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";

                service_details_description.loadDataWithBaseURL("", String.format(text,description), "text/html", "UTF-8", "");


            }catch (Exception e){
                e.printStackTrace();
            }
/*

            if(pDlog.isShowing())
                pDlog.dismiss();
*/
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
                        new FetchServiceDetails(id).execute();
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
