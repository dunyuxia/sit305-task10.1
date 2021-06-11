package com.example.foodrescueapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>
{
	ArrayList<Item> items = new ArrayList<>();
	IActivity iActivity;

	MyAdapter(ArrayList<Item> items, IActivity iActivity)
	{
		this.items.addAll(items);
		this.iActivity = iActivity;
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
	{
		holder.setContent(items.get(position), iActivity);
		holder.itemView.setOnClickListener(v ->
		{
			if (iActivity != null)
				iActivity.onItem(items.get(position));
		});
	}

	@Override
	public int getItemCount()
	{
		return items.size();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food, parent, false));
	}
}