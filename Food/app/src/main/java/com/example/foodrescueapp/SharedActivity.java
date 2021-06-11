package com.example.foodrescueapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SharedActivity extends AppCompatActivity implements IActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);
	}

	@Override
	protected void onResume()
	{
		Database sqliteDatabase = new Database(this);
		ArrayList<Item> items = sqliteDatabase.loadFoodItems(true);

		RecyclerView recyclerView = findViewById(R.id.recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setAdapter(new MyAdapter(items, this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

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

			case R.id.account:
			{
				//	TODO
				break;
			}

			case R.id.my_list:
			{
				startActivity(new Intent(this, SharedActivity.class));
				break;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onShare(int id, String title)
	{

	}

	@Override
	public void onItem(Item item)
	{
		Intent intent = new Intent(this, FoodActivity.class);
		intent.putExtra("Item", item);
		startActivity(intent);
	}

	@Override
	public void onBackPressed()
	{
		startActivity(new Intent(this, HomeActivity.class));
		super.onBackPressed();
	}
}