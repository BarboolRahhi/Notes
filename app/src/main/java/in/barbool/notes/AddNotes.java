package in.barbool.notes;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNotes extends AppCompatActivity {
    DBManager dbManager;
    Button addNotesBtn;
    EditText mTitle,mAbout;
    TextView mDate;
    ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);

        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //getSupportActionBar().setTitle(currentDate);

        addNotesBtn = (Button) findViewById(R.id.Addbtn);
        mTitle = (EditText) findViewById(R.id.editTitle);
        mAbout = (EditText) findViewById(R.id.editAbout);
        mDate = (TextView) findViewById(R.id.date);
        mBackBtn = (ImageView) findViewById(R.id.backbtn);

        mDate.setText(currentDate);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title  = mTitle.getText().toString();
                String about = mAbout.getText().toString();
                if (!title.equals("") && !about.equals("")){

                    ContentValues values = new ContentValues();
                    values.put(DBManager.ColUserName,mTitle.getText().toString());
                    values.put(DBManager.ColPassWord,mAbout.getText().toString());
                    values.put(DBManager.ColDate,currentDate);
                    long id = dbManager.Insert(values);
                    if (id>0)
                        Toast.makeText(AddNotes.this, "New Note is added: "+id, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AddNotes.this, "Note cannot Added", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AddNotes.this, "Please fill the both field to add data", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
}
