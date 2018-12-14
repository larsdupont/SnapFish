package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.ikas.lcd.report.Report;
import dk.ikas.lcd.settings.Settings;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, FirebaseController.FireBaseControllerListener {

    private final String TAG = "ListActivity";

    private final Integer size = Settings.getInstance().getListSize();
    private final FirebaseController ctrl = new FirebaseController();
    private ListView view;
    private ArrayList<Report> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.view = findViewById(R.id.list_card_view);

        this.ctrl.setListener(this);
        this.ctrl.GetReports(this.size);

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
//                intent = new Intent(this, ListActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "List Reports", Toast.LENGTH_LONG).show();
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
            case (R.id.picture):
                Intent intent = new Intent(this, ShowReportActivity.class);
                intent.putExtra("report", v.getTag().toString());
                startActivity(intent, null);
                Toast.makeText(this, "Show Report", Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    public void onDataChange(Report data) {

        for (Integer idx = 0; idx < this.reportList.size(); idx = idx + 1) {
            {
            }
            if (this.reportList.get(idx).getUuid() == data.getUuid()) {
                this.reportList.set(idx, data);
                fillView();
                return;
            }
        }
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

    @Override
    public void onImageSaved(Report data) {
        return;
    }

    private void fillView() {

        ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(this, R.layout.card_view, reportList) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.card_view, parent, false);
                }
                Report report = getItem(position);

                if (report.getHasImage()) {
                    if (report.getUri() != null) {
                        ImageView ivPicture = convertView.findViewById(R.id.picture);
                        ivPicture.setImageURI(report.getUri());
                        ivPicture.setTag(report.getUuid());
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
        adapter.notifyDataSetChanged();

    }
}
