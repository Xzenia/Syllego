package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;

    UserDataController udc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        udc = new UserDataController(this);
    }

    public void LoginUser(View view) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder("");
        String username = "";
        String password = "";
        if (usernameEditText.getText().toString().matches("")){
            sb.append("Username field is empty!\n");
        } else {
            username = usernameEditText.getText().toString();
        }
        if (passwordEditText.getText().toString().matches("")){
            sb.append("Password field is empty!");
        } else {
            password = passwordEditText.getText().toString();
        }

        if (sb.toString().matches("")){
            boolean isLoggedIn = udc.authenticateUser(username, password);

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

    public void GoToCreateAccountActivity(View view){
        Intent createAccountActivity = new Intent(this, CreateAccountActivity.class);
        startActivity(createAccountActivity);
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
