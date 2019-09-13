package com.example.mountaindiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class MyProfilActivity extends AppCompatActivity {

    private Button buttonSave;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextNumber;
    private GlobalData gd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextNumber = findViewById(R.id.editTextNumber);
        gd = new GlobalData();
        initPersonalData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gd.setPersonalData(editTextName.getText().toString(), editTextSurname.getText().toString(), editTextNumber.getText().toString());
                finish();
            }
        });
    }

    private void initPersonalData() {
        int index = GlobalData.getDignity().indexOf(" ");
        String name = GlobalData.getName();
        String surname = GlobalData.getSurname();
        editTextName.setText((name));
        editTextSurname.setText(surname);
        editTextNumber.setText(GlobalData.getNumberSOS());
    }
}
