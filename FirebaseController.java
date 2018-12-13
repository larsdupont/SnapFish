package dk.ikas.lcd.examproject;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

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
import java.util.ArrayList;

import dk.ikas.lcd.report.Report;
import dk.ikas.lcd.settings.Settings;

public class FirebaseController {

    private final String TAG = "FirebaseController";
    private final String community = Settings.getInstance().getCommunity();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(community);
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference(community);
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private FireBaseControllerListener listener;
    private ArrayList<Report> reportList = new ArrayList<>();
    private File localFile = null;

    public interface FireBaseControllerListener {

        public void onDataChange(Report data);

        public void onDataLoaded(ArrayList<Report> data);

        public void onDataSaved(Report data);

    }

    public void setListener(FireBaseControllerListener listener) {

        this.listener = listener;

    }

    public FirebaseController() {

        this.listener = null;

    }

    public void GetImage(final Report report) {

        if (firebaseUser != null) {
            return;
        }

        //    private final long ONE_MEGABYTE = 1024 * 1024;
        if (report.getHasImage()) {
            try {

                localFile = File.createTempFile(report.getUuid(), ".jpg");
                report.setUri(Uri.parse(localFile.toString()));
                storageRef
                        .child(report.getUuid())
                        .getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot != null) {
                                    Log.w(TAG, "The Image Download Succeeded: " + taskSnapshot.getStorage().getName());
                                }
                                listener.onDataChange(report);
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.w(TAG, "The Image Download Failed: " + exception.getMessage(), exception);
                            }

                        });

            } catch (IOException e) {
                Log.e(TAG, "Image Download Exception: " + e.getMessage(), e);
            }
        }
    }

    public void SetImage(final Report report) {

        if (firebaseUser != null) {
            return;
        }

        Uri uri = report.getUri();
        if (uri != null) {

            storageRef
                    .child(report.getUuid())
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "Image Upload Success");
                            SetReport(report);
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Image Upload Failure: " + e.getMessage(), e);
                        }

                    });

        }

    }

    public void GetReports(Integer size) {

        if (firebaseUser != null) {
            return;
        }

        ref
                .orderByChild("timeStamp")
                .limitToLast(size)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dsC : dataSnapshot.getChildren()) {
                            Report report = dsC.getValue(Report.class);
                            if (report != null) {
                                reportList.add(report);
                                GetImage(report);
                            }
                        }
                        listener.onDataLoaded(reportList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Log.w(TAG, "The read failed: " + databaseError.getCode());
                        Log.w(TAG, "The read failed: : " + databaseError.getMessage(), databaseError.toException());

                    }

                });

    }

    public void SetReport(final Report report) {

        if (firebaseUser != null) {
            return;
        }

        report.setUid(firebaseUser.getUid());
        report.setUri(null);
        report.setTimeStamp(System.currentTimeMillis());
        ref
                .child(report.getUuid())
                .setValue(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void v) {
                        listener.onDataSaved(report);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "report Upload Failed: " + e.getMessage(), e);
                    }

                });

    }

}
