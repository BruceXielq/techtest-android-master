package org.ehealthinnovation.techtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PatientDetailsActivity extends AppCompatActivity {
    String patientId, patientGender, patientBirthDay, patientName;
    EditText etName, etGender, etBirthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        patientId = intent.getStringExtra("patientId");
        patientGender = intent.getStringExtra("patientGender");
        patientBirthDay = intent.getStringExtra("patientBirthDay");

        etName = findViewById(R.id.editTextName);
        etName.setText(patientName);
        etGender = findViewById(R.id.editTextGender);
        etGender.setText(patientGender);
        etBirthDay = findViewById(R.id.editTextBirthDay);
        etBirthDay.setText(patientBirthDay);

    }

    public void updatePatient(View view)
    {
        Toast.makeText(getApplicationContext(),"Update Patient : " + patientName,Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("patientName",etName.getText().toString());
        returnIntent.putExtra("patientGender",etGender.getText().toString());
        returnIntent.putExtra("patientBirthDay",etBirthDay.getText().toString());
        returnIntent.putExtra("patientId",patientId);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void closePatientDetails(View view)
    {
        Toast.makeText(getApplicationContext(),"Return to patient List",Toast.LENGTH_SHORT).show();
        finish();
    }

}
