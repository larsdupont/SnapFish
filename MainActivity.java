package dk.ikas.lcd.examproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements CreateReport.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";
    private final Integer AuthenticationActivity = 1;
    private final Integer PhotoActivity = 2;
    //private final Integer CreateReport = 2;

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
//        if (this.mAuth.getCurrentUser() == null) {
//            findViewById(R.id.action_CreateReport).setVisibility(View.INVISIBLE);
//        }
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
//            case R.id.action_EditReport:
//                fragment = new EditReport();
//                Toast.makeText(this, "Edit report.", Toast.LENGTH_LONG).show();
//                break;
            case R.id.action_ListReports:
                fragment = new ListReports();
                Toast.makeText(this, "Home.", Toast.LENGTH_LONG).show();
                break;
//            case R.id.action_Setting:
//                fragment = new Setting();
//                Toast.makeText(this, "Settings.", Toast.LENGTH_LONG).show();
//                break;
            case R.id.action_ShowReport:
                fragment = new ShowReport();
                Toast.makeText(this, "Show report.", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_signin:
                //fragment = new SignInFragment();
                //Toast.makeText(this, "Sign In.", Toast.LENGTH_LONG);
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivityForResult(intent, this.AuthenticationActivity);
                return true;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.AuthenticationActivity) {

            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d(TAG, "onActivityResult: " + id);

        } else if (requestCode == this.PhotoActivity) {

            switch (resultCode) {
                case -1:
                    if (resultCode == RESULT_OK) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Log.d(TAG, "onActivityResult: " + selectedImage);
                        // TODO: 01/12/2018  
                        //imageview.setImageURI(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = data.getData();
                        Log.d(TAG, "onActivityResult: " + selectedImage);
                        // TODO: 01/12/2018
                        //imageview.setImageURI(selectedImage);
                    }
                    break;
            }
        }

    }

    @Override
    public void SaveClicked(Report report) {

        new ReportController(report).saveReport();

    }

    @Override
    public void ImageClicked() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, this.PhotoActivity);

    }
}
