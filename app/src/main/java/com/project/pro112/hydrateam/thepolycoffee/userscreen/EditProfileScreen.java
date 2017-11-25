package com.project.pro112.hydrateam.thepolycoffee.userscreen;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.pro112.hydrateam.thepolycoffee.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileScreen extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    //Khai bao View:
    Toolbar toolbar;
    CircleImageView imgAvatar;
    TextView clickEditPhoto,
            fullnameProfile,
            textViewEmail,
            textViewBirthDay,
            textViewSdt,
            textViewGender,
            textViewTitle;
    EditText editTextFullNameProfile,
            editTextEmailProfile,
            editTextBirthDayProfile,
            editTextGender, editTextContactNumber;
    Button btnSave;

    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //FIREBASE DATABASE FIELDS
    DatabaseReference mUserDatabse;
    StorageReference mStorageRef;

    //IMAGE HOLD URI
    Uri imageHoldUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        toolbar.setTitle("");
        textViewTitle.setText("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Doi Font:
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans-Regular.ttf");
        clickEditPhoto.setTypeface(face);
        textViewSdt.setTypeface(face);
        textViewEmail.setTypeface(face);
        textViewBirthDay.setTypeface(face);
        fullnameProfile.setTypeface(face);
        textViewGender.setTypeface(face);
        btnSave.setTypeface(face);

        //Su kien click vao TextView Edit Photo:
        clickEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicSelection();
            }
        });

        //Su Kien onclick Button Save:
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });

        //Su Kien Thay Doi BirthDay Bang Datepicker Dialog:
        editTextBirthDayProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBirthDay();
            }
        });

        //Su Kien Nhan Nut Gender:
        editTextGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGender();
            }
        });
    }

    //Set Gender:
    private void setGender() {
        PopupMenu popupMenu = new PopupMenu(EditProfileScreen.this, editTextGender);
        getMenuInflater().inflate(R.menu.gender, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.male: {
                        editTextGender.setText("Male");
                        break;
                    }
                    case R.id.female: {
                        editTextGender.setText("Female");
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    //Set BirthDay:
    private void setBirthDay() {
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileScreen.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
                    editTextBirthDayProfile.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    //Code click vao TextView Edit Photo:
    private void profilePicSelection() {
        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileScreen.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    //Mo galary:
    private void galleryIntent() {
        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    //Mo Camera chup hinh:
    private void cameraIntent() {
        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //Code su kien nut saveProfile:
    private void saveUserProfile() {

        String fullName = editTextFullNameProfile.getText().toString().trim();
        String email = editTextEmailProfile.getText().toString().trim();
        String birthDay = editTextBirthDayProfile.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String contactNumber = editTextContactNumber.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            editTextFullNameProfile.requestFocus();
            Toast.makeText(this, "Full Name can not be empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(contactNumber)) {
            Toast.makeText(this, "Contact Number not be empty", Toast.LENGTH_SHORT).show();
            editTextContactNumber.requestFocus();
        } else {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }
    }

    // ActivityResultIntent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //SAVE URI FROM CAMERA
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                imgAvatar.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //Anh Xa View:
    private void initView() {
        //FIREBASE DATABASE INSTANCE
        mAuth = FirebaseAuth.getInstance();
        mUserDatabse = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //FindViewByID:
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        textViewTitle = (TextView) findViewById(R.id.tvTitleToolbar);

        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatarProfile);
        clickEditPhoto = (TextView) findViewById(R.id.clickEditPhoto);
        editTextFullNameProfile = (EditText) findViewById(R.id.editTextFullNameProfile);
        editTextFullNameProfile.requestFocus();
        editTextEmailProfile = (EditText) findViewById(R.id.editTextEmailProfile);
        editTextBirthDayProfile = (EditText) findViewById(R.id.editTextBirthDayProfile);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextContactNumber = (EditText) findViewById(R.id.editTextContactNumber);
        btnSave = (Button) findViewById(R.id.buttonSaveInfoProfile);
        editTextBirthDayProfile.setFocusable(false);
        editTextGender.setFocusable(false);

        textViewSdt = (TextView) findViewById(R.id.textViewSdt);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewBirthDay = (TextView) findViewById(R.id.textViewBirthDay);
        fullnameProfile = (TextView) findViewById(R.id.fullnameProfile);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
