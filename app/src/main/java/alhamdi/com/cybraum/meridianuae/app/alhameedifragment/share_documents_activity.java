package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;



import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.Document_popup.Doc_popup_adapter;
import es.dmoral.toasty.Toasty;

public class share_documents_activity extends AppCompatActivity {
    RecyclerView popupRecyclerView;
    LinearLayout popup_share;
    ImageButton document_image_selection_close;
    Doc_popup_adapter popupadapter;
    ProgressDialog pDlog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share_documents_activity);
        popupRecyclerView = (RecyclerView) findViewById(R.id.document_popup_recyclerview);
        RecyclerView.LayoutManager mLayoutManager2=new GridLayoutManager(share_documents_activity.this,2);
        popupRecyclerView.setLayoutManager(mLayoutManager2);
        popupRecyclerView.setItemAnimator(new DefaultItemAnimator());
        document_image_selection_close = (ImageButton)findViewById(R.id.document_image_selection_close);

        popup_share=(LinearLayout)findViewById(R.id.popup_share);

        Bundle extras = getIntent().getExtras();
        String ckdurl[]=extras.getStringArray("checked_items");

        popupadapter = new Doc_popup_adapter(share_documents_activity.this, ckdurl);
        popupRecyclerView.setAdapter(popupadapter);

        document_image_selection_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//deleting priviosly downloded images from application storage directry ---start
        try {
            File dir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
            FileUtils.deleteDirectory(dir);
        }catch (Exception e){
            e.printStackTrace();
        }
//deleting priviosly downloded images from application storage directry ---ende
        popup_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("_______________________________________________________________________");
                System.out.println("Selected image urls inside share_documents_activity(not in adapter )");
                String checked_images[]=popupadapter.CheckedimageUrls;

                try {
                    List<String> list = new ArrayList<String>();

                    for (String s : checked_images) {
                        if (s != null && s.length() > 0) {
                            list.add(s);
                        }
                        checked_images = list.toArray(new String[list.size()]);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                for(int i=0;i<checked_images.length;i++)
                {
                    System.out.println("share_documents_activity --- url : "+checked_images[i]);

                }

                if(checked_images.length==0){
                    /*Toast.makeText(getApplicationContext(),"select image to share",Toast.LENGTH_SHORT).show();*/
                    Toasty.info(getApplicationContext(), "Select image to share", Toast.LENGTH_LONG, true).show();
                }else {



                    popupadapter.shareFiles();


                    finish();
                }
            }
        });
    }

}
