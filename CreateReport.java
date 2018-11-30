package dk.ikas.lcd.examproject;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateReport extends Fragment {

    private final static String TAG = "CreateReport";
    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void SaveClicked(Report report);
    }

    public CreateReport() {

    }

    public static CreateReport newInstance() {
        CreateReport fragment = new CreateReport();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_report, container, false);
        Button btn = (Button) view.findViewById(R.id.btnSaveReport);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Report report = createReport();
                if (report != null && mListener != null) {
                    mListener.SaveClicked(report);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateReport.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Report createReport() {

        Report report = new Report();

        EditText etDate = (EditText) getView().findViewById(R.id.date);
        if (isEmpty(etDate)) {
            etDate.setHint("@string/mandatory_field");
            etDate.setFocusable(true);
        } else {
            report.setDate(etDate.getText().toString());
        }

        EditText etLength = (EditText) getView().findViewById(R.id.length);
        if (!isEmpty(etLength)) {
            report.setLength(Float.parseFloat(etLength.getText().toString()));
        }

        EditText etNotes = (EditText) getView().findViewById(R.id.notes);
        if (!isEmpty(etNotes)) {
            report.setNotes(etNotes.getText().toString());
        }

        EditText etNumber = (EditText) getView().findViewById(R.id.number);
        if (!isEmpty(etNumber)) {
            report.setNumber(Integer.parseInt(etNumber.getText().toString()));
        }

        EditText etPlace = (EditText) getView().findViewById(R.id.place);
        if (!isEmpty(etPlace)) {
            report.setPlace(etPlace.getText().toString());
        }

        EditText etPicture = (EditText) getView().findViewById(R.id.picture);
        if (!isEmpty(etPicture)) {
            //report.setPicture(Image.Plane(etPicture.getText().toString()));
        }

        EditText etRemarks = (EditText) getView().findViewById(R.id.remarks);
        if (!isEmpty(etRemarks)) {
            report.setRemarks(etRemarks.getText().toString());
        }

        EditText etSpecies = (EditText) getView().findViewById(R.id.species);
        if (!isEmpty(etSpecies)) {
            report.setSpecies(etSpecies.getText().toString());
        }

        EditText etTemperature = (EditText) getView().findViewById(R.id.temperature);
        if (!isEmpty(etTemperature)) {
            report.setTemperature(Float.parseFloat(etTemperature.getText().toString()));
        }

        EditText etTime = (EditText) getView().findViewById(R.id.time);
        if (!isEmpty(etTime)) {
            report.setTime(etTime.getText().toString());
        }

        EditText etVisibility = (EditText) getView().findViewById(R.id.visibility);
        if (!isEmpty(etVisibility)) {
            report.setVisibility(Float.parseFloat(etVisibility.getText().toString()));
        }

        EditText etWeather = (EditText) getView().findViewById(R.id.weather);
        if (!isEmpty(etWeather)) {
            report.setWeather(etWeather.getText().toString());
        }

        EditText etWeight = (EditText) getView().findViewById(R.id.weight);
        if (!isEmpty(etWeight)) {
            report.setWeight(Float.parseFloat(etWeight.getText().toString()));
        }
        return report;

//       FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            try {
//
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
//                String uuid = UUID.randomUUID().toString();
//                DatabaseReference ref = root.child("pike85").child(uuid);
//
//                report.setUid(uid);
//                report.setTemperature(System.currentTimeMillis());
//                ref.setValue(report);
//
//            } catch (Exception e) {
//                Log.e(TAG, "createReport: ", e);
//            }
//        }
    }

    private Boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }

    private Date getDateTime(EditText text) {
        if (!isEmpty(text)) {
            try {
                String dateTime = text.getText().toString().trim();
                DateFormat format = new SimpleDateFormat("dd:MM:yyyy hh:mm:ss");
                return format.parse(dateTime);
            } catch (Exception ex) {
                Log.e("CreateReport", "getDateTime: ", ex);
            }
        }
        return null;
    }

    private Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}