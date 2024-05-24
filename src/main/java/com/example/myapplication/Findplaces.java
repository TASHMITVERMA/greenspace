package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DonateBook extends AppCompatActivity {

    // variable declaration
    EditText bookNameEt, authorNameEt;
    Spinner spinnerAutonomous, spinnerGenre;
    String email;
    Integer userid;
ImageView i7;

    // Database Helper
    DbHelper usersDbHelper = new DbHelper(this);
    DbHelper booksDbHelper = new DbHelper(this);
    DbHelper categoriesDbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_donation_form);

        // get email
        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("email");
        Toast.makeText(this, "Email: "+ email, Toast.LENGTH_SHORT).show();

        // initailizations
        spinnerAutonomous = findViewById(R.id.spinnerAutonomous);
        spinnerAutonomous = findViewById(R.id.spinnerAutonomous);
        bookNameEt = findViewById(R.id.editTextBookName);
        authorNameEt = findViewById(R.id.editTextAuthorName);
        i7=findViewById(R.id.imageView7);
        // Spinner - autonomous
        String[] values2 = {"No, I don't want to be", "Yes, please keep me autonomous"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAutonomous.setAdapter(adapter2);

        // Spinner - genre
        spinnerGenre = findViewById(R.id.spinnerGenre);
        String[] values = {"KMC Green", "MIT ground", "END Point", "VGT Park","Arbi Falls","REAL PEACOCK POINT","Baba's point","Manipal Lake"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        i7.setImageResource(R.drawable.kmc); // Replace R.drawable.kmc_green_image with the actual resource ID of the image for KMC Green
                        break;
                    case 1:
                        i7.setImageResource(R.drawable.mit); // Replace R.drawable.mit_ground_image with the actual resource ID of the image for MIT Ground
                        break;
                    case 2:
                        i7.setImageResource(R.drawable.end); // Replace R.drawable.mit_ground_image with the actual resource ID of the image for MIT Ground
                        break;
                    case 7:
                        i7.setImageResource(R.drawable.lake); // Replace R.drawable.mit_ground_image with the actual resource ID of the image for MIT Ground
                        break;
                    // Add cases for other positions as needed
                    default:
                        // Set a default image or do nothing
                        i7.setImageResource(R.drawable.end);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        // Button
        TextView donateBooks = findViewById(R.id.textView15);
        donateBooks.setOnClickListener(v -> {
            int creditValue;
            String bookname, author, genre, defect;
            bookname = bookNameEt.getText().toString();
            author = authorNameEt.getText().toString();
            genre = spinnerGenre.getSelectedItem().toString();
            byte[] image = new byte[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100}; //edit this
            defect = "None"; // edit this
            creditValue = 10;

            if (bookname.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Please enter all the details" + genre, Toast.LENGTH_LONG).show();
                return;
            }

            if (email == null || email.isEmpty()) {
                Toast.makeText(this, "Please enter your email" + email, Toast.LENGTH_LONG).show();
                return;
            }

            int genreId = categoriesDbHelper.getOrInsertGenreId(genre);

            long newRowIdBook = booksDbHelper.insertBook(bookname, author, genreId, creditValue, defect);
            if (newRowIdBook != -1) {
                Toast.makeText(this, "DATA added successfully", Toast.LENGTH_SHORT).show();
                userid = usersDbHelper.getUserIdByEmail(email);
                long newRowIdDonation = booksDbHelper.insertDonation(userid, 0, getCurrentDate(), "Pending");
                if (newRowIdDonation != -1) {
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DonateBook.this, HomePage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "not successful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "not added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to home button
        ImageView homeButton = findViewById(R.id.imageView14);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }

    public String getCurrentDate() {
        // Get current date and time
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        // Format the date according to the defined format
        return dateFormat.format(currentDate);
    }

}
