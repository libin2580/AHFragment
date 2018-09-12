package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;
import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models.FaqModel;

/**
 * Created by anvin on 1/30/2017.
 */

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.viewHolder> {

    private ArrayList<FaqModel>arrayVal;
    private Context appContext;
    private int lastPosition = -1;

    public FaqAdapter(Context context1,ArrayList<FaqModel>Values){
        this.arrayVal=Values;
        this.appContext=context1;


    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_faq,parent,false);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {


        holder.id.setText(arrayVal.get(position).getId().toString());


        holder.question.setText(arrayVal.get(position).getQuestion().toString());


        holder.answer.setText(arrayVal.get(position).getAnswer().toString());
    holder.childScroll.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }
});

        System.out.println("---------------------------------------------------------------------");
        System.out.println("Ques : "+arrayVal.get(position).getQuestion().toString());
        System.out.println("Aswr : "+arrayVal.get(position).getAnswer().toString());
        System.out.println("---------------------------------------------------------------------");
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
        return arrayVal.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
TextView id,question,answer;
        ScrollView childScroll;
        public viewHolder(View itemView) {
            super(itemView);
            id=(TextView) itemView.findViewById(R.id.fqa_id_no);
            question=(TextView) itemView.findViewById(R.id.faq_question);
            answer=(TextView) itemView.findViewById(R.id.faq_answer);
            childScroll=(ScrollView)itemView.findViewById(R.id.childScroll);
        }
    }
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
