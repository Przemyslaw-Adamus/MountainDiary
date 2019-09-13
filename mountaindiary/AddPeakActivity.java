package com.example.mountaindiary;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddPeakActivity extends AppCompatActivity implements LocationListener {
    private static final String[] RANGE = new String[]{
            "Karpaty - Tatry",
            "Karpaty - Pieniny",
            "Karpaty - Beskid Śląski",
            "Karpaty - Beskid Mały",
            "Karpaty - Beskid Makowski",
            "Karpaty - Beskid Żywiecki",
            "Karpaty - Beskid Wyspowy",
            "Karpaty - Gorce",
            "Karpaty - Beskid Sądecki",
            "Karpaty - Beskid Niski",
            "Karpaty - Bieszczady",
            "Karpaty - Góry Sanocko-Turczańskie",
            "Góry Świętokrzyskie - Pasmo Klonowskie",
            "Góry Świętokrzyskie - Pasmo Bostowskie",
            "Góry Świętokrzyskie - Pasmo Oblęgorskie",
            "Góry Świętokrzyskie - Pasmo Masłowskie",
            "Góry Świętokrzyskie - Łysogóry",
            "Góry Świętokrzyskie - Pasmo Jeleniowskie",
            "Góry Świętokrzyskie - Pasmo Zgórskie",
            "Góry Świętokrzyskie - Pasmo Posłowickie",
            "Góry Świętokrzyskie - Pasmo Dymińskie",
            "Góry Świętokrzyskie - Pasmo Brzechowskie",
            "Góry Świętokrzyskie - Pasmo Orłowińskie",
            "Góry Świętokrzyskie - Pasmo Iwaniskie",
            "Góry Świętokrzyskie - Pasmo Wygiełzowskie",
            "Góry Świętokrzyskie - Pasmo Chęcińskie",
            "Góry Świętokrzyskie - Pasmo Bolechowieckie",
            "Góry Świętokrzyskie - Pasmo Daleszyckie",
            "Góry Świętokrzyskie - Pasmo Cisowskie",
            "Góry Świętokrzyskie - Pasmo Ociesęckie",
            "Góry Świętokrzyskie - Pasmo Kadzielniańskie",
            "Sudety - Karkonosze",
            "Sudety - Góry Izerskie",
            "Sudety - Góry Kaczawskie",
            "Sudety - Rudawy Janowickie",
            "Sudety - Góry Wałbrzyskie",
            "Sudety - Góry Kamienne",
            "Sudety - Góry Sowie",
            "Sudety - Góry Bardzkie",
            "Sudety - Góry Stołowe",
            "Sudety - Góry Bystrzyckie",
            "Sudety - Góry Orlickie",
            "Sudety - Masyw Śnieżnika",
            "Sudety - Góry Bialskie",
            "Sudety - Góry Złote",
            "Sudety - Góry Opawskie"};

    private GlobalData gd;
    private LocationManager lm;
    private String bestProvider;
    private Location location;
    private Criteria criteria;
    private Calendar calendar;
    private Button buttonAddAddPeak;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextHeight;
    private EditText editTextCoordX;
    private EditText editTextCoordY;
    private AutoCompleteTextView autoCompleteTextViewCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peak);
        gd = new GlobalData();
        calendar = Calendar.getInstance();

        buttonAddAddPeak = findViewById(R.id.buttonAddAddPeak);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextHeight = findViewById(R.id.editTextHeight);
        autoCompleteTextViewCurrency = findViewById(R.id.autoCompleteTextView);
        editTextCoordX = findViewById(R.id.editTextCoordX);
        editTextCoordY = findViewById(R.id.editTextCoordY);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = lm.getLastKnownLocation(bestProvider);
        if (location == null) {
            List<String> providers = lm.getAllProviders();
            for (int i = providers.size() - 1; i >= 0; i--) {
                location = lm.getLastKnownLocation(providers.get(i));
                if (location != null) break;
            }
        }
        lm.requestLocationUpdates(bestProvider, 1000, 1, this);
        editTextCoordX.setText(Double.toString(location.getLatitude()));
        editTextCoordY.setText(Double.toString(location.getLongitude()));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, RANGE);
        autoCompleteTextViewCurrency.setAdapter(adapter);

        buttonAddAddPeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String index = editTextName.getText().toString() + editTextHeight.getText().toString();
                if (editTextName.getText().toString().length() <= 0) {
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie podałeś nazwy szczytu")
                            .show();
                } else if (editTextCoordX.getText().toString().length() <= 0) {
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie podałeś szerokości geograficznej szczytu!")
                            .show();
                } else if (editTextCoordY.getText().toString().length() <= 0) {
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie podaleś długości geograficznej szczytu!")
                            .show();
                } else if (editTextHeight.getText().toString().length() <= 0) {
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie podaleś wysokości szczytu !")
                            .show();
                } else if (!isRangeGet(autoCompleteTextViewCurrency.getText().toString())) {
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Wybierz pasmo górskie z podpowiedzi!")
                            .show();
                } else if (Integer.valueOf(editTextHeight.getText().toString()) < 1) {
                    //return;
                    new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Podana wysokość jest nieprawidłowa!")
                            .show();
                } else {
                    String chr = autoCompleteTextViewCurrency.getText().toString();

                    final String title = editTextName.getText().toString();
                    final String chain = chr.substring(0, chr.indexOf("-") - 1);
                    final String range = chr.substring(chain.length() + 3);
                    final int height = Integer.valueOf(editTextHeight.getText().toString());
                    final String description = "OPIS:" + editTextDescription.getText().toString();
                    final float xCord = Float.valueOf(editTextCoordX.getText().toString());
                    final float yCord = Float.valueOf(editTextCoordY.getText().toString());
                    int rangeId = 15;
                    switch (chain) {
                        case "Karpaty":
                            rangeId = 12;
                            break;
                        case "Sudety":
                            rangeId = 13;
                            break;
                        case "Góry Świętokrzyskie":
                            rangeId = 14;
                            break;
                        default:
                            rangeId = 15;
                            break;
                    }
                    final int finalRangeId = rangeId;
                    if (yCord > 14.9373 && yCord < 24.4128 && xCord > 48.3193 && xCord < 51.5171) {
                        if (gd.getPeakNameAndHeight(title + height).size() > 0) {
                            new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Podany szczyt już istnieje!")
                                    .setConfirmText("Poprawię")
                                    .show();
                        } else {
                            new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Czy jesteś pewny?")
                                    .setContentText("chcesz dodać taki szczyt ?")
                                    .setConfirmText("Tak")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Peak peak = new Peak(finalRangeId, title, chain, range, height, description, xCord, yCord);

                                            gd.addPeak(peak);
                                            new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Yeaa...")
                                                    .setContentText("Poprawnie dodano nowy szczyt. Teraz możesz zarejestrować jego zdobycie.")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            finish();
                                                            Intent costsActivity = new Intent(AddPeakActivity.this, MountainDiaryActivity.class);
                                                            startActivity(costsActivity);
                                                        }
                                                    })
                                                    .show();

                                        }
                                    })
                                    .setCancelButton("Nie", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        SweetAlertDialog dialog = new SweetAlertDialog(AddPeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Podane współżędne nie obejmują Karpat, Sudet ani Gór Świętokrzyskich")
                                .setConfirmText("Poprawię");
                        dialog.show();
                    }
                }
            }

            private boolean isRangeGet(String toString) {
                for (String string : RANGE) {
                    if (toString.compareTo(string) == 0) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MountainDiaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = lm.getLastKnownLocation(bestProvider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
