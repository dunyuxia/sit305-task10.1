package com.example.foodrescueapp;

import android.util.Log;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class Database
{
	private final SQLiteDatabase sqLiteDatabase;

	Database(Context context)
	{
		DBHelper dbHelper = new DBHelper(context, "FoodRescueApp.db", 1);
		sqLiteDatabase = dbHelper.getWritableDatabase();
	}

	public long signup(String name, String account, String phone, String address, String password)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("account", account);
		contentValues.put("phone", phone);
		contentValues.put("address", address);
		contentValues.put("password", password);
		return sqLiteDatabase.insert("Accounts", null, contentValues);
	}

	public Account login(String account, String password)
	{
		Account user = null;

		Cursor cursor =  sqLiteDatabase.rawQuery("select * from Accounts where account = ? and password = ?", new String[]{account, password});

		if (cursor.getCount() != 0)
		{
			cursor.moveToNext();
			String fullname = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			String address = cursor.getString(cursor.getColumnIndex("address"));
			user = new Account(fullname, account, phone, address);
		}

		cursor.close();

		return user;
	}

	public boolean addFoodItem(String uri, String title, String description, String addtime, double price, float quantity, String location)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("uri", uri);
		contentValues.put("title", title);
		contentValues.put("description", description);
		contentValues.put("addtime", addtime);
		contentValues.put("price", price);
		contentValues.put("quantity", quantity);
		contentValues.put("location", location);
		contentValues.put("shared", false);
		return sqLiteDatabase.insert("FoodItems", null, contentValues) != -1;
	}

	public void shareFoodItem(int itemId)
	{
		sqLiteDatabase.execSQL("update FoodItems set shared = 'true' where id = " + itemId);
	}

	public ArrayList<Item> loadFoodItems(boolean loadSharedOnly)
	{
		ArrayList<Item> foods = new ArrayList<>();

		Cursor cursor =  sqLiteDatabase.rawQuery(loadSharedOnly ? "select * from FoodItems where shared = 'true'" : "select * from FoodItems", null);

		while (cursor.moveToNext())
		{
			foods.add(new Item(cursor));
		}

		cursor.close();

		return foods;
	}

	static class DBHelper extends SQLiteOpenHelper
	{
		DBHelper(Context context, String dbName, int dbVersion)
		{
			super(context, dbName, null, dbVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("create table Accounts(id integer primary key autoincrement, name text, account text, phone text, address text, password text)");
			db.execSQL("create table FoodItems(id integer primary key autoincrement, uri text, title text, description text, addtime datetime, price number, quantity number, location text, shared bool)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.i("Database", "Database version upgraded.");
		}
	}
}