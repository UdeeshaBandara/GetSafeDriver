package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import lk.hd192.getsafedriver.Encryption.Encrypt;
import lk.hd192.getsafedriver.Utils.GetTimeAgo;
import lk.hd192.getsafedriver.Utils.MsgPoJo;
import lk.hd192.getsafedriver.Utils.TinyDB;

public class Conversation extends AppCompatActivity {

    byte[] keyBytes;
    SwipeRefreshLayout mRefreshLayout;
    EditText txtMsgContent;
    String encryptedMsg = "";

    private ImageView btnSend;
    TextView txt_user_name;


    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private DatabaseReference mRootRef, messageRef;
    private RecyclerView mMessageList, chatMessageView;
    TinyDB tinyDB;
    private final List<MsgPoJo> messagesList = new ArrayList<>();

    String childId, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        tinyDB = new TinyDB(getApplicationContext());
        txt_user_name = findViewById(R.id.txt_user_name);
//        try {
//
//            encrypt.generateRSAKey();
//
//            tinyDB.putString("public_key", Base64.getEncoder().encodeToString(encrypt.getPublicKey().getEncoded()));
//            tinyDB.putString("private_key", Base64.getEncoder().encodeToString(encrypt.getPrivateKey().getEncoded()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        mRootRef = FirebaseDatabase.getInstance().getReference();
        try {

            if (tinyDB.getBoolean("isStaffDriver")) {

                txt_user_name.setText(getIntent().getStringExtra("name"));
                messageRef = mRootRef.child("Staff_Drivers").child(tinyDB.getString("driver_id")).child("Passengers").child(getIntent().getStringExtra("id")).child("messages");
            } else {

                txt_user_name.setText(getIntent().getStringExtra("name") + "'s Parent");
                messageRef = mRootRef.child("School_Drivers").child(tinyDB.getString("driver_id")).child("Passengers").child(getIntent().getStringExtra("id")).child(getIntent().getStringExtra("child_id")).child("messages");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        keyRef = mRootRef.child("Staff_Drivers").child("add_driver_id_here").child("Passengers").child("Add_user_id_here").child("key");
//        keyRef.setValue(tinyDB.getString("public_key"));

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
//        mChatUser = "a0tW1ZdZySMuDb28Za0RyoSDrlz1";
//        mCurrentUserId = mAuth.getCurrentUser().getUid();
//
//        mRootRef = FirebaseDatabase.getInstance().getReference();

        chatMessageView = findViewById(R.id.chat_message_view);
        mAdapter = new MessageAdapter(this, messagesList);
        mMessageList = findViewById(R.id.recycler_messages);
        mRefreshLayout = findViewById(R.id.message_swipe_layout);
        txtMsgContent = findViewById(R.id.txt_msg_content);
        btnSend = findViewById(R.id.btn_send);
        mLinearLayout = new LinearLayoutManager(this);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(mLinearLayout);
        mMessageList.setAdapter(mAdapter);


        loadMessages();


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                messagesList.clear();
                loadMessages();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                Log.e("msg send", "ok");
            }
        });

    }

    private void sendMessage() {
        String message = txtMsgContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {


//            keyBytes= keyRef.get().getResult().getValue().toString().getBytes();

            messageRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        try {
//                            keyBytes = Base64.getDecoder().decode(task.getResult().getValue().toString());
//                            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//                            KeyFactory kf = KeyFactory.getInstance("RSA");
//                            encryptedMsg = encrypt.encryptData(kf.generatePublic(spec), message);
                            Log.e("j", task.getResult().getValue() + "");


                            DatabaseReference user_message_push = messageRef.push();
                            String push_id = user_message_push.getKey();

                            java.util.Map messageMap = new HashMap();
                            messageMap.put("message",message);
                            messageMap.put("seen", false);
                            messageMap.put("from", "driver");
                            messageMap.put("time", ServerValue.TIMESTAMP);

                            txtMsgContent.setText("");
                            messageRef.child(push_id).updateChildren(messageMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Log.e("Error", databaseError.getMessage());
                                    }
                                }
                            });
                        } catch (Exception e) {
                            Log.e("jsds", e.getMessage());

                        }

                    }
                }
            });


        }
    }

//
//    PrivateKey generatePrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
//
//
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(tinyDB.getString("private_key")));
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//
//
//    }

    private void loadMessages() {

        //Reference location to user msgs

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        //Refresh recycle view with the newly sent msgs
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MsgPoJo messages = dataSnapshot.getValue(MsgPoJo.class);
                messagesList.add(messages);
                mAdapter.notifyDataSetChanged();
                mMessageList.scrollToPosition(messagesList.size() - 1);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

        private List<MsgPoJo> mMessageList;
        Context mContext;

        public MessageAdapter(Context context, List<MsgPoJo> mMessageList) {
            this.mMessageList = mMessageList;
            mContext = context;

        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_conversation, parent, false);
            return new MessageViewHolder(v);
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView txtMsgFrom, txtMsgTo, txtMsgFromTime, txtMsgToTime;
            public LinearLayout lnrTo, lnrFrom;


            public MessageViewHolder(View view) {
                super(view);
                txtMsgFrom = view.findViewById(R.id.txt_msg_from);
                txtMsgTo = view.findViewById(R.id.txt_msg_to);
                txtMsgFromTime = view.findViewById(R.id.txt_msg_from_time);
                txtMsgFromTime = view.findViewById(R.id.txt_msg_from_time);
                txtMsgToTime = view.findViewById(R.id.txt_msg_to_time);
                lnrFrom = view.findViewById(R.id.lnr_from);
                lnrTo = view.findViewById(R.id.lnr_to);
            }


        }


        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

            MsgPoJo c = mMessageList.get(position);


            String from_user = c.getFrom();

            String timeAgo = GetTimeAgo.getTimeAgo(c.getTime(), mContext);


            if (from_user.equals("driver")) {
                holder.lnrFrom.setVisibility(View.GONE);
                holder.lnrTo.setVisibility(View.VISIBLE);
//                holder.txtMsgTo.setText(c.getMessage());

//                try {
//                    holder.txtMsgTo.setText(Base64.getEncoder().encodeToString(encrypt.decryptData(generatePrivateKey(), c.getMessage())));
                holder.txtMsgTo.setText(c.getMessage());
//                } catch (GeneralSecurityException e) {
//                    e.printStackTrace();
//                }

                holder.txtMsgToTime.setText(timeAgo);

            } else {
                holder.lnrFrom.setVisibility(View.VISIBLE);
                holder.lnrTo.setVisibility(View.GONE);
//                try {
//                    holder.txtMsgFrom.setText(Base64.getEncoder().encodeToString(encrypt.decryptData(generatePrivateKey(), c.getMessage())));
//                } catch (GeneralSecurityException e) {
//                    e.printStackTrace();
//                }
                holder.txtMsgFrom.setText(c.getMessage());
                holder.txtMsgFromTime.setText(timeAgo);
            }


        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }
    }

}