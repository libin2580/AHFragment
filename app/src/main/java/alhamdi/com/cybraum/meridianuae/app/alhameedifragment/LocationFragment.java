package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.permission.CheckAndRequestPermission;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    SessionManager sm;

    public static DrawerLayout drawer;
    LinearLayout myLocation,searchLocation;
    ProgressDialog prgDlg;
    double fetched_latitude,fetched_longitude;
    TextView location_name;

    String addressLine="",locality="",AdminArea="",countryName="",postalcode="",getfeatureName="";
    boolean isConnected=false;
    Snackbar snackbar;
    GoogleMap mGoogleMap;
    MapView mapView;

    View mView;

    TextView heading;

GoogleMap gmap;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
public CheckAndRequestPermission cp=new CheckAndRequestPermission();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
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


        mView = inflater.inflate(R.layout.fragment_location, container, false);
        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.VISIBLE);

        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        heading=(TextView)mView.findViewById(R.id.heading);
        heading.setTypeface(myFont1);



        //this is important
       /* searchLocation=(LinearLayout)findViewById(R.id.search_location);*/
        location_name=(TextView)mView.findViewById(R.id.location_name);
        myLocation=(LinearLayout)mView.findViewById(R.id.my_location);
        mapView = (MapView) mView.findViewById(R.id.map);
        location_name.setTypeface(myFont1);
       cp.checkAndRequestPermissions(getActivity());


        if (mapView != null) {
            // Initialise the MapView
            mapView.onCreate(null);
            mapView.onResume();
            // Set the map ready callback to receive the GoogleMap object
            mapView.getMapAsync(this);
        }




        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(fetched_latitude,fetched_longitude)).zoom(17).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return mView;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        mGoogleMap=googleMap;
        ConnectionCheck();
        if(isConnected) {
            new FetchDetails().execute();
        }
        else{
            /*Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();*/
            Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
        }

        System.out.println("__________________________inside on map ready start__________________________");
        System.out.println("latiutude :"+fetched_latitude);
        System.out.println("long :"+fetched_longitude);

        System.out.println("__________________________inside on map ready end__________________________");
        LatLng pos = new LatLng(fetched_latitude, fetched_longitude);
       /* mGoogleMap.addMarker(new MarkerOptions().position(pos));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(fetched_latitude,fetched_longitude)).zoom(17).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
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
    class FetchDetails extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDlg = new ProgressDialog(getActivity(), R.style.MyTheme);
            prgDlg.setCancelable(false);
            ConnectionCheck();
            if(!isConnected){
               /* Toast.makeText(getActivity(),"Please enable your internet connection",Toast.LENGTH_LONG).show();*/
                Toasty.info(getActivity(), "No internet connection", Toast.LENGTH_LONG, true).show();
            }
            else {
                /*prgDlg.show();*/
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/contact.php");
            if (jsonString != null) {
                try {
                    JSONArray imageArray = new JSONArray(jsonString);
                    for (int i = 0; i < imageArray.length(); i++) {
                        JSONObject jsonData = imageArray.getJSONObject(i);


                        fetched_latitude = Double.parseDouble(jsonData.getString("latitude"));
                        fetched_longitude = Double.parseDouble(jsonData.getString("longitude"));

                        System.out.println("***************************************************");
                        System.out.println(" fetched_latitude : " + jsonData.getString("latitude") + "\n");
                        System.out.println("fetched_longitude : " + jsonData.getString("longitude"));
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

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        fetched_latitude,
                        fetched_longitude,
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                ioException.printStackTrace();
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                illegalArgumentException.printStackTrace();
            }


try {
    if (addresses != null /*|| addresses.size()  != 0*/) {
        Address address = addresses.get(0);


        addressLine = address.getAddressLine(0);
        locality = address.getLocality();
        AdminArea = address.getAdminArea();
        countryName = address.getCountryName();
        postalcode = address.getPostalCode();
        getfeatureName = address.getFeatureName();

        System.out.println("\naddressLine=" + address.getAddressLine(0) +
                "\nlocality=" + address.getLocality() +
                "\nAdminArea=" + address.getAdminArea() +
                "\ncountryName=" + address.getCountryName() +
                "\npostalcode=" + address.getPostalCode() +
                "\ngetfeatureName=" + address.getFeatureName());


        location_name.setText("Mazaya Centre - Sheikh Zayed Collector Rd");// or 'addressLine'
    } else {

        location_name.setText("unable to find location");
        // Fetch the address lines using getAddressLine,
        // join them, and send them to the thread.


    }

    // Add a marker in Sydney and move the camera
    LatLng sydney = new LatLng(fetched_latitude, fetched_longitude);

    mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Mazaya Centre - Sheikh Zayed Collector Rd"));// or 'addressLine'
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(fetched_latitude, fetched_longitude)).zoom(17).build();
    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
}catch (Exception e){
    e.printStackTrace();
}
           /* if(prgDlg.isShowing())
                prgDlg.dismiss();*/
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





}
