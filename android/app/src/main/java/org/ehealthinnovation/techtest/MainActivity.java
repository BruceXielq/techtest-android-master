package org.ehealthinnovation.techtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;


public class MainActivity extends AppCompatActivity {
    private static final int SEARCH_REQUEST_CODE = 100;
    private static final int PATIENT_DETAILS_REQUEST_CODE = 101;
    private List<String> patienSummary = new ArrayList<>();
    private int updatePatientPosition = -1;
    //private String[] patienSummary = new String[10];
    int count = 0;
    ListView listView;
    TextView textView;
    String[] listItem;
    org.hl7.fhir.dstu3.model.Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ***** this code only for test use, need to use IntentService to search data from server
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // *** Use the DSTU 3 data model. The newer R4 model has limited Android support.
        FhirContext context = FhirContext.forDstu3();

        // *** Use the baseDstu3 server URL.
        // The newer baseR4 URL used in the test server examples has limited Android support.
        IGenericClient client = context.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

        // *** Take caution specifying search parameters, too many and the server times out.
        //org.hl7.fhir.dstu3.model.Bundle
        bundle = client.search().forResource(Patient.class)
                .where(Patient.FAMILY.matches().value("Lee"))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();
        if (null == bundle){
            Toast.makeText(getApplicationContext(),"No Patient found!", Toast.LENGTH_LONG).show();
            return;
        } else {
            Patient patient;
            if (null != bundle.getEntry()) {
                for(int i = 0; i < bundle.getEntry().size(); i++){
                    patient = (Patient) bundle.getEntry().get(i).getResource();
                    if (null != patient){
                        patienSummary.add( patient.getName().get(0).getGiven().get(0) + "."+ patient.getName().get(0).getFamily()
                                + "##" + patient.getGender() + "##" + patient.getBirthDate()
                                + "##" + patient.getIdBase());
                        count++;
                        if (count > 9) break;
                    }
                }
            }
        }

        listView=findViewById(R.id.listView);
        textView=findViewById(R.id.textView);
        listItem = patienSummary.toArray(new String[0]);
        String [] patientName = new String[listItem.length];
        String [] patientGender = new String[listItem.length];
        String [] patientBirthday = new String[listItem.length];
        String [] patientId = new String[listItem.length];

        if (null == listItem){
           listItem = getResources().getStringArray(R.array.array_technology);
        } else {
            for(int i = 0; i < listItem.length; i++){
                String[] arrOfStr = listItem[i].split("##");
                patientName[i] = arrOfStr[0];
                patientGender[i] = arrOfStr[1];
                patientBirthday[i] = arrOfStr[2];
                patientId[i] = arrOfStr[3];
            }
        }
        listItem = patientName;
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                updatePatientPosition = position;
                String value=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PatientDetailsActivity.class);
                intent.putExtra("patientName",listItem[position]);
                intent.putExtra("patientGender",patientGender[position]);
                intent.putExtra("patientBirthDay",patientBirthday[position]);
                intent.putExtra("patientId",patientId[position]);
                startActivityForResult(intent, PATIENT_DETAILS_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PATIENT_DETAILS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String patientId = data.getStringExtra("patientId");
                String patientName = data.getStringExtra("patientName");
                String patientGender = data.getStringExtra("patientGender");
                String patientBirthDay = data.getStringExtra("patientBirthDay");
                Toast.makeText(getApplicationContext(), "updatePatientPosition = [" + updatePatientPosition
                        + "] patientId = [" + patientId  + "] patientName = [" + patientName  + "] patientBirthDay = [" + patientBirthDay , Toast.LENGTH_LONG).show();

                // call server to update patient info;
            }
        }
        if (requestCode == SEARCH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnString = data.getStringExtra("result");
                // call server to update patient info;
            }
        }
    }
}
