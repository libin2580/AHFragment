package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.AppointmentFragment;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.CircleTransform;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.Login;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.MySpannable;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.SessionManager;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.MyLawyerModel;
import at.blogc.android.views.ExpandableTextView;

/**
 * Created by anvin on 1/13/2017.
 */

public class MyLawyerAdapter extends RecyclerView.Adapter<MyLawyerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyLawyerModel>arrayVal;
    boolean expandable;
    String skiped;
    SessionManager sm;
    private int lastPosition = -1;

    public MyLawyerAdapter(Context appContext,ArrayList<MyLawyerModel>vals){
        this.context=appContext;
        this.arrayVal=vals;
        //checking if skiped login screen --start
        sm=new SessionManager(context);
        HashMap<String, String> details = sm.getUserDetails();
        skiped=details.get(sm.KEY_IS_SKIPED);
//checking if skiped login screen --end
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_lawyer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.lawyer_name.setText(arrayVal.get(position).getName().toString());
        holder.lawyer_department.setText(arrayVal.get(position).getDepartment().toString());
        holder.lawyer_email.setText(arrayVal.get(position).getEmail().toString());
        holder.lawyer_phone.setText(arrayVal.get(position).getPhone().toString());
        holder.lawyer_address.setText(arrayVal.get(position).getDescription().toString());
        holder.lawyer_cases.setText(arrayVal.get(position).getCases().toString());


// set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        holder.lawyer_address.setAnimationDuration(1000L);

        // set interpolators for both expanding and collapsing animations
        holder.lawyer_address.setInterpolator(new OvershootInterpolator());

// or set them separately
        holder.lawyer_address.setExpandInterpolator(new OvershootInterpolator());
        holder.lawyer_address.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        holder.lawyer_view_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                holder.lawyer_address.toggle();
                holder.lawyer_view_more.setText(holder.lawyer_address.isExpanded() ? "view less" : "view more");
            }
        });

// but, you can also do the checks yourself
        holder.lawyer_view_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (holder.lawyer_address.isExpanded())
                {
                    holder.lawyer_address.collapse();
                    holder.lawyer_view_more.setText("view more");
                }
                else
                {
                    holder.lawyer_address.expand();
                    holder.lawyer_view_more.setText("view less");
                }
            }
        });

// listen for expand / collapse events
        holder.lawyer_address.setOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d("TAG", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d("TAG", "ExpandableTextView collapsed");
            }
        });



        Picasso.with(context).load(arrayVal.get(position).getImage())



                .transform(new CircleTransform())


                .into(holder.lawyer_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.gallery_progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });



        holder.childScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

     /* holder.lawyer_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        holder.make_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(skiped.equalsIgnoreCase("true")){
                    /*Toasty.info(context, "You must login to continue", Toast.LENGTH_SHORT, true).show();*/
                    new AlertDialog.Builder(context)

                            .setMessage("You must login to continue")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent i = new Intent(context, Login.class);

                                    context.startActivity(i);
                                    ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);

                                    ((Activity)context).finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else {
                    AppointmentFragment mlf = new AppointmentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("law_id", arrayVal.get(position).getLaw_id().toString());
                    mlf.setArguments(bundle);
                    ((Activity) context).getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, mlf).addToBackStack("").commit();
                }
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
        return arrayVal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView lawyer_image;
        ScrollView childScroll;
        ProgressBar gallery_progressbar;
        TextView lawyer_name,lawyer_department,lawyer_email,lawyer_phone,lawyer_cases,lawyer_view_more;
        ExpandableTextView lawyer_address;
        LinearLayout make_appointment;
        public ViewHolder(View itemView) {
            super(itemView);

            lawyer_image=(ImageView)itemView.findViewById(R.id.lawyer_image);
            lawyer_name=(TextView)itemView.findViewById(R.id.lawyer_name);
            lawyer_department=(TextView)itemView.findViewById(R.id.lawyer_department);
            lawyer_email=(TextView)itemView.findViewById(R.id.lawyer_email);
            lawyer_phone=(TextView)itemView.findViewById(R.id.lawyer_phone);
            lawyer_address=(ExpandableTextView)itemView.findViewById(R.id.lawyer_address);
            lawyer_cases=(TextView)itemView.findViewById(R.id.lawyer_cases);
            gallery_progressbar=(ProgressBar)itemView.findViewById(R.id.gallery_progressbar);
            childScroll=(ScrollView)itemView.findViewById(R.id.childScroll);
            make_appointment=(LinearLayout)itemView.findViewById(R.id.make_appointment);
            lawyer_view_more=(TextView)itemView.findViewById(R.id.lawyer_view_more);

        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);

                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

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
