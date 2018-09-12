package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by anvin on 11/15/2016.
 */

public class SlideshowDialogFragment extends android.app.DialogFragment {
    private String TAG=SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<String> mArrayUri;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    ImageView slideshow_close;
    PhotoViewAttacher mAttacher;

    private int selectedPosition=0;
    public static SlideshowDialogFragment newInstance(){
        System.out.println("**************************************************");
        System.out.println("Inside 'newInstance' in 'SlideshoeDialogFragment'");
        System.out.println("**************************************************");

        SlideshowDialogFragment f=new SlideshowDialogFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        System.out.println("**************************************************");
        System.out.println("Inside 'onCreateView' in 'SlideshoeDialogFragment'");
        System.out.println("**************************************************");

        View v=inflater.inflate(R.layout.fragment_image_slider,container,false);
        viewPager=(ViewPager)v.findViewById(R.id.viewpager);


        mArrayUri=(ArrayList<String>)getArguments().getSerializable("images");
        selectedPosition=getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + mArrayUri.size());

        myViewPagerAdapter=new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListner);
        setCurrentItem(selectedPosition);


        return  v;
    }
    public void setCurrentItem(int position){

        System.out.println("**************************************************");
        System.out.println("Inside 'setCurrentItem' in 'SlideshoeDialogFragment'");
        System.out.println("**************************************************");
        viewPager.setCurrentItem(position,false);
        displayMetaInfo(selectedPosition);
    }

    //page change listner
    ViewPager.OnPageChangeListener viewPagerPageChangeListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            System.out.println("**************************************************");
            System.out.println("Inside 'onPageSelected' in 'SlideshoeDialogFragment'");
            System.out.println("**************************************************");
            displayMetaInfo(position);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void displayMetaInfo(int position){

        String imgname=mArrayUri.get(position);

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private  LayoutInflater layoutInflater;
        public MyViewPagerAdapter(){
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.image_fullscreen_preview,container,false);

            slideshow_close=(ImageView) view.findViewById(R.id.slideshow_close);

           /* TouchImageView imageViewPreview=(TouchImageView) view.findViewById(R.id.image_preview);*/
            /* imageViewPreview.setMaxZoom(4f);*/
            ImageView imageViewPreview=(ImageView) view.findViewById(R.id.image_preview);

            slideshow_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("-------------------------------------slideshow back button pressed ------------------------------------");
                    getFragmentManager().popBackStack();
                }
            });

            Glide.with(getActivity()).load(mArrayUri.get(position))
                    .thumbnail(0.5f)
                    .crossFade()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);
            mAttacher = new PhotoViewAttacher(imageViewPreview);

            mAttacher.update();


            container.addView(view);
            mAttacher.update();
            return view;
        }

        @Override
        public int getCount() {
            return mArrayUri.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==((View) object);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
