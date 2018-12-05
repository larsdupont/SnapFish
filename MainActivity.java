package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    public final Integer AuthenticationActivity = 1;
    public final Integer ReportActivity = 2;
    public final Integer ListActivity = 3;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_action_main).setVisible(false);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.findItem(R.id.menu_action_create).setVisible(false);
            menu.findItem(R.id.menu_action_list).setVisible(false);
            menu.findItem(R.id.menu_action_settings).setVisible(false);
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
//            case R.id.menu_action_main:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent, null);
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
//                return true;
            case R.id.menu_action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.AuthenticationActivity) {

            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d(TAG, "onActivityResult: " + id);

        }
    }
}
