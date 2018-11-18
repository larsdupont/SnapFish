package dk.ikas.lcd.examproject;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CreateReport.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_CreateReport:
                fragment = new CreateReport();
                Toast.makeText(this, "Create report.", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_EditReport:
                fragment = new EditReport();
                Toast.makeText(this, "Edit report.", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_ListReports:
                fragment = new ListReports();
                Toast.makeText(this, "Home.", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_Setting:
                fragment = new Setting();
                Toast.makeText(this, "Settings.", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_ShowReport:
                fragment = new ShowReport();
                Toast.makeText(this, "Show report.", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        return true;
    }

    @Override
    public void SaveClicked(Report report) {

        Log.d("Report", report.getNotes());
        Log.d("Report", report.getDate().toString());
        Log.d("Report", report.getTime().toString());
    }
}
