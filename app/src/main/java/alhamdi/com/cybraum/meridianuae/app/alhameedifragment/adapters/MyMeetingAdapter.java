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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.EditMyMeetingFragment;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.HomeMainActivityNew;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.MyMeetingModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by anvin on 12/27/2016.
 */

public class MyMeetingAdapter extends RecyclerView.Adapter<MyMeetingAdapter.viewholder> {

    private Context context;
    private ArrayList<MyMeetingModel>arrayval;
    public int pos;
    ProgressDialog pDlog;
    boolean isConnected=false;
    private int lastPosition = -1;

    public MyMeetingAdapter(Context context1,ArrayList<MyMeetingModel> array){
        this.context=context1;
        this.arrayval=array;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_meeting,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(final viewholder holder, final int position) {

        holder.meeting_date.setText(arrayval.get(position).getDate().toString());
        holder.meeting_time.setText(arrayval.get(position).getTime_from().toString());
        holder.meeting_heading.setText(arrayval.get(position).getDescription().toString());
        holder.meeting_id.setText(arrayval.get(position).getMeet_id().toString());
        holder.user_id.setText(arrayval.get(position).getUser_id().toString());

        System.out.println("____________________________________________________________________");
        System.out.println(" Meet id order");
        System.out.println("heading : "+arrayval.get(position).getDescription().toString());
        System.out.println("meet_id : "+arrayval.get(position).getMeet_id().toString());

        System.out.println("____________________________________________________________________");

        final String heading=holder.meeting_heading.getText().toString();


        holder.edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(context,"'"+heading+"' edit clicked",Toast.LENGTH_SHORT).show();*/

                System.out.println("____________________________________________________________________");
                System.out.println("Inside Adapter , date :"+holder.meeting_date.getText().toString());
                System.out.println("____________________________________________________________________");

                EditMyMeetingFragment em=new EditMyMeetingFragment();

                Bundle bundle=new Bundle();
                bundle.putString("meet_id", holder.meeting_id.getText().toString());
                bundle.putString("title",holder.meeting_heading.getText().toString());
                bundle.putString("date",holder.meeting_date.getText().toString());
                bundle.putString("starting_time",holder.meeting_time.getText().toString());
                            em.setArguments(bundle);
                ((Activity)context).getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame,em).addToBackStack("").commit();

            }
        });

        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setMessage("Delete '"+holder.meeting_heading.getText().toString()+"' ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(ConnectionCheck()) {
                            new SentToServer(arrayval.get(position).getUser_id().toString(), arrayval.get(position).getMeet_id().toString()).execute();
                            ///////////////// REMOVING ITEM FROM RECYCLE LIST --START/////////////////
                            arrayval.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayval.size());
                            if (arrayval.size() == 0) {
                                ((Activity) context).getFragmentManager().popBackStackImmediate();
                            }
                            ///////////////// REMOVING ITEM FROM RECYCLE LIST --END/////////////////
                        }else
                        {

                            Toasty.info(context, "No internet connection", Toast.LENGTH_LONG, true).show();
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
        return arrayval.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        Typeface myFont1 = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    public TextView meeting_heading;
    public TextView meeting_date;
    public TextView meeting_time;
    private LinearLayout edit_image,delete_image;
        public TextView meeting_id;
        public TextView user_id;
    public viewholder(View itemView) {
        super(itemView);

        meeting_heading=(TextView)itemView.findViewById(R.id.meeting_heading);
        meeting_date=(TextView)itemView.findViewById(R.id.meeting_date);
        meeting_time=(TextView)itemView.findViewById(R.id.meeting_time);
        meeting_id=(TextView)itemView.findViewById(R.id.meeting_id);
        user_id=(TextView)itemView.findViewById(R.id.user_id);
        edit_image=(LinearLayout) itemView.findViewById(R.id.meeting_edit_image);
        delete_image=(LinearLayout) itemView.findViewById(R.id.meeting_delete_image);

        meeting_heading.setTypeface(myFont1);
        meeting_date.setTypeface(myFont1);
        meeting_time.setTypeface(myFont1);


    }
}
    class SentToServer extends AsyncTask<String,String,String> {
        String user_id,meet_id;

        public SentToServer(String user_id,String meet_id){
            this.user_id=user_id;
            this.meet_id=meet_id;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDlog = new ProgressDialog(context,R.style.MyTheme);

            pDlog.setCancelable(false);
            pDlog.show();*/
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.VISIBLE);
            HomeMainActivityNew.progress_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {



                URL url = new URL("http://app.alhammadilaw.com.php56-1.dfw3-2.websitetestlink.com/services/delete_meeting.php");
                System.out.println("____________________________________________________________________");
                System.out.println("Inside SentToServer ");
                System.out.println("meet_id : "+meet_id);
                System.out.println("user_id : "+user_id);
                System.out.println("____________________________________________________________________");
                JSONObject postDataParams=new JSONObject();
                postDataParams.put("id",meet_id);
                postDataParams.put("user_id",user_id);

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
            /*if(pDlog.isShowing())
                pDlog.dismiss();*/
            HomeMainActivityNew.progress_loader.setVisibility(View.GONE);
            HomeMainActivityNew.progress_loader_layout.setVisibility(View.GONE);

            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();*/
            if (result.contentEquals("\"success\"")){


                /*Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();*/
                Toasty.success(context, "Deleted", Toast.LENGTH_LONG, true).show();


            }
            else
            {
                /*Toast.makeText(context,"Failed.Try again later.",Toast.LENGTH_LONG).show();*/
                Toasty.error(context, "Failed.Try again later", Toast.LENGTH_LONG, true).show();

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
    public void onViewDetachedFromWindow(viewholder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}
