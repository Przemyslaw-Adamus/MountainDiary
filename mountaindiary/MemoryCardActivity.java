package com.example.mountaindiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MemoryCardActivity extends AppCompatActivity {

    private int CHOICE = 0;
    private Button buttonSave;
    private Button buttonFoto;
    private Button buttonGallery;
    private Button buttonClean;
    private ImageView[] imageViewTab = new ImageView[5];
    private Bitmap[] bitmap = new Bitmap[5];
    private String[] currentPhotoPath = new String[5];
    private EditText editTextDescription;
    private GlobalData gd;
    private int index;
    private Gain selectedGain = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_card);

        buttonSave = findViewById(R.id.buttonSave);
        buttonFoto= findViewById(R.id.buttonFoto);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonClean = findViewById(R.id.buttonClean);
        imageViewTab[0] = findViewById(R.id.imageView0);
        imageViewTab[1] = findViewById(R.id.imageView1);
        imageViewTab[2] = findViewById(R.id.imageView2);
        imageViewTab[3] = findViewById(R.id.imageView3);
        imageViewTab[4] = findViewById(R.id.imageView4);
        editTextDescription = findViewById(R.id.editTextDescription);

        initMemoryCard();

        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x1 = Float.toString(selectedGain.getCoordX());
                String y1 = Float.toString(selectedGain.getCoordY());
                String x2 = Float.toString(gd.getPeakId(selectedGain.getPeakId()).getxCord());
                String y2 = Float.toString(gd.getPeakId(selectedGain.getPeakId()).getyCord());
                Formatter formatter = new Formatter();
                formatter.format("https://www.google.com/maps/dir/%1$s,%2$s/%3$s,%4$s/data=!4m2!4m1!3e2",x1,y1,x2,y2);
                Uri uri = Uri.parse(formatter.toString());
                Intent intentWWW = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intentWWW);
            }
        });

        imageViewTab[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
                CHOICE = 0;
            }
        });

        imageViewTab[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
                CHOICE = 1;
            }
        });

        imageViewTab[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
                CHOICE = 2;
            }
        });

        imageViewTab[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
                CHOICE = 3;
            }
        });

        imageViewTab[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
                CHOICE = 4;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gd.updateGain(
                        selectedGain.getId(),
                        currentPhotoPath[0],
                        currentPhotoPath[1],
                        currentPhotoPath[2],
                        currentPhotoPath[3],
                        currentPhotoPath[4],
                        editTextDescription.getText().toString()
                );
                new SweetAlertDialog(MemoryCardActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Yeaa...")
                            .setContentText("Dane zostaly zaktualizowane pomyślnie.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                    Intent intent = new Intent(MemoryCardActivity.this,MountainDiaryActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
            }
        });

        buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextDescription.setText("");
            }
        });

    }

    private void createDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setNeutralButton(getString(R.string.returnn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final String CUSTOM_ADAPTER_TEXT = "text";
        final String CUSTOM_ADAPTER_IMAGE = "image";

        List<Map<String, Object>> dialogItemList = new ArrayList<Map<String, Object>>();

            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, "    Zrób aparatem");
            itemMap.put(CUSTOM_ADAPTER_IMAGE, R.drawable.camera);
            dialogItemList.add(itemMap);
            itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, "    Wybierz z galerii");
            itemMap.put(CUSTOM_ADAPTER_IMAGE, R.drawable.gallery);
            dialogItemList.add(itemMap);
            itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, "    Usuń ");
            itemMap.put(CUSTOM_ADAPTER_IMAGE, R.drawable.eraser);
            dialogItemList.add(itemMap);
            itemMap = new HashMap<String, Object>();
            itemMap.put(CUSTOM_ADAPTER_TEXT, "    Podgląd");
            itemMap.put(CUSTOM_ADAPTER_IMAGE, R.drawable.eye);
            dialogItemList.add(itemMap);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dialogItemList,
                R.layout.activity_alert_dialog_simple_adapter_row,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView,R.id.alertDialogItemTextView});

        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                switch (itemIndex){
                    case 0:
                        createTakePictureIntent();
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie"),1);
                        break;
                    case 2:
                        deleteFoto();
                        break;
                    case 3:
                        showFoto();
                        break;
                }
            }
        });

        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    private void showFoto() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_foto);
        ImageView imageViewFoto = dialog.findViewById(R.id.imageViewShow);
        imageViewFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewFoto.setImageBitmap(bitmap[CHOICE]);

        dialog.show();
    }

    private void deleteFoto() {
        imageViewTab[CHOICE].setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageViewTab[CHOICE].setImageResource(R.drawable.tap);
        currentPhotoPath[CHOICE] = "";
        gd.updateGain(
                selectedGain.getId(),
                currentPhotoPath[0],
                currentPhotoPath[1],
                currentPhotoPath[2],
                currentPhotoPath[3],
                currentPhotoPath[4],
                editTextDescription.getText().toString()
        );
    }

    private void initMemoryCard(){
        gd = new GlobalData();
        File[] file = new File[5];
        String[] path = new String[5];
        index = getIntent().getIntExtra("index",-1);
        if(index != -1) {
            editTextDescription.setText(gd.getGainsList().get(index).getMemory());
            buttonFoto.setEnabled(false);
            //buttonGallery.setEnabled(false);
            buttonFoto.setAlpha(0.33f);
            //buttonGallery.setAlpha(0.33f);

            selectedGain = gd.getGainsList().get(index);
            path[0] = selectedGain.getUri0();
            path[0].trim();
            currentPhotoPath[0] = selectedGain.getUri0();

            path[1] = selectedGain.getUri1();
            path[1].trim();
            currentPhotoPath[1] = selectedGain.getUri1();

            path[2] = selectedGain.getUri2();
            path[2].trim();
            currentPhotoPath[2] = selectedGain.getUri2();

            path[3] = selectedGain.getUri3();
            path[3].trim();
            currentPhotoPath[3] = selectedGain.getUri3();

            path[4] = selectedGain.getUri4();
            path[4].trim();
            currentPhotoPath[4] = selectedGain.getUri4();
        }

        for(int i=0; i<5;i++) {
            if(path[i]!=" "){
                path[i] = cleanPath(path[i]);
                try {
                    file[i] = new File(path[i]);
                    Log.e("URI:", Uri.fromFile(file[i]).toString());
                    bitmap[i] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file[i]));

                    ExifInterface ei = new ExifInterface(path[i]);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bitmap[i] = rotateImage(bitmap[i], 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bitmap[i] = rotateImage(bitmap[i], 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bitmap[i] = rotateImage(bitmap[i], 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            bitmap[i] = bitmap[i];
                    }
                    imageViewTab[i].setImageBitmap(bitmap[i]);
                    imageViewTab[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap rotateImage(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }

    private String cleanPath(String path) {
        int indexSub =-1;

        if (path != " ") {
            indexSub = path.indexOf("///");
            if (indexSub != -1) {
                path = path.substring(indexSub);
                Log.e("PATH",path);
                return path;
            }
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        String realPath = null;
        try {
            if(resultCode == RESULT_OK){
                switch (requestCode){
                    case 0:

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
                        currentPhotoPath[CHOICE] = realPath;
                        break;
                }
                gd.updateGain(
                        selectedGain.getId(),
                        currentPhotoPath[0],
                        currentPhotoPath[1],
                        currentPhotoPath[2],
                        currentPhotoPath[3],
                        currentPhotoPath[4],
                        editTextDescription.getText().toString()
                );
                initMemoryCard();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        Date date = Calendar.getInstance().getTime();
        final String stringDate = (String) DateFormat.format("ddMMyyyy_HHmm_",date);
        String path = "IMG_" + stringDate  + (gd.getGainsList().size()+1) + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(path,".jpg",storageDir);
        currentPhotoPath[CHOICE] = image.getAbsolutePath();
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

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(this, MountainDiaryActivity.class);
        startActivity(intent);
    }
}
