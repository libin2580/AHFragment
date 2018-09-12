package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters.GalleryAdapter;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.image_uploading.Vis_Upload;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddDocumentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDocumentFragment extends android.app.Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

    private GalleryAdapter mAdapter;


    private LinearLayout buttonChoose;
    private LinearLayout buttonUpload;
    SessionManager sm;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=100;
    private static final int MEDIA_TYPE_IMAGE=1;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int REQUEST_CODE = 1;
    String selectedFilePath;
    static String filename = "null", filenamepath = "null";
    boolean isConnected=false;
    String stored_user_id;
    private ImageView document_imageView;
    String uploadImage;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog pDlog1,pDlog2,pDlog;
    private Bitmap bitmap;
    EditText document_title;
    String str_document_title;
    RecyclerView rv1;
    TextView document_error_txt;
    ArrayList<String> FileUriArrayList;
    View v;
    String [] passingFileNames;
    String skiped;
    int arrFlag=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddDocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDocumentFragment newInstance(String param1, String param2) {
        AddDocumentFragment fragment = new AddDocumentFragment();
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
        v= inflater.inflate(R.layout.fragment_add_document, container, false);
       /* document_imageView=(ImageView)v.findViewById(R.id.document_imageView);*/

        buttonChoose = (LinearLayout) v.findViewById(R.id.buttonChoose);
        buttonUpload = (LinearLayout) v.findViewById(R.id.buttonUpload);
        document_title=(EditText)v.findViewById(R.id.document_title);

        /*buttonChoose.setTransformationMethod(null);*/

        document_error_txt=(TextView)v.findViewById(R.id.document_error_txt);

        rv1=(RecyclerView)v.findViewById(R.id.recyclerViewgallery);

        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getActivity(),2);
        rv1.setLayoutManager(mLayoutManager);
        rv1.setItemAnimator(new DefaultItemAnimator());

        FileUriArrayList=new ArrayList<>();
        FileUriArrayList.clear();
        passingFileNames=null;

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        /*getting user id start*/
        sm=new SessionManager(getActivity());
        HashMap<String, String> details = sm.getUserDetails();
        stored_user_id = details.get(sm.KEY_ID);
        skiped=details.get(sm.KEY_IS_SKIPED);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("stored user id : "+stored_user_id);
        /*getting user id end*/






        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (skiped.equalsIgnoreCase("true")) {
                    /*Toasty.info(getActivity(), "You must login to continue", Toast.LENGTH_SHORT, true).show();*/
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
                } else {

                    str_document_title = document_title.getText().toString();
                    checkAndRequestPermissions();

                    if (str_document_title.isEmpty() && FileUriArrayList.size() == 0) {
                        document_title.setError("enter document title");
                        rv1.setVisibility(View.GONE);
                        document_error_txt.setVisibility(View.VISIBLE);

                    }
                    if (str_document_title.isEmpty()) {
                        document_title.setError("enter document title");
                        rv1.setVisibility(View.VISIBLE);
                    }
                    if (FileUriArrayList.size() == 0) {


                        rv1.setVisibility(View.GONE);
                        document_error_txt.setVisibility(View.VISIBLE);
                    }

                    if (!str_document_title.isEmpty() && FileUriArrayList.size() != 0) {

                        if (str_document_title.length() > 25) {
                            document_title.setError("Document title length can't exceed 25 characters");
                        } else {

                            if (FileUriArrayList.size() != 0) {

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
                        }
                        //
                    }
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






    private void showFileChooser() {

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
        /*pDlog=new ProgressDialog(getActivity(),R.style.MyTheme);
        pDlog.setCancelable(false);
        pDlog.show();*/
        HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
        HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

        try{
            if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                //The array list has the image paths of the selected images
                ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                StringBuffer stringBuffer = new StringBuffer();
                FileUriArrayList.clear();
                FileUriArrayList=new ArrayList<>(images.size());

                for (int i = 0, l = images.size(); i < l; i++) {
                    stringBuffer.append(images.get(i).path + "\n");
                    FileUriArrayList.add(i, images.get(i).path);
                }
                System.out.println("stringBuffer : "+stringBuffer.toString());

            }

            for (int k=0;k<FileUriArrayList.size();k++)
            {
                System.out.println("--------------------------"+  FileUriArrayList.get(k)  +"-----------------------------");

            }
            mAdapter=new GalleryAdapter(getActivity(),FileUriArrayList);
            rv1.setAdapter(mAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*if(pDlog.isShowing())
            pDlog.dismiss();*/
        HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
        HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);


    }
    private void uploadVideo1(final ArrayList<String> mArrayUri1 ) {


        class UploadVideo extends AsyncTask<Void, Void, String> {

            ArrayList<String> mArrayUri2;
            Random generator = new Random();
            int rand = generator.nextInt(500) + 1;
            String doc_id;
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
                System.out.println("doc_id : " + stored_user_id + rand);
                doc_id=(stored_user_id  + rand).replaceAll("\\s+","");

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               /* if(pDlog2.isShowing())
                    pDlog2.dismiss();*/
                HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
                HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

                if(s.contentEquals("\"success\"")){

                    /*Toast.makeText(getActivity(),"Document Uploaded",Toast.LENGTH_LONG).show();*/
                    Toasty.success(getActivity(), "Document Uploaded", Toast.LENGTH_LONG, true).show();
                    getFragmentManager().popBackStackImmediate();
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

                String requestURL = "http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/add_document.php";

                try {


                    String file_uris[]=new String[mArrayUri2.size()];
                    for(int i=0;i<mArrayUri2.size();i++) {
                        Serverresponse="";


                        Vis_Upload multipart = new Vis_Upload(requestURL, charset);

                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");

                    multipart.addFormField("user_id", stored_user_id);
                    multipart.addFormField("title1", str_document_title);
                    multipart.addFormField("doc_id", doc_id);


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

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readstorage = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readstorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(getActivity(),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkAndRequestPermissions();
    }
    private void previewCapturedImage() {
        try{
            //  imgPreview.setVisibility(View.VISIBLE);
            BitmapFactory.Options options=new BitmapFactory.Options();
// downsizing image as it throws OutOfMemory Exception for larger
// images
            options.inSampleSize = 8;



            final Bitmap bitmap=BitmapFactory.decodeFile(fileUri.getPath(),options);
            document_imageView.setImageBitmap(bitmap);
            System.out.println("--------------------------------");
            System.out.println("FILE URI IN PREVIEW : "+fileUri);
            System.out.println("--------------------------------");
        }catch (NullPointerException e){
            e.printStackTrace();
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
