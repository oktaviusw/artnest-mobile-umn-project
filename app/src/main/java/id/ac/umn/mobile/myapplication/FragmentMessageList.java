package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentMessageList extends Fragment {

    List<ModelChatInformation> chatData = new ArrayList<>();
    RecyclerView recyclerViewChat;
    RVAListChat adapterChatList;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_message,container,false);

        seedData();

        recyclerViewChat = myView.findViewById(R.id.RV_chat_list);
        adapterChatList = new RVAListChat(myView.getContext(),chatData);
        adapterChatList.notifyDataSetChanged();
        recyclerViewChat.setAdapter(adapterChatList);

        return myView;
    }


    public void seedData(){
        ModelChatInformation data1 = new ModelChatInformation("1", "Oktavius Wiguna", "2", "Anna Jenae", "text", "Hallo Anna");
        ModelChatInformation data2 = new ModelChatInformation("3", "Handy Goandy", "2", "Anna Jenae", "text", "Dichat Okta gak?");

        chatData.add(data1);
        chatData.add(data2);
    }
}
