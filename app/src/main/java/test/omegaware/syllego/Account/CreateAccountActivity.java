package test.omegaware.syllego.Account;

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
import com.google.firebase.auth.UserProfileChangeRequest;

import test.omegaware.syllego.Controller.UserDataController;
import test.omegaware.syllego.R;
import test.omegaware.syllego.Model.User;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private EditText emailEditText;
    private EditText fullNameEditText;
    private EditText departmentEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private FirebaseAuth firebaseAuth;

    private UserDataController udc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.UsernameEditText);
        fullNameEditText = findViewById(R.id.FullNameEditText);
        departmentEditText = findViewById(R.id.DepartmentNameEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        confirmEditText = findViewById(R.id.ConfirmPasswordEditText);
        firebaseAuth = FirebaseAuth.getInstance();

        udc = new UserDataController();
    }

    public void CreateAccount(View view) {
        StringBuilder errors = new StringBuilder();
        if(!passwordEditText.getText().toString().trim().equals(confirmEditText.getText().toString().trim())){
            errors.append("Passwords don't match. Please check your password fields.");
            errors.append("\n");
            return;
        }

        if (fullNameEditText.getText().toString().matches("")){
            errors.append("Full name field is empty!");
            errors.append("\n");
        }

        if (departmentEditText.getText().toString().matches("")){
            errors.append("Department field is empty!");
            errors.append("\n");
        }

        if (emailEditText.getText().toString().equals("")){
            errors.append("Email field is empty!");
            errors.append("\n");
        }

        if (passwordEditText.getText().toString().equals("")){
            errors.append("Password field is empty");
            errors.append("\n");
        }

        if (errors.toString().matches("")){
            firebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                User newUser = new User();
                                newUser.setName(fullNameEditText.getText().toString());
                                newUser.setDepartment(departmentEditText.getText().toString());
                                newUser.setUserId(firebaseAuth.getCurrentUser().getUid());

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullNameEditText.getText().toString()).build();

                                firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                                udc.addUser(newUser);
                                Log.d(TAG, "createUserWithEmail:success");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                toastMessage("Registration failed: "+task.getException());
                            }
                        }
                    });
        } else {
            toastMessage(errors.toString());
        }
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
