package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.ikas.lcd.report.Report;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener, FirebaseController.FireBaseControllerListener {

    private final String TAG = "ReportActivity";
    private final Integer PhotoActivity = 1;
    private final FirebaseController ctrl = new FirebaseController();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        this.ctrl.setListener(this);

        findViewById(R.id.saveReport).setOnClickListener(this);
        findViewById(R.id.selectPicture).setOnClickListener(this);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);

        TextInputEditText vDate = findViewById(R.id.date);
        vDate.setText(formattedDate.toString());

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
//                intent = new Intent(this, ReportActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "New Report", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.saveReport):
                this.ctrl.SetImage(createReport());
                break;
            case (R.id.selectPicture):
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, this.PhotoActivity);
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

    @Override
    public void onDataChange(Report data) {
        return;
    }

    @Override
    public void onDataLoaded(ArrayList<Report> data) {
        return;
    }

    @Override
    public void onDataSaved(Report data) {

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }

    private Report createReport() {

        EditText etDate = findViewById(R.id.date);
        EditText etTime = findViewById(R.id.time);
        EditText etPlace = findViewById(R.id.place);
        EditText etWeather = findViewById(R.id.weather);
        EditText etVisibility = findViewById(R.id.visibility);
        EditText etTemperature = findViewById(R.id.temperature);
        EditText etSpecies = findViewById(R.id.species);
        EditText etWeight = findViewById(R.id.weight);
        EditText etLength = findViewById(R.id.length);
        EditText etNumber = findViewById(R.id.number);
        EditText etNotes = findViewById(R.id.notes);
        EditText etRemarks = findViewById(R.id.remarks);

        try {

            return new Report(
                    etDate.getText().toString(),
                    etTime.getText().toString(),
                    etPlace.getText().toString(),
                    etWeather.getText().toString(),
                    etVisibility.getText().toString(),
                    etTemperature.getText().toString(),
                    etSpecies.getText().toString(),
                    etWeight.getText().toString(),
                    etLength.getText().toString(),
                    etNumber.getText().toString(),
                    etNotes.getText().toString(),
                    etRemarks.getText().toString(),
                    this.imageUri,
                    this.imageUri.getPath(),
                    FirebaseAuth.getInstance().getUid().toString()
            );

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Report();
        }
    }

}
