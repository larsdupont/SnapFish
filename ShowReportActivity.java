package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import dk.ikas.lcd.settings.Settings;

public class ShowReportActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ShowReportActivity";
    private final Integer PhotoActivity = 1;
    private final String community = Settings.getInstance().getCommunity();

    private String uuid;
    private Report report = new Report();
    private Uri imageUri = null;
    private File localFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get("report") != null) {
            this.uuid = extras.get("report").toString();
            try {
                this.localFile = File.createTempFile(this.uuid, ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getReport();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_action_authenticate:
                intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Authenticate", Toast.LENGTH_LONG).show();
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
                intent = new Intent(this, MainActivity.class);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.saveReport):
                if (createReport()) {
                    uploadImage();
                    uploadReport();
                }
                break;
            case (R.id.selectPicture):
                selectPicture();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.PhotoActivity) {
            if (resultCode == RESULT_OK) {
                try {

                    this.imageUri = data.getData();
                    ImageView imageView = findViewById(R.id.picture);
                    imageView.setImageURI(this.imageUri);

                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: ", e);
                }
            }
        }
    }

    public void selectPicture() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, this.PhotoActivity);

    }

    private void getReport() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(community);
        ref.child(this.uuid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    report = dataSnapshot.getValue(Report.class);
                    report.setUri(Uri.parse(localFile.toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }

        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(community).child(this.uuid);
        storageRef.getFile(this.localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot != null) {
                    System.out.println("The read succeeded: " + taskSnapshot.getStorage().getName());
                }
                setReport();
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("onFailure download image: " + e.getMessage());
            }

        });

    }

    private Boolean createReport() {

        try {

            EditText etDate = findViewById(R.id.date);
            if (isEmpty(etDate)) {
                etDate.setHint("@string/mandatory_field");
                etDate.setFocusable(true);
            } else {
                this.report.setDate(etDate.getText().toString());
            }

            EditText etLength = findViewById(R.id.length);
            if (!isEmpty(etLength)) {
                this.report.setLength(Double.parseDouble(etLength.getText().toString()));
            }

            EditText etNotes = findViewById(R.id.notes);
            if (!isEmpty(etNotes)) {
                this.report.setNotes(etNotes.getText().toString());
            }

            EditText etNumber = findViewById(R.id.number);
            if (!isEmpty(etNumber)) {
                this.report.setNumber(Integer.parseInt(etNumber.getText().toString()));
            }

            EditText etPlace = findViewById(R.id.place);
            if (!isEmpty(etPlace)) {
                this.report.setPlace(etPlace.getText().toString());
            }

            if (this.imageUri != null) {
                this.report.setUri(this.imageUri);
                this.report.setImage(this.imageUri.getPath());
                this.report.setHasImage(true);
            }

            EditText etRemarks = findViewById(R.id.remarks);
            if (!isEmpty(etRemarks)) {
                this.report.setRemarks(etRemarks.getText().toString());
            }

            EditText etSpecies = findViewById(R.id.species);
            if (!isEmpty(etSpecies)) {
                this.report.setSpecies(etSpecies.getText().toString());
            }

            EditText etTemperature = findViewById(R.id.temperature);
            if (!isEmpty(etTemperature)) {
                this.report.setTemperature(Double.parseDouble(etTemperature.getText().toString()));
            }

            EditText etTime = findViewById(R.id.time);
            if (!isEmpty(etTime)) {
                this.report.setTime(etTime.getText().toString());
            }

            EditText etVisibility = findViewById(R.id.visibility);
            if (!isEmpty(etVisibility)) {
                this.report.setVisibility(Double.parseDouble(etVisibility.getText().toString()));
            }

            EditText etWeather = findViewById(R.id.weather);
            if (!isEmpty(etWeather)) {
                this.report.setWeather(etWeather.getText().toString());
            }

            EditText etWeight = findViewById(R.id.weight);
            if (!isEmpty(etWeight)) {
                this.report.setWeight(Double.parseDouble(etWeight.getText().toString()));
            }
            return true;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() <= 0;
    }

    private void uploadImage() {

        Uri uri = this.report.getUri();
        if (uri != null) {

            StorageReference ref = FirebaseStorage.getInstance().getReference(community).child(this.report.getUuid());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(ShowReportActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(ShowReportActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                        }
//                    });
        }
    }

    private void uploadReport() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {

                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref = root.child(community).child(this.report.getUuid());

                this.report.setUri(null);
                this.report.setUid(uid);
                this.report.setTimeStamp(System.currentTimeMillis());

                try {

                    ref.setValue(this.report);
                    Toast.makeText(ShowReportActivity.this, "Report uploaded ", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ShowReportActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(TAG, "createReport: ", e);
            }
        }
    }

    private void setReport() {

        EditText etDate = findViewById(R.id.date);
        etDate.setText(this.report.getDate());

        EditText etTime = findViewById(R.id.time);
        etTime.setText(this.report.getTime());

        EditText etPlace = findViewById(R.id.place);
        etPlace.setText(this.report.getPlace());

        EditText etWeather = findViewById(R.id.weather);
        etWeather.setText(this.report.getWeather());

        EditText etVisibility = findViewById(R.id.visibility);
        etVisibility.setText(this.report.getVisibility().toString());

        EditText etTemperature = findViewById(R.id.temperature);
        etTemperature.setText(this.report.getTemperature().toString());

        EditText etSpecies = findViewById(R.id.species);
        etSpecies.setText(this.report.getWeather());

        EditText etWeight = findViewById(R.id.weight);
        etWeight.setText(this.report.getWeight().toString());

        EditText etLength = findViewById(R.id.length);
        etLength.setText(this.report.getLength().toString());

        EditText etNumber = findViewById(R.id.number);
        etNumber.setText(this.report.getNumber().toString());

        EditText etNotes = findViewById(R.id.notes);
        etNotes.setText(this.report.getNotes());

        EditText etRemarks = findViewById(R.id.remarks);
        etRemarks.setText(this.report.getRemarks());

        ImageView ivPicture = findViewById(R.id.picture);
        ivPicture.setImageURI(this.report.getUri());

    }
}
