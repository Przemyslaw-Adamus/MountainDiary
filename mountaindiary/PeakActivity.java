package com.example.mountaindiary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class PeakActivity extends AppCompatActivity implements LocationListener {
    private int CONTINUE =-1;
    private RecyclerView recyclerView;
    private ItemGainAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button buttonConfirmLocation;
    private Button buttonWiki;
    private Button buttonConfirmFoto;
    private Button buttonMap;
    private TextView textViewName;
    private TextView textViewChain;
    private TextView textViewRange;
    private TextView textViewHeight;
    private TextView textViewDescription;

    private GlobalData gd;
    private int index;
    private List<Gain> gains;
    private Peak selectPeak;
    private LocationManager lm;
    private String bestProvider;
    private Location location;
    private Criteria criteria;
    private Calendar calendar;
    private String currentPhotoPath;
    private boolean REQUEST_TAKE_PHOTO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peak);
        recyclerView = findViewById(R.id.recyclerViewPeakLight);
        buttonConfirmLocation = findViewById(R.id.buttonConfirmLocation);
        buttonMap = findViewById(R.id.buttonlocationPeak);
        buttonConfirmFoto = findViewById(R.id.buttonConfirmFoto);
        buttonWiki = findViewById(R.id.buttonWiki);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewRange = findViewById(R.id.textViewRange);
        textViewChain = findViewById(R.id.textViewChain);
        textViewName = findViewById(R.id.textViewName);

        gd = new GlobalData();
        initTextViews();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        itemAdapter = new ItemGainAdapter((ArrayList<Gain>) gains);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Nie wyraziłeś jeszcze zgody na urzywanie GPS przez aplikację \"Górski Pamietnik\". Możesz to zmienić w ustawieniach")
                    .show();
        }
        else{

            lm.requestLocationUpdates(bestProvider, 1000, 1, this);
            location = lm.getLastKnownLocation(bestProvider);
            if(location == null){
                List<String> providers = lm.getAllProviders();
                for (int i=providers.size()-1; i>=0; i--) {
                    location = lm.getLastKnownLocation(providers.get(i));
                    if (location != null) break;
                }
            }
        }

        buttonConfirmLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie wyraziłeś jeszcze zgody na urzywanie GPS przez aplikację \"Górski Pamietnik\". Możesz to zmienić w ustawieniach telefonu")
                            .show();
                    return;
                }
                if (location == null) {
                    new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops... Brak odczytu GPS")
                            .setContentText("Znajdujesz się poza zasięgiem lub Twój GPS jest wyłączony")
                            .show();
                    return;
                }
                float distance = getDistanceInMeters((float) location.getLatitude(), (float) location.getLongitude(), selectPeak.getxCord(), selectPeak.getyCord());
                if (distance < 300f) {
                    Date date = Calendar.getInstance().getTime();
                    String stringDate = (String) DateFormat.format("dd-MM-yyyy   HH:mm", date);

                    if (date.getMonth() >= 11 || date.getMonth() <= 2) {
                        gd.addGain(new Gain(selectPeak.getId(), selectPeak.getTitle() + selectPeak.getHeight(), stringDate, "", (float) location.getLatitude(), (float) location.getLongitude(), " ", " ", " ", " ", " ", 1));
                    } else {
                        gd.addGain(new Gain(selectPeak.getId(), selectPeak.getTitle() + selectPeak.getHeight(), "", stringDate, (float) location.getLatitude(), (float) location.getLongitude(), " ", " ", " ", " ", " ", 1));
                    }
                    new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("GRATULACJĘ !")
                            .setContentText("Cieszymy się razem z Tobą z kolejnego zdobytego szczytu. Brawo")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                    Intent intent = new Intent(PeakActivity.this, MountainDiaryActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops... Jesteś za daleko od szczytu")
                            .setContentText("Szacunkowa odległość do celu to: " + distance + " m")
                            .show();
                }
            }

        });

        buttonConfirmFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nie wyraziłeś jeszcze zgody na urzywanie zdjęć/aparatu przez aplikację \"Górski Pamietnik\". Możesz to zmienić w ustawieniach telefonu")
                            .show();
                    return;
                }
                final Dialog dialog = new Dialog(PeakActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choose_foto);
                ImageButton buttonFotoDialog = dialog.findViewById(R.id.buttonFotoDialog);
                ImageButton buttonGalleryDialog = dialog.findViewById(R.id.buttonGalleryDialog);
                buttonFotoDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("POTWIERDZENIE ZDOBYCIA SZCZYTU")
                                .setContentText("Na zdjęciu umieścić tabliczkę znamionową szczytu, lub widok, który pozwoli na jego identyfikację.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        createTakePictureIntent();
                                    }
                                })
                                .show();
                    }
                });
                buttonGalleryDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("POTWIERDZENIE ZDOBYCIA SZCZYTU")
                                .setContentText("Na zdjęciu umieścić tabliczkę znamionową szczytu, lub widok, który pozwoli na jego identyfikację.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie"), 1);
                                    }
                                })
                                .show();
                    }
                });
                dialog.show();

            }
        });

        buttonWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://pl.wikipedia.org/wiki/" + gd.getPeaksList().get(index).getTitle());
                Intent intentWWW = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentWWW);
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formatter formatter = new Formatter();
                formatter.format("https://www.google.com/maps/place/%1$s,%2$s/@%1$s,%2$s",Float.toString(selectPeak.getxCord()),Float.toString(selectPeak.getyCord()));
                Uri uri = Uri.parse(formatter.toString());
                Intent intentWWW = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentWWW);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initTextViews() {
        index = getIntent().getIntExtra("id", -1);
        if (index == -1) {
            Toast.makeText(this, "ERROR INDEX -1", Toast.LENGTH_LONG).show();
            return;
        }
        selectPeak = gd.getPeaksList().get(index);
        textViewName.setText(selectPeak.getTitle());
        textViewChain.setText("Łańcuch: " + selectPeak.getChain());
        textViewRange.setText("Pasmo: " + selectPeak.getRange());
        textViewHeight.setText("Wysokość: " + String.valueOf(selectPeak.getHeight()) + " m.n.p.m");
        textViewDescription.setText("Opis: " + selectPeak.getDescription());

        gains = new ArrayList<Gain>();
        for (Gain gain : gd.getGainsList()) {
            if (gain.getPeakStringId().equals(selectPeak.getTitle() + selectPeak.getHeight()))
                gains.add(gain);
        }
    }

    public float getDistanceInMeters(float x1, float y1, float x2, float y2) {
        float radius = 6371000f;// promień Ziemi w metrach
        float diffLat = x1 - x2;
        float diffLng = y1 - y2;
        float a = (float) (sin(diffLat / 2.) * sin(diffLat / 2.) + cos(x2) * cos(x1) * sin(diffLng / 2.) * sin(diffLng / 2.));

        float b = (float) (2. * asin(sqrt(a)));
        b = (float) (2.0 * atan2(sqrt(a), sqrt(1.0 - a)));
        float distance = round((radius * b));
        distance = (float) (sqrt(pow((x2 - x1), 2.) + pow((cos((x1 * Math.PI) / 180.)) * (y2 - y1), 2.)) * (40075.704 / 360.0));
        distance *= 1000f;
        Log.v("X1:", Float.toString(x1));
        Log.v("Y1:", Float.toString(y1));
        Log.v("X2:", Float.toString(x2));
        Log.v("Y2:", Float.toString(y2));

        Log.v("ODLEGŁSC:", Float.toString(distance));

        return distance;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(PeakActivity.this, MountainDiaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PeakActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bitmap bitmap = null;
        Uri uri = null;
        String realPath = null;
        try {
            if (resultCode == RESULT_OK) {
                REQUEST_TAKE_PHOTO = true;
                switch (requestCode) {
                    case 0:
                        try {
                            File file = new File(currentPhotoPath);
                            uri = Uri.fromFile(file);
                            realPath = uri.toString();
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT < 11)
                            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, intent.getData());

                            // SDK >= 11 && SDK < 19
                        else if (Build.VERSION.SDK_INT < 19)
                            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, intent.getData());

                            // SDK > 19 (Android 4.4)
                        else
                            realPath = RealPathUtil.getRealPathFromURI_API19(this, intent.getData());
                        try {
                            uri = intent.getData();
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                getGain(bitmap, realPath);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void getGain(Bitmap bitmap, String realPath) {
        if (REQUEST_TAKE_PHOTO == false && bitmap == null && realPath == null) {
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Pamiętaj aby zatwierdzić zdobycie szczytu, najpierw musisz zrobić zdjęcie.")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Nie wyraziłeś jeszcze zgody na urzywanie GPS przez aplikację \"Górski Pamietnik\". Możesz to zmienić w ustawieniach. \nCzy chcesz zapisać zdobycie szczytu bez loaklizacji wykonania zdjęcia?")
                    .setCancelButton("NIE", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CONTINUE = 1;
                        }
                    })
                    .setConfirmButton("TAK", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CONTINUE = 0;
                        }
                    })
                    .show();
        }
        else if(location==null){
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops... Brak odczytu GPS")
                    .setContentText("Znajdujesz się poza zasięgiem lub Twój GPS jest wyłączony.\nCzy chcesz zapisać zdobycie szczytu bez loaklizacji wykonania zdjęcia?")
                    .setCancelButton("NIE", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CONTINUE = 1;
                            sweetAlertDialog.dismissWithAnimation();
                            return;
                        }
                    })
                    .setConfirmButton("TAK", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CONTINUE = 0;
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        Date date = Calendar.getInstance().getTime();
        String stringDate = (String) DateFormat.format("dd-MM-yyyy   HH:mm",date);
        if(CONTINUE == -1){
            if(date.getMonth()>=11 || date.getMonth()<=2){
                gd.addGain(new Gain(selectPeak.getId(),selectPeak.getTitle() + selectPeak.getHeight(),stringDate,"",(float)location.getLatitude(),(float)location.getLongitude(), realPath, " "," "," "," ",0));
            }
            else{
                gd.addGain(new Gain(selectPeak.getId(),selectPeak.getTitle() + selectPeak.getHeight(),"",stringDate,(float)location.getLatitude(),(float)location.getLongitude(), realPath," "," "," "," ",0));
            }
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("GRATULACJĘ !")
                    .setContentText("Cieszymy się razem z Tobą z kolejnego zdobytego szczytu. Brawo")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                            Intent intent = new Intent(PeakActivity.this, MountainDiaryActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
        else if(CONTINUE == 0){
            if(date.getMonth()>=11 || date.getMonth()<=2){
                gd.addGain(new Gain(selectPeak.getId(),selectPeak.getTitle() + selectPeak.getHeight(),stringDate,"",0f,0f, realPath, " "," "," "," ",0));
            }
            else{
                gd.addGain(new Gain(selectPeak.getId(),selectPeak.getTitle() + selectPeak.getHeight(),"",stringDate,0f,0f, realPath," "," "," "," ",0));
            }
            new SweetAlertDialog(PeakActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("GRATULACJĘ !")
                    .setContentText("Cieszymy się razem z Tobą z kolejnego zdobytego szczytu. Brawo")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                            Intent intent = new Intent(PeakActivity.this, MountainDiaryActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    }

    private File createImageFile() throws IOException {
        Date date = Calendar.getInstance().getTime();
        final String stringDate = (String) DateFormat.format("ddMMyyyy_HHmm_",date);
        String path = "IMG_" + stringDate  + (gd.getGainsList().size()+1) + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(path,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void createTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File image = null;
            try {
                image = createImageFile();
            } catch (IOException ex) {
                Log.e("E:","Error occurred while creating the File");
            }
            if (image != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.mountaindiary", image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }
    }
}
