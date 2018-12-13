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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.ikas.lcd.report.Report;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener, FirebaseController.FireBaseControllerListener {

    private final String TAG = "ReportActivity";
    private final Integer PhotoActivity = 1;
    private final FirebaseController ctrl = new FirebaseController();
    private Report report;
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
                    this.ctrl.SetImage(this.report);
                }
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

}
