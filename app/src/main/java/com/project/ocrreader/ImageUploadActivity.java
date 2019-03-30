package com.project.ocrreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ImageUploadActivity extends AppCompatActivity {
    ImageView image;
    Button choose, upload;
    int PICK_IMAGE_REQUEST = 111;
    String URL = "http://192.168.1.101/url/DemoService/upload";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private Image imageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        image = (ImageView) findViewById(R.id.image);
        choose = (Button) findViewById(R.id.choose);
        upload = (Button) findViewById(R.id.upload);
        //opening image chooser option
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(12);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ImageUploadActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();
                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                //sending image to server
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        if (s.equals("true")) {
                            Toast.makeText(ImageUploadActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ImageUploadActivity.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ImageUploadActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        ;
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("image", imageString);
                        return parameters;
                    }
                };
                RequestQueue rQueue = Volley.newRequestQueue(ImageUploadActivity.this);
                rQueue.add(request);
            }
        });
    }


    private void showPicker(int code) {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Select ") // folder selection title
                .toolbarImageTitle("select") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .includeVideo(false)// Show video on image picker
                .single()
                .showCamera(true)
                .start(IpCons.RC_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        if (requestCode == IpCons.RC_IMAGE_PICKER) {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                try {
                    if (requestCode == IpCons.RC_IMAGE_PICKER) {
                        try {
                            imageResult = ImagePicker.getFirstImageOrNull(data);
                            System.out.println(imageResult.getPath());
                            File file=new File(imageResult.getPath());
                            Uri filePath=Uri.fromFile(file);
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                            //Setting image to ImageView
                            image.setImageBitmap(bitmap);
						/*	UCrop.Options options = new UCrop.Options();
							options.setCompressionQuality(50);
							UCrop.of(Uri.fromFile(new File(imageResult.getPath())), Uri.fromFile(new File(imageResult.getPath())))
									.withAspectRatio(5, 5)
									.withMaxResultSize(Utils.WIDTH, Utils.HEIGHT).withOptions(options)
									.start(EditContact.this, UCrop.REQUEST_CROP);
*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }


        }

    }

}
