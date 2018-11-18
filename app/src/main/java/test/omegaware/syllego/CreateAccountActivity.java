package test.omegaware.syllego;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        confirmEditText = findViewById(R.id.ConfirmPasswordEditText);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void CreateAccount(View view) {
        if(!passwordEditText.getText().toString().trim().equals(confirmEditText.getText().toString().trim())){
            toastMessage("Passwords don't match. Please check your password fields.");
            return;
        }

        if (!emailEditText.getText().toString().trim().equals("") && !passwordEditText.getText().toString().trim().equals("")){
            firebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                toastMessage("Registration failed: "+task.getException());
                            }
                        }
                    });
        } else {
            toastMessage("Fields are empty! Please fill them with the required information.");
        }
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
