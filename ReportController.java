package dk.ikas.lcd.examproject;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ReportController {

    private final static String TAG = "ReportController";
    private final String uuid;

    private Report report;

    public ReportController(Report report) {
        this.report = report;
        this.uuid = UUID.randomUUID().toString();
    }

    public void saveReport() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {

                uploadImage();

            } catch (Exception e) {
                Log.e(TAG, "saveReport: ", e);
            }
        }

    }

//    public Report getReport() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("getReport", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("getReport", "Failed to read value.", error.toException());
//            }
//        });
//        return report;
//    }

    private void uploadImage() {

        Uri uri = this.report.getUri();
        if (uri != null) {

//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

            StorageReference ref = FirebaseStorage.getInstance().getReference("pike85").child(this.uuid);
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "onSuccess: ");
                            uploadReport();
                            //progressDialog.dismiss();
                            //Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                            //progressDialog.dismiss();
                            //Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void uploadReport(){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref = root.child("pike85").child(this.uuid);

                this.report.setUri(null);
                this.report.setUid(uid);
                this.report.setTimeStamp(System.currentTimeMillis());

                try {
                    ref.setValue(this.report);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Log.e(TAG, "createReport: ", e);
            }
        }

    }

}


