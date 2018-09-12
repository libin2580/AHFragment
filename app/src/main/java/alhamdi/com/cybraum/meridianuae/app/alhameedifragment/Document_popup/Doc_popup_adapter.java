package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.Document_popup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import es.dmoral.toasty.Toasty;

/**
 * Created by anvin on 2/6/2017.
 */

public class Doc_popup_adapter extends RecyclerView.Adapter<Doc_popup_adapter.viewHolder> {


    private Context appContext;
    public String[] imageUrls;
    public String[] CheckedimageUrls;
    public boolean[] checkboxStatus;
    public String stored_user_id;
    ArrayList<Uri>fileUris;
    private int count=0;


    ProgressDialog pDlog2;
    ArrayList<Uri> files = new ArrayList<Uri>();


    boolean isConnected=false;


    public  Doc_popup_adapter(Context context1,String[] values){
        try {
            imageUrls = new String[values.length];
            CheckedimageUrls = null;
            CheckedimageUrls = new String[values.length];
            checkboxStatus = new boolean[values.length];
            this.imageUrls = values;
            this.appContext = context1;

            fileUris = new ArrayList<>();
            files.clear();
            for (int i = 0; i < values.length; i++) {
                System.out.println("url (inside adapter) : " + values[i]);
            /*imageUrls.set(i,values.get(i).getImage_url());*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_document_image_selection,parent,false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        holder.doc_details_progressbar.setVisibility(View.VISIBLE);
        holder.document_check_mark.setChecked(checkboxStatus[position]);


        Picasso.with(appContext).load(imageUrls[position].toString())
                .fit()


                .into(holder.doc_detail_thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.doc_details_progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });


holder.document_check_mark.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {





        holder.doc_detail_thumbnail.setDrawingCacheEnabled(true);
            if (holder.document_check_mark.isChecked() == true) {


                checkboxStatus[position] = true; //for retaining checkbox status even after recyclerview scrolling
                count++;
                System.out.println("count in adapter : " + count);
                System.out.println("Inside checkbox selected");

                CheckedimageUrls[position] = imageUrls[position];
                System.out.println("Added : " + CheckedimageUrls[position]);


                    Bitmap bmp=holder.doc_detail_thumbnail.getDrawingCache();


                    /*System.out.println("getLocalBitmapUri(bm) : "+getLocalBitmapUri(bmp));*/
                    fileUris.add(getLocalBitmapUri(bmp));//file uris contains the location of image file in storage



            }


            if (holder.document_check_mark.isChecked() == false) {

                    System.out.println("Inside checkbox deselected");
                    System.out.println("removed: " + CheckedimageUrls[position]+"\nfile uri : "+fileUris.get(position).toString());
                    checkboxStatus[position] = false; //for retaining checkbox status even after recyclerview scrolling
                    count--;
                    CheckedimageUrls[position] = "";

                    try {
                        fileUris.remove(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                System.out.println("count in adapter : " + count);

        }
        if(count==0){
            fileUris.clear();

        }
        else if(count>10){

            /*Toast.makeText(appContext,"Sorry , can't share more than 10 documents at a time",Toast.LENGTH_SHORT).show();*/
            Toasty.info(appContext, "Sorry , can't share more than 10 documents at a time", Toast.LENGTH_LONG, true).show();
            ((Activity)appContext).findViewById(R.id.popup_share).setVisibility(View.GONE);
            ((Activity)appContext).findViewById(R.id.popup_share_disabled).setVisibility(View.VISIBLE);
        }else{
            ((Activity)appContext).findViewById(R.id.popup_share).setVisibility(View.VISIBLE);
            ((Activity)appContext).findViewById(R.id.popup_share_disabled).setVisibility(View.GONE);
        }

    }
});
    }

       @Override
    public int getItemCount() {

       return imageUrls.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public ImageView doc_detail_thumbnail;
        public ProgressBar doc_details_progressbar;
public CheckBox document_check_mark;


        public viewHolder(View itemView) {
            super(itemView);
            doc_detail_thumbnail=(ImageView)itemView.findViewById(R.id.doc_detail_thumbnail_popup);
            doc_details_progressbar=(ProgressBar)itemView.findViewById(R.id.doc_details_progressbar);
            document_check_mark=(CheckBox)itemView.findViewById(R.id.document_check_mark);


        }
    }



  public void shareFiles(){

      System.out.println("file uris");
      for(Uri u:fileUris)
      {
          System.out.println("uri : "+u.toString());
      }

      Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
      i.setType("image/*");
      i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
      i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      appContext.startActivity(Intent.createChooser(i, "Share Image"));

      fileUris.clear();




    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    /* CHECKING INTERNET CONNECTION -- START*/
    public boolean ConnectionCheck(){
        ConnectivityManager cm =(ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }
/* CHECKING INTERNET CONNECTION -- end*/


}
