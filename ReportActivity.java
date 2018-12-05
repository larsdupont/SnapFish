package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ReportActivity";
    private final Integer PhotoActivity = 1;
    private Uri imageUri;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        findViewById(R.id.saveReport).setOnClickListener(this);
        findViewById(R.id.selectPicture).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private Boolean createReport() {

        try {

            this.report = new Report();

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

            StorageReference ref = FirebaseStorage.getInstance().getReference("pike85").child(this.report.getUuid());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(ReportActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(ReportActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                DatabaseReference ref = root.child("pike85").child(this.report.getUuid());

                this.report.setUri(null);
                this.report.setUid(uid);
                this.report.setTimeStamp(System.currentTimeMillis());

                try {

                    ref.setValue(this.report);
                    Toast.makeText(ReportActivity.this, "Report uploaded ", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ReportActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(TAG, "createReport: ", e);
            }
        }
    }
}
