package dk.ikas.lcd.examproject;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ReportController {

    private final static String TAG = "ReportController";
    private Report report;

    public ReportController(Report report) {
        this.report = report;
    }

    public void saveReport() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                String uuid = UUID.randomUUID().toString();
                DatabaseReference ref = root.child("pike85").child(uuid);

                this.report.setUid(uid);
                this.report.setTemperature(System.currentTimeMillis());
                ref.setValue(this.report);

            } catch (Exception e) {
                Log.e(TAG, "createReport: ", e);
            }
        }

    }

    public Report getReport() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("getReport", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getReport", "Failed to read value.", error.toException());
            }
        });
        return report;
    }
}
