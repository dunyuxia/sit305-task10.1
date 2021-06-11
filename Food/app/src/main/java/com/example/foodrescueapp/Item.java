package com.example.foodrescueapp;

import android.database.Cursor;

import java.io.Serializable;

public class Item implements Serializable
{
	int id;
	String uri;
	String title;
	String description;
	String addtime;
	double price;
	double quantity;
	String location;

	Item(Cursor cursor)
	{
		id = cursor.getInt(cursor.getColumnIndex("id"));
		uri = cursor.getString(cursor.getColumnIndex("uri"));
		title = cursor.getString(cursor.getColumnIndex("title"));
		description = cursor.getString(cursor.getColumnIndex("description"));
		addtime = cursor.getString(cursor.getColumnIndex("addtime"));
		price = cursor.getInt(cursor.getColumnIndex("price"));
		quantity = cursor.getFloat(cursor.getColumnIndex("quantity"));
		location = cursor.getString(cursor.getColumnIndex("location"));
	}
}
