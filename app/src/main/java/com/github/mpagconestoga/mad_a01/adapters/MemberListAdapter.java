/*
 *	FILE			: memberListAdapter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		:This file contains the class definition for the memberListAdapter
                    Inspiration for this class credit: https://www.youtube.com/watch?v=17NbUcEts9c
 *
 */

package com.github.mpagconestoga.mad_a01.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mpagconestoga.mad_a01.R;
import com.github.mpagconestoga.mad_a01.objects.MemberListItem;

import java.util.ArrayList;

/*
        Class Name: MemberListAdapter
        Purpose: Adapter for the Recycler views to select members to a task
 */
public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.memberListViewHolder> {
    private ArrayList<MemberListItem> memberList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onDeleteClick(int position);
    }

    /*
     *    METHOD      :     setOnItemClickListener
     *    DESCRIPTION :     This sets the listener for the click when a user presses the delete icon
     *                      on the task creation screen
     *    PARAMETERS  :     OnItemClickListener -> object that listener will be set to
     *    RETURNS     :     VOID
     * */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class memberListViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mName;
        public ImageView mDeleteImage;

        public memberListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.memberPic);
            mName = itemView.findViewById(R.id.memberName);
            mDeleteImage = itemView.findViewById(R.id.deleteImage);



            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }


    /*
     *    METHOD      :   Class constructor
     *    DESCRIPTION :   sets the list attribute of this class. Used to hold the list of members currently
     *                    assigned to the task
     *    PARAMETERS  :   ArrayList<memberListItem> list -> list of members
     *    RETURNS     :   VOID
     * */
    public MemberListAdapter(ArrayList<MemberListItem> list)
    {
        memberList =  list;
    }

    @NonNull
    @Override
    public memberListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list, parent, false);
        memberListViewHolder evh = new memberListViewHolder(v, mListener);
        return evh;
    }

    /*
     *    METHOD      :   onBindViewHolder
     *    DESCRIPTION :
     *    PARAMETERS  :   memberListViewHolder holder -> viewholder being bound;
     *                    int position -> position of the member
     *    RETURNS     :   VOID
     * */
    @Override
    public void onBindViewHolder(@NonNull memberListViewHolder holder, int position) {
        MemberListItem currentMember = memberList.get(position);

        holder.mImageView.setImageResource(currentMember.getmImage());
        holder.mName.setText(currentMember.getmName());
        holder.mDeleteImage.setImageResource(currentMember.getmDeleteImage());
    }

    //Getter for the size of the memberList(list of members currently attached to a new task)
    @Override
    public int getItemCount() {
        return memberList.size();
    }
}

