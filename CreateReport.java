package dk.ikas.lcd.examproject;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateReport extends Fragment {

    private OnFragmentInteractionListener mListener;

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

        EditText etDate = (EditText) getView().findViewById(R.id.date);
        EditText etLength = (EditText) getView().findViewById(R.id.length);
        EditText etNotes = (EditText) getView().findViewById(R.id.notes);
        EditText etNumber = (EditText) getView().findViewById(R.id.number);
        EditText etPlace = (EditText) getView().findViewById(R.id.place);
        EditText etPicture = (EditText) getView().findViewById(R.id.picture);
        EditText etRemarks = (EditText) getView().findViewById(R.id.remarks);
        EditText etSpecies = (EditText) getView().findViewById(R.id.species);
        EditText etTemperature = (EditText) getView().findViewById(R.id.temperature);
        EditText etTime = (EditText) getView().findViewById(R.id.time);
        EditText etVisibility = (EditText) getView().findViewById(R.id.visibility);
        EditText etWeather = (EditText) getView().findViewById(R.id.weather);
        EditText etWeight = (EditText) getView().findViewById(R.id.weight);

        Report result = new Report();
        if (etDate.getText().toString() != "") {
            //result.setDate(LocalDate.parse(etDate.getText().toString()));
        }
        if (etLength.getText().toString() != "") {
            result.setLength(Float.parseFloat(etLength.getText().toString()));
        }
        result.setNotes(etNotes.getText().toString());
        if (etNumber.getText().toString() != "") {
            result.setNumber(Integer.parseInt(etNumber.getText().toString()));
        }
        result.setPlace(etPlace.getText().toString());
        if (etPicture.getText().toString() != "") {
            //result.setPicture(Image.Plane(etPicture.getText().toString()));
        }
        result.setRemarks(etRemarks.getText().toString());
        result.setSpecies(etSpecies.getText().toString());
        if (etTemperature.getText().toString() != "") {
            result.setTemperature(Float.parseFloat(etTemperature.getText().toString()));
        }
        if (etTime.getText().toString() != "") {
            //result.setTime(LocalDateTime.parse(etTime.getText()));
        }
        if (etVisibility.getText().toString() != "") {
            result.setVisibility(Float.parseFloat(etVisibility.getText().toString()));
        }
        result.setWeather(etWeather.getText().toString());
        if (etWeight.getText().toString() != "") {
            result.setWeight(Float.parseFloat(etWeight.getText().toString()));
        }
        return result;
    }
}
