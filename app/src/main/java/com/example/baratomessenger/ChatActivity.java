package com.example.baratomessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Button btnsend;
    private ListView chatlist;
    private ArrayAdapter adapter;
    private ArrayList messagelist;
    private String selecteduser;
    private long t=10800;
    private String currentUser , targetUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        selecteduser = getIntent().getStringExtra("selectedUser");
        setTitle(selecteduser+"");
        FancyToast.makeText(ChatActivity.this, "Chat with " + selecteduser, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        btnsend = findViewById(R.id.btnsend);

        chatlist = findViewById(R.id.chatlist);
        messagelist = new ArrayList();
        adapter = new ArrayAdapter(ChatActivity.this,android.R.layout.simple_list_item_1,messagelist);
        chatlist.setAdapter(adapter);

        try {

            ParseQuery<ParseObject> firstUserChatQuery = new ParseQuery<ParseObject>("chat");
            ParseQuery<ParseObject> secondUserChatQuery = new ParseQuery<ParseObject>("chat");

            firstUserChatQuery.whereEqualTo("sendBy", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("sendTo", selecteduser);

            secondUserChatQuery.whereEqualTo("sendBy", selecteduser);
            secondUserChatQuery.whereEqualTo("sendTo", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            final ParseQuery<ParseObject> myQueries = ParseQuery.or(allQueries);
            myQueries.orderByAscending("createdAt");
            myQueries.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String messages = chatObject.get("message") + "";
                            if (chatObject.get("sendBy").equals(ParseUser.getCurrentUser().getUsername())) {
                                messages = ParseUser.getCurrentUser().getUsername() + " : " + messages;
                            }
                            if (chatObject.get("sendBy").equals(selecteduser)) {
                                messages = selecteduser + " : " + messages;
                            }
                            messagelist.add(messages);
                        }
                        adapter.notifyDataSetChanged();
                        chatlist.smoothScrollToPosition(messagelist.size()-1);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edtmessage = findViewById(R.id.edtmessage);

                ParseObject chat = new ParseObject("chat");
                chat.put("sendTo", selecteduser);
                chat.put("sendBy", ParseUser.getCurrentUser().getUsername());
                chat.put("message", edtmessage.getText().toString());
                chat.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(ChatActivity.this, "Message sent", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            messagelist.add(ParseUser.getCurrentUser().getUsername() + " : " + edtmessage.getText().toString());
                            adapter.notifyDataSetChanged();
                            chatlist.smoothScrollToPosition(messagelist.size() - 1);
                            edtmessage.setText("");
                        } else {
                            FancyToast.makeText(ChatActivity.this, "Message couldn't sent", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemrefreshchat:
                try {

                    ParseQuery<ParseObject> firstUserChatQuery = new ParseQuery<ParseObject>("chat");
                    ParseQuery<ParseObject> secondUserChatQuery = new ParseQuery<ParseObject>("chat");

                    firstUserChatQuery.whereEqualTo("sendBy", ParseUser.getCurrentUser().getUsername());
                    firstUserChatQuery.whereEqualTo("sendTo", selecteduser);

                    secondUserChatQuery.whereEqualTo("sendBy", selecteduser);
                    secondUserChatQuery.whereEqualTo("sendTo", ParseUser.getCurrentUser().getUsername());

                    ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
                    allQueries.add(firstUserChatQuery);
                    allQueries.add(secondUserChatQuery);

                    final ParseQuery<ParseObject> myQueries = ParseQuery.or(allQueries);
                    myQueries.orderByAscending("createdAt");
                    myQueries.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {
                                for (ParseObject chatObject : objects) {
                                    String messages = chatObject.get("message") + "";
                                    if (chatObject.get("sendBy").equals(ParseUser.getCurrentUser().getUsername())) {
                                        messages = ParseUser.getCurrentUser().getUsername() + " : " + messages;
                                    }
                                    if (chatObject.get("sendBy").equals(selecteduser)) {
                                        messages = selecteduser + " : " + messages;
                                    }
                                    messagelist.add(messages);
                                }
                                adapter.notifyDataSetChanged();
                                chatlist.smoothScrollToPosition(messagelist.size() - 1);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messagelist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.mychatlistview,null);

            TextView txtcurrentUser = findViewById(R.id.user2);
            TextView txttargetUser = view.findViewById(R.id.user1);

            // txtcurrentUser.setText();
            // txttargetUser.setText();

            return view;
        }
    }
}