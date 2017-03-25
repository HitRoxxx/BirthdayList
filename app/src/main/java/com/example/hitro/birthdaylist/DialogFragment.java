package com.example.hitro.birthdaylist;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {

    private View vv;
    private ImageView iv;
    private int PICK_IMAGE_REQUEST = 1;
    private EditText birthday, name, phone_no, email;
    Button cancel, add, calender;
    private Bitmap bitmap;
    private MyDatabase md;

    String n, p, e, b, noData = "";

    public DialogFragment() {
        // Required empty public constructor
    }

    static DialogFragment newInstance() {
        return new DialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vv = inflater.inflate(R.layout.fragment_dialog, container, false);
        iv = (ImageView) vv.findViewById(R.id.imageView3);
        birthday = (EditText) vv.findViewById(R.id.editText4);
        name = (EditText) vv.findViewById(R.id.editText);
        phone_no = (EditText) vv.findViewById(R.id.editText2);
        email = (EditText) vv.findViewById(R.id.editText3);

        hintMaker("Birthday - dd/mm/yyyy", birthday);
        hintMaker("Name", name);

        cancel = (Button) vv.findViewById(R.id.button2);
        add = (Button) vv.findViewById(R.id.button);
        calender = (Button) vv.findViewById(R.id.button3);
        byte[] image;
        md = new MyDatabase(getActivity());

        try {
            image = savedInstanceState.getByteArray("image");
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            //  bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            //  BitmapFactory.Options options = new BitmapFactory.Options();
            //  bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
            iv.setImageBitmap(bitmap);

        } catch (Exception e) {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_image);
            iv.setImageBitmap(bitmap);
        }


        return vv;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                // intent.setType("image/*");
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int yy = c.get(Calendar.YEAR);
                int dd = c.get(Calendar.DAY_OF_MONTH);
                int mm = c.get(Calendar.MONTH);
                DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday.setText("" + dayOfMonth + "/" + (++month) + "/" + year);
                    }
                }, yy, mm, dd);
                datepickerdialog.show();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = name.getText().toString();
                p = phone_no.getText().toString();
                e = email.getText().toString();
                b = birthday.getText().toString();

                if (!n.equals("")) {
                    if (!b.equals("")) {
                        if (p.equals("")) {
                            noData += "Phone no - null" + "\n";
                            if (e.equals("")) {
                                noData += "Email - null" + "\n";
                                createAlertbox(noData);

                            } else {
                                createAlertbox(noData);
                            }
                        } else {
                            if (e.equals("")) {
                                noData += "Email - null" + "\n";
                                createAlertbox(noData);

                            } else {
                                saveData(n, b, e, p,converterImageIntoByteArray(bitmap));
                            }
                        }
                    } else {
                        birthday.setError("Add Birthday");
                    }
                } else {
                    name.setError("Add Name");
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putByteArray("image", converterImageIntoByteArray(bitmap));
    }

    public byte[] converterImageIntoByteArray(Bitmap b) {
      /*  //b is the Bitmap

//calculate how many bytes our image consists of.
        int bytes = b.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] array = buffer.array(); //Get the underlying array containing the data.
        return array;*/

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public void hintMaker(String first, EditText tbTemp) {
        //---------------------------------------------------------------------------------------

        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(first);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tbTemp.setHint(builder);
    }

    private void saveData(String s1, String s2, String s3, String s4,byte[] i1) {

     //   Toast.makeText(getActivity(), "Data Saved" + s1 + s2 + s3 + s4, Toast.LENGTH_SHORT).show();
        md.insertData(s1,s2,s3,s4,i1);
        dismiss();
    }

    public void createAlertbox(String str) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Pardon");
        ab.setMessage("Some Data Not Filled \n"+str);
        ab.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(n, b, e, p,converterImageIntoByteArray(bitmap));
            }
        });
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (p.equals(""))
                {
                    phone_no.setError("");
                }
                if (e.equals(""))
                {
                    email.setError("");
                }
                noData ="";
            }
        });
        ab.setCancelable(false);//so that it don't fed when clicked outside alert dialog box
        ab.show();
    }
}
