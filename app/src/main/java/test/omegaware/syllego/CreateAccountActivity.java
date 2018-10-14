package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class CreateAccountActivity extends AppCompatActivity {

    EditText fullNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;

    UserDataController udc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        fullNameEditText = findViewById(R.id.FullNameEditText);
        usernameEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        udc = new UserDataController(this);

    }

    public void CreateAccount(View view) throws NoSuchAlgorithmException{
        StringBuilder sb = new StringBuilder("");
        User newUser = new User();

        if (fullNameEditText.getText().toString().matches("")){
            sb.append("Full Name field is empty!\n");
        } else {
            newUser.setFullName(fullNameEditText.getText().toString());
        }

        if (usernameEditText.getText().toString().matches("")){
            sb.append("Username field is empty!\n");
        } else {
            newUser.setUsername(usernameEditText.getText().toString());
        }

        if (passwordEditText.getText().toString().matches("")){
            sb.append("Password field is empty!");
        } else {
            newUser.setPasswordHash(udc.getHashPassword(passwordEditText.getText().toString()));
        }

        if (sb.toString().matches("")){
            boolean isLoggedIn = udc.createUser(newUser);

            if (isLoggedIn){
                Intent goToMainActivity = new Intent(this, BookList.class);
                startActivity(goToMainActivity);
            } else {
                toastMessage("Username or password is incorrect!");
            }

        } else {
            toastMessage(sb.toString());
        }
    }


    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
