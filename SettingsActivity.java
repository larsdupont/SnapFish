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

    private final static String TAG = "SettingsActivity";

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.findItem(R.id.menu_action_create).setVisible(false);
            menu.findItem(R.id.menu_action_main).setVisible(false);
            menu.findItem(R.id.menu_action_list).setVisible(false);
        }
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
            case R.id.menu_action_list:
                intent = new Intent(this, ListActivity.class);
                startActivity(intent, null);
                Toast.makeText(this, "List Reports", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_action_main:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent, null);
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.menu_action_settings:
//                intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent, null);
//                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onClick(View view) {

        Settings settings = Settings.getInstance();

        EditText etCommunity = findViewById(R.id.settings_community);
        Editable text = etCommunity.getText();
        settings.setCommunity(text.toString());

        RadioButton enMetrics = findViewById(R.id.settings_metrics_en);
        RadioButton euMetrics = findViewById(R.id.settings_metrics_eu);
        if (enMetrics.isChecked()) {
            settings.setMetric(Settings.Metrics.ENGLISH);
        } else if (euMetrics.isChecked()) {
            settings.setMetric(Settings.Metrics.CONTINELTAL);
        }

        settings.savePreferences();

    }
}
