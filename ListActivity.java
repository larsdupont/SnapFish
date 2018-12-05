package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    final String TAG = "ListActivity";

    final long ONE_MEGABYTE = 1024 * 1024;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("pike85");

    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReference("pike85");

    ListView view;
    ArrayList<Report> reportList = new ArrayList<>();
    File localFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.view = findViewById(R.id.list_card_view);
        final ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(this, R.layout.card_view, reportList) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.card_view, parent, false);
                }
                Report report = getItem(position);

                if (report.getHasImage()) {
                    if (report.getUri() != null) {
                        ImageView ivPicture = convertView.findViewById(R.id.picture);
                        ivPicture.setImageURI(report.getUri());
                    }
                }

                TextView twDate = convertView.findViewById(R.id.date);
                twDate.setText(report.getDate());

                TextView twTime = convertView.findViewById(R.id.time);
                twTime.setText(report.getTime());

//                TextView twPlace = convertView.findViewById(R.id.place);
//                twPlace.setText(report.getPlace());
//
//                TextView twWeather = convertView.findViewById(R.id.weather);
//                twWeather.setText(report.getWeather());

//                TextView twVisibility = convertView.findViewById(R.id.visibility);
//                twVisibility.setText(report.getVisibility().toString());

//                TextView twTemperature = convertView.findViewById(R.id.temperature);
//                twTemperature.setText(report.getTemperature().toString());

                TextView twSpecies = convertView.findViewById(R.id.species);
                twSpecies.setText(report.getSpecies());

                TextView twWeight = convertView.findViewById(R.id.weight);
                twWeight.setText(report.getWeight().toString());

                TextView twLength = convertView.findViewById(R.id.length);
                twLength.setText(report.getLength().toString());

//                TextView twNumber = convertView.findViewById(R.id.number);
//                twNumber.setText(report.getNumber().toString());
//
//                TextView twNotes = convertView.findViewById(R.id.notes);
//                twNotes.setText(report.getNotes());
//
//                TextView twRemarks = convertView.findViewById(R.id.remarks);
//                twRemarks.setText(report.getRemarks());

                //return super.getView(position, convertView, parent);
                return convertView;
            }

        };
        this.view.setAdapter(adapter);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("timeStamp").limitToLast(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot ds = dataSnapshot;
                for (DataSnapshot dsC : ds.getChildren()) {

                    Report report = dsC.getValue(Report.class);
                    reportList.add(report);

                }
                adapter.notifyDataSetChanged();

                for (Report report : reportList) {
                    if (report.getHasImage()) {
                        try {

                            StorageReference pathReference = storageRef.child(report.getUuid());
                            //https://firebasestorage.googleapis.com/v0/b/snapfish-981d9.appspot.com/o/pike85%2F08b973f6-fe9f-4216-b4b2-e371d2568fc1?alt=media&token=deb66605-b571-4e5d-82a4-1673e20cc9ba
                            localFile = File.createTempFile(report.getUuid(), ".jpg");
                            report.setUri(Uri.parse(localFile.toString()));
                            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot != null) {
                                        System.out.println("The read succeeded: " + taskSnapshot.getStorage().getName());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e(TAG, "onFailure: ", exception);
                                    // Handle any errors
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                System.out.println("The read failed: " + databaseError.getCode());
                Log.w(TAG, "onCancelled: ", databaseError.toException());

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_action_list).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_action_authenticate:
                intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent, null);
                Toast.makeText(this, "Authenticate", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_create:
                intent = new Intent(this, ReportActivity.class);
                startActivity(intent, null);
                Toast.makeText(this, "Create Report", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.menu_action_list:
//                intent = new Intent(this, ListActivity.class);
//                startActivity(intent, null);
//                Toast.makeText(this, "List Reports", Toast.LENGTH_LONG).show();
//                break;
            case R.id.menu_action_main:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent, null);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
