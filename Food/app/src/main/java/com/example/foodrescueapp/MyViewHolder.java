package com.example.foodrescueapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

class MyViewHolder extends RecyclerView.ViewHolder
{
	private final Context context;
	private final ImageView image;
	private final TextView title;
	private final TextView description;
	private final AppCompatButton share;

	MyViewHolder(View v)
	{
		super(v);
		context = v.getContext();
		image = v.findViewById(R.id.image);
		title = v.findViewById(R.id.title);
		description = v.findViewById(R.id.description);
		share = v.findViewById(R.id.share);
	}

	void setContent(Item item, IActivity iActivity)
	{
		try
		{
			image.setImageBitmap(BitmapFactory.decodeFile(context.getFilesDir().toString() + File.separator + item.uri));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		title.setText(item.title);
		description.setText(item.description);

		share.setOnClickListener(v -> iActivity.onShare(item.id, item.title));
	}
}