package uk.ac.mmu.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * allows users to sign up with a new account
 */

public class SignUp extends AppCompatActivity {

    MaterialEditText editPhone, editName, editPassword, editSecureCode;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);

        editName = (MaterialEditText) findViewById(R.id.editName);
        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        editSecureCode = (MaterialEditText) findViewById(R.id.editSecureCode);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Firebase database connection
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {


                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (editName.getText().length() == 0 || editPhone.getText().length() == 0 || editPassword.getText().length() == 0) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
                            } else if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                            } else if (editPassword.getText().length() < 6) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Password length is too short", Toast.LENGTH_SHORT).show();
                            } else if (editPhone.getText().length() < 5) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone number length is too short", Toast.LENGTH_SHORT).show();
                            } else {

                                mDialog.dismiss();
                                User user = new User(editName.getText().toString(),
                                        editPassword.getText().toString(),
                                        editSecureCode.getText().toString());
                                table_user.child(editPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
