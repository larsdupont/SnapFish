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
import android.widget.ImageButton;
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
import java.io.Serializable;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

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
                        ImageButton btnImage = convertView.findViewById(R.id.picture);
                        btnImage.setImageURI(report.getUri());
                        btnImage.setTag(report.getUuid());
                    }
                }

                TextView twDate = convertView.findViewById(R.id.date);
                twDate.setText(report.getDate());

                TextView twTime = convertView.findViewById(R.id.time);
                twTime.setText(report.getTime());

                TextView twSpecies = convertView.findViewById(R.id.species);
                twSpecies.setText(report.getSpecies());

                TextView twWeight = convertView.findViewById(R.id.weight);
                twWeight.setText(report.getWeight().toString());

                TextView twLength = convertView.findViewById(R.id.length);
                twLength.setText(report.getLength().toString());

                return convertView;
            }

        };
        this.view.setAdapter(adapter);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("timeStamp").limitToLast(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsC : dataSnapshot.getChildren()) {
                    Report report = dsC.getValue(Report.class);
                    reportList.add(report);
                }
                adapter.notifyDataSetChanged();

                for (Report report : reportList) {
                    if (report.getHasImage()) {
                        try {

                            StorageReference pathReference = storageRef.child(report.getUuid());
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
        menu.findItem(R.id.menu_action_list).setEnabled(false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.picture):
                Intent intent = new Intent(this, ShowReportActivity.class);
                intent.putExtra("report", v.getTag().toString());
                startActivity(intent, null);
                Toast.makeText(this, "Show Report", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
