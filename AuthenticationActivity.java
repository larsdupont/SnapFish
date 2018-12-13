package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Authentication";
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser firebaseUser = auth.getCurrentUser();

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authetication);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.verifyEmailButton).setOnClickListener(this);

        SetTest();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (firebaseUser == null) {
            menu.findItem(R.id.menu_action_authenticate).setVisible(false);
            menu.findItem(R.id.menu_action_create).setVisible(false);
            menu.findItem(R.id.menu_action_list).setVisible(false);
            menu.findItem(R.id.menu_action_main).setVisible(false);
            menu.findItem(R.id.menu_action_settings).setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_action_authenticate:
//                intent = new Intent(this, AuthenticationActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "Authenticate", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_create:
                intent = new Intent(this, ReportActivity.class);
                startActivity(intent);
                Toast.makeText(this, "New Report", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_list:
                intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                Toast.makeText(this, "List Reports", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_action_main:
                intent = new Intent(this, TileActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public void onStart() {

        super.onStart();
        updateUI();

    }

    @Override
    public void onClick(View v) {

        this.email = this.mEmailField.getText().toString();
        this.password = this.mPasswordField.getText().toString();

        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            signUp();
        } else if (i == R.id.emailSignInButton) {
            signIn();
        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        }
    }

    @Override
    public void onStop() {

        super.onStop();

    }

    private void signUp() {

        Log.d(TAG, "createAccount:" + this.email);
        if (!validateForm()) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            updateUI();
                            finishUp();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });

    }

    private void signIn() {

        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI();
                            finishUp();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
    }

    private void signOut() {

        auth.signOut();
        updateUI();

    }

    private void sendEmailVerification() {

        findViewById(R.id.verifyEmailButton).setEnabled(false);
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verifyEmailButton).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(AuthenticationActivity.this, "Verification email sent to " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            finishUp();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean validateForm() {

        boolean valid = true;

        String email = this.mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.mEmailField.setError("Required.");
            valid = false;
        } else {
            this.mEmailField.setError(null);
        }

        String password = this.mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.mPasswordField.setError("Required.");
            valid = false;
        } else {
            this.mPasswordField.setError(null);
        }

        return valid;

    }

    private void updateUI() {

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    private void SetTest() {

        this.email = "lars.dupont@youmail.dk";
        this.password = "Mathi@s99";

        this.mEmailField.setText(this.email);
        this.mPasswordField.setText(this.password);

    }

    private void finishUp() {

        try {
            Intent intent = new Intent(this, TileActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "finishUp: ", e);
        }

    }
}
