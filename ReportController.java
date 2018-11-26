package dk.ikas.lcd.examproject;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportController {

    private Report report;

    public ReportController(Report report) {
        this.report = report;
    }

    public void saveReport() {

        if(this.report == null)
        {
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello, World!");
        try {
            myRef.setValue(this.report.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
