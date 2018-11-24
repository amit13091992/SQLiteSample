package android.practices.sqlitesample.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.practices.sqlitesample.DataBase.NotePadDB;
import android.practices.sqlitesample.R;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Amit on 16-Apr-18.
 */
public class DisplayNotes extends AppCompatActivity {
    Bundle extras;
    private EditText edt_notesTitle;
    private EditText edt_notesDetails;
    private int id_To_Update = 0;
    private Snackbar snackbar;
    private NotePadDB notePadDB;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        edt_notesTitle = (EditText) findViewById(R.id.idTextTitle);
        edt_notesDetails = (EditText) findViewById(R.id.idTextDescription);
        relativeLayout = (RelativeLayout) findViewById(R.id.rootlayout);
        notePadDB = new NotePadDB(this);
        //get saved data from notes-list.
        extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = notePadDB.getNotesData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String nameString = rs.getString(rs.getColumnIndex(notePadDB.name));
                String remarksString = rs.getString(rs.getColumnIndex(notePadDB.remark));
                if (!rs.isClosed()) {
                    rs.close();
                }
                edt_notesTitle.setText((CharSequence) nameString);
                edt_notesDetails.setText((CharSequence) remarksString);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            getMenuInflater().inflate(R.menu.display_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        String dateString = null;
        switch (item.getItemId()) {
            case R.id.DeleteNote:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.DeleteNote)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        notePadDB.deleteNotes(id_To_Update);
                                        if (edt_notesTitle.getText().toString().trim().equals("") &&
                                                edt_notesDetails.getText().toString().trim().equals("")) {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(DisplayNotes.this, "Deleted Note Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        }
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure to delete this note?");
                d.show();
                return true;
            case R.id.SaveNote:
                Bundle extras = getIntent().getExtras();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                dateString = formattedDate;
                if (extras != null) {
                    int Value = extras.getInt("id");
                    if (Value > 0) {
                        if (edt_notesDetails.getText().toString().trim().equals("")
                                || edt_notesTitle.getText().toString().trim().equals("")) {
                            snackbar = Snackbar.make(relativeLayout, "Please fill in name of the note", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            if (notePadDB.updateNotes(id_To_Update, edt_notesTitle.getText()
                                    .toString(), dateString, edt_notesDetails.getText()
                                    .toString())) {
                                snackbar = Snackbar.make(relativeLayout, "Your note Updated Successfully!!!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                snackbar = Snackbar.make(relativeLayout, "There's an error. That's all I can tell. Sorry!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    } else {
                        if (edt_notesDetails.getText().toString().trim().equals("")
                                || edt_notesTitle.getText().toString().trim().equals("")) {
                            snackbar = Snackbar
                                    .make(relativeLayout, "Please fill in name of the note", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            if (notePadDB.insertNotes(edt_notesTitle.getText().toString(), dateString,
                                    edt_notesDetails.getText().toString())) {
                                snackbar = Snackbar.make(relativeLayout, "Added Successfully.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                startActivity(new Intent(DisplayNotes.this, MainActivity.class));
                                finish();
                            } else {
                                snackbar = Snackbar.make(relativeLayout, "Unfortunately Task Failed.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        return;
    }


}
