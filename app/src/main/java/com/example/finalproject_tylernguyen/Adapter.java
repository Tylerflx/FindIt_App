package com.example.finalproject_tylernguyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
     List<Job> jobs; //contain all the jobs

     public Adapter(Context ctx, List<Job> jobs){
         //to receive the jobs from adapter
         this.inflater = LayoutInflater.from(ctx);
         this.jobs = jobs;
     }
    @NonNull
    //assign layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //using the cardview layout
        View v = inflater.inflate(R.layout.custom_result_layout, parent, false);
        return new ViewHolder(v);
    }
    //use view we created, assign data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         //bind the data
        //holder.fav.setImageDrawable(get);
        holder.job_title.setText(jobs.get(position).getJob_title());
        holder.job_type.setText(jobs.get(position).getPosition_type());
        holder.job_location.setText(jobs.get(position).getJob_location());
        Picasso.get().load(jobs.get(position).getLogo_url()).into(holder.company_logo);


    }
    //number of items that will be view
    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView job_title, job_type, job_location;
        ImageView company_logo;
        ImageButton fav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign items
            job_title = itemView.findViewById(R.id.jobTitle);
            job_type = itemView.findViewById(R.id.positionTitle);
            job_location = itemView.findViewById(R.id.locationTitle);
            company_logo = itemView.findViewById(R.id.companyLogo);
            fav = itemView.findViewById(R.id.fav_btn);
        }
    }
}
