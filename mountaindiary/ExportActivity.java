package com.example.mountaindiary;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ExportActivity extends AppCompatActivity {

    private static final String DB_NAME = "MountainDiaryDB";

    private Button buttonGainExport;
    private Button buttonPeakExport;
    private Button buttonGainImport;
    private Button buttonPeakImport;
    private EditText editTextExport;
    private GlobalData gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        gd = new GlobalData();

        buttonGainExport = findViewById(R.id.buttonGainExport);
        buttonPeakExport = findViewById(R.id.buttonPeakExport);
        buttonGainImport = findViewById(R.id.buttonGainImport);
        buttonPeakImport = findViewById(R.id.buttonPeakImport);
        editTextExport = findViewById(R.id.editTextExport);

        buttonGainExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="";
                for (Gain gain : gd.getGainsList()){
                       text += new StringBuilder()
                               .append(gain.getId())
                               .append(";")
                               .append(gain.getPeakId())
                               .append(";")
                               .append(gain.getPeakStringId())
                               .append(";")
                               .append(gain.getDateWinter())
                               .append(";")
                               .append(gain.getDateSummer())
                               .append(";")
                               .append(gain.getCoordX())
                               .append(";")
                               .append(gain.getCoordY())
                               .append(";")
                               .append(gain.getUri0())
                               .append(";")
                               .append(gain.getUri1())
                               .append(";")
                               .append(gain.getUri2())
                               .append(";")
                               .append(gain.getUri3())
                               .append(";")
                               .append(gain.getUri4())
                               .append(";")
                               .append(gain.getMemory())
                               .append("\n")
                               .toString();
                }
                editTextExport.setText(text);
                exportDB();
            }
        });

        buttonPeakExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="";
                for (Peak peak : gd.getPeaksList()){
//                    if(peak.getId()<=638){
//                        continue;
//                    }
                    text += new StringBuilder()
                            .append(peak.getId())
                            .append(";")
                            .append(peak.getTitle())
                            .append(";")
                            .append(peak.getChain())
                            .append(";")
                            .append(peak.getRange())
                            .append(";")
                            .append(peak.getHeight())
                            .append(";")
                            .append(peak.getDescription())
                            .append(";")
                            .append(peak.getxCord())
                            .append(";")
                            .append(peak.getyCord())
                            .append(";")
                            .append(peak.getGainId())
                            .append(";")
                            .append(peak.getRangeId())
                            .append("\n")
                            .toString();
                }
                editTextExport.setText(text);
                exportDB();
            }
        });

        buttonGainImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();
            }
        });

        buttonPeakImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();
            }
        });
    }

    private void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "MountainDiary"
                        + "//databases//" + DB_NAME;
                String backupDBPath  = "/BackupFolder/" + DB_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    private void importDB() {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "MountainDiary"
                        + "//databases//" + DB_NAME;
                String backupDBPath  = "/BackupFolder/" + DB_NAME;
                File  backupDB= new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }
}
