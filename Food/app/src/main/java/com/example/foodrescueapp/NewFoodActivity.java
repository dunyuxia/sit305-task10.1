package com.example.foodrescueapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewFoodActivity extends AppCompatActivity
{
	ImageView image;
	EditText title;
	EditText description;
	DatePicker datepicker;
	EditText price;
	EditText quantity;
	EditText location;

	String uri;
	Bitmap bitmap;
	String datetime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_food);

		image = findViewById(R.id.image);
		image.setOnClickListener(v ->
		{
			if (ContextCompat.checkSelfPermission(NewFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(NewFoodActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
			}
			else
			{
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);

				startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
			}
		});

		title = findViewById(R.id.title);
		description = findViewById(R.id.description);
		datepicker = findViewById(R.id.datetime);
		datepicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) ->
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			uri = String.format("%s.jpg", new SimpleDateFormat("yyyyMMddhhmmssaa", Locale.getDefault()).format(calendar.getTime()));
			datetime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.UK).format(calendar.getTime());
		});

		price = findViewById(R.id.price);
		quantity = findViewById(R.id.quantity);
		location = findViewById(R.id.location);

		uri = "";
	}

	public void onSave(View view)
	{
		String title = this.title.getText().toString();
		String description = this.description.getText().toString();
		String priceStr = price.getText().toString();
		String quantityStr = quantity.getText().toString();
		String location = this.location.getText().toString();

		if (uri.length() > 0 && title.length() > 0 && description.length() > 0 && priceStr.length() > 0 && quantityStr.length() > 0 && location.length() > 0)
		{
			Database sqliteDatabase = new Database(this);

			if (sqliteDatabase.addFoodItem(uri, title, description, datetime, Double.parseDouble(priceStr), Float.parseFloat(quantityStr), location))
			{
				String targetPath = getFilesDir().toString();

				File saveFile = new File(targetPath, uri);

				try
				{
					FileOutputStream fos = new FileOutputStream(saveFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
					fos.flush();
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				startActivity(new Intent(this, HomeActivity.class));
			}
			else
			{
				Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(getBaseContext(), "All fields are mandatory. DateTime must be confirmed.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == 1)
		{
			assert data != null;
			Uri imageUri = data.getData();

			try
			{
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
				image.setImageBitmap(bitmap);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}