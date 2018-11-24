package android.practices.sqlitesample.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.practices.sqlitesample.DataBase.NotePadDB;
import android.practices.sqlitesample.R;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Amit on 16-Apr-18.
 */

public class MainActivity extends AppCompatActivity {

    NotePadDB notePadDB;
    FloatingActionButton btnAddNote;
    RelativeLayout relativeLayout;
    SimpleCursorAdapter simpleCursorAdapter;
    private ListView notesListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.rootlayout);
        notePadDB = new NotePadDB(this);
        btnAddNote = (FloatingActionButton) findViewById(R.id.btnadd);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(), DisplayNotes.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });

        Cursor c = notePadDB.fetchAllNotes();
        String[] fieldNames = new String[]{NotePadDB.name, NotePadDB._id, NotePadDB.dates, NotePadDB.remark};
        int[] display = new int[]{R.id.idNoteName, R.id.idNoteID, R.id.idNoteDate, R.id.idNoteRemarks};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.note_item, c, fieldNames, display, 0);

        notesListview = (ListView) findViewById(R.id.listView1);
        notesListview.setAdapter(simpleCursorAdapter);
        notesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                LinearLayout linearLayoutParent = (LinearLayout) arg1;
                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(0);
                TextView m = (TextView) linearLayoutChild.getChildAt(1);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                Intent intent = new Intent(getApplicationContext(), DisplayNotes.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        this.menu = menu;
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.AddNote:
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("id", 0);
//                Intent intent = new Intent(getApplicationContext(), DisplayNotes.class);
//                intent.putExtras(dataBundle);
//                startActivity(intent);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
