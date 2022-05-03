package com.example.voterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voterapp.CandidateActivity;
import com.example.voterapp.R;
import com.example.voterapp.ResultActivity;
import com.example.voterapp.model.Election;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ViewHolder> {

    private List<Election> listItems;
    private Context context;

    public ElectionAdapter(List<Election> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.election_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Election election = listItems.get(position);
        holder.txtElectionType.setText(election.getElection_type());
        holder.txtElectionId.setText(election.getElection_id());
        holder.txtDate.setText(election.getElection_date());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, election.getElection_id() + " clicked", Toast.LENGTH_SHORT).show();
                //start new activity according to status of election
                //open candidate activity if date is today
                try {
                    String myDate = election.getElection_date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(myDate);
                    long millis = date.getTime();
                    boolean isToday = DateUtils.isToday(millis);
                    if (isToday) {
                        Intent intent = new Intent(context, CandidateActivity.class);
                        intent.putExtra("election_id", election.getElection_id());
                        intent.putExtra("election_date", election.getElection_date());
                        intent.putExtra("election_type", election.getElection_type());
                        context.startActivity(intent);
                    } else {
//                        Toast.makeText(context, "Not Today", Toast.LENGTH_SHORT).show();
                        if (election.getElection_status().equalsIgnoreCase("Not Declared")) {
                            Toast.makeText(context, "Result Not Declared Yet..", Toast.LENGTH_SHORT).show();
                        } else if (election.getElection_status().equalsIgnoreCase("Result Declared")) {
                            //open result activity to show the result of election
                            Intent intent = new Intent(context, ResultActivity.class);
                            intent.putExtra("election_id", election.getElection_id());
                            context.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtElectionId, txtElectionType, txtDate;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtElectionId = (TextView) itemView.findViewById(R.id.txtElectionId);
            txtElectionType = (TextView) itemView.findViewById(R.id.txtElectionType);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            imageView = (ImageView) itemView.findViewById(R.id.imgCastVote);
        }
    }
}
