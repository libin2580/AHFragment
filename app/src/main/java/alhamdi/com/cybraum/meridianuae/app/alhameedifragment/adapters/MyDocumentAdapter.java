package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.DocumentDetails;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.HomeMainActivityNew;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.MyDocumentModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by anvin on 1/5/2017.
 */

public class MyDocumentAdapter extends RecyclerView.Adapter<MyDocumentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyDocumentModel> arrayVal1;
    private String doc_id,userid,image_url;
    ProgressDialog pDlog2;
    Snackbar snackbar;
    boolean isConnected=false;
    private int lastPosition = -1;
    public MyDocumentAdapter(Context appContext,ArrayList<MyDocumentModel> arrayVal2){
        this.context=appContext;
        this.arrayVal1=arrayVal2;
        for(int i=0;i<arrayVal1.size();i++){
            System.out.println(arrayVal1.get(i).getTitle());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_documents,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        holder.heading.setText(arrayVal1.get(position).getTitle().toString());
        doc_id=arrayVal1.get(position).getDoc_id().toString();
        userid=arrayVal1.get(position).getUser_id().toString();
        /*image_url=arrayVal1.get(position).getUrl().toString();*/

        holder.doc_id.setText(doc_id);


        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.document_image.setImageResource(R.drawable.my_document_red);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        DocumentDetails em=new DocumentDetails();
                        Bundle bundle=new Bundle();
                        bundle.putString("doc_id", holder.doc_id.getText().toString());
                        bundle.putString("doc_title", holder.heading.getText().toString());
                        em.setArguments(bundle);

                        ((Activity)context).getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,em).addToBackStack("").commit();
                    }
                }, 100);



            }
        });
/*        holder.box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.document_image.setImageResource(R.drawable.my_document_red);

                        break;
                    case MotionEvent.ACTION_UP:
                        *//*AppointmentFragment sf=new AppointmentFragment();
                        getFragmentManager().beginTransaction().replace(R.id.frame, sf).addToBackStack("").commit();*//*

                        DocumentDetails em=new DocumentDetails();

                        Bundle bundle=new Bundle();
                        bundle.putString("doc_id", holder.doc_id.getText().toString());
                        bundle.putString("doc_title", holder.heading.getText().toString());
                        em.setArguments(bundle);
                        ((Activity)context).getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,em).addToBackStack("").commit();


                        holder.document_image.setImageResource(R.drawable.my_document);
                        break;

                }


                return true;
            }
        });*/
        final String title=arrayVal1.get(position).getTitle().toString();



        holder.delete_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog=new AlertDialog.Builder(context);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                    /*dialog.setMessage("Delete " + title + " document ?");*/
                    dialog.setMessage(Html.fromHtml("Delete <i>'" +title.trim()+"'</i> document ?", Html.FROM_HTML_MODE_LEGACY));

                }else {
                    dialog.setMessage(Html.fromHtml("Delete <i>'"+title.trim()+"'</i> document ?"));

                }
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConnectionCheck();
                        if(!isConnected){

                            /*Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();*/
                            Toasty.info(context, "No internet connection", Toast.LENGTH_SHORT, true).show();

                        }
                        else {

                            new SentDeleteRequest(arrayVal1.get(position).getDoc_id().toString(),position).execute();
                        }






                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });

// Here you apply the animation when the view is bound
        /*setAnimation(holder.itemView, position);*/
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return arrayVal1.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        Typeface myFont1 = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        public TextView heading,doc_id;
        public LinearLayout box,delete_Document;
        public ImageView document_image;
        public ViewHolder(View itemView) {
            super(itemView);
            heading=(TextView)itemView.findViewById(R.id.my_documents_heading);
            box=(LinearLayout)itemView.findViewById(R.id.my_document_box);
            document_image=(ImageView)itemView.findViewById(R.id.my_document_image);
            heading.setTypeface(myFont1);
            doc_id=(TextView)itemView.findViewById(R.id.doc_id);
            delete_Document=(LinearLayout)itemView.findViewById(R.id.delete_Document);

        }
    }
    class SentDeleteRequest extends AsyncTask<String,String,String> {
        String doc_uniq_id;
        int position;
        public SentDeleteRequest(String dlt_id,int pos){
            doc_uniq_id=dlt_id;
            position=pos;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog2 = new ProgressDialog(context,R.style.MyTheme);

            pDlog2.setCancelable(false);
            pDlog2.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {



                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/deletealldocument.php");
                System.out.println("____________________________________________________________________");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("doc_uniq_id",doc_uniq_id);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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
           /* if(pDlog2.isShowing())
                pDlog2.dismiss();
*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);
            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){

                /*Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();*/
                Toasty.success(context, "Deleted", Toast.LENGTH_SHORT, true).show();

                arrayVal1.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,arrayVal1.size());
                if(arrayVal1.size()==0){
                    ((Activity)context).getFragmentManager().popBackStackImmediate();
                }


            }
            else
            {
                /*Toast.makeText(context,"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                Toasty.error(context, "Failed.Try again later", Toast.LENGTH_SHORT, true).show();

            }
        }
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
    /* CHECKING INTERNET CONNECTION -- START*/
    public boolean ConnectionCheck(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }
/* CHECKING INTERNET CONNECTION -- end*/
private void setAnimation(View viewToAnimate, int position)
{
    // If the bound view wasn't previously displayed on screen, it's animated
    if (position > lastPosition)
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_in);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }
}
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

}
