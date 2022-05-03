package com.example.voterapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voterapp.CandidateActivity;
import com.example.voterapp.FingerprintActivity;
import com.example.voterapp.PrefManager;
import com.example.voterapp.R;
import com.example.voterapp.model.Candidate;
import com.example.voterapp.model.Election;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    private List<Candidate> listItems;
    private Context context;
    PrefManager prefManager;

    public CandidateAdapter(List<Candidate> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        prefManager = new PrefManager(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get single row for candidate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Candidate candidate = listItems.get(position);
        holder.txtPartyName.setText(candidate.getParty());
        holder.txtCandidateName.setText(candidate.getName());
        holder.txtMobile.setText(candidate.getMobile());
        holder.txtDOB.setText(candidate.getDob());
        holder.txtGender.setText(candidate.getGender());
        Picasso.with(context)
                .load(candidate.getPhoto())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCandidateName, txtDOB, txtGender, txtPartyName, txtMobile;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCandidateName = (TextView) itemView.findViewById(R.id.txtCandidateName);
            txtDOB = (TextView) itemView.findViewById(R.id.txtDOB);
            txtGender = (TextView) itemView.findViewById(R.id.txtGender);
            txtMobile = (TextView) itemView.findViewById(R.id.txtMobile);
            txtPartyName = (TextView) itemView.findViewById(R.id.txtPartyName);
            imageView = (CircleImageView) itemView.findViewById(R.id.iv_circular_user_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Candidate candidate = listItems.get(position);
//                    Toast.makeText(context, "" + candidate.getCandidate_id(), Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setMessage("Do you want cast vote?")
                            .setTitle("Alert")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(context, FingerprintActivity.class);
//                                    intent.putExtra("candidate_id",);
                                    prefManager.setAttendance( candidate.getCandidate_id());
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .create();
                    alertDialog.show();

                }
            });

        }
    }
}
