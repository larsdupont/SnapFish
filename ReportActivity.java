package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ReportActivity";
    private final Integer PhotoActivity = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        findViewById(R.id.saveReport).setOnClickListener(this);
        findViewById(R.id.selectPicture).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.saveReport):
                createReport();
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

    private void createReport() {

        Report report = new Report();

        EditText etDate = findViewById(R.id.date);
        if (isEmpty(etDate)) {
            etDate.setHint("@string/mandatory_field");
            etDate.setFocusable(true);
        } else {
            report.setDate(etDate.getText().toString());
        }

        EditText etLength = findViewById(R.id.length);
        if (!isEmpty(etLength)) {
            report.setLength(Double.parseDouble(etLength.getText().toString()));
        }

        EditText etNotes = findViewById(R.id.notes);
        if (!isEmpty(etNotes)) {
            report.setNotes(etNotes.getText().toString());
        }

        EditText etNumber = findViewById(R.id.number);
        if (!isEmpty(etNumber)) {
            report.setNumber(Integer.parseInt(etNumber.getText().toString()));
        }

        EditText etPlace = findViewById(R.id.place);
        if (!isEmpty(etPlace)) {
            report.setPlace(etPlace.getText().toString());
        }

        if (this.imageUri != null) {
            report.setUri(this.imageUri);
        }

        EditText etRemarks = findViewById(R.id.remarks);
        if (!isEmpty(etRemarks)) {
            report.setRemarks(etRemarks.getText().toString());
        }

        EditText etSpecies = findViewById(R.id.species);
        if (!isEmpty(etSpecies)) {
            report.setSpecies(etSpecies.getText().toString());
        }

        EditText etTemperature = findViewById(R.id.temperature);
        if (!isEmpty(etTemperature)) {
            report.setTemperature(Double.parseDouble(etTemperature.getText().toString()));
        }

        EditText etTime = findViewById(R.id.time);
        if (!isEmpty(etTime)) {
            report.setTime(etTime.getText().toString());
        }

        EditText etVisibility = findViewById(R.id.visibility);
        if (!isEmpty(etVisibility)) {
            report.setVisibility(Double.parseDouble(etVisibility.getText().toString()));
        }

        EditText etWeather = findViewById(R.id.weather);
        if (!isEmpty(etWeather)) {
            report.setWeather(etWeather.getText().toString());
        }

        EditText etWeight = findViewById(R.id.weight);
        if (!isEmpty(etWeight)) {
            report.setWeight(Double.parseDouble(etWeight.getText().toString()));
        }
        new ReportController(report).saveReport();
    }

    private Boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }

    public void selectPicture() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, this.PhotoActivity);

    }

}
