package com.itservz.bookex.android;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itservz.bookex.android.model.ChatMessage;
import com.itservz.bookex.android.service.ChatMessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,
        ChatMessageService.MessagesCallbacks {

    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";

    private ArrayList<ChatMessage> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private ChatMessageService.MessagesListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(true);
        }

        mRecipient = "James Bond";

        mListView = (ListView) findViewById(R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        setTitle(mRecipient);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button sendMessage = (Button) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        String[] ids = {"James", " - ", "Bond"};
        Arrays.sort(ids);
        mConvoId = ids[0] + ids[1] + ids[2];

        mListener = ChatMessageService.addMessagesListener(mConvoId, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(BookDetailFragment.ARG_ITEM_ID));
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");
        ChatMessage msg = new ChatMessage();
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender("James Bond");

        ChatMessageService.saveMessage(msg, mConvoId);
    }

    @Override
    public void onMessageAdded(ChatMessage message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatMessageService.stop(mListener);
    }


    private class MessagesAdapter extends ArrayAdapter<ChatMessage> {
        MessagesAdapter(ArrayList<ChatMessage> messages) {
            super(ChatActivity.this, R.layout.chat_message, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            ChatMessage message = getItem(position);

            TextView nameView = (TextView) convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nameView.getLayoutParams();


            if ("James Bond".equals(message.getSender())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nameView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight, null));
                }
                layoutParams.gravity = Gravity.RIGHT;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nameView.setBackgroundColor(getResources().getColor(R.color.colorTextIcon, null));
                }
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);
            return convertView;
        }
    }
}
