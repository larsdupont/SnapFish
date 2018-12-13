package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import dk.ikas.lcd.report.Report;

public class TileActivity extends AppCompatActivity implements FirebaseController.FireBaseControllerListener {

    private final String TAG = "TileActivity";
    private final FirebaseController ctrl = new FirebaseController();
    private ArrayList<Report> reportList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile);

        ctrl.setListener(this);
        ctrl.GetReports(10);
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

    @Override
    public void onDataChange(Report data) {

        updateView(data);

    }

    @Override
    public void onDataLoaded(ArrayList<Report> data) {

        this.reportList = data;
        fillView();

    }

    @Override
    public void onDataSaved(Report data) {
        return;
    }

    private void fillView() {

        for (int idx = 0; idx < this.reportList.size(); idx = idx + 1) {
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
                if(view != null) {
                    view.setTag(report.getUuid());
                    view.setImageURI(report.getUri());
                }

            }
        }
    }

    private void updateView(Report report){

        LinearLayout mainView = findViewById(R.id.tile_layout_main);
        for(Integer idx = 0; idx < mainView.getChildCount(); idx = idx + 1){

            LinearLayout middleView = (LinearLayout) mainView.getChildAt(idx);
            for (Integer jdx = 0; jdx < middleView.getChildCount(); jdx = jdx + 1){

                ImageView imageView = (ImageView) middleView.getChildAt(jdx);
                if(imageView.getTag() == report.getUuid())
                {
                    imageView.setImageURI(report.getUri());
                    return;
                }

            }

        }

    }
}
