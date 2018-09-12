package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.HomeMainActivityNew;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.gallery.SlideshowDialogFragment;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.DocumentDetailsModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by anvin on 2/6/2017.
 */

public class DocumentDetailsAdapter extends RecyclerView.Adapter<DocumentDetailsAdapter.viewHolder> {
    private ArrayList<DocumentDetailsModel>arreyValues;
    private Context appContext;
    public ArrayList<String>imageUrls;
    public String stored_user_id;
    ProgressDialog pDlog2;
    private int lastPosition = -1;

    Snackbar snackbar;
    boolean isConnected=false;
    public  DocumentDetailsAdapter(Context context1,ArrayList<DocumentDetailsModel> values,String user_id){
        this.arreyValues=values;
        this.appContext=context1;
        imageUrls=new ArrayList<>();
        stored_user_id=user_id;

        for(int i=0;i<values.size();i++)
        {
            imageUrls.add(values.get(i).getImage_url());
            /*imageUrls.set(i,values.get(i).getImage_url());*/
        }
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_document_details_thumbnail,parent,false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder,final int position) {
        holder.doc_details_progressbar.setVisibility(View.VISIBLE);


        Glide.with(appContext).load(arreyValues.get(position).getImage_url())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.doc_details_progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()

                .into(holder.doc_detail_thumbnail);




        holder.doc_detail_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", imageUrls);
                bundle.putInt("position", position);

                android.app.FragmentTransaction ft = ((Activity)appContext).getFragmentManager().beginTransaction().addToBackStack("");
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });


        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog=new AlertDialog.Builder(appContext);
                dialog.setMessage("Delete this document ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new SentDeleteRequest(arreyValues.get(position).getId().toString(),position).execute();
                       /* mArrayUri.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mArrayUri.size());*/
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                ConnectionCheck();
                if(!isConnected){
                    /*Toast.makeText(appContext,"No internet connection",Toast.LENGTH_SHORT).show();*/
                    Toasty.info(appContext, "No internet connection", Toast.LENGTH_LONG, true).show();
                }
                else {
                    dialog.show();
                }

            }
        });

// Here you apply the animation when the view is bound
        /*setAnimation(holder.itemView, position);*/
        Animation animation = AnimationUtils.loadAnimation(appContext,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


    }

    @Override
    public int getItemCount() {
        return arreyValues.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public ImageView doc_detail_thumbnail;
        public ProgressBar doc_details_progressbar;
        public ImageButton image_delete;
        CheckBox document_check_mark;

        public viewHolder(View itemView) {
            super(itemView);
            doc_detail_thumbnail=(ImageView)itemView.findViewById(R.id.doc_detail_thumbnail);
            doc_details_progressbar=(ProgressBar)itemView.findViewById(R.id.doc_details_progressbar);
            image_delete=(ImageButton)itemView.findViewById(R.id.image_delete);
            document_check_mark=(CheckBox) itemView.findViewById(R.id.document_check_mark);


        }
    }
    class SentDeleteRequest extends AsyncTask<String,String,String> {
String delete_id;
        int position;
        public SentDeleteRequest(String dlt_id,int pos){
            delete_id=dlt_id;
            position=pos;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDlog2 = new ProgressDialog(appContext,R.style.MyTheme);

            pDlog2.setCancelable(false);
            pDlog2.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {



                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/delete_document.php");
                System.out.println("____________________________________________________________________");
                System.out.println("Inside SentToServer in documentdetrails adapter ");
                System.out.println("user id : "+stored_user_id);
                System.out.println("delete_id : "+delete_id);
                System.out.println("____________________________________________________________________");
                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id",stored_user_id);
                postDataParams.put("id",delete_id);

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
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){

               /* Toast.makeText(appContext,"Deleted",Toast.LENGTH_LONG).show();*/
                Toasty.success(appContext, "Deleted", Toast.LENGTH_LONG, true).show();

                arreyValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,arreyValues.size());
                if(arreyValues.size()==0){
                    ((Activity)appContext).getFragmentManager().popBackStackImmediate();
                }


            }
            else
            {
                Toasty.error(appContext, "Failed.Try again later", Toast.LENGTH_LONG, true).show();
                /*Toast.makeText(appContext,"Failed.Try again later.",Toast.LENGTH_LONG).show();*/

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
                (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

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
            Animation animation = AnimationUtils.loadAnimation(appContext, R.anim.push_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public void onViewDetachedFromWindow(viewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

}
