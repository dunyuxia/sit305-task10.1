package com.example.foodrescueapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements IActivity
{
	Database sqliteDatabase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setTitle(R.string.discover_free_food);

		sqliteDatabase = new Database(this);
		items = new ArrayList<>();
	}

	@Override
	protected void onResume()
	{
		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		LinearLayoutManager vLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(vLayoutManager);
		vLayoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setAdapter(new MyAdapter(sqliteDatabase.loadFoodItems(false), this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	public static ArrayList<Item> items;
	@Override
	@SuppressLint("NonConstantResourceId")
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.home:
			{
				startActivity(new Intent(this, HomeActivity.class));
				break;
			}

			case R.id.my_list:
			{
				startActivity(new Intent(this, SharedActivity.class));
				break;
			}

			case R.id.cart:
			{
				startActivity(new Intent(this, CartActivity.class));
				break;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	public void onShare(int id, String title)
	{
		sqliteDatabase.shareFoodItem(id);
		Toast.makeText(getBaseContext(), String.format(Locale.getDefault(), "%s is shared to my list.", title), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItem(Item item)
	{
		Intent intent = new Intent(this, FoodActivity.class);
		intent.putExtra("Item", item);
		startActivity(intent);
	}

	public void onAdd(View view)
	{
		startActivity(new Intent(this, NewFoodActivity.class));
	}

	@Override
	public void onBackPressed()
	{
		startActivity(new Intent(this, HomeActivity.class));
		super.onBackPressed();
	}
}