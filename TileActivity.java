package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import dk.ikas.lcd.settings.Settings;

public class TileActivity extends AppCompatActivity {

    private final String TAG = "TileActivity";
    private final String community = Settings.getInstance().getCommunity();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(community);
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference(community);

    private ArrayList<Report> reportList = new ArrayList<>();
    private File localFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);

        // Attach a listener to read the data at our posts reference
        ref.orderByChild("timeStamp").limitToLast(10).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsC : dataSnapshot.getChildren()) {
                    Report report = dsC.getValue(Report.class);
                    if (report != null) {
                        reportList.add(report);
                    }
                }
                //adapter.notifyDataSetChanged();

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
                                    FillView();
                                    //adapter.notifyDataSetChanged();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                    Log.e(TAG, "onFailure: ", exception);

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
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
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

    private void FillView() {

        for(int idx = 0; idx < this.reportList.size(); idx = idx + 1) {
            Report report = this.reportList.get(idx);
            if (report.getUri() != null) {

                int id = 0;
                switch (idx + 1) {
                    case 1:
                        id = R.id.tile_one;
                        break;
                    case 2:
                        id = R.id.tile_two;
                        break;
                    case 3:
                        id = R.id.tile_three;
                        break;
                    case 4:
                        id = R.id.tile_four;
                        break;
                    case 5:
                        id = R.id.tile_five;
                        break;
                    case 6:
                        id = R.id.tile_six;
                        break;
                    case 7:
                        id = R.id.tile_seven;
                        break;
                    case 8:
                        id = R.id.tile_eight;
                        break;
                    case 9:
                        id = R.id.tile_nine;
                        break;
                    case 10:
                        id = R.id.tile_ten;
                        break;

                }
                ImageView view = findViewById(id);
                view.setImageURI(report.getUri());

            }
        }
    }
}
