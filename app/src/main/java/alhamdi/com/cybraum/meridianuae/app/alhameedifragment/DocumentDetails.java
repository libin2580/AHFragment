package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters.DocumentDetailsAdapter;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.httphandler.HttpHandler;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.image_uploading.Vis_Upload;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.DocumentDetailsModel;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DocumentDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DocumentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentDetails extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static DrawerLayout drawer;
    Snackbar snackbar;
    private PopupWindow mPopupWindow;
    boolean isConnected=false;
    ProgressDialog pDlog,pDlog2,pDlog3,pDlog4;
    String doc_id="",doc_title="",stored_user_id="",str_new_doc_title;
    TextView heading,no_doc_found;
    int flag=0,delete_id=0;
    LinearLayout add_new_document,edit_doc_title,change_document_title,share_specific_document;
    ArrayList<String> FileUriArrayList;
    DocumentDetailsAdapter dda;

    ArrayList<DocumentDetailsModel>DocDetailsArraylist;
    LinearLayout my_account_layout;
    View customView;
    RecyclerView document_details_recycler_view;
    SessionManager sm;
    EditText new_document_title;
    TextView document_title_status;
    ImageButton document_title_change_close;
    String[] image_url_for_popup,url_from_popup;
    ProgressBar popup_progressbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DocumentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentDetails newInstance(String param1, String param2) {
        DocumentDetails fragment = new DocumentDetails();
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
        View v= inflater.inflate(R.layout.fragment_document_details, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        } else {
           /* Snackbar.with(g, null)
                    .type(Type.SUCCESS)
                    .message("You already granted permission!")
                    .duration(Duration.SHORT)
                    .show();*/
        }

        HomeMainActivityNew.doubleBackToExitPressedOnce = false;
        Typeface myFont1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        drawer= (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.signin_bg);
        HomeMainActivityNew.user_icon.setVisibility(View.GONE);
        HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_black_24dp);
        HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon2.setVisibility(View.VISIBLE);
        HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

        heading=(TextView)v.findViewById(R.id.heading);
        no_doc_found=(TextView)v.findViewById(R.id.no_doc_found);

        my_account_layout = (LinearLayout) v.findViewById(R.id.my_account_layout);

        DocDetailsArraylist=new ArrayList<>();
        add_new_document=(LinearLayout)v.findViewById(R.id.add_new_document);
        edit_doc_title=(LinearLayout)v.findViewById(R.id.edit_doc_title) ;
        share_specific_document=(LinearLayout)v.findViewById(R.id.share_specific_document);


          /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        /*getting user id end*/


        FileUriArrayList=new ArrayList<>();
        FileUriArrayList.clear();
        /*getting doc_id start*/

        Bundle bundle=this.getArguments();
        if(bundle!=null) {
            doc_id = bundle.getString("doc_id");
            doc_title = bundle.getString("doc_title");
        }


        if(doc_title!="")
        {
            heading.setText(doc_title);
        }
        document_details_recycler_view=(RecyclerView)v.findViewById(R.id.document_details_recycler_view);
        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getActivity(),2);
        document_details_recycler_view.setLayoutManager(mLayoutManager);
        document_details_recycler_view.setItemAnimator(new DefaultItemAnimator());




        ConnectionCheck();
        if(isConnected) {
            new FetchSpecificDocuments().execute();
        }
        else{

            ShowSnackbar();
            /*Toast.makeText(getActivity(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
        }

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflator.inflate(R.layout.popup_change_doc_title, null);
        try {
            new_document_title = (EditText) customView.findViewById(R.id.new_document_title);
            change_document_title = (LinearLayout) customView.findViewById(R.id.change_document_title);
            document_title_status = (TextView) customView.findViewById(R.id.document_title_status);
            popup_progressbar=(ProgressBar)customView.findViewById(R.id.popup_progressbar);
            new_document_title.setTypeface(myFont1);
            document_title_status.setTypeface(myFont1);



        }catch (Exception e){
            e.printStackTrace();
        }








        add_new_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

showFileChooser();

            }
        });

        edit_doc_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayPopup();
            }
        });
        change_document_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    str_new_doc_title=new_document_title.getText().toString();
                    if (str_new_doc_title.isEmpty()||str_new_doc_title=="") {


                        document_title_status.setText("Title field can't be empty");
                        document_title_status.setVisibility(View.VISIBLE);
                    }
                    else if(str_new_doc_title.length()>25){
                        document_title_status.setText("Document title length can't exceed 25 characters");
                        document_title_status.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                       /* if (!android.util.Patterns.EMAIL_ADDRESS.matcher(str_password.trim()).matches()) {
                            status.setText("Invalied e-mail");
                            status.setVisibility(View.VISIBLE);
                        }
                        else {*/
                        document_title_status.setVisibility(View.GONE);

                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        if (ConnectionCheck()) {
                            ChangeDocTitle(str_new_doc_title);
                        } else {

                          /*  final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Alert");

                            alertDialog.setMessage("No Internet");


                            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();


                                }
                            });
                            alertDialog.show();*/
                            Toasty.info(getActivity(), "Something went wrong.\nCheck your network connection", Toast.LENGTH_LONG, true).show();
                        }


                       /* }*/
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        share_specific_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionCheck()) {




                    Intent m=new Intent(getActivity(),share_documents_activity.class);
                    m.putExtra("checked_items",image_url_for_popup);
                    startActivity(m);



                } else {

                    final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert");

                    alertDialog.setMessage("No Internet");


                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });
                    alertDialog.show();



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
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    class FetchSpecificDocuments extends AsyncTask<String,String,String> {

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
            System.out.println("////////////////////////////////////   doc_id : "+doc_id);
            HttpHandler h = new HttpHandler();
            String jsonString = h.makeServiceCall("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/document_images.php?doc_id="+doc_id);

            if (jsonString != null) {
                flag=0;
                try {
                    JSONArray valueArray = new JSONArray(jsonString);

                    image_url_for_popup=new String[valueArray.length()];
                    Arrays.fill( image_url_for_popup, null );
                    url_from_popup=new String[valueArray.length()];
                    for (int i = 0; i <valueArray.length(); i++) {
                        JSONObject jsonData = valueArray.getJSONObject(i);
                        DocumentDetailsModel ddm=new DocumentDetailsModel();
                        ddm.setId(jsonData.getString("id"));
                      /*  ddm.setTitle_id(jsonData.getString("title_id"));*/
                        ddm.setImage_url(jsonData.getString("image"));

                        image_url_for_popup[i]=jsonData.getString("image");

                        delete_id=Integer.parseInt(jsonData.getString("id"));

                        DocDetailsArraylist.add(ddm);

                        System.out.println("***************************************************");
                        System.out.println( jsonData.getString("id") + "\n");
                      /*  System.out.println( jsonData.getString("title_id") + "\n");*/
                        System.out.println( jsonData.getString("image") + "\n");
                        System.out.println("***************************************************");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(jsonString.equals("")){
                flag=1;
            }}
           catch (Exception e){
               e.printStackTrace();
           }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println(" flag : "+flag);
            if(flag==1){
                document_details_recycler_view.setVisibility(View.GONE);
                no_doc_found.setVisibility(View.VISIBLE);
                System.out.println("inside error");
            }
            else {
                try {
                    no_doc_found.setVisibility(View.GONE);
                    document_details_recycler_view.setVisibility(View.VISIBLE);
                    dda = new DocumentDetailsAdapter(getActivity(), DocDetailsArraylist,stored_user_id);
                    document_details_recycler_view.setAdapter(dda);

//popup image selection adapter filling start
                    /*popupadapter = new Doc_popup_adapter(getActivity(), image_url_for_popup);
                    popupRecyclerView.setAdapter(popupadapter);*/

//popup image selection adapter filling end


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




           /* if(pDlog.isShowing())
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
                        new FetchSpecificDocuments().execute();
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
   /* class SentDeleteRequest extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDlog2 = new ProgressDialog(getActivity(),R.style.MyTheme);

            pDlog2.setCancelable(false);
            pDlog2.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {



                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/delete_document.php");
                System.out.println("____________________________________________________________________");
                System.out.println("Inside SentToServer ");
                System.out.println("user id : "+stored_user_id);
                System.out.println("delete_id : "+delete_id);
                System.out.println("____________________________________________________________________");
                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("id",delete_id);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 *//* milliseconds *//*);
                conn.setConnectTimeout(15000 *//* milliseconds *//*);
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
            if(pDlog2.isShowing())
                pDlog2.dismiss();

            *//*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*//*
            if (result.contentEquals("\"success\"")){

                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStackImmediate();

            }
            else
            {
                Toast.makeText(getActivity(),"Failed.Try again later.",Toast.LENGTH_LONG).show();

            }
        }
    }*/
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
    public void showFileChooser() {

        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/




    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
       /* pDlog3=new ProgressDialog(getActivity(),R.style.MyTheme);
        pDlog3.setCancelable(false);
        pDlog3.show();*/
        HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
        HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

        try {
            if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                //The array list has the image paths of the selected images
                ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                StringBuffer stringBuffer = new StringBuffer();
                FileUriArrayList.clear();
                FileUriArrayList = new ArrayList<>(images.size());

                for (int i = 0, l = images.size(); i < l; i++) {
                    stringBuffer.append(images.get(i).path + "\n");
                    FileUriArrayList.add(i, images.get(i).path);
                }
                System.out.println("stringBuffer : " + stringBuffer.toString());

            }

            for (int k = 0; k < FileUriArrayList.size(); k++) {
                System.out.println("--------------------------" + FileUriArrayList.get(k) + "-----------------------------");

            }
            if(FileUriArrayList.size()!=0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Are you sure to add these new files ?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    if (ConnectionCheck()) {

                        System.out.println("filename (before caling uploadVideo1())   : " + FileUriArrayList.size());




                        uploadVideo1(FileUriArrayList);


                    } else {

                        final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Alert");

                        alertDialog.setMessage("No Internet");


                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                            }
                        });
                        alertDialog.show();


                    }

                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FileUriArrayList.clear();
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }

        }
        catch (Exception e){
            e.printStackTrace();
        }
       /* if(pDlog3.isShowing())
            pDlog3.dismiss();*/
        HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
        HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);


    }
    private void uploadVideo1(final ArrayList<String> mArrayUri1 ) {
    class UploadVideo extends AsyncTask<Void, Void, String> {

        ArrayList<String> mArrayUri2;


        String Serverresponse="";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog2=new ProgressDialog(getActivity(),R.style.MyTheme);
            pDlog2.setCancelable(false);
            pDlog2.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

            mArrayUri2=FileUriArrayList;



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if(pDlog2.isShowing())
                pDlog2.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            if(s.contentEquals("\"success\"")){

                ConnectionCheck();
                if(isConnected) {
                    DocDetailsArraylist.clear();
                    new FetchSpecificDocuments().execute();
                }
                else{

                    ShowSnackbar();
            /*Toast.makeText(getActivity(),"Please enable internet connection.",Toast.LENGTH_SHORT).show();*/
                }
                /*Toast.makeText(getActivity(),"New Documents Added",Toast.LENGTH_LONG).show();*/
                Toasty.success(getActivity(),mArrayUri2.size()+" New Documents Added", Toast.LENGTH_LONG, true).show();

            }
            else {
                /*Toast.makeText(getActivity(),"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                Toasty.error(getActivity(), "Failed.Try again later", Toast.LENGTH_LONG, true).show();
            }
            // textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
            // textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        protected String doInBackground(Void... params) {
            String charset = "UTF-8";

            String requestURL = "http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/edit_document.php";

            try {


                String file_uris[]=new String[mArrayUri2.size()];
                for(int i=0;i<mArrayUri2.size();i++) {
                    Serverresponse="";


                    Vis_Upload multipart = new Vis_Upload(requestURL, charset);

                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");

                    multipart.addFormField("user_id", stored_user_id);
                    multipart.addFormField("doc_id", doc_id);
                  /*  multipart.addFormField("title1", "new title");*/


                    file_uris[i]=mArrayUri2.get(i).toString();


                    /*File file=new File(mArrayUri2.get(i));*/
                    multipart.addFilePart("photo", file_uris[i],mArrayUri2.size());
                    List<String> response = multipart.finish();

                    System.out.println("SERVER REPLIED:");

                    for (String line : response) {
                        System.out.println(line);
                        Serverresponse+=line;
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Serverresponse:"+Serverresponse);
            return  Serverresponse;
        }
    }
    UploadVideo uv = new UploadVideo();
    uv.execute();
}
    public void displayPopup()
    {
        try {
            doc_title=heading.getText().toString();
            new_document_title.setText(doc_title.trim());
            document_title_status.setVisibility(View.GONE);
            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);



            document_title_change_close = (ImageButton) customView.findViewById(R.id.document_title_change_close);
            document_title_change_close.setOnClickListener(new View.OnClickListener() {
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
   /* public void displayImageSelectionPopup()
    {
        try {

            popupwindowimageselection = new PopupWindow(customViewImageSelction, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                popupwindowimageselection.setElevation(5.0f);
            }
            popupwindowimageselection.setFocusable(true);
            popupwindowimageselection.setAnimationStyle(R.style.popupAnimation);



            document_image_selection_close = (ImageButton) customViewImageSelction.findViewById(R.id.document_image_selection_close);
            document_image_selection_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupwindowimageselection.dismiss();
                }
            });


            popupwindowimageselection.showAtLocation(my_account_layout, Gravity.CENTER, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    private void ChangeDocTitle(final String new_title ) {
        class UploadnewTitle extends AsyncTask<Void, Void, String> {

           String title;




            String Serverresponse="";



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               /* pDlog4=new ProgressDialog(getActivity(),R.style.MyTheme);
                pDlog4.setCancelable(false);
                pDlog4.show();*/
                /*HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
                HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);*/

                popup_progressbar.setVisibility(View.VISIBLE);
                change_document_title.setVisibility(View.GONE);

                title=new_title;
                System.out.println("new_title : "+new_title.trim());



            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                /*if(pDlog4.isShowing())
                    pDlog4.dismiss();*/
                /*HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);*/
                popup_progressbar.setVisibility(View.GONE);
                change_document_title.setVisibility(View.VISIBLE);

                if(s.contentEquals("\"success\"")){


                    heading.setText(title);
                    mPopupWindow.dismiss();

                    /*Toast.makeText(getActivity(),"Documents Title Updated",Toast.LENGTH_LONG).show();*/
                    Toasty.success(getActivity(), "Document Title Updated", Toast.LENGTH_LONG, true).show();
                }
                else {
                   /* Toast.makeText(getActivity(),"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                    Toasty.error(getActivity(), "Failed.Try again later", Toast.LENGTH_LONG, true).show();

                }
                // textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                // textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                String charset = "UTF-8";

                String requestURL = "http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/edit_document.php";

                try {



                        Serverresponse="";


                        Vis_Upload multipart = new Vis_Upload(requestURL, charset);

                        multipart.addHeaderField("User-Agent", "CodeJava");
                        multipart.addHeaderField("Test-Header", "Header-Value");

                        multipart.addFormField("user_id", stored_user_id);
                        multipart.addFormField("doc_id", doc_id);
                  multipart.addFormField("title1", title);

                        List<String> response = multipart.finish();

                        System.out.println("SERVER REPLIED:");

                        for (String line : response) {
                            System.out.println(line);
                            Serverresponse+=line;
                        }

                } catch (IOException ex) {
                    System.err.println(ex);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("Serverresponse:"+Serverresponse);
                return  Serverresponse;
            }
        }
        UploadnewTitle uv = new UploadnewTitle();
        uv.execute();
    }
}
