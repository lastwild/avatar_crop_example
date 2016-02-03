package ru.altarix.emias.addavatar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.makeramen.roundedimageview.RoundedImageView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private RoundedImageView avatarImage;
    DialogUtils dialogUtils;
    private Uri photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avatarImage = (RoundedImageView) findViewById(R.id.avatar);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils = new DialogUtils(MainActivity.this);
                dialogUtils.createAlertDialog( getResources().getStringArray(R.array.addictional_avatar_items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setImage(which);
                    }
                });
            }
        });
    }

    private void setImage(int which) {
        switch (which) {
            case 0:
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_GALLERY);
                break;
            case 1:
                try {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT,
                            createImageFile()), REQUEST_CAMERA);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                performCrop(data.getData());
            } else if (requestCode == REQUEST_CAMERA) {
                performCrop(photoPath);
            } else if (requestCode == Crop.REQUEST_CROP) {
                avatarImage.setImageURI(Crop.getOutput(data));
            }
        }
    }

    private void performCrop(Uri imageUri) {
        try {
            Crop.of(imageUri,createImageFile()).asSquare().start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    private Uri createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        photoPath = Uri.parse("file:" + image.getAbsolutePath());
        return photoPath;
    }
}
