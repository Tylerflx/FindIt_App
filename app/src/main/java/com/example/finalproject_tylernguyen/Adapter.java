package com.example.finalproject_tylernguyen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Job> jobs; //contain all the jobs
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    private Context context;

    public Adapter(Context ctx, List<Job> jobs){
        //to receive the jobs from adapter
        this.inflater = LayoutInflater.from(ctx);
        this.jobs = jobs;
        this.context=ctx;
    }

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
        final String  name = jobs.get(position).getJob_title();
        //holder.fav.setImageDrawable(get);
        holder.job_title.setText(jobs.get(position).getJob_title());
        holder.job_type.setText(jobs.get(position).getPosition_type());
        holder.job_location.setText(jobs.get(position).getJob_location());
        Picasso.get().load(jobs.get(position).getLogo_url()).into(holder.company_logo);

        //if heart button on click
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //access fav list in database
                holder.fav_list = database.getReference("FavoriteList").child(currentUserId).child(jobs.get(position).getJob_id());
                //set job
                final String name = jobs.get(position).getJob_title();
                //fill the heart button if on click
                holder.fav_list.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //if data on change then set the fav button to full red
                        holder.fav.setImageResource(R.drawable.ic_favourite);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //if unchange then keep the same
                        holder.fav.setImageResource(R.drawable.ic_favorite_border);

                    }
                });
                //add to database
                holder.fav_list.setValue(name);
                Toast.makeText(v.getContext(), "Added to Favourite List", Toast.LENGTH_SHORT).show();
            }
        });
        holder.job_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(context, JobInfo.class);
                it.putExtra("id", jobs.get(position).getJob_id());
                context.startActivity(it);
            }
        });
    }
    //number of items that will be view
    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView job_title, job_type, job_location, job_id, job_url;
        ImageView company_logo;
        ImageButton fav;
        DatabaseReference fav_list;
        Job job;
        ConstraintLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            job = new Job();
            //assign items
            job_title = itemView.findViewById(R.id.jobTitle);
            job_type = itemView.findViewById(R.id.positionTitle);
            job_location = itemView.findViewById(R.id.locationTitle);
            company_logo = itemView.findViewById(R.id.companyLogo);
            parent_layout = itemView.findViewById(R.id.relate);
            fav = itemView.findViewById(R.id.fvrt_f2_item);
        }

    }
}
