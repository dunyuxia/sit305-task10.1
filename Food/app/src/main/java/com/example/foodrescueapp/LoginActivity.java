package com.example.foodrescueapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void onLogin(View view)
	{
		Database sqliteDatabase = new Database(this);
		EditText account = findViewById(R.id.account);
		EditText password = findViewById(R.id.password);

		if (!Pattern.compile("^([a–zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\\\\\.][A-Za-z]{2,3}([\\\\\\\\.][A-Za-z]{2})?$").matcher(account.getText().toString()).matches())
		{
			Toast.makeText(getBaseContext(), "Account must be a valid e-mail.", Toast.LENGTH_LONG).show();
			return;
		}

		if (sqliteDatabase.login(account.getText().toString(), password.getText().toString()) != null)
		{
			startActivity(new Intent(this, HomeActivity.class));
			return;
		}

		Toast.makeText(getBaseContext(), "Login Failed. Please confirm your account and password.", Toast.LENGTH_SHORT).show();
	}

	public void onSignUp(View view)
	{
		startActivity(new Intent(this, SignUpActivity.class));
	}
}