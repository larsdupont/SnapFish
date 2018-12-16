package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import dk.ikas.lcd.settings.Settings;

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "SettingsActivity";
    private final Settings settings = Settings.getInstance();

    EditText etCommunity;
    RadioButton enMetrics;
    RadioButton euMetrics;
    EditText etListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.etCommunity = findViewById(R.id.settings_community);
        this.etCommunity.setText(settings.getCommunity());

        this.enMetrics = findViewById(R.id.settings_metrics_en);
        this.euMetrics = findViewById(R.id.settings_metrics_eu);
        if(settings.getMetric() == Settings.Metrics.ENGLISH){
            this.enMetrics.isChecked();
        } else {
            this.euMetrics.isChecked();
        }

        this.etListSize = findViewById(R.id.settings_list_size);
        this.etListSize.setText(settings.getListSize().toString());

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
                intent = new Intent(this, TileActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_action_settings:
//                intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    public void onClick(View view) {

        Editable text = this.etCommunity.getText();
        settings.setCommunity(text.toString());

        if (this.enMetrics.isChecked()) {
            settings.setMetric(Settings.Metrics.ENGLISH);
        } else if (this.euMetrics.isChecked()) {
            settings.setMetric(Settings.Metrics.CONTINELTAL);
        }

        Integer size = Integer.parseInt(this.etListSize.getText().toString());
        settings.setListSize(size);

        settings.savePreferences(this.getBaseContext());

    }
}
