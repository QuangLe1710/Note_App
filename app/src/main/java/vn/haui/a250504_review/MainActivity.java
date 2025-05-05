package vn.haui.a250504_review;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvTitle, tvContent;
    ListView lvNote;
    ArrayList<Note> arrNote;
    ArrayAdapter arrayAdapterNotes;
    Intent intent;
    ActivityResultLauncher activityResultLauncher;
    MySQLiteDB objMySQLiteDB;
    public int currentPosition=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        arrNote = new ArrayList<Note>();
        //tao csdl
        objMySQLiteDB = new MySQLiteDB(this, "myNotes.db", null, 1);
        // tao bang
        String sql = "CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY ASC, title text, content text)";
        objMySQLiteDB.myExcecuteSQL(sql);


//        lstNote.add(new Note("Tìm hiểu hệ thống thực",
//                "21/04/2025-26/04/2025 Tìm hiểu, thu thập thông tin, tổ chức lưu trữ, phân tích đánh gia đối tượng có liên quan và các tính năng chính"));
//        lstNote.add(new Note("Phát triển ứng dụng",
//                "26/04/2025-17/05/2025-Hoàn thành ứng dụng và tài liệu báo cáo"));
        myMapping();

        //Nhan du lieu ActivityResultLauch tra ve
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == 999) {
                            intent = o.getData();
                            Note objNewNote = (Note) intent.getSerializableExtra("objNewNote");
                             //them du lieu vao bang
                            String sql = "INSERT INTO notes VALUES(null,'" +
                                    objNewNote.getTitle() + "','" +
                                    objNewNote.getContent() + "')";
                            objMySQLiteDB.myExcecuteSQL(sql);
                            // de xem du lieu sau cap nhat co 2 phuong an
                            //1. them vao dl moi vao Arraylist
//                            arrNote.add(objNewNote);
//                            arrayAdapterNotes.notifyDataSetChanged();

                            //2. doc lai tu co so dl
                            readMySQLiteDB(objMySQLiteDB);

                        }
                        if (o.getResultCode() == 888) {
                            intent = o.getData();
                            Note objNoteUpdate= (Note) intent.getSerializableExtra("objNoteUpdate");

                            //Cap nhat du lieu vao bang
                            String sql = "UPDATE notes SET " +
                                    "title = '" + objNoteUpdate.getTitle() + "'," +
                                    "content = '"+objNoteUpdate.getContent() + "'" +
                                    "WHERE id ="+objNoteUpdate.getId();
                            objMySQLiteDB.myExcecuteSQL(sql);
                            // de xem du lieu sau cap nhat co 2 phuong an
                            //1. them vao dl moi vao Arraylist
//                            arrNote.add(objNewNote);
//                            arrayAdapterNotes.notifyDataSetChanged();

                            //2. doc lai tu co so dl
                            readMySQLiteDB(objMySQLiteDB);

                        }
                    }
                });


        readMySQLiteDB(objMySQLiteDB);

        if (!arrNote.isEmpty()) {
            currentPosition=0;
            myDisplay(arrNote.get(currentPosition));
        }


        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition=position;
                myDisplay(arrNote.get(position));
            }
        });

        // Chuc nang xoa ban ghi trong db
        lvNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note objNote = arrNote.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chu y xoa ban ghi");
                builder.setMessage("Ban co chac chan khong?");
                // Add the buttons.
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User taps OK button.
                        deleteFromTable(objNote.getId());
                        readMySQLiteDB(objMySQLiteDB);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                        dialog.cancel();
                    }
                });
                // Set other dialog properties.

                // Create the AlertDialog.
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    private void deleteFromTable(int id) {
        String sql = "DELETE FROM  notes WHERE id=" + id;
        objMySQLiteDB.myExcecuteSQL(sql);
    }

    private void readMySQLiteDB(MySQLiteDB objMySQLiteDB) {
        //doc du lieu
        String sql = "SELECT * FROM notes";
        Cursor cursor = objMySQLiteDB.myGetSql(sql);
        arrNote.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            arrNote.add(new Note(id, title, content));
        }
        arrayAdapterNotes.notifyDataSetChanged();

    }

    // Function hiển thị thông tin Note
    private void myDisplay(Note obj) {

        tvTitle.setText(obj.toString());
        tvContent.setText(obj.getContent());
    }

    // Ánh xạ giao diện
    private void myMapping() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);

        lvNote = findViewById(R.id.lv_note);
        arrayAdapterNotes = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrNote);
        lvNote.setAdapter(arrayAdapterNotes);

    }

    // Tạo menu Option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // Event chọn Menu Item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_add_new) {
            intent = new Intent(MainActivity.this, AddNewNote.class);
            activityResultLauncher.launch(intent);
        }else{
            if(item.getItemId()==R.id.item_edit){
                intent=new Intent(MainActivity.this, EditNote.class);
                intent.putExtra("objNote",arrNote.get(currentPosition));
                activityResultLauncher.launch(intent);
            }else{
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}