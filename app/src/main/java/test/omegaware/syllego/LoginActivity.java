package test.omegaware.syllego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    private static final int RC_SIGN_IN = 6969;
    private static final String TAG = "LoginActivity";
    EditText emailEditText;
    EditText passwordEditText;
    SignInButton signInButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.UsernameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void LoginUser(View view) {
        if (emailEditText.getText().toString().trim() != "" && passwordEditText.getText().toString().trim() != ""){
            firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                toastMessage("User UID: "+user.getUid());
                                goToBookList(user.getUid());

                            } else {
                                // If sign in fails, display a message to the user.
                                toastMessage("Email or password is incorrect! Please try again.");
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }
                        }
                    });
        }

    }

    public void goToBookList(String uid){
        Intent goToBookList = new Intent(this, BookList.class);
        goToBookList.putExtra("UserID", uid);
        startActivity(goToBookList);
    }
    public void GoToCreateAccountActivity(View view){
        Intent createAccountActivity = new Intent(this, CreateAccountActivity.class);
        startActivity(createAccountActivity);
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
