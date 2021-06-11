package com.example.foodrescueapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	public void onSignUp(View view)
	{
		Database sqliteDatabase = new Database(this);

		EditText name = findViewById(R.id.name);
		EditText account = findViewById(R.id.account);
		EditText phone = findViewById(R.id.phone);
		EditText address = findViewById(R.id.address);
		EditText password = findViewById(R.id.password);
		EditText confirmedPassword = findViewById(R.id.confirmed_password);

		String nameStr = name.getText().toString();
		String accountStr = account.getText().toString();
		String phoneStr = phone.getText().toString();
		String addressStr = address.getText().toString();
		String passwordStr = password.getText().toString();
		String confirmed_passwordStr = confirmedPassword.getText().toString();

		if (nameStr.length() > 0 && accountStr.length() > 0 && phoneStr.length() > 0 && addressStr.length() > 0 && passwordStr.length() > 0 && confirmed_passwordStr.length() > 0)
		{
			if (!Pattern.compile("^([a–zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\\\\\.][A-Za-z]{2,3}([\\\\\\\\.][A-Za-z]{2})?$").matcher(accountStr).matches())
			{
				Toast.makeText(getBaseContext(), "Account must be a valid e-mail.", Toast.LENGTH_LONG).show();
				return;
			}

			if (!passwordStr.equals(confirmed_passwordStr))
			{
				Toast.makeText(getBaseContext(), "The passwords do not match. Please confirm and try again.", Toast.LENGTH_LONG).show();
				return;
			}

			if (sqliteDatabase.signup(nameStr, accountStr, phoneStr, addressStr, passwordStr) != -1)
			{
				startActivity(new Intent(this, LoginActivity.class));
			}
			else
			{
				Toast.makeText(getBaseContext(), "Failed. Confirm your account and password please.", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(getBaseContext(), "You have to input all the fields in this form.", Toast.LENGTH_LONG).show();
		}
	}
}