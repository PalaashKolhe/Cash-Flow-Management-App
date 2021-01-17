package com.example.htn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.content.pm.PackageManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import android.net.Uri;
import java.lang.String;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.MlKit;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

public class FirstFragment extends Fragment {
    // Variables
    Button btnTakePic;
    ImageView imgView;
    static final int CAPTURE_IMG_REQ = 1;
    // Code
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        btnTakePic = view.findViewById(R.id.promptPic);
        imgView = view.findViewById(R.id.imageView);

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();
            }
        });

        return view;
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takepicintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takepicintent, CAPTURE_IMG_REQ);
            // if (takepicintent.resolveActivity != null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Toast.makeText(getContext(),"This app wont work!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        imgView.setImageBitmap(imageBitmap);
        //TextView myTextView = imgView.findViewById(R.id.textView2);

        try {
            InputImage image = InputImage.fromBitmap(imageBitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient();
            Task<Text> result = recognizer.process(image);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Text resultText = result.getResult();
            String finalText = resultText.getText();
            List<List> strings_and_int = parseContent(finalText);
            System.out.println(strings_and_int);


        } catch (Exception e) {

        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    public List<List> parseContent (String unparsed) {
        String[] list_of_things = unparsed.split(" ");
        list_of_things = unparsed.split("\n");
        ArrayList<Double> to_return_ints = new ArrayList<Double>();
        ArrayList<String> to_return_strings = new ArrayList<String>();
        double foo;
        for (int i = 0; i < list_of_things.length; i++) {
            try {
                foo = Double.parseDouble(list_of_things[i]);
                // foo = Integer.parseInt(list_of_things[i]);
                to_return_ints.add(foo);
            } catch (NumberFormatException e) {
                to_return_strings.add(list_of_things[i]);
            }
        }
        // This is the object to return now
        List<List> everything = new ArrayList<List>();
        everything.add(to_return_strings);
        everything.add(to_return_ints);
        return everything;
    };
}

