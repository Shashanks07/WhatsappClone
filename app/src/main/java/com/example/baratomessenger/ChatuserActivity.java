package com.example.baratomessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatuserActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatuser);

        listview = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(ChatuserActivity.this,android.R.layout.simple_list_item_1,arrayList);

        try{
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseUser user : objects){
                            arrayList.add(user.getUsername());
                        }
                        listview.setAdapter(arrayAdapter);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ChatuserActivity.this,ChatActivity.class);
                intent.putExtra("selectedUser",arrayList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.itemlogout){
            ParseUser.getCurrentUser().logOut();
            FancyToast.makeText(ChatuserActivity.this,"Log Out Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            Intent intent = new Intent(ChatuserActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}