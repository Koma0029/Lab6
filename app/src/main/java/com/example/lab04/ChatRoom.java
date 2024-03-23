package com.example.lab04;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04.databinding.ActivityChatRoomBinding;
import com.example.lab04.databinding.ReceivedMessageBinding;
import com.example.lab04.databinding.SentMessageBinding;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.ChatMessage;
import data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel ;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null) {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }

        binding.sendButton.setOnClickListener(clk->{
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(messageText,currentDateandTime,true);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size()-1);
            //clear the previous text
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(clk->{
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(messageText,currentDateandTime,false);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size()-1);
            //clear the previous text
            binding.textInput.setText("");
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceivedMessageBinding binding = ReceivedMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder myRowHolder, int position) {
                myRowHolder.messageText.setText("");
                myRowHolder.timeText.setText("");

                ChatMessage obj = messages.get(position);
                myRowHolder.messageText.setText(obj.getMessage());
                myRowHolder.timeText.setText(obj.getTimeSent());

            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                return (obj.getIsSentButton() ? 0 : 1);
            }
        });
    }
}

class MyRowHolder extends RecyclerView.ViewHolder {

    TextView messageText;
    TextView timeText;
    public MyRowHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.message);
        timeText= itemView.findViewById(R.id.time);
    }
}